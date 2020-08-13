package cn.gtmap.estateplat.server.web.wf.djxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDjsjdxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcdjBydjgzs")
public class BdcdjBydjgzsController  extends BaseController {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private SysWorkFlowInstanceService workFlowInstanceService;
    @Autowired
    private SysWorkFlowDefineService workFlowDefineService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private Repo repository;
    @Autowired
    private BdcDjsjdxxMapper bdcDjsjdxxMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcSjxxService bdcSjxxService;
    @Autowired
    private BdcBydjService bdcBydjService;


    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            model.addAttribute("wiid", wiid);
        }
        if (StringUtils.isNotBlank(proid)) {
            //bdc_xm
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            //获取WorkflowName
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowInstanceService.getWorkflowInstanceByProId(proid);
            if (pfWorkFlowInstanceVo != null) {
                PfWorkFlowDefineVo pfWorkFlowDefineVo = workFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                if (pfWorkFlowDefineVo != null) {
                    model.addAttribute("bdcdjlx", pfWorkFlowDefineVo.getWorkflowName());
                }
            }
            HashMap<String,Object> param=new HashMap<String, Object>();
            param.put("proid",proid);
            List<BdcBydjdjd> BdcBydjdjdList=bdcBydjService.getBdcBydjdjd(param);
            BdcSpxx bdcSpxx = getBdcSpxx(proid);
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            String qlrs = bdcQlrService.combinationQlr(bdcQlrList);
            Map<String,Object> sjxxMap=getBdcSjxx(proid);
            String sjrq = (String)sjxxMap.get("sjrq");
            model.addAttribute("sjrq", sjrq);
            model.addAttribute("qlrs", qlrs);
            model.addAttribute("proid", proid);
            model.addAttribute("bdcXm", bdcXm);
            model.addAttribute("bdcSpxx",bdcSpxx);
            model.addAttribute("bdcBydjdjd",CollectionUtils.isNotEmpty(BdcBydjdjdList)?BdcBydjdjdList.get(0):new BdcBydjdjd() );
        }
        return "wf/core/" + dwdm + "/djxx/bdcdjBydjgzs";
    }

    /**
     *
     * @param proid
     * @return
     */
    public  Map<String,Object> getBdcSjxx(String proid){
        BdcSjxx bdcSjxx=null;
        String sjrq=null;
        Map<String,Object> sjxxMap=new HashMap<String,Object>();
        Example bdcsjxxExample = new Example(BdcSjxx.class);
        bdcsjxxExample.createCriteria().andEqualTo("proid", proid);
        List<BdcSjxx> bdcSjxxList = entityMapper.selectByExample(bdcsjxxExample);
        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
            bdcSjxx = bdcSjxxList.get(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sjrq = sdf.format(bdcSjxx.getSjrq());
        } else {
            bdcSjxx = new BdcSjxx();
            bdcSjxx.setSjr(getUserName());
            sjrq = CalendarUtil.formatDateToString(new Date());
        }
        sjxxMap.put("sjrq",sjrq);
        sjxxMap.put("bdcSjxx",bdcSjxx);
        return sjxxMap;
    }

    /**
     * @param proid
     * @return
     */
    public BdcSpxx getBdcSpxx(String proid) {
        //bdc_spxx
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
    @RequestMapping(value = "/saveBydjxx", method = RequestMethod.POST)
    public Map saveBydjxx(Model model, BdcXm bdcXm,BdcBydjdjd bdcBydjdjd, BdcSjxx bdcSjxx, BdcSpxx bdcSpxx, String s, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        HashMap<String,Object> map = Maps.newHashMap();
        if(StringUtils.isBlank(proid)){
            map.put("msg", "fail");
            return map;
        }
        if(StringUtils.isNotBlank(s)){
            List<String> tzyys=JSON.parseArray(s, String.class);
            s=StringUtils.join(tzyys,",");
        }
        map.put("proid",proid);
        List<BdcBydjdjd> bdcBydjdjdList=bdcBydjService.getBdcBydjdjd(map);
        if(CollectionUtils.isNotEmpty(bdcBydjdjdList)){
            BdcBydjdjd oldBdcBydjdjd=bdcBydjdjdList.get(0);
            oldBdcBydjdjd.setBz(bdcBydjdjd.getBz());
            oldBdcBydjdjd.setTzyy(s);
            entityMapper.saveOrUpdate(oldBdcBydjdjd,oldBdcBydjdjd.getJdsid());
        }else{
            bdcBydjdjd.setJdsid(UUIDGenerator.generate18());
            bdcBydjdjd.setTzyy(s);
            entityMapper.saveOrUpdate(bdcBydjdjd,bdcBydjdjd.getJdsid());
        }
        map.put("msg", "success");
        return map;
    }
}
