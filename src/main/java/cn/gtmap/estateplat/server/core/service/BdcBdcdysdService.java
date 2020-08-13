package cn.gtmap.estateplat.server.core.service;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产单元锁定
 */
public interface BdcBdcdysdService {
    /**
     * 根据sdid更新锁定原因和锁定时间
     * @return
     */
    public void updateXzyyAndSdsj(String sdids,String xzyy);
}
