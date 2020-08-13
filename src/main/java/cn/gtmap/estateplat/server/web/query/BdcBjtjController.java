package cn.gtmap.estateplat.server.web.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcZdSqlx;
import cn.gtmap.estateplat.server.core.mapper.BdcdjtjMapper;
import cn.gtmap.estateplat.server.web.main.BaseController;

/**
 * 办件统计查询并导出EXCEL
 * @author  wuhongrui
 * 2016年11月1日
 * @version v1.0 
 * @description
 */
@Controller
@RequestMapping("/bdcbjtj")
public class BdcBjtjController extends BaseController {

	@Autowired
	private Repo repository;
	@Autowired
	private BdcdjtjMapper bdcdjtjMapper;

	private static final String PARAMETER_CXQSSJ = "cxqssj";
	private static final String PARAMETER_CXJSSJ = "cxjssj";

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("bdcdjUrl", bdcdjUrl);
		model.addAttribute("reportUrl", reportUrl);
		return "query/bdcBjtj";
	}

	@ResponseBody
	@RequestMapping("/getBdcdjtjPagesJson")
	public Object getBdcdjtjPagesJson(Pageable pageable, String cxqssj,
			String cxjssj) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(cxqssj)) {
			map.put(PARAMETER_CXQSSJ, cxqssj);
		}

		if (StringUtils.isNotBlank(cxjssj)) {
			map.put(PARAMETER_CXJSSJ, cxjssj);
		}
		return repository.selectPaging("getBdcdjtjByPage",map, pageable);
	}

	/**
	 * 分类导出EXCEL
	 * @author wuhongrui
	 * @description
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exporExcel")
	public Object exporExcel(HttpServletRequest request,
			HttpServletResponse response,String cxjssj,String cxqssj) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(cxjssj)){
			map.put(PARAMETER_CXJSSJ, cxjssj);
		}
		if(StringUtils.isNotBlank(cxqssj)){
			map.put(PARAMETER_CXQSSJ, cxqssj);
		}
		@SuppressWarnings("rawtypes")
		List rowList = new ArrayList();
		List list = null;
	    List sqlxList = new ArrayList();
		
		List<BdcZdSqlx> bdcZdSqlxList = bdcZdGlService.getBdcSqlx();
		if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
			for (int i = 0; i < bdcZdSqlxList.size(); i++) {
				map.put("sqlx", bdcZdSqlxList.get(i).getDm());
				sqlxList.add(bdcZdSqlxList.get(i).getMc());
			    list = bdcdjtjMapper.getBdcdjtjByPage(map);
				rowList.add(list);
			}
		}
        
		model.put(PARAMETER_CXQSSJ, cxqssj);
		model.put(PARAMETER_CXJSSJ, cxjssj);
		model.put("rowList", rowList);
		model.put("sqlxList", sqlxList);
		return new ModelAndView(new ViewExcel(), model);
	}

}
