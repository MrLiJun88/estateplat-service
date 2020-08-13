package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.service.server.BdcSlbhService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/31
 * @description 不动产项目受理编号服务
 */
//@Service
public class BdcSlbhServiceImpl implements BdcSlbhService {

    @Autowired
    BdcSlbhCustomServiceContext bdcSlbhCustomServiceContext;

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/31
     * @param bdcXm 不动产项目
     * @return 返回新生成的受理编号
     * @description 为不动产项目生成受理编号，并返回
     */
    @Override
    public synchronized String generateBdcXmSlbh(final BdcXm bdcXm) {
         return bdcSlbhCustomServiceContext.getSlbhService().generateBdcXmSlbh(bdcXm);
    }

}
