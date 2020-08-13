package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSqlxYwmxRel;
import cn.gtmap.estateplat.model.server.core.BdcXtYwmx;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/21 0021
 * @description 不动产系统业务模型数据服务
 */
public interface BdcXtYwmxService {
    /**
     * @param sqlx 申请类型
     * @return 不动产系统业务模型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据sqlx获取不动产系统业务模型
     */
    List<BdcXtYwmx> getBdcXtYwmxBySqlx(String sqlx);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据sqlx获取不动产申请类型业务模型关系
     */
    List<BdcSqlxYwmxRel> getBdcSqlxYwmxRelBySqlx(String sqlx);
}
