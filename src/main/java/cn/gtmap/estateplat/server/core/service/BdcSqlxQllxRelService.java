package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSqlxQllxRel;

import java.util.List;
import java.util.Map;

/**
 * 不动产登记申请类型与权利类型关系
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-20
 */
public interface BdcSqlxQllxRelService {
    /**
     * zdd  根据map条件查询BdcSqlxQllxRel
     *
     * @param map
     * @return
     */
    List<BdcSqlxQllxRel> andEqualQueryBdcSqlxQllxRel(final Map<String, Object> map);

    /**
    * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
    * @data:2016/3/29
    * @param:sqlxdm
    * @return:String
    * @description: 根据申请类型查询权利类型
    * */
    public String getQllxBySqlx(final String sqlxdm);
}
