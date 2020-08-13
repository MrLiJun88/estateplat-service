package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by IntelliJ IDEA.
 * User: zx 证书关系查询
 */
@Controller
@RequestMapping("/bdcZsRel")
public class BdcZsRelController extends BaseController {
    @Autowired
    BdcXmRelService bdcXmRelService;

    /*查封列表**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "query/bdcZsRel";
    }

    @ResponseBody
    @RequestMapping("/getZsRelXml")
    public String getZsRelJson(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        String zsRelXml = "";
        String portalUrl = super.portalUrl;
        if (StringUtils.isNotBlank(proid)) {
            zsRelXml = bdcXmRelService.getHisXmRelXml(proid, bdcdyh,portalUrl);
        }
        return zsRelXml;
    }

    @ResponseBody
    @RequestMapping("/getAllXmRelXml")
    public String getDyhRelJson( @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        String zsRelXml = "";
        String portalUrl = super.portalUrl;
        if (StringUtils.isNotBlank(proid)) {
            zsRelXml = bdcXmRelService.getAllXmRelXml(proid, bdcdyh,portalUrl);
        }
        return zsRelXml;
    }

    @ResponseBody
    @RequestMapping("/getDyAllXmRelXml")
    public String getDyAllXmRelXml(@RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        String zsRelXml = "";
        String portalUrl = super.portalUrl;
        if (StringUtils.isNotBlank(bdcdyh)) {
            zsRelXml = bdcXmRelService.getDyAllXmRelXml(bdcdyh,portalUrl);
        }
        return zsRelXml;
    }

}
