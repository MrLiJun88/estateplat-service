package cn.gtmap.estateplat.server.core.service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/3/7
 */
public interface FzWorkFlowBackService {
    /*
     * 发证节点退回处理
     * */
    void fzWorkFlowBack(final String proid,final String userid,final String activityid,final String targetActivityDefids);
}
