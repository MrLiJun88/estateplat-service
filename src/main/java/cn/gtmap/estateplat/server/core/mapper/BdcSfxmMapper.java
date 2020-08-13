package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcSfxm;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 收费信息
 */
@Repository
public interface BdcSfxmMapper {
    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@Description:
     *@Date 15:51 2017/4/6
     */
    List<BdcSfxm>  getSfXm(HashMap map);
}
