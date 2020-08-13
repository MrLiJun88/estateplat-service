package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcHysyq;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 海域（含无居民海岛） 使用权登记信息
 * Created by lst on 2015/3/18
 */
public interface BdcHysyqMapper {
    /**
     * 获取海域（含无居民海岛） 使用权登记信息
     *
     * @param map
     * @return
     */
    BdcHysyq getBdcHysyq(Map map);

    /**
     * 保存海域（含无居民海岛） 使用权登记信息
     *
     * @param bdcHysyq
     * @return
     */
    void saveBdcHysyq(BdcHysyq bdcHysyq);
}
