package cn.gtmap.estateplat.server.core.aop;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.config.ConfigRedundantFieldService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/8/6
 * @description 冗余字段服务
 */
public class RedundantField {
    @Autowired
    private ConfigRedundantFieldService redundantFieldService;
    @Autowired
    private BdcXmService bdcXmService;


    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 同步冗余字段
     */
    @Async
    public void synchronizationField(JoinPoint joinPoint) {
        if (checkFeasible(joinPoint)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(joinPoint.getArgs()[0].toString());
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                redundantFieldService.synchronizationField(bdcXm.getWiid());
            }
        }
    }


    private Boolean checkFeasible(JoinPoint joinPoint) {
        if (joinPoint != null && joinPoint.getArgs() != null && joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] instanceof String) {
            String proid = joinPoint.getArgs()[0].toString();
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && StringUtils.isNoneBlank(bdcXm.getProid(), bdcXm.getWiid())) {
                    return true;
                }
            }
        }
        return false;
    }
}
