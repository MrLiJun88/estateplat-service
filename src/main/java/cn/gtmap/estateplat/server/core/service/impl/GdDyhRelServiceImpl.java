package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.GdDyhRel;
import cn.gtmap.estateplat.server.core.service.GdDyhRelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/3
 * @description
 */
@Service
public class GdDyhRelServiceImpl  implements GdDyhRelService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<GdDyhRel> queryGdDyhRelListByBdcid(String bdcid) {
        List<GdDyhRel> gdDyhRelList = null;
        if(StringUtils.isNotBlank(bdcid)) {
            Example example = new Example(GdDyhRel.class);
            example.createCriteria().andEqualTo("gdid", bdcid);
            gdDyhRelList = entityMapper.selectByExample(GdDyhRel.class,example);
        }
        return gdDyhRelList;
    }

    @Override
    public List<GdDyhRel> queryGdDyhRelListByBdcdyh(String bdcdyh) {
        List<GdDyhRel> gdDyhRelList = null;
        if(StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(GdDyhRel.class);
            example.createCriteria().andEqualTo("bdcdyh", bdcdyh);
            gdDyhRelList = entityMapper.selectByExample(GdDyhRel.class,example);
        }
        return gdDyhRelList;
    }
}
