package cn.gtmap.estateplat.server.core.service;

import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/3/23
 * @description 不动产字段对比
 */
public interface BdcZddbService {

    /**
     * 获取dzwyt字段对比结果
     *
     * @param proid
     */
    String getBdcZddbJgByDzwyt(String proid);

    /**
     * 获取djzwmj字段对比结果
     *
     * @param proid
     */
    String getBdcZddbJgByDjzwmj(String proid);

    /**
     * 获取zdzhyt字段对比结果
     *
     * @param proid
     */
    String getBdcZddbJgByZdzhyt(String proid);

    /**
     * 获取zdzhmj字段对比结果
     *
     * @param proid
     */
    String getBdcZddbJgByZdzhmj(String proid);

    /**
     *
     * @param proid
     * @return
     */
    Map<String,Object> getZdxx(String proid,boolean withZd);

    /**
     *
     * @param proid
     * @return
     */
    Map<String,Object> getFwxx(String proid);


}
