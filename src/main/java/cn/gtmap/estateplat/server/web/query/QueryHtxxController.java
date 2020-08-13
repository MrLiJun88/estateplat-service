package cn.gtmap.estateplat.server.web.query;


import cn.gtmap.estateplat.model.server.core.BdcGdRel;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
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
import java.util.List;

/**
 * 查询合同信息
 * User: zhangxing
 * Date: 14-5-1
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryHtxx")
public class QueryHtxxController extends BaseController {

    @Autowired
    private OmpDataService ompDataService;

    @Autowired
    private BdcdyService bdcdyService;

    @RequestMapping("")
    public ModelAndView queryLpbFromMap(Model model, @RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) throws Exception {
        String url = "";
        if (StringUtils.isNotBlank(proid)) {
            String bdcdyh = bdcdyService.getBdcdyhByProid(proid);
            if (StringUtils.isNotBlank(bdcdyh)) {
                List<BdcGdRel> bdcGdRelList = ompDataService.getBdcGdRelList(bdcdyh, proid);
                if (CollectionUtils.isNotEmpty(bdcGdRelList)) {
                    url = AppConfig.getProperty("onemap.view.htxx.url") + "?bh=" + bdcGdRelList.get(0).getGdhth();
                }
            }
        }
        if (StringUtils.isBlank(url)) {
            url = bdcdjUrl + "/index/errorMsg?msg=" + URLEncoder.encode("没有关联合同！", Constants.DEFAULT_CHARSET);
        }

        response.sendRedirect(url);
        return null;
    }

}
