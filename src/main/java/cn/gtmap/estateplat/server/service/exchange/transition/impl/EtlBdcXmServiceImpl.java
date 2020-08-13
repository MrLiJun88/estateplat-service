package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzH;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.exchange.transition.QzZd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import cn.gtmap.estateplat.utils.CalendarUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 过渡库信息转换到对应业务库bdc_xm 表
 *
 * @author <a href="mailto:sunyan@gtmap.cn">gtsy</a>
 * @version 1.0, 2015/11/4
 */
@Service
public class EtlBdcXmServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcXm> bdcXms = null;
        if (qzHead != null) {
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            BdcXm bdcXm = new BdcXm();
            //把proid清空防止转换数据的时候proid不能覆盖
            bdcXm.setProid(null);
            bdcXm.setBh(CalendarUtil.getTimeMs());
            if (CollectionUtils.isNotEmpty(qzYwxxList)) {
                QzYwxx qzYwxx = qzYwxxList.get(0);
                dozerUtil.beanDateConvert(qzYwxx, bdcXm);
            }
            List<QzZd> qzZds = qzHead.getData().getQz_zds();
            List<QzH> qzHs = qzHead.getData().getQz_hs();
            if (CollectionUtils.isNotEmpty(qzZds)) {
                QzZd qzZd = qzZds.get(0);
                if (qzZd != null) {
                    dozerUtil.beanDateConvert(qzZd, bdcXm);
                }
            }
            if (CollectionUtils.isNotEmpty(qzHs)) {
                QzH qzH = qzHs.get(0);
                if (qzH != null) {
                    dozerUtil.beanDateConvert(qzH, bdcXm);
                }
            }
            bdcXms = new ArrayList<BdcXm>();
            bdcXms.add(bdcXm);
        }
        return (List<T>) bdcXms;
    }
}
