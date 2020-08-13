package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcLq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.ui.Model;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记林权服务
 */
public interface BdcLqService {
    void saveBdcLqxx(BdcLq bdcLq);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产构（建）筑物所有权登记信息页面
     */
    Model initBdcLqForPl(Model model, String qlid, BdcXm bdcXm);
}
