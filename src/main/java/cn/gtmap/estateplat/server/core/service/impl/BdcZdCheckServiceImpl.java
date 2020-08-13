package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcZdCheck;
import cn.gtmap.estateplat.server.core.service.BdcZdCheckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * lst
 * 验证信息字典表
 */
@Repository
public class BdcZdCheckServiceImpl implements BdcZdCheckService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void saveZdCheck(BdcZdCheck bdcZdCheck) {
        entityMapper.insertSelective(bdcZdCheck);
    }

    @Override
    public void delZdCheck(final String id) {
        if(StringUtils.isNotBlank(id))
            entityMapper.deleteByPrimaryKey(BdcZdCheck.class, id);
    }

    @Override
    public void updateZdCheck(BdcZdCheck bdcZdCheck) {
        entityMapper.updateByPrimaryKeySelective(bdcZdCheck);
    }

    @Override
    public List<BdcZdCheck> getZdCheck(Example example) {
        return entityMapper.selectByExample(BdcZdCheck.class, example);
    }
}
