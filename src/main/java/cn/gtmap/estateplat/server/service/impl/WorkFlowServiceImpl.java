package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.WorkFlowService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.WorkFlowXml;
import cn.gtmap.estateplat.server.utils.WorkFlowXmlUtil;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.*;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;

import static cn.gtmap.estateplat.server.utils.Constants.WORK_FLOW_STUFF;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/1/24
 * @description 工作流服务
 */
@Service
public class WorkFlowServiceImpl implements WorkFlowService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    SysTaskService sysTaskService;
    @Autowired
    WorkFlowCoreService workFlowCoreService;
    @Autowired
    SysWorkFlowInstanceService workFlowIntanceService;
    @Autowired
    SysTaskService taskService;
    @Autowired
    SysDynamicSignService sysDynamicSignService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService nodeService;
    @Autowired
    SysActivityService sysActivityService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());



    @Override
    public void deleteProject(String wiid, String taskid, String userid){
        //调用 paltfrom 执行平台工作流事件
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
        if (StringUtils.isNotBlank(wiid)) {
            pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
        }else if (StringUtils.isNotBlank(taskid)) {
            PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
            if (pfTaskVo != null && StringUtils.isNotBlank(pfTaskVo.getActivityId())) {
                PfActivityVo pfActivityVo = sysActivityService.getActivityById(pfTaskVo.getActivityId());
                if (pfActivityVo != null && StringUtils.isNotBlank(pfActivityVo.getWorkflowInstanceId())) {
                    pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(pfActivityVo.getWorkflowInstanceId());
                }
            }
        }
        String msg = "";
        try{
            msg = delTask(pfWorkFlowInstanceVo.getWorkflowIntanceId(), taskid, userid, "", pfWorkFlowInstanceVo.getProId(), pfWorkFlowInstanceVo);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }



    /**
     * 删除
     * 管理员 或 在首节点,同时也是创建人 可删除工作流;
     * 在首节点，不是创建人,是参与者 可删除所属任务;
     * 在首节点，不是创建人和参与者 或 不在首节点  无法删除
     *
     * @return
     * @throws Exception
     */
    private String delTask(String wiid, String taskid, String userid, String reason, String proid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo) {
        String msg = "";
        if (StringUtils.isBlank(wiid))
            wiid = pfWorkFlowInstanceVo.getWorkflowIntanceId();
        PfTaskVo taskVo = taskService.getTask(taskid);
        try {
            switch (permitDel(taskVo, userid)) {
                case 0: {
                    delProject(wiid, userid);
                    msg = "1";
                    break;
                }
                case 1: {
                    delProject(wiid, userid);
                    msg = "1";
                    break;
                }
                case 3: {
                    msg = "3";
                    throw new RuntimeException(msg);
                }
                default: {
                    msg = "2";
                    throw new RuntimeException(msg);
                }
            }
        } catch (Exception e) {
            if(StringUtils.isBlank(msg))
                msg="3";
            throw new RuntimeException(msg);
        }

        return msg;
    }


    /**
     * 判断删除情况
     *
     * @param taskVo
     * @return 0 超级用户 或 在首节点,同时也是创建人;
     * 1 在首节点，不是创建人;是参与者(参与者只有一人时);
     * 2 在首节点，不是创建人;是参与者(参与者有多人时)
     * 3 该项目不在首节点，无法删除
     * 5 无返回
     */
    private int permitDel(PfTaskVo taskVo, String userId) throws Exception {
        if (StringUtils.equals(userId, "0") || taskVo == null) {
            return 0;
        } else {
            PfActivityVo activityVo = taskService.getActivity(taskVo.getActivityId());
            PfWorkFlowInstanceVo workFlowInstanceVo = workFlowIntanceService.getWorkflowInstance(activityVo.getWorkflowInstanceId());
            WorkFlowXml workXml = WorkFlowXmlUtil.getInstanceModel(workFlowInstanceVo);
            if (workXml.getBeginActivityDefine().equals(activityVo.getActivityDefinitionId())) {
                if (workFlowInstanceVo.getCreateUser().equals(userId)) {
                    return 0;
                } else {
                    if (userId.equals(taskVo.getUserVo().getUserId())) {
                        return 0;
                    } else {
                        return 3;
                    }
                }
            }else{
                return 2;
            }
        }
    }

    /**
     * 删除项目
     *
     * @param wiid
     */
    private String delProject(String wiid, String userId){
        String msg = "";
        PfWorkFlowInstanceVo workFlowInstanceVo = workFlowIntanceService.getWorkflowInstance(wiid);
        try {
            //删除附件
            PfWorkFlowInstanceVo intanceVo = workFlowIntanceService.getWorkflowInstance(wiid);
            Space space = nodeService.getWorkSpace(WORK_FLOW_STUFF);
            Node tempNode = nodeService.getNode(space.getId(), intanceVo.getProId());
            nodeService.remove(tempNode.getId());
        } catch (Exception e) {
        }
        //删除用户签名
        try {
            sysDynamicSignService.deleteUserSignByProId(workFlowInstanceVo.getProId());
        } catch (Exception e) {
        }

        //删除工作流实例
        try {
            workFlowCoreService.deleteWorkFlowInstance(userId, wiid);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }
}
