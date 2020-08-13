package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcDyq;
import org.springframework.stereotype.Component;

/**
 *
 * Created by lst on 2015/3/18
 * @description 地役权登记信息
 */

public interface BdcDyqMapper {
    /**
     * 获取地役权登记信息
     *
     * @param proid
     * @return
     */
    BdcDyq getBdcDyqByProid(String proid);

    /**
     * 保存地役权登记信息
     *
     * @param bdcDyq
     * @return
     */
    void saveBdcDyq(BdcDyq bdcDyq);
}
