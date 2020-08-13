package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
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
 * @author <a href="mailto:lijian@gtmap.cn">lizhi</a>
 * @version 1.1.0, 2016/12/19.
 * @description 商品房分摊土地核减登记(逐户解押+逐户注销)
 */
public class CreatComplexSpfZhjyZhzxProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Resource(name = "createProjectZhjydjServiceImpl")
    CreateProjectZhjydjServiceImpl createProjectZhjydjService;
    @Resource(name = "createProjectZhzxdjServiceImpl")
    CreateProjectZhzxdjServiceImpl createProjectZhzxdjService;
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
        //lst根据工作流wiid  查找是否已经存在  如果存在 则删除对应的记录
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
        try{
        if (project.getXmly().equals(Constants.XMLY_BDC)) {
            project.setSqlx(sqlx1);
            project.setDjlx(Constants.DJLX_DYDJ_DM);
            Project projectTemp=new Project();
            BeanUtils.copyProperties(projectTemp,project);
            List<InsertVo> zhjyList = createProjectZhjydjService.initVoFromOldData(project);
            insertVoList.addAll(zhjyList);

            projectTemp.setProid(UUIDGenerator.generate());
            projectTemp.setSqlx(sqlx2);
            projectTemp.setDjlx(Constants.DJLX_ZXDJ_DM);
            List<BdcXmRel> bdcXmRelList=new ArrayList<BdcXmRel>();
            BdcXmRel bdcXmRel = projectTemp.getBdcXmRelList().get(0);
            bdcXmRel.setRelid(UUIDGenerator.generate18());
            bdcXmRel.setProid(projectTemp.getProid());
            bdcXmRelList.add(bdcXmRel);
            projectTemp.setBdcXmRelList(bdcXmRelList);
            List<InsertVo> zhzxList = createProjectZhzxdjService.initVoFromOldData(projectTemp);
            insertVoList.addAll(zhzxList);
        }
        }catch (Exception e){
            logger.error("CreatComplexSpfZhjyZhzxProjectServiceImpl.initVoFromOldData",e);
        }

        return insertVoList;
    }

}