package cn.gtmap.estateplat.server.web.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常页面
 *
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2015/7/2
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorPageController {
    @RequestMapping("404")
    public String error404() {
        return "common/404";
    }

    @RequestMapping("500")
    public String error500() {
        return "common/500";
    }
}
