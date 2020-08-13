package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.server.core.service.BdcZsQlrRelService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator on 2015/4/26.
 */
@Controller
@RequestMapping("/delZsByQlrid")
public class DelZsByQlridController extends BaseController {

    @Autowired
    BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    BdcZsService bdcZsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void delZsByQlrId(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qlrid", required = false) String qlrid) {
        if (StringUtils.isNotBlank(qlrid)) {
            bdcZsQlrRelService.delBdcZsAndZsQlrRelByQlrid(qlrid);
        }
    }
}
