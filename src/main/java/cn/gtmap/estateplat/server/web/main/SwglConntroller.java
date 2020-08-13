package cn.gtmap.estateplat.server.web.main;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-10
 */
@Controller
@RequestMapping("/swgl")
public class SwglConntroller extends BaseController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {

        return "/swgl/swgl";
    }
}
