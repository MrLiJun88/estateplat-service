package cn.gtmap.estateplat.server.core.service;

import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/3/27
 * @description 不动产交易状态接口
 */
public interface BdcdyTradingStatusService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param fwbm
     * @return
     * @description 通过房屋编码查询存量房备案合同状态
     */
    String getTradingStatus(String fwbm);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param fwbm
     * @return
     * @description 根据房屋编码获取不动产单元交易状态 蓝特接口
     */
    String getLtTradingStatus(String fwbm);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @param djzx
     * @return
     * @description 验证交易状态
     */
    Map validate(String proid, String djzx);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 保存前验证通过登记子项验证交易状态
     */
    Map checkBdcTradingStatusBeforeSave(String proid,String djzx);

    /**
     * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @param bdcqzh
     * @return
     * @description 根据不动产权证获取交易备案状态（常熟）
     */
    String getTradingFilingStatus(String bdcqzh);
}
