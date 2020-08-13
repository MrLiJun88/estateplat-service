package cn.gtmap.estateplat.server.web.query;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.BdcZdDjlx;
import cn.gtmap.estateplat.model.server.core.BdcZdQllx;
import cn.gtmap.estateplat.model.server.core.BdcZdSqlx;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zzhw
 * Date: 15-3-20
 * Time: 下午4:43
 * doc:审批表查询
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/spb")
public class SpbController extends BaseController {
    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private Repo repository;

    @Autowired
    private BdcZdGlService bdcZdGlService;

    /*审批表查询**/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String proid) {
        List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
        List<BdcZdQllx> qllxList = bdcZdGlService.getBdcQllx();
        List<BdcZdSqlx> sqlxList = bdcZdGlService.getBdcSqlxList();

        String sqspGjss = AppConfig.getProperty("sqspGjss.order");
        List<String> sqspGjssOrderList = new ArrayList<String>();
        if(StringUtils.isNotBlank(sqspGjss) && sqspGjss.split(",").length > 0){
            for(String sqspGjssZd : sqspGjss.split(",")){
                sqspGjssOrderList.add(sqspGjssZd);
            }
        }
        model.addAttribute("sqspGjss", sqspGjss);
        model.addAttribute("sqspGjssOrderList", sqspGjssOrderList);


        model.addAttribute("djListJson", JSONObject.toJSONString(djlxList));
        model.addAttribute("qlListJson", JSONObject.toJSONString(qllxList));
        model.addAttribute("sqlxListJson", JSONObject.toJSONString(sqlxList));
        model.addAttribute("djList", djlxList);
        model.addAttribute("qlList", qllxList);
        model.addAttribute("sqList", sqlxList);
        model.addAttribute("proid", proid);
        return "query/spbList";
    }

    @ResponseBody
    @RequestMapping("/getSpbPagesJson")
    public Object getBdcqzPagesJson(Pageable pageable, String qlr, String ywr, String bdcdyh, String sqlx, String qllx, String sqzsbs, String sqfbcz, String zl, String dcxc) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        } else {
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if (StringUtils.isNotBlank(ywr)) {
                map.put("ywr", ywr);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put("bdcdyh", StringUtils.deleteWhitespace(bdcdyh));
            }
            if (StringUtils.isNotBlank(sqlx)) {
                map.put("sqlx", sqlx);
            }
            if (StringUtils.isNotBlank(qllx)) {
                map.put("qllx", qllx);
            }
            if (StringUtils.isNotBlank(sqzsbs)) {
                map.put("sqzsbs", sqzsbs);
            }
            if (StringUtils.isNotBlank(sqfbcz)) {
                map.put("sqfbcz", sqfbcz);
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (StringUtils.isNotBlank(userDwdm))
            map.put("xzqdm", userDwdm);
        Page<HashMap> dataPaging = repository.selectPaging("getSpbByPage", map, pageable);
        return dataPaging;
    }

}
