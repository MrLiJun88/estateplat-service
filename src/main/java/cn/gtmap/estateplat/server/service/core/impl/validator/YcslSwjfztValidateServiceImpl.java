package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.service.core.config.ValidateNodeConfigService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/11/25
 * @description 验证一窗受理税务缴税状态-未缴费
 */
public class YcslSwjfztValidateServiceImpl implements ProjectValidateService {
    protected final Logger logger = LoggerFactory.getLogger(YcslSwjfztValidateServiceImpl.class);
    @Autowired
    private ValidateNodeConfigService validateNodeConfigService;

    @Override
    public Map<String, Object> validate(HashMap param) {
        Map map = new HashMap();
        Project project= (Project)param.get("project");
        String url = AppConfig.getProperty("ycsl.validate.swjfzt.url");
        /**
         * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
         * @description 验证过滤节点
         */
        Boolean validateEnable = validateNodeConfigService.nodeValidateEnable(project,this.getCode());
        if(project != null&&StringUtils.isNotBlank(project.getYcslywh())&&StringUtils.isNotBlank(url)&&validateEnable) {
            Boolean taxPayable = false;
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpResponse = null;
            try{
                url = AppConfig.getProperty("etl.url") + AppConfig.getProperty("ycsl.validate.swjfzt.url") + project.getYcslywh();
                httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(url);
                httpclient.execute(httpGet);
                HttpEntity resEntity = httpResponse.getEntity();
                if(resEntity != null){
                    String result = EntityUtils.toString(resEntity, Constants.DEFAULT_CHARSET);
                    HashMap resultMap = JSON.parseObject(result, HashMap.class);
                    if(resultMap != null&&StringUtils.equals(CommonUtil.formatEmptyValue(resultMap.get("complete")),ParamsConstants.TRUE_LOWERCASE)){
                        taxPayable = true;
                    }
                }
                EntityUtils.consume(resEntity);
            }catch (Exception e) {
                logger.error("YcslSwjfztValidateServiceImpl.validate", e);
            }finally {
                if (httpResponse != null) {
                    try {
                        httpResponse.close();
                    } catch (IOException e) {
                        logger.error("YcslSwjfztValidateServiceImpl.validate", e);
                    }
                }
                if (httpclient != null) {
                    try {
                        httpclient.close();
                    } catch (IOException e) {
                        logger.error("YcslSwjfztValidateServiceImpl.validate", e);
                    }
                }
            }
            if(!taxPayable) {
                map.put("info", project.getProid());
            }
        }
        return map;
    }

    @Override
    public String getCode() {
        return "403";
    }

    @Override
    public String getDescription() {
        return "验证一窗受理税务缴税状态是否完税";
    }
}
