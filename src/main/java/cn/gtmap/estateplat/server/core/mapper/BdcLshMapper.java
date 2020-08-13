package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcLsh;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/1/17
 * @description 不动产流水号
 */
public interface BdcLshMapper {

    /**
     * 获取最大流水号
     *
     * @param map
     * @return
     */
    Integer getMaxLsh(Map map);

    /**
     * 获取流水号列表
     *
     * @param map
     * @return
     */
    List<BdcLsh> getBdcLshList(Map map);

}
