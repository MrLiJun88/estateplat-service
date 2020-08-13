package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2017-8-21
 * @description 验证不动产单元备案
 */
@Service
public class BdcdyBaServiceImpl implements ProjectValidateService {
    @Autowired
    private BdcdyService bdcdyService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> proidList = new ArrayList<String>();
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            String bdcdyh = "";
            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                bdcdyh = project.getBdcdyh();
            } else {
                bdcdyh = bdcdyService.getBdcdyhByProid(project.getProid());
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                HttpClient client = null;
                PostMethod method = null;
                try {
                    String url = AppConfig.getProperty("building-contract.url") + "/htbaServerClient/checkIsba";
                    client = new HttpClient();
                    client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                    client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                    method = new PostMethod(url);
                    method.setRequestHeader("Connection", "close");
                    method.addParameter("bdcdyh", bdcdyh);
                    client.executeMethod(method);
                    String isba = method.getResponseBodyAsString();
                    if (!StringUtils.equals(isba, "true")) {
                        proidList.add(project.getProid());
                    }
                } catch (IOException e) {
                    logger.error("BdcdyBaServiceImpl.validate",e);
                } finally {
                    if(method != null) {
                        method.releaseConnection();
                    }
                    if(client != null) {
                        ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(proidList)) {
            map.put("info", proidList);
        }else {
            map.put("info", null);
        }
        return map;
    }

    @Override
    public String getCode() {
        return "911";
    }

    @Override
    public String getDescription() {
        return "验证不动产单元是否是备案";
    }
}
