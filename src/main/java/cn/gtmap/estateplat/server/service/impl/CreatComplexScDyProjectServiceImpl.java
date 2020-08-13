package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQllxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
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
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.1.0, 2016/11/21.
 * @description 房屋做首次，并且创建抵押项目，针对以前土地做了抵押又盖了房子的情况
 */
public class CreatComplexScDyProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectDydjServiceImpl creatProjectDydjService;
    @Resource(name = "creatProjectScdjService")
    CreatProjectScdjServiceImpl creatProjectScdjService;
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


    @Override
    public List<InsertVo> initVoFromOldData(Xmxx xmxx) {
        Project project = null;
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }
        String workflowProid = "";
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }

        creatProjectNode(workflowProid);

        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 批量创建合并流程时，会删除之前已经插入数据库的项目
         */
        String sqlx1=null;
        String sqlx2=null;
        String sqlxdm = "";
        if(StringUtils.isNotBlank(project.getSqlx())){
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            HashMap hbSqlxMap= ReadXmlProps.getHbSqlx(sqlxdm);
            if(hbSqlxMap!=null){
                sqlx1= CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
                sqlx2= CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
            }
        }
        if (project != null) {

            /**
             * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
             * @description 创建xm,xmrel,bdcdy,spxx,djb,qlr信息
             */
            if (project.getXmly().equals(Constants.XMLY_BDC)) {   //从新建选择不动产权证
                project.setDjlx(Constants.DJLX_CSDJ_DM);
                List<InsertVo> fwsyqVoList=initSyqFromBdc(project,sqlx1);
                super.insertProjectData(fwsyqVoList);
                /**
                 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
                 * @description 查询bdc_zjjzwxx,有现势的抵押才生成抵押权利
                 */
                Boolean isdy = false;
                List<BdcZjjzwxx> bdcZjjzwxxList = null;
                if(StringUtils.isNotBlank(project.getBdcdyh())) {
                    Example example = new Example(BdcZjjzwxx.class);
                    example.createCriteria().andEqualTo("bdcdyh", project.getBdcdyh());
                   bdcZjjzwxxList = entityMapper.selectByExample(example);
                }
                if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                    for (BdcZjjzwxx bdcZjjzwxx : bdcZjjzwxxList) {
                        if (StringUtils.isBlank(bdcZjjzwxx.getDyzt()) || (StringUtils.isNotBlank(bdcZjjzwxx.getDyzt()) && bdcZjjzwxx.getDyzt().equals("0"))) {
                            isdy = true;
                            /**
                             * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
                             * @description 抵押权的proid暂存于该字段中用于继承权利人和义务人
                             */
                            project.setMsg(bdcZjjzwxx.getProid());
                            break;
                        }
                    }
                }
                if (isdy) {
                    //创建抵押流程
                    project.setYxmid(project.getProid());
                    project.setProid(UUIDGenerator.generate());
                    initDyFromBdc(project, sqlx2);
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
            dyProject.setProid(project.getProid());
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
            List<InsertVo> dylist = creatProjectDydjService.initVoFromOldData(dyProject);
            //新生成的抵押继承原抵押项目的权利人和义务人
            for (InsertVo vo : dylist) {
                if (vo instanceof BdcQlr) {
                    continue;
                } else if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        insertVoList.add(bdcSjxx);
                    }
                }
                insertVoList.add(vo);
            }
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(project.getMsg());
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                    bdcQlr.setProid(dyProject.getProid());
                }
                dylist.addAll(bdcQlrList);
            }
            super.insertProjectData(dylist);
            dylist.clear();
        } catch (Exception e) {
            logger.error("CreatComplexScDyProjectServiceImpl.initDyFromBdc",e);
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 创建房屋首次登记
     */
    public List<InsertVo> initSyqFromBdc(Project project,String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //使用不动产单元号确定权利类型
        String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
        project.setQllx(qllxdm);
        //房屋所有权证办理不动产权证
        List<InsertVo> list = creatProjectScdjService.initVoFromOldData(project);
        for (InsertVo vo : list) {
            if (vo instanceof BdcXm) {
                BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                if (bdcSjxx != null) {
                    bdcSjxx.setProid(project.getProid());
                    bdcSjxx.setWiid(project.getWiid());
                    bdcSjxx.setSjxxid(UUIDGenerator.generate());
                    insertVoList.add(bdcSjxx);
                }
                if(StringUtils.isNotBlank(sqlx1)) {
                    ((BdcXm) vo).setSqlx(sqlx1);
                }
            }
        }
        insertVoList.addAll(list);
        return  insertVoList;
    }

}