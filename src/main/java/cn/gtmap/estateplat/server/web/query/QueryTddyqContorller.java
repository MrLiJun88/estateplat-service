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
 * 查询土地抵押权
 * User: wangchangzhou
 * Date: 16-04-07
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/queryTddyq")
public class QueryTddyqContorller extends BaseController {

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
        return "query/tddyqList";
    }

    @ResponseBody
    @RequestMapping("/getTddyqPagesJson")
    public Object getTddyqPagesJson(Pageable pageable, String dyqr, String dyr, String tdzh,String zl, String dydjzmh, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc.trim()));
        } else {
            if (StringUtils.isNotBlank(dyqr)) {
                map.put("dyqr", dyqr.trim());
            }
            if (StringUtils.isNotBlank(dyr)) {
                map.put("dyr", dyr.trim());
            }
            if(StringUtils.isNotBlank(tdzh)) {
            	map.put("tdzh", tdzh.trim());
            }
            if(StringUtils.isNotBlank(zl)) {
                map.put("zl", zl.trim());
            }
            if(StringUtils.isNotBlank(dydjzmh)) {
                map.put("dydjzmh", dydjzmh.trim());
            }
        }
        return repository.selectPaging("getTddyqByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getQlrByDyid")
    public HashMap getQlrByDyid(String dyid) {
        HashMap resultMap = new HashMap();

        //获得抵押权人
        String dyqr =  gdQlrService.getGdQlrsByQlid(dyid, Constants.QLRLX_QLR);
        if (StringUtils.isNotBlank(dyqr)){
            resultMap.put("dyqr",dyqr);
        }else{
            resultMap.put("dyqr","");
        }

        //获取抵押人
        String dyr = gdQlrService.getGdQlrsByQlid(dyid, Constants.QLRLX_YWR);
        if (StringUtils.isNotBlank(dyr)){
            resultMap.put("dyr",dyr);
        }else{
            resultMap.put("dyr","");
        }

        return resultMap;
    }


}
