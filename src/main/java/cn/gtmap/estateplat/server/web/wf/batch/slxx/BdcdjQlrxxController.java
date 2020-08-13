package cn.gtmap.estateplat.server.web.wf.batch.slxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.utils.Constants;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 16-12-9
 * @description       不动产登记权利人信息
 */
@Controller
@RequestMapping("/bdcdjQlrxx")
public class BdcdjQlrxxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private EntityMapper entityMapper;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, BdcQlr bdcQlr, @RequestParam(value = "qlrid", required = false) String qlrid) {
        if (StringUtils.isNotBlank(qlrid)) {
            bdcQlr = bdcQlrService.getBdcQlrByQlrid(qlrid);
            List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
            model.addAttribute("zjlxList", zjlxList);
            model.addAttribute(bdcQlr);
        }
        return "wf/batch/slxx/addQlr";
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
    @RequestMapping(value = "addQlr", method = RequestMethod.GET)
    public String addQlr(Model model, BdcQlr bdcQlr, String proid) {
        if (StringUtils.isNotBlank(proid)) {
            bdcQlr = new BdcQlr();
            bdcQlr.setProid(proid);
            bdcQlr.setQlrid(UUIDGenerator.generate18());
            List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
            model.addAttribute("zjlxList", zjlxList);
            model.addAttribute(bdcQlr);
        }
        return "wf/batch/slxx/addQlr";
    }


    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 删除权利人信息
     */
    @ResponseBody
    @RequestMapping(value = "/delQlrxx", method = RequestMethod.POST)
    public Map delQlrxx(Model model, String qlrid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(qlrid)) {
            bdcQlrService.delBdcQlrByQlrid(qlrid);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 设置权利人类型
     */
    @ResponseBody
    @RequestMapping(value = "/setQlrlx", method = RequestMethod.POST)
    public Map setQlrlx(Model model, String qlrid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(qlrid)) {
            BdcQlr bdcQlr = bdcQlrService.getBdcQlrByQlrid(qlrid);
            if(bdcQlr!=null&&StringUtils.isNotBlank(bdcQlr.getQlrlx())) {
                if ( StringUtils.equals(Constants.QLRLX_QLR, bdcQlr.getQlrlx())) {
                    bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                } else if (StringUtils.equals(Constants.QLRLX_YWR, bdcQlr.getQlrlx())) {
                    bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                }
                bdcQlrService.saveBdcQlr(bdcQlr);
                returnvalue = "success";
            }
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
    @RequestMapping("/saveQlrxx")
    public Map saveQlrxx(Model model, BdcQlr bdcQlr) {
        Map map = Maps.newHashMap();
        String msg = "fail";
        if (bdcQlr != null && StringUtils.isNotBlank(bdcQlr.getQlrid())) {
            bdcQlrService.saveBdcQlr(bdcQlr);
            msg = "success";
        }
        map.put("msg", msg);
        return map;
    }

}
