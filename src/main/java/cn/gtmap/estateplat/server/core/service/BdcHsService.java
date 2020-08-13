package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcFwHs;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记户室
 * @author lst
 * @version V1.0, 15-4-17
 */
public interface BdcHsService {
    /**
     * 将xml数据转换成json数据
     *添加djbid是为了查询权利中的登记簿的本数
     * @param xmlDoc
     * @return
     */
    List<HashMap> xmlToJson(final String xmlDoc,final String djbid);

    /**
     * 获取访问戶室信息
     *
     * @param map
     * @return
     */
    List<BdcFwHs> getBdcFwhsQlztList(final Map map);

    /**
     * 获取楼盘地址，主要获取是展现楼盘表还是展现权利信息
     *
     * @param djbId
     * @return
     */
    String getLpbUrl(final String djbId);

    /**
     * 获取过渡房屋信息状态
     *
     * @param map
     * @return
     */
    List<Map> getGdFwhsList(final Map map);

    /**
     * 根据戶室id在过渡表查询权属状态
     *
     * @param gdFwList
     * @param hsId
     * @return
     */
    String getGdFwQszt(final List<Map> gdFwList, final String hsId);

    /**
     * 根据戶室id获得权属状态
     *
     * @param bdcFwHsList
     * @param gdFwhsList
     * @param hsId
     * @param djHsQlzt
     * @return
     */
    String getHsQsztByHsId(final List<BdcFwHs> bdcFwHsList, final List<Map> gdFwhsList, final String hsId, final String djHsQlzt);


    /**
     * 查询hs视图 验证是否用展示楼盘表  不要删掉
     *
     * @param keyCode
     * @return
     */
    List<String> selectHsCount(@Param("keyCode") final String keyCode);


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
    public List<Map> getGdYcFwhsList(HashMap map);


    /**
     * @param bdcdyh
     * @param qlzt
     * @author liujie
     * @rerutn
     * @description 获取户室发证类型状态
     */
    public String getFzlxQlzt(String bdcdyh,String qlzt);
}
