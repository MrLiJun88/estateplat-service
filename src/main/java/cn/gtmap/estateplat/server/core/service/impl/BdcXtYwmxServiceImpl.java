package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSqlxYwmxRel;
import cn.gtmap.estateplat.model.server.core.BdcXtYwmx;
import cn.gtmap.estateplat.server.core.service.BdcXtYwmxService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/21 0021
 * @description
 */
@Service
public class BdcXtYwmxServiceImpl implements BdcXtYwmxService {
    @Autowired
    private EntityMapper entityMapper;

    /**
     * @param sqlx 申请类型
     * @return 不动产系统业务模型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据sqlx获取不动产系统业务模型
     */
    @Override
    public List<BdcXtYwmx> getBdcXtYwmxBySqlx(String sqlx) {
        List<BdcXtYwmx> bdcXtYwmxList = Lists.newArrayList();
        List<BdcSqlxYwmxRel> bdcSqlxYwmxRelList = getBdcSqlxYwmxRelBySqlx(sqlx);
        if (CollectionUtils.isNotEmpty(bdcSqlxYwmxRelList)) {
            Collections.sort(bdcSqlxYwmxRelList);
            for (int i = 0; i < bdcSqlxYwmxRelList.size(); i++) {
                BdcSqlxYwmxRel bdcSqlxYwmxRel = bdcSqlxYwmxRelList.get(i);
                if (StringUtils.isNotBlank(bdcSqlxYwmxRel.getMxid())) {
                    BdcXtYwmx bdcXtYwmx = entityMapper.selectByPrimaryKey(BdcXtYwmx.class, bdcSqlxYwmxRel.getMxid());
                    if (bdcXtYwmx != null) {
                        bdcXtYwmxList.add(bdcXtYwmx);
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(bdcXtYwmxList)) {
            bdcXtYwmxList = Collections.EMPTY_LIST;
        }
        return bdcXtYwmxList;
    }

    @Override
    public List<BdcSqlxYwmxRel> getBdcSqlxYwmxRelBySqlx(String sqlx) {
        List<BdcSqlxYwmxRel> bdcSqlxYwmxRelList = Lists.newArrayList();
        if (StringUtils.isNotBlank(sqlx)) {
            Example example = new Example(BdcSqlxYwmxRel.class);
            example.createCriteria().andEqualTo("sqlx", sqlx);
            bdcSqlxYwmxRelList = entityMapper.selectByExample(example);
        }
        if (CollectionUtils.isEmpty(bdcSqlxYwmxRelList)) {
            bdcSqlxYwmxRelList = Collections.EMPTY_LIST;
        }
        return bdcSqlxYwmxRelList;
    }
}
