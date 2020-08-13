package cn.gtmap.estateplat.server.service.split.impl;

import cn.gtmap.estateplat.model.server.core.BdcFwfzxx;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.service.split.BdcSplitDataService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 拆分数据具体实现
 */
@Service
public class BdcSplitBdcXmServiceImpl implements BdcSplitDataService {
    /**
     * @param ysjData      原数据
     * @param splitNum     循环数据
     * @param bdcSplitData 拆分后数据
     * @param fgid
     * @return 拆分后数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据具体实现
     */
    @Override
    public BdcSplitData splitDate(BdcSplitData ysjData, SplitNum splitNum, BdcSplitData bdcSplitData, String fgid) throws InvocationTargetException, IllegalAccessException {
        if (ysjData != null && ysjData.getBdcXm() != null && ysjData.getBdcSpxx() != null && splitNum.getBdcBdcdy() != null) {
            BdcXm bdcXm = new BdcXm();
            BeanUtils.copyProperties(bdcXm, ysjData.getBdcXm());
            BdcSpxx bdcSpxx = new BdcSpxx();
            BeanUtils.copyProperties(bdcSpxx, ysjData.getBdcSpxx());
            bdcXm.setFgid(fgid);
            bdcXm.setProid(UUIDGenerator.generate18());
            //处理原产权proid字段,之前已排序默认第一个项目为产权项目
            if (StringUtils.isBlank(splitNum.getYcqproid())) {
                splitNum.setYcqproid(bdcXm.getProid());
                bdcXm.setYcqproid(bdcXm.getProid());
            } else {
                bdcXm.setYcqproid(splitNum.getYcqproid());
            }
            splitNum.setProid(bdcXm.getProid());
            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
            bdcSpxx.setProid(bdcXm.getProid());
            if (splitNum.getBdcFwfzxx() != null) {
                BdcFwfzxx bdcFwfzxx = splitNum.getBdcFwfzxx();
                bdcXm.setZl(bdcFwfzxx.getXmmc());
                bdcSpxx.setZl(bdcFwfzxx.getXmmc());
                bdcSpxx.setMj(bdcFwfzxx.getJzmj());
            } else if (StringUtils.isNotBlank(splitNum.getTdzl())) {
                bdcXm.setZl(splitNum.getTdzl());
                bdcSpxx.setZl(splitNum.getTdzl());
                bdcSpxx.setZdzhmj(splitNum.getTdmj());
            }
            if (splitNum.getBdcBdcdy() != null) {
                bdcXm.setBdcdyid(splitNum.getBdcBdcdy().getBdcdyid());
                bdcXm.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                bdcSpxx.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
                bdcSpxx.setBdcdyh(splitNum.getBdcBdcdy().getBdcdyh());
            }
            bdcSplitData.setBdcXm(bdcXm);
            bdcSplitData.setBdcSpxx(bdcSpxx);
        }
        return bdcSplitData;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "BDC_XM,BDC_SPXX";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 2;
    }
}
