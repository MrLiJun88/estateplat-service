package cn.gtmap.estateplat.server.sj.zs;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;

import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description 证书通用方法
 */
public interface BdcZsTyService {
    /**
     * @param wiid 工作流实例ID
     * @param lclx 流程类型
     * @return 证书类型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取证书类型
     */
    Map getZsFont(String wiid, String lclx);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化纯房证书
     */
    BdcZs initCfzs(BdcZs bdcZs, BdcXm bdcXm);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化纯房证书
     */
    String getZsqllx(String qllx, BdcXm bdcXm);
}
