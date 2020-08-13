package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcJzwgy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 建筑物区分所有权业主共有部分登记信息
 * Created by lst on 2015/3/18
 */
public interface BdcJzwgyMapper {
    /**
     * 获取建筑物区分所有权业主共有部分登记信息
     *
     * @param map
     * @return
     */
    BdcJzwgy getBdcJzwgy(Map map);
}
