package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.thread.BdcInitInfoThread;
import cn.gtmap.estateplat.server.thread.BdcThreadEngine;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/10/18
 * @description 采用多线程处理商品房首次登记
 */
public class CreatComplexProjectThreadServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    ProjectService projectService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcThreadEngine bdcThreadEngine;
    @Autowired
    private EntityMapper entityMapper;



    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project = null;
        List<InsertVo> sjxxinsertVoList = new ArrayList<InsertVo>();
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        Map<String, List<InsertVo>> inertMap = new HashMap<String, List<InsertVo>>();
        if(xmxx instanceof Project) {
            project = (Project) xmxx;
        }

        if(project != null && StringUtils.isNotBlank(project.getProid())) {
            UserInfo userInfo = SessionUtil.getCurrentUser();
            if(userInfo != null && StringUtils.isNotBlank(userInfo.getId())) {
                project.setUserId(userInfo.getId());
            }
            String wiid = project.getWiid();
            String workflowProid = "";
            if(StringUtils.isNotBlank(project.getWorkflowProid())) {
                workflowProid = project.getWorkflowProid();
            } else if(StringUtils.isNotBlank(project.getProid())) {
                workflowProid = project.getProid();
            }
            creatProjectNode(workflowProid);
            //lst获取收件信息  防止删除后找不到
            if(StringUtils.isNotBlank(wiid)) {
                List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(wiid);
                if(CollectionUtils.isNotEmpty(bdcSjxxList)) {
                    sjxxinsertVoList.addAll(bdcSjxxList);
                    inertMap.put(BdcSjxx.class.getSimpleName(), sjxxinsertVoList);
                }
            }
            List<BdcInitInfoThread> bdcInitInfoThreadList = new ArrayList<BdcInitInfoThread>();
            project.setBdclx(Constants.BDCLX_TDFW);
            if((StringUtils.isNotBlank(project.getDcbIndex()) || CollectionUtils.isNotEmpty(project.getDcbIndexs()))&&CollectionUtils.isNotEmpty(project.getDjIds()) && CollectionUtils.isNotEmpty(project.getBdcdyhs())){// 混合选择
                // 先保存独幢创建需要的信息，然后置空，先进行户室的创建
                List<String> djsjIds = project.getDjIds();
                List<String> bdcdyhIds = project.getBdcdyhs();
                project.setDjIds(null);
                project.setBdcdyhs(null);
                initHsXxForProject(project,bdcInitInfoThreadList);
                // 置空户室创建项目的数据，再进行独幢的创建
                project.setDcbIndexs(null);
                project.setBdcdyhs(bdcdyhIds);
                project.setDjIds(djsjIds);
                initDzXxForProject(project,bdcInitInfoThreadList);
            }else {
                if (StringUtils.isNotBlank(project.getDcbIndex()) || CollectionUtils.isNotEmpty(project.getDcbIndexs())) {//选择逻辑幢的情况
                    initHsXxForProject(project,bdcInitInfoThreadList);
                } else if (CollectionUtils.isNotEmpty(project.getDjIds()) && CollectionUtils.isNotEmpty(project.getBdcdyhs())) { //多选不动产单元情况
                    initDzXxForProject(project,bdcInitInfoThreadList);
                }
            }
            bdcThreadEngine.excuteThread(bdcInitInfoThreadList);
        }
        return insertVoList;
    }


    /**
     * @description 户室信息创建项目
     */
    private void initHsXxForProject(Project project, List<BdcInitInfoThread> bdcInsertVoThreadList){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("fw_dcb_index", project.getDcbIndex());
        if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
            map.put("fw_dcb_indexs", project.getDcbIndexs());
        }
        //读取配置判断附属设施是否参与项目生成
        String fsssGlCreatBdcXm = AppConfig.getProperty("fsss.create.bdcxm");
        List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(map);
        String proid = project.getProid();
        if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
            List<String> bdcdyhIds = new ArrayList<String>();
            List<String> djsjIds = new ArrayList<String>();
            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                if(!(StringUtils.isNotBlank(fsssGlCreatBdcXm)&&StringUtils.equals(fsssGlCreatBdcXm,"false")&&
                        djsjFwHs!=null&&StringUtils.isNotBlank(djsjFwHs.getIsfsss())&&StringUtils.equals(djsjFwHs.getIsfsss(),"1"))&&djsjFwHs!=null){
                    Project newProject = null;
                    try {
                        newProject = (Project) BeanUtils.cloneBean(project);
                    } catch (Exception e) {
                        logger.error("CreatComplexProjectThreadServiceImpl.initHsXxForProject",e);
                    }
                    String djId = djsjFwHs.getFwHsIndex();
                    String bdcdyh = djsjFwHs.getBdcdyh();
                    djsjIds.add(djId);
                    bdcdyhIds.add(bdcdyh);
                    if(newProject != null){
                        newProject.setDjId(djId);
                        newProject.setBdcdyh(bdcdyh);
                        newProject.setProid(proid);
                        newProject.setZdzhh(djsjFwHs.getLszd());
                        BdcInitInfoThread bdcInitInfoThread = new BdcInitInfoThread(newProject,entityMapper);
                        bdcInsertVoThreadList.add(bdcInitInfoThread);
                    }
                    proid = UUIDGenerator.generate18();
                }
            }
            project.setDjIds(djsjIds);
            project.setBdcdyhs(bdcdyhIds);
        }
    }

    /**
     * @description 独幢信息创建项目
     */
    private void initDzXxForProject(Project project, List<BdcInitInfoThread> bdcInsertVoThreadList){
        List<String> djsjIds = project.getDjIds();
        List<String> bdcdyhIds = project.getBdcdyhs();
        //读取配置判断附属设施是否参与项目生成
        String fsssGlCreatBdcXm = AppConfig.getProperty("fsss.create.bdcxm");
        if (bdcdyhIds.size() == djsjIds.size() && CollectionUtils.isNotEmpty(bdcdyhIds)) {
            if(bdcdyhIds.size() == 1) {
                String[] djIdArr = StringUtils.split(djsjIds.get(0), "\\$");
                String[] bdcdyhArr = StringUtils.split(bdcdyhIds.get(0), "\\$");
                djsjIds = Arrays.asList(djIdArr);
                bdcdyhIds = Arrays.asList(bdcdyhArr);
            }
            String djId;
            String bdcdyh;
            String proid = project.getProid();

            /**
             * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
             * @description 针对户室逻辑幢和独幢混选proid重复问题的解决逻辑
             */
            if(CollectionUtils.isNotEmpty(bdcInsertVoThreadList)){
                //户室逻辑幢也占用project.getProid()因此生成一个新的proid
                proid = UUIDGenerator.generate18();
            }

            for (int i = 0; i < djsjIds.size(); i++) {
                djId = djsjIds.get(i);
                HashMap<String, String> djidHashMap = new HashMap<String, String>();
                djidHashMap.put("fw_hs_index", djId);
                List<Map> fwhsHashMapList = djsjFwService.getDjsjFwHsByMap(djidHashMap);
                if(StringUtils.isNotBlank(fsssGlCreatBdcXm)&&StringUtils.equals(fsssGlCreatBdcXm,"false")
                        && CollectionUtils.isNotEmpty(fwhsHashMapList) && fwhsHashMapList.get(0) != null
                        && fwhsHashMapList.get(0).get("ISFSSS") != null && StringUtils.equals("1", fwhsHashMapList.get(0).get("ISFSSS").toString())){
                    continue;
                }
                Project newProject = null;
                try {
                    newProject = (Project) BeanUtils.cloneBean(project);
                } catch (Exception e) {
                    logger.error("CreatComplexProjectThreadServiceImpl.initHsXxForProject",e);
                }
                bdcdyh = bdcdyhIds.get(i);
                if(newProject != null){
                    newProject.setDjId(djId);
                    newProject.setBdcdyh(bdcdyh);
                    newProject.setProid(proid);
                    BdcInitInfoThread bdcInitInfoThread = new BdcInitInfoThread(newProject,entityMapper);
                    bdcInsertVoThreadList.add(bdcInitInfoThread);
                }
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
    }
}
