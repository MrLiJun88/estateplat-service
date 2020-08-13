package cn.gtmap.estateplat.server.service.currency.impl;

import cn.gtmap.estateplat.server.service.currency.CurrencyService;
import cn.gtmap.estateplat.server.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020/3/16
 * @description 获取c包接口
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final String currencyUrl = AppConfig.getProperty("currency.url");

    @Override
    public void saveJyxx(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(currencyUrl).append("/rest/v1.0/jy/jiangyin/saveJyxxToBdc").append("?wiid=" + wiid);
            HttpUtil.getJyxxRest(stringBuilder.toString());
        }
    }

    @Override
    public String checkJyzt(String fwbm, String tslx) {
        if (StringUtils.isNotBlank(fwbm)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(currencyUrl).append("/rest/v1.0/jy/jiangyin/checkJyzt").append("?fwbm=" + fwbm).append("&tslx=" + tslx);
            return HttpUtil.getJyxxRest(stringBuilder.toString());
        }
        return null;
    }

    @Override
    public void tsJyFwzt(String wiid, String tszt) {
        if (StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(tszt)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(currencyUrl).append("/rest/v1.0/jy/jiangyin/tsJyFwzt").append("?wiid=" + wiid).append("&tszt=" + tszt);
            HttpUtil.getJyxxRest(stringBuilder.toString());
        }
    }

    @Override
    public String checkHouseZt(List<Map> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            Map reqestMap = new HashMap();
            reqestMap.put("houseList", mapList);
            String url = currencyUrl + "/rest/v1.0/jy/jiangyin/checkHouseZt";
            return HttpUtil.postForObject(url, reqestMap, 10);
        }
        return null;
    }

    /**
     * @param proid
     * @param userid
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 完成业务环节
     */
    @Override
    public void finishStep(String proid, String userid) {
        String url = currencyUrl + "/rest/v1.0/sjgx/finishStep?proid=" + proid + "&userid" + userid;
        HttpUtil.getJyxxRest(url);
    }

    @Override
    public void updateSlzt(String proid, String slzt) {
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(slzt)) {
            String url = currencyUrl + "/rest/updateSlzt?proid=" + proid + "&slzt=" + slzt;
            HttpUtil.getJyxxRest(url);
        }
    }

    @Override
    public void changeDjzt(String wwslbh) {
        String url = currencyUrl + "/wwsq/changeDjzt?sqslbh=" + wwslbh;
        HttpUtil.getJyxxRest(url);
    }


}
