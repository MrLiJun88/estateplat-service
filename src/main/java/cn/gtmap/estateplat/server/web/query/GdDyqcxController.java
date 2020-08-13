package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.GdZdFcxtDjlx;
import cn.gtmap.estateplat.server.core.service.GdQlrService;
import cn.gtmap.estateplat.server.core.service.GdXmService;
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
 * Created with IntelliJ IDEA.
 * User: yinyao
 * Date: 15-11-28
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/gdDyqcx")
public class GdDyqcxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private GdQlrService gdQlrService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        //取得过渡登记类型字典
        List<GdZdFcxtDjlx> gdDjlxList = gdXmService.getGdZdFcxtDjlx();
        model.addAttribute("gdDjlxList", JSONObject.toJSONString(gdDjlxList));
        return "query/gdDyq";
    }

    @ResponseBody
    @RequestMapping("/getgdDyqPagesJsonace")
    public Object getgdSyqPagesJsonace(Pageable pageable, String dcxc, String fczh, String dyr,String dyqr,String fwzl,String dydjzmh) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(fczh))
                map.put("fczh", fczh.trim());
            if (StringUtils.isNotBlank(dyr))
                map.put("dyr", dyr.trim());
            if (StringUtils.isNotBlank(dyqr))
                map.put("dyqr", dyqr.trim());
            if (StringUtils.isNotBlank(fwzl))
                map.put("fwzl",fwzl.trim());
            if (StringUtils.isNotBlank(dydjzmh))
                map.put("dydjzmh",dydjzmh.trim());
        }
        return repository.selectPaging("getGdDYQByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getGdDyQlr")
    public HashMap getGdDyQlr(String qlid){
        HashMap map = new HashMap();
        //获得抵押权人
        String dyqr = gdQlrService.getGdQlrsByQlid(qlid, Constants.QLRLX_QLR);
        if (StringUtils.isNotBlank(dyqr)){
            map.put("dyqr",dyqr);
        }else{
            map.put("dyqr","");
        }

        //获取抵押人
        String dyr = gdQlrService.getGdQlrsByQlid(qlid, Constants.QLRLX_YWR);
        if (StringUtils.isNotBlank(dyr)){
            map.put("dyr",dyr);
        }else{
            map.put("dyr","");
        }

        return map;
    }
}

