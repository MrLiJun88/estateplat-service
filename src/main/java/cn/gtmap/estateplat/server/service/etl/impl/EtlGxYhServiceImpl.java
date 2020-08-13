package cn.gtmap.estateplat.server.service.etl.impl;

import cn.gtmap.estateplat.server.service.etl.EtlGxYhService;
import com.gtis.config.AppConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/7/11
 * @description
 */
@Service
public class EtlGxYhServiceImpl implements EtlGxYhService {
    @Override
    public void dbYhxx(String proid, String wiid, String bjblzt, String userid) {
        String etlDbYhxxUrl = AppConfig.getProperty("etl.dbYhxx.url");
        if(StringUtils.isNotBlank(proid)&&StringUtils.isNotBlank(etlDbYhxxUrl)){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(AppConfig.getProperty("etl.url")).append(etlDbYhxxUrl).append("?proid=" + proid).append("&wiid=" + wiid).append("&bjbldbzt=" + bjblzt).append("&userid=" + userid);
            HttpClient client = null;
            GetMethod method = null;
            try {
                client = new HttpClient();
                client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 5000);
                client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
                method = new GetMethod(stringBuilder.toString());
                method.setRequestHeader("Connection", "close");
                client.executeMethod(method);
            } catch (IOException e) {
                LoggerFactory.getLogger(this.getClass()).error("EtlGxYhServiceImpl.dbYhxx", e);
            } finally {
                if(method != null) {
                    method.releaseConnection();
                }
                if(client != null) {
                    ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                }
            }
        }
    }
}
