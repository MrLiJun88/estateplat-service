package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdDyhRel;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 单元号关系服务
 */
public interface GdDyhRelService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcid
     * @rerutn List<GdDyhRel>
     * @description
     */
    List<GdDyhRel> queryGdDyhRelListByBdcid(final String bdcid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcdyh
     * @rerutn List<GdDyhRel>
     * @description
     */
    List<GdDyhRel> queryGdDyhRelListByBdcdyh(final String bdcdyh);

}
