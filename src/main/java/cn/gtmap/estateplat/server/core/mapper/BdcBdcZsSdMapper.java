package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2016/9/19
 * @description 不动产证书查询接口
 */
@Repository
public interface BdcBdcZsSdMapper {
    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 获取证书锁定数据
     */
    public List<BdcBdcZsSd> getBdcZsSdList(HashMap map);
}
