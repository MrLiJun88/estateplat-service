package cn.gtmap.estateplat.server.core.service;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
 * @version 1.0, 2018/4/8
 * @description 匹配清单信息服务
 */
public interface BdcPpdService {
    /**
     * @author yanzhenkun
     * @description  获取匹配单信息
     */
    public List<HashMap> getPpdxxMapByBdcdyh(HashMap map);
    /**
     * @author yanzhenkun
     * @description  获取业务类型
     */
    public List<HashMap> getYwlxMapByQlid(HashMap map);
}
