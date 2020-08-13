package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcYyService;
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
 * @description       不动产登记异议信息
 */
@Controller
@RequestMapping("/bdcYyxx")
public class BdcYyxxController  extends BaseController{
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcYyService bdcYyService;

    /**
     *@Author:<a href="mailto:juyulin@gtmap.cn">juyulin</a>
     *@Description:保存异议登记信息
     *@Date 16:01 2017/2/16
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcYyxx", method = RequestMethod.POST)
    public Map saveBdcYyxx(Model model, BdcYy bdcYy, BdcSpxx bdcSpxx, BdcXm bdcXm){
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcYy != null && StringUtils.isNotBlank(bdcYy.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcYyService.saveBdcYy(bdcYy);
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
}
