package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;
import cn.gtmap.estateplat.model.server.core.BdcZs;

/**
 * .
 * <p/>
 * 考虑到后期证书编号的形式比较多样  所以单独一个服务获取编号
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-19
 */
public interface BdczsBhService {
    /**
     * zdd 获取不动产证号简称
     *
     * @param bdcXtConfig
     * @return
     */
    String getProvinceShortName(BdcXtConfig bdcXtConfig);

    /**
     * zdd 获取不动产证号 年份
     *
     * @param bdcXtConfig
     * @return
     */
    String getBhYear(BdcXtConfig bdcXtConfig);

    /**
     * zdd 获取不动产证号 行政区简称
     *
     * @param bdcXtConfig
     * @return
     */
    String getXzqShortName(BdcXtConfig bdcXtConfig);

    /**
     * zdd 获取不动产证号 流水号
     *
     * @param bdcXtConfig
     * @return
     */
    String getLsh(BdcXtConfig bdcXtConfig, String zsFont, int zsIndex, BdcXm bdcXm);

    /**
     * 获取不动产权证编号
     *
     * @param bdcXm
     * @param bdcZs
     * @param zsIndex
     * @return
     */
    BdcZs creatBdcqzBh(BdcXm bdcXm, BdcZs bdcZs, int zsIndex, String zstype);

    /**
     * @param bdcXm
     * @return 证书类型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取证书类型
     */
    String getZsTpye(BdcXm bdcXm);
}
