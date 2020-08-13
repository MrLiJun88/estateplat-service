package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.server.model.PfWorkFlowEvent;
import cn.gtmap.estateplat.server.service.PfWorkFlowEventConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/12/19
 * @description 工作流事件配置表服务
 */
@Service
public class PfWorkFlowEventConfigurationServiceImpl implements PfWorkFlowEventConfigurationService {
    @Autowired
    private EntityMapper entityMapper;


    @Override
    public List<PfWorkFlowEvent> getPfWorkFlowEventList(String workflowDefinitionId, String activityDefinitionId, String workFlowEventName){
        List<PfWorkFlowEvent> pfWorkFlowEventList = null;
        if(StringUtils.isNotBlank(workflowDefinitionId)&&StringUtils.isNotBlank(workFlowEventName)){
            Example example = new Example(PfWorkFlowEvent.class);
            if(StringUtils.isNotBlank(activityDefinitionId)){
                example.createCriteria().andEqualTo("workFlowDefinitionId", workflowDefinitionId).andEqualTo("activityDefinitionId", activityDefinitionId).andEqualTo("workFlowEventName", workFlowEventName);
            }else{
                example.createCriteria().andEqualTo("workFlowDefinitionId", workflowDefinitionId).andEqualTo("workFlowEventName", workFlowEventName);
            }
            pfWorkFlowEventList = entityMapper.selectByExample(example);
        }
        return pfWorkFlowEventList;
    }
}
