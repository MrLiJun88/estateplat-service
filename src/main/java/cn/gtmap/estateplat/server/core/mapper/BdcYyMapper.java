package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcYy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 异议登记信息
 * Created by lst on 2015/3/17
 */
@Repository
public interface BdcYyMapper {
    /**
     * 获取异议登记信息
     *
     * @param proid
     * @return
     */
    BdcYy getBdcYyByProid(String proid);

    /**
     * 保存异议登记信息
     *
     * @param bdcYy
     * @return
     */
    void saveBdcYy(BdcYy bdcYy);

    /*
   * zwq 可以通过bdcdyh查询异议登记信息
   *
   * */

    List<BdcYy> getBdcYyBybdcdyh(Map map);

    /**
     * 获取不动产异议登记信息
     *
     * @param map
     * @return
     */
    List<BdcYy> queryBdcYy(final Map map);


}
