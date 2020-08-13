package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZsCd;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2019/12/12
 * @description 不动产证书裁定
 */
public interface BdcZsCdService {

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 增加不动产证书裁定
     */
    Map addBdcZsCd(BdcZsCd bdcZsCd);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 使用流程进行裁定 根据不动产项目更新bdczscd状况
     */
    void updateBdcZscdByBdcXm(BdcXm bdcXm);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 对于解除裁定流程 根据不动产项目更新bdczscd状况
     */
    void cancelBdcZscdByBdcXm(BdcXm bdcXm);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/7/28 14:41
      * @description  查询证书裁定
      */
    List<BdcZsCd> getBdcZscdList(Map paramMap);
}
