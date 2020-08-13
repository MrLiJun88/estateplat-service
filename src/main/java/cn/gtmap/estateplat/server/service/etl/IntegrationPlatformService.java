package cn.gtmap.estateplat.server.service.etl;

import com.alibaba.fastjson.JSONObject;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/1/22
 * @description 集成平台服务接口
 */
public interface IntegrationPlatformService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param proid
     * @description 从文件中心取出集成平台的人员照片附件上传到登记项目的文件中心中
     */
    void uploadRyzpFileFromJcptToBdcdj(String proid);

    Object getBusinessData(String businessNo,String proid);

    JSONObject importYwbh(String businessNo, String proid);
}
