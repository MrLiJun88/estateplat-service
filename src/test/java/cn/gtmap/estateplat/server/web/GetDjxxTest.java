package cn.gtmap.estateplat.server.web;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.impl.CreatProjectDyBgdjServiceImpl;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.plat.service.SysCalendarService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2016/3/8
 * @description 获取地籍信息测试用例
 */
public class GetDjxxTest extends BdcBaseUnitTest {
    @Resource(name = "creatProjectDefaultService")
    CreatProjectService creatProjectDefaultService;

    @Autowired
    DjxxMapper djxxMapper;

    List<Project> projects=null;

    @Test
    public void getDjxx() {
        int thread_num = 50;
        int client_num = 100;
        Map keywordMap = new HashMap();

            int size = keywordMap.size();
            ExecutorService exec = Executors.newCachedThreadPool();
// 50个线程可以同时访问
            final Semaphore semp = new Semaphore(thread_num);
// 模拟2000个客户端访问
            for (int index = 0; index < client_num; index++) {
                final int NO = index;
                Runnable run = new Runnable() {


                    public void run() {
                        try {
// 获取许可
                            semp.acquire();
                            System.out.println("Thread:" + NO);
                            String host = "http://127.0.0.1/estateplat-server/index/test" ;
                                URL url = new URL(host);// 此处填写供测试的url
                                HttpURLConnection connection = (HttpURLConnection) url
                                .openConnection();
//                                 connection.setRequestMethod("POST");
//                                 connection.setRequestProperty("Proxy-Connection",
//                                 "Keep-Alive");
                                connection.setDoOutput(true);
                                connection.setDoInput(true);
                                PrintWriter out = new PrintWriter(connection
                                                                    .getOutputStream());
                                out.print("d");
                                out.flush();
                                out.close();
                                BufferedReader in = new BufferedReader(
                                                                    new InputStreamReader(connection
                                                                    .getInputStream()));
                                String line = "";
                                String result = "";
                                while ((line = in.readLine()) != null) {
                                result += line;
                                }
// 释放
                            System.out.println("第：" + NO + " 个");
                            semp.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                };
                exec.execute(run);
            }
// 退出线程池
            exec.shutdown();

    }


}
