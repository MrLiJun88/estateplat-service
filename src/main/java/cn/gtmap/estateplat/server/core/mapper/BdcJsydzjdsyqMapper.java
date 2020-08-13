package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcJsydzjdsyq;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by lst on 2015/3/18
 * @description 建设用地使用权、宅基地使用权登记信息
 */
public interface BdcJsydzjdsyqMapper {
    /**
     * 获取建设用地使用权、宅基地使用权登记信息
     *
     * @param map
     * @return
     */
    BdcJsydzjdsyq getBdcJsydzjdsyq(Map map);

    /**
     * 获取建设用地使用权、宅基地使用权登记信息
     *
     * @param map
     * @return
     */
    List<BdcJsydzjdsyq> getBdcJsydzjdsyqList(Map map);


    /**
     * 根据地籍号，获取建设用地
     *
     * @param djh
     * @return
     */
    List<BdcJsydzjdsyq> getJsyByDjh(String djh);
}
