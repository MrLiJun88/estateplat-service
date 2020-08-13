package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdFwMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-16
 */

public class ProjectServiceImpl implements ProjectService {
    @Resource(name = "creatProjectDefaultService")
    CreatProjectService creatProjectDefaultService;
    @Resource(name = "creatProjectScdjService")
    CreatProjectService creatProjectScdjService;
    @Resource(name = "creatProjectZydjService")
    CreatProjectService creatProjectZydjService;
    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectService creatProjectDydjService;
    @Resource(name = "creatProjectBgdjServiceImpl")
    CreatProjectService creatProjectBgdjServiceImpl;
    @Resource(name = "creatComplexBgdjServiceImpl")
    CreatProjectService creatComplexBgdjServiceImpl;
    @Resource(name = "creatProjectMulBdcdyDyBgdjServiceImpl")
    CreatProjectService creatProjectMulBdcdyDyBgdjServiceImpl;
    @Resource(name = "creatProjectCfdjServiceImpl")
    CreatProjectService creatProjectCfdjServiceImpl;
    @Resource(name = "creatProjectZxdjServiceImpl")
    CreatProjectService creatProjectZxdjServiceImpl;
    @Resource(name = "creatProjectYgdjServiceImpl")
    CreatProjectService creatProjectYgdjServiceImpl;
    @Resource(name = "CreatComplexBgWithDyServiceImpl")
    CreatProjectService creatComplexBgWithDyServiceImpl;
    @Resource(name = "creatProjectDyBgdjServiceImpl")
    CreatProjectService creatProjectDyBgdjServiceImpl;
    @Resource(name = "creatProjectYydjServiceImpl")
    CreatProjectService creatProjectYydjServiceImpl;
    @Resource(name = "creatProjectGzdjServiceImpl")
    CreatProjectService creatProjectGzdjServiceImpl;
    @Resource(name = "creatProjectYsbzServiceImpl")
    CreatProjectService creatProjectYsbzServiceImpl;
    @Resource(name = "creatProjectHmZydjService")
    CreatProjectService creatProjectHmZydjService;
    @Resource(name = "creatProjectZxTxdjServiceImpl")
    CreatProjectService creatProjectZxTxdjServiceImpl;
    @Resource(name = "creatProjectCfdjNoQlServiceImpl")
    CreatProjectService creatProjectCfdjNoQlServiceImpl;
    @Resource(name = "creatProjectZxTxdjNoQlServiceImpl")
    CreatProjectService creatProjectZxTxdjNoQlServiceImpl;
    @Resource(name = "creatProjectBgZyHbServiceImpl")
    CreatProjectService creatProjectBgZyHbServiceImpl;
    @Resource(name = "CreatProjectZyAndQtzyHbServiceImpl")
    CreatProjectService CreatProjectZyAndQtzyHbServiceImpl;
    @Resource(name = "creatProjectZxTdsyqDjNoQlServiceImpl")
    CreatProjectService creatProjectZxTdsyqDjNoQlServiceImpl;
    @Resource(name = "creatComplexProjectServiceImpl")
    CreatProjectService creatComplexProjectServiceImpl;
    @Resource(name = "creatProjectSfcdjfdjService")
    CreatProjectService creatProjectSfcdjfdjService;
    @Resource(name = "creatProjectHydjServiceImpl")
    CreatProjectService creatProjectHydjServiceImpl;
    @Resource(name = "creatComplexYgdjServiceImpl")
    CreatProjectService creatComplexYgdjServiceImpl;
    @Resource(name = "creatComplexCfProjectServiceImpl")
    CreatProjectService creatComplexCfProjectServiceImpl;
    @Resource(name = "creatComplexYzxdjServiceImpl")
    CreatProjectService creatComplexYzxdjServiceImpl;
    @Resource(name = "creatComplexDydjProjectServiceImpl")
    CreatProjectService creatComplexDydjProjectServiceImpl;
    @Resource(name = "creatComplexDybgdjProjectServiceImpl")
    CreatProjectService creatComplexDybgdjProjectServiceImpl;
    @Resource(name = "creatComplexZyDyProjectServiceImpl")
    CreatProjectService creatComplexZyDyProjectServiceImpl;
    @Resource(name = "CreatComplexZyWithDyProjectServiceImpl")
    CreatProjectService creatComplexZyWithDyProjectServiceImpl;
    @Resource(name = "creatComplexZyProjectServiceImpl")
    CreatProjectService creatComplexZyProjectServiceImpl;
    @Resource(name = "creatComplexHzDyProjectServiceImpl")
    CreatProjectService creatComplexHzDyProjectServiceImpl;
    @Resource(name = "CreatComplexHzWithDyProjectServiceImpl")
    CreatProjectService creatComplexHzWithDyProjectServiceImpl;
    @Resource(name = "creatComplexYgdjPlProjectServiceImpl")
    CreatProjectService creatComplexYgdjPlProjectServiceImpl;
    @Resource(name = "creatComplexYsbzProjectServiceImpl")
    CreatProjectService creatComplexYsbzProjectServiceImpl;
    @Resource(name = "creatProjectFwFgHbBgServiceImpl")
    CreatProjectService creatProjectFwFgHbBgServiceImpl;
    @Resource(name = "creatComplexProjectNewServiceImpl")
    CreatProjectService creatComplexProjectNewServiceImpl;
    @Resource(name = "creatProjectFwFgHbZyServiceImpl")
    CreatProjectService creatProjectFwFgHbZyServiceImpl;
    @Resource(name = "creatProjectTdFgHbBgServiceImpl")
    CreatProjectService creatProjectTdFgHbBgServiceImpl;
    @Resource(name = "creatProjectTdFgHbZyServiceImpl")
    CreatProjectService creatProjectTdFgHbZyServiceImpl;
    @Resource(name = "createProjectZhjydjServiceImpl")
    CreatProjectService createProjectZhjydjServiceImpl;
    @Resource(name = "createProjectZhzxdjServiceImpl")
    CreatProjectService createProjectZhzxdjServiceImpl;
    @Resource(name = "creatProjectSfZydjServiceImpl")
    CreatProjectService creatProjectSfZydjServiceImpl;

    @Resource(name = "createDyZxForZjgcServiceImpl")
    CreatProjectService createDyZxForZjgcServiceImpl;
    @Resource(name = "createDyBgForZjgcServiceImpl")
    CreatProjectService createDyBgForZjgcServiceImpl;
    @Resource(name = "createDyZyForZjgcServiceImpl")
    CreatProjectService createDyZyForZjgcServiceImpl;
    @Resource(name = "creatComplexDyZydjProjectServiceImpl")
    CreatProjectService creatComplexDyZydjProjectServiceImpl;
    @Resource(name = "creatComplexZxTxProjectServiceImpl")
    CreatProjectService creatComplexZxTxProjectServiceImpl;
    @Resource(name = "creatProjectBdcdyDjServiceImpl")
    CreatProjectService creatProjectBdcdyDjService;
    @Resource(name = "creatComplexSpfZhjyZhzxProjectServiceImpl")
    CreatProjectService creatComplexSpfZhjyZhzxProjectServiceImpl;
    @Resource(name = "creatProjectYgBgdjServiceImpl")
    CreatProjectService creatProjectYgBgdjService;
    @Resource(name = "creatComplexScdjAndPldyProjectServiceImpl")
    CreatComplexScdjAndPldyProjectServiceImpl creatComplexScdjAndPldyProjectServiceImpl;
    @Resource(name = "CreatComplexScWithDyServiceImpl")
    CreatProjectService creatComplexScWithDyServiceImpl;
    @Resource(name = "creatProjectSyTtBgdjServiceImpl")
    CreatProjectService creatProjectSyTtBgdjServiceImpl;
    @Resource(name = "creatComplexFgHbHzServiceImpl")
    CreatProjectService creatComplexFgHbHzServiceImpl;
    @Resource(name = "creatProjectArbitraryServiceImpl")
    CreatProjectService creatProjectArbitraryServiceImpl;
    @Resource(name = "createComplexScYtdDyServiceImpl")
    CreatProjectService createComplexScYtdDyServiceImpl;
    @Resource(name = "creatComplexScDyProjectService")
    CreatProjectService creatComplexScDyProjectService;
    @Resource(name = "creatProjectHhLcdjServiceImpl")
    CreatProjectService creatProjectHhLcdjServiceImpl;

    @Resource(name = "turnProjectDefaultService")
    TurnProjectService turnProjectDefaultService;
    @Resource(name = "turnProjectDydjServiceImpl")
    TurnProjectService turnProjectDydjServiceImpl;
    @Resource(name = "turnProjectBgdjServiceImpl")
    TurnProjectService turnProjectBgdjServiceImpl;
    @Resource(name = "turnProjectDyBgdjServiceImpl")
    TurnProjectService turnProjectDyBgdjServiceImpl;
    @Resource(name = "turnProjectCfdjServiceImpl")
    TurnProjectService turnProjectCfdjServiceImpl;
    @Resource(name = "turnProjectZxdjServiceImpl")
    TurnProjectService turnProjectZxdjServiceImpl;
    @Resource(name = "turnProjectYgdjServiceImpl")
    TurnProjectService turnProjectYgdjServiceImpl;
    @Resource(name = "turnProjectYydjServiceImpl")
    TurnProjectService turnProjectYydjServiceImpl;
    @Resource(name = "turnProjectZydjServiceImpl")
    TurnProjectService turnProjectZydjServiceImpl;
    @Resource(name = "turnProjectBgdjWithDybgServiceImpl")
    TurnProjectService turnProjectBgdjWithDybgServiceImpl;
    @Resource(name = "turnProjectGzdjServiceImpl")
    TurnProjectService turnProjectGzdjServiceImpl;
    @Resource(name = "turnComplexDydjProjectServiceImpl")
    TurnProjectService turnComplexDydjProjectServiceImpl;
    @Resource(name = "turnComplexScdjProjectServiceImpl")
    TurnProjectService turnComplexScdjProjectServiceImpl;
    @Resource(name = "turnComplexScdjCybzProjectServiceImpl")
    TurnProjectService turnComplexScdjCybzProjectServiceImpl;
    @Resource(name = "turnComplexSigleDydjProjectServiceImpl")
    TurnProjectService turnComplexSigleDydjProjectServiceImpl;
    @Resource(name = "turnComplexYgdjProjectServiceImpl")
    TurnProjectService turnComplexYgdjProjectServiceImpl;
    @Resource(name = "turnComplexYzxdjProjectServiceImpl")
    TurnProjectService turnComplexYzxdjProjectServiceImpl;
    @Resource(name = "turnComplexBgdjProjectServiceImpl")
    TurnProjectService turnComplexBgdjProjectServiceImpl;
    @Resource(name = "turnComplexZydjProjectServiceImpl")
    TurnProjectService turnComplexZydjProjectServiceImpl;
    @Resource(name = "turnComplexZyDyProjectServiceImpl")
    TurnProjectService turnComplexZyDyProjectServiceImpl;
    @Resource(name = "turnComplexBgZyProjectServiceImpl")
    TurnProjectService turnComplexBgZyProjectServiceImpl;
    @Resource(name = "turnComplexDyBgServiceImpl")
    TurnProjectService turnComplexDyBgServiceImpl;
    @Resource(name = "turnComplexHzDyProjectServiceImpl")
    TurnProjectService turnComplexHzDyProjectServiceImpl;
    @Resource(name = "turnComplexYgdjPlProjectServiceImpl")
    TurnProjectService turnComplexYgdjPlProjectServiceImpl;
    @Resource(name = "turnComplexYsbzProjectServiceImpl")
    TurnProjectService turnComplexYsbzProjectServiceImpl;
    @Resource(name = "turnComplexScdjAndPldyProjectServiceImpl")
    TurnComplexScdjAndPldyProjectServiceImpl turnComplexScdjAndPldyProjectServiceImpl;
    @Resource(name = "turnProjectDyZxForZjgcServiceImpl")
    TurnProjectService turnProjectDyZxForZjgcServiceImpl;
    @Resource(name = "turnProjectDyBgForZjgcServiceImpl")
    TurnProjectService turnProjectDyBgForZjgcServiceImpl;
    @Resource(name = "turnProjectDyZyForZjgcServiceImpl")
    TurnProjectService turnProjectDyZyForZjgcServiceImpl;
    @Resource(name = "turnProjectZhDydjServiceImpl")
    TurnProjectService turnProjectZhDydjServiceImpl;
    @Resource(name = "turnProjectYgBgdjServiceImpl")
    TurnProjectService turnProjectYgBgdjService;
    @Resource(name = "turnProjectSyTtBgdjServiceImpl")
    TurnProjectService turnProjectSyTtBgdjServiceImpl;
    @Resource(name = "turnComplexFgHbHzProjectServiceImpl")
    TurnProjectService turnComplexFgHbHzProjectServiceImpl;
    @Resource(name = "turnProjectArbitraryServiceImpl")
    TurnProjectService turnProjectArbitraryServiceImpl;
    @Resource(name = "turnComplexScYtdDyProjectServiceImpl")
    TurnProjectService turnComplexScYtdDyProjectServiceImpl;
    @Resource(name = "turnProjectJsydsyqLhdjService")
    TurnProjectService turnProjectJsydsyqLhdjService;

    @Resource(name = "turnProjectNewDjDjService")
    TurnProjectService turnProjectNewDjDjServiceImpl;

    @Resource(name = "endProjectDefaultServiceImpl")
    EndProjectService endProjectDefaultServiceImpl;
    @Resource(name = "endProjectZydjServiceImpl")
    EndProjectService endProjectZydjServiceImpl;
    @Resource(name = "endProjectDydjServiceImpl")
    EndProjectService endProjectDydjServiceImpl;
    @Resource(name = "endProjectCfdjServiceImpl")
    EndProjectService endProjectCfdjServiceImpl;
    @Resource(name = "endProjectZxdjServiceImpl")
    EndProjectService endProjectZxdjServiceImpl;
    @Resource(name = "endProjectYgdjServiceImpl")
    EndProjectService endProjectYgdjServiceImpl;
    @Resource(name = "endProjectYydjServiceImpl")
    EndProjectService endProjectYydjServiceImpl;
    @Resource(name = "endProjectGzdjServiceImpl")
    EndProjectService endProjectGzdjServiceImpl;
    @Resource(name = "endProjectYsbzServiceImpl")
    EndProjectService endProjectYsbzServiceImpl;
    @Resource(name = "endComplexProjectServiceImpl")
    EndProjectService endComplexProjectServiceImpl;
    @Resource(name = "endProjectJfdjServiceImpl")
    EndProjectService endProjectJfdjServiceImpl;
    @Resource(name = "endComplexYgdjServiceImpl")
    EndProjectService endComplexYgdjServiceImpl;
    @Resource(name = "endComplexHzDyProjectServiceImpl")
    EndProjectService endComplexHzDyProjectServiceImpl;
    @Resource(name = "endProjectDyZxForZjgcServiceImpl")
    EndProjectService endProjectDyZxForZjgcServiceImpl;
    @Resource(name = "endProjectDyBgForZjgcServiceImpl")
    EndProjectService endProjectDyBgForZjgcServiceImpl;
    @Resource(name = "endProjectDyZyForZjgcServiceImpl")
    EndProjectService endProjectDyZyForZjgcServiceImpl;
    @Resource(name = "endProjectBdcDjServiceImpl")
    EndProjectService endProjectBdcDjServiceImpl;
    @Resource(name = "endProjectZjdyZxfdydjServiceImpl")
    EndProjectService endProjectZjdyZxfdydjServiceImpl;
    /**
     * 批量选择证书（多个不动产单元） 进行批量抵押变更
     *
     * @author lisongtao
     */
    @Resource(name = "endComplexDybgdjServiceImpl")
    EndProjectService endComplexDybgdjServiceImpl;
    @Resource(name = "endComplexZyDyProjectServiceImpl")
    EndProjectService endComplexZyDyProjectServiceImpl;
    @Resource(name = "endComplexYzxdjServiceImpl")
    EndProjectService endComplexYzxdjServiceImpl;
    @Resource(name = "endProjectWppZxdjServiceImpl")
    EndProjectService endProjectWppZxdjServiceImpl;
    @Resource(name = "endProjectZhjydjServiceImpl")
    EndProjectService endProjectZhjydjServiceImpl;
    @Resource(name = "endProjectZhzxdjServiceImpl")
    EndProjectService endProjectZhzxdjServiceImpl;
    @Resource(name = "endProjectSfcdjfdjServiceImpl")
    EndProjectService endProjectSfcdjfdjServiceImpl;
    @Resource(name = "endComplexScDyProjectServiceImpl")
    EndProjectService endComplexScDyProjectServiceImpl;
    @Resource(name = "endProjectZyDybgServiceImpl")
    EndProjectService endProjectZyDybgServiceImpl;
    @Resource(name = "delProjectDefaultServiceImpl")
    DelProjectService delProjectDefaultServiceImpl;
    @Resource(name = "delProjectScdjServiceImpl")
    DelProjectService delProjectScdjServiceImpl;
    @Resource(name = "delProjectZydjServiceImpl")
    DelProjectService delProjectZydjServiceImpl;
    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;
    @Resource(name = "delProjectCfdjServiceImpl")
    DelProjectService delProjectCfdjServiceImpl;
    @Resource(name = "delProjectZxdjServiceImpl")
    DelProjectService delProjectZxdjServiceImpl;
    @Resource(name = "delProjectYgdjServiceImpl")
    DelProjectService delProjectYgdjServiceImpl;
    @Resource(name = "delProjectYydjServiceImpl")
    DelProjectService delProjectYydjServiceImpl;
    @Resource(name = "delProjectGzdjServiceImpl")
    DelProjectService delProjectGzdjServiceImpl;
    @Resource(name = "delProjectYsbzServiceImpl")
    DelProjectService delProjectYsbzServiceImpl;
    @Resource(name = "endComplexScdjAndPldyProjectServiceImpl")
    EndComplexScdjAndPldyProjectServiceImpl endComplexScdjAndPldyProjectServiceImpl;
    @Resource(name = "delProjectDyZxForZjgcServiceImpl")
    DelProjectService delProjectDyZxForZjgcServiceImpl;
    @Resource(name = "delProjectDyBgForZjgcServiceImpl")
    DelProjectService delProjectDyBgForZjgcServiceImpl;
    @Resource(name = "delProjectDyZyForZjgcServiceImpl")
    DelProjectService delProjectDyZyForZjgcServiceImpl;
    @Resource(name = "delProjectScdjAndPldyProjectServiceImpl")
    DelProjectService delProjectScdjAndPldyProjectServiceImpl;
    @Resource(name = "delProjectCxSqServiceImpl")
    DelProjectService delProjectCxSqServiceImpl;

    @Resource(name = "delProjectJddjServiceImpl")
    DelProjectService delProjectJddjServiceImpl;
    @Resource(name = "delProjectBdcDjServiceImpl")
    DelProjectService delProjectBdcDjServiceImpl;

    @Resource(name = "registerProjectCertificateServiceImpl")
    RegisterProjectService registerProjectCertificateServiceImpl;
    @Resource(name = "registerProjectNoCertificateServiceImpl")
    RegisterProjectService registerProjectNoCertificateServiceImpl;
    @Resource(name = "registerProjectZxdjServiceImpl")
    RegisterProjectService registerProjectZxdjServiceImpl;


    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcFwFsssService bdcFwFsssService;
    @Autowired
    private GdFwMapper gdFwMapper;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcJsydsyqLhxxService bdcJsydsyqLhxxService;
    @Autowired
    private BdcDjsjService bdcDjsjService;

    private static final String FW_DCB_INDEX = "fw_dcb_index";
    private static final String BDCDYH = "BDCDYH";

    @Override
    public Project creatProjectEvent(CreatProjectService creatProjectService, Xmxx xmxx) {
        //创建工作流项目
        final Project project = creatProjectService.creatWorkFlow(xmxx);
        //创建文件中心相应的节点
        creatProjectService.creatProjectNode(project.getProid());
        //根据已有信息初始化不动产项目信息
        List<InsertVo> list = creatProjectService.initVoFromOldData(xmxx);
        //保存不动产项目信息
        creatProjectService.insertProjectData(list);
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
        //更新不动产工作流项目remark和工程名称
        creatProjectService.updateWorkFlow(bdcXm);
        return project;
    }

    @Override
    public void turnProjectEvent(TurnProjectService turnProjectService, Xmxx xmxx) {
        turnProjectService.saveQllxVo((BdcXm) xmxx);
        turnProjectService.saveBdcZs((BdcXm) xmxx);
    }

    @Override
    public void turnProjectEventDbr(String userName, Xmxx xmxx) {
        QllxVo qllxVo = null;
        BdcXm bdcXm = (BdcXm) xmxx;
        qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
        if (qllxVo != null) {
            qllxVo.setDbr(userName);
            qllxVo.setDjsj(new Date());

        } else {
            QllxVo initQllx = qllxService.getQllxVoFromBdcXm(bdcXm);
            if (initQllx != null) {
                initQllx.setDbr(userName);
                initQllx.setDjsj(new Date());
                entityMapper.insertSelective(initQllx);
            }
        }
    }

    @Override
    public void endProjectEvent(EndProjectService endProjectService, BdcXm bdcXm) throws Exception {
        endProjectService.changeYqllxzt(bdcXm);
        endProjectService.changeXmzt(bdcXm);
    }

    @Override
    public void batchEndProjectEvent(EndProjectService endProjectService, List<BdcXm> bdcXmList) throws Exception {
        endProjectService.batchChangeYqllxzt(bdcXmList);
        endProjectService.batchChangeXmzt(bdcXmList);
    }

    @Override
    public void changeQlztProjectEvent(EndProjectService endProjectService, BdcXm bdcXm) throws Exception {
        endProjectService.changeYqllxzt(bdcXm);
    }

    @Override
    public void delProjectEvent(DelProjectService delProjectService, String proid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        delProjectEvent(delProjectService, bdcXm);
    }

    @Override
    public void delProjectEvent(DelProjectService delProjectService, BdcXm bdcXm) {
        String proid = bdcXm.getProid();
        delProjectService.delZsbh(proid);
        delProjectService.changeYqllxzt(bdcXm);
        bdcFwFsssService.delBdcFwfsss(proid);
        bdcJsydsyqLhxxService.delBdcJsydLhxx(proid);
        delProjectService.delBdcBdxx(bdcXm);
        delProjectService.delBdcBdcZsSd(bdcXm);
        delProjectService.delBdcXm(proid);
        delProjectService.delProjectNode(proid);
        delProjectService.delWorkFlow(proid);

    }

    @Override
    public void batchDelProjectEvent(DelProjectService delProjectService, List<BdcXm> bdcXmList, String mainProid) {
        delProjectService.batchDelBdcZsbh(bdcXmList);
        delProjectService.batchChangeYqllxzt(bdcXmList, mainProid);
        bdcFwFsssService.batchDelBdcFwfsss(bdcXmList);
        bdcJsydsyqLhxxService.batchDelBdcJsydLhxx(bdcXmList);
        delProjectService.batchDelBdcBdxx(bdcXmList);
        delProjectService.batchDelBdcXm(bdcXmList);
        delProjectService.delProjectNode(mainProid);
        delProjectService.delWorkFlow(mainProid);

    }

    @Override
    public void backProjectEvent(EndProjectService endProjectService, BdcXm bdcXm) {
        endProjectService.backXmzt(bdcXm);
        endProjectService.backYqllxzt(bdcXm);
    }

    @Override
    public String checkProject(String proid) {
        return null;
    }

    @Override
    public CreatProjectService getCreatProjectService(BdcXm bdcXm) {
        //zdd 暂时写死  后面可以考虑spring配置实现
        CreatProjectService creatProjectService = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            //判断是否有多个不动产单元
            boolean mulBdcdys = checkMulBdcdys(bdcXm);
            String sqlxdm = "";
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            //hqz 未匹配不动产单元，调用公共流程
            List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
            if (bppSqlxdmList.contains(bdcXm.getSqlx()) && !CommonUtil.indexOfStrs(Constants.SQLX_NOBDCDY_GDQL, bdcXm.getSqlx())) {
                creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CSDJ_DM) || ((StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YZX)) || StringUtils.equals(sqlxdm, Constants.SQLX_YZX))) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) || bdcXm.getQllx().equals(Constants.QLLX_DYQ))) {
                    creatProjectService = creatProjectDydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_PLFZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_GJPTSCDJ_DM))) {
                    creatProjectService = creatComplexProjectNewServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("152")) {
                    creatProjectService = creatProjectHydjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("135") || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("135"))) {
                    creatProjectService = creatComplexYzxdjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCKFSZC_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFSCKFSZC_DM))) {
                    creatProjectService = creatProjectBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFXZBG_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFXZBG_DM))) {
                    creatProjectService = creatProjectBgdjServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_GYJSYD_GZW_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_GYJSYD_GZW_DM)))) {
                    creatProjectService = createComplexScYtdDyServiceImpl;
                } else {
                    creatProjectService = creatProjectScdjService;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM) || (StringUtils.equals(sqlxdm, "218") || StringUtils.equals(bdcXm.getSqlx(), "218") || StringUtils.equals(sqlxdm, Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(sqlxdm, "219") || StringUtils.equals(bdcXm.getSqlx(), "219") || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_CLF_ZDYD))) {
                creatProjectService = creatProjectZydjService;
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("218")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("218")) || (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_YFZYDYDJ_DM)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YFZYDYDJ_DM)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CLF_ZDYD)) || (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_CLF_ZDYD))) {
                    creatProjectService = creatComplexZyDyProjectServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("219")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("219"))) {
                    creatProjectService = creatComplexZyWithDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBZY_DM))) {
                    creatProjectService = creatProjectFwFgHbZyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBZY_DM))) {
                    creatProjectService = creatProjectTdFgHbZyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_ZY_SFCD))) {
                    creatProjectService = creatProjectSfZydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_BGDJ_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_BGZY_DM)) {
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                //zdd 抵押权（地役权）需要调用单独的变更服务
                if (qllxVo instanceof BdcDyaq || qllxVo instanceof BdcDyq) {
                    creatProjectService = creatProjectDyBgdjServiceImpl;
                } else {
                    if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBHZ_DM))) {
                        creatProjectService = creatComplexFgHbHzServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_SYTTBG_DM))) {
                        creatProjectService = creatProjectSyTtBgdjServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && CommonUtil.indexOfStrs(Constants.MJHJ_SQLX, sqlxdm)) {
                        creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                    } else {
                        creatProjectService = creatProjectBgdjServiceImpl;
                    }
                    if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBBG_DM))) {
                        creatProjectService = creatProjectFwFgHbBgServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBZY_DM))) {
                        creatProjectService = creatProjectFwFgHbZyServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBBG_DM))) {
                        creatProjectService = creatProjectTdFgHbBgServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBZY_DM))) {
                        creatProjectService = creatProjectTdFgHbZyServiceImpl;
                    }
                }
                //合并流程单独提出来
                if (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                } else if (StringUtils.equals(Constants.SQLX_BGZY_DM, sqlxdm)) {
                    creatProjectService = creatProjectBgZyHbServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_GZDJ_DM)) {
                creatProjectService = creatProjectGzdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZXDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals("18") || bdcXm.getQllx().equals("17"))) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTdsyqDjNoQlServiceImpl;
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TD_ZHZX)) {
                    creatProjectService = createProjectZhzxdjServiceImpl;
                } else {
                    creatProjectService = creatProjectZxdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YGDJ_DM) || (StringUtils.equals(sqlxdm, "706") || StringUtils.equals(bdcXm.getSqlx(), "706"))) {
                //zdd 注销预告登记用注销的逻辑
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("703") || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YGDY_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFZX)) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY, sqlxdm) || StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY, bdcXm.getSqlx()) || StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY_XS, sqlxdm) || StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY_XS, bdcXm.getSqlx())) {
                    creatProjectService = creatComplexYgdjServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_YGSPFDY)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_DYPL)) {
                    creatProjectService = creatComplexYgdjPlProjectServiceImpl;
                } else if ((StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(Constants.SQLX_YG_YGSPF, bdcXm.getSqlx())) || StringUtils.equals(sqlxdm, Constants.SQLX_YG_BDCDY) || StringUtils.equals(sqlxdm, Constants.SQLX_YG_YGSPF)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYBG)) {
                    creatProjectService = creatProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BGDJ)) {
                    creatProjectService = creatProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_MMYG)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else {
                    creatProjectService = creatProjectZxdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CFDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_JF) || bdcXm.getSqlx().equals(Constants.SQLX_PLJF) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL))) {
                    if (bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL)) {
                        creatProjectService = creatProjectSfcdjfdjService;
                    } else {
                        creatProjectService = creatProjectZxTxdjServiceImpl;
                    }
                }//纯土地不匹配不动产单元，不传入原权利
                else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    if (bdcXm.getSqlx().equals("809")) {
                        creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                    } else {
                        creatProjectService = creatProjectCfdjNoQlServiceImpl;
                    }
                } else if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_HHLC)) {
                    creatProjectService = creatProjectHhLcdjServiceImpl;
                } else {
                    creatProjectService = creatProjectCfdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_DYDJ_DM)) {

                if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_HHLC)) {
                    creatProjectService = creatProjectHhLcdjServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZYDJ_SQLXDM, bdcXm.getSqlx())) {
                    //针对单独将抵押权登记
                    creatProjectService = creatProjectHmZydjService;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_BGDJ_SQLXDM, bdcXm.getSqlx())) {
                    //jyl 区分的太草率了
                    creatProjectService = creatProjectDyBgdjServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押转移单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM)) {
                    creatProjectService = createDyZyForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押变更单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
                    creatProjectService = createDyBgForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押注销单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_FW_DM)) {
                    creatProjectService = createDyZxForZjgcServiceImpl;
                }
                /**
                 * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
                 * @description 在建工程抵押权转现抵押
                 */
                else if (StringUtils.equals(Constants.SQLX_ZJJZWDY_ZX_DM, bdcXm.getSqlx())) {
                    creatProjectService = creatComplexScdjAndPldyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLDYZX)) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDY_YBZS_SQLXDM) || StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDY_DDD_SQLXDM)) {
                    creatProjectService = creatProjectDydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDYBG_YBZS_SQLXDM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYBG))) {
                    creatProjectService = creatComplexDybgdjProjectServiceImpl;
                }
                //纯土地不匹配不动产单元，不传入原权利
                else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TD_ZHJY)) {
                    creatProjectService = createProjectZhjydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYZY)) {
                    creatProjectService = creatComplexDyZydjProjectServiceImpl;
                } else {
                    creatProjectService = creatProjectDydjService;

                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YYDJ_DM)) {
                //sc 注销异议登记，走注销Service
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    creatProjectService = creatProjectZxdjServiceImpl;
                } else {
                    creatProjectService = creatProjectYydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM)) {
                if (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLZY_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLBG_DM)
                        || StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_CQDJHZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_PLHZ_DM))
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_PLYSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_OLDDM))
                    creatProjectService = creatProjectYsbzServiceImpl;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_DM))) {
                    creatProjectService = creatProjectDydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZX)) {
                    creatProjectService = creatProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZY)) {
                    creatProjectService = creatProjectZydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_BG)) {
                    creatProjectService = creatProjectDyBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLHZ)) {
                    creatProjectService = creatProjectYsbzServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CQZMD_HZ)) {
                    creatProjectService = creatProjectZydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_BDCDYDJ_DM)
                        || bdcXm.getSqlx().equals(Constants.SQLX_BDCDYBGDJ_DM)) || StringUtils.equals(Constants.SQLX_QJDCDJ_DM, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_BDCDYJD_DM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectBdcdyDjService;
                } else {
                    creatProjectService = creatProjectDefaultService;
                }
                //合并流程单独提出来
                if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_HZDY_DM)) {
                    creatProjectService = creatComplexHzDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (sqlxdm.equals(Constants.SQLX_DYHZ_DM) || sqlxdm.equals(Constants.SQLX_YSBZDY_DM))) {
                    creatProjectService = creatComplexHzWithDyProjectServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_HBDJ_DM)) {
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SC_DY))
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SC_DY))) {
                    creatProjectService = creatComplexScWithDyServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_DYHZ_DM)) ||
                        (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_DYHZ_DM)) ||
                                bdcXm.getSqlx().equals(Constants.SQLX_YSBZDY_DM))) {
                    creatProjectService = creatComplexHzWithDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DYBG_DM)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_HZDY_DM)) ||
                        (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_HZDY_DM))) {
                    creatProjectService = creatComplexHzDyProjectServiceImpl;
                } else if (bdcXm.getSqlx().equals(Constants.SQLX_SPFFTTD_ZHJY_ZHZX)) {
                    creatProjectService = creatComplexSpfZhjyZhzxProjectServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_HDDJ_RYZH) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_HDDJ_RYZH)) {
                    creatProjectService = creatProjectArbitraryServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_JSYDSYQ_LHDJ)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCDY_DM)) {
                    creatProjectService = creatComplexScDyProjectService;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_ZYQTZY_DM)) {
                    creatProjectService = CreatProjectZyAndQtzyHbServiceImpl;
                } else {
                    creatProjectService = creatProjectDefaultService;
                }
            } else {
                creatProjectService = creatProjectDefaultService;
            }
        } else {
            creatProjectService = creatProjectDefaultService;
        }
        return creatProjectService;
    }

    @Override
    public CreatProjectService getCreatProjectServiceForAddQl(BdcXm bdcXm) {
        //zdd 暂时写死  后面可以考虑spring配置实现
        CreatProjectService creatProjectService = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            //判断是否有多个不动产单元
            boolean mulBdcdys = false;
            String sqlxdm = "";
            //hqz 未匹配不动产单元，调用公共流程
            List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
            if (bppSqlxdmList.contains(bdcXm.getSqlx())) {
                creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CSDJ_DM) || ((StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("135")) || StringUtils.equals(sqlxdm, "135"))) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) || bdcXm.getQllx().equals(Constants.QLLX_DYQ))) {
                    creatProjectService = creatProjectDydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_PLFZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_GJPTSCDJ_DM))) {
                    creatProjectService = creatComplexProjectNewServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("152")) {
                    creatProjectService = creatProjectHydjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("135") || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("135"))) {
                    creatProjectService = creatComplexYzxdjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCKFSZC_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFSCKFSZC_DM))) {
                    creatProjectService = creatComplexBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFXZBG_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFXZBG_DM))) {
                    creatProjectService = creatComplexBgdjServiceImpl;
                } else {
                    creatProjectService = creatProjectScdjService;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM) || (StringUtils.equals(sqlxdm, "218") || StringUtils.equals(bdcXm.getSqlx(), "218") || StringUtils.equals(sqlxdm, Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(sqlxdm, "219") || StringUtils.equals(bdcXm.getSqlx(), "219") || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_CLF_ZDYD))) {
                creatProjectService = creatProjectZydjService;
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("218")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("218")) || (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_YFZYDYDJ_DM)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YFZYDYDJ_DM)) || (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_CLF_ZDYD)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CLF_ZDYD))) {
                    creatProjectService = creatComplexZyDyProjectServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("219")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("219"))) {
                    creatProjectService = creatComplexZyWithDyProjectServiceImpl;
                }
                //zq多不动产单元批量的 分割 合并 转移统一从此逻辑处理
                if (mulBdcdys && ((StringUtils.isNotBlank(sqlxdm) && !sqlxdm.equals("218")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && !bdcXm.getSqlx().equals("218")) || (StringUtils.isNotBlank(sqlxdm) && !sqlxdm.equals(Constants.SQLX_CLF_ZDYD)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && !bdcXm.getSqlx().equals(Constants.SQLX_CLF_ZDYD)))) {
                    creatProjectService = creatComplexZyProjectServiceImpl;
                }
                if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBZY_DM))) {
                    creatProjectService = creatProjectFwFgHbZyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBZY_DM))) {
                    creatProjectService = creatProjectTdFgHbZyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_ZY_SFCD))) {
                    creatProjectService = creatProjectSfZydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_BGDJ_DM)) {
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                //zdd 抵押权（地役权）需要调用单独的变更服务
                if (qllxVo instanceof BdcDyaq || qllxVo instanceof BdcDyq) {
                    if (mulBdcdys) {
                        creatProjectService = creatProjectMulBdcdyDyBgdjServiceImpl;
                    } else {
                        creatProjectService = creatProjectDyBgdjServiceImpl;
                    }
                } else {
                    //zq多不动产单元批量的 分割 合并 转移统一从此逻辑处理
                    if (mulBdcdys) {
                        if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBHZ_DM))) {
                            creatProjectService = creatComplexFgHbHzServiceImpl;
                        } else {
                            creatProjectService = creatComplexBgdjServiceImpl;
                        }
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_SYTTBG_DM))) {
                        creatProjectService = creatProjectSyTtBgdjServiceImpl;
                    } else {
                        creatProjectService = creatProjectBgdjServiceImpl;
                    }
                    if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBBG_DM))) {
                        creatProjectService = creatProjectFwFgHbBgServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBZY_DM))) {
                        creatProjectService = creatProjectFwFgHbZyServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBBG_DM))) {
                        creatProjectService = creatProjectTdFgHbBgServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBZY_DM))) {
                        creatProjectService = creatProjectTdFgHbZyServiceImpl;
                    }
                }
                //合并流程单独提出来
                if (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_GZDJ_DM)) {
                creatProjectService = creatProjectGzdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZXDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals("18") || bdcXm.getQllx().equals("17"))) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTdsyqDjNoQlServiceImpl;
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TD_ZHZX)) {
                    creatProjectService = createProjectZhzxdjServiceImpl;
                } else {
                    creatProjectService = creatProjectZxdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YGDJ_DM) || (StringUtils.equals(sqlxdm, "706") || StringUtils.equals(bdcXm.getSqlx(), "706"))) {
                //zdd 注销预告登记用注销的逻辑
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("703") || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YGDY_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFZX)) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("706")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("706"))) {
                    creatProjectService = creatComplexYgdjServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_YGSPFDY)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_DYPL)) {
                    creatProjectService = creatComplexYgdjPlProjectServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_BDCDY) || StringUtils.equals(sqlxdm, Constants.SQLX_YG_YGSPF)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYBG)) {
                    creatProjectService = creatProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BGDJ)) {
                    creatProjectService = creatProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_MMYG)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else {
                    creatProjectService = creatProjectZxdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CFDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_JF) || bdcXm.getSqlx().equals(Constants.SQLX_PLJF) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL))) {
                    if (!mulBdcdys) {
                        if (bdcXm.getSqlx().equals(Constants.SQLX_SFCD)) {
                            creatProjectService = creatProjectSfcdjfdjService;
                        } else {
                            creatProjectService = creatProjectZxTxdjServiceImpl;
                        }
                    } else {
                        creatProjectService = creatComplexCfProjectServiceImpl;
                    }
                }//纯土地不匹配不动产单元，不传入原权利
                else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    if (bdcXm.getSqlx().equals("809")) {
                        creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                    } else {
                        creatProjectService = creatProjectCfdjNoQlServiceImpl;
                    }

                } else if (mulBdcdys) {
                    creatProjectService = creatComplexCfProjectServiceImpl;
                } else {
                    creatProjectService = creatProjectCfdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_DYDJ_DM)) {
                //针对单独将抵押权登记
                if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZYDJ_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectHmZydjService;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_BGDJ_SQLXDM, bdcXm.getSqlx())) {
                    if (mulBdcdys) {
                        creatProjectService = creatComplexDybgdjProjectServiceImpl;
                    } else {
                        creatProjectService = creatProjectDyBgdjServiceImpl;
                    }
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押转移单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM)) {
                    creatProjectService = createDyZyForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押变更单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
                    creatProjectService = createDyBgForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押注销单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_FW_DM)) {
                    creatProjectService = createDyZxForZjgcServiceImpl;
                }
                /**
                 * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
                 * @description 在建工程抵押权转现抵押
                 */
                else if (StringUtils.equals(Constants.SQLX_ZJJZWDY_ZX_DM, bdcXm.getSqlx())) {
                    creatProjectService = creatComplexScdjAndPldyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLDYZX)) {
                    creatProjectService = creatComplexZxTxProjectServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.DJLX_PLDY_YBZS_SQLXDM) || bdcXm.getSqlx().equals(Constants.DJLX_PLDY_DDD_SQLXDM)) {
                    creatProjectService = creatComplexDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDYBG_YBZS_SQLXDM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYBG))) {
                    creatProjectService = creatComplexDybgdjProjectServiceImpl;
                }
                //纯土地不匹配不动产单元，不传入原权利
                else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TD_ZHJY)) {
                    creatProjectService = createProjectZhjydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYZY)) {
                    creatProjectService = creatComplexDyZydjProjectServiceImpl;
                } else {
                    if (mulBdcdys) {
                        /**
                         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                         * @description 在建工程新建选择逻辑撞逻辑和商品房首次登记一样
                         */
                        if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZWDY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZWDY_FW_DM)) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZWDY_DDD_FW_DM)) {
                            creatProjectService = creatComplexProjectServiceImpl;
                        } else {
                            creatProjectService = creatComplexDydjProjectServiceImpl;
                        }
                    } else {
                        creatProjectService = creatProjectDydjService;
                    }
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YYDJ_DM)) {
                //sc 注销异议登记，走注销Service
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    creatProjectService = creatProjectZxdjServiceImpl;
                } else {
                    creatProjectService = creatProjectYydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM)) {
                if (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLZY_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLBG_DM)
                        || StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_CQDJHZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_PLHZ_DM))
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_PLYSBZ_DM))
                    creatProjectService = creatProjectYsbzServiceImpl;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_DM))) {
                    creatProjectService = creatProjectDydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZX)) {
                    creatProjectService = creatProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZY)) {
                    creatProjectService = creatProjectZydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_BG)) {
                    creatProjectService = creatProjectDyBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLHZ)) {
                    if (mulBdcdys) {
                        creatProjectService = creatComplexYsbzProjectServiceImpl;
                    }
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CQZMD_HZ)) {
                    if (mulBdcdys) {
                        creatProjectService = creatComplexZyProjectServiceImpl;
                    } else {
                        creatProjectService = creatProjectZydjService;
                    }
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_BDCDYDJ_DM)
                        || bdcXm.getSqlx().equals(Constants.SQLX_BDCDYBGDJ_DM)) || bdcXm.getSqlx().equals(Constants.SQLX_QJDCDJ_DM) || StringUtils.equals(Constants.SQLX_BDCDYJD_DM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectBdcdyDjService;
                } else {
                    creatProjectService = creatProjectDefaultService;
                }
                //合并流程单独提出来
                if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_HZDY_DM)) {
                    creatProjectService = creatComplexHzDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (sqlxdm.equals(Constants.SQLX_DYHZ_DM) || sqlxdm.equals(Constants.SQLX_YSBZDY_DM))) {
                    creatProjectService = creatComplexHzWithDyProjectServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_HBDJ_DM)) {
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SC_DY))
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SC_DY))) {
                    creatProjectService = creatComplexScWithDyServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_DYHZ_DM)) ||
                        (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_DYHZ_DM)) ||
                                bdcXm.getSqlx().equals(Constants.SQLX_YSBZDY_DM))) {
                    creatProjectService = creatComplexHzWithDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DYBG_DM)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_HZDY_DM)) ||
                        (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_HZDY_DM))) {
                    creatProjectService = creatComplexHzDyProjectServiceImpl;
                } else if (bdcXm.getSqlx().equals(Constants.SQLX_SPFFTTD_ZHJY_ZHZX)) {
                    creatProjectService = creatComplexSpfZhjyZhzxProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_JSYDSYQ_LHDJ)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                } else {
                    creatProjectService = creatProjectDefaultService;
                }
            } else {
                creatProjectService = creatProjectDefaultService;
            }
        } else {
            creatProjectService = creatProjectDefaultService;
        }
        return creatProjectService;
    }

    @Override
    public TurnProjectService getTurnProjectService(BdcXm bdcXm) {

        TurnProjectService turnProjectService = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            //判断是否有多个不动产单元
            boolean mulBdcdys = checkMulBdcdys(bdcXm);
            String sqlxdm = "";
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }

            if (bdcXm.getDjlx().equals(Constants.DJLX_CSDJ_DM) || (bdcXm.getSqlx().equals("135") || StringUtils.equals(sqlxdm, "135"))) {
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("135")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("135"))) {
                    turnProjectService = turnComplexYzxdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) || bdcXm.getQllx().equals(Constants.QLLX_DYQ))) {
                    turnProjectService = turnProjectDydjServiceImpl;
                    /**
                     *@author <a herf="mailto:lichaolong@gtmap.cn">lichaolong</a>
                     *@description 判断申请类型，商品房及业主共有部分首次登记,规范代码
                     */
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_SPFGYSCDJ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_GJPTSCDJ_DM))) {
                    turnProjectService = turnComplexScdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFGYSCDJ_CYBZ_DM) ) {
                    turnProjectService = turnComplexScdjCybzProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCKFSZC_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFSCKFSZC_DM))) {
                    turnProjectService = turnComplexScdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFXZBG_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFXZBG_DM))) {
                    turnProjectService = turnComplexScdjProjectServiceImpl;
                } else if (((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_GYJSYD_GZW_DM))
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_GYJSYD_GZW_DM)))) {
                    turnProjectService = turnComplexScYtdDyProjectServiceImpl;
                } else {
                    turnProjectService = turnProjectDefaultService;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_BGDJ_DM)) {
                if (mulBdcdys) {
                    turnProjectService = turnComplexBgdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_SYTTBG_DM))) {
                    turnProjectService = turnProjectSyTtBgdjServiceImpl;
                } else {
                    turnProjectService = turnProjectBgdjServiceImpl;
                }
                if (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) {
                    turnProjectService = turnComplexHzDyProjectServiceImpl;
                }
                if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBHZ_DM))) {
                    turnProjectService = turnComplexFgHbHzProjectServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM) || (StringUtils.equals(sqlxdm, "218") || StringUtils.equals(bdcXm.getSqlx(), "218") || StringUtils.equals(sqlxdm, Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(sqlxdm, "219") || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_CLF_ZDYD) || StringUtils.equals(Constants.SQLX_BGZY_DM, sqlxdm) || StringUtils.equals(Constants.SQLX_ZYQTZY_DM, sqlxdm))) {
                if (mulBdcdys) {
                    turnProjectService = turnComplexZydjProjectServiceImpl;
                } else {
                    turnProjectService = turnProjectZydjServiceImpl;
                }
                if (StringUtils.isNotBlank(sqlxdm) && (sqlxdm.equals("218") || sqlxdm.equals(Constants.SQLX_YFZYDYDJ_DM) || sqlxdm.equals("219") || sqlxdm.equals(Constants.SQLX_CLF_ZDYD))) {
                    turnProjectService = turnComplexZyDyProjectServiceImpl;
                }
                if (StringUtils.equals(Constants.SQLX_BGZY_DM, sqlxdm) || StringUtils.equals(Constants.SQLX_ZYQTZY_DM, sqlxdm)) {
                    turnProjectService = turnComplexBgZyProjectServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_GZDJ_DM)) {
                turnProjectService = turnProjectGzdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZXDJ_DM)) {
                turnProjectService = turnProjectZxdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YGDJ_DM)) {
                //zdd 注销预告登记用注销的逻辑
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("703") || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YGDY_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFZX)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else if (StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY, sqlxdm) || StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY, bdcXm.getSqlx()) || StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY_XS, sqlxdm) || StringUtils.equalsIgnoreCase(Constants.SQLX_YG_DY_XS, bdcXm.getSqlx())) {
                    turnProjectService = turnComplexYgdjProjectServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_DYPL)) {
                    turnProjectService = turnComplexYgdjPlProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YG_BGDJ)) {
                    turnProjectService = turnProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YG_YGDYBG)) {
                    turnProjectService = turnProjectYgBgdjService;
                } else {
                    turnProjectService = turnProjectYgdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CFDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_JF) || bdcXm.getSqlx().equals(Constants.SQLX_PLJF) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL) || bdcXm.getSqlx().equals(Constants.SQLX_FWJF_DM) || bdcXm.getSqlx().equals(Constants.SQLX_TDJF_DM))) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else {
                    turnProjectService = turnProjectCfdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_DYDJ_DM)) {
                //针对单独将抵押权登记
                if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZYDJ_SQLXDM, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.DJLX_DY_BGDJ_SQLXDM, bdcXm.getSqlx())) {
                    if (mulBdcdys) {
                        turnProjectService = turnComplexDyBgServiceImpl;
                    } else {
                        turnProjectService = turnProjectDyBgdjServiceImpl;
                    }

                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押转移单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM)) {
                    turnProjectService = turnProjectDyZyForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押变更单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
                    turnProjectService = turnProjectDyBgForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押注销单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_FW_DM)) {
                    turnProjectService = turnProjectDyZxForZjgcServiceImpl;
                }
                /**
                 * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
                 * @description 在建工程抵押权转现抵押
                 */
                else if (StringUtils.equals(Constants.SQLX_ZJJZWDY_ZX_DM, bdcXm.getSqlx())) {
                    turnProjectService = turnComplexScdjAndPldyProjectServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx())) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else if (Constants.DJLX_PLDY_YBZS_SQLXDM.equals(bdcXm.getSqlx()) || Constants.DJLX_PLDYBG_YBZS_SQLXDM.equals(bdcXm.getSqlx()) || Constants.SQLX_LQ_PLDY_DM.equals(bdcXm.getSqlx())) {
                    turnProjectService = turnComplexSigleDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && CommonUtil.indexOfStrs(Constants.SQLX_ZHDK, bdcXm.getSqlx())) {
                    turnProjectService = turnProjectZhDydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.DJLX_PLDY_DDD_SQLXDM)) {
                    turnProjectService = turnComplexDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_ZJJZWDY_DDD_FW_DM)) {
                    turnProjectService = turnComplexDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLDYZX)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else {
                    if (mulBdcdys) {
                        turnProjectService = turnComplexSigleDydjProjectServiceImpl;
                    } else {
                        turnProjectService = turnProjectDydjServiceImpl;
                    }
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YYDJ_DM)) {
                //sc 注销异议登记，走注销Service
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else {
                    turnProjectService = turnProjectYydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM) && StringUtils.isNotBlank(sqlxdm) &&
                    StringUtils.equals(sqlxdm, Constants.SQLX_HZDY_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_DYHZ_DM)
                    || StringUtils.equals(sqlxdm, Constants.SQLX_SC_DY) || StringUtils.equals(sqlxdm, Constants.SQLX_YSBZDY_DM)) {
                turnProjectService = turnComplexHzDyProjectServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM)) {
                if (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLZY_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLBG_DM)
                        || StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_CQDJHZ_DM))
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_OLDDM))
                    turnProjectService = turnProjectGzdjServiceImpl;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_DM)))
                    turnProjectService = turnProjectDydjServiceImpl;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) &&
                        ((StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_BG)) || StringUtils.endsWithIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_ZY))) {
                    turnProjectService = turnProjectDyBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZX)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_BDCDYDJ_DM) || StringUtils.equals(Constants.SQLX_BDCDYJD_DM, bdcXm.getSqlx()))) {
                    turnProjectService = turnProjectNewDjDjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_PLHZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_PLYSBZ_DM))) {
                    turnProjectService = turnComplexSigleDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLHZ)) {
                    if (mulBdcdys)
                        turnProjectService = turnComplexYsbzProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CQZMD_HZ)) {
                    if (mulBdcdys) {
                        turnProjectService = turnComplexZydjProjectServiceImpl;
                    } else {
                        turnProjectService = turnProjectZydjServiceImpl;
                    }
                } else {
                    turnProjectService = turnProjectDefaultService;
                }
            } else if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_HBDJ_DM)) {
                if ((StringUtils.equals(sqlxdm, Constants.SQLX_HZDY_DM) || bdcXm.getSqlx().equals(Constants.SQLX_HZDY_DM) ||
                        StringUtils.equals(sqlxdm, Constants.SQLX_DYHZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_DYHZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_YSBZDY_DM))) {
                    turnProjectService = turnComplexHzDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_JSYDSYQ_LHDJ)) {
                    turnProjectService = turnProjectJsydsyqLhdjService;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCDY_DM)) {
                    turnProjectService = turnComplexZyDyProjectServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) {
                    turnProjectService = turnProjectBgdjWithDybgServiceImpl;
                } else {
                    turnProjectService = turnProjectDefaultService;
                }
            } else {
                turnProjectService = turnProjectDefaultService;
            }
        } else {
            turnProjectService = turnProjectDefaultService;
        }
        return turnProjectService;
    }

    @Override
    public TurnProjectService getTurnProjectServiceForAddQl(BdcXm bdcXm) {

        TurnProjectService turnProjectService = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            //判断是否有多个不动产单元
            boolean mulBdcdys = false;
            String sqlxdm = "";
            if (bdcXm.getDjlx().equals(Constants.DJLX_CSDJ_DM) || (bdcXm.getSqlx().equals("135") || StringUtils.equals(sqlxdm, "135"))) {
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("135")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("135"))) {
                    turnProjectService = turnComplexYzxdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) || bdcXm.getQllx().equals(Constants.QLLX_DYQ))) {
                    turnProjectService = turnProjectDydjServiceImpl;
                    /**
                     *@author <a herf="mailto:lichaolong@gtmap.cn">lichaolong</a>
                     *@description 判断申请类型，商品房及业主共有部分首次登记,规范代码
                     */
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_SPFGYSCDJ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_GJPTSCDJ_DM))) {
                    turnProjectService = turnComplexScdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCKFSZC_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFSCKFSZC_DM))) {
                    turnProjectService = turnComplexScdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFXZBG_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFXZBG_DM))) {
                    turnProjectService = turnComplexScdjProjectServiceImpl;
                } else {
                    turnProjectService = turnProjectDefaultService;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_BGDJ_DM)) {
                if (mulBdcdys) {
                    turnProjectService = turnComplexBgdjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_SYTTBG_DM))) {
                    turnProjectService = turnProjectSyTtBgdjServiceImpl;
                } else {
                    turnProjectService = turnProjectBgdjServiceImpl;
                }
                if (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) {
                    turnProjectService = turnComplexHzDyProjectServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM) || (StringUtils.equals(sqlxdm, "218") || StringUtils.equals(bdcXm.getSqlx(), "218") || StringUtils.equals(sqlxdm, Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(sqlxdm, "219") || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_CLF_ZDYD))) {
                if (mulBdcdys) {
                    turnProjectService = turnComplexZydjProjectServiceImpl;
                } else {
                    turnProjectService = turnProjectZydjServiceImpl;
                }
                if (StringUtils.isNotBlank(sqlxdm) && (sqlxdm.equals("218") || sqlxdm.equals(Constants.SQLX_YFZYDYDJ_DM) || sqlxdm.equals("219") || sqlxdm.equals(Constants.SQLX_CLF_ZDYD))) {
                    turnProjectService = turnComplexZyDyProjectServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_GZDJ_DM)) {
                turnProjectService = turnProjectGzdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZXDJ_DM)) {
                turnProjectService = turnProjectZxdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YGDJ_DM)) {
                //zdd 注销预告登记用注销的逻辑
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("703") || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YGDY_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFZX)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("706")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("706"))) {
                    turnProjectService = turnComplexYgdjProjectServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_DYPL)) {
                    turnProjectService = turnComplexYgdjPlProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YG_BGDJ)) {
                    turnProjectService = turnProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YG_YGDYBG)) {
                    turnProjectService = turnProjectYgBgdjService;
                } else {
                    turnProjectService = turnProjectYgdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CFDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_JF) || bdcXm.getSqlx().equals(Constants.SQLX_PLJF) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL))) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else {
                    turnProjectService = turnProjectCfdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_DYDJ_DM)) {
                //针对单独将抵押权登记
                if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZYDJ_SQLXDM, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.DJLX_DY_BGDJ_SQLXDM, bdcXm.getSqlx())) {
                    if (mulBdcdys) {
                        turnProjectService = turnComplexDyBgServiceImpl;
                    } else {
                        turnProjectService = turnProjectDyBgdjServiceImpl;
                    }
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押转移单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM)) {
                    turnProjectService = turnProjectDyZyForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押变更单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
                    turnProjectService = turnProjectDyBgForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押注销单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_FW_DM)) {
                    turnProjectService = turnProjectDyZxForZjgcServiceImpl;
                }
                /**
                 * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
                 * @description 在建工程抵押权转现抵押
                 */
                else if (StringUtils.equals(Constants.SQLX_ZJJZWDY_ZX_DM, bdcXm.getSqlx())) {
                    turnProjectService = turnComplexScdjAndPldyProjectServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx())) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else if (Constants.DJLX_PLDY_YBZS_SQLXDM.equals(bdcXm.getSqlx()) || Constants.DJLX_PLDYBG_YBZS_SQLXDM.equals(bdcXm.getSqlx())) {
                    turnProjectService = turnComplexSigleDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && CommonUtil.indexOfStrs(Constants.SQLX_ZHDK, bdcXm.getSqlx())) {
                    turnProjectService = turnProjectZhDydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.DJLX_PLDY_DDD_SQLXDM)) {
                    turnProjectService = turnComplexDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_ZJJZWDY_DDD_FW_DM)) {
                    turnProjectService = turnComplexDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLDYZX)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else {
                    if (mulBdcdys) {
                        turnProjectService = turnComplexSigleDydjProjectServiceImpl;
                    } else {
                        turnProjectService = turnProjectDydjServiceImpl;
                    }
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YYDJ_DM)) {
                //sc 注销异议登记，走注销Service
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else {
                    turnProjectService = turnProjectYydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM) && StringUtils.isNotBlank(sqlxdm) &&
                    StringUtils.equals(sqlxdm, Constants.SQLX_HZDY_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_DYHZ_DM)
                    || StringUtils.equals(sqlxdm, Constants.SQLX_SC_DY) || StringUtils.equals(sqlxdm, Constants.SQLX_YSBZDY_DM)) {
                turnProjectService = turnComplexHzDyProjectServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM)) {
                if (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLZY_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLBG_DM)
                        || StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_CQDJHZ_DM)))
                    turnProjectService = turnProjectGzdjServiceImpl;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_DM))) {
                    turnProjectService = turnProjectDydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) &&
                        ((StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_BG)) || StringUtils.endsWithIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_ZY))) {
                    turnProjectService = turnProjectDyBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZX)) {
                    turnProjectService = turnProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_PLHZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_PLYSBZ_DM))) {
                    turnProjectService = turnComplexSigleDydjProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLHZ)) {
                    if (mulBdcdys)
                        turnProjectService = turnComplexYsbzProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CQZMD_HZ)) {
                    if (mulBdcdys) {
                        turnProjectService = turnComplexZydjProjectServiceImpl;
                    } else {
                        turnProjectService = turnProjectZydjServiceImpl;
                    }
                } else {
                    turnProjectService = turnProjectDefaultService;
                }

            } else if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_HBDJ_DM) && (StringUtils.equals(sqlxdm, Constants.SQLX_HZDY_DM) || bdcXm.getSqlx().equals(Constants.SQLX_HZDY_DM) ||
                    StringUtils.equals(sqlxdm, Constants.SQLX_DYHZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_DYHZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_YSBZDY_DM))) {
                turnProjectService = turnComplexHzDyProjectServiceImpl;
            } else {
                turnProjectService = turnProjectDefaultService;
            }
        } else {
            turnProjectService = turnProjectDefaultService;
        }
        return turnProjectService;
    }

    @Override
    public TurnProjectService getTurnProjectServiceArbitrary() {
        return turnProjectArbitraryServiceImpl;
    }

    @Override
    public EndProjectService getEndProjectService(BdcXm bdcXm) {
        EndProjectService endProjectService = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            String sqlxdm = "";
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            if (bdcXm.getDjlx().equals(Constants.DJLX_CSDJ_DM) || (bdcXm.getSqlx().equals("135") || StringUtils.equals(sqlxdm, "135"))) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) || bdcXm.getQllx().equals(Constants.QLLX_DYQ)) && !(bdcXm.getSqlx().equals("135") || StringUtils.equals(sqlxdm, "135"))) {
                    endProjectService = StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GYJSYD_GZW_DM) ? endComplexHzDyProjectServiceImpl : endProjectDydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_PLFZ_DM) || bdcXm.getSqlx().equals(Constants.SQLX_GJPTSCDJ_DM))) {
                    endProjectService = endComplexProjectServiceImpl;
                } else if ((bdcXm.getSqlx().equals("135") || StringUtils.equals(sqlxdm, "135"))) {
                    endProjectService = endComplexYzxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFSCKFSZC_DM)) {
                    endProjectService = endProjectZydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFXZBG_DM)) {
                    endProjectService = endProjectZydjServiceImpl;
                } else {
                    endProjectService = endProjectDefaultServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_BGZY_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_ZYQTZY_DM)) {
                endProjectService = endProjectZydjServiceImpl;
                if (StringUtils.isNotBlank(sqlxdm) && (sqlxdm.equals("218") || sqlxdm.equals(Constants.SQLX_YFZYDYDJ_DM) || sqlxdm.equals(Constants.SQLX_CLF_ZDYD))) {
                    endProjectService = endComplexZyDyProjectServiceImpl;
                }
                if (sqlxdm.equals("219")) {
                    endProjectService = endProjectZyDybgServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_BGDJ_DM)) {
                //变更登记办结与转移登记逻辑一致
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GYJSYDHB_BGDJ)) {
                    endProjectService = endProjectZxdjServiceImpl;
                } else {
                    endProjectService = endProjectZydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_GZDJ_DM)) {
                endProjectService = endProjectGzdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZXDJ_DM)) {
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TD_ZHZX)) {
                    endProjectService = endProjectZhzxdjServiceImpl;
                } else {
                    endProjectService = endProjectZxdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YGDJ_DM)) {
                //zdd 注销预告登记用注销的逻辑
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("703") || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YGDY_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BGDJ) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYBG)) {
                    endProjectService = endProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("706") || StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("706")) {
                    endProjectService = endComplexYgdjServiceImpl;
                } else {
                    endProjectService = endProjectYgdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CFDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals("803")
                        || bdcXm.getSqlx().equals("807") || bdcXm.getSqlx().equals("809"))) {
                    endProjectService = endProjectJfdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL)
                        || bdcXm.getSqlx().equals(Constants.SQLX_ZX_SFCD))) {
                    endProjectService = endProjectSfcdjfdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_CF_GDCF))) {
                    endProjectService = endProjectZydjServiceImpl;
                } else {
                    endProjectService = endProjectCfdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_DYDJ_DM)) {
                //针对单独将抵押权登记
                if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZYDJ_SQLXDM, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.DJLX_DY_BGDJ_SQLXDM, bdcXm.getSqlx())) {
                    endProjectService = endProjectZydjServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押转移单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM)) {
                    endProjectService = endProjectDyZyForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押变更单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
                    endProjectService = endProjectDyBgForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押注销单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_FW_DM)) {
                    endProjectService = endProjectDyZxForZjgcServiceImpl;
                } else if (StringUtils.equals(Constants.SQLX_ZJJZWDY_ZX_DM, bdcXm.getSqlx())) {
                    endProjectService = endComplexScdjAndPldyProjectServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx())) {
                    endProjectService = endProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.DJLX_PLDY_YBZS_SQLXDM) || bdcXm.getSqlx().equals(Constants.SQLX_ZHDK_DYSC) || bdcXm.getSqlx().equals(Constants.SQLX_LQ_PLDY_DM))) {
                    endProjectService = endComplexProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.DJLX_PLDYBG_YBZS_SQLXDM) || CommonUtil.indexOfStrs(Constants.SQLX_ZHDK_ZX, bdcXm.getSqlx()))) {
                    endProjectService = endComplexDybgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_TD_ZHJY)) {
                    endProjectService = endProjectZhjydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLDYZX)) {
                    endProjectService = endProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_ZJJZWDYZXFDY_DM)) {
                    endProjectService = endProjectZjdyZxfdydjServiceImpl;
                } else {
                    endProjectService = endProjectDydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YYDJ_DM)) {
                //sc 注销异议登记，走注销Service
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    endProjectService = endProjectZxdjServiceImpl;
                } else {
                    endProjectService = endProjectYydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM) && StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)) {
                endProjectService = endComplexHzDyProjectServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM)) {
                if (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLZY_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLBG_DM)
                        || StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_CQDJHZ_DM))
                        || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YSBZ_OLDDM))
                    endProjectService = endProjectYsbzServiceImpl;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_DM))) {
                    endProjectService = endProjectDydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZX)) {
                    endProjectService = endProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CQZMD_HZ)) {
                    endProjectService = endProjectZydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(Constants.SQLX_BDCDYDJ_DM, bdcXm.getSqlx()) || StringUtils.equals(Constants.SQLX_BDCDYJD_DM, bdcXm.getSqlx()))) {
                    endProjectService = endProjectBdcDjServiceImpl;
                } else {
                    endProjectService = endProjectDefaultServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_HBDJ_DM)) {
                if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_JSYDSYQ_LHDJ)) {
                    endProjectService = endComplexZyDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCDY_DM)) {
                    endProjectService = endComplexScDyProjectServiceImpl;
                } else {
                    endProjectService = endComplexHzDyProjectServiceImpl;
                }
            } else {
                endProjectService = endProjectDefaultServiceImpl;
            }
            if (StringUtils.isNotBlank(sqlxdm) && (sqlxdm.equals("218") || sqlxdm.equals(Constants.SQLX_YFZYDYDJ_DM) || sqlxdm.equals(Constants.SQLX_CLF_ZDYD) || sqlxdm.equals(Constants.SQLX_HZDY_DM))) { //合并流程会修改子流程的申请类型，所以单独考虑
                endProjectService = endComplexZyDyProjectServiceImpl;
            }
            if (sqlxdm.equals("219")) {
                endProjectService = endProjectZyDybgServiceImpl;
            }
            List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
            if (bppSqlxdmList.contains(sqlxdm)) {
                endProjectService = endProjectWppZxdjServiceImpl;
            }
            if (sqlxdm.equals(Constants.SQLX_SC_DY)) {
                endProjectService = endComplexHzDyProjectServiceImpl;
            }
            if (sqlxdm.equals(Constants.SQLX_SPFSCDY_DM)) {
                endProjectService = endComplexScDyProjectServiceImpl;
            }
            if (CommonUtil.indexOfStrs(Constants.SQLX_NOBDCDY_GDQL, sqlxdm)) {
                endProjectService = endProjectCfdjServiceImpl;
            }
            if (sqlxdm.equals(Constants.SQLX_DYBG_DM) || sqlxdm.equals(Constants.SQLX_DYHZ_DM)
                    || sqlxdm.equals(Constants.SQLX_FWDYBG_DM) || sqlxdm.equals(Constants.SQLX_GYJSDYBG_DM)) {
                endProjectService = endProjectZyDybgServiceImpl;
            }
        } else {
            endProjectService = endProjectDefaultServiceImpl;
        }
        return endProjectService;
    }

    @Override
    public DelProjectService getDelProjectService(BdcXm bdcXm) {
        DelProjectService delProjectService = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            if (bdcXm.getDjlx().equals(Constants.DJLX_CSDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) || bdcXm.getQllx().equals(Constants.QLLX_DYQ))) {
                    delProjectService = delProjectDydjServiceImpl;
                } else {
                    delProjectService = delProjectScdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM)) {
                delProjectService = delProjectZydjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_BGDJ_DM)) {
                //zdd 删除逻辑与转移登记一致
                delProjectService = delProjectZydjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_GZDJ_DM)) {
                delProjectService = delProjectGzdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZXDJ_DM)) {
                delProjectService = delProjectZxdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YGDJ_DM)) {
                //zdd 注销预告登记用注销的逻辑
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("703") || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFZX)) {
                    delProjectService = delProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BGDJ) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYBG))) {
                    delProjectService = delProjectZydjServiceImpl;
                } else {
                    delProjectService = delProjectYgdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CFDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (CommonUtil.indexOfStrs(Constants.SQLX_ZXCFDJ_DM, bdcXm.getSqlx()))) {
                    delProjectService = delProjectZxdjServiceImpl;
                } else {
                    delProjectService = delProjectCfdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_DYDJ_DM)) {
                //针对单独将抵押权登记
                if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZYDJ_SQLXDM, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.DJLX_DY_BGDJ_SQLXDM, bdcXm.getSqlx()) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYBG) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYZY))
                    delProjectService = delProjectZydjServiceImpl;

                /**
                 * @author bianwen
                 * @description 在建建筑物抵押转移单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM)) {
                    delProjectService = delProjectDyZyForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押变更单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
                    delProjectService = delProjectDyBgForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押注销单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_FW_DM)) {
                    delProjectService = delProjectDyZxForZjgcServiceImpl;
                } else if (StringUtils.equals(Constants.SQLX_ZJJZWDY_ZX_DM, bdcXm.getSqlx())) {
                    delProjectService = delProjectScdjAndPldyProjectServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx())) {
                    delProjectService = delProjectZxdjServiceImpl;
                } else {
                    delProjectService = delProjectDydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YYDJ_DM)) {
                //sc 注销异议登记，走注销Service
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    delProjectService = delProjectZxdjServiceImpl;
                } else {
                    delProjectService = delProjectYydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_CQDJHZ_DM))
                        || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YSBZ_OLDDM)) {
                    delProjectService = delProjectYsbzServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_DM))) {
                    delProjectService = delProjectDydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZX)) {
                    delProjectService = delProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_CQZMD_HZ) || bdcXm.getSqlx().equals(Constants.SQLX_DY_ZY) || bdcXm.getSqlx().equals(Constants.SQLX_DY_BG))) {
                    delProjectService = delProjectZydjServiceImpl;
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_BDC_CXSQ)) {
                    delProjectService = delProjectCxSqServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(Constants.SQLX_BDCDYDJ_DM, bdcXm.getSqlx())) {
                    delProjectService = delProjectBdcDjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(Constants.SQLX_BDCDYJD_DM, bdcXm.getSqlx())) {
                    delProjectService = delProjectJddjServiceImpl;
                } else {
                    delProjectService = delProjectDefaultServiceImpl;
                }

            } else {
                delProjectService = delProjectDefaultServiceImpl;
            }
        } else {
            delProjectService = delProjectDefaultServiceImpl;
        }
        return delProjectService;
    }

    @Override
    public void creatZs(TurnProjectService turnProjectService, Xmxx xmxx) {
        turnProjectService.saveBdcZs((BdcXm) xmxx);
    }

    @Override
    public Project turnWfActivity(TurnProjectService turnProjectService, Project project) {
        return turnProjectService.turnWfActivity(project);
    }

    @Override
    public Project initGdDataToBdcXmRel(Project project, String gdproids, String qlids) {
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        List<BdcXmRel> newBbdcXmRelList = new ArrayList<BdcXmRel>();
        List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
        boolean hasppbdcdy = true;
        if (StringUtils.isNotBlank(qlids)) {
            String[] qlidsArr = qlids.split(",");
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 通过qlid得到所有的房屋id
             */
            for (String qlid : qlidsArr) {
                List<GdBdcQlRel> gdBdcQlRelListtemp = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                if (CollectionUtils.isNotEmpty(gdBdcQlRelListtemp)) {
                    gdBdcQlRelList.addAll(gdBdcQlRelListtemp);
                }
            }
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 根据有多少房屋，组织参数，通过fwid查询匹配关系，暂不考虑纯土地
             */
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                    HashMap map = new HashMap();
                    map.put("qlid", gdBdcQlRel.getQlid());
                    List<GdFwsyq> gdFwsyqtempList = gdFwService.andEqualQueryGdFwsyq(map);
                    List<GdTdsyq> gdTdsyqtempList = gdTdService.andEqualQueryGdTdsyq(map);
                    map.clear();
                    map.put("cfid", gdBdcQlRel.getQlid());
                    List<GdCf> gdCftempList = gdFwService.andEqualQueryGdCf(map);
                    map.clear();
                    map.put("dyid", gdBdcQlRel.getQlid());
                    List<GdDy> gdDytempList = gdFwService.andEqualQueryGdDy(map);
                    bdcXmRel.setYdjxmly(Constants.SJLY_GDFW_XMLY);
                    if (CollectionUtils.isNotEmpty(gdDyhRelList) && !StringUtils.equals(project.getDjlx(), Constants.DJLX_HBDJ_DM) && !CommonUtil.indexOfStrs(Constants.SQLX_FGHB_DM, project.getSqlx())) {
                        hasppbdcdy = false;
                        bdcXmRel.setQjid(gdDyhRelList.get(0).getDjid());
                        bdcXmRel.setYqlid(gdBdcQlRel.getQlid());
                        //zq处理多个房产证，合并一个不动产单元，多权利合并问题，存入到bdcxmrel表以逗号相隔
                        String qlidstemp = bdcGdDyhRelService.getGdDyhRelQlidsByDjids(gdDyhRelList.get(0).getDjid(), qlids);
                        if (StringUtils.isNotBlank(qlidstemp)) bdcXmRel.setYqlid(qlidstemp);
                        //zq修改存入qlid，两个权利匹配一个不动产单元，合并时会存入两个不动产单元
                    } else {
                        bdcXmRel.setYqlid(gdBdcQlRel.getQlid());
                    }
                    if (CollectionUtils.isNotEmpty(gdFwsyqtempList)) {
                        bdcXmRel.setYproid(gdFwsyqtempList.get(0).getProid());
                    } else if (CollectionUtils.isNotEmpty(gdTdsyqtempList)) {
                        bdcXmRel.setYproid(gdTdsyqtempList.get(0).getProid());
                        bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
                    } else if (CollectionUtils.isNotEmpty(gdCftempList)) {
                        bdcXmRel.setYproid(gdCftempList.get(0).getProid());
                    } else if (CollectionUtils.isNotEmpty(gdDytempList)) {
                        bdcXmRel.setYproid(gdDytempList.get(0).getProid());
                    }
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 根据qlid获取proid
                     */
                    String proidsTemp = bdcGdDyhRelService.getProidsByDjids(bdcXmRel.getYqlid());
                    if (StringUtils.isNotBlank(proidsTemp)) bdcXmRel.setYproid(proidsTemp);
                    bdcXmRelList.add(bdcXmRel);
                }
            }
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 数据重组 去除重复fwid
             */
            if (!hasppbdcdy) {
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    List<String> qlidList = new ArrayList<String>();
                    for (int i = 0; i < bdcXmRelList.size(); i++) {
                        if (!qlidList.contains(bdcXmRelList.get(i).getQjid())) {
                            qlidList.add(bdcXmRelList.get(i).getQjid());
                            newBbdcXmRelList.add(bdcXmRelList.get(i));
                        }
                    }
                }
            } else {
                newBbdcXmRelList = bdcXmRelList;
            }
        }
        project.setBdcXmRelList(newBbdcXmRelList);
        return project;
    }


    @Override
    public Project initGdDataToBdcXmRel(Project project, String gdproids, String qlids, String bdclx) {
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        List<BdcXmRel> newBbdcXmRelList = new ArrayList<BdcXmRel>();
        List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
        boolean hasppbdcdy = true;
        //根据wdid获取
        String djlxdm = "";
        String sqlxdm = "";
        String qllx = "";
        if (project != null && StringUtils.isNotBlank(project.getWorkFlowDefId())) {
            List<Map> mapList = bdcXmService.getAllLxByWdid(project.getWorkFlowDefId());
            if (CollectionUtils.isNotEmpty(mapList)) {
                sqlxdm = CommonUtil.formatEmptyValue(mapList.get(0).get("SQLXDM"));
                qllx = CommonUtil.formatEmptyValue(mapList.get(0).get("QLLXDM"));
                djlxdm = CommonUtil.formatEmptyValue(mapList.get(0).get("DJLXDM"));
                project.setSqlx(sqlxdm);
                project.setQllx(qllx);
                project.setDjlx(djlxdm);
            }
        }
        if (StringUtils.isNotBlank(qlids)) {
            String[] qlidsArr = qlids.split(",");
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 通过qlid得到所有的房屋id
             */
            for (String qlid : qlidsArr) {
                List<GdBdcQlRel> gdBdcQlRelListtemp = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                if (CollectionUtils.isNotEmpty(gdBdcQlRelListtemp)) {
                    gdBdcQlRelList.addAll(gdBdcQlRelListtemp);
                }
            }
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 根据有多少房屋，组织参数，通过fwid查询匹配关系，暂不考虑纯土地
             */
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                if (StringUtils.isNotBlank(gdBdcQlRelList.get(0).getBdclx())) {
                    project.setBdclx(gdBdcQlRelList.get(0).getBdclx());
                } else {
                    project.setBdclx(bdclx);
                }
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    //jyl 控制附属设施，附属设施的房子现阶段不参与流程的生成。只做最后的附记内容。
                    GdFw gdFw = gdFwService.queryGdFw(gdBdcQlRel.getBdcid());
                    List<BdcGdDyhRel> gdDyhRelList = null;
                    String fsssFlag = "否";
                    if (gdFw != null && StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(), "1")) {
                        fsssFlag = "是";
                    } else {
                        gdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                    }
                    if (!StringUtils.equals(fsssFlag, "是")) {
                        completeGdDataToBdcXmRel(project, gdBdcQlRel, bdcXmRel, gdDyhRelList, djlxdm, qllx, qlidsArr, hasppbdcdy);
                        //批量选择相同不动产单元数据，组织bdcXmRel
                        //1:相同qjid；0：不同qjid
                        int flag = multiSelectSameBdcdyBdcXmRel(bdcXmRelList, bdcXmRel);
                        if (flag == ParamsConstants.NUMBER_ZERO) {
                            bdcXmRelList.add(bdcXmRel);
                        }
                    } else {
                        if (CommonUtil.indexOfStrs(Constants.DJLX_CFDJ_JF_SQLXDM, sqlxdm)) {
                            completeGdDataToBdcXmRel(project, gdBdcQlRel, bdcXmRel, gdDyhRelList, djlxdm, qllx, qlidsArr, hasppbdcdy);
                            //批量选择相同不动产单元数据，组织bdcXmRel
                            //1:相同qjid；0：不同qjid
                            int flag = multiSelectSameBdcdyBdcXmRel(bdcXmRelList, bdcXmRel);
                            if (flag == ParamsConstants.NUMBER_ZERO) {
                                bdcXmRelList.add(bdcXmRel);
                            }
                        }
                    }
                }
            }
            //重组数据
            recombineBdcXmRelDate(bdcXmRelList, newBbdcXmRelList, hasppbdcdy, sqlxdm);
        }
        if (CollectionUtils.isNotEmpty(newBbdcXmRelList)) {
            String completeBdcXmRel = AppConfig.getProperty("multiFcz.bdcxmRel.complete");
            if ((!StringUtils.equals(Constants.SQLX_YG_ZX, project.getSqlx())) && StringUtils.isNotEmpty(project.getBdcdyh()) && (StringUtils.equals("1", bdcDjsjService.getBdcdyfwlxByBdcdyh(project.getBdcdyh())) || StringUtils.equals(ParamsConstants.TRUE_LOWERCASE, completeBdcXmRel))) {
                initGdMultiPpAndDzToBdcXmRel(project, newBbdcXmRelList);
            }
            project.setBdcXmRelList(newBbdcXmRelList);
        }
        return project;
    }

    @Override
    public void initGdMultiPpAndDzToBdcXmRel(Project project, List<BdcXmRel> newBbdcXmRelList) {
        HashMap map = new HashMap();
        String gdidTemp = "";
        Example gdDyhRelExample = new Example(GdDyhRel.class);
        gdDyhRelExample.createCriteria().andEqualTo("bdcdyh", project.getBdcdyh());
        List<GdDyhRel> gdDyhRelList = entityMapper.selectByExample(gdDyhRelExample);
        List<GdDyhRel> targetList = new ArrayList<GdDyhRel>();
        if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
            for (GdDyhRel gdDyhRel : gdDyhRelList) {
                if (null != gdDyhRel) {
                    if (StringUtils.isNotBlank(newBbdcXmRelList.get(0).getYqlid())) {
                        Example gdBdcQlRelExample = new Example(GdBdcQlRel.class);
                        gdBdcQlRelExample.createCriteria().andEqualTo("qlid", newBbdcXmRelList.get(0).getYqlid());
                        List<GdBdcQlRel> gdBdcQlRelList1 = entityMapper.selectByExample(gdBdcQlRelExample);
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelList1)) {
                            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList1) {
                                if (null != gdBdcQlRel && StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                                    gdidTemp = gdBdcQlRel.getBdcid();
                                }
                            }
                        }
                    }
                    if (!StringUtils.equals(gdidTemp, gdDyhRel.getGdid())) {
                        targetList.add(gdDyhRel);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(targetList)) {
            Object obj = null;
            if (StringUtils.isNotBlank(newBbdcXmRelList.get(0).getYproid())) {
                obj = gdFwService.makeSureQllxByGdproid(newBbdcXmRelList.get(0).getYproid());
            }
            for (GdDyhRel gdDyhRel : targetList) {
                if (null != gdDyhRel) {
                    Example gdBdcQlRelExample = new Example(GdBdcQlRel.class);
                    gdBdcQlRelExample.createCriteria().andEqualTo("bdcid", gdDyhRel.getGdid());
                    List<GdBdcQlRel> gdBdcQlRelTempList = entityMapper.selectByExample(gdBdcQlRelExample);
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelTempList)) {
                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelTempList) {
                            if (obj instanceof GdFwsyq) {
                                map.put("qlid", gdBdcQlRel.getQlid());
                                map.put("iszx", 0);
                                List<GdFwsyq> gdFwsyqtempList = gdFwService.andEqualQueryGdFwsyq(map);
                                map.clear();
                                if (CollectionUtils.isNotEmpty(gdFwsyqtempList)) {
                                    GdFwsyq gdFwsyqTemp = gdFwsyqtempList.get(0);
                                    if (gdFwsyqTemp != null) {
                                        if ((StringUtils.indexOf(newBbdcXmRelList.get(0).getYproid(), gdFwsyqTemp.getProid()) == -1)) {
                                            newBbdcXmRelList.get(0).setYproid(newBbdcXmRelList.get(0).getYproid() + "," + gdFwsyqTemp.getProid());
                                        }
                                        if ((StringUtils.indexOf(newBbdcXmRelList.get(0).getYqlid(), gdFwsyqTemp.getQlid()) == -1)) {
                                            newBbdcXmRelList.get(0).setYqlid(newBbdcXmRelList.get(0).getYqlid() + "," + gdFwsyqTemp.getQlid());
                                        }
                                    }
                                }
                            } else if (obj instanceof GdDy) {
                                if (StringUtils.isNotBlank(project.getGdproid())) {
                                    newBbdcXmRelList.get(0).setYproid(project.getGdproid());
                                } else if (StringUtils.isNotBlank(project.getYxmid())) {
                                    newBbdcXmRelList.get(0).setYproid(project.getYxmid());
                                }

                                if (StringUtils.isNotBlank(project.getYqlid())) {
                                    newBbdcXmRelList.get(0).setYproid(project.getYqlid());
                                }

                            } else if (obj instanceof GdYg) {
                                map.put("ygid", gdBdcQlRel.getQlid());
                                map.put("iszx", 0);
                                List<GdYg> gdYgtempList = gdFwService.andEqualQueryGdYg(map);
                                map.clear();
                                if (CollectionUtils.isNotEmpty(gdYgtempList)) {
                                    GdYg gdYgTemp = gdYgtempList.get(0);
                                    if (gdYgTemp != null) {
                                        if ((StringUtils.indexOf(gdYgTemp.getProid(), newBbdcXmRelList.get(0).getYproid()) == -1)) {
                                            newBbdcXmRelList.get(0).setYproid(newBbdcXmRelList.get(0).getYproid() + "," + gdYgTemp.getProid());
                                        }
                                        if ((StringUtils.indexOf(gdYgTemp.getYgid(), newBbdcXmRelList.get(0).getYqlid()) == -1)) {
                                            newBbdcXmRelList.get(0).setYqlid(newBbdcXmRelList.get(0).getYqlid() + "," + gdYgTemp.getYgid());
                                        }
                                    }
                                }
                            } else if (obj instanceof GdCf) {
                                String yproid = "";
                                if (StringUtils.isNotBlank(project.getGdproid())) {
                                    yproid = project.getGdproid();
                                } else if (StringUtils.isNotBlank(project.getYxmid())) {
                                    yproid = project.getYxmid();
                                }
                                if (StringUtils.isNotBlank(project.getYqlid())) {
                                    yproid = project.getYqlid();
                                }
                                newBbdcXmRelList.get(0).setYproid(yproid);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void completeGdDataToBdcXmRel(Project project, GdBdcQlRel gdBdcQlRel, BdcXmRel bdcXmRel, List<BdcGdDyhRel> gdDyhRelList, String djlxdm, String qllx, String[] qlidsArr, boolean hasppbdcdy) {
        HashMap map = new HashMap();
        map.put("qlid", gdBdcQlRel.getQlid());
        List<GdFwsyq> gdFwsyqtempList = gdFwService.andEqualQueryGdFwsyq(map);
        List<GdTdsyq> gdTdsyqList = gdTdService.andEqualQueryGdTdsyq(map);
        map.clear();
        map.put("cfid", gdBdcQlRel.getQlid());
        List<GdCf> gdCftempList = gdFwService.andEqualQueryGdCf(map);
        map.clear();
        map.put("dyid", gdBdcQlRel.getQlid());
        List<GdDy> gdDytempList = gdFwService.andEqualQueryGdDy(map);
        map.clear();
        map.put("ygid", gdBdcQlRel.getQlid());
        List<GdYg> gdYgtempList = gdFwService.andEqualQueryGdYg(map);
        if (CollectionUtils.isNotEmpty(gdDyhRelList) && !StringUtils.equals(djlxdm, Constants.DJLX_HBDJ_DM) && (!StringUtils.equals(qllx, Constants.QLLX_DYAQ) && qlidsArr.length < 2)) {
            bdcXmRel.setQjid(gdDyhRelList.get(0).getDjid());
            //zq处理多个房产证，合并一个不动产单元，多权利合并问题，存入到bdcxmrel表以逗号相隔
            String qlidstemp = gdBdcQlRel.getQlid();
            //zq修改存入qlid，两个权利匹配一个不动产单元，合并时会存入两个不动产单元
            if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                bdcXmRel.setYdjxmly(Constants.SJLY_GDFW_XMLY);
            } else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
            }
            bdcXmRel.setYqlid(qlidstemp);
        } else {
            if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                bdcXmRel.setQjid(gdDyhRelList.get(0).getDjid());
            }
            if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                bdcXmRel.setYdjxmly(Constants.SJLY_GDFW_XMLY);
            } else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
            }
            bdcXmRel.setYqlid(gdBdcQlRel.getQlid());
        }
        if (CollectionUtils.isNotEmpty(gdFwsyqtempList)) {
            bdcXmRel.setYproid(gdFwsyqtempList.get(0).getProid());
        } else if (CollectionUtils.isNotEmpty(gdCftempList)) {
            //区分土地和房屋
            if (StringUtils.isNotBlank(gdCftempList.get(0).getBdclx()) && StringUtils.equals(gdCftempList.get(0).getBdclx(), Constants.BDCLX_TD)) {
                bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
            }
            bdcXmRel.setYproid(gdCftempList.get(0).getProid());
        } else if (CollectionUtils.isNotEmpty(gdDytempList)) {
            //区分土地和房屋
            if (StringUtils.isNotBlank(gdDytempList.get(0).getBdclx()) && StringUtils.equals(gdDytempList.get(0).getBdclx(), Constants.BDCLX_TD)) {
                bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
            }
            bdcXmRel.setYproid(gdDytempList.get(0).getProid());
        } else if (CollectionUtils.isNotEmpty(gdYgtempList)) {
            //区分土地和房屋
            if (StringUtils.isNotBlank(gdYgtempList.get(0).getBdclx()) && StringUtils.equals(gdYgtempList.get(0).getBdclx(), Constants.BDCLX_TD)) {
                bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
            }
            bdcXmRel.setYproid(gdYgtempList.get(0).getProid());
        } else if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
            bdcXmRel.setYproid(gdTdsyqList.get(0).getProid());
            bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
        }
    }

    @Override
    public int multiSelectSameBdcdyBdcXmRel(List<BdcXmRel> bdcXmRelList, BdcXmRel bdcXmRel) {
        int flag = ParamsConstants.NUMBER_ZERO;
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (int i = 0; i < bdcXmRelList.size(); i++) {
                BdcXmRel bdcXmRelTemp = bdcXmRelList.get(i);
                if (StringUtils.isNotBlank(bdcXmRel.getQjid()) && StringUtils.isNotBlank(bdcXmRelTemp.getQjid())
                        && StringUtils.equals(bdcXmRelTemp.getQjid(), bdcXmRel.getQjid())) {
                    //jyl 针对一证多房的项目内多幢
                    if (StringUtils.isNoneBlank(bdcXmRel.getYqlid()) && StringUtils.isNoneBlank(bdcXmRelTemp.getYqlid()) && bdcXmRelTemp.getYqlid().indexOf(bdcXmRel.getYqlid()) > -1) {
                        flag = ParamsConstants.NUMBER_ONE;
                        break;
                    } else {
                        //拼接yproid
                        bdcXmRelTemp.setYproid(bdcXmRelTemp.getYproid() + "," + bdcXmRel.getYproid());
                        //拼接yqlid
                        bdcXmRelTemp.setYqlid(bdcXmRelTemp.getYqlid() + "," + bdcXmRel.getYqlid());
                        flag = ParamsConstants.NUMBER_ONE;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public void recombineBdcXmRelDate(List<BdcXmRel> bdcXmRelList, List<BdcXmRel> newBbdcXmRelList, boolean hasppbdcdy, String sqlxdm) {
        /**
         * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
         * @description 检查是否匹配不动产单元
         */
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            int i = 0;
            for (; i < bdcXmRelList.size(); i++) {
                if (StringUtils.isNotBlank(bdcXmRelList.get(i).getQjid())) {
                    break;
                }
            }
            if (i == bdcXmRelList.size()) {
                hasppbdcdy = false;
            }
        }

        /**
         * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
         * @description 数据重组 去除重复fwid
         */
        if (!hasppbdcdy || (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm, Constants.SQLX_PLGDDYZX_DM))) {
            if (CollectionUtils.isNotEmpty(bdcXmRelList) && bdcXmRelList.size() > 1) {
                List<String> qlidList = new ArrayList<String>();
                for (int i = 0; i < bdcXmRelList.size(); i++) {
                    if (!qlidList.contains(bdcXmRelList.get(i).getYqlid())) {
                        qlidList.add(bdcXmRelList.get(i).getYqlid());
                        newBbdcXmRelList.add(bdcXmRelList.get(i));
                    }
                }
            } else {
                newBbdcXmRelList.addAll(bdcXmRelList);
            }
        } else {
            newBbdcXmRelList.addAll(bdcXmRelList);
        }
    }

    @Override
    public Project initGdDataToBdcXmRelForPl(Project project, String gdproids, String qlids) {
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        List<BdcXmRel> newBbdcXmRelList = new ArrayList<BdcXmRel>();
        List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
        boolean hasppbdcdy = true;
        if (StringUtils.isNotBlank(qlids)) {
            String[] qlidsArr = qlids.split(",");
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 通过qlid得到所有的房屋id
             */
            for (String qlid : qlidsArr) {
                List<GdBdcQlRel> gdBdcQlRelListtemp = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                if (CollectionUtils.isNotEmpty(gdBdcQlRelListtemp)) {
                    gdBdcQlRelList.addAll(gdBdcQlRelListtemp);
                }
            }
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 根据有多少房屋，组织参数，通过fwid查询匹配关系，暂不考虑纯土地
             */
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                    HashMap map = new HashMap();
                    map.put("qlid", gdBdcQlRel.getQlid());
                    List<GdFwsyq> gdFwsyqtempList = gdFwService.andEqualQueryGdFwsyq(map);
                    List<GdTdsyq> gdTdsyqtempList = gdTdService.andEqualQueryGdTdsyq(map);
                    map.clear();
                    map.put("cfid", gdBdcQlRel.getQlid());
                    List<GdCf> gdCftempList = gdFwService.andEqualQueryGdCf(map);
                    map.clear();
                    map.put("dyid", gdBdcQlRel.getQlid());
                    List<GdDy> gdDytempList = gdFwService.andEqualQueryGdDy(map);
                    bdcXmRel.setYdjxmly(Constants.SJLY_GDFW_XMLY);
                    if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                        hasppbdcdy = false;
                        bdcXmRel.setQjid(gdDyhRelList.get(0).getDjid());
                        bdcXmRel.setYqlid(gdBdcQlRel.getQlid());
                        //zq处理多个房产证，合并一个不动产单元，多权利合并问题，存入到bdcxmrel表以逗号相隔
                        String qlidstemp = bdcGdDyhRelService.getGdDyhRelQlidsByDjids(gdDyhRelList.get(0).getDjid(), qlids);
                        if (StringUtils.isNotBlank(qlidstemp)) bdcXmRel.setYqlid(qlidstemp);
                        //zq修改存入qlid，两个权利匹配一个不动产单元，合并时会存入两个不动产单元
                    } else {
                        bdcXmRel.setYqlid(gdBdcQlRel.getQlid());
                    }
                    if (CollectionUtils.isNotEmpty(gdFwsyqtempList)) {
                        bdcXmRel.setYproid(gdFwsyqtempList.get(0).getProid());
                    } else if (CollectionUtils.isNotEmpty(gdTdsyqtempList)) {
                        bdcXmRel.setYproid(gdTdsyqtempList.get(0).getProid());
                        bdcXmRel.setYdjxmly(Constants.SJLY_GD_XMLY);
                    } else if (CollectionUtils.isNotEmpty(gdCftempList)) {
                        bdcXmRel.setYproid(gdCftempList.get(0).getProid());
                    } else if (CollectionUtils.isNotEmpty(gdDytempList)) {
                        bdcXmRel.setYproid(gdDytempList.get(0).getProid());
                    }
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 根据qlid获取proid
                     */
                    String proidsTemp = bdcGdDyhRelService.getProidsByDjids(bdcXmRel.getYqlid());
                    if (StringUtils.isNotBlank(proidsTemp)) {
                        bdcXmRel.setYproid(proidsTemp);
                    }
                    bdcXmRelList.add(bdcXmRel);
                }
            }
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 数据重组 去除重复fwid
             */
            if (!hasppbdcdy) {
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    List<String> qlidList = new ArrayList<String>();
                    for (int i = 0; i < bdcXmRelList.size(); i++) {
                        if (!qlidList.contains(bdcXmRelList.get(i).getQjid())) {
                            qlidList.add(bdcXmRelList.get(i).getQjid());
                            newBbdcXmRelList.add(bdcXmRelList.get(i));
                        }
                    }
                }
            } else {
                newBbdcXmRelList = bdcXmRelList;
            }
        }
        project.setBdcXmRelList(newBbdcXmRelList);
        return project;
    }

    @Override
    public Project initBdcDataToBdcXmRel(Project project, String gwc) {
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            //zdd 一个流程多个项目处理 选择楼盘表
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
            if (null != bdcXm && StringUtils.isNotBlank(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLFZ_DM) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) {
                if (StringUtils.equals(gwc, "1")) {
                    if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
                        List<String> djsjIds = new LinkedList<String>();
                        HashMap map = new HashMap();
                        map.put("fw_dcb_indexs", project.getDcbIndexs());
                        List<DjsjFwHs> djsjFwHsList = null;
                        if (StringUtils.equals(project.getSfyc(), "1")) {
                            djsjFwHsList = djsjFwService.getDjsjFwYcHs(map);
                        } else {
                            djsjFwHsList = djsjFwService.getDjsjFwHs(map);
                        }
                        if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                                djsjIds.add(djsjFwHs.getFwHsIndex());
                            }
                        }
                        project.getDjIds().addAll(djsjIds);
                    }
                } else if (StringUtils.isNotBlank(project.getDcbIndex())) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(FW_DCB_INDEX, project.getDcbIndex());
                    List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(map);
                    List<String> djsjIds = new ArrayList<String>();
                    if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                        for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                            djsjIds.add(djsjFwHs.getFwHsIndex());
                        }
                    } else {
                        HashMap<String, String> ychsMap = new HashMap<String, String>();
                        ychsMap.put(FW_DCB_INDEX, project.getDcbIndex());
                        List<DjsjFwHs> djsjFwYcHsList = djsjFwService.getDjsjFwYcHs(ychsMap);
                        if (CollectionUtils.isNotEmpty(djsjFwYcHsList)) {
                            for (DjsjFwHs djsjFwHs : djsjFwYcHsList) {
                                djsjIds.add(djsjFwHs.getFwHsIndex());
                            }
                        }
                    }
                    project.setDjIds(djsjIds);
                } else if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
                    List<String> djsjIds = new LinkedList<String>();
                    HashMap map = new HashMap();
                    map.put("fw_dcb_indexs", project.getDcbIndexs());
                    List<DjsjFwHs> djsjFwHsList = null;
                    if (StringUtils.equals(project.getSfyc(), "1")) {
                        djsjFwHsList = djsjFwService.getDjsjFwYcHs(map);
                    } else {
                        djsjFwHsList = djsjFwService.getDjsjFwHs(map);
                    }
                    if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                        for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                            djsjIds.add(djsjFwHs.getFwHsIndex());
                        }
                    }
                    project.setDjIds(djsjIds);
                } else if (CollectionUtils.isNotEmpty(project.getDjIds())) {
                    //当人工选择逻辑幢处理下djIds
                    String djIds = project.getDjIds().get(0);
                    if (StringUtils.isNotBlank(djIds) && StringUtils.indexOf(djIds, Constants.SPLIT_STR) > -1) {
                        String[] djAllIds = StringUtils.split(djIds, Constants.SPLIT_STR);
                        List<String> djsjIds = new ArrayList<String>();
                        for (String djId : djAllIds) {
                            djsjIds.add(djId);
                        }
                        project.setDjIds(djsjIds);
                    }
                }
                //针对单个流程重复选的时候执行下面方法；
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.size() == 1 && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                        //zhouwanqing 多次选择不动产单元会出现冗余不动产单元和登记簿以及bdc_td的数据
                        bdcdyService.delDjbAndTd(bdcXm);
                        bdcdyService.delXmBdcdy(bdcXm.getBdcdyid());
                    }
                    project = bdcXmService.getProjectFromBdcXm(bdcXm, project);
                }
            } else {
                HashMap<String, String> map = new HashMap<String, String>();
                List<Map> ljzList = null;
                if (StringUtils.isNotBlank(project.getDcbIndex())) {
                    map.put(FW_DCB_INDEX, project.getDcbIndex());
                    ljzList = djsjFwService.getLpbList(map);
                }
                String bdcdyfwlx = "";
                List<String> djids = new ArrayList<String>();
                List<String> bdcdys = new ArrayList<String>();
                //处理独幢
                if (CollectionUtils.isNotEmpty(ljzList)) {
                    Map ljzmap = ljzList.get(0);
                    bdcdyfwlx = (String) (ljzmap.get("BDCDYFWLX"));
                    String bdcdyh = (String) (ljzmap.get(BDCDYH));
                    if (StringUtils.isNotBlank(bdcdyfwlx) && "2".equals(bdcdyfwlx) && StringUtils.isNotBlank(bdcdyh)) {
                        bdcdys.add(bdcdyh);
                        project.setBdcdyhs(bdcdys);
                        djids.add(project.getDcbIndex());
                        project.setDjIds(djids);
                        project.setDcbIndex(null);
                        project.setDcbIndexs(null);
                    }
                } else if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
                    //批量选择独幢
                    List<String> plDcbIndexList = new ArrayList<String>();
                    List<String> plBdcdyhList = new ArrayList<String>();
                    // 混合选择的时候保存户室的fw_dcb_index
                    List<String> eliminateDzList = new ArrayList<String>();
                    for (String dcbIndex : project.getDcbIndexs()) {
                        map.put(FW_DCB_INDEX, dcbIndex);
                        List<Map> plLjzList = djsjFwService.getLpbList(map);
                        Map ljzmap = plLjzList.get(0);
                        bdcdyfwlx = (String) (ljzmap.get("BDCDYFWLX"));
                        String bdcdyh = (String) (ljzmap.get(BDCDYH));
                        if (StringUtils.isNotBlank(bdcdyfwlx) && "2".equals(bdcdyfwlx)) {
                            if (StringUtils.isNotBlank(bdcdyh)) {
                                plDcbIndexList.add(dcbIndex);
                                plBdcdyhList.add(bdcdyh);
                            }
                        } else {
                            eliminateDzList.add(dcbIndex);
                        }
                    }
                    djids.addAll(plDcbIndexList);
                    project.setDjIds(djids);
                    bdcdys.addAll(plBdcdyhList);
                    project.setBdcdyhs(bdcdys);
                    if (CollectionUtils.isNotEmpty(project.getBdcdyhs()) && CollectionUtils.isNotEmpty(project.getDjIds())) {
                        project.setDcbIndexs(eliminateDzList);
                    }
                }
                project = bdcXmService.getProjectFromBdcXm(bdcXm, project);
            }
            project.setBdcdyid(null);
            List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
            //jyl 数据格式统一转换成bdc_xm_rel格式
            if (CollectionUtils.isEmpty(project.getBdcXmRelList())) {
                if (CollectionUtils.isNotEmpty(project.getDjIds())) {
                    if (project.getDjIds().size() == 1) {
                        String djIds = project.getDjIds().get(0);
                        String[] djAllIds = StringUtils.split(djIds, Constants.SPLIT_STR);
                        for (String djId : djAllIds) {
                            project.setDjId(djId);
                            BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                            if (bdcXmRel != null) {
                                bdcXmRelList.add(bdcXmRel);
                            }
                        }
                    } else {
                        for (String djId : project.getDjIds()) {
                            project.setDjId(djId);
                            BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                            if (bdcXmRel != null) {
                                bdcXmRelList.add(bdcXmRel);
                            }
                        }
                    }
                    System.out.println("项目个数：" + bdcXmRelList.size());
//                    if(CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
//                        BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
//                        if(bdcXmRel != null) {
//                            bdcXmRelList.add(bdcXmRel);
//                        }
//                    }
                    System.out.println("项目个数：" + bdcXmRelList.size());
                } else if (!StringUtils.equals(project.getSqlx(), Constants.SQLX_PLHZ_DM) && !StringUtils.equals(project.getSqlx(), Constants.SQLX_PLYSBZ_DM)) {
                    BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                    if (bdcXmRel != null) {
                        bdcXmRelList.add(bdcXmRel);
                    }
                }
                if ((StringUtils.equals(project.getSqlx(), Constants.SQLX_PLHZ_DM) || StringUtils.equals(project.getSqlx(), Constants.SQLX_PLYSBZ_DM)) && StringUtils.isNotBlank(project.getYbdcqzh())) {
                    List<BdcXmzsRel> bdcXmzsRelList = bdcXmRelService.getProidByBdcqzh(project.getYbdcqzh());
                    if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
                        for (int i = 0; i < bdcXmzsRelList.size(); i++) {
                            BdcXmRel bdcXmRel = new BdcXmRel();
                            bdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRel.setQjid(project.getDjId());
                            bdcXmRel.setProid(project.getProid());
                            bdcXmRel.setYqlid(project.getYqlid());
                            bdcXmRel.setYdjxmly(project.getXmly());
                            bdcXmRel.setYproid(bdcXmzsRelList.get(i).getProid());
                            bdcXmRelList.add(bdcXmRel);
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                project.setBdcXmRelList(bdcXmRelList);
                            }
                        }
                    }
                } else {
                    //合并流程不动产项目关系表必须有值
                    project.setBdcXmRelList(bdcXmRelList);
                }
            }
        }
        return project;
    }

    public boolean checkMulBdcdys(BdcXm bdcXm) {

        boolean mulBdcdys = false;
        if (bdcXm instanceof Project) {
            Project project = (Project) bdcXm;
            if (CollectionUtils.isNotEmpty(project.getDjIds())) {
                String djIds = project.getDjIds().get(0);
                if (djIds != null && djIds.indexOf("$") > -1) {
                    if (StringUtils.split(djIds, "$").length > 1) {
                        mulBdcdys = true;
                    }
                } else {
                    //zwq  这个是djids在传进来就被处理过，为保证其他未考虑的情况，所以上面判断未去掉
                    if (project.getDjIds().size() > 1) {
                        mulBdcdys = true;
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(project.getBdcXmRelList()) && project.getBdcXmRelList().size() > 1) {
                mulBdcdys = true;
            }
            if (CollectionUtils.isNotEmpty(project.getBdcXmRelList()) && StringUtils.equals(project.getXmly(), Constants.SJLY_GDFW_XMLY)) {
                //zq过渡的一个房产证一个房屋，走单个流程是没有初始化bdcxmrellist，但是如果是一证多房和合并的匹配一个不动产单元，还应该走批量的流程，会初始化bdcxmrellist
                mulBdcdys = true;
            }
            if (CollectionUtils.isNotEmpty(project.getDcbIndexs())
                    && StringUtils.isNotBlank(project.getSqlx()) && (StringUtils.equals(project.getSqlx(), Constants.SQLX_PLFZ_DM) || StringUtils.equals(project.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) && CollectionUtils.isEmpty(project.getBdcdyhs()) && StringUtils.isBlank(project.getBdcdyh())) {
                mulBdcdys = true;
            }
        } else {
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                /**
                 * @author <a href="mailto:liangqing@gtmap.cn">liangqing</a>
                 * @description 转发时候，通过获取当前项目的wiid判断是不是多个项目来判断是否多个不动产单元
                 */
                //jyl  过滤不动产附属设施的不动产项目。
                HashMap xmMap = new HashMap();
                xmMap.put("wiid", bdcXm.getWiid());
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(xmMap);
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if (bdcXmList != null && bdcXmList.size() > 1 && CollectionUtils.isNotEmpty(bdcXmRelList) && StringUtils.isNotBlank(bdcXmRelList.get(0).getQjid())) {
                    mulBdcdys = true;
                }
                /**
                 * @author <a href="mailto:zhanglili@gtmap.cn">zhanglili</a>
                 * @description (芜湖需求)抵押权变更(批量), 从新建走批量选择不动产单元，无qjid
                 */
                if (bdcXmList != null && bdcXmList.size() > 1 && (StringUtils.equals(Constants.SQLX_DYBGPL_DM, bdcXm.getSqlx())
                        || StringUtils.equals(Constants.SQLX_CQZMD_HZ, bdcXm.getSqlx()))) {
                    mulBdcdys = true;
                }
            }
        }


        return mulBdcdys;
    }


    @Override
    public void dbBdcXm(TurnProjectService turnProjectService, BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            turnProjectService.saveBdcqzh(bdcXm.getWiid());
        }
    }

    @Override
    public Project initProject(Project project) {
        if (project.getDjIds() != null) {
            //当人工选择逻辑幢处理下djIds
            String djIds = project.getDjIds().get(0);
            if (StringUtils.isNotBlank(djIds) && StringUtils.indexOf(djIds, Constants.SPLIT_STR) > -1) {
                String[] djAllIds = StringUtils.split(djIds, Constants.SPLIT_STR);
                List<String> djsjIds = new ArrayList<String>();
                for (String djId : djAllIds) {
                    djsjIds.add(djId);
                }
                project.setDjIds(djsjIds);
            }
            String bdcdyhs = "";
            if (CollectionUtils.isNotEmpty(project.getBdcdyhs()))
                bdcdyhs = project.getBdcdyhs().get(0);
            if (StringUtils.isNotBlank(bdcdyhs) && StringUtils.indexOf(bdcdyhs, Constants.SPLIT_STR) > -1) {
                String[] bdcdyhAllIds = StringUtils.split(bdcdyhs, Constants.SPLIT_STR);
                List<String> bdcdyhIds = new ArrayList<String>();
                for (String bdcdyh : bdcdyhAllIds) {
                    bdcdyhIds.add(bdcdyh);
                }
                project.setBdcdyhs(bdcdyhIds);
            }
        }
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            //yinyao 针对单个流程重复选的时候执行下面方法；
            project = bdcXmService.getProjectFromBdcXm(bdcXm, project);
        }
        project.setBdcdyid(null);
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        //jyl  新增的批量的添加不动产单元保留原的
        if (StringUtils.isNotBlank(bdcXm.getXmzt()) && StringUtils.isNotBlank(bdcXm.getDwdm())) {
            project.setProid(UUIDGenerator.generate18());
            if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                for (int i = 0; i < project.getBdcXmRelList().size(); i++) {
                    project.getBdcXmRelList().get(i).setProid(project.getProid());
                }
            }
        }
        //  zhanglili用于新建批量生成多个bdc_xm_rel表
        if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
            for (int i = 0; i < project.getBdcXmRelList().size(); i++) {
                Project newProject = new Project();
                newProject.setDjId(project.getBdcXmRelList().get(i).getQjid());
                newProject.setYxmid(project.getBdcXmRelList().get(i).getYproid());
                newProject.setYqlid(project.getBdcXmRelList().get(i).getYqlid());
                newProject.setProid(project.getBdcXmRelList().get(i).getProid());
                newProject.setXmly(project.getBdcXmRelList().get(i).getYdjxmly());
                BdcXmRel bdcXmRelTemp = bdcXmRelService.creatBdcXmRelFromProject(newProject);
                if (bdcXmRelTemp != null) {
                    bdcXmRelList.add(bdcXmRelTemp);
                }
            }
            if (project.getBdcXmRelList().size() == 1) {
                //如果是多选时，选择了其中一个，赋值到项目(选择证)
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getBdcXmRelList().get(0).getYproid());
                if (bdcBdcdy != null) {
                    project.setBdcdyh(bdcBdcdy.getBdcdyh());
                    project.setBdcdyid(bdcBdcdy.getBdcdyid());
                    project.setBdclx(bdcBdcdy.getBdclx());
                    project.setZdzhh(StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19));
                } else {
                    //如果是多选时，选择了其中一个，赋值到项目
                    HashMap map = new HashMap();
                    map.put("id", project.getBdcXmRelList().get(0).getQjid());
                    map.put("bdclx", Constants.BDCLX_TDFW);
                    List<Map> bdcdyMapList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyMapList)) {
                        project.setBdcdyh(CommonUtil.formatEmptyValue(bdcdyMapList.get(0).get(BDCDYH)));
                        project.setBdclx(Constants.BDCLX_TDFW);
                        project.setZdzhh(StringUtils.substring(CommonUtil.formatEmptyValue(bdcdyMapList.get(0).get(BDCDYH)), 0, 19));
                    } else {
                        HashMap tdMap = new HashMap();
                        tdMap.put("id", project.getBdcXmRelList().get(0).getQjid());
                        tdMap.put("bdclx", Constants.BDCLX_TD);
                        List<Map> tdBdcdyMapList = bdcdyService.getDjBdcdyListByPage(tdMap);
                        if (CollectionUtils.isNotEmpty(tdBdcdyMapList)) {
                            project.setBdcdyh(CommonUtil.formatEmptyValue(tdBdcdyMapList.get(0).get(BDCDYH)));
                            project.setBdclx(Constants.BDCLX_TD);
                            project.setZdzhh(StringUtils.substring(CommonUtil.formatEmptyValue(tdBdcdyMapList.get(0).get(BDCDYH)), 0, 19));
                        }
                    }
                }
            }
        } else {
            //yinyao 解决批量查封逻辑幢问题 djids不为空时，遍历生成bdcXmRelList
            if (CollectionUtils.isNotEmpty(project.getDjIds())) {
                for (String djId : project.getDjIds()) {
                    project.setDjId(djId);
                    BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                    if (bdcXmRel != null) {
                        bdcXmRelList.add(bdcXmRel);
                    }
                }
            } else {
                BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                if (bdcXmRel != null) {
                    bdcXmRelList.add(bdcXmRel);
                }
            }
        }
        return project;
    }

    @Override
    public void initBdcFwfsss(Project project, String qlids) {
        List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
        if (StringUtils.isNotBlank(qlids)) {
            String[] qlidsArr = qlids.split(",");
            for (String qlid : qlidsArr) {
                List<GdBdcQlRel> gdBdcQlRelListtemp = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                gdBdcQlRelList.addAll(gdBdcQlRelListtemp);
            }
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                project.setBdclx(gdBdcQlRelList.get(0).getBdclx());
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    //jyl 控制附属设施，附属设施的房子现阶段不参与流程的生成。只做最后的附记内容。
                    GdFw gdFw = gdFwService.queryGdFw(gdBdcQlRel.getBdcid());
                    if (gdFw != null && StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(), "1")) {
                        List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                        BdcFwfsss bdcFwfsss = gdFwMapper.getFwfsssByFwid(gdFw.getFwid());
                        bdcFwfsss.setFwfsssid(gdFw.getFwid());
                        if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                            bdcFwfsss.setBdcdyid(gdDyhRelList.get(0).getDjid());
                        }
                        bdcFwfsss.setProid(project.getProid());
                        bdcFwfsss.setWiid(project.getWiid());
                        entityMapper.saveOrUpdate(bdcFwfsss, bdcFwfsss.getFwfsssid());
                    }
                }
            }
        }
    }

    @Override
    public void initBdcFwfsss(BdcXm bdcXm) {
        BdcXmRel bdcXmRel = null;
        if (bdcXm != null) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                bdcXmRel = bdcXmRelList.get(0);
            }
        }

        BdcXm ybdcxm = null;
        if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
            ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
        }
        if (ybdcxm != null) {
            BdcBdcdy bdcBdcdy = null;
            if (StringUtils.isNotBlank(ybdcxm.getBdcdyid())) {
                bdcBdcdy = bdcdyService.queryBdcdyById(ybdcxm.getBdcdyid());
            }
            Example example = new Example(BdcFwfsss.class);
            if (StringUtils.isNotBlank(ybdcxm.getProid())) {
                example.createCriteria().andEqualTo("proid", ybdcxm.getProid());
            }
            if (StringUtils.isNotBlank(ybdcxm.getWiid())) {
                example.createCriteria().andEqualTo("wiid", ybdcxm.getWiid());
            }
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                example.createCriteria().andEqualTo("zfbdcdyh", bdcBdcdy.getBdcdyh());
            }
            List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(BdcFwfsss.class, example);
            if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                    bdcFwfsss.setFwfsssid(UUIDGenerator.generate());
                    bdcFwfsss.setProid(bdcXm.getProid());
                    bdcFwfsss.setWiid(bdcXm.getWiid());
                    entityMapper.saveOrUpdate(bdcFwfsss, bdcFwfsss.getFwfsssid());
                }
            }
        }
    }


    @Override
    public CreatProjectService getCreatProjectService(BdcXm bdcXm, String isScFsss) {
        //zdd 暂时写死  后面可以考虑spring配置实现
        CreatProjectService creatProjectService = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjlx())) {
            String sqlxdm = "";
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            if (StringUtils.isNotBlank(isScFsss) && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(isScFsss, "true")) {
                sqlxdm = bdcXm.getSqlx();
            }
            //hqz 未匹配不动产单元，调用公共流程
            List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
            if (bppSqlxdmList.contains(bdcXm.getSqlx()) && !CommonUtil.indexOfStrs(Constants.SQLX_NOBDCDY_GDQL, bdcXm.getSqlx())) {
                creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CSDJ_DM) || ((StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YZX)) || StringUtils.equals(sqlxdm, Constants.SQLX_YZX))) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) || bdcXm.getQllx().equals(Constants.QLLX_DYQ))) {
                    creatProjectService = creatProjectDydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM))) {
                    creatProjectService = creatComplexProjectNewServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("152")) {
                    creatProjectService = creatProjectHydjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("135") || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("135"))) {
                    creatProjectService = creatComplexYzxdjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCKFSZC_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFSCKFSZC_DM))) {
                    creatProjectService = creatProjectBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFXZBG_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SPFXZBG_DM))) {
                    creatProjectService = creatProjectBgdjServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_GYJSYD_GZW_DM)
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_GYJSYD_GZW_DM)))) {
                    creatProjectService = createComplexScYtdDyServiceImpl;
                } else {
                    creatProjectService = creatProjectScdjService;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM) || (StringUtils.equals(sqlxdm, "218") || StringUtils.equals(bdcXm.getSqlx(), "218") || StringUtils.equals(sqlxdm, Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YFZYDYDJ_DM) || StringUtils.equals(sqlxdm, "219") || StringUtils.equals(bdcXm.getSqlx(), "219") || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_CLF_ZDYD))) {
                creatProjectService = creatProjectZydjService;
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("218")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("218")) || (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_YFZYDYDJ_DM)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_YFZYDYDJ_DM)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CLF_ZDYD)) || (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_CLF_ZDYD)))
                    creatProjectService = creatComplexZyDyProjectServiceImpl;
                else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("219")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("219")))
                    creatProjectService = creatComplexZyWithDyProjectServiceImpl;
                else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBZY_DM))) {
                    creatProjectService = creatProjectFwFgHbZyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBZY_DM))) {
                    creatProjectService = creatProjectTdFgHbZyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_ZY_SFCD))) {
                    creatProjectService = creatProjectSfZydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_BGDJ_DM)) {
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                //zdd 抵押权（地役权）需要调用单独的变更服务
                if (qllxVo instanceof BdcDyaq || qllxVo instanceof BdcDyq) {
                    creatProjectService = creatProjectDyBgdjServiceImpl;
                } else {
                    if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBHZ_DM))) {
                        creatProjectService = creatComplexFgHbHzServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_SYTTBG_DM))) {
                        creatProjectService = creatProjectSyTtBgdjServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && CommonUtil.indexOfStrs(Constants.MJHJ_SQLX, sqlxdm)) {
                        creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                    } else {
                        creatProjectService = creatProjectBgdjServiceImpl;
                    }
                    if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBBG_DM))) {
                        creatProjectService = creatProjectFwFgHbBgServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_FWFGHBZY_DM))) {
                        creatProjectService = creatProjectFwFgHbZyServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBBG_DM))) {
                        creatProjectService = creatProjectTdFgHbBgServiceImpl;
                    } else if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBZY_DM))) {
                        creatProjectService = creatProjectTdFgHbZyServiceImpl;
                    }
                }
                //合并流程单独提出来
                if (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_GZDJ_DM)) {
                creatProjectService = creatProjectGzdjServiceImpl;
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_ZXDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getQllx()) && (bdcXm.getQllx().equals("18") || bdcXm.getQllx().equals("17"))) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTdsyqDjNoQlServiceImpl;
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TD_ZHZX)) {
                    creatProjectService = createProjectZhzxdjServiceImpl;
                } else {
                    creatProjectService = creatProjectZxdjServiceImpl;
                }

            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YGDJ_DM) || (StringUtils.equals(sqlxdm, "706") || StringUtils.equals(bdcXm.getSqlx(), "706"))) {
                //zdd 注销预告登记用注销的逻辑
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("703") || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YGDY_ZX) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFZX)) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals("706")) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals("706"))) {
                    creatProjectService = creatComplexYgdjServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_YGSPFDY)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_DYPL)) {
                    creatProjectService = creatComplexYgdjPlProjectServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_YG_BDCDY) || StringUtils.equals(sqlxdm, Constants.SQLX_YG_YGSPF)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGDYBG)) {
                    creatProjectService = creatProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BGDJ)) {
                    creatProjectService = creatProjectYgBgdjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_MMYG)) {
                    creatProjectService = creatProjectYgdjServiceImpl;
                } else {
                    creatProjectService = creatProjectZxdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_CFDJ_DM)) {
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_JF) || bdcXm.getSqlx().equals(Constants.SQLX_PLJF) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL))) {
                    if (bdcXm.getSqlx().equals(Constants.SQLX_SFCD) || bdcXm.getSqlx().equals(Constants.SQLX_SFCD_PL)) {
                        creatProjectService = creatProjectSfcdjfdjService;
                    } else {
                        creatProjectService = creatProjectZxTxdjServiceImpl;
                    }
                }//纯土地不匹配不动产单元，不传入原权利
                else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    if (bdcXm.getSqlx().equals("809")) {
                        creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                    } else {
                        creatProjectService = creatProjectCfdjNoQlServiceImpl;
                    }
                } else if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_HHLC)) {
                    creatProjectService = creatProjectHhLcdjServiceImpl;
                } else {
                    creatProjectService = creatProjectCfdjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_DYDJ_DM)) {

                if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_HHLC)) {
                    creatProjectService = creatProjectHhLcdjServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZYDJ_SQLXDM, bdcXm.getSqlx()))
                    //针对单独将抵押权登记
                    creatProjectService = creatProjectHmZydjService;
                else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_BGDJ_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectDyBgdjServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押转移单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM)) {
                    creatProjectService = createDyZyForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押变更单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
                    creatProjectService = createDyBgForZjgcServiceImpl;
                }
                /**
                 * @author bianwen
                 * @description 在建建筑物抵押注销单独分开
                 */
                else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZW_ZX_FW_DM)) {
                    creatProjectService = createDyZxForZjgcServiceImpl;
                }
                /**
                 * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
                 * @description 在建工程抵押权转现抵押
                 */
                else if (StringUtils.equals(Constants.SQLX_ZJJZWDY_ZX_DM, bdcXm.getSqlx())) {
                    creatProjectService = creatComplexScdjAndPldyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLDYZX)) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.DJLX_PLDY_YBZS_SQLXDM) || bdcXm.getSqlx().equals(Constants.DJLX_PLDY_DDD_SQLXDM)) {
                    creatProjectService = creatProjectDydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDYBG_YBZS_SQLXDM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYBG))) {
                    creatProjectService = creatComplexDybgdjProjectServiceImpl;
                }
                //纯土地不匹配不动产单元，不传入原权利
                else if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                    creatProjectService = creatProjectZxTxdjNoQlServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TD_ZHJY)) {
                    creatProjectService = createProjectZhjydjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZHDK_DYZY)) {
                    creatProjectService = creatComplexDyZydjProjectServiceImpl;
                } else {
                    creatProjectService = creatProjectDydjService;

                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_YYDJ_DM)) {
                //sc 注销异议登记，走注销Service
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    creatProjectService = creatProjectZxdjServiceImpl;
                } else {
                    creatProjectService = creatProjectYydjServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_QTDJ_DM)) {
                if (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLZY_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DT_DYQLBG_DM)
                        || StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_CQDJHZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_PLHZ_DM))
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_PLYSBZ_DM)
                        || StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_YSBZ_OLDDM))
                    creatProjectService = creatProjectYsbzServiceImpl;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equalsIgnoreCase(bdcXm.getSqlx(), Constants.SQLX_DY_DM)))
                    creatProjectService = creatProjectDydjService;
                else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZX)) {
                    creatProjectService = creatProjectZxdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_ZY)) {
                    creatProjectService = creatProjectZydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DY_BG)) {
                    creatProjectService = creatProjectDyBgdjServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_PLHZ)) {
                    creatProjectService = creatProjectYsbzServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_CQZMD_HZ)) {
                    creatProjectService = creatProjectZydjService;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_BDCDYDJ_DM)
                        || bdcXm.getSqlx().equals(Constants.SQLX_BDCDYBGDJ_DM)) || bdcXm.getSqlx().equals(Constants.SQLX_QJDCDJ_DM)) {
                    creatProjectService = creatProjectBdcdyDjService;
                } else {
                    creatProjectService = creatProjectDefaultService;
                }
                //合并流程单独提出来
                if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_HZDY_DM)) {
                    creatProjectService = creatComplexHzDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && (sqlxdm.equals(Constants.SQLX_DYHZ_DM) || sqlxdm.equals(Constants.SQLX_YSBZDY_DM))) {
                    creatProjectService = creatComplexHzWithDyProjectServiceImpl;
                }
            } else if (bdcXm.getDjlx().equals(Constants.DJLX_HBDJ_DM)) {
                if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SC_DY))
                        || (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_SC_DY))) {
                    creatProjectService = creatComplexScWithDyServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_DYHZ_DM)) ||
                        (StringUtils.isNotBlank(bdcXm.getSqlx()) && (bdcXm.getSqlx().equals(Constants.SQLX_DYHZ_DM)) ||
                                bdcXm.getSqlx().equals(Constants.SQLX_YSBZDY_DM))) {
                    creatProjectService = creatComplexHzWithDyProjectServiceImpl;
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_DYBG_DM)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                } else if ((StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_HZDY_DM)) ||
                        (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXm.getSqlx().equals(Constants.SQLX_HZDY_DM))) {
                    creatProjectService = creatComplexHzDyProjectServiceImpl;
                } else if (bdcXm.getSqlx().equals(Constants.SQLX_SPFFTTD_ZHJY_ZHZX)) {
                    creatProjectService = creatComplexSpfZhjyZhzxProjectServiceImpl;
                } else if (StringUtils.equals(sqlxdm, Constants.SQLX_HDDJ_RYZH) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_HDDJ_RYZH)) {
                    creatProjectService = creatProjectArbitraryServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_JSYDSYQ_LHDJ)) {
                    creatProjectService = creatComplexBgWithDyServiceImpl;
                } else if (StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_SPFSCDY_DM)) {
                    creatProjectService = creatComplexScDyProjectService;
                } else {
                    creatProjectService = creatProjectDefaultService;
                }
            } else {
                creatProjectService = creatProjectDefaultService;
            }
        } else {
            creatProjectService = creatProjectDefaultService;
        }
        return creatProjectService;
    }

    @Override
    public RegisterProjectService getRegisterProjectService(BdcXm bdcXm) {
        RegisterProjectService registerProjectService = null;
        if (bdcXm != null) {
            if (CommonUtil.indexOfStrs(Constants.SQLX_N0_SCZS, bdcXm.getSqlx())) {
                registerProjectService = registerProjectNoCertificateServiceImpl;
            } else if (CommonUtil.indexOfStrs(Constants.SQLX_ZXDJ, bdcXm.getSqlx())) {
                registerProjectService = registerProjectZxdjServiceImpl;
            } else {
                registerProjectService = registerProjectCertificateServiceImpl;
            }
        }
        return registerProjectService;
    }

}
