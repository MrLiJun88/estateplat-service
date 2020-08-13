package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.service.BdcSlbhBhService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/4/12
 * @description  不动产受理编号服务Context
 */
public class BdcSlbhCustomServiceContext {
    private final static String DEFAULT_TYPE="default";

    private Map<String, BdcSlbhBhService> bdcSlbhServiceMap;

    public void setBdcSlbhServiceMap(Map<String, BdcSlbhBhService> bdcSlbhServiceMap) {
        this.bdcSlbhServiceMap = bdcSlbhServiceMap;
    }


    /**
     *
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取受理编号服务
     */
    public BdcSlbhBhService getSlbhService() {
        String bdcSlbhGz = AppConfig.getProperty("xmbh.bhgz");
        if(StringUtils.isBlank(bdcSlbhGz)){
            return bdcSlbhServiceMap.get(DEFAULT_TYPE);
        }
        return bdcSlbhServiceMap.containsKey(bdcSlbhGz)?bdcSlbhServiceMap.get(bdcSlbhGz):bdcSlbhServiceMap.get(DEFAULT_TYPE);
    }
}
