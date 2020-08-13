package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.service.SyncRyzdService;
import cn.gtmap.estateplat.server.service.config.ConfigRedundantFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-06
 * @description
 */
@Service
public class SyncRyzdServiceImpl implements SyncRyzdService {
    @Autowired
    private ConfigRedundantFieldService redundantFieldService;


    @Async
    @Override
    public void synchronizationField(String wiid) {
        redundantFieldService.synchronizationField(wiid);
    }
}
