package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcDyaq;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcDyaqService;
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
 * @description       不动产登记抵押信息
 */
@Controller
@RequestMapping("/bdcDyaqxx")
public class BdcDyaqxxController extends BaseController {
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcDyaqService bdcDyaqService;

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@Description:保存抵押权信息
     *@Date 11:10 2017/2/15
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcDyaqxx", method = RequestMethod.POST)
    public Map saveBdcDyaqxx(Model model, BdcDyaq bdcDyaq, BdcSpxx bdcSpxx, BdcXm bdcXm){
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcDyaq != null && StringUtils.isNotBlank(bdcDyaq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcDyaqService.saveBdcDyaq(bdcDyaq);
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
}
