/**
 * 
 */
package cn.gtmap.estateplat.server.core.mapper;

import java.util.List;
import java.util.Map;

import cn.gtmap.estateplat.model.server.core.BdcXtQy;

/**
 * @author wangtao
 * @version  2016年4月20日
 * @description 操作企业信息表
 */
public interface BdcCompanyMapper {
    /**
     * 
     * @author wangtao
     * @description 查询企业信息列表
     * @param 
     * @return List<Map>
     */
	List<Map> getCompanyListByPage();
}
