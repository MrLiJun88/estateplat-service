package cn.gtmap.estateplat.server.core.service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public interface BdcJsxxService {

    /**
     * 获取所有数据
     * @param map
     * @return
     */
    List<Map> getBdcJsxxListByPage(Map map);
}
