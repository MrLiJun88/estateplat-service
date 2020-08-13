package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.BdcdyTradingStatusService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.model.TradingStatusAndDjzx;
import cn.gtmap.estateplat.server.model.trade.DjzxValidate;
import cn.gtmap.estateplat.server.model.trade.Result;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/3/27
 * @description
 */
@Service
public class BdcdyTradingStatusServiceImpl implements BdcdyTradingStatusService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private DjsjFwService djsjFwService;

    @Override
    public String getTradingStatus(String fwbm) {
        String tradingStatus = "";
        String tradingStatusWsUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("trading.status.ws.url"));
        if (StringUtils.isNotBlank(fwbm) && StringUtils.isNotBlank(tradingStatusWsUrl)) {
            String accountcode = StringUtils.deleteWhitespace(AppConfig.getProperty("trading.status.accountcode"));
            String password = StringUtils.deleteWhitespace(AppConfig.getProperty("trading.status.password"));
            String soapReq = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                    "   <soapenv:Header>\n" +
                    "       <tem:Certificate>\n" +
                    "           <tem:AccountCode>" + accountcode + "</tem:AccountCode>\n" +
                    "           <tem:PassWord>" + password + "</tem:PassWord>\n" +
                    "       </tem:Certificate>\n" +
                    "   </soapenv:Header>\n" +
                    "   <soapenv:Body>\n" +
                    "       <tem:SWRS_ZT>\n" +
                    "           <tem:FWID_NO>" + fwbm + "</tem:FWID_NO>\n" +
                    "       </tem:SWRS_ZT>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            String response = "";
            try {
                URL wsurl = new URL(tradingStatusWsUrl);
                String respBody = UrlConnectionUtil.getInfoByUrlConn(wsurl, soapReq);
                Document document = DocumentHelper.parseText(respBody);
                Element rootElt = document.getRootElement(); // 获取根节点
                Iterator body = rootElt.elementIterator("Body");
                while (body.hasNext()) {
                    Element recordResult = (Element) body.next();
                    response = UrlConnectionUtil.nextSubElement("SWRS_ZTResult", "", recordResult);
                }
            } catch (Exception e) {
                response = ParamsConstants.EXCEPTION_LOWERCASE;
                logger.error("BdcdyTradingStatusServiceImpl.getTradingStatus",e);
            }

            if(StringUtils.equals(response,ParamsConstants.EXCEPTION_LOWERCASE)){
                tradingStatus = ParamsConstants.EXCEPTION_LOWERCASE;
            }else{
                Result result = new JaxbUtil(Result.class, JaxbUtil.CollectionWrapper.class).fromXml(response);
                if(result != null&&StringUtils.equals(result.getIsSucessfull(),"1")&&result.getData()!=null) {
                    tradingStatus = result.getData().getZt();
                }
            }
        }
        return tradingStatus;
    }

    @Override
    public String getLtTradingStatus(String fwbm) {
        String tradingStatus = "";
        String tradingStatusWsUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("trading.status.ws.url"));
        if (StringUtils.isNotBlank(fwbm) && StringUtils.isNotBlank(tradingStatusWsUrl)) {
            String accountcode = StringUtils.deleteWhitespace(AppConfig.getProperty("trading.status.accountcode"));
            String password = StringUtils.deleteWhitespace(AppConfig.getProperty("trading.status.password"));
            String soapReq = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Header>\n" +
                    "    <Certificate xmlns=\"http://tempuri.org/\">\n" +
                    "      <AccountCode>" + accountcode + "</AccountCode>\n" +
                    "      <PassWord>" + password + "</PassWord>\n" +
                    "    </Certificate>\n" +
                    "  </soap:Header>\n" +
                    "  <soap:Body>\n" +
                    "    <LT_SPF_ZT xmlns=\"http://tempuri.org/\">\n" +
                    "      <FWID_NO>" + fwbm + "</FWID_NO>\n" +
                    "    </LT_SPF_ZT>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";

            String response = "";
            try {
                URL wsurl = new URL(tradingStatusWsUrl);
                String respBody = UrlConnectionUtil.getInfoByUrlConn(wsurl, soapReq);
                Document document = DocumentHelper.parseText(respBody);
                Element rootElt = document.getRootElement(); // 获取根节点
                Iterator body = rootElt.elementIterator("Body");
                while (body.hasNext()) {
                    Element recordResult = (Element) body.next();
                    response = UrlConnectionUtil.nextSubElement("LT_SPF_ZTResult", "", recordResult);
                }
            } catch (Exception e) {
                response = ParamsConstants.EXCEPTION_LOWERCASE;
                logger.error("BdcdyTradingStatusServiceImpl.getTradingStatus",e);
            }

            if(StringUtils.equals(response,ParamsConstants.EXCEPTION_LOWERCASE)){
                tradingStatus = ParamsConstants.EXCEPTION_LOWERCASE;
            }else{
                Result result = new JaxbUtil(Result.class, JaxbUtil.CollectionWrapper.class).fromXml(response);
                if(result != null&&StringUtils.equals(result.getIsSucessfull(),"1")&&result.getData()!=null) {
                    tradingStatus = result.getData().getZt();
                }
            }
        }
        return tradingStatus;
    }

    @Override
    public Map validate(String proid, String djzx) {
        Map resultMap = Maps.newHashMap();
        if(StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            String bdcdyh = bdcdyService.getBdcdyhByProid(proid);
            String tradingStatus = getTradingStatus(bdcdyh);
            if(bdcXm != null&&StringUtils.isNotBlank(bdcXm.getSqlx())
                    &&StringUtils.isNotBlank(bdcdyh)&&StringUtils.isNotBlank(tradingStatus)) {
                List<DjzxValidate> djzxValidateList = ReadJsonUtil.getDjzxValidate();
                if(CollectionUtils.isNotEmpty(djzxValidateList)) {
                    for(DjzxValidate djzxValidate:djzxValidateList) {
                        if(StringUtils.isNotBlank(djzxValidate.getSqlx())
                                &&StringUtils.equals(djzxValidate.getDjzx(),djzx)
                                &&StringUtils.indexOf(djzxValidate.getJyzt(),tradingStatus)>-1){
                            resultMap.put("checkModel",djzxValidate.getCheckModel());
                            resultMap.put("checkMsg",djzxValidate.getCheckMsg());
                            break;
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public Map checkBdcTradingStatusBeforeSave(String proid,String djzx) {
        Map resultMap = new HashMap();
        if (StringUtils.isNotBlank(proid)){
            BdcXm bdcxm = bdcXmService.getBdcXmByProid(proid);
            String bdcdyh = bdcdyService.getBdcdyhByProid(proid);
            if (bdcxm != null && StringUtils.equals(bdcxm.getSqlx(),Constants.SQLX_FWMM_DM) && StringUtils.isNotBlank(bdcdyh)
                    && StringUtils.isNotBlank(djzx)){
                HashMap paramMap = new HashMap();
                String fwbm = null;
                resultMap.put("sqlxdm",Constants.SQLX_FWMM_DM);
                paramMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(paramMap);
                if(CollectionUtils.isNotEmpty(djsjFwHsList)) {
                    fwbm = djsjFwHsList.get(0).getFwbm();
                }
                //用bdcdyh搜不到fwbm可能是多幢,多幢只验证其中1户的交易状态
                if (StringUtils.isBlank(fwbm)){
                    List<DjsjFwHs> djsjDzFwHsList = djsjFwService.getDzFwHsListByBdcdyh(bdcdyh);
                    if(CollectionUtils.isNotEmpty(djsjDzFwHsList)){
                        fwbm = djsjDzFwHsList.get(0).getFwbm();
                    }
                }
                String tradingStatus = getTradingStatus(fwbm);
                if (StringUtils.isNotBlank(tradingStatus)){
                    if(StringUtils.equals(tradingStatus,ParamsConstants.EXCEPTION_LOWERCASE)){
                        resultMap.put("errorMsg","接口获取异常，保存失败！");
                    }
                    List<TradingStatusAndDjzx> tradingStatusAndDjzxList = ReadJsonUtil.getDjzxAndTradingStatus();
                    if (CollectionUtils.isNotEmpty(tradingStatusAndDjzxList)){
                        String alertStatus = null;
                        String confirmStatus = null;
                        for (TradingStatusAndDjzx tradingStatusAndDjzx: tradingStatusAndDjzxList){
                            //查询项目登记子项的alert和confirm状态的交易状态
                            if (StringUtils.equals(djzx,tradingStatusAndDjzx.getDjzxdm())){
                                alertStatus = tradingStatusAndDjzx.getAlertStatus();
                                confirmStatus = tradingStatusAndDjzx.getConfirmStatus();
                                break;
                            }
                        }
                        if (StringUtils.isNotBlank(alertStatus)){
                            String[] alertStatusArray = alertStatus.split(",");
                            if(CommonUtil.indexOfStrs(alertStatusArray,tradingStatus)){
                                resultMap.put("alertFlag",ParamsConstants.TRUE_LOWERCASE);
                                resultMap.put("alertMsg",tradingStatus+"，保存失败！");
                            }
                        }
                        if(StringUtils.isNotBlank(confirmStatus)){
                            String[] confirmStatusArray = confirmStatus.split(",");
                            if(CommonUtil.indexOfStrs(confirmStatusArray,tradingStatus)){
                                resultMap.put("confirmFlag",ParamsConstants.TRUE_LOWERCASE);
                                resultMap.put("confirmMsg",tradingStatus+"，是否继续：");
                            }
                        }
                    }
                }else{
                    resultMap.put("errorMsg","房屋编码为空，保存失败!");
                }
            }
        }
        return resultMap;
    }

    @Override
    public String getTradingFilingStatus(String bdcqzh) {
        String tradingStatus = "";
        String tradingStatusWsUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("trading.status.ws.url"));
        try {
            if (StringUtils.isNotBlank(bdcqzh) && StringUtils.isNotBlank(tradingStatusWsUrl)) {
                String param = "{\"sign\":\"\",\"paratype\":\"1\",\"paravalue\":\"" + bdcqzh + "\"}";
                String soapReq = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "       <tem:GetJiaoYi_HTYZ>\n" +
                        "           <!--Optional:-->\n" +
                        "               <tem:json>" + param + "</tem:json>\n" +
                        "       </tem:GetJiaoYi_HTYZ>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
                logger.error("常熟交易备案状态验证参数传入:" + soapReq);
                String jsonResult = UrlConnectionUtil.getRespBody(tradingStatusWsUrl, soapReq, "GetJiaoYi_HTYZResult");
                logger.error("常熟交易备案状态验证接口返回:" + jsonResult);
                Map<String, Object> jsonResponse = JSONObject.parseObject(jsonResult);
                if(jsonResponse != null && jsonResponse.get("success") == Boolean.TRUE && jsonResponse.get("message")!=null) {
                    tradingStatus = jsonResponse.get("message").toString();
                }
            }
        } catch (Exception e) {
            tradingStatus = ParamsConstants.EXCEPTION_LOWERCASE;
            logger.error("BdcdyTradingStatusServiceImpl.getTradingFilingStatus",e);
        }
        return tradingStatus;
    }
}
