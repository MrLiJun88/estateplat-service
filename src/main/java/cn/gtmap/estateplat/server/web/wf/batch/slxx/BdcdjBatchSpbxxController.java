package cn.gtmap.estateplat.server.web.wf.batch.slxx;

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
import com.gtis.plat.vo.PfSignVo;
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

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Controller
@RequestMapping("/bdcdjBatchSpbxx")
public class BdcdjBatchSpbxxController extends BaseController {
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
    @Autowired
    SysSignService signService;

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
                List<BdcXtYh> bdcXtYhList=new ArrayList<BdcXtYh>();
                if(StringUtils.equals(bdcXm.getQllx(),Constants.QLLX_DYAQ)){
                    bdcXtYhList=bdcBankService.getBankListByPage();
                }
                model.addAttribute("yhList",bdcXtYhList);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getMjdw())){
                    model.addAttribute("mjdw", bdcSpxx.getMjdw());
                }
                List<Map> djsyMcList = bdcZdGlService.getComDjsy();
                List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
                List<Map> zdzhytList = bdcTdService.getZdty();
                Map<String, Object> tdsyqx = bdcFdcqService.getTdsyqx(proid);
                List<BdcFdcq> bdcFdcqList=bdcFdcqService.getBdcFdcqByProid(proid);
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
                model.addAttribute("zmj",getSumOfJzmj(wiid));
                model.addAttribute("bdcFdcq", CollectionUtils.isNotEmpty(bdcFdcqList)?bdcFdcqList.get(0):new BdcFdcq());
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
                model.addAttribute("bdcSpxx", bdcSpxx==null?new BdcSpxx():bdcSpxx);
                model.addAttribute("bdcBdcdy", bdcBdcdy);
                model.addAttribute("tdsyjsqx", null != tdsyqx ? tdsyqx.get("syjsqx") : "");
                model.addAttribute("tdsyksqx", null != tdsyqx ? tdsyqx.get("syksqx") : "");
            }
        }
        return "wf/core/" + dwdm + "/batch/bdcdjSpbxx";
    }

    /**
     * 获取总建筑面积
     * @param wiid
     */
    public Double getSumOfJzmj(String wiid){
        Double zmj=0.0;
        List<BdcSpxx> bdcSpxxList=bdcSpxxService.getBdcSpxxByWiid(wiid);
        if(CollectionUtils.isNotEmpty(bdcSpxxList)){
            for(BdcSpxx bdcSpxx:bdcSpxxList){
                if(null!=bdcSpxx.getMj()){
                    zmj+=bdcSpxx.getMj();
                }
            }
        }
        return zmj;
    }


    @ResponseBody
    @RequestMapping(value = "/saveBdcSpbxx", method = RequestMethod.POST)
    public Map saveBdcSpbxx(Model model, BdcSpxx bdcSpxx,BdcFdcq bdcFdcq, BdcXm bdcXm, String s, String gyfs) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid()) && StringUtils.isNotBlank(bdcXm.getQllx())) {
            //处理权利人及义务人的保存
            if (StringUtils.isNotBlank(s)) {
                List<BdcQlr> bdcQlrlist = JSON.parseArray(s, BdcQlr.class);
                saveQlr(bdcQlrlist, gyfs);
                model.addAttribute("bdcQlrlist",bdcQlrlist);
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
}
