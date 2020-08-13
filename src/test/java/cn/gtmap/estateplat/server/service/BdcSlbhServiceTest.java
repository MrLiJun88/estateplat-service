package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.BdcGdxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.service.server.BdcSlbhService;
import com.alibaba.fastjson.JSON;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/31
 * @description
 */
public class BdcSlbhServiceTest extends BdcBaseUnitTest {

    @Autowired
    private BdcXmService bdcXmService;
    @Test
    public void testSlbh(){
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(null);
        int count = bdcXmList.size();
        ExecutorService executorService = Executors.newFixedThreadPool(count);

        for (int i = 0; i < 1000; i++)
            executorService.execute(new Task(bdcXmList.get(i)));

        executorService.shutdown();
        while (!executorService.isTerminated()) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public class Task implements Runnable {
        private BdcXm bdcXm;

        public Task(BdcXm bdcXm) {
            this.bdcXm = bdcXm;
        }


        @Override
        public void run() {
            try {
//                if(StringUtils.isBlank(bdcXm.getLsh())&&bdcXm.getCjsj()!=null)
//                System.out.println(bdcXmService.creatXmbh(bdcXm));
                HttpClient client = new HttpClient();
                // 模拟登录页面 login.jsp->main.jsp
                client.getHostConfiguration().setHost("127.0.0.1", 80);
                PostMethod post = new PostMethod( "/cas/slogin" );
                NameValuePair name = new NameValuePair( "username" , "Admin" );
                NameValuePair pass = new NameValuePair( "password" , "1" );
                NameValuePair url = new NameValuePair( "url" , "http://127.0.0.1/portal/taskCenter/createTask" );
                post.setRequestBody( new NameValuePair[]{name,pass,url});
                int status = client.executeMethod(post);
                System.out.println(post.getResponseBodyAsString());
                post.releaseConnection();
                // 查看 cookie 信息
                CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
                Cookie[] cookies = cookiespec.match("127.0.0.1", 80, "/" , false , client.getState().getCookies());
                if (cookies.length == 0) {
                    System.out.println( "None" );
                } else {
                    for ( int i = 0; i < cookies.length; i++) {
                        System.out.println(cookies[i].toString());
                    }
                }
                GetMethod get1=new GetMethod("/estateplat-server/bdcJgSjgl/createCsdj?bdclx=TDFW&sfzxtdz=1&djlx=999&fwid=162070-2-20&tdid=&dah=&lqid=&cqid=&djId=%7B70d6bfdc-f633-1d3d-3bd8-a619946db63&bdcdyh=320705003011GB00117F00010045&workFlowDefId=36C8471F9D404C788221B2ADC8FE57E6&sqlxMc=%E8%BD%AC%E7%A7%BB%E6%8A%B5%E6%8A%BC%E5%90%88%E5%8A%9E%E7%99%BB%E8%AE%B0&xmmc=%E6%96%B0%E6%B5%A6%E5%8C%BA%E7%AB%99%E4%B8%AD%E8%A1%97%E9%BE%99%E6%B2%B3%E5%8C%97%E8%B7%AF149%23&tdzh=&ppzt=3&dyid=&ygid=&cfid=&yyid=&gdproid=CQ200206160013&mulGdfw=false&djIds=&bdcdyhs=&tdids=&qlid=CQ200206160013&qlzt=&gdproids=&qlids=&ybdcqzh=" );
                client.executeMethod(get1);
                System.out.println(get1.getResponseBodyAsString());
                get1.releaseConnection();

                // 访问所需的页面 main2.jsp
                GetMethod get=new GetMethod("/portal/taskCenter/createTask?wdid=36C8471F9D404C788221B2ADC8FE57E6");
                client.executeMethod(get);
                Object test=JSON.parse(get.getResponseBodyAsString());
                get.releaseConnection();

                GetMethod get2=new GetMethod("/estateplat-server/wfProject/creatProjectEvent?wdid=36C8471F9D404C788221B2ADC8FE57E6&userid=0&proid="+((Map)test).get("proid")+"&wfid="+((Map)test).get("wfid"));
                client.executeMethod(get2);
                get2.getResponseBodyAsString();
                get2.releaseConnection();



//                HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
//                connectionManager.getParams().setDefaultMaxConnectionsPerHost(10);
//                connectionManager.getParams().setConnectionTimeout(30000);
//                connectionManager.getParams().setSoTimeout(30000);
//                HttpClient httpClient = new HttpClient(connectionManager);
//                String gdInterface =  "http://127.0.0.1/cas/slogin?username=Admin&password=1&url=http://127.0.0.1/portal/taskCenter/createTask";
//                PostMethod postMethod = new PostMethod(gdInterface);
//                postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
//                        NameValuePair[] postData = new NameValuePair[1];
//                        postData[0] = new NameValuePair("wdid", "19D3CDE088174478943341B32EF3238C");
//                        postMethod.addParameters(postData);
//c
//                httpClient.executeMethod(postMethod);
//
//                String responseXml = postMethod.getResponseBodyAsString();
//                System.out.println(httpClient.getState());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
