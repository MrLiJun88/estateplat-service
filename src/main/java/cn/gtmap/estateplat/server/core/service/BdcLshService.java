package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcLsh;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/1/17
 * @description 不动产流水号
 */
public interface BdcLshService {

    /**
     * 获取最大流水号
     *
     * @param map
     * @return
     */
    Integer getMaxLsh(Map map);

    /**
     * 获取流水号
     *
     * @param bhlxmc
     * @param nf
     * @param qh
     * @return
     */
    String getLsh(String bhlxmc,String nf,String qh);



    /**
     * 获取区号
     *
     * @param userid
     * @return
     */
    String getQh(String userid);

    /**
     * 获取区号
     *
     * @param dwdm
     * @return
     */
    String getQhByDwdm(String dwdm);

    /**
     * 获取编号
     *
     * @param bhlxmc
     * @param nf
     * @param qh
     * @param lsh
     * @return
     */
    String getBh(String bhlxmc,String nf,String qh,String lsh);

    /**
     * 获取流水号列表
     *
     * @param map
     * @return
     */
    List<BdcLsh> getBdcLshList(Map map);

    /**
     * 根据编号类型名称获取编号类型代码
     *
     * @param bhlxMc
     * @return
     */
    String getBhlxDmBybhlxMc(String bhlxMc);

    /**
     * 根据编号类型名称获取编号类型代码
     *
     * @param bhlxdm
     * @return
     */
    Integer getLshwsByBhlxdm(String bhlxdm);

    /**
     * 根据编号类型名称获取编号模板
     *
     * @param bhlxdm
     * @return
     */
    String getBhMbByBhlxdm(String bhlxdm);

    /**
     * 根据用户id获取编号
     *
     * @param userid
     * @return
     */
    String getBh(String userid,String bhlxmc);
}
