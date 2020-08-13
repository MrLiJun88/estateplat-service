package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcPpgxXm;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BdcPpgxXmMapper {
    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/14 17:39
      * @description 根据bdcdyh查询BdcPpgxXm
      */
    List<BdcPpgxXm> getBdcPpgxXmByBdcdyh(String bdcdyh);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 10:37
      * @description 根据Map查询bdcPpgxXm
      */
    List<BdcPpgxXm> getBdcPpgxXmByMap(Map<String,Object> paramMap);
}
