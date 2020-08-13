package cn.gtmap.estateplat.server.service.exchange.transition.impl;


import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzQlr;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 权利人信息
 * Created by zhx on 2015/12/10.
 */
@Service
public class EtlBdcQlrServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {

        List<BdcQlr> bdcQlrs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzQlr> qzQlrList = qzHead.getData().getQz_qlrs();
            bdcQlrs = new ArrayList<BdcQlr>();
            if (CollectionUtils.isNotEmpty(qzQlrList)) {
                for (QzQlr qzQlr : qzQlrList) {
                    BdcQlr bdcQlr = new BdcQlr();
                    //把tQlrid清空防止转换数据的时候tQlrid不能覆盖
                    bdcQlr.setQlrid(null);
                    dozerUtil.beanDateConvert(qzQlr, bdcQlr);
                    //处理权利人表的qlrlx讲dm转换
                    if (bdcQlr.getQlrlx() != null && StringUtils.equals(bdcQlr.getQlrlx(), "1")) {
                        bdcQlr.setQlrlx("qlr");
                    } else if (bdcQlr.getQlrlx() != null && StringUtils.equals(bdcQlr.getQlrlx(), "2")) {
                        bdcQlr.setQlrlx("ywr");
                    }
                    bdcQlrs.add(bdcQlr);
                }
            }

        }
        return (List<T>) bdcQlrs;
    }
}

