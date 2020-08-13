package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcQsq;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 其他相关权利登记信息（取水权、探矿权、采矿权等）
 * Created by lst on 2015/3/18
 */
public interface BdcQsqMapper {
    /**
     * 获取其他相关权利登记信息（取水权、探矿权、采矿权等）
     *
     * @param map
     * @return
     */
    BdcQsq getBdcQsq(Map map);

    /**
     * 保存其他相关权利登记信息（取水权、探矿权、采矿权等）
     *
     * @param bdcQsq
     * @return
     */
    void saveBdcQsq(BdcQsq bdcQsq);
}
