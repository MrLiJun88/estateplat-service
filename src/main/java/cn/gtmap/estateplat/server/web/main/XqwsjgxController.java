package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcSfxm;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSfxmService;
import cn.gtmap.estateplat.server.core.service.BdcSfxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.HttpClientUtil;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
 * @Time 2020/4/2 16:24
 * @description 行权网数据共享
 */
@Controller
@RequestMapping("/xqwSjgx")
public class XqwsjgxController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String CURRENCY_URL = AppConfig.getProperty("currency.url");
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSfxxService bdcSfxxService;

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 推送数据到行权网
     */
    @ResponseBody
    @RequestMapping(value = "/pushSjToXqw")
    void pushSjToXqw(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "userid", required = false) String userid) {
        if (StringUtils.isNotBlank(proid)) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            String url = StringUtils.EMPTY;
            try {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && StringUtils.isBlank(bdcXm.getXqwywh())) {
                    paramMap.put("proid", proid);
                    paramMap.put("userid", userid);
                    url = CURRENCY_URL + "/rest/v1.0/sjgx/pushDjbjxx";
                    HttpClientUtil.doGet(url, paramMap);
                    List<BdcSfxm> bdcSfxmList = bdcSfxxService.queryBdcSfxmByWiid(bdcXm.getWiid());
                    if (CollectionUtils.isNotEmpty(bdcSfxmList)) {
                        url = CURRENCY_URL + "/rest/v1.0/sjgx/pushSfxx";
                        HttpClientUtil.doGet(url, paramMap);
                    }
                    url = CURRENCY_URL + "/rest/v1.0/sjgx/finishStep";
                    HttpClientUtil.doGet(url, paramMap);
                }
            } catch (Exception e) {
                logger.error("推送办件信息错误：", e);
            }
        }
    }

}
