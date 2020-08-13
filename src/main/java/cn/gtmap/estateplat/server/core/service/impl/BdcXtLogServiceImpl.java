package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXtLog;
import cn.gtmap.estateplat.server.core.service.BdcXtLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * @author lst
 * @version 15-4-15
 */
@Repository
public class BdcXtLogServiceImpl implements BdcXtLogService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void addLog(final BdcXtLog bdcXtLog) {
        entityMapper.insertSelective(bdcXtLog);
    }

    @Override
    public void delLog(final String logid) {
        entityMapper.deleteByPrimaryKey(BdcXtLog.class, logid);
    }
}
