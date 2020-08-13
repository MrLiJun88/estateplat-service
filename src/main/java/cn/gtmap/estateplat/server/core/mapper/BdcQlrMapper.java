package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 权利人mapper
 * Created by lst on 2015/3/12.
 */
@Repository
public interface BdcQlrMapper {
    /**
     * 根据项目类型数组删除权利人信息
     *
     * @param proids
     */
    void delQlrByProids(String[] proids);

    /**
     * 插入不动产权利人信息
     *
     * @param qlr
     */
    void saveDjBdcQlrxx(BdcQlr qlr);

    List<BdcQlr> queryBdcQlrByProid(String proid);

    List<BdcQlr> queryBdcQlrList(Map map);

    /**
     * 根据地基号获取农用地使用权
     *
     * @param djh
     * @return
     */
    List<NydQlr> getNydQlrByDjh(String djh);

    List<DjsjQszdQlr> getQszdQlrDjh(String djh);

    /**
     * 把权利人里面的过渡数据转换为字典数据
     */
    String changQlrsfzjzlToDm(String qlrsfzjzl);

    List<String> getGyfsByProid(String proid);

    /**
     * 根据权利人姓名和身份证号来查
     *
     * @param map
     * @return
     */
    List<Map> getBdcdyidByProid(Map map);


	/**
	 * 通过proid获取权利人名称数组
	 */
	List<String> getQlrmcByProid(String proid);

    List<DjsjGzwQlr> getGzwQlrByDcbIndex(String dcbIndex);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:
    *@Date 19:55 2017/3/15
    */
    List<Map> ywrMcAndZjhByProid(String proid);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:
    *@Date 20:03 2017/3/15
    */
    List<Map> qlrMcAndZjhByProid(String proid);

    /**
     *@Author:<a href="mailto:liuxing@gtmap.cn">liuxing</a>
     *@Description:
     *@Date 21:29 2017/4/10
     */
    String getZjzlByMc(String mc);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量查询不动产权利人
     */
    List<BdcQlr> getBdcQlrListByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量删除不动产权利人
     */
    void batchDelBdcQlrByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @author jiangganzhi
     * @description  通过proid获取需要打印的存根权利人信息
     */
    HashMap getFzjlQlrxxList(HashMap map);
}
