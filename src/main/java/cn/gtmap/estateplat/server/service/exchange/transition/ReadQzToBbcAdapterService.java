package cn.gtmap.estateplat.server.service.exchange.transition;


import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.server.core.InsertVo;

import java.util.List;

/**
 * Created by zdd on 2015/12/9.
 * 读取过渡数据到不动产业务对象  由多个实现类实现不同的不动产对象数据的获取
 */
public interface ReadQzToBbcAdapterService {

    /**
     * zdd 根据读取的过渡数据集（QzHead），取初始化不动产产业务数据对象
     *
     * @param qzHead
     * @return 返回单一类的不动产业务对象
     */
    <T> List<T> getBdcDataSet(QzHead qzHead);

    /**
     * 相同javabean转换
     *
     * @param fromList 来源集合
     * @param tolist   转换集合
     * @return
     */
    List<InsertVo> getBdInsertList(List fromList, List tolist);

}
