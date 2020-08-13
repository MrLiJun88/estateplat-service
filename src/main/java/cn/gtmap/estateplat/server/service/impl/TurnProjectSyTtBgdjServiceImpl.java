package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcFdcqDzService;
import cn.gtmap.estateplat.server.core.service.BdcFwfzxxService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 变更登记转发服务
 *
 * @author <a href="mailto:yanyong@gtmap.cn">yanyong</a>
 * @version V1.0, 17-01-20
 */
public class TurnProjectSyTtBgdjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;

        qllxVo = qllxService.makeSureQllx(bdcXm);
        QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
        //zdd 转移登记 需要继承原权利信息
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        for (BdcXmRel bdcXmRel : bdcXmRelList) {
            qllxVo = qllxService.makeSureQllx(bdcXm);
            if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                List<QllxVo> yqllxList = qllxService.queryQllx(bdcXm);
                QllxVo yqllxVo = null;
                if (CollectionUtils.isNotEmpty(yqllxList))
                    yqllxVo = yqllxList.get(0);
                if (yqllxVo == null)
                    yqllxVo = qllxService.makeSureQllx(bdcXm);

                yqllxVo.setQlid(UUIDGenerator.generate18());
                yqllxVo.setProid(bdcXm.getProid());
                yqllxVo.setYwh(bdcXm.getBh());
                yqllxVo.setDbr(null);
                yqllxVo.setDjsj(null);
                //zdd 不应该继承原来项目的附记
                //姜堰需要继承上一手附记，其他地方不配置不影响
                String isJcFj = AppConfig.getProperty("isJcFj");
                if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcFj, "false")) {
                    yqllxVo.setFj("");
                }
                //zdd 前后的权利类型相同
                yqllxVo.setQszt(0);
                qllxVo = yqllxVo;
            }
            qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);

            if (qllxVo instanceof BdcFdcqDz&&StringUtils.isNotBlank(bdcXm.getXmly())&&StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)) {
                /**
                 *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                 *@description 禅道16600 多幢重新读取权籍信息生成bdc_fdcq_dz和bdc_fwfzxx
                 */
                BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                qllxVo = bdcFdcqDzService.reGenerateBdcFdcqDzFromQj(bdcXm,bdcFdcqDz);
            }
            if (qllxVo != null) {
                if (qllxVoTemp == null) {
                    entityMapper.insertSelective(qllxVo);
                } else {
                    qllxVo.setQlid(qllxVoTemp.getQlid());
                    entityMapper.updateByPrimaryKeySelective(qllxVo);
                }
            }
        }
        return qllxVo;
    }
}
