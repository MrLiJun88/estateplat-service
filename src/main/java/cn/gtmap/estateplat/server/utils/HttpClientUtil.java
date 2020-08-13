package cn.gtmap.estateplat.server.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2017/12/13
 * @description httpClient工具类
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * @return httpClient实例
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 初始化httpClient
     */
    private static HttpClient initHttpClient() {
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams httpConnectionManagerParams = connectionManager.getParams();
        httpConnectionManagerParams.setSoTimeout(30000);
        httpConnectionManagerParams.setConnectionTimeout(5000);
        httpConnectionManagerParams.setMaxTotalConnections(256);
        httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(32);
        return new HttpClient(connectionManager);
    }

    /**
     * @param url      请求地址
     * @param paramMap 请求参数
     * @return 请求结果
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 发送post请求
     */
    public static String sendPostRequest(String url, Map<String, String> paramMap) throws IOException {
        if (StringUtils.isNoneBlank(url)) {
            HttpClient httpClient = initHttpClient();
            PostMethod postMethod = new PostMethod(url);
            Set<String> keySet = paramMap.keySet();
            try {
                if (CollectionUtils.isNotEmpty(keySet)) {
                    NameValuePair[] postData = new NameValuePair[keySet.size()];
                    int i = 0;
                    for (String key : keySet) {
                        postData[i] = new NameValuePair(key, paramMap.get(key));
                        i++;
                    }
                    postMethod.addParameters(postData);
                /*如果提交的参数中有中文字符，需转为 utf-8 格式编码*/
                    postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
                    if (httpClient.executeMethod(postMethod) == HttpStatus.SC_OK) {
                        return postMethod.getResponseBodyAsString();
                    }
                }
            } catch (Exception e) {
                LOGGER.error("httpClient.executeMethod() Exception!", e);
            }
        }
        return null;
    }

    public static String doGet(String url, Map<String, Object> map) throws Exception{
        CloseableHttpClient httpClient= HttpClients.createDefault();
        // 声明URIBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        // 判断参数map是否为非空
        if (map != null) {
            // 遍历参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 设置参数
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        return  EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    /**
     * @param url     请求地址
     * @param jsonStr json 请求参数
     * @return
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @description
     */
    public static String doPostJson(String url, String jsonStr){

        if (StringUtils.isNoneBlank(url) && StringUtils.isNotBlank(url)) {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            ContentType contentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), "UTF-8");
            StringEntity stringEntity = new StringEntity(jsonStr, contentType);
            httpPost.setEntity(stringEntity);
            // 设置请求的header
//            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            // 设置请求的参数
//            StringEntity entity = new StringEntity(jsonStr, "utf-8");
//            entity.setContentEncoding("UTF-8");
//            entity.setContentType("application/json");
//            httpPost.setEntity(entity);
            try {
                // 执行post请求
                HttpResponse response = httpClient.execute(httpPost);

                //获取服务器端返回的状态码
                int code = response.getStatusLine().getStatusCode();

                if (code == HttpStatus.SC_OK && response.getEntity() != null) {
                    return EntityUtils.toString(response.getEntity());

                }
                ((CloseableHttpResponse) response).close();
            }  catch (Exception e) {
                LOGGER.error("Exception", e);
            }

        }
        return null;
    }

    public  static String queryResultService(String modeType,String jsonObject,String url){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2000);
        requestFactory.setReadTimeout(2000);
        RestTemplate restTemplate=new RestTemplate(requestFactory);
        String resultJson="";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        if ( StringUtils.isNoneBlank(modeType) && StringUtils.equals(modeType.toUpperCase(),"GET")){
            HashMap<String, Object> paramMap = new HashMap<String, Object>();
            if (StringUtils.isNoneBlank(jsonObject)){
                paramMap=mapStringToHashMap(jsonObject);
            }
//            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
//                    postParameters, headers);
            resultJson= restTemplate.getForObject(url, String.class,paramMap);
        }else {
            HttpEntity<String> formEntity = new HttpEntity<String>(jsonObject, headers);
            resultJson = restTemplate.postForObject(url, formEntity, String.class);

        }
        headers.clear();
        return resultJson;
    }

    public static  HashMap<String, Object> mapStringToHashMap(String str){
        str=str.substring(1, str.length()-1);
        String[] strs=str.split(",");
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        for (String string : strs) {
            String key=string.split(":")[0];
            key=key.substring(1, key.length()-1);
            String value=string.split(":")[1];
            value=value.substring(1, value.length()-1);
            paramMap.put(key, value);
        }
        return paramMap;
    }
}
