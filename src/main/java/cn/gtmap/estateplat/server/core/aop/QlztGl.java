package cn.gtmap.estateplat.server.core.aop;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2017/12/25
 * @description 权利状态管理
 */

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.SyncQlztService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public class QlztGl {
    @Autowired
    private  SyncQlztService syncQlztService;
    @Autowired
    private BdcXmService bdcXmService;

    /**
     * @return 返回信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 更新证书证明查询表权利状态根据proid
     */
    @Async
    public void updateQlztByProid(JoinPoint joinPoint) {
        if (checkFeasible(joinPoint)) {
            syncQlztService.updateQlztByProid(joinPoint.getArgs()[0].toString());
        }
    }

    /**
     * @return 返回信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 更新证书证明查询表权利状态根据proid
     */
    @Async
    public void updateQlztByProidForBack(JoinPoint joinPoint) {
        if (checkFeasible(joinPoint)) {
            syncQlztService.updateQlztByProidForBack(joinPoint.getArgs()[0].toString());
        }
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @param:joinPoint
     * @Description:aop删除事件不动产证书证明管理
     * @Date 9:24 2018/1/2
     */
    @Async
    public void updateQlztByDeleteEnvent(JoinPoint joinPoint) {
        if (checkFeasible(joinPoint)) {
            syncQlztService.updateQlztForDelete(joinPoint.getArgs()[0].toString());
        }
    }

    private Boolean checkFeasible(JoinPoint joinPoint) {
        if (joinPoint != null && joinPoint.getArgs() != null && joinPoint.getArgs().length > 0
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
