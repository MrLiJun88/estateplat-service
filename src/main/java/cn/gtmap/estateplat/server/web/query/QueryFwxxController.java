package cn.gtmap.estateplat.server.web.query;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;

@Controller
@RequestMapping("/queryfwxx")
public class QueryFwxxController extends BaseController {
    @Autowired
    private Repo repository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("reporturl", reportUrl);
        return "query/fcxxList";
    }

    @ResponseBody
    @RequestMapping("/getFwxxByPagesJson")
    public Object getBdcdyPagesJson(Pageable pageable, String fwzl, String sfzh, String cqzh) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(fwzl)) {
            map.put("fwzl", StringUtils.deleteWhitespace(fwzl));
        }
        if (StringUtils.isNotBlank(sfzh)) {
            map.put("sfzh", StringUtils.deleteWhitespace(sfzh));
        }
        if (StringUtils.isNotBlank(cqzh)) {
            map.put("cqzh", StringUtils.deleteWhitespace(cqzh));
        }
        return repository.selectPaging("getFwxxByPage", map, pageable);
    }
}
