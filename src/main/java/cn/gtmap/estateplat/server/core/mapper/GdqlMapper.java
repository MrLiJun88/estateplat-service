package cn.gtmap.estateplat.server.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/*
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0,2016/4/19.
 * @description 过度权利展示
 */
@Repository
public interface GdqlMapper {
    /**
     * @param map
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 为了提高查询效率先查询出qlid
     */
    List<String> getGdfwqlQlid(HashMap map);

    /**
     * @param hh
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 为了提高查询效率先查询出qlid
     */
    List<String> getGdtdqlQlid(String hh);

    /**
     * @param map
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 为了提高查询效率先查询出qlid
     */
    Integer getGdtdqlCount(HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据过渡ID即不动产ID去获取不动产类型以及QLIDS
     */
    HashMap<String,String> getBdcLxAndQlIdsByGdId(@Param(value="gdId")String gdId);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据QLID获取产权证号
     */
    List<HashMap<String, String>> getCqZhQlrZslxByQlId(HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 获取证书状态
     */
    HashMap<String,Object> getZsZtByQlId(HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据QLID查询过渡ID
     */
    List<HashMap<String,String>> getGdIdsByQlId(HashMap map);

    //crj 根据 过度权利类型 找到对应的 不动产权利类型
    HashMap<String,String> getQllxdmFromGdQl(HashMap map);
    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 根据map获取gd_fw_ql
     **/
    List<HashMap> getGdFwQlByMap(HashMap map);
}
