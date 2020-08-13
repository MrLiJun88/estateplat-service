package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQllxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @version 1.0, 2016/12/14
 * @author<a href = "mailto；liuxing@gtmap.cn">lx</a>
 * @description 房屋带抵押换证登记
 */
public class CreatComplexHzWithDyProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    EntityMapper entityMapper;
    @Resource(name = "creatProjectGzdjServiceImpl")
    private CreatProjectGzdjServiceImpl creatProjectGzdjService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    QllxParentService qllxParentService;
    @Autowired
    BdcQllxMapper bdcQllxMapper;
    @Autowired
    BdcSqlxDjyyRelService bdcSqlxDjyyRelService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdDyService gdDyService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        if (xmxx instanceof Project)
            project = (Project) xmxx;
        else
            throw new AppException(4005);
        String workflowProid = "";
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }

        creatProjectNode(workflowProid);

        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //根据工作流ID-wiid查找相应的记录，若有，则删除
        if (StringUtils.isNotBlank(project.getWiid())) {
            //删除收件信息记录
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }
        //获取组合登记两个不同申请类型
        String sqlxdm = "";
        if (StringUtils.isNotBlank(project.getSqlx())&&StringUtils.isNotBlank(project.getWiid())) {
            //获取平台的申请类型代码,主要为了合并
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        //分别从过渡和不动产创建xm,xmrel,bdcdy,djb,qlr,spxx信息
        if (project.getXmly().equals(Constants.XMLY_BDC)) {
            List<InsertVo> fdcqVoList = initFdcqFromBdc(project, sqlxdm);
            super.insertProjectData(fdcqVoList);
            List<InsertVo> ywrForDy = new ArrayList<InsertVo>();
            String bdcdyid = "";
            if (CollectionUtils.isNotEmpty(fdcqVoList)) {
                for (InsertVo insertVo : fdcqVoList) {
                    if (insertVo instanceof BdcBdcdy) {
                        bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                    }
                    if (insertVo instanceof BdcQlr&&((BdcQlr) insertVo).getQlrlx().equals(Constants.QLRLX_QLR)) {
                        ywrForDy.add(insertVo);
                    }
                }
            }

            project.setProid(UUIDGenerator.generate());
            project.setBdcdyid(bdcdyid);
            insertVoList.addAll(initDyFromBdc(project, sqlxdm, ywrForDy));
        } else {

            String pplx = AppConfig.getProperty("sjpp.type");
            List<InsertVo> dyVoList = null;
            if (StringUtils.isNotBlank(pplx) && pplx.equals(Constants.PPLX_CG)) {
                String syqQlid = "";
                List<GdFw> gdFwList = null;
                List<GdDy> gdDyList = new ArrayList<GdDy>();
                if (StringUtils.isNotBlank(project.getGdproid())) {
                    if (StringUtils.equals(project.getBdclx(),Constants.BDCLX_TDFW)) {
                        List<GdFwsyq> fwsyqList = gdFwService.getGdFwsyqListByGdproid(project.getGdproid(), Constants.GDQL_ISZX_WZX);
                        if (CollectionUtils.isNotEmpty(fwsyqList)) {
                            syqQlid = fwsyqList.get(0).getQlid();
                        }
                        gdFwList = gdFwService.getGdFwByQlid(syqQlid);
                        if (CollectionUtils.isNotEmpty(gdFwList)) {
                            for(GdFw gdFw:gdFwList) {
                                List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(gdFw.getFwid(), null);
                                if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                    for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                        GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRel.getQlid(), Constants.GDQL_ISZX_WZX);
                                        if (gdDy != null) {
                                            gdDyList.add(gdDy);
                                        }
                                    }
                                }
                            }
                        }
                    } else if(StringUtils.equals(project.getBdclx(),Constants.BDCLX_TD)){
                        List<GdTdsyq> tdsyqList = gdTdService.getGdTdsyqListByGdproid(project.getGdproid(), Constants.GDQL_ISZX_WZX);
                        if (CollectionUtils.isNotEmpty(tdsyqList)) {
                            syqQlid = tdsyqList.get(0).getQlid();
                        }
                        List<GdTd> gdTdList = gdTdService.getGdTdByQlid(syqQlid);
                        if (CollectionUtils.isNotEmpty(gdTdList)) {
                            for(GdTd gdTd:gdTdList) {
                                List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(gdTd.getTdid(), null);
                                if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                    for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                        GdDy gdDy = gdTdService.getGddyqByQlid(gdBdcQlRel.getQlid(),Constants.GDQL_ISZX_WZX);
                                        if (gdDy != null) {
                                            gdDyList.add(gdDy);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                project.setYqlid(syqQlid);
                List<InsertVo> fwsyqVoList = initFdcqFromGd(project, sqlxdm);
                //zx只能先保存
                super.insertProjectData(fwsyqVoList);

                //下面是抵押部分
                String bdcdyid = "";
                if (CollectionUtils.isNotEmpty(fwsyqVoList)) {
                    for (InsertVo insertVo : fwsyqVoList) {
                        if (insertVo instanceof BdcBdcdy) {
                            bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                        }
                    }
                }
                //房屋不动产抵押或者土地不动产抵押
                HashMap map = new HashMap();
                map.put("bdcdyid", bdcdyid);
                map.put("qszt",Constants.QLLX_QSZT_XS);
                List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                for (int i = 0; i < bdcDyaqList.size(); i++) {
                    project.setProid(UUIDGenerator.generate());
                    project.setBdcdyid(bdcdyid);
                    project.setXmly(Constants.XMLY_BDC);
                    dyVoList = initCgDyFromBdc(project, sqlxdm, bdcDyaqList.get(i).getProid(), bdcDyaqList.get(i).getQlid());
                    insertVoList.addAll(dyVoList);
                }

                //房屋不动产过渡抵押或者土地过渡抵押
                project.setBdcdyid(bdcdyid);
                List<InsertVo> dyList = initDyFromGd(project, gdDyList);
                if(CollectionUtils.isNotEmpty(dyList)) {
                    insertVoList.addAll(dyList);
                }

                //房屋匹配的土地证的不动产抵押和过渡抵押
                if(StringUtils.equals(project.getBdclx(),Constants.BDCLX_TDFW)&&CollectionUtils.isNotEmpty(gdFwList)) {
                    List<BdcDyaq> bdcDyaqFromPpTdList = new ArrayList<BdcDyaq>();
                    List<GdDy> pptdGdDyList = new ArrayList<GdDy>();
                    List<String> pptdBdcdyhList = new ArrayList<String>();
                    List<String> pptdGdTdidList = new ArrayList<String>();
                    for(GdFw gdFw:gdFwList) {
                        List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdFw.getFwid());
                        if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                            for (BdcGdDyhRel gdDyhRel : gdDyhRelList) {
                                if (StringUtils.isNotBlank(gdDyhRel.getTdid())&&!pptdGdTdidList.contains(gdDyhRel.getTdid())) {
                                    pptdGdTdidList.add(gdDyhRel.getTdid());
                                    List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(gdDyhRel.getTdid(), null);
                                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                            GdDy gdDy = gdTdService.getGddyqByQlid(gdBdcQlRel.getQlid(), Constants.GDQL_ISZX_WZX);
                                            if (gdDy != null) {
                                                pptdGdDyList.add(gdDy);
                                            }
                                        }
                                    }

                                    List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdDyhRel.getTdid());
                                    if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                                        for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                                            if (StringUtils.isNotBlank(bdcGdDyhRel.getBdcdyh())&&!pptdBdcdyhList.contains(bdcGdDyhRel.getBdcdyh())) {
                                                pptdBdcdyhList.add(bdcGdDyhRel.getBdcdyh());
                                                HashMap hashMap = new HashMap();
                                                hashMap.put("bdcDyh", bdcGdDyhRel.getBdcdyh());
                                                hashMap.put("qszt", Constants.QLLX_QSZT_XS);
                                                List<BdcDyaq> bdcDyaqFromPpTdTmepList = bdcDyaqService.queryBdcDyaq(hashMap);
                                                if(CollectionUtils.isNotEmpty(bdcDyaqFromPpTdTmepList)) {
                                                    bdcDyaqFromPpTdList.addAll(bdcDyaqFromPpTdTmepList);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(pptdGdDyList)) {
                        project.setBdcdyid(bdcdyid);
                        List<InsertVo> pptdGdDyInsertVo = initDyFromGd(project, pptdGdDyList);
                        if (CollectionUtils.isNotEmpty(pptdGdDyInsertVo)) {
                            insertVoList.addAll(pptdGdDyInsertVo);
                        }
                    }

                    if (CollectionUtils.isNotEmpty(bdcDyaqFromPpTdList)) {
                        for (BdcDyaq bdcDyaq : bdcDyaqFromPpTdList) {
                            project.setProid(UUIDGenerator.generate());
                            project.setBdcdyid(bdcdyid);
                            project.setXmly(Constants.XMLY_BDC);
                            dyVoList = initCgDyFromBdc(project, sqlxdm,bdcDyaq.getProid(),bdcDyaq.getQlid());
                            insertVoList.addAll(dyVoList);
                        }
                    }
                }
            }
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
     * @description 从过渡库创建换证登记
     */
    public List<InsertVo> initFdcqFromGd(Project project, String sqlxdm) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            //房屋所有权证办理不动产权证走换证
            List<InsertVo> list = creatProjectGzdjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    //根据平台sqlxdm赋值所有权的sqlx和djlx
                    if (StringUtils.isNotBlank(sqlxdm) && StringUtils.isNotBlank(((BdcXm) vo).getBdclx())
                            && StringUtils.isNotBlank(((BdcXm) vo).getQllx())) {
                        HashMap map = getLxBySqlxAndBdclx(sqlxdm, ((BdcXm) vo).getBdclx(), ((BdcXm) vo).getQllx(), "");
                        ((BdcXm) vo).setDjlx((String) map.get("djlx"));
                        ((BdcXm) vo).setSqlx((String) map.get("sqlx"));
                    }
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:liuxing@gtmap.cn">lx</a>
     * @description 从不动产登记库继承转移信息
     */
    public List<InsertVo> initFdcqFromBdc(Project project, String sqlxdm) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
        project.setQllx(qllxdm);
        List<InsertVo> list = creatProjectGzdjService.initVoFromOldData(project);
        if (CollectionUtils.isNotEmpty(list)) {
            for (InsertVo Vo : list) {
                if (Vo instanceof BdcXm) {
                    //根据平台sqlxdm赋值所有权申请类型、登记类型
                    if (StringUtils.isNotBlank(sqlxdm) && StringUtils.isNotBlank(((BdcXm) Vo).getBdclx())
                            && StringUtils.isNotBlank(((BdcXm) Vo).getQllx())) {
                        HashMap map = getLxBySqlxAndBdclx(sqlxdm, ((BdcXm) Vo).getBdclx(), ((BdcXm) Vo).getQllx(), "");
                        ((BdcXm) Vo).setDjlx((String) map.get("djlx"));
                        ((BdcXm) Vo).setSqlx((String) map.get("sqlx"));
                    }
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) Vo);
                    ((BdcXm) Vo).setDjsy(CommonUtil.formatEmptyValue(bdcQllxMapper.queryDjsyByQllx(((BdcXm) Vo).getQllx()).get(0)));
                    ((BdcXm) Vo).setDjyy(bdcSqlxDjyyRelService.getDjyyBySqlx(((BdcXm) Vo).getSqlx()));
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        insertVoList.add(bdcSjxx);
                    }
                }
            }
            insertVoList.addAll(list);
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:liuxing@gtmap.cn">lx</a>
     * @description 从不动产登记库生成抵押情况，如果不存在抵押则做抵押首次
     */
    public List<InsertVo> initDyFromBdc(Project project, String sqlxdm, List<InsertVo> ywrForDy) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (StringUtils.isNotBlank(project.getBdcdyh())) {
            HashMap map = new HashMap();
            map.put("bdcdyh", project.getBdcdyh());
            map.put("qszt", "1");
            List<QllxParent> bdcDyaqList = qllxParentService.queryQllxVo(new BdcDyaq(), map);
            //如果正式库存在抵押，则继承抵押信息
            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                for (int i = 0; i < bdcDyaqList.size(); i++) {
                    Project dyProject = new Project();
                    try {
                        BeanUtils.copyProperties(dyProject, project);
                        String proid = UUIDGenerator.generate18();
                        dyProject.setProid(proid);
                        dyProject.setDjId("");
                        dyProject.setGdproid("");
                        dyProject.setGdsyqid("");
                        dyProject.setYqlid("");
                        dyProject.setYbdcdyid(bdcDyaqList.get(i).getBdcdyid());
                        dyProject.setYxmid(bdcDyaqList.get(i).getProid());
                        dyProject.setXmly(Constants.XMLY_BDC);
                        dyProject.setQllx(Constants.QLLX_DYAQ);
                        dyProject.setDjsy(Constants.DJSY_DYAQ);
                        dyProject.setDjlx(Constants.DJLX_DYDJ_DM);
                        //创建xmrel
                        BdcXmRel bdcXmRel = new BdcXmRel();
                        bdcXmRel.setRelid(UUIDGenerator.generate18());
                        bdcXmRel.setProid(proid);
                        bdcXmRel.setYproid(bdcDyaqList.get(i).getProid());
                        bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
                        bdcXmRelList.add(bdcXmRel);
                        dyProject.setBdcXmRelList(bdcXmRelList);
                        List<InsertVo> dylist = creatProjectDyBgdjService.initVoFromOldData(dyProject);
                        //抵押的义务人应该是换证的权利人，所以不需要义务人
                        String dyProid = "";
                        for (InsertVo vo : dylist) {
                            if (vo instanceof BdcQlr) {
                                if (((BdcQlr) vo).getQlrlx().equals(Constants.QLRLX_YWR))
                                    continue;
                            } else if (vo instanceof BdcXm) {
                                dyProid = ((BdcXm) vo).getProid();
                                BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                                if (bdcSjxx != null) {
                                    bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                    insertVoList.add(bdcSjxx);
                                }
                                //抵押权变更申请类型获取
                                if (StringUtils.isNotBlank(((BdcXm) vo).getBdclx()) && StringUtils.isNotBlank(dyProject.getBdcdyh())
                                        && StringUtils.isNotBlank(((BdcXm) vo).getQllx())) {
                                    HashMap map1 = getLxBySqlxAndBdclx("", ((BdcXm) vo).getBdclx(), ((BdcXm) vo).getQllx(), dyProject.getBdcdyh());
                                    ((BdcXm) vo).setSqlx((String) map1.get("sqlx"));
                                    ((BdcXm) vo).setDydjlx(Constants.DJLX_BGDJ_DM);
                                }
                            }
                            insertVoList.add(vo);
                        }
                        if (CollectionUtils.isNotEmpty(ywrForDy)) {
                            for (InsertVo Vo : ywrForDy) {
                                BdcQlr bdcYwr = new BdcQlr();
                                BeanUtils.copyProperties(bdcYwr, (BdcQlr)Vo);
                                bdcYwr.setQlrid(UUIDGenerator.generate());
                                bdcYwr.setProid(dyProid);
                                bdcYwr.setQlrlx(Constants.QLRLX_YWR);
                                insertVoList.add(bdcYwr);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("CreatComplexHzWithDyProjectServiceImpl.initDyFromBdc",e);
                    }
                }
            }
        }
        return insertVoList;
    }


    /**
     * @author <a href="mailto:liuxing@gtmap.cn">lx</a>
     * @description 在成果时，从过渡库生成抵押情况
     */
    public List<InsertVo> initDyFromGd(Project project, List<GdDy> gdDyList) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (CollectionUtils.isNotEmpty(gdDyList)) {
            for (int i = 0; i < gdDyList.size(); i++) {
                Project dyProject = new Project();
                try {
                    BeanUtils.copyProperties(dyProject, project);
                    dyProject.setYqlid(gdDyList.get(i).getDyid());
                    dyProject.setProid(UUIDGenerator.generate18());
                    dyProject.setGdproid(gdDyList.get(i).getProid());

                    dyProject.setQllx(Constants.QLLX_DYAQ);
                    dyProject.setDjsy(Constants.DJSY_DYAQ);
                    dyProject.setDjlx(Constants.DJLX_DYDJ_DM);
                    List<InsertVo> dylist = creatProjectDyBgdjService.initVoFromOldData(dyProject);
                    for (InsertVo vo : dylist) {
                        if (vo instanceof BdcXm) {
                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                            if (bdcSjxx != null) {
                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                insertVoList.add(bdcSjxx);
                            }
                            //抵押权变更申请类型获取
                            if (StringUtils.isNotBlank(((BdcXm) vo).getBdclx()) && StringUtils.isNotBlank(dyProject.getBdcdyh())
                                    && StringUtils.isNotBlank(((BdcXm) vo).getQllx())) {
                                HashMap map = getLxBySqlxAndBdclx("", ((BdcXm) vo).getBdclx(), ((BdcXm) vo).getQllx(), dyProject.getBdcdyh());
                                ((BdcXm) vo).setSqlx((String) map.get("sqlx"));
                                ((BdcXm) vo).setDydjlx(Constants.DJLX_BGDJ_DM);
                                ((BdcXm) vo).setYbdcqzmh(gdDyList.get(i).getDydjzmh());
                            }
                        }
                        insertVoList.add(vo);
                    }
                } catch (Exception e) {
                    logger.error("CreatComplexHzWithDyProjectServiceImpl.initDyFromGd",e);
                }
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 在成果时，从不动产库生成抵押情况
     */
    public List<InsertVo> initCgDyFromBdc(Project project, String sqlx2, String yproid, String yqlid) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //需要考虑过程数据和成果数据的权利人问题
        if (project != null && StringUtils.isNotBlank(project.getProid()) && StringUtils.isNotBlank(project.getBdcdyid())) {
            project.setDjlx(Constants.DJLX_DYDJ_DM);
            if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                project.setSqlx(Constants.SQLX_FWDY_DM);
            } else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                project.setSqlx(Constants.SQLX_TDDY_DM);
            }
            project.setQllx(Constants.QLLX_DYAQ);
            project.setDjsy(Constants.DJSY_DYAQ);
            project.setYqlid(yqlid);
            project.setYxmid(yproid);
            List<InsertVo> list = creatProjectDyBgdjService.initVoFromOldData(project);
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    if(ybdcXm != null) {
                        ((BdcXm) vo).setYbdcqzh(ybdcXm.getYbdcqzh());
                    }
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(yproid);
                    if (CollectionUtils.isNotEmpty(bdcZsList) && StringUtils.isNotBlank(bdcZsList.get(0).getBdcqzh())) {
                        ((BdcXm) vo).setYbdcqzmh(bdcZsList.get(0).getBdcqzh());
                    }
                    insertVoList.add(vo);
                } else {
                    insertVoList.add(vo);
                }
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:liuxing@gtmap.cn">lx</a>
     * @description 根据平台申请类型判断所有权与抵押权的sqlx和djlx
     */
    public HashMap getLxBySqlxAndBdclx(String sqlxdm, String bdclx, String qllxdm, String bdcdyh) {
        HashMap map = new HashMap();
        String sqlx1 = "";
        String sqlx2 = "";
        //优先读取合并登记配置文件中的申请类型
        HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(sqlxdm);
        if (hbSqlxMap != null) {
            sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
            sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
        }
        if (StringUtils.isNotBlank(qllxdm) && !qllxdm.equals(Constants.QLLX_DYAQ)) {
            if (StringUtils.isNotBlank(sqlxdm)) {
                if (sqlxdm.equals(Constants.SQLX_DYHZ_DM)) {
                    map.put("sqlx", Constants.SQLX_HZ_DM);
                    map.put("djlx", Constants.DJLX_QTDJ_DM);
                } else if (sqlxdm.equals(Constants.SQLX_YSBZDY_DM)) {
                    map.put("sqlx", Constants.SQLX_YSBZ_DM);
                    map.put("djlx", Constants.DJLX_QTDJ_DM);
                } else if (sqlxdm.equals(Constants.SQLX_DYBG_DM)) {
                    map.put("djlx", Constants.DJLX_BGDJ_DM);
                    if (StringUtils.isNotBlank(bdclx) && bdclx.equals(Constants.BDCLX_TDFW)) {
                        if (StringUtils.isNotBlank(sqlx1)) {
                            map.put("sqlx", sqlx1);
                        } else {
                            map.put("sqlx", Constants.SQLX_BG_DM);
                        }
                    } else if (bdclx.equals(Constants.BDCLX_TD)) {
                        if (StringUtils.equals(qllxdm, Constants.QLLX_JTTD_CBJYQ))
                            map.put("sqlx", Constants.SQLX_NYDBG_DM);
                        else if (StringUtils.equals(qllxdm, Constants.QLLX_GYTD_JSYDSYQ))
                            map.put("sqlx", Constants.SQLX_JDMJYTBG_DM);
                        else if (StringUtils.equals(qllxdm, Constants.QLLX_JTTD_JSYDSYQ))
                            map.put("sqlx", Constants.SQLX_JTJSBG_DM);
                    } else if (bdclx.equals(Constants.BDCLX_HY)) {
                        map.put("sqlx", Constants.SQLX_HYBG_DM);
                    } else if (bdclx.equals(Constants.BDCLX_TDSL)) {
                        map.put("sqlx", Constants.SQLX_LQBG_DM);
                    }
                }
            }
        } else {
            if (StringUtils.isNotBlank(bdclx) && StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                map.put("sqlx", Constants.SQLX_FWDYBG_DM);
            } else if (StringUtils.isNotBlank(bdclx) && StringUtils.equals(bdclx, Constants.BDCLX_HY)) {
                map.put("sqlx", Constants.SQLX_HYDYBG_DM);
            } else if (StringUtils.isNotBlank(bdclx) && StringUtils.equals(bdclx, Constants.BDCLX_TDSL)) {
                map.put("sqlx", Constants.SQLX_LQDYBG_DM);
            } else if (StringUtils.isNotBlank(bdclx) && StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                String qllx = bdcdyService.getQllxFormBdcdy(bdcdyh);
                if (StringUtils.isNotBlank(qllx) && StringUtils.equals(qllx, Constants.QLLX_JTTD_JSYDSYQ))
                    map.put("sqlx", Constants.SQLX_JTJSDYBG_DM);
                else if (StringUtils.isNotBlank(qllx) && StringUtils.equals(qllx, Constants.QLLX_GYTD_JSYDSYQ))
                    map.put("sqlx", Constants.SQLX_GYJSDYBG_DM);
                else if (StringUtils.isNotBlank(qllx) && StringUtils.equals(qllx, Constants.QLLX_JTTD_CBJYQ))
                    map.put("sqlx", Constants.SQLX_NYDDYBG_DM);
            }
        }
        return map;
    }
}
