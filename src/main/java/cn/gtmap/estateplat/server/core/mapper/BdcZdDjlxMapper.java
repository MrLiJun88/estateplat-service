package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcZdDjlx;
import org.springframework.stereotype.Component;

/**
 * @description 登记类型字典表
 * Date: 15-3-29
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public interface BdcZdDjlxMapper {
    /**
     * 根据名称获取登记类型字典项
     * @param mc
     * @return
     */
    BdcZdDjlx getBdcZdDjlx(String mc);
}
