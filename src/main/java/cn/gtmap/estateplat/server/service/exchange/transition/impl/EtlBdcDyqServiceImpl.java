package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzDYiq;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcDyq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 地役权登记信息
 * Created by zhangshiyao on 2015-12-10.
 */
@Service
public class EtlBdcDyqServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcDyq> bdcDyqList = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzDYiq> qzDYiqList = qzHead.getData().getQz_dyiqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcDyqList = new ArrayList<BdcDyq>();
            if (CollectionUtils.isNotEmpty(qzDYiqList)) {
                for (QzDYiq qzDYiq : qzDYiqList) {
                    BdcDyq bdcDyq = new BdcDyq();
                    bdcDyq.setQllx("19");
                    dozerUtil.beanDateConvert(qzDYiq, bdcDyq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcDyq);
                    bdcDyqList.add(bdcDyq);
                }
            }
        }
        return (List<T>) bdcDyqList;
    }
}
