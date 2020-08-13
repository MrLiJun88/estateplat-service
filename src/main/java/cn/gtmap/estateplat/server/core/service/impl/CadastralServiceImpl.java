package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.service.CadastralService;
import cn.gtmap.estateplat.server.model.cadastral.SyncIsfsssParam;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/12/1
 * @description
 */
@Service
public class CadastralServiceImpl implements CadastralService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Async
    @Override
    public void syncQlrForCadastral(String wiid) {
        String syncUrl = AppConfig.getProperty("sync.url");
        String syncCadastral = AppConfig.getProperty("isSyncCadastral");
        if(StringUtils.isNotBlank(wiid) && Boolean.parseBoolean(syncCadastral)){
            HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
            HttpConnectionManagerParams params = connectionManager.getParams();
            params.setSoTimeout(30000);
            params.setConnectionTimeout(5000);
            params.setMaxTotalConnections(256);
            params.setDefaultMaxConnectionsPerHost(32);
            HttpClient client = new HttpClient(connectionManager);
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            PostMethod method = new PostMethod(syncUrl + wiid);
            method.setRequestHeader("Connection", "close");
            try {
                client.executeMethod(method);
                String responseXml = method.getResponseBodyAsString();
                logger.info(responseXml);
            } catch (IOException e) {
                logger.error("CadastralServiceImpl.syncQlrForCadastral",e);
            }finally {
                method.releaseConnection();
                ((MultiThreadedHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            }
        }
    }

    @Async
    @Override
    public void syncIsfsssForCadastral(SyncIsfsssParam syncIsfsssParam) {
        String syncUrl = AppConfig.getProperty("cadastralIsFsss.sync.url");
        String syncCadastral = AppConfig.getProperty("cadastralIsFsss.sync");
        if(syncIsfsssParam != null&&Boolean.parseBoolean(syncCadastral)&&StringUtils.isNotBlank(syncUrl)){
            PostMethod method = null;
            HttpClient client = null;
            try{
                HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
                HttpConnectionManagerParams params = connectionManager.getParams();
                params.setSoTimeout(30000);
                params.setConnectionTimeout(5000);
                params.setMaxTotalConnections(256);
                params.setDefaultMaxConnectionsPerHost(32);
                client = new HttpClient(connectionManager);
                client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
                syncUrl = syncUrl + URLEncoder.encode(JSON.toJSONString(syncIsfsssParam));
                logger.info("更新权籍附属设施地址为：" + syncUrl);
                method = new PostMethod(syncUrl);
                method.setRequestHeader("Connection", "close");
                client.executeMethod(method);
                String responseXml = method.getResponseBodyAsString();
                logger.info(responseXml);
            }catch (Exception e) {
                logger.error("CadastralServiceImpl.syncIsfsssForCadastral",e);
            }finally {
                method.releaseConnection();
                ((MultiThreadedHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            }
        }
    }
}
