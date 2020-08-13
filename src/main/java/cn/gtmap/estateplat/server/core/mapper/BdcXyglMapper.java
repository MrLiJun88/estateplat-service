package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXygl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 不动产信用管理
 *
 * @author <a href="mailto:chenjia@gtmap.cn">cj</a>
 * @version V1.0, 15-3-12
 */
@Repository
public interface BdcXyglMapper {
    List<BdcXygl> getBdcXyglByQlrzjh(final Map map);
}

