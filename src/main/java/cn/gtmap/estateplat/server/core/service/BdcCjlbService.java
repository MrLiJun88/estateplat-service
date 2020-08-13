package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcCjlb;

/**
* 不动产登记持件信息
* @author yy
* @version V1.0, 16-5-24
* @since
*/
public interface BdcCjlbService {
    /**
     * 根据主键插入或更新持件信息
     * @param bdcCjlb 插入持件信息
     */
    void insertCjlbOrUpdateByPrimaryKey(final BdcCjlb bdcCjlb);

}
