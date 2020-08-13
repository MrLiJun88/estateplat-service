package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSqr;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSqrService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2017/2/13
 * @description 不动产申请人信息接口
 */
@Service
public class BdcSqrServiceImpl implements BdcSqrService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public BdcSqr initBdcSqrListFromBdcQlr(BdcQlr bdcQlr, String wiid) {
        BdcSqr bdcSqr = null;
        if (bdcQlr != null) {
            bdcSqr = new BdcSqr();
            bdcSqr.setSqrid(UUIDGenerator.generate());
            bdcSqr.setWiid(wiid);
            bdcSqr.setBz(bdcQlr.getBz());
            bdcSqr.setSqrmc(bdcQlr.getQlrmc());
            bdcSqr.setZjzl(bdcQlr.getQlrsfzjzl());
            bdcSqr.setZjh(bdcQlr.getQlrzjh());
            bdcSqr.setTxdz(bdcQlr.getQlrtxdz());
            bdcSqr.setYb(bdcQlr.getQlryb());
            bdcSqr.setFrmc(bdcQlr.getQlrfddbr());
            bdcSqr.setFrdh(bdcQlr.getQlrfddbrdh());
            bdcSqr.setDlrmc(bdcQlr.getQlrdlr());
            bdcSqr.setDlrdh(bdcQlr.getQlrdlrdh());
            bdcSqr.setDljg(bdcQlr.getQlrdljg());
            if (StringUtils.isNotBlank(bdcQlr.getQlrlx()) && StringUtils.equals(bdcQlr.getQlrlx(), Constants.QLRLX_QLR))
                bdcSqr.setSqrlb(Constants.SQRLB_QLR);
            else
                bdcSqr.setSqrlb(Constants.SQRLB_YWR);
            bdcSqr.setXh(bdcQlr.getSxh());
            bdcSqr.setGyfs(bdcQlr.getGyfs());
            bdcSqr.setSfczr(bdcQlr.getSfczr());
            bdcSqr.setXb(bdcQlr.getXb());
            bdcSqr.setQlbl(bdcQlr.getQlbl());
            bdcSqr.setSshy(bdcQlr.getSshy());
            bdcSqr.setLxdh(bdcQlr.getQlrlxdh());

        }
        return bdcSqr;
    }

    private BdcQlr initBdcQlrListFromBdcSqr(BdcQlr bdcQlr, BdcSqr bdcSqr, String proid, String qlrlx) {
        if (bdcQlr == null)
            bdcQlr = new BdcQlr();
        if (bdcSqr != null) {
            if (StringUtils.isBlank(bdcQlr.getQlrid()))
                bdcQlr.setQlrid(UUIDGenerator.generate());
            bdcQlr.setProid(proid);
            bdcQlr.setBz(bdcSqr.getBz());
            bdcQlr.setQlrmc(bdcSqr.getSqrmc());
            bdcQlr.setQlrsfzjzl(bdcSqr.getZjzl());
            bdcQlr.setQlrzjh(bdcSqr.getZjh());
            bdcQlr.setQlrtxdz(bdcSqr.getTxdz());
            bdcQlr.setQlryb(bdcSqr.getYb());
            bdcQlr.setQlrfddbr(bdcSqr.getFrmc());
            bdcQlr.setQlrfddbrdh(bdcSqr.getFrdh());
            bdcQlr.setQlrdlr(bdcSqr.getDlrmc());
            bdcQlr.setQlrdlrdh(bdcSqr.getDlrdh());
            bdcQlr.setQlrdljg(bdcSqr.getDljg());
            if(StringUtils.isNotBlank(qlrlx)){
                bdcQlr.setQlrlx(qlrlx);
            }else if (StringUtils.isNotBlank(bdcSqr.getSqrlb()) && StringUtils.equals(bdcSqr.getSqrlb(), Constants.SQRLB_YWR))
                bdcQlr.setQlrlx(Constants.QLRLX_YWR);
            else
                bdcQlr.setQlrlx(Constants.QLRLX_QLR);
            if(bdcSqr.getXh()!=null)
                bdcQlr.setSxh(bdcSqr.getXh());
            else
                bdcQlr.setSxh(1);
            bdcQlr.setGyfs(bdcSqr.getGyfs());
            bdcQlr.setSfczr(bdcSqr.getSfczr());
            bdcQlr.setXb(bdcSqr.getXb());
            bdcQlr.setQlbl(bdcSqr.getQlbl());
            bdcQlr.setSshy(bdcSqr.getSshy());
            bdcQlr.setQlrlxdh(bdcSqr.getLxdh());
            bdcQlr.setQygyr(bdcSqr.getQygyr());
        }
        return bdcQlr;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcSqr> getBdcSqrListByWiid(String wiid) {
        Example example = new Example(BdcSqr.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("wiid", wiid);
        return entityMapper.selectByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public BdcSqr getBdcSqrBySqrid(String sqrid) {
        return entityMapper.selectByPrimaryKey(BdcSqr.class, sqrid);
    }

    @Override
    @Transactional
    public void delBdcSqrBySqrid(String sqrid) {
        BdcSqr bdcSqr=getBdcSqrBySqrid(sqrid);
        if(bdcSqr!=null) {
            //删除权利人
            List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrByQlrmcZjh(bdcSqr.getSqrmc(), bdcSqr.getZjzl(), bdcSqr.getZjh());
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for(BdcQlr bdcQlr:bdcQlrList) {
                    if((StringUtils.equals(bdcSqr.getSqrlb(),Constants.SQRLB_QLR) && StringUtils.equals(bdcQlr.getQlrlx(),Constants.QLRLX_QLR))
                            || (StringUtils.equals(bdcSqr.getSqrlb(),Constants.SQRLB_YWR) && StringUtils.equals(bdcQlr.getQlrlx(),Constants.QLRLX_YWR)))
                        entityMapper.deleteByPrimaryKey(BdcQlr.class, bdcQlr.getQlrid());
                }
            }
        }
        //删除申请人
        entityMapper.deleteByPrimaryKey(BdcSqr.class, sqrid);
    }

    @Override
    public void saveBdcSqr(BdcSqr bdcSqr) {
        List<BdcSqr> bdcSqrList = getBdcSqrListByWiid(bdcSqr.getWiid());
        if (bdcSqr.getXh() == null) {
            int xh=0;
            if (CollectionUtils.isNotEmpty(bdcSqrList)) {
                for(BdcSqr bdcSqr1:bdcSqrList){
                   if(StringUtils.equals(bdcSqr1.getSqrlb(),bdcSqr.getSqrlb()))
                       xh++;
                }
            }
            bdcSqr.setXh(xh+1);
        }
        entityMapper.saveOrUpdate(bdcSqr, bdcSqr.getSqrid());
    }

    @Override
    public void glBdcSqr(String sqrid, String proid, String wiid, String qlrlx) {
        BdcSqr bdcSqr = getBdcSqrBySqrid(sqrid);
        if (bdcSqr != null) {
            List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrByQlrmcZjh(bdcSqr.getSqrmc(), bdcSqr.getZjzl(), bdcSqr.getZjh());
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                //已关联处理
                BdcQlr bdcQlr = bdcQlrList.get(0);
                //不是相同的项目时将权利人id重新赋值
                if(!StringUtils.equals(bdcQlr.getProid(),proid))
                    bdcQlr.setQlrid(null);
                initBdcQlrListFromBdcSqr(bdcQlr, bdcSqr, proid,qlrlx);
                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
            } else {
                //未关联处理
                BdcQlr bdcQlr = initBdcQlrListFromBdcSqr(null, bdcSqr, proid,qlrlx);
                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
            }
        }
    }
}
