package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcDelZszh;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 删除的证书证号mapper
 * Created by lst on 2015/3/12.
 */
@Component
public interface BdcDelZszhMapper {

    /**
     * 获取删除证书证号
     *
     * @param map
     * @return
     */
    List<BdcDelZszh> getBdcDelZszhList(Map map);

    /**
     * 更新是否可用状态
     *
     * @param bdcDelZszh
     * @return
     */
    void updateIsUse(BdcDelZszh bdcDelZszh);

    /**
     * 获取被删的最小的产权证号
     *
     * @param
     * @return
     */
    BdcDelZszh getBdcDelZszhMinBdcqzh(HashMap map);
}
