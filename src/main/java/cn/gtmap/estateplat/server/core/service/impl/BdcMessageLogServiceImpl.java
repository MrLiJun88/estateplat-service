package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcZdSqlx;
import cn.gtmap.estateplat.server.core.service.BdcMessageLogService;
import cn.gtmap.estateplat.server.model.BdcMessageLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/7/31
 * @description
 */
@Service
public class BdcMessageLogServiceImpl implements BdcMessageLogService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void saveOrUpdateBdcMessageLog(BdcMessageLog bdcMessageLog) {
        if(bdcMessageLog != null&&StringUtils.isNotBlank(bdcMessageLog.getId())) {
            entityMapper.saveOrUpdate(bdcMessageLog,bdcMessageLog.getId());
        }
    }

    @Override
    public BdcMessageLog getBdcMessageLogById(String id) {
        BdcMessageLog bdcMessageLog = null;
        if(StringUtils.isNotBlank(id)) {
            bdcMessageLog = entityMapper.selectByPrimaryKey(BdcMessageLog.class,id);
        }
        return bdcMessageLog;
    }

    @Override
    public List<BdcMessageLog> getBdcMessageLogList(String fszt) {
        List<BdcMessageLog> bdcMessageLogList = null;
        if(StringUtils.isNotBlank(fszt)) {
            Example example = new Example(BdcMessageLog.class);
            example.createCriteria().andEqualTo("fszt", fszt);
            bdcMessageLogList = entityMapper.selectByExample(BdcMessageLog.class, example);
        }
        return bdcMessageLogList;
    }
}
