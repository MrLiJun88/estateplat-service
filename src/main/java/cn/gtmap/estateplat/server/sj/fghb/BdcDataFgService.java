package cn.gtmap.estateplat.server.sj.fghb;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcFwfzxx;
import cn.gtmap.estateplat.server.sj.InterfaceCode;

import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/16
 * @description 不动产数据分割服务
 */
public interface BdcDataFgService  extends InterfaceCode {

    /**
     * @param dataMap 拆分参数
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 拆分数据
     */
    void splitData(Map dataMap);

    /**
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 拆分数据
     */
    void splitDataFromOldData(String splitCqProid, BdcFwfzxx bdcFwfzxx, BdcBdcdy bdcBdcdy,BdcBdcdy dzBdcBdcdy,BdcFdcqDz bdcFdcqDz);

    /**
     * @param dataMap 拆分参数
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 删除拆分原数据
     */
    void deleteSplitOldData(Map dataMap);
}
