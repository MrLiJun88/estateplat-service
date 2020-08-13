package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXtSjglResource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description 登记资源关系
 * Created by lst on 2015/3/18
 */
public interface BdcXtSjglRelationMapper {
    /**
     * 通过登记类型获取资源
     *
     * @param djlxId
     * @return
     */
    List<BdcXtSjglResource> getResourceByDjlx(@Param("djlxId") String djlxId);

    /**
     * 获取登记类型资源最大序号
     *
     * @return
     */
    Integer getMaxXh(@Param("djlxId") String djlxId);
}
