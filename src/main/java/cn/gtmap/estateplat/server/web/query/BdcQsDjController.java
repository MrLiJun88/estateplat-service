package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author  xiejianan
 * 2016年4月27日
 * @version v1.0 
 * @description
 */
@Controller
@RequestMapping("/bdcqsdj")
public class BdcQsDjController extends BaseController{
	 @Autowired
	 private Repo repository;
	 @Autowired
	 private BdcQlrService bdcQlrService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, String proid) {
		model.addAttribute("proid", proid);
		model.addAttribute("reporturl", reportUrl);
		return "query/bdcqsdj";
	}

	@ResponseBody
	@RequestMapping("/getQlrJsonace")
	public Object getbdcCfPagesJsonace(Pageable pageable, String xm, String zs,String zl) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("xm", StringUtils.deleteWhitespace(xm));
		map.put("zs", StringUtils.deleteWhitespace(zs));
		map.put("zl", StringUtils.deleteWhitespace(zl));
		return repository.selectPaging("queryBdcQsByPage", map,pageable);

	}

	@ResponseBody
	@RequestMapping("/getBdcdyidByProid")
	public HashMap getBdcdyidByProid(String qlr, String qlrzjh) throws Exception {
		qlr = URLDecoder.decode(qlr, "UTF-8");
		HashMap resultmap = new HashMap();
		HashMap map = new HashMap();
		map.put("qlr", qlr);
		map.put("qlrzjh", qlrzjh);
		List<Map> bdcdyidlist = bdcQlrService.getBdcdyidByProid(map);
		StringBuilder bdcdyid = new StringBuilder();
		StringBuilder qlrid = new StringBuilder();
		if (CollectionUtils.isNotEmpty(bdcdyidlist)) {
			for (int i = 0; i < bdcdyidlist.size(); i++) {
				bdcdyid.append(bdcdyidlist.get(i).get("BDCDYID")).append(",");
				qlrid.append(bdcdyidlist.get(i).get("QLRID")).append(",");
			}
		}
		resultmap.put("bdcdyid", bdcdyid.substring(0, bdcdyid.length() - 1));
		resultmap.put("qlrid", qlrid.substring(0, qlrid.length() - 1));
		return resultmap;
	}


	@ResponseBody
	@RequestMapping("/getBdcdyhxx")
	public Object getBdcdyhxx(String bdcdyid){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("bdcdyid", StringUtils.deleteWhitespace(bdcdyid));
		return repository.selectOne("getBdcdyXxByBdcdyid", map);
	}

}
