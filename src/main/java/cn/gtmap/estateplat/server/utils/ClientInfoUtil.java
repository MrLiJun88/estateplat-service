package cn.gtmap.estateplat.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2016/11/2
 * @description 获取客户端的信息
 */
public class ClientInfoUtil {
    private static final String PARAMETER_UNKNOWN = "unknown";
    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description
     */
    private ClientInfoUtil(){

    }

    //得到客户端IP地址
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || PARAMETER_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || PARAMETER_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || PARAMETER_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || PARAMETER_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || PARAMETER_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    //得到客户端MAC地址
    public static String getMACAddress(String ip) {
        String str = "";
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            LineNumberReader input = new LineNumberReader(new InputStreamReader(p.getInputStream(),"GBK"));
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null&&str.indexOf("MAC") > 1) {
                    macAddress = str.substring(str.indexOf('=') + 2, str.length());
                    break;
                }
            }
        } catch (IOException e) {
            logger.error("ClientInfoUtil.getMACAddress",e);
        }
        return macAddress;
    }

    //得到客户端计算机名
    public static String getComputerName(String ip){
        String computerName = "";
        String str = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            LineNumberReader input = new LineNumberReader(new InputStreamReader(p.getInputStream(),"GBK"));
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    str=str.trim().replaceAll("\\s{1,}", "");
                    if (str.indexOf('<') > 1 ) {
                        computerName = str.substring(0, str.indexOf('<'));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("ClientInfoUtil.getComputerName",e);
        }
        return computerName;
    }
}
