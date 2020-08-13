package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcTd;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 宗地信息
 * @author zzhw
 * @version V1.0, 15-3-18
 */
@Repository
public interface BdcTdMapper {

    /**
     * zzhw 根据宗地宗海号查找BdcTd
     *
     * @param zdzhh
     * @return
     */
    BdcTd selectBdcTd(String zdzhh);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据宗地特征码获取判定方式
     */
    HashMap<String,String> getFsAndDefaultZdLbByZdTzm(@Param(value = "zdtzm")String zdtzm);
    /**
     * 
     * @author wangtao
     * @description
     * @param 
     * @return List<Map>
     */
    List<Map> getZdyt();
}
