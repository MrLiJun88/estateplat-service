package cn.gtmap.estateplat.server.service.currency.impl;

import cn.gtmap.estateplat.server.service.currency.ExchangeService;
import cn.gtmap.estateplat.server.utils.HttpUtil;
import com.gtis.config.AppConfig;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/5/12 0012
 * @description
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {
    private static final String exchangeUrl = AppConfig.getProperty("exchange.url");

    @Override
    public void changeLcjdmc(String proid, String wiid, String targetActivityDefids) {
        String url = exchangeUrl + "/share/db/changeLcjdmc?proid=" + proid + "&wiid=" + wiid + "&targetActivityDefids=" + targetActivityDefids;
        HttpUtil.getJyxxRest(url);
    }
}
