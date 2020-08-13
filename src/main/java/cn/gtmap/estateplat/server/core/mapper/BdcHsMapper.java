package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcFwHs;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lst
 * @version V1.0, 15-3-18
 * @description 权籍数据中的户室信息
 */

public interface BdcHsMapper {
    /**
     * 查询hs视图 验证是否用展示楼盘表  不要删掉
     *
     * @param keyCode
     * @return
     */
    List<String> selectHsCount(@Param("keyCode") String keyCode);

    /**
     * 获取戶室信息
     *
     * @param map
     * @return
     */
    List<BdcFwHs> getBdcFwhsQlztList(Map map);

    /**
     * 获取过渡房屋信息状态
     *
     * @param map
     * @return
     */
    List<Map> getGdFwhsList(Map map);


    /**
     * @param map
     * @author bianwen
     * @rerutn
     * @description 获取访问预测戶室信息
     */
    public List<BdcFwHs> getBdcYcFwhsQlztList(HashMap map);

    /**
     * @param map
     * @author bianwen
     * @rerutn
     * @description 获取过渡预测房屋信息状态
     */
    public List<Map> getGdYcFwhsList(Map map);

    public String getBdcdyhByHsid(String hsid);
}
