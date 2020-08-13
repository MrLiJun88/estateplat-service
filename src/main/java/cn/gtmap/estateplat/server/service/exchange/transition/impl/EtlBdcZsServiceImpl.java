package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.server.utils.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 过渡库信息转换到对应业务库bdc_zs表
 * 为了保证版本库一致 注释掉部分代码
 *
 * @author <a href="mailto:sunyan@gtmap.cn">gtsy</a>
 * @version 1.0, 2015/11/4
 */
@Service
public class EtlBdcZsServiceImpl implements ReadQzToBbcService {

    @Autowired
    private DozerUtil dozerUtil;

    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcZs> bdcZses = null;
        //        if(qzHead!=null && qzHead.getData()!=null) {
        //            bdcZses=new ArrayList<BdcZs>();
        //            List<QzFz> qzFzList=qzHead.getData().getQz_fzs();
        //            List<QzSz> qzSzList=qzHead.getData().getQz_szs();
        //            if(CollectionUtils.isNotEmpty(qzFzList)){
        //                for(QzFz qzFz:qzFzList){
        //                    BdcZs bdcZs=new BdcZs();
        //                    bdcZs.setZsid( UUIDGenerator.generate18());
        //                    dozerUtil.beanDateConvert(qzFz,bdcZs);
        //                    if(CollectionUtils.isNotEmpty(qzSzList)){
        //                        for (QzSz qzSz:qzSzList){
        //                            dozerUtil.beanDateConvert(qzSz,bdcZs);
        //                        }
        //                    }
        //                    bdcZses.add(bdcZs);
        //                }
        //            }
        //        }
        return (List<T>) bdcZses;
    }
}
