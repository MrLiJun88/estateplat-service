package cn.gtmap.estateplat.server.utils;

import com.gtis.config.AppConfig;
import com.gtis.config.PropertyPlaceholderHelper;
import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.model.impl.File;
import com.gtis.fileCenter.model.impl.NodeImpl;
import com.gtis.fileCenter.service.FileService;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.*;
import com.gtis.plat.vo.*;
import com.gtis.plat.wf.model.ActivityModel;
import com.gtis.spring.Container;
import com.gtis.util.DataSourceManager;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 平台或者文件中心的接口
 *
 * @author Administrator
 */
@Service("platformUtil")
public class PlatformUtil {

    private static final Logger logger = LoggerFactory.getLogger(PlatformUtil.class);
    public static final String PAGE_ENTER_FROM_TASKLIST = "task";
    public static final String PAGE_ENTER_FROM_PROJECTLIST = "project";


    public static FileService getFileService() {
        return (FileService) Container.getBean("fileService");
    }

    public static SysTaskService getTaskService() {
        return (SysTaskService) Container.getBean("TaskService");
    }

    public static WorkFlowCoreService getWorkFlowCoreService() {
        return (WorkFlowCoreService) Container.getBean("WorkFlowCoreService");
    }

    public static SysWorkFlowDefineService getWorkFlowDefineService() {
        return (SysWorkFlowDefineService) Container.getBean("SysWorkFlowDefineService");
    }

    public static SysWorkFlowInstanceService getSysWorkFlowInstanceService() {
        return (SysWorkFlowInstanceService) Container.getBean("SysWorkFlowInstanceService");
    }

    public static NodeService getNodeService() {
        return (NodeService) Container.getBean("fileCenterNodeServiceImpl");
    }

    public static SysOpinionService getSysOpinionService() {
        return (SysOpinionService) Container.getBean("SysOpinionService");
    }
    public static SysUserService getSysUserService() {
        return (SysUserService) Container.getBean("SysUserServiceImpl");
    }

    public static String getLoggedUserId() {
        return SessionUtil.getCurrentUserId();
    }

    public static List<PfOrganVo> getOrganListByUserId(String userId) {
        SysUserService sysuserService = getSysUserService();
        return sysuserService.getOrganListByUser(userId);
    }

    //获取所有部门
    public static List<PfOrganVo> getOrganList() {
        SysUserService sysuserService = getSysUserService();
        return sysuserService.getAllOrganList();
    }

    //根据部门id获取该部门下的所有用户
    public static List<PfUserVo> getUsersByOrganId(String organId) {
        SysUserService sysuserService = getSysUserService();
        return sysuserService.getUserListByOragn(organId);
    }

    public static String getProIDFromPlatform(String workFlowInstanceId) {
        String tempProId = "";
        SysWorkFlowInstanceService tempSysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = tempSysWorkFlowInstanceService.getWorkflowInstance(workFlowInstanceId);
        if (pfWorkFlowInstanceVo != null) {
            tempProId = pfWorkFlowInstanceVo.getProId();
        }
        return tempProId;
    }

    /**
     * 通过proid获取流程类型
     *
     * @param proid
     * @return
     */
    public static String getWorkFlowNameByProid(String proid) {
        String workFlowName = "";
        SysWorkFlowInstanceService tempSysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = tempSysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
        if (pfWorkFlowInstanceVo != null) {
            String workdefindId = pfWorkFlowInstanceVo.getWorkflowDefinitionId();
            SysWorkFlowDefineService sysWorkFlowDefineService = getWorkFlowDefineService();
            PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(workdefindId);
            if (StringUtils.isNotBlank(pfWorkFlowDefineVo.getWorkflowName())) {
                workFlowName = pfWorkFlowDefineVo.getWorkflowName();
            }
        }
        return workFlowName;
    }

    /**
     * @param projectId
     * @return
     */
    public static String getWfRemarkByProjectId(String projectId) {
        SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        SysWorkFlowDefineService sysWorkFlowDefineService = getWorkFlowDefineService();
        PfWorkFlowInstanceVo wfInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(projectId);
        PfWorkFlowDefineVo wfDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(wfInstanceVo.getWorkflowDefinitionId());
        String wfRemark = wfDefineVo.getRemark();
        if (wfRemark == null) {
            wfRemark = "";
        }
        return wfRemark;
    }

    /**
     * zdd 根据项目ID 查找工作流信息
     *
     * @param projectId
     * @return
     */
    public static String getWfInstanceIdByProjectId(String projectId) {
        String wfInstanceId = "";
        if (StringUtils.isNotBlank(projectId)) {
            SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
            PfWorkFlowInstanceVo wfInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(projectId);
            if (wfInstanceVo != null) {
                wfInstanceId = wfInstanceVo.getWorkflowIntanceId();
            }
        }

        return wfInstanceId;
    }

    public static String getPfActivityNameByTaskId(String taskId) {
        SysTaskService sysTaskService = getTaskService();
        String activitDefinitionName = "";
        if (taskId != null && !taskId.equals("")) {
            PfTaskVo pfTask = sysTaskService.getTask(taskId);
            if(pfTask==null)
                pfTask = sysTaskService.getHistoryTask(taskId);
            if (pfTask != null) {
                String activitId = pfTask.getActivityId();
                PfActivityVo pfActivityVo = sysTaskService.getActivity(activitId);
                activitDefinitionName = pfActivityVo.getActivityName();
            }
        }
        return activitDefinitionName;
    }

    public static SysAuthorService getSysAuthorService() {
        return (SysAuthorService) Container.getBean("SysAuthorService");
    }

    /**
     * 获取页面的进入类型
     *
     * @param proid
     * @param taskid
     * @return
     */
    public static String getPageEnterType(String proid, String taskid) {
        if (taskid == null || taskid.trim().length() == 0) {
            return PAGE_ENTER_FROM_PROJECTLIST;
        } else {
            PfTaskVo tempPfTaskVo = getTaskService().getTask(taskid);
            if (tempPfTaskVo == null) {
                // 如果取回来的值为空，则按照项目列表的权限进行获取
                return PAGE_ENTER_FROM_PROJECTLIST;
            } else {
                return PAGE_ENTER_FROM_TASKLIST;
            }
        }
    }

    public static List<PfPartitionInfoVo> getAuthorList(
            Map<String, String> paraMap) {
        List<PfPartitionInfoVo> partitionList = null;

        String taskid = paraMap.get("taskid");
        String proid = paraMap.get("proid");
        String rid = paraMap.get("rid");
        String from = paraMap.get("from");
        String roles = paraMap.get("roles");

        if (from == null) {
            from = "";
        }

        if (from.equalsIgnoreCase(PAGE_ENTER_FROM_TASKLIST)) {
            PfTaskVo tempPfTaskVo = getTaskService().getTask(taskid);
            if (tempPfTaskVo == null) {
                // 如果取回来的值为空，则按照项目列表的权限进行获取
                partitionList = getSysAuthorService()
                        .getProjectResrouceFunAuthorList(proid, roles, rid);
            } else {
                partitionList = getSysAuthorService().getTaskFormAuthorList(
                        taskid, roles, rid);
            }
        } else if (from.equalsIgnoreCase(PAGE_ENTER_FROM_PROJECTLIST)) {
            partitionList = getSysAuthorService()
                    .getProjectResrouceFunAuthorList(proid, roles, rid);
        } else {
            partitionList = getSysAuthorService()
                    .getSystemResrouceFunAuthorList(roles, rid);
        }
        return partitionList;

    }

    /**
     * @param userId
     * @return
     */
    public static List<PfRoleVo> getRoleListByUser(String userId) {
        SysUserService sysuserService = getSysUserService();
        return sysuserService.getRoleListByUser(userId);
    }

    /**
     * 获取用户自定义的意见
     *
     * @param userId
     * @return
     */
    public static List<PfOpinionVo> getUseDefinedOpnion(String userId) {
        SysOpinionService tempSysOpinionService = getSysOpinionService();
        if (tempSysOpinionService == null) {
            return new Vector<PfOpinionVo>();
        }
        return tempSysOpinionService.getOpinionListByUserId(userId);
    }

    public static String getCurrentUserLoginName() {
        SysUserService sysUserService = getSysUserService();
        String userId = SessionUtil.getCurrentUserId();
        PfUserVo pfUserVo = sysUserService.getUserVo(userId);
        return pfUserVo.getLoginName();
    }

    public static PfUserVo getCurrentUserInfo() {
        SysUserService sysUserService = getSysUserService();
        String userId = SessionUtil.getCurrentUserId();
        return sysUserService.getUserVo(userId);
    }

    public static String getCurrentUserId() {
        PfUserVo tempPfUserVo = getCurrentUserInfo();
        if (tempPfUserVo == null) {
            return "";
        }
        return tempPfUserVo.getUserId();
    }

    public static String getCurrentUserDwdm() {
        SysUserService userService = getSysUserService();
        String userDwdm = userService.getUserRegionCode(getCurrentUserId());
        if (userDwdm != null) {
            return userDwdm;
        } else {
            return "";
        }
    }

    //以下方法仅供测试跨数据库使用
    public static List getAllUsersAndDepts() {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        List result = new ArrayList();
        try {
            conn = ((DataSource) Container.getBean("egov")).getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select t1.user_id,t1.user_name,t2.organ_name from pf_user t1,pf_organ t2,pf_user_organ_rel t3 where t1.user_id=t3.user_id and t2.organ_id=t3.organ_id");
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", rs.getString("USER_ID"));
                result.add(map);
            }
        } catch (SQLException e) {
            logger.error("PlatformUtil.getAllUsersAndDepts",e);
        } finally {
            DataSourceManager.attemptClose(rs);
            DataSourceManager.attemptClose(stmt);
            DataSourceManager.attemptClose(conn);
        }
        return result;
    }

    public static String getActivityDesc(String projectId, String activityId) {
        SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        Document document = null;
        try {
            document = DocumentHelper.parseText(sysWorkFlowInstanceService.getWorkflowInstanceXml(projectId));
        } catch (DocumentException e) {
            logger.error("PlatformUtil.getActivityDesc",e);
        }
        if (document == null) {
            throw new NullPointerException();
        }
        Element root = document.getRootElement();
        Node node1 = root.selectSingleNode("//Package/WorkflowProcesses/WorkflowProcess/Activities/Activity[@Id='" + activityId + "']/Description");
        String activityDesc = null;
        if (node1 != null) {
            activityDesc = node1.getText();
        }
        if (activityDesc != null) {
            return activityDesc;
        } else {
            SysTaskService sysTaskService = (SysTaskService) Container.getBean("TaskService");
            PfActivityVo pfActivityVo = sysTaskService.getActivity(activityId);
            if (pfActivityVo != null) {
                activityDesc = pfActivityVo.getActivityName();
            }
            return activityDesc;
        }
    }

    public static List<ActivityModel> getAllActivity(String projectId) {
        List<ActivityModel> activityModelList = new ArrayList<ActivityModel>();
        SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        Document document = null;
        try {
            document = DocumentHelper.parseText(sysWorkFlowInstanceService.getWorkflowInstanceXml(projectId));
        } catch (DocumentException e) {
            logger.error("PlatformUtil.getAllActivity",e);
        }

        if(document != null){
            Element root = document.getRootElement();
            Node node1 = root.selectSingleNode("//Package/WorkflowProcesses/WorkflowProcess/Activities");
            if (node1 != null) {
                List<DefaultElement> activitiesDefaultElement = ((DefaultElement) node1).content();
                if (CollectionUtils.isNotEmpty(activitiesDefaultElement)) {
                    for (DefaultElement activities : activitiesDefaultElement) {
                        ActivityModel activityModel = new ActivityModel(activities);
                        activityModelList.add(activityModel);
                    }
                }
            }
        }
        return activityModelList;
    }

    public static String getCurrentUserName(String userid) {
        SysUserService sysUserService = getSysUserService();
        PfUserVo userVo = sysUserService.getUserVo(userid);
        if (userVo != null) {
            return userVo.getUserName();
        }
        return "";
    }

    /**
     * zx 根据工作流定义id 获取工作流name
     *
     * @param wdid
     * @return PfWorkFlowDefineVo
     */
    public static String getWdNameByWdid(String wdid) {
        String wdName = "";
        SysWorkFlowDefineService sysWorkFlowDefineService = getWorkFlowDefineService();
        if (StringUtils.isNotBlank(wdid)) {
            PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(wdid);
            if (pfWorkFlowDefineVo != null) {
                wdName = pfWorkFlowDefineVo.getWorkflowName();
            }
        }
        return wdName;
    }

    /**
     * @author bianwen
     * @description  根据wiid获取pf_workflow_instance中的proid
     */
    public static String getPfProidByWiid(String wfid) {
        String proid = "";
        SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        PfWorkFlowInstanceVo vo=sysWorkFlowInstanceService.getWorkflowInstance(wfid);
        if(vo!=null){
            proid=vo.getProId();
        }
        return proid;
    }

    /**
     * zdd 根据项目ID 获取流程ID
     *
     * @param projectId
     * @return
     */
    public static String getWfDfidByProjectId(String projectId) {
        String dfid = "";
        SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        PfWorkFlowInstanceVo wfInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(projectId);
        if (wfInstanceVo != null)
            dfid = wfInstanceVo.getWorkflowDefinitionId();
        return dfid;
    }

    /**
     * jyl 根据流程实例ID获取流程ID
     *
     * @param wiid
     * @return
     */
    public static String getWfDfidByWiid(String wiid) {
        String dfid = "";
        SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
        PfWorkFlowInstanceVo wfInstanceVo =sysWorkFlowInstanceService.getWorkflowInstance(wiid);
        if (wfInstanceVo != null) {
            dfid = wfInstanceVo.getWorkflowDefinitionId();
        }
        return dfid;
    }

    /**
     * 工作流节点Id
     *
     * @param taskId
     * @return activitDefinitionId
     */
    public static String getPfActivityIdByTaskId(String taskId) {
        SysTaskService sysTaskService = getTaskService();
        String activitDefinitionId = "";
        if (taskId != null && !taskId.equals("")) {
            PfTaskVo pfTask = sysTaskService.getTask(taskId);
            if (pfTask != null) {
                String activitId = pfTask.getActivityId();
                PfActivityVo pfActivityVo = sysTaskService.getActivity(activitId);
                activitDefinitionId = pfActivityVo.getActivityDefinitionId();
            }
        }
        return activitDefinitionId;
    }

  /**
   * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
   * @param taskId
   * @return
   * @description 根据taskId获取proid
   */
    public static String getProidByTaskId(String taskId) {
        String proid = "";
        if (StringUtils.isNotBlank(taskId)) {
            SysTaskService sysTaskService = getTaskService();
            PfTaskVo pfTask = sysTaskService.getTask(taskId);
            if (pfTask != null) {
                PfActivityVo pfActivityVo = sysTaskService.getActivity(pfTask.getActivityId());
                if(pfActivityVo != null){
                    SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(pfActivityVo.getWorkflowInstanceId());
                    if(pfWorkFlowInstanceVo != null){
                        proid = pfWorkFlowInstanceVo.getProId();
                    }
                }
            }
        }
        return proid;
    }

    /**
     * @author liujie
     *
     * @param wfid
     * @param activitDefinitionId
     * @return activityDefineName
     */
    public static String getTargetActivityName(String wfid,String activitDefinitionId){
        String  activityDefineName = "";
        if(StringUtils.isNotBlank(wfid) && StringUtils.isNotBlank(activitDefinitionId)){
            SysWorkFlowDefineService sysWorkFlowDefineService = getWorkFlowDefineService();
            SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wfid);
            if(pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())){
                PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                if(pfWorkFlowDefineVo != null){
                    String xml = sysWorkFlowDefineService.getWorkFlowDefineXml(pfWorkFlowDefineVo);
                    if(StringUtils.isNotBlank(xml)) {
                        WorkFlowXml workXml = new WorkFlowXml(xml);
                        ActivityModel activityModel = workXml.getActivity(activitDefinitionId);
                        if (activityModel != null && StringUtils.isNotBlank(activityModel.getActivityDefineName())) {
                            activityDefineName = activityModel.getActivityDefineName();
                        }
                    }
                }
            }
        }
        return activityDefineName;
    }

    public static Integer createFileFolderByclmc(Integer parentId, String folderNodeName) {
        com.gtis.fileCenter.model.Node tempNode = null;
        NodeService nodeService = getNodeService();
        if (StringUtils.isNotBlank(folderNodeName)) {
            try {
                tempNode = nodeService.getNode(parentId, folderNodeName, true);
            } catch (NodeNotFoundException e) {
                logger.error("PlatformUtil.createFileFolderByclmc",e);
            }
            return tempNode!=null?tempNode.getId():-1;
        } else {
            return -1;
        }
    }

    /**
     * @author bianwen
     * @param
     * @return
     * @description 收件单材料修改，同时文件中心nodename修改
     */
    public static Integer createFileFolderByclmcAndnodeid(Integer parentId, String folderNodeName,Integer nodeId) {
        com.gtis.fileCenter.model.Node tempNode = null;
        NodeService nodeService = getNodeService();
        if (StringUtils.isNotBlank(folderNodeName)) {
            if(nodeId!=null){
                tempNode=  nodeService.getNode(nodeId) ;
                if(tempNode!=null){
                    tempNode.setName(folderNodeName);
                    nodeService.save(tempNode);
                }
            }
            else {
                try {
                    tempNode = nodeService.getNode(parentId, folderNodeName, true);
                } catch (NodeNotFoundException e) {
                    logger.error("PlatformUtil.createFileFolderByclmcAndnodeid",e);
                }
            }
            if(tempNode != null){
                return tempNode.getId();
            }else{
                return -1;
            }
        } else {
            return -1;
        }
    }
    public static Integer getProjectFileId(String projectId) {
        NodeService nodeService = getNodeService();
        Space space = nodeService.getWorkSpace(Constants.WORK_FLOW_STUFF);
        return createFileFolderByclmc(space.getId(), projectId);
    }

    public String getToken() {
        //zdd 数据中心万能的token
        String token = Constants.TOKEN;
        try {
            NodeService nodeService = getNodeService();
            Space space = nodeService.getWorkSpace(Constants.WORK_FLOW_STUFF);

            if (space != null) {
                token = nodeService.getToken(space);
            }
        } catch (Exception e) {
            logger.error("PlatformUtil.getToken",e);
        }
        return token;
    }

    public static int getAllChildFilesCountByNodeName(Integer parentId, String nodeName) {
        int num = 0;
        NodeService nodeService = getNodeService();
        if (StringUtils.isNotBlank(nodeName)) {
            if (parentId == null) {
                Space space = nodeService.getWorkSpace("WORK_FLOW_STUFF");
                com.gtis.fileCenter.model.Node node = nodeService.getNode(space.getId(), nodeName, true);
                if (node != null) {
                    parentId = node.getId();
                }
            }
            if (parentId != null) {
                com.gtis.fileCenter.model.Node node = getNodeService().getChildNode(parentId, nodeName);
                if (node != null) {
                    num = getNodeService().getAllChildFilesCount(node.getId());
                }
            }

        }
        return num;
    }

    /**
     * zdd 将附件复制到目标节点
     *
     * @param fileIds
     * @param parentId
     * @return
     */
    public static Boolean copyFileImplToNode(List<Integer> fileIds, Integer parentId) {
        if (CollectionUtils.isNotEmpty(fileIds)) {
            Integer[] ids = {};
            ids = fileIds.toArray(ids);
            NodeService nodeService = getNodeService();
            nodeService.copy(ids, parentId, true);
        }
        return true;
    }

    /**
     * zdd 非工作流附件存储目录
     *
     * @param proid
     * @return
     */
    public int creatGlobleNode(String proid) {
        NodeService nodeService = getNodeService();
        Space space = nodeService.getWorkSpace("GLOBLE_STUFF");
        com.gtis.fileCenter.model.Node node = nodeService.getNode(space.getId(), proid, true);
        return node.getId();
    }

    /**
     * zdd  非工作流附件  项目对应的所有附件ID
     *
     * @param proid
     * @return
     */
    public List<Integer> getGlobleFileIds(String proid) {
        List<Integer> ids = new ArrayList<Integer>();
        Integer parentId = creatGlobleNode(proid);
        NodeService nodeService = getNodeService();
        List<com.gtis.fileCenter.model.Node> nodes = nodeService.getAllChildNodes(parentId);
        if (nodes != null) {
            for (com.gtis.fileCenter.model.Node node : nodes) {
                if (node instanceof File) {
                    ids.add(node.getId());
                }
            }
        }
        return ids;
    }

    /**
     * zdd 将磁盘路径下的所有文件上传到 非工作流文件中心
     *
     * @param proid
     * @param basepath
     * @return
     */
    public boolean uploadDiskFileToGlobleFc(String proid, String basepath) {
        boolean bol = false;
        Integer nodeId = creatGlobleNode(proid);
        if (StringUtils.isNotBlank(basepath)) {
            java.io.File dir = new java.io.File(basepath);
            bol = uploadDiskFileToFc(dir, nodeId);
        }
        return bol;
    }

    private Boolean uploadDiskFileToFc(java.io.File dir, Integer parentId) {
        Boolean bol = false;
        try {
            if (dir.isDirectory()) {
                java.io.File f[] = dir.listFiles();
                if (f != null) {
                    for (int i = 0; i < f.length; i++) {
                        if (f[i].isDirectory()) {
                            uploadDiskFileToFc(f[i], parentId);//文件夹 递归调用
                        } else {
                            if (f[i].getName().toLowerCase().endsWith(".db")) {
                                continue;
                            }
                            //文件上传
                            FileService fileService = getFileService();
                            fileService.uploadFile(f[i], parentId, null, null, true, false);
                        }
                    }
                }
            }
            bol = true;
        } catch (Exception e) {
            logger.error("PlatformUtil.uploadDiskFileToFc",e);
            bol = false;
        }
        return bol;
    }

    /**
     * zdd 下载附件到服务器磁盘目录
     *
     * @param proid
     * @param basepath
     * @return
     */
    public boolean downFileToDisk(String proid, String basepath) {
        boolean bol = false;
        if (StringUtils.isNotBlank(proid)) {
            Integer nodeId = getProjectFileId(proid);
            NodeService nodeService = getNodeService();
            String proidPath = "";
            List<com.gtis.fileCenter.model.Node> listNode = nodeService.getChildNodes(nodeId);

            if (StringUtils.isNotBlank(basepath)) {
                proidPath = creatFileToDisk(basepath + "\\" + proid, null);
            } else {
                proidPath = "c:\\temp\\";
                proidPath = creatFileToDisk(proidPath + "\\" + proid, null);
            }
            for (com.gtis.fileCenter.model.Node node : listNode) {
                creatFile(node, proidPath);
            }
        }
        bol = true;
        return bol;
    }

    private Boolean creatFile(com.gtis.fileCenter.model.Node node, String path) {
        Boolean bol = false;
        try {
            if (node instanceof File) {
                File file = (File) node;
                creatFileToDisk(path + "\\" + file.getName(), file);
            } else if (node instanceof NodeImpl) {
                NodeService nodeService = getNodeService();
                List<com.gtis.fileCenter.model.Node> listNode = nodeService.getChildNodes(node.getId());
                path = creatFileToDisk(path + "\\" + node.getName(), null);
                for (com.gtis.fileCenter.model.Node childnode : listNode) {
                    creatFile(childnode, path); //递归调用
                }
            }
            bol = true;
        } catch (Exception e) {
            logger.error("PlatformUtil.creatFile",e);
            bol = false;
        }
        return bol;
    }

    private String creatFileToDisk(String path, File file) {
        try {
            FileService fileService = getFileService();
            if (StringUtils.isNotBlank(path)) {
                java.io.File dir = new java.io.File(path);
                if (file != null) {
                    //zdd 下载附件到磁盘  如果存在 先删除
                    dir.deleteOnExit();
                    fileService.downloadToFile(file.getId(), path);
                } else {
                    //zdd 创建目录
                    if (!dir.exists() && !path.endsWith(java.io.File.separator)) {
                        dir.mkdirs();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("PlatformUtil.creatFileToDisk",e);
        }
        return path;
    }

    /**
     * zdd 将字符串中的opt(业务系统opt参数)参数替换为实际值
     *
     * @param url
     * @return
     */
    public static String initOptProperties(String url) {
        if (url != null) {
            PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
            Properties properties = new Properties();
            properties.putAll(AppConfig.getProperties());
            url = propertyPlaceholderHelper.replacePlaceholders(url, properties);
            if (url.indexOf("${")>-1){
                String key = url.substring(url.indexOf("${")+2,url.indexOf('}'));
                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(AppConfig.getProperty(key))) {
                    url = url.replace("${" + key + "}", AppConfig.getProperty(key));
                }
            }
        }

        return url;
    }

    /**
     * sc 根据项目ID和材料名称删除文明
     *
     * @param proid
     * @param clmc
     */
    public static void deleteFileByProidAndClmc(String proid, String clmc) {
        NodeService nodeService = getNodeService();
        Integer nodeId = getProjectFileId(proid);
        try {
            com.gtis.fileCenter.model.Node node = nodeService.getNode(nodeId, clmc, false);
            nodeService.remove(node.getId());
        }catch (NodeNotFoundException e) {
            logger.error("PlatformUtil.deleteFileByProidAndClmc",e);
        }
    }

    /**
     * zdd 根据业务Name 获取工作流程列表
     *
     * @param businessName
     * @return map :id  name
     */
    public List<HashMap<String, String>> getWorkFlowDefMap(String businessName) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        SysWorkFlowDefineService sysWorkFlowDefineService = getWorkFlowDefineService();
        List<com.gtis.plat.vo.PfWorkFlowDefineVo> pfWorkFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineList();
        if (CollectionUtils.isNotEmpty(pfWorkFlowDefineVoList)) {
            for (PfWorkFlowDefineVo pfWorkFlowDefineVo : pfWorkFlowDefineVoList) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("DM", pfWorkFlowDefineVo.getWorkflowDefinitionId());
                map.put("MC", pfWorkFlowDefineVo.getWorkflowName());
                if (StringUtils.isNotBlank(businessName)) {
                    if (pfWorkFlowDefineVo.getBusinessVo().getBusinessName().equals(businessName)) {
                        list.add(map);
                    }
                } else {
                    list.add(map);
                }
            }
        }
        return list;
    }

    public static void copyNodeToNode(Integer yparentId, String yfolderNodeName, Integer parentId, String folderNodeName, boolean cover) {
        NodeService nodeService = getNodeService();
        try {
            int ynodeid = createFileFolderByclmc(yparentId, yfolderNodeName);
            int nodeid = createFileFolderByclmc(parentId, folderNodeName);
            List<com.gtis.fileCenter.model.Node> yNodeList = nodeService.getAllChildNodes(ynodeid);

            if (CollectionUtils.isNotEmpty(yNodeList)) {
                Integer[] yNodeIds = new Integer[yNodeList.size()];
                int i = 0;
                for (com.gtis.fileCenter.model.Node node : yNodeList) {
                    yNodeIds[i] = node.getId();
                    i++;
                }
                nodeService.copy(yNodeIds, nodeid, cover);
            }
        } catch (NodeNotFoundException e) {
            logger.error("PlatformUtil.copyNodeToNode",e);
        }

    }

    /**
     * zdd 根据业务Name 获取工作流程列表
     *
     * @param wfid
     * @return PfWorkFlowDefineVo
     */
    public static String getBusinessNameByWfid(String wfid) {
        String businessName = "";
        SysWorkFlowDefineService sysWorkFlowDefineService = getWorkFlowDefineService();

        PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(wfid);
        if (pfWorkFlowDefineVo != null && pfWorkFlowDefineVo.getBusinessVo() != null) {
            businessName = pfWorkFlowDefineVo.getBusinessVo().getBusinessName();
        }
        return businessName;
    }

    /**
     * lst  工作流附件  项目对应的所有附件ID
     *
     * @param proid
     * @return
     */
    public static List<Integer> getFileIds(String proid) {
        List<Integer> ids = new ArrayList<Integer>();
        NodeService nodeService = getNodeService();
        Space space = nodeService.getWorkSpace(Constants.WORK_FLOW_STUFF);
        com.gtis.fileCenter.model.Node tempNode = nodeService.getNode(space.getId(), proid, true);
        List<com.gtis.fileCenter.model.Node> nodes = nodeService.getChildNodes(tempNode.getId());
        if (nodes != null) {
            for (com.gtis.fileCenter.model.Node node : nodes) {
                ids.add(node.getId());
            }
        }
        return ids;
    }

    /**
     * lst 工作流附件存储目录
     *
     * @param proid
     * @return
     */
    public static int creatNode(String proid) {
        NodeService nodeService = getNodeService();
        Space space = nodeService.getWorkSpace(Constants.WORK_FLOW_STUFF);
        com.gtis.fileCenter.model.Node node = nodeService.getNode(space.getId(), proid, true);
        return node.getId();
    }

    /**
     * lst 通过项目id删除file
     *
     * @param proId
     * @return
     */
    public Boolean delProjectFileNode(String proId) {
        Boolean bol = true;
        try {
            NodeService nodeService = getNodeService();
            Space space = nodeService.getWorkSpace(Constants.WORK_FLOW_STUFF);
            com.gtis.fileCenter.model.Node tempNode = nodeService.getNode(space.getId(), proId);
            nodeService.remove(tempNode.getId());
        } catch (Exception e) {
            bol = false;
            logger.error("PlatformUtil.delProjectFileNode",e);
        }
        return bol;
    }

    public static Boolean uploadFileFromUrl(String url, Integer parentId, String name) {
        Boolean bol = false;
        try {
            if (StringUtils.isNotBlank(url) && parentId!=null) {
                URL fileUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                if (conn != null) {
                    //增加请求完毕后关闭链接的头信息
                    conn.setRequestProperty("Connection", "close");
                    //文件上传
                    FileService fileService = getFileService();
                    fileService.uploadFile(conn.getInputStream(), parentId, name, null, true, false);
                    conn.disconnect();
                }
            }
            bol = true;
        } catch (Exception e) {
            logger.error("PlatformUtil.uploadFileFromUrl",e);
            bol = false;
        }
        return bol;
    }

    public static String getCurrentUserDwdmByUserid(String userid) {
        SysUserService userService = getSysUserService();
        String userDwdm = userService.getUserRegionCode(userid);
        if (userDwdm != null) {
            return userDwdm;
        } else {
            return "";
        }
    }
    /**
     * @param  userId
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据userid获取对应的所在单位的名称
    */
    public static String getOrganNameByUserid(String userId){
        String organName = "";
        List<PfOrganVo> pfOrganVoList = getOrganListByUserId(userId);
        if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
            PfOrganVo pfOrganVo = pfOrganVoList.get(0);
            if(null != pfOrganVo && StringUtils.isNotBlank(pfOrganVo.getOrganName())){
                organName = pfOrganVo.getOrganName();
            }
        }
        return organName;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param wiid
     * @description 根据wiid获取PfTaskVo
     */
    public static PfTaskVo getPfTaskVoByWiid(String wiid) {
        PfTaskVo pfTaskVo = null;
        if(StringUtils.isNotBlank(wiid)){
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = getSysWorkFlowInstanceService().getWorkflowInstance(wiid);
            List<PfTaskVo> pfTaskVoList =  getTaskService().getTaskListByInstance(pfWorkFlowInstanceVo.getWorkflowIntanceId());
            if(CollectionUtils.isNotEmpty(pfTaskVoList)) {
                pfTaskVo = pfTaskVoList.get(0);
            }
        }
        return pfTaskVo;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param pfTaskVo
     * @description 根据pfTaskVo获取activityName
     */
    public static String getActivityNameByPfTaskVo(PfTaskVo pfTaskVo) {
        String activityName = "";
        if(pfTaskVo!= null){
            PfActivityVo pfActivityVo = getTaskService().getActivity(pfTaskVo.getActivityId());
            if(pfActivityVo!= null) {
                activityName = pfActivityVo.getActivityName();
            }
        }
        return activityName;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param pfTaskVo
     * @description 根据pfTaskVo获取activitDefinitionId
     */
    public static String getActivitDefinitionIdByPfTaskVo(PfTaskVo pfTaskVo){
        String activitDefinitionId = "";
        if(pfTaskVo != null) {
            SysTaskService sysTaskService = getTaskService();
            PfActivityVo pfActivityVo = sysTaskService.getActivity(pfTaskVo.getActivityId());
            activitDefinitionId = pfActivityVo.getActivityDefinitionId();
        }
        return activitDefinitionId;
    }

    /**
     * @author liujie
     *
     * @param wiid
     * @param activitDefinitionName
     * @return 获取流程节点定义id
     */
    public static String getActivitDefinitionIdByPfTaskVo(String wiid,String activitDefinitionName){
        String activitDefinitionId = "";
        if(StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(activitDefinitionName)){
            SysWorkFlowDefineService sysWorkFlowDefineService = getWorkFlowDefineService();
            SysWorkFlowInstanceService sysWorkFlowInstanceService = getSysWorkFlowInstanceService();
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
            if(pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())){
                PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                if(pfWorkFlowDefineVo != null){
                    String xml = sysWorkFlowDefineService.getWorkFlowDefineXml(pfWorkFlowDefineVo);
                    if(StringUtils.isNotBlank(xml)) {
                        WorkFlowXml workXml = new WorkFlowXml(xml);
                        List<ActivityModel> activityList = workXml.getActivityList();
                        if (CollectionUtils.isNotEmpty(activityList)) {
                            for(ActivityModel activityModel:activityList){
                                if(StringUtils.equals(activityModel.getActivityDefineName(),activitDefinitionName)){
                                    activitDefinitionId = activityModel.getDefineId();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return activitDefinitionId;
    }

    public static String getActivityName(String proid) {
        String activityName = "";
        if (StringUtils.isNotBlank(proid)) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = getSysWorkFlowInstanceService().getWorkflowInstanceByProId(proid);
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowIntanceId())) {
                SysTaskService sysTaskService = (SysTaskService) Container.getBean("TaskService");
                List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
                    activityName = getPfActivityNameByTaskId(pfTaskVoList.get(0).getTaskId());
                }
            }
        }
        return activityName;
    }
}
