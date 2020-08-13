package cn.gtmap.estateplat.server.service.split.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.service.split.BdcSplitDataService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/4/2
 * @description 拆分只需要修改proid信息的数据
 */
@Service
public class BdcSplitBdcXxServiceImpl implements BdcSplitDataService {
    @Override
    public BdcSplitData splitDate(BdcSplitData ysjData, SplitNum splitNum, BdcSplitData bdcSplitData, String fgid) throws InvocationTargetException, IllegalAccessException {
        if (ysjData != null && splitNum != null && StringUtils.isNotBlank(splitNum.getProid())) {
            // 项目关系
            if (CollectionUtils.isNotEmpty(ysjData.getBdcXmRelList())) {
                List<BdcXmRel> newBdcXmRelList = new ArrayList<>();
                for (BdcXmRel bdcXmRel : ysjData.getBdcXmRelList()) {
                    BdcXmRel newBdcXmRel = new BdcXmRel();
                    BeanUtils.copyProperties(newBdcXmRel, bdcXmRel);
                    newBdcXmRel.setProid(splitNum.getProid());
                    newBdcXmRel.setRelid(UUIDGenerator.generate18());
                    if(ysjData.getQllxVo() instanceof BdcCf || ysjData.getQllxVo() instanceof BdcDyaq){
                        newBdcXmRel.setYproid(splitNum.getYcqproid());
                    }
                    newBdcXmRelList.add(newBdcXmRel);
                }
                bdcSplitData.setBdcXmRelList(newBdcXmRelList);
            }
            // 权利人
            if (CollectionUtils.isNotEmpty(ysjData.getBdcQlrList())) {
                List<BdcQlr> newBdcQlrList = new ArrayList<>();
                for (BdcQlr bdcQlr : ysjData.getBdcQlrList()) {
                    BdcQlr newBdcQlr  = new BdcQlr();
                    BeanUtils.copyProperties(newBdcQlr, bdcQlr);
                    newBdcQlr.setProid(splitNum.getProid());
                    newBdcQlr.setQlrid(UUIDGenerator.generate18());
                    newBdcQlrList.add(newBdcQlr);
                }
                bdcSplitData.setBdcQlrList(newBdcQlrList);
            }
            if (CollectionUtils.isNotEmpty(ysjData.getBdcXmzsRelList()) && CollectionUtils.isNotEmpty(ysjData.getBdcZsList())) {
                List<BdcXmzsRel> newBdcXmzsRelList = new ArrayList<>();
                List<BdcZs> newBdcZsList = new ArrayList<>();
                for (int i = 0; i < ysjData.getBdcXmzsRelList().size(); i++) {
                    // 证书
                    BdcZs newBdcZs = new BdcZs();
                    BeanUtils.copyProperties(newBdcZs, ysjData.getBdcZsList().get(i));
                    newBdcZs.setZsid(UUIDGenerator.generate18());
                    newBdcZsList.add(newBdcZs);
                    // 项目证书关系
                    BdcXmzsRel newBdcXmzsRel = new BdcXmzsRel();
                    BeanUtils.copyProperties(newBdcXmzsRel, ysjData.getBdcXmzsRelList().get(i));
                    newBdcXmzsRel.setProid(splitNum.getProid());
                    newBdcXmzsRel.setZsid(newBdcZs.getZsid());
                    newBdcXmzsRel.setXmzsgxid(UUIDGenerator.generate18());
                    newBdcXmzsRelList.add(newBdcXmzsRel);
                    // 证书权利人关系
                    if (CollectionUtils.isNotEmpty(bdcSplitData.getBdcQlrList())) {
                        List<BdcZsQlrRel> newBdcZsQlrRelList = new ArrayList<>();
                        for (BdcQlr bdcQlr : bdcSplitData.getBdcQlrList()) {
                            BdcZsQlrRel newBdcZsQlrRel = new BdcZsQlrRel();
                            newBdcZsQlrRel.setZsid(newBdcZs.getZsid());
                            newBdcZsQlrRel.setQlrid(bdcQlr.getQlrid());
                            newBdcZsQlrRel.setGxid(UUIDGenerator.generate18());
                            newBdcZsQlrRelList.add(newBdcZsQlrRel);
                        }
                        bdcSplitData.setBdcZsQlrRelList(newBdcZsQlrRelList);
                    }
                }
                bdcSplitData.setBdcZsList(newBdcZsList);
                bdcSplitData.setBdcXmzsRelList(newBdcXmzsRelList);
            }
            // 收件信息
            if (ysjData.getBdcSjxx() != null) {
                BdcSjxx bdcSjxx = new BdcSjxx();
                BeanUtils.copyProperties(bdcSjxx, ysjData.getBdcSjxx());
                bdcSjxx.setProid(splitNum.getProid());
                bdcSjxx.setSjxxid(UUIDGenerator.generate18());
                bdcSplitData.setBdcSjxx(bdcSjxx);
                // 不动产收件材料
                if (CollectionUtils.isNotEmpty(ysjData.getBdcSjclList())) {
                    List<BdcSjcl> newBdcSjclList = new ArrayList<>();
                    for (BdcSjcl bdcSjcl : ysjData.getBdcSjclList()) {
                        BdcSjcl newBdcSjcl = new BdcSjcl();
                        BeanUtils.copyProperties(newBdcSjcl, bdcSjcl);
                        newBdcSjcl.setSjxxid(bdcSjxx.getSjxxid());
                        newBdcSjcl.setSjclid(UUIDGenerator.generate18());
                        newBdcSjclList.add(newBdcSjcl);
                    }
                    bdcSplitData.setBdcSjclList(newBdcSjclList);
                }
            }
            // 归档信息
            if (ysjData.getBdcGdxx() != null) {
                BdcGdxx bdcGdxx = new BdcGdxx();
                BeanUtils.copyProperties(bdcGdxx, ysjData.getBdcGdxx());
                bdcGdxx.setXmid(splitNum.getProid());
                bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                bdcSplitData.setBdcGdxx(bdcGdxx);
            }
            // 收费信息
            if (ysjData.getBdcSfxx() != null) {
                BdcSfxx bdcSfxx = new BdcSfxx();
                BeanUtils.copyProperties(bdcSfxx, ysjData.getBdcSfxx());
                bdcSfxx.setProid(splitNum.getProid());
                bdcSfxx.setSfxxid(UUIDGenerator.generate18());
                bdcSplitData.setBdcSfxx(bdcSfxx);
                // 收费项目
                if (CollectionUtils.isNotEmpty(ysjData.getBdcSfxmList())) {
                    List<BdcSfxm> newBdcSfxmList = new ArrayList<>();
                    for (BdcSfxm bdcSfxm : ysjData.getBdcSfxmList()) {
                        BdcSfxm newBdcSfxm = new BdcSfxm();
                        BeanUtils.copyProperties(newBdcSfxm, bdcSfxm);
                        newBdcSfxm.setSfxxid(bdcSfxx.getSfxxid());
                        newBdcSfxm.setSfxmid(UUIDGenerator.generate18());
                        newBdcSfxmList.add(newBdcSfxm);
                    }
                    bdcSplitData.setBdcSfxmList(newBdcSfxmList);
                }
            }
            // 不动产单元锁定
            if (ysjData.getBdcBdcdySd() != null) {
                BdcBdcdySd bdcBdcdySd = new BdcBdcdySd();
                BeanUtils.copyProperties(bdcBdcdySd, ysjData.getBdcBdcdySd());
                bdcBdcdySd.setProid(splitNum.getProid());
                if (splitNum.getBdcBdcdy() != null) {
                    bdcBdcdySd.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                    bdcBdcdySd.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                }
                if (splitNum.getBdcFwfzxx() != null) {
                    bdcBdcdySd.setZl(splitNum.getBdcFwfzxx().getXmmc());
                } else {
                    bdcBdcdySd.setZl(splitNum.getTdzl());
                }
                bdcSplitData.setBdcBdcdySd(bdcBdcdySd);
            }
        }
        return bdcSplitData;
    }

    @Override
    public String getIntetfacaCode() {
        return "BDC_XM_REL,BDC_QLR,BDC_ZS,BDC_XMZS_REL,BDC_ZS_QLR_REL,BDC_SJXX,BDC_SJCL,BDC_GDXX,BDC_SFXX,BDC_SFXM,BDC_BDCDYSD";
    }

    @Override
    public Integer getSxh() {
        return 3;
    }
}
