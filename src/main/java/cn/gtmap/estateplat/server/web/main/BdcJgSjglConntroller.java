package cn.gtmap.estateplat.server.web.main;


import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.examine.BdcExamineParam;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdFwMapper;
import cn.gtmap.estateplat.server.core.model.CorrelationData;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.server.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.service.config.RedundantFieldService;
import cn.gtmap.estateplat.service.examine.BdcExamineService;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.vo.*;
import com.gtis.spring.Container;
import com.gtis.web.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.*;

/**
 * .
 * <p/>
 * 结果数据管理
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version V1.0, 15-3-10
 */
@Controller
@RequestMapping("/bdcJgSjgl")
public class BdcJgSjglConntroller extends BaseController {
    @Autowired
    private Repo repository;
    @Resource(name = "delProjectDefaultServiceImpl")
    private DelProjectService delProjectDefaultServiceImpl;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdqlService gdqlService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private ProjectCheckInfoService projectCheckInfoService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private GdSjglService dSjglService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private BdcCheckMatchDataService bdcCheckMatchDataService;
    @Autowired
    private BdcFwFsssService bdcFwFsssService;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private ExamineCheckInfoService examineCheckInfoService;
    @Autowired
    private BdcExamineService bdcExamineService;
    @Autowired
    private GdFwMapper gdFwMapper;
    @Autowired
    GdQlDyhRelService gdQlDyhRelService;
    @Autowired
    private BdcPpdService bdcPpdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcTzmDjsjRelService bdcTzmDjsjRelService;
    @Autowired
    private BdcDataPicModel bdcDataPicModel;
    @Autowired
    private  SelectBdcdyManageService selectBdcdyManageService;
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;
    @Autowired
    private RedundantFieldService redundantFieldService;
    @Autowired
    private QllxService qllxService;


    private static final String HHSEARCH = "hhSearch";
    private static final String PARAMETER_SYS_VERSION= "sys.version";
    private static final String PARAMETER_VIEWBYDWDM= "viewByDwdm";
    private static final String PARAMETER_SJPP_MULFCZTDZPP= "sjpp.mulFczTdzPp";
    private static final String PARAMETER_FW_FILTERFSSS_GHYT= "fw.filterFsss.ghyt";
    private static final String PARAMETER_ISEXCFWLX= "isExcfwlx";

    /**
     * 获取房屋信息分页数据
     *
     * @param pageable
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdFwJson")
    public Object getGdFwJson(Pageable pageable, String qlrzjh, GdFwQl gdFwQl, String hhSearch, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdQlids", required = false) String gdQlids, @RequestParam(value = "checkMulGdFw", required = false) String checkMulGdFw,
                              @RequestParam(value = "sfsh", required = false) String sfsh,String qlr, String zl, String fczh,String fuzzyQuery) {
        long startTime = System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(hhSearch)) {
            map.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }else{
            if (StringUtils.isNotBlank(qlr)){
                map.put("qlrSearch",StringUtils.deleteWhitespace(qlr));
            }
            if (StringUtils.isNotBlank(fczh)){
                map.put("fczhSearch",StringUtils.deleteWhitespace(fczh));
            }
            if (StringUtils.isNotBlank(zl)){
                map.put("zlSearch",StringUtils.deleteWhitespace(zl));
            }
            if (StringUtils.isNotBlank(fuzzyQuery)){
                map.put("fuzzyQuery",StringUtils.deleteWhitespace(fuzzyQuery));
            }
        }
        if(StringUtils.equals(checkMulGdFw, "true")) {
            if(StringUtils.isNotBlank(gdQlids)) {
                map.put(ParamsConstants.QLIDS_LOWERCASE, gdQlids.split(","));
            } else {
                map.put(ParamsConstants.QLIDS_LOWERCASE, "1");
            }
        }
          Page<HashMap> dataPaging = repository.selectPaging("getGdFwJgJsonByPage", map, pageable);
        logger.error("fw代码程序运行时间：" + (System.currentTimeMillis() - startTime) + "ms");
          return dataPaging;
    }


    /**
     * 跳转到不动产单元匹配页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "toDataPic")
    public String toDataPicCp(Model model, @RequestParam(value = "matchTdzh", required = false) String matchTdzh,
                              @RequestParam(value = "editFlag", required = false) String editFlag,
                              @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt,
                              @RequestParam(value = "showZjfzBtn", required = false) String showZjfzBtn,
                              @RequestParam(value = "msgInfo", required = false) String msgInfo) {
        bdcDataPicModel.initBdcDataPicModel(model);
        if(StringUtils.isBlank(matchTdzh)) {
            matchTdzh = "";
        }
        if(StringUtils.isBlank(editFlag)) {
            editFlag = "";
        }
        if(StringUtils.isBlank(filterFwPpzt)) {
            filterFwPpzt = "";
        }
        if(StringUtils.isBlank(showZjfzBtn)) {
            showZjfzBtn = "";
        }
        //是否允许多本房产证匹配多本土地证
        String mulFczTdzPp = AppConfig.getProperty(PARAMETER_SJPP_MULFCZTDZPP);
        //是否展示房产数据修改按钮
        String showFcsjUpdate = "false";
        //是否展示土地数据修改按钮
        String showTdsjUpdate = "false";
        String fcsjUpdateYh = AppConfig.getProperty("fcsj.update.jsm");
        String tdsjUpdateYh = AppConfig.getProperty("tdsj.update.jsm");
        String userName = SessionUtil.getCurrentUser().getUsername();
        if (StringUtils.isNotBlank(fcsjUpdateYh) && StringUtils.indexOf(fcsjUpdateYh,userName) > -1) {
            showFcsjUpdate = "true";
        }
        if (StringUtils.isNotBlank(tdsjUpdateYh) && StringUtils.indexOf(tdsjUpdateYh,userName) > -1) {
            showTdsjUpdate = "true";
        }
        model.addAttribute("showFcsjUpdate", showFcsjUpdate);
        model.addAttribute("showTdsjUpdate", showTdsjUpdate);
        model.addAttribute("mulFczTdzPp", mulFczTdzPp);
        model.addAttribute("matchTdzh", matchTdzh);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("filterFwPpzt", filterFwPpzt);
        model.addAttribute("showZjfzBtn", showZjfzBtn);
        model.addAttribute("msgInfo", msgInfo);
        model.addAttribute("dwdm", dwdm);
        model.addAttribute("showOptimize",StringUtils.isNotBlank(AppConfig.getProperty("selectFczhAndTdzh.showOptimization")) ? AppConfig.getProperty("selectFczhAndTdzh.showOptimization") : "false");
        String isShowPphzd = AppConfig.getProperty("is_show_pphzd");
        if(StringUtils.isBlank(isShowPphzd)) {
            isShowPphzd = ParamsConstants.FALSE_LOWERCASE;
        }
        model.addAttribute("isShowPphzd", isShowPphzd);
        if(dwdm.equals("320500") || dwdm.equals("320600") || dwdm.equals("320683") || dwdm.equals("320900")) {
            return "/sjgl/" + dwdm + "/jgDataPic";
        } else {
            return "/sjgl/default/jgDataPic";
        }
    }

    /**
     * 选择不动产单元页面跳转到不动产单元匹配页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "toDataPicForSelect")
    public String toDataPicForSelect(Model model, @RequestParam(value = "matchTdzh", required = false) String matchTdzh,
                              @RequestParam(value = "editFlag", required = false) String editFlag,
                              @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt,
                              @RequestParam(value = "showZjfzBtn", required = false) String showZjfzBtn,
                              @RequestParam(value = "msgInfo", required = false) String msgInfo,String gdzh,String gdly) {
        bdcDataPicModel.initBdcDataPicModel(model);
        if(StringUtils.isBlank(matchTdzh)) {
            matchTdzh = "";
        }
        if(StringUtils.isBlank(editFlag)) {
            editFlag = "";
        }
        if(StringUtils.isBlank(filterFwPpzt)) {
            filterFwPpzt = "";
        }
        if(StringUtils.isBlank(showZjfzBtn)) {
            showZjfzBtn = "";
        }
        model.addAttribute("matchTdzh", matchTdzh);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("filterFwPpzt", filterFwPpzt);
        model.addAttribute("showZjfzBtn", showZjfzBtn);
        model.addAttribute("msgInfo", msgInfo);
        model.addAttribute("dwdm", dwdm);
        model.addAttribute("gdzh", gdzh);
        model.addAttribute("gdly", gdly);
        model.addAttribute("showOptimize",StringUtils.isNotBlank(AppConfig.getProperty("selectFczhAndTdzh.showOptimization")) ? AppConfig.getProperty("selectFczhAndTdzh.showOptimization") : "false");
        String isShowPphzd = AppConfig.getProperty("is_show_pphzd");
        if(StringUtils.isBlank(isShowPphzd)) {
            isShowPphzd = ParamsConstants.FALSE_LOWERCASE;
        }
        model.addAttribute("isShowPphzd", isShowPphzd);
        if(dwdm.equals("320500") || dwdm.equals("320600") || dwdm.equals("320683") || dwdm.equals("320900")) {
            return "/sjgl/" + dwdm + "/jgDataPicForSelect";
        } else {
            return "/sjgl/default/jgDataPic";
        }
    }


    /**
     * zx 获取房屋信息
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "asyncGetGdFwxxByQlid")
    public HashMap asyncGetGdFwxxByQlid(Model model, String qlid, String bdclx, String bdcid) {
        HashMap resultMap = new HashMap();
        if(StringUtils.isNotBlank(qlid)) {
            //获取过渡权利
            List gdQlList = gdFwService.getGdQlListByQlid(qlid, null, bdclx);
            String ppzt = gdFwService.getPpztByQlid(qlid, Constants.BDCLX_TDFW, bdcid);
            resultMap.put("ppzt", ppzt);
            //过渡项目的权利状态
            String qlzt = gdFwService.getQlztByQlid(qlid, null, bdclx, gdQlList);
            resultMap.put(ParamsConstants.QLZTS_LOWERCASE, qlzt);
        }
        return resultMap;
    }

    /**
     * 不动产单元号分页数据
     *
     * @param pageable
     * @param pageable
     * @param qlr
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcDyhPagesJson")
    public Object getBdcDyhPagesJson(Pageable pageable, String tdids, String hhSearch, String dah, String qlr, String djh,
                                     @RequestParam(value = "bdcdyh", required = false) String bdcdyh, String zl, String bdclx, @RequestParam(value = "bdcdyhs", required = false) String bdcdyhs, HttpServletRequest request) {
        HashMap map = new HashMap();
        if(StringUtils.isNotBlank(tdids)) {
            HashMap bdcdymap = new HashMap();
            bdcdymap.put("tdids", tdids.split(","));
            String bdcdyhsForTd = gdTdService.getBdcdyhByTdids(bdcdymap);
            if(StringUtils.isNotBlank(bdcdyhsForTd))
                map.put("bdcdyhsForTd", bdcdyhsForTd.split(","));
        }

        map.put("qlr", qlr);
        map.put("djh", djh);
        map.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
        map.put("zl", zl);
        map.put("dah", dah);
        map.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        map.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));

        if(StringUtils.isNotBlank(bdcdyhs) && bdcdyhs.split(",").length > 0)
            map.put("bdcdyhs", bdcdyhs.split(","));
        if(StringUtils.isNotBlank(hhSearch)) {
            String[] djids = bdcdyService.getDjQlrIdsByQlr(StringUtils.deleteWhitespace(hhSearch), bdclx);
            if(djids != null && djids.length > 0)
                map.put("djids", djids);
        }
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if(StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if(StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }

        String viewByDwdm = AppConfig.getProperty(PARAMETER_VIEWBYDWDM);
        if(StringUtils.equals(viewByDwdm, "true")) {
            map.put(PARAMETER_VIEWBYDWDM, viewByDwdm);
        }
        return repository.selectPaging("getDjBdcdyListByPage", map,pageable);
    }

    /**
     * 获取权利人
     *
     * @param model
     * @param djid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQlrByDjid")
    public HashMap<String, Object> getQlrByDjid(Model model, String djid, String bdclxdm) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String qlr = "";
        if(StringUtils.isNotBlank(djid))
            qlr = bdcdyService.getDjQlrByDjid(djid, bdclxdm, null);
        resultMap.put("qlr", qlr);
        return resultMap;
    }

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据地籍号获取老地籍号
     */
    @ResponseBody
    @RequestMapping("/getOldDjhByDjh")
    public HashMap<String, Object> getOldDjhByDjh(String djh) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String oldDjh = "";
        if(StringUtils.isNotBlank(djh)) {
            List<String> ydjhList = bdcDjsjService.getOldDjh(djh);
            if(CollectionUtils.isNotEmpty(ydjhList)) {
                oldDjh = ydjhList.get(0);
            }
        }
        resultMap.put("oldDjh", oldDjh);
        return resultMap;
    }

    /**
     * 土地/林权/不动产单元匹配页面
     * 根据登记类型和不动产类型获取申请类型
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getSqlxByDjlx")
    public Object getSqlxByBdclxDjlx(Model model, String djlx, String bdclx) {
        String djlxdm = bdcZdGlService.getDjlxDmByMc(djlx);
        return bdcZdGlService.getSqlxByBdclxDjlx(bdclx, djlxdm);
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据构建平台流程一级类和不动产类型获取申请类型
     */
    @ResponseBody
    @RequestMapping(value = "getSqlxByBusinessId")
    public List<Map> getSqlxByBusinessId(String businessId) {
        List<Map> pfWorkFlowDefineVoMap = new ArrayList<Map>();
        if(StringUtils.isEmpty(businessId)) {
            List<PfBusinessVo> pfBusinessVoList = sysWorkFlowDefineService.getBusinessList();
            if(CollectionUtils.isNotEmpty(pfBusinessVoList))
                businessId = pfBusinessVoList.get(0).getBusinessId();
        }
        List<PfWorkFlowDefineVo> pfWorkFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineByBusiness(businessId);
        if(CollectionUtils.isNotEmpty(pfWorkFlowDefineVoList)) {
            for(int i = 0; i < pfWorkFlowDefineVoList.size(); i++) {
                HashMap map = new HashMap();
                map.put("wfId", pfWorkFlowDefineVoList.get(i).getWorkflowDefinitionId());
                map.put("wfName", pfWorkFlowDefineVoList.get(i).getWorkflowName());
                pfWorkFlowDefineVoMap.add(map);
            }
        }
        return pfWorkFlowDefineVoMap;
    }

    @ResponseBody
    @RequestMapping(value = "getDjzxByWfid")
    public List<HashMap> getDjzx(String wfid) {
        List<HashMap> djzx = new ArrayList<HashMap>();
        if(StringUtils.isNoneBlank(wfid)) {
            HashMap map = new HashMap();
            map.put("wdid", wfid);
            djzx = bdcZdGlService.getDjzx(map);
        }
        return djzx;
    }

    /**
     * 匹配不动产单元和房产证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryBdcdyhByQlid")
    public List queryBdcdyhByQlid(Model model, String proid, String bdclx, String qlid) {
        List list = new ArrayList();
        List<GdFw> gdFwList = null;
        if((CollectionUtils.isEmpty(list)) && StringUtils.isNotBlank(qlid)) {
            gdFwList = gdFwService.getGdFwByQlid(qlid);
            //房屋按规划用途过滤附属设施（车库，阁楼等）
            String fwFilterFsssGhyt = AppConfig.getProperty(PARAMETER_FW_FILTERFSSS_GHYT);
            //房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
            if(StringUtils.equals(fwFilterFsssGhyt, "true") && gdFwList.size() > 1) {
                HashMap queryGdFwMap = new HashMap();
                queryGdFwMap.put(PARAMETER_ISEXCFWLX, "true");
                queryGdFwMap.put("qlid", qlid);
                gdFwList = gdFwService.getGdFw(queryGdFwMap);
            }
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                for(GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", gdFw.getFwid());
                    if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                        list.addAll(gdDyhRelList);
                    }
                }
            }
        }

        if(CollectionUtils.isEmpty(list) && StringUtils.isNotBlank(qlid) && CollectionUtils.isNotEmpty(gdFwList)) {
            gdFwList = gdFwService.getGdFwByQlid(qlid);
            //房屋按规划用途过滤附属设施（车库，阁楼等）
            String fwFilterFsssGhyt = AppConfig.getProperty(PARAMETER_FW_FILTERFSSS_GHYT);
            //房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
            if(StringUtils.equals(fwFilterFsssGhyt, "true") && gdFwList.size() > 1) {
                HashMap queryGdFwMap = new HashMap();
                queryGdFwMap.put(PARAMETER_ISEXCFWLX, "true");
                queryGdFwMap.put("qlid", qlid);
                gdFwList = gdFwService.getGdFw(queryGdFwMap);
            }
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                StringBuilder gdFwDahs = new StringBuilder();
                for(GdFw gdFw : gdFwList) {
                    if(StringUtils.isNotBlank(gdFw.getDah()) && !gdFwDahs.toString().contains(gdFw.getDah())) {
                        if(StringUtils.isBlank(gdFwDahs)) {
                            gdFwDahs.append(gdFw.getDah());
                        }else {
                            gdFwDahs.append(",").append(gdFw.getDah());
                        }
                    }
                }

                if(StringUtils.isNotBlank(gdFwDahs)) {
                    HashMap parmMap = new HashMap();
                    parmMap.put("fcdahs", gdFwDahs.toString().split(","));
                    list = bdcdyService.queryBdcdyhByDah(parmMap);
                }

            }
        }
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 连云港这边把拟似不动产单元号放进备注里根据根据这个字段查询不动产单元
         */
        if(CollectionUtils.isEmpty(list)) {
            gdFwList = gdFwService.getGdFwByQlid(qlid);
            if(CollectionUtils.isNotEmpty(gdFwList)&&StringUtils.isNotBlank(gdFwList.get(0).getBz()) && gdFwList.get(0).getBz().length() >= 19) {
                List<String> bdcdyhs = new ArrayList<String>();
                bdcdyhs.add(gdFwList.get(0).getBz());
                list = bdcdyhs;
            }
        }
        //如果没值上面的entityMapper会返回null
        if(list == null) {
            list = new ArrayList();
        }
        return list;
    }

    /**
     * 获取过渡房产登记对应的不动产申请类型
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getGdFcDjlxToSqlxWdid")
    public HashMap getGdFcDjlxToSqlxWdid(Model model, @RequestParam(value = "djlx", required = false) String djlx) {
        HashMap map = new HashMap();
        String wdid = "";
        String busiName = "";
        if(StringUtils.isNotBlank(djlx)) {
            wdid = bdcZdGlService.getGdFcDjlxToSqlxWdid(djlx);
            if(StringUtils.isNotBlank(wdid)) {
                busiName = PlatformUtil.getBusinessNameByWfid(wdid);
            }
        }
        map.put("wdid", wdid);
        map.put("busiName", busiName);
        return map;
    }

    /**
     * 获取土地分页数据
     *
     * @param sidx
     * @param gdTd
     * @param sord
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdTdJson")
    public Object getGdTdJson(Pageable pageable, @RequestParam(value = "sidx", required = false) String sidx,
                              @RequestParam(value = "sord", required = false) String sord, @RequestParam(value = "iszx", required = false) Integer iszx,
                              @RequestParam(value = "hhSearch", required = false) String hhSearch, @RequestParam(value = "rf1Dwmc", required = false) String rf1Dwmc,
                              @RequestParam(value = "rf1zjh", required = false) String rf1zjh, @RequestParam(value = "tdzh", required = false) String tdzh,
                              @RequestParam(value = "jqtdzh", required = false) String jqtdzh, @RequestParam(value = "jqdjh", required = false) String jqdjh,
                              @RequestParam(value = "jqzl", required = false) String jqzl, @RequestParam(value = "gdTd", required = false) GdTd gdTd,
                              @RequestParam(value = "tdid", required = false) String tdid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh,
                              HttpServletRequest request, @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt,
                              @RequestParam(value = "tdids", required = false) String tdids, @RequestParam(value = "fwtdz", required = false) String fwtdz,
                              @RequestParam(value = "sfsh", required = false) String sfsh, @RequestParam(value = "qlids", required = false) String qlids,
                              String djhSearch, String tdZlSearch, String tdzhSearch, String fuzzyQueryTd) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(hhSearch)){
            try {
                hhSearch = URLDecoder.decode(hhSearch,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("BdcJgSjglConntroller.getGdTdJson",e);
            }
            map.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }
        if(StringUtils.isBlank(hhSearch)) {
            if(StringUtils.isNotBlank(jqtdzh))
                map.put("jqtdzh", StringUtils.deleteWhitespace(jqtdzh));
            if(StringUtils.isNotBlank(jqdjh))
                map.put("jqdjh", StringUtils.deleteWhitespace(jqdjh));
            if(StringUtils.isNotBlank(jqzl))
                map.put("jqzl", StringUtils.deleteWhitespace(jqzl));
        }

        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        String needSh = AppConfig.getProperty("needSh");
        if(StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if(StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        if(StringUtils.isNotBlank(needSh) && StringUtils.equals("true", needSh)
                &&StringUtils.isNotBlank(sfsh)) {
            map.put("sfsh", sfsh);
        }

        String viewByDwdm = AppConfig.getProperty(PARAMETER_VIEWBYDWDM);
        if(StringUtils.equals(viewByDwdm, "true")) {
            map.put(PARAMETER_VIEWBYDWDM, viewByDwdm);
        }
        if (StringUtils.isNotBlank(djhSearch)){
            map.put("djhSearch",StringUtils.deleteWhitespace(djhSearch));
        }
        if (StringUtils.isNotBlank(tdZlSearch)){
            map.put("tdZlSearch",StringUtils.deleteWhitespace(tdZlSearch));
        }
        if (StringUtils.isNotBlank(tdzhSearch)){
            map.put("tdzhSearch",StringUtils.deleteWhitespace(tdzhSearch));
        }
        if (StringUtils.isNotBlank(fuzzyQueryTd)){
            map.put("fuzzyQueryTd",StringUtils.deleteWhitespace(fuzzyQueryTd));
        }
        map.put("iszx", iszx);
        map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TD);
        if(StringUtils.isNotBlank(qlids)) {
            map.put(ParamsConstants.QLIDS_LOWERCASE, qlids.split(","));
        }
        return repository.selectPaging("getGdTdJsonByPage", map, pageable);
    }


    /**
     * 匹配不动产单元和房产项目
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkGdfwNum")
    public HashMap checkGdfwNum(Model model, String gdproid, String isExcfwlx, String qlid) {
        List<GdFw> gdFwList = null;
        HashMap checkMsgMap = new HashMap();
        //是否存在一个项目多个房屋
        String mulGdfw = ParamsConstants.FALSE_LOWERCASE;
        StringBuilder stringBuilder = new StringBuilder();
        //是否已经匹配
        String hasPic = "true";
        if(StringUtils.isNotBlank(gdproid)) {
            //获取过渡房屋列表
            gdFwList = gdFwService.getGdFwByQlid(qlid);
            //房屋按规划用途过滤附属设施（车库，阁楼等）
            String fwFilterFsssGhyt = AppConfig.getProperty(PARAMETER_FW_FILTERFSSS_GHYT);
            //房屋查询判断是否是多个房屋时 排除车库等gd_fw_exclx表中的数据
            if(StringUtils.equals(fwFilterFsssGhyt, "true") && gdFwList.size() > 1) {
                HashMap queryGdFwMap = new HashMap();
                queryGdFwMap.put(PARAMETER_ISEXCFWLX, "true");
                queryGdFwMap.put("qlid", qlid);
                gdFwList = gdFwService.getGdFw(queryGdFwMap);
            }
            //判断是否有多个房屋
            if(gdFwList != null && gdFwList.size() > 1) {
                mulGdfw = "true";
            }
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                for(GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", gdFw.getFwid());
                    if(CollectionUtils.isEmpty(gdDyhRelList)) {
                        hasPic = ParamsConstants.FALSE_LOWERCASE;
                        break;
                    }
                }
                List<String> fwidList = Lists.newArrayList();
                for(GdFw gdFw : gdFwList) {
                    fwidList.add(gdFw.getFwid());
                }
                if(!fwidList.isEmpty())
                    stringBuilder.append(StringUtils.join(fwidList, ","));
            }
        }
        checkMsgMap.put("mulGdfw", mulGdfw);
        checkMsgMap.put("hasPic", hasPic);
        checkMsgMap.put("fwid", stringBuilder);
        return checkMsgMap;
    }

    /**
     * 根据不动产单元类型获取登记类型
     *
     * @param bdclx
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDjlxByBdclx")
    public Object getDjlxByBdclx(Model model, String bdclx) {
        return bdcZdGlService.getDjlxByBdclx(bdclx);
    }


    /**
     * zx 获取土地信息
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "asyncGetGdTdxxByQlid")
    public HashMap asyncGetGdTdxxByQlid(Model model, String qlid, String bdclx, String proid) {
        HashMap resultMap = new HashMap();
        if(StringUtils.isNotBlank(qlid)) {
            //获取过渡权利
            List gdQlList = gdFwService.getGdQlListByQlid(qlid, null, bdclx);
            //过渡项目的权利状态
            String qlzt = gdFwService.getQlztByQlid(qlid, null, bdclx, gdQlList);
            resultMap.put(ParamsConstants.QLZTS_LOWERCASE, qlzt);
            String ppzt = gdFwService.getPpztByQlid(qlid, Constants.BDCLX_TD, proid);
            resultMap.put("ppzt", ppzt);
        }
        return resultMap;
    }

    /**
     * 根据过渡房屋项目id匹配房产证和土地证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryTdByQlid")
    public List queryTdByQlid(Model model, @RequestParam(value = "qlid", required = false) String qlid) {
        List list = new ArrayList<BdcGdDyhRel>();
        if(StringUtils.isNotBlank(qlid)) {
            List<GdFw> gdFwList = gdFwService.getGdFwByQlid(qlid);
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                list = new ArrayList<BdcGdDyhRel>();
                for(GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", gdFw.getFwid());
                    List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel("", qlid, "");
                    if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                        list.addAll(gdQlDyhRelList);
                    } else if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                        list.addAll(gdDyhRelList);
                    }
                }
            }
        }

        if(CollectionUtils.isEmpty(list)&&StringUtils.isNotBlank(qlid)) {
            List<GdTdsyq> gdTdsyqList = gdFwService.queryTdsyqByQlid(qlid);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)) {
                for(GdTdsyq gdTdsyq : gdTdsyqList) {
                    List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(gdTdsyq.getQlid());
                    if(CollectionUtils.isNotEmpty(gdTdList)) {
                        for(GdTd gdTd : gdTdList) {
                            BdcGdDyhRel bdcGdDyhRel = new BdcGdDyhRel();
                            bdcGdDyhRel.setTdid(gdTd.getTdid());
                            list.add(bdcGdDyhRel);
                        }
                    }
                }
            }
        }
        return list;
    }


    /**
     * 匹配不动产单元和房产证
     *
     * @param model
     * @return
     */
    @Transactional
    @ResponseBody
    @RequestMapping(value = "matchData")
    public HashMap matchData(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh,
                             @RequestParam(value = "djId", required = false) String djId,
                             @RequestParam(value = "gdid", required = false) String gdid,
                             @RequestParam(value = "tdid", required = false) String tdid,
                             @RequestParam(value = "gdproid", required = false) String gdproid,
                             @RequestParam(value = "ppzt", required = false) String ppzt,
                             @RequestParam(value = "bdclx", required = false) String bdclx,
                             @RequestParam(value = "tdqlid", required = false) String tdqlid,
                             @RequestParam(value = "qlid", required = false) String qlid,
                             @RequestParam(value = "fwids", required = false) String fwids
    ) {
        String msg = "匹配成功";
        HashMap result = new HashMap();
        List<String> fwidList = new ArrayList<String>();
        //是否允许多本房产证匹配多本土地证
        String mulFczTdzPp = AppConfig.getProperty(PARAMETER_SJPP_MULFCZTDZPP);
        try {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 如果没有匹配不动产单元页面传过来的是undefined ，则不需要简历关系表
             */
            if(StringUtils.isNotBlank(bdcdyh)&&!StringUtils.equals(bdcdyh, "undefined")) {
                if(StringUtils.isNotBlank(fwids)) {
                    //jyl一证多房的批量匹配
                    String[] fwidArr = fwids.split(",");
                    if(fwidArr != null && fwidArr.length > 0) {
                        fwidList = Arrays.asList(fwidArr);
                    }
                } else if(StringUtils.isNotBlank(gdid)){
                    if(StringUtils.isNotBlank(mulFczTdzPp) && StringUtils.equals(mulFczTdzPp,"true")){
                        if(StringUtils.contains(gdid,",")){
                            String[] gdids = gdid.split(",");
                            if(gdids != null && gdids.length > 0){
                                for(String gdidTemp : gdids){
                                    if(StringUtils.isNotBlank(gdidTemp)){
                                        fwidList.add(gdidTemp);
                                    }
                                }
                            }
                        }else{
                            List<String> fwidTempList = gdFwService.getFwidByQlid(qlid);
                            if(CollectionUtils.isNotEmpty(fwidTempList)){
                                if(fwidTempList.size() > 1){
                                    for(String fwidTemp : fwidTempList){
                                        if(StringUtils.isNotBlank(fwidTemp)){
                                            fwidList.add(fwidTemp);
                                        }
                                    }
                                }else{
                                    fwidList.add(gdid);
                                }
                            }else{
                                List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(qlid);
                                if(CollectionUtils.isNotEmpty(gdTdList)){
                                    fwidList.add(gdid);
                                }
                            }
                        }
                    }else {
                        fwidList.add(gdid);
                    }
                }

                if(CollectionUtils.isNotEmpty(fwidList)) {
                    for(String fwidTemp : fwidList) {
                        //zq 插入过渡房屋权利或者过渡土地与不动产单元之间的关系 多权利合并引起的问题
                        if(StringUtils.isNotBlank(fwidTemp)) {
                            List<String> qlids = null;
                            if(StringUtils.isNotBlank(mulFczTdzPp) && StringUtils.equals(mulFczTdzPp,"true")){
                                qlids = gdQlDyhRelService.saveGdQlDyhRelsForMul(fwidTemp, bdclx, qlid, tdqlid, bdcdyh, djId);
                            }else{
                                qlids = gdQlDyhRelService.saveGdQlDyhRels(fwidTemp, bdclx, qlid, tdqlid, bdcdyh, djId);
                            }
                            if(CollectionUtils.isNotEmpty(qlids)) {
                                //hqz根据bdc权利对应的项目proid集合批量更新状态
                                gdXmService.updateGdxmPpztByQlids(qlids, ppzt);
                            }
                        }
                        bdcGdDyhRelService.saveGdDyhRels(fwidTemp, tdid, tdqlid, bdcdyh, djId);
                    }
                }
            }

            //苏州需求，读取配置判断是否在匹配成功后弹出匹配清单
            String isShowPphzd = AppConfig.getProperty("is_show_pphzd");
            if(StringUtils.isBlank(isShowPphzd)) {
                isShowPphzd = ParamsConstants.FALSE_LOWERCASE;
            }
            result.put("is_show_pphzd",isShowPphzd);

            if (StringUtils.isNotBlank(gdproid)&&StringUtils.isNotBlank(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationgdfield.queue"))&&StringUtils.equals(AppConfig.getProperty("bdcdj.use.rabbitmq"),"true")) {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("proid",gdproid);
                CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
                correlationData.setMessage(jsonObject);
                correlationData.setRoutingKey(AppConfig.getProperty("bdcdj.rabbitmq.bdc.synchronizationgdfield.queue"));
                rabbitmqSendMessageService.sendMsg(gdproid,correlationData.getRoutingKey(),JSON.toJSONString(jsonObject),correlationData);
            }else if (StringUtils.isNotBlank(gdproid)) {
                redundantFieldService.synchronizationGdField(gdproid);
            }
        } catch (Exception e) {
            msg = "匹配失败";
            logger.error("BdcJgSjglConntroller.matchData",e);
        }
        result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return result;

    }


    /**
     * 匹配不动产单元和房产证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "matchDataAll")
    public HashMap matchDataAll(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh,
                                @RequestParam(value = "djId", required = false) String djId,
                                @RequestParam(value = "gdid", required = false) String gdid,
                                @RequestParam(value = "tdid", required = false) String tdid,
                                @RequestParam(value = "gdproid", required = false) String gdproid,
                                @RequestParam(value = "ppzt", required = false) String ppzt,
                                @RequestParam(value = "bdclx", required = false) String bdclx,
                                @RequestParam(value = "tdqlid", required = false) String tdqlid,
                                @RequestParam(value = "qlid", required = false) String qlid,
                                @RequestParam(value = "fwids", required = false) String fwids
    ) {
        HashMap result = new HashMap();
        String msg = "匹配成功";
        //是否允许多本房产证匹配多本土地证
        String mulFczTdzPp = AppConfig.getProperty(PARAMETER_SJPP_MULFCZTDZPP);
        try {
            //jyl 一键匹配根据gdproid获取所有的fwid
            List<GdFw> gdFwList = gdFwService.getGdFwByProid(gdproid, "");
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                for(GdFw gdFw : gdFwList) {
                    String fwidTemp = gdFw.getFwid();
                    //根据房屋信息获取权籍不动产单元信息
                    HashMap map = gdFwService.getBdcdyh(gdFw);
                    if(map != null && map.get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                        bdcdyh = map.get(ParamsConstants.BDCDYH_CAPITAL).toString();
                        if(map.get("ID") != null)
                            djId = map.get("ID").toString();
                    }
                    //根据坐落获取土地信息
                    if(StringUtils.isNoneBlank(gdFw.getFwzl())) {
                        List<String> tdQlidList = gdFwMapper.getTdQlidByZl(gdFw.getFwzl());
                        if(CollectionUtils.isNotEmpty(tdQlidList)) {
                            tdqlid = tdQlidList.get(0);
                        }
                        if(StringUtils.isNoneBlank(gdFw.getFwzl())) {
                            List<String> tdidList = gdFwMapper.getTdidByZl(gdFw.getFwzl());
                            if(CollectionUtils.isNotEmpty(tdidList)) {
                                tdid = tdidList.get(0);
                            }
                        }
                    }
                    //jyl 不动产证书做抵押，不需要匹配土地证
                    List<GdDy> gdDyList = null;
                    List<GdDy> gdDyByFwidList = new ArrayList<GdDy>();
                    gdDyList = gdFwService.getGdDyListByGdproid(gdproid, 0);
                    if(CollectionUtils.isNotEmpty(gdDyList)) {
                        for(GdDy gdDy : gdDyList) {
                            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(fwidTemp, gdDy.getDyid());
                            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                gdDyByFwidList.add(gdDy);
                            }
                        }
                        if(CollectionUtils.isNotEmpty(gdDyByFwidList)
                                &&gdDyByFwidList.get(0).getYqzh() != null
                                &&gdDyByFwidList.get(0).getYqzh().indexOf("不动产") > -1) {
                            tdid = "";
                            tdqlid = "";
                        }
                    }
                    List<GdFwsyq> gdFwsyqList = null;
                    List<GdFwsyq> gdFwsyqByFwidList = new ArrayList<GdFwsyq>();
                    gdFwsyqList = gdFwService.getGdFwsyqListByGdproid(gdproid, 0);
                    if(CollectionUtils.isNotEmpty(gdFwsyqList)) {
                        for(GdFwsyq gdFwsyq : gdFwsyqList) {
                            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(fwidTemp, gdFwsyq.getQlid());
                            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                gdFwsyqByFwidList.add(gdFwsyq);
                            }
                        }
                        if(CollectionUtils.isNotEmpty(gdFwsyqByFwidList)
                                &&StringUtils.isNotBlank(gdFwsyqByFwidList.get(0).getYqzh())
                                && gdFwsyqByFwidList.get(0).getYqzh().indexOf("不动产") > -1) {
                            tdid = "";
                            tdqlid = "";
                        }
                    }
                    //zq 插入过渡房屋权利或者过渡土地与不动产单元之间的关系 多权利合并引起的问题
                    if(StringUtils.isNotBlank(fwidTemp) && StringUtils.isNotEmpty(djId)) {
                        List<String> qlids = null;
                        if(StringUtils.isNotBlank(mulFczTdzPp) && StringUtils.equals(mulFczTdzPp,"true")){
                            qlids = gdQlDyhRelService.saveGdQlDyhRelsForMul(fwidTemp, bdclx, qlid, tdqlid, bdcdyh, djId);
                        }else{
                            qlids = gdQlDyhRelService.saveGdQlDyhRels(fwidTemp, bdclx, qlid, tdqlid, bdcdyh, djId);
                        }
                        if(CollectionUtils.isNotEmpty(qlids)) {
                            //hqz根据bdc权利对应的项目proid集合批量更新状态
                            gdXmService.updateGdxmPpztByQlids(qlids, ppzt);
                        }
                    }
                    bdcGdDyhRelService.saveGdDyhRels(fwidTemp, tdid, tdqlid, bdcdyh, djId);
                }
            }
        } catch (Exception e) {
            msg = "匹配失败";
            logger.error("BdcJgSjglConntroller.matchDataAll",e);
        }
        result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return result;
    }

    /**
     * 获取房屋匹配的土地分页数据
     *
     * @param sidx
     * @param gdTdQl
     * @param sord
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdFwTdJson")
    public Object getGdFwTdJson(Pageable pageable, GdTdQl gdTdQl, @RequestParam(value = "sidx", required = false) String sidx, @RequestParam(value = "sord", required = false) String sord, @RequestParam(value = "iszx", required = false) Integer iszx, @RequestParam(value = "hhSearch", required = false) String hhSearch, @RequestParam(value = "rf1Dwmc", required = false) String rf1Dwmc, @RequestParam(value = "qlrzjh", required = false) String qlrzjh, @RequestParam(value = "tdid", required = false) String tdid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletRequest request, @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt, @RequestParam(value = "tdids", required = false) String tdids, @RequestParam(value = "fwtdz", required = false) String fwtdz, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "qlid", required = false) String qlid) {
        HashMap slcxMap = new HashMap();
        long startTime = System.currentTimeMillis();
        if(gdTdQl != null) {
            if(StringUtils.isNotBlank(gdTdQl.getTdzh()))
                slcxMap.put("tdzh", gdTdQl.getTdzh());
            if(StringUtils.isNotBlank(gdTdQl.getTdzl()))
                slcxMap.put("tdzl", gdTdQl.getTdzl());
            if(StringUtils.isNotBlank(gdTdQl.getQlr()))
                slcxMap.put("qlr", gdTdQl.getQlr());
            if(StringUtils.isNotBlank(gdTdQl.getDjh()))
                slcxMap.put("djh", gdTdQl.getDjh());
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(qlid)) {
            map.put(ParamsConstants.QLIDS_LOWERCASE, Arrays.asList(qlid.split(",")));
        }

        if(StringUtils.isNotBlank(tdid)) {
            map.put("tdid", tdid);
        }

        if(StringUtils.isNotBlank(hhSearch) || slcxMap.size() > 0 || StringUtils.isNotBlank(qlrzjh)) {
            if(StringUtils.isNotBlank(qlrzjh)) {
                Example example = new Example(GdQlr.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("qlrzjh", qlrzjh);
                List<GdQlr> gdQlrList = entityMapper.selectByExample(GdQlr.class, example);
                if(CollectionUtils.isNotEmpty(gdQlrList)) {
                    List<String> qlidList = new ArrayList<String>();
                    for(GdQlr gdQlr : gdQlrList) {
                        qlidList.add(gdQlr.getQlid());
                    }
                    if(CollectionUtils.isNotEmpty(qlidList)) {
                        map.put(ParamsConstants.QLIDS_LOWERCASE, qlidList);
                    }
                }
            }
            //混合查询
            if(map.size() == 0) {
                map = slcxMap;
                map.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
            }
        }
        if(StringUtils.isNotBlank(tdids) && tdids.split(",").length > 0) {
            map.put("tdids", tdids.split(","));
        }
        Page<HashMap> dataPaging = repository.selectPaging("getGdFwTdJsonByPage", map, pageable);
        logger.info("fwTd代码程序运行时间：" + (System.currentTimeMillis() - startTime) + "ms");
        return dataPaging;
    }

    /**
     * zx 获取土地权利状态
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "asyncGetGdQlztByBdcid")
    public HashMap asyncGetGdQlztByBdcid(Model model, String tdid, String bdclx) {
        HashMap resultMap = new HashMap();
        if(StringUtils.isNotBlank(tdid)) {
            //获取过渡权利 页面传过来tdid就是qlid
            List gdQlList = gdFwService.getGdQlListByQlid(tdid, null, bdclx);
            //过渡项目的权利状态
            String qlzt = gdFwService.getQlztByQlid(tdid, null, bdclx, gdQlList);
            resultMap.put(ParamsConstants.QLZTS_LOWERCASE, qlzt);
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "createCsdj")
    public Project creatCsdj(Model model, Project project, String sqlxMc, String lx, @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdproids", required = false) String gdproids, @RequestParam(value = "qlids", required = false) String qlids) {
        if(StringUtils.isNotBlank(lx)) {
            project.setBdclx(lx);
        } else {
            project.setBdclx(Constants.BDCLX_TDFW);
        }
        Project returnProject = null;
        String proid = "";
        //防止生成方式qh
        if(!StringUtils.equals(dwdm, "default")) {
            project.setDwdm(dwdm);
        }
        if(StringUtils.equals(AppConfig.getProperty("dwdm"), Constants.DWDM_SZ)) {
            if(StringUtils.isNotBlank(project.getProid())) {
                proid = project.getProid();
            } else {
                proid = UUIDGenerator.generate18();
            }
        } else {
            proid = UUIDGenerator.generate18();
        }
        if(StringUtils.isBlank(gdproids) && StringUtils.isNotBlank(gdproid)) {
            gdproids = gdproid;
        }
        if(StringUtils.isBlank(qlids) && StringUtils.isNotBlank(qlid)) {
            qlids = qlid;
        }

        //首页创建过渡项目，编号问题处理
        BdcXm bdcXm = null;
        if(StringUtils.isNotBlank(project.getProid())) {
            bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh())) {
                project.setBh(bdcXm.getBh());
            }
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid()) && StringUtils.isBlank(project.getWiid())) {
                project.setWiid(bdcXm.getWiid());
            }
        }

        //jyl 根据工作流wiid  查找是否已经存在  如果存在 则删除对应的记录
        if(StringUtils.isNotBlank(project.getWiid())) {
            List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            if(CollectionUtils.isNotEmpty(xmList)) {
                for(BdcXm bdcXmTemp : xmList) {
                    delProjectDefaultServiceImpl.delBdcBdxx(bdcXmTemp);
                    delProjectDefaultServiceImpl.delBdcXm(bdcXmTemp.getProid());
                }
            }
        }
        //zdd 如果选择房产信息   不动产类型一定为Constants.BDCLX_TDFW  由于构筑物（BDCLX_TDGZW）极少发证 暂不考虑
        //zq 初始化批量的过渡参数，转换为bdcxmrel对象
        projectService.initGdDataToBdcXmRel(project, gdproids, qlids, lx);
        if(StringUtils.isBlank(project.getUserId())){
            project.setUserId(super.getUserId());
        }
        List<BdcXmRel> bdcXmRelList = null;
        bdcXmRelList = project.getBdcXmRelList();
        //zhengqi按照bdcxmrel循环批量初始化数据
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for(BdcXmRel bdcXmReltemp : bdcXmRelList) {
                //jyl组织单个不动产单元信息的project
                List<BdcXmRel> tempbdcXmRelList = new ArrayList<BdcXmRel>();
                project.setProid(proid);
                tempbdcXmRelList.add(bdcXmReltemp);
                project.setBdcXmRelList(tempbdcXmRelList);
                project.setYqlid(bdcXmReltemp.getYqlid());
                project.setGdproid(bdcXmReltemp.getYproid());
                if(StringUtils.isNotBlank(bdcXmReltemp.getQjid())) {
                    project.setDjId(bdcXmReltemp.getQjid());
                    HashMap map = new HashMap();
                    if (StringUtils.isNotBlank(project.getBdclx())) {
                        map.put("bdclx", project.getBdclx());
                    }
                    map.put("id", project.getDjId());
                    List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyList)
                            &&bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL)
                            &&bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                        project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                    }
                    if(StringUtils.isBlank(project.getBdcdyh())) {
                        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                            DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(project.getDjId());
                            if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                                project.setBdcdyh(djsjFwxx.getBdcdyh());
                            }
                        }
                    }

                }
                //获取权利类型和登记事由、申请类型
                if(StringUtils.isNotBlank(sqlxMc)) {
                    List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
                    if(CollectionUtils.isNotEmpty(mapList)) {
                        Map map = mapList.get(0);
                        String djsy = "";
                        if(map.get(ParamsConstants.QLLXDM_CAPITAL) != null) {
                            project.setQllx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL)));
                        }
                        if(map.get(ParamsConstants.SQLXDM_CAPITAL) != null) {
                            project.setSqlx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.SQLXDM_CAPITAL)));
                            /**
                             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                             *@description 依生效法律文书申请转移登记 可能是土地转移也可能是房屋转移，因此其权利类型，登记事由不能读取配置，通过不动产单元号来判断
                             */
                            if(project!=null&&StringUtils.isNotBlank(project.getSqlx())&&StringUtils.equals(project.getSqlx(),Constants.SQLX_ZY_SFCD)&&StringUtils.isNotBlank(project.getBdcdyh())){
                                djsy = bdcTzmDjsjRelService.getDjsjByBdcdyh(project.getBdcdyh());
                                String qllx = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
                                if(StringUtils.isNotBlank(qllx)){
                                    project.setQllx(qllx);
                                }
                            }else{
                                djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                            }
                        }
                        if(StringUtils.isNotBlank(djsy)) {
                            project.setDjsy(djsy);
                        } else {
                            project.setDjsy(CommonUtil.formatEmptyValue(map.get("DJSYDM")));
                        }
                    }
                }
                //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断
                if(StringUtils.isBlank(project.getQllx())) {
                    String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
                    project.setQllx(qllxdm);
                }
                /**
                 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
                 * @description 更正登记，遗失补发换证登记，通过过渡证书类型来判断权利类型
                 */
                if((project.getDjlx().equals(Constants.DJLX_GZDJ_DM)
                        || project.getSqlx().equals(Constants.SQLX_YSBZ_DM)
                        || project.getSqlx().equals(Constants.SQLX_YSBZ_ZM_DM)
                        || project.getSqlx().equals(Constants.SQLX_HZ_DM))
                        && StringUtils.isNotBlank(project.getGdproid())) {
                    Object insertVo = gdFwService.makeSureQllxByGdproid(gdproid);
                    if(insertVo instanceof GdDy) {
                        project.setQllx(Constants.QLLX_DYAQ);
                    } else if(insertVo instanceof GdYg) {
                        project.setQllx(Constants.QLLX_YGDJ);
                    } else if(insertVo instanceof GdYy) {
                        project.setQllx(Constants.QLLX_YYDJ);
                    }
                }

                if(StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                    project.setXmly(Constants.XMLY_FWSP);
                }else {
                    project.setXmly(Constants.XMLY_TDSP);
                }

                returnProject = projectService.creatProjectEvent(projectService.getCreatProjectService((BdcXm) project), project);
                bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
                //lj 匹配创建合并流程，存在多个项目
                List<BdcXm> bdcXmList = null;
                if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                    bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                }
                if(CollectionUtils.isNotEmpty(bdcXmList)) {
                    //记录遗失补证或换证生成的权利Id
                    for(BdcXm xm : bdcXmList) {
                        String sqlx = xm.getSqlx();
                        //hqz 未匹配不动产单元，不生成权利
                        List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
                        if(!(bppSqlxdmList.contains(sqlx) || CommonUtil.indexOfStrs(Constants.SQLX_NOBDCDY_GDQL, sqlx)) && !(StringUtils.equals(sqlx,Constants.SQLX_SFCD) && StringUtils.isBlank(xm.getBdcdyid()))) {
                            turnProjectDefaultService.saveQllxVo(xm);
                        }

                        /**
                         *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                         *@description 抵押查封通过前台给出提示判断是否带入附属设施,此处不执行
                         */
                        QllxVo qllxVo = qllxService.makeSureQllx(xm);
                        BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(xm.getBdcdyid());
                        if(qllxVo instanceof BdcFdcq&&bdcdy != null){
                            bdcFwFsssService.initializeBdcFwfsss(bdcdy.getBdcdyh(), xm);
                        }
                        bdcSpxxService.dealWithSpxxZdzhmj(xm);
                    }
                }
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
        return returnProject;
    }

    @ResponseBody
    @RequestMapping(value = "/wwCreateGdProject")
    public String wwCreateGdProject(@RequestBody Project project) {
        String sqlxmc = null;
        String msg = null;
        if(project != null && StringUtils.isNotBlank(project.getSqlx())){
            sqlxmc = bdcZdGlService.getSqlxMcByDm(project.getSqlx());
        }
        Project returnProject = creatCsdj(null,project,sqlxmc,project.getBdclx(),project.getYqlid(),project.getGdproid(),null,null);
        if(returnProject != null){
            msg = "成功";
        }else{
            msg = "失败";
        }
        return msg;
    }

    /**
     * lst 获取要注销的项目的权利状态 判断是否可注销
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "isCancel")
    public HashMap isCancel(Model model, Project project, String sqlxMc, String bdclx) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HashMap resultMap = new HashMap();
        boolean isCancel = true;
        String msg = "";
        String checkModel = "";
        List<BdcExamineParam> bdcExamineParamList = null;
        String sqlx = StringUtils.EMPTY;
        if(StringUtils.isNotBlank(sqlxMc)) {
            //根据登记名称获取该登记申请类型代码
            List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
            if(CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                if(map.get(ParamsConstants.SQLXDM_CAPITAL) != null) {
                    sqlx = map.get(ParamsConstants.SQLXDM_CAPITAL).toString();
                }
            }
        }

        List<String> unExamineSqlx = ReadXmlProps.getUnExamineSqlxDm();
        Project projectTemp = (Project) BeanUtils.cloneBean(project);
        if(CollectionUtils.isEmpty(unExamineSqlx) || (CollectionUtils.isNotEmpty(unExamineSqlx) && !unExamineSqlx.contains(sqlx))) {
            if(StringUtils.isNotBlank(project.getGdproid())) {
                StringBuilder tempGdProids = new StringBuilder();
                for(String gdProid : StringUtils.split(project.getGdproid(), ",")) {
                    tempGdProids.append(gdProid);
                    tempGdProids.append(Constants.SPLIT_STR);
                }
                projectTemp.setGdproid(tempGdProids.toString());
                bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(StringUtils.EMPTY, projectTemp);
            }
            if(CollectionUtils.isNotEmpty(bdcExamineParamList)) {
                Map<String, Object> examineMap = bdcExamineService.performExamine(bdcExamineParamList, project.getWiid());
                if(examineMap != null && examineMap.containsKey("info") && examineMap.get("info") != null) {
                    resultMap.put(ParamsConstants.RESULT_LOWERCASE, false);
                    resultMap.put(ParamsConstants.CHECKMODEL_HUMP, ParamsConstants.ALERT_CAPITAL);
                    resultMap.put("msg", examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString());
                    if(examineMap.get("xzwh") != null) {
                        resultMap.put("xzwh", examineMap.get("xzwh").toString());
                    }
                    if(examineMap.get("info") != null) {
                        resultMap.put("info", examineMap.get("info"));
                    }
                    return resultMap;
                }
            }
        }
        //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断

        if(StringUtils.isBlank(project.getQllx())) {
            String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(qllxdm);
        }
        //获取权利类型和登记事由、申请类型
        if(StringUtils.isNotBlank(sqlxMc)) {
            //根据登记名称获取改登记对应记录信息，如权利类型代码，申请类型代码
            List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
            if(CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                if (StringUtils.isNotBlank(project.getQllx())) {
                    String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
                    project.setQllx(qllxdm);
                } else {
                    project.setQllx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL)));
                }
                project.setSqlx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.SQLXDM_CAPITAL)));

                if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                    project.setXmly(Constants.XMLY_FWSP);
                } else if (StringUtils.equals(bdclx, Constants.BDCLX_TD)){
                    project.setXmly(Constants.XMLY_TDSP);
                }
                if(StringUtils.isBlank(project.getBdclx())) {
                    project.setBdclx(project.getBdclx());
                }
            }
        }
        if(StringUtils.isNotBlank(project.getSqlx())) {
            String yqllxdm = bdcZdGlService.getYqllxBySqlx(project.getSqlx());
            if(StringUtils.equals(bdclx, "TDFW")) {
                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getGdproid());
            } else if(StringUtils.equals(bdclx, "TD")) {
                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getGdproid());
            } else if(StringUtils.equals(bdclx, "TDSL")) {
//                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getLqid());
            } else if(StringUtils.equals(bdclx, "TDCQ")) {
//                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getCqid());
            }
            if(!isCancel) {
                msg = "请选择对应的证书创建项目";
            }
        }
        //ZDD 只验证alert的
        List<Map<String, Object>> checkMsg = null;
        if(isCancel) {
            checkMsg = projectCheckInfoService.checkXmByProject(project);
            if(CollectionUtils.isNotEmpty(checkMsg)) {
                for(Map<String, Object> map : checkMsg) {
                    if(map.containsKey(ParamsConstants.CHECKMODEL_HUMP) && map.get(ParamsConstants.CHECKMODEL_HUMP).toString().toUpperCase().equals(ParamsConstants.ALERT_CAPITAL)) {
                        isCancel = false;
                        checkModel = ParamsConstants.ALERT_CAPITAL;
                        msg = CommonUtil.formatEmptyValue(map.get(ParamsConstants.CHECKMSG_HUMP));
                        break;
                    }
                }
                if(isCancel) {
                    for(Map<String, Object> map : checkMsg) {
                        if(map.containsKey(ParamsConstants.CHECKMODEL_HUMP) && map.get(ParamsConstants.CHECKMODEL_HUMP).toString().toUpperCase().equals("CONFIRM")) {
                            isCancel = false;
                            checkModel = "CONFIRM";
                            if(StringUtils.isBlank(msg))
                                msg = CommonUtil.formatEmptyValue(map.get(ParamsConstants.CHECKMSG_HUMP));
                            else
                                msg = msg + "<br/>" + CommonUtil.formatEmptyValue(map.get(ParamsConstants.CHECKMSG_HUMP));
                        }
                    }
                }
            }
        }
        resultMap.put(ParamsConstants.RESULT_LOWERCASE, isCancel);
        resultMap.put(ParamsConstants.CHECKMODEL_HUMP, checkModel);
        resultMap.put("msg", msg);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/wwCheckGdXm")
    public HashMap wwCheckBdcXm(@RequestBody Project project) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String sqlxmc = null;
        if(project != null && StringUtils.isNotBlank(project.getSqlx())){
            sqlxmc = bdcZdGlService.getSqlxMcByDm(project.getSqlx());
        }
        return isCancel(null,project,sqlxmc,project.getBdclx());
    }


    /**
     * liujie 过渡数据创建项目展示所有验证项
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "isCancelAll")
    public List<Map<String, Object>> isCancelAll(Model model, Project project, String sqlxMc, String bdclx) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        boolean isCancel = true;
        List<BdcExamineParam> bdcExamineParamList = null;
        Map allLxMap = null;
        if(StringUtils.isNotBlank(sqlxMc)) {
            //根据登记名称获取该登记申请类型代码
            List<Map> allLxMapList = bdcXmService.getAllLxByWfName(sqlxMc);
            if(CollectionUtils.isNotEmpty(allLxMapList)) {
                allLxMap = allLxMapList.get(0);
                project.setQllx(CommonUtil.formatEmptyValue(allLxMap.get(ParamsConstants.QLLXDM_CAPITAL)));
                project.setSqlx(CommonUtil.formatEmptyValue(allLxMap.get(ParamsConstants.SQLXDM_CAPITAL)));
            }
        }

        List<String> unExamineSqlx = ReadXmlProps.getUnExamineSqlxDm();
        Project projectTemp = (Project) BeanUtils.cloneBean(project);
        if(CollectionUtils.isEmpty(unExamineSqlx) || (CollectionUtils.isNotEmpty(unExamineSqlx) && !unExamineSqlx.contains(project.getSqlx()))) {
            if(StringUtils.isNotBlank(project.getGdproid())) {
                StringBuilder tempGdProids = new StringBuilder();
                for(String gdProid : StringUtils.split(project.getGdproid(), ",")) {
                    tempGdProids.append(gdProid);
                    tempGdProids.append(Constants.SPLIT_STR);
                }
                projectTemp.setGdproid(tempGdProids.toString());
                bdcExamineParamList = examineCheckInfoService.getBdcExamineParam(StringUtils.EMPTY, projectTemp);
            }
            if(CollectionUtils.isNotEmpty(bdcExamineParamList)) {
                Map<String, Object> examineMap = bdcExamineService.performExamine(bdcExamineParamList, project.getWiid());
                if(examineMap != null && examineMap.containsKey("info") && examineMap.get("info") != null) {
                    Map<String, Object> returnMap = new HashMap<String, Object>();
                    returnMap.put(ParamsConstants.RESULT_LOWERCASE, false);
                    returnMap.put(ParamsConstants.CHECKMODEL_HUMP, "alert");
                    returnMap.put(ParamsConstants.CHECKMSG_HUMP, examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString());
                    if(examineMap.get("xzwh") != null) {
                        returnMap.put("xzwh", examineMap.get("xzwh").toString());
                    }
                    if(examineMap.get("info") != null) {
                        returnMap.put("info", examineMap.get("info"));
                    }
                    String checkMsg =  examineMap.get(ParamsConstants.CHECKMSG_HUMP).toString();
                    if(!StringUtils.contains(checkMsg,"null")){
                        resultList.add(returnMap);
                        return resultList;
                    }
                }
            }
        }
        //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断
        if(StringUtils.isBlank(project.getQllx())) {
            String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(qllxdm);
        }
        if(StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            project.setXmly(Constants.XMLY_FWSP);
        }else if(StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
            project.setXmly(Constants.XMLY_TDSP);
        }

        if(StringUtils.isNotBlank(project.getSqlx())) {
            String yqllxdm = bdcZdGlService.getYqllxBySqlx(project.getSqlx());
            if(StringUtils.equals(bdclx, "TDFW")) {
                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getGdproid());
            } else if(StringUtils.equals(bdclx, "TD")) {
                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getGdproid());
            } else if(StringUtils.equals(bdclx, "TDSL")) {
//                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getLqid());
            } else if(StringUtils.equals(bdclx, "TDCQ")) {
//                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getCqid());
            }
        }

        if(isCancel) {
            List<Map<String, Object>> resultListTemp = projectCheckInfoService.checkXmByProject(project);
            if(CollectionUtils.isNotEmpty(resultListTemp)) {
                resultList = resultListTemp;
            }
        }
        return resultList;
    }


    /**
     * 验证原权利是否已经创建项目
     *
     * @param qlid
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkThisQlidAvailable")
    public String checkThisQlidAvailable(String qlid,String type,String bdcdyh) {
        if(StringUtils.isNotBlank(qlid)) {
            Example example = new Example(BdcXmRel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("yqlid",qlid);
            List<BdcXmRel> bdcXmRelList = entityMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for(BdcXmRel bdcXmRel:bdcXmRelList) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getProid());
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                        if(bdcBdcdy != null&&StringUtils.equals(bdcBdcdy.getBdcdyh(),bdcdyh)) {
                            return ParamsConstants.FALSE_LOWERCASE;
                        }
                    }
                }
            }
        }
        return "true";
    }

    /**
     * @param
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn 验证批量流程
     * @description
     */
    @ResponseBody
    @RequestMapping("/checkMulXmFw")
    public HashMap checkMulXmFw(Project project, @RequestParam(value = "gdproids", required = false) String gdproids, @RequestParam(value = "qlids", required = false) String qlids, @RequestParam(value = "sqlxMc", required = false) String sqlxMc) {
        //批量流程选择时候的验证处理，不记入验证配置项
        HashMap returnmap = new HashMap();
        String msg = "";
        if(StringUtils.isNotBlank(qlids)) {
            List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
            String[] qlidArr = qlids.split(",");
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 处理批量验证前台选择的处理
             */
            if(qlidArr != null && qlidArr.length > 1) {
                /**
                 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
                 * @description 通过qlid得到所有的房屋id
                 */
                for(String qlid : qlidArr) {
                    List<GdBdcQlRel> gdBdcQlRelListtemp = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                    if(CollectionUtils.isNotEmpty(gdBdcQlRelListtemp)) {
                        gdBdcQlRelList.addAll(gdBdcQlRelListtemp);
                    }
                }
                StringBuilder gdids = new StringBuilder();
                if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                    for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                        if(StringUtils.isBlank(gdids)) {
                            gdids.append(gdBdcQlRel.getBdcid());
                        } else {
                            gdids.append(",").append(gdBdcQlRel.getBdcid());
                        }
                    }
                }
                List<BdcGdDyhRel> bdcGdDyhRelListTemp = bdcGdDyhRelService.getGdDyhRelList(gdids.toString());
                if(CollectionUtils.isNotEmpty(bdcGdDyhRelListTemp) && CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                    //跨宗匹配土地的有可能会有多个，所以判断单元号关系是否少于房屋数量
                    if(bdcGdDyhRelListTemp.size() < gdBdcQlRelList.size()) {
                        msg = Constants.PLMSG;
                    }
                    for(BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelListTemp) {
                        if(StringUtils.isBlank(bdcGdDyhRel.getBdcdyh())) {
                            msg = Constants.PLMSG;
                            break;
                        }
                    }
                } else if(CollectionUtils.isEmpty(bdcGdDyhRelListTemp)) {
                    msg = Constants.PLMSG;
                }
            }
            /**
             * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
             * @description 处理批量每一本证书的单个验证，只验证alert
             */
            if(StringUtils.isBlank(msg)) {
                project = projectService.initGdDataToBdcXmRel(project, gdproids, qlids);
                //获取权利类型和登记事由、申请类型
                if(StringUtils.isNotBlank(sqlxMc)) {
                    List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
                    if(CollectionUtils.isNotEmpty(mapList)) {
                        HashMap map = (HashMap) mapList.get(0);
                        project.setQllx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL)));
                        project.setSqlx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.SQLXDM_CAPITAL)));
                        project.setBdclx(Constants.BDCLX_TDFW);
                    }
                }
                if(CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                    for(BdcXmRel bdcXmRel : project.getBdcXmRelList()) {
                        project.setDjId(bdcXmRel.getQjid());
                        project.setGdproid(bdcXmRel.getYproid());

                        List<BdcGdDyhRel> gdDyhRelList = null;
                        List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRel.getYqlid());
                        if(CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)) {
                            GdBdcQlRel gdBdcQlRel = gdBdcQlRelListTemp.get(0);
                            if(gdBdcQlRel != null) {
                                gdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                            }
                        }

                        if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                            project.setBdcdyh(gdDyhRelList.get(0).getBdcdyh());
                        }
                        project.setYqlid(bdcXmRel.getYqlid());
                        List<Map<String, Object>> checkMsg = projectCheckInfoService.checkXmByProject(project);
                        if(CollectionUtils.isNotEmpty(checkMsg)) {
                            for(Map<String, Object> map : checkMsg) {
                                if(map.containsKey(ParamsConstants.CHECKMODEL_HUMP) && map.get(ParamsConstants.CHECKMODEL_HUMP).toString().toUpperCase().equals(ParamsConstants.ALERT_CAPITAL)) {
                                    msg = CommonUtil.formatEmptyValue(map.get(ParamsConstants.CHECKMSG_HUMP));
                                    break;
                                }
                            }
                        } else {
                            String yqllxdm = bdcZdGlService.getYqllxBySqlx(project.getSqlx());
                            boolean isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), bdcXmRel.getYproid());
                            if(!isCancel) {
                                msg = "请选择对应的证书创建项目";
                            }
                        }
                        if(StringUtils.isNotEmpty(msg)) {
                            break;
                        }
                    }
                }
            }
            returnmap.put("msg", msg);
        }
        return returnmap;
    }


    /**
     * @param
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn 验证批量流程
     * @description
     */
    @ResponseBody
    @RequestMapping("/checkMulXm")
    public List<Map<String, Object>>  checkMulXm(Project project, @RequestParam(value = "gdproids", required = false) String gdproids, @RequestParam(value = "qlids", required = false) String qlids, @RequestParam(value = "sqlxMc", required = false) String sqlxMc) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if(StringUtils.isNotBlank(qlids)) {
            String[] qlidArr = qlids.split(",");
            if(qlidArr != null && qlidArr.length > 1) {
                project = projectService.initGdDataToBdcXmRel(project, gdproids, qlids);
                //获取权利类型和登记事由、申请类型
                if(StringUtils.isNotBlank(sqlxMc)) {
                    List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
                    if(CollectionUtils.isNotEmpty(mapList)) {
                        HashMap map = (HashMap) mapList.get(0);
                        project.setQllx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.QLLXDM_CAPITAL)));
                        project.setSqlx(CommonUtil.formatEmptyValue(map.get(ParamsConstants.SQLXDM_CAPITAL)));
                        project.setBdclx(Constants.BDCLX_TDFW);
                    }
                }
                if(CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                    for(BdcXmRel bdcXmRel : project.getBdcXmRelList()) {
                        project.setDjId(bdcXmRel.getQjid());
                        project.setGdproid(bdcXmRel.getYproid());

                        List<BdcGdDyhRel> gdDyhRelList = null;
                        List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRel.getYqlid());
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)) {
                            GdBdcQlRel gdBdcQlRel = gdBdcQlRelListTemp.get(0);
                            if (gdBdcQlRel != null)
                                gdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                        }

                        if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                            project.setBdcdyh(gdDyhRelList.get(0).getBdcdyh());
                        }
                        project.setYqlid(bdcXmRel.getYqlid());
                        List<Map<String, Object>> resultTemp = projectCheckInfoService.checkXmByProject(project);
                        if(CollectionUtils.isNotEmpty(resultTemp)){
                            resultList.addAll(resultTemp);
                        }
                    }

                }
            }
        }
        return resultList;
    }


    /**
     * 查询项目
     */
    @ResponseBody
    @RequestMapping("/getBdcXmJson")
    public Object getBdcXmJson(Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(hhSearch)) {
            map.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        }
        return repository.selectPaging("getBdcXmJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/glxm")
    public Project glxm(Model model, Project project, String proid, String sqlxMc, @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdproids", required = false) String gdproids, @RequestParam(value = "qlids", required = false) String qlids, @RequestParam(value = "bdclx", required = false) String bdclx) {
        if(StringUtils.equals(bdclx, Constants.BDCLX_TDFW))
            project.setXmly(Constants.XMLY_FWSP);
        else if(StringUtils.equals(bdclx, Constants.BDCLX_TD))
            project.setXmly(Constants.XMLY_TDSP);


        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        CreatProjectService creatProjectService = projectService.getCreatProjectService(bdcXm);
        bdcXm.setXmly(project.getXmly());
        project.setYqlid(qlid);
        if(StringUtils.indexOf(project.getBdcdyh(), ",") > -1) {
            project.setYqlid(qlids);
            project.setGdproid(gdproids);
        } else
            project.setGdproid(gdproid);
        if(StringUtils.isNotBlank(project.getGdproid())) {
            String bdcdys = gdXmService.getBdcdyhsByGdProid(project.getGdproid());
            if(StringUtils.isNotBlank(bdcdys)) {
                List<String> bdcdyList = new ArrayList<String>();
                bdcdyList.add(bdcdys);
                project.setBdcdyhs(bdcdyList);
            }
        }
        project.setBh(bdcXm.getBh());
        project.setQllx(bdcXm.getQllx());
        // liujie 针对换证没有qllx的情况
        if(StringUtils.isBlank(project.getQllx())) {
            String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(qllxdm);
            bdcXm.setQllx(qllxdm);
        }
        project.setSqlx(bdcXm.getSqlx());
        project.setGdproid(gdproid);
        project.setProid(proid);
        List<InsertVo> list = creatProjectService.initVoFromOldData(project);
        creatProjectService.insertProjectData(list);
        bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        //        更新remark和工程名称
        creatProjectService.updateWorkFlow(bdcXm);
        TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);

        PfWorkFlowInstanceVo pf = super.getWorkFlowInstance(proid);

        SysTaskService sysTaskService = (SysTaskService) Container.getBean("TaskService");
        List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pf.getWorkflowIntanceId());
        if(CollectionUtils.isNotEmpty(pfTaskVoList)) {
            project.setTaskid(pfTaskVoList.get(0).getTaskId());
        }
        turnProjectDefaultService.saveQllxVo(bdcXm);
        return project;
    }


    //验证过渡关联的信息与收件单是否相符
    @ResponseBody
    @RequestMapping(value = "yzGlxmxx", method = RequestMethod.GET)
    public boolean yzGlxmxx(@RequestParam(value = "zl", required = false) String
                                    zl, @RequestParam(value = "proid", required = false) String proid) throws UnsupportedEncodingException {
        //多个权利人是用‘/’分隔符分开
        Boolean pd = false;
        BdcXm bdcXm = null;
        if(StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        if(bdcXm != null) {
            if(StringUtils.equals(bdcXm.getZl(), URLDecoder.decode(zl, "UTF-8"))) {
                pd = true;
            } else {
                return false;
            }

        }

        return pd;
    }

    /**
     * 土地匹配不动产单元
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryBdcdyhByTdidDjh")
    public List queryBdcdyhByTdid(Model model, String qlid) {
        List list = new ArrayList<HashMap>();
        List<GdTd> gdTdList = null;
        if(StringUtils.isNotBlank(qlid)) {
            gdTdList = gdTdService.getGdTdListByQlid(qlid);
            if(CollectionUtils.isNotEmpty(gdTdList)) {
                list = new ArrayList<BdcGdDyhRel>();
                for(GdTd gdTd : gdTdList) {
                    List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", gdTd.getTdid());
                    if(CollectionUtils.isNotEmpty(bdcGdDyhRelList))
                        list.addAll(bdcGdDyhRelList);
                }
            }
        }
        if(CollectionUtils.isEmpty(list)) {
            list = new ArrayList<BdcGdDyhRel>();
            if(CollectionUtils.isNotEmpty(gdTdList)) {
                for(GdTd gdTd : gdTdList) {
                    String newDjh = gdTdService.getNewDjh("dhdzb", gdTd.getTdid());
                    if(StringUtils.isBlank(newDjh))
                        newDjh = gdTd.getDjh();
                    if(StringUtils.isNotBlank(newDjh)) {
                        HashMap map = new HashMap();
                        map.put("djh", newDjh);
                        map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TD);
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if(CollectionUtils.isNotEmpty(bdcdyList)) {
                            BdcGdDyhRel bdcGdDyhRel = new BdcGdDyhRel();
                            bdcGdDyhRel.setBdcdyh(CommonUtil.formatEmptyValue(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)));
                            bdcGdDyhRel.setTdid(gdTd.getTdid());
                            list.add(bdcGdDyhRel);
                        }
                    }
                }
            }
        }
        //如果没值上面的entityMapper会返回null
        if(CollectionUtils.isEmpty(list)) {
            list = new ArrayList<BdcGdDyhRel>();
            BdcGdDyhRel bdcGdDyhRel = new BdcGdDyhRel();
            if(CollectionUtils.isNotEmpty(gdTdList))
                bdcGdDyhRel.setGdid(gdTdList.get(0).getTdid());
            list.add(bdcGdDyhRel);
        }
        return list;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 验证是否可以匹配
     */
    @ResponseBody
    @RequestMapping(value = "checkMatchData")
    public Map checkMatchData(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh,
                              @RequestParam(value = "djId", required = false) String djId,
                              @RequestParam(value = "gdid", required = false) String gdid,
                              @RequestParam(value = "tdid", required = false) String tdid,
                              @RequestParam(value = "gdproid", required = false) String gdproid,
                              @RequestParam(value = "ppzt", required = false) String ppzt,
                              @RequestParam(value = "bdclx", required = false) String bdclx,
                              @RequestParam(value = "tdqlid", required = false) String tdqlid,
                              @RequestParam(value = "qlid", required = false) String qlid) {
        return bdcCheckMatchDataService.checkMatchData(qlid, gdid, bdcdyh, tdid, tdqlid, bdclx);
    }

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 验证房屋跨宗匹配
     **/
    @ResponseBody
    @RequestMapping(value = "checkKzMatchData")
    public HashMap checkKzMatchData(String bdcdyh, String tdid, String gdid, String qlid) {
        return bdcCheckMatchDataService.checkMulMatchData(qlid, gdid, bdcdyh, tdid, Constants.BDCLX_TDFW);
    }

    /**
     * 获取林权分页数据
     *
     * @param page
     * @param rows
     * @param sidx
     * @param sord
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdLqJson")
    public Object getGdLqJson(Pageable pageable, String sidx, String sord, String hhSearch, GdLq gdLq, String
            rf1zjh, String rf1Dwmc, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(hhSearch)) {
            Example example = new Example(GdQlr.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("qlr", StringUtils.deleteWhitespace(hhSearch));
            List<GdQlr> gdQlrList = entityMapper.selectByExample(GdQlr.class, example);
            if(CollectionUtils.isNotEmpty(gdQlrList)) {
                List<String> lqids = new ArrayList<String>();
                for(GdQlr gdQlr : gdQlrList) {
                    lqids.add(gdQlr.getQlid());
                }
                if(CollectionUtils.isNotEmpty(lqids)) {
                    map.put("hhSearchLqids", lqids);
                }
            }
        }
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if(StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if(StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        map.put("rf1Dwmc", rf1Dwmc);
        map.put("lqzh", gdLq.getLqzh());
        map.put("lqzl", gdLq.getLqzl());
        map.put("lz", gdLq.getLz());
        map.put("syqmj", gdLq.getSyqmj());
        map.put("rf1zjh", rf1zjh);
        //混合查询
        map.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        return repository.selectPaging("getGdLqJsonByPage", map,pageable);
    }

    /**
     * 匹配不动产单元和房产证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryBdcdyhByGdid")
    public List<?> queryBdcdyhByDah(Model model, String gdid) {
        List<?> list = new ArrayList<HashMap>();
        if(StringUtils.isNotBlank(gdid)) {
            list = bdcGdDyhRelService.getGdDyhRel("", gdid);
        }

        //如果没值上面的entityMapper会返回null
        if(list == null) {
            list = new ArrayList<HashMap>();
        }
        return list;
    }

    /**
     * 获取草权分页数据
     *
     * @param page
     * @param rows
     * @param sidx
     * @param sord
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdCqJson")
    public Object getGdCqJson(Pageable pageable, String sidx, String sord, String hhSearch, GdCq gdCq, String
            rf1zjh, String rf1Dwmc, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if(StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if(StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        map.put("rf1Dwmc", rf1Dwmc);
        map.put("cqzh", gdCq.getCqzh());
        map.put("zl", gdCq.getZl());
        map.put("cbmj", gdCq.getCbmj());
        map.put("rf1zjh", rf1zjh);
        //混合查询
        map.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        Page<HashMap> dataPaging = repository.selectPaging("getGdCqJsonByPage", map, pageable);
        return dataPaging;
    }

    /**
     * 打开一个项目多个房屋匹配界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "openMulGdFw")
    public String openMulGdFw(Model model, @RequestParam(value = "sqlx", required = false) String sqlx) {
        model.addAttribute("sqlx", sqlx);
        return "/sjgl/mulGdFw";
    }

    /**
     * 注销权利
     */
    @ResponseBody
    @RequestMapping(value = "zxQl")
    public HashMap<String, Object> zxQl(String qlid, String lx) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String userid = super.getUserId();
        String msg = "";
        if(lx.equals("gdFw")) {
            msg = dSjglService.zxQl(qlid,userid);
        } else if(lx.equals("gdTd")) {
            msg = dSjglService.zxGdTdQl(qlid,userid);
        }
        if(StringUtils.isBlank(msg)) {
            msg = "fail";
        }
        resultMap.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "checkMulCf")
    public HashMap<String, Object> checkMulCf(String qlid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String msg = "";
        List<GdCf> gdCfList = gdCfService.getGdCfListByQlid(qlid, 0);
        if(CollectionUtils.isNotEmpty(gdCfList)&&gdCfList.size() > 1) {
            msg = "mulCf";
        }
        resultMap.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return resultMap;
    }

    /**
     * 解除注销权利
     */
    @ResponseBody
    @RequestMapping(value = "jczxQl")
    public HashMap<String, Object> jczxQl(String qlid, String lx) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String msg = "";
        if(lx.equals("gdFw")) {
            msg = dSjglService.jczxQl(qlid);
        } else if(lx.equals("gdTd")) {
            msg = dSjglService.jczxGdTdQl(qlid);
        }

        if(StringUtils.isBlank(msg)) {
            msg = "fail";
        }
        resultMap.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return resultMap;
    }

    //    /**
//     * 删除项目关联
//     */
    @ResponseBody
    @RequestMapping("/deletexmgl")
    public HashMap<String, Object> deletexmgl(String gdproid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String msg = gdXmService.deleteXmgl(gdproid, "", "", "");
        resultMap.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return resultMap;
    }

    /**
     * @author <a href="mailto:zhanglili@gtmap.cn">zhanglili</a>
     * @description匹配界面查看所选房屋列表
     */
    @RequestMapping("/qurrySelectGdfw")
    public String qurrySelectGdfw(Model model, String qlids) {
        model.addAttribute(ParamsConstants.QLIDS_LOWERCASE, qlids);
        return "sjgl/selectGdfw";
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 匹配界面查看所选土地列表
     */
    @RequestMapping("/qurrySelectGdTd")
    public String qurrySelectGdTd(Model model, String qlids) {
        model.addAttribute(ParamsConstants.QLIDS_LOWERCASE, qlids);
        return "sjgl/selectGdTd";
    }

    /**
     * 获得限制原因
     *
     * @param cqzh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getXzyy", method = RequestMethod.GET)
    public Map<String, String> getXzyy(@RequestParam(value = "cqzh", required = false) String cqzh, @RequestParam(value = "qlid", required =false)String qlid) {
        return  selectBdcdyManageService.getXzyy(qlid,cqzh);
    }

    /**
     * 检查所选过渡数据是否已被锁定
     *
     * @param cqzh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkGdSjSd", method = RequestMethod.GET)
    public String checkGdSjSd(@RequestParam(value = "cqzh", required = false) String cqzh) {
        String msg = "true";
        List<GdBdcSd> list = gdXmService.getGdBdcSd("cqzh", cqzh == null ? "" : cqzh, Constants.SDZT_SD);

        //该条过渡数据已被锁定
        if(CollectionUtils.isNotEmpty(list)) {
            msg = ParamsConstants.FALSE_LOWERCASE;
        }
        return msg;
    }

    /**
     * 添加过渡数据锁定
     *
     * @param qlid
     * @param cqzh
     * @param bdclx
     * @param proid
     * @param xzyy
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "lockGdSj", method = RequestMethod.GET)
    public HashMap<String, String> lockGdSj(@RequestParam(value = "qlid", required = false) String qlid,
                                            @RequestParam(value = "cqzh", required = false) String cqzh,
                                            @RequestParam(value = "bdclx", required = false) String bdclx,
                                            @RequestParam(value = "proid", required = false) String proid,
                                            @RequestParam(value = "xzyy", required = false) String xzyy) {
        HashMap<String, String> map = new HashMap<String, String>();
        GdBdcSd gdBdcSd = new GdBdcSd();
        gdBdcSd.setSdid(UUIDGenerator.generate18() == null ? "" : UUIDGenerator.generate18());
        gdBdcSd.setCqzh(cqzh == null ? "" : cqzh);
        gdBdcSd.setQlid(qlid == null ? "" : qlid);
        gdBdcSd.setBdclx(bdclx == null ? "" : bdclx);
        gdBdcSd.setProid(proid == null ? "" : proid);
        gdBdcSd.setXzyy(xzyy == null ? "" : xzyy);
        gdBdcSd.setXztype("");
        gdBdcSd.setXzzt(1);
        gdBdcSd.setSdr(super.getUserName());
        gdBdcSd.setSdsj(new Date(System.currentTimeMillis()));
        int resultCode = gdXmService.saveOrupdateGdBdcSd(gdBdcSd);

        if(resultCode == 0) {
            map.put("flag", ParamsConstants.FALSE_LOWERCASE);
            map.put("msg", "锁定失败!");
        } else {
            map.put("flag", "true");
            map.put("msg", "锁定成功!");
        }

        return map;
    }

    /**
     * 过渡数据解锁
     *
     * @param cqzh
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "UnlockGdSj", method = RequestMethod.GET)
    public HashMap<String, String> unlockGdSj(@RequestParam(value = "cqzh", required = false) String cqzh) {
        int resultCode = 0;
        HashMap<String, String> map = new HashMap<String, String>();
        List<GdBdcSd> list = gdXmService.getGdBdcSd("cqzh", cqzh == null ? "" : cqzh, Constants.SDZT_SD);

        //无法查到锁定数据，即页面上选择的过渡项目没有被锁定
        if(null == list || list.size() == 0) {
            map.put("flag", ParamsConstants.FALSE_LOWERCASE);
            map.put("msg", "该过渡数据没有被锁定，不能解锁！");
            return map;
        } else {
            if(list.get(0) != null) {
                GdBdcSd gdBdcSd = list.get(0);
                gdBdcSd.setXzzt(0);
                gdBdcSd.setJsr(super.getUserName());
                gdBdcSd.setJssj(new Date(System.currentTimeMillis()));
                resultCode = gdXmService.saveOrupdateGdBdcSd(gdBdcSd);
            }
        }
        if(resultCode == 0) {
            map.put("flag", ParamsConstants.FALSE_LOWERCASE);
            map.put("msg", "解锁失败!");
        } else {
            map.put("flag", "true");
            map.put("msg", "解锁成功!");
        }

        return map;
    }

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 历史证书查询界面即过渡数据的历史证书展示
     */
    @RequestMapping("/toDisplayGdData")
    public String toDisplayGdData(Model model) {
        return "query/displayZs";
    }

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 过渡数据证书关系展示
     */
    @RequestMapping(value = "displayZsRel")
    public String showZsRelTemp(Model model, @RequestParam(value = "qlId", required = false) String qlId, @RequestParam(value = "bdcLx", required = false) String bdcLx) {
        model.addAttribute("qlId", qlId);
        model.addAttribute(ParamsConstants.BDCLX_HUMP, bdcLx);
        return "/query/displayZsRel";
    }

    @ResponseBody
    @RequestMapping("/getGdEntityJson")
    public Object getGdEntityJson(Model model, Pageable pageable, String gdId, String bdcLx) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(gdId)) {
            map.put("gdId", gdId);
        } else {
            throw new AppException("未找到过渡数据ID", AppException.ENTITY_EX, new Object());
        }
        if(StringUtils.isNotBlank(bdcLx)) {
            map.put(ParamsConstants.BDCLX_HUMP, bdcLx);
        } else {
            throw new AppException("未指定不动产类型", AppException.ENTITY_EX, new Object());
        }
        model.addAttribute(ParamsConstants.BDCLX_HUMP, bdcLx);
        return repository.selectPaging("getGdEntityJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getGdInfoJson")
    public Object getGdInfoJson(Model model, Pageable pageable, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(hhSearch)) {
            paramMap.put(HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
            List<HashMap<String, String>> list = gdqlService.getCqZhQlrZslxByQlId(paramMap);
            if(CollectionUtils.isNotEmpty(list)) {
                List<String> qlIdList = new ArrayList<String>();
                for(HashMap<String, String> hashMap : list) {
                    qlIdList.add(hashMap.get("QLID"));
                }
                paramMap.clear();
                paramMap.put(ParamsConstants.QLIDS_HUMP, qlIdList);
                String gdIds = gdqlService.getGdIdsByQlId(paramMap, "GDID");
                if(StringUtils.isNotBlank(gdIds)) {
                    String tempGdIds = gdIds.substring(0, gdIds.length() - 1);
                    if(tempGdIds.split(",").length > 0) {
                        map.put("gdIds", tempGdIds.split(","));
                    }
                }
            } else {
                map.clear();
                map.put("zl", "nullNullNullNullNull");
            }
        }
        return repository.selectPaging("getGdInfoJsonByPage", map, pageable);
    }

    @ResponseBody
    @RequestMapping("/getBdcLxAndQlIdsByGdId")
    public HashMap<String, String> getBdcLxAndQlIdsByGdId(String gdId) {
        return gdqlService.getBdcLxAndQlIdsByGdId(gdId);
    }

    @ResponseBody
    @RequestMapping("/getCqZhAndQlrByQlId")
    public HashMap getCqZhAndQlrByQlId(String qlId) {
        HashMap map = new HashMap();
        HashMap stringHashMap = new HashMap<String, Object>();
        map.put(ParamsConstants.QLIDS_HUMP, qlId.split(","));
        List<HashMap<String, String>> list = gdqlService.getCqZhQlrZslxByQlId(map);
        StringBuilder cqZhStringBuilder = new StringBuilder();
        StringBuilder qlrStringBuilder = new StringBuilder();
        StringBuilder zsZtStringBuiledr = new StringBuilder();
        if(CollectionUtils.isNotEmpty(list)) {
            for(HashMap<String, String> hashMap : list) {
                if(StringUtils.isNotBlank(hashMap.get("CQZH"))) {
                    cqZhStringBuilder.append(hashMap.get("CQZH"));
                    map.clear();
                    map.put("CQZH", hashMap.get("CQZH"));
                    map.put("QLID", hashMap.get("QLID"));
                    if(StringUtils.isNotBlank(hashMap.get("ZSLX"))) {
                        if(StringUtils.contains(hashMap.get("ZSLX"), Constants.GDQL_TXZ)) {
                            map.put("ZSLX", Constants.GDQL_DY_CPT);
                        }
                        if(StringUtils.contains(hashMap.get("ZSLX"), Constants.GDQL_YG)) {
                            map.put("ZSLX", Constants.GDQL_YG_CPT);
                        }
                        if(StringUtils.contains(hashMap.get("ZSLX"), Constants.GDQL_YY)) {
                            map.put("ZSLX", Constants.GDQL_YY_CPT);
                        }
                        if(StringUtils.contains(hashMap.get("ZSLX"), Constants.GDQL_CF)) {
                            map.put("ZSLX", Constants.GDQL_CF_CPT);
                        }
                        if(StringUtils.contains(hashMap.get("ZSLX"), Constants.GDQL_FWSYQ)) {
                            map.put("ZSLX", Constants.GDQL_FWSYQ_CPT);
                        }
                        if(StringUtils.contains(hashMap.get("ZSLX"), Constants.GDQL_TDZ)) {
                            map.put("ZSLX", Constants.GDQL_TDSYQ_CPT);
                        }
                    }
                    if(StringUtils.isNotBlank(gdqlService.getGdZsZt(map))) {
                        zsZtStringBuiledr.append(gdqlService.getGdZsZt(map));
                        zsZtStringBuiledr.append("$");
                    }
                }
                cqZhStringBuilder.append(" ");
                if(StringUtils.isNotBlank(hashMap.get("QLR"))) {
                    qlrStringBuilder.append(hashMap.get("QLR"));
                }
                qlrStringBuilder.append(" ");
            }
        }
        stringHashMap.put("CQZH", cqZhStringBuilder.toString());
        stringHashMap.put("QLR", qlrStringBuilder.toString());
        if(StringUtils.isNotBlank(zsZtStringBuiledr.toString())) {
            stringHashMap.put("ZSZT", zsZtStringBuiledr.toString().substring(0, zsZtStringBuiledr.length() - 1));
        }
        return stringHashMap;
    }

    @ResponseBody
    @RequestMapping("/getGdQlInfoJson")
    public Object getGdQlInfoJson(Model model, Pageable pageable, String qlId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String[] qlIds = qlId.split(",");
        map.put(ParamsConstants.QLIDS_HUMP, qlIds);
        return repository.selectPaging("getCqZhQlrZslxByQlId", map, pageable);
    }

    @ResponseBody
    @RequestMapping(value = "asyncGetGdXxByQlid")
    public HashMap asyncGetGdXxByQlid(Model model, String qlid, String bdclx) {
        HashMap resultMap = new HashMap();
        if(StringUtils.isNotBlank(qlid)) {
            //获取过渡权利
            List gdQlList = gdFwService.getGdQlListByQlid(qlid, null, bdclx);
            //过渡项目的权利状态
            String qlzt = gdFwService.getQlztByQlid(qlid, null, bdclx, gdQlList);
            resultMap.put(ParamsConstants.QLZTS_LOWERCASE, qlzt);
            String zls = gdFwService.getGdZlsByGdproid(qlid, null, bdclx, gdQlList);
            resultMap.put("zls", zls);
        }
        return resultMap;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 获取林权权利人信息
     */
    @ResponseBody
    @RequestMapping(value = "asyncGetGdqlrByLqid")
    public HashMap asyncGetGdqlrByLqid(Model model, String lqid) {
        HashMap resultMap = new HashMap();
        if(StringUtils.isNotBlank(lqid)) {
            String qlr = gdQlrService.getGdQlrsByQlid(lqid, Constants.QLRLX_QLR);
            resultMap.put("qlr", qlr);
        }
        return resultMap;
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 获取林权匹配状态
     */
    @ResponseBody
    @RequestMapping(value = "getLqPpztByLqid")
    public Boolean getLqPpztByLqid(String lqid) {
        Boolean flag = false;
        if(StringUtils.isNotBlank(lqid)) {
            Example example = new Example(GdDyhRel.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("gdid", lqid);
            List<GdDyhRel> gdDyhRelList = entityMapper.selectByExample(GdDyhRel.class, example);
            if(CollectionUtils.isNotEmpty(gdDyhRelList)) {
                flag = true;
            }
        }
        return flag;
    }

    @ResponseBody
    @RequestMapping(value = "getBdcdyidOrFwid")
    public String getBdcdyidOrFwid(String bdcdyh, String fwDcbIndex) {
        String result = null;
        if(StringUtils.isNotBlank(bdcdyh)) {
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcdyh);
            if(StringUtils.isNotBlank(bdcdyid)) {
                result = bdcdyid;
            }
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "getFwidByQlid")
    public String getFwidByQlid(String qlid) {
        String fwid = "";
        if(StringUtils.isNotBlank(qlid)) {
            List<String> fwiList = gdFwService.getFwidByQlid(qlid);
            if(CollectionUtils.isNotEmpty(fwiList))
                fwid = fwiList.get(0);
        }
        return fwid;
    }

    @ResponseBody
    @RequestMapping(value = "getFwidsByQlid")
    public String getFwidsByQlid(String qlid) {
        StringBuilder fwids = new StringBuilder();
        if(StringUtils.isNotBlank(qlid)) {
            List<String> fwidList = gdFwService.getFwidByQlid(qlid);
            if(CollectionUtils.isNotEmpty(fwidList)){
                for(String fwid : fwidList){
                    if(StringUtils.isNotBlank(fwid)){
                        fwids.append(",").append(fwid);
                    }
                }
            }
        }
        return fwids.toString();
    }

    /**
     *@Author:<a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
     * @Description:展示匹配单信息
     * @Date 15:41 2018/4/8
     * @param bdcdyh,qlid
     */
    @RequestMapping(value = "showPphzdxx", method = RequestMethod.GET)
    public String showPphzdxx(Model model,@RequestParam(value = "bdcdyh", required = false)String bdcdyh,@RequestParam(value = "qlid", required = false)String qlid, HttpServletRequest request){
        HashMap map = new HashMap();
        HashMap ywlxMap = new HashMap();
        HashMap ppdXx = null;
        HashMap ywlx = new HashMap();
        List<HashMap> hashMapList;
        List<HashMap> ywlxList;
        String ppdXxIsNull = "true";
        if(StringUtils.isNotEmpty(qlid)){
            ywlxMap.put("qlid",qlid);
            ywlxList = bdcPpdService.getYwlxMapByQlid(ywlxMap);
            if(CollectionUtils.isNotEmpty(ywlxList)){
                ywlx = ywlxList.get(0);
            }
        }
        if(StringUtils.isNotEmpty(bdcdyh)){
            map.put(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
            hashMapList = bdcPpdService.getPpdxxMapByBdcdyh(map);
            if(CollectionUtils.isNotEmpty(hashMapList)){
                ppdXx = hashMapList.get(0);
                if(ppdXx!=null){
                    if(ywlx!=null){
                        ppdXx.put("bz",ywlx.get("YWLX"));
                    }else{
                        ppdXx.put("bz","");
                    }
                    model.addAttribute("ppdXx",ppdXx);
                    if(ppdXx.get("QLR")!=null&&StringUtils.isNotEmpty(ppdXx.get("QLR").toString())){
                        model.addAttribute("qlrmc",ppdXx.get("QLR").toString());
                    }else{
                        model.addAttribute("qlrmc","");
                    }
                    if(ppdXx.get(ParamsConstants.BDCDYH_CAPITAL)!=null&&StringUtils.isNotEmpty(ppdXx.get(ParamsConstants.BDCDYH_CAPITAL).toString())){
                        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE,ppdXx.get(ParamsConstants.BDCDYH_CAPITAL).toString());
                    }else{
                        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE,"");
                    }
                    if(ppdXx.get("ZL")!=null&&StringUtils.isNotEmpty(ppdXx.get("ZL").toString())){
                        model.addAttribute("zl",ppdXx.get("ZL").toString());
                    }else{
                        model.addAttribute("zl","");
                    }
                    if(ppdXx.get("FCZH")!=null&&StringUtils.isNotEmpty(ppdXx.get("FCZH").toString())){
                        model.addAttribute("fczh",ppdXx.get("FCZH").toString());
                    }else{
                        model.addAttribute("fczh","");
                    }
                    if(ppdXx.get("TDZ")!=null&&StringUtils.isNotEmpty(ppdXx.get("TDZ").toString())){
                        model.addAttribute("tdz",ppdXx.get("TDZ").toString());
                    }else{
                        model.addAttribute("tdz","");
                    }
                    if(ppdXx.get("bz")!=null&&StringUtils.isNotEmpty(ppdXx.get("bz").toString())){
                        model.addAttribute("bz",ppdXx.get("bz").toString());
                    }else{
                        model.addAttribute("bz","");
                    }
                    ppdXxIsNull = ParamsConstants.FALSE_LOWERCASE;
                }
            }
        }
        model.addAttribute("ppdXxIsNull",ppdXxIsNull);
        model.addAttribute(ParamsConstants.BDCDYH_LOWERCASE,bdcdyh);
        model.addAttribute("qlid",qlid);
        return "sjgl/320500/bdcpphzd";
    }

    @ResponseBody
    @RequestMapping(value = "/getBdcdyhByQueryInfo")
    public Map<String, Object> getBdcdyhByQueryInfo(String bdclx,String zl){
        HashMap<String, Object> resultMap = Maps.newHashMap();
        String bdcdyh = "";
        if(StringUtils.isNotBlank(zl) && StringUtils.isNotBlank(bdclx)){
            HashMap<String,String> queryMap = Maps.newHashMap();
            queryMap.put("zl",zl);
            queryMap.put(ParamsConstants.BDCLX_LOWERCASE,bdclx);
            List<BdcBdcdy> bdcBdcdyList = bdcdyService.getBdcdyInfoByQueryMap(queryMap);
            if(CollectionUtils.isNotEmpty(bdcBdcdyList)){
                for(BdcBdcdy tempBdcbdcdy: bdcBdcdyList){
                    if(null!=tempBdcbdcdy && StringUtils.isNotBlank(tempBdcbdcdy.getBdcdyh())){
                        bdcdyh = tempBdcbdcdy.getBdcdyh();
                        break;
                    }
                }
            }
        }
        resultMap.put(ParamsConstants.RESULT_LOWERCASE,bdcdyh);
        return resultMap;
    }
}