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
@RequestMapping("/haBdcxm")
public class HaBdcxmContorller extends BaseController {
	@Autowired
	private Repo repository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("bdcdjUrl", bdcdjUrl);
		model.addAttribute("reportUrl", reportUrl);
		return "query/haBdcxmList";
	}

	@ResponseBody
	@RequestMapping("/getBdcxmByPagesJson")
	public Object getBdcdyPagesJson(Pageable pageable, String dcxc,
			String bdcdyh, String zl, String cqrxm, String cqrzjh, String slbh,String qsrq,String jsrq
			) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dcxc)) {
			map.put("dcxc", dcxc.trim());
		} else {
			if (StringUtils.isNotBlank(bdcdyh)) {
				map.put("bdcdyh", bdcdyh.trim());
			}
			if (StringUtils.isNotBlank(zl)) {
				map.put("zl", StringUtils.deleteWhitespace(zl.trim()));
			}
			if (StringUtils.isNotBlank(cqrxm)) {
				map.put("cqrxm", StringUtils.deleteWhitespace(cqrxm.trim()));
			}
			if (StringUtils.isNotBlank(cqrzjh)) {
				map.put("cqrzjh", StringUtils.deleteWhitespace(cqrzjh.trim()));
			}
			if (StringUtils.isNotBlank(slbh)) {
				map.put("slbh", StringUtils.deleteWhitespace(slbh.trim()));
			}
			if (StringUtils.isNotBlank(qsrq)) {
				map.put("qsrq", qsrq.trim());
			}
			if (StringUtils.isNotBlank(jsrq)) {
				map.put("jsrq", jsrq.trim());
			}
		}
		return repository.selectPaging("getHaBdcxmByPage",map, pageable);
	}

	@ResponseBody
	@RequestMapping("/getHaBdcxmQlxx")
	public Object queryHaBdcxmQlxx(String proid) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("proid", proid);
		return repository.selectOne("getHaBdcxmQlxx", map);
	}
}
