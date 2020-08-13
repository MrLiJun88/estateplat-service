package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcHst;
import cn.gtmap.estateplat.model.server.core.DjsjZdZdt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author zzhw
 * @version V1.0, 15-3-18
 * @description 权籍数据中的房屋户室图
 */

public interface BdcHstMapper {

    /**
     * zzhw 根据bdcdyh查视图hst
     *
     * @param bdcdyh
     * @return
     */
    List<BdcHst> selectBdcHst(String bdcdyh);

    List<DjsjZdZdt> selectBdcZdt(String djh);

}
