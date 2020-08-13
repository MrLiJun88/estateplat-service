package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcYg;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
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
 * @description       不动产登记预告信息
 */
@Controller
@RequestMapping("/bdcYgxx")
public class BdcYgxxController extends BaseController {
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcYgService bdcYgService;
    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@Description:保存预告登记信息
     *@Date 16:01 2017/2/16
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcYgxx", method = RequestMethod.POST)
    public Map saveBdcYgxx(Model model, BdcYg bdcYg, BdcSpxx bdcSpxx, BdcXm bdcXm){
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcYg != null && StringUtils.isNotBlank(bdcYg.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcYgService.saveYgxx(bdcYg);
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
}
