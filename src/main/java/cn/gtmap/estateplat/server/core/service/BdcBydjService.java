package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcBydjdjd;
import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/11/12
 * @description 不动产不予登记接口
 */
public interface BdcBydjService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @param userid 用户ID
     * @return
     * @description 创建不动产不予登记登记单
     */
    void creatBdcBydjdjd(BdcXm bdcXm,String userid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @return
     * @description 删除不动产不予登记登记单
     */
    void deleteBdcBydjdjd(BdcXm bdcXm);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param map
     * @return
     * @description 获取不动产不予登记登记单最大流水号
     */
    Integer getMaxLsh(HashMap map);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param  map
     * @return
     * @description 获取不动产不予登记登记单
     */
    List<BdcBydjdjd> getBdcBydjdjd(HashMap map);
}
