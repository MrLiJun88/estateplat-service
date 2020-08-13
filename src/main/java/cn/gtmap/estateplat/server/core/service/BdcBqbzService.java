package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/1/18
 * @description 不动产补齐补正接口
 */
public interface BdcBqbzService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @param userid 用户ID
     * @return
     * @description 创建不动产补正材料通知书
     */
    void creatBdcBzcltzs(BdcXm bdcXm,String userid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm 不动产项目
     * @return
     * @description 删除不动产补正材料通知书
     */
    void deleteBdcBzcltzs(BdcXm bdcXm);

}
