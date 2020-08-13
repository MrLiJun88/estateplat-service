package cn.gtmap.estateplat.server.web.wf.djxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcdjBdcdyxxList")
public class BdcBdcdyxxListController extends BaseController {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcYgService bdcYgService;


    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<Map<String, Object>> returnValueList = new LinkedList<Map<String, Object>>();
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            String spbreportName = "bdc_fdcq_spb.cpt";
            String sqsreportName = "bdc_fdcq_sps.cpt";
            String djlx = null;
            String sqlx = null;
            boolean showFzlxColumn = Boolean.TRUE;
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    djlx = StringUtils.isBlank(djlx) ? bdcXm.getDjlx() : djlx;
                    sqlx = StringUtils.isBlank(sqlx) ? bdcXm.getSqlx() : sqlx;
                    HashMap<String, Object> returnValue = new HashMap<String, Object>();
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                    List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                    BdcSpxx bdcSpxx = getBdcSpxx(bdcXm.getProid());
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                    showFzlxColumn=CollectionUtils.isEmpty(bdcFdcqList)?Boolean.FALSE:showFzlxColumn;
                    Map<String, Object> map = new HashMap<String, Object>();
                    if(bdcBdcdy != null)
                        map.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    List<BdcZjjzwxx> bdcZjjzwxxList = bdcZjjzwxxService.getZjjzwxx(map);
                    BdcFdcq bdcFdcq = CollectionUtils.isNotEmpty(bdcFdcqList) ? bdcFdcqList.get(0) : null;
                    if (CollectionUtils.isNotEmpty(bdcZjjzwxxList) && null != bdcFdcq) {
                        bdcFdcq.setFzlx(Constants.FZLX_FZS);
                    }
                    List<BdcYg> bdcYgList = null;
                    if(bdcBdcdy != null)
                    bdcYgList = bdcYgService.getBdcYgList(bdcBdcdy.getBdcdyh(), "1");
                    if (CollectionUtils.isNotEmpty(bdcYgList) && null != bdcFdcq) {
                        bdcFdcq.setFzlx(Constants.FZLX_FZM);
                    }
                    returnValue.put("bdcQlrList", StringUtils.equals(Constants.DJLX_DYDJ_DM, djlx) ? bdcYwrList : bdcQlrList);
                    returnValue.put("bdcSpxx", bdcSpxx);
                    returnValue.put("bdcXm", bdcXm);
                    if(bdcBdcdy != null)
                        returnValue.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    returnValue.put("qlid", null!=bdcFdcq?bdcFdcq.getQlid():"");
                    returnValue.put("fzlx", null!=bdcFdcq?bdcFdcq.getFzlx():"");
                    returnValueList.add(returnValue);
                }
            }
            spbreportName=StringUtils.equals(Constants.SQLX_PLCF,sqlx)?
                    "bdc_plcfshb.cpt":(StringUtils.equals(Constants.SQLX_JF,sqlx)?
                    "bdc_pljfshb.cpt":spbreportName);
            List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
            List<Map> zdqlxz=bdcZdGlService.getZdQlxz();
            model.addAttribute("zdqlxz", zdqlxz);
            model.addAttribute("fwytList", fwytList);
            model.addAttribute("spbreportName", spbreportName);
            model.addAttribute("sqsreportName", sqsreportName);
            model.addAttribute("showFzlxColumn", showFzlxColumn);
            model.addAttribute("proid", proid);
            model.addAttribute("wiid", wiid);
            model.addAttribute("columnName", StringUtils.equals(djlx, Constants.DJLX_DYDJ_DM) ? "抵押人" : "权利人");
            model.addAttribute("returnValueList", returnValueList);
        }
        return "wf/core/" + dwdm + "/djxx/bdcBdcdyxxList";
    }


    /**
     * @param proid
     * @return
     */
    public BdcSpxx getBdcSpxx(String proid) {
        BdcSpxx bdcSpxx = null;
        Example bdcspxxExample = new Example(BdcSpxx.class);
        bdcspxxExample.createCriteria().andEqualTo("proid", proid);
        List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(bdcspxxExample);
        if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
            bdcSpxx = bdcSpxxList.get(0);
        }
        return bdcSpxx;
    }

    @ResponseBody
    @RequestMapping(value = "/saveBdcdyxx", method = RequestMethod.POST)
    public Map saveBdcdyxx(Model model, String spxx, String qlr, String fdcq,
                           @RequestParam(value = "proid", required = false) String proid,
                           @RequestParam(value = "wiid", required = false) String wiid) {
        Map map = Maps.newHashMap();
        if (StringUtils.isBlank(spxx) && StringUtils.isBlank(fdcq) && StringUtils.isBlank(qlr)) {
            map.put("message", "fail");
            return map;
        }
        updateInfo(spxx, BdcSpxx.class);
        updateInfo(qlr, BdcQlr.class);
        updateInfo(fdcq, BdcFdcq.class);
        map.put("message", "success");
        return map;
    }

    /**
     * @param param
     * @param t
     */
    public void updateInfo(String param, Class<?> t) {
        List<?> list = JSON.parseArray(param, t);
        if (CollectionUtils.isNotEmpty(list)) {
            entityMapper.batchSaveSelective(list);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateAllBdcdyxx", method = RequestMethod.POST)
    public Map updateAllBdcdyxx(Model model, String spxx, String qlr, String fdcq,
                                 @RequestParam(value = "proid", required = false) String proid,
                                 @RequestParam(value = "wiid", required = false) String wiid){
        Map map = Maps.newHashMap();
        if (StringUtils.isBlank(spxx)) {
            map.put("message", "fail");
            return map;
        }
        updateInfo(spxx, BdcSpxx.class);
        map.put("message", "success");
        return map;

    }
}
