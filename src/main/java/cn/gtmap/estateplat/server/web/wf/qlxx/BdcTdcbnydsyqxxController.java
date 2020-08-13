package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcTdcbnydsyqService;
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
 * @description  土地承包经营权、农用地的其他使用权登记信息（非林地）
 */
@Controller
@RequestMapping("/bdcTdcbnydsyqxx")
public class BdcTdcbnydsyqxxController  extends BaseController{
    @Autowired
    BdcTdcbnydsyqService bdcTdcbnydsyqService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;

    /**
     *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     *@Description:保存土地承包经营权、农用地的其他使用权登记信息（非林地）
     *@Date 14:44 2017/2/16
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcTdcbnydsyqxx", method = RequestMethod.POST)
    public Map saveBdcTdcbnydsyqxx(Model model, BdcTdcbnydsyq bdcTdcbnydsyq, BdcSpxx bdcSpxx, BdcXm bdcXm){
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcTdcbnydsyq != null && StringUtils.isNotBlank(bdcTdcbnydsyq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            bdcTdcbnydsyqService.saveBdcTdcbnydsyq(bdcTdcbnydsyq);
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

}
