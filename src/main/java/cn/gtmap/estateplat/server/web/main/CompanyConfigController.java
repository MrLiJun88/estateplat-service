/**
 * 
 */
package cn.gtmap.estateplat.server.web.main;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import com.alibaba.fastjson.JSONObject;

import cn.gtmap.estateplat.model.server.core.BdcXtQy;
import cn.gtmap.estateplat.model.server.core.BdcZdZjlx;
import cn.gtmap.estateplat.server.core.service.BdcCompanyService;

/**
 * @author wangtao
 * @version  2016年4月20日
 * @description 对企业配置信息进行增删改查
 */
@Controller
@RequestMapping("/bdcConfig")
public class CompanyConfigController extends BaseController {
	
	
	@Autowired
    private Repo repository;
	
	@Autowired
	private BdcCompanyService bdcCompanyService ;
	/**
	 * 
	 * @author wangtao
	 * @description 查询证件类型为模板提供下拉框选项
	 * @param 
	 * @return String
	 */
	@RequestMapping("/toCompanyConfig")
    public String toCompanyConfig(Model model) {     
		List<BdcZdZjlx> bdcZdZjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("bdcZdZjlxListJosn", JSONObject.toJSONString(bdcZdZjlxList));
        model.addAttribute("bdcZdZjlxList", bdcZdZjlxList);
        return "config/company";
    }
     
	
	/**
	 * 
	 * @author wangtao
	 * @description 查询企业信息分页数据
	 * @param 
	 * @return Object
	 */
	
	@ResponseBody
    @RequestMapping("/getCompanyPagesJson")
    public Object getCompanyPagesJson(Pageable pageable, String sidx, String sord, BdcXtQy bdcXtQy) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        return repository.selectPaging("getCompanyListByPage", map, pageable);
         
    }

   /**
    * 
    * @author wangtao
    * @description 保存企业信息
    * @param 
    * @return HashMap
    */
    @ResponseBody
    @RequestMapping("/saveCompany")
    public HashMap saveCompany(Model model, BdcXtQy bdcXtQy) {
        HashMap map = new HashMap();
        String result = "保存成功";
        try {
        	bdcCompanyService.insertOrUpdateByPrimaryKey(bdcXtQy);
        } catch (Exception e) {
            logger.error("CompanyConfigController.saveCompany",e);
            result = "保存失败";
        }
        map.put("result", result);
        return map;
    }

    /**
     * 
     * @author wangtao
     * @description 删除企业信息
     * @param 
     * @return HashMap
     */
    @ResponseBody
    @RequestMapping("/delCompany")
    public HashMap delCompany(Model model, String ids) {
        HashMap map = new HashMap();
        String result = "删除成功！";
        try {
        	bdcCompanyService.deleteCompanyByPrimaryKey(ids);
        } catch (Exception e) {
            result = "删除失败！";
            logger.error("CompanyConfigController.delCompany",e);
        } finally {
            map.put("result", result);
        }
        return map;
    }
}
