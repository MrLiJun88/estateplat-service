package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcCjlb;
import cn.gtmap.estateplat.server.core.service.BdcCjlbService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
* 持件信息
* @author yy
* @version V1.0, 06-5-24
* @description
*/
@Repository
public class BdcCjlbServiceImpl implements BdcCjlbService {
    /**
     * entity对象Mapper.
     * @author yy
     * @description entity对象Mapper.
     */
    @Autowired
    private EntityMapper entityMapper;
    /**
     * 根据主键插入或更新
     * @param bdcCjlb 插入持件信息
     */
    @Override
    @Transactional
    public void insertCjlbOrUpdateByPrimaryKey(final BdcCjlb bdcCjlb) {
        if (StringUtils.isBlank(bdcCjlb.getCjid())) {
           bdcCjlb.setCjid(UUIDGenerator.generate18());
        }
        entityMapper.saveOrUpdate(bdcCjlb,bdcCjlb.getCjid());
    }

}
