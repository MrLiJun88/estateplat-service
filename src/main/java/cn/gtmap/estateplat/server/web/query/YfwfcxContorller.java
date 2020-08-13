package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;


@Controller
@RequestMapping("/lsfccx")
public class YfwfcxContorller extends BaseController {
	
	@Autowired
	SqlSession sqlSession;
	
	@Autowired
	private Repo repository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("bdcdjUrl", bdcdjUrl);
		model.addAttribute("reportUrl", reportUrl);
		return "query/yfwfcx";
	}
	
	@ResponseBody
	@RequestMapping ("/getFwxxByPagesJson")
	public Object getBdcdyPagesJson(Pageable pageable,String xms,String sfzhs) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String xm[] = null;
		String sfzh[] = null;
		if (StringUtils.isNotBlank(xms)) {
			xm = xms.split(",");
		}
		if (StringUtils.isNotBlank(sfzhs)) {
			sfzh = sfzhs.split(",");
		}
		StringBuilder builder = new StringBuilder();
		if (null != xm && xm.length > 0&&sfzh != null&&xm.length == sfzh.length) {
			for (int i = 0; i < xm.length; i++) {
				if (0 != i) {
					builder.append(" or ");
				} else {
					builder.append(" and ");
				}
				builder.append(" (((c.qlrzjh ='").append(sfzh[i]).append("' )");
				if (18 == sfzh[i].length()) {
					String oldSfzh = sfzh[i].substring(0, 6)
							+ sfzh[i].substring(8, 17);
					builder.append("   or c.qlrzjh = '").append(oldSfzh)
							.append("'");
				}
				builder.append(" ) and  c.qlrmc='").append(xm[i]).append("' )");
			}
		}
		StringBuilder gdBuilder = new StringBuilder();
		if (null != xm && xm.length > 0&&sfzh != null&&xm.length == sfzh.length) {
			for (int i = 0; i < xm.length; i++) {
				if (0 != i) {
					gdBuilder.append(" or ");
				} else {
					gdBuilder.append(" and ");
				}
				gdBuilder.append(" ( ((t1.qlrzjh ='").append(sfzh[i])
						.append("')");
				if (18 == sfzh[i].length()) {
					String oldSfzh = sfzh[i].substring(0, 6)
							+ sfzh[i].substring(8, 17);
					gdBuilder.append("  or t1.qlrzjh = '").append(oldSfzh)
							.append("'");
				}
				gdBuilder.append(" ) and  t1.qlr= '").append(xm[i]).append("'")
						.append(" )");
			}
		}
		map.put("builder", builder);
		map.put("gdBuilder", gdBuilder);
		return repository.selectPaging("getFwXmxxByPage",map, pageable);
	}
}
