package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSqlxDjsyRel;
import cn.gtmap.estateplat.server.core.service.BdcSqlxDjsyRelService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sc
 * Date: 15-9-9
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcSqlxDjsyRelServiceImpl implements BdcSqlxDjsyRelService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BdcSqlxDjsyRel> andEqualQueryBdcSqlxDjsyRel(final Map<String, String> map) {
        List<BdcSqlxDjsyRel> list = null;
        Example bdcSqlxDjsyRel = new Example(BdcSqlxDjsyRel.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = bdcSqlxDjsyRel.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andEqualTo(key.toString(), val);
            }
        }
        if(CollectionUtils.isNotEmpty( bdcSqlxDjsyRel.getOredCriteria()) && CollectionUtils.isNotEmpty(bdcSqlxDjsyRel.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(BdcSqlxDjsyRel.class, bdcSqlxDjsyRel);

        return list;
    }

    @Override
    public String getDjsyBySqlx(final String sqlx) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("sqlx", sqlx);
        List<BdcSqlxDjsyRel> list = andEqualQueryBdcSqlxDjsyRel(map);
        if (CollectionUtils.isNotEmpty(list))
            return list.get(0).getDjsy();
        return null;
    }
}
