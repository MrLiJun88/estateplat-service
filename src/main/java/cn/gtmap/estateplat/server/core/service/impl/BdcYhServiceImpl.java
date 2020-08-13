package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.server.core.service.BdcYhService;
import cn.gtmap.estateplat.model.server.core.BdcXtYh;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 银行配置.
 * @author liujie
 * @version V1.0, 15-9-18
 * @description 银行配置服务，用于系统管理中的银行配置功能
 */
@Repository
public class BdcYhServiceImpl implements BdcYhService {
    /**
     * entity对象Mapper.
     * @author liujie
     * @description entity对象Mapper.
     */
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<BdcXtYh> getBankListByYhmc(String yhmc) {
        List<BdcXtYh> bdcXtYhList = null;
        if (StringUtils.isNotBlank(yhmc)) {
            Example bdcXtYhExample = new Example(BdcXtYh.class);
            bdcXtYhExample.createCriteria().andEqualTo("yhmc", yhmc);
            bdcXtYhList = entityMapper.selectByExample(bdcXtYhExample);
        }
        return bdcXtYhList;
    }
}
