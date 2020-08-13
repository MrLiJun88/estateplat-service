package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzSj;
import cn.gtmap.estateplat.model.server.core.BdcSjxx;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 过渡库信息转换到对应业务库bdc_sjxx 表
 *
 * @author <a href="mailto:sunyan@gtmap.cn">gtsy</a>
 * @version 1.0, 2015/11/4
 */
@Service
public class EtlBdcSjxxServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcSjxx> sjxxes = null;
        if (qzHead != null && qzHead.getData() != null) {
            sjxxes = new ArrayList<BdcSjxx>();
            List<QzSj> sjList = qzHead.getData().getQz_sjcls();
            if (CollectionUtils.isNotEmpty(sjList)) {
                for (QzSj qzSj : sjList) {
                    BdcSjxx sjxx = new BdcSjxx();
                    dozerUtil.beanDateConvert(qzSj, sjxx);
                    sjxxes.add(sjxx);
                }
            }
        }
        return (List<T>) sjxxes;
    }
}
