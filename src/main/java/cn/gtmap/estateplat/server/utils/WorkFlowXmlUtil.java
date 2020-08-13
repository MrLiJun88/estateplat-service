package cn.gtmap.estateplat.server.utils;

import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.spring.Container;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
 * @version 1.0, ${date}
 * @description: ${todo}
 */
public class WorkFlowXmlUtil {
    @Autowired
    SysWorkFlowInstanceService workFlowIntanceService;


    public SysWorkFlowInstanceService getWorkFlowIntanceService() {
        return workFlowIntanceService;
    }

    public void setWorkFlowIntanceService(SysWorkFlowInstanceService workFlowIntanceService) {
        this.workFlowIntanceService = workFlowIntanceService;
    }

    public static WorkFlowXml getInstanceModel(PfWorkFlowInstanceVo instanceVo) {
        WorkFlowXmlUtil factory = (WorkFlowXmlUtil) Container
                .getBean("workFlowXmlUtil");
        return factory.getWorkFlowInstanceModel(instanceVo);
    }

    public WorkFlowXml getWorkFlowInstanceModel(PfWorkFlowInstanceVo instanceVo) {
        String xml = workFlowIntanceService
                .getWorkflowInstanceXml(instanceVo);
        WorkFlowXml modelXml = new WorkFlowXml(xml);
        modelXml.setModifyDate(instanceVo.getModifyDate());
        return modelXml;
    }
}
