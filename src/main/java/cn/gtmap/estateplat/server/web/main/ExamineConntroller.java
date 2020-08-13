package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.core.service.ExamineCheckInfoService;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2017-1-17
 * @description
 */
@Controller
@RequestMapping(value = "/examine")
public class ExamineConntroller {
    @Autowired
    private ExamineCheckInfoService examineCheckInfoService;

    /**
     * @return Map<String, String>
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 创建wiid
     */
    @ResponseBody
    @RequestMapping("creatWiid")
    public Map<String, String> creatWiid(String examineInfo, String wiid) throws UnsupportedEncodingException {
        HashMap resultMap = new HashMap();
        if (StringUtils.isBlank(wiid)) {
            wiid = UUIDGenerator.generate18();
        }
        String examineUrl = AppConfig.getProperty("examine.url");
        String lwsqUrl = examineUrl + "/bdcXzyzLw/addXzyzLw?wiid=" + wiid + "&examineInfo=" + examineInfo;
        resultMap.put("lwsqUrl", lwsqUrl);
        resultMap.put("wiid", wiid);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("getXzwh")
    public String getXzwh(String wiid) throws UnsupportedEncodingException {
        String lwsqUrl = "";
        String examineUrl = AppConfig.getProperty("examine.url");
        String url = examineCheckInfoService.getXzwhByWiid(wiid);
        if (StringUtils.isNotBlank(url)) {
            lwsqUrl = examineUrl + examineCheckInfoService.getXzwhByWiid(wiid);
        }
        return lwsqUrl;
    }
}
