package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcTdsyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcTdsyqService;
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
 * @description   不动产登记抵押信息
 */
@Controller
@RequestMapping("/bdcTdsyqxx")
public class BdcTdsyqxxController extends BaseController {
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcTdsyqService bdcTdsyqService;
    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@Description:保存土地所有权登记信息
     *@Date 15:26 2017/2/16
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcTdsyqxx", method = RequestMethod.POST)
    public Map saveBdcTdsyqxx(Model model, BdcTdsyq bdcTdsyq, BdcSpxx bdcSpxx, BdcXm bdcXm){
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcTdsyq != null && StringUtils.isNotBlank(bdcTdsyq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcTdsyqService.saveBdcTdsyq(bdcTdsyq);
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
}
