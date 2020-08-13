package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/4/16
 * @description 房产分户图跳转资源
 */
@Controller
@RequestMapping("/fcfht")
public class FcfhtControloller extends BaseController {
    @Autowired
    BdcdyService bdcdyService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void index(@RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) throws Exception {
        String url = "";
        try {
            String fcfhtUrl = AppConfig.getProperty("fcfht.url");
            if (StringUtils.isNotBlank(fcfhtUrl)) {
                url = fcfhtUrl;
            }
            if (StringUtils.isNotBlank(proid)) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                if (bdcBdcdy != null) {
                    url = url + "&roomfdh=" + bdcBdcdy.getFwbm() + "&bdcdyh=" + bdcBdcdy.getBdcdyh();
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("Unexpected error in yqllxResource function", e);
            url = bdcdjUrl + "/index/toError";
            e.printStackTrace();
        }
        response.sendRedirect(url);
    }
}
