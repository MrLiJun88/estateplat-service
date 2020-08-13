package cn.gtmap.estateplat.server.core.log;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.log.AuditLogService;
import cn.gtmap.estateplat.model.server.core.BdcXtLog;
import cn.gtmap.estateplat.utils.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/15
 * @description 不动产登记审计日志服务
 */
public class AuditLogServiceImpl implements AuditLogService {

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/3/15
     * @description mapper对象
     */
    @Autowired
    private EntityMapper entityMapper;

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 保存不动产审计日志
     * @param bdcXtLog 审计日志对象
     */
    @Override
    @Transactional
    public void saveAuditLog(BdcXtLog bdcXtLog) {
        if(StringUtils.isBlank(bdcXtLog.getLogid()))
            bdcXtLog.setLogid(UUID.hex32());
        entityMapper.saveOrUpdate(bdcXtLog,bdcXtLog.getLogid());
    }
}
