package cn.gtmap.estateplat.server.web.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcZdQszt;
import cn.gtmap.estateplat.server.web.main.BaseController;

@Controller
@RequestMapping("/queryCfxx")
public class QueryCfxxController extends BaseController{
    @Autowired  
	private Repo repository;
    
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String index(Model model){
    	model.addAttribute("reporturl", reportUrl);
    	List<BdcZdQszt> qsztList = bdcZdGlService.getBdcZdQszt();
    	Iterator<BdcZdQszt> iterator = qsztList.iterator();
    	for(;iterator.hasNext();){
    		BdcZdQszt qszt = iterator.next();
    		if(qszt.getMc().contains("现势")){
    			qszt.setMc("否");
    		}else if(qszt.getMc().contains("历史")){
    			qszt.setMc("是");
    		}else{
    			iterator.remove();
    		}
    	}
    	model.addAttribute("qsztListJson",JSONObject.toJSONString(qsztList));
    	model.addAttribute("qsztList",qsztList);
    	return "query/cfxxList"; 
    }
    @ResponseBody
    @RequestMapping("/getCfxxPagesJson")
    public Object getCfxxPagesJson(Pageable pageable,String dcxc,String djh,String bdcdyh,String bcfr,String cfjg,String cfzt){
    	HashMap<String, Object> map =new HashMap<String, Object>();
    	if(StringUtils.isNotBlank(dcxc)){
    		map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
    	}else {
    		if(StringUtils.isNotBlank(djh)){
    			map.put("djh", djh);
    		}
    		if(StringUtils.isNotBlank(bdcdyh)){
    			map.put("bdcdyh", bdcdyh);
    		}
    		if(StringUtils.isNotBlank(bcfr)){
    			map.put("bcfr", bcfr);
    		}
    		if(StringUtils.isNotBlank(cfjg)){
    			map.put("cfjg", cfjg);
    		}
    		if(StringUtils.isNotBlank(cfzt)){
    			map.put("cfzt", cfzt);
    		}
    	}
		return repository.selectPaging("queryCfxxByPage", map, pageable);
    }
      
      
}
