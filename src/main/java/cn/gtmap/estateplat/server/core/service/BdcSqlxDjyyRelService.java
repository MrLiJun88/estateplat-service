package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSqlxDjyyRel;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @description 申请类型登记事由关系接口
 */
public interface BdcSqlxDjyyRelService {
    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param map 查询条件map，其中key为数据库字段名称，value为条件值
     * @return 符合查询条件的登记原因列表
     * @description 根据传入的map查询条件返回登记原因列表
     */
    List<BdcSqlxDjyyRel> andEqualQueryBdcSqlxDjyyRel(final Map<String, String> map);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param sqlx 申请类型
     * @return 相对应的登记原因
     * @description 根据申请类型获取登记原因
     */
    String getDjyyBySqlx(final String sqlx);
}
