package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.server.service.currency.CurrencyService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-26
 * @description 获取交易信息
 */
@Service
public class BdcQtSaveJyServiceImpl implements BdcQtDqService {
    @Autowired
    private CurrencyService currencyService;


    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (!StringUtils.equals(projectPar.getXtly(), Constants.XTLY_JCPTSL)) {
            currencyService.saveJyxx(projectPar.getWiid());
        }
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bcjyxx";
    }
}
