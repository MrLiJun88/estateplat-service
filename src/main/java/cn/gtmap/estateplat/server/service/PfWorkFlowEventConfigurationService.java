package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.server.model.PfWorkFlowEvent;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/12/19
 * @description 工作流事件配置表服务
 */
public interface PfWorkFlowEventConfigurationService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param workflowDefinitionId
     * @param activityDefinitionId
     * @param workflowEventName
     * @description 获取工作流事件配置表数据
     */
    public List<PfWorkFlowEvent> getPfWorkFlowEventList(String workflowDefinitionId, String activityDefinitionId,String workflowEventName);
}
