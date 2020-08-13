package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQllxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
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
 * @description 转移并且抵押新证
 */
public class CreatComplexZyDyProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectDydjServiceImpl creatProjectDydjServiceImpl;
    @Resource(name = "creatProjectZydjService")
    CreatProjectZydjServiceImpl creatProjectZydjService;
    @Resource(name = "creatProjectSfZydjServiceImpl")
    CreatProjectSfZydjServiceImpl creatProjectSfZydjService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    QllxParentService qllxParentService;
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
    private BdcYgService bdcYgService;
    @Autowired
    private SysWorkFlowDefineService workFlowDefineService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcGdDyhRelService gdDyhRelService;
    @Autowired
    private GdYgService gdYgService;

    private static final String PARAMETER_SYQQLID = "syqqlid";
    private static final String PARAMETER_DYQLID = "dyqlid";



    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        if (xmxx instanceof Project){
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

        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //lst根据工作流wiid  查找是否已经存在  如果存在 则删除对应的记录
        if (StringUtils.isNotBlank(project.getWiid())) {
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }
        String sqlx1 = null;
        String sqlx2 = null;
        String sqlxdm = "";
        String createSqlxMc = null;

        if (StringUtils.isNotBlank(project.getSqlx())) {
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
                if (pfWorkFlowInstanceVo != null) {
                    PfWorkFlowDefineVo pfWorkFlowDefineVo = workFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                    if(pfWorkFlowDefineVo != null) {
                        createSqlxMc = pfWorkFlowDefineVo.getWorkflowName();
                    }
                }
            }
            HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(sqlxdm);
            if (hbSqlxMap != null) {
                if(StringUtils.isNotBlank(createSqlxMc) && StringUtils.equals(createSqlxMc,Constants.SQLX_SPFZYDY_MC)){
                    sqlx1 = Constants.SQLX_SPFMMZYDJ_DM;
                }else {
                    sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
                }
                sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
            }
        }

        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 创建xm, xmrel, bdcdy, spxx, djb, qlr信息
         */
        if (StringUtils.equals(project.getXmly(),Constants.XMLY_BDC)) {   //从新建选择不动产权证
            project.setDjlx(Constants.DJLX_ZYDJ_DM);
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
            insertVoList.addAll(initDyFromBdc(project, sqlx2, sqlxdm));
        } else { //从匹配选择过渡证
            String syqQlid = "";
            String dyQlid = "";
            if (StringUtils.isNotBlank(project.getGdproid())) {
                HashMap<String,String> qlidMap = getSyqQlidAndDyQlid(project.getGdproid());
                if(qlidMap != null){
                    if(StringUtils.isNotBlank(qlidMap.get(PARAMETER_SYQQLID))){
                        syqQlid = qlidMap.get(PARAMETER_SYQQLID);
                    }
                    if(StringUtils.isNotBlank(qlidMap.get(PARAMETER_DYQLID))){
                        dyQlid = qlidMap.get(PARAMETER_DYQLID);
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
            String pplx = AppConfig.getProperty("sjpp.type");
            List<InsertVo> dyList = null;
            if (StringUtils.isNotBlank(pplx) && pplx.equals(Constants.PPLX_GC)) {
                //过程
                project.setProid(UUIDGenerator.generate());
                project.setYqlid(dyQlid);
                project.setBdcdyid(bdcdyid);
                dyList = initDyFromGd(project, sqlx2, sqlxdm);
            } else {
                //成果
                project.setProid(UUIDGenerator.generate());
                project.setBdcdyid(bdcdyid);
                dyList = initDyFromBdc(project, sqlx2, sqlxdm);
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
            //使用不动产单元号确定权利类型
            String createQllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(createQllxdm);
            //房屋所有权证办理不动产权证走转移
            List<InsertVo> list = null;
            if(StringUtils.equals(sqlx1,Constants.SQLX_ZY_SFCD)){
                list = creatProjectSfZydjService.initVoFromOldData(project);
            }else{
                list = creatProjectZydjService.initVoFromOldData(project);
            }
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
                    }else {
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
     * @description 从不动产登记库生成抵押情况，如果不存在抵押则做抵押首次
     */
    public List<InsertVo> initDyFromBdc(Project project, String sqlx2, String sqlxdm) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        String sqlx = project.getSqlx();
        //如果正式库不存在抵押，则创建一个新的抵押权利，不管是新建还是过渡过来的项目，相当于直接拿着所有权证创建抵押
        //只需要修改proid,djlx,sqlx,qllx
        Project dyProject = new Project();
        try {
            BeanUtils.copyProperties(dyProject, project);

            dyProject.setProid(project.getProid());
            dyProject.setDjlx(Constants.DJLX_DYDJ_DM);
            if (StringUtils.isNotBlank(sqlx2)) {
                dyProject.setSqlx(sqlx2);
            } else {
                if (StringUtils.equals(dyProject.getBdclx(), Constants.BDCLX_TDFW)) {
                    dyProject.setSqlx(Constants.SQLX_FWDY_DM);
                }
                else if (StringUtils.equals(dyProject.getBdclx(), Constants.BDCLX_TD)) {
                    dyProject.setSqlx(Constants.SQLX_TDDY_DM);
                }
            }
            dyProject.setQllx(Constants.QLLX_DYAQ);
            List<InsertVo> dylist = null;
            if(StringUtils.equals(sqlxdm, Constants.SQLX_CLF)) {
                dylist = creatProjectDyBgdjService.initVoFromOldData(dyProject);
            }else{
                dylist = creatProjectDydjServiceImpl.initVoFromOldData(dyProject);
            }
            List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(project.getBdcdyh(), "1");
            List<GdYg> gdYgList = gdYgService.getGdygListByBdcdyh(project.getBdcdyh());
            //新生成的抵押没有权利人和义务人（商品房转移抵押合办需要继承预告抵押的权利人义务人）
            for (InsertVo vo : dylist) {
                if (vo instanceof BdcQlr) {
                    if(StringUtils.equals(sqlx,Constants.SQLX_CLF) && (CollectionUtils.isNotEmpty(bdcYgList) ||CollectionUtils.isNotEmpty(gdYgList))){
                    }else{
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
            logger.error("CreatComplexZyDyProjectServiceImpl.initVoFromOldData",e);
        }

        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 在过程时，从过渡库生成抵押情况
     */
    public List<InsertVo> initDyFromGd(Project project, String sqlx2, String sqlxdm) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //需要考虑过程数据和成果数据的权利人问题
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            project.setDjlx(Constants.DJLX_DYDJ_DM);
            if (StringUtils.isNotBlank(sqlx2)) {
                project.setSqlx(sqlx2);
            } else {
                if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                    project.setSqlx(Constants.SQLX_FWDY_DM);
                }else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                    project.setSqlx(Constants.SQLX_TDDY_DM);
                }
            }
            project.setQllx(Constants.QLLX_DYAQ);
            List<InsertVo> list = null;
            if(StringUtils.equals(sqlxdm, Constants.SQLX_CLF)){
                list = creatProjectDyBgdjService.initVoFromOldData(project);
            }else {
               list = creatProjectDydjServiceImpl.initVoFromOldData(project);
            }
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
                }
                insertVoList.add(vo);
            }
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
        //房屋所有权证办理不动产权证走转移
        List<InsertVo> list = null;
        if(StringUtils.equals(sqlx1,Constants.SQLX_ZY_SFCD)){
            list = creatProjectSfZydjService.initVoFromOldData(project);
        }else{
            list = creatProjectZydjService.initVoFromOldData(project);
        }
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
                    if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                        if (qllxdm.equals(Constants.QLLX_GYTD_FWSUQ)) {
                            //存量房买卖转移和抵押登记 商品房买卖转移和抵押登记 登记类型需要区分插入
                            if(StringUtils.isNotBlank(project.getSqlx()) && StringUtils.equals(project.getSqlx(),Constants.SQLX_CLF)){
                                if(StringUtils.isNotBlank(project.getYxmid())){
                                    String yproid = project.getYxmid();
                                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(yproid);
                                    if(null != bdcXm){
                                        if(StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_SPFGYSCDJ_DM)){
                                            ((BdcXm) vo).setSqlx("211");
                                        }else{
                                            ((BdcXm) vo).setSqlx("212");
                                        }
                                    }
                                }
                            }else{
                                ((BdcXm) vo).setSqlx("225");
                            }
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
                logger.error("CreatComplexZyDyProjectServiceImpl.saveOrUpdateProjectData",e);
            }

            if (StringUtils.isNotBlank(id) && entityMapper.selectByPrimaryKey(vo.getClass(), id) != null) {
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
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param project
     * @param list
     * @param ygdjzlmc
     * @description 处理预告信息
     */
    public void handleYgxx(Project project, List<InsertVo> list,String ygdjzlmc) {
        if (StringUtils.isNotBlank(project.getBdcdyh())) {
            List<GdYg> gdYgList = new ArrayList<GdYg>();
            List<BdcGdDyhRel> gdDyhRelList = gdDyhRelService.getGdDyhRelByDyh(project.getBdcdyh());
            if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                for (BdcGdDyhRel gdDyhRel : gdDyhRelList) {
                    if (StringUtils.isNotBlank(gdDyhRel.getGdid())) {
                        List<GdYg> gdYgListTmep = gdYgService.queryGdYgByBdcId(gdDyhRel.getGdid());
                        if (CollectionUtils.isNotEmpty(gdYgListTmep)) {
                            for (GdYg gdYg : gdYgListTmep) {
                                if (gdYg.getIszx() != 1) {
                                    gdYgList.add(gdYg);
                                }
                            }
                        }
                    }
                }
            }

            List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(project.getBdcdyh(), Constants.QLLX_QSZT_XS.toString());

            if (CollectionUtils.isNotEmpty(gdYgList) || CollectionUtils.isNotEmpty(bdcYgList)) {
                addYgxx(list, project, gdYgList, bdcYgList,ygdjzlmc);
            }
        }

    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param list
     * @param project
     * @param gdYgList
     * @param bdcYgList
     * @param ygdjzlmc
     * @description 增加预告信息
     */
    public void addYgxx(List<InsertVo> list, Project project, List<GdYg> gdYgList, List<BdcYg> bdcYgList, String ygdjzlmc) {
        StringBuilder ybdcqzh = new StringBuilder();
        if (CollectionUtils.isNotEmpty(gdYgList)) {
            for (GdYg gdYg : gdYgList) {
                //增加ybdcqzh
                if(gdYg != null && StringUtils.equals(StringUtils.deleteWhitespace(gdYg.getYgdjzl()), ygdjzlmc)) {
                    if (StringUtils.isNotBlank(gdYg.getYgdjzmh())) {
                        if (StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh.append(gdYg.getYgdjzmh());
                        } else {
                            ybdcqzh.append(",").append(gdYg.getYgdjzmh());
                        }
                    }

                    //增加bdc_xm_rel
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setYqlid(gdYg.getYgid());
                    bdcXmRel.setYproid(gdYg.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                    list.add(bdcXmRel);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(bdcYgList)) {
            String bdcYgdjzl = "";
            if(StringUtils.equals(ygdjzlmc,Constants.YGDJZL_YGSPF_MC)) {
                bdcYgdjzl = Constants.YGDJZL_MM;
            }else if(StringUtils.equals(ygdjzlmc,Constants.YGDJZL_YGSPFDYAQ_MC)){
                bdcYgdjzl = Constants.YGDJZL_DY;
            }

            for(BdcYg bdcYg:bdcYgList){
                if(bdcYg != null && StringUtils.isNotBlank(bdcYgdjzl) && StringUtils.indexOf(bdcYgdjzl,bdcYg.getYgdjzl())> -1){
                    //增加ybdcqzh
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcYg.getProid());
                    if(CollectionUtils.isNotEmpty(bdcZsList)){
                        for(BdcZs bdcZs:bdcZsList){
                            if(StringUtils.isNotBlank(bdcZs.getBdcdyh())){
                                if (StringUtils.isBlank(ybdcqzh)) {
                                    ybdcqzh.append(bdcZs.getBdcqzh());
                                } else {
                                    ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                    }

                    //增加bdc_xm_rel
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setYqlid(bdcYg.getQlid());
                    bdcXmRel.setYproid(bdcYg.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                    list.add(bdcXmRel);
                }
            }
        }

        for (InsertVo insertVo : list) {
            if (insertVo instanceof BdcXm) {
                BdcXm bdcXm = (BdcXm) insertVo;
                if(StringUtils.isBlank(bdcXm.getYbdcqzh())){
                    bdcXm.setYbdcqzh(ybdcqzh.toString());
                }else{
                    bdcXm.setYbdcqzh(bdcXm.getYbdcqzh() + "," + ybdcqzh.toString());
                }
            }
        }
    }

    public HashMap<String,String> getSyqQlidAndDyQlid(String gdProid){
        HashMap<String,String> map = new HashMap();
        if(StringUtils.isNotBlank(gdProid)){
            String[] gdProids = gdProid.split(",");
            StringBuilder syqQlidBuilder = new StringBuilder();
            StringBuilder dyQlidBuilder = new StringBuilder();
            if(gdProids != null && gdProids.length > 0){
                for(String gdProidTemps : gdProids){
                    List<GdFwsyq> fwsyqList = gdFwService.getGdFwsyqListByGdproid(gdProidTemps, 0);
                    if (CollectionUtils.isNotEmpty(fwsyqList)) {
                        if(StringUtils.isBlank(syqQlidBuilder)){
                            syqQlidBuilder.append(fwsyqList.get(0).getQlid());
                        }else{
                            syqQlidBuilder.append(",").append(fwsyqList.get(0).getQlid());
                        }
                    }
                    List<GdDy> dyList = gdFwService.getGdDyListByGdproid(gdProidTemps, 0);
                    if (CollectionUtils.isNotEmpty(dyList)) {
                        if(StringUtils.isBlank(dyQlidBuilder)){
                            dyQlidBuilder.append(dyList.get(0).getDyid());
                        }else{
                            dyQlidBuilder.append(",").append(dyList.get(0).getDyid());
                        }
                    }
                }
                if(StringUtils.isNotBlank(syqQlidBuilder)){
                    map.put(PARAMETER_SYQQLID,syqQlidBuilder.toString());
                }
                if(StringUtils.isNotBlank(dyQlidBuilder)){
                    map.put(PARAMETER_DYQLID,dyQlidBuilder.toString());
                }
            }
        }
        return map;
    }
}