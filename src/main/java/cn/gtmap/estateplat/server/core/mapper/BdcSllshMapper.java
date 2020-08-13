package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBh;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcSllsh;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 不动产受理流水号
 * Created by lst on 2016/12/7
 */

public interface BdcSllshMapper {


    /**
     * 获取不动产编号
     *
     * @param map
     * @return
     */
    List<BdcSllsh> getBdcSllshList(Map map);
}
