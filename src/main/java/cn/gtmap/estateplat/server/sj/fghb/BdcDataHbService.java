package cn.gtmap.estateplat.server.sj.fghb;

import cn.gtmap.estateplat.server.sj.InterfaceCode;

import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/16
 * @description 不动产数据合并服务
 */
public interface BdcDataHbService  extends InterfaceCode {
    /**
     * @param dataMap 更新字段值参数
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 合并数据
     */
    void combineData(Map dataMap);

    /**
     * @param delProid 需要删除的proid
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 删除多余数据
     */
    void deleteXmInfo(String delProid);

    /**
     * @param combineProid 需要合并的proid
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 处理合并数据
     */
    void combineXmInfo(String combineProid,Map dataMap);
}
