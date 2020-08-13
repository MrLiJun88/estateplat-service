package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcXtConfigService;
import cn.gtmap.estateplat.server.core.service.BdcZsCreateZhService;
import cn.gtmap.estateplat.server.service.BdczsBhService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description
 */
@Service
public class BdcZsCreateZhServiceImpl implements BdcZsCreateZhService {
    @Autowired
    private BdczsBhService bdczsBhService;
    @Autowired
    private BdcXtConfigService bdcXtConfigService;

    /**
     * @param bdcZs  不动产证书
     * @param bdcXm  不动产项目
     * @param zstype 证书类型
     * @return 不动产证书
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 生成证书编号
     */
    @Override
    public synchronized BdcZs createBdcZh(BdcZs bdcZs, BdcXm bdcXm, String zstype) {
        if (bdcZs != null && bdcXm != null && StringUtils.isBlank(bdcZs.getBdcqzh())) {
            String zsFont = "";
            if (StringUtils.isNotBlank(zstype)) {
                BdcXtConfig bdcXtConfig = bdcXtConfigService.queryBdczsBhConfig(bdcXm);
                String nf = bdczsBhService.getBhYear(bdcXtConfig);
                String sqsjc = bdczsBhService.getProvinceShortName(bdcXtConfig);
                String szsxqc = bdczsBhService.getXzqShortName(bdcXtConfig);
                zsFont = zstype;
            } else {
                zsFont = bdczsBhService.getZsTpye(bdcXm);
            }

        }
        return bdcZs;
    }
}
