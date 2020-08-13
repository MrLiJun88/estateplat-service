package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcYg;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 不动产预告登记信息
 * Created by lst on 2015/3/17
 */
public interface BdcYgMapper {
    /**
     * 获取不动产预告登记信息
     *
     * @param proid
     * @return
     */
    String getBdcYgByProid(String proid);

    /**
     * 保存不动产预告登记信息
     *
     * @param bdcYg
     * @return
     */
    void saveBdcYg(BdcYg bdcYg);

    /**
     * 获取与预告信息
     *
     * @param map
     * @return
     */
    List<BdcYg> getBdcYgList(Map map);


}
