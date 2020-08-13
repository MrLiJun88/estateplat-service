package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-5
 */
public class TurnProjectSpfGyyzServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    BdcXmService bdcXmService;

    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            List<InsertVo> insertVoList = new ArrayList<InsertVo>();
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getProid())) {
                    bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                    //zdd 此处后续需要优化  根据实际情况调用对应的服务  暂时调用默认的
                    List<InsertVo> insertVos = super.createQllxVo(bdcXm);
                    if (CollectionUtils.isNotEmpty(insertVos)) {
                        insertVoList.addAll(insertVos);
                    }
                }
            }
            super.saveOrUpdateInsertVo(insertVoList);
        }
        return qllxVo;
    }

}
