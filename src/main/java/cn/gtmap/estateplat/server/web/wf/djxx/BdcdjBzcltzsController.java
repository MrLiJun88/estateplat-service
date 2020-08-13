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
import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcdjBzcltzs")
public class BdcdjBzcltzsController extends BaseController {
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


    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            model.addAttribute("wiid", wiid);
        }
        if (StringUtils.isNotBlank(proid)) {
            //获取sjxxNum
            Integer sjxxNum = bdcDjsjdxxMapper.getBdcSjclByProid(proid);
            //bdc_xm
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            String djzxdm = (StringUtils.isBlank(bdcXm.getDjzx())) ? bdcDjsjdxxMapper.getDjzxByProid(proid):bdcXm.getDjzx() ;
            //处理收件材料
            HashMap map = new HashMap();
            map.put("proid", proid);
            map.put("djzxdm", djzxdm);
            List<Map> sjclList = sjxxNum != 0 ? bdcSjxxService.getSjclWithProidByPage(map) : bdcSjxxService.getSjclWithProidAndDjzxByPage(map);
            //获取WorkflowName
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowInstanceService.getWorkflowInstanceByProId(proid);
            if (pfWorkFlowInstanceVo != null) {
                PfWorkFlowDefineVo pfWorkFlowDefineVo = workFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                if (pfWorkFlowDefineVo != null) {
                    model.addAttribute("bdcdjlx", pfWorkFlowDefineVo.getWorkflowName());
                }
            }
            BdcSpxx bdcSpxx = getBdcSpxx(proid);
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            String qlrs = bdcQlrService.combinationQlr(bdcQlrList);
            Map<String,Object> sjxxMap=getBdcSjxx( proid);
            BdcSjxx bdcSjxx = (BdcSjxx)sjxxMap.get("bdcSjxx");
            String sjrq = (String)sjxxMap.get("sjrq");
            model.addAttribute("sjrq", sjrq);
            model.addAttribute("bdcSjxx", bdcSjxx);
            model.addAttribute("qlrs", qlrs);
            model.addAttribute("proid", proid);
            model.addAttribute("sjclList", sjclList);
            model.addAttribute("sjxxNum", sjxxNum);
            model.addAttribute("bdcXm", bdcXm);
            model.addAttribute("bdcSpxx",bdcSpxx != null ? bdcSpxx : new BdcSpxx());
        }
        return "wf/core/" + dwdm + "/djxx/bdcdjBzcltzs";
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
    @RequestMapping(value = "/saveSlxx", method = RequestMethod.POST)
    public Map saveSlxx(Model model, BdcXm bdcXm, BdcSjxx bdcSjxx, BdcSpxx bdcSpxx, String s, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            //获取实例所有不动产项目表
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    //更新所有项目表
                    bdcXm.setProid(bdcXmTemp.getProid());
                    bdcXmService.saveBdcXm(bdcXm);
                    //以下如果没有主键则为新增
                    //更新所有审批信息表
                    if (StringUtils.isNotBlank(bdcSpxx.getSpxxid())) {
                        bdcSpxx.setProid(bdcXmTemp.getProid());
                        bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    } else {
                        bdcSpxx.setProid(bdcXmTemp.getProid());
                        bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                        bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    }
                    //更新所有收件信息表
                    if (StringUtils.isNotBlank(bdcSjxx.getSjxxid())) {
                        bdcSjxx.setProid(bdcXmTemp.getProid());
                        bdcSjxxService.saveBdcSjxx(bdcSjxx);
                    } else {
                        bdcSjxx.setProid(bdcXmTemp.getProid());
                        bdcSjxx.setSjxxid(UUIDGenerator.generate18());
                        bdcSjxxService.saveBdcSjxx(bdcSjxx);
                    }
                    returnvalue = "success";
                }
            }
        }
        map.put("msg", returnvalue);
        return map;
    }


}
