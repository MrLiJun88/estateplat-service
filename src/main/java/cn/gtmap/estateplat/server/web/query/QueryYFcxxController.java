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
@RequestMapping("/queryYFcxx")
public class QueryYFcxxController extends BaseController {
	@Autowired
	private Repo repository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("reporturl", reportUrl);
		return "query/yfcdjcxList";
	}

	@ResponseBody
	@RequestMapping(value = "/initList", method = RequestMethod.GET)
	public Object initList(Pageable pageable, String dcxc, String ghyt,
			String fwzl, String fwjg, String jzmj, String qlr, String fczh) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
			map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
		
			map.put("ghyt", StringUtils.deleteWhitespace(ghyt));
			map.put("fwzl", StringUtils.deleteWhitespace(fwzl));
			map.put("fwjg", StringUtils.deleteWhitespace(fwjg));
			map.put("jzmj", StringUtils.isNotBlank(jzmj)?Double.valueOf(jzmj):null);
			map.put("qlr", StringUtils.deleteWhitespace(qlr));
			map.put("fczh", StringUtils.deleteWhitespace(fczh));
			
		return repository.selectPaging("queryGdFwsyqByPage", map, pageable);
	}

	@ResponseBody
	@RequestMapping(value = "/getgdfwxxbyfczh", method = RequestMethod.GET)
	public Object getGdFwxxByFczh(String fczh) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fczh", StringUtils.deleteWhitespace(fczh));
		return repository.selectOne("getGdFwxxByFczh", map);
	}
	
	@RequestMapping(value = "/getDetailsByFczh", method = RequestMethod.GET)
	public String getDetailsByFczh(Model model,String fczh) {
		model.addAttribute("reporturl", reportUrl);
		model.addAttribute("fczh", fczh);
		return "query/yfcQuery";
	}

	@RequestMapping(value = "/yfcxxQueryList", method = RequestMethod.GET)
	public String yfcxxQueryList(Model model,String fczh){
		model.addAttribute("fczh", fczh);
		return "query/yfcxxQueryList";
	}
	@RequestMapping(value = "/yfcCqxxQueryList", method = RequestMethod.GET)
	public String yfcCqxxQueryList(Model model,String fczh){
		model.addAttribute("fczh", fczh);
		return "query/yfcCqxxQueryList";
	}
	@RequestMapping(value = "/yfcCfxxQueryList", method = RequestMethod.GET)
	public String yfcCfxxQueryList(Model model,String fczh){
		model.addAttribute("fczh", fczh);
		return "query/yfcCfxxQueryList";
	}
	@RequestMapping(value = "/yfcDyaxxQueryList", method = RequestMethod.GET)
	public String getYfc(Model model,String fczh){
		model.addAttribute("fczh", fczh);
		return "query/yfcDyaxxQueryList";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getYfcxxListByFczh", method = RequestMethod.GET)
	public Object getYfcxxListByFczh(Pageable pageable,String fczh) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fczh", StringUtils.deleteWhitespace(fczh));
		return repository.selectPaging("getYfcxxListByFczh", map, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getYfcCqxxListByFczh", method = RequestMethod.GET)
	public Object getYfcCqxxListByFczh(Pageable pageable,String fczh) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fczh", StringUtils.deleteWhitespace(fczh));
		return repository.selectPaging("getYfcCqxxListByFczh", map, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getYfcCfxxListByFczh", method = RequestMethod.GET)
	public Object getYfcCfxxListByFczh(Pageable pageable,String fczh) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fczh", StringUtils.deleteWhitespace(fczh));
		return repository.selectPaging(	"getYfcCfxxListByFczh", map, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getYfcDyaxxListByFczh", method = RequestMethod.GET)
	public Object getYfcDyaxxListByFczh(String fczh,Pageable pageable) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fczh", StringUtils.deleteWhitespace(fczh));
		return repository.selectPaging(	"getYfcDyaxxListByFczh", map, pageable);
	}
}
