package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/4/13
 * @description 不动产项目受理编号编号服务
 */
public interface BdcSlbhBhService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version 1.0, 2017/4/13
     * @param bdcXm 不动产项目
     * @return 返回新生成的受理编号
     * @description 为不动产项目生成受理编号，并返回
     */
    String generateBdcXmSlbh(final BdcXm bdcXm);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @return
     * @description 获取受理编号
     */
    String getBdcXmSlbh(final BdcXm bdcXm);

}
