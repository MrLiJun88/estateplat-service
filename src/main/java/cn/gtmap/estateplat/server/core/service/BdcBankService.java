package cn.gtmap.estateplat.server.core.service;


import cn.gtmap.estateplat.model.server.core.BdcXtYh;

import java.util.List;

/**
 * 不动产登记银行配置.
 *
 * @author liujie
 * @version V1.0, 15-9-18
 * @since
 */
public interface BdcBankService {

    /**
     * 根据主键删除银行配置.
     * @param ids 主键，多个主键以逗号隔开
     */
    void deleteBankByPrimaryKey(final String ids);

    /**
     * 根据主键插入或更新银行配置.
     * @param bdcBank 银行配置信息
     */
    void insertOrUpdateByPrimaryKey(final BdcXtYh bdcBank);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取系统银行信息
    *@Date 18:19 2017/4/24
    */
    List<BdcXtYh> getBankListByPage();
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:根据yhmc获取银行信息
    *@Date 10:57 2017/4/25
    */
    List<BdcXtYh> getBankListByYhmc(String yhmc);
}
