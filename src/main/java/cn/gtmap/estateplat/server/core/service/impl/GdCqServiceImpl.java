package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
import cn.gtmap.estateplat.model.server.core.GdCq;
import cn.gtmap.estateplat.server.core.service.GdCqService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 过渡草权
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-10
 */
@Service
public class GdCqServiceImpl implements GdCqService {
    @Autowired
    EntityMapper entityMapper;

    @Override
    public GdCq queryGdCqById(final String cqid) {
        if (StringUtils.isNotBlank(cqid)) {
            return entityMapper.selectByPrimaryKey(GdCq.class, cqid);
        }
        return null;
    }

    @Override
    public BdcTdcbnydsyq getBdcTdcbFromGdCq(final GdCq gdCq, BdcTdcbnydsyq tdcbnydsyq) {
        if (tdcbnydsyq == null) {
            tdcbnydsyq = new BdcTdcbnydsyq();
            tdcbnydsyq.setQlid(UUIDGenerator.generate18());
        }
        if (gdCq != null) {
            if (StringUtils.isNotBlank(gdCq.getCdgyqk()))
                tdcbnydsyq.setGyqk(gdCq.getCdgyqk());
            if (gdCq.getQlksrq() != null)
                tdcbnydsyq.setSyksqx(gdCq.getQlksrq());
            if (gdCq.getQljsrq() != null)
                tdcbnydsyq.setSyjsqx(gdCq.getQljsrq());
            if (StringUtils.isNotBlank(gdCq.getTdsuqxz()))
                tdcbnydsyq.setTdsyqxz(gdCq.getTdsuqxz());
            if (StringUtils.isNotBlank(gdCq.getYzyfs()))
                tdcbnydsyq.setYzyfs(gdCq.getYzyfs());
            if (StringUtils.isNotBlank(gdCq.getCyzl()))
                tdcbnydsyq.setCyzl(gdCq.getCyzl());
            if (gdCq.getSyzcl() != null)
                tdcbnydsyq.setSyzcl(gdCq.getSyzcl());
            if (gdCq.getCbmj() != null)
                tdcbnydsyq.setSyqmj(gdCq.getCbmj());
        }
        return tdcbnydsyq;
    }
}
