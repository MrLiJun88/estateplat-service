package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcFwfsssMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2017/04/13
 * @description  房屋附属设施管理
 */
@Controller
@RequestMapping("/bdcFwfsss")
public class BdcFwfsssController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    BdcFwFsssService bdcFwFsssService;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    ProjectService projectService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcFwfsssMapper bdcFwfsssMapper;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid
            , @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        model.addAttribute(ParamsConstants.WIID_LOWERCASE, wiid);
        return "/fsss/glFsss";
    }


    @RequestMapping(value = "addIndex")
    public String addIndex(Model model, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid) {
        model.addAttribute(ParamsConstants.WIID_LOWERCASE, wiid);
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        model.addAttribute(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
        return "/fsss/addFsss";
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 获取过渡房屋信息, 关联表gd_xm, gd_fw, gd_bdc_ql_rel,以及过渡相关权利表
     */
    @ResponseBody
    @RequestMapping("/getGdFwForSyqJsonByPage")
    public Object getGdFwForSyqJsonByPage(Pageable pageable, String hhSearch, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("hhSearch", dcxc);
        }
        return repository.selectPaging("getGdFwfsssJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getDjsjBdcdyPagesJson")
    public Object getBdcXmPagesJson(Pageable pageable, String djh, String bdcdyh, String dcxc, String qlr, String tdzl, String bdcdyhs) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //附属设施肯定是TDFW
        if (StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
            String[] djids = bdcdyService.getDjQlrIdsByQlr(dcxc, Constants.BDCLX_TDFW);
            if (djids != null && djids.length > 0)
                map.put("djids", djids);
            String bdcdycxFwbhZdmc = AppConfig.getProperty("bdcdycx.fwbh.zdmc");
            if (StringUtils.isNotBlank(bdcdycxFwbhZdmc)) {
                map.put("fwbh", "t." + bdcdycxFwbhZdmc + "='" + StringUtils.deleteWhitespace(dcxc) + "'");
            }
        } else {
            if (StringUtils.isNotBlank(djh)) {
                map.put("djh", djh);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if (StringUtils.isNotBlank(tdzl)) {
                map.put("tdzl", tdzl);
            }
        }
        map.put("bdclx", Constants.BDCLX_TDFW);
        if (StringUtils.isNotBlank(bdcdyhs)) {
            List<String> bdcdyhList = new ArrayList<String>();
            for (String tempBdcdyh : bdcdyhs.split(",")) {
                if (StringUtils.isNotBlank(tempBdcdyh)) {
                    bdcdyhList.add(tempBdcdyh);
                }
            }
            map.put("bdcdyhs", bdcdyhList);
        }
        //根据行政区过滤
        String userDwdm = super.getWhereXzqdm();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(userDwdm))
            map.put("xzqdm", userDwdm);

        map.put("isfsss", "1");
        return repository.selectPaging("getDjsjBdcdyByPage", map, pageable);
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 获取主房屋信息
     */
    @ResponseBody
    @RequestMapping("/getBdcFwxxJson")
    public Object getBdcFwxxJson(Pageable pageable, @RequestParam(value = "wiid", required = false) String wiid, String zl) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }
        if (StringUtils.isNotBlank(zl)) {
            map.put("zl", zl);
        }
        return repository.selectPaging("getFwBdcdyJsonByPage", map, pageable);
    }

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 获取附属设施信息
     */
    @ResponseBody
    @RequestMapping("/getBdcFwFsssJson")
    public Object getBdcFwFsssJson(Pageable pageable, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "hhSearch", required = false) String hhSearch, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, String proid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
            if (StringUtils.isNotBlank(bdcdyid)) {
                map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiidAndBdcdyid(wiid, bdcdyid);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    BdcXm bdcXm = bdcXmList.get(0);
                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                        map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(proid)) {
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
        }
        return repository.selectPaging("getBdcFwFsssJsonByPage", map, pageable);
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 关联主房和附属设施
     */
    @ResponseBody
    @RequestMapping(value = "glFsss")
    public HashMap glFsss(@RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "fwfsssids", required = false) String fwfsssids, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        HashMap result = new HashMap();
        BdcSpxx bdcSpxx = new BdcSpxx();
        BdcFdcq bdcFdcq = new BdcFdcq();
        String msg = "关联失败";
        try {
            if (StringUtils.isNotBlank(bdcdyh) && StringUtils.isNotBlank(fwfsssids)) {
                if (StringUtils.isNotBlank(proid)) {
                    bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
                    if (CollectionUtils.isNotEmpty(bdcFdcqList))
                        bdcFdcq = bdcFdcqList.get(0);
                }
                //根据配置判断附属设施面积累加到发证面积中时，是否房屋土地面积都累加还是只累加房屋面积
                Boolean addFsssFwAndTdIntoFzmj = bdcFwFsssService.checkAddAllMjIntoFzmj(wiid);
                String[] fwfsssidList = fwfsssids.split(",");
                if (fwfsssidList != null && fwfsssidList.length > 0) {
                    for (String fwfssid : fwfsssidList) {
                        BdcFwfsss bdcFwfsss = entityMapper.selectByPrimaryKey(BdcFwfsss.class, fwfssid);
                        if (bdcFwfsss != null && StringUtils.isNotBlank(bdcFwfsss.getZfbdcdyh()) && StringUtils.equals(bdcdyh, bdcFwfsss.getZfbdcdyh())) {
                            continue;
                        }

                        if (bdcFwfsss != null && StringUtils.equals(CommonUtil.formatEmptyValue(bdcFwfsss.getJrfzmj()), Constants.BDCFWFSSS_JRFZMJ_YES)) {
                            //附属设施的面积在关联的时候加入发证面积，bdc_spxx面积和产权的面积
                            bdcFwFsssService.addBdcFwfsssMj(bdcFwfsss, bdcSpxx);
                            bdcFwFsssService.addBdcFwfsssMj(wiid, bdcFwfsss, bdcFdcq);
                            if (addFsssFwAndTdIntoFzmj) {
                                bdcFwFsssService.addBdcFwfsssTdmj(bdcFwfsss, bdcSpxx);
                            }
                        }
                        if (bdcFwfsss != null) {
                            bdcFwfsss.setZfbdcdyh(bdcdyh);
                            entityMapper.saveOrUpdate(bdcFwfsss, bdcFwfsss.getFwfsssid());
                            msg = "关联成功";
                        }
                    }
                }
            }
        } catch (Exception e) {
            msg = "关联失败";
            logger.error("BdcFwfsssController.glFsss", e);
        }
        result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return result;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 取消关联主房和附属设施
     */
    @ResponseBody
    @RequestMapping(value = "disGlFsss")
    public HashMap disGlFsss(@RequestParam(value = "fwfsssids", required = false) String fwfsssids, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        HashMap result = new HashMap();
        BdcSpxx bdcSpxx = new BdcSpxx();
        BdcFdcq bdcFdcq = null;
        String msg = "取消关联失败";
        try {
            if (StringUtils.isNotBlank(fwfsssids) && StringUtils.isNoneBlank(wiid)) {
                //根据配置判断附属设施面积累加到发证面积中时，是否房屋土地面积都累加还是只累加房屋面积
                Boolean addFsssFwAndTdIntoFzmj = bdcFwFsssService.checkAddAllMjIntoFzmj(wiid);
                String[] fwfsssidList = fwfsssids.split(",");
                if (fwfsssidList != null && fwfsssidList.length > 0) {
                    //拿到选择的fsss对应fwbm，通过fwbm取到该附属设施关联到的当前流程下的所有proid，处理所有被关联的项目的相关面积
                    for (String fwfssid : fwfsssidList) {
                        BdcFwfsss bdcFwfsss = bdcFwFsssService.getFwfsssByfwfssid(fwfssid);
                        if (StringUtils.isNoneBlank(bdcFwfsss.getFwbm())) {
                            HashMap map = new HashMap();
                            map.put("fwbm", bdcFwfsss.getFwbm());
                            map.put("wiid", wiid);
                            List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssList(map);
                            if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                                for (BdcFwfsss bdcFwfsssTemp : bdcFwfsssList) {
                                    if (bdcFwfsssTemp != null && StringUtils.isBlank(bdcFwfsssTemp.getZfbdcdyh())) {
                                        continue;
                                    }
                                    bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcFwfsssTemp.getProid());
                                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcFwfsssTemp.getProid());
                                    if (bdcFwfsssTemp != null && StringUtils.equals(CommonUtil.formatEmptyValue(bdcFwfsssTemp.getJrfzmj()), Constants.BDCFWFSSS_JRFZMJ_YES)) {
                                        //取消关联的时候发证面积要减去附属设施的面积
                                        bdcFwFsssService.subtractBdcFwfsssMj(bdcFwfsssTemp, bdcSpxx, bdcFdcqList);
                                        //合并流程关联附属设施不带入抵押时，同步抵押spxx中的mj
                                        bdcFwFsssService.synchronizeDyaSpxxMj(wiid);
                                        if (addFsssFwAndTdIntoFzmj) {
                                            bdcFwFsssService.subtractBdcFwfsssTdmj(bdcFwfsssTemp, bdcSpxx);
                                        }
                                    }
                                    if (bdcFwfsssTemp != null) {
                                        bdcFwfsssTemp.setZfbdcdyh(null);
                                        bdcFwFsssService.saveBdcFwfsssNull(bdcFwfsssTemp);
                                        msg = "取消关联成功";
                                    }
                                }
                            }
                            bdcFwFsssService.subtractBdcFwfsssFwDymj(bdcFwfsss, wiid, null);
                        }

                    }
                }
            }
        } catch (Exception e) {
            msg = "取消关联失败";
            logger.error("BdcFwfsssController.disGlFsss", e);
        }
        result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return result;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 删除附属设施
     */
    @ResponseBody
    @RequestMapping(value = "delFsss")
    public HashMap delFsss(@RequestParam(value = "fwfsssids", required = false) String fwfsssids, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        HashMap result = new HashMap();
        BdcSpxx bdcSpxx = new BdcSpxx();
        BdcFdcq bdcFdcq = new BdcFdcq();
        String msg = "删除失败";
        try {
            if (StringUtils.isNotBlank(fwfsssids) && StringUtils.isNoneBlank(wiid)) {
                //根据配置判断附属设施面积累加到发证面积中时，是否房屋土地面积都累加还是只累加房屋面积
                Boolean addFsssFwAndTdIntoFzmj = bdcFwFsssService.checkAddAllMjIntoFzmj(wiid);
                String[] fwfsssidList = fwfsssids.split(",");
                if (fwfsssidList != null && fwfsssidList.length > 0) {
                    for (String fwfsssid : fwfsssidList) {
                        //拿到选择的fsss对应fwbm，通过fwbm取到该附属设施关联到的当前流程下的所有proid，处理所有被关联的项目的相关面积
                        BdcFwfsss bdcFwfsss = bdcFwFsssService.getFwfsssByfwfssid(fwfsssid);
                        if (StringUtils.isNoneBlank(bdcFwfsss.getFwbm())) {
                            HashMap map = new HashMap();
                            map.put("fwbm", bdcFwfsss.getFwbm());
                            map.put("wiid", wiid);
                            List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssList(map);
                            if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                                for (BdcFwfsss bdcFwfsssTemp : bdcFwfsssList) {
                                    if (bdcFwfsssTemp != null && StringUtils.isNotBlank(bdcFwfsssTemp.getZfbdcdyh()) && StringUtils.equals(CommonUtil.formatEmptyValue(bdcFwfsssTemp.getJrfzmj()), Constants.BDCFWFSSS_JRFZMJ_YES)) {
                                        bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcFwfsssTemp.getProid());
                                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcFwfsssTemp.getProid());
                                        bdcFwFsssService.subtractBdcFwfsssMj(bdcFwfsssTemp, bdcSpxx, bdcFdcqList);
                                        if (addFsssFwAndTdIntoFzmj) {
                                            bdcFwFsssService.subtractBdcFwfsssTdmj(bdcFwfsssTemp, bdcSpxx);
                                        }
                                    }
                                    if (bdcFwfsssTemp != null) {
                                        bdcFwFsssService.delBdcFwfsssByFwfsssid(bdcFwfsssTemp.getFwfsssid());
                                    }
                                    msg = "删除成功";
                                }
                            }
                            bdcFwFsssService.subtractBdcFwfsssFwDymj(bdcFwfsss, wiid, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            msg = "删除失败";
            logger.error("BdcFwfsssController.delFsss", e);
        }
        result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return result;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 取消随同主房
     */
    @ResponseBody
    @RequestMapping(value = "delWithZf")
    public HashMap delWithZf(@RequestParam(value = "fwfsssids", required = false) String fwfsssids, @RequestParam(value = "qllx", required = false) String qllx) {
        HashMap result = new HashMap();
        String msg = "删除失败";
        try {
            if (StringUtils.isNotBlank(fwfsssids)) {
                String[] fwfsssidList = fwfsssids.split(",");
                if (fwfsssidList != null && fwfsssidList.length > 0) {
                    for (String fwfssid : fwfsssidList) {
                        BdcFwfsss bdcFwfsss = bdcFwFsssService.getFwfsssByfwfssid(fwfssid);
                        if (bdcFwfsss != null && StringUtils.isNotBlank(bdcFwfsss.getWiid()) && StringUtils.isNotBlank(qllx)) {
                            //删除对应抵押、查封项目的fsss信息
                            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcFwfsss.getWiid());
                            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                                for (BdcXm bdcXmTmp : bdcXmList) {
                                    if (StringUtils.isNotBlank(bdcXmTmp.getProid()) && StringUtils.isNotBlank(bdcXmTmp.getQllx()) && StringUtils.equals(bdcXmTmp.getQllx(), qllx)) {
                                        List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssListByProid(bdcXmTmp.getProid());
                                        if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                                            for (BdcFwfsss bdcFwfsssTmp : bdcFwfsssList) {
                                                if (bdcFwfsss != null && StringUtils.isNotBlank(bdcFwfsss.getZfbdcdyh())
                                                        && StringUtils.equals(CommonUtil.formatEmptyValue(bdcFwfsss.getJrfzmj()), Constants.BDCFWFSSS_JRFZMJ_YES)) {
                                                    bdcFwFsssService.subtractBdcFwfsssFwDymj(bdcFwfsss, null, bdcXmTmp.getProid());
                                                }
                                                if (StringUtils.isNotBlank(bdcFwfsssTmp.getFwfsssid()) && StringUtils.equals(bdcXmTmp.getProid(), bdcFwfsssTmp.getProid())) {
                                                    bdcFwFsssService.delBdcFwfsssByFwfsssid(bdcFwfsssTmp.getFwfsssid());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        msg = "删除成功";
                    }
                }
            }
        } catch (Exception e) {
            msg = "删除失败";
            logger.error("BdcFwfsssController.delWithZf", e);
        }
        result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return result;
    }

    /**
     * @param bdcdyid 不动产单元id
     * @param wiid    流程实例id
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 检查主房的权利状态用于确认附属设施是不是随同抵押或者随同查封
     */
    @ResponseBody
    @RequestMapping(value = "checkZfqlzt", method = RequestMethod.GET)
    public HashMap checkZfqlzt(@RequestParam(value = "bdcdyid", required = false) String bdcdyid, @RequestParam(value = "wiid", required = false) String wiid) {
        HashMap result = new HashMap();
        String qlzt = "no";
        if (StringUtils.isNotBlank(bdcdyid) && StringUtils.isNotBlank(wiid)) {
            //获取同一流程实例中同一不动产单元的所有项目
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiidAndBdcdyid(wiid, bdcdyid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    if (StringUtils.isNotBlank(bdcXm.getQllx())) {
                        if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_CFDJ)) {
                            qlzt = "cf";
                            break;
                        } else if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                            qlzt = "dy";
                            break;
                        }
                    }
                }
            }
        }
        result.put("qlzt", qlzt);
        return result;
    }

    /**
     * @param proid
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @rerutn
     * @description 检查过渡一证多房是否存在附属设施
     */
    @ResponseBody
    @RequestMapping(value = "checkExistFsssForGd", method = {RequestMethod.POST, RequestMethod.GET})
    public HashMap checkExistFsssForGd(@RequestParam(value = "proid", required = false) String proid) {
        HashMap result = new HashMap();
        String msg = bdcFwFsssService.checkExistFsssForGd(proid);
        result.put("msg", msg);
        return result;
    }

    /**
     * @param proid
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 检查主房是否存在附属设施
     */
    @ResponseBody
    @RequestMapping(value = "checkExistFsss", method = {RequestMethod.POST, RequestMethod.GET})
    public HashMap checkExistFsss(@RequestParam(value = "proid", required = false) String proid) {
        HashMap result = new HashMap();
        String msg = bdcFwFsssService.checkExistFsss(proid);
        result.put("msg", msg);
        return result;
    }

    /**
     * @param qlFlag 用于判断附属设施是否随同主房一起抵押一起查封，当然这两个权利是不会同时出现的。
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 添加主房和附属设施（过渡）
     */
    @ResponseBody
    @RequestMapping(value = "addGdFsss")
    public HashMap addGdFsss(@RequestParam(value = "fwids", required = false) String fwids, @RequestParam(value = "qlids", required = false) String qlids,
                             @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid,
                             @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "qlFlag", required = false) String qlFlag,
                             @RequestParam(value = "jrfzmj", required = false) String jrfzmj) {
        HashMap result = new HashMap();
        BdcSpxx bdcSpxx = new BdcSpxx();
        String msg = "关联失败";
        try {
            if (StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(bdcdyh) && StringUtils.isNotBlank(fwids)) {
                String[] fwidList = fwids.split(",");
                List<BdcXm> bdcXmList = null;
                //不随同抵押或者查封，就要把相应的项目去掉
                if (StringUtils.equals(qlFlag, "true")) {
                    bdcXmList = bdcXmService.getBdcXmListByWiidAndBdcdyid(wiid, bdcdyid);
                } else {
                    HashMap map = new HashMap();
                    map.put(ParamsConstants.WIID_LOWERCASE, wiid);
                    map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                    map.put("noDy", "true");
                    map.put("noCf", "true");
                    bdcXmList = bdcXmService.getBdcXmList(map);
                }
                if (fwidList != null && fwidList.length > 0) {
                    for (int i = 0; i < fwidList.length; i++) {
                        String fwid = fwidList[i];
                        if (StringUtils.isNotBlank(fwid)) {
                            GdFw gdFw = gdFwService.queryGdFw(fwid);
                            List<BdcFwfsss> bdcFwfsssList = null;
                            if (gdFw != null && StringUtils.isNotBlank(gdFw.getDah())) {
                                HashMap map = new HashMap();
                                map.put("fwbm", gdFw.getDah());
                                bdcFwfsssList = bdcFwFsssService.getBdcFwfsssList(map);
                            }
                            if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                                msg = "该附属设施已关联！";
                                continue;
                            }

                            //判断是否匹配不动产单元
                            String gdFwBdcdyh = "";
                            String gdFwQjid = "";
                            List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", fwid);
                            //理论上一个房子匹配的不动产单元应该是一样的。
                            if (CollectionUtils.isNotEmpty(gdDyhRelList) && StringUtils.isNotEmpty(gdDyhRelList.get(0).getBdcdyh()) && StringUtils.isNotEmpty(gdDyhRelList.get(0).getDjid())) {
                                gdFwBdcdyh = gdDyhRelList.get(0).getBdcdyh();
                                gdFwQjid = gdDyhRelList.get(0).getDjid();
                            }
                            //根据是否匹配不动产单元分别处理，匹配不动产单元需要生成权利，不匹配则不需要。
                            if (StringUtils.isNotBlank(gdFwBdcdyh) && StringUtils.isNotBlank(gdFwQjid)) {
                                //标记过渡房屋为附属设施
                                gdFwService.setFsss(fwid, "1");
                                //增加附属设施信息
                                BdcFwfsss bdcFwfsss = bdcFwFsssService.intiFwfsssByFwid(fwid);

                                if (StringUtils.equals(jrfzmj, Constants.BDCFWFSSS_JRFZMJ_YES)) {
                                    bdcFwfsss.setJrfzmj(Short.valueOf(Constants.BDCFWFSSS_JRFZMJ_YES));
                                    //bdc_spxx坐落增加附属设施信息
                                    for (BdcXm bdcXmTemp : bdcXmList) {
                                        bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXmTemp.getProid());
                                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXmTemp.getProid());
                                        bdcFwFsssService.addBdcFwfsssMj(wiid, bdcFwfsss, bdcSpxx, bdcFdcqList);
                                        //合并流程关联附属设施不带入抵押时，同步抵押spxx中的mj
                                        bdcFwFsssService.synchronizeDyaSpxxMj(wiid);
                                        bdcFwFsssService.saveMjFromBdcSpxxToBdcDyaq(bdcXmTemp.getProid());
                                    }
                                } else {
                                    bdcFwfsss.setJrfzmj(Short.valueOf(Constants.BDCFWFSSS_JRFZMJ_NO));
                                }

                                //循环项目为给个项目单独插入bdc_fwsss信息
                                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                                    for (BdcXm bdcXm : bdcXmList) {
                                        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                            //判断项目的bdcdyh与传进来的是否一致，一致的情况下允许为该项目插入附属设施信息
                                            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && StringUtils.equals(bdcdyh, bdcBdcdy.getBdcdyh()) && bdcFwfsss != null) {
                                                List<BdcZdFwyt> bdcZdFwytList = bdcZdGlService.getBdcZdFwyt();
                                                //gd_fw用途为名称，保存bdcfwfsss时保存代码
                                                for (BdcZdFwyt bdcZdFwyt : bdcZdFwytList) {
                                                    if (StringUtils.equals(bdcZdFwyt.getMc().trim(), bdcFwfsss.getGhyt().trim())) {
                                                        bdcFwfsss.setGhyt(bdcZdFwyt.getDm());
                                                    }
                                                }
                                                bdcFwfsss.setBdcdyh(gdFwBdcdyh);
                                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                                bdcFwfsss.setZfbdcdyh(bdcdyh);
                                                bdcFwfsss.setWiid(wiid);
                                                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                                                    bdcFwfsss.setProid(bdcXm.getProid());
                                                }
                                                bdcFwFsssService.saveBdcFwfsss(bdcFwfsss);
                                                msg = ParamsConstants.SUCCESS_LOWERCASE;
                                            }
                                        }
                                    }
                                }
                            } else {
                                //标记过渡房屋为附属设施
                                gdFwService.setFsss(fwid, "1");
                                BdcFwfsss bdcFwfsss = bdcFwFsssService.intiFwfsssByFwid(fwid);

                                if (StringUtils.equals(jrfzmj, Constants.BDCFWFSSS_JRFZMJ_YES)) {
                                    bdcFwfsss.setJrfzmj(Short.valueOf(Constants.BDCFWFSSS_JRFZMJ_YES));
                                    for (BdcXm bdcXmTemp : bdcXmList) {
                                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXmTemp.getProid());
                                        bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXmTemp.getProid());
                                        bdcFwFsssService.addBdcFwfsssMj(wiid, bdcFwfsss, bdcSpxx, bdcFdcqList);
                                        //合并流程关联附属设施不带入抵押时，同步抵押spxx中的mj
                                        bdcFwFsssService.synchronizeDyaSpxxMj(wiid);
                                        bdcFwFsssService.saveMjFromBdcSpxxToBdcDyaq(bdcXmTemp.getProid());
                                    }
                                } else {
                                    bdcFwfsss.setJrfzmj(Short.valueOf(Constants.BDCFWFSSS_JRFZMJ_NO));
                                }

                                //循环项目为给个项目单独插入bdc_fwsss信息
                                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                                    for (BdcXm bdcXm : bdcXmList) {
                                        //判断项目的bdcdyh与传进来的是否一致，一致的情况下允许为该项目插入附属设施信息
                                        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && StringUtils.equals(bdcdyh, bdcBdcdy.getBdcdyh()) && bdcFwfsss != null) {
                                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                                bdcFwfsss.setZfbdcdyh(bdcdyh);
                                                bdcFwfsss.setWiid(wiid);
                                                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                                                    bdcFwfsss.setProid(bdcXm.getProid());
                                                }
                                                bdcFwFsssService.saveBdcFwfsss(bdcFwfsss);
                                                msg = ParamsConstants.SUCCESS_LOWERCASE;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            msg = "关联失败";
            logger.error("BdcFwfsssController.addGdFsss", e);
        }
        result.put("msg", msg);
        return result;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 添加主房和附属设施（不动产）
     */
    @ResponseBody
    @RequestMapping(value = "addBdcFsss")
    public HashMap addBdcFsss(@RequestParam(value = "djids", required = false) String djids, @RequestParam(value = "bdcdyhs", required = false) String bdcdyhs,
                              @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid,
                              @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "qlFlag", required = false) String qlFlag,
                              @RequestParam(value = "jrfzmj", required = false) String jrfzmj) {
        HashMap result = new HashMap();
        BdcSpxx bdcSpxx = null;
        String msg = "关联失败";
        if (StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(bdcdyh) && StringUtils.isNotBlank(djids) && StringUtils.isNotBlank(bdcdyhs)) {
            String[] djidList = djids.split(",");
            String[] bdcdyhList = bdcdyhs.split(",");
            List<BdcXm> bdcXmList = null;
            //不随同抵押或者查封，就要把相应的项目去掉
            if (StringUtils.equals("true", qlFlag)) {
                bdcXmList = bdcXmService.getBdcXmListByWiidAndBdcdyid(wiid, bdcdyid);
            } else {
                HashMap map = new HashMap();
                map.put(ParamsConstants.WIID_LOWERCASE, wiid);
                map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                map.put("noDy", "true");
                map.put("noCf", "true");
                bdcXmList = bdcXmService.getBdcXmList(map);
            }
            //根据配置判断附属设施面积累加到发证面积中时，是否房屋土地面积都累加还是只累加房屋面积
            Boolean addFsssFwAndTdIntoFzmj = bdcFwFsssService.checkAddAllMjIntoFzmj(wiid);

            if (djidList != null && djidList.length > 0) {
                for (int i = 0; i < djidList.length; i++) {
                    String djid = djidList[i];
                    String fsssBdcdyh = bdcdyhList[i];
                    HashMap map = new HashMap();
                    map.put(ParamsConstants.WIID_LOWERCASE, wiid);
                    map.put(ParamsConstants.ZFBDCDYH_LOWERCASE, bdcdyh);
                    map.put(ParamsConstants.BDCDYH_LOWERCASE, fsssBdcdyh);
                    List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssList(map);
                    if (CollectionUtils.isNotEmpty(bdcFwfsssList))
                        continue;

                    BdcFwfsss bdcFwfsss = bdcFwFsssService.getFwfsssByQjid(djid);
                    if (bdcFwfsss != null && StringUtils.equals(jrfzmj, Constants.BDCFWFSSS_JRFZMJ_YES)) {
                        bdcFwfsss.setJrfzmj(Short.valueOf(Constants.BDCFWFSSS_JRFZMJ_YES));
                        for (BdcXm bdcXmTemp : bdcXmList) {
                            bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXmTemp.getProid());
                            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXmTemp.getProid());
                            bdcFwFsssService.addBdcFwfsssMj(wiid, bdcFwfsss, bdcSpxx, bdcFdcqList);
                            if (addFsssFwAndTdIntoFzmj) {
                                bdcFwFsssService.addBdcFwfsssTdmj(bdcFwfsss, bdcSpxx);
                            }
                            //合并流程关联附属设施不带入抵押时，同步抵押spxx中的mj
                            bdcFwFsssService.synchronizeDyaSpxxMj(wiid);
                            bdcFwFsssService.saveMjFromBdcSpxxToBdcDyaq(bdcXmTemp.getProid());
                        }

                    } else if (bdcFwfsss != null) {
                        bdcFwfsss.setJrfzmj(Short.valueOf(Constants.BDCFWFSSS_JRFZMJ_NO));
                    }

                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        for (BdcXm bdcXm : bdcXmList) {
                            if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && StringUtils.equals(bdcdyh, bdcBdcdy.getBdcdyh()) && bdcFwfsss != null) {
                                    bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                    bdcFwfsss.setZfbdcdyh(bdcdyh);
                                    bdcFwfsss.setWiid(wiid);
                                    if (StringUtils.isNotEmpty(bdcXm.getProid())) {
                                        bdcFwfsss.setProid(bdcXm.getProid());
                                    }
                                    bdcFwFsssService.saveBdcFwfsss(bdcFwfsss);
                                    msg = ParamsConstants.SUCCESS_LOWERCASE;
                                }
                            }
                        }
                    }
                }
            }
        }
        result.put("msg", msg);
        return result;
    }

    /**
     * @param proid
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 检查主房是否存在附属设施
     */
    @ResponseBody
    @RequestMapping(value = "getFsssBdcXm", method = RequestMethod.GET)
    public HashMap getFsssBdcXm(@RequestParam(value = "proid", required = false) String proid) {
        HashMap result = new HashMap();
        StringBuilder yxmids = new StringBuilder();
        StringBuilder bdcdyhs = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            List<BdcFwfsss> bdcFwFsssList = bdcFwFsssService.getBdcFwfsssListByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcFwFsssList)) {
                List<BdcXm> fsssBdcXmList = bdcFwFsssService.getFsssBdcXmList(bdcFwFsssList);
                if (CollectionUtils.isNotEmpty(fsssBdcXmList)) {
                    for (BdcXm bdcXm : fsssBdcXmList) {
                        if (StringUtils.isBlank(yxmids)) {
                            yxmids.append(bdcXm.getProid());
                        } else {
                            yxmids.append("," + bdcXm.getProid());
                        }
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                            if (StringUtils.isBlank(bdcdyhs)) {
                                bdcdyhs.append(bdcBdcdy.getBdcdyh());
                            } else {
                                yxmids.append("," + bdcBdcdy.getBdcdyh());
                            }
                        }
                    }
                }
            }
        }
        result.put("yxmids", yxmids);
        result.put("bdcdyhs", bdcdyhs);
        return result;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 初始化不动产附属设施的项目
     */
    @ResponseBody
    @RequestMapping(value = "initVoFromFsss")
    public String initVoFromFsss(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "ybdcdyh", required = false) String ybdcdyh
            , @RequestParam(value = "yproid", required = false) String yproid) {
        String msg = "继承失败";
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(ybdcdyh) && StringUtils.isNotBlank(yproid)) {
            //jyl 主房项目
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            //jyl 原项目
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
            Project project = new Project();
            //初始化附属设施项目和权利
            String newProid = UUIDGenerator.generate18();
            project = bdcXmService.getProjectFromBdcXm(bdcXm, project);
            project.setProid(newProid);
            project.setBdcdyh(ybdcdyh);
            project.setDjId(null);
            BdcXmRel bdcXmRel = new BdcXmRel();
            bdcXmRel.setProid(newProid);
            bdcXmRel.setYproid(yproid);
            bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
            List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
            bdcXmRelList.add(bdcXmRel);
            project.setBdcXmRelList(bdcXmRelList);
            project.setBdcdyid(null);
            project.setYxmid(yproid);
            project.setYbdcdyid(ybdcXm.getBdcdyid());
            bdcFwFsssService.initBdcFwfsssByProject(project);
            msg = "继承成功";
        }
        return msg;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 初始化不动产附属设施的项目
     */
    @ResponseBody
    @RequestMapping(value = "initBdcFwfsssFromFsss")
    public String initBdcFwfsssFromFsss(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "yproid", required = false) String yproid) {
        String msg = "继承失败";
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(yproid)) {
            //jyl 主房项目
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            List<BdcXm> bdcXmList = null;
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            }
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                if (bdcXmList.size() > 1) {
                    BdcXm cqXm = null;
                    for (BdcXm xm : bdcXmList) {
                        QllxVo qllxVo = qllxService.makeSureQllx(xm);
                        if (qllxVo instanceof BdcFdcq) {
                            cqXm = xm;
                        }
                    }
                    BdcFdcq bdcFdcq = null;
                    BdcSpxx cqSpxx = null;
                    if (cqXm != null) {
                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(cqXm.getProid());
                        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                            bdcFdcq = bdcFdcqList.get(0);
                        }
                        cqSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                    }
                    if (bdcFdcq != null && cqSpxx != null) {
                        for (BdcXm xm : bdcXmList) {
                            QllxVo qllxVo = qllxService.makeSureQllx(xm);
                            if (qllxVo instanceof BdcDyaq) {
                                BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(xm.getProid());
                                bdcDyaq.setFwdymj(cqSpxx.getMj());
                                BdcSpxx dySpxx = bdcSpxxService.queryBdcSpxxByProid(xm.getProid());
                                dySpxx.setMj(cqSpxx.getMj());
                                List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssListByProidOnly(cqXm.getProid());
                                if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                                    for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                                        bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                        bdcFwfsss.setWiid(xm.getWiid());
                                        bdcFwfsss.setProid(xm.getProid());
                                        bdcFwfsss.setYproid(yproid);
                                        bdcFwFsssService.saveBdcFwfsss(bdcFwfsss);
                                        msg = "继承成功";
                                    }
                                }
                            }
                        }
                    }

                } else {
                    List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssListByProidOnly(yproid);
                    if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                        if (!(qllxVo instanceof BdcFdcq)) {
                            for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                bdcFwfsss.setWiid(bdcXm.getWiid());
                                bdcFwfsss.setProid(bdcXm.getProid());
                                bdcFwfsss.setYproid(yproid);
                                bdcFwFsssService.saveBdcFwfsss(bdcFwfsss);
                                msg = "继承成功";
                            }
                        }
                    }
                }
            }


        }
        return msg;
    }

    /**
     * @param
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @rerutn
     * @description 过渡继承房屋附属设施到抵押查封中
     */
    @ResponseBody
    @RequestMapping(value = "fsssInheritForGd")
    public String fsssInheritForGd(@RequestParam(value = "proid", required = false) String proid) {
        String msg = "继承失败";
        if (StringUtils.isNotBlank(proid)) {
            //jyl 主房项目
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    if (bdcXmList.size() > 1) {
                        for (BdcXm bdcXmTmp : bdcXmList) {
                            //判断当前项目是产权还是抵押查封的
                            if (StringUtils.isNotBlank(bdcXmTmp.getQllx()) && StringUtils.equals(bdcXmTmp.getQllx(), Constants.QLLX_DYAQ)) {
                                //jgz 一证多房情况下 自动带入附属设施
                                List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.inheritCqFwfsss(bdcXmTmp.getProid(), Constants.QLLX_DYAQ);
                                //jgz 附属设施单独发过房产证，创建关系带入项目
                                bdcXmRelService.creatBdcXmRelForFsssGdFwsyq(bdcXmTmp);
                                //抵押权继承，处理抵押面积
                                bdcDyaqService.dealInheritFsssForDyaq(bdcXmTmp.getProid(), bdcFwfsssList);
                                msg = "继承成功";
                            } else if (StringUtils.isNotBlank(bdcXmTmp.getQllx()) && StringUtils.equals(bdcXmTmp.getQllx(), Constants.QLLX_CFDJ)) {
                                //jgz 一证多房情况下 自动带入附属设施
                                bdcFwFsssService.inheritCqFwfsss(bdcXmTmp.getProid(), Constants.QLLX_CFDJ);
                                //jgz 附属设施单独发过房产证，创建关系带入项目
                                bdcXmRelService.creatBdcXmRelForFsssGdFwsyq(bdcXmTmp);
                                msg = "继承成功";
                            }
                        }
                    } else {
                        bdcFwFsssService.autoCreatBdcFsss(bdcXm.getProid());
                        BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                        if (bdcdy != null) {
                            bdcFwFsssService.intiBdcFwfsssForZhs(bdcdy.getBdcdyh(), bdcXm);
                        }
                        //针对抵押权sqlx=1019，将fsss的mj（bdcSpxx.mj）带入bdcDyaq.fwdymj
                        if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWDY_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWDY_XS_DM)) {
                            bdcFwFsssService.saveMjFromBdcSpxxToBdcDyaq(bdcXm.getProid());
                        }
                    }
                }
            }
        }
        return msg;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description
     */
    @ResponseBody
    @RequestMapping(value = "fsssNotInheritForGd")
    public String fsssNotInheritForGd(@RequestParam(value = "proid", required = false) String proid) {
        String msg = "fail";
        if (StringUtils.isNotBlank(proid)) {
            String fsssTbQjZhs = AppConfig.getProperty("fsss.same.qj.zhs");
            Double zmj = 0.0;
            if (StringUtils.equals(fsssTbQjZhs, "true")) {
                String zfbdcdyh = bdcdyService.getBdcdyhByProid(proid);
                if (StringUtils.isNotBlank(zfbdcdyh)) {
                    List<BdcFwfsss> bdcFwfsssList = bdcFwfsssMapper.initBdcFwfsssFromZhsWithZhsFwbm(zfbdcdyh);
                    if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                        for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                            if (bdcFwfsss.getJzmj() != null && bdcFwfsss.getJzmj() > 0) {
                                zmj += bdcFwfsss.getJzmj();
                            }
                        }
                    }
                }
            } else {
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                        HashMap<String, Object> queryMap = Maps.newHashMap();
                        queryMap.put("qlid", bdcXmRel.getYqlid());
                        List<GdFwQl> gdFwQlList = gdFwService.getGdFwQlByMap(queryMap);
                        if (CollectionUtils.isNotEmpty(gdFwQlList)) {
                            GdFwQl gdFwQl = gdFwQlList.get(0);
                            if (gdFwQl != null && StringUtils.isNotBlank(gdFwQl.getQlid())) {
                                List<GdFw> gdFwList = gdFwService.getGdFwByQlid(gdFwQl.getQlid());
                                if (CollectionUtils.isNotEmpty(gdFwList)) {
                                    for (GdFw gdFw : gdFwList) {
                                        if (StringUtils.isNotBlank(gdFw.getFwid()) && StringUtils.isNotBlank(gdFw.getIsfsss()) && StringUtils.equals(gdFw.getIsfsss(), "1")) {
                                            Boolean saveFsss = false;
                                            if (StringUtils.isNotBlank(gdFw.getDah())) {
                                                HashMap map = new HashMap();
                                                map.put("fwbm", gdFw.getDah());
                                                List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(map);
                                                if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                                                    saveFsss = true;
                                                }
                                            }
                                            if (saveFsss && gdFw.getJzmj() != null && gdFw.getJzmj() > 0) {
                                                zmj += gdFw.getJzmj();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

//            BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(proid);
//            if(bdcDyaq != null&&bdcDyaq.getFwdymj() != null&&bdcDyaq.getFwdymj() >0&&bdcDyaq.getFwdymj() >zmj&&zmj >0) {
//                bdcDyaq.setFwdymj(bdcDyaq.getFwdymj() - zmj);
//                bdcDyaqService.saveBdcDyaq(bdcDyaq);
//            }
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            if (bdcSpxx != null && bdcSpxx.getMj() != null && zmj > 0) {
                bdcSpxx.setMj(bdcSpxx.getMj() + zmj);
                entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
            }

        }
        return msg;
    }


    /**
     * @param
     * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @rerutn
     * @description 不继承房屋附属设施
     */
    @ResponseBody
    @RequestMapping(value = "initBdcFwfsssNotInherit")
    public String initBdcFwfsssNotInherit(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "yproid", required = false) String yproid) {
        String msg = "继承失败";
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(yproid)) {
            //jyl 主房项目
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(yproid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid()) && ybdcXm != null && bdcBdcdy != null) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXmTmp : bdcXmList) {
                        //判断当前项目是产权还是抵押查封的
                        if (StringUtils.isNotBlank(bdcXmTmp.getQllx()) && StringUtils.equals(bdcXmTmp.getQllx(), Constants.QLLX_DYAQ)) {
                            //抵押权不继承，处理抵押面积和抵押项目中的spxx里面的面积
                            bdcDyaqService.dealNotInheritFsssForDyaq(bdcXmTmp.getProid(), yproid);
                        } else if (StringUtils.isNotBlank(bdcXmTmp.getQllx()) && StringUtils.equals(bdcXmTmp.getQllx(), Constants.QLLX_CFDJ)) {
                            //查封不继承，处理查封项目中的spxx里面的面积
                            bdcCfService.dealNotInheritFsssForCf(bdcXmTmp.getProid(), yproid);
                        }
                    }
                }
            }
        }
        return msg;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 附属信息
     */
    @RequestMapping(value = "bdcFwfsssxx", method = RequestMethod.GET)
    public String bdcFwfsssxx(Model model, BdcFwfsss bdcFwfsss, String fwfsssid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        bdcFwfsss = bdcFwFsssService.getFwfsssByfwfssid(fwfsssid);
        model.addAttribute("bdcFwfsss", bdcFwfsss);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "/fsss/bdcFwfsssxx";
    }

    /**
     * @param
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 调查信息中展示附属信息列表信息
     */
    @RequestMapping(value = "bdcFwfsssList", method = RequestMethod.GET)
    public String bdcFwfsssList(Model model, String wiid, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, String proid) {
        model.addAttribute(ParamsConstants.WIID_LOWERCASE, wiid);
        model.addAttribute(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
        model.addAttribute(ParamsConstants.PROID_LOWERCASE, proid);
        return "/fsss/bdcFwfsssList";
    }

}
