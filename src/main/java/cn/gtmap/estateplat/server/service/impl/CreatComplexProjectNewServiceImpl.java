package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * 选择不动产单元批量发证（商品房批量发证）
 *
 * @author lst
 * @version V1.0, 15-12-24
 */
public class CreatComplexProjectNewServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    ProjectService projectService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;



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
            List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
            Map<String, InsertVo> bdcTdMap = new HashMap<String, InsertVo>();
            Map<String, InsertVo> bdcDjbMap = new HashMap<String, InsertVo>();
            project.setBdclx(Constants.BDCLX_TDFW);
            if((StringUtils.isNotBlank(project.getDcbIndex()) || CollectionUtils.isNotEmpty(project.getDcbIndexs()))&&CollectionUtils.isNotEmpty(project.getDjIds()) && CollectionUtils.isNotEmpty(project.getBdcdyhs())){// 混合选择
                // 先保存独幢创建需要的信息，然后置空，先进行户室的创建
                List<String> djsjIds = project.getDjIds();
                List<String> bdcdyhIds = project.getBdcdyhs();
                project.setDjIds(null);
                project.setBdcdyhs(null);

                initHsXxForProject(project,bdcXmRelList,bdcTdMap,bdcDjbMap,inertMap);
				// 置空户室创建项目的数据，再进行独幢的创建
                project.setDcbIndexs(null);
                project.setBdcdyhs(bdcdyhIds);
                project.setDjIds(djsjIds);

                initDzXxForProject(project,bdcXmRelList,bdcTdMap,bdcDjbMap,inertMap);
            }else {
                if (StringUtils.isNotBlank(project.getDcbIndex()) || CollectionUtils.isNotEmpty(project.getDcbIndexs())) {//选择逻辑幢的情况

                    initHsXxForProject(project,bdcXmRelList,bdcTdMap,bdcDjbMap,inertMap);

                } else if (CollectionUtils.isNotEmpty(project.getDjIds()) && CollectionUtils.isNotEmpty(project.getBdcdyhs())) { //多选不动产单元情况
                    initDzXxForProject(project,bdcXmRelList,bdcTdMap,bdcDjbMap,inertMap);
                }
            }


            //循环Map进行批量插入
            if(null != inertMap && inertMap.size() > 0) {
                Iterator it = inertMap.keySet().iterator();
                String key;
                List<InsertVo> list;
                while(it.hasNext()) {
                    key = it.next().toString();
                    list = inertMap.get(key);
                    if(CollectionUtils.isNotEmpty(list)) {
                        entityMapper.insertBatchSelective(list);
                        insertVoList.addAll(list);
                    }
                }
            }
        }
        return insertVoList;
    }


    public List<InsertVo> initScDjData(Project project, Map<String, InsertVo> bdcTdMap, Map<String, InsertVo> bdcDjbMap, List<BdcXmRel> bdcXmRelList, Map<String, List<InsertVo>> inertMap) {
        List<InsertVo> list;
        InitVoFromParm initVoFromParm = super.getDjxx(project);
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 盐城需求，获取土地证号显示在原证号中
         */
        if (StringUtils.isBlank(project.getYbdcqzh())) {
            String bdcdyh = StringUtils.left(project.getBdcdyh(), 19) + "W00000000";
            String bdcqzh = bdcZsService.getYtdbdcqzhByZdbdcdyh(bdcdyh);
            if (StringUtils.isBlank(bdcdyh))
                bdcqzh = bdcZsService.getYtdzhByZdbdcdyh(bdcdyh);
            if (StringUtils.isNotBlank(bdcqzh)) {
                project.setYbdcqzh(bdcqzh);
                bdcxm.setYbdcqzh(bdcqzh);
            }
        } else {
            bdcxm.setYbdcqzh(project.getYbdcqzh());
        }
        if(inertMap.containsKey(bdcxm.getClass().getSimpleName())) {
            list = inertMap.get(bdcxm.getClass().getSimpleName());
            list.add(bdcxm);
        } else {
            list = new ArrayList<InsertVo>();
            list.add(bdcxm);
        }
        inertMap.put(bdcxm.getClass().getSimpleName(), list);

        BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
        bdcXmRelList.add(bdcXmRel);
        if(inertMap.containsKey(bdcXmRel.getClass().getSimpleName())) {
            list = inertMap.get(bdcXmRel.getClass().getSimpleName());
            list.add(bdcXmRel);
        } else {
            list = new ArrayList<InsertVo>();
            list.add(bdcXmRel);
        }
        inertMap.put(bdcXmRel.getClass().getSimpleName(), list);

        BdcBdcdjb bdcdjb = null;
        if(StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            if(!bdcDjbMap.containsKey(project.getZdzhh())) {
                List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
                if(CollectionUtils.isEmpty(bdcdjbTemps)) {
                    bdcdjb = initBdcdjb(initVoFromParm);
                    if(inertMap.containsKey(bdcdjb.getClass().getSimpleName())) {
                        list = inertMap.get(bdcdjb.getClass().getSimpleName());
                        list.add(bdcdjb);
                    } else {
                        list = new ArrayList<InsertVo>();
                        list.add(bdcdjb);
                    }
                    inertMap.put(bdcdjb.getClass().getSimpleName(), list);
                    bdcDjbMap.put(bdcdjb.getZdzhh(), bdcdjb);
                } else {
                    bdcdjb = bdcdjbTemps.get(0);
                    bdcDjbMap.put(bdcdjb.getZdzhh(), bdcdjb);
                }
            } else {
                bdcdjb = (BdcBdcdjb) bdcDjbMap.get(project.getZdzhh());
            }
            // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
            if(!bdcTdMap.containsKey(project.getZdzhh())) {
                BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
                if(bdcTd == null) {
                    bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                    if(inertMap.containsKey(bdcTd.getClass().getSimpleName())) {
                        list = inertMap.get(bdcTd.getClass().getSimpleName());
                        list.add(bdcTd);
                    } else {
                        list = new ArrayList<InsertVo>();
                        list.add(bdcTd);
                    }
                    inertMap.put(bdcTd.getClass().getSimpleName(), list);
                    bdcTdMap.put(bdcTd.getZdzhh(), bdcTd);
                } else {
                    bdcTdMap.put(bdcTd.getZdzhh(), bdcTd);
                }
            }
        }

        BdcSpxx bdcSpxx = initBdcSpxx(initVoFromParm, null);
        if(bdcSpxx != null) {
            if(inertMap.containsKey(bdcSpxx.getClass().getSimpleName())) {
                list = inertMap.get(bdcSpxx.getClass().getSimpleName());
                list.add(bdcSpxx);
            } else {
                list = new ArrayList<InsertVo>();
                list.add(bdcSpxx);
            }
            inertMap.put(bdcSpxx.getClass().getSimpleName(), list);
        }

        if(StringUtils.isNotBlank(project.getBdcdyh())) {
            BdcBdcdy tempBdcdy = bdcdyService.queryBdcdyByBdcdyh(project.getBdcdyh());
            if(null == tempBdcdy) {
                BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
                if(bdcdy != null) {
                    bdcdy.setBdcdyid(UUIDGenerator.generate18());
                    bdcxm.setBdcdyid(bdcdy.getBdcdyid());
                    if(inertMap.containsKey(bdcdy.getClass().getSimpleName())) {
                        list = inertMap.get(bdcdy.getClass().getSimpleName());
                        list.add(bdcdy);
                    } else {
                        list = new ArrayList<InsertVo>();
                        list.add(bdcdy);
                    }
                    inertMap.put(bdcdy.getClass().getSimpleName(), list);
                }
            } else {
                bdcxm.setBdcdyid(tempBdcdy.getBdcdyid());
            }
        }

        //zx增加权利人
        List<BdcQlr> bdcQlrList = initBdcQlrFromDjsj(initVoFromParm.getProject(), initVoFromParm.getDjsjFwQlrList(), initVoFromParm.getDjsjLqxx(), initVoFromParm.getDjsjZdxxList(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getCbzdDcb(), Constants.QLRLX_QLR);
        if(CollectionUtils.isNotEmpty(bdcQlrList)) {
            List<BdcQlr> tempBdcQlrs = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if(CollectionUtils.isNotEmpty(tempBdcQlrs)) {
                for(BdcQlr qlr : tempBdcQlrs)
                    bdcQlrService.delBdcQlrByQlrid(qlr.getQlrid(), Constants.QLRLX_QLR);
            }
            BdcQlr bdcQlr = bdcQlrList.get(0);
            if(inertMap.containsKey(bdcQlr.getClass().getSimpleName())) {
                list = inertMap.get(bdcQlr.getClass().getSimpleName());
                list.addAll(bdcQlrList);
            } else {
                list = new ArrayList<InsertVo>();
                list.addAll(bdcQlrList);
            }
            inertMap.put(bdcQlr.getClass().getSimpleName(), list);
        }

        return list;
    }

    @Override
    protected BdcBdcdy initBdcdy(final InitVoFromParm initVoFromParm, final BdcBdcdjb bdcdjb) {
        BdcBdcdy bdcdy = null;
        //zdd 如果不动产单元号为空  则不生成
        Project project = initVoFromParm.getProject();
        if(project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            bdcdy = new BdcBdcdy();
            if(bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid()))
                bdcdy.setDjbid(bdcdjb.getDjbid());
            bdcdy = bdcdyService.getBdcdyFromProject(project, bdcdy);
            bdcdy = bdcdyService.getBdcdyFromFw(initVoFromParm.getDjsjFwxx(), bdcdy);
            if(StringUtils.isNotBlank(project.getYxmid())) {
                bdcdy = bdcdyService.getBdcdyFromYProject(project, bdcdy);
            }

        }
        return bdcdy;
    }

    @Override
    @Transactional
    public void saveOrUpdateProjectData(final List<InsertVo> dataList) {
    }

    /**
     * @description 户室信息创建项目
     */
    private void initHsXxForProject(Project project, List<BdcXmRel> bdcXmRelList, Map<String, InsertVo> bdcTdMap, Map<String, InsertVo> bdcDjbMap, Map<String, List<InsertVo>> inertMap){
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
                    String djId = djsjFwHs.getFwHsIndex();
                    String bdcdyh = djsjFwHs.getBdcdyh();
                    djsjIds.add(djId);
                    bdcdyhIds.add(bdcdyh);
                    project.setDjId(djId);
                    project.setBdcdyh(bdcdyh);
                    project.setProid(proid);
                    project.setZdzhh(djsjFwHs.getLszd());
                    initScDjData(project, bdcTdMap, bdcDjbMap, bdcXmRelList, inertMap);
                    proid = UUIDGenerator.generate18();
                }
            }
            project.setDjIds(djsjIds);
            project.setBdcdyhs(bdcdyhIds);
            project.setBdcXmRelList(bdcXmRelList);
        }
    }

    /**
     * @description 独幢信息创建项目
    */
    private void initDzXxForProject(Project project, List<BdcXmRel> bdcXmRelList, Map<String, InsertVo> bdcTdMap, Map<String, InsertVo> bdcDjbMap, Map<String, List<InsertVo>> inertMap){
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
                bdcdyh = bdcdyhIds.get(i);
                project.setDjId(djId);
                project.setBdcdyh(bdcdyh);
                project.setProid(proid);
                initScDjData(project, bdcTdMap, bdcDjbMap, bdcXmRelList, inertMap);
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
        project.setBdcXmRelList(bdcXmRelList);

    }
}
