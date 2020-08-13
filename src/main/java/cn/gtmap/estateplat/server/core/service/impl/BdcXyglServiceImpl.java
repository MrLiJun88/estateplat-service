package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXygl;
import cn.gtmap.estateplat.server.core.mapper.BdcXyglMapper;
import cn.gtmap.estateplat.server.core.service.BdcXyglService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BdcXyglServiceImpl implements BdcXyglService {
    @Autowired
    BdcXyglMapper bdcXyglMapper;


    @Override
    public boolean checkBzfry(String qlrzjh) {
        int count = 0;
        if(StringUtils.isNotBlank(qlrzjh)) {
            Map map = new HashMap();
            map.put("qlrzjh",qlrzjh);
            map.put("sjly",Constants.SJLY_ZFGLZX);
            List<BdcXygl> list = bdcXyglMapper.getBdcXyglByQlrzjh(map);
            if(CollectionUtils.isNotEmpty(list)){
                count = list.size();
            }
        }
        return count != 0;
    }
}
