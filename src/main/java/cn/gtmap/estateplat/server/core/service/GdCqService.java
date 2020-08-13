package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
import cn.gtmap.estateplat.model.server.core.GdCq;

/**
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-10
 * @description 过渡草权服务
 */
public interface GdCqService {

    /**
     * zdd 根据草地产权证主键查找草权信息
     *
     * @param cqid
     * @return
     */
    GdCq queryGdCqById(final String cqid);

    /**
     * zdd 从过渡草权信息中读取数据
     *
     * @param gdCq
     * @param tdcbnydsyq
     * @return
     */
    BdcTdcbnydsyq getBdcTdcbFromGdCq(final GdCq gdCq, BdcTdcbnydsyq tdcbnydsyq);
}
