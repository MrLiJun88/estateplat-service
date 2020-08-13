package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQllxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.1.0, 2016/4/14.
 * @description 在抵押状态下转移，原抵押情况跟着一起转移
 */

public class CreatComplexZyWithDyProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Resource(name = "creatProjectZydjService")
    CreatProjectZydjServiceImpl creatProjectZydjService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    BdcQllxMapper bdcQllxMapper;
    @Autowired
    BdcSqlxDjyyRelService bdcSqlxDjyyRelService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcZsService bdcZsService;


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }else {
            throw new AppException(4005);
        }
        String workflowProid = "";
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }

        creatProjectNode(workflowProid);
        //lst根据工作流wiid  查找是否已经存在  如果存在 则删除对应的记录
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (StringUtils.isNotBlank(project.getWiid())) {
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }


        String sqlx1 = null;
        String sqlx2 = null;
        if (StringUtils.isNotBlank(project.getSqlx())) {
            String sqlxdm = "";
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(sqlxdm);
            if (hbSqlxMap != null) {
                sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
                sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
            }
        }
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 创建xm, xmrel, bdcdy, spxx, djb, qlr信息
         */
        if (project.getXmly().equals(Constants.XMLY_BDC)) {   //从新建选择不动产权证
            List<InsertVo> fwsyqVoList = initSyqFromBdc(project, sqlx1);
            super.insertProjectData(fwsyqVoList);
            String bdcdyid = "";
            if (CollectionUtils.isNotEmpty(fwsyqVoList)) {
                for (InsertVo insertVo : fwsyqVoList) {
                    if (insertVo instanceof BdcBdcdy) {
                        bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                    }
                }
            }
            //创建抵押流程
            project.setProid(UUIDGenerator.generate());
            project.setBdcdyid(bdcdyid);

            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("qszt", Constants.QLLX_QSZT_XS);
            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                for (BdcDyaq bdcDyaq : bdcDyaqList) {
                    project.setYxmid(bdcDyaq.getProid());
                    project.setXmly(Constants.XMLY_BDC);
                    insertVoList.addAll(initDyFromBdc(project, sqlx2));
                }
            }
        } else { //从匹配选择过渡证

            String pplx = AppConfig.getProperty("sjpp.type");
            List<InsertVo> dyList = new ArrayList<InsertVo>();
            if (StringUtils.isNotBlank(pplx) && pplx.equals(Constants.PPLX_CG)) {
                //过程
                String ydyGdproid = "";
                String syqQlid = "";
                String dyQlid = "";
                List<GdDy> gdDyList = null;
                List<BdcDyaq> bdcDyList = null;
                if (StringUtils.isNotBlank(project.getGdproid())) {
                    List<GdFwsyq> fwsyqList = gdFwService.getGdFwsyqListByGdproid(project.getGdproid(), 0);
                    if (CollectionUtils.isNotEmpty(fwsyqList))
                        syqQlid = fwsyqList.get(0).getQlid();
                    if (StringUtils.isNotBlank(syqQlid)) {
                        List<GdFw> gdFwList = gdFwService.getGdFwByQlid(syqQlid);
                        if (CollectionUtils.isNotEmpty(gdFwList)) {
                            String fwid = gdFwList.get(0).getFwid();
                            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(fwid, null);
                            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                    GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRel.getQlid(), 0);
                                    if (gdDy != null) {
                                        dyQlid = gdDy.getDyid();
                                        ydyGdproid = gdDy.getProid();
                                    }
                                }
                            }
                        }
                    }
                }
                project.setYqlid(syqQlid);
                project.setDjlx(Constants.DJLX_ZYDJ_DM);
                List<InsertVo> fwsyqVoList = initSyqFromGd(project, sqlx1);
                //zx只能先保存
                super.insertProjectData(fwsyqVoList);

                String bdcdyid = "";
                if (CollectionUtils.isNotEmpty(fwsyqVoList)) {
                    for (InsertVo insertVo : fwsyqVoList) {
                        if (insertVo instanceof BdcBdcdy) {
                            bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                        }
                    }
                }

                gdDyList = gdFwService.getGdDyListByGdproid(ydyGdproid, 0);
                if (CollectionUtils.isNotEmpty(gdDyList)) {
                    for (int i = 0; i < gdDyList.size(); i++) {
                        project.setYqlid(dyQlid);
                        project.setGdproid(ydyGdproid);
                        project.setBdcdyid(bdcdyid);
                        dyList = initDyFromGd(project, gdDyList, sqlx2);
                    }
                }
                HashMap map = new HashMap();
                map.put(ParamsConstants.BDCDYID_LOWERCASE,bdcdyid);
                map.put("qszt","1");
                bdcDyList = bdcDyaqService.queryBdcDyaq(map);
                if (CollectionUtils.isNotEmpty(bdcDyList)){
                    for (int i = 0; i < bdcDyList.size(); i++){
                        project.setYxmid(bdcDyList.get(i).getProid());
                        project.setBdcdyid(bdcdyid);
                        dyList.addAll(initDyFromBdc(project, sqlx2));
                    }
                }
            } else {
                String ydyGdproid = "";
                String syqQlid = "";
                String dyQlid = "";
                if (StringUtils.isNotBlank(project.getGdproid())) {
                    List<GdFwsyq> fwsyqList = gdFwService.getGdFwsyqListByGdproid(project.getGdproid(), 0);
                    if (CollectionUtils.isNotEmpty(fwsyqList))
                        syqQlid = fwsyqList.get(0).getQlid();
                    if (StringUtils.isNotBlank(syqQlid)) {
                        List<GdFw> gdFwList = gdFwService.getGdFwByQlid(syqQlid);
                        if (CollectionUtils.isNotEmpty(gdFwList)) {
                            String fwid = gdFwList.get(0).getFwid();
                            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(fwid, null);
                            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                    GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRel.getQlid(), 0);
                                    if (gdDy != null) {
                                        dyQlid = gdDy.getDyid();
                                        ydyGdproid = gdDy.getProid();
                                    }
                                }
                            }
                        }
                    }
                }
                project.setYqlid(syqQlid);

                project.setDjlx(Constants.DJLX_ZYDJ_DM);
                List<InsertVo> fwsyqVoList = initSyqFromGdCg(project, sqlx1);
                //zx只能先保存
                super.insertProjectData(fwsyqVoList);

                String bdcdyid = "";
                if (CollectionUtils.isNotEmpty(fwsyqVoList)) {
                    for (InsertVo insertVo : fwsyqVoList) {
                        if (insertVo instanceof BdcBdcdy)
                            bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                    }
                }
                HashMap map = new HashMap();
                map.put(ParamsConstants.BDCDYID_LOWERCASE,bdcdyid);
                List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                for (int i = 0; i < bdcDyaqList.size();i++){
                    project.setProid(UUIDGenerator.generate());
                    project.setYqlid(dyQlid);
                    project.setGdproid(ydyGdproid);
                    project.setBdcdyid(bdcdyid);
                    dyList = initDyFromGd(project, sqlx2,bdcDyaqList.get(i).getProid(),bdcDyaqList.get(i).getQlid());
                }
            }
            insertVoList.addAll(dyList);
        }
        //赋值登记事由和登记原因
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            for (InsertVo vo : insertVoList) {
                if (vo instanceof BdcXm) {
                    ((BdcXm) vo).setDjsy(CommonUtil.formatEmptyValue(bdcQllxMapper.queryDjsyByQllx(((BdcXm) vo).getQllx()).get(0)));
                    ((BdcXm) vo).setDjyy(bdcSqlxDjyyRelService.getDjyyBySqlx(((BdcXm) vo).getSqlx()));
                }
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从过渡库创建转移登记
     */
    public List<InsertVo> initSyqFromGd(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            //房屋所有权证办理不动产权证走转移
            List<InsertVo> list = creatProjectZydjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                String qllxdm = project.getQllx();
                //修改申请类型为买卖转移登记
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    if (StringUtils.isNotBlank(sqlx1))
                        ((BdcXm) vo).setSqlx(sqlx1);
                    else {
                        if (project.getBdclx().equals(Constants.BDCLX_TDFW)) {
                            if (qllxdm.equals(Constants.QLLX_GYTD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("225");
                            } else if (qllxdm.equals(Constants.QLLX_ZJD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("229");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDFWSUQ)) {
                                ((BdcXm) vo).setSqlx("213");
                            }
                        } else if (project.getBdclx().equals(Constants.BDCLX_TD)) {
                            if (qllxdm.equals(Constants.QLLX_JTTD_SUQ)) {
                                ((BdcXm) vo).setSqlx("214");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("217");
                            } else if (qllxdm.equals(Constants.QLLX_GYTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("215");
                            }
                        }
                    }
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从过渡库继承抵押信息
     */
    public List<InsertVo> initDyFromGd(Project project, List<GdDy> gdDyList, String sqlx2) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (CollectionUtils.isNotEmpty(gdDyList)) {
            for (int i = 0; i < gdDyList.size(); i++) {
                Project dyProject = new Project();
                try {
                    BeanUtils.copyProperties(dyProject, project);
                    dyProject.setYqlid(gdDyList.get(i).getDyid());
                    dyProject.setProid(UUIDGenerator.generate18());
                    dyProject.setGdproid(gdDyList.get(i).getProid());
                    //修改权利类型
                    dyProject.setQllx(Constants.QLLX_DYAQ);
                    dyProject.setDjsy(Constants.DJSY_DYAQ);
                    dyProject.setDjlx(Constants.DJLX_DYDJ_DM);
                    List<InsertVo> dylist = creatProjectDyBgdjService.initVoFromOldData(dyProject);

                    for (InsertVo vo : dylist) {
                        //抵押的义务人应该是转移的权利人，所以不需要义务人
                        if (vo instanceof BdcQlr) {
                            if (((BdcQlr) vo).getQlrlx().equals(Constants.QLRLX_YWR))
                                continue;
                        } else if (vo instanceof BdcXm) {
                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                            if (bdcSjxx != null) {
                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                insertVoList.add(bdcSjxx);
                            }
                            if (StringUtils.isNotBlank(sqlx2)) {
                                ((BdcXm) vo).setSqlx(sqlx2);
                            } else {
                                if (project.getBdclx().equals(Constants.BDCLX_TDFW)) {
                                    ((BdcXm) vo).setSqlx("1012");
                                } else if (project.getBdclx().equals(Constants.BDCLX_TD)) {
                                    ((BdcXm) vo).setSqlx("1005");
                                }
                            }
                        }
                        insertVoList.add(vo);
                    }

                } catch (Exception e) {
                    logger.error("CreatComplexZyWithDyProjectServiceImpl.initDyFromGd",e);
                }
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从不动产登记库生成抵押情况，如果不存在抵押则做抵押首次
     */
    public List<InsertVo> initDyFromBdc(Project project,String sqlx2) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //如果正式库不存在抵押，则创建一个新的抵押权利，不管是新建还是过渡过来的项目，相当于直接拿着所有权证创建抵押
        //只需要修改proid,djlx,sqlx,qllx
        Project dyProject = new Project();
        try {
            BeanUtils.copyProperties(dyProject,project);

            dyProject.setProid(UUIDGenerator.generate18());
            dyProject.setDjlx(Constants.DJLX_DYDJ_DM);
            if(StringUtils.isNotBlank(sqlx2)){
                dyProject.setSqlx(sqlx2);
            }else {
                if (StringUtils.equals(dyProject.getBdclx(), Constants.BDCLX_TDFW))
                    dyProject.setSqlx(Constants.SQLX_FWDY_DM);
                else if (StringUtils.equals(dyProject.getBdclx(), Constants.BDCLX_TD))
                    dyProject.setSqlx(Constants.SQLX_TDDY_DM);
            }
            dyProject.setQllx(Constants.QLLX_DYAQ);
            List<InsertVo> dylist = creatProjectDyBgdjService.initVoFromOldData(dyProject);
            //新生成的抵押没有权利人和义务人
            for (InsertVo vo : dylist) {
                if (vo instanceof BdcQlr) {
                    //抵押变更带入抵押权人,不需要义务人
                    BdcQlr bdcqlr = (BdcQlr) vo;
                    if(StringUtils.equals(bdcqlr.getQlrlx(),Constants.QLRLX_YWR)){
                        continue;
                    }
                } else if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        insertVoList.add(bdcSjxx);
                    }
                }
                insertVoList.add(vo);
            }
        } catch (Exception e) {
            logger.error("CreatComplexZyWithDyProjectServiceImpl.initDyFromBdc",e);
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从不动产登记库继承转移信息
     */
    public List<InsertVo> initSyqFromBdc(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //使用不动产单元号确定权利类型
        String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
        project.setQllx(qllxdm);
        //修改转移项目的登记类型为200
        project.setDjlx(Constants.DJLX_ZYDJ_DM);
        //房屋所有权证办理不动产权证走转移
        List<InsertVo> list = creatProjectZydjService.initVoFromOldData(project);
        for (InsertVo vo : list) {
            if (vo instanceof BdcXm) {
                BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                if (bdcSjxx != null) {
                    bdcSjxx.setSjxxid(UUIDGenerator.generate());
                    insertVoList.add(bdcSjxx);
                }
                if (StringUtils.isNotBlank(sqlx1)) {
                    ((BdcXm) vo).setSqlx(sqlx1);
                } else {
                    if (project.getBdclx().equals(Constants.BDCLX_TDFW)) {
                        if (qllxdm.equals(Constants.QLLX_GYTD_FWSUQ)) {
                            ((BdcXm) vo).setSqlx("225");
                        } else if (qllxdm.equals(Constants.QLLX_ZJD_FWSUQ)) {
                            ((BdcXm) vo).setSqlx("229");
                        } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDFWSUQ)) {
                            ((BdcXm) vo).setSqlx("213");
                        }
                    } else if (project.getBdclx().equals(Constants.BDCLX_TD)) {
                        if (qllxdm.equals(Constants.QLLX_JTTD_SUQ)) {
                            ((BdcXm) vo).setSqlx("214");
                        } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDSYQ)) {
                            ((BdcXm) vo).setSqlx("217");
                        } else if (qllxdm.equals(Constants.QLLX_GYTD_JSYDSYQ)) {
                            ((BdcXm) vo).setSqlx("215");
                        }
                    }
                }
            }
        }
        insertVoList.addAll(list);
        return insertVoList;
    }

    @Override
    @Transactional
    public void saveOrUpdateProjectData(final List<InsertVo> list) {
        //存储分组后的List数据
        List<List<InsertVo>> newList = new ArrayList<List<InsertVo>>();
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        List<String> zdzhhList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        List<String> bdcTdList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            InsertVo vo = list.get(i);
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                logger.error("CreatComplexZyWithDyProjectServiceImpl.saveOrUpdateProjectData",e);
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

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从过渡库创建变更登记
     */
    public List<InsertVo> initSyqFromGdCg(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            //修改转移项目的登记类型为200
            project.setDjlx(Constants.DJLX_ZYDJ_DM);
            //房屋所有权证办理不动产权证走转移
            List<InsertVo> list = creatProjectZydjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                String qllxdm = project.getQllx();
                //修改申请类型为买卖转移登记
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    if (StringUtils.isNotBlank(sqlx1)) {
                        ((BdcXm) vo).setSqlx(sqlx1);
                    } else {
                        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                            if (qllxdm.equals(Constants.QLLX_GYTD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("225");
                            } else if (qllxdm.equals(Constants.QLLX_ZJD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("229");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDFWSUQ)) {
                                ((BdcXm) vo).setSqlx("213");
                            }
                        } else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                            if (qllxdm.equals(Constants.QLLX_JTTD_SUQ)) {
                                ((BdcXm) vo).setSqlx("214");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("217");
                            } else if (qllxdm.equals(Constants.QLLX_GYTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("215");
                            }
                        }
                    }
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 在过程时，从过渡库生成抵押情况
     */
    public List<InsertVo> initDyFromGd(Project project, String sqlx2,String proid,String qlid) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //需要考虑过程数据和成果数据的权利人问题
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            project.setDjlx(Constants.DJLX_DYDJ_DM);
            if (StringUtils.isNotBlank(sqlx2)) {
                project.setSqlx(sqlx2);
            } else {
                if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW))
                    project.setSqlx(Constants.SQLX_FWDY_DM);
                else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD))
                    project.setSqlx(Constants.SQLX_TDDY_DM);
            }
            project.setQllx(Constants.QLLX_DYAQ);
            project.setDjsy(Constants.DJSY_DYAQ);
            if(StringUtils.isNotBlank(project.getBdcdyid())){
                HashMap map = new HashMap();
                map.put(ParamsConstants.BDCDYID_LOWERCASE,project.getBdcdyid());
                List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                if (bdcDyaqList != null){
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcDyaqList.get(0).getProid());
                    project.setYqlid(bdcXmRelList.get(0).getYqlid());
                    project.setGdproid(bdcXmRelList.get(0).getYproid());
                }
            }
            List<InsertVo> list = creatProjectDyBgdjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                //修改申请类型为买卖转移登记
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    if(StringUtils.isNotBlank(proid)){
                        HashMap map = new HashMap();
                        map.put("proid",proid);
                        List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                        if(CollectionUtils.isNotEmpty(bdcDyaqList) && StringUtils.isNotBlank(bdcDyaqList.get(0).getProid())){
                            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcDyaqList.get(0).getProid());
                            if(CollectionUtils.isNotEmpty(bdcZsList) && StringUtils.isNotBlank(bdcZsList.get(0).getBdcqzh())){
                                ((BdcXm) vo).setYbdcqzh(bdcZsList.get(0).getBdcqzh());
                            }
                        }
                    }
                    insertVoList.add(vo);
                } else if(vo instanceof BdcXmRel){
                    ((BdcXmRel) vo).setYproid(proid);
                    ((BdcXmRel) vo).setYqlid(qlid);
                    insertVoList.add(vo);

                }else if (vo instanceof BdcQlr) {
                    if (StringUtils.equals(((BdcQlr) vo).getQlrlx(), Constants.QLRLX_QLR)){
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                        if(CollectionUtils.isNotEmpty(bdcQlrList)) {
                            BdcQlr bdcQlr = new BdcQlr();
                            bdcQlr.setQlrid(UUIDGenerator.generate());
                            bdcQlr.setProid(project.getProid());
                            bdcQlr.setQlrmc(bdcQlrList.get(0).getQlrmc());
                            bdcQlr.setQlrsfzjzl(bdcQlrList.get(0).getQlrsfzjzl());
                            bdcQlr.setQlrzjh(bdcQlrList.get(0).getQlrzjh());
                            bdcQlr.setQlrlx(bdcQlrList.get(0).getQlrlx());
                            bdcQlr.setQlrlxdh(bdcQlrList.get(0).getQlrlxdh());
                            insertVoList.add(bdcQlr);
                        }
                    }
                } else
                    insertVoList.add(vo);
            }
        }
        return insertVoList;
    }
}