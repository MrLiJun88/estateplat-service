package cn.gtmap.estateplat.server.core.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-12-2
 * Time: 下午4:44
 * @description 房产数据交换服务
 */
public interface FcExchangeService {
    /**
     * 查询过度项目是否纯在
     *
     * @param proid
     * @return
     */
    boolean gdXmIsPureIn(String proid);

    void insertGdXm(Map map);

    void insertGdSyq(Map map);

    void insertGdDy(Map map);

    void insertGdCf(Map map);

    void insertGdYg(Map map);

    void insertGdFw(Map map);

    void insertGdQlr(Map map);

    void insertGdYzh(Map map);

    HashMap getJtztByZsid(String zsid);

}
