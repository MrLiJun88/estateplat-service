package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcXtLimitfield;
import cn.gtmap.estateplat.model.server.core.BdcXtLimitfieldofGd;
import cn.gtmap.estateplat.server.core.service.BdcXtLimitfieldService;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产验证功能
 *
 * @author lst
 * @version V1.0, 15-4-20
 */
@Controller
@RequestMapping("/bdcLimit")
public class BdcXtLimitConntroller extends BaseController {
    @Autowired
    private BdcXtLimitfieldService bdcXtLimitfieldService;

    public static final String CONFIGURATION_PARAMETER_VALIDATE_BGCOLOR = "validate.bgcolor";
    public static final String PARAMETER_COLOR = "color";
    public static final String PARAMETER_DEFAULT_COLOR = "#fcf7c7";

    @ResponseBody
    @RequestMapping("/getFRValidateInfo")
    public HashMap getFRValidateInfo(Model model, String taskid, String proid, String cptName, String isYqlxx) {
        HashMap result = new HashMap();
        String color = PlatformUtil.initOptProperties(AppConfig.getProperty(CONFIGURATION_PARAMETER_VALIDATE_BGCOLOR));
        if (!StringUtils.isNotBlank(color)) {
            color = PARAMETER_DEFAULT_COLOR;
        }
        result.put(PARAMETER_COLOR, color);
        List<BdcXtLimitfield> list = null;
        if (StringUtils.isNotBlank(cptName) && StringUtils.isNotBlank(taskid) && StringUtils.isNotBlank(proid)) {
            if (StringUtils.equals(isYqlxx, "true"))
                cptName = "y" + cptName;
            String wfDfid = PlatformUtil.getWfDfidByProjectId(proid);
            String actyId = PlatformUtil.getPfActivityIdByTaskId(taskid);
            list = bdcXtLimitfieldService.getLimitfield(wfDfid, actyId, cptName);
        }
        result.put("data", list);
        return result;
    }

    @ResponseBody
    @RequestMapping("/getFRValidategdInfo")
    public HashMap getFRValidategdInfo(Model model, String cptName) {
        HashMap result = new HashMap();
        String color = PlatformUtil.initOptProperties(AppConfig.getProperty(CONFIGURATION_PARAMETER_VALIDATE_BGCOLOR));
        if (!StringUtils.isNotBlank(color)) {
            color = PARAMETER_DEFAULT_COLOR;
        }
        result.put(PARAMETER_COLOR, color);
        List<BdcXtLimitfieldofGd> list = null;
        if (StringUtils.isNotBlank(cptName)) {
            list = bdcXtLimitfieldService.getLimitfieldOfGd(cptName);
        }
        result.put("data", list);

        return result;

    }


    @ResponseBody
    @RequestMapping("/getZfValidate")
    public List<Map> getZfValidate(Model model, String taskid, String proid) {

        return bdcXtLimitfieldService.validateMsg(taskid, proid);
    }

    @ResponseBody
    @RequestMapping("/getFRValidateColor")
    public HashMap getFRValidateColor(Model model) {
        HashMap result = new HashMap();
        String color = PlatformUtil.initOptProperties(AppConfig.getProperty(CONFIGURATION_PARAMETER_VALIDATE_BGCOLOR));
        if (!StringUtils.isNotBlank(color)) {
            color = PARAMETER_DEFAULT_COLOR;
        }
        result.put(PARAMETER_COLOR, color);
        return result;
    }

    @ResponseBody
    @RequestMapping("/getLimitfieldColor")
    public HashMap getLimitfieldColor(Model model, String taskid, String wiid) {
        HashMap result = new HashMap();
        String color = PlatformUtil.initOptProperties(AppConfig.getProperty(CONFIGURATION_PARAMETER_VALIDATE_BGCOLOR));
        if (!StringUtils.isNotBlank(color)) {
            color = PARAMETER_DEFAULT_COLOR;
        }
        result.put(PARAMETER_COLOR, color);
        List<BdcXtLimitfield> list = new ArrayList<BdcXtLimitfield>();
        if(StringUtils.isNotBlank(wiid)) {
            String wfDfid = PlatformUtil.getWfDfidByWiid(wiid);
            String actyId = PlatformUtil.getPfActivityIdByTaskId(taskid);
            list = bdcXtLimitfieldService.getLimitfield(wfDfid, actyId, "");
        }
        result.put("data", list);
        return result;
    }
}
