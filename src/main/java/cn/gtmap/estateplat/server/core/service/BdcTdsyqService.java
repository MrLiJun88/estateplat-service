package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcTdsyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.GdTdsyq;
import org.springframework.ui.Model;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记土地所有权服务
 */
public interface BdcTdsyqService {
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存土地所有权登记信息
    *@Date 15:30 2017/2/16
    */
    void  saveBdcTdsyq(BdcTdsyq bdcTdsyq);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产土地所有权登记信息页面
     */
    Model initBdcTdsyqForPl(Model model, String qlid, BdcXm bdcXm);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 根据qlid更新gd_fwsyq的cqzhjc字段
     */
    void updateCqzhjcByQlid(final String qlid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 根据qlid查找gd_tdsyq
     */
    GdTdsyq queryGdtdsyqByQlid(String qlid);
}
