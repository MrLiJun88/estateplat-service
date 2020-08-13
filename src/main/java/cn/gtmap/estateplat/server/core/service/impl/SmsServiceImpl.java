package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.sms.impl.CmccSmsServiceForZjgImpl;
import cn.gtmap.estateplat.core.support.sms.impl.CuccSmsServiceImpl;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/10/26
 * @description 短信服务
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXtConfigService bdcXtConfigService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcZsService bdcZsService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param proid
     * @param activityName
     * @return
     *
     * @description 发送短信
     **/
    @Async
    @Override
    public void sendSms(String proid, String activityName) {
        String smsMode = StringUtils.deleteWhitespace(AppConfig.getProperty("sms.mode"));
        if(StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(activityName) &&StringUtils.isNotBlank(smsMode)) {
            List<HashMap> smsPropsList = ReadXmlProps.getSmsPropsList();
            if(CollectionUtils.isNotEmpty(smsPropsList)){
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                for(HashMap smsProp:smsPropsList){
                    if(StringUtils.equals(CommonUtil.formatEmptyValue(smsProp.get("node")),activityName) && !(StringUtils.indexOf(CommonUtil.formatEmptyValue(smsProp.get("unsqlx"))+",",bdcXm.getSqlx()+",")>-1)){
                        String content = CommonUtil.formatEmptyValue(smsProp.get("content"));
                        Integer delay = CommonUtil.formatObjectToInteger(smsProp.get("delay"));
                        logger.info("---------开始发短信--------");
                        if(StringUtils.equals(smsMode, Constants.SMS_MODE_CUCC)){
                            beginSendCuccSms(bdcXm,content,delay);
                        }else if(StringUtils.equals(smsMode, Constants.SMS_MODE_CMCC)){
                            beginSendCmccSms(bdcXm,content);
                        }
                        break;
                    }
                }
            }
        }

    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm
     * @param content
     * @return
     *
     * @description 替换短信内容
     **/
    private String replaceSmsContent(BdcXm bdcXm,String content) {
        String smsContent = "";
        if(bdcXm != null && StringUtils.isNotBlank(content)) {
            if(StringUtils.indexOf(content,"slbh") > -1) {
                content = StringUtils.replace(content, "slbh", bdcXm.getBh());
            }

            if(StringUtils.indexOf(content,"djlx") > -1) {
                String djlxDm = bdcXm.getDjlx();
                if(StringUtils.isNotBlank(djlxDm)) {
                    HashMap map = Maps.newHashMap();
                    map.put("dm",djlxDm);
                    List<HashMap> djlxMapList = bdcZdGlService.getBdcZdDjlx(map);
                    if(CollectionUtils.isNotEmpty(djlxMapList)) {
                        HashMap djlxMap = djlxMapList.get(0);
                        content = StringUtils.replace(content, "djlx",CommonUtil.formatEmptyValue(djlxMap.get("MC")));
                    }else{
                        content = StringUtils.replace(content, "djlx","");
                    }
                }else {
                    content = StringUtils.replace(content, "djlx","");
                }
            }

            if(StringUtils.indexOf(content,"szrq") > -1) {
                List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(bdcXm.getProid());
                if(CollectionUtils.isNotEmpty(bdcZsList)) {
                    BdcZs bdcZs = bdcZsList.get(0);
                    if(bdcZs.getSzrq() != null) {
                        content = StringUtils.replace(content, "szrq", CalendarUtil.formateToStrChinaYMDDate(bdcZs.getSzrq()));
                    }else{
                        content = StringUtils.replace(content, "szrq","");
                    }
                }else {
                    content = StringUtils.replace(content, "szrq","");
                }
            }

            if(StringUtils.indexOf(content,"now") > -1) {
                   content = StringUtils.replace(content, "now",CalendarUtil.formateToStrChinaYMDDate(new Date()));
            }
            if(StringUtils.indexOf(content,"djjg") > -1) {
                BdcXtConfig bdcXtConfig = bdcXtConfigService.queryBdczsBhConfig(bdcXm);
                if(bdcXtConfig != null) {
                    content = StringUtils.replace(content, "djjg", bdcXtConfig.getDjjg());
                }else{
                    content = StringUtils.replace(content, "djjg", "");
                }
            }

            smsContent = content;
        }
        return smsContent;
    }



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm
     * @param content
     * @return
     *
     * @description 开始发送中国联通短信
     **/
    private void beginSendCuccSms(BdcXm bdcXm,String content,Integer delay) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            String smsUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("cucc.sms.url"));
            String smsSpCode = StringUtils.deleteWhitespace(AppConfig.getProperty("cucc.sms.spcode"));
            String smsUsername = StringUtils.deleteWhitespace(AppConfig.getProperty("cucc.sms.username"));
            String smsPassword = StringUtils.deleteWhitespace(AppConfig.getProperty("cucc.sms.password"));
            if (StringUtils.isNotBlank(smsUrl) && StringUtils.isNotBlank(smsSpCode) && StringUtils.isNotBlank(smsUsername) &&
            StringUtils.isNotBlank(smsPassword) && StringUtils.isNotBlank(content)) {
                logger.info("---------开始发送中国联通短信--------");
                CuccSmsServiceImpl cuccSmsService = new CuccSmsServiceImpl();
                cuccSmsService.setSmsUrl(smsUrl);
                cuccSmsService.setSpCode(smsSpCode);
                cuccSmsService.setUsername(smsUsername);
                cuccSmsService.setPassword(smsPassword);
                if(delay != null && delay > 0) {
                    Calendar newCalendar = Calendar.getInstance();
                    newCalendar.setTime(CalendarUtil.getCurHMSDate());
                    newCalendar.add(Calendar.HOUR,delay);
                    String scheduleTime = CalendarUtil.sdf_time.format(newCalendar.getTime());
                    cuccSmsService.setScheduleTime(scheduleTime);
                }

                HttpClient client = HttpClients.createDefault();
                cuccSmsService.setHttpClient(client);

                if(StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_GJPTSCDJ_DM)) {
                    content = replaceSmsContent(bdcXm,content);
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            if (StringUtils.isNoneBlank(bdcQlr.getQlrlxdh())) {
                                cuccSmsService.sendSms(StringUtils.deleteWhitespace(bdcQlr.getQlrlxdh()), content);
                            }
                        }
                    }
                } else {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        for (BdcXm bdcXmTemp : bdcXmList) {
                            if (bdcXmTemp != null && StringUtils.isNoneBlank(bdcXmTemp.getProid())) {
                                String smsContent = replaceSmsContent(bdcXmTemp,content);
                                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmTemp.getProid());
                                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                    for (BdcQlr bdcQlr : bdcQlrList) {
                                        if (StringUtils.isNoneBlank(bdcQlr.getQlrlxdh())) {
                                            cuccSmsService.sendSms(StringUtils.deleteWhitespace(bdcQlr.getQlrlxdh()),smsContent);
                                        }
                                    }
                                }
                            }
                        }
                    }else {
                        content = replaceSmsContent(bdcXm,content);
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                            for (BdcQlr bdcQlr : bdcQlrList) {
                                if (StringUtils.isNoneBlank(bdcQlr.getQlrlxdh())) {
                                    cuccSmsService.sendSms(StringUtils.deleteWhitespace(bdcQlr.getQlrlxdh()), content);
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param bdcXm
     * @param content
     * @return
     *
     * @description 开始发送中国移动短信
     **/
    private void beginSendCmccSms(BdcXm bdcXm,String content) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            String smsUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("cmcc.sms.url"));
            String applicationID = StringUtils.deleteWhitespace(AppConfig.getProperty("cmcc.sms.applicationID"));
            String sffsdx = AppConfig.getProperty("sffsdx.tc");
            if (StringUtils.isNotBlank(content) && ((StringUtils.isNotBlank(smsUrl) && StringUtils.isNotBlank(applicationID)) || StringUtils.equals("true",sffsdx))) {
                logger.info("---------开始发送中国移动短信--------");
                String prefixTel = "tel:";
                CmccSmsServiceForZjgImpl cmccSmsServiceForZjg = new CmccSmsServiceForZjgImpl();
                //太仓发送短信不需要进行如下处理
                if (!StringUtils.equals("true", sffsdx)) {
                    cmccSmsServiceForZjg.initMasSmsService(smsUrl);
                    cmccSmsServiceForZjg.setApplicationID(applicationID);
                }
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_GJPTSCDJ_DM)) {
                    content = replaceSmsContent(bdcXm,content);
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            if (StringUtils.isNoneBlank(bdcQlr.getQlrlxdh())) {
                                if (StringUtils.equals("true", sffsdx)) {
                                    sendSmsForTaiC(bdcQlr.getQlrlxdh(), content);
                                } else {
                                    String requestIdentifier = cmccSmsServiceForZjg.sendSms(prefixTel + bdcQlr.getQlrlxdh(), content);
                                    logger.info(requestIdentifier);
                                }
                            }
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                        if (CollectionUtils.isNotEmpty(bdcXmList)) {
                            for (BdcXm bdcXmTemp : bdcXmList) {
                                if (bdcXmTemp != null && StringUtils.isNoneBlank(bdcXmTemp.getProid())) {
                                    content = replaceSmsContent(bdcXmTemp,content);
                                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmTemp.getProid());
                                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                        for (BdcQlr bdcQlr : bdcQlrList) {
                                            if (StringUtils.isNoneBlank(bdcQlr.getQlrlxdh())) {
                                                if (StringUtils.equals("true", sffsdx)) {
                                                    sendSmsForTaiC(bdcQlr.getQlrlxdh(), content);
                                                } else {
                                                    String requestIdentifier = cmccSmsServiceForZjg.sendSms(prefixTel + bdcQlr.getQlrlxdh(), content);
                                                    logger.info(requestIdentifier);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        content = replaceSmsContent(bdcXm,content);
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                            for (BdcQlr bdcQlr : bdcQlrList) {
                                if (StringUtils.isNoneBlank(bdcQlr.getQlrlxdh())) {
                                    if (StringUtils.equals("true", sffsdx)) {
                                        sendSmsForTaiC(bdcQlr.getQlrlxdh(), content);
                                    } else {
                                        String requestIdentifier = cmccSmsServiceForZjg.sendSms(prefixTel + bdcQlr.getQlrlxdh(), content);
                                        logger.info(requestIdentifier);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //太仓发送短信需要将信息存入移动指定的数据库中
    private void sendSmsForTaiC(String phoneNum, String content) {
        String etlUrl = AppConfig.getProperty("etl.url");
        String url = etlUrl + "/sendMsgTc/saveMsg";
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Constants.DEFAULT_CHARSET);
        PostMethod method = new PostMethod(url);
        method.setParameter("destaddr", phoneNum);
        method.setParameter("content", content);
        try {
            httpClient.executeMethod(method);
        } catch (IOException e) {
            logger.error("SmsService.sendSmsForTaiC",e);
        } finally {
            method.releaseConnection();
            ((SimpleHttpConnectionManager)httpClient.getHttpConnectionManager()).shutdown();
        }
    }

}
