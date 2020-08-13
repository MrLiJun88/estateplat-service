package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcSjclMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ArchivePostService;
import cn.gtmap.estateplat.server.utils.ArchiveModel;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadJsonFileUtil;
import cn.gtmap.estateplat.utils.Charsets;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfUserVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-25
 */

public class ArchivePostServiceImpl implements ArchivePostService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String LOGSINGLEINFO = "ArchivePostService.postBdcXmInfo";

    private static final String LOGPLINFO = "ArchivePostService.archiveInterfaceCallPl";
    @Resource(name = "fileCenterNodeServiceImpl")
    NodeService nodeService;

    @Autowired
    QllxService qllxService;

    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcZdGlService bdcZdGlService;
    @Autowired
    BdcGdxxService bdcGdxxService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private BdcSjclMapper bdcSjclMapper;

    @Autowired
    private BdcZdQllxService bdcZdQllxService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXtLimitfieldService bdcXtLimitfieldService;

    @Override
    public BdcGdxx postBdcXmInfo(BdcXm bdcXm) {
        String userName = SessionUtil.getCurrentUser() != null ? SessionUtil.getCurrentUser().getUsername() : "";

        return postBdcXmInfo(bdcXm, userName);
    }

    @Override
    public BdcGdxx postBdcXmInfoNew(BdcXm bdcXm) {
        String userName = SessionUtil.getCurrentUser() != null ? SessionUtil.getCurrentUser().getUsername() : "";
        String jsonStr = ReadJsonFileUtil.readJsonFile("conf/server/postBdcXmToArchive.json");
        String sqlxdm = "";
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        boolean postMul = true;
        if (StringUtils.isNotBlank(jsonStr)) {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if (jsonObject.containsKey(sqlxdm)) {
                postMul = false;
            }
        }
        BdcGdxx bdcGdxx = null;
        if (postMul) {
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXmTemp : bdcXmList) {
                        bdcGdxx = postInfoNew(bdcXmTemp, userName);
                    }
                }
            } else {
                bdcGdxx = postInfoNew(bdcXm, userName);
            }
        } else {
            bdcGdxx = postInfoNew(bdcXm, userName);
            if (bdcGdxx != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXmTemp : bdcXmList) {
                        if (!StringUtils.equals(bdcXmTemp.getProid(), bdcXm.getProid())) {
                            bdcGdxx.setXmid(bdcXmTemp.getProid());
                            bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                            bdcGdxxService.insertBdcGdxx(bdcGdxx);
                        }
                    }
                }
            }
        }
        return bdcGdxx;
    }

    @Override
    public String postBdcXmInfoByProid(String proids, String cxgd) {
        String msg = "";
        String gdcgMsg = "";
        String gdsbMsg = "";
        if (StringUtils.isNotBlank(proids) && StringUtils.equals(cxgd, "true")) {
            for (String proid : StringUtils.split(proids, ",")) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null) {
                    BdcGdxx bdcGdxx = postBdcXmInfoNew(bdcXm);
                    if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getDaid())) {
                        gdcgMsg = "success!";
                    } else if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getGdxx())) {
                        msg = bdcGdxx.getGdxx();
                    } else {
                        gdsbMsg = "归档失败！";
                    }
                } else {
                    gdsbMsg = "未找到项目!";
                }
            }
        } else if (StringUtils.isNotBlank(proids)) {
            for (String proid : StringUtils.split(proids, ",")) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                BdcGdxx ybdcGdxx = bdcGdxxService.queryBdcGdxxByProid(proid);
                if (bdcXm != null && ybdcGdxx == null) {
                    BdcGdxx bdcGdxx = postBdcXmInfoNew(bdcXm);
                    if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getDaid())) {
                        gdcgMsg = "success!";
                    } else if (bdcGdxx != null && StringUtils.isNotBlank(bdcGdxx.getGdxx())) {
                        msg = bdcGdxx.getGdxx();
                    } else {
                        gdsbMsg = "归档失败！";
                    }
                } else {
                    gdsbMsg = "未找到项目!";
                }
            }
        }
        if (StringUtils.isNotBlank(gdsbMsg) && StringUtils.isBlank(gdcgMsg)) {
            msg = "归档失败!";
        } else if (StringUtils.isNotBlank(gdsbMsg) && StringUtils.isBlank(gdcgMsg)) {
            msg = "部分归档失败！";
        } else if (StringUtils.isNotBlank(gdcgMsg) && StringUtils.isBlank(gdsbMsg)) {
            msg = gdcgMsg;
        }
        return msg;
    }

    private BdcGdxx postInfoNew(BdcXm bdcXm, String userName) {
        String archiveUrl = AppConfig.getProperty("archive.url");
        String xml = "";
        String wfProid = PlatformUtil.getPfProidByWiid(bdcXm.getWiid());
        Document document = null;
        BdcGdxx bdcGdxx = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            String proid = bdcXm.getProid();
            //zdd 处理项目权利以及证书信息
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            String archiveName = bdcZdGlService.getArchiveNameBySqlx(bdcXm.getSqlx());
            String jsonStr = ReadJsonFileUtil.readJsonFile("conf/server/postBdcXmToArchive.json");
            List<JSONObject> jsonObjectList = new ArrayList<>();
            String sqlxdm = bdcXm.getSqlx();
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            if (StringUtils.isNotBlank(jsonStr)) {
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                if (jsonObject != null && jsonObject.containsKey(sqlxdm)) {
                    jsonObjectList = (List<JSONObject>) jsonObject.get(sqlxdm);
                }
                if (CollectionUtils.isEmpty(jsonObjectList) && jsonObject.containsKey(archiveName)) {
                    jsonObjectList = (List<JSONObject>) jsonObject.get(archiveName);
                }
            }

            if (StringUtils.isNotBlank(archiveName)) {
                String mlh = "";
                String zslx = "zs";
                if (CollectionUtils.isNotEmpty(bdcZsList) && StringUtils.equals(bdcZsList.get(0).getZstype(), Constants.BDCQZM_BH_FONT)) {
                    zslx = "zms";
                }
                Example example = new Example(BdcXtDamlh.class);
                example.createCriteria().andEqualTo("zslx", zslx).andEqualTo("bdclx", bdcXm.getBdclx());
                List<BdcXtDamlh> bdcXtDamlhList = entityMapper.selectByExample(BdcXtDamlh.class, example);
                if (CollectionUtils.isNotEmpty(bdcXtDamlhList)) {
                    mlh = bdcXtDamlhList.get(0).getMlh();
                }
                List<BdcSjcl> bdcSjclList = bdcSjclMapper.getSjclByproid(bdcXm.getProid());

                document = ArchiveModel.createFwbdcXmlNew(document, nodeService, bdcXtLimitfieldService, bdcXm, archiveName, mlh, bdcSjclList, wfProid, jsonObjectList, bdcQlrList);
                xml = document.asXML();
                bdcGdxx = bdcGdxxService.selectBdcGdxx(bdcXm);

                String gdInterface = archiveUrl + "/gateway.action";
                String gdUpdateInterface = archiveUrl + "/gateway!update.action";
                String updateGdSqlx = AppConfig.getProperty("updateGdSqlx");
                String[] gdUpdateSqlx = updateGdSqlx.split(",");
                if (StringUtils.isNotEmpty(xml)) {
                    CloseableHttpClient closeableHttpClient;
                    CloseableHttpResponse closeableHttpResponse = null;
                    if (!CommonUtil.indexOfStrs(gdUpdateSqlx, bdcXm.getSqlx()) && bdcGdxx == null || (bdcGdxx != null && StringUtils.isBlank(bdcGdxx.getDaid()))) {
                        try {
                            bdcGdxx = new BdcGdxx();
                            bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                            bdcGdxx.setXmid(bdcXm.getProid());
                            bdcGdxx.setGdrq(new Date());
                            bdcGdxx.setGdr(userName);
                            bdcGdxx.setModelname(archiveName);
                            closeableHttpClient = (CloseableHttpClient) httpClient;
                            HttpPost httpPost = new HttpPost(gdInterface);
                            List<NameValuePair> params = Lists.newArrayList();
                            params.add(new BasicNameValuePair("data", xml));
                            httpPost.setEntity(new UrlEncodedFormEntity(params, Charsets.CHARSET_UTF8));
                            closeableHttpResponse = closeableHttpClient.execute(httpPost);
                            if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                                String responseXml = EntityUtils.toString(closeableHttpResponse.getEntity());
                                if (StringUtils.isNotBlank(responseXml)) {
                                    bdcGdxx = bdcGdxxService.initBdcGdxx(bdcGdxx, responseXml);
                                }
                            }
                            bdcGdxxService.insertBdcGdxx(bdcGdxx);
                        } catch (SocketTimeoutException eTimeOut) {
                            logger.error(LOGSINGLEINFO, eTimeOut.getMessage());
                        } catch (IOException e) {
                            logger.error(LOGSINGLEINFO, e.getMessage());
                        } catch (Exception e) {
                            logger.error(LOGSINGLEINFO, e.getMessage());
                        } finally {
                            if (closeableHttpResponse != null) {
                                try {
                                    closeableHttpResponse.close();
                                } catch (IOException e) {
                                    logger.error(LOGSINGLEINFO, e.getMessage());
                                }
                            }
                        }
                    } else {
                        closeableHttpClient = (CloseableHttpClient) httpClient;
                        HttpPost httpPost = new HttpPost(gdUpdateInterface);
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("data", xml));
                        httpPost.setEntity(new UrlEncodedFormEntity(params, Charsets.CHARSET_UTF8));
                        try {
                            closeableHttpResponse = closeableHttpClient.execute(httpPost);
                        } catch (IOException e) {
                            logger.error(LOGSINGLEINFO, e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
                        }
                        if (closeableHttpResponse != null && closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                            try {
                                String responseXml = EntityUtils.toString(closeableHttpResponse.getEntity());
                                if (StringUtils.isNotBlank(responseXml)) {
                                    bdcGdxx = new BdcGdxx();
                                    bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                                    bdcGdxx.setXmid(bdcXm.getProid());
                                    bdcGdxx.setGdrq(new Date());
                                    bdcGdxx.setGdr(userName);
                                    bdcGdxx.setModelname(archiveName);
                                    bdcGdxx = bdcGdxxService.initBdcGdxx(bdcGdxx, responseXml);
                                    bdcGdxxService.insertBdcGdxx(bdcGdxx);
                                }
                            } catch (IOException e) {
                                logger.error(LOGSINGLEINFO, e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                }
            }
        }
        return bdcGdxx;
    }

    /**
     * 将不动产登记项目信息归档到档案系统，主要用于没用用户信息的自动归档
     *
     * @param bdcXm    不动产登记项目信息
     * @param userName 归档人
     * @return
     */
    @Override
    public BdcGdxx postBdcXmInfo(BdcXm bdcXm, String userName) {
        String archiveUrl = AppConfig.getProperty("archive.url");
        String cjr = "";
        String xml = "";
        String wfProid = PlatformUtil.getPfProidByWiid(bdcXm.getWiid());
        Document document = null;
        BdcGdxx bdcGdxx = null;
        String currentUserDwdm = "";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            String proid = bdcXm.getProid();

            //zdd 处理项目权利以及证书信息
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            /**
             * @author bianwen
             * @description 将spxx表中的代码转换为名称
             */
            if (bdcSpxx != null) {
                bdcSpxx = bdcZdGlService.changeDmToMc(bdcSpxx);
            }

            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);

            //zdd  根据权利类型找到对应的档案业务名称
            List<BdcZdQllx> bdcZdQllxList = bdcZdGlService.getBdcQllx();
            String archiveName = "";
            if (CollectionUtils.isNotEmpty(bdcZdQllxList)) {
                for (BdcZdQllx bdcZdQllx : bdcZdQllxList) {
                    if (bdcZdQllx.getDm().equals(bdcXm.getQllx())) {
                        archiveName = bdcZdQllx.getArchiveName();
                    }
                }
            }
            if (StringUtils.isNotBlank(archiveName)) {
                String mlh = "";
                String zslx = "zs";
                if (CollectionUtils.isNotEmpty(bdcZsList) && StringUtils.equals(bdcZsList.get(0).getZstype(), Constants.BDCQZM_BH_FONT)) {
                    zslx = "zms";
                }
                Example example = new Example(BdcXtDamlh.class);
                example.createCriteria().andEqualTo("zslx", zslx).andEqualTo("bdclx", bdcXm.getBdclx());
                List<BdcXtDamlh> bdcXtDamlhList = entityMapper.selectByExample(BdcXtDamlh.class, example);
                if (CollectionUtils.isNotEmpty(bdcXtDamlhList)) {
                    mlh = bdcXtDamlhList.get(0).getMlh();
                }
                List<BdcSjcl> bdcSjclList = bdcSjclMapper.getSjclByproid(bdcXm.getProid());
                /**
                 * @author bianwen
                 * @description bdcxm表中的qllx转换为名称
                 */
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getQllx())) {
                    BdcZdQllx bdcZdQllx = bdcZdQllxService.queryBdcZdQllxByDm(bdcXm.getQllx());
                    if (StringUtils.isNotBlank(bdcZdQllx.getMc())) {
                        bdcXm.setQllx(bdcZdQllx.getMc());
                    }
                }
                //吴江取xm创建人的username
                cjr = getCjrByBdcXm(bdcXm);
                currentUserDwdm = getCurrentCjrDwdm(cjr);
                document = ArchiveModel.createFwbdcXml(document, nodeService, bdcXm, qllxVo, bdcSpxx, bdcQlrList, bdcZsList, archiveName, mlh, bdcSjclList, wfProid, currentUserDwdm);
                xml = document.asXML();
                bdcGdxx = bdcGdxxService.selectBdcGdxx(bdcXm);

                String gdInterface = archiveUrl + "/gateway.action";
                String gdUpdateInterface = archiveUrl + "/gateway!update.action";
                if (StringUtils.isNotEmpty(xml)) {
                    CloseableHttpClient closeableHttpClient;
                    CloseableHttpResponse closeableHttpResponse = null;
                    if (bdcGdxx == null || (bdcGdxx != null && StringUtils.isBlank(bdcGdxx.getDaid()))) {
                        try {
                            bdcGdxx = new BdcGdxx();
                            bdcGdxx.setGdxxid(UUIDGenerator.generate18());
                            bdcGdxx.setXmid(bdcXm.getProid());
                            bdcGdxx.setGdrq(new Date());
                            bdcGdxx.setGdr(userName);
                            bdcGdxx.setModelname(archiveName);
                            closeableHttpClient = (CloseableHttpClient) httpClient;
                            HttpPost httpPost = new HttpPost(gdInterface);
                            List<NameValuePair> params = Lists.newArrayList();
                            params.add(new BasicNameValuePair("data", xml));
                            httpPost.setEntity(new UrlEncodedFormEntity(params, Charsets.CHARSET_UTF8));
                            closeableHttpResponse = closeableHttpClient.execute(httpPost);
                            if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                                String responseXml = EntityUtils.toString(closeableHttpResponse.getEntity());
                                if (StringUtils.isNotBlank(responseXml)) {
                                    bdcGdxx = bdcGdxxService.initBdcGdxx(bdcGdxx, responseXml);
                                }
                            }
                            bdcGdxxService.insertBdcGdxx(bdcGdxx);
                        } catch (SocketTimeoutException eTimeOut) {
                            logger.error(LOGSINGLEINFO, eTimeOut.getMessage());
                        } catch (IOException e) {
                            logger.error(LOGSINGLEINFO, e.getMessage());
                        } catch (Exception e) {
                            logger.error(LOGSINGLEINFO, e.getMessage());
                        } finally {
                            if (closeableHttpResponse != null) {
                                try {
                                    closeableHttpResponse.close();
                                } catch (IOException e) {
                                    logger.error(LOGSINGLEINFO, e.getMessage());
                                }
                            }
                        }
                    } else {
                        closeableHttpClient = (CloseableHttpClient) httpClient;
                        HttpPost httpPost = new HttpPost(gdUpdateInterface);
                        List<NameValuePair> params = Lists.newArrayList();
                        params.add(new BasicNameValuePair("data", xml));
                        httpPost.setEntity(new UrlEncodedFormEntity(params, Charsets.CHARSET_UTF8));
                        try {
                            closeableHttpResponse = closeableHttpClient.execute(httpPost);
                        } catch (IOException e) {
                            logger.error(LOGSINGLEINFO, e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
                        }
                        if (closeableHttpResponse != null && closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                            try {
                                String responseXml = EntityUtils.toString(closeableHttpResponse.getEntity());
                            } catch (IOException e) {
                                logger.error(LOGSINGLEINFO, e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                }
            }
        }
        return bdcGdxx;
    }

    /**
     * @param
     * @author bianwen
     * @rerutn
     * @description 批量归档
     */
    public List<BdcGdxx> postPlBdcXmInfo(List<BdcXm> bdcXmList, String userName) {
        Document document = null;
        String currentUserDwdm = "";
        String cjr = "";
        String wfProid = PlatformUtil.getPfProidByWiid(bdcXmList.get(0).getWiid());
        for (int i = 0; i < bdcXmList.size(); i++) {
            BdcXm bdcXm = bdcXmList.get(i);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                String proid = bdcXm.getProid();
                //zdd 处理项目权利以及证书信息
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                /**
                 * @author bianwen
                 * @description 将spxx表中的代码转换为名称
                 */
                if (bdcSpxx != null) {
                    bdcSpxx = bdcZdGlService.changeDmToMc(bdcSpxx);
                }
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);

                //zdd  根据权利类型找到对应的档案业务名称
                List<BdcZdQllx> bdcZdQllxList = bdcZdGlService.getBdcQllx();
                String archiveName = "";
                if (CollectionUtils.isNotEmpty(bdcZdQllxList)) {
                    for (BdcZdQllx bdcZdQllx : bdcZdQllxList) {
                        if (bdcZdQllx.getDm().equals(bdcXm.getQllx())) {
                            archiveName = bdcZdQllx.getArchiveName();
                        }
                    }
                }
                if (StringUtils.isNotBlank(archiveName)) {
                    String mlh = "";
                    String zslx = "zs";
                    if (CollectionUtils.isNotEmpty(bdcZsList) && StringUtils.equals(bdcZsList.get(0).getZstype(), Constants.BDCQZM_BH_FONT)) {
                        zslx = "zms";
                    }
                    Example example = new Example(BdcXtDamlh.class);
                    example.createCriteria().andEqualTo("zslx", zslx).andEqualTo("bdclx", bdcXm.getBdclx());
                    List<BdcXtDamlh> bdcXtDamlhList = entityMapper.selectByExample(BdcXtDamlh.class, example);
                    if (CollectionUtils.isNotEmpty(bdcXtDamlhList)) {
                        mlh = bdcXtDamlhList.get(0).getMlh();
                    }

                    List<BdcSjcl> bdcSjclList = bdcSjclMapper.getSjclByproid(bdcXm.getProid());
                    if (CollectionUtils.isEmpty(bdcSjclList)) {
                        bdcSjclList = bdcSjclMapper.getSjclByproid(wfProid);
                    }
                    /**
                     * @author bianwen
                     * @description bdcxm表中的qllx转换为名称
                     */
                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getQllx())) {
                        BdcZdQllx bdcZdQllx = bdcZdQllxService.queryBdcZdQllxByDm(bdcXm.getQllx());
                        if (StringUtils.isNotBlank(bdcZdQllx.getMc())) {
                            bdcXm.setQllx(bdcZdQllx.getMc());
                        }
                    }

                    //吴江取xm创建人的username
                    cjr = getCjrByBdcXm(bdcXm);
                    currentUserDwdm = getCurrentCjrDwdm(cjr);
                    document = ArchiveModel.createFwbdcXml(document, nodeService, bdcXm, qllxVo, bdcSpxx, bdcQlrList, bdcZsList, archiveName, mlh, bdcSjclList, wfProid, currentUserDwdm);
                }
            }

        }
        List<BdcGdxx> bdcGdxxList = null;
        if (document != null) {
            String xml = document.asXML();
            bdcGdxxList = archiveInterfaceCallPl(xml, userName);
        }
        return bdcGdxxList;
    }

    public List<BdcGdxx> archiveInterfaceCallPl(String xml, String userName) {
        String archiveUrl = AppConfig.getProperty("archive.url");
        List<BdcGdxx> bdcGdxxList = new ArrayList<BdcGdxx>();
        CloseableHttpClient closeableHttpClient;
        CloseableHttpResponse closeableHttpResponse = null;

        String gdInterface = archiveUrl + "/gateway.action";
        if (StringUtils.isNotBlank(gdInterface) && StringUtils.isNotBlank(xml)) {
            try {
                closeableHttpClient = (CloseableHttpClient) httpClient;
                HttpPost httpPost = new HttpPost(gdInterface);
                List<NameValuePair> params = Lists.newArrayList();
                params.add(new BasicNameValuePair("data", xml));
                httpPost.setEntity(new UrlEncodedFormEntity(params, Charsets.CHARSET_UTF8));
                closeableHttpResponse = closeableHttpClient.execute(httpPost);
                if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                    String responseXml = EntityUtils.toString(closeableHttpResponse.getEntity());
                    if (StringUtils.isNotBlank(responseXml)) {
                        bdcGdxxList = bdcGdxxService.initPlBdcGdxx(responseXml);
                    }
                }
                if (CollectionUtils.isNotEmpty(bdcGdxxList)) {
                    for (int i = 0; i < bdcGdxxList.size(); i++) {
                        BdcGdxx bdcGdxx = bdcGdxxList.get(i);
                        bdcGdxx.setGdr(userName);
                        bdcGdxxService.insertBdcGdxx(bdcGdxx);
                    }
                }
            } catch (SocketTimeoutException eTimeOut) {
                logger.error(LOGPLINFO, eTimeOut.getMessage());
            } catch (IOException e) {
                logger.error(LOGPLINFO, e.getMessage());
            } catch (Exception e) {
                logger.error(LOGPLINFO, e.getMessage());
            } finally {
                if (closeableHttpResponse != null) {
                    try {
                        closeableHttpResponse.close();
                    } catch (IOException e) {
                        logger.error(LOGPLINFO, e.getMessage());
                    }
                }
            }
        }
        return bdcGdxxList;
    }


    /**
     * @param username
     * @author <a herf="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据当前用户获取对应的单位代码，用于获取其对应的档案目录号
     */
    public String getCurrentCjrDwdm(String username) {
        String currentDwdm = "";
        if (StringUtils.isNotBlank(username)) {
            List<PfUserVo> userVoList = sysUserService.getUserVoListByUserName(username);
            if (CollectionUtils.isNotEmpty(userVoList)) {
                PfUserVo userVo = userVoList.get(0);
                if (null != userVo && StringUtils.isNotBlank(userVo.getUserId())) {
                    List<PfOrganVo> pfOrganVoList = sysUserService.getOrganListByUser(userVo.getUserId());
                    if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
                        PfOrganVo pfOrganVo = pfOrganVoList.get(0);
                        if (null != pfOrganVo && StringUtils.isNotBlank(pfOrganVo.getRegionCode())) {
                            //江阴取创建用户的organ_code
                            currentDwdm = pfOrganVo.getRegionCode();
                        }
                    }
                }
            }
        }
        return currentDwdm;
    }

    private String getCjrByBdcXm(BdcXm bdcXm) {
        String cjr = "";
        if (null != bdcXm) {
            if (StringUtils.isNotBlank(bdcXm.getCjr())) {
                cjr = bdcXm.getCjr();
            } else {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getCreateUser())) {
                    PfUserVo pfUserVo = sysUserService.getUserVo(pfWorkFlowInstanceVo.getCreateUser());
                    if (null != pfUserVo && StringUtils.isNotBlank(pfUserVo.getUserName())) {
                        cjr = pfUserVo.getUserName();
                    }
                }
            }
        }
        return cjr;
    }

}
