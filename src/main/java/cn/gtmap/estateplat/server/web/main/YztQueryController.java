package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.core.service.YztIntService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-10-8
 * Time: 下午2:49
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/yztQuery")
public class YztQueryController extends BaseController {
    @Autowired
    YztIntService yztIntService;

    @ResponseBody
    @RequestMapping(value = "/getOutfields")
    public Object getOutfields(@RequestParam(value = "o", required = false) String o, @RequestParam(value = "w", required = false) String w, @RequestParam(value = "l", required = false) String l, @RequestParam(value = "p", required = false) int p, @RequestParam(value = "s", required = false) int s) {
        JSONArray jsonArray = JSON.parseArray(w);
        return yztIntService.getSerchJson(jsonArray, l, p, s);
    }
}
