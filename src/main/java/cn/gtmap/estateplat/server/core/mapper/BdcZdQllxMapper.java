package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @description 权利类型字典项
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-23
 */
@Repository
public interface BdcZdQllxMapper {
    /**
     * zdd 根据代码查找权利类型字典表
     *
     * @param dm
     * @return
     */
    BdcZdQllx queryBdcZdQllxByDm(@Param("dm") String dm);

    /*
    * zwq 获取所有权利类型
    * */
    List<BdcZdQllx> queryBdcZdQllxByDm();

    /**
     * zdd 获取所有的权利类型字典表
     *
     * @return
     */
    List<BdcZdQllx> getAllBdcZdQllx();

    QllxVo queryQlxx(HashMap map);
}
