package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcJsydzjdsyq;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcJsydzjdsyqService;
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
 * @description       不动产登记建设用地使用权、宅基地使用权登记信息
 */
@Controller
@RequestMapping("/bdcJsydzjdsyqxx")
public class BdcJsydzjdsyqxxContoller extends BaseController {
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存建设用地使用权、宅基地使用权登记信息
     */

    @ResponseBody
    @RequestMapping(value = "/saveBdcJsydzjdsyq", method = RequestMethod.POST)
    public Map saveBdcJsydzjdsyq(Model model, BdcJsydzjdsyq bdcJsydzjdsyq, BdcSpxx bdcSpxx, BdcXm bdcXm) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcJsydzjdsyq != null && StringUtils.isNotBlank(bdcJsydzjdsyq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            if(bdcSpxx.getZdzhmj()!=null)
                bdcJsydzjdsyq.setSyqmj(bdcSpxx.getZdzhmj());
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            bdcXmService.saveBdcXm(bdcXm);
            bdcJsydzjdsyqService.saveBdcJsydzjdsyq(bdcJsydzjdsyq);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
}
