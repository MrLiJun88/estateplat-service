package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXtCheckinfo;
import cn.gtmap.estateplat.server.core.service.BdcXtCheckinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * lst
 * 验证信息字典表
 */
@Repository
public class BdcXtCheckinfoServiceImpl implements BdcXtCheckinfoService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void saveXtCheckinfo(BdcXtCheckinfo bdcXtCheckinfo) {
        entityMapper.insertSelective(bdcXtCheckinfo);
    }

    @Override
    public void delXtCheckinfo(final String id) {
        entityMapper.deleteByPrimaryKey(BdcXtCheckinfo.class, id);
    }

    @Override
    public void updateXtCheckinfo(BdcXtCheckinfo bdcXtCheckinfo) {
        entityMapper.updateByPrimaryKeySelective(bdcXtCheckinfo);
    }

    @Override
    public List<BdcXtCheckinfo> getXtCheckinfo(final Example example) {
        return entityMapper.selectByExampleNotNull(BdcXtCheckinfo.class, example);
    }
}
