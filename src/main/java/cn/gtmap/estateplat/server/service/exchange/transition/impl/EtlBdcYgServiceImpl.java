package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYgdj;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcYg;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 预告登记信息
 * Created by zhx on 2015/12/10.
 */
@Service
public class EtlBdcYgServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {

        List<BdcYg> bdcYgs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzYgdj> qzQgdjList = qzHead.getData().getQz_ygdjs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcYgs = new ArrayList<BdcYg>();
            if (CollectionUtils.isNotEmpty(qzQgdjList)) {
                for (QzYgdj qzYgdj : qzQgdjList) {
                    BdcYg bdcYg = new BdcYg();
                    bdcYg.setQllx("19");
                    dozerUtil.beanDateConvert(qzYgdj, bdcYg);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcYg);
                    bdcYgs.add(bdcYg);
                }
            }
        }
        return (List<T>) bdcYgs;
    }
}
