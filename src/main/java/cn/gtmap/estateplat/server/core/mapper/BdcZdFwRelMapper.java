package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcSpfZdHjgx;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 *不动产商品房与宗地核减关系Mapper
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2016/9/13
 */
@Repository
public interface BdcZdFwRelMapper {
    /**
     *根据proid修改核减金额和土地面积
     */
    void updateJeAndMj(Map map);

    /**
     * 获取总的房屋核减金额
     * @param proid
     * @return
     */
    Map getSumFwhjje(String proid);

    /**
     * 获取总的项目核减金额
     * @param proid
     * @return
     */
    Map getSumXmhjje(String proid);

    /**
     * 获取总的核减土地面积
     * @param proid
     * @return
     */
    Map getSumhjjeTdmj(String proid);

    /**
     *
     * @param map
     * @return
     */
    List<BdcSpfZdHjgx> getBdcZdFwRelList(Map map);
}
