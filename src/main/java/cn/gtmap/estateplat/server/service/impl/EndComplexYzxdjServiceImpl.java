package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcTdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * zx  预转现办结
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-8-25
 */
public class EndComplexYzxdjServiceImpl extends EndComplexProjectServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcXmService bdcXmService;


    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        //zdd 如果存在过渡数据  需要注销过渡权属状态
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    changeYgQszt(bdcXm);
                }
            }
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            if (qllxVo instanceof BdcDyaq) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    if (ybdcXm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcXm);
                        if (yqllxVo instanceof BdcDyaq) {
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                        }
                    }
                }
            }
            if (qllxVo instanceof BdcFdcq) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    if (ybdcXm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcXm);
                        if (yqllxVo != null && (yqllxVo instanceof BdcFdcq || yqllxVo instanceof BdcJsydzjdsyq)) {
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                        }
                    }
                }
            }
            bdcTdService.changeGySjydZt(bdcXm, new ArrayList<String>());

            /**
             * @author bianwen
             * @description 修改当前权利状态
             */
            qllxService.endQllxZt(bdcXm);
        }
    }
}
