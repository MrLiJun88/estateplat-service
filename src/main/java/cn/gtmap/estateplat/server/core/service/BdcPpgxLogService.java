package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcPpgxLog;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/26
 * @description 不动产匹配关系日志表 服务
 */
public interface BdcPpgxLogService {

    /**
     * @param map 关系日志表参数map
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据传入参数Map插入关系日志表
     */
    void insertBdcPpgxLogByMap(Map map);

    /**
     * @param bdcPpgxLog
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 保存bdcPpgxLog
     */
    void saveBdcPpgxLog(BdcPpgxLog bdcPpgxLog);

    /**
     * @param map
     * @return
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据传入Map参数查询关系日志表
     */
    List<BdcPpgxLog> getBdcPpgxLogByMap(Map map);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 16:49
      * @description 根据匹配时间排序 查询匹配日志表
      */
    List<BdcPpgxLog> getBdcPpgxLogOrderByPpsj(BdcPpgxLog bdcPpgxLog);
}
