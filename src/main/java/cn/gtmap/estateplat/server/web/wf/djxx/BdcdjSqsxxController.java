package cn.gtmap.estateplat.server.web.wf.djxx;/*
 * @author <a href="mailto:hzj@gtmap.cn">hzj</a>
 * @version 1.0, 2017/2/22
 * @description  不动产登记申请书信息
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

@Controller
@RequestMapping("/bdcdjSqsxx")
public class BdcdjSqsxxController extends BaseController {
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
                Map<String, Object> tdsyqx = bdcFdcqService.getTdsyqx(proid);
                List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                List<Map> zdzhqlxzList = bdcZdGlService.getZdQlxz();
                String gyfs = "";
                List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
                //根据权利人和义务人的size定义前台的申请人情况的合并行数。
                Integer qlrLength = 6;
                Integer ywrLength = 4;
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    qlrLength = bdcQlrList.size() * 6;
                }
                if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                    ywrLength = bdcYwrList.size() * 4;
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
                model.addAttribute("bdcYwrSize", CollectionUtils.isNotEmpty(bdcYwrList) ? bdcYwrList.size() : 0);
                model.addAttribute("bdcQlrSize", CollectionUtils.isNotEmpty(bdcQlrList) ? bdcQlrList.size() : 0);
                model.addAttribute("gyfsList", gyfsList);
                model.addAttribute("gyfs", gyfs);
                model.addAttribute("zdzhqlxzList", zdzhqlxzList);
                model.addAttribute("djlxList", djlxList);
                model.addAttribute("fwytList", fwytList);
                model.addAttribute("zdzhytList", zdzhytList);
                model.addAttribute("bdcXm", bdcXm);
                model.addAttribute("gyrsffbcz", bdcXm.getSqfbcz());
                model.addAttribute("bdcsfgy", CollectionUtils.isNotEmpty(bdcQlrList)&&bdcQlrList.size()>1 ?1:0);
                model.addAttribute("bdcSpxx", bdcSpxx);
                model.addAttribute("bdcBdcdy", bdcBdcdy);
                model.addAttribute("showRows", Boolean.TRUE);
                model.addAttribute("tdsyjsqx", null != tdsyqx ? tdsyqx.get("syjsqx") : "");
                model.addAttribute("tdsyksqx", null != tdsyqx ? tdsyqx.get("syksqx") : "");
            }
        }
        return "wf/core/" + dwdm + "/djxx/bdcdjSqsxx";
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:保存申请书信息
     * @Date 15:01 2017/2/22
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcSqsxx", method = RequestMethod.POST)
    public Map saveBdcSqsxx(Model model, BdcDyaq bdcDyaq, BdcDyq bdcDyq, BdcYg bdcYg, BdcSpxx bdcSpxx, BdcXm bdcXm, String s, String gyfs) {
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
                            if (StringUtils.equals(bdcQlr.getQlrlx(),Constants.QLRLX_QLR) && StringUtils.isNotBlank(gyfs)) {
                                bdcQlr.setGyfs(gyfs);
                            }
                            //判断前台组织的权利人对象是否是新添加的
                            if (bdcQlr.getQlrid().length() < 10) {
                                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                                    bdcQlrService.saveBdcQlr(bdcQlr);
                                }
                            } else {
                                if(StringUtils.isNotBlank(bdcQlr.getQlrmc())){
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
     * @Description: 删除申请书权利人
     * @Date 15:36 2017/3/27
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
     * @Description: 申请书打印
     * @Date 15:36 2017/3/28
     */
    @RequestMapping(value = "bdcdjSqsList")
    public String bdcdjSqsList(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        model.addAttribute("wiid", wiid);
        model.addAttribute("proid", proid);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/print/bdcdjSqsList";
    }
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:申请书保存时验证定着物填写的是否是字典表中的值
    *@Date 14:32 2017/4/24
    */
    @ResponseBody
    @RequestMapping(value = "validateDzwyt",method = RequestMethod.POST)
    public Map validateDzwyt(String yt){
        HashMap map=new HashMap();
        String returnvalue = "fail";
        map.put("dm", yt);
        List<HashMap> dzwList = bdcZdGlService.getDzwytZdb(map);
        if(CollectionUtils.isNotEmpty(dzwList)){
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return  map;
    }

    /**
     * 添加权利人
     * @param bdcQlr
     */
    @ResponseBody
    @RequestMapping(value="addQlr")
    public boolean addQlr(BdcQlr bdcQlr,String bdcQlrSize){
        boolean result=false;
        int sxh=StringUtils.isBlank(bdcQlrSize)?0:Integer.parseInt(bdcQlrSize);
        if(StringUtils.isNotBlank(bdcQlr.getProid())){
            bdcQlr.setQlrid(UUIDGenerator.generate18());
            bdcQlr.setSxh(++sxh);
            entityMapper.saveOrUpdate(bdcQlr,bdcQlr.getQlrid());
            result=true;
        }
        return result;
    }

}
