package cn.gtmap.estateplat.server.web;

import cn.gtmap.estateplat.core.support.sms.impl.CuccSmsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/11/6
 * @description
 */
public class WebServiceTest {
    @Test
    public void sendSms(){
        String smsUrl = "http://js.ums86.com:8899/sms/Api/Send.do";
        //String content = "您的预约已提交，请届时携带尾号为4414证件和不动产相关资料到服务窗口办理。";
        String content = "您提交的不动产djlx申请已受理，受理编号为slbh。";

        if (StringUtils.isNotBlank(smsUrl) && StringUtils.isNotBlank(content)) {
            CuccSmsServiceImpl cuccSmsService = new CuccSmsServiceImpl();
            cuccSmsService.setSmsUrl(smsUrl);
            cuccSmsService.setSpCode("244378");
            cuccSmsService.setUsername("sz_bdc");
            cuccSmsService.setPassword("bdc@1234");


            HttpClient client = HttpClients.createDefault();
            cuccSmsService.setHttpClient(client);
            cuccSmsService.sendSms("18551756612",content);
        }


    }
}
