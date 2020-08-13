package cn.gtmap.estateplat.server.core.mapper;


import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-14
 * @description 不动产登记权籍数据Mapper
 */
@Repository
public interface DjxxMapper {


    List<Map> getDjFwDyh(Map map);

    List<Map> getDjFwWlcs(Map map);

    Integer getDjFwMaxSxh(Map map);

    /**
     * @author liujie
     * @description
     */
    Integer getDjFwMaxSxhByDyhIsNull(Map map);

    Integer getDjFwHsCount(Map map);

    List<Map> getFwmcListByDcbId(String dcbId);

    /**
     * zdd 根据不动产单元号  获取自然幢号   用于定位
     *
     * @param bdcdyh
     * @return
     */
    List<String> getZrzhByBdcdyh(String bdcdyh);

    List<Map> getFWDcbList(Map map);

    /**
     * 查询不动产单元
     *
     * @param map
     * @return
     */
    List<Map> getDjBdcdyListByPage(Map map);

    /**
     * 获取权利人
     *
     * @param map
     * @return
     */
    List<Map> getDjQlrList(Map map);

    /**
     * 根据自然幢号获取房屋类型
     *
     * @param zrzh
     * @return
     */
    List<String> getBdcfwlxByzrzh(String zrzh);

    /**
     * 根据自然幢号获取不动产单元号
     *
     * @param map
     * @return
     */
    List<String> getBdcdyhByZrzh(Map map);

    /**
     * 通过过渡库的不动产单元编号查询地级数据的djid
     *
     * @param map
     * @return
     */
    String getDjid(Map map);


    List<Map> getDjsjBdcdyByPage(Map map);

    /**
     * @author bianwen
     * @description
     */
    List<Map> getDjYcFwDyh(Map map);
    Integer getYcDjFwMaxSxh(Map map);

    List<Map> getLpbList(Map map);

    /**
     * @param table 权籍库名称 bdcdyh 不动产单元号
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 这个是为了网外创建节省创建时间
     **/
    String getFwDjidByBdcdyh(@Param("table") String table, @Param("bdcdyh") String bdcdyh);

    /**
    * @Description: 通过不动产单元号去搜索预测户室的djid
    * @param table 权籍库库名， bdcdyh 不动产单元号
    * @return String 预测djid，即权籍库fw_ychs的id
    * @throws
    * @author xusong
    * @date 2018/8/8 14:25
    */
    String getYcFwDjidByBdcdyh(@Param("table") String table, @Param("bdcdyh") String bdcdyh);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/28 9:32
      * @description
      */
    String getDjsjBdcdyhByBdcdyh(Map<String,Object> paramMap);
}
