package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzDyaq;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 抵押权信息
 * Created by zhangshiyao on 2015-12-10.
 */
@Service
public class EtlBdcDyaqServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcDyaq> bdcDyaqList = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzDyaq> qzDyaqList = qzHead.getData().getQz_dyaqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcDyaqList = new ArrayList<BdcDyaq>();
            if (CollectionUtils.isNotEmpty(qzDyaqList)) {
                for (QzDyaq qzDyaq : qzDyaqList) {
                    BdcDyaq bdcDyaq = new BdcDyaq();
                    dozerUtil.beanDateConvert(qzDyaq, bdcDyaq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcDyaq);
                    bdcDyaqList.add(bdcDyaq);
                }
            }
        }
        return (List<T>) bdcDyaqList;
    }
}
