package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcTzmDjsjRel;

import java.util.List;
import java.util.Map;

/**
 *
 * User: sc
 * Date: 15-9-9
 * Time: 下午4:21
 * @description 特征码登记数据关系
 */
public interface BdcTzmDjsjRelService {
    /**
     * sc 查询特征码登记事由对照表
     *
     * @param map
     * @return
     */
    List<BdcTzmDjsjRel> andEqualQueryBdcTzmDjsjRel(final Map<String, String> map);

    /**
     * sc 根据宗地宗海特征码和定作物特征码查询登记事由
     *
     * @param zdzhtzm
     * @param dzwtzm
     * @return
     */
    String getDjsjByTzm(final String zdzhtzm,final String dzwtzm);

    /**
     * sc 根据不动产单元号查询登记事由
     *
     * @param bdcdyh
     * @return
     */
    String getDjsjByBdcdyh(final String bdcdyh);
}
