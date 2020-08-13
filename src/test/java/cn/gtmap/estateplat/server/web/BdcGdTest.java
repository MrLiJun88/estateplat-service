package cn.gtmap.estateplat.server.web;

import cn.gtmap.estateplat.core.support.xml.XmlUtils;
import cn.gtmap.estateplat.model.exchange.transition.QzHead;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.model.FoldersModel;
import cn.gtmap.estateplat.server.web.main.CreateSjdclController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.plat.service.SysCalendarService;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/8
 * @description 查封服务单元测试用例
 */
public class BdcGdTest extends BdcBaseUnitTest {
    @Autowired
    SysCalendarService sysCalendarService;

    @Test
    public void getBdcCflxMc(){
        Date lzrq = sysCalendarService.getOverTime(new Date(), "5");
        System.out.println(CalendarUtil.formatDateToString(lzrq));
//        CreateSjdclController createSjdcl=new CreateSjdclController();
//        String xml="<folders>\n" +
//                "  <sjbh>20160506103809384</sjbh>\n" +
//                "  <folder>\n" +
//                "    <id>1001</id>\n" +
//                "    <name>询问笔录</name>\n" +
//                "    <files>\n" +
//                "      <file>\n" +
//                "        <id>201604261514428391604280001</id>\n" +
//                "        <name>201604261514428391604280001.jpg</name>\n" +
//                "        <url>http://192.168.0.64/iis_image/2016/04/28/2016042802010063/201604261514428391604280001.jpg</url>\n" +
//                "      </file>\n" +
//                "    </files>\n" +
//                "  </folder>\n" +
//                "</folders>";
////        createSjdcl.gdFile(xml);
//        XmlUtils xmlUtils=new XmlUtils(FoldersModel.class);
//        FoldersModel foldersModel=(FoldersModel) xmlUtils.fromXml(xml, false);
//        System.out.println(foldersModel);


//        XmlUtils<FoldersModel> xmlUtils=new XmlUtils();
//        FoldersModel foldersModel= xmlUtils.fromXml(xml, false);
//        gdController.gdFile(null,xml);
    }

    /**
     * @description
     * 		向服务器端发送请求报文
     * @return
     * @author sunjianwu
     * @throws IOException
     * @date 2016年5月3日 上午12:22:00
     */
//    @Test
//    public void sendRequest() throws IOException {
//        String message= "";
//        CloseableHttpClient httpclient = null;
//        CloseableHttpResponse httpResponse = null;
//        //请求地址
//        try {
//            String  url="http://192.168.0.126:8080/estateplat-server/createSjdcl/uploadSjcl";
//            String xmlData="<folders>\n" +
//                    "  <sjbh>20160506103809384</sjbh>\n" +
//                    "  <folder>\n" +
//                    "    <id>1001</id>\n" +
//                    "    <name>询问笔录</name>\n" +
//                    "    <files>\n" +
//                    "      <file>\n" +
//                    "        <id>201604261514428391604280001</id>\n" +
//                    "        <name>bg.png</name>\n" +
//                    "        <url>http://192.168.0.126:8080/estateplat-server/static/img/bg.png</url>\n" +
//                    "      </file>\n" +
//                    "    </files>\n" +
//                    "  </folder>\n" +
//                    "</folders>";
//            //post请求
//            HttpPost httpPost = new HttpPost(url);
//            //设置字符集编码
//            ContentType contentType = ContentType.create(ContentType.TEXT_XML.getMimeType(), "UTF-8");
//            //封装xml报文
//            StringEntity stringEntity = new StringEntity(URLEncoder.encode(xmlData, "UTF-8")  ,contentType);
//
//            httpPost.setEntity(stringEntity);
//            //创建httpclient请求对象
//            httpclient = HttpClients.createDefault();
//            //执行请求信息，返回响应对象
//            httpResponse = httpclient.execute(httpPost);
//            //获取服务器端返回的状态码
//            int code = httpResponse.getStatusLine().getStatusCode();
//            if (code == HttpStatus.SC_OK){
//                if(httpResponse.getEntity() !=null){
//                    message = EntityUtils.toString(httpResponse.getEntity());
//                }
//            }
//        } catch (ClientProtocolException e) {
//        } catch (IOException e) {
//        }finally{
//            if(httpResponse!=null)
//                httpResponse.close();
//            if(httpclient !=null)
//                httpclient.close();
//        }
//    }

}
