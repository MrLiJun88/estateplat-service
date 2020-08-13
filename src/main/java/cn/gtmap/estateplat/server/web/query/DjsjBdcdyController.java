package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.server.web.main.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-3-20
 * Time: 下午4:43
 * dos:不动产单元目录
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/djsjBdcdy")
public class DjsjBdcdyController extends BaseController {

    /*审批表选择土地证**/
    @RequestMapping(value = "tdz", method = RequestMethod.GET)
    public String indexTdz(Model model, String proid) {
        model.addAttribute("proid", proid);
        return "query/djsjBdcdyList";
    }

    /*审批表选择房产证**/
    @RequestMapping(value = "fcz", method = RequestMethod.GET)
    public String indexFcz(Model model, String proid) {
        model.addAttribute("proid", proid);
        return "query/djsjBdcdyList";
    }
}
