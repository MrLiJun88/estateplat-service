package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.GdYy;
import cn.gtmap.estateplat.server.core.service.GdYyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @version 1.0, 2019-02-25
 * @description GdYy服务实现
 */
@Service
public class GdYyServiceImpl implements GdYyService {

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public GdYy queryGdYyByYyid(final String yyid) {
        return StringUtils.isNotBlank(yyid) ? entityMapper.selectByPrimaryKey(GdYy.class, yyid) : null;
    }
}
