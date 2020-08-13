package cn.gtmap.estateplat.server.sj.zs;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.sj.InterfaceCode;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description 获取证书流水号
 */
public interface BdcZsGetbhService extends InterfaceCode {
    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取证书流水号
     */
    List<BdcZs> getbh(List<BdcZs> bdcZsList, String font, BdcXm bdcXm);
}
