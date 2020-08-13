package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/4/10
 * @description 不动产历史信息
 */
@Controller
@RequestMapping("/bdcdyLsxx")
public class BdcLsxxController extends BaseController {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model,String wiid) {
        String analysisUrl = AppConfig.getProperty("analysis.url");
        model.addAttribute("analysisUrl",analysisUrl);
        model.addAttribute("wiid",wiid);
        return "query/bdcdyLsxx";
    }


    @RequestMapping(value = "bdcdyLsxx",method = RequestMethod.GET)
    public void index(String proid,HttpServletResponse response) throws IOException {
        String url = "";
        if(StringUtils.isNotBlank(proid)){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdy(bdcXm.getWiid());
                if(CollectionUtils.isNotEmpty(bdcBdcdyList)){
                    if(bdcBdcdyList.size() == 1){
                        BdcBdcdy bdcBdcdy = bdcBdcdyList.get(0);
                        url = bdcdjUrl + "/bdcdyLsxx/bdcdyLsxxTz?proid=" + proid + "&bdcdyid=" + bdcBdcdy.getBdcdyid() + "&bdclx=" + bdcBdcdy.getBdclx();
                    } else{
                        url = bdcdjUrl + "/bdcdyLsxx?wiid=" + bdcXm.getWiid();
                    }
                }
            }

        }
        response.sendRedirect(url);
    }

    //历史信息弹出查看时的跳转页面
    @RequestMapping(value = "bdcdyLsxxTz", method = RequestMethod.GET)
    public String bdcdyLsxxTz(Model model,String proid,String bdcdyid,String bdclx) {
        String analysisUrl = AppConfig.getProperty("analysis.url");
        model.addAttribute("analysisUrl",analysisUrl);
        model.addAttribute("proid",proid);
        model.addAttribute("bdcdyid",bdcdyid);
        model.addAttribute("bdclx",bdclx);
        return "query/bdcdyLsxxTz";
    }
}
