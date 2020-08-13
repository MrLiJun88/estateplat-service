package cn.gtmap.estateplat.server.core.mapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
 * @version 1.0, 2018/4/8
 * @description
 */
public interface BdcPpdMapper {
    /**
     *
     * @author yanzhenkun
     * @description 获取匹配单信息
     * @param
     * @return List
     */
    public List<HashMap> getPpdxxMapByBdcdyh(HashMap map);

    /**
     *
     * @author yanzhenkun
     * @description 获取业务类型
     * @param
     * @return List
     */
    public List<HashMap> getYwlxMapByQlid(HashMap map);
}
