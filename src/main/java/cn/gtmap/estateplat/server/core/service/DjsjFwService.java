package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lst on 2015/3/24
 * @description 房屋的权籍数据服务
 */
public interface DjsjFwService {

    /**
     * 获取房屋类型
     *
     * @param djid
     * @return
     */
    DjsjFwxx getDjsjFwxx(final String djid);

    /**
     * 获取不动产单元
     */
    DjsjBdcdy getDjsjBdcdyByDjid(final String djid);

    List<DjsjFwXmxx> getDjsjFwXmxx(Map map);

    List<DjsjFwLjz> getDjsjFwLjz(Map map);

    List<DjsjFwHs> getDjsjFwHs(Map map);

    List<Map> getDjsjFwHsByMap(Map map);

    List<DjsjFwHs> getDjsjFwYcHs(Map map);

    String getFwlxByProid(final String proid);

    /**
     * 获取房屋类型根据地籍id
     *
     * @param djid
     * @return
     */
    String getFwlxByDjid(final String djid);

    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param bdcdyh
     *@return djid
     *@description 通过bdcdyh获取djid
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
     * @author bianwen
     * @param djid
     * @rerutn
     * @description 针对预告选择预测数据，根据房屋预测户室获取房屋类型
     */
    DjsjFwxx getDjsjFwxxByFwychs(String djid);

    /**
     * @author jiangganzhi
     * @param
     * @return
     * @description  获取逻辑幢信息
     */
    List<Map> getLpbList(Map map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcdyh
     * @return
     * @description 根据不动产单元号获取List<DjsjFwHs>
     */
    List<DjsjFwHs> getDjsjFwHsListByBdcdyh(String bdcdyh);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过bdcdyh获取多幢房屋的fwhs信息
     */
    List<DjsjFwHs> getDzFwHsListByBdcdyh(String bdcdyh);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过fwbm获取
     */
    List<DjsjFwHs> getDjsjFwHsByFwbm(String fwbm);

    /**
     *@auther <a href="mailto:chenjia@gtmap.cn">chenjia</a>
     *@description 获取房屋备案状态
     */
    Map getFwBazt(List<DjsjFwHs> djsjFwHsList);
}
