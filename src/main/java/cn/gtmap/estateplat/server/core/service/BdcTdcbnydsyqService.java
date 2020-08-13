package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.ui.Model;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记土地承包经营权、农用地使用权登记
 */
public interface BdcTdcbnydsyqService {

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存土地承包经营权、农用地的其他使用权登记信息（非林地）
    *@Date 14:50 2017/2/16
    */
    void saveBdcTdcbnydsyq(BdcTdcbnydsyq bdcTdcbnydsyq);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产土地承包经营权、农用地的其他使用权登记信息（非林地）
     */
    Model initBdcTdcbnydsyqForPl(Model model, String qlid, BdcXm bdcXm);
}
