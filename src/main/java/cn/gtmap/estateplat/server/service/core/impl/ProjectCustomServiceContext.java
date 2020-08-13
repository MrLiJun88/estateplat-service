package cn.gtmap.estateplat.server.service.core.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.service.core.ProjectCustomService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">jane</a>
 * @version 1.0, 2016/4/19
 * @desciption 登记服务Context
 */
public class ProjectCustomServiceContext {
    private final static String DEFAULT_TYPE="default";

    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;

    private Map<String, ProjectCustomService> djServiceMap;


    public void setDjServiceMap(Map<String, ProjectCustomService> djServiceMap) {
        this.djServiceMap = djServiceMap;
    }

    /**
     *
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 获取登记服务
     * @param sqlx 申请类型
     * @param qllx 权利类型
     */
    public ProjectCustomService getDjService(String sqlx, String qllx) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(StringUtils.isNotBlank(sqlx)?sqlx:"");
        if(StringUtils.isNotBlank(qllx)) {
            keyBuilder.append("_");
            keyBuilder.append(qllx);
        }
        return djServiceMap.containsKey(keyBuilder.toString())?djServiceMap.get(keyBuilder.toString()):djServiceMap.get(DEFAULT_TYPE);
    }

    /**
     *
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 获取登记服务
     * @param wiid 工作流项目ID
     */
    public ProjectCustomService getDjServiceByWiid(String wiid) {
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
        if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
            String sqlx = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if(CollectionUtils.isNotEmpty(bdcXmList)) {
                String qllx = null;
                if(bdcXmList.size()==1)
                    qllx=bdcXmList.get(0).getQllx();
                return getDjService(sqlx, qllx);
            }
        }
        return djServiceMap.get(DEFAULT_TYPE);
    }
}
