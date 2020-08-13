package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.DjsjZdbgjlb;
import cn.gtmap.estateplat.server.core.mapper.DjsjLsBdcdyhMapper;
import cn.gtmap.estateplat.server.core.service.DjsjZdbgjlbService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lj</a>
 * @version 1.0, 2016-05-11
 * @description
 */
@Service
public class DjsjZdbgjlbServiceImpl implements DjsjZdbgjlbService {
	@Autowired
	private EntityMapper entityMapper;
    @Autowired
    private DjsjLsBdcdyhMapper djsjLsBdcdyhMapper;

	@Override
	public List<DjsjZdbgjlb> andEqualQueryDjsjZdbgjlb(Map<String, String> map) {
		List<DjsjZdbgjlb> list = null;
		Example bdcSqlxDjsyRel = new Example(DjsjZdbgjlb.class);
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
		if(CollectionUtils.isNotEmpty(bdcSqlxDjsyRel.getOredCriteria()) && CollectionUtils.isNotEmpty(bdcSqlxDjsyRel.getOredCriteria().get(0).getAllCriteria()))
			list = entityMapper.selectByExample(DjsjZdbgjlb.class, bdcSqlxDjsyRel);
		return list;
	}

	@Override
	public List<String> getYbhListByBh(String bh) {
        List<String> ybhList = new ArrayList<String>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("bh", bh);
		List<DjsjZdbgjlb> list = andEqualQueryDjsjZdbgjlb(map);
		if (CollectionUtils.isNotEmpty(list))
			for (DjsjZdbgjlb djsjZdbgjlb : list)
            ybhList.add(djsjZdbgjlb.getYbh());
		return ybhList;
	}

	@Override
	public List<String> getTdBdcdyhListByBh(String bh){
		List<String> tdBdcdyhList = new ArrayList<String>();
		if(StringUtils.isNotBlank(bh)){
			List<String> yDjhList = getYbhListByBh(bh);
			if(CollectionUtils.isNotEmpty(yDjhList)){
				for(String yDjh:yDjhList){
					if(StringUtils.isNotBlank(yDjh)){
						tdBdcdyhList.add(yDjh+ Constants.TD_BDCDYH_HJW);
					}
				}
			}
		}
		return tdBdcdyhList;
	}

    @Override
    public List<String> getBdcdyhListByBh(String bh) {
        return djsjLsBdcdyhMapper.getBdcdyhListByBh(bh);
    }
}
