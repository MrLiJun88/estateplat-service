package cn.gtmap.estateplat.server.web.query;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadJsonFileUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version V1.0, 2016-12-29
 * @description 不动产收费统计
 */
@Controller
@RequestMapping("/bdcSf")
public class BdcSfController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcYhService bdcYhService;
    @Autowired
    private BdcBankService bdcBankService;
    @Autowired
    private BdcSfxxService bdcSfxxService;
    @Autowired
    private BdcSfxmService bdcSfxmService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        List<Map> jkrList = new ArrayList<>();
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/pz/sflx.json");
        if (StringUtils.isNotBlank(jsonStr)) {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                Map map = new HashMap();
                map.put("jkrmc", entry.getKey());
                jkrList.add(map);
            }
        }
        model.addAttribute("jkrList", jkrList);
        model.addAttribute("bdcXtYhList", bdcBankService.getBankListByPage());
        return "query/bdcsf";
    }

    @ResponseBody
    @RequestMapping("/getBdcSfxxListJsonByPage")
    public Object getBdcSfxxListJson(Pageable pageable, String bh, String zl, String jkr, String bdcqzh, String fwbm, String cjqssj, String cjzzsj, String sflx) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(jkr)) {
            map.put("jkr", StringUtils.deleteWhitespace(jkr.trim()));
        }
        if (StringUtils.isNotBlank(cjqssj)) {
            map.put("cjqssj", cjqssj);
        }
        if (StringUtils.isNotBlank(cjzzsj)) {
            map.put("cjzzsj", cjzzsj);
        }
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/pz/sflx.json");
        if (StringUtils.isNotBlank(jsonStr)) {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if (jsonObject.containsKey(sflx)) {
                JSONObject object = (JSONObject) jsonObject.get(sflx);
                if (object.containsKey("sfxmmc")) {
                    String sfxmmc = String.valueOf(object.get("sfxmmc"));
                    map.put("sfxmmc", StringUtils.split(sfxmmc, ","));
                }
            }
        }
        return repository.selectPaging("getBdcSfxxListJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping(value = "changeSfzt")
    public String changeSfzt(String proid) {
        Example example = new Example(BdcSfxx.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proid", proid);
        List<BdcSfxx> bdcSfxxList = entityMapper.selectByExample(BdcSfxx.class, example);
        if (CollectionUtils.isNotEmpty(bdcSfxxList)) {
            for (BdcSfxx bdcSfxx : bdcSfxxList) {
                bdcSfxx.setSfzt("1");
                bdcSfxx.setSjrq(new Date());
                entityMapper.saveOrUpdate(bdcSfxx, bdcSfxx.getSfxxid());
            }
        }
        return null;
    }

    /**
     * 获取权利人
     *
     * @param model
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQlrByProid")
    public HashMap<String, Object> getQlrByProid(Model model, String proid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        StringBuilder qlrs = new StringBuilder();
        StringBuilder ywrs = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrByProid(proid, Constants.QLRLX_QLR);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (int i = 0; i < bdcQlrList.size(); i++) {
                    if (i == 0) {
                        qlrs.append(bdcQlrList.get(i).getQlrmc());
                    } else {
                        qlrs.append(",").append(bdcQlrList.get(i).getQlrmc());
                    }
                }
            }
            List<BdcQlr> bdcQlrYwrList = bdcQlrService.getBdcQlrByProid(proid, Constants.QLRLX_YWR);
            if (CollectionUtils.isNotEmpty(bdcQlrYwrList)) {
                for (int i = 0; i < bdcQlrYwrList.size(); i++) {
                    if (i == 0) {
                        ywrs.append(bdcQlrYwrList.get(i).getQlrmc());
                    } else {
                        ywrs.append(",").append(bdcQlrYwrList.get(i).getQlrmc());
                    }
                }
            }
        }
        resultMap.put("ywr", ywrs);
        resultMap.put("qlr", qlrs);
        return resultMap;
    }

    /**
     * 获取不动产权证号
     *
     * @param model
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcqzhByProid")
    public HashMap<String, Object> getBdcqzhByProid(Model model, String proid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        BdcXm bdcXm = null;
        List<BdcZs> bdcZsList = null;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcZsList = bdcZsService.getPlZsByWiid(bdcXm.getWiid());
        }
        StringBuilder bdcqzhs = new StringBuilder();
        //不动产证书list里面可能会存在重复的
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            for (int i = 0; i < bdcZsList.size(); i++) {
                if (i == 0) {
                    bdcqzhs.append(bdcZsList.get(i).getBdcqzh());
                } else {
                    if (bdcqzhs.indexOf(bdcZsList.get(i).getBdcqzh()) == -1) {
                        bdcqzhs.append(",").append(bdcZsList.get(i).getBdcqzh());
                    }
                }
            }
        }
        String[] bdcqzh = bdcqzhs.toString().split(",");
        if (bdcqzh.length > 1) {
            resultMap.put("bdcqzh", bdcqzh[0] + "等");
            return resultMap;
        }
        resultMap.put("bdcqzh", bdcqzhs);
        return resultMap;
    }

    /**
     * 获取坐落
     *
     * @param model
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getZlByProid")
    public HashMap<String, Object> getZlByProid(Model model, String proid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        BdcXm bdcXm = null;
        List<BdcSpxx> bdcSpxxList = null;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcSpxxList = bdcSpxxService.getBdcSpxxByWiid(bdcXm.getWiid());
        }
        String zls = "";
        if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
            if (bdcSpxxList.size() > 1) {
                zls = bdcSpxxList.get(0).getZl() + "等";
            } else {
                zls = bdcSpxxList.get(0).getZl();
            }
        }
        resultMap.put("zl", zls);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/getOtherByProid")
    public HashMap<String, Object> getOtherByProid(String proid, String sfxxid, String sflx) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String sqlxmc = "";
        String dzwyt = "";
        String sfxmid = "";
        double hj = 0.0;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx())) {
                sqlxmc = bdcZdGlService.getSqlxMcByDm(bdcXm.getSqlx());
            }
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getYt())) {
                dzwyt = bdcZdGlService.getFwytByDm(bdcSpxx.getYt());
            }
        }
        if (StringUtils.isNotBlank(sfxxid)) {
            HashMap map = new HashMap();
            if (StringUtils.isNotBlank(sflx)) {
                String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/pz/sflx.json");
                if (StringUtils.isNotBlank(jsonStr)) {
                    JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                    if (jsonObject.containsKey(sflx)) {
                        JSONObject object = (JSONObject) jsonObject.get(sflx);
                        if (object.containsKey("sfxmmc")) {
                            String sfxmmc = String.valueOf(object.get("sfxmmc"));
                            map.put("sfxmmc", StringUtils.split(sfxmmc, ","));
                        }
                    }
                }
            }
            map.put("sfxxid", sfxxid);
            List<BdcSfxm> bdcSfxmList = bdcSfxmService.queryBdcSfXm(map);
            if (CollectionUtils.isNotEmpty(bdcSfxmList)) {
                for (BdcSfxm bdcSfxm : bdcSfxmList) {
                    if (bdcSfxm.getJe() != null && bdcSfxm.getJe() > 0) {
                        hj = hj + bdcSfxm.getJe();
                        if (StringUtils.isNotBlank(sfxmid)) {
                            sfxmid += "," + bdcSfxm.getSfxmid();
                        } else {
                            sfxmid = bdcSfxm.getSfxmid();
                        }
                    }
                }
            }
        }
        resultMap.put("sqlxmc", sqlxmc);
        resultMap.put("dzwyt", dzwyt);
        resultMap.put("hj", hj);
        resultMap.put("sfxmid", sfxmid);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/printMul")
    public HashMap<String, Object> printMul(String bh, String zl, String jkr, String bdcqzh, String fwbm, String cjqssj, String cjzzsj, String sflx, String sfxxid, String sfxmid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        String uuid = UUIDGenerator.generate18();
        String cxrq = "";
        String skrzh = "";
        String skrkhyh = "";
        if (StringUtils.isNotBlank(bh)) {
            map.put("bh", StringUtils.deleteWhitespace(bh.trim()));
        }
        if (StringUtils.isNotBlank(zl)) {
            map.put("zl", StringUtils.deleteWhitespace(zl.trim()));
        }
        if (StringUtils.isNotBlank(jkr)) {
            map.put("jkr", StringUtils.deleteWhitespace(jkr.trim()));
        }
        if (StringUtils.isNotBlank(bdcqzh)) {
            map.put("bdcqzh", StringUtils.deleteWhitespace(bdcqzh.trim()));
        }
        if (StringUtils.isNotBlank(fwbm)) {
            map.put("fwbm", StringUtils.deleteWhitespace(fwbm.trim()));
        }
        if (StringUtils.isNotBlank(cjqssj)) {
            map.put("cjqssj", cjqssj);
            cxrq = cjqssj + "起";
        }
        if (StringUtils.isNotBlank(cjzzsj)) {
            map.put("cjzzsj", cjzzsj);
            if (StringUtils.isNotBlank(cxrq)) {
                cxrq += cjzzsj + "止";
            } else {
                cxrq = cjzzsj + "止";
            }
        }
        String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/pz/sflx.json");
        if (StringUtils.isNotBlank(jsonStr)) {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if (StringUtils.isBlank(sflx)) {
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    JSONObject object = (JSONObject) jsonObject.get(entry.getKey());
                    if (object.containsKey("skrzh") && StringUtils.isNotBlank((CharSequence) object.get("skrzh"))) {
                        if (StringUtils.isNotBlank(skrzh)) {
                            skrzh += "," + object.get("skrzh");
                        } else {
                            skrzh = String.valueOf(object.get("skrzh"));
                        }
                    }
                    if (object.containsKey("skrkhyh") && StringUtils.isNotBlank((CharSequence) object.get("skrkhyh"))) {
                        if (StringUtils.isNotBlank(skrkhyh)) {
                            skrkhyh += "," + object.get("skrkhyh");
                        } else {
                            skrkhyh = String.valueOf(object.get("skrkhyh"));
                        }
                    }
                }
            } else if (jsonObject.containsKey(sflx)) {
                JSONObject object = (JSONObject) jsonObject.get(sflx);
                if (object.containsKey("sfxmmc")) {
                    String sfxmmc = String.valueOf(object.get("sfxmmc"));
                    map.put("sfxmmc", StringUtils.split(sfxmmc, ","));
                }
                if (object.containsKey("skrzh") && StringUtils.isNotBlank((CharSequence) object.get("skrzh"))) {
                    skrzh = String.valueOf(object.get("skrzh"));
                }
                if (object.containsKey("skrkhyh") && StringUtils.isNotBlank((CharSequence) object.get("skrkhyh"))) {
                    skrkhyh = String.valueOf(object.get("skrkhyh"));
                }
            }
        }
        if (StringUtils.isBlank(sfxxid)) {
            List<Map> mapList = bdcSfxxService.getBdcSfxxList(map);
            if (CollectionUtils.isNotEmpty(mapList)) {
                for (Map result : mapList) {
                    if (result.containsKey("SFXXID")) {
                        if (StringUtils.isNotBlank(sfxxid)) {
                            sfxxid += "," + result.get("SFXXID");
                        } else {
                            sfxxid = String.valueOf(result.get("SFXXID"));
                        }
                        HashMap<String, Object> hashMap = getOtherByProid(null, String.valueOf(result.get("SFXXID")), sflx);
                        if (hashMap.containsKey("sfxmid") && StringUtils.isNotBlank((CharSequence) hashMap.get("sfxmid"))) {
                            if (StringUtils.isNotBlank(sfxmid)) {
                                sfxmid += "," + hashMap.get("sfxmid");
                            } else {
                                sfxmid = String.valueOf(hashMap.get("sfxmid"));
                            }
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(sfxxid)) {
            String[] sfxxids = StringUtils.split(sfxxid, ",");
            for (String id : sfxxids) {
                Map tempMap = new HashMap();
                tempMap.put("uuid", uuid);
                tempMap.put("id", id);
                bdcSfxxService.insertIdToTemp(tempMap);
            }
        }

        if (StringUtils.isNotBlank(sfxmid)) {
            String[] sfxmids = StringUtils.split(sfxmid, ",");
            for (String id : sfxmids) {
                Map tempMap = new HashMap();
                tempMap.put("uuid", uuid);
                tempMap.put("id", id);
                bdcSfxxService.insertIdToTemp(tempMap);
            }
        }

        resultMap.put("cxrq", cxrq);
        resultMap.put("skrzh", skrzh);
        resultMap.put("skrkhyh", skrkhyh);
        resultMap.put("sfxxid", sfxxid);
        resultMap.put("sfxmid", sfxmid);
        resultMap.put("uuid", uuid);
        return resultMap;
    }

    /**
     * 获取节点名称
     *
     * @param proid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getActivityNameByProid")
    public String getActivityNameByProid(String proid) {
        String taskid = null;
        String activityName = null;
        PfWorkFlowInstanceVo pf = super.getWorkFlowInstance(proid);
        SysTaskService sysTaskService = (SysTaskService) Container.getBean("TaskService");
        if (pf != null) {
            List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pf.getWorkflowIntanceId());
            if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
                taskid = pfTaskVoList.get(0).getTaskId();
                activityName = PlatformUtil.getPfActivityNameByTaskId(taskid);
            }
        }
        return activityName;
    }

    @ResponseBody
    @RequestMapping("/getSfdPrintData")
    public HashMap<String, Object> getSfdPrintData(String proid) {
        String zl = "";
        String sqlxmc = "";
        String isShowPljf = "";
        HashMap<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotBlank(proid)) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            zl = StringUtils.isNotBlank(bdcSpxx.getZl()) ? bdcSpxx.getZl() : "";
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                sqlxmc = bdcXmService.getSqlxMcByWiid(bdcXm.getWiid());
            }
            Boolean isHb = bdcXmService.isHb(bdcXm.getProid());
            resultMap.put("isHb", isHb);
            String wfDfid = PlatformUtil.getWfDfidByWiid(bdcXm.getWiid());
            String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(wfDfid);
            if (StringUtils.equals(AppConfig.getProperty("SFD.TYSF.SHOW"), "true")) {
                if (StringUtils.isNotBlank(sqlxdm) && CommonUtil.indexOfStrs(Constants.TYSF_SHOW_SQLX, sqlxdm)) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    for (BdcXm xm : bdcXmList) {
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(xm.getProid());
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                List<BdcXtYh> bdcXtYhList = bdcYhService.getBankListByYhmc(bdcQlr.getQlrmc());
                                if (CollectionUtils.isNotEmpty(bdcXtYhList) && StringUtils.equals(bdcXtYhList.get(0).getSftysf(), Constants.SFTYSF_S)) {
                                    isShowPljf = "true";
                                }
                            }
                        }
                    }
                }
            }
        }
        resultMap.put("zl", zl);
        resultMap.put("sqlxMc", sqlxmc);
        resultMap.put("isShowPljf", isShowPljf);
        resultMap.put("isSfztVersion", AppConfig.getProperty("sfd.version.sfzt"));
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/delPrintBdcSfxxByUuid")
    public HashMap<String, Object> delPrintBdcSfxxByUuid(String uuid) {
        if (StringUtils.isNotBlank(uuid)) {
            bdcSfxxService.delPrintBdcSfxxTemp(uuid);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("/getSfxmmc")
    public Map getSfxmmc(String sflx) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotBlank(sflx)) {
            String jsonStr = ReadJsonFileUtil.readJsonFile("/conf/server/pz/sflx.json");
            if (StringUtils.isNotBlank(jsonStr)) {
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                if (jsonObject.containsKey(sflx)) {
                    JSONObject object = (JSONObject) jsonObject.get(sflx);
                    if (object.containsKey("sfxmmc")) {
                        String sfxmmc = String.valueOf(object.get("sfxmmc"));
                        if(StringUtils.isNotBlank(sfxmmc)) {
                            String[] sfxmmcs = StringUtils.split(sfxmmc, ",");
                            sfxmmc=StringUtils.EMPTY;
                            for (String mc : sfxmmcs) {
                                sfxmmc+="'"+mc+"',";
                            }
                            resultMap.put("sfxmmc", sfxmmc.substring(0,sfxmmc.length()-1));
                        }
                    }
                }
            }
        }
        return resultMap;
    }
}
