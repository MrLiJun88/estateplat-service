package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.QllxParent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-1
 * @description 权利父类型
 */
@Repository
public interface QllxParentMapper {
    /**
     * zdd 查询不动产权利的基本共有信息
     *
     * @param map
     * @return
     */
    List<QllxParent> queryQllxVo(Map map);

    List<Map> queryQllxMap(Map map);
}
