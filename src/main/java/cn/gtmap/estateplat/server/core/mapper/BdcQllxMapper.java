package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.GdYg;
import cn.gtmap.estateplat.model.server.core.QllxParent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-21
 * @description 权利类型信息
 */
@Repository
public interface BdcQllxMapper {

    List<QllxParent> queryQllxByProid(String proid);

    /**
     * lst 根据权利类型获取登记事由
     *
     * @param qllx
     * @return
     */
    List<String> queryDjsyByQllx(@Param("qllx") String qllx);

    /**
     * sc根据proid获取qilid
     *
     * @param proid
     * @return
     */

    List<String> getQllxIdByproid(@Param("proid") String proid);

    /**
     * 获取权利信息列表
     *
     * @param map
     * @return
     */
    List<Map> getQllxListByPage(Map map);

    /**
     * 获取权利实体
     *
     * @param map
     * @return
     */
    List<QllxParent> queryQllx(Map map);

    /**
     * 获取权利信息列表
     *
     * @param map
     * @return
     */
    List<Map> getBdcdyQlxxList(Map map);

    /**
     * 通过不动产单元号获取匹配的过度预告信息
     * @param bdcdy
     * @return
     */
    List<Map>  getGdYgByBdcdyh(String bdcdy);
    /**
     * 通过不动产单元号获取匹配的过度预告信息
     * @param bdcdy
     * @return
     */
    List<GdYg> getGdYgXxByBdcdyh(String bdcdy);
}
