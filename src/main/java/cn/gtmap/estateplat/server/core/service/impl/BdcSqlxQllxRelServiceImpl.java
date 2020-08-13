package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSqlxQllxRel;
import cn.gtmap.estateplat.server.core.service.BdcSqlxQllxRelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-20
 */
@Service
public class BdcSqlxQllxRelServiceImpl implements BdcSqlxQllxRelService {

    @Autowired
    EntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BdcSqlxQllxRel> andEqualQueryBdcSqlxQllxRel(final Map<String, Object> map) {
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Example qllxTemp = new Example(BdcSqlxQllxRel.class);
            Example.Criteria criteria = qllxTemp.createCriteria();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andEqualTo(key.toString(), val);
            }
            if(CollectionUtils.isNotEmpty( qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
                bdcSqlxQllxRelList = entityMapper.selectByExample(BdcSqlxQllxRel.class, qllxTemp);
        }
        return bdcSqlxQllxRelList;
    }

    @Override
    public String getQllxBySqlx(final String sqlxdm) {
        String qllx = "";
        if (StringUtils.isNotBlank(sqlxdm)) {
            HashMap hashMap = new HashMap();
            hashMap.put("sqlxdm", sqlxdm);
            List<BdcSqlxQllxRel> bdcSqlxQllxRelList = andEqualQueryBdcSqlxQllxRel(hashMap);
            //zhouwanqing 通过申请类型取出的这个关系数据是唯一的
            if (CollectionUtils.isNotEmpty(bdcSqlxQllxRelList)&&StringUtils.isNotBlank(bdcSqlxQllxRelList.get(0).getQllxdm())) {
                qllx = bdcSqlxQllxRelList.get(0).getQllxdm();
            }
        }
        return qllx;
    }
}
