package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzTdsyq;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcTdsyq;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 土地所有权
 * Created by zhangshiyao on 2015-12-10.
 */
@Service
public class EtlBdcTdsyqServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcTdsyq> bdcTdsyqList = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzTdsyq> qzTdsyqList = qzHead.getData().getQz_tdsyqs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcTdsyqList = new ArrayList<BdcTdsyq>();
            if (CollectionUtils.isNotEmpty(qzTdsyqList)) {
                for (QzTdsyq qzTdsyq : qzTdsyqList) {
                    BdcTdsyq bdcTdsyq = new BdcTdsyq();
                    bdcTdsyq.setQlid(UUIDGenerator.generate());
                    dozerUtil.beanDateConvert(qzTdsyq, bdcTdsyq);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcTdsyq);
                    bdcTdsyqList.add(bdcTdsyq);
                }
            }
        }
        return (List<T>) bdcTdsyqList;
    }
}
