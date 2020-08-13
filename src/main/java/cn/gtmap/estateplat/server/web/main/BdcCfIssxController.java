package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.sj.cf.CfSxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */

@Controller
@RequestMapping("/bdcCfIssx")
public class BdcCfIssxController extends BaseController {
    @Autowired
    private CfSxService cfSxService;

    /**
     * 不动产查封失效
     *
     * @param proid
     */
    @ResponseBody
    @RequestMapping(value = "/setCfIssx")
    public Map setCfIssx(String proid, String sxyy, String ip) {
        return cfSxService.cfsx(proid, sxyy, ip);
    }


}
