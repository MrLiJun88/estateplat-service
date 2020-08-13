package cn.gtmap.estateplat.server.service.exchange.transition.impl;


import cn.gtmap.estateplat.model.exchange.transition.QzFdcq2;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 房地产权信息
 * Created by zhx on 2015/12/10.
 */
@Service
public class EtlBdcFdcqServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {

        List<BdcFdcq> bdcFdcqs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzFdcq2> qzFdcq2List = qzHead.getData().getQz_fdcq2s();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcFdcqs = new ArrayList<BdcFdcq>();
            if (CollectionUtils.isNotEmpty(qzFdcq2List)) {
                for (QzFdcq2 qzFdcq2 : qzFdcq2List) {
                    BdcFdcq bdcFdcq = new BdcFdcq();
                    dozerUtil.beanDateConvert(qzFdcq2, bdcFdcq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcFdcq);
                    bdcFdcqs.add(bdcFdcq);
                }
            }

        }
        return (List<T>) bdcFdcqs;
    }
}

