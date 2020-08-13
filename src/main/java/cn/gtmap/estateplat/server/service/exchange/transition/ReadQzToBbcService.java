package cn.gtmap.estateplat.server.service.exchange.transition;


import cn.gtmap.estateplat.model.exchange.transition.QzHead;

import java.util.List;

/**
 * Created by zdd on 2015/12/9.
 * 转换单个不动产业务对象服务
 */
public interface ReadQzToBbcService {

    /**
     * 根据过渡数据读取业务数据
     *
     * @param qzHead
     * @return 返回单一种类的不动产业务对象
     */
    <T> List<T> getBdcData(QzHead qzHead);

}
