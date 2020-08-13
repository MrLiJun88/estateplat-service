package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.core.service.BdcdyService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/10/11
 * @description 一张图定位
 */
@Controller
@RequestMapping("/oneMap")
public class OneMapDwController extends BaseController {
    @Autowired
    private BdcdyService bdcdyService;

    @RequestMapping("/dw")
    public String toOntSjgl(Model model, String proid, String wiid) {
        String onemapDwUrl = AppConfig.getProperty("onemap.dwUrl");
        if (StringUtils.isNotBlank(onemapDwUrl) && StringUtils.isNotBlank(proid)) {
            String bdcdyh = bdcdyService.getBdcdyhByProid(proid);
            if (StringUtils.isNotBlank(bdcdyh)) {
                String djh = bdcdyh.substring(0, 19);
                onemapDwUrl += "?hideLeftPanel=true&hideTopBar=true&action=location&params={%22params%22:{%22layerAlias%22:%22%E5%AE%97%E5%9C%B0%22,%22where%22:%22DJH%20in%20(%27" + djh + "%27)%22},%22type%22:%22layerLocation%22}";
                model.addAttribute("onemapDwUrl", onemapDwUrl);
            }
        }
        return "main/oneMapDw";
    }

}
