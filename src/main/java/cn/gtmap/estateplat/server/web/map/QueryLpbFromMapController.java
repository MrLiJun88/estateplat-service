package cn.gtmap.estateplat.server.web.map;


import cn.gtmap.estateplat.server.core.service.BdcHsService;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 从服务查询楼盘表
 * User: zhangxing
 * Date: 14-5-1
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryLpbFromMap")
public class QueryLpbFromMapController extends BaseController {
    @Autowired
    private BdcHsService bdcHsService;

    @Autowired
    private DjxxMapper djxxMapper;

    @RequestMapping("")
    public ModelAndView queryLpbFromMap(Model model, @RequestParam(value = "ZRZH", required = false) String zrzh, @RequestParam(value = "LSZD", required = false) String lszd, HttpServletResponse response) throws Exception {
        String url = "";
        if (StringUtils.isNotBlank(zrzh) && StringUtils.isNotBlank(lszd) && !StringUtils.equalsIgnoreCase(zrzh, "undefined") && !StringUtils.equalsIgnoreCase(lszd, "undefined")) {
            HashMap map = new HashMap();
            map.put("zrzh", zrzh);
            map.put("lszd", lszd);
            List<Map> fwDcbList = djxxMapper.getFWDcbList(map);
            if (CollectionUtils.isNotEmpty(fwDcbList)) {
                url = bdcHsService.getLpbUrl(CommonUtil.formatEmptyValue(fwDcbList.get(0).get("FW_DCB_INDEX")));
            }
        }
        if (StringUtils.isBlank(url)) {
            url = bdcdjUrl + "/index/errorMsg?msg=" + URLEncoder.encode("没有找到楼盘！", Constants.DEFAULT_CHARSET);
        }

        response.sendRedirect(url);
        return null;
    }

}
