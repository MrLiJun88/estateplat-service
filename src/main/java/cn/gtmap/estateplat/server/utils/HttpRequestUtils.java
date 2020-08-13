package cn.gtmap.estateplat.server.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @description Http请求工具类
 */
public class HttpRequestUtils {
    protected static final Log log = LogFactory.getLog(HttpRequestUtils.class);
    private static final String HTTP_LOG_INFO = "HttpRequestUtils.sendPost";

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        log.error("请求地址: " + url);
        log.error("请求参数: " + param);
        OutputStreamWriter  out = null;
        BufferedReader in = null;
        String result = "";
        if (StringUtils.isNotBlank(url)) {
            try {
                URL realUrl = new URL(url);
                // 打开和URL之间的连接
                URLConnection connection = realUrl.openConnection();
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");


                // 发送POST请求必须设置如下两行
                connection.setDoOutput(true);
                connection.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
                // 发送请求参数
                out.write(param);
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                log.error(HTTP_LOG_INFO, e);
            } finally {
                // 使用finally块来关闭输出流、输入流
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ex) {
                    log.error(HTTP_LOG_INFO, ex);
                }
            }
        }
        log.error("请求返回值： " + result);
        return result;
    }


    public static String sendGet(String url, String param, String charsetName) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url;
            if(StringUtils.isNotBlank(param)){
                urlName = url + "?" + param;
            }
            URL realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), charsetName));
            String line;
            while ((line = in.readLine()) != null) {
                if (StringUtils.isNotBlank(result)) {
                    result += "/n" + line;
                } else {
                    result += line;
                }

            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


}
