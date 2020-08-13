package cn.gtmap.estateplat.server.web.query;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;

@Controller
@RequestMapping("/yfwfcxjl")
public class YfwfCxjlController extends BaseController {
	@Autowired
	private Repo repository;
	
	 @RequestMapping(value = "", method = RequestMethod.GET)
	    public String indexTdz(Model model) {
		 model.addAttribute("bdcdjUrl", bdcdjUrl);
	     model.addAttribute("reportUrl", reportUrl);	     
	        return "query/yfwfCxjl";
	    }
	 
	 @ResponseBody
	 @RequestMapping("/queryCxjl")
	    public Object getCxjgList(Pageable pageable, 
	    		@RequestParam(value="cxqssj",required = false)String cxqssj,
	    		@RequestParam(value="cxjssj",required = false)String cxjssj) {
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("cxqssj", StringUtils.deleteWhitespace(cxqssj));
	        map.put("cxjssj", StringUtils.deleteWhitespace(cxjssj));
	        map.put("type","yfwf");
	        return repository.selectPaging("queryCxjlByPage", map,pageable);
	    }
	
}
