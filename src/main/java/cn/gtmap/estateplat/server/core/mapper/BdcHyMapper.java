package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcHy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * 宗海信息
 * @author zhx
 * @version V1.0, 15-3-18
 */
@Repository
public interface BdcHyMapper {

    /**
     * zhx 根据宗地宗海号查找BdcHy
     *
     * @param zdzhh
     * @return
     */
    BdcHy selectBdcHy(String zdzhh);

}
