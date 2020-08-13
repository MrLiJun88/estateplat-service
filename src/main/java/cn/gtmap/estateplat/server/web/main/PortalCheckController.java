package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.utils.ReadJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 * portal调取接口验证
 *
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version V1.0, 2020-3-21
 */
@Controller
@RequestMapping("/portalCheck")
public class PortalCheckController {
    @Autowired
    private BdcXmService bdcXmService;

    @ResponseBody
    @RequestMapping(value = "/checkSfdc", method = {RequestMethod.POST, RequestMethod.GET})
    public Object checkSfdc(@RequestParam(value = "proid", required = false) String proid) {
        Map<String, Object> result = new HashMap<>();
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        Integer dcsjzs = ReadJsonUtil.getDcsjzs(bdcXm.getSqlx(), bdcXm.getDjlx());
        if (dcsjzs != null && bdcXm.getDcsjzs() != null && dcsjzs >= bdcXm.getDcsjzs()) {
            result.put("sfdc", "true");
        } else {
            result.put("sfdc", "false");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/checkSmzt", method = {RequestMethod.POST, RequestMethod.GET})
    public Object checkSmzt(@RequestParam(value = "wiids", required = false) String wiids) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(wiids)) {
            String[] wiidList = StringUtils.split(wiids, ",");
            for (String wiid : wiidList) {
                HashMap map= new HashMap();
                map.put("wiid",wiid);
                String smzt = bdcXmService.getSmztByWiid(wiid);
                map.put("smzt",smzt);
                result.add(map);
            }
        }
        return result;
    }
}
