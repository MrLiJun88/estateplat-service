package cn.gtmap.estateplat.server.service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/1/24
 * @description 工作流服务
 */
public interface WorkFlowService {


    /**
     * @author <a href="mailto:liujie@gtmap.cn">xusong</a>
     * @param wiid
     * @param taskid
     * @param userid
     * @description 删除工作流
     */
    void deleteProject(String wiid, String taskid, String userid);
}
