package cn.gtmap.estateplat.server.service.config;

import cn.gtmap.estateplat.model.server.core.BdcXm;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/9/18
 * @description 冗余字段同步服务
 */
public interface ConfigRedundantFieldService {
    /**
     * @param bdcXm
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 同步字段信息
     */
    void synchronizationField(BdcXm bdcXm);

    /**
     * @param wiid 工作流实例ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 批量同步字段信息
     */
    void synchronizationField(String wiid);
}
