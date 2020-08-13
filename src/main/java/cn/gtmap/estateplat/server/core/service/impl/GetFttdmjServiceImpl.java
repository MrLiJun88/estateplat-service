package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.core.service.GetFttdmjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lj
 * Date: 15-10-22
 * Time: 下午7:52
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GetFttdmjServiceImpl implements GetFttdmjService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Override
    public String calculateFttdmj(final String djh) {
        Double zdmj = bdcZdGlService.getZdmj(djh);
        List<BdcFdcq> list = bdcZdGlService.getFdcqs(djh);
        double jzzmj = 0;
        if (list != null) {
            for (BdcFdcq bdcFdcq : list) {
                jzzmj += bdcFdcq.getJzmj();
            }
        }
        if (list != null && zdmj != null && jzzmj != 0) {
            for (BdcFdcq bdcFdcq : list) {
                double tempfttdmj = ((zdmj / jzzmj) * bdcFdcq.getJzmj());
                BigDecimal b = BigDecimal.valueOf(tempfttdmj);
                double fttdmj = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                bdcFdcq.setFttdmj(fttdmj);
                entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
            }
        } else {
            return "失败";
        }
        return "成功";
    }
}
