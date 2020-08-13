package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description 不动产证书创建证号业务逻辑
 */
public interface BdcZsCreateZhService {
    /**
     * @param bdcZs  不动产证书
     * @param bdcXm  不动产项目
     * @param zstype 证书类型
     * @return 不动产证书
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 生成证书编号
     */
    BdcZs createBdcZh(BdcZs bdcZs, BdcXm bdcXm, String zstype);
}
