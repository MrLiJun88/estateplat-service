package cn.gtmap.estateplat.server.service.exchange.transition.impl;


import cn.gtmap.estateplat.model.exchange.transition.QzH;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.exchange.transition.QzZd;
import cn.gtmap.estateplat.model.exchange.transition.QzZh;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 过渡库信息转换到对应业务库bdc_bdcdy表
 *
 * @author <a href="mailto:sunyan@gtmap.cn">gtsy</a>
 * @version 1.0, 2015/11/4
 */
@Service
public class EtlBdcBdcdyServiceImpl implements ReadQzToBbcService {
    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcBdcdy> bdcBdcdies = null;
        if (qzHead != null && qzHead.getData() != null) {
            List<QzZd> qzZdList = qzHead.getData().getQz_zds();
            List<QzH> qzHList = qzHead.getData().getQz_hs();
            List<QzZh> qzZhList = qzHead.getData().getQz_zhs();
            bdcBdcdies = new ArrayList<BdcBdcdy>();
            //户室
            if (CollectionUtils.isNotEmpty(qzHList)) {
                for (QzH qzH : qzHList) {
                    BdcBdcdy bdcBdcdy = new BdcBdcdy();
                    //把Bdcdyid清空防止转换数据的时候Bdcdyid不能覆盖
                    bdcBdcdy.setBdcdyid(null);
                    dozerUtil.beanDateConvert(qzH, bdcBdcdy);
                    if (bdcBdcdy.getBdcdyh() != null && !bdcBdcdy.getBdcdyh().equals(""))
                        bdcBdcdies.add(bdcBdcdy);
                }
            }
            //宗地
            if (CollectionUtils.isNotEmpty(qzZdList)) {
                for (QzZd qzZd : qzZdList) {
                    BdcBdcdy bdcBdcdy = new BdcBdcdy();
                    dozerUtil.beanDateConvert(qzZd, bdcBdcdy);
                    if (bdcBdcdy.getBdcdyh() != null && !bdcBdcdy.getBdcdyh().equals(""))
                        bdcBdcdies.add(bdcBdcdy);
                }
            }
            //宗海
            if (CollectionUtils.isNotEmpty(qzZhList)) {
                for (QzZh qzZh : qzZhList) {
                    BdcBdcdy bdcBdcdy = new BdcBdcdy();
                    bdcBdcdy.setBdcdyid(UUIDGenerator.generate());
                    dozerUtil.beanDateConvert(qzZh, bdcBdcdy);
                    if (bdcBdcdy.getBdcdyh() != null && !bdcBdcdy.getBdcdyh().equals(""))
                        bdcBdcdies.add(bdcBdcdy);
                }
            }
        }
        return (List<T>) bdcBdcdies;
    }
}
