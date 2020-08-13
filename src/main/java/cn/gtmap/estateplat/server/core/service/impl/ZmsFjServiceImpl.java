package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcDyaqMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcXtZsQlqtzkMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcYgMapper;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.core.service.ZmsFjService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zwq
 * Date: 15-12-8
 * Time: 下午5:02
 */
@Service
public class ZmsFjServiceImpl implements ZmsFjService {
    @Autowired
    BdcDyaqMapper bdcDyaqMapper;
    @Autowired
    BdcXtZsQlqtzkMapper bdcXtZsQlqtzkMapper;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    DjsjFwService djsjFwService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcYgMapper bdcYgMapper;

    @Override
    public List<String> changeQkSz(List<String> list, String xx, String scfj, String qz) {

        int wz = -1;
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i != list.size(); i++) {
                wz = list.get(i).indexOf(xx);
                if (wz > -1) {
                    list.set(i, scfj);
                    break;
                }
            }
        }
        if (wz == -1&&StringUtils.isNotBlank(qz)) {
            if (list.size() > Integer.parseInt(qz)) {
                int j = Integer.parseInt(qz) - 1;
                list.add(j, scfj);
            } else {
                list.add(scfj);
            }
        }
        return list;
    }
}
