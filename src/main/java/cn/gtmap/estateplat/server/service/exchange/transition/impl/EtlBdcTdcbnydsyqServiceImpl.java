package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzNydsyq;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 农用地信息
 * Created by zhx on 2015/12/10.
 */
@Service
public class EtlBdcTdcbnydsyqServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {

        List<BdcTdcbnydsyq> bdcTdcbnydsyqs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzNydsyq> nydsyqList = qzHead.getData().getQz_nydsyqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcTdcbnydsyqs = new ArrayList<BdcTdcbnydsyq>();
            if (CollectionUtils.isNotEmpty(nydsyqList)) {
                for (QzNydsyq qzNydsyq : nydsyqList) {
                    BdcTdcbnydsyq bdcTdcbnydsyq = new BdcTdcbnydsyq();
                    dozerUtil.beanDateConvert(qzNydsyq, bdcTdcbnydsyq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcTdcbnydsyq);
                    bdcTdcbnydsyqs.add(bdcTdcbnydsyq);
                }
            }

        }
        return (List<T>) bdcTdcbnydsyqs;
    }
}
