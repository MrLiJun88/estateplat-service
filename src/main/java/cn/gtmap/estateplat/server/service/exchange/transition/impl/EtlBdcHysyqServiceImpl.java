package cn.gtmap.estateplat.server.service.exchange.transition.impl;


import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzHysyq;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcHysyq;
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
public class EtlBdcHysyqServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcHysyq> bdcHysyqs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzHysyq> qzHysyqList = qzHead.getData().getQz_hysyqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcHysyqs = new ArrayList<BdcHysyq>();
            if (CollectionUtils.isNotEmpty(qzHysyqList)) {
                for (QzHysyq qzHysyq : qzHysyqList) {
                    BdcHysyq bdcHysyq = new BdcHysyq();
                    dozerUtil.beanDateConvert(qzHysyq, bdcHysyq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcHysyq);
                    bdcHysyqs.add(bdcHysyq);
                }
            }
        }
        return (List<T>) bdcHysyqs;
    }
}
