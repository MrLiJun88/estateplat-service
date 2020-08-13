package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.DjsjZdbgjlb;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lj</a>
 * @version 1.0, 2016-05-11
 * @description 宗地变更
 */
public interface DjsjZdbgjlbService {
	/**
	 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
	 * @param map 查询条件map，其中key为数据库字段名称，value为条件值
	 * @return 符合查询条件的宗地变更记录
	 * @description 根据传入的map查询条件返回宗地变更记录
	 */

	List<DjsjZdbgjlb> andEqualQueryDjsjZdbgjlb(Map<String, String> map);

	/**
	 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
	 * @param bh 新地籍号
	 * @return 相对应的原地籍号
	 * @description 根据新地籍号获取原地籍号
	 */

	List<String> getYbhListByBh(String bh);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param bh 新地籍号
     * @return 相对应的原地籍号
     * @description 根据新地籍号获取原地籍号上的所有不动产单元号
     */
    List<String> getBdcdyhListByBh(String bh);

    /**
     *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     *@param bh 新地籍号
     *@return bdcdyh
     *@description 根据新的地籍号获取原地籍号对应的纯土地的不动产单元号
     */
    List<String> getTdBdcdyhListByBh(String bh);
}
