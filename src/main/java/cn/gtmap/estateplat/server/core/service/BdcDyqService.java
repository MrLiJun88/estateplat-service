package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcDyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.ui.Model;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 地役权服务
 */
public interface BdcDyqService {
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:保存地役权信息
    *@Date 8:48 2017/2/16
    */
    void saveBdcDyq(BdcDyq bdcDyq);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产建设用地使用权、宅基地使用权信息页面
     */
    Model initBdcDyqForPl(Model model, String qlid, BdcXm bdcXm);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 11:25
      * @description 根据proid查询地役权信息
      */
    BdcDyq queryBdcDyqByProid(String proid);
}
