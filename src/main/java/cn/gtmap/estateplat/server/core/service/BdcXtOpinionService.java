package cn.gtmap.estateplat.server.core.service;


import cn.gtmap.estateplat.model.server.core.BdcXtOpinion;

/**
 * .
 * 获取默认意见
 *
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jgz</a>
 */
public interface BdcXtOpinionService {

    /**
     * jaingganzhi 获取配置的默认意见
     *
     * @return
     */
    String getConfigOpinion(String proid,BdcXtOpinion bdcXtOpinion);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param mb
     * @param sql
     * @param proid
     * @return
     * @description 根据数据源替换模板内容
     */
    String replaceMb(String mb, String sql, String proid);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param wdid
     * @return
     * @description 获取默认配置默认意见
     */
    String getDefautConfigOpinion(String wdid,String activityName, String proid);
}
