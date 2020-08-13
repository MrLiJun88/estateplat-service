package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/2/17
 * @description 登记图形库接口
 */
public interface RegisterGraphicService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @return
     * @description 插入登记图形图数据
     */
    void insertDjtxkSj(BdcXm bdcXm);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @return
     * @description 删除登记图形图数据
     */
    void deleteDjtxkSj(BdcXm bdcXm);
}
