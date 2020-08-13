package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcLq;
import cn.gtmap.estateplat.model.server.core.GdLq;

/**
 * .
 * <p/>
 * 过渡林权信息处理服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-10
 */
public interface GdLqService {

    /**
     * zdd 根据林权主键查找林权信息
     *
     * @param lqid
     * @return
     */
    GdLq queryGdLqById(final String lqid);

    /**
     * zdd 从过渡林权信息中读取数据
     *
     * @param gdLq
     * @param bdcLq
     * @return
     */
    BdcLq getBdcLqFromGdLq(final GdLq gdLq, BdcLq bdcLq);
}
