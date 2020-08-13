package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcJzwsyq;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 构（建）筑物所有权登记信息
 * Created by lst on 2015/3/18
 */
public interface BdcJzwsyqMapper {
    /**
     * 获取构（建）筑物所有权登记信息
     *
     * @param proid
     * @return
     */
    BdcJzwsyq getBdcJzwsyq(String proid);

    /**
     * 保存构（建）筑物所有权登记信息
     *
     * @param bdcJzwsyq
     * @return
     */
    void saveBdcJzwsyq(BdcJzwsyq bdcJzwsyq);
}
