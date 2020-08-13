package cn.gtmap.estateplat.server.service.split.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.service.split.BdcSplitDataService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/4/2
 * @description
 */
@Service
public class BdcSplitQlServiceImpl implements BdcSplitDataService {
    @Override
    public BdcSplitData splitDate(BdcSplitData ysjData, SplitNum splitNum, BdcSplitData bdcSplitData, String fgid) throws InvocationTargetException, IllegalAccessException {
        if (ysjData != null && splitNum != null && StringUtils.isNotBlank(splitNum.getProid()) && ysjData.getQllxVo() != null) {
            QllxVo qllxVo = ysjData.getQllxVo();
            if (qllxVo instanceof BdcFdcqDz) {
                BdcFdcq bdcFdcq = new BdcFdcq();
                bdcFdcq.setQlid(UUIDGenerator.generate18());
                bdcFdcq.setProid(splitNum.getProid());
                bdcFdcq.setQszt(Constants.QLLX_QSZT_XS);
                if (bdcSplitData != null && bdcSplitData.getBdcXm() != null) {
                    bdcFdcq.setYwh(bdcSplitData.getBdcXm().getBh());
                }
                if (splitNum.getBdcFwfzxx() != null) {
                    bdcFdcq.setZl(splitNum.getBdcFwfzxx().getXmmc());
                    bdcFdcq.setMyzcs(splitNum.getBdcFwfzxx().getZcs());
                    bdcFdcq.setGhyt(splitNum.getBdcFwfzxx().getGhyt());
                    bdcFdcq.setFwjg(splitNum.getBdcFwfzxx().getFwjg());
                    bdcFdcq.setJzmj(splitNum.getBdcFwfzxx().getJzmj());
                    bdcFdcq.setFwxz(splitNum.getBdcFwfzxx().getFwxz());
                    bdcFdcq.setSzmyc(splitNum.getBdcFwfzxx().getSzc());
                    bdcFdcq.setJznd(splitNum.getBdcFwfzxx().getJznd());
                    bdcFdcq.setFsss(splitNum.getBdcFwfzxx().getFsss());
                    if (StringUtils.isNotBlank(splitNum.getBdcFwfzxx().getSzc())) {
                        if (NumberUtils.isNumber(splitNum.getBdcFwfzxx().getSzc())) {
                            bdcFdcq.setSzc(Integer.valueOf(splitNum.getBdcFwfzxx().getSzc()));
                        } else {
                            bdcFdcq.setSzmyc(splitNum.getBdcFwfzxx().getSzc());
                        }
                    }
                    if (StringUtils.isNotBlank(splitNum.getBdcFwfzxx().getZcs())) {
                        if (NumberUtils.isNumber(splitNum.getBdcFwfzxx().getZcs())) {
                            bdcFdcq.setZcs(Integer.valueOf(splitNum.getBdcFwfzxx().getZcs()));
                        } else {
                            bdcFdcq.setMyzcs(splitNum.getBdcFwfzxx().getZcs());
                        }
                    }
                }
                if (splitNum.getBdcBdcdy() != null) {
                    bdcFdcq.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                    bdcFdcq.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                }
                bdcSplitData.setQllxVo(bdcFdcq);
            } else if (qllxVo instanceof BdcCf) {
                BdcCf bdcCf = new BdcCf();
                BeanUtils.copyProperties(bdcCf, qllxVo);
                bdcCf.setQlid(UUIDGenerator.generate18());
                bdcCf.setProid(splitNum.getProid());
                if (splitNum.getBdcFwfzxx() != null) {
                    bdcCf.setZl(splitNum.getBdcFwfzxx().getXmmc());
                } else {
                    bdcCf.setZl(splitNum.getTdzl());
                }
                if (splitNum.getBdcBdcdy() != null) {
                    bdcCf.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                    bdcCf.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                }
                bdcSplitData.setQllxVo(bdcCf);
            } else if (qllxVo instanceof BdcDyaq) {
                BdcDyaq bdcDyaq = new BdcDyaq();
                BeanUtils.copyProperties(bdcDyaq, qllxVo);
                bdcDyaq.setQlid(UUIDGenerator.generate18());
                bdcDyaq.setProid(splitNum.getProid());
                if (splitNum.getBdcFwfzxx() != null) {
                    bdcDyaq.setZl(splitNum.getBdcFwfzxx().getXmmc());
                } else {
                    bdcDyaq.setZl(splitNum.getTdzl());
                }
                if (splitNum.getBdcBdcdy() != null) {
                    bdcDyaq.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                    bdcDyaq.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                }
                bdcSplitData.setQllxVo(bdcDyaq);
            } else if (qllxVo instanceof BdcYy) {
                BdcYy bdcYy = new BdcYy();
                BeanUtils.copyProperties(bdcYy, qllxVo);
                bdcYy.setQlid(UUIDGenerator.generate18());
                bdcYy.setProid(splitNum.getProid());
                if (splitNum.getBdcFwfzxx() != null) {
                    bdcYy.setZl(splitNum.getBdcFwfzxx().getXmmc());
                } else {
                    bdcYy.setZl(splitNum.getTdzl());
                }
                if (splitNum.getBdcBdcdy() != null) {
                    bdcYy.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                    bdcYy.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                }
                bdcSplitData.setQllxVo(bdcYy);
            } else if (qllxVo instanceof BdcYg) {
                BdcYg bdcYg = new BdcYg();
                BeanUtils.copyProperties(bdcYg, qllxVo);
                bdcYg.setQlid(UUIDGenerator.generate18());
                bdcYg.setProid(splitNum.getProid());
                if (splitNum.getBdcFwfzxx() != null) {
                    bdcYg.setZl(splitNum.getBdcFwfzxx().getXmmc());
                } else {
                    bdcYg.setZl(splitNum.getTdzl());
                }
                if (splitNum.getBdcBdcdy() != null) {
                    bdcYg.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                    bdcYg.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                }
                bdcSplitData.setQllxVo(bdcYg);
            } else if (qllxVo instanceof BdcJsydzjdsyq) {
                BdcJsydzjdsyq bdcJsydzjdsyq = new BdcJsydzjdsyq();
                BeanUtils.copyProperties(bdcJsydzjdsyq, qllxVo);
                bdcJsydzjdsyq.setQlid(UUIDGenerator.generate18());
                bdcJsydzjdsyq.setProid(splitNum.getProid());
                bdcJsydzjdsyq.setZl(splitNum.getTdzl());
                bdcJsydzjdsyq.setSyqmj(splitNum.getTdmj());
                bdcSplitData.setQllxVo(bdcJsydzjdsyq);
            } else {
                QllxVo newQllxVo = new QllxParent();
                BeanUtils.copyProperties(newQllxVo, qllxVo);
                newQllxVo.setQlid(UUIDGenerator.generate18());
                newQllxVo.setProid(splitNum.getProid());
                if (splitNum.getBdcBdcdy() != null) {
                    newQllxVo.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                }
                bdcSplitData.setQllxVo(newQllxVo);
            }
        }
        return bdcSplitData;
    }

    @Override
    public String getIntetfacaCode() {
        return "BDC_FDCQ,BDC_CF,BDC_DAYQ,BDC_YG,BDC_YY,BDC_JSYDZJDSYQ";
    }

    @Override
    public Integer getSxh() {
        return 4;
    }
}
