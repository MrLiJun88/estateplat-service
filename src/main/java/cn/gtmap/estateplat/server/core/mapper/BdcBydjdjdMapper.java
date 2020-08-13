package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBydjdjd;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/11/12
 * @description  不动产不予登记登记单
 */
public interface BdcBydjdjdMapper {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param  map
     * @return
     * @description 获取证最大流水号
     */
    Integer getMaxLsh(HashMap map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param   map
     * @return
     * @description 获取不动产不予登记登记单
     */
    List<BdcBydjdjd> getBdcBydjdjd(HashMap map);
}
