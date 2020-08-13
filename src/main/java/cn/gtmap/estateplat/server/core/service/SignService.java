package cn.gtmap.estateplat.server.core.service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/3/5
 * @description 电子签名服务
 */
public interface SignService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 项目ID
     * @param userid 用户ID
     * @param activityid 当前活动ID
     * @param targetActivityDefids 目标活动定义ID
     * @return
     * @description 针对工作流活动取回或退回的签名意见处理
     */
    void handleRetreatSign(final String proid, final String userid, final String activityid, final String targetActivityDefids);

    /**
     * @author <a href="mailto:xuchao@gtmap.cn">xuchao</a>
     * @param proid 项目ID
     * @return
     * @description 针对工作流活动转发，根据配置签名userid,和key ，并自动签名
     */
    String handleTurnAutoSignBySignkeys(final String proid,final String userid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 昆山流程转发自动签名
     */
    void autoSignBeforeTurn(final String proid,final String userid,final String workflowNodeName);
}
