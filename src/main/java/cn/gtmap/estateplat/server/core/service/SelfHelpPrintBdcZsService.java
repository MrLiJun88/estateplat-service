package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.model.omp.BdcZzdzjFjxx;

import java.util.Map;

/**
 * @author <a href="mailto:zhangwentao@gtmap.cn">zhangwentao</a>
 * @version 1.0, 2018/9/12
 * @description
 */
public interface SelfHelpPrintBdcZsService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @param bdcZs
     * @description 自助打印机打印证书接口验证及更新证书编号及发证信息
     */
    Map checkData(BdcXm bdcXm, BdcZs bdcZs);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/6/11 15:38
      * @description 自助打印机保存打证照片和扫描件附件：
      */
    Map savePrintZsFj(BdcZzdzjFjxx bdczzdzjFjxx);
}
