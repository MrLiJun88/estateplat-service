package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcYgTurnZyService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 批量抵押或发证   附件复制到每一个项目
 *
 * @author lst
 * @version V1.0, 15-12-24
 */
public class EndComplexProjectServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcYgTurnZyService bdcYgTurnZyService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public void changeXmzt(BdcXm bdcXm) {
        try {
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXmRel> bdcXmRelList = null;
                bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                bdcXmService.endBdcXm(bdcXm);
                //zdd 修改权利状态
                qllxService.endQllxZt(bdcXm);
                if (!StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM)
                        && !StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_GJPTSCDJ_DM)
                        && CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx())) {
                            super.changeGdsjQszt(bdcXmRel, 1);
                        }
                    }
                }
            }

        } catch (Exception e) {
           logger.error("EndComplexProjectServiceImpl.changeXmzt",e);
        }

    }
}
