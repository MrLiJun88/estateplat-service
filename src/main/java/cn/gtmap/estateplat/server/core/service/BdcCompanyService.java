/**
 * 
 */
package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXtQy;

/**
 * @author wangtao
 * @version 2016年4月20日
 * @description
 */
public interface BdcCompanyService {
    /**
     * 
     * @author wangtao
     * @description 根据企业ID对企业信息进行删除
     * @param 
     * @return void
     */
	void deleteCompanyByPrimaryKey(final String ids);
   /**
    * 
    * @author wangtao
    * @description  增加或者修改企业信息
    * @param 
    * @return void
    */
	void insertOrUpdateByPrimaryKey(final BdcXtQy bdcXtQy);

}
