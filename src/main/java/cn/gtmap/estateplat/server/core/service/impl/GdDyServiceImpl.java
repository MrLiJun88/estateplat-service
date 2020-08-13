package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.GdCf;
import cn.gtmap.estateplat.model.server.core.GdDy;
import cn.gtmap.estateplat.server.core.service.GdDyService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @version 1.0, 2018-11-05
 * @description 过度抵押权利服务
 */
@Service
public class GdDyServiceImpl implements GdDyService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public GdDy getGdDyByDydjzmh(final String dydjzmh) {
        GdDy gdDy = null;
        if (StringUtils.isNotBlank(dydjzmh)) {
            Example example = new Example(GdDy.class);
            example.createCriteria().andEqualTo("dydjzmh", dydjzmh);
            List<GdDy> gdDyList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(gdDyList)) {
                gdDy = gdDyList.get(0);
            }
        }
        return gdDy;
    }

    @Override
    public GdDy getGdDyByDyDyid(final String dyid){
        return StringUtils.isNotBlank(dyid)?entityMapper.selectByPrimaryKey(GdDy.class,dyid):null;
    }
}
