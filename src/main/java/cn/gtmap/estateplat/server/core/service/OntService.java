package cn.gtmap.estateplat.server.core.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @description 外网收件服务类
 */
public interface OntService {

    List<Object> getExcelAsInputStream(InputStream inputStream, String proid,String bdcdyh,String sjh);

    void initOntFile(String wiid,String proid);

    List<Map<String,String>> getOntBdcXmInfoByExcel(InputStream inputStream,String saveFileDir,String id);

    String appendPathAndDeleteFile(String fileName,String type);
}
