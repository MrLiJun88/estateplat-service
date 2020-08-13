package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXymx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public interface BdcXymxService {

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
     * @description 通过xyglid获取相应的信用明细数据
     **/
    List<BdcXymx> getBdcXymxByXyglid(String xyglid);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过证件号获取现势信用信息，即权利人+证件种类+证件号
     **/
    HashMap getXsBdcXyxxByZjh(String qlrzjh,String qlrmc);
}
