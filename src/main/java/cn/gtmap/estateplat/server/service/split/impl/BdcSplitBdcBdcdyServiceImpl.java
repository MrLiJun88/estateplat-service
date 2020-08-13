package cn.gtmap.estateplat.server.service.split.impl;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.split.BdcSplitDataService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 拆分数据具体实现
 */
@Service
public class BdcSplitBdcBdcdyServiceImpl implements BdcSplitDataService {
    @Autowired
    private BdcdyService bdcdyService;

    /**
     * @param ysjData      原数据
     * @param splitNum     循环数据
     * @param bdcSplitData 拆分后数据
     * @return 拆分后数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据具体实现
     */
    @Override
    public BdcSplitData splitDate(BdcSplitData ysjData, SplitNum splitNum, BdcSplitData bdcSplitData, String fgid) {
        if (ysjData != null && ysjData.getBdcBdcdy() != null && splitNum.getBdcBdcdy() == null) {
            String bdcdyh = ysjData.getBdcBdcdy().getBdcdyh();
            String djh = StringUtils.substring(bdcdyh, 0, 6) + "000000" + StringUtils.substring(bdcdyh, 12, 14) + "00000" + StringUtils.substring(bdcdyh, 19, 20);
            String newBdcdyh = bdcdyService.getBdcdyh(djh, bdcSplitData.getXh());
            BdcBdcdy bdcBdcdy = new BdcBdcdy();
            bdcBdcdy.setBdcdyid(UUIDGenerator.generate18());
            bdcBdcdy.setBdcdyh(newBdcdyh);
            bdcBdcdy.setDjbid(ysjData.getBdcBdcdy().getDjbid());
            if (splitNum.getBdcFwfzxx() != null) {
                if (StringUtils.isNotBlank(splitNum.getBdcFwfzxx().getBdcdybh())) {
                    bdcBdcdy.setFwbm(splitNum.getBdcFwfzxx().getBdcdybh());
                }
                bdcBdcdy.setBdclx(Constants.BDCLX_TDFW);
                bdcBdcdy.setBdcdyfwlx(Constants.DJSJ_FWHS_DM);
            } else {
                bdcBdcdy.setBdclx(Constants.BDCLX_TD);
            }
            splitNum.setBdcBdcdy(bdcBdcdy);
            bdcSplitData.setBdcBdcdy(bdcBdcdy);
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
        return "BDC_BDCDY";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 1;
    }
}
