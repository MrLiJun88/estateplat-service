package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcTdsyq;
import org.springframework.stereotype.Component;

/**
 * @description 土地所有权登记信息
 * Created by lst on 2015/3/18
 */
public interface BdcTdsyqMapper {
    /**
     * 获取土地所有权登记信息
     *
     * @param proid
     * @return
     */
    BdcTdsyq getBdcTdsyq(String proid);
}
