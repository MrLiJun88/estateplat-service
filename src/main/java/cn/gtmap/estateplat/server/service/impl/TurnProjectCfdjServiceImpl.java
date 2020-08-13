package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.QllxService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 查封登记转发服务  不需要生成证书（证明）
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class TurnProjectCfdjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, bdcXm.getProid());
        QllxVo initQllx = qllxService.getQllxVoFromBdcXm(bdcXm);
        if (initQllx != null) {
            entityMapper.insertSelective(initQllx);
        }
        return qllxVo;
    }

    @Override
    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        //不需要生成证书（证明）
        return null;
    }
}
