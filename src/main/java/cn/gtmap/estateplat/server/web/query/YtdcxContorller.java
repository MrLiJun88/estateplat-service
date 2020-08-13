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
@RequestMapping("/ytdcx")
public class YtdcxContorller extends BaseController {
	@Autowired
	private Repo repository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("bdcdjUrl", bdcdjUrl);
		model.addAttribute("reportUrl", reportUrl);
		return "query/ytddjcxList";
	}

	@ResponseBody
	@RequestMapping("/getYtdxxByPagesJson")
	public Object getBdcdyPagesJson(Pageable pageable, String dcxc, String qlr,
			String tdzh, String yt, String syqmj, String djh, String tdzl) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dcxc)) {
			map.put("dcxc", dcxc.trim());
		} else {
			if (StringUtils.isNotBlank(qlr)) {
				map.put("qlr", qlr.trim());
			}
			if (StringUtils.isNotBlank(tdzh)) {
				map.put("tdzh", StringUtils.deleteWhitespace(tdzh.trim()));
			}
			if (StringUtils.isNotBlank(yt)) {
				map.put("yt", StringUtils.deleteWhitespace(yt.trim()));
			}
			if (StringUtils.isNotBlank(syqmj)) {
				map.put("syqmj", StringUtils.deleteWhitespace(syqmj.trim()));
			}
			if (StringUtils.isNotBlank(djh)) {
				map.put("djh", StringUtils.deleteWhitespace(djh.trim()));
			}
			if (StringUtils.isNotBlank(tdzl)) {
				map.put("tdzl", StringUtils.deleteWhitespace(tdzl.trim()));
			}
		}
		return repository.selectPaging("getYtdxxByPage",map, pageable);
	}
}
