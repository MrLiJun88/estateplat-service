package cn.gtmap.estateplat.server.web.query;


import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;


@Controller
@RequestMapping("/zjgFccx")
public class ZjgFccxContorller extends BaseController {
    @Autowired
    private Repo repository;
 
    /*审批表选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("bdcdjUrl", bdcdjUrl);
        model.addAttribute("reportUrl", reportUrl);
        return "query/zjgFccx";
    }

    @ResponseBody
    @RequestMapping("/getZjgFcxxByPagesJson")
    public Object getBdcdyPagesJson(Pageable pageable, String sfzh, String xm) {
        HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(sfzh)) {
			map.put("sfzh", StringUtils.deleteWhitespace(sfzh.trim()));
			if(18==sfzh.trim().length()){
				String oldSfzh=sfzh.substring(0, 6)+sfzh.substring(8, 17);
				map.put("oldSfzh", oldSfzh);
			}
		}
		if (StringUtils.isNotBlank(xm)) {
			map.put("xm", xm.trim());
		}
        return repository.selectPaging("getJtFcxxByPage", map, pageable);
    }
}
