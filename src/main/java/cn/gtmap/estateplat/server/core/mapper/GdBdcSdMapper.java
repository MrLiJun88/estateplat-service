package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.GdBdcSd;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">songhaowen</a>
 * @version 1.0, 2016/5/16
 * @description 过度不动产单元锁定
 */
@Repository
public interface GdBdcSdMapper {

    /**
     * 添加
     *
     * @param sd
     */
    void insertGdBdcSd(GdBdcSd sd);

    /**
     * @param
     * @return
     */
    int findGdBdcSdExsit(String qlid);

    /**
     * 更新
     */

    void updateGdBdcSd(GdBdcSd sd);

    /**
     *获取所有过度不动产锁定
     * @param sd
     * @return
     */
    List<GdBdcSd> getGdSdByPage(Map map);

    /**
     * 根据sdid查询
     * @param sdid
     * @return
     */
    GdBdcSd findGdBdcSd(String qlid);

    /**
     * 获取过度土地
     * @param map
     * @return
     */
    List<Map> getGdTdByPage(Map map);

    /**
     * 获取房屋状态
     * @param qlid
     * @return
     */
    List<String> getFwIdByQlid(String qlid);

    /**
     * 获取状态
     * @param qlid
     * @return
     */
    GdBdcSd getXzztBySdid(String qlid);

    /**
     * 高级查询获取过渡土地和过渡锁定
     * @param map
     * @return
     */
    List<Map> getGdTdHighLevelSearchByPage(Map map);

    /**
     * 高级查询获取过渡房屋和过渡锁定
     * @param map
     * @return
     */
    List<Map> getGdFwHighLevelSearchByPage(Map map);

}
