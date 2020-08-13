package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.examine.BdcExamineParam;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.AutoSignWfNodeName;
import cn.gtmap.estateplat.server.service.*;
import cn.gtmap.estateplat.server.service.archives.ArchivesService;
import cn.gtmap.estateplat.server.service.config.ConfigSyncQlztService;
import cn.gtmap.estateplat.server.service.core.ProjectLifeManageService;
import cn.gtmap.estateplat.server.service.core.ProjectLifeThreadServcie;
import cn.gtmap.estateplat.server.service.currency.CurrencyService;
import cn.gtmap.estateplat.server.service.etl.EtlGxYhService;
import cn.gtmap.estateplat.server.service.currency.ExchangeService;
import cn.gtmap.estateplat.server.service.impl.DelProjectScdjForSingleXmImpl;
import cn.gtmap.estateplat.server.sj.yw.WfProjectService;
import cn.gtmap.estateplat.server.sj.zs.CompleteZsInfoService;
import cn.gtmap.estateplat.server.thread.BdcChangeYqlztThread;
import cn.gtmap.estateplat.server.thread.BdcCheckThread;
import cn.gtmap.estateplat.server.thread.BdcThreadEngine;
import cn.gtmap.estateplat.server.thread.TurnProjectEventDbrThread;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.service.examine.BdcExamineService;
import cn.gtmap.estateplat.service.exchange.share.RealEstateShareService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 15-3-17
 * Time: 下午6:23
 * Des:工作流项目方法
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/wfProject")
public class WfProjectController extends BaseController {

    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcZsbhService bdcZsbhService;
    @Autowired
    private SignService signService;
    @Autowired
    private FzWorkFlowBackService fzWorkFlowBackService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcSfxxService bdcSfxxService;

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ProjectCheckInfoService projectCheckInfoService;
    @Autowired
    private BdcXtCheckinfoService bdcXtCheckinfoService;

    @Autowired
    private ArchivePostService archivePostService;

    @Autowired
    private DjsjFwService djsjFwService;

    @Autowired
    private BdcSjdService bdcSjdService;

    @Autowired
    private BdcXmRelService bdcXmRelService;

    @Autowired
    private BdcZsService bdcZsService;

    @Autowired
    private BdcXtConfigService bdcXtConfigService;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private ProjectLifeManageService projectLifeManageService;
    @Autowired
    SysSignService sysSignService;
    @Autowired
    BdcSpfZdHjgxService bdcSpfZdHjgxService;
    @Autowired
    private BdcByslService bdcByslService;
    @Autowired
    private BdcBydjService bdcBydjService;
    @Autowired
    private BdcBqbzService bdcBqbzService;
    @Autowired
    private CadastralService cadastralService;
    @Autowired
    private RegisterGraphicService registerGraphicService;
    @Autowired
    private BdcComplexFgHbHzService bdcComplexFgHbHzService;
    @Autowired
    private BdcXmcqRelService bdcXmcqRelService;
    @Autowired
    private ExamineCheckInfoService examineCheckInfoService;
    @Autowired
    private BdcExamineService bdcExamineService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Resource(name = "delProjectDefaultServiceImpl")
    DelProjectService delProjectDefaultServiceImpl;
    @Autowired
    private RealEstateShareService realEstateShareService;
    @Autowired
    private ConfigSyncQlztService configSyncQlztService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private GdCfService gdCfService;

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private EtlGxYhService etlGxYhService;
    @Autowired
    private CheckXmService checkXmService;
    @Autowired
    private BdcThreadEngine bdcThreadEngine;
    @Autowired
    private DelProjectScdjForSingleXmImpl delProjectScdjForSingleXmImpl;
    @Autowired
    private BdcSdService bdcSdService;
    @Autowired
    private ProjectLifeThreadServcie projectLifeThreadServcie;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private WorkFlowEventService workFlowEventService;
    @Autowired
    private BdcBtxyzService bdcBtxyzService;
    @Autowired
    private BdcXtLimitfieldService bdcXtLimitfieldService;
    @Autowired
    private GxYhService gxYhService;
    @Autowired
    private WfProjectService wfProjectService;
    private static final String CONFIGURATION_PARAMETER_ARCHIVES_URL = "archives.url";
    private static final String PARAMETER_CHARSET_GBK = "text/xml;charset=GBK";
    @Autowired
    private BdcBdcZsSdService bdcBdcZsSdService;
    @Autowired
    private CompleteZsInfoService completeZsInfoService;

    private boolean newZh = AppConfig.getBooleanProperty("newZh", false);

    /**
     * zdd
     *
     * @param project
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/initVoFromOldData")
    public String createWfProject(Project project) {
        String msg = "失败";
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            if (StringUtils.isBlank(project.getUserId())) {
                project.setUserId(super.getUserId());
            }
            try {
                wfProjectService.initData(project);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            msg = "成功";
        }
        return msg;
    }

    /**
     * zx 创建项目
     *
     * @param proid
     * @param userid
     */
    @RequestMapping(value = "/creatProjectEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public String creatProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                    @RequestParam(value = "wfid", required = false) String wiid,
                                    @RequestParam(value = "userid", required = false) String userid,
                                    @RequestParam(value = "wdid", required = false) String wdid) {
        Project project = new Project();
        project.setProid(proid);
        project.setWorkFlowDefId(wdid);
        project.setWiid(wiid);
        project.setUserId(userid);
        projectLifeManageService.createProject(project);
        return null;
    }

    @RequestMapping(value = "/turnProjectEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                 @RequestParam(value = "activityid", required = false) String activityid,
                                 @RequestParam(value = "userid", required = false) String userid,
                                 @RequestParam(value = "taskid", required = false) String taskid,
                                 @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {

        //是否转发到缮证节点
        Boolean isSz = true;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (StringUtils.isNotBlank(targetActivityName) && !(StringUtils.equals(targetActivityName, Constants.WORKFLOW_SZ) || StringUtils.equals(targetActivityName, Constants.WORKFLOW_DB))) {
                isSz = false;
            }
        }
        if (isSz) {
            //zdd  生成证书的逻辑根据业务来确定是生成一本还是多本   此处不在直接循环生成证书
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
                //补全证书相关信息
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
                turnProjectDefaultService.setUserid(userid);
                if (CollectionUtils.isEmpty(bdcZsList)) {
                    if (StringUtils.isNotBlank(bdcXm.getBdclx()) && (Constants.BDCLX_TDSL.equals(bdcXm.getBdclx()) || Constants.BDCLX_TDQT.equals(bdcXm.getBdclx())) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                        projectLifeManageService.generateProjectZs(bdcXm.getWiid(), null);
                    } else {
                        if (StringUtils.equals(bdcXm.getSftqsczs(), "1")) {
                            projectService.dbBdcXm(turnProjectDefaultService, bdcXm);
                        } else {
                            creatPreviewZsEvent(proid, userid);
                            completeZsInfoService.completeZsInfo(bdcXm.getWiid());
                        }
                    }
                } else {
                    completeZsInfoService.completeZsInfo(bdcXm.getWiid());
                }
                try {
                    //liujie 更新权籍库
                    if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                        cadastralService.syncQlrForCadastral(bdcXm.getWiid());
                    }
                    //同步登记图形库
                    registerGraphicService.insertDjtxkSj(bdcXm);
                } catch (Exception e) {
                    logger.error("更新权籍库失败！", e);
                }
            }
        }
    }

    @RequestMapping(value = "/dbBdcXm", method = {RequestMethod.POST, RequestMethod.GET})
    public void dbBdcXm(@RequestParam(value = "proid", required = false) String proid,
                        @RequestParam(value = "activityid", required = false) String activityid,
                        @RequestParam(value = "userid", required = false) String userid,
                        @RequestParam(value = "taskid", required = false) String taskid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
            projectService.dbBdcXm(turnProjectDefaultService, bdcXm);
        }

        //张家港激扬档案登簿推送审批表信息
        String archivesUrl = AppConfig.getProperty(CONFIGURATION_PARAMETER_ARCHIVES_URL);
        if (StringUtils.isNotBlank(archivesUrl)) {
            try {
                archivesService.pushArchivesFile(proid, userid);
            } catch (Exception e) {
                logger.error("张家港激扬档案登簿推送审批表信息失败！", e);
            }
        }

    }

    /**
     * zdd 只是针对登簿人和登簿时间赋值
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/turnProjectEventDbr", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventDbr(@RequestParam(value = "proid", required = false) String proid,
                                    @RequestParam(value = "activityid", required = false) String activityid,
                                    @RequestParam(value = "userid", required = false) String userid,
                                    @RequestParam(value = "taskid", required = false) String taskid,
                                    @RequestParam(value = "dbrRead", required = false) String dbrRead,
                                    @RequestParam(value = "defaultUserId", required = false) String defaultUserId,
                                    @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        //是否转发到缮证节点
        Boolean isSz = true;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (!StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CFDJ_DM) && StringUtils.isNotBlank(targetActivityName) && !(StringUtils.equals(targetActivityName, Constants.WORKFLOW_SZ) || StringUtils.equals(targetActivityName, Constants.WORKFLOW_DB))) {
                isSz = false;
            }
        }
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 盐城不需要到缮证才登簿，有节点id
         */
        if (isSz) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            List<BdcXm> xmList = new ArrayList<BdcXm>();
            //lst 添加批量操作 对原逻辑不影响
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            } else {
                xmList.add(bdcXm);
            }

            Date dbsj = CalendarUtil.getCurHMSDate();
            List<TurnProjectEventDbrThread> turnProjectEventDbrThreadList = new ArrayList<>();
            for (BdcXm xm : xmList) {
                TurnProjectEventDbrThread turnProjectEventDbrThread = new TurnProjectEventDbrThread(qllxService, bdcXmService, entityMapper, xm, userid, dbsj, defaultUserId, dbrRead);
                turnProjectEventDbrThreadList.add(turnProjectEventDbrThread);
            }
            if (CollectionUtils.isNotEmpty(turnProjectEventDbrThreadList)) {
                bdcThreadEngine.excuteThread(turnProjectEventDbrThreadList);
            }
        }

    }

    /**
     * zx 只是针对缮证人赋值
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/turnProjectEventSzr", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventSzr(@RequestParam(value = "proid", required = false) String
                                            proid, @RequestParam(value = "activityid", required = false) String
                                            activityid, @RequestParam(value = "userid", required = false) String
                                            userid, @RequestParam(value = "taskid", required = false) String
                                            taskid, @RequestParam(value = "dbrRead", required = false) String
                                            dbrRead, @RequestParam(value = "defaultUserId", required = false) String defaultUserId) {
        if (StringUtils.isNotBlank(proid)) {
            //lst 添加批量操作 对原逻辑不影响
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                for (BdcXm xm : xmList) {
                    bdcZsService.updateSzrByProid(xm.getProid(), userid, defaultUserId, dbrRead);
                }
            } else {
                bdcZsService.updateSzrByProid(proid, userid, defaultUserId, dbrRead);
            }
        }
    }


    /**
     * jyl  针对特定节点自动签名
     *
     * @param proid
     * @param signKey
     */
    @RequestMapping(value = "/turnProjectEventSign", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventSign(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "userid", required = false) String userid, @RequestParam(value = "signKey", required = false) String signKey, @RequestParam(value = "taskid", required = false) String taskid) {
        if (StringUtils.isNotBlank(signKey) && StringUtils.isNotBlank(proid)) {
            List<PfSignVo> signList = sysSignService.getSignList(signKey, proid);
            if (CollectionUtils.isEmpty(signList)) {
                PfSignVo signVo = new PfSignVo();
                signVo.setProId(proid);
                signVo.setSignKey(signKey);
                signVo.setSignId(UUIDGenerator.generate());
                signVo.setSignDate(Calendar.getInstance().getTime());
                signVo.setSignType("1");
                //获取默认意见
                String activityName = "";
                String wfName = "";
                if (StringUtils.isNotBlank(taskid)) {
                    activityName = PlatformUtil.getPfActivityNameByTaskId(taskid);
                }
                if (StringUtils.isNotBlank(proid)) {
                    wfName = PlatformUtil.getWorkFlowNameByProid(proid);
                }
                Example example = new Example(BdcXtOpinion.class);
                if (StringUtils.isNotBlank(wfName) && StringUtils.isNotBlank(activityName)) {
                    example.createCriteria().andEqualTo("workflowname", wfName).andEqualTo("activitytype", activityName);
                } else if (StringUtils.isNotBlank(wfName)) {
                    example.createCriteria().andEqualTo("workflowname", wfName);
                }
                String option = "";
                if (CollectionUtils.isNotEmpty(example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())) {
                    List<BdcXtOpinion> bdcXtOpinionList = entityMapper.selectByExample(BdcXtOpinion.class, example);
                    if (CollectionUtils.isNotEmpty(bdcXtOpinionList)) {
                        option = bdcXtOpinionList.get(0).getContent();
                    }
                }
                signVo.setSignOpinion(option);
                if (StringUtils.isNotBlank(userid)) {
                    String userName = getUserNameById(userid);
                    signVo.setUserId(userid);
                    if (StringUtils.isNotBlank(userName)) {
                        signVo.setSignName(userName);
                    }
                }
                sysSignService.insertAutoSign(signVo);
            }
        }
    }


    /**
     * zdd 只是针对抵押注销登簿人和注销登簿时间赋值
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/turnProjectEventDyZxDbr", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventDyZxDbr(@RequestParam(value = "proid", required = false) String proid,
                                        @RequestParam(value = "activityid", required = false) String activityid,
                                        @RequestParam(value = "userid", required = false) String userid,
                                        @RequestParam(value = "taskid", required = false) String taskid,
                                        @RequestParam(value = "dbrRead", required = false) String dbrRead,
                                        @RequestParam(value = "defaultUserId", required = false) String defaultUserId) {

        List<BdcXm> bdcXmList = null;
        BdcXm xm = bdcXmService.getBdcXmByProid(proid);
        if (xm != null && StringUtils.isNotBlank(xm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(xm.getWiid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXmTemp : bdcXmList) {
                List<String> yProidList = bdcXmRelService.getYproid(bdcXmTemp.getProid());
                if (CollectionUtils.isNotEmpty(yProidList)) {
                    for (String yProid : yProidList) {
                        if (StringUtils.isNotBlank(yProid)) {
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(yProid);
                            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                            QllxVo yQllxVo = null;
                            if (qllxVo != null) {
                                yQllxVo = qllxService.queryQllxVo(qllxVo, yProid);
                                if (yQllxVo == null && bdcXmTemp != null) {
                                    qllxVo = qllxService.queryQllxVo(bdcXmTemp);
                                }
                            } else {
                                qllxVo = qllxService.getQllxVoByProid(yProid);
                                if (null == qllxVo && (StringUtils.equals(Constants.SQLX_PLDYZX, bdcXmTemp.getSqlx()) ||
                                        StringUtils.equals(Constants.SQLX_DYZX, bdcXmTemp.getSqlx()) ||
                                        StringUtils.equals(Constants.SQLX_JSYDSYQDYAQ_ZX, bdcXmTemp.getSqlx()) ||
                                        StringUtils.equals(Constants.SQLX_JSYDSYQ_ZX, bdcXmTemp.getSqlx()) ||
                                        StringUtils.equals(Constants.SQLX_JSYDGZWSYQ_ZX, bdcXmTemp.getSqlx())) ||
                                        StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_PLJF) ||
                                        StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_JF)) {
                                    qllxVo = qllxService.queryQllxVo(bdcXmTemp);
                                }
                            }
                            if (yQllxVo != null) {
                                yQllxVo = qllxService.updateZxDbr(yQllxVo, userid, defaultUserId, dbrRead, proid);
                                entityMapper.updateByPrimaryKeySelective(yQllxVo);

                            } else if (qllxVo instanceof BdcDyaq || qllxVo instanceof BdcCf) {
                                qllxVo = qllxService.updateZxDbr(qllxVo, userid, defaultUserId, dbrRead, proid);
                                entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
                            }
                        }
                    }
                }
                //批量抵押部分注销换证的，当前流程被注销的权利
                if (StringUtils.equals(Constants.DJLX_PLDYBG_YBZS_SQLXDM, bdcXmTemp.getSqlx())) {
                    QllxVo qllxVo = qllxService.queryQllxVo(bdcXmTemp);
                    if (null != qllxVo && Constants.QLLX_QSZT_HR == qllxVo.getQszt()) {
                        qllxVo = qllxService.updateZxDbr(qllxVo, userid, defaultUserId, dbrRead, proid);
                        entityMapper.saveOrUpdate(qllxVo, qllxVo.getQlid());
                    }
                }
                //抵押（原他项证）注销登记，过渡查封注销登记处理注销登簿人等信息
                if (StringUtils.equals(Constants.SQLX_GDDYZX_DM, bdcXmTemp.getSqlx()) || StringUtils.equals(Constants.SQLX_SFCD, bdcXmTemp.getSqlx())
                        || StringUtils.equals(Constants.SQLX_SFCD_PL, bdcXmTemp.getSqlx())
                        || StringUtils.equals(Constants.SQLX_FWJF_DM, bdcXmTemp.getSqlx())
                        || StringUtils.equals(Constants.SQLX_TDJF_DM, bdcXmTemp.getSqlx())
                        || StringUtils.equals(Constants.SQLX_PLGDDYZX_DM, bdcXmTemp.getSqlx())) {
                    qllxService.updateGdZxDbr(bdcXmTemp, userid, defaultUserId, dbrRead, proid);
                }
            }
        }
    }

    /**
     * liujie 不予受理节点转发事件，生成不予受理通知书
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/turnProjectEventBysl", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventBysl(@RequestParam(value = "proid", required = false) String proid,
                                     @RequestParam(value = "activityid", required = false) String activityid,
                                     @RequestParam(value = "userid", required = false) String userid,
                                     @RequestParam(value = "taskid", required = false) String taskid) {

        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcByslService.creatBdcBysltzs(bdcXm, userid);

            //改变项目状态为不予受理
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                for (BdcXm xm : xmList) {
                    bdcXmService.changeXmzt(xm.getProid(), Constants.XMZT_BYSL);
                }
            } else {
                bdcXmService.changeXmzt(proid, Constants.XMZT_BYSL);
            }
        }
    }

    /**
     * liujie 不予受理通知书节点退回事件，删除不予受理通知书
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/backByslEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void backByslEvent(@RequestParam(value = "proid", required = false) String proid,
                              @RequestParam(value = "activityid", required = false) String activityid,
                              @RequestParam(value = "userid", required = false) String userid,
                              @RequestParam(value = "taskid", required = false) String taskid) {

        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcByslService.deleteBdcBysltzs(bdcXm);

            //改变项目状态为临时
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                for (BdcXm xm : xmList) {
                    bdcXmService.changeXmzt(xm.getProid(), Constants.XMZT_LS);
                }
            } else {
                bdcXmService.changeXmzt(proid, Constants.XMZT_LS);
            }
        }
    }

    /**
     * liujie 不予登记节点转发事件，生成不动产不予登记登记单
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/turnProjectEventBydj", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventBydj(@RequestParam(value = "proid", required = false) String proid,
                                     @RequestParam(value = "activityid", required = false) String activityid,
                                     @RequestParam(value = "userid", required = false) String userid,
                                     @RequestParam(value = "taskid", required = false) String taskid) {

        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcBydjService.creatBdcBydjdjd(bdcXm, userid);

            //改变项目状态为不予登记
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                for (BdcXm xm : xmList) {
                    bdcXmService.changeXmzt(xm.getProid(), Constants.XMZT_BYDJ);
                }
            } else {
                bdcXmService.changeXmzt(proid, Constants.XMZT_BYDJ);
            }
        }
    }

    /**
     * liujie 不予登记登记单节点退回事件，删除不动产不予登记登记单
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/backBydjEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void backBydjEvent(@RequestParam(value = "proid", required = false) String proid,
                              @RequestParam(value = "activityid", required = false) String activityid,
                              @RequestParam(value = "userid", required = false) String userid,
                              @RequestParam(value = "taskid", required = false) String taskid) {

        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcBydjService.deleteBdcBydjdjd(bdcXm);

            //改变项目状态为临时
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                for (BdcXm xm : xmList) {
                    bdcXmService.changeXmzt(xm.getProid(), Constants.XMZT_LS);
                }
            } else {
                bdcXmService.changeXmzt(proid, Constants.XMZT_LS);
            }
        }
    }


    /**
     * liujie 转发到补齐补正节点事件，生成不动产补正材料通知书
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/turnProjectEventBqbz", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventBqbz(@RequestParam(value = "proid", required = false) String proid,
                                     @RequestParam(value = "activityid", required = false) String activityid,
                                     @RequestParam(value = "userid", required = false) String userid,
                                     @RequestParam(value = "taskid", required = false) String taskid,
                                     @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {


        //是否转发到补齐补正节点
        Boolean isBqbz = false;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (StringUtils.isNotBlank(targetActivityName) && StringUtils.equals(targetActivityName, Constants.WORKFLOW_BQBZ)) {
                isBqbz = true;
            }
        }

        if (isBqbz && StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcBqbzService.creatBdcBzcltzs(bdcXm, userid);
        }

    }

    /**
     * liujie 补齐补正节点退回事件，删除不动产补正材料通知书
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/backBqbzEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void backBqbzEvent(@RequestParam(value = "proid", required = false) String proid,
                              @RequestParam(value = "activityid", required = false) String activityid,
                              @RequestParam(value = "userid", required = false) String userid,
                              @RequestParam(value = "taskid", required = false) String taskid) {

        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcBqbzService.deleteBdcBzcltzs(bdcXm);
        }
    }

    @RequestMapping(value = "/creatZs", method = {RequestMethod.POST, RequestMethod.GET})
    public void creatZs(@RequestParam(value = "proid", required = false) String proid,
                        @RequestParam(value = "activityid", required = false) String activityid,
                        @RequestParam(value = "userid", required = false) String userid,
                        @RequestParam(value = "taskid", required = false) String taskid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
            projectService.creatZs(turnProjectDefaultService, bdcXm);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/beforeTurnProjectEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void beforeTurnProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                       @RequestParam(value = "activityid", required = false) String activityid,
                                       @RequestParam(value = "userid", required = false) String userid,
                                       @RequestParam(value = "taskid", required = false) String taskid,
                                       HttpServletResponse response) {
        String autoSignFlag = AppConfig.getProperty("autoSign.enable");
        if (StringUtils.equals(autoSignFlag, ParamsConstants.TRUE_LOWERCASE)) {
            String workflowNodeName = PlatformUtil.getPfActivityNameByTaskId(taskid);
            //昆山签名节点转发自动签名
            List<AutoSignWfNodeName> autoSignWfNodeNameList = ReadJsonUtil.getWorkFlowNodeName();
            if (CollectionUtils.isNotEmpty(autoSignWfNodeNameList) && StringUtils.isNotBlank(autoSignWfNodeNameList.get(0).getTotalNodeName())) {
                String[] totalNodeNameArray = autoSignWfNodeNameList.get(0).getTotalNodeName().split(",");
                if (CommonUtil.indexOfStrs(totalNodeNameArray, workflowNodeName)) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                        //分割合并流程每个项目都需要签字
                        if (CommonUtil.indexOfStrs(Constants.SQLX_SIGNMUL, bdcXm.getSqlx())) {
                            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                                for (BdcXm bdcXmTemp : bdcXmList) {
                                    signService.autoSignBeforeTurn(bdcXmTemp.getProid(), userid, workflowNodeName);
                                }
                            }
                        } else {
                            signService.autoSignBeforeTurn(proid, userid, workflowNodeName);
                        }
                    }
                }

            }
        }
        String str = "";
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            List<Map> list = null;
            String btxyzMultithreading = AppConfig.getProperty("btxyz.multithreading");
            if (StringUtils.equals(btxyzMultithreading, ParamsConstants.TRUE_LOWERCASE)) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                String workFlowId = pfWorkFlowInstanceVo.getWorkflowDefinitionId();
                String workflowNodeId = PlatformUtil.getPfActivityIdByTaskId(taskid);
                list = bdcBtxyzService.btxyz(proid, workFlowId, workflowNodeId);
            } else {
                list = bdcXtLimitfieldService.validateMsg(taskid, proid);
            }
            if (CollectionUtils.isNotEmpty(list)) {
                for (Map map : list) {
                    if (map.containsKey(ParamsConstants.ERROR_LOWERCASE) && map.get(ParamsConstants.ERROR_LOWERCASE) != null && StringUtils.isNotBlank(map.get(ParamsConstants.ERROR_LOWERCASE).toString())) {
                        if (StringUtils.isNotBlank(str)) {
                            str = str + "；" + map.get(ParamsConstants.ERROR_LOWERCASE).toString();
                        } else {
                            str = map.get(ParamsConstants.ERROR_LOWERCASE).toString();
                        }
                    }

                }
            }
            //ZDD 只验证alert的
            if (StringUtils.isBlank(str)) {
                List<String> unExamineSqlx = ReadXmlProps.getUnExamineSqlxDm();
                if (CollectionUtils.isEmpty(unExamineSqlx) || (CollectionUtils.isNotEmpty(unExamineSqlx) && !unExamineSqlx.contains(bdcXm.getSqlx()))) {
                    List<BdcExamineParam> bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(bdcXm.getWiid(), null);
                    Map<String, Object> examineMap = bdcExamineService.performExamine(bdcExamineParamList, bdcXm.getWiid(), bdcXm.getSqlx());
                    if (examineMap != null && examineMap.containsKey("info") && examineMap.get("info") != null) {
                        str = StringUtils.indexOf(examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString(), "<br/>") > -1 ? StringUtils.replace(examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString(), "<br/>", "\\n") : examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString();
                    }
                }
                if (StringUtils.isBlank(str)) {
                    List<Map<String, Object>> checkMsg = new ArrayList<Map<String, Object>>();
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (bdcXmList != null && bdcXmList.size() > 10) {
                        /**
                         * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
                         * @description 批量流程项目个数大于10的采用多线程处理验证
                         */
                        List<BdcCheckThread> bdcCheckThreadList = new ArrayList<BdcCheckThread>();
                        for (BdcXm bdcXmTemp : bdcXmList) {
                            Project project = new Project();
                            project.setProid(bdcXmTemp.getProid());
                            BdcCheckThread bdcCheckThread = new BdcCheckThread(checkXmService, projectCheckInfoService, true, project);
                            bdcCheckThreadList.add(bdcCheckThread);
                        }
                        bdcThreadEngine.excuteThread(bdcCheckThreadList);
                        for (BdcCheckThread bdcCheckThread : bdcCheckThreadList) {
                            List<Map<String, Object>> singleCheckThreadReuslt = bdcCheckThread.getResult();
                            if (CollectionUtils.isNotEmpty(singleCheckThreadReuslt)) {
                                checkMsg.addAll(singleCheckThreadReuslt);
                            }
                        }
                    } else {
                        checkMsg.addAll(projectCheckInfoService.checkXm(proid, false, ParamsConstants.ALERT_LOWERCASE, userid, taskid));
                    }

                    if (CollectionUtils.isNotEmpty(checkMsg)) {
                        for (Map<String, Object> map : checkMsg) {
                            if (map.containsKey(ParamsConstants.CHECKMODEL_HUMP) && map.get(ParamsConstants.CHECKMODEL_HUMP).toString().toUpperCase().equals("ALERT")) {
                                str = map.get(ParamsConstants.CHECKMSG_HUMP).toString();
                                break;
                            }
                        }
                    }
                }
            }
        }

        try {
            response.setContentType(PARAMETER_CHARSET_GBK);
            PrintWriter out = response.getWriter();
            out.println(str);
            out.flush();
            out.close();

        } catch (Exception e) {
            logger.error("WfProjectController.beforeTurnProjectEvent", e);
        }
    }

    /**
     * 改变原权利状态事件为历史
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/changeYqlZtEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void changeYqlZtEvent(@RequestParam(value = "proid", required = false) String proid,
                                 @RequestParam(value = "activityid", required = false) String activityid,
                                 @RequestParam(value = "userid", required = false) String userid,
                                 @RequestParam(value = "taskid", required = false) String taskid,
                                 @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) throws Exception {


        //是否转发到缮证节点
        Boolean isSz = true;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (StringUtils.isNotBlank(targetActivityName) && !(StringUtils.equals(targetActivityName, Constants.WORKFLOW_SZ) || StringUtils.equals(targetActivityName, Constants.WORKFLOW_DB))) {
                isSz = false;
            }
        }

        if (isSz || StringUtils.equals(dwdm, "320900")) {
            String wiid = "";
            if (pfWorkFlowInstanceVo != null) {
                wiid = pfWorkFlowInstanceVo.getWorkflowIntanceId();
            }
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcChangeYqlztThread> bdcChangeYqlztThreadList = new ArrayList<BdcChangeYqlztThread>();
                for (BdcXm bdcXm : bdcXmList) {
                    if (bdcXm != null) {
                        EndProjectService endProjectDefaultServiceImpl = projectService.getEndProjectService(bdcXm);
                        BdcChangeYqlztThread bdcChangeYqlztThread = new BdcChangeYqlztThread(bdcZsbhService, projectService, endProjectDefaultServiceImpl, bdcXm);
                        bdcChangeYqlztThreadList.add(bdcChangeYqlztThread);
                    }
                }
                if (CollectionUtils.isNotEmpty(bdcChangeYqlztThreadList)) {
                    bdcThreadEngine.excuteThread(bdcChangeYqlztThreadList);
                }
            }
        }
    }

    /**
     * hqz 登薄现势退回事件
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/backYqlZtEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void backYqlZtEvent(@RequestParam(value = "proid", required = false) String proid,
                               @RequestParam(value = "activityid", required = false) String activityid,
                               @RequestParam(value = "userid", required = false) String userid,
                               @RequestParam(value = "taskid", required = false) String taskid) {
        BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
        if (bdcXmTemp != null && StringUtils.isNotBlank(bdcXmTemp.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXmTemp.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    if (bdcXm != null) {
                        bdcZsbhService.changeZsSyqk(bdcXm);
                        EndProjectService endProjectDefaultServiceImpl = projectService.getEndProjectService(bdcXm);
                        projectService.backProjectEvent(endProjectDefaultServiceImpl, bdcXm);
                        //改变xmzt为零时
                        bdcXm.setXmzt(Constants.XMZT_LS);
                        entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                    }
                }
            }
            //土地分割合并换证退回将注销的原土地的抵押还原
            bdcComplexFgHbHzService.changeYzdDyQszt(bdcXmTemp.getWiid());
        }
    }


    /**
     * 退回改变原权利状态为现势
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     */
    @RequestMapping(value = "/changeYqlZtqhEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void changeYqlZtqhEvent(@RequestParam(value = "proid", required = false) String proid,
                                   @RequestParam(value = "activityid", required = false) String activityid,
                                   @RequestParam(value = "userid", required = false) String userid,
                                   @RequestParam(value = "taskid", required = false) String taskid,
                                   @RequestParam(value = "qszt", required = false) String qszt) {
        if (StringUtils.isNotBlank(qszt)) {
            return;
        }
        BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
        List<BdcXm> bdcXmList = null;
        if (bdcXmTemp != null && StringUtils.isNotBlank(bdcXmTemp.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXmTemp.getWiid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                if (bdcXm != null) {
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        for (BdcXmRel bdcXmRel : bdcXmRelList) {
                            if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)) {
                                qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                            } else {
                                if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                                    gdXmService.updateGdQszt(bdcXmRel.getYqlid(), 0);
                                }
                            }
                        }
                    }
                    /**
                     * 商品房首次登记（开发商自持），退回的时候将当前权利变为QLLX_QSZT_LS
                     */
                    if (Constants.SQLX_SPFSCKFSZC_DM.equals(bdcXm.getSqlx()) || Constants.SQLX_SPFXZBG_DM.equals(bdcXm.getSqlx())) {
                        qllxService.changeQllxZt(bdcXm.getProid(), Constants.QLLX_QSZT_LS);
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/endProjectEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void endProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                @RequestParam(value = "activityid", required = false) String activityid,
                                @RequestParam(value = "userid", required = false) String userid,
                                @RequestParam(value = "taskid", required = false) String taskid) throws Exception {

        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            String sjppType = AppConfig.getProperty("sjpp.type");
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                BdcXm bdcXmTemp = bdcXmList.get(0);
                EndProjectService endProjectDefaultService = projectService.getEndProjectService(bdcXmTemp);
                if (StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_PLFZ_DM)) {
                    //将证书状态从已使用变为已打证
                    bdcZsbhService.batchChangeZsSyqk(bdcXmList);
                    //办结在项目表中保存领证日期
                    bdcXmService.batchUpdateBdcXmLzrq(bdcXmList, new Date());
                    projectService.batchEndProjectEvent(endProjectDefaultService, bdcXmList);
                    //办结转发的时间作为领证日期
                    bdcZsService.batchUpateBdcZsLzrqByzsidList(bdcXmList, new Date());
                } else {
                    for (BdcXm bdcXm1 : bdcXmList) {
                        if (bdcXm1 != null) {
                            if (!StringUtils.equals(bdcXm1.getXmzt(), Constants.XMZT_BYDJ) && !StringUtils.equals(bdcXm1.getXmzt(), Constants.XMZT_BYSL)) {
                                //zwq 将证书状态从已使用变为已打证
                                bdcZsbhService.changeZsSyqk(bdcXm1);
                                //jyl 办结独立——林权,水产养殖权
                                EndProjectService endProjectDefaultServiceImpl = projectService.getEndProjectService(bdcXm1);
                                projectService.endProjectEvent(endProjectDefaultServiceImpl, bdcXm1);

                                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm1.getProid());
                                if ((StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_FWCF_DM) || StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_FWJF_DM))
                                        && CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                    gdFwService.updateGdXmPpzt(bdcXmRelList.get(0).getYproid(), "0");
                                }
                                //zhouwanqing 过程匹配改变匹配状态
                                if (StringUtils.equals(sjppType, Constants.PPLX_GC)
                                        && CollectionUtils.isNotEmpty(bdcXmRelList)
                                        && !StringUtils.equals(bdcXmRelList.get(0).getYdjxmly(), Constants.XMLY_BDC)) {
                                    gdFwService.updateGdXmPpzt(bdcXmRelList.get(0).getYproid(), "4");
                                }

                                //jiangganzhi 记录办结转发的时间作为领证日期
                                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm1.getProid());
                                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                                    BdcZs bdcZs = bdcZsList.get(0);
                                    bdcZs.setLzrq(DateUtils.now());
                                    entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                                }
                                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_JCXZ)) {
                                    bdcBdcZsSdService.changeXzzt(bdcXm1.getProid(), Constants.XZZT_ZC, userid);
                                }
                            }
                        }
                    }
                }
            }

            //同步登记图形库(注销登记删除图形库数据)
            if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_ZXDJ_DM)) {
                registerGraphicService.deleteDjtxkSj(bdcXm);
            }

        }

    }

    /**
     * 批量办结工作流事件
     *
     * @param proid
     * @return
     */
    @RequestMapping(value = "/batchEndProjectEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void batchEndProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                     @RequestParam(value = "activityid", required = false) String activityid,
                                     @RequestParam(value = "userid", required = false) String userid,
                                     @RequestParam(value = "taskid", required = false) String taskid) throws Exception {

        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        List<BdcXm> bdcXmList = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            BdcXm bdcXmTemp = bdcXmList.get(0);
            EndProjectService endProjectDefaultServiceImpl = projectService.getEndProjectService(bdcXmTemp);
            projectService.batchEndProjectEvent(endProjectDefaultServiceImpl, bdcXmList);

            //办结转发的时间作为领证日期
            bdcZsService.batchUpateBdcZsLzrqByzsidList(bdcXmList, new Date());
        }

        //同步登记图形库(注销登记删除图形库数据)
        if (bdcXm != null && StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_ZXDJ_DM)) {
            registerGraphicService.deleteDjtxkSj(bdcXm);
        }
    }

    @RequestMapping(value = "/postBdcXmToArchive", method = {RequestMethod.POST, RequestMethod.GET})
    public void postBdcXmInfoToArchive(@RequestParam(value = "proid", required = false) String proid,
                                       @RequestParam(value = "activityid", required = false) String activityid,
                                       @RequestParam(value = "userid", required = false) String userid,
                                       @RequestParam(value = "taskid", required = false) String taskid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            archivePostService.postBdcXmInfoNew(bdcXm);
        }
    }

    @RequestMapping(value = "/updateWorkFlow")
    public void updateWorkFlow(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        BdcXm bdcXm = null;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        } else if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList))
                for (BdcXm bdcXm1 : bdcXmList) {
                    if (StringUtils.isNotBlank(bdcXm1.getSqlx()) && !StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_DY_GDDY) && !StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_CF_GDCF)) {
                        bdcXm = bdcXm1;
                        break;
                    }
                }
        }
        if (bdcXm != null) {
            CreatProjectService creatProjectService = projectService.getCreatProjectService(bdcXm);
            creatProjectService.updateWorkFlow(bdcXm);
        }
    }

    /**
     * 验证不动产项目
     *
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/checkBdcXm")
    public List<Map<String, Object>> checkBdcXm(Model model, Project project) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        //选择楼盘表某些户室可能存在不动产单元号$拼接，导致只验证最后选择的户室，此时应该是批量验证
        List<String> bdcdyhList = project.getBdcdyhs();
        List<String> bdcdyhs = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(bdcdyhList) && bdcdyhList.size() == 1) {
            String bdcdyh = bdcdyhList.get(0);
            if (bdcdyh.indexOf(Constants.SPLIT_STR) >= 0) {
                String[] bdcdyhArr = bdcdyh.split("\\$");
                for (String bdcdyhtemp : bdcdyhArr) {
                    bdcdyhs.add(bdcdyhtemp);
                }
            }
            project.setBdcdyhs(bdcdyhs);
        }
        if (project.getBdcdyhs() != null && project.getBdcdyhs().size() > 1) {
            resultList = checkMulBdcXm(null, project);
            return resultList;
        }
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
        List<String> unExamineSqlx = ReadXmlProps.getUnExamineSqlxDm();
        if (CollectionUtils.isEmpty(unExamineSqlx) || (CollectionUtils.isNotEmpty(unExamineSqlx) && bdcXm != null && !unExamineSqlx.contains(bdcXm.getSqlx()))) {
            List<BdcExamineParam> bdcExamineParamList = new ArrayList<BdcExamineParam>();
            List<String> dcbIndexList = new ArrayList<String>();
            if (bdcXm != null) {
                if (StringUtils.isNotBlank(project.getDcbIndex())) {
                    dcbIndexList.add(project.getDcbIndex());
                } else {
                    bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(bdcXm.getWiid(), project);
                }
            } else {
                bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(StringUtils.EMPTY, project);
            }
            Map<String, Object> examineMap = null;
            if (CollectionUtils.isNotEmpty(dcbIndexList)) {
                examineMap = bdcExamineService.performExamineLjz(dcbIndexList, bdcXm.getWiid());
            } else {
                examineMap = bdcExamineService.performExamine(bdcExamineParamList, bdcXm.getWiid());
            }
            if (examineMap != null && examineMap.containsKey("info") && examineMap.get("info") != null) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                returnMap.put("info", examineMap.get("info"));
                returnMap.put(ParamsConstants.CHECKMODEL_HUMP, "alert");
                returnMap.put(ParamsConstants.CHECKMSG_HUMP, examineMap.get(ParamsConstants.CHECKMSG_HUMP));
                returnMap.put("checkPorids", examineMap.get("info"));
                //苏州无查封创建例外 故注释 避免进入是否例外界面
//                returnMap.put("wiid", bdcXm.getWiid());
                if (examineMap.get("xzwh") != null) {
                    returnMap.put("xzwh", examineMap.get("xzwh").toString());
                }
                if (examineMap.get(ParamsConstants.EXAMINEINFO_HUMP) != null) {
                    returnMap.put(ParamsConstants.EXAMINEINFO_HUMP, examineMap.get(ParamsConstants.EXAMINEINFO_HUMP));
                }
                resultList.add(returnMap);
                return resultList;
            }
        }
        BdcXm ybdcXm = new BdcXm();
        if (StringUtils.isNotBlank(project.getYxmid())) {
            ybdcXm = bdcXmService.getBdcXmByProid(project.getYxmid());
        }
        if (bdcXm != null) {
            Example example = new Example(BdcXtCheckinfo.class);
            //zdd 如果都为空  则不验证
            if (StringUtils.isNotBlank(bdcXm.getSqlx()) || StringUtils.isNotBlank(bdcXm.getQllx())) {
                Example.Criteria criteria = example.createCriteria();
                if (StringUtils.isNotBlank(bdcXm.getSqlx())) {
                    criteria.andEqualTo("sqlxdm", bdcXm.getSqlx());
                }
                if (StringUtils.isNotBlank(bdcXm.getQllx())) {
                    criteria.andEqualTo("qllxdm", bdcXm.getQllx());
                } else if (ybdcXm != null && StringUtils.isNotBlank(ybdcXm.getQllx())) {
                    criteria.andEqualTo("qllxdm", ybdcXm.getQllx());
                }
                if (StringUtils.isBlank(project.getSqlx())) {
                    project.setSqlx(bdcXm.getSqlx());
                }
                criteria.andNotEqualNvlTo("checkType", "2", "0");
                List<BdcXtCheckinfo> list = bdcXtCheckinfoService.getXtCheckinfo(example);
                List<Map<String, Object>> resultListTemp = projectCheckInfoService.checkXm(list, project);
                if (CollectionUtils.isNotEmpty(resultListTemp)) {
                    resultList = resultListTemp;
                }
            }
        }
        return resultList;
    }

    /**
     * @author bianwen
     * @description 选择逻辑幢, 验证所有户室
     */
    @ResponseBody
    @RequestMapping(value = "/checkLjz")
    public List<Map<String, Object>> checkLjz(Project project) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
            HashMap map = new HashMap();
            List<String> djsjIds = new ArrayList<String>();
            List<String> bdcdys = new ArrayList<String>();

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
                    bdcdys.add(djsjFwHs.getBdcdyh());
                }
                project.setBdcdyhs(bdcdys);
                resultList = checkMulBdcXm(null, project);
            }
        }
        return resultList;
    }

    /**
     * @author bianwen
     * @description 在建工程抵押或批量抵押选一条权利验证所有
     */
    @ResponseBody
    @RequestMapping(value = "/checkBdcXmForDyChoseOne")
    public List<Map<String, Object>> checkBdcXmForDyChoseOne(Project project) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        String proids = bdcXmService.getProidsByProid(project.getYxmid());
        List<String> yxmidList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        if (StringUtils.isNotBlank(proids)) {
            for (String proid : StringUtils.split(proids, Constants.SPLIT_STR)) {
                yxmidList.add(proid);
                bdcdyhList.add(bdcdyService.getBdcdyhByProid(proid));
            }
            project.setBdcdyhs(bdcdyhList);
            project.setYxmids(yxmidList);
            resultList = checkMulBdcXm(null, project);
        }
        return resultList;
    }

    /**
     * 验证不动产项目 批量的
     *
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "checkMulBdcXm")
    public List<Map<String, Object>> checkMulBdcXm(Model model, Project project) {
        List<Map<String, Object>> resultListTemp = Lists.newArrayList();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
        List<String> unExamineSqlx = ReadXmlProps.getUnExamineSqlxDm();
        if (CollectionUtils.isEmpty(unExamineSqlx) || (CollectionUtils.isNotEmpty(unExamineSqlx) && bdcXm != null && !unExamineSqlx.contains(bdcXm.getSqlx()))) {
            List<String> dcbIndexList = new ArrayList<String>();
            List<BdcExamineParam> bdcExamineParamList = null;
            if (bdcXm != null) {
                if (StringUtils.isNotBlank(project.getDcbIndex())) {
                    dcbIndexList.add(project.getDcbIndex());
                } else if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
                    for (String dcbIndex : project.getDcbIndexs()) {
                        if (StringUtils.contains(dcbIndex, Constants.SPLIT_STR)) {
                            dcbIndexList.addAll(Arrays.asList(StringUtils.split(dcbIndex, Constants.SPLIT_STR)));
                        } else {
                            dcbIndexList.add(dcbIndex);
                        }
                    }
                } else {
                    bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(bdcXm.getWiid(), project);
                }
            } else {
                bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(StringUtils.EMPTY, project);
            }
            Map<String, Object> examineMap = null;
            if (CollectionUtils.isNotEmpty(dcbIndexList)) {
                examineMap = bdcExamineService.performExamineLjz(dcbIndexList, bdcXm.getWiid());
            } else {
                examineMap = bdcExamineService.performExamine(bdcExamineParamList, bdcXm.getWiid());
            }
            if (examineMap != null && examineMap.containsKey("info") && examineMap.get("info") != null) {
                Map<String, Object> returnMap = new HashMap<String, Object>();
                returnMap.put("info", examineMap.get("info"));
                returnMap.put(ParamsConstants.CHECKMODEL_HUMP, "alert");
                returnMap.put(ParamsConstants.CHECKMSG_HUMP, examineMap.get(ParamsConstants.CHECKMSG_HUMP));
                returnMap.put("checkPorids", examineMap.get("info"));
                returnMap.put("wiid", bdcXm.getWiid());
                if (examineMap.get("xzwh") != null) {
                    returnMap.put("xzwh", examineMap.get("xzwh").toString());
                }
                if (examineMap.get(ParamsConstants.EXAMINEINFO_HUMP) != null) {
                    returnMap.put(ParamsConstants.EXAMINEINFO_HUMP, examineMap.get(ParamsConstants.EXAMINEINFO_HUMP));
                }
                resultListTemp.add(returnMap);
                return resultListTemp;
            }
        }
        long startTime = System.currentTimeMillis();//记录开始时
        if (CollectionUtils.isNotEmpty(project.getBdcdyhs())) {
            List<String> yxmidList = project.getYxmids();
            List<String> bdcdyhList = project.getBdcdyhs();
            //多线程服务参数
            List<BdcCheckThread> bdcCheckThreadList = new ArrayList<BdcCheckThread>();
            for (int i = 0; i < bdcdyhList.size(); i++) {
                if (yxmidList != null && yxmidList.size() > i) {
                    project.setYxmid(yxmidList.get(i));
                }
                project.setBdcdyh(bdcdyhList.get(i));
                BdcXm ybdcXm = new BdcXm();
                if (StringUtils.isNotBlank(project.getYxmid())) {
                    ybdcXm = bdcXmService.getBdcXmByProid(project.getYxmid());
                }
                if (bdcXm != null && (StringUtils.isNoneBlank(bdcXm.getSqlx()) || StringUtils.isNoneBlank(bdcXm.getQllx()))) {
                    if (StringUtils.isNotBlank(bdcXm.getSqlx())) {
                        project.setSqlx(bdcXm.getSqlx());
                    }
                    if (StringUtils.isNotBlank(bdcXm.getQllx())) {
                        project.setQllx(bdcXm.getQllx());
                    } else if (StringUtils.isNotBlank(ybdcXm.getQllx())) {
                        project.setQllx(ybdcXm.getQllx());
                    }
                    if (StringUtils.isNotBlank(bdcXm.getDjlx())) {
                        project.setDjlx(bdcXm.getDjlx());
                    }
                    // 验证不动产单元是否正在办理其他登记时 会通过BH 过滤当前项目
                    if (StringUtils.isNotBlank(bdcXm.getBh())) {
                        project.setBh(bdcXm.getBh());
                    }

                    BdcCheckThread bdcCheckThread = new BdcCheckThread(checkXmService, projectCheckInfoService, false, project);
                    bdcCheckThreadList.add(bdcCheckThread);
                }
            }

            /**
             * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
             * @description 批量流程验证采用多线程处理
             */
            bdcThreadEngine.excuteThread(bdcCheckThreadList);
            for (BdcCheckThread bdcCheckThread : bdcCheckThreadList) {
                List<Map<String, Object>> singleCheckThreadReuslt = bdcCheckThread.getResult();
                if (CollectionUtils.isNotEmpty(singleCheckThreadReuslt)) {
                    resultListTemp.addAll(singleCheckThreadReuslt);
                }
            }
        }

        long endTime = System.currentTimeMillis();//记录结束时间
        float excTime = (float) (endTime - startTime) / 1000;
        logger.info("批量流程验证执行时间：" + excTime + "s");
        resultListTemp = PublicUtil.qcCheckMap(resultListTemp);
        return resultListTemp;
    }

    @ResponseBody
    @RequestMapping(value = "/delSingleXmForSpfsc", method = {RequestMethod.POST, RequestMethod.GET})
    public Map<String, String> delSingleXmForSpfsc(@RequestParam(value = "proid", required = false) String proid) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String msg = ParamsConstants.SUCCESS_LOWERCASE;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
            try {
                projectService.delProjectEvent(delProjectScdjForSingleXmImpl, bdcXmTemp);
            } catch (Exception e) {
                msg = "删除异常，请联系管理员";
            }
        }
        hashMap.put("msg", msg);
        return hashMap;
    }

    @ResponseBody
    @RequestMapping(value = "/plDelXmForSpfsc", method = {RequestMethod.POST, RequestMethod.GET})
    public Map<String, String> plDelXmForSpfsc(@RequestParam(value = "proids", required = false) String proids) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String msg = ParamsConstants.SUCCESS_LOWERCASE;
        if (StringUtils.isNotBlank(proids)) {
            String[] proidArr = StringUtils.split(proids, ",");
            for (String proid : proidArr) {
                BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
                try {
                    projectService.delProjectEvent(delProjectScdjForSingleXmImpl, bdcXmTemp);
                } catch (Exception e) {
                    msg = "删除异常，请联系管理员";
                }
            }
        }
        hashMap.put("msg", msg);
        return hashMap;
    }

    @RequestMapping(value = "/workFlowDel", method = {RequestMethod.POST, RequestMethod.GET})
    public void workFlowDel(@RequestParam(value = "proid", required = false) String proid) {
        String wiid = "";
        String sjppType = AppConfig.getProperty("sjpp.type");
        BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
        if (bdcXmTemp != null) {
            wiid = bdcXmTemp.getWiid();
        }

        //删除流程更新互联网+银行申请办理状态
        if (bdcXmTemp != null && StringUtils.isNotBlank(bdcXmTemp.getWwslbh())) {
            currencyService.changeDjzt(bdcXmTemp.getWwslbh());
        }

        /**
         * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
         * @description 调c包接口获取/推送交易信息
         */
        String jyIntoBdcSqlx = AppConfig.getProperty("jy.del.sqlxdm");
        if (StringUtils.isNotBlank(jyIntoBdcSqlx)) {
            String[] sqlxArray = jyIntoBdcSqlx.split(",");
            if (sqlxArray != null && Arrays.asList(sqlxArray).contains(bdcXmTemp.getSqlx())) {
                currencyService.tsJyFwzt(bdcXmTemp.getWiid(), Constants.JYTSZT_CX);
            }
        }
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 根据wiid删除项目产权关系表
         */
        bdcXmcqRelService.deleteBdcXmcqRelByWiid(wiid);
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (bdcXmTemp != null && StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
            projectService.delProjectEvent(projectService.getDelProjectService(bdcXmTemp), bdcXmTemp);
        } else if (bdcXmTemp != null && (StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcXmTemp.getSqlx(), Constants.SQLX_GJPTSCDJ_DM))) {
            projectService.batchDelProjectEvent(projectService.getDelProjectService(bdcXmTemp), bdcXmList, proid);
        } else {
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    if (bdcXm != null) {
                        //zhouwanqing 过程匹配改变匹配状态
                        if (StringUtils.equals(sjppType, Constants.PPLX_GC)) {
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList))
                                gdFwService.updateGdXmPpzt(bdcXmRelList.get(0).getYproid(), "2");
                        }
                        if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM)
                                || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)
                                || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM)) {
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                gdFwService.updateGdXmPpzt(bdcXmRelList.get(0).getYproid(), "0");
                                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD)
                                        || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)) {
                                    gdXmService.deleteGdCf(bdcXmRelList.get(0).getProid());
                                }
                                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM)) {
                                    gdCfService.deleteGdJf(bdcXmRelList.get(0).getYproid());
                                }
                            }
                        }
                        //jyl 删除独立——林权,水产养殖
                        projectService.delProjectEvent(projectService.getDelProjectService(bdcXm), bdcXm);

                        //土地分割合并换证删除将注销的原土地的抵押还原
                        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                            bdcComplexFgHbHzService.changeYzdDyQszt(bdcXm.getWiid());
                        }
                    }
                }
            }
        }

        try {
            configSyncQlztService.syncBdcRelateQlzt(proid);
        } catch (Exception e) {
            logger.error("更新相关证书证明号、不动产单元、共享库等状态错误", e);
        }

    }


    /**
     * 批量删除工作流事件
     *
     * @param proid
     * @return
     */
    @RequestMapping(value = "/batchWorkFlowDel", method = {RequestMethod.POST, RequestMethod.GET})
    public void batchWorkFlowDel(@RequestParam(value = "proid", required = false) String proid) {
        BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
        if (bdcXmTemp != null) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXmTemp.getWiid());
            projectService.batchDelProjectEvent(projectService.getDelProjectService(bdcXmTemp), bdcXmList, proid);
        }
    }


    @RequestMapping(value = "/turnProjectEventFz", method = {RequestMethod.POST, RequestMethod.GET})
    public void turnProjectEventFz(@RequestParam(value = "proid", required = false) String
                                           proid, @RequestParam(value = "activityid", required = false) String
                                           activityid, @RequestParam(value = "userid", required = false) String
                                           userid, @RequestParam(value = "taskid", required = false) String
                                           taskid, @RequestParam(value = "dbrRead", required = false) String
                                           dbrRead, @RequestParam(value = "defaultUserId", required = false) String defaultUserId) {
        //lst 添加批量操作 对原逻辑不影响
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        BdcZsbh bdcZsbh = new BdcZsbh();
        bdcZsbh.setLqrid(userid);
        bdcZsbh.setLqr(super.getUserNameById(userid));
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            for (BdcXm xm : xmList) {
                bdcZsbhService.changeZsBhZt(xm, bdcZsbh);
            }
        } else {
            bdcZsbhService.changeZsBhZt(bdcXm, bdcZsbh);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/beforeTurnProjectEventEnd", method = {RequestMethod.POST, RequestMethod.GET})
    public String beforeTurnProjectEventEnd(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "userid", required = false) String userid) {
        String str = "";
        if (StringUtils.isBlank(str)) {
            List<Map<String, Object>> checkMsg = projectCheckInfoService.checkXm(proid, true, ParamsConstants.ALERT_LOWERCASE, userid, "");
            if (CollectionUtils.isNotEmpty(checkMsg)) {
                for (Map<String, Object> map : checkMsg) {
                    if (map.containsKey(ParamsConstants.CHECKMODEL_HUMP)) {
                        str = map.get(ParamsConstants.CHECKMSG_HUMP).toString();
                        break;
                    }
                }
            }
        }
        return str;
    }

    /**
     * sc：缮证退回事件，用于还原证书编号
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/workFlowBackZsBh", method = {RequestMethod.POST, RequestMethod.GET})
    public String workFlowBackZsBh(@RequestParam(value = "proid", required = false) String proid) {
        String str = "";
        //lst 添加批量操作的还原证书编号
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            for (BdcXm xm : xmList) {
                bdcZsbhService.workFlowBackZsBh(xm.getProid());
                if (!StringUtils.equals(xm.getSftqsczs(), "1")) {
                    //不是提前生成证书 删除证书、合并流程删除原证号
                    bdcZsService.delBdcZsByProid(xm.getProid());
                    bdcZsService.deleteHblcYbdcqzh(proid);
                } else {
                    //提前生成证书 删除证号
                    bdcZsService.delBdcZsBdcqzhAndZsbhByProid(xm.getProid());
                }
            }
        } else {
            bdcZsbhService.workFlowBackZsBh(proid);
            if (!StringUtils.equals(bdcXm.getSftqsczs(), "1")) {
                //不是提前生成证书 删除证书、合并流程删除原证号
                bdcZsService.delBdcZsByProid(proid);
                bdcZsService.deleteHblcYbdcqzh(proid);
            } else {
                //提前生成证书 删除证号
                bdcZsService.delBdcZsBdcqzhAndZsbhByProid(proid);
            }
        }
        return str;
    }

    /**
     * sc：发证退回事件
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/workFlowBackFz", method = {RequestMethod.POST, RequestMethod.GET})
    public String workFlowBackFz(@RequestParam(value = "proid", required = false) String
                                         proid, @RequestParam(value = "userid", required = false) String
                                         userid, @RequestParam(value = "activityid", required = false) String
                                         activityid, @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        String str = "";
        fzWorkFlowBackService.fzWorkFlowBack(proid, userid, activityid, targetActivityDefids);
        return str;
    }

    /**
     * zx：查封后直接创建轮候查封
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createLhcfByProid", method = {RequestMethod.POST, RequestMethod.GET})
    public String createLhcfByProid(@RequestParam(value = "proid", required = false) String
                                            proid, @RequestParam(value = "yproid", required = false) String
                                            yproid, @RequestParam(value = "createSqlxdm", required = false) String
                                            createSqlxdm, @RequestParam(value = "bdcdyh", required = false) String
                                            bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid) {
        String msg = "";
        try {
            //删除当前项目的信息
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            projectService.delProjectEvent(projectService.getDelProjectService(bdcXm), bdcXm);
            //创建轮候查封
            Project project = new Project();
            project.setUserId(super.getUserId());
            project.setYxmid(yproid);
            //获取权利类型和登记事由、申请类型
            if (StringUtils.isNotBlank(createSqlxdm)) {
                String wfid = bdcZdGlService.getWdidBySqlxdm(createSqlxdm);
                String djlxdm = bdcZdGlService.getDjlxDmBySqlxdm(createSqlxdm);
                project.setWorkFlowDefId(wfid);
                project.setDjlx(djlxdm);
                List<BdcSqlxQllxRel> bdcSqlxQllxRelList = bdcXtConfigService.getOthersBySqlx(createSqlxdm);
                if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)) {
                    BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
                    if (bdcSqlxQllxRel != null) {
                        project.setQllx(bdcSqlxQllxRel.getQllxdm());
                        project.setSqlx(createSqlxdm);
                        project.setDjsy(bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx()));
                        project.setBdcdyid(bdcdyid);
                        project.setYbdcdyid(bdcdyid);
                        project.setBdcdyh(bdcdyh);
                    }
                }
            }
            Project returnProject = projectService.creatProjectEvent(projectService.getCreatProjectService((BdcXm) project), project);
            if (returnProject != null) {
                //获取哪个登记类型service
                BdcXm createBdcXm = bdcXmService.getBdcXmByProid(project.getProid());
                bdcSjdService.createSjxxByBdcxm(createBdcXm);
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
                turnProjectDefaultService.saveQllxVo(createBdcXm);
                msg = returnProject.getTaskid();
            }
            if (StringUtils.isBlank(msg)) {
                msg = ParamsConstants.ERROR_LOWERCASE;
            }
        } catch (Exception e) {
            msg = ParamsConstants.ERROR_LOWERCASE;
            logger.error("WfProjectController.createLhcfByProid", e);
        }

        return msg;
    }

    /**
     * sc：改变项目状态为2；
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/turnProjectEventXmzt", method = {RequestMethod.POST, RequestMethod.GET})
    public String turnProjectEventChangeXmzt(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        String str = "";

        //是否转发到缮证节点
        Boolean isSz = true;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (StringUtils.isNotBlank(targetActivityName) && !(StringUtils.equals(targetActivityName, Constants.WORKFLOW_SZ) || StringUtils.equals(targetActivityName, Constants.WORKFLOW_DB))) {
                isSz = false;
            }
        }
        if (isSz) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            List<String> djhList = new ArrayList<>();
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if (CommonUtil.indexOfStrs(Constants.BATCH_OPERATION_SQLX_DM, bdcXm.getSqlx())) {
                    bdcXmService.batchChangeXmzt(xmList, Constants.XMZT_SZ);
                    for (BdcXm xm : xmList) {
                        bdcTdService.changeGySjydZt(xm, djhList);
                    }
                } else {
                    for (BdcXm xm : xmList) {
                        bdcXmService.changeXmzt(xm.getProid(), Constants.XMZT_SZ);
                        bdcTdService.changeGySjydZt(xm, djhList);
                    }
                }
            } else {
                bdcXmService.changeXmzt(proid, Constants.XMZT_SZ);
                bdcTdService.changeGySjydZt(bdcXm, djhList);
            }
        }
        return str;
    }

    /**
     * sc：改变项目状态为0；
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/workFlowBackXmzt", method = {RequestMethod.POST, RequestMethod.GET})
    public String workFlowBackXmzt(@RequestParam(value = "proid", required = false) String proid) {
        String str = "";
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        bdcXmService.changeXmzt(proid, Constants.XMZT_LS);
        bdcTdService.changeBackQlFj(bdcXm);
        return str;
    }


    /**
     * 流程退回处理签名意见信息
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/workFlowBackHandleSign", method = {RequestMethod.POST, RequestMethod.GET})
    public String workFlowBackHandleSign(@RequestParam(value = "proid", required = false) String
                                                 proid, @RequestParam(value = "userid", required = false) String
                                                 userid, @RequestParam(value = "activityid", required = false) String
                                                 activityid, @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        String str = "";
        signService.handleRetreatSign(proid, userid, activityid, targetActivityDefids);
        return str;
    }

    /**
     * lj: 查看原项目时获取原项目的wiid
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "toGetWiid", method = {RequestMethod.POST, RequestMethod.GET})
    public String toGetWiid(@RequestParam(value = "proid", required = false) String proid) {
        String wiid = "";
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            wiid = bdcXm.getWiid();
        }
        return wiid;
    }

    /**
     * liujie: 查看原项目时获取原项目信息
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "toGetYxmxx", method = {RequestMethod.POST, RequestMethod.GET})
    public Map toGetYxmxx(@RequestParam(value = "proid", required = false) String proid) {
        HashMap map = new HashMap();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        BdcBdcdySd bdcBdcdySd = bdcdyService.queryBdcdySdById(proid);
        List<BdcBdcdySd> bdcBdcdySdList = bdcdyService.queryBdcdySdByBdcdyh(proid);
        if (bdcXm != null) {
            map.put("WIID", bdcXm.getWiid());
            map.put("SQLX", bdcXm.getSqlx());
            map.put("BDCDYID", bdcXm.getBdcdyid());
        }
        //jiangganzhi 不动产单元冻结查看与警示信息查看区分
        if (bdcBdcdySd != null && StringUtils.isNotBlank(bdcBdcdySd.getXzzt()) && StringUtils.equals(bdcBdcdySd.getXzzt(), Constants.XZZT_SD) && StringUtils.isNotBlank(bdcBdcdySd.getBh())) {
            map.put("BDCDYSD", "SD");
            map.put("BDCSDBH", bdcBdcdySd.getBh());
            map.put(ParamsConstants.BDCDYH_CAPITAL, bdcBdcdySd.getBdcdyh());
        }
        if (CollectionUtils.isNotEmpty(bdcBdcdySdList)) {
            BdcBdcdySd bdcdySd = bdcBdcdySdList.get(0);
            if (StringUtils.isNotBlank(bdcdySd.getXzzt()) && StringUtils.equals(bdcdySd.getXzzt(), Constants.XZZT_SD) && StringUtils.isNotBlank(bdcdySd.getBh())) {
                map.put("BDCDYSD", "SD");
                map.put("BDCSDBH", bdcdySd.getBh());
                map.put(ParamsConstants.BDCDYH_CAPITAL, bdcdySd.getBdcdyh());
            }
        }
        return map;
    }

    /**
     * @param project
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 流程中关联不动产单元
     */
    @ResponseBody
    @RequestMapping(value = "/glBdcdy")
    public String glBdcdy(Project project) {
        String msg = "失败";
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                bdcGdDyhRelService.updateGdDyhRelByProidAndBdcdyh(project.getProid(), project.getBdcdyh());
            }
            CreatProjectService creatProjectService = projectService.getCreatProjectService(project);
            creatProjectService.glBdcdyh(project);
            msg = "成功";
        }
        return msg;
    }

    /**
     * @param project
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 流程中关联证书
     */
    @ResponseBody
    @RequestMapping(value = "/glZs")
    public String glZs(Project project) {
        String msg = "失败";
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            if (StringUtils.isNotBlank(project.getYbdcqzh())) {
                BdcXm xbdcXm = bdcXmService.getBdcXmByProid(project.getProid());
                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(project.getYxmid());
                if (xbdcXm != null) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(xbdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        for (BdcXm bdcXm : bdcXmList) {
                            bdcXm.setYbdcqzh(project.getYbdcqzh());
                            if (ybdcXm != null) {
                                bdcXm.setYbh(ybdcXm.getBh());
                            }
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                    bdcXmRel.setYproid(project.getYxmid());
                                    entityMapper.saveOrUpdate(bdcXmRel, bdcXmRel.getRelid());
                                }
                            }
                            List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdy(bdcXm.getWiid());
                            if (CollectionUtils.isNotEmpty(bdcBdcdyList)) {
                                for (BdcBdcdy bdcBdcdy : bdcBdcdyList) {
                                    bdcBdcdy.setYbdcdyh(project.getBdcdyh());
                                    bdcBdcdy.setBz(Constants.FG_BDCDY_BEGIN + project.getBdcdyh() + Constants.FG_BDCDY_END);
                                    entityMapper.saveOrUpdate(bdcBdcdy, bdcBdcdy.getBdcdyid());
                                }
                            }
                            entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                        }
                    }
                }
            }

            msg = "成功";
        }
        return msg;
    }


    /**
     * xuchao: 复审转缮证节点，根据配置的核准人，自动获取核准人签名
     * 配置文件中需要配置：
     * #配置指定用户自动签名，多个分别用","隔开，并且隔开后user和key的个数必须相同，user为空字符串，默认是转发人
     * sign.auto.user=,3FC714D1E84049ABBFAF93D0CC0B6899,
     * sign.auto.key=hdr,fsr2,fsr
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/workFlowTurnAutoSignBySignkeys", method = {RequestMethod.POST, RequestMethod.GET})
    public String workFlowTurnAutoSignBySignkeys(@RequestParam(value = "proid", required = false) String proid,
                                                 @RequestParam(value = "userid", required = false) String userid) {
        String str = "";
        str = signService.handleTurnAutoSignBySignkeys(proid, userid);
        return str;
    }

    @ResponseBody
    @RequestMapping(value = "/reSaveZs", method = {RequestMethod.POST, RequestMethod.GET})
    public Map reSaveZs(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        BdcXm bdcXm = null;
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(wiid)) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            bdcXm = bdcXmList.get(0);
            for (BdcXm bdcXm1 : bdcXmList) {
                bdcXm1.setSftqsczs("1");
                bdcXmService.saveBdcXm(bdcXm1);
            }
        }
        if (bdcXm == null && StringUtils.isNotBlank(proid)) {
            bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        }
        if (bdcXm != null) {
            List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    if (bdcZs != null && StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                        throw new AppException(3013);
                    }
                }
            }
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
            turnProjectDefaultService.saveBdcZs(bdcXm, null);
            returnvalue = ParamsConstants.SUCCESS_LOWERCASE;
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 生成证书(任意流程)
     */
    @ResponseBody
    @RequestMapping(value = "/reSaveZsArbitrary", method = {RequestMethod.POST, RequestMethod.GET})
    public Map reSaveZsArbitrary(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "bdcqzFlag", required = false) String bdcqzFlag, @RequestParam(value = "bdcqzmFlag", required = false) String bdcqzmFlag) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        BdcXm bdcXm = null;
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(wiid)) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            bdcXm = bdcXmList.get(0);
            for (BdcXm bdcXm1 : bdcXmList) {
                bdcXm1.setSftqsczs("1");
                bdcXmService.saveBdcXm(bdcXm1);
            }
        }
        if (bdcXm == null && StringUtils.isNotBlank(proid)) {
            bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        }
        if (bdcXm != null) {
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectServiceArbitrary();
            turnProjectDefaultService.saveBdcZsArbitrary(bdcXm, bdcqzFlag, bdcqzmFlag);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @param
     * @return
     * @author bianwen
     * @description 在建工程关联过渡土地证
     */
    @ResponseBody
    @RequestMapping(value = "/glGdtd")
    public String glGdtd(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qlid", required = false) String qlid) {
        String msg = "失败";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm xbdcXm = bdcXmService.getBdcXmByProid(proid);
            if (xbdcXm != null) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(xbdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXm : bdcXmList) {
                        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                bdcXmRel.setYqlid(qlid);
                                entityMapper.saveOrUpdate(bdcXmRel, bdcXmRel.getRelid());
                            }
                        }
                    }
                }
            }
            msg = "成功";
        }
        return msg;
    }


    @ResponseBody
    @RequestMapping(value = "/isSaveZs", method = {RequestMethod.POST, RequestMethod.GET})
    public void isSaveZs(@RequestParam(value = "proid", required = false) String proid,
                         @RequestParam(value = "activityid", required = false) String activityid,
                         @RequestParam(value = "userid", required = false) String userid,
                         @RequestParam(value = "taskid", required = false) String taskid,
                         HttpServletResponse response) {
        String str = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                if (CollectionUtils.isEmpty(bdcZsList)) {
                    str = "证书或证明未生成，请点击审批表上的生成证书按钮！";
                }
            }
        }

        try {
            response.setContentType(PARAMETER_CHARSET_GBK);
            PrintWriter out = response.getWriter();
            out.println(str);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("WfProjectController.isSaveZs", e);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/checkZzrq", method = {RequestMethod.POST, RequestMethod.GET})
    public void checkZzrq(@RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) {
        String str = "";
        BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
        if (bdcXmTemp != null && StringUtils.isNotBlank(bdcXmTemp.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXmTemp.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    //   jyl 审批表增加两个验证，土地权利性质为"出让"时，终止日期不为空；权利性质为"划拨"时，终止日期应为空
                    if (StringUtils.isBlank(str)) {
                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                        if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getZdzhqlxz()) && StringUtils.equals(bdcSpxx.getZdzhqlxz(), "101")) {
                            str = "土地权利性质为\"划拨\"时，终止日期应为空";
                            break;
                        }
                    }
                }
            }
        }
        try {
            response.setContentType(PARAMETER_CHARSET_GBK);
            PrintWriter out = response.getWriter();
            out.println(str);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("WfProjectController.checkZzrq", e);
        }
    }


    //公告推送到中间库
    @ResponseBody
    @RequestMapping(value = "/shareAnnouncement2DbByWiid", method = {RequestMethod.POST, RequestMethod.GET})
    public Map shareAnnouncement2DbByWiid(@RequestParam(value = "wiid", required = false) String wiid,
                                          HttpServletResponse response) {
        HashMap map = new HashMap();
        realEstateShareService.shareAnnouncement2DbByWiid(wiid);
        map.put("msg", "success");
        return map;
    }


    /**
     * 发送短信工作流事件触发发送短信
     *
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     * @param targetActivityDefids
     */
    @ResponseBody
    @RequestMapping(value = "/sendSmsProjectEvent")
    public void sendSmsProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                    @RequestParam(value = "activityid", required = false) String activityid,
                                    @RequestParam(value = "userid", required = false) String userid,
                                    @RequestParam(value = "taskid", required = false) String taskid,
                                    @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {

        String activityName = PlatformUtil.getPfActivityNameByTaskId(taskid);
        smsService.sendSms(proid, activityName);
    }


    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 改变证书锁定的状态
     */
    @ResponseBody
    @RequestMapping(value = "/changeSdZtEvent")
    public void changeSdZtEvent(@RequestParam(value = "proid", required = false) String proid) {
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcSdService.changeSdZt(bdcXm);
        }
    }


    /**
     * @param
     * @return
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 生成项目的同时对附记赋值
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcXtConfigFj")
    public void getBdcXtConfigFj(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    bdcZsService.saveBdcXtConfigFj(bdcXm);
                }
            }
        }
    }

    /**
     * @param
     * @return
     * @author <a herf="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据proid获取taskid
     */
    @ResponseBody
    @RequestMapping("/getTaskId")
    public Map<String, Object> testGetTaskId(String proid) {
        Map map = Maps.newHashMap();
        if (StringUtils.isNotBlank(proid)) {
            PfWorkFlowInstanceVo pf = super.getWorkFlowInstance(proid);
            SysTaskService sysTaskService = PlatformUtil.getTaskService();
            List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pf.getWorkflowIntanceId());
            if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
                map.put("msg", pfTaskVoList.get(0).getTaskId());
            }
        }
        return map;
    }

    @RequestMapping(value = "/creatPreviewZsEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void creatPreviewZsEvent(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "userid", required = false) String userid) {
        //zdd  生成证书的逻辑根据业务来确定是生成一本还是多本   此处不在直接循环生成证书
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
        if (CollectionUtils.isEmpty(bdcZsList)) {
            String previewZs = "true";
            if (bdcXm != null) {
                if (StringUtils.isNotBlank(bdcXm.getBdclx()) && (Constants.BDCLX_TDSL.equals(bdcXm.getBdclx()) || Constants.BDCLX_TDQT.equals(bdcXm.getBdclx())) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                    projectLifeManageService.generateProjectZs(bdcXm.getWiid(), previewZs);
                } else {
                    TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
                    turnProjectDefaultService.setUserid(userid);
                    turnProjectDefaultService.saveBdcZs(bdcXm, previewZs);
                }
            }
        }
        if (bdcXm != null) {
            //针对抵押登记各业务，权利人为配置的同一银行的时候，缴费状态自动改变成已缴费
            String wfDfid = PlatformUtil.getWfDfidByWiid(bdcXm.getWiid());
            String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(wfDfid);
            if (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(AppConfig.getProperty("SFD.TYSF.SHOW"), ParamsConstants.TRUE_LOWERCASE) && CommonUtil.indexOfStrs(Constants.TYSF_SHOW_SQLX, sqlxdm)) {
                bdcSfxxService.changesfzt(proid);
            }
        }
    }

    @RequestMapping(value = "/completeZsInfoEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void completeZsInfoEvent(@RequestParam(value = "proid", required = false) String proid,
                                    @RequestParam(value = "activityid", required = false) String activityid,
                                    @RequestParam(value = "userid", required = false) String userid,
                                    @RequestParam(value = "taskid", required = false) String taskid,
                                    @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        //是否转发到缮证节点
        Boolean isSz = true;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (StringUtils.isNotBlank(targetActivityName) && !(StringUtils.equals(targetActivityName, Constants.WORKFLOW_SZ) || StringUtils.equals(targetActivityName, Constants.WORKFLOW_DB))) {
                isSz = false;
            }
        }
        if (isSz) {
            //zdd  生成证书的逻辑根据业务来确定是生成一本还是多本   此处不在直接循环生成证书
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
                //补全证书相关信息
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
                turnProjectDefaultService.setUserid(userid);
                if (CollectionUtils.isEmpty(bdcZsList)) {
                    turnProjectDefaultService.creatBdcZsInfo(proid, userid);
                    bdcXmService.inheritXzzrnxInfoForNullPreviewZs(bdcXm);
                } else {
                    if (newZh) {
                        completeZsInfoService.completeZsInfo(bdcXm.getWiid());
                    } else {
                        turnProjectDefaultService.completeZsInfo(bdcXm);
                    }
                }
                try {
                    //liujie 更新权籍库
                    if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                        cadastralService.syncQlrForCadastral(bdcXm.getWiid());
                    }
                    //同步登记图形库
                    registerGraphicService.insertDjtxkSj(bdcXm);
                } catch (Exception e) {
                    logger.error("wfProject/completeZsInfoEvent");
                }
            }
        }
    }

    @RequestMapping(value = "/dbTsJyzt", method = {RequestMethod.POST, RequestMethod.GET})
    public void dbTsJyzt(@RequestParam(value = "proid", required = false) String proid,
                         @RequestParam(value = "wiid", required = false) String wiid) {
        /**
         * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
         * @description 调c包接口获取/推送交易信息
         */
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);

            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcXm.getBdcdyh());
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getFwbm()) && StringUtils.equals(bdcBdcdy.getBdclx(), Constants.BDCLX_TDFW)) {
                    currencyService.tsJyFwzt(bdcXm.getWiid(), Constants.JYTSZT_SD);
                }
            }
        }
    }

    @RequestMapping(value = "/delPreviewZsEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void delPreviewZsEvent(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "userid", required = false) String userid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            bdcZsService.delBdcZsByWiid(bdcXm.getWiid());
            bdcXmService.delXzzrnxInfoFj(bdcXm);
        }
    }

    @RequestMapping(value = "/backZsEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void backZsEvent(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "userid", required = false) String userid, @RequestParam(value = "wfid", required = false) String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(wiid);
            for (BdcXm bdcXm : xmList) {
                bdcZsbhService.workFlowBackZsBh(bdcXm.getProid());
                //提前生成证书 删除证号
                bdcZsService.delBdcZsBdcqzhAndZsbhByProid(bdcXm.getProid());
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/completeXmbh", method = {RequestMethod.POST, RequestMethod.GET})
    public Map completeXmbh(@RequestParam(value = "proid", required = false) String proid) {
        Map map = new HashMap();
        String msg = bdcXmService.completeXmbh(proid);
        map.put("msg", msg);
        return map;
    }

    /**
     * @param proid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 验证转发comfirm验证项，获取comfirm验证msg
     */
    @ResponseBody
    @RequestMapping(value = "/getConfirmMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getConfirmMsg(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "taskid", required = false) String taskid) {
        Map map = Maps.newHashMap();
        String msg = "";
        List<Map<String, Object>> checkMap = projectCheckInfoService.checkXmComfirmItems(proid, taskid);
        if (CollectionUtils.isNotEmpty(checkMap)) {
            for (Map<String, Object> checkMsg : checkMap) {
                if (checkMsg.containsKey(ParamsConstants.CHECKMSG_HUMP) && StringUtils.isNotBlank(CommonUtil.formatEmptyValue(checkMsg.get(ParamsConstants.CHECKMSG_HUMP)))) {
                    msg = CommonUtil.formatEmptyValue(checkMsg.get(ParamsConstants.CHECKMSG_HUMP));
                    break;
                }
            }
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * @param proid
     * @param userid
     * @param activityid
     * @param targetActivityDefids
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 集成平台的件退回删除项目
     */
    @RequestMapping(value = "/workFlowBackHandleJcpt", method = {RequestMethod.POST, RequestMethod.GET})
    public void workFlowBackHandleJcpt(@RequestParam(value = "proid", required = false) String proid,
                                       @RequestParam(value = "userid", required = false) String userid,
                                       @RequestParam(value = "activityid", required = false) String activityid,
                                       @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(userid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh()) && StringUtils.isNotBlank(bdcXm.getWwslbh())
                    && StringUtils.equals(bdcXm.getBh(), bdcXm.getWwslbh()) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfTaskVo pfTaskVo = PlatformUtil.getPfTaskVoByWiid(bdcXm.getWiid());
                if (pfTaskVo != null && StringUtils.isNotBlank(pfTaskVo.getTaskId())) {
                    workFlowService.deleteProject(bdcXm.getWiid(), pfTaskVo.getTaskId(), userid);
                }
            }
        }
    }


    @ResponseBody
    @RequestMapping(value = "/registerProjectEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void registerProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                     @RequestParam(value = "activityid", required = false) String activityid,
                                     @RequestParam(value = "userid", required = false) String userid,
                                     @RequestParam(value = "taskid", required = false) String taskid,
                                     @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) throws Exception {

        workFlowEventService.workFlowEventRegister(proid, activityid, userid, taskid, targetActivityDefids);
    }


    @ResponseBody
    @RequestMapping(value = "/beforeTurnRegisterProjectEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void beforeTurnRegisterProjectEvent(@RequestParam(value = "proid", required = false) String proid,
                                               @RequestParam(value = "activityid", required = false) String activityid,
                                               @RequestParam(value = "userid", required = false) String userid,
                                               @RequestParam(value = "taskid", required = false) String taskid,
                                               HttpServletResponse response) throws Exception {

        String result = "";
        try {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            RegisterProjectService registerProjectService = projectService.getRegisterProjectService(bdcXm);
            registerProjectService.registerProject(proid, userid);
        } catch (Exception e) {
            result = Constants.REGISTER_FAIL_TIPS;
            logger.error("WfProjectController.beforeTurnRegisterProjectEvent", e);
        }

        try {
            response.setContentType(PARAMETER_CHARSET_GBK);
            PrintWriter out = response.getWriter();
            out.println(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("WfProjectController.beforeTurnRegisterProjectEvent", e);
        }
    }

    @RequestMapping(value = "/inheritXzzrnxInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public void inheritXzzrnxInfo(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        //是否转发到缮证节点
        Boolean isSz = true;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (StringUtils.isNotBlank(targetActivityName) && !(StringUtils.equals(targetActivityName, Constants.WORKFLOW_SZ) || StringUtils.equals(targetActivityName, Constants.WORKFLOW_DB))) {
                isSz = false;
            }
        }
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcXmService.inheritXzzrnxInfo(bdcXm, isSz);
        }
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setAutoGrowNestedPaths(true);
        binder.setAutoGrowCollectionLimit(1024);
    }

    @ResponseBody
    @RequestMapping(value = "/wwCheckBdcXm")
    public List<Map<String, Object>> wwCheckBdcXm(@RequestBody Project project) {
        return checkBdcXm(null, project);
    }

    @ResponseBody
    @RequestMapping(value = "/wwCheckMulBdcXm")
    public List<Map<String, Object>> wwCheckMulBdcXm(@RequestBody Project project) {
        return checkMulBdcXm(null, project);
    }

    @ResponseBody
    @RequestMapping(value = "/wwCreateWfProject")
    public String wwCreateWfProject(@RequestBody Project project) {
        return createWfProject(project);
    }

    @ResponseBody
    @RequestMapping(value = "/wwGetBdcXtConfigQlqtzkAndFj")
    public void wwGetBdcXtConfigQlqtzkAndFj(@RequestBody Map<String, Object> param) {
        if (MapUtils.isNotEmpty(param)) {
            List<String> wiidList = (List<String>) param.get("wiidArray");
            if (CollectionUtils.isNotEmpty(wiidList)) {
                for (String wiid : wiidList) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        for (BdcXm bdcXm : bdcXmList) {
                            bdcZsService.saveBdcXtConfigQlqtzkAndFj(bdcXm);
                        }
                    }
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/gxYhValidateEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void gxYhValidateEvent(@RequestParam(value = "proid", required = false) String proid,
                                  @RequestParam(value = "activityid", required = false) String activityid,
                                  @RequestParam(value = "userid", required = false) String userid,
                                  @RequestParam(value = "taskid", required = false) String taskid,
                                  HttpServletResponse response) {
        String str = "";
        Map<String, Object> map = gxYhService.validateGxYhInterface(proid, userid);
        if (map != null && map.get("info") != null && StringUtils.isNotBlank(map.get("info").toString())) {
            str = map.get("info").toString();
        }
        try {
            response.setContentType(PARAMETER_CHARSET_GBK);
            PrintWriter out = response.getWriter();
            out.println(str);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("WfProjectController.gxYhValidateEvent", e);
        }
    }

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 互联网+验证接口推送前需要证号 因此将完善证号功能单独提出来
     */
    @RequestMapping(value = "/completeZsRelatedInfoEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void completeZsRelatedInfoEvent(@RequestParam(value = "proid", required = false) String proid,
                                           @RequestParam(value = "userid", required = false) String userid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
            //补全证书相关信息
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
            turnProjectDefaultService.setUserid(userid);
            if (CollectionUtils.isEmpty(bdcZsList)) {
                turnProjectDefaultService.creatBdcZsInfo(proid, userid);
                bdcXmService.inheritXzzrnxInfoForNullPreviewZs(bdcXm);
            } else {
                turnProjectDefaultService.completeZsInfo(bdcXm);
            }
        }
    }

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 转发至缮证节点后需要做的相关推送事件
     */
    @RequestMapping(value = "/doSzEvent", method = {RequestMethod.POST, RequestMethod.GET})
    public void doSzEvent(@RequestParam(value = "proid", required = false) String proid,
                          @RequestParam(value = "userid", required = false) String userid,
                          @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) {
        //是否转发到缮证节点
        Boolean isSz = true;
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String targetActivityName = PlatformUtil.getTargetActivityName(pfWorkFlowInstanceVo.getWorkflowIntanceId(), targetActivityDefids);
            if (StringUtils.isNotBlank(targetActivityName) && !(StringUtils.equals(targetActivityName, Constants.WORKFLOW_SZ) || StringUtils.equals(targetActivityName, Constants.WORKFLOW_DB))) {
                isSz = false;
            }
        }
        if (isSz) {
            //zdd  生成证书的逻辑根据业务来确定是生成一本还是多本   此处不在直接循环生成证书
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                bdcXmService.zsAopByBdcXm(bdcXm);
                try {
                    //liujie 更新权籍库
                    if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                        cadastralService.syncQlrForCadastral(bdcXm.getWiid());
                    }
                    //同步登记图形库
                    registerGraphicService.insertDjtxkSj(bdcXm);
                } catch (Exception e) {
                    logger.error("wfProject/completeSzInfoEvent", e);
                }
                if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        if (bdcXmList.size() == 1) {
                            BdcXm bdcXmTemp = bdcXmList.get(0);
                            if (StringUtils.isNotBlank(bdcXmTemp.getYhsqywh())) {
                                etlGxYhService.dbYhxx(bdcXmTemp.getProid(), "", "", userid);
                            }
                        } else {
                            for (BdcXm bdcXmTemp : bdcXmList) {
                                //更新互联网+银行登簿状态
                                if (StringUtils.equals(bdcXmTemp.getQllx(), Constants.QLLX_DYAQ) && StringUtils.isNotBlank(bdcXmTemp.getYhsqywh())) {
                                    etlGxYhService.dbYhxx(bdcXmTemp.getProid(), "", "", userid);
                                }
                            }
                        }
                    }

                }
            }

            //张家港激扬档案登簿推送审批表信息
            String archivesUrl = AppConfig.getProperty(CONFIGURATION_PARAMETER_ARCHIVES_URL);
            if (StringUtils.isNotBlank(archivesUrl)) {
                try {
                    archivesService.pushArchivesFile(proid, userid);
                } catch (Exception e) {
                    logger.error("WfProjectController.completeSzInfoEvent", e);
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/wwCheckXmByProject")
    public List<Map<String, Object>> wwCheckXmByProject(@RequestBody Project project) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HashMap resultMap = new HashMap();
        List<Map<String, Object>> resultList = Lists.newArrayList();
        List<String> unExamineSqlx = ReadXmlProps.getUnExamineSqlxDm();
        Project projectTemp = (Project) BeanUtils.cloneBean(project);
        List<BdcExamineParam> bdcExamineParamList = null;
        if (CollectionUtils.isEmpty(unExamineSqlx) || (CollectionUtils.isNotEmpty(unExamineSqlx) && !unExamineSqlx.contains(project.getSqlx()))) {
            bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(StringUtils.EMPTY, projectTemp);
            if (CollectionUtils.isNotEmpty(bdcExamineParamList)) {
                Map<String, Object> examineMap = bdcExamineService.performExamine(bdcExamineParamList, project.getWiid());
                if (examineMap != null && examineMap.containsKey("info") && examineMap.get("info") != null) {
                    resultMap.put("result", false);
                    resultMap.put("checkModel", "ALERT");
                    resultMap.put("checkMsg", examineMap.get("checkMsg"));
                    if (examineMap.get("xzwh") != null) {
                        resultMap.put("xzwh", examineMap.get("xzwh").toString());
                    }
                    if (examineMap.get("examineInfo") != null) {
                        resultMap.put("examineInfo", examineMap.get("examineInfo"));
                    }
                    resultList.add(resultMap);
                    return resultList;
                }
            }
        }
        return projectCheckInfoService.checkXmByProject(project);
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 解锁其他限制权利
     */
    @ResponseBody
    @RequestMapping(value = "/unlockQtxzql", method = {RequestMethod.POST, RequestMethod.GET})
    public void unlockQtxzql(@RequestParam(value = "proid", required = false) String proid,
                             @RequestParam(value = "userid", required = false) String userid,
                             HttpServletResponse response) {
        try {
            bdcBdcZsSdService.changeYzsXzzt(proid, Constants.XZZT_ZC, userid);
        } catch (Exception e) {
            logger.error("WfProjectController.unlockQtxzql", e);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/endProjectZx")
    public void endProjectZx(@RequestParam(value = "proid", required = false) String proid,
                             @RequestParam(value = "activityid", required = false) String activityid,
                             @RequestParam(value = "wiid", required = false) String wiid,
                             @RequestParam(value = "userid", required = false) String userid,
                             @RequestParam(value = "taskid", required = false) String taskid,
                             @RequestParam(value = "dbrRead", required = false) String dbrRead,
                             @RequestParam(value = "defaultUserId", required = false) String defaultUserId,
                             @RequestParam(value = "targetActivityDefids", required = false) String targetActivityDefids) throws Exception {
        changeYqlZtEvent(proid, activityid, userid, taskid, targetActivityDefids);
        turnProjectEventDyZxDbr(proid, activityid, userid, taskid, dbrRead, defaultUserId);
        endProjectEvent(proid, activityid, userid, taskid);
        dbTsJyzt(proid, userid);
        currencyService.finishStep(proid, userid);
        exchangeService.changeLcjdmc(proid, wiid, targetActivityDefids);
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/6/22 14:57
     * @description 更新互联网受理状态
     */
    @ResponseBody
    @RequestMapping(value = "/updateSlzt")
    public void updateSlzt(@RequestParam(value = "proid", required = false) String proid,
                           @RequestParam(value = "slzt", required = false) String slzt) {
        currencyService.updateSlzt(proid, slzt);
    }

}
