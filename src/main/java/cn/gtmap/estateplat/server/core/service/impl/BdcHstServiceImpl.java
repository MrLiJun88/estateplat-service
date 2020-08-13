package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcHst;
import cn.gtmap.estateplat.model.server.core.DjsjZdZdt;
import cn.gtmap.estateplat.server.core.mapper.BdcHstMapper;
import cn.gtmap.estateplat.server.core.service.BdcHstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author zzhw
 * @version V1.0, 15-3-18
 */
@Repository
public class BdcHstServiceImpl implements BdcHstService {

    @Autowired
    private BdcHstMapper bdcHstMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BdcHst> selectBdcHst(final String bdcdyh) {
        return bdcHstMapper.selectBdcHst(bdcdyh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjZdZdt> selectBdcZdt(final String djh) {
        return bdcHstMapper.selectBdcZdt(djh);
    }
}
