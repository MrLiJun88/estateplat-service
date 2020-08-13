package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import cn.gtmap.estateplat.server.core.mapper.BdcZdQllxMapper;
import cn.gtmap.estateplat.server.core.service.BdcZdQllxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-23
 */
@Service
public class BdcZdQllxServiceImpl implements BdcZdQllxService {
    @Autowired
    private BdcZdQllxMapper bdcZdQllxMapper;

    @Override
    public BdcZdQllx queryBdcZdQllxByDm(final String dm) {
        BdcZdQllx bdcZdQllx = null;
        if (StringUtils.isNotBlank(dm))
            bdcZdQllx = bdcZdQllxMapper.queryBdcZdQllxByDm(dm);
        return bdcZdQllx;
    }

    @Override
    public List<BdcZdQllx> getAllBdcZdQllx() {
        return bdcZdQllxMapper.getAllBdcZdQllx();
    }
}
