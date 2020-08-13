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
public interface BdcYhService {
    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@Description:根据yhmc获取银行信息
     *@Date 10:57 2017/4/25
     */
    List<BdcXtYh> getBankListByYhmc(String yhmc);
}
