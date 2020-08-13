package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcBysltzs;
import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/11/11
 * @description  不动产不予受理接口
 */
public interface BdcByslService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @param userid 用户ID
     * @return
     * @description 创建不动产不予受理通知书
     */
    void creatBdcBysltzs(BdcXm bdcXm,String userid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @return
     * @description 删除不动产不予受理通知书
     */
    void deleteBdcBysltzs(BdcXm bdcXm);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param map
     * @return
     * @description 获取不动产不予受理通知书最大流水号
     */
    Integer getMaxLsh(HashMap map);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param  map
     * @return
     * @description 获取不动产不予受理通知书
     */
    List<BdcBysltzs> getBdcBysltzs(HashMap map);

}

