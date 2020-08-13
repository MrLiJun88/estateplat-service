package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.GdZdFcxtDjlx;
import cn.gtmap.estateplat.server.core.service.GdQlrService;
import cn.gtmap.estateplat.server.core.service.GdXmService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
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
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yinyao
 * Date: 15-11-28
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/gdSyqcx")
public class GdSyqcxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private GdQlrService gdQlrService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model,@RequestParam(value = "gdtd", required = false) String gdtd) {
        //取得过渡登记类型字典
        List<GdZdFcxtDjlx> gdDjlxList = gdXmService.getGdZdFcxtDjlx();
        String fcBaseUrl = AppConfig.getProperty("fc.baseUrl");
        model.addAttribute("fcBaseUrl",fcBaseUrl);
        model.addAttribute("gdDjlxList", JSONObject.toJSONString(gdDjlxList));
        String path= "query/gdSyq";
        if(StringUtils.isNotBlank(gdtd)){
            path="query/gdTdsyq";
        }
        return path;
    }

    @ResponseBody
    @RequestMapping("/getgdSyqPagesJsonace")
    public Object getgdSyqPagesJsonace(Pageable pageable, String dcxc, String fczh, String qlr,String fwzl) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(fczh))
                map.put("fczh", fczh.trim());
            if (StringUtils.isNotBlank(qlr))
                map.put("qlr", qlr.trim());
            if (StringUtils.isNotBlank(fwzl))
                map.put("fwzl",fwzl.trim());
        }
        return repository.selectPaging("getGdSYQByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getGdQlr")
    public HashMap getGdQlr(String qlid){
        HashMap<String, Object> map = new HashMap<String, Object>();
        //获得抵押权人
        String dyqr = gdQlrService.getGdQlrsByQlid(qlid, Constants.QLRLX_QLR);
        if (StringUtils.isNotBlank(dyqr)){
            map.put("QLR",dyqr);
        }else{
            map.put("QLR","");
        }
        return map;
    }

    /**
     * @author bianwen
     * @param
     * @return
     * @description
     */
    @ResponseBody
    @RequestMapping("/getGdTdsyqPageJson")
    public Object getGdTdsyqPageJson(Pageable pageable,String dcxc, String tdzh, String zl, String qlr,String djh) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(tdzh))
                map.put("tdzh", tdzh.trim());
            if (StringUtils.isNotBlank(qlr))
                map.put("qlr", qlr.trim());
            if (StringUtils.isNotBlank(zl))
                map.put("zl",zl.trim());
            if (StringUtils.isNotBlank(djh))
                map.put("djh",djh.trim());
        }
        return repository.selectPaging("getGdTdsyqByPage", map, pageable);
    }
}

