package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSqlxDjsyRel;

import java.util.List;
import java.util.Map;

/**
 * 不动产登记申请类型与登记事由关系
 * User: sc
 * Date: 15-9-9
 * Time: 下午4:58
 * To change this template use File | Settings | File Templates.
 */
public interface BdcSqlxDjsyRelService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param map 查询条件map，其中key为数据库字段名称，value为条件值
     * @return 符合查询条件的登记事由列表
     * @description 根据传入的map查询条件返回登记事由列表
     */

    List<BdcSqlxDjsyRel> andEqualQueryBdcSqlxDjsyRel(final Map<String, String> map);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param sqlx 申请类型
     * @return 相对应的登记事由
     * @description 根据申请类型获取登记事由
     */

    String getDjsyBySqlx(final String sqlx);
}
