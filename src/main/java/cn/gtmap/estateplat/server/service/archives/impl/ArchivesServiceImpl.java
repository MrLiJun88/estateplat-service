package cn.gtmap.estateplat.server.service.archives.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.service.archives.ArchivesService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.fr.base.FRContext;
import com.fr.dav.LocalEnv;
import com.fr.general.ModuleContext;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.ImageExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.report.ReportHelper;
import com.fr.report.module.EngineModule;
import com.fr.stable.WriteActor;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.vo.PfSignVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/19
 * @description
 */
@Service
public class ArchivesServiceImpl implements ArchivesService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private SysSignService sysSignService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PARAMATER_BLRMC = "办理人名称";
    private static final String PARAMATER_BLRYJ = "办理人意见";
    private static final String PARAMETER_ERROR_PUSHARCHIVESSPBXMLFILE = "ArchivesServiceImpl.pushArchivesSpbXmlFile";

    @Async
    @Override
    public void pushArchivesFile(String proid,String userid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        pushArchivesSpbJpgFile(bdcXm, userid);
        pushArchivesSpbXmlFile(bdcXm);
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @param userid
     * @rerutn
     * @description 向激扬档案推送审批表jpg图片文件
     */
    void pushArchivesSpbJpgFile(BdcXm bdcXm, String userid) {
        String reportEnvpath = StringUtils.deleteWhitespace(AppConfig.getProperty("report.envpath"));
        String reportSpbCptPath = StringUtils.deleteWhitespace(AppConfig.getProperty("report.spb.cpt.path"));
        String pathName = StringUtils.deleteWhitespace(AppConfig.getProperty("spb.output.path"));
        if(bdcXm != null && StringUtils.isNotBlank(reportEnvpath) && StringUtils.isNotBlank(reportSpbCptPath)
                && StringUtils.isNotBlank(pathName))  {
            FileOutputStream outputStream = null;
            try {
                FRContext.setCurrentEnv(new LocalEnv(reportEnvpath));
                ModuleContext.startModule(EngineModule.class.getName());
                TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), reportSpbCptPath);
                //设置模版参数
                java.util.Map<String, String> parameterMap = new java.util.HashMap<String, String>();
                // 帆软中非数据集读取的数据需要重新组织传入
                HashMap<String,String> additionalMap = bdcSpxxService.getArchiveAdditionalInfo(bdcXm);
                String platFormUrl = AppConfig.getPlatFormUrl();
                parameterMap.put("tdOrTdfwMj", CommonUtil.formatEmptyValue(additionalMap.get("tdOrTdfwMj")));
                parameterMap.put("tdOrTdfwYt", CommonUtil.formatEmptyValue(additionalMap.get("tdOrTdfwYt")));
                parameterMap.put("tdOrTdfwQlxz", CommonUtil.formatEmptyValue(additionalMap.get("tdOrTdfwQlxz")));
                parameterMap.put("platFormUrl",platFormUrl);
                parameterMap.put("proid", bdcXm.getProid());
                parameterMap.put("userid", userid);
                ReportHelper.clearFormulaResult(workbook);
                pathName += bdcXm.getProid() + ".jpg";
                outputStream = new FileOutputStream(new File(pathName));
                ImageExporter imageExport = new ImageExporter();
                imageExport.export(outputStream, workbook.execute(parameterMap,new WriteActor()));
                ModuleContext.stopModules();
            } catch (Exception e) {
                logger.error("ArchivesServiceImpl.pushArchivesSpbJpgFile",e);
            }finally {
                try {
                    if(outputStream != null){
                        outputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("ArchivesServiceImpl.pushArchivesSpbJpgFile",e);
                }
            }
            //调用激扬档案接口推送审批表jpg文件
            if(StringUtils.isNotBlank(pathName)) {
                beginPushSpbJpg(new File(pathName),bdcXm.getBh());
            }
        }
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param spbjpgFile
     * @param sjdh
     * @rerutn
     * @description 开始向激扬档案上传审批表jpg图片文件
     */
    void beginPushSpbJpg(File spbjpgFile, String sjdh){
        String uploadSpbUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("uploadfile.spbjpg.url"));
        if(StringUtils.isNotBlank(uploadSpbUrl) && StringUtils.isNotBlank(sjdh)) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpPost httppost = new HttpPost(uploadSpbUrl);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                multipartEntityBuilder.setCharset(Charset.forName(Constants.DEFAULT_CHARSET));
                multipartEntityBuilder.addTextBody("sjdh", sjdh);
                multipartEntityBuilder.addBinaryBody("filename", spbjpgFile);

                HttpEntity httpEntity = multipartEntityBuilder.build();
                httppost.setEntity(httpEntity);
                CloseableHttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    String result = EntityUtils.toString(resEntity, Constants.DEFAULT_CHARSET);
                    if(StringUtils.equals(result,"success")){
                        logger.info("推送审批表jpg图片文件成功！");
                    }else {
                        logger.info("推送审批表jpg图片文件失败！");
                    }
                }
                EntityUtils.consume(resEntity);
                response.close();
            } catch (IOException e) {
                logger.error("ArchivesServiceImpl.beginPushSpbJpg",e);
            }finally {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    logger.error("ArchivesServiceImpl.beginPushSpbJpg",e);
                }
            }
        }
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @rerutn
     * @description 向激扬档案推送审批表xml文件
     */
    void pushArchivesSpbXmlFile(BdcXm bdcXm) {
        String pathName = StringUtils.deleteWhitespace(AppConfig.getProperty("spb.output.path"));
        if(StringUtils.isNotBlank(pathName) && bdcXm != null)  {
            StringBuilder qlrmc = new StringBuilder();
            String tdzl = "";
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
            if(bdcSpxx != null) {
               tdzl = bdcSpxx.getZl();
            }
            List<BdcQlr> bdcqlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
            if(CollectionUtils.isNotEmpty(bdcqlrList)) {
               for(BdcQlr bdcqlr:bdcqlrList) {
                   if(StringUtils.isNotBlank(bdcqlr.getQlrmc())) {
                       if(StringUtils.isBlank(qlrmc)) {
                           qlrmc.append(bdcqlr.getQlrmc());
                       } else{
                           qlrmc.append(",").append(bdcqlr.getQlrmc());
                       }
                   }
               }
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                if(builder != null){
                    Document document = builder.newDocument();
                    document.setXmlVersion("1.0");
                    Element root = document.createElement("不动产电子归档内容");
                    document.appendChild(root);
                    Element ywxxElement = document.createElement("业务信息");
                    Element qlrmcElement = document.createElement("权利人名称");
                    qlrmcElement.setTextContent(String.valueOf(qlrmc));
                    ywxxElement.appendChild(qlrmcElement);
                    Element tdzlElement = document.createElement("土地坐落");
                    tdzlElement.setTextContent(tdzl);
                    ywxxElement.appendChild(tdzlElement);

                    root.appendChild(ywxxElement);
                    Element lcspxx = document.createElement("流程审批信息");

                    List<PfSignVo> csrSignList = sysSignService.getSignList("csr", bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(csrSignList)) {
                        PfSignVo csrSign = csrSignList.get(0);
                        Element lcjd = document.createElement("流程节点");
                        Element jdmc = document.createElement("节点名称");
                        jdmc.setTextContent("受理（初审）");
                        Element blrmc = document.createElement(PARAMATER_BLRMC);
                        blrmc.setTextContent(csrSign.getSignName());
                        Element blryj = document.createElement(PARAMATER_BLRYJ);
                        blryj.setTextContent(csrSign.getSignOpinion());
                        Element sj = document.createElement("时间");
                        sj.setTextContent(CalendarUtil.formatDateToString(csrSign.getSignDate()));

                        lcjd.appendChild(blrmc);
                        lcjd.appendChild(jdmc);
                        lcjd.appendChild(blryj);
                        lcjd.appendChild(sj);
                        lcspxx.appendChild(lcjd);
                    }

                    List<PfSignVo> fsrSignList = sysSignService.getSignList("fsr", bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(fsrSignList)) {
                        PfSignVo fsrSign = fsrSignList.get(0);

                        Element lcjd = document.createElement("流程节点");
                        Element jdmc = document.createElement("节点名称");
                        jdmc.setTextContent("复审");
                        Element blrmc = document.createElement(PARAMATER_BLRMC);
                        blrmc.setTextContent(fsrSign.getSignName());
                        Element blryj = document.createElement(PARAMATER_BLRYJ);
                        blryj.setTextContent(fsrSign.getSignOpinion());
                        Element sj = document.createElement("时间");
                        sj.setTextContent(CalendarUtil.formatDateToString(fsrSign.getSignDate()));

                        lcjd.appendChild(blrmc);
                        lcjd.appendChild(jdmc);
                        lcjd.appendChild(blryj);
                        lcjd.appendChild(sj);
                        lcspxx.appendChild(lcjd);
                    }

                    List<PfSignVo> hdrSignList = sysSignService.getSignList("hdr", bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(hdrSignList)) {
                        PfSignVo hdrSign = hdrSignList.get(0);

                        Element lcjd = document.createElement("流程节点");
                        Element jdmc = document.createElement("节点名称");
                        jdmc.setTextContent("核定");
                        Element blrmc = document.createElement(PARAMATER_BLRMC);
                        blrmc.setTextContent(hdrSign.getSignName());
                        Element blryj = document.createElement(PARAMATER_BLRYJ);
                        blryj.setTextContent(hdrSign.getSignOpinion());
                        Element sj = document.createElement("时间");
                        sj.setTextContent(CalendarUtil.formatDateToString(hdrSign.getSignDate()));

                        lcjd.appendChild(blrmc);
                        lcjd.appendChild(jdmc);
                        lcjd.appendChild(blryj);
                        lcjd.appendChild(sj);
                        lcspxx.appendChild(lcjd);
                    }
                    root.appendChild(lcspxx);
                    TransformerFactory transFactory = TransformerFactory.newInstance();
                    Transformer transFormer = transFactory.newTransformer();
                    DOMSource domSource = new DOMSource(document);

                    //export string
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if(transFormer != null){
                        transFormer.transform(domSource, new StreamResult(bos));
                    }
                    logger.info("审批表xml：" + bos.toString());

                    //save as file
                    File spbxmlFile = new File(pathName + bdcXm.getProid() + ".xml");
                    if(!spbxmlFile.exists()){
                        spbxmlFile.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(spbxmlFile);
                    StreamResult xmlResult = new StreamResult(out);
                    if(transFormer != null) {
                        transFormer.transform(domSource, xmlResult);
                    }
                    //开始向激扬档案上传审批表xml文件
                    beginPushSpbXml(spbxmlFile,bdcXm.getBh());
                }
            } catch (TransformerConfigurationException e) {
                logger.error(PARAMETER_ERROR_PUSHARCHIVESSPBXMLFILE,e);
            }catch (TransformerException e) {
                logger.error(PARAMETER_ERROR_PUSHARCHIVESSPBXMLFILE,e);
            }catch (IOException e) {
                logger.error(PARAMETER_ERROR_PUSHARCHIVESSPBXMLFILE,e);
            }catch (ParserConfigurationException e) {
                logger.error(PARAMETER_ERROR_PUSHARCHIVESSPBXMLFILE,e);
            }
        }
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param spbxmlFile
     * @param sjdh
     * @rerutn
     * @description 开始向激扬档案上传审批表xml文件
     */
    void beginPushSpbXml(File spbxmlFile, String sjdh){
        String uploadSpbUrl = StringUtils.deleteWhitespace(AppConfig.getProperty("uploadfile.spbxml.url"));
        if(StringUtils.isNotBlank(uploadSpbUrl) && StringUtils.isNotBlank(sjdh)) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpPost httppost = new HttpPost(uploadSpbUrl);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                multipartEntityBuilder.setCharset(Charset.forName(Constants.DEFAULT_CHARSET));
                multipartEntityBuilder.addTextBody("sjdh", sjdh);
                multipartEntityBuilder.addBinaryBody("filename", spbxmlFile);

                HttpEntity httpEntity = multipartEntityBuilder.build();
                httppost.setEntity(httpEntity);
                CloseableHttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    String result = EntityUtils.toString(resEntity, Constants.DEFAULT_CHARSET);
                    if(StringUtils.equals(result,"success")){
                        logger.error("推送审批表xml文件成功！");
                    }else {
                        logger.error("推送审批表xml文件失败！");
                    }
                }
                EntityUtils.consume(resEntity);
                response.close();
            } catch (IOException e) {
                logger.error("ArchivesServiceImpl.beginPushSpbXml",e);
            }finally {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    logger.error("ArchivesServiceImpl.beginPushSpbXml",e);
                }
            }
        }
    }

}
