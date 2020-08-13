package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wuhubdcdjdacx")
public class WuhuDacxController extends BaseController {
	@Autowired
	private QllxService qllxService;
	@Autowired
	private Repo repository;

	public static final String PARAMETER_REPORT_URL = "report.url";
	public static final String PARAMETER_BDCDJ_URL = "bdcdj.url";

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model, String proid) {
		String bdcdjUrl = AppConfig.getProperty(PARAMETER_BDCDJ_URL);
		String reportUrl = AppConfig.getProperty(PARAMETER_REPORT_URL);
		List<HashMap<String, String>> sqlxlist = bdcZdGlService.getTDFWSqlxGddjlx();
		model.addAttribute("sqlxlist", sqlxlist);
		model.addAttribute("bdcdjUrl", bdcdjUrl);
		model.addAttribute("reportUrl", reportUrl);
		return "query/wuhuBdcdjDAcx";
	}

	/**
	 * @author xiejianan
	 * @description
	 * @param model
	 * @param bdcqzh
	 * @param ly
	 * @return
	 * @throws IOException
	 */
	// 用不动产权证号检验是否是一证多房情况，打开不同的报表,现已作废
	@RequestMapping(value = "/openDetails", method = RequestMethod.GET)
	public void fccx(HttpServletResponse response, String bdcqzh, String zsid)
			throws IOException {
		String reportUrl = AppConfig.getProperty(PARAMETER_REPORT_URL);
		String frName = "bdc_djxxcxjg.cpt";// 一证一房对应的报表
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bdcqzh", StringUtils.isNotBlank(bdcqzh) ? bdcqzh.trim() : null);
		HashMap resultmap = repository.selectOne("getBdcdyidNumByBdcqzh", map);
		if (Integer.parseInt(resultmap.get("NUMS").toString()) > 1) {
			frName = "bdc_djcxjgdf.cpt";// 一证多房对应的报表
		}
		response.sendRedirect(reportUrl + "/ReportServer?reportlet=edit%2F"
				+ frName + "&op=write&zsid=" + zsid);
	}

	@ResponseBody
	@RequestMapping(value = "/getBdcqzzt", method = RequestMethod.GET)
	public HashMap getBdcdyQllx(Model model, String bdcdyid) {
		HashMap resultMap = new HashMap();
		List<Map> cfList = null;
		List<Map> dyaList = null;
		if (StringUtils.isNotBlank(bdcdyid)) {
			cfList = qllxService.getBdcGDCfxxByBdcdyid(bdcdyid);
			dyaList = qllxService.getDyaxxByBdcdyid(bdcdyid);
		}
		// true为正常 ，false为查封
		if (CollectionUtils.isEmpty(cfList)) {
			resultMap.put("cf", true);
		} else {
			resultMap.put("cf", false);
		}
		// true为正常，false为抵押
		if (CollectionUtils.isEmpty(dyaList)) {
			resultMap.put("dya", true);
		} else {
			resultMap.put("dya", false);
		}
		return resultMap;
	}

	@ResponseBody
	@RequestMapping("/queryWuhuDAxxList")
	public Object queryFcsysList(Pageable pageable, String dcxc, String cqzh,
			String sqlx, String qlr, String qlrzjh, String fwzl, String yg,
			String wdqlr, String wdqlrzjh) throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dcxc)) {
			map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
		} else {
			if (StringUtils.isNotBlank(wdqlr)
					&& StringUtils.isNotBlank(wdqlrzjh)) {
				map.put("wdqlr", wdqlr.trim());
				map.put("wdqlrzjh", wdqlrzjh.trim());
			} else {
				if (StringUtils.isNotBlank(cqzh)) {
					map.put("cqzh", cqzh.trim());
				}
				if (StringUtils.isNotBlank(sqlx)) {
					map.put("sqlx", sqlx.trim());
				}
				if (StringUtils.isNotBlank(qlr)) {
					map.put("qlr", qlr.trim());
				}
				if (StringUtils.isNotBlank(qlrzjh)) {
					map.put("qlrzjh", qlrzjh.trim());
				}
				if (StringUtils.isNotBlank(fwzl)) {
					map.put("fwzl", fwzl.trim());
				}
			}
		}
		if (StringUtils.isNotBlank(yg)) {
			map.put("yg", yg.trim());
		}
		return  repository.selectPaging("WuhuQueryBdcdjDAListByPage", map, pageable);
	}

	/**
	 * 
	 * @param bdcdyid
	 * @return 二次查询
	 */
	@ResponseBody
	@RequestMapping(value = "getBdcGDxx", method = RequestMethod.GET)
	public Object queryCfdjList(
			@RequestParam(value = "bdcdyid", required = false) String bdcdyid,
			String yg) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bdcdyid", bdcdyid);
		if (StringUtils.isNotBlank(yg)) {
			map.put("yg", yg.trim());
		}
		return repository.selectOne("getWuhuBdcdjDAxxByBdcqzh",	map);
	}
	
	/**
	 * @author xiejianan
	 * @description 金坛比芜湖需求多加一个18位证件号转为15位证件号,故将两者的
	 * @param model
	 * @param proid
	 * @return
	 */
	@RequestMapping(value = "/jintan", method = RequestMethod.GET)
	public String jintanIndex(Model model, String proid) {
		String bdcdjUrl = AppConfig.getProperty(PARAMETER_BDCDJ_URL);
		String reportUrl = AppConfig.getProperty(PARAMETER_REPORT_URL);
		List<HashMap<String, String>> sqlxlist = bdcZdGlService.getTDFWSqlxGddjlx();
		model.addAttribute("sqlxlist", sqlxlist);
		model.addAttribute("bdcdjUrl", bdcdjUrl);
		model.addAttribute("reportUrl", reportUrl);
		return "query/jintanBdcdjDAcx";
	}
	
	@ResponseBody
	@RequestMapping("/queryWuhuDAxxListJintan")
	public Object queryFcsysListJintan(Pageable pageable, String dcxc, String cqzh,
			String sqlx, String qlr, String qlrzjh, String fwzl, String yg,
			String wdqlr, String wdqlrzjh) throws ParseException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dcxc)) {
			map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
		} else {
			if (StringUtils.isNotBlank(wdqlr)
					&& StringUtils.isNotBlank(wdqlrzjh)) {
				map.put("wdqlr", wdqlr.trim());
				map.put("wdqlrzjh", wdqlrzjh.trim());
				if (StringUtils.isNotBlank(wdqlrzjh)&&wdqlrzjh.trim().length()==18) {
					StringBuilder wdqlrzjh2 = new StringBuilder();
					wdqlrzjh2.append(wdqlrzjh.trim().substring(0, 6));
					wdqlrzjh2.append(wdqlrzjh.trim().substring(8, 17));
					map.put("wdqlrzjh2",wdqlrzjh2.toString());
				}
			} else {
				if (StringUtils.isNotBlank(cqzh)) {
					map.put("cqzh", cqzh.trim());
				}
				if (StringUtils.isNotBlank(sqlx)) {
					map.put("sqlx", sqlx.trim());
				}
				if (StringUtils.isNotBlank(qlr)) {
					map.put("qlr", qlr.trim());
				}
				if (StringUtils.isNotBlank(qlrzjh)) {
					map.put("qlrzjh", qlrzjh.trim());
				}
				if (StringUtils.isNotBlank(qlrzjh)&&qlrzjh.trim().length()==18) {
					StringBuilder qlrzjh2 = new StringBuilder();
					qlrzjh2.append(qlrzjh.trim().substring(0, 6));
					qlrzjh2.append(qlrzjh.trim().substring(8, 17));
					map.put("qlrzjh2",qlrzjh2.toString());
				}
				if (StringUtils.isNotBlank(fwzl)) {
					map.put("fwzl", fwzl.trim());
				}
			}
		}
		if (StringUtils.isNotBlank(yg)) {
			map.put("yg", yg.trim());
		}
		return repository.selectPaging("WuhuQueryBdcdjDAListByPage", map, pageable);
	}
}
