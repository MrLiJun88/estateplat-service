package cn.gtmap.estateplat.server.core.service;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/10/26
 * @description 短信接口
 */
public interface SmsService {
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param proid
     * @param activityName
     * @return
     *
     * @description 发送短信
     **/
    void sendSms(String proid, String activityName);
}
