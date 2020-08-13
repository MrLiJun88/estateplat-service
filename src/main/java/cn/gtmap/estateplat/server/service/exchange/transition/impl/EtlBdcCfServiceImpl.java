package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzCfdj;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 查封信息
 * Created by zhx on 2015/12/10.
 */
@Service
public class EtlBdcCfServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {

        List<BdcCf> bdcCfs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzCfdj> qzCfdjList = qzHead.getData().getQz_cfdjs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcCfs = new ArrayList<BdcCf>();
            if (CollectionUtils.isNotEmpty(qzCfdjList)) {
                for (QzCfdj qzCfdj : qzCfdjList) {
                    BdcCf bdcCf = new BdcCf();
                    bdcCf.setQllx("21");
                    dozerUtil.beanDateConvert(qzCfdj, bdcCf);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcCf);
                    bdcCfs.add(bdcCf);
                }
            }

        }
        return (List<T>) bdcCfs;
    }
}
