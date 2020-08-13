package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcZsCd;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2019/12/12
 * @description 不动产证书裁定
 */
@Repository
public interface BdcZsCdMapper {
    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/7/28 14:43
      * @description 查询不动产证书裁定
      */
    List<BdcZsCd> getBdcZscdList(Map map);
}
