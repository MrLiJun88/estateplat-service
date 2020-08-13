package cn.gtmap.estateplat.server.core.service;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/*
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0,2016/4/19.
 * @description 过度权利展示
 */
public interface GdqlService {
    /**
     * @param qlid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据qlid保存房屋坐落
     */
    void saveFwzlByQlid(final String qlid);

    /**
     * @param qlid
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @rerutn
     * @description 根据qlid保存土地坐落
     */
    void saveTdzlByQlid(final String qlid);

    /**
     * @param qlid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据qlid保存权利人
     */
    void saveQlrByQlid(final String qlid);

    /**
     * @param hh map
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 为了提高查询效率先查询出qlid
     */
    List<String> getGdfwqlQlid(final String hh,final HashMap map);

    /**
     * @param hh
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 为了提高查询效率先查询出qlid
     */
    List<String> getGdtdqlQlid(final String hh);

    /**
     * @param hh map
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 为了提高查询效率先查询出qlid
     */
    Integer getGdfwqlCount(final String hh,final HashMap map);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @param
     * @rerutn
     * @description
     */
    void saveGdfw(final String fwid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @rerutn
     * @description
     */
    void saveGdtd(final String tdid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据过渡ID即不动产ID去获取不动产类型以及QLIDS
     */
    HashMap<String, String> getBdcLxAndQlIdsByGdId(@Param(value = "gdId") final String gdId);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据QLID获取产权证号
     */
    List<HashMap<String, String>> getCqZhQlrZslxByQlId(final HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 获取过渡证书状态
     */
    String getGdZsZt(final HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据QLID查询过渡ID
     */
    String getGdIdsByQlId(final HashMap map,final String flag);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 根据证号获取gd_ql_fw
     **/
    List<HashMap> getGdFwQlByFczh(String fczh);

    /**
     * @param qlid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据过渡的qlid获取qllx名字
    */
    String getQllxNameByQlid(final String qlid);
}
