package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzFdcq1;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzYwxx;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 房地产权信息(多幢)
 * Created by zhx on 2015/12/15.
 */
@Service
public class EtlBdcFdcqDzServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {

        List<BdcFdcqDz> bdcFdcqDzs = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzFdcq1> qzFdcq1List = qzHead.getData().getQz_fdcq1s();
            List<QzYwxx> qzYwxxList = qzHead.getQzYwxxList();
            bdcFdcqDzs = new ArrayList<BdcFdcqDz>();
            if (CollectionUtils.isNotEmpty(qzFdcq1List)) {
                for (QzFdcq1 qzFdcq1 : qzFdcq1List) {
                    BdcFdcqDz bdcFdcqDz = new BdcFdcqDz();
                    dozerUtil.beanDateConvert(qzFdcq1, bdcFdcqDz);
                    if (CollectionUtils.isNotEmpty(qzYwxxList))
                        logger.info(String.valueOf(qzYwxxList.get(0).getDjsj()));
                    dozerUtil.beanDateConvert(qzYwxxList.get(0), bdcFdcqDz);
                    logger.info(String.valueOf(bdcFdcqDz.getDjsj()));
                    bdcFdcqDzs.add(bdcFdcqDz);
                }
            }

        }
        return (List<T>) bdcFdcqDzs;
    }
}
