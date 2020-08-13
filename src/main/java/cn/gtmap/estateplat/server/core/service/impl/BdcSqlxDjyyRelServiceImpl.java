package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSqlxDjyyRel;
import cn.gtmap.estateplat.server.core.service.BdcSqlxDjyyRelService;
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
 * User: lijian
 * Date: 16-4-15
 * Time: 下午8:00
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcSqlxDjyyRelServiceImpl implements BdcSqlxDjyyRelService {
    @Autowired
    EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BdcSqlxDjyyRel> andEqualQueryBdcSqlxDjyyRel(final Map<String, String> map) {
        List<BdcSqlxDjyyRel> list = null;
        Example bdcSqlxDjyyRel = new Example(BdcSqlxDjyyRel.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = bdcSqlxDjyyRel.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andEqualTo(key.toString(), val);
            }
        }
        if(CollectionUtils.isNotEmpty( bdcSqlxDjyyRel.getOredCriteria()) && CollectionUtils.isNotEmpty(bdcSqlxDjyyRel.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(BdcSqlxDjyyRel.class, bdcSqlxDjyyRel);
        return list;
    }

    @Override
    public String getDjyyBySqlx(final String sqlx) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("sqlxdm", sqlx);
        List<BdcSqlxDjyyRel> list = andEqualQueryBdcSqlxDjyyRel(map);
        if (CollectionUtils.isNotEmpty(list))
            return list.get(0).getDjyy();
        return null;
    }
}
