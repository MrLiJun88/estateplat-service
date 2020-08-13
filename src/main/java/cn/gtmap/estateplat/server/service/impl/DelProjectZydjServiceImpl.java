package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 * 转移登记  需要改变原权利类型状态
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-26
 */
public class DelProjectZydjServiceImpl extends DelProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;

    @Override
    public void changeYqllxzt(BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                changeGdsjQszt(bdcXmRel, qllxVo, 0);
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                }
            }
        }
    }
}
