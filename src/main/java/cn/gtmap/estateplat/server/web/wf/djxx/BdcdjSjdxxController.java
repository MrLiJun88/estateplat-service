package cn.gtmap.estateplat.server.web.wf.djxx;/*
 * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
 * @version 1.0, 17-2-21
 * @description   不动产登记收件单信息
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDjsjdxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.thoughtworks.xstream.mapper.Mapper;
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

import java.util.*;

@Controller
@RequestMapping("/bdcdjSjdxx")
public class BdcdjSjdxxController extends BaseController {
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
    private  BdcSjclService bdcSjclService;
    @Autowired
    private  BdcBankService bdcBankService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("zjlxList", zjlxList);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        String djzxdm = "";
        if (StringUtils.isNotBlank(wiid)) {
            model.addAttribute("wiid", wiid);
        }
        if (StringUtils.isNotBlank(proid)) {
            model.addAttribute("proid", proid);
            //获取sjxxNum
            Integer sjxxNum = bdcDjsjdxxMapper.getBdcSjclByProid(proid);
            model.addAttribute("sjxxNum",sjxxNum);
            //获取登记子项
            List<HashMap> djzxList = bdcZdGlService.getDjzxByProid(proid);
            model.addAttribute("djzxList", djzxList);
            //bdc_xm
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            djzxdm = (StringUtils.isBlank(bdcXm.getDjzx())) ? bdcDjsjdxxMapper.getDjzxByProid(proid):bdcXm.getDjzx() ;
            model.addAttribute(bdcXm);
            model.addAttribute("djzxdm",djzxdm);
            //判断当qllx为抵押时获取默认银行列表
            List<BdcXtYh> bdcXtYhList=new ArrayList<BdcXtYh>();
            if(StringUtils.equals(bdcXm.getQllx(),Constants.QLLX_DYAQ)){
                bdcXtYhList=bdcBankService.getBankListByPage();
            }
            model.addAttribute("yhList",bdcXtYhList);
            //处理收件材料
            HashMap map = new HashMap();
            if (sjxxNum != 0) {
                map.put("proid", proid);
                List<Map> sjclList = bdcSjxxService.getSjclWithProidByPage(map);
                for (int i = 0; i <sjclList.size() ; i++) {
                    if(sjclList.get(i).get("YS")==null){
                        sjclList.get(i).put("YS",1);
                    }
                }
                model.addAttribute("sjclList", sjclList);
            } else {
                map.put("proid", proid);
                map.put("djzxdm", djzxdm);
                List<Map> sjclList = bdcSjxxService.getSjclWithProidAndDjzxByPage(map);
                model.addAttribute("sjclList", sjclList);
            }
            //获取WorkflowName
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowInstanceService.getWorkflowInstanceByProId(proid);
            if (pfWorkFlowInstanceVo != null) {
                PfWorkFlowDefineVo pfWorkFlowDefineVo = workFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                if (pfWorkFlowDefineVo != null) {
                    model.addAttribute("bdcdjlx", pfWorkFlowDefineVo.getWorkflowName());
                }
            }
            //bdc_qlr
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcQlrList)){
                model.addAttribute("bdcQlrList",bdcQlrList);
            }else{
                BdcQlr bdcQlr=new BdcQlr();
                bdcQlr.setQlrid(UUIDGenerator.generate18());
                bdcQlr.setProid(proid);
                List<BdcQlr> bdcQlrListTemp=new ArrayList<BdcQlr>();
                bdcQlrListTemp.add(bdcQlr);
                model.addAttribute("bdcQlrList",bdcQlrListTemp);
            }
            //bdc_spxx
            Example bdcspxxExample = new Example(BdcSpxx.class);
            bdcspxxExample.createCriteria().andEqualTo("proid", proid);
            List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(bdcspxxExample);
            if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
                BdcSpxx bdcSpxx = bdcSpxxList.get(0);
                model.addAttribute(bdcSpxx);
            } else {
                model.addAttribute(new BdcSpxx());
            }
            //bdc_sjxx
            Example bdcsjxxExample = new Example(BdcSjxx.class);
            bdcsjxxExample.createCriteria().andEqualTo("proid", proid);
            List<BdcSjxx> bdcSjxxList = entityMapper.selectByExample(bdcsjxxExample);
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                String sjrq = "";
                BdcSjxx bdcSjxx = bdcSjxxList.get(0);
                model.addAttribute(bdcSjxx);
                sjrq = CalendarUtil.formateToStrChinaYMDDate( bdcSjxx.getSjrq());
                model.addAttribute("sjrq", sjrq);
            } else {
                BdcSjxx bdcSjxx=new BdcSjxx();
                bdcSjxx.setSjr(getUserName());
                model.addAttribute(new BdcSjcl());
                model.addAttribute( "bdcSjxx",bdcSjxx);
                model.addAttribute("sjrq", CalendarUtil.formatDateToString( new Date()));
            }


        }

        return "wf/core/"+dwdm+"/djxx/bdcdjSjdxx";

    }

    @ResponseBody
    @RequestMapping(value = "getDjzxByAjax", method = RequestMethod.POST)
    public Map getDjzxByAjax(Model model, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid,@RequestParam(value = "djzx", required = false) String djzx){
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if(StringUtils.isNotBlank(djzx)&&djzx!=null){
            map.put("proid", proid);
            map.put("djzxdm", djzx);
            List<Map> sjclList=bdcSjxxService.getSjclWithProidAndDjzxByPage(map);
            String json=JSON.toJSONString(sjclList);
            map.put("sjclList",json);
            returnvalue = "success";
        }
        map.put("msg",returnvalue);
//         index(model,wiid,proid,djzx);
        return  map;
    }
    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 获取收件材料
     */
    @ResponseBody
    @RequestMapping("/getSjclxxPagesJson")
    public Object getSjclxxPagesJson(Pageable pageable, String proid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Page<HashMap> dataPaging = null;
        if (StringUtils.isNotBlank(proid)) {
            //获取sjxxNum和djzxdm
            Integer sjxxNum = bdcDjsjdxxMapper.getBdcSjclByProid(proid);
            BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
            String djzxdm = "";
            //如果bdc_xm没有Djzx，则取bdc_sqlx_djzx_rel
            if (StringUtils.isBlank(bdcXm.getDjzx())) {
                djzxdm = bdcDjsjdxxMapper.getDjzxByProid(proid);
            } else {
                djzxdm = bdcXm.getDjzx();
            }
            if (sjxxNum == 0) {
                map.put("proid", proid);
                map.put("djzxdm", djzxdm);
                dataPaging = repository.selectPaging("getSjclWithProidAndDjzxByPage", map, pageable);
            } else {
                map.put("proid", proid);
                dataPaging = repository.selectPaging("getSjclWithProidByPage", map, pageable);
            }
        }
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 保存收件单信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveSlxx", method = RequestMethod.POST)
    public Map saveSlxx(Model model, BdcXm bdcXm, BdcSjxx bdcSjxx,BdcSpxx bdcSpxx,String s, String sjdsjrq,@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
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
                   if(StringUtils.isNotBlank(bdcSpxx.getSpxxid())){
                       bdcSpxx.setProid(bdcXmTemp.getProid());
                       bdcSpxxService.saveBdcSpxx(bdcSpxx);
                   }else{
                       bdcSpxx.setProid(bdcXmTemp.getProid());
                       bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                       bdcSpxxService.saveBdcSpxx(bdcSpxx);
                   }
                    //更新所有权利人表,保存多个有问题
//                    if(StringUtils.isNotBlank(proid)){
//                        Map proidMap=new HashMap();
//                        proidMap.put("proid",proid);
//                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrList(proidMap);
//                        if(CollectionUtils.isNotEmpty(bdcQlrList)){
//                            for(BdcQlr bdcQlr:bdcQlrList){
//                                bdcQlr.setQlrmc(qlrmc);
//                                bdcQlr.setQlrsfzjzl(qlrsfzjzl);
//                                bdcQlr.setQlrzjh(qlrzjh);
//                                bdcQlr.setQlrlxdh(qlrlxdh);
//                                bdcQlrService.saveBdcQlr(bdcQlr);
//                            }
//                        }else{
//                            BdcQlr bdcQlr=new BdcQlr();
//                            bdcQlr.setProid(bdcXmTemp.getProid());
//                            bdcQlr.setQlrid(UUIDGenerator.generate18());
//                            bdcQlr.setQlrmc(qlrmc);
//                            bdcQlr.setQlrsfzjzl(qlrsfzjzl);
//                            bdcQlr.setQlrzjh(qlrzjh);
//                            bdcQlr.setQlrlxdh(qlrlxdh);
//                            bdcQlrService.saveBdcQlr(bdcQlr);
//                        }
//                    }
                    //处理权利人保存
                    if (StringUtils.isNotBlank(s)) {
                        List<BdcQlr> bdcQlrlist = JSON.parseArray(s, BdcQlr.class);
                        model.addAttribute("bdcQlrlist", bdcQlrlist);
                        if (CollectionUtils.isNotEmpty(bdcQlrlist) && bdcQlrlist.size() > 0) {
                            for (BdcQlr bdcQlr : bdcQlrlist) {
                                if (StringUtils.isNotBlank(bdcQlr.getQlrid())) {
                                    //判断前台组织的权利人对象是否是新添加的
                                    if (bdcQlr.getQlrid().length() < 10) {
                                        if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                            bdcQlr.setQlrid(UUIDGenerator.generate18());
                                            bdcQlrService.saveBdcQlr(bdcQlr);
                                        }
                                    } else {
                                        if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                            bdcQlrService.saveBdcQlr(bdcQlr);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //更新所有收件信息表
                    if(StringUtils.isNotBlank(bdcSjxx.getSjxxid())){

                        bdcSjxx.setProid(bdcXmTemp.getProid());
                        bdcSjxx.setSjrq(CalendarUtil.formatDate(sjdsjrq));
                        bdcSjxxService.saveBdcSjxx(bdcSjxx);
                    }else{
                        bdcSjxx.setProid(bdcXmTemp.getProid());
                        bdcSjxx.setSjrq(CalendarUtil.formatDate(sjdsjrq));
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

    @ResponseBody
    @RequestMapping(value = "/changeDjzx", method = RequestMethod.POST)
    public Map changeDjzx(Model model, BdcXm bdcXm,@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid,@RequestParam(value = "djzxdm", required = false) String djzxdm) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            //获取实例所有不动产项目表
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    //更新项目表djzx
                    if (StringUtils.equals(bdcXmTemp.getProid(), proid)) {
                        bdcXmTemp.setDjzx(djzxdm);
                        bdcXmService.saveBdcXm(bdcXmTemp);
                    }
                }
            }
            //删除存在于sjcll表中的信息
            BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
            if (bdcSjxx != null && StringUtils.isNotBlank(bdcSjxx.getSjxxid())) {
                Example example = new Example(BdcSjcl.class);
                example.createCriteria().andEqualTo("sjxxid", bdcSjxx.getSjxxid());
                entityMapper.deleteByExample(example);
                returnvalue = "success";
            }
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:为了前台增加sjcl
     * @Date 17:55 2017/4/19
     */
    @ResponseBody
    @RequestMapping(value = "sjclAdd", method = RequestMethod.POST)
    public Map sjclAdd(Model model, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "sjxxid", required = false) String sjxxid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(sjxxid) && sjxxid != null) {
            map.put("proid", proid);
            String newxh=bdcSjxxService.getSjclXhBySjxxid(sjxxid);
            BdcSjcl bdcSjcl=new BdcSjcl();
            bdcSjcl.setXh(Integer.parseInt(newxh));
            bdcSjcl.setSjxxid(sjxxid);
            bdcSjcl.setSjclid(UUIDGenerator.generate18());
            bdcSjcl.setFs(1);
            bdcSjcl.setYs(1);
            bdcSjcl.setCllx("1");
            bdcSjcl.setClmc("");
            String json = JSON.toJSONString(bdcSjcl);
            map.put("bdcSjcl", json);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        //         index(model,wiid,proid,djzx);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "saveSjclList", method = RequestMethod.POST)
    public Map saveSjclList(Model model, String s) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(s)) {
            List<BdcSjcl> bdcSjclList = JSON.parseArray(s, BdcSjcl.class);
            model.addAttribute("bdcSjclList", bdcSjclList);
            if (CollectionUtils.isNotEmpty(bdcSjclList) && bdcSjclList.size() > 0) {
                for (BdcSjcl bdcSjcl : bdcSjclList) {
                    if (StringUtils.isNotBlank(bdcSjcl.getClmc())) {
                        bdcSjclService.saveBdcSjcl(bdcSjcl);
                        returnvalue = "true";
                    }
                }
            }
        }
        map.put("msg", returnvalue);
        return map;
    }


    /**
     * @Author:<a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @Description: 收件单打印
     * @Date 15:36 2017/3/28
     */
    @RequestMapping(value = "bdcdjSjdList")
    public String bdcdjSjdList(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid,@RequestParam(value = "from", required = false) String from,@RequestParam(value = "taskid", required = false) String taskid,@RequestParam(value = "rid", required = false) String rid) {
        model.addAttribute("wiid", wiid);
        model.addAttribute("proid", proid);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/print/bdcdjSjdList";
    }

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:抵押情况时权利人名称改变身份证件种类和证件号随之改变
    *@Date 10:48 2017/4/25
    */
    @ResponseBody
    @RequestMapping(value = "getxtYh",method = RequestMethod.POST)
    public  Map getxtYh(String qlrmc,String proid){
        HashMap map=new HashMap();
        String qlrsfzjzl="";
        String qlrzjh="";
        if(StringUtils.isNotBlank(qlrmc)&&StringUtils.isNotBlank(proid)){
            List<BdcXtYh> bdcXtYhList=bdcBankService.getBankListByYhmc(qlrmc);
            if(CollectionUtils.isNotEmpty(bdcXtYhList)&&bdcXtYhList.size()>0){
                qlrsfzjzl=bdcXtYhList.get(0).getZjlx();
                qlrzjh=bdcXtYhList.get(0).getZjbh();
            }

        }
        map.put("qlrsfzjzl",qlrsfzjzl);
        map.put("qlrzjh",qlrzjh);
        return map;
    }
}
