package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzLq;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcLq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:1056293546@qq.com">gtxiaojian</a>
 * @version 1.0, 2015/12/10
 */
@Service
public class EtlBdcLqServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcLq> bdcLqs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzLq> qzLqList = qzHead.getData().getQz_lqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcLqs = new ArrayList<BdcLq>();
            if (CollectionUtils.isNotEmpty(qzLqList)) {
                for (QzLq qzLq : qzLqList) {
                    BdcLq bdcLq = new BdcLq();
                    dozerUtil.beanDateConvert(qzLq, bdcLq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcLq);
                    bdcLqs.add(bdcLq);
                }
            }
        }
        return (List<T>) bdcLqs;
    }
}
