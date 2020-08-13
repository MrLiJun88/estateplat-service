package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/12/26
 * @description
 */
public class EndComplexScdjAndPldyProjectServiceImpl extends EndProjectZydjServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;

    @Override
    public void changeYqllxzt(BdcXm bdcXm) {

        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        }

        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                super.changeGdsjQszt(bdcXmRel, Constants.QLLX_QSZT_XS);
            }
        }

        qllxService.endQllxZt(bdcXm);
    }
}
