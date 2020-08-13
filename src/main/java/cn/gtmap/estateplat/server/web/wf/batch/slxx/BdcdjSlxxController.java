package cn.gtmap.estateplat.server.web.wf.batch.slxx;

import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;

import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;


/*
 * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
 * @version 1.0, 16-11-28
 * @description       不动产登记受理信息
 */

@Controller
@RequestMapping("/bdcdjSlxx")
public class BdcdjSlxxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcSjxxService bdcSjxxService;
    @Autowired
    private BdcSjclService bdcSjclService;
    @Autowired
    private BdcGgService bdcGgService;
    @Autowired
    SysSignService signService;
    @Autowired
    private  BdcdyService bdcdyService;
    @Autowired
    private SysWorkFlowInstanceService workFlowInstanceService;
    @Autowired
    private SysWorkFlowDefineService workFlowDefineService;



    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        if (StringUtils.isNotBlank(wiid)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String sjrq = "";
            String lzrq = "";
            String mjdw = "";
            String zsids = "";
            String zmids = "";
            String zsProids = "";
            String zmProids = "";
            model.addAttribute("sjrq", sjrq);
            model.addAttribute("lzrq", lzrq);
            model.addAttribute("mjdw", mjdw);
            model.addAttribute("wiid", wiid);
            //初始化bdcXm的信息
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                BdcXm bdcXm = bdcXmList.get(0);
                //处理领证日期
                if (bdcXm.getLzrq() != null) {
                    lzrq = sdf.format(bdcXm.getLzrq());
                    model.addAttribute("lzrq", lzrq);
                }
                model.addAttribute("bdcXm", bdcXm);
                //处理面积单位
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                if (bdcSpxx != null)
                    mjdw = bdcSpxx.getMjdw();
                model.addAttribute("mjdw", mjdw);
            } else {
                BdcXm bdcXm = new BdcXm();
                bdcXm.setProid(proid);
                bdcXm.setWiid(wiid);
                model.addAttribute("bdcXm", bdcXm);
            }
            //初始化收件信息
            BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
            if (bdcSjxx != null && bdcSjxx.getSjrq() != null) {
                //处理收件日期
                sjrq = sdf.format(bdcSjxx.getSjrq());
                model.addAttribute("sjrq", sjrq);
            } else if (bdcSjxx == null) {
                bdcSjxx = new BdcSjxx();
                bdcSjxx.setSjxxid(UUIDGenerator.generate18());
                bdcSjxx.setWiid(wiid);
            }
            model.addAttribute("bdcSjxx", bdcSjxx);
        }
        if (StringUtils.isNotBlank(proid)) {
            model.addAttribute("proid", proid);
        }
        //身份证件种类
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("zjlxListJosn", JSONObject.toJSONString(zjlxList));
        return "wf/batch/slxx/bdcdjSlxx";
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 项目信息
     */
    @ResponseBody
    @RequestMapping("/getBdcXmxxPagesJson")
    public Object getBdcXmxxPagesJson(Pageable pageable, String wiid, String bdcdyid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid))
            map.put("wiid", wiid);
        if (StringUtils.isNotBlank(bdcdyid))
            map.put("bdcdyid", bdcdyid);
        Page<HashMap> dataPaging = repository.selectPaging("getBdcXmxxByPage", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 不动产单元信息
     */
    @ResponseBody
    @RequestMapping("/getBdcdyxxPagesJson")
    public Object getBdcdyxxPagesJson(Pageable pageable, String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getBdcdyxxByPage", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 权利信息
     */
    @ResponseBody
    @RequestMapping("/getQlxxPagesJson")
    public Object getQlxxPagesJson(Pageable pageable, String wiid, String bdcdyid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid))
            map.put("wiid", wiid);
        if (StringUtils.isNotBlank(bdcdyid))
            map.put("bdcdyid", bdcdyid);
        Page<HashMap> dataPaging = repository.selectPaging("getSlxxQlxxByPage", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 根据权利类型代码获取名称
     */
    @ResponseBody
    @RequestMapping("/getQllxMc")
    public HashMap<String, Object> getQllxMc(String qllx) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(qllx)) {
            HashMap map = new HashMap();
            map.put("dm", qllx);
            List<BdcZdQllx> bdcZdQllxList = bdcZdGlService.getBdcZdQllx(map);
            if (CollectionUtils.isNotEmpty(bdcZdQllxList)) {
                BdcZdQllx bdcZdQllx = bdcZdQllxList.get(0);
                if (bdcZdQllx != null && StringUtils.isNotBlank(bdcZdQllx.getMc())) {
                    resultMap.put("mc", bdcZdQllx.getMc());
                }
            }
        }
        return resultMap;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 原权利登记信息
     */
    @ResponseBody
    @RequestMapping("/getYqlxxPagesJson")
    public Object getYqlxxPagesJson(Pageable pageable, String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getYqlxxByPage", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 申请材料列表
     */
    @ResponseBody
    @RequestMapping("/getSjclPagesJson")
    public Object getSjclPagesJson(Pageable pageable, String wiid, String proid, String djzx) {
        String flag = "0";
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(proid)) {
            map.put("proid", proid);
            //jyl 初始化收件材料。理论上应该创建就初始化，但是原来系统没有这么做，为了不破坏原来的逻辑，暂时按在这里初始化。
            BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
            if (bdcSjxx != null) {
                List<BdcSjcl> bdcSjclList = bdcSjclService.queryBdcSjclBySjxxid(bdcSjxx.getSjxxid());
                if (CollectionUtils.isEmpty(bdcSjclList)) {
                    if (StringUtils.isNotBlank(proid)) {
                        map.put("proid", proid);
                        if (StringUtils.isNotBlank(djzx)) {
                            map.put("djzx", djzx);
                        } else {
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjzx())) {
                                map.put("djzx", bdcXm.getDjzx());
                            } else {
                                List<HashMap> djzxList = bdcZdGlService.getDjzxByProid(proid);
                                if (CollectionUtils.isNotEmpty(djzxList) && djzxList.get(0).get("MC") != null) {
                                    map.put("djzx", djzxList.get(0).get("DM").toString());
                                }
                            }
                        }
                        flag = "1";
                    }
                }
            }
        }
        Page<HashMap> dataPaging = null;
        if (StringUtils.equals(flag, "1")) {
            dataPaging = repository.selectPaging("getbdcXtSjclByPage", map, pageable);
        } else {
            dataPaging = repository.selectPaging("getSlxxSjclByPage", map, pageable);
        }
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存收件信息
     */
    @ResponseBody
    @RequestMapping(value = "/creatSjcl", method = RequestMethod.POST)
    public Map creatSjcl(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
            //jyl 初始化收件材料。理论上应该创建就初始化，但是原来系统没有这么做，为了不破坏原来的逻辑，暂时按在这里初始化。
            BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
            if (bdcSjxx != null) {
                List<BdcSjcl> bdcSjclList = bdcSjclService.queryBdcSjclBySjxxid(bdcSjxx.getSjxxid());
                if (CollectionUtils.isEmpty(bdcSjclList)) {
                    if (StringUtils.isNotBlank(proid)) {
                        map.put("proid", proid);
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getDjzx())) {
                            map.put("djzx", bdcXm.getDjzx());
                        } else {
                            List<HashMap> djzxList = bdcZdGlService.getDjzxByProid(proid);
                            if (CollectionUtils.isNotEmpty(djzxList) && djzxList.get(0).get("MC") != null) {
                                map.put("djzx", djzxList.get(0).get("DM").toString());
                            }
                        }
                        List<BdcSjcl> bdcXtSjclList = bdcSjclService.getbdcXtSjcl(map);
                        if (CollectionUtils.isNotEmpty(bdcXtSjclList)) {
                            for (BdcSjcl bdcSjcl : bdcXtSjclList) {
                                bdcSjcl.setSjxxid(bdcSjxx.getSjxxid());
                                if (StringUtils.isBlank(bdcSjcl.getSjclid())) {
                                    bdcSjcl.setSjclid(UUIDGenerator.generate18());
                                }
                                bdcSjcl.setYs(1);
                                bdcSjclService.saveBdcSjcl(bdcSjcl);
                                returnvalue = "success";
                            }
                        }
                    }
                } else {
                    returnvalue = "success";
                }
            }
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @param
     * @author <a href="mailto:qiuchuanghe@gtmap.cn">qiuchuanghe</a>
     * @rerutn
     * @description公告信息
     */
    @ResponseBody
    @RequestMapping("/getGgxxPagesJson")
    public Object getGgxxPagesJson(Pageable pageable, String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getGgxxPagesJson", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description证书信息
     */
    @ResponseBody
    @RequestMapping("/getZsxxPagesJson")
    public Object getZsxxPagesJson(Pageable pageable, String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getZsxxPagesJson", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 点击收件单
     */
    @ResponseBody
    @RequestMapping(value = "bdcdjSlxOpenSjd", method = RequestMethod.GET)
    public HashMap bdcdjSlxOpenSjd(@RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        int sjxxNum = 0;
        String djzxdm = "";
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(proid)) {
            BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
            if (bdcSjxx != null) {
                List<BdcSjcl> bdcSjclList = bdcSjclService.queryBdcSjclBySjxxid(bdcSjxx.getSjxxid());
                if (CollectionUtils.isNotEmpty(bdcSjclList)) {
                    sjxxNum = bdcSjclList.size();
                }
            }
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                if (StringUtils.isNotBlank(bdcXm.getDjzx())) {
                    djzxdm = bdcXm.getDjzx();
                }
            }
        }
        resultMap.put("sjxxNum", sjxxNum);
        resultMap.put("djzxdm", djzxdm);
        resultMap.put("proid", proid);
        return resultMap;
    }


    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存收件信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveSlxx", method = RequestMethod.POST)
    public Map saveSlxx(Model model, BdcXm bdcXm, BdcSjxx bdcSjxx, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "mjdw", required = false) String mjdw) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            //获取实例所有不动产项目表
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    //更新面积单位
                    if (StringUtils.isNoneBlank(mjdw)) {
                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXmTemp.getProid());
                        if (bdcSpxx != null) {
                            bdcSpxx.setMjdw(mjdw);
                            bdcSpxxService.saveBdcSpxx(bdcSpxx);
                        }
                    }
                    //更新所有项目表
                    bdcXm.setProid(bdcXmTemp.getProid());
                    bdcXm.setSqfbcz("否");
                    bdcXmService.saveBdcXm(bdcXm);
                }
                returnvalue = "success";
            }
        }
        if (bdcSjxx != null) {
            bdcSjxxService.saveBdcSjxx(bdcSjxx);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 保存收件单
     */
    @ResponseBody
    @RequestMapping(value = "/saveSjd", method = RequestMethod.POST)
    public Map saveSjd(Model model, BdcXm bdcXm, BdcSpxx bdcSpxx, @RequestParam(value = "proid", required = false) String proid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcXm != null) {
            bdcXmService.saveBdcXm(bdcXm);
            returnvalue = "success";
        }
        if (bdcSpxx != null) {
            bdcSpxxService.saveBdcSpxx(bdcSpxx);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    @RequestMapping(value = "bdcGgxx", method = RequestMethod.GET)
    public String bdcGgxx(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid
            , @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(wiid)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Map> bdcGglxList = bdcZdGlService.getZdGglxlist(new HashMap());
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcGg bdcGg = new BdcGg();
            //获取当前时间
            String ggid = "";
            Date cjsj1 = new Date();
            String cjsj = sdf.format(cjsj1);
            String kssj = "";
            String jssj = "";
            String gglx = "";
            bdcGg.setProid(proid);
            bdcGg.setWiid(wiid);
            bdcGg.setCjrmc(getUserName());
            bdcGg.setGgbh(bdcXm.getBh());
            model.addAttribute("cjsj", cjsj);
            model.addAttribute("bdcGglxList", bdcGglxList);
            model.addAttribute("kssj", kssj);
            model.addAttribute("jssj", jssj);
            model.addAttribute("ggid", ggid);
            model.addAttribute("gglx", gglx);
            model.addAttribute("bdcGg", bdcGg);
        }
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/bdcGgxx";
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:初始化新增收件材料页面
     * @Date 16:59 2017/3/7
     */
    @RequestMapping(value = "bdcSjclxx", method = RequestMethod.GET)
    public String bdcSqcl(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "sjxxid", required = false) String sjxxid
            , @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        String clmc = "";
        String sjclid = "";
        String cllx = "";
        int fs = 1;
        int ys = 1;
        model.addAttribute("ys", ys);
        model.addAttribute("fs", fs);
        model.addAttribute("clmc", clmc);
        model.addAttribute("proid", proid);
        model.addAttribute("wiid", wiid);
        model.addAttribute("sjxxid", sjxxid);
        model.addAttribute("sjclid", sjclid);
        model.addAttribute("cllx", cllx);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/bdcSjclxx";
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:保存新增的收件材料信息
     * @Date 20:04 2017/3/6
     */
    @ResponseBody
    @RequestMapping(value = "saveSjclxx", method = RequestMethod.POST)
    public Map saveSjclxx(Model model, BdcSjcl bdcSjcl) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isBlank(bdcSjcl.getSjclid())) {
            bdcSjcl.setSjclid(UUIDGenerator.generate18());
            bdcSjcl.setMrfs(1);
            if (StringUtils.isNotBlank(bdcSjcl.getSjxxid())) {
                Integer xh = bdcSjclService.getSjclMaxXh(bdcSjcl.getSjxxid());
                if (xh != null)
                    bdcSjcl.setXh(xh);
                else
                    bdcSjcl.setXh(1);
                bdcSjclService.saveBdcSjcl(bdcSjcl);
                returnvalue = "true";
            }
        } else {
            bdcSjclService.saveBdcSjcl(bdcSjcl);
            returnvalue = "true";
        }

        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:保存公告信息
     * @Date 10:08 2017/3/8
     */
    @ResponseBody
    @RequestMapping(value = "saveGgxx", method = RequestMethod.POST)
    public Map saveGgxx(Model model, @RequestParam(value = "wiid", required = false) String wiid, BdcGg bdcGg) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isBlank(bdcGg.getGgid())) {
            bdcGg.setCjrid(getUserId());
            bdcGg.setGgdw(getOrganName().get(0).getOrganName() + "不动产登记中心");
            bdcGg.setGgid(UUIDGenerator.generate18());
            bdcGgService.saveGgxx(bdcGg);
            returnvalue = "true";
        } else {
            bdcGgService.saveGgxx(bdcGg);
            returnvalue = "true";
        }

        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:删除选中的公告信息
     * @Date 13:58 2017/3/8
     */
    @ResponseBody
    @RequestMapping(value = "delGgxx", method = RequestMethod.POST)
    public Map deleteGgxx(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "ggid", required = false) String ggid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(ggid) && StringUtils.isNotBlank(proid)) {
            bdcGgService.deleteGgxx(ggid);
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:编辑查看公告信息
     * @Date 15:14 2017/3/8
     */
    @RequestMapping(value = "editGgxx", method = RequestMethod.GET)
    public String editGgxx(Model model, @RequestParam(value = "ggid", required = false) String ggid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map> bdcGglxList = bdcZdGlService.getZdGglxlist(new HashMap());
        String kssj = "";
        String jssj = "";
        String cjsj = "";
        BdcGg bdcGg = null;
        if (StringUtils.isNotBlank(ggid)) {

            bdcGg = bdcGgService.getBdcGgByGgid(ggid);
            if (bdcGg.getKssj() != null) {
                kssj = sdf.format(bdcGg.getKssj());
            }
            if (bdcGg.getJssj() != null) {
                jssj = sdf.format(bdcGg.getJssj());
            }
            if (bdcGg.getCjsj() != null) {
                cjsj = sdf.format(bdcGg.getCjsj());
            }
        }
        String gglx = bdcGg.getGglx();
        model.addAttribute("kssj", kssj);
        model.addAttribute("jssj", jssj);
        model.addAttribute("cjsj", cjsj);
        model.addAttribute("bdcGg", bdcGg);
        model.addAttribute("ggid", ggid);
        model.addAttribute("gglx", gglx);
        model.addAttribute("bdcGglxList", bdcGglxList);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/bdcGgxx";
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:编辑收件材料信息editSjcl
     * @Date 19:29 2017/3/8
     */
    @RequestMapping(value = "editSjcl", method = RequestMethod.GET)
    public String editSjclxx(Model model, @RequestParam(value = "sjclid", required = false) String sjclid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, BdcSjcl bdcSjcl
            , @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        BdcSjxx bdcSjxx = bdcSjxxService.queryBdcSjxxByProid(proid);
        String sjxxid = bdcSjxx.getSjxxid();
        String clmc = "";
        String cllx = "";
        int fs = 1;
        int ys = 1;
        if (StringUtils.isNotBlank(sjclid)) {
            bdcSjcl = bdcSjclService.getBdcSjclById(sjclid);
            if (bdcSjcl != null) {
                clmc = bdcSjcl.getClmc();
                if (Integer.valueOf(bdcSjcl.getFs()) != null) {
                    fs = bdcSjcl.getFs();
                }
                if (bdcSjcl.getYs() != null) {
                    ys = bdcSjcl.getYs();
                }
                cllx = bdcSjcl.getCllx();
            }
        }
        model.addAttribute("cllx", cllx);
        model.addAttribute("clmc", clmc);
        model.addAttribute("fs", fs);
        model.addAttribute("ys", ys);
        model.addAttribute("sjxxid", sjxxid);
        model.addAttribute("sjclid", sjclid);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/bdcSjclxx";
    }


    @ResponseBody
    @RequestMapping(value = "getQlProidByWiid", method = RequestMethod.GET)
    public HashMap getQlProidByWiid(@RequestParam(value = "wiid", required = false) String wiid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String proid = "";
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm1 : bdcXmList) {
                if (!StringUtils.equals(bdcXm1.getQllx(), "18") && StringUtils.isNotBlank(bdcXm1.getProid())) {
                    proid = bdcXm1.getProid();
                }
            }
        }
        resultMap.put("proid", proid);
        return resultMap;
    }

    /**
     * @param proid 浏览该项目附件
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @description
     */
    @RequestMapping(value = "/browseFile", method = RequestMethod.GET)
    public String createAllFileFolder(Model model, @RequestParam(value = "proid", required = false) String proid) {
        String fileCenterUrl = "fcm";
        Integer project_fileId = null;
        fileCenterUrl = fileCenterUrl + AppConfig.getFileCenterUrl().substring(4);
        project_fileId = PlatformUtil.getProjectFileId(proid);
        model.addAttribute("fileCenterUrl", fileCenterUrl);
        model.addAttribute("nodeId", project_fileId);
        return "wf/batch/slxx/file-browse";
    }

    /**
     * @Author:<a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @Description: 生成证书
     * @Date 14:00 2017/04/05
     */
    @RequestMapping(value = "creatBdcqz")
    public String creatBdcqz(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        model.addAttribute("wiid", wiid);
        model.addAttribute("proid", proid);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/creatBdcqz";
    }

    /**
     * @Author:<a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @Description: 审核信息（签名)
     * @Date 14:00 2017/04/06
     */
    @RequestMapping(value = "bdcdjShxx")
    public String bdcdjShxx(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
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
        model.addAttribute("wiid", wiid);
        model.addAttribute("proid", proid);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/qtdjxx/bdcdjShxx";
    }

    /**
     * @Author:<a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @Description: 收件信息
     * @Date 14:00 2017/04/27
     */
    @RequestMapping(value = "bdcdjSjxx")
    public String bdcdjSjxx(Model model, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
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
            //获取登记子项
            List<HashMap> djzxList = bdcZdGlService.getDjzxByProid(proid);
            model.addAttribute("djzxList", djzxList);
            String djlxMc="";
            //bdc_xm
            BdcXm bdcXm=bdcXmService.getBdcXmByProid(proid);
            model.addAttribute("bdcXm", bdcXm);
            if(bdcXm!=null&&StringUtils.isNotBlank(bdcXm.getDjlx())){
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("dm", bdcXm.getDjlx());
                List<HashMap> djlxList = bdcZdGlService.getBdcZdDjlx(map);
                if (CollectionUtils.isNotEmpty(djlxList) && djlxList.get(0).get("MC") != null)
                    djlxMc=djlxList.get(0).get("MC").toString();
            }
            model.addAttribute("djlxMc", djlxMc);
            //bdc_spxx
            BdcSpxx bdcSpxx=bdcSpxxService.queryBdcSpxxByProid(proid);
            if(bdcSpxx==null){
                bdcSpxx=new BdcSpxx();
                bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                bdcSpxx.setProid(proid);
            }
            model.addAttribute("bdcSpxx", bdcSpxx);
            //获取WorkflowName
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowInstanceService.getWorkflowInstanceByProId(proid);
            if (pfWorkFlowInstanceVo != null) {
                PfWorkFlowDefineVo pfWorkFlowDefineVo = workFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                if (pfWorkFlowDefineVo != null) {
                    model.addAttribute("bdcdjlx", pfWorkFlowDefineVo.getWorkflowName());
                }
            }
            //bdc_sjxx
             BdcSjxx bdcSjxx=bdcSjxxService.queryBdcSjxxByProid(proid);
            if(bdcSjxx==null){
                bdcSjxx=new BdcSjxx();
                bdcSjxx.setSjr(getUserName());
                model.addAttribute(new BdcSjcl());
                model.addAttribute( "bdcSjxx",bdcSjxx);
                model.addAttribute("sjrq", CalendarUtil.formatDateToString( new Date()));
            }
            model.addAttribute("bdcSjxx", bdcSjxx);
        }
        return "wf/batch/slxx/bdcSjxx";
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 附属设施信息
     */
    @ResponseBody
    @RequestMapping("/getFsssxxPagesJson")
    public Object getFsssxxPagesJson(Pageable pageable, String wiid, String bdcdyid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)&&StringUtils.isNotBlank(bdcdyid)){
          BdcBdcdy bdcBdcdy=bdcdyService.queryBdcdyById(bdcdyid);
            if(bdcBdcdy!=null&&StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())){
                map.put("wiid", wiid);
                map.put("zfbdcdyh", bdcBdcdy.getBdcdyh());
            }
        }
        Page<HashMap> dataPaging = repository.selectPaging("getFsssxxByPage", map, pageable);
        return dataPaging;
    }
}
