package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXtLog;

/**
 * 不动产登记系统日志
 * @author lst
 * @version 15-4-15
 */
public interface BdcXtLogService {

    /**
     * 新增系统日志
     *
     * @param bdcXtLog
     */
    void addLog(final BdcXtLog bdcXtLog);

    /**
     * 删除系统日志
     *
     * @param logid
     */
    void delLog(final String logid);

}
