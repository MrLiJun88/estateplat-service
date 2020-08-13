package cn.gtmap.estateplat.server.service.config.impl;

import cn.gtmap.estateplat.server.core.service.SyncQlztService;
import cn.gtmap.estateplat.server.service.config.ConfigSyncQlztService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/9/19
 * @description
 */
@Service
public class ConfigSyncQlztServiceImpl implements ConfigSyncQlztService {
    @Autowired
    private SyncQlztService syncQlztService;

    @Override
    public void syncBdcRelateQlzt(String proid) {
        if(StringUtils.isNotBlank(proid)) {
            //hzj 根据proid获取其他关联证书证明号
            List<Map<String, Object>> mapList = syncQlztService.queryQlztBeforDelet(proid);
            //hzj 删除之前获取不动产单元状态管理需要的map
            Map<String, Object> bdcdyztMap = syncQlztService.queryBdcdyZtMapBeforDelet(proid);
            //hzj  共享删除之前获取参数
            Map gxMap = syncQlztService.queryGxztBeforDelet(proid);
            //hzj 更新相关证书证明号、不动产单元、共享库等状态
            if (CollectionUtils.isNotEmpty(mapList)) {
                syncQlztService.updateQlztAfterDelet(mapList);
            }
            if (null != bdcdyztMap) {
                syncQlztService.updateBdcdyZtMapAfterDelet(bdcdyztMap);
            }
            if (null != gxMap) {
                syncQlztService.updateGxztAfterDelet(gxMap);
            }
        }
    }
}
