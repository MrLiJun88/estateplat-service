package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/12/18
 * @description 自动工作流服务
 */
@Service
public interface AutoWorkFlowService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcZs
     * @param activityName
     * @description 自动转发工作流
     */
    void autoTurnProject(BdcZs bdcZs, String activityName,String userid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @param activityName
     * @param targetActivityName
     * @param userid
     * @description 自动转发工作流
     */
    void autoTurnProjectByBdcXm(BdcXm bdcXm, String activityName, String targetActivityName, String userid);
}
