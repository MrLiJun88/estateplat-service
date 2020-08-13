package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcGdDyhRel;
import cn.gtmap.estateplat.model.server.core.GdFw;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcGdDyhRelService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.utils.Charsets;
import com.google.common.collect.Lists;
import com.gtis.config.AppConfig;
import com.rabbitmq.tools.json.JSONReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/10/19
 * @description
 */
public class BdcdyFwxzValidateServiceImpl implements ProjectValidateService {

    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private HttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map<String, Object> map = new HashMap<String, Object>();
        Project project= (Project)param.get("project");
        String qh = "";
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            List<String> qhList = bdcdyService.getCqqidByBdcdy(project.getBdcdyh());
            if (CollectionUtils.isNotEmpty(qhList)) {
                qh = qhList.get(0);
            }
        }
        if (project != null && StringUtils.isNotBlank(project.getGdproid())) {
            HashMap map1 = new HashMap();
            map1.put("proid", project.getGdproid());
            List<String> qhList = gdFwService.getCqqidByGdProid(map1);
            if (CollectionUtils.isNotEmpty(qhList)) {
                qh = qhList.get(0);
            }
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 只考虑连云港一个权力对应一个房屋
             */
            if (StringUtils.isEmpty(qh)) {
                List<GdFw> gdFwList = gdFwService.getGdFwByProid(project.getGdproid(), "");
                if (CollectionUtils.isNotEmpty(gdFwList)) {
                    List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", gdFwList.get(0).getFwid());
                    if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                        List<String> qhList1 = bdcdyService.getCqqidByBdcdy(gdDyhRelList.get(0).getBdcdyh());
                        if (CollectionUtils.isNotEmpty(qhList1)) {
                            qh = qhList1.get(0);
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(qh)) {
            CloseableHttpClient closeableHttpClient;
            CloseableHttpResponse closeableHttpResponse = null;
            try {
                closeableHttpClient = (CloseableHttpClient) httpClient;
                String etlUrl = AppConfig.getProperty("etl.url");
                String url = etlUrl + "/gx/fw/xz";
                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> params = Lists.newArrayList();
                params.add(new BasicNameValuePair("qh", qh));
                httpPost.setEntity(new UrlEncodedFormEntity(params, Charsets.CHARSET_UTF8));
                closeableHttpResponse = closeableHttpClient.execute(httpPost);
                if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                    String returnJson = EntityUtils.toString(closeableHttpResponse.getEntity());
                    if (StringUtils.isNotBlank(returnJson)) {
                        JSONReader jsonReader = new JSONReader();
                        Object json = jsonReader.read(returnJson);
                        if (json != null) {
                            HashMap resultMap = (HashMap) json;
                            if (resultMap.get("ret") != null&&StringUtils.equals(resultMap.get("ret").toString(), "false")&&resultMap.get("msg") != null) {
                                map.put("info", resultMap.get("msg").toString());
                            }
                        }
                    }
                }

            } catch (IOException e) {
                logger.error("BdcdyFwxzValidateServiceImpl.validate",e);
            } finally {
                if (closeableHttpResponse != null) {
                    try {
                        closeableHttpResponse.close();
                    } catch (IOException e) {
                        logger.error("BdcdyFwxzValidateServiceImpl.validate",e);
                    }
                }
            }
        } else {
            map.put("info", "该房屋没有丘号,请重新选择");
        }
        return map;
    }

    @Override
    public String getCode() {
        return "132";
    }

    @Override
    public String getDescription() {
        return "验证房屋是否给限制";
    }
}
