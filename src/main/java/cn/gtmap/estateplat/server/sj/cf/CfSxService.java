package cn.gtmap.estateplat.server.sj.cf;

import cn.gtmap.estateplat.model.server.core.BdcCf;

import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-06
 * @description 查封失效服务
 */
public interface CfSxService {

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 查封失效
     */
    Map cfsx(String proid, String sxyy, String ip);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 保存日志
     */
    void saveLog(BdcCf bdcCf, String userid, String ip);
}
