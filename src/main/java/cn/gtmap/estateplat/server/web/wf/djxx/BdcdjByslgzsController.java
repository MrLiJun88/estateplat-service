package cn.gtmap.estateplat.server.web.wf.djxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDjsjdxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
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
@RequestMapping("/bdcdjByslgzs")
public class BdcdjByslgzsController  extends BaseController {
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
    private BdcByslService bdcByslService;


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
            //bdc_qlr
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            String qlrs = bdcQlrService.combinationQlr(bdcQlrList);
            Map<String,Object> sjxxMap=getBdcSjxx( proid);
            BdcSjxx bdcSjxx = (BdcSjxx)sjxxMap.get("bdcSjxx");
            String sjrq = (String)sjxxMap.get("sjrq");
            List<BdcBysltzs> bdcBysltzsList=bdcByslService.getBdcBysltzs(map);
            model.addAttribute("bdcBysltzs",CollectionUtils.isNotEmpty(bdcBysltzsList)?bdcBysltzsList.get(0):new BdcBysltzs());
            model.addAttribute("sjrq", sjrq);
            model.addAttribute("bdcSjxx", bdcSjxx);
            model.addAttribute("qlrs", qlrs);
            model.addAttribute("proid", proid);
            model.addAttribute("sjclList", sjclList);
            model.addAttribute("sjxxNum", sjxxNum);
            model.addAttribute("bdcXm", bdcXm);
            model.addAttribute("bdcSpxx",bdcSpxx != null ? bdcSpxx : new BdcSpxx());
        }
        return "wf/core/" + dwdm + "/djxx/bdcdjByslgzs";
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
    @RequestMapping(value = "/saveByslxx", method = RequestMethod.POST)
    public Map saveSlxx(Model model,BdcBysltzs bdcBysltzs,BdcSpxx bdcSpxx,BdcXm bdcXm, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        HashMap<String,Object> map = Maps.newHashMap();
        if(StringUtils.isBlank(proid)){
            map.put("msg", "fail");
            return map;
        }
        map.put("proid",proid);
        List<BdcBysltzs> bdcBysltzsList=bdcByslService.getBdcBysltzs(map);
        if(CollectionUtils.isNotEmpty(bdcBysltzsList)){
            BdcBysltzs oldBdcBysltzs=bdcBysltzsList.get(0);
            oldBdcBysltzs.setBz(bdcBysltzs.getBz());
            oldBdcBysltzs.setTzyy(bdcBysltzs.getTzyy());
            entityMapper.saveOrUpdate(oldBdcBysltzs,oldBdcBysltzs.getTzsid());
        }else{
            bdcBysltzs.setTzsid(UUIDGenerator.generate18());
            entityMapper.saveOrUpdate(bdcBysltzs,bdcBysltzs.getTzsid());
        }
        map.put("msg", "success");
        return map;
    }
}
