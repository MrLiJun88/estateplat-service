package cn.gtmap.estateplat.server.web.wf.qlxx;

import cn.gtmap.estateplat.model.server.core.BdcHysyq;
import cn.gtmap.estateplat.server.core.service.BdcHysyqService;
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
 * @description       不动产登记海域使用权信息
 */
@Controller
@RequestMapping("/bdchysyqxx")
public class BdcHysyqxxController extends BaseController {
    @Autowired
    private BdcHysyqService bdcHysyqService;

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存海域（含无居民海岛） 使用权登记信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcHysyqxx", method = RequestMethod.POST)
    public Map saveBdcHysyqxx(Model model, BdcHysyq bdcHysyq) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcHysyq != null && StringUtils.isNotBlank(bdcHysyq.getQlid())) {
            bdcHysyqService.saveBdcHysyq(bdcHysyq);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
}
