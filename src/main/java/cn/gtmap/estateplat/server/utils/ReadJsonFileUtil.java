package cn.gtmap.estateplat.server.utils;

import com.gtis.config.AppConfig;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
 * @version 1.0, 2019/2/25
 * @description
 */
public class ReadJsonFileUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReadJsonFileUtil.class);
    public static String readJsonFile(String jsonPath){
        String filePath = AppConfig.getEgovHome() + jsonPath;
        String removeStr = "file:/";
        filePath = filePath.replace(removeStr,"");
        File dir = new File(filePath);
        if (!dir.exists()){
            logger.error("jsonFile not found");
        }
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(dir);
            InputStreamReader isr = new InputStreamReader(fis,ParamsConstants.DEFAULT_CHARSET_UTF8);
            BufferedReader br = new BufferedReader(isr);
            String str = null;
            while ((str = br.readLine())!= null){
                sb.append(str);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            logger.error("ReadJsonFileUtil.readJsonFile",e);
        }
        return sb.toString();
    }
}
