package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.model.server.core.DjsjFwLjz;
import cn.gtmap.estateplat.model.server.core.DjsjFwXmxx;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zx
 * Date: 15-4-21
 * Time: 下午3:35
 * @description 获取房屋相关权籍信息
 */
@Repository
public interface DjsjFwMapper {
    List<DjsjFwXmxx> getDjsjFwXmxx(Map map);

    List<DjsjFwLjz> getDjsjFwLjz(Map map);

    List<DjsjFwHs> getDjsjFwHs(Map map);

    List<Map> getDjsjFwHsByMap(Map map);

    List<DjsjFwHs> getDjsjFwYcHs(Map map);

    List<DjsjFwHs> getDjsjFwHsSumMj(Map map);

    /**
     * @param bdcdyh
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<String>
     * @description 根据不动产单元号获取房屋类型
     */
    List<String> getBdcfwlxByBdcdyh(String bdcdyh);

    /**
     * @param bdcdyh
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<String>
     * @description 根据不动产单元号获取房屋类型
     */
    List<String> getBdcfwlxById(String bdcdyh);
    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param bdcdyh
     *@return djid
     *@description 通过不动产单元号获取地籍id
     */
    String getDjidByBdcdyh(String bdcdyh);

    /**
     * @author bianwen
     * @param
     * @return
     * @description  根据不动产单元取预测户室（在建工程抵押）
     */
    List<Map> getDjsjYcFwHsForZjgcdy(HashMap paraMap);

    /**
     *
     * @return
     */
    List<Map> getDjsjHsBgJlb(Map param);

    /**
     * @author liujie
     * @param  map
     * @return
     * @description  根据不动产单元号获取户室变更前的不动产单元号
     */
    List<String> getYbdcdyhByBdcdyh(Map map);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过bdcdyh获取多幢房屋的fwhs信息
     */
    List<DjsjFwHs> getDzFwHs(Map queryMap);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过fwbm获取
     */
    List<DjsjFwHs> getDjsjFwHsByFwbm(String fwbm);

}
