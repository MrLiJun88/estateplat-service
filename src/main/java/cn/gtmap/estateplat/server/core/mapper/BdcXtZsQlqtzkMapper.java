package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXtZsQlqtzk;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 证书权利其他状况配置
 * Date: 15-4-15
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
public interface BdcXtZsQlqtzkMapper {
    /**
     * sc: 获取ZS 权利其他状况模板
     *
     * @param map
     * @return
     */
    List<BdcXtZsQlqtzk> getBdcXtZsQlqtzk(Map map);
}
