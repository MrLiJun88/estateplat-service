/**
 * 
 */
package cn.gtmap.estateplat.server.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gtis.common.util.UUIDGenerator;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXtQy;
import cn.gtmap.estateplat.server.core.service.BdcCompanyService;

/**
 * @author wangtao
 * @version 2016年4月20日
 * @description
 */
@Service
public class BdcCompanyServiceImpl implements BdcCompanyService {

	@Autowired
	private EntityMapper entityMapper;

	/**
	 * @author wangtao
	 * @description 根据企业ID对企业信息进行删除
	 * @param
	 * @return
	 */
	@Override
	public void deleteCompanyByPrimaryKey(final String ids) {
		if(StringUtils.isBlank(ids)) return;
		String[] id = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			if(StringUtils.isNotBlank(id[i])) entityMapper.deleteByPrimaryKey(BdcXtQy.class, id[i]);
		}
	}

	/**
	 * @author wangtao
	 * @description 增加或者修改企业信息
	 * @param
	 * @return
	 */
	@Override
	public void insertOrUpdateByPrimaryKey(final BdcXtQy bdcXtQy) {
		if(bdcXtQy==null)
			return;
		if (StringUtils.isBlank(bdcXtQy.getQyid())) {
			bdcXtQy.setQyid(UUIDGenerator.generate18());
		}
		entityMapper.saveOrUpdate(bdcXtQy, bdcXtQy.getQyid());

	}

}
