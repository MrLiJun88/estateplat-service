package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBdcdjb;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-18
 * @description 不动产登记簿
 */
@Repository
public interface BdcdjbMapper {

    /**
     * zdd 根据宗地宗海号查找bdcdjb
     *
     * @param zdzhh 宗地宗海号
     * @return
     */
    List<BdcBdcdjb> selectBdcdjb(final String zdzhh);


    /**
     * sc 多条件查询bdcdjb
     *
     * @param map
     * @return
     */
    List<BdcBdcdjb> getBdcdjbByPage(final Map map);

    /**
     * zx 根据登记薄获取不动产单元目录
     *
     * @param map
     * @return
     */
    List<Map> getQldjByPage(final Map map);

}
