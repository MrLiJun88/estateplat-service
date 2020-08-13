package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcLq;
import cn.gtmap.estateplat.model.server.core.GdLq;
import cn.gtmap.estateplat.server.core.service.GdLqService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 过渡林权
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-10
 */
@Service
public class GdLqServiceImpl implements GdLqService {
    @Autowired
    EntityMapper entityMapper;

    @Override
    public GdLq queryGdLqById(final String lqid) {
        if (StringUtils.isNotBlank(lqid)) {
            return entityMapper.selectByPrimaryKey(GdLq.class, lqid);
        }
        return null;
    }

    @Override
    public BdcLq getBdcLqFromGdLq(final GdLq gdLq, BdcLq bdcLq) {
        if (bdcLq == null) {
            bdcLq = new BdcLq();
            bdcLq.setQlid(UUIDGenerator.generate18());
        }
        if (gdLq != null) {
            if (StringUtils.isNotBlank(gdLq.getLdgyqk()))
                bdcLq.setGyqk(gdLq.getLdgyqk());
            if (gdLq.getSyqmj() != null)
                bdcLq.setSyqmj(gdLq.getSyqmj());
            if (gdLq.getLdsyksrq() != null)
                bdcLq.setLdsyksqx(gdLq.getLdsyksrq());
            if (gdLq.getLdsyjsrq() != null)
                bdcLq.setLdsyjsqx(gdLq.getLdsyjsrq());
            if (StringUtils.isNotBlank(gdLq.getLdsuqxz()))
                bdcLq.setLdsyqxz(gdLq.getLdsuqxz());
            if (StringUtils.isNotBlank(gdLq.getZysz()))
                bdcLq.setZysz(gdLq.getZysz());
            if (gdLq.getZs() != null)
                bdcLq.setZs(gdLq.getZs());
            if (StringUtils.isNotBlank(gdLq.getLz()))
                bdcLq.setLz(gdLq.getLz());
            if (StringUtils.isNotBlank(gdLq.getZlnd()))
                bdcLq.setZlnd(gdLq.getZlnd());
            if (StringUtils.isNotBlank(gdLq.getXdm()))
                bdcLq.setXdm(gdLq.getXdm());
            if (StringUtils.isNotBlank(gdLq.getLb()))
                bdcLq.setLb(gdLq.getLb());
            if (StringUtils.isNotBlank(gdLq.getXb()))
                bdcLq.setLb(gdLq.getXb());
            if (StringUtils.isNotBlank(gdLq.getQy()))
                bdcLq.setQy(gdLq.getQy());
            if (StringUtils.isNotBlank(gdLq.getLmsyqr()))
                bdcLq.setLmsyqr(gdLq.getLmsyqr());
            if (StringUtils.isNotBlank(gdLq.getLmsuqr()))
                bdcLq.setLmsuqr(gdLq.getLmsuqr());
            if (StringUtils.isNotBlank(gdLq.getFbfmc()))
                bdcLq.setFbfmc(gdLq.getFbfmc());
        }
        return bdcLq;
    }
}
