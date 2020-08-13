package cn.gtmap.estateplat.server.service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/4/29
 * @description 工作流事件处理接口
 */
public interface WorkFlowEventService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param activityid
     * @param userid
     * @param taskid
     * @param targetActivityDefids
     * @return
     * @description 工作流事件登簿（改变权利状态、改变项目状态、赋值的登簿信息（登簿人、登簿时间）、生成证书或者完善不动产权证号）
     */
    void workFlowEventRegister(String proid, String activityid, String userid, String taskid, String targetActivityDefids) throws Exception;



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param userid
     * @return
     * @description 工作流事件登簿（改变权利状态、改变项目状态、赋值的登簿信息（登簿人、登簿时间）、生成证书或者完善不动产权证号）
     */
    void workFlowEventRegister(String proid, String userid) throws Exception;
}
