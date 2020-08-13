package cn.gtmap.estateplat.server.web.query;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;

@Controller
@RequestMapping("/bdcDjbQuery")
public class BdcDjbQueryController extends BaseController {
    @Autowired
    private Repo repository;

    @RequestMapping(value = "")
    public String getDjb(Model model, @RequestParam(value = "wiid", required = false) String wiid) {
        model.addAttribute("bdcdjUrl", bdcdjUrl);
        model.addAttribute("reportUrl", reportUrl);
        model.addAttribute("wiid", wiid);
        return "query/bdcdjbQuery";
    }

    @RequestMapping(value = "cxDjbxx")
    public String cxDjbxx(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        model.addAttribute("bdcdjUrl", bdcdjUrl);
        model.addAttribute("reportUrl", reportUrl);
        model.addAttribute("wiid", wiid);
        model.addAttribute("proid", proid);
        return "query/bdcdjCxDjbxx";
    }

    @ResponseBody
    @RequestMapping(value = "/djbQueryList")
    public Object queryDjb(Pageable pageable, String wiid, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNoneBlank(dcxc)) {
            map.put("zl", StringUtils.deleteWhitespace(dcxc));
        }
        if (StringUtils.isNoneBlank(wiid)) {
            map.put("wiid", StringUtils.deleteWhitespace(wiid));
        }
        return repository.selectPaging("queryBdcdjbByPage", map, pageable);
    }

}
