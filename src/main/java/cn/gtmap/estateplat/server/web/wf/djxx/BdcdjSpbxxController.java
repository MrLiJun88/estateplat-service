package cn.gtmap.estateplat.server.web.wf.djxx;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2017/2/28
 * @description  不动产审批表信息
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfSignVo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/bdcdjSpbxx")
public class BdcdjSpbxxController extends BaseController {
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    QllxService qllxService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcZdGlMapper bdcZdGlMapper;
    @Autowired
    BdcTdService bdcTdService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    BdcDyqService bdcDyqService;
    @Autowired
    BdcYgService bdcYgService;
    @Autowired
    SysSignService signService;
    @Autowired
    BdcBankService bdcBankService;
    @Autowired
    BdcCfService bdcCfService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;

    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "wiid", required = false) String wiid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String mjdw = "";
        String zwlxksqx = "";
        String zwlxjsqx = "";
        String ygzwlxksqx = "";
        String ygzwlxjsqx = "";
        Double zj = null;
        BdcDyaq bdcDyaq = new BdcDyaq();
        BdcDyq bdcDyq = new BdcDyq();
        BdcYg bdcYg = new BdcYg();
        model.addAttribute("mjdw", mjdw);
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);

                if (qllxVo instanceof BdcDyaq) {
                    bdcDyaq = entityMapper.selectByPrimaryKey(BdcDyaq.class, qllxVo.getQlid());
                    //处理抵押期限日期格式
                    if (bdcDyaq != null && bdcDyaq.getZwlxksqx() != null) {
                        zwlxksqx = sdf.format(bdcDyaq.getZwlxksqx());
                    }
                    if (bdcDyaq != null && bdcDyaq.getZwlxjsqx() != null) {
                        zwlxjsqx = sdf.format(bdcDyaq.getZwlxjsqx());
                    }
                    //处理抵押价格
                    if (bdcDyaq != null && bdcDyaq.getTddyjg() != null && bdcDyaq.getFwdyjg() != null) {
                        zj = bdcDyaq.getFwdyjg() + bdcDyaq.getTddyjg();
                    } else if (bdcDyaq != null && bdcDyaq.getTddyjg() != null) {
                        zj = bdcDyaq.getFwdyjg();
                    } else if (bdcDyaq != null && bdcDyaq.getFwdyjg() != null) {
                        zj = bdcDyaq.getTddyjg();
                    }
                } else if (qllxVo instanceof BdcDyq) {
                    bdcDyq = entityMapper.selectByPrimaryKey(BdcDyq.class, qllxVo.getQlid());
                } else if (qllxVo instanceof BdcYg) {
                    bdcYg = entityMapper.selectByPrimaryKey(BdcYg.class, qllxVo.getQlid());
                    //处理抵押期限日期格式
                    if (bdcYg != null && bdcYg.getZwlxksqx() != null) {
                        ygzwlxksqx = sdf.format(bdcYg.getZwlxksqx());
                    }
                    if (bdcYg != null && bdcYg.getZwlxjsqx() != null) {
                        ygzwlxjsqx = sdf.format(bdcYg.getZwlxjsqx());
                    }
                }
                model.addAttribute("zwlxksqx", zwlxksqx);
                model.addAttribute("zwlxjsqx", zwlxjsqx);
                model.addAttribute("ygzwlxksqx", ygzwlxksqx);
                model.addAttribute("ygzwlxjsqx", ygzwlxjsqx);
                model.addAttribute("bdcDyaq", bdcDyaq);
                model.addAttribute("bdcDyq", bdcDyq);
                model.addAttribute("bdcYg", bdcYg);
                //判断当qllx为抵押时获取默认银行列表
                List<BdcXtYh> bdcXtYhList=new ArrayList<BdcXtYh>();
                if(StringUtils.equals(bdcXm.getQllx(),Constants.QLLX_DYAQ)){
                    bdcXtYhList=bdcBankService.getBankListByPage();
                }
                model.addAttribute("yhList",bdcXtYhList);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getMjdw()))
                    model.addAttribute("mjdw", bdcSpxx.getMjdw());
                List<Map> djsyMcList = bdcZdGlService.getComDjsy();
                List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
                List<Map> zdzhytList = bdcTdService.getZdty();
                List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                List<Map> zdzhqlxzList = bdcZdGlService.getZdQlxz();
                String gyfs = "";
                List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
                //根据权利人和义务人的size定义前台的申请人情况的合并行数。
                Integer qlrLength = 2;
                Integer ywrLength = 2;
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    qlrLength = bdcQlrList.size() * 2;
                }
                if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                    ywrLength = bdcYwrList.size() * 2;
                }
                Integer rowsize = qlrLength + ywrLength + 1;
                model.addAttribute("rowsize", rowsize);
                List<Map> gyfsList = bdcZdGlMapper.getZdGyfs();
                List<Map> bdclxList = bdcZdGlMapper.getZdBdclx();
                List<HashMap> yhlxList = bdcZdGlMapper.getBdcZdYhlx(new HashMap());
                List<HashMap> gzwlxList = bdcZdGlMapper.getGjzwLxZdb(new HashMap());
                List<HashMap> lzList = bdcZdGlMapper.getBdcZdLz(new HashMap());
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    gyfs = bdcQlrList.get(0).getGyfs();
                }
                if (bdcSpxx == null)
                    bdcSpxx = new BdcSpxx();
                if (qllxVo == null)
                    qllxVo = new BdcDyaq();
                //获取签名信息
                List<PfSignVo> csrSignList = signService.getSignList("csr", proid);
                PfSignVo csrSign = new PfSignVo();
                PfSignVo fsrSign = new PfSignVo();
                PfSignVo hdrSign = new PfSignVo();
                String csrq = "";
                String fsrq = "";
                String hdrq = "";
                if (CollectionUtils.isNotEmpty(csrSignList)) {
                    csrSign = csrSignList.get(0);
                    if (csrSign.getSignDate() != null) {
                        csrq = sdf.format(csrSign.getSignDate());
                    }
                }
                List<PfSignVo> fsrSignList = signService.getSignList("fsr", proid);
                if (CollectionUtils.isNotEmpty(fsrSignList)) {
                    fsrSign = fsrSignList.get(0);
                    if (fsrSign.getSignDate() != null) {
                        fsrq = sdf.format(fsrSign.getSignDate());
                    }
                }
                List<PfSignVo> hdrSignList = signService.getSignList("hdr", proid);
                if (CollectionUtils.isNotEmpty(hdrSignList)) {
                    hdrSign = hdrSignList.get(0);
                    if (hdrSign.getSignDate() != null) {
                        hdrq = sdf.format(hdrSign.getSignDate());
                    }
                }
                model.addAttribute("csrq", csrq);
                model.addAttribute("fsrq", fsrq);
                model.addAttribute("hdrq", hdrq);
                model.addAttribute("csrSign", csrSign);
                model.addAttribute("fsrSign", fsrSign);
                model.addAttribute("hdrSign", hdrSign);
                model.addAttribute("zjlxList", zjlxList);
                model.addAttribute("djsyMcList", djsyMcList);
                model.addAttribute("lzList", lzList);
                model.addAttribute("gzwlxList", gzwlxList);
                model.addAttribute("yhlxList", yhlxList);
                model.addAttribute("bdclxList", bdclxList);
                model.addAttribute("qllxVo", qllxVo);
                model.addAttribute("zj", zj);
                model.addAttribute("bdcQlrList", bdcQlrList);
                model.addAttribute("bdcYwrList", bdcYwrList);
                model.addAttribute("gyfsList", gyfsList);
                model.addAttribute("gyfs", gyfs);
                model.addAttribute("zdzhqlxzList", zdzhqlxzList);
                model.addAttribute("djlxList", djlxList);
                model.addAttribute("fwytList", fwytList);
                model.addAttribute("zdzhytList", zdzhytList);
                model.addAttribute("bdcXm", bdcXm);
                model.addAttribute("bdcSpxx", bdcSpxx);
                model.addAttribute("bdcBdcdy", bdcBdcdy);
                model.addAttribute("showRows", Boolean.FALSE);
            }
        }
        return "wf/core/" + dwdm + "/djxx/bdcdjSpbxx";
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:保存审批表信息
     * @Date 9:58 2017/2/28
     */
    @ResponseBody
    @RequestMapping(value = "saveBdcSpbxx", method = RequestMethod.POST)
    public Map saveBdcSpbxx(Model model, BdcDyaq bdcDyaq, BdcDyq bdcDyq, BdcYg bdcYg, BdcSpxx bdcSpxx, BdcXm bdcXm, String s, String gyfs) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid()) && StringUtils.isNotBlank(bdcXm.getQllx())) {
            //处理权利人及义务人的保存
            if (StringUtils.isNotBlank(s)) {
                List<BdcQlr> bdcQlrlist = JSON.parseArray(s, BdcQlr.class);
                model.addAttribute("bdcQlrlist", bdcQlrlist);
                if (CollectionUtils.isNotEmpty(bdcQlrlist) && bdcQlrlist.size() > 0) {
                    for (BdcQlr bdcQlr : bdcQlrlist) {
                        if (StringUtils.isNotBlank(bdcQlr.getQlrid())) {
                            if (StringUtils.equals(bdcQlr.getQlrlx(),Constants.QLRLX_QLR)) {
                                bdcQlr.setGyfs(gyfs);
                            }
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
            QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
            if (qllxVo instanceof BdcDyaq) {
                if (bdcDyaq != null && StringUtils.isNotBlank(bdcDyaq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid())) {
                    bdcDyaqService.saveBdcDyaq(bdcDyaq);
                    bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    bdcXmService.saveBdcXm(bdcXm);
                    returnvalue = "success";
                }
            } else if (qllxVo instanceof BdcDyq) {
                if (bdcDyq != null && StringUtils.isNotBlank(bdcDyq.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid())) {
                    bdcDyqService.saveBdcDyq(bdcDyq);
                    bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    bdcXmService.saveBdcXm(bdcXm);
                    returnvalue = "success";
                }
            } else if (qllxVo instanceof BdcYg) {
                if (bdcYg != null && StringUtils.isNotBlank(bdcYg.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid())) {
                    bdcYgService.saveYgxx(bdcYg);
                    bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    bdcXmService.saveBdcXm(bdcXm);
                    returnvalue = "success";
                }
            } else if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid())) {
                bdcSpxxService.saveBdcSpxx(bdcSpxx);
                bdcXmService.saveBdcXm(bdcXm);
                returnvalue = "success";
            }
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description: 删除审批表权利人
     * @Date 18:36 2017/3/26
     */
    @ResponseBody
    @RequestMapping(value = "delBdcQlr", method = RequestMethod.POST)
    public Map delBdcQlr(Model model, String s) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(s)) {
            bdcQlrService.delBdcQlrByQlrid(s);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @Author:<a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @Description: 审批表打印
     * @Date 15:36 2017/3/28
     */
    @RequestMapping(value = "bdcdjSpbList")
    public String bdcdjSpbList(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        model.addAttribute("wiid", wiid);
        model.addAttribute("proid", proid);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/print/bdcdjSpbList";
    }



    /**
     * @Author:<a href="mailto:liujie@gtmap.cn">liujie</a>
     * @Description: 查封解封审批表信息
     * @Date 9:20 2017/5/4
     */
    @RequestMapping(value = "cfJfSpbxx", method = RequestMethod.GET)
    public String cfJfShbxx(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "wiid", required = false) String wiid) {
        String isJf = "false";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cfksqx = "";
        String cfjsqx = "";
        String jfsj = "";
        BdcCf bdcCf = new BdcCf();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                if(StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_JF)){
                    isJf = "true";
                    model.addAttribute("isJf", isJf);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                    if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                        BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                        BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                        qllxVo = qllxService.queryQllxVo(ybdcXm);
                    }
                }

                if (qllxVo instanceof BdcCf) {
                    bdcCf = entityMapper.selectByPrimaryKey(BdcCf.class, qllxVo.getQlid());
                    //处理查封期限日期格式
                    if (bdcCf != null && bdcCf.getCfksqx() != null) {
                        cfksqx = sdf.format(bdcCf.getCfksqx());
                    }
                    if (bdcCf != null && bdcCf.getCfjsqx() != null) {
                        cfjsqx = sdf.format(bdcCf.getCfjsqx());
                    }
                    if(bdcCf != null && bdcCf.getJfsj() != null) {
                        jfsj = sdf.format(bdcCf.getJfsj());
                    }
                }
                model.addAttribute("cfksqx", cfksqx);
                model.addAttribute("cfjsqx", cfjsqx);
                model.addAttribute("jfsj", jfsj);
                model.addAttribute("bdcCf", bdcCf);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                if (bdcSpxx != null && bdcSpxx.getMj() != null)
                    model.addAttribute("mj", bdcSpxx.getMj());
                List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
                List<Map> zdzhytList = bdcTdService.getZdty();
                List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                List<BdcZdCflx> cflxList = bdcZdGlService.getBdcCflx();
                //获取查封权利人信息
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                String qlr = bdcQlrService.combinationQlr(bdcQlrList);
                model.addAttribute("qlr", qlr);
                List<Map> bdclxList = bdcZdGlMapper.getZdBdclx();
                if (bdcSpxx == null)
                    bdcSpxx = new BdcSpxx();
                if (qllxVo == null)
                    qllxVo = new BdcDyaq();
                //获取签名信息
                List<PfSignVo> csrSignList = signService.getSignList("csr", proid);
                PfSignVo csrSign = new PfSignVo();
                PfSignVo fsrSign = new PfSignVo();
                PfSignVo hdrSign = new PfSignVo();
                String csrq = "";
                String fsrq = "";
                String hdrq = "";
                if (CollectionUtils.isNotEmpty(csrSignList)) {
                    csrSign = csrSignList.get(0);
                    if (csrSign.getSignDate() != null) {
                        csrq = sdf.format(csrSign.getSignDate());
                    }
                }
                List<PfSignVo> fsrSignList = signService.getSignList("fsr", proid);
                if (CollectionUtils.isNotEmpty(fsrSignList)) {
                    fsrSign = fsrSignList.get(0);
                    if (fsrSign.getSignDate() != null) {
                        fsrq = sdf.format(fsrSign.getSignDate());
                    }
                }
                List<PfSignVo> hdrSignList = signService.getSignList("hdr", proid);
                if (CollectionUtils.isNotEmpty(hdrSignList)) {
                    hdrSign = hdrSignList.get(0);
                    if (hdrSign.getSignDate() != null) {
                        hdrq = sdf.format(hdrSign.getSignDate());
                    }
                }

                //获取WorkflowName
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
                if (pfWorkFlowInstanceVo != null) {
                    PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                    if (pfWorkFlowDefineVo != null) {
                        model.addAttribute("bdcdjlx", pfWorkFlowDefineVo.getWorkflowName());
                    }
                }
                model.addAttribute("csrq", csrq);
                model.addAttribute("fsrq", fsrq);
                model.addAttribute("hdrq", hdrq);
                model.addAttribute("csrSign", csrSign);
                model.addAttribute("fsrSign", fsrSign);
                model.addAttribute("hdrSign", hdrSign);
                model.addAttribute("bdclxList", bdclxList);
                model.addAttribute("qllxVo", qllxVo);
                model.addAttribute("djlxList", djlxList);
                model.addAttribute("fwytList", fwytList);
                model.addAttribute("zdzhytList", zdzhytList);
                model.addAttribute("cflxList", cflxList);
                model.addAttribute("bdcXm", bdcXm);
                model.addAttribute("bdcSpxx", bdcSpxx);
                model.addAttribute("bdcBdcdy", bdcBdcdy);
            }
        }
        return "wf/core/" + dwdm + "/djxx/bdcdjCfJfSpbxx";
    }


    /**
     * @Author:<a href="mailto:liujie@gtmap.cn">liujie</a>
     * @Description:保存查封审核表信息
     * @Date 15:16 2017/5/4
     */
    @ResponseBody
    @RequestMapping(value = "saveBdcCfSpbxx", method = RequestMethod.POST)
    public Map saveBdcCfShbxx(Model model, BdcCf bdcCf, BdcSpxx bdcSpxx, BdcXm bdcXm, String s) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid()) && StringUtils.isNotBlank(bdcXm.getQllx())) {
            //处理查封权利保存
            if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid())) {
                //QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                if(StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_JF)){
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                        BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                        BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                        if(ybdcXm != null) {
                            bdcCf.setProid(ybdcXm.getProid());
                            bdcCf.setYwh(ybdcXm.getBh());
                        }
                    }
                }
                bdcCfService.saveBdcCf(bdcCf);

                bdcSpxxService.saveBdcSpxx(bdcSpxx);
                bdcXmService.saveBdcXm(bdcXm);
                returnvalue = "success";
            }
        }
        map.put("msg", returnvalue);
        return map;
    }
}


