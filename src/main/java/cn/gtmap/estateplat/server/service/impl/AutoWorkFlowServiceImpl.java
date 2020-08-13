package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.examine.BdcExamineParam;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXmZsRelService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.core.service.ExamineCheckInfoService;
import cn.gtmap.estateplat.server.model.PfWorkFlowEvent;
import cn.gtmap.estateplat.server.service.AutoWorkFlowService;
import cn.gtmap.estateplat.server.service.PfWorkFlowEventConfigurationService;
import cn.gtmap.estateplat.server.service.ProjectCheckInfoService;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.service.examine.BdcExamineService;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.service.WorkFlowCoreService;
import com.gtis.plat.vo.*;
import com.gtis.plat.wf.WorkFlowInfo;
import com.gtis.plat.wf.model.ActivityModel;
import com.gtis.plat.wf.model.PerformerTaskModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/12/19
 * @description 自动工作流服务
 */
@Service
public class AutoWorkFlowServiceImpl implements AutoWorkFlowService {
    @Autowired
    private PfWorkFlowEventConfigurationService pfWorkFlowEventConfigurationService;
    @Autowired
    private ExamineCheckInfoService examineCheckInfoService;
    @Autowired
    private BdcExamineService bdcExamineService;
    @Autowired
    private ProjectCheckInfoService projectCheckInfoService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private BdcZdGlService bdcZdGlService;


    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final  String ROLEID = "RoleId";

    @Async
    @Override
    public void autoTurnProject(BdcZs bdcZs, String activityName,String currentUserid){
        BdcXm bdcXm = null;
        if (StringUtils.isNotBlank(bdcZs.getZsid())) {
            String proid = bdcXmZsRelService.getProidByZsid(bdcZs.getZsid());
            if (StringUtils.isNotBlank(proid)) {
                bdcXm = bdcXmService.getBdcXmByProid(proid);
            }
        }
        if(bdcXm != null) {
            PfTaskVo pfTaskVo = PlatformUtil.getPfTaskVoByWiid(bdcXm.getWiid());
            String pfActivityName = PlatformUtil.getActivityNameByPfTaskVo(pfTaskVo);
            if(StringUtils.equals(pfActivityName,activityName)) {
                Boolean turnEnable = beforeTurnProjectEvent(bdcXm,currentUserid);
                if(turnEnable){
                    String userid = StringUtils.deleteWhitespace(AppConfig.getProperty("auto.turn.project.userid"));
                    String roleid = StringUtils.deleteWhitespace(AppConfig.getProperty("auto.turn.project.roleid"));
                    beginPostWorkFlow(bdcXm,pfTaskVo,false,userid,roleid,Constants.WORKFLOW_FZ);
                    //自动转发办结
                    autoTurnProjectEnd(bdcXm,Constants.WORKFLOW_FZ,currentUserid);
                }
            }
        }
    }

    @Override
    public void autoTurnProjectByBdcXm(BdcXm bdcXm, String activityName, String targetActivityName, String userid) {
        if(bdcXm != null) {
            PfTaskVo pfTaskVo = PlatformUtil.getPfTaskVoByWiid(bdcXm.getWiid());
            String pfActivityName = PlatformUtil.getActivityNameByPfTaskVo(pfTaskVo);
            String roleid = getRoleid(bdcXm,userid,targetActivityName);
            if(StringUtils.equals(pfActivityName,activityName)) {
                Boolean turnEnable = beforeTurnProjectEvent(bdcXm,userid);
                if(turnEnable){
                    beginPostWorkFlow(bdcXm,pfTaskVo,false,userid,roleid,targetActivityName);
                }
            }
        }

    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @param userid
     * @param targetActivityName
     * @description
     */
    private String getRoleid(BdcXm bdcXm, String userid, String targetActivityName) {
        String roleid = "";
        if(StringUtils.isNotBlank(userid)&& StringUtils.isNotBlank(targetActivityName)) {
            List<PfRoleVo> pfRoleVoList = PlatformUtil.getRoleListByUser(userid);
            if(CollectionUtils.isNotEmpty(pfRoleVoList)){
                List<String> roleList = new ArrayList<String>();
                for(PfRoleVo pfRoleVo:pfRoleVoList) {
                    roleList.add(pfRoleVo.getRoleId());
                }
                String sqlxdm = "";
                //获取平台的申请类型代码,主要为了合并
                if(StringUtils.isNotBlank(bdcXm.getWiid())) {
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = PlatformUtil.getSysWorkFlowInstanceService().getWorkflowInstance(bdcXm.getWiid());
                    if(pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                        sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                    }
                }
                List<HashMap> roleMapList = ReadXmlProps.getRoleList();
                if(CollectionUtils.isNotEmpty(roleMapList)) {
                    for(HashMap roleMap:roleMapList) {
                        if(StringUtils.equals(sqlxdm,CommonUtil.formatEmptyValue(roleMap.get("sqlx")))
                                &&StringUtils.equals(targetActivityName,CommonUtil.formatEmptyValue(roleMap.get("node")))){
                            if(roleList.contains(CommonUtil.formatEmptyValue(roleMap.get("roleid")))){
                                return CommonUtil.formatEmptyValue(roleMap.get("roleid"));
                            }
                        }
                    }
                }
            }
        }
        return roleid;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @description activityName
     */
    private void autoTurnProjectEnd(BdcXm bdcXm, String activityName,String userid){
        PfTaskVo pfTaskVo = PlatformUtil.getPfTaskVoByWiid(bdcXm.getWiid());
        String pfActivityName = PlatformUtil.getActivityNameByPfTaskVo(pfTaskVo);
        if(StringUtils.equals(pfActivityName, activityName)) {
            Boolean turnEnable = beforeTurnProjectEvent(bdcXm,userid);
            if(turnEnable) {
                beginPostWorkFlow(bdcXm,pfTaskVo,true,"","",activityName);
            }
        }
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @param pfTaskVo
     * @param isWorkFlowEventEnd
     * @param userid
     * @param roleid
     * @param targetActivityName
     * @description 开始转发工作流
     */
    private void beginPostWorkFlow(BdcXm bdcXm, PfTaskVo pfTaskVo, Boolean isWorkFlowEventEnd, String userid, String roleid, String targetActivityName){
        if(pfTaskVo != null) {
            WorkFlowCoreService workFlowCoreService = PlatformUtil.getWorkFlowCoreService();
            SysWorkFlowDefineService sysWorkFlowDefineService = PlatformUtil.getWorkFlowDefineService();
            SysWorkFlowInstanceService sysWorkFlowInstanceService  = PlatformUtil.getSysWorkFlowInstanceService();
            PfUserVo pfUserVo = pfTaskVo.getUserVo();
            WorkFlowInfo info = null;
            try {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if(pfWorkFlowInstanceVo != null && pfUserVo != null) {
                    info = workFlowCoreService.getWorkFlowTurnInfo(pfUserVo.getUserId(),pfTaskVo.getTaskId());
                    // 处理活动
                    if (info != null) {
                        String activitDefinitionId = PlatformUtil.getActivitDefinitionIdByPfTaskVo(bdcXm.getWiid(),targetActivityName);
                        Map<String,List<Map>> subProcessEndTargetActivitiesTemp = getSubProcessEndTargetActivities(userid,roleid,activitDefinitionId);
                        if(subProcessEndTargetActivitiesTemp != null) {
                            info.setSubProcessEndTargetActivities(subProcessEndTargetActivitiesTemp);
                        }
                        List<ActivityModel> lstActivityModel = info.getTransInfo().getTranActivitys();
                        PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                        //如果splittype为xor
                        String xml = sysWorkFlowDefineService.getWorkFlowDefineXml(pfWorkFlowDefineVo);
                        WorkFlowXml workXml = new WorkFlowXml(xml);
                        workXml.setModifyDate(pfWorkFlowDefineVo.getModifyDate());
                        ActivityModel activityModel = workXml.getActivity(info.getSourceActivity().getActivityDefinitionId());
                        if (StringUtils.isBlank(activityModel.getSplitType()) || activityModel.getSplitType().equalsIgnoreCase("XOR")) {
                            int i = 0;
                            for (Iterator<ActivityModel> it = lstActivityModel.iterator(); it.hasNext(); ) {
                                ActivityModel activityModelTmep = it.next();
                                if (activityModelTmep!=null&&!StringUtils.equals(activityModelTmep.getDefineId(),activitDefinitionId)) {
                                    it.remove();
                                }
                                i++;
                            }
                        }

                        //处理活动的子流程所属主流程下一个节点的办理人和角色信息
                        Map<String,List<Map>> subProcessEndTargetActivities = info.getSubProcessEndTargetActivities();
                        if (subProcessEndTargetActivities != null) {
                            for (Iterator<ActivityModel> it = lstActivityModel.iterator();it.hasNext();) {
                                ActivityModel tmpActivityModel = it.next();
                                if(!subProcessEndTargetActivities.containsKey(tmpActivityModel.getDefineId()))
                                    it.remove();
                                else{
                                    List<Map> userInfoList = subProcessEndTargetActivities.get(tmpActivityModel.getDefineId());
                                    List<String> userIds = new ArrayList<String>();
                                    List<String> performerIds = new ArrayList<String>();   //转发任务中，人员的部门或角色ids
                                    for(Map userInfoMap: userInfoList){
                                        if (userInfoMap.get(ROLEID) != null&& org.apache.commons.lang.StringUtils.isNotBlank(String.valueOf(userInfoMap.get(ROLEID)))){
                                            performerIds.add(String.valueOf(userInfoMap.get(ROLEID)));
                                            userIds.add(String.valueOf(userInfoMap.get("Id")));
                                        }else{
                                            if (userInfoMap.get("Id")!=null&&StringUtils.isNotBlank(String.valueOf(userInfoMap.get("Id")))) {
                                                userIds.add(String.valueOf(userInfoMap.get("Id")));
                                            }
                                        }
                                    }

                                    List<String> userTmpList = new ArrayList<String>();
                                    List<PerformerTaskModel> lstPerformer = tmpActivityModel.getPerformerModelList();
                                    //过滤用户
                                    if (CollectionUtils.isNotEmpty(userIds)) {
                                        for (Iterator<PerformerTaskModel> itPerformer = lstPerformer.iterator(); itPerformer.hasNext(); ) {
                                            PerformerTaskModel taskModel = itPerformer.next();
                                            if (taskModel.getId() != null && !performerIds.contains(taskModel.getId())) {
                                                itPerformer.remove();
                                            }
                                            for (Iterator<PfUserVo> userIterator = taskModel.getUserList().iterator(); userIterator.hasNext(); ) {
                                                PfUserVo vo = userIterator.next();
                                                if (userTmpList.contains(vo.getUserId())) {
                                                    userIterator.remove();
                                                } else {
                                                    if (!userIds.contains(vo.getUserId())) {
                                                        userIterator.remove();
                                                    } else {
                                                        userTmpList.add(vo.getUserId());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //开始转发
                        workFlowCoreService.postWorkFlow(pfUserVo.getUserId(), pfTaskVo.getTaskId(), info);
                        handleWorkFlowEvent(bdcXm,pfTaskVo,isWorkFlowEventEnd,activitDefinitionId);
                    }
                }
            } catch (Exception e) {
                logger.error("AutoWorkFlowServiceImpl.beginPostWorkFlow",e);
            }
        }
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param userid
     * @param roleid
     * @param activitDefinitionId
     * @description 初始化subProcessEndTargetActivities
     */
    private Map<String,List<Map>> getSubProcessEndTargetActivities(String userid, String roleid, String activitDefinitionId){
        Map<String,List<Map>> subProcessEndTargetActivities = null;
        if(StringUtils.isNotBlank(userid)&&StringUtils.isNotBlank(roleid)&&StringUtils.isNotBlank(activitDefinitionId)){
            subProcessEndTargetActivities = new HashMap<String, List<Map>>();
            List<Map> userInfoList = new ArrayList<Map>();
            Map userInfoMap = new HashMap();
            userInfoMap.put("Id",userid);
            userInfoMap.put(ROLEID,roleid);
            userInfoList.add(userInfoMap);
            subProcessEndTargetActivities.put(activitDefinitionId,userInfoList);
        }
        return subProcessEndTargetActivities;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @description 自动转发前进行项目验证
     */
    private Boolean beforeTurnProjectEvent(BdcXm bdcXm,String userid){
        boolean turnEnable = true;
        if(bdcXm != null) {
            String str = "";
            List<String> unExamineSqlx = ReadXmlProps.getUnExamineSqlxDm();
            if (CollectionUtils.isEmpty(unExamineSqlx) || (CollectionUtils.isNotEmpty(unExamineSqlx) && !unExamineSqlx.contains(bdcXm.getSqlx()))) {
                List<BdcExamineParam> bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(bdcXm.getWiid(), null);
                Map<String, Object> examineMap = bdcExamineService.performExamine(bdcExamineParamList, bdcXm.getWiid());
                if (examineMap != null && examineMap.containsKey("info") && examineMap.get("info") != null) {
                    str = StringUtils.indexOf(examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString(), "<br/>") > -1 ? StringUtils.replace(examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString(), "<br/>", "\\n") : examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString();
                }
            }
            if(StringUtils.isBlank(str)){
                List<Map<String, Object>> checkMsg = projectCheckInfoService.checkXm(bdcXm.getProid(), false,ParamsConstants.ALERT_LOWERCASE,userid,"");
                if (CollectionUtils.isNotEmpty(checkMsg)) {
                    for (Map<String, Object> map : checkMsg) {
                        if (map.containsKey(ParamsConstants.CHECKMODEL_HUMP) && map.get(ParamsConstants.CHECKMODEL_HUMP).toString().toUpperCase().equals("ALERT")) {
                            turnEnable = false;
                            logger.info(map.get(ParamsConstants.CHECKMSG_HUMP).toString());
                            break;
                        }
                    }
                }
            }else{
                turnEnable = false;
                logger.info(str);
            }
        }
        return turnEnable;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @param pfTaskVo
     * @description 处理流程工作流事件
     */
    private void handleWorkFlowEvent(BdcXm bdcXm,PfTaskVo pfTaskVo,Boolean isWorkFlowEventEnd,String activitDefinitionId){
        String workflowDefinitionId = PlatformUtil.getWfDfidByWiid(bdcXm.getWiid());
        List<PfWorkFlowEvent> pfWorkFlowEventList = null;
        if(isWorkFlowEventEnd){
           pfWorkFlowEventList = pfWorkFlowEventConfigurationService.getPfWorkFlowEventList(workflowDefinitionId,"",Constants.EVENT_TYPE_WORKFLOW_END);
        }else{
            String activityDefinitionId = PlatformUtil.getActivitDefinitionIdByPfTaskVo(pfTaskVo);
            pfWorkFlowEventList = pfWorkFlowEventConfigurationService.getPfWorkFlowEventList(workflowDefinitionId,activityDefinitionId,Constants.EVENT_TYPE_WORKFLOW_TURN);
        }
        if(CollectionUtils.isNotEmpty(pfWorkFlowEventList)) {
            for(PfWorkFlowEvent pfWorkFlowEvent:pfWorkFlowEventList) {
                String url = "";
                if(StringUtils.isNotBlank(pfWorkFlowEvent.getWorkFlowEventUrl())){
                    if(StringUtils.indexOf(pfWorkFlowEvent.getWorkFlowEventUrl(),"${exchange.url}") > -1) {
                        url = StringUtils.replace(pfWorkFlowEvent.getWorkFlowEventUrl(),"${exchange.url}",StringUtils.deleteWhitespace(AppConfig.getProperty("exchange.url")));
                    }else if(StringUtils.indexOf(pfWorkFlowEvent.getWorkFlowEventUrl(),"${bdcdj.url}") > -1){
                        url = StringUtils.replace(pfWorkFlowEvent.getWorkFlowEventUrl(),"${bdcdj.url}",StringUtils.deleteWhitespace(AppConfig.getProperty("bdcdj.url")));
                    }else if(StringUtils.indexOf(pfWorkFlowEvent.getWorkFlowEventUrl(),"${etl.url}") > -1){
                        url = StringUtils.replace(pfWorkFlowEvent.getWorkFlowEventUrl(),"${etl.url}",StringUtils.deleteWhitespace(AppConfig.getProperty("etl.url")));
                    }
                    getRequestWorkFlowEvent(url,bdcXm,pfTaskVo,activitDefinitionId);
                }
            }
       }

    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param url
     * @param bdcXm
     * @param pfTaskVo
     * @description get方式请求工作流事件处理方法
     */
     private void getRequestWorkFlowEvent (String url,BdcXm bdcXm,PfTaskVo pfTaskVo, String activityDefinitionId){
         if(StringUtils.isNotBlank(url)&&bdcXm != null&&pfTaskVo != null){
             StringBuilder stringBuilder = new StringBuilder();
             stringBuilder.append(url).append("?proid=" + bdcXm.getProid()).append("&wiid=" + bdcXm.getWiid()).append("&userid=" + pfTaskVo.getUserVo().getUserId()).append("&targetActivityDefids=" + activityDefinitionId);
             HttpClient client = null;
             GetMethod method = null;
             try {
                 client = new HttpClient();
                 client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 5000);
                 client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
                 method = new GetMethod(stringBuilder.toString());
                 method.setRequestHeader("Connection", "close");
                 client.executeMethod(method);
             } catch (IOException e) {
                 logger.error("AutoWorkFlowServiceImpl.requestWorkFlowEvent", e);
             }finally {
                 if(method != null) {
                     method.releaseConnection();
                 }
                 if(client != null) {
                     ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                 }
             }
         }
     }
}
