package cn.gtmap.estateplat.server.core.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 归档信息
 */
public interface BdcGdxxMapper {
	/**
	 * 
	 * @author wangtao
	 * @description 统计项目id用于判断是否已经归档
	 * @param 
	 * @return int
	 */
	int countXmid(String xmid);

	/**
	 *
	 * @author yanzhenkun
	 * @description 获取档案封皮信息
	 * @param
	 * @return List
	 */
	List<HashMap> getDafpxxMapByProid(HashMap map);
	
	/**
	 * 
	 * @description 统计项目id用于判断是否已经归档
	 * @param 
	 * @return int
	 */
	int countXmidByWiid(String wiid);

	/**
	 *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
	 *@param
	 *@return
	 *@description 获取统编号流水号
	 */
	String getSzyjdbh();

	/**
	 * @description 根据归档信息获取xmid
	 * @param map
	 */
	List<String> getGdxxXmidListByMap(HashMap map);


	/**
	 * @description 根据目录号获取最大的案卷号
	 * @param mlh
	 */
	int getCurrentMaxAjhByMlh(String mlh);

	/**
	 * @description 根据目录号获取最大的案卷号（过渡）
	 * @param mlh
	 */
	int getCurrentMaxAjhByMlhGd(String mlh);

	/**
	 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
	 * @param
	 * @return
	 * @description 根据档案类型获取档案号最大值
	 */
	Long getMaxDah(@Param(value = "gdlx") String gdlx);

	/**
	 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
	 * @param
	 * @return
	 * @description 高新区dah组成是1位分类字母+8流水号，所以取最大值需要截取后8位
	 */
	Long getMaxLsh(@Param(value = "gdlx") String gdlx);

	/**
	 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
	 * @param
	 * @return
	 * @description 根据档案类型获取盒号最大值
	 */
	Integer getMaxHh(@Param(value = "gdlx") String gdlx);

	/**
	 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
	 * @param xmidArray
	 * @return
	 * @description 根据xmid批量删除归档信息
	 */
	void delGdxxByXmid(@Param(value = "xmidArray") String[] xmidArray);

	/**
	 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
	 * @param
	 * @return
	 * @description 根据xmid和归档类型获取dah
	 */
	List<String> getBdcGdxxDahList(HashMap map);

	/**
	 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
	 * @param gdlx
	 * @return
	 * @description 太仓归档根据gdlx查询最大案卷号
	 */
	Integer getMaxAjhByGdlx(@Param("gdlx") String gdlx);


	/**
	 * @param map
	 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
	 * @rerutn
	 * @description 更新目录号
	 */
	void changeMlh(Map map);

	/**
	 *@auther <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
	 *@params
	 *@return
	 *@description 获取过渡档案封皮
	 */
	List<HashMap> getGdDafpxxMapByGdxxid(HashMap map);

}
