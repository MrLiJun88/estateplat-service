package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.QllxParent;
import cn.gtmap.estateplat.model.server.core.QllxVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-1
 */
public interface QllxParentService {

    /**
     * zdd 根据map参数  获取对应的状态正常的qllx
     *
     * @param qllxVo 实例化的qllx
     * @param map
     * @return QllxParent
     */
    List<QllxParent> queryZtzcQllxVo(QllxVo qllxVo, Map<String, Object> map);

    /**
     * zdd  根据map参数  获取对应的状态正常的qllx
     *
     * @param qllxVo
     * @param map
     * @return
     */
    List<Map> queryZtzcQllxMap(QllxVo qllxVo, Map<String, Object> map);

    /**
     * zdd 根据不动产单元号  获取对应的注销状态的qllx
     *
     * @param qllxVo 实例化的qllx
     * @param bdcdyh 不动产单元号
     * @return QllxParent
     */
    List<QllxParent> queryLogoutQllxVo(QllxVo qllxVo, String bdcdyh);


    /**
     * sc 根据不动产单元号  获取对应的cf状态的qllx
     *
     * @param qllxVo 实例化的qllx
     * @param bdcdyh 不动产单元号
     * @return QllxParent
     */
    List<QllxParent> queryLogcfQllxVo(QllxVo qllxVo, String bdcdyh, String xmzt, String isycf);


    /**
     * sc 自定义查询权利
     *
     * @param qllxVo 实例化的qllx
     * @param map
     * @return QllxParent
     */
    List<QllxParent> queryQllxVo(QllxVo qllxVo, Map<String, Object> map);

}
