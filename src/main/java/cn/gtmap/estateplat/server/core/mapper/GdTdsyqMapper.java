package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.GdTdsyq;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 过渡土地所有权
 */
public interface GdTdsyqMapper {
    /**
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @param bdcdyh 不动产单元号
     * @return
     * @Description: 通过qlid关联gd_tdsyq和gd_ql_dyh_rel
     */
    List<GdTdsyq> getTdGdTdsyqListByBdcdyh(String bdcdyh);
}
