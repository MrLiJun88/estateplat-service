package cn.gtmap.estateplat.server.service.config;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/9/19
 * @description 更新相关证书证明号、不动产单元、共享库等状态服务
 */
public interface ConfigSyncQlztService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @return
     * @description 更新相关证书证明号、不动产单元、共享库等状态
     */
    void syncBdcRelateQlzt(String proid);
}
