package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXtSjglResource;

import java.util.List;

/**
 * 登记资源关系
 * Created by lst on 2015/3/21
 */
public interface BdcXtSjglRelationService {
    /**
     * 根据等级类型获取资源
     *
     * @param djlxId
     * @return
     */
    List<BdcXtSjglResource> getResourceByDjlx(final String djlxId);

    /**
     * zdd 将URL中的opt参数替换为实际值
     *
     * @param resourceList
     * @return
     */
    List<BdcXtSjglResource> initPropertyUrl(List<BdcXtSjglResource> resourceList);

    /**
     * 获取登记类型资源最大序号
     *
     * @return
     */
    Integer getMaxXh(final String djlxId);

}
