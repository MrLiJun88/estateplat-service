package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-23
 */
@Service
public interface BdcZdQllxService {
    /**
     * zdd 根据代码查找权利类型字典表
     *
     * @param dm
     * @return
     */
    BdcZdQllx queryBdcZdQllxByDm(final String dm);

    /**
     * zdd 获取所有的权利类型字典表
     *
     * @return
     */
    List<BdcZdQllx> getAllBdcZdQllx();

}
