package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdYy;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 过渡异议
 */
public interface GdYyService {

    /**
     * @param yyid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据yyid查询gdyy
    */
    GdYy queryGdYyByYyid(final String yyid);
}
