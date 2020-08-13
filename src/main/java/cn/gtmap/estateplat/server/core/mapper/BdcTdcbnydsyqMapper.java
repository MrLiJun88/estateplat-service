package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
import org.springframework.stereotype.Repository;

/**
 * @description 土地承包经营权、农用地使用权登记信息
 * Created by lst on 2015/3/18
 */
@Repository
public interface BdcTdcbnydsyqMapper {
    /**
     * 获取土地承包经营权、农用地使用权登记信息
     *
     * @param proid
     * @return
     */
    BdcTdcbnydsyq getBdcTdcbnydsyq(String proid);
}
