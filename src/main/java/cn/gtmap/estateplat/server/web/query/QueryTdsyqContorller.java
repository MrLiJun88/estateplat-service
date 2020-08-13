package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.GdZdFcxtDjlx;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * 查询土地使用权
 * User: wangchangzhou
 * Date: 16-04-07
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryTdsyq")
public class QueryTdsyqContorller extends BaseController {

    @Autowired
    private Repo repository;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private GdQlrService gdQlrService;

    /*审批表选择不动产单元**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        //取得过渡登记类型
        List<GdZdFcxtDjlx> gdDjlxList = gdXmService.getGdZdFcxtDjlx();
        model.addAttribute("gdDjlxList", JSONObject.toJSONString(gdDjlxList));
        return "query/tdsyqList";
    }

    @ResponseBody
    @RequestMapping("/getTdsyqPagesJson")
    public Object getTdsyqPagesJson(Pageable pageable, String zl, String qlr, String tdzh, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
        } else {
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl.trim());
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr.trim());
            }
           if(StringUtils.isNotBlank(tdzh)) {
            	map.put("tdzh", tdzh.trim());
            }
        }
        return repository.selectPaging("getTdsyqByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getQlrByQlid")
    public HashMap getQlrByQlid(String qlid) {
        HashMap resultMap = new HashMap();

        //获得权利人
        String qlr =gdQlrService.getGdQlrsByQlid(qlid, Constants.QLRLX_QLR);
        if (StringUtils.isNotBlank(qlr)){
            resultMap.put("qlr_str",qlr);
        }else{
            resultMap.put("qlr_str","");
        }
        return resultMap;
    }

}
