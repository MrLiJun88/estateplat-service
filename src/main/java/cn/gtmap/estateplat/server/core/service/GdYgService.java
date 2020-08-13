package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdYg;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 过渡预告
 */
public interface GdYgService {
    List<GdYg> queryGdYgByBdcId(final String bdcId);

    List<GdYg> getGdygListByBdcdyh(String bdcdyh);

    /**
     * @param ygid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据ygid获取Gdyg
    */
    GdYg queryGdYgByYgid(final String ygid);
}
