package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcLq;
import org.springframework.stereotype.Component;

/**
 * @description 不动产林权登记信息
 * Created by lst on 2015/3/18
 */
public interface BdcLqMapper {
    /**
     * 获取不动产林权登记信息
     *
     * @param proid
     * @return
     */
    BdcLq getBdcLq(String proid);

    /**
     * 保存不动产林权登记信息
     *
     * @param bdcLq
     * @return
     */
    void saveBdcLq(BdcLq bdcLq);
}
