package cn.gtmap.estateplat.server.core.aop;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.service.history.HistoryService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * @author <a href="mailto:Will@gtmap.cn">Will</a>
 * @version 1.0, 2017-06-16
 * @description
 */
public class History {

    private BdcXmService bdcXmService;

    private HistoryService historyService;

    private Boolean enableHistory = AppConfig.getBooleanProperty("enableHistory", false);

    public void setBdcXmService(BdcXmService bdcXmService) {
        this.bdcXmService = bdcXmService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Async
    public void createHistoryRel(JoinPoint joinPoint) {
        if (checkFeasible(joinPoint)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(joinPoint.getArgs()[0].toString());
            historyService.createHistoryRel(bdcXm.getWiid());
        }
    }

    @Async
    public void deleteHistoryRel(JoinPoint joinPoint) {
        if (checkFeasible(joinPoint)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(joinPoint.getArgs()[0].toString());
            historyService.deleteHistoryRel(bdcXm.getWiid());
        }
    }

    private Boolean checkFeasible(JoinPoint joinPoint) {
        if (enableHistory && joinPoint != null && joinPoint.getArgs() != null && joinPoint.getArgs().length > 0
                &&joinPoint.getArgs()[0] instanceof String) {
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
