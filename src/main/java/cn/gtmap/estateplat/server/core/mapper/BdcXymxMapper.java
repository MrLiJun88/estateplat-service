package cn.gtmap.estateplat.server.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Component
public interface BdcXymxMapper {
    /**
     * 获取所有数据
     *
     * @param map
     * @return
     */
    List<Map> getBdcXymxListByPage(Map map);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description
     **/
    HashMap getXsBdcXyxxByZjh(@Param("qlrzjh") String qlrzjh, @Param("qlrmc") String qlrmc);
}
