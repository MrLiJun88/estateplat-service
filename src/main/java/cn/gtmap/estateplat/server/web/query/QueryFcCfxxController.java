package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
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

/**
 * 查看查封信息
 * User: changzhe
 * Date: 16-04-07
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryFcCfxx")
public class QueryFcCfxxController extends BaseController {
	@Autowired
	private Repo repository;
	@Autowired
	private GdQlrService gdQlrService;

	@RequestMapping(value = "",method = RequestMethod.GET)
	public String index(Model model){
		return "query/fcCfxxList";
	}

	@ResponseBody
	@RequestMapping("/getFcCfxxPagesJson")
	public Object getBdcdyPagesJson(Pageable pageable, String cfjg, String qlr, String fczh, String zl ,String dcxc) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dcxc)) {
			map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
		} else {
			if (StringUtils.isNotBlank(cfjg)) {
				map.put("cfjg", cfjg.trim());
			}
			if (StringUtils.isNotBlank(qlr)) {
				map.put("qlr",qlr.trim() );
			}
			if(StringUtils.isNotBlank(fczh)) {
				map.put("fczh", StringUtils.deleteWhitespace(fczh.trim()));
			}
			if(StringUtils.isNotBlank(zl)) {
				map.put("zl", zl.trim());
			}
		}
		return repository.selectPaging("getFcCfxxByPage", map, pageable);
	}

	@ResponseBody
	@RequestMapping("/getQlrByCfid")
	public HashMap getQlrByCfid(String cfid) {
		HashMap resultMap = new HashMap();
		//获得权利人
		String qlr = gdQlrService.getGdQlrsByQlid(cfid, Constants.QLRLX_QLR);
		if (StringUtils.isNotBlank(qlr)){
			resultMap.put("qlr_str",qlr);
		}else{
			resultMap.put("qlr_str","");
		}
		return resultMap;
	}

}
