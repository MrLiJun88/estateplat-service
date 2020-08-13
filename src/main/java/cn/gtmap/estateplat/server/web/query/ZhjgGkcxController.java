package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcZdQszt;
import cn.gtmap.estateplat.server.core.service.ZjgGkcxService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/zhjggkcx")
public class ZhjgGkcxController extends BaseController {
	@Autowired
	private Repo repository;
	@Autowired
	private ZjgGkcxService zjgGkcxService;

	@RequestMapping("")
	public String initList(Model model) {
		List<BdcZdQszt> qsztList = bdcZdGlService.getBdcZdQszt();
		model.addAttribute("qsztListJson", JSONObject.toJSONString(qsztList));
		model.addAttribute("qsztList", qsztList);
		return "query/zhjggkcx";
	}

	@ResponseBody
	@RequestMapping("/getZhjgCxxList")
	public Object getZhjgCxxList(Pageable pageable, String qlr, String bdcdyh,
			String bdcqzh, String qszt, String dcxc,
			String fzqssj, String fzjssj, String zl,String[] fanwei) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dcxc)) {
			map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
		} else {
			if (StringUtils.isNotBlank(qlr)) {
				map.put("qlr", StringUtils.deleteWhitespace(qlr));
			}
			if (StringUtils.isNotBlank(bdcdyh)) {
				map.put("bdcdyh", StringUtils.deleteWhitespace(bdcdyh));
			}
			if (StringUtils.isNotBlank(bdcqzh)) {
				map.put("bdcqzh", StringUtils.deleteWhitespace(bdcqzh));
			}
			if (StringUtils.isNotBlank(qszt)) {
				map.put("qszt", StringUtils.deleteWhitespace(qszt));
			}
			if (StringUtils.isNotBlank(zl)) {
				map.put("zl", StringUtils.deleteWhitespace(zl));
			}
			if (StringUtils.isNotBlank(fzqssj)) {
				map.put("fzqssj", fzqssj.trim());
			}
			if (StringUtils.isNotBlank(fzjssj)) {
				map.put("fzjssj", fzjssj.trim());
			}
		}

		if(fanwei != null && fanwei.length>0){//如果页面已做勾选，则按勾选内容
			for(String range:fanwei){
				if(StringUtils.isNotBlank(range)){
					if(range.equals("td")){
						map.put("notExcludeTD", "yes");//是否查询gd_td相关信息
					}else if(range.equals("fw")){
						map.put("notExcludeFW", "yes");//是否查询gd_fw相关信息
					}else if(range.equals("dj")){
						map.put("notExcludeDJ", "yes");//是否查询bdc_相关信息
					}
				}
			}
		}else{//如果页面未做勾选，则显示全部
			map.put("notExcludeTD", "yes");//是否查询gd_td相关信息
			map.put("notExcludeFW", "yes");//是否查询gd_fw相关信息
			map.put("notExcludeDJ", "yes");//是否查询bdc_相关信息
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/getDetailsBYzsidZhjg")
	public Object getDetailsBYzsidZhjg(@RequestParam(value = "zsid", required = false) String zsid) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("zsid", zsid);
		return repository.selectOne("getDeatilsBYzsidZhjg",map);
	}
	
	@ResponseBody
	@RequestMapping("/getDycfXXByZsidZhjg")
	public void getDycfXXByZsidZhjg(@RequestParam(value = "zsid", required = false) String zsid,HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("zsid", zsid);
		List<Map> cflist = zjgGkcxService.getCfxxByZsid(map);
		List<Map> dylist = zjgGkcxService.getDyaxxByZsid(map);
		HashMap<String, String> r = new HashMap<String, String>();
		String dy="no";
		String cf="no";
		String zx="no";
		String zc="no";
		if(CollectionUtils.isNotEmpty(cflist)) {
			for (Map result : cflist) {
				if (StringUtils.isNotBlank(result.get("LY") != null ? result.get("LY").toString() : null)
						&&"cf".equals(result.get("LY").toString())
				&&"1".equals(result.get("QSZT").toString())) {
					cf = "yes";
					break;
				}
			}
		}
		if(CollectionUtils.isNotEmpty(dylist)) {
			for (Map result : dylist) {
				if (StringUtils.isNotBlank(result.get("LY") != null ? result.get("LY").toString() : null)
						&&"dy".equals(result.get("LY").toString())
						&&"1".equals(result.get("QSZT").toString())) {
					dy = "yes";
					break;
				}
			}
		}
		if(CollectionUtils.isEmpty(cflist)&&CollectionUtils.isEmpty(dylist)){
			zc="yes";
		}
		if(zc!="yes"&&dy!="yes"&&cf!="yes"){
			zx="yes";
		}
		r.put("dy", dy);
		r.put("cf", cf);
		r.put("zx", zx);
		r.put("zc", zc);
		out.print(JSONObject.toJSONString(r));
		out.flush();
		out.close();
	}
}
