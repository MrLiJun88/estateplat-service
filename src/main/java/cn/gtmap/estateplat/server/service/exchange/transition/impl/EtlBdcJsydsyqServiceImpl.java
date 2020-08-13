package cn.gtmap.estateplat.server.service.exchange.transition.impl;


import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzJsydsyq;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcJsydzjdsyq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 建设用地信息
 * Created by zhx on 2015/12/10.
 */
@Service
public class EtlBdcJsydsyqServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {

        List<BdcJsydzjdsyq> bdcJsydzjdsyqs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzJsydsyq> jsydsyqList = qzHead.getData().getQz_jsydsyqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcJsydzjdsyqs = new ArrayList<BdcJsydzjdsyq>();
            if (CollectionUtils.isNotEmpty(jsydsyqList)) {
                for (QzJsydsyq qzJsydsyq : jsydsyqList) {
                    BdcJsydzjdsyq bdcJsydzjdsyq = new BdcJsydzjdsyq();
                    dozerUtil.beanDateConvert(qzJsydsyq, bdcJsydzjdsyq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcJsydzjdsyq);
                    bdcJsydzjdsyqs.add(bdcJsydzjdsyq);
                }
            }
        }
        return (List<T>) bdcJsydzjdsyqs;
    }

}
