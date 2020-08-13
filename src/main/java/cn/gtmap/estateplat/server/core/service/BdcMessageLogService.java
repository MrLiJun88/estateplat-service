package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.server.model.BdcMessageLog;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/31
 * @description 不动产消息日志接口
 */
public interface BdcMessageLogService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcMessageLog
     * @return
     * @description 更新不动产消息日志信息
     */
    void saveOrUpdateBdcMessageLog(BdcMessageLog bdcMessageLog);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param id
     * @return BdcMessageLog
     * @description 根据id获取BdcMessageLog
     */
    BdcMessageLog getBdcMessageLogById(String id);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description
     */
    List<BdcMessageLog> getBdcMessageLogList(String fszt);
}
