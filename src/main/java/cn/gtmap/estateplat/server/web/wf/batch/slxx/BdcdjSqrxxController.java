package cn.gtmap.estateplat.server.web.wf.batch.slxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSqrService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.support.hibernate.UUIDHexGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 16-12-9
 * @description       不动产登记权利人信息
 */
@Controller
@RequestMapping("/bdcdjSqrxx")
public class BdcdjSqrxxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcSqrService bdcSqrService;
    @Autowired
    private EntityMapper entityMapper;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        request.setAttribute("zjlxList", zjlxList);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, BdcSqr bdcSqr, @RequestParam(value = "sqrid", required = false) String sqrid,@RequestParam(value = "from", required = false) String from,@RequestParam(value = "taskid", required = false) String taskid,@RequestParam(value = "rid", required = false) String rid) {
        if (StringUtils.isNotBlank(sqrid)) {
            bdcSqr = bdcSqrService.getBdcSqrBySqrid(sqrid);
            model.addAttribute(bdcSqr);
        }
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/addSqr";
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 申请人信息
     */
    @ResponseBody
    @RequestMapping("/getSqrxxPagesJson")
    public Object getSqrxxPagesJson(Pageable pageable, String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getSqrxxByPage", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 新增申请人信息
     */
    @RequestMapping(value = "addSqr", method = RequestMethod.GET)
    public String addSqr(Model model, BdcSqr bdcSqr, String wiid,@RequestParam(value = "from", required = false) String from,@RequestParam(value = "taskid", required = false) String taskid,@RequestParam(value = "rid", required = false) String rid) {
        if (StringUtils.isNotBlank(wiid)) {
            bdcSqr = new BdcSqr();
            bdcSqr.setSqrid(UUIDGenerator.generate18());
            bdcSqr.setWiid(wiid);
            model.addAttribute("bdcSqr",bdcSqr);
        }
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/addSqr";
    }


    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 删除权利人信息
     */
    @ResponseBody
    @RequestMapping(value = "/delSqrxx", method = RequestMethod.POST)
    public Map delSqrxx(Model model, String sqrid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(sqrid)) {
            bdcSqrService.delBdcSqrBySqrid(sqrid);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }
    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存权利人信息
     */
    @ResponseBody
    @RequestMapping(value = "saveSqrxx")
    public HashMap saveSqrxx( BdcSqr bdcSqr) {
        HashMap map = Maps.newHashMap();
        String msg = "fail";
        if (bdcSqr != null && StringUtils.isNotBlank(bdcSqr.getSqrid()) && StringUtils.isNotBlank(bdcSqr.getSqrmc())) {
            bdcSqrService.saveBdcSqr(bdcSqr);
            msg = "success";
        }
        map.put("msg", msg);
        return map;
    }
    /**
     * @param
     * @author <a href="mailto:zhangxxing@gtmap.cn">zx</a>
     * @rerutn
     * @description 关联申请人
     */
    @ResponseBody
    @RequestMapping(value = "glSqr")
    public HashMap glSqr( @RequestParam(value = "sqrid", required = false) String sqrid, @RequestParam(value = "wiid", required = false) String wiid
            , @RequestParam(value = "proids", required = false) String proids,@RequestParam(value = "qlids", required = false) String qlids,
                          @RequestParam(value = "qllxdms", required = false) String qllxdms, @RequestParam(value = "qlrlx", required = false) String qlrlx) {
        HashMap map = Maps.newHashMap();
        String msg = "fail";
        if(StringUtils.isNotBlank(sqrid)&&StringUtils.isNotBlank(proids)){
            String[] proidArr=proids.split(",");
            for(int i = 0;i<proidArr.length;i++){
                bdcSqrService.glBdcSqr(sqrid,proidArr[i],wiid,qlrlx);
                msg = "success";
            }
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 检查申请人是否保存
     */
    @ResponseBody
    @RequestMapping(value = "/checkSqr", method = RequestMethod.GET)
    public HashMap checkSqr(@RequestParam(value = "sqrid", required = false) String sqrid) {
        HashMap map = Maps.newHashMap();
        String msg = "fail";
        if (StringUtils.isNotBlank(sqrid)) {
            BdcSqr bdcSqr = bdcSqrService.getBdcSqrBySqrid(sqrid);
            if (bdcSqr != null) {
                msg = "success";
            }
        }
        map.put("msg", msg);
        return map;
    }

}
