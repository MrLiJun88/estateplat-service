package cn.gtmap.estateplat.server.web.main;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxing
 * Date: 14-5-1
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {
    @Autowired
    CreatProjectService creatProjectYydjServiceImpl;

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    DjxxMapper djxxMapper;


    /**
     * 跳转新建页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toAddBdcxm")
    public String toAddBdcxm(Model model) {
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<BdcZdQllx> qllxList = bdcZdGlService.getBdcQllx();
        model.addAttribute("djList", djlxList);
        model.addAttribute("qlList", qllxList);
        return "sjgl/addBdcxm2";
    }

    /**
     * @param model
     * @return
     */
    @RequestMapping("/toError")
    public String toError(Model model) {
        return "main/error";
    }

    @RequestMapping("/toPic")
    public String toPic(Model model) {
        return "sjgl/pic";
    }

    /**
     * 综合统计分析
     *
     * @param model
     * @return
     */
    @RequestMapping("/toChartReport")
    public String toChartReport(Model model) {
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<BdcZdQllx> qllxList = bdcZdGlService.getBdcQllx();
        List<Map> bdcList = bdcZdGlService.getZdBdclx();

        String bdcdjtjGjss = AppConfig.getProperty("bdcdjtjGjss.order");
        List<String> bdcdjtjGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(bdcdjtjGjss) && bdcdjtjGjss.split(",").length > 0){
            for(String bdcdjtjGjssZd : bdcdjtjGjss.split(",")){
                bdcdjtjGjssOrderList.add(bdcdjtjGjssZd);
            }
        }
        model.addAttribute("bdcdjtjGjss", bdcdjtjGjss);
        model.addAttribute("bdcdjtjGjssOrderList", bdcdjtjGjssOrderList);

        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        model.addAttribute("userDwdm", userDwdm);
        model.addAttribute("djList", djlxList);
        model.addAttribute("qlList", qllxList);
        model.addAttribute("bdcList", bdcList);
        return "analysis/chartReport";
    }

    /**
     * 抵押金额统计分析
     *
     * @param model
     * @return
     */
    @RequestMapping("/toDyjeChartReport")
    public String toDyjeChartReport(Model model) {
        List<Map> dyfsList = bdcZdGlService.getZdDyfs();
        model.addAttribute("dyfsList", dyfsList);
        return "analysis/dyjeChartReport";
    }

    /**
     * 商品房统计分析
     *
     * @param model
     * @return
     */
    @RequestMapping("/toSpfChartReport")
    public String toSpfChartReport(Model model) {
        return "analysis/spfChartReport";
    }

    @RequestMapping(value = "/errorMsg", method = RequestMethod.GET)
    public String redirectErrorMsg(Model model, String msg) throws UnsupportedEncodingException {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(msg))
            msg = URLDecoder.decode(msg, "UTF-8");
        model.addAttribute("msg", msg);
        return "common/errorMsg";
    }

    @RequestMapping("/gridDemo")
    public String gridDemo(Model model) throws IOException {
        return "main/gridDemo";
    }
    @ResponseBody
    @RequestMapping("/getYyCount")
    public Object getYyCount(@RequestParam(value = "userid", required = false) String userid) {
       return 3;
    }
}
