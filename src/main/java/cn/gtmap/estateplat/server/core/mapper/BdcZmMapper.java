package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xhc on 2015/11/12.
 * @description 不动产登记证明Mapper
 */
public interface BdcZmMapper {
    /**
     * 获取证明类型字典数据
     *
     * @return
     */
    List<Map> getZmMc();

    /**
     * 根据证明id删除证明数据
     *
     * @param zmid
     */
    void delBdcZmByZmid(String zmid);

    /**
     * 生成证明
     *
     * @param map
     */
    void creatZm(Map map);
}
