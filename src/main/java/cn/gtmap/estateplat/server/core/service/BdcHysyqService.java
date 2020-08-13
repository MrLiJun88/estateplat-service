package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcHysyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 海域使用权
 */
public interface BdcHysyqService {

    /**
     * 获取海域（含无居民海岛） 使用权登记信息
     *
     * @param map
     * @return
     */
    BdcHysyq getBdcHysyq(final Map map);

    /**
     * 保存海域（含无居民海岛） 使用权登记信息
     *
     * @param bdcHysyq
     * @return
     */
    void saveBdcHysyq(BdcHysyq bdcHysyq);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产海域（含无居民海岛） 使用权登记信息页面
     */
    Model initBdcHysyqForPl(Model model, String qlid, BdcXm bdcXm);
}
