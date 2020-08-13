package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.server.web.main.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: zuozhengwei
 * Date: 15-3-29
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 * doc:房屋权利人查询
 */
@Controller
@RequestMapping("/fwqlr")
public class FwqlrController extends BaseController {
    @Autowired
    private Repo repository;


    /*楼盘表查询**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        model.addAttribute("proid", proid);
        model.addAttribute("bdcdyh", bdcdyh);
        return "query/fwqlr";
    }

    @ResponseBody
    @RequestMapping("/getFwqlrPagesJson")
    public Object getFwqlrPagesJson(Pageable pageable, String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh) {
        HashMap<String, String> map = new HashMap<String, String>();
        Page<HashMap> dataPaging = null;
        //zwq 有yproid的取原项目的djid
        if(StringUtils.isBlank(bdcdyh)){
            return null;
        }

        //hqz 修改了qlr查找逻辑
        map.put("bdcdyh",bdcdyh);
        dataPaging = repository.selectPaging("queryDjsjBdcdyListByPage", map, pageable);
        String queryDjid = "";
        if(null != dataPaging && dataPaging.getItemSize() > 0){
            queryDjid = (String) dataPaging.getRow(0).get("ID");
        }
        if (StringUtils.isNotBlank(queryDjid)) {
            map.put("id", queryDjid);
            return repository.selectPaging("getDjsjQlrByPage", map, pageable);
        }
        return null;
    }
}
