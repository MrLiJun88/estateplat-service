package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcJzwsyq;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcJzwsyqService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 16-12-14
 * @description       不动产登记构（建）筑物所有权登记信息
 */
@Controller
@RequestMapping("/bdcJzwsyqxx")
public class BdcJzwsyqxxController extends BaseController {
    @Autowired
    private BdcJzwsyqService bdcJzwsyqService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存构（建）筑物所有权登记信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcJzwsyqxx", method = RequestMethod.POST)
    public Map saveBdcJzwsyqxx(Model model, BdcJzwsyq bdcJzwsyq, BdcSpxx bdcSpxx, BdcXm bdcXm) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcJzwsyq != null && StringUtils.isNotBlank(bdcJzwsyq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            bdcJzwsyqService.saveBdcJzwsyq(bdcJzwsyq);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

}
