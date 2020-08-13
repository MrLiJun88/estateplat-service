package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdDy;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 过渡抵押服务
 */
public interface GdDyService {
    /**
     * @param  dydjzmh
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据dydjzmh获取过度抵押
    */
    GdDy getGdDyByDydjzmh(final String dydjzmh);

    /**
     * @param dyid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据dyid获取过度抵押
    */
    GdDy getGdDyByDyDyid(final String dyid);

}
