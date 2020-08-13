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



@RequestMapping("/fccx")
public class FccxContorller extends BaseController {
    @Autowired
    private Repo repository;
 
    /*审批表选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("bdcdjUrl", bdcdjUrl);
        model.addAttribute("reportUrl", reportUrl);
        return "query/fccx";
    }

    @ResponseBody
    @RequestMapping("/getFcxxByPagesJson")
    public Object getBdcdyPagesJson(Pageable pageable, String sfzh, String xm) {
        HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(sfzh)) {
			map.put("sfzh", StringUtils.deleteWhitespace(sfzh.trim()));
		}
		if (StringUtils.isNotBlank(xm)) {
			map.put("xm", xm.trim());
		}
        return repository.selectPaging("getFcxxByPage", map, pageable);
    }
}
