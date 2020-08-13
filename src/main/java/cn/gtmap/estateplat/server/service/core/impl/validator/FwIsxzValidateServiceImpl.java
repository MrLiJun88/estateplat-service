package cn.gtmap.estateplat.server.service.core.impl.validator;

import cn.gtmap.estateplat.model.server.core.BdcGdDyhRel;
import cn.gtmap.estateplat.model.server.core.GdFw;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcGdDyhRelService;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.config.AppConfig;
import com.rabbitmq.tools.json.JSONReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.0, 2016/7/29
 * @description 验证房屋是否给限制
 */
public class FwIsxzValidateServiceImpl implements ProjectValidateService {
    /**
     * @param project 项目信息
     * @return
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @version 1.0, 2016/7/29
     * @description 验证项目有效性
     */
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private HttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Map<String, Object> validate(HashMap param) {
        Project project = (Project) param.get("project");
        Map<String, Object> map = new HashMap<String, Object>();
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
            try {
                HttpMethodParams params = new HttpMethodParams();
                params.setContentCharset("GB2312");
                String etlUrl = AppConfig.getProperty("etl.url");
                String url = etlUrl + "/gx/fw/xz";
                httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, Constants.DEFAULT_CHARSET);
                PostMethod method = new PostMethod(url);
                method.setParameter("qh", qh);
                httpClient.executeMethod(method);
                String returnJson = method.getResponseBodyAsString();
                if (StringUtils.isNotBlank(returnJson)) {
                    JSONReader jsonReader = new JSONReader();
                    Object json = jsonReader.read(returnJson);
                    if (json != null) {
                        HashMap resultMap = (HashMap) json;
                        if (resultMap.get("ret") != null&&StringUtils.equals(resultMap.get("ret").toString(), "false")&&
                                resultMap.get("msg") != null) {
                            map.put("info", resultMap.get("msg").toString());
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("FwIsxzValidateServiceImpl.validate",e);
            }
        } else {
            map.put("info", "该房屋没有丘号,请重新选择");
        }
        return map;
    }

    /**
     * @return 获取此验证逻辑的代码编号
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的代码编号
     */
    @Override
    public String getCode() {
        return "101";
    }

    /**
     * @return 获取此验证逻辑的描述信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @description 获取此验证逻辑的描述信息
     */
    @Override
    public String getDescription() {
        return "验证不动产单元是否查封";
    }
}
