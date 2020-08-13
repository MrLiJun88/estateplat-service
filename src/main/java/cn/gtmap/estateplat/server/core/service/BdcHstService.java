package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcHst;
import cn.gtmap.estateplat.model.server.core.DjsjZdZdt;

import java.util.List;

/**
 * 不动产登记房产户室图
 * @author zzhw
 * @version V1.0, 15-3-18
 */
public interface BdcHstService {
    /**
     * zzhw根据bdcdyh查视图hst
     *
     * @param bdcdyh
     * @return
     */
    List<BdcHst> selectBdcHst(final String bdcdyh);

    List<DjsjZdZdt> selectBdcZdt(final String djh);
}
