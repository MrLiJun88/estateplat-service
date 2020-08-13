package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzGjzwsyq;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcJzwsyq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 构（建）筑物所有权登记信息
 * Created by zhangshiyao on 2015-12-10.
 */
@Service
public class EtlBdcJzwsyqServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcJzwsyq> bdcJzwsyqList = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzGjzwsyq> qzGjzwsyqList = qzHead.getData().getQz_gjzwsyqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcJzwsyqList = new ArrayList<BdcJzwsyq>();
            if (CollectionUtils.isNotEmpty(qzGjzwsyqList)) {
                for (QzGjzwsyq qzGjzwsyq : qzGjzwsyqList) {
                    BdcJzwsyq bdcJzwsyq = new BdcJzwsyq();
                    dozerUtil.beanDateConvert(qzGjzwsyq, bdcJzwsyq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcJzwsyq);
                    bdcJzwsyqList.add(bdcJzwsyq);
                }
            }
        }
        return (List<T>) bdcJzwsyqList;
    }
}
