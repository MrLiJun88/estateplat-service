package cn.gtmap.estateplat.server.web.wf.batch.slxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.main.BaseController;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
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
@RequestMapping("/bdcdjBatchSqsxx")
public class BdcdjBatchSqsxxController extends BaseController {
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
    BdcBankService bdcBankService;
    @Autowired
    private BdcFdcqService bdcFdcqService;

    @RequestMapping(value = " ", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "wiid", required = false) String wiid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String mjdw = null;
        Double zj = null;
        model.addAttribute("mjdw", mjdw);
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                //判断当qllx为抵押时获取默认银行列表
                List<BdcXtYh> bdcXtYhList = new ArrayList<BdcXtYh>();
                if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                    bdcXtYhList = bdcBankService.getBankListByPage();
                }
                model.addAttribute("yhList", bdcXtYhList);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getMjdw()))
                    model.addAttribute("mjdw", bdcSpxx.getMjdw());
                List<Map> djsyMcList = bdcZdGlService.getComDjsy();
                List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
                List<Map> zdzhytList = bdcTdService.getZdty();
                Map<String, Object> tdsyqx = bdcFdcqService.getTdsyqx(proid);
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
                List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                List<Map> zdzhqlxzList = bdcZdGlService.getZdQlxz();
                String gyfs = null;
                List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
                //根据权利人和义务人的size定义前台的申请人情况的合并行数。
                Integer qlrLength = 6;
                Integer ywrLength = 6;
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    qlrLength = bdcQlrList.size() * 6;
                }
                if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                    ywrLength = bdcYwrList.size() * 6;
                }
                Integer rowsize = qlrLength + ywrLength + 1;
                model.addAttribute("rowsize", rowsize);
                List<Map> gyfsList = bdcZdGlMapper.getZdGyfs();
                List<Map> bdclxList = bdcZdGlMapper.getZdBdclx();
                List<HashMap> yhlxList = bdcZdGlMapper.getBdcZdYhlx(new HashMap());
                List<HashMap> gzwlxList = bdcZdGlMapper.getGjzwLxZdb(new HashMap());
                List<HashMap> lzList = bdcZdGlMapper.getBdcZdLz(new HashMap());
                List<HashMap> fwxzList = bdcZdGlService.getBdcZdFwxz(new HashMap());
                List<Map> fwjgList = bdcZdGlService.getZdFwjg();
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    gyfs = bdcQlrList.get(0).getGyfs();
                }
                model.addAttribute("zmj", getSumOfJzmj(wiid));
                model.addAttribute("bdcFdcq", CollectionUtils.isNotEmpty(bdcFdcqList) ? bdcFdcqList.get(0) : new BdcFdcq());
                model.addAttribute("fwxzList", fwxzList);
                model.addAttribute("fwjgList", fwjgList);
                model.addAttribute("zjlxList", zjlxList);
                model.addAttribute("djsyMcList", djsyMcList);
                model.addAttribute("lzList", lzList);
                model.addAttribute("gzwlxList", gzwlxList);
                model.addAttribute("yhlxList", yhlxList);
                model.addAttribute("bdclxList", bdclxList);
                model.addAttribute("zj", zj);
                model.addAttribute("bdcQlrList", bdcQlrList);
                model.addAttribute("bdcYwrList", bdcYwrList);
                model.addAttribute("bdcYwrSize", CollectionUtils.isNotEmpty(bdcYwrList) ? bdcYwrList.size() : 0);
                model.addAttribute("bdcQlrSize", CollectionUtils.isNotEmpty(bdcQlrList) ? bdcQlrList.size() : 0);
                model.addAttribute("gyfsList", gyfsList);
                model.addAttribute("gyfs", gyfs);
                model.addAttribute("zdzhqlxzList", zdzhqlxzList);
                model.addAttribute("djlxList", djlxList);
                model.addAttribute("fwytList", fwytList);
                model.addAttribute("zdzhytList", zdzhytList);
                model.addAttribute("bdcXm", bdcXm);
                model.addAttribute("bdcSpxx", bdcSpxx == null ? new BdcSpxx() : bdcSpxx);
                model.addAttribute("bdcBdcdy", bdcBdcdy);
                model.addAttribute("tdsyjsqx", null != tdsyqx ? tdsyqx.get("syjsqx") : "");
                model.addAttribute("tdsyksqx", null != tdsyqx ? tdsyqx.get("syksqx") : "");
            }
        }
        return "wf/core/" + dwdm + "/batch/bdcdjSqsxx";
    }

    /**
     * 获取总建筑面积
     *
     * @param wiid
     */
    public Double getSumOfJzmj(String wiid) {
        Double zmj = 0.0;
        List<BdcSpxx> bdcSpxxList = bdcSpxxService.getBdcSpxxByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
            for (BdcSpxx bdcSpxx : bdcSpxxList) {
                if (null != bdcSpxx.getMj()) {
                    zmj += bdcSpxx.getMj();
                }
            }
        }
        return zmj;
    }


    @ResponseBody
    @RequestMapping(value = "/saveBdcSqsxx", method = RequestMethod.POST)
    public Map saveBdcSqsxx(Model model, BdcSpxx bdcSpxx,BdcFdcq bdcFdcq, BdcXm bdcXm, String s, String gyfs) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid()) && StringUtils.isNotBlank(bdcXm.getQllx())) {
            //处理权利人及义务人的保存
            if (StringUtils.isNotBlank(s)) {
                List<BdcQlr> bdcQlrlist = JSON.parseArray(s, BdcQlr.class);
                saveQlr(bdcQlrlist, gyfs);
            }
            entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
            entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
            entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * 保存权利人
     */
    public void saveQlr(List<BdcQlr> bdcQlrlist, String gyfs) {
        if (CollectionUtils.isNotEmpty(bdcQlrlist)) {
            for (BdcQlr bdcQlr : bdcQlrlist) {
                if (StringUtils.isNotBlank(bdcQlr.getQlrid())) {
                    if (StringUtils.equals(bdcQlr.getQlrlx(), "qlr")) {
                        bdcQlr.setGyfs(gyfs);
                    }
                    if(bdcQlr.getQlrid().indexOf("new")!=-1){
                        bdcQlr.setQlrid(UUIDGenerator.generate18());
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                        entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                    }
                }
            }
        }
    }


    /**
     *
     * @param model
     * @param proid
     * @param wiid
     * @return
     */
    @RequestMapping(value = "/bdcdjSqsList ", method = RequestMethod.GET)
    public String bdcdjSqsList(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<Map<String, Object>> returnValueList = new LinkedList<Map<String, Object>>();
            List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            String sqsreportName = "bdc_sqs.cpt";
            String djlx = null;
            String sqlx = null;
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    djlx = StringUtils.isBlank(djlx) ? bdcXm.getDjlx() : djlx;
                    sqlx = StringUtils.isBlank(sqlx) ? bdcXm.getSqlx() : sqlx;
                    HashMap<String, Object> returnValue = new HashMap<String, Object>();
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                    List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                    BdcSpxx bdcSpxx =bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    returnValue.put("bdcQlrList",bdcQlrList);
                    returnValue.put("bdcYwrList",bdcYwrList);
                    returnValue.put("bdcSpxx", bdcSpxx);
                    returnValue.put("bdcXm", bdcXm);
                    returnValue.put("bdcdyh", bdcBdcdy.getBdcdyh());
                    returnValueList.add(returnValue);
                    returnValue.put("index",returnValueList.indexOf(returnValue)+1);
                }
            }
            List<Map> djsyMcList = bdcZdGlService.getComDjsy();
            model.addAttribute("djsyMcList", djsyMcList);
            model.addAttribute("sqsreportName", sqsreportName);
            model.addAttribute("proid", proid);
            model.addAttribute("wiid", wiid);
            model.addAttribute("returnValueList", returnValueList);
            model.addAttribute("djlxList",djlxList);
        }
        return "wf/core/" + dwdm + "/batch/bdcdjSqsList";
    }

}
