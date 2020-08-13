package cn.gtmap.estateplat.server.core.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @description 互联网+接口相关功能
 */

public interface GxYhService {
    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 验证互联网+接口调用是否成功
     */
    Map<String, Object> validateGxYhInterface(String proid,String userid);
}

