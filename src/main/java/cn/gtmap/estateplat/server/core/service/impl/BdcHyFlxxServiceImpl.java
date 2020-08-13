package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcHyFlxx;
import cn.gtmap.estateplat.model.server.core.DJsjZhjnbdyjlb;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcHyFlxxService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhx
 * @version V1.0, 15-3-18
 */
@Repository
public class BdcHyFlxxServiceImpl implements BdcHyFlxxService {

    @Autowired
    BdcDjsjService bdcDjsjService;

    /**
     * 将地籍数据转换到HyFlxx表里
     *
     * @param hyid
     * @param zhdm
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<BdcHyFlxx> getBdcHyFlxxFromZHJNBDYJLB(final String hyid, final String zhdm) {
        List<DJsjZhjnbdyjlb> dJsjZhjnbdyjlbList = bdcDjsjService.getDJsjZhjnbdyjlb(zhdm);
        List<BdcHyFlxx> bdcHyFlxxList = null;
        if (CollectionUtils.isNotEmpty(dJsjZhjnbdyjlbList)) {
            bdcHyFlxxList = new ArrayList<BdcHyFlxx>();
            for (DJsjZhjnbdyjlb jlb : dJsjZhjnbdyjlbList) {
                BdcHyFlxx bdcHyFlxx = new BdcHyFlxx();
                bdcHyFlxx.setHyid(hyid);
                bdcHyFlxx.setHyflxxid(UUIDGenerator.generate18());
                bdcHyFlxx.setSyfs(jlb.getYhfs());
                bdcHyFlxx.setMj(jlb.getNbdymj());
                bdcHyFlxx.setSyj(jlb.getSyjse());
                bdcHyFlxxList.add(bdcHyFlxx);
            }
        }
        return bdcHyFlxxList;
    }
}
