package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/31
 * @description 不动产项目受理编号服务
 */
public interface BdcSlbhService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/31
     * @param bdcXm 不动产项目
     * @return 返回新生成的受理编号
     * @description 为不动产项目生成受理编号，并返回
     */
    String generateBdcXmSlbh(final BdcXm bdcXm);
}
