package cn.gtmap.estateplat.server.service.split;

import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;
import cn.gtmap.estateplat.server.sj.InterfaceCode;
import cn.gtmap.estateplat.server.sj.InterfacerSx;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 拆分数据具体实现
 */
public interface BdcSplitDataService extends InterfaceCode, InterfacerSx {
    /**
     * @param ysjData      原数据
     * @param splitNum     循环数据
     * @param bdcSplitData 拆分后数据
     * @return 拆分后数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据具体实现
     */
    BdcSplitData splitDate(BdcSplitData ysjData, SplitNum splitNum, BdcSplitData bdcSplitData, String fgid) throws InvocationTargetException, IllegalAccessException;
}
