package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcJsydzjdsyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 15-10-27
 * Time: 上午10:04
 * @description 不动产登记建设用地使用权、宅基地使用权利服务
 */
public interface BdcJsydzjdsyqService {

    /**
     * 获取建设用地使用权、宅基地使用权登记信息
     *
     * @param map
     * @return
     */
    BdcJsydzjdsyq getBdcJsydzjdsyq(final Map map);

    BdcJsydzjdsyq getBdcJsydzjdsyq(String proid);

    /**
     * 获取建设用地使用权、宅基地使用权登记信息集合
     *
     * @param map
     * @return
     */
    List<BdcJsydzjdsyq> getBdcJsydzjdsyqList(final Map map);


    /**
     * 根据地籍号，获取建设用地
     *
     * @param djh 地籍号（19位宗地统一编码地籍号）
     * @return
     */
    List<BdcJsydzjdsyq> getJsyByDjh(final String djh);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param bdcJsydzjdsyq
     * @return
     * @description 保存建设用地使用权、宅基地使用权登记信息
     */
    void saveBdcJsydzjdsyq(BdcJsydzjdsyq bdcJsydzjdsyq);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产建设用地使用权、宅基地使用权信息页面
     */
    Model initBdcJsydzjdsyqForPl(Model model, String qlid, BdcXm bdcXm);
}
