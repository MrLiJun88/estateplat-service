package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.log.AuditLog;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.GdSaveLogSecvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2016/5/05
 * @description 过度日志
 */
@Service
public class GdSaveLogSecviceImpl implements GdSaveLogSecvice {
    private static Logger logger = LoggerFactory.getLogger(GdSaveLogSecviceImpl.class);
    @Autowired
    EntityMapper entityMapper;


    @Override
    @AuditLog(name = "过度表保存日志", description = "过度项目")
    public void gdXmLog(GdXm gdXm) {
        logger.debug("过度项目保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度房屋所有权")
    public void gdFwsyqLog(GdFwsyq gdFwsyq) {
        logger.debug("过度房屋所有权保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度土地所有权")
    public void gdTdsyqLog(GdTdsyq gdTdsyq) {
        logger.debug("过度土地所有权保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度抵押")
    public void gdDyLog(GdDy gdDy) {
        logger.debug("过度抵押保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度查封")
    public void gdCfLog(GdCf gdCf) {
        logger.debug("过度查封保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度预告")
    public void gdYgLog(GdYg gdYg) {
        logger.debug("过度预告保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度异议")
    public void gdYyLog(GdYy gdYy) {
        logger.debug("过度异议保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度房屋")
    public void gdFwLog(GdFw gdFw) {
        logger.debug("过度房屋保存日志");
    }

    @Override
    @AuditLog(name = "过度表保存日志", description = "过度土地")
    public void gdTdLog(GdTd gdTd) {
        logger.debug("过度土地保存日志");
    }

    @Override
    @AuditLog(name = "过度单元号匹配关系表保存日志", description = "过度匹配关系")
    public void gdDyhRelLog(BdcGdDyhRel bdcGdDyhRel) {
        logger.debug("过度单元号匹配关系表保存日志");
    }

    @Override
    @AuditLog(name = "过度权利单元号匹配关系表保存日志", description = "过度匹配关系")
    public void gdQlDyhRelLog(GdQlDyhRel gdQlDyhRel) {
        logger.debug("过度权利单元号匹配关系表保存日志");
    }

    @Override
    @AuditLog(name = "过度单元号取消匹配关系表保存日志", description = "过度取消匹配关系")
    public void gdDyhRelQxppLog(BdcGdDyhRel bdcGdDyhRel) {
        logger.debug("过度单元号取消匹配关系表保存日志");
    }

    @Override
    @AuditLog(name = "过度权利单元号取消匹配关系表保存日志", description = "过度取消匹配关系")
    public void gdQlDyhRelQxppLog(GdQlDyhRel gdQlDyhRel) {
        logger.debug("过度权利单元号取消匹配关系表保存日志");
    }
}
