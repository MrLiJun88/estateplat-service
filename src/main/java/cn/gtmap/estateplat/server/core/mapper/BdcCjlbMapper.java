package cn.gtmap.estateplat.server.core.mapper;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;

/**
 * @description 持件列表信息
 * Date: 15-9-18
 * Time: 下午1:50
 */
@Repository
public interface BdcCjlbMapper {

    List<HashMap> getBdcCjlbByBh(String bh);
}
