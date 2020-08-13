package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
 * @version 1.0, 16-6-13
 * @description 关联土地证
 */
@Controller
@RequestMapping("/glZs")
public class GlZsController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GlZsService glZsService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;

    public static final String MESSAGE_GZSYCF = "该证书已经查封";
    public static final String MESSAGE_GZSYDY = "该证书已经抵押";


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, String wiid, String proid) {
        model.addAttribute("wiid", wiid);
        model.addAttribute("proid", proid);
        model.addAttribute("reportUrl", reportUrl);
        return "query/selectTdz";
    }

    @RequestMapping(value = "glFcz", method = RequestMethod.GET)
    public String glFcz(Model model, String wiid, String isFsss) {
        model.addAttribute("wiid", wiid);
        model.addAttribute("isFsss", isFsss);
        model.addAttribute("reportUrl", reportUrl);
        return "query/selectFcz";
    }

    /**
     * @param
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 查找土地证
     */
    @ResponseBody
    @RequestMapping(value = "/selectTdz")
    public Object getDanXXJsonace(Pageable pageable, String zl, String zh, String djh, String qlr, String exactQuery, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(ParamsConstants.ZL_LOWERCASE, StringUtils.deleteWhitespace(zl));
        map.put("djh", StringUtils.deleteWhitespace(djh));
        map.put("zh", StringUtils.deleteWhitespace(zh));
        map.put("qlr", StringUtils.deleteWhitespace(qlr));
        map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        return repository.selectPaging("getTdJsonByPage", map, pageable);
    }

    /**
     * @param
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @rerutn
     * @description 查找土地证
     */
    @ResponseBody
    @RequestMapping(value = "/selectFcz")
    public Object selectFcz(Pageable pageable, String zl, String bdcdyh, String bdcqzh, String qlr, String exactQuery, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(ParamsConstants.ZL_LOWERCASE, StringUtils.deleteWhitespace(zl));
        map.put(ParamsConstants.BDCDYH_LOWERCASE, StringUtils.deleteWhitespace(bdcdyh));
        map.put(ParamsConstants.BDCQZH_LOWERCASE, StringUtils.deleteWhitespace(bdcqzh));
        map.put("qlr", StringUtils.deleteWhitespace(qlr));
        map.put(ParamsConstants.EXACTQUERY_LOWERCASE, exactQuery);
        return repository.selectPaging("getFcJsonByPage", map, pageable);
    }

    /**
     * @param qlids 土地证的qlid
     * @param wiid  项目工作流Id
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 验证土地证
     */
    @RequestMapping(value = "/validateYtdz")
    @ResponseBody
    public HashMap validateYtdz(Model model, String qlids, String wiid, String proids, String xmlys, String proid) {
        List<HashMap> resultList = new ArrayList<HashMap>();
        HashMap map = null;
        try {
            if (StringUtils.isNotBlank(qlids)) {
                int i = 0;
                String[] qlidStr = qlids.split(",");
                String[] proidStr = proids.split(",");
                String[] xmlyStr = xmlys.split(",");
                for (String qlid : qlidStr) {
                    String xmly = xmlyStr[i];
                    String yproid = proidStr[i];
                    if (StringUtils.equals(xmly, Constants.XMLY_BDC)) {
                        BdcXm bdcxm = bdcXmService.getBdcXmByProid(yproid);
                        String bdcdyid = bdcxm.getBdcdyid();
                        if (StringUtils.isNotBlank(bdcdyid)) {
                            List<Map> cfList = qllxService.getCfxxByBdcdyId(bdcdyid);
                            List<BdcDyaq> dyList = qllxService.getDyxxByBdcdyId(bdcdyid);
                            if (CollectionUtils.isNotEmpty(cfList)) {
                                map = Maps.newHashMap();
                                map.put("msg", MESSAGE_GZSYCF);
                                map.put("type", Constants.CHECKMODEL_ALERT);
                                resultList.add(map);
                            } else if (CollectionUtils.isNotEmpty(dyList)) {
                                map = Maps.newHashMap();
                                map.put("msg", MESSAGE_GZSYDY);
                                map.put("type", Constants.CHECKMODEL_COMFIRM);
                                resultList.add(map);
                            }
                        }
                    } else {
                        List gdQlList = gdFwService.getGdQlListByQlid(qlid, null, Constants.BDCLX_TD);
                        String qlzt = gdFwService.getQlztByQlid(qlid, null, Constants.BDCLX_TD, gdQlList);
                        if (qlzt.indexOf(Constants.GD_QLZT_CF) > -1) {
                            map = Maps.newHashMap();
                            map.put("msg", MESSAGE_GZSYCF);
                            map.put("type", Constants.CHECKMODEL_ALERT);
                            resultList.add(map);
                        } else if (qlzt.indexOf(Constants.GD_QLZT_DY) > -1) {
                            map = Maps.newHashMap();
                            map.put("msg", MESSAGE_GZSYDY);
                            map.put("type", Constants.CHECKMODEL_COMFIRM);
                            resultList.add(map);
                        }
                    }
                    i++;
                }

            }
        } catch (Exception e) {
            logger.error("GlZsController.validateYtdz", e);
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            for (HashMap resultMap : resultList) {
                String type = CommonUtil.formatEmptyValue(resultMap.get("type"));
                if (StringUtils.equals(type, Constants.CHECKMODEL_ALERT)) {
                    return map;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            for (HashMap resultMap : resultList) {
                String type = CommonUtil.formatEmptyValue(resultMap.get("type"));
                if (StringUtils.equals(type, Constants.CHECKMODEL_COMFIRM)) {
                    return map;
                }
            }
        }

        map = Maps.newHashMap();
        map.put("msg", "");
        map.put("type", "");
        return map;
    }

    /**
     * @param qlids 土地证的qlid
     * @param wiid  项目工作流Id
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 关联土地证注销
     */
    @RequestMapping(value = "/glYtdz")
    @ResponseBody
    public HashMap glYtdz(Model model, @RequestParam(value = "qlids", required = false) String qlids, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proids", required = false) String proids, @RequestParam(value = "xmlys", required = false) String xmlys, @RequestParam(value = "proid", required = false) String proid) {
        HashMap map = new HashMap();
        String msg = "";
        try {
            if (StringUtils.isNotBlank(qlids)) {
                glZsService.gltdz(qlids, wiid, proid);
            }
            msg = "success";
        } catch (Exception e) {
            logger.error("GlZsController.glYtdz", e);
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * @param qlids 房产证的qlid
     * @param wiid  项目工作流Id
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 验证房产证
     */
    @RequestMapping(value = "/validateYfcz")
    @ResponseBody
    public HashMap validateYfcz(Model model, String qlids, String wiid, String proids, String xmlys, String isFsss, String proid) {
        List<HashMap> resultList = new ArrayList<HashMap>();
        HashMap map = null;
        BdcXm bdcXm = null;
        try {
            Boolean needCheckCf = true;
            if (StringUtils.isNotBlank(wiid)) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    bdcXm = bdcXmList.get(0);
                }
            } else if (StringUtils.isNotBlank(proid)) {
                bdcXm = bdcXmService.getBdcXmByProid(proid);
            }
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM) || CommonUtil.indexOfStrs(Constants.CF_SQLX, bdcXm.getSqlx()))) {
                needCheckCf = false;
            }

            if (StringUtils.isNotBlank(qlids)) {
                int i = 0;
                String[] qlidStr = qlids.split(",");
                String[] proidStr = proids.split(",");
                String[] xmlyStr = xmlys.split(",");
                for (String qlid : qlidStr) {
                    String xmly = xmlyStr[i];
                    String yproid = proidStr[i];
                    if (StringUtils.equals(xmly, Constants.XMLY_BDC)) {
                        BdcXm bdcxm = bdcXmService.getBdcXmByProid(yproid);
                        String bdcdyid = bdcxm.getBdcdyid();
                        if (StringUtils.isNotBlank(bdcdyid)) {
                            List<Map> cfList = qllxService.getCfxxByBdcdyId(bdcdyid);
                            List<BdcDyaq> dyList = qllxService.getDyxxByBdcdyId(bdcdyid);
                            if (CollectionUtils.isNotEmpty(cfList) && needCheckCf) {
                                map = Maps.newHashMap();
                                map.put("msg", MESSAGE_GZSYCF);
                                map.put("type", Constants.CHECKMODEL_ALERT);
                                resultList.add(map);
                            } else if (CollectionUtils.isNotEmpty(dyList)) {
                                map = Maps.newHashMap();
                                map.put("msg", MESSAGE_GZSYDY);
                                map.put("type", Constants.CHECKMODEL_COMFIRM);
                                resultList.add(map);
                            }
                        }
                    } else {
                        List gdQlList = gdFwService.getGdQlListByQlid(qlid, null, Constants.BDCLX_TDFW);
                        String qlzt = gdFwService.getQlztByQlid(qlid, null, Constants.BDCLX_TDFW, gdQlList);
                        if (qlzt.indexOf(Constants.GD_QLZT_CF) > -1 && needCheckCf) {
                            map = Maps.newHashMap();
                            map.put("msg", MESSAGE_GZSYCF);
                            map.put("type", Constants.CHECKMODEL_ALERT);
                            resultList.add(map);
                        } else if (qlzt.indexOf(Constants.GD_QLZT_DY) > -1) {
                            map = Maps.newHashMap();
                            map.put("msg", MESSAGE_GZSYDY);
                            map.put("type", Constants.CHECKMODEL_COMFIRM);
                            resultList.add(map);
                        }
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            logger.error("GlZsController.validateYfcz", e);
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            for (HashMap resultMap : resultList) {
                String type = CommonUtil.formatEmptyValue(resultMap.get("type"));
                if (StringUtils.equals(type, Constants.CHECKMODEL_ALERT)) {
                    return map;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            for (HashMap resultMap : resultList) {
                String type = CommonUtil.formatEmptyValue(resultMap.get("type"));
                if (StringUtils.equals(type, Constants.CHECKMODEL_COMFIRM)) {
                    return map;
                }
            }
        }

        map = Maps.newHashMap();
        map.put("msg", "");
        map.put("type", "");
        return map;
    }


    /**
     * @param qlids 房产证的qlid
     * @param wiid  项目工作流Id
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 关联房产证注销
     */
    @RequestMapping(value = "/glYfcz")
    @ResponseBody
    public HashMap glYfcz(Model model, @RequestParam(value = "qlids", required = false) String qlids, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proids", required = false) String proids, @RequestParam(value = "xmlys", required = false) String xmlys, @RequestParam(value = "isFsss", required = false) String isFsss, @RequestParam(value = "proid", required = false) String proid) {
        HashMap map = new HashMap();
        String msg = "";
        try {
            if (StringUtils.isNotBlank(qlids)) {
                int i = 0;
                String[] qlidStr = qlids.split(",");
                String[] proidStr = proids.split(",");
                String[] xmlyStr = xmlys.split(",");

                for (String qlid : qlidStr) {
                    String xmly = xmlyStr[i];
                    String yproid = proidStr[i];
                    List<BdcXmRel> newBdcXmRelList = new ArrayList<BdcXmRel>();
                    List<BdcXm> bdcXmList = new ArrayList<>();
                    if (StringUtils.isNotBlank(proid)) {
                        BdcXm bdcXmByProid = bdcXmService.getBdcXmByProid(proid);
                        bdcXmList.add(bdcXmByProid);
                    } else {
                        bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                    }

                    String sqlxdm = "";
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
                    if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                        sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                    }
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        for (BdcXm bdcXm : bdcXmList) {
                            //tanyue合并登记中的抵押登记不将关联的证加入到ybdcqzh中
                            if (StringUtils.indexOf(CommonUtil.formatEmptyValue(bdcXm.getYbdcqzh()) + ",", bdcZsService.getFczhByQlid(qlid) + ",") < 0 && !(CommonUtil.indexOfStrs(Constants.SQLX_HBDY, sqlxdm) && StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ))) {
                                if (StringUtils.isNotBlank(CommonUtil.formatEmptyValue(bdcXm.getYbdcqzh()))) {
                                    bdcXm.setYbdcqzh(CommonUtil.formatEmptyValue(bdcXm.getYbdcqzh()) + "," + bdcZsService.getFczhByQlid(qlid));
                                } else {
                                    bdcXm.setYbdcqzh(bdcZsService.getFczhByQlid(qlid));
                                }
                                entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                                List<BdcXmRel> bdcXmRels = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                                if (CollectionUtils.isNotEmpty(bdcXmRels)) {
                                    newBdcXmRelList.addAll(bdcXmRels);
                                    //允许插入
                                    boolean allowInsert = true;
                                    //允许更新
                                    boolean allowUpdate = false;

                                    for (BdcXmRel bdcXmRel : bdcXmRels) {
                                        if (StringUtils.isNotBlank(bdcXmRel.getYqlid()) && StringUtils.equals(bdcXmRel.getYqlid(), qlid)) {
                                            allowInsert = false;
                                        }
                                        if (StringUtils.isBlank(bdcXmRel.getYproid())) {
                                            allowUpdate = true;
                                        }
                                    }
                                    if (allowInsert) {
                                        for (BdcXmRel bdcXmRel : bdcXmRels) {
                                            bdcXmRel.setYproid(yproid);
                                            bdcXmRel.setYqlid(qlid);
                                            bdcXmRel.setYdjxmly(xmly);
                                            if (!allowUpdate) {
                                                bdcXmRel.setRelid(UUIDGenerator.generate());
                                            }
                                            newBdcXmRelList.add(bdcXmRel);
                                        }
                                    }
                                }
                                if (!dwdm.equals("320900")) {
                                    glZsService.saveZsQlr(bdcXm, xmly, yproid, qlid);
                                }
                                List<GdFwsyq> gdFwsyqList = gdFwService.getGdFwsyqListByGdproid(yproid, 0);
                                List<GdFw> gdFwList = gdFwService.getGdFwByQlid(qlid);
                                if (StringUtils.isNotBlank(isFsss) && Boolean.parseBoolean(isFsss)) {
                                    glZsService.addFsssxx2BdcFdcq(gdFwsyqList, gdFwList, bdcXm);
                                } else {
                                    glZsService.saveFwxxToBdcFdcq(gdFwsyqList, gdFwList, bdcXm);
                                }
                                if (CollectionUtils.isNotEmpty(gdFwList)) {
                                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                                    if (bdcSpxx != null) {
                                        for (GdFw gdFw : gdFwList) {
                                            if (CollectionUtils.isNotEmpty(bdcXmRels)) {
                                                BdcGdDyhRel gdDyhRel = new BdcGdDyhRel();
                                                gdDyhRel.setBdcdyh(bdcSpxx.getBdcdyh());
                                                gdDyhRel.setDjid(bdcXmRels.get(0).getQjid());
                                                gdDyhRel.setGdid(gdFw.getFwid());
                                                bdcGdDyhRelService.addGdDyhRel(gdDyhRel);
                                            }
                                        }
                                        GdQlDyhRel gdQlDyhRel = new GdQlDyhRel();
                                        gdQlDyhRel.setBdcdyh(bdcSpxx.getBdcdyh());
                                        gdQlDyhRel.setDjid(bdcXmRels.get(0).getQjid());
                                        gdQlDyhRel.setQlid(qlid);
                                        gdQlDyhRel.setBdclx(Constants.BDCLX_TDFW);
                                        bdcGdDyhRelService.addGdQlDyhRel(gdQlDyhRel);
                                    }
                                }
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(newBdcXmRelList)) {
                        //过滤重复数据并保存
                        List<String> bdcXmRelKeyList = new ArrayList<String>();
                        for (BdcXmRel bdcXmRel : newBdcXmRelList) {
                            if (!bdcXmRelKeyList.contains(bdcXmRel.getProid() + bdcXmRel.getQjid() + bdcXmRel.getYproid())) {
                                bdcXmRelKeyList.add(bdcXmRel.getProid() + bdcXmRel.getQjid() + bdcXmRel.getYproid());
                                entityMapper.saveOrUpdate(bdcXmRel, bdcXmRel.getRelid());
                            }

                        }
                    }
                    i++;
                }
                msg = "success";
            }
        } catch (Exception e) {
            logger.error("GlZsController.glYfcz", e);
        }
        map.put("msg", msg);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "deleteGlzs")
    public String deleteGlzs(String wiid, String proid) {
        String msg = "删除失败";
        List<BdcXm> bdcXmList = new ArrayList<>();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXmByProid = bdcXmService.getBdcXmByProid(proid);
            bdcXmList.add(bdcXmByProid);
        } else {
            bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                bdcXm.setYbdcqzh("");
                entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                            if (bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getBdcdyh())) {
                                String bdclx = "";
                                if (StringUtils.indexOf(bdcSpxx.getBdcdyh(), "W") > -1) {
                                    bdclx = Constants.BDCLX_TD;
                                } else {
                                    bdclx = Constants.BDCLX_TDFW;
                                }
                                bdcGdDyhRelService.deleteGdPpgx(bdcXmRel.getYqlid(), bdclx);
                            }
                        }
                        bdcXmRel.setYproid("");
                        bdcXmRel.setYqlid("");
                        bdcXmRel.setYdjxmly("");
                        entityMapper.saveOrUpdate(bdcXmRel, bdcXmRel.getRelid());
                    }
                }
            }
            msg = "删除成功";
        }
        return msg;
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 获取房屋不动产单元、产权证和坐落信息
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcXxByProidAndXmly")
    public Map getBdcXxByProidAndXmly(String proid, String xmly) {
        Map map = Maps.newHashMap();
        if (StringUtils.isNoneBlank(proid) && StringUtils.isNotBlank(xmly)) {
            if (StringUtils.equals(Constants.XMLY_BDC, xmly)) {
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                if (bdcSpxx != null) {
                    map.put(ParamsConstants.ZL_LOWERCASE, CommonUtil.formatEmptyValue(bdcSpxx.getZl()));
                    map.put(ParamsConstants.BDCDYH_LOWERCASE, CommonUtil.formatEmptyValue(bdcSpxx.getBdcdyh()));
                }
                map.put(ParamsConstants.BDCQZH_LOWERCASE, bdcZsService.getCombineBdcqzhByProid(proid));
            } else {
                HashMap queryMap = Maps.newHashMap();
                queryMap.put(ParamsConstants.PROID_LOWERCASE, proid);
                List<GdFwQl> gdFwQlList = gdFwService.getGdFwQlByMap(queryMap);
                if (CollectionUtils.isNotEmpty(gdFwQlList)) {
                    GdFwQl gdFwQl = gdFwQlList.get(0);
                    map.put(ParamsConstants.ZL_LOWERCASE, CommonUtil.formatEmptyValue(gdFwQl.getFwzl()));
                    map.put(ParamsConstants.BDCQZH_LOWERCASE, CommonUtil.formatEmptyValue(gdFwQl.getFczh()));
                    List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(null, CommonUtil.formatEmptyValue(gdFwQl.getQlid()), null);
                    if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                        GdQlDyhRel gdQlDyhRel = gdQlDyhRelList.get(0);
                        map.put(ParamsConstants.BDCDYH_LOWERCASE, CommonUtil.formatEmptyValue(gdQlDyhRel.getBdcdyh()));
                    }
                }
            }
        }
        return map;
    }

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 获取土地地籍号、土地证号和坐落信息
     */
    @ResponseBody
    @RequestMapping(value = "/getBdcTdXxByProidAndXmly")
    public Map getBdcTdXxByProidAndXmly(String proid, String xmly) {
        Map map = Maps.newHashMap();
        if (StringUtils.isNoneBlank(proid) && StringUtils.isNotBlank(xmly)) {
            if (StringUtils.equals(Constants.XMLY_BDC, xmly)) {
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
                if (null != bdcSpxx) {
                    map.put(ParamsConstants.ZL_LOWERCASE, CommonUtil.formatEmptyValue(bdcSpxx.getZl()));
                    String bdcdyh = CommonUtil.formatEmptyValue(bdcSpxx.getBdcdyh());
                    if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() >= 19) {
                        map.put("djh", StringUtils.substring(bdcdyh, 0, 19));
                    }
                }
                map.put("zh", bdcZsService.getCombineBdcqzhByProid(proid));
            } else {
                HashMap queryMap = Maps.newHashMap();
                queryMap.put(ParamsConstants.PROID_LOWERCASE, proid);
                List<HashMap> gdTdQlMapList = gdTdService.getGdTdQl(queryMap);
                if (CollectionUtils.isNotEmpty(gdTdQlMapList)) {
                    HashMap gdTdQlMap = gdTdQlMapList.get(0);
                    map.put("zh", CommonUtil.formatEmptyValue(gdTdQlMap.get("TDZH")));
                    map.put(ParamsConstants.ZL_LOWERCASE, CommonUtil.formatEmptyValue(gdTdQlMap.get("TDZL")));
                    map.put("djh", CommonUtil.formatEmptyValue(gdTdQlMap.get("DJH")));
                }
            }
        }
        return map;
    }

}
