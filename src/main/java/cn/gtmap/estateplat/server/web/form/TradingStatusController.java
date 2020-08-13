package cn.gtmap.estateplat.server.web.form;

import cn.gtmap.estateplat.server.core.service.BdcdyTradingStatusService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 2019/3/29
 * @description 昆山交易状态验证
 */
@Controller
@RequestMapping("/tradingStatus")
public class TradingStatusController extends BaseController {
    @Autowired
    private BdcdyTradingStatusService bdcdyTradingStatusService;

    @ResponseBody
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public Map validate(@RequestParam(value = "proid", required = false) String proid,@RequestParam(value = "djzx", required = false) String djzx) {
       return bdcdyTradingStatusService.validate(proid,djzx);
    }

    @ResponseBody
    @RequestMapping(value = "checkBdcdyTradingStatus")
    public Map checkBdcdyTradingStatus(String proid,String djzx){
        return bdcdyTradingStatusService.checkBdcTradingStatusBeforeSave(proid,djzx);
    }

}
