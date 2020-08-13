package cn.gtmap.estateplat.server.service;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/5/7
 * @description 不动产登记工作流项目登簿会调用该接口
 */
public interface RegisterProjectService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param userid
     * @return
     * @description 登簿项目
     */
    void registerProject(String proid, String userid) throws Exception;

}
