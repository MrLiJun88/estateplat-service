package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcJjdXx;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 15-12-10
 * Time: 下午8:02
 * @description 不动产交接单信息
 */
public interface BdcJjdMapper {
    List<BdcJjdXx> getJjdxxJsonByPage(Map map);

    List<BdcJjdXx> getJjdJson(Map map);

    String getJjdXh(String nf);

    List<String> getBeforeSlh(String bh);

    List<String> getAfterSlh(String bh);

}
