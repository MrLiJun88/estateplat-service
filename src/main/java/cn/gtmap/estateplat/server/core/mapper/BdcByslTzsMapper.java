package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBysltzs;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:wangchangzhou@gtmap.cn">wangchangzhou</a>
 * @version V1.0, 16-4-19
 * @description 不予受理通知书
 */
@Repository
public interface BdcByslTzsMapper {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取证最大流水号
     */
    Integer getMaxLsh(HashMap map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param   map
     * @return
     * @description 获取不动产不予受理通知书
     */
    List<BdcBysltzs> getBdcBysltzs(HashMap map);

}
