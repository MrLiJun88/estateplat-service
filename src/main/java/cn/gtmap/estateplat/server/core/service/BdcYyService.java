package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcYy;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记异议登记服务
 */
public interface BdcYyService {
    /**
     * 获取不动产异议登记信息
     *
     * @param map
     * @return
     */
    List<BdcYy> queryBdcYy(final Map map);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param bdcYy
     * @return
     * @description 保存不动产异议信息
     */
    void saveBdcYy(BdcYy bdcYy);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产异议信息页面
     */
    Model initBdcYyForPl(Model model, String qlid, BdcXm bdcXm);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 15:08
      * @description  根据proid查询bdcYy
      */
    BdcYy getBdcYyByProid(String proid);
}
