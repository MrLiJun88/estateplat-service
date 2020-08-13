package cn.gtmap.estateplat.server.utils;

import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by Mr.GG on 2017/11/7 0007.
 */
public class UrlConnectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(UrlConnectionUtil.class);
    /**
     * URLConnection调用webservices
     * @param wsUrl webservices服务地址
     * @param soapReq 请求报文体
     * @return
     */
    public static String getInfoByUrlConn(URL wsUrl, String soapReq){
        String respBody = "";
        OutputStream os = null;
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();
            if(conn != null) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
                //增加请求完毕后关闭链接的头信息
                conn.setRequestProperty("Connection", "close");
                os = conn.getOutputStream();
                os.write(soapReq.getBytes("UTF-8"));
                is = conn.getInputStream();
                byte[] b = new byte[1024];
                int len = 0;
                while( (len = is.read(b))!=-1 ){
                    String ss = new String(b,0,len,"UTF-8");
                    respBody += ss;
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            logger.error("创建URL失败！",e);
        } catch (IOException e) {
            logger.error("URL打开失败！",e);
        }finally {
            try {
                if(os != null) {
                    os.close();
                    os = null;
                }
                if(is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                logger.error("关闭流失败！",e);
            }
        }
        return respBody;
    }

    /**
     * URLConnection调用webservices  这里因为继承平台的问题，单独写一个函数，用gbk编码
     * @param wsUrl webservices服务地址
     * @param soapReq 请求报文体
     * @return
     */
    public static String getInfoByUrlConnWithGbkCode(URL wsUrl, String soapReq){
        String respBody = "";
        OutputStream os = null;
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();
            if(conn != null) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
                //增加请求完毕后关闭链接的头信息
                conn.setRequestProperty("Connection", "close");
                os = conn.getOutputStream();
                os.write(soapReq.getBytes("UTF-8"));
                is = conn.getInputStream();
                byte[] b = new byte[1024];
                int len = 0;
                while( (len = is.read(b))!=-1 ){
                    String ss = new String(b,0,len,"GBK");
                    respBody += ss;
                }
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            logger.error("创建URL失败！",e);
        } catch (IOException e) {
            logger.error("URL打开失败！",e);
        }finally {
            try {
                if(is != null) {
                    is.close();
                    is = null;
                }
               if(os != null) {
                   os.close();
                   os = null;
               }
            } catch (IOException e) {
                logger.error("关闭流失败！",e);
            }
        }
        return respBody;
    }

    /**
     * 递归方法，查找本节点是否有标记信息，如果没有就查找下一层，
     * 在下一层里同样查找本层节点，只要找到值，就层层返回。
     * @param node 节点标签名
     * @param attr 节点属性值
     * @param el 当前节点对象
     * @return 目标值
     */
    public static String nextSubElement(String node, String attr, Element el) {
        if (el.getName().equals(node)) {
            //说明 找到了目标节点
            //属性值为空说明取标签内容
            if (attr.equals("")) {
                Iterator sub2 = el.elementIterator();
                //有子节点说明标签内容不是单一值，需要拿到查询结果
                if (sub2.hasNext()) {
                    while (sub2.hasNext()) {
                        Element s2 = (Element) sub2.next();
                    }
                } else {
                    return  el.getText();
                }
            } else {
                Attribute attrbute = el.attribute(attr);
                return attrbute.getText();
            }
        } else {
            Iterator sub2 = el.elementIterator();
            while (sub2.hasNext()) {
                Element sub = (Element) sub2.next();
                return nextSubElement(node, attr, sub);
            }
        }
        return "";
    }

    public static String getRespBody( String url, String soapReq, String eleName){
        String jsonResp = "";
        try{
            URL wsurl = new URL(url);
            String respBody = UrlConnectionUtil.getInfoByUrlConn(wsurl,soapReq);
            Document document= DocumentHelper.parseText(respBody);
            Element rootElt = document.getRootElement(); // 获取根节点
            Iterator body = rootElt.elementIterator("Body");
            while (body.hasNext()) {
                Element recordResult = (Element) body.next();
                jsonResp = UrlConnectionUtil.nextSubElement(eleName,"",recordResult);
            }
        }catch (MalformedURLException e){
            logger.error("UrlConnectionUtil.getRespBody");
        }catch (DocumentException e){
            logger.error("UrlConnectionUtil.getRespBody");
        }
        return jsonResp;
    }


    public static String getJcptRespBody( String url, String soapReq, String eleName){
        String jsonResp = "";
        try{
            URL wsurl = new URL(url);
            String respBody = UrlConnectionUtil.getInfoByUrlConnWithGbkCode(wsurl,soapReq);
            Document document= DocumentHelper.parseText(respBody);
            Element rootElt = document.getRootElement(); // 获取根节点
            Iterator body = rootElt.elementIterator("Body");
            while (body.hasNext()) {
                Element recordResult = (Element) body.next();
                jsonResp = UrlConnectionUtil.nextSubElement(eleName,"",recordResult);
            }
        }catch (MalformedURLException e){
            logger.error("UrlConnectionUtil.getJcptRespBody");
        }catch (DocumentException e){
            logger.error("UrlConnectionUtil.getJcptRespBody");
        }
        return jsonResp;
    }
}
