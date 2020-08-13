package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.exchange.transition.QzYydj;
import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 过渡库信息转换到对应业务库bdc_yy表
 *
 * @author <a href="mailto:sunyan@gtmap.cn">gtsy</a>
 * @version 1.0, 2015/11/4
 */
@Service
public class EtlBdcYyServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcYy> bdcYys = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzYydj> qzYydjList = qzHead.getData().getQz_yydjs();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcYys = new ArrayList<BdcYy>();
            if (CollectionUtils.isNotEmpty(qzYydjList)) {
                for (QzYydj qzYydj : qzYydjList) {
                    BdcYy bdcYy = new BdcYy();
                    bdcYy.setQllx("20");
                    dozerUtil.beanDateConvert(qzYydj, bdcYy);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcYy);
                    bdcYys.add(bdcYy);
                }
            }
        }
        return (List<T>) bdcYys;
    }
}
