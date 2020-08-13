package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.mapper.WfRecvImgMapper;
import cn.gtmap.estateplat.server.core.service.EtlFcFjPicToBdcService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-5-5
 * Time: 下午9:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EtlFcFjPicToBdcServiceImpl implements EtlFcFjPicToBdcService {
    private static final Log log = LogFactory.getLog(EtlFcFjPicToBdcServiceImpl.class);

    @Autowired
    private WfRecvImgMapper wfRecvimgMapper;

    @Override
    public byte[] getImage(String filePath, String fileName) {
        String basePath = AppConfig.getProperty("base.path");
        if (StringUtils.isNotBlank(basePath)) {
            String path = basePath + "/" + filePath + "/" + fileName + ".jpg";
            FileInputStream fileIs = null;
            try {
                fileIs = new FileInputStream(path);
                int i = 0; //得到文件大小
                if (fileIs != null) {
                    i = fileIs.available();
                    byte data[] = new byte[i];
                    fileIs.read(data);  //读数据
                    fileIs.close();
                    return data;
                }
            } catch (Exception e) {
                if(fileIs!=null)
                    try {
                        fileIs.close();
                    } catch (IOException e1) {
                        log.error("EtlFcFjPicToBdcServiceImpl.getImage",e);
                    }
                log.error("系统找不到图像文件："+path);
            }
        }
        return null;
    }

    @Override
    public byte[] getTdImage(String filePath, String fileName) {
        String baseTdPath = AppConfig.getProperty("baseTd.path");
        if (StringUtils.isNotBlank(baseTdPath)) {
            String path = baseTdPath + "\\" + filePath;
            FileInputStream fileIs = null;
            try {
                fileIs = new FileInputStream(path);
                int i = 0; //得到文件大小
                if (fileIs != null) {
                    i = fileIs.available();
                    byte data[] = new byte[i];
                    fileIs.read(data);  //读数据
                    fileIs.close();
                    return data;
                }
            } catch (Exception e) {
                log.error("系统找不到图像文件："+path);
            }
        }
        return null;
    }


    @Override
    public List<HashMap> getImageRoute(String syqid) {
        return wfRecvimgMapper.getImageRoute(syqid);
    }

    @Override
    public List<HashMap> getTdImageRoute(String projectid){
        return  wfRecvimgMapper.getTdImageRoute(projectid);
    }

    @Override
    public List<HashMap> getfcywid(String syqid) {
        return wfRecvimgMapper.getFcywid(syqid);
    }

}
