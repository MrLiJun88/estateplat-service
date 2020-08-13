package cn.gtmap.estateplat.server.utils;

import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2018/12/17
 * @description
 */
public class CxmlUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CxmlUtils.class);
    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param obj
     * @return
     * @description将java实体类转换成xml格式
     */
    public static String beanConvertXml(Object obj) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
    public static String listToXmlstr(List<Map> list) {
        Document document = listToDocument(list);
        LOGGER.debug(document.asXML());
        return document.asXML();
    }
    public static String listToXmlFile(List<Map> list,String fileName) {
        String currentMonth= CalendarUtil.getDateMonth(new Date());
        String egovData= StringUtils.replace(AppConfig.getDataHome(),"file:/","")+ File.separator+"estateplat-ha"+File.separator+currentMonth;
        File rootFile=new File(egovData);
        if(!rootFile.exists()){
            rootFile.mkdirs();
        }
        String xmlFilePath =egovData+File.separator+fileName+".xml";
        Document document = listToDocument(list);
        //输出xml的路径
        File xmlFile = new File(xmlFilePath);
        if(!xmlFile.exists()) {
            XMLWriter out = null;
            try {
                //对xml输出格式化
                FileOutputStream fos = new FileOutputStream(xmlFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("UTF-8");
                out = new XMLWriter(bw, format);
                out.write(document);
            } catch (IOException e) {
                LOGGER.error("", e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("", e);
                }
            }
        }
        return xmlFile.getPath();
    }

    private static Document listToDocument(List<Map> list) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element archivesElement = document.addElement("xml");
        for (Map map : list) {
            //添加第一个档案节点
            Element archiveElement = archivesElement.addElement("datas");
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Element fieldtm = archiveElement.addElement(String.valueOf(entry.getKey()).toLowerCase());
                String value=entry.getValue()==null?"":String.valueOf(entry.getValue());
                fieldtm.addCDATA(StringUtils.defaultString(value,""));
            }
        }
        return document;
    }
}
