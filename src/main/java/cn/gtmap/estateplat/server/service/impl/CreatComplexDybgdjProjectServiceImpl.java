package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSjdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lisongtao@gtmap.cn">lst</a>
 * @version 1.1.0, 2016/3/12.
 * @description 批量选择证书（多个不动产单元） 进行批量抵押变更登记
 */
public class CreatComplexDybgdjProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Resource(name = "creatProjectGzdjServiceImpl")
    CreatProjectService creatProjectGzdjServiceImpl;
    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;



    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project = null;
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();

        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }
        if (project != null && StringUtils.isNotBlank(project.getProid()) &&CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
            List<BdcXmRel> bdcXmRelList = project.getBdcXmRelList();
            String wiid = project.getWiid();
            String workflowProid = "";
            if (StringUtils.isNotBlank(project.getWorkflowProid())) {
                workflowProid = project.getWorkflowProid();
            } else if (StringUtils.isNotBlank(project.getProid())) {
                workflowProid = project.getProid();
            }

            creatProjectNode(workflowProid);
            String proid = project.getProid();
            //lst获取收件信息  防止删除后找不到
            if (StringUtils.isNotBlank(wiid)) {
                List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(wiid);
                if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                    insertVoList.addAll(bdcSjxxList);
                }
            }

            //zdd 将不动产单元编号前19位作为键   登记薄ID作为值
            HashMap<String,String> djbidMap = new HashMap<String, String>();
            if(StringUtils.equals(Constants.DJLX_PLDYBG_YBZS_SQLXDM,project.getSqlx())) {
                bdcXmRelList = getPldybgXmRelList(project,bdcXmRelList);
            }
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                //zdd 将后台传过来的项目关系信息  初始化到project对象中   便于后面创建单个项目
                if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                    project.setDjId(bdcXmRel.getQjid());
                }
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    project.setYxmid(bdcXmRel.getYproid());
                }
                if (StringUtils.isNotBlank(bdcXmRel.getYdjxmly())) {
                    project.setXmly(bdcXmRel.getYdjxmly());
                }
                if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                    project.setYqlid(bdcXmRel.getYqlid());
                }
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    project.setGdproid(bdcXmRel.getYproid());
                }
                //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                project.setProid(proid);

                //zdd 不动产单元号获取
                if (StringUtils.isNotBlank(project.getDjId())){
                    HashMap map = new HashMap();
                    if (StringUtils.isNotBlank(project.getBdclx())) {
                        map.put("bdclx", project.getBdclx());
                    }
                    map.put("id", project.getDjId());
                    List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyList)&&bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)!=null){
                        project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                    }
                }else if (StringUtils.isNotBlank(project.getYxmid())
                        &&StringUtils.isNotBlank(project.getXmly()) && project.getXmly().equals(Constants.XMLY_BDC)){
                    //否则来源不动产自身
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                    if (bdcBdcdy!=null){
                        project.setBdcdyh(bdcBdcdy.getBdcdyh());
                        djbidMap.put(bdcBdcdy.getBdcdyh().substring(0,19),bdcBdcdy.getDjbid());
                    }
                }
                //zdd 批量抵押  登记薄可能不一致
                if(StringUtils.isNotBlank(project.getBdcdyh())){
                    project.setZdzhh(project.getBdcdyh().substring(0,19));
                    if (djbidMap.containsKey(project.getBdcdyh().substring(0,19))) {
                        project.setDjbid(djbidMap.get(project.getBdcdyh().substring(0, 19)));
                    }else{
                        String djbid = UUIDGenerator.generate18();
                        djbidMap.put(project.getBdcdyh().substring(0,19),djbid);
                        project.setDjbid(djbid);
                    }
                }
                if(StringUtils.equals(Constants.DJLX_PLDYBG_YBZS_SQLXDM,project.getSqlx())) {
                    List<BdcXmRel> tempBdcXmRel = new ArrayList<BdcXmRel>();
                    tempBdcXmRel.add(bdcXmRel);
                    project.setBdcXmRelList(tempBdcXmRel);
                }
                List<InsertVo> list = null;
                list = creatProjectGzdjServiceImpl.initVoFromOldData(project);
                insertVoList.addAll(list);
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
        return insertVoList;
    }

    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param
    * @return
    * @description 根据单个项目的project的yxmid找到上一首业务同流程所有抵押权没注销权利的proid
    */
    private List<BdcXmRel> getPldybgXmRelList(Project project, List<BdcXmRel> bdcXmRelList) {
        List<BdcXmRel> bdcXmRelListAll = new ArrayList<BdcXmRel>();
        bdcXmRelListAll.addAll(bdcXmRelList);
        if(StringUtils.equals(Constants.XMLY_BDC,project.getXmly()) && 1==bdcXmRelList.size()) {
            List<String> yxmidList=bdcXmService.getXsDyxmProidsByproid(project.getYxmid());
            for (String yxmid : yxmidList) {
                if(!StringUtils.equals(yxmid,project.getYxmid())) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                    bdcXmRel.setYproid(yxmid);
                    bdcXmRelListAll.add(bdcXmRel);
                }
            }
        }
        return bdcXmRelListAll;
    }

    @Override
    @Transactional
    public void saveOrUpdateProjectData(final List<InsertVo> dataList) {
        //存储分组后的List数据
        List<List<InsertVo>> newList = new ArrayList<List<InsertVo>>();
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        List<String> zdzhhList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        List<String> bdcTdList = new ArrayList<String>();
        for (int i = 0; i < dataList.size(); i++) {
            InsertVo vo = dataList.get(i);
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                logger.error("CreatComplexDybgdjProjectServiceImpl.saveOrUpdateProjectData",e);
            }
            if (StringUtils.isNotBlank(id)&&entityMapper.selectByPrimaryKey(vo.getClass(), id) != null) {
                entityMapper.updateByPrimaryKeySelective(vo);
                continue;
            }
            //zdd 特殊处理BdcBdcdy  不动产单元号不能重复
            if (vo instanceof BdcBdcdy) {
                BdcBdcdy bdcdy = (BdcBdcdy) vo;
                if (bdcdyhList.contains(bdcdy.getBdcdyh())) {
                    continue;
                } else {
                    bdcdyhList.add(bdcdy.getBdcdyh());
                }

                //zdd 特殊处理BdcBdcdjb
            } else if (vo instanceof BdcBdcdjb) {
                BdcBdcdjb bdcdjb = (BdcBdcdjb) vo;
                if (zdzhhList.contains(bdcdjb.getZdzhh())) {
                    continue;
                } else {
                    zdzhhList.add(bdcdjb.getZdzhh());
                }
            } else if (vo instanceof BdcTd) {
                BdcTd bdcTd = (BdcTd) vo;
                if (bdcTdList.contains(bdcTd.getZdzhh())) {
                    continue;
                } else {
                    bdcTdList.add(bdcTd.getZdzhh());
                }
            }
            //如果没有值
            if (map.get(vo.getClass().getSimpleName()) == null) {
                voList = new ArrayList<InsertVo>();
                map.put(vo.getClass().getSimpleName(), voList);
                newList.add(voList);
            } else {
                voList = map.get(vo.getClass().getSimpleName());
            }
            voList.add(vo);
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            for (int j = 0; j < newList.size(); j++) {
                entityMapper.insertBatchSelective(newList.get(j));
            }
        }
    }
}
