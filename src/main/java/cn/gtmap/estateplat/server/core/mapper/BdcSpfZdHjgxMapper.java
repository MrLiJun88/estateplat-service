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
public interface BdcSpfZdHjgxMapper {
    /**
     *获取商品房核减信息
     * @param map
     * @return
     */
    List<BdcSpfZdHjgx> getBdcZdFwRelList(Map map);
}
