package cn.gtmap.estateplat.server.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/5/12 0012
 * @description
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String postForObject(String url, Object obj, int timeout) {
        String resultJson = "";
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(timeout * 1000);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(JSON.toJSONString(obj), headers);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        resultJson = restTemplate.postForObject(url, formEntity, String.class);
        return resultJson;
    }

    public static String getJyxxRest(String urlStr) {
        String responseXml = "";
        if (StringUtils.isNotBlank(urlStr)) {
            HttpClient client = null;
            GetMethod method = null;
            try {
                client = new HttpClient();
                client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 20000);
                client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 20000);
                method = new GetMethod(urlStr);
                method.setRequestHeader("Connection", "close");
                client.executeMethod(method);
                responseXml = method.getResponseBodyAsString();
            } catch (IOException e) {
                logger.error("CurrencyServiceImpl.class", e);
            } finally {
                if (method != null) {
                    method.releaseConnection();
                }
                if (client != null) {
                    ((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
                }
            }
        }
        return responseXml;
    }
}
