package cn.gtmap.estateplat.server.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 表单验证
 * Created by lst on 2015/4/20.
 */
@Repository
public interface BdcXtLimitfieldMapper {

    /**
     * 获取转发的时候所有的验证信息
     *
     * @param map
     * @return
     */
    List<Map> getZyValidates(final Map map);

    /**
     * 运行验证sql
     *
     * @param sql
     * @return
     */
    List<Map> runSql(@Param("sql") final  String sql);

    /**
     * 验证过度房屋数据里的字典数据是否在字典表里存在
     *
     * @param fwid
     * @return
     */
    HashMap<String, Object> checkGdFwZdsjByFwid(@Param("fwid") final String fwid);

    /**
     * 验证过度土地数据里的字典数据是否在字典表里存在
     *
     * @param tdid
     * @return
     */
    HashMap<String, Object> checkGdTdZdsjByTdid(@Param("tdid") final String tdid);

    /**
     * 验证过度林权数据里的字典数据是否在字典表里存在
     *
     * @param lqid
     * @return
     */
    HashMap<String, Object> checkGdLqZdsjByLqid(@Param("lqid") final String lqid);

    /**
     * 验证过度草权数据里的字典数据是否在字典表里存在
     *
     * @param cqid
     * @return
     */
    HashMap<String, Object> checkGdCqZdsjByCqid(@Param("cqid") final String cqid);

    /**
     * 验证不动产单元字典数据
     *
     * @param fcdah
     * @return
     */
    List<HashMap<String, Object>> checkBdcdyByFcdah(@Param("fcdah") final String fcdah);

    /**
     * 验证过度预告数据里的字典数据是否在字典表里存在
     *
     * @param ygid
     * @return
     */
    HashMap<String, Object> checkGdYgZdsjByYgid(@Param("ygid") final String ygid);

    /**
     * 验证过度查封数据里的字典数据是否在字典表里存在
     *
     * @param cfid
     * @return
     */
    HashMap<String, Object> checkGdCfZdsjByCfid(@Param("cfid") final String cfid);

    /**
     * 验证过度抵押数据里的字典数据是否在字典表里存在
     *
     * @param dyid
     * @return
     */
    HashMap<String, Object> checkGdDyZdsjByDyid(@Param("dyid") final String dyid);

    /**
     * 验证过渡权利人数据里的字典数据是否在字典表里存在
     *
     * @param qlrid
     * @return
     */
    HashMap<String, Object> checkGdQlrZdsjByQlrid(@Param("qlrid") final String qlrid);

    /**
     * @param map workflowId     工作流定义ID workflowNodeId 节点ID
     * @return tableId 表ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取分组tableID
     */
    List<String> getTableIdGroupTable(Map map);
}
