package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcDelZszh;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 证书mapper
 * Created by lst on 2015/3/12.
 */
@Repository
public interface BdcZsMapper {

    /**
     * zdd 获取最大流水号
     * sc 根据年份过滤
     *
     * @return
     */
    Integer getMaxLsh(Map map);

    /**
     * 获取不动产权证流水号序列
     * @return
     */
    public Integer getMaxLshByXl(HashMap map);

    List<BdcZs> queryBdcZs(Map<String, String> map);

    /**
     * sc  根据proid获取原不动产权证号
     *
     * @param proid
     * @return
     */
    String getYbdcqzhByProid(String proid);

    /**
     * 根据proid获取过渡证书编号
     */
    List<Map> getGdzsByProid(String proid);


    /**
     * 根据proid获取批量证书
     *
     * @param proid
     * @return
     */
    List<BdcZs> getPlZsByProid(String proid);

    /**
     * lst 根据wiid获取批量证书
     *
     * @param wiid
     * @return
     */
    List<BdcZs> getPlZsByWiid(String wiid);

    /**
     * 批量获取证书
     * @param map
     * @return
     */
    List<Map> getPlZs(Map map);

    /**
     * 根据proid和bdcid获取原证号
     *
     * @param map
     * @return
     */
    String getYzhByProidAndBdcid(Map map);

    /*
   * zwq 根据proid和bdcdyh查找原不动产权证
   *
   * */
    List<BdcZs> getYbdcqzByProidAndBdcdyh(Map map);


    /**
     * 通过qlid获取土地证号
     * @param qlid
     * @return
     */
    String getTdzhByQlid(String qlid);

    /**
     * @param qlid
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 获取房产不动产权证号
     */
    public String getFczhByQlid(String qlid);
   /**
     * @param map
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @description 获取视图不动产证书信息
     */
    public List<HashMap> getViewBdcqzList(HashMap map);

    /**
     * 更新打印状态
     * @param map
     */
    void updateDyzt(Map map);
    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据宗地不动产单元号获取不动产权证号
     */
    public String getYtdbdcqzhByZdbdcdyh(String bdcdyh);
    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 根据宗地不动产单元号获取土地证号
     */
    public String getYtdzhByZdbdcdyh(String bdcdyh);

    /**
     * @author
     * @description 通过zsid获取bdcdy数量
     */
    Integer getBdcdyCountByZsid(String zsid);

    String getProidByBdczqh(String bdcqzh);


    /**
     * @param bdcZsList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据zsidList批量删除证书表记录
     */
    void batchDelBdcZsByBdcZsList(List<BdcZs> bdcZsList);

    /**
     * @param zsidList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据proidList批量删除项目证书关系表记录
     */
    void batchDelBdcZsByZsidList(List<String> zsidList);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据proidList批量删除项目证书关系表记录
     */
    void batchDelBdcXmZsRelByBdcXmList(List<BdcXm> bdcXmList);


    /**
     * @param bdcDelZszhList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcDelZszhList批量插入不动产删除证书证号表记录
     */
    //void batchInsertBdcDelZszhByBdcDelZszhList(Map map);
    void batchInsertBdcDelZszhByBdcDelZszhList(List<BdcDelZszh> bdcDelZszhList);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcXmList批量查询证书证号表记录
     */
    List<String> getZsidListByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @param map
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据zsidList批量更新证书领证日期
     */
    void batchUpateBdcZsLzrqByzsidList(Map map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map
     * @description 根据map获取证书列表
     */
    List<BdcZs> getPlZsByMap(Map map);

    /**
     * @author jiangganzhi
     * @description  通过proid获取需要打印的存根信息
     */
    HashMap getFzjlBdcqzxxList(HashMap map);

    /**
     * @param zsid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据zsid获取wiid
    */
    String getXmWiidByZsid(@Param("zsid")String zsid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据zxzh获取当年首次登记证最大流水号
     */
    Integer getMaxScdjzLsh(Map map);

    /**
     * @param proid 项目ID
     * @return 产权证号
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据项目ID获取产权证号
     */
    Map queryBdcqzhByProid(String proid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 根据bdcdyh查询bdczs
      */
    List<BdcZs> queryBdcZsByBdcdyh(String bdcdyh);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据wiid获取不动产证书通过坐落排序
     */
    List<BdcZs> getBdcZsListByWiidOrderByZl(final String wiid);
}
