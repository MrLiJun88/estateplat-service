package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcTzmDjsjRel;
import cn.gtmap.estateplat.server.core.service.BdcTzmDjsjRelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sc
 * Date: 15-9-9
 * Time: 下午4:22
 * @description 特征码登记数据关系
 */
@Repository
public class BdcTzmDjsjRelServiceImpl implements BdcTzmDjsjRelService {
    @Autowired
    private EntityMapper entityMapper;


    @Override
    public List<BdcTzmDjsjRel> andEqualQueryBdcTzmDjsjRel(final Map<String, String> map) {
        List<BdcTzmDjsjRel> list = null;
        Example bdcTzmDjsjRel = new Example(BdcTzmDjsjRel.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = bdcTzmDjsjRel.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null)
                    criteria.andEqualTo(key.toString(), val);
            }
        }
        if(CollectionUtils.isNotEmpty( bdcTzmDjsjRel.getOredCriteria()) && CollectionUtils.isNotEmpty(bdcTzmDjsjRel.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(BdcTzmDjsjRel.class, bdcTzmDjsjRel);
        return list;
    }

    @Override
    public String getDjsjByTzm(final String zdzhtzm,final  String dzwtzm) {
        if (StringUtils.isNotBlank(zdzhtzm) && StringUtils.isNotBlank(dzwtzm)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("zdzhtzm", zdzhtzm);
            map.put("dzwtzm", dzwtzm);
            List<BdcTzmDjsjRel> list = andEqualQueryBdcTzmDjsjRel(map);
            if (CollectionUtils.isNotEmpty(list))
                return list.get(0).getDjsy();
        }
        return null;
    }

    @Override
    public String getDjsjByBdcdyh(final String bdcdyh) {
       if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() > 20) {
           String zdzhtzm = "";
           String dzwtzm = "";
            zdzhtzm = StringUtils.substring(bdcdyh, 12, 14);
            dzwtzm = StringUtils.substring(bdcdyh, 19, 20);
            return getDjsjByTzm(zdzhtzm, dzwtzm);
        }
        return null;
    }

}
