package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 审批信息
 */
@Repository
public interface BdcSpxxMapper {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量删除审批信息
     */
    void batchDelBdcSpxx(List<BdcXm> bdcXmList);
}
