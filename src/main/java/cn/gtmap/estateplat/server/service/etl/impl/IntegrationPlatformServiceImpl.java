package cn.gtmap.estateplat.server.service.etl.impl;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.etl.IntegrationPlatformService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/1/22
 * @description 从文件中心取出集成平台的人员照片附件上传到登记项目的文件中心中
 */
@Service
public class IntegrationPlatformServiceImpl implements IntegrationPlatformService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private String etlUrl = AppConfig.getProperty("etl.url");
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public void uploadRyzpFileFromJcptToBdcdj(String proid) {
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(etlUrl)) {
            HttpClient client = null;
            GetMethod method = null;
            try {
                client = new HttpClient();
                client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 5000);
                client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
                method = new GetMethod(etlUrl + "/integrationPlatformNew/uploadRyzpFileFromJcptToBdcdj?proid=" + proid);
                method.setRequestHeader("Connection", "close");
                client.executeMethod(method);
            } catch (IOException e) {
                logger.error("IntegrationPlatformServiceImpl.uploadRyzpFileFromJcptToBdcdj", e);
            } finally {
                if (method != null) {
                    method.releaseConnection();
                }
                if (client != null) {
                    ((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
                }
            }
        }
    }

    @Override
    public Object getBusinessData(String businessNo, String proid) {
        String returnStr = "";
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(etlUrl)) {
            String url = etlUrl + "/integrationPlatform/getBusinessData?businessNo="+businessNo+"&proid=" + proid;
            try {
                returnStr = HttpClientUtil.doGet(url.trim(), null);
            } catch (Exception e) {
                logger.error("IntegrationPlatformServiceImpl.getBusinessData", e);
            }
        }
        if (StringUtils.isNotBlank(returnStr)) {
            List<JSONObject> jsonObjectList = (List<JSONObject>) JSONObject.parse(returnStr);
            if (CollectionUtils.isNotEmpty(jsonObjectList)) {
                for (JSONObject jsonObject : jsonObjectList) {
                    if (jsonObject.containsKey("propertyid") && StringUtils.isNotBlank((CharSequence) jsonObject.get("propertyid"))) {
                        String bdcqzh = String.valueOf(jsonObject.get("propertyid"));
                        if (StringUtils.isNotBlank(bdcqzh)) {
                            String yproid = bdcZsService.getProidByBdcqzh(bdcqzh);
                            if (StringUtils.isBlank(yproid)) {
                                yproid = bdcZsService.getProidByBdcqzhjc(bdcqzh, Constants.BDCQZS_BH_FONT);
                            }
                            if (StringUtils.isNotBlank(yproid)) {
                                jsonObject.put("yproid", yproid);
                                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(yproid);
                                if (bdcBdcdy != null) {
                                    jsonObject.put("bdcdyid", bdcBdcdy.getBdcdyid());
                                    if (StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                                        jsonObject.put("bdcdyh", bdcBdcdy.getBdcdyh());
                                    }
                                    if (StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                                        jsonObject.put("bdclx", bdcBdcdy.getBdclx());
                                    }
                                }
                            }
                        }
                    }
                }
                returnStr = JSONObject.toJSONString(jsonObjectList);
            }
        }
        return returnStr;
    }

    @Override
    public JSONObject importYwbh(String businessNo, String proid) {
        JSONObject returnJson = new JSONObject();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(etlUrl) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            String url = etlUrl + "/integrationPlatform/importYwbh?businessNo=" + businessNo + "&proid=" + proid;
            try {
                String returnStr = HttpClientUtil.doGet(url, null);
                if (StringUtils.isNotBlank(returnStr)) {
                    returnJson = JSON.parseObject(returnStr);
                }
            } catch (Exception e) {
                logger.error("IntegrationPlatformServiceImpl.importYwbh", e);
            }
        }
        return returnJson;
    }
}
