package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdDhDzhMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.*;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.Codecs;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.vo.PfBusinessVo;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 16-2-16
 * Time: 下午6:24
 * dos:过渡数据按项目
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/bdcSjgl")
public class BdcSjglConntroller extends BaseController {

    @Autowired
    private Repo repository;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private GdDhDzhMapper gdDhDzhMapper;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    private ProjectCheckInfoService projectCheckInfoService;
    @Autowired
    private GdSjglService dSjglService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private GdqlService gdqlService;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private GdSaveLogSecvice gdSaveLogSecvice;
    @Resource(name = "delProjectDefaultServiceImpl")
    private DelProjectService delProjectDefaultServiceImpl;

    public static final String PARAMETER_SYS_VERSION = "sys.version";
    public static final String PARAMETER_HHSEARCH = "hhSearch";
    public static final String PARAMETER_TABLENAME = "${tableName}";

    /**
     * 跳转到土地/林权/不动产单元匹配页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "toDataPic")
    public String toDataPicCp(Model model, @RequestParam(value = "matchTdzh", required = false) String matchTdzh, @RequestParam(value = "editFlag", required = false) String editFlag, @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt, @RequestParam(value = "showZjfzBtn", required = false) String showZjfzBtn, @RequestParam(value = "msgInfo", required = false) String msgInfo) {
        List<PfBusinessVo> pfBusinessVoList = sysWorkFlowDefineService.getBusinessList();
        List<Map> djlxList = bdcZdGlService.getDjlxByBdclx(Constants.BDCLX_TDFW);
        List<Map> sqlxList = bdcZdGlService.getSqlxByBdclxDjlx(Constants.BDCLX_TDFW, Constants.DJLX_CSDJ_DM);
        String gdTabOrder = AppConfig.getProperty("gdTab.order");
        List<String> gdTabOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(gdTabOrder) && gdTabOrder.split(",").length > 0) {
            for (String gdTab : gdTabOrder.split(","))
                gdTabOrderList.add(gdTab);
        }
        if (StringUtils.isBlank(matchTdzh))
            matchTdzh = "";
        if (StringUtils.isBlank(editFlag))
            editFlag = "";
        if (StringUtils.isBlank(filterFwPpzt))
            filterFwPpzt = "";
        if (StringUtils.isBlank(showZjfzBtn))
            showZjfzBtn = "";
        if (StringUtils.isBlank(msgInfo))
            msgInfo = "null";
        else if (StringUtils.equals(msgInfo, "0"))
            msgInfo = "插入数据成功";
        else if (StringUtils.equals(msgInfo, "badfile"))
            msgInfo = "读取文件失败";
        else if (StringUtils.equals(msgInfo, "baddata"))
            msgInfo = "插入数据失败，请检查数据";
        List<String> sqlxdmList = ReadXmlProps.getUnPpGdfwtdSqlxDm();
        String wfids = bdcZdGlService.getWdidsBySqlxdm(sqlxdmList);
        String gdTabLoadData = AppConfig.getProperty("gdTab.loadData");
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        model.addAttribute("sysVersion", sysVersion);
        model.addAttribute("gdTabOrderList", gdTabOrderList);
        model.addAttribute("gdTabOrder", gdTabOrder);
        model.addAttribute("gdTabLoadData", gdTabLoadData);
        model.addAttribute("sqlxList", sqlxList);
        model.addAttribute("matchTdzh", matchTdzh);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("filterFwPpzt", filterFwPpzt);
        model.addAttribute("showZjfzBtn", showZjfzBtn);
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("wfids", wfids);
        model.addAttribute("msgInfo", msgInfo);
        model.addAttribute("dwdm", dwdm);
        model.addAttribute("pfBusinessVoList", pfBusinessVoList);
        return "/sjgl/"+ dwdm +"/dataPic";
    }

    /**
     * 打开一个项目多个房屋匹配界面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "openMulGdFwPic")
    public String openMulGdFwPic(Model model, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "sqlxdm", required = false) String sqlxdm, @RequestParam(value = "readOnly", required = false) String readOnly) {
        List<String> sqlxdmList = ReadXmlProps.getUnPpGdfwtdSqlxDm();
        String wfids = bdcZdGlService.getWdidsBySqlxdm(sqlxdmList);
        model.addAttribute("sqlxdm", sqlxdm);
        model.addAttribute("gdproid", gdproid);
        model.addAttribute("readOnly", readOnly);
        model.addAttribute("wfids", wfids);
        model.addAttribute("qlid", qlid);
        return "/sjgl/mulGdFwPic";
    }

    /**
     * 跳转到系统日志页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "toXtLog")
    public String toXtLog(Model model) {
        return "/sjgl/xtLog";
    }

    /**
     * 获取房屋项目分页数据
     *
     * @param page
     * @param rows
     * @param sidx
     * @param sord
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdXmFwJsonByPage")
    public Object getGdXmFwJsonByPage(int page, int rows, String sidx, Integer iszx, String sord, String dah, GdFw gdFw, String gdproid, String hhSearch, String rf1Dwmc, String rf1zjh, String fczh, HttpServletRequest request, @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(gdproid))
            map.put("proid", gdproid);
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if (StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        if (StringUtils.isNotBlank(hhSearch)) {
            List<String> proidsQlr = gdXmService.getGdProidByQlr(StringUtils.deleteWhitespace(hhSearch));
            List<String> proidsFwzl = gdXmService.getGdProidByFwzl(StringUtils.deleteWhitespace(hhSearch));
            if (CollectionUtils.isNotEmpty(proidsQlr)) {
                map.put("proidsQlr", proidsQlr.toArray());
            } else if (CollectionUtils.isNotEmpty(proidsFwzl)) {
                map.put("proidsFwzl", proidsFwzl.toArray());
            } else {
                map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
            }
        }
        if (StringUtils.isNotBlank(filterFwPpzt) && filterFwPpzt.split(",").length > 0) {
            map.put("ppzts", filterFwPpzt.split(","));
        }
        map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TDFW);
        return repository.selectPaging("getGdXmFwJsonByPage", map, page - 1, rows);
    }

    /**
     * 不动产单元号分页数据
     *
     * @param page
     * @param rows
     * @param sidx
     * @param sord
     * @param qlr
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcDyhPagesJson")
    public Object getBdcDyhPagesJson(int page, int rows, String sidx, String sord, String tdids, String hhSearch, String dah, String qlr, String djh, String bdcdyh, String zl, String bdclx, @RequestParam(value = "bdcdyhs", required = false) String bdcdyhs, HttpServletRequest request) {
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(tdids)) {
            HashMap bdcdymap = new HashMap();
            bdcdymap.put(ParamsConstants.TDIDS_LOWERCASE, tdids.split(","));
            String bdcdyhsForTd = gdTdService.getBdcdyhByTdids(bdcdymap);
            if (StringUtils.isNotBlank(bdcdyhsForTd))
                map.put("bdcdyhsForTd", bdcdyhsForTd.split(","));
        }

        map.put("qlr", qlr);
        map.put("djh", djh);
        map.put("bdcdyh", bdcdyh);
        map.put("zl", zl);
        map.put("dah", dah);
        map.put(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        if (StringUtils.isNotBlank(bdcdyhs) && bdcdyhs.split(",").length > 0)
            map.put("bdcdyhs", bdcdyhs.split(","));
        if (StringUtils.isNotBlank(hhSearch)) {
            String[] djids = bdcdyService.getDjQlrIdsByQlr(StringUtils.deleteWhitespace(hhSearch), bdclx);
            if (djids != null && djids.length > 0)
                map.put("djids", djids);
        }
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if (StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        return repository.selectPaging("getDjBdcdyListByPage", map, page - 1, rows);
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
    public Object getGdLqJson(int page, int rows, String sidx, String sord, String hhSearch, GdLq gdLq, String rf1zjh, String rf1Dwmc, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if (StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        map.put("rf1Dwmc", rf1Dwmc);
        map.put("lqzh", gdLq.getLqzh());
        map.put("lqzl", gdLq.getLqzl());
        map.put("lz", gdLq.getLz());
        map.put("syqmj", gdLq.getSyqmj());
        map.put("rf1zjh", rf1zjh);
        //混合查询
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        return repository.selectPaging("getGdLqJsonByPage", map, page - 1, rows);
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
    public Object getGdCqJson(int page, int rows, String sidx, String sord, String hhSearch, GdCq gdCq, String rf1zjh, String rf1Dwmc, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if (StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        map.put("rf1Dwmc", rf1Dwmc);
        map.put("cqzh", gdCq.getCqzh());
        map.put("zl", gdCq.getZl());
        map.put("cbmj", gdCq.getCbmj());
        map.put("rf1zjh", rf1zjh);
        //混合查询
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        return repository.selectPaging("getGdCqJsonByPage", map, page - 1, rows);
    }

    /**
     * 获取土地分页数据
     *
     * @param page
     * @param rows
     * @param sidx
     * @param gdTd
     * @param sord
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGdTdJson")
    public Object getGdTdJson(int page, int rows, @RequestParam(value = "sidx", required = false) String sidx, @RequestParam(value = "sord", required = false) String sord, @RequestParam(value = "iszx", required = false) Integer iszx, @RequestParam(value = "hhSearch", required = false) String hhSearch, @RequestParam(value = "rf1Dwmc", required = false) String rf1Dwmc, @RequestParam(value = "rf1zjh", required = false) String rf1zjh, @RequestParam(value = "tdzh", required = false) String tdzh, @RequestParam(value = "gdTd", required = false) GdTd gdTd, @RequestParam(value = "tdid", required = false) String tdid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletRequest request, @RequestParam(value = "filterFwPpzt", required = false) String filterFwPpzt, @RequestParam(value = "tdids", required = false) String tdids, @RequestParam(value = "fwtdz", required = false) String fwtdz) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(hhSearch)) {
            //灌云要求增加权利人查询
            List<GdQlr> gdQlrList = gdTdService.getQlrByName(hhSearch);
            if (CollectionUtils.isNotEmpty(gdQlrList)) {
                StringBuilder sbqlid = new StringBuilder();
                for (int i = 0; i < gdQlrList.size(); i++) {
                    GdQlr gdQlr = gdQlrList.get(i);
                    sbqlid.append("'").append(gdQlr.getQlid()).append("'").append(",").append(sbqlid);
                }
                hhSearch = sbqlid.toString().substring(0, sbqlid.length() - 1);
                map.put("hhSearchQlr", hhSearch);
            } else {
                map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
            }
            //混合查询
        } else {
            map.put("tdzh", tdzh);
            if (gdTd != null) {
                map.put("zl", gdTd.getZl());
                map.put("zdmj", gdTd.getZdmj());
                map.put("djh", gdTd.getDjh());
                map.put("yt", gdTd.getYt());
            }
        }
        if (StringUtils.isNotBlank(tdid))
            map.put("tdid", tdid);

        if (StringUtils.isNotBlank(bdcdyh)) {
            List<String> dhList = gdDhDzhMapper.getAllDjh(StringUtils.substring(bdcdyh, 0, 19));
            StringBuilder queryDh = new StringBuilder();
            if (CollectionUtils.isNotEmpty(dhList)) {
                for (String dh : dhList) {
                    if (StringUtils.isNotBlank(queryDh)) {
                        queryDh.append(" or instr(djh,'").append(dh).append("' )>0");
                    }else {
                        queryDh.append("(").append("instr(djh,'").append(dh).append("' )>0");
                    }
                }
                queryDh .append(")");
            }
            if (StringUtils.isNotBlank(queryDh)) {
                map.put("djhs", queryDh);
            }
        }
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if (StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        map.put("iszx", iszx);
        if (StringUtils.isBlank(fwtdz)) {
            map.put("fwtdz", "true");
        }
        map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TD);
        if (tdids != null && StringUtils.isNotBlank(tdids) && tdids.split(",").length > 0) {
            map.put(ParamsConstants.TDIDS_LOWERCASE, tdids.split(","));
        }
        return repository.selectPaging("getGdTdXmJsonByPage", map, page - 1, rows);
    }

    /**
     * 查询项目
     */
    @ResponseBody
    @RequestMapping("/getBdcXmJson")
    public Object getBdcXmJson(int page, int rows, String hhSearch) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(PARAMETER_HHSEARCH, StringUtils.deleteWhitespace(hhSearch));
        return repository.selectPaging("getBdcXmJsonByPage", map, page - 1, rows);
    }

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 获取过渡房屋信息, 关联表gd_xm, gd_fw, gd_bdc_ql_rel,以及过渡相关权利表
     */
    @ResponseBody
    @RequestMapping("/getGdFwJson")
    public Object getGdFwJson(int page, int rows, String dah,
                              GdFw gdFw, String hhSearch,
                              String fczh, HttpServletRequest request,
                              @RequestParam(value = "gdproid", required = false) String gdproid,
                              @RequestParam(value = "qlids", required = false) String gdQlids,
                              @RequestParam(value = "checkMulGdFw", required = false) String checkMulGdFw) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("fczh", fczh);
        map.put("dah", dah);
        map.put("fwzl", gdFw.getFwzl());
        map.put("ghyt", gdFw.getGhyt());
        map.put("fwjg", gdFw.getFwjg());
        map.put("jzmj", gdFw.getJzmj());
        if (StringUtils.isNotBlank(hhSearch)) {
            map.put(PARAMETER_HHSEARCH,hhSearch);
        }

        if (StringUtils.isNotBlank(gdproid)) {
            String qlids = gdFwService.getGdFwQlidsByProid(gdproid);
            if (StringUtils.isNotBlank(qlids) && qlids.split(",").length > 0) {
                map.put(ParamsConstants.QLIDS_LOWERCASE, qlids.split(","));
            }else {
                map.put(ParamsConstants.QLIDS_LOWERCASE, "");
            }
        }
        if (StringUtils.equals(checkMulGdFw, "true")) {
            if (StringUtils.isNotBlank(gdQlids)) {
                map.put(ParamsConstants.QLIDS_LOWERCASE, gdQlids.split(","));
            } else {
                map.put(ParamsConstants.QLIDS_LOWERCASE, "");
            }
        }
        String sysVersion = AppConfig.getProperty(PARAMETER_SYS_VERSION);
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NM)) {
            String userDwdm = super.getWhereXzqdm();
            if (StringUtils.isNotBlank(userDwdm))
                map.put("dwdm", userDwdm);
        }
        return repository.selectPaging("getGdFwJsonByPage", map, page - 1, rows);
    }

    /**
     * lst  系统日志
     *
     * @param page
     * @param rows
     * @param sidx
     * @param sord
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcXtLogListByPage")
    public Object getBdcXtLogListByPage(int page, int rows, String sidx, String sord, String username, String jsrq, String qsrq, String controllerMsg, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("controllerMsg", controllerMsg);
        if (StringUtils.isNotBlank(jsrq)) {
            map.put("jsrq", CalendarUtil.formatDate(jsrq));
        }
        if (StringUtils.isNotBlank(qsrq)) {
            map.put("qsrq",  CalendarUtil.formatDate(qsrq));
        }
        return repository.selectPaging("getBdcXtLogListByPage", map, page - 1, rows);
    }

    @ResponseBody
    @RequestMapping(value = "getErrorZdxxToBz")
    public String getErrorZdxxToBz(Model model, String proid, String bdcdyh) {
        StringBuilder msg = new StringBuilder();
        List<BdcXmRel> bdcXmReltTempList = bdcXmRelService.queryBdcXmRelByProid(proid);
        if (StringUtils.isNotBlank(proid)) {
            BdcXmRel bdcXmRel = null;
            if (CollectionUtils.isNotEmpty(bdcXmReltTempList)) {
                bdcXmRel = bdcXmRelService.queryBdcXmRelByProid(proid).get(0);
            }
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);//根据不动产proid查找不动产单元
            BdcGdDyhRel gdDyhRel = null;//获取过渡单元号关系表
            if (bdcdyh != null) {
                List<BdcGdDyhRel> list = bdcGdDyhRelService.getGdDyhRel(bdcdyh.replace(" ", ""), "");
                if (list != null) {
                    gdDyhRel = list.get(0);
                }
            }
            if (bdcXmRel != null && gdDyhRel != null && (bdcXm == null || (bdcXm != null && StringUtils.isBlank(bdcXm.getBz())))) {
                List<String> list = null;
                if (StringUtils.isNotBlank(bdcXmRel.getYdjxmly()) && Constants.XMLY_FWSP.equals(bdcXmRel.getYdjxmly())) {
                    list = bdcCheckCancelService.getGdFwErrorZdsjByFwid(gdDyhRel.getGdid());
                    List<GdFwsyq> gdFwsyqList = gdFwService.queryGdFwsyqByGdproid(bdcXmRel.getYproid());
                    if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                        if (list == null)
                            list = new ArrayList<String>();
                        for (GdFwsyq gdFwsyq : gdFwsyqList) {
                            if (gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getQlid())) {
                                List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(gdFwsyq.getQlid(), Constants.QLRLX_QLR);
                                if (CollectionUtils.isNotEmpty(gdQlrList)) {
                                    int i = 0;
                                    for (GdQlr gdQlr : gdQlrList) {
                                        if (gdQlr != null && StringUtils.isNotBlank(gdQlr.getQlbl())) {
                                            if (i == 0)
                                                list.add("权利人共有情况：");
                                            list.add(gdQlr.getQlr() + PublicUtil.percentage(gdQlr.getQlbl()));
                                        }
                                        i++;
                                    }
                                }
                            }
                        }
                    }
                    if (list == null)
                        list = new ArrayList<String>();
                    if (StringUtils.isNotBlank(gdDyhRel.getTdid()) && StringUtils.isNotBlank(bdcXmRel.getYproid()))
                        list.addAll(bdcCheckCancelService.getGdTdErrorZdsjByTdid(gdDyhRel.getTdid(), Constants.BDCLX_TDFW));

                } else if (Constants.XMLY_TDSP.equals(bdcXmRel.getYdjxmly())) {
                    if (CollectionUtils.isEmpty(list))
                        list = new ArrayList<String>();
                    if (StringUtils.isNotBlank(gdDyhRel.getGdid()) && StringUtils.isNotBlank(bdcXmRel.getYproid()))
                        list.addAll(bdcCheckCancelService.getGdTdErrorZdsjByTdid(gdDyhRel.getGdid(), Constants.BDCLX_TD));

                }
                if (CollectionUtils.isNotEmpty(list)) {
                    msg.append("原不动产信息: ");
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0 || i == 1)
                            msg.append(list.get(i));
                        else
                            msg.append(",").append(list.get(i));
                    }
                }
            }
        }
        return msg.toString();
    }

    /**
     * 匹配不动产单元和房产证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryBdcdyhByDahAndFwid")
    public List<?> queryBdcdyhByDah(Model model, String dah, String fwid) {
        List<?> list = new ArrayList<HashMap>();
        if (StringUtils.isNotBlank(fwid))
            list = bdcGdDyhRelService.getGdDyhRelByGdId(fwid);
        if (CollectionUtils.isEmpty(list) && StringUtils.isNotBlank(dah)) {
            HashMap parmMap = new HashMap();
            parmMap.put("fcdah", dah);
            list = bdcdyService.queryBdcdyhByDah(parmMap);
        }
        //如果没值上面的entityMapper会返回null
        if (list == null) {
            list = new ArrayList<HashMap>();
        }
        return list;
    }

    /**
     * 匹配房产证和土地证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryTdByFwid")
    public List<?> queryTdByFwid(Model model, @RequestParam(value = "fwid", required = false) String fwid, @RequestParam(value = "dah", required = false) String dah) {
        List<?> list = new ArrayList<HashMap>();
        if (StringUtils.isNotBlank(fwid)) {
            list = bdcGdDyhRelService.getGdDyhRelByGdId(fwid);
        }
        if (CollectionUtils.isEmpty(list)) {
            if (StringUtils.isNotBlank(dah))
                list = gdTdService.queryTdsyqByFwid(dah);
            else {
                GdFw gdFw = gdFwService.queryGdFw(fwid);
                dah = gdFw.getDah();
                list = gdTdService.queryTdsyqByFwid(dah);
            }
        }

        //如果没值上面的entityMapper会返回null
        if (list == null) {
            list = new ArrayList<HashMap>();
        }
        return list;
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
    public List<Map> getSqlxByBdclxDjlx(Model model, String djlx, String bdclx) {
        String djlxdm = bdcZdGlService.getDjlxDmByMc(djlx);
        return bdcZdGlService.getSqlxByBdclxDjlx(bdclx, djlxdm);
    }

    /**
     * 根据不动产单元类型获取登记类型
     *
     * @param bdclx
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDjlxByBdclx")
    public List<Map> getDjlxByBdclx(Model model, String bdclx) {
        return bdcZdGlService.getDjlxByBdclx(bdclx);
    }

    @ResponseBody
    @RequestMapping(value = "/getPpzt")
    public String getPpzt(@RequestParam(value = "gdproid", required = false) String gdproid) {
        String ppzt = "";
        if (gdproid != null) {
            GdXm gdXm = gdXmService.getGdXm(gdproid);
            if (gdXm != null) {
                ppzt = gdXm.getPpzt();
            }
        }
        return ppzt;
    }


    //    /**
    //     * 删除项目关联
    //     */
    @ResponseBody
    @RequestMapping("/deletexmgl")
    public HashMap<String, Object> deletexmgl(String gdproid) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String msg = gdXmService.deleteXmgl(gdproid, "", "", "");
        if (StringUtils.equals(msg, "删除成功")) {
            gdXmService.updateGdxmPpzt(gdproid, Constants.GD_XM_YPPWFZ);
        }
        resultMap.put(ParamsConstants.RESULT_LOWERCASE, msg);
        return resultMap;
    }

    /**
     * 删除过度项目信息
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "deleteGdXm")
    public String deleteGdXm(String proid) {
        String msg = "删除失败";
        try {
            msg = gdXmService.deleteGdXm(proid);
        } catch (Exception e) {
            logger.error("BdcSjglConntroller.deleteGdXm",e);
        }
        return msg;
    }

    /**
     * 注销权利
     */
    @ResponseBody
    @RequestMapping(value = "zxQl")
    public String zxQl(String qlid) {
        return dSjglService.zxQl(qlid,super.getUserId());
    }

    /**
     * 解除注销权利
     */
    @ResponseBody
    @RequestMapping(value = "jczxQl")
    public String jczxQl(String qlid) {
        return dSjglService.jczxQl(qlid);
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
        if (StringUtils.isNotBlank(gdid)) {
            list = bdcGdDyhRelService.getGdDyhRel("", gdid);
        }
        //如果没值上面的entityMapper会返回null
        if (list == null) {
            list = new ArrayList<HashMap>();
        }
        return list;
    }


    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 根据是否在gd_dyh_rel表中有记录判断是否匹配
     */
    @ResponseBody
    @RequestMapping(value = "judgeIsPp")
    public HashMap<String, Boolean> judgeIsPp(String gdId) {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        List<BdcGdDyhRel> list = new ArrayList<BdcGdDyhRel>();
        if (StringUtils.isNotBlank(gdId)) {
            list = bdcGdDyhRelService.getGdDyhRelByGdId(gdId);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            map.put("isHavePp", true);
        } else {
            map.put("isHavePp", false);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/getBdcdys")
    public String[] getBdcdys(String tdids) {
        String[] bdcdys = null;
        if (StringUtils.isNotBlank(tdids)) {
            HashMap bdcdymap = new HashMap();
            bdcdymap.put(ParamsConstants.TDIDS_LOWERCASE, tdids.split(","));
            String bdcdyhsForTd = gdTdService.getBdcdyhByTdids(bdcdymap);
            if (StringUtils.isNotBlank(bdcdyhsForTd))
                bdcdys = bdcdyhsForTd.split(",");
        }
        return bdcdys;
    }

    /**
     * 土地匹配不动产单元
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryBdcdyhByTdidDjh")
    public List<?> queryBdcdyhByTdid(Model model, String gdproid, String djh) {
        List<?> list = new ArrayList<HashMap>();
        if (StringUtils.isNotBlank(gdproid))
            list = bdcGdDyhRelService.getGdDyhRelByGdId(gdproid);
        if (CollectionUtils.isEmpty(list) && StringUtils.isNotBlank(djh)) {
            String newDjh = gdDhDzhMapper.getNewDhByOldDh(djh);
            if (StringUtils.isBlank(newDjh))
                newDjh = djh;
            if (StringUtils.isNotBlank(newDjh)) {
                HashMap map = new HashMap();
                map.put("djh", newDjh);
                map.put(ParamsConstants.BDCLX_LOWERCASE, Constants.BDCLX_TD);
                List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                if (CollectionUtils.isNotEmpty(bdcdyList)) {
                    List<BdcGdDyhRel> bdcGdDyhRelList = new ArrayList<BdcGdDyhRel>();
                    BdcGdDyhRel bdcGdDyhRel = new BdcGdDyhRel();
                    bdcGdDyhRel.setBdcdyh(CommonUtil.formatEmptyValue(bdcdyList.get(0).get("BDCDYH")));
                    bdcGdDyhRelList.add(bdcGdDyhRel);
                    list = bdcGdDyhRelList;
                }
            }
        }
        //如果没值上面的entityMapper会返回null
        if (list == null) {
            list = new ArrayList<HashMap>();
        }
        return list;
    }

    /**
     * 匹配不动产单元和房产项目
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkGdfwNum")
    public HashMap checkGdfwNum(Model model, String gdproid, String isck) {
        HashMap checkMsgMap = new HashMap();
        //是否存在一个项目多个房屋
        String mulGdfw = "false";
        StringBuilder stringBuilder = new StringBuilder();
        //是否已经匹配
        String hasPic = "true";
        if (StringUtils.isNotBlank(gdproid)) {
            List<GdFw> gdFwList = gdFwService.getGdFwByProidForCheckFwDz(gdproid, isck);
            List<String> dahList = null;
            List<String> fwidList = null;
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                dahList = Lists.newArrayList();
                fwidList = Lists.newArrayList();
                for (GdFw gdFw : gdFwList) {
                    if (!dahList.contains(gdFw.getDah())) {
                        dahList.add(gdFw.getDah());
                        fwidList.add(gdFw.getFwid());
                    }
                }
            }
            if (dahList != null && dahList.size() > 1) {
                mulGdfw = "true";
            }
            if(CollectionUtils.isNotEmpty(gdFwList)){
                for(GdFw gdFw:gdFwList){
                    List<BdcGdDyhRel> gdDyhRelList = bdcGdDyhRelService.getGdDyhRel("", gdFw.getFwid());
                    if (CollectionUtils.isEmpty(gdDyhRelList)) {
                        hasPic = "false";
                        break;
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(fwidList) && !fwidList.isEmpty()) {
                stringBuilder.append(StringUtils.join(fwidList, ","));
            }

        }
        checkMsgMap.put("mulGdfw", mulGdfw);
        checkMsgMap.put("hasPic", hasPic);
        checkMsgMap.put("fwid", stringBuilder);
        return checkMsgMap;
    }

    /**
     * 获取过渡房产登记对应的不动产申请类型
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getGdFcDjlxToSqlxWfid")
    public HashMap getGdFcDjlxToSqlxWfid(Model model, @RequestParam(value = "djlx", required = false) String djlx) {
        HashMap map = new HashMap();
        String wfid = "";
        String busiName = "";
        if (StringUtils.isNotBlank(djlx)) {
            wfid = bdcZdGlService.getGdFcDjlxToSqlxWdid(djlx);
            if (StringUtils.isNotBlank(wfid)) {
                busiName = PlatformUtil.getBusinessNameByWfid(wfid);
            }
        }
        map.put("wfid", wfid);
        map.put("busiName", busiName);
        return map;
    }

    /**
     * 匹配不动产单元和房产项目
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryBdcdyhByGdProid")
    public List<?> queryBdcdyhByGdProid(Model model, String gdproid, String isck) {
        List<?> list = new ArrayList<HashMap>();
        if (StringUtils.isNotBlank(gdproid)) {
            List<GdFw> gdFwList = gdFwService.getGdFwByProid(gdproid, "");
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                List<BdcGdDyhRel> bdcGdDyhRels = new ArrayList<BdcGdDyhRel>();
                for (GdFw gdFw : gdFwList) {
                    List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdFw.getFwid());
                    if (CollectionUtils.isNotEmpty(bdcGdDyhRelList))
                        bdcGdDyhRels.addAll(bdcGdDyhRelList);
                }
                if (CollectionUtils.isNotEmpty(bdcGdDyhRels))
                    list = bdcGdDyhRels;
            }
        }
        if (CollectionUtils.isEmpty(list) && StringUtils.isNotBlank(gdproid)) {
            String dahs = gdFwService.getGdFwDahsByProid(gdproid);
            if (StringUtils.isNotBlank(dahs) && dahs.split(",").length > 0) {
                HashMap parmMap = new HashMap();
                parmMap.put("fcdahs", dahs.split(","));
                list = bdcdyService.queryBdcdyhByDah(parmMap);
            }
        }
        //如果没值上面的entityMapper会返回null
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<HashMap>();
        }
        return list;
    }

    /**
     * 根据过渡房屋项目id匹配房产证和土地证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryTdByGdproid")
    public List<?> queryTdByGdproid(Model model, @RequestParam(value = "gdproid", required = false) String gdproid) {
        List<?> list = new ArrayList<HashMap>();
        if (StringUtils.isNotBlank(gdproid)) {
            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdproid(gdproid);
            if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                List<GdQlDyhRel> gdQlDyhRels = new ArrayList<GdQlDyhRel>();
                for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                    List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(bdcGdDyhRel.getBdcdyh(), "", "");
                    if (CollectionUtils.isNotEmpty(gdQlDyhRelList))
                        gdQlDyhRels.addAll(gdQlDyhRelList);
                }
                if (CollectionUtils.isNotEmpty(gdQlDyhRels))
                    list = gdQlDyhRels;
            }
            if (CollectionUtils.isEmpty(list))
                list = bdcGdDyhRelList;
        }

        if (CollectionUtils.isEmpty(list)&&StringUtils.isNotBlank(gdproid)) {
            list = gdFwService.queryTdsyqByGdproid(gdproid);
        }

        //如果没值上面的entityMapper会返回null
        if (list == null) {
            list = new ArrayList<HashMap>();
        }
        return list;
    }

    /**
     * 匹配不动产单元和房产证
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "matchData")
    public HashMap matchData(Model model, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "djId", required = false) String djId, @RequestParam(value = "gdid", required = false) String gdid, @RequestParam(value = "tdid", required = false) String tdid, @RequestParam(value = "fwid", required = false) String fwid, @RequestParam(value = "ppzt", required = false) String ppzt) {
        HashMap result = new HashMap();
        String msg = "匹配成功";
        try {
            if (StringUtils.isNotBlank(bdcdyh) && !bdcdyh.equals("undefined")) {
                BdcGdDyhRel bdcGdDyhRel = new BdcGdDyhRel();
                if (StringUtils.isNotBlank(fwid)) {
                    bdcGdDyhRel.setGdid(fwid);
                }else {
                    bdcGdDyhRel.setGdid(gdid);
                }
                bdcGdDyhRel.setBdcdyh(bdcdyh);
                bdcGdDyhRel.setDjid(djId);
                bdcGdDyhRel.setTdid(tdid);
                bdcGdDyhRelService.addGdDyhRel(bdcGdDyhRel);
                gdXmService.updateGdxmPpzt(gdid, ppzt);
            }
        } catch (Exception e) {
            msg = "匹配失败";
            logger.error("BdcSjglConntroller.matchData",e);
        } finally {
            result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        }
        return result;
    }


    /*
   * zwq 取消匹配
   * */
    @ResponseBody
    @RequestMapping(value = "dismatch", method = RequestMethod.POST)
    public String dismatch(@RequestParam(value = "gdid", required = false) String gdid, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "tdid", required = false) String tdid, @RequestParam(value = "cqid", required = false) String cqid, @RequestParam(value = "lqid", required = false) String lqid, @RequestParam(value = "gdtab", required = false) String gdtab, @RequestParam(value = "fwid", required = false) String fwid) {
        //删除过渡关联表
        String msg = "error";
        //删除对应匹配记录(林权草权没管)
        try{
            List<String> qlids = new ArrayList<String>();
            Example example = new Example(BdcGdDyhRel.class);
            Example.Criteria criteria = example.createCriteria();

            if(StringUtils.isNotBlank(gdtab) && gdtab.equals("fw")){
                if(StringUtils.isNotBlank(fwid)){
                    String[] fwids = {};
                    if (StringUtils.isNotBlank(fwid)) {
                        fwids = fwid.split(",");
                    }
                    if(fwids.length > 0){
                        for(String fwidTemp : fwids){
                            example = new Example(BdcGdDyhRel.class);
                            criteria = example.createCriteria();
                            if (StringUtils.isNotBlank(fwidTemp)){
                                criteria.andEqualTo("gdid", fwidTemp);
                            }
                            if(CollectionUtils.isNotEmpty( example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria())){
                                List<BdcGdDyhRel> gdDyhRelListTmp = entityMapper.selectByExample(BdcGdDyhRel.class,example);
                                if(CollectionUtils.isNotEmpty(gdDyhRelListTmp)){
                                    for(BdcGdDyhRel bdcGdDyhRel:gdDyhRelListTmp){
                                        gdSaveLogSecvice.gdDyhRelQxppLog(bdcGdDyhRel);
                                    }
                                }
                                entityMapper.deleteByExample(BdcGdDyhRel.class, example);
                            }
                            if(StringUtils.isNotBlank(fwidTemp)){
                                example = new Example(GdBdcQlRel.class);
                                criteria = example.createCriteria();
                                criteria.andEqualTo(ParamsConstants.BDCID_LOWERCASE,fwidTemp);
                                List<GdBdcQlRel> gdBdcQlRelLst = entityMapper.selectByExample(example);
                                if(CollectionUtils.isNotEmpty(gdBdcQlRelLst)){
                                    for(GdBdcQlRel gdqlRel : gdBdcQlRelLst){
                                        example = new Example(GdQlDyhRel.class);
                                        criteria = example.createCriteria();
                                        criteria.andEqualTo("qlid",gdqlRel.getQlid());
                                        qlids.add(gdqlRel.getQlid());
                                        List<GdQlDyhRel> gdQlDyhRelList = entityMapper.selectByExample(GdQlDyhRel.class,example);
                                        if(CollectionUtils.isNotEmpty(gdQlDyhRelList)){
                                            for(GdQlDyhRel gdQlDyhRel:gdQlDyhRelList){
                                                gdSaveLogSecvice.gdQlDyhRelQxppLog(gdQlDyhRel);
                                            }
                                        }
                                        entityMapper.deleteByExample(GdQlDyhRel.class,example);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else  if(StringUtils.isNotBlank(gdtab) && gdtab.equals("td") && StringUtils.isNotBlank(tdid)){
                /**
                 * @author bianwen
                 * @description 对于过渡土地，传的tdid和gdid是同一个值
                 */
                criteria.andEqualTo("gdid", gdid);
                List<BdcGdDyhRel> gdDyhRelListTmp = entityMapper.selectByExample(BdcGdDyhRel.class,example);
                if(CollectionUtils.isNotEmpty(gdDyhRelListTmp)){
                    for(BdcGdDyhRel bdcGdDyhRel:gdDyhRelListTmp){
                        gdSaveLogSecvice.gdDyhRelQxppLog(bdcGdDyhRel);
                    }
                }
                entityMapper.deleteByExample(BdcGdDyhRel.class, example);

                example = new Example(GdBdcQlRel.class);
                criteria = example.createCriteria();
                criteria.andEqualTo(ParamsConstants.BDCID_LOWERCASE,tdid);
                List<GdBdcQlRel> gdBdcQlRelLst = entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(gdBdcQlRelLst)){
                    for(GdBdcQlRel gdqlRel : gdBdcQlRelLst){
                        example = new Example(GdQlDyhRel.class);
                        criteria = example.createCriteria();
                        criteria.andEqualTo("qlid",gdqlRel.getQlid());
                        qlids.add(gdqlRel.getQlid());
                        List<GdQlDyhRel> gdQlDyhRelList = entityMapper.selectByExample(GdQlDyhRel.class,example);
                        if(CollectionUtils.isNotEmpty(gdQlDyhRelList)){
                            for(GdQlDyhRel gdQlDyhRel:gdQlDyhRelList){
                                gdSaveLogSecvice.gdQlDyhRelQxppLog(gdQlDyhRel);
                            }
                        }
                        entityMapper.deleteByExample(GdQlDyhRel.class,example);
                    }
                }
            }else  if(StringUtils.isNotBlank(gdtab) && gdtab.equals("lq") && StringUtils.isNotBlank(lqid)){
                /**
                 * @author bianwen
                 * @description 对于过渡土地，传的tdid和gdid是同一个值
                 */
                criteria.andEqualTo("gdid", gdid);
                List<BdcGdDyhRel> gdDyhRelListTmp = entityMapper.selectByExample(BdcGdDyhRel.class,example);
                if(CollectionUtils.isNotEmpty(gdDyhRelListTmp)){
                    for(BdcGdDyhRel bdcGdDyhRel:gdDyhRelListTmp){
                        gdSaveLogSecvice.gdDyhRelQxppLog(bdcGdDyhRel);
                    }
                }
                entityMapper.deleteByExample(BdcGdDyhRel.class, example);

                example = new Example(GdBdcQlRel.class);
                criteria = example.createCriteria();
                criteria.andEqualTo(ParamsConstants.BDCID_LOWERCASE,tdid);
                List<GdBdcQlRel> gdBdcQlRelLst = entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(gdBdcQlRelLst)){
                    for(GdBdcQlRel gdqlRel : gdBdcQlRelLst){
                        example = new Example(GdQlDyhRel.class);
                        criteria = example.createCriteria();
                        criteria.andEqualTo("qlid",gdqlRel.getQlid());
                        qlids.add(gdqlRel.getQlid());
                        List<GdQlDyhRel> gdQlDyhRelList = entityMapper.selectByExample(GdQlDyhRel.class,example);
                        if(CollectionUtils.isNotEmpty(gdQlDyhRelList)){
                            for(GdQlDyhRel gdQlDyhRel:gdQlDyhRelList){
                                gdSaveLogSecvice.gdQlDyhRelQxppLog(gdQlDyhRel);
                            }
                        }
                        entityMapper.deleteByExample(GdQlDyhRel.class,example);
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(qlids)){
                //hqz根据bdc权利对应的项目proid集合批量更新状态
                gdXmService.updateGdxmPpztByQlids(qlids, Constants.GD_PPZT_WPP);
            }
            msg="success";
        }
        catch (Exception e){
           logger.error("",e);
        }
        return msg;

    }


    /**
     * 获取原地籍号
     *
     * @param model
     * @param djid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQlrByDjid")
    public HashMap<String, Object> getQlrByDjid(Model model, String djid, String bdclxdm, @RequestParam(value = "zdtzm", required = false) String zdtzm) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String qlr = "";
        if (StringUtils.isNotBlank(djid))
            qlr = bdcdyService.getDjQlrByDjid(djid, bdclxdm, zdtzm);
        resultMap.put("qlr", qlr);
        return resultMap;
    }

    /**
     * 删除项目
     *
     * @param model
     * @param ydjh
     * @return
     */
    @ResponseBody
    @RequestMapping("/getDjhByYdjh")
    public HashMap<String, Object> getDjhByYdjh(Model model, String ydjh) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        String djh = gdDhDzhMapper.getNewDhByOldDh(ydjh);
        resultMap.put("djh", djh);
        return resultMap;
    }

    /**
     * lst 获取抵押预告查封状态
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDyYgCfStatus")
    public HashMap getDyYgCfStatus(Model model, String bdcid, @RequestParam(value = "dyid", required = false) String dyid) {
        HashMap resultMap = new HashMap();
        //zwq 土地证书获取状态
        String xx = gdTdService.getTdzsZt(bdcid, dyid);
        // true 正常  false 查封
        if (StringUtils.indexOf(xx, "查封") > -1) {
            resultMap.put("cf", false);
        } else {
            resultMap.put("cf", true);
        }
        // true 正常  false 抵押
        if (StringUtils.indexOf(xx, "抵押") > -1) {
            resultMap.put("dy", false);
        } else {
            resultMap.put("dy", true);
        }
        // true 正常  false 预告
        if (StringUtils.indexOf(xx, "预告") > -1) {
            resultMap.put("yg", false);
        } else {
            resultMap.put("yg", true);
        }

        if (StringUtils.indexOf(xx, "注销") > -1) {
            resultMap.put("dyZx", false);
        } else {
            resultMap.put("dyZx", true);
        }
        return resultMap;
    }

    //验证过渡关联的信息与收件单是否相符
    @ResponseBody
    @RequestMapping(value = "yzGlxmxx", method = RequestMethod.GET)
    public boolean yzGlxmxx(@RequestParam(value = "zl", required = false) String zl, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qlr", required = false) String qlr) {
        //多个权利人是用‘/’分隔符分开
        Boolean pd = false;
        BdcXm bdcXm = null;
        List<String> qlrList = null;

        List<BdcQlr> bdcQlrList = null;
        if (StringUtils.isNotBlank(qlr)) {
            qlrList = Arrays.asList(qlr.split("/"));
        }
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
            bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
        }
        if (bdcXm != null) {
            if (StringUtils.equals(bdcXm.getZl(), zl)) {
                pd = true;
            } else {
                return false;
            }
            if (CollectionUtils.isEmpty(bdcQlrList) && CollectionUtils.isEmpty(qlrList)) {
                pd = true;
            } else if (CollectionUtils.isNotEmpty(bdcQlrList) && CollectionUtils.isNotEmpty(qlrList)
                    &&bdcQlrList.size() == qlrList.size()) {
                for (BdcQlr xmqlr : bdcQlrList) {
                    for (String gdqlr : qlrList) {
                        if (StringUtils.equals(xmqlr.getQlrmc(), gdqlr)) {
                            pd = true;
                        } else {
                            pd = false;
                            break;
                        }
                    }

                }
            }
        }
        return pd;
    }

    @ResponseBody
    @RequestMapping("/glxm")
    public Project glxm(String proid, Project project, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "lx", required = false) String lx, @RequestParam(value = "gdFwWay", required = false) String gdFwWay, @RequestParam(value = "step", required = false) String step) {
        //sc 考虑项目内多幢
        if (StringUtils.isBlank(project.getBdcdyh()) && CollectionUtils.isNotEmpty(project.getBdcdyhs())
                &&StringUtils.split(project.getBdcdyhs().get(0), Constants.SPLIT_STR).length == 1) {
            project.setBdcdyh(project.getBdcdyhs().get(0));
        }
        if (StringUtils.isBlank(project.getDjId()) && CollectionUtils.isNotEmpty(project.getDjIds())
                &&StringUtils.split(project.getDjIds().get(0), Constants.SPLIT_STR).length == 1) {
            project.setDjId(project.getDjIds().get(0));
        }
        if (StringUtils.isNotBlank(project.getGdproid())) {
            String bdcdys = gdXmService.getBdcdyhsByGdProid(project.getGdproid());
            if (StringUtils.isNotBlank(bdcdys)) {
                List<String> bdcdyList = new ArrayList<String>();
                bdcdyList.add(bdcdys);
                project.setBdcdyhs(bdcdyList);
            }
        }

        //zhouwanqing通过gdproid获取yqlid,yqlid用于权利人获取
        if (StringUtils.isNotBlank(project.getGdproid())) {
            String qlid = gdXmService.getQlidByGdproid(project.getGdproid());
            project.setYqlid(qlid);
        }

        if (StringUtils.isNotBlank(lx)) {
            project.setBdclx(lx);
        } else {
            project.setBdclx(Constants.BDCLX_TDFW);
        }

        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
            project.setXmly(Constants.XMLY_FWSP);
        } else {
            project.setXmly(Constants.XMLY_TDSP);
        }
        //这边是为了区分匹配创建还是关联创建
        project.setMsg("gl");
        entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
        project.setBh(bdcXm.getBh());
        project.setDjlx(bdcXm.getDjlx());
        project.setQllx(bdcXm.getQllx());
        project.setSqlx(bdcXm.getSqlx());
        project.setGdproid(gdproid);
        project.setProid(proid);
        CreatProjectService creatProjectService = projectService.getCreatProjectService(project);
        List<InsertVo> list = creatProjectService.initVoFromOldData(project);
        creatProjectService.insertProjectData(list);
        //防止返回消息出现"gl"
        if (StringUtils.equals(project.getMsg(), "gl"))
            project.setMsg(null);

        bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        //        更新remark和工程名称
        creatProjectService.updateWorkFlow(bdcXm);
        TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);

        PfWorkFlowInstanceVo pf = super.getWorkFlowInstance(proid);

        SysTaskService sysTaskService = (SysTaskService) Container.getBean("TaskService");
        List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pf.getWorkflowIntanceId());
        if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
            project.setTaskid(pfTaskVoList.get(0).getTaskId());
        }
        turnProjectDefaultService.saveQllxVo(bdcXm);
        gdXmService.updateGdxmPpzt(gdproid, Constants.GD_XM_YPPZZFZ);
        return project;
    }

    /**
     * zx 获取房屋信息
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getGdXmPpzt")
    public HashMap getGdXmPpzt(Model model, String proid, String bdclx) {
        HashMap resultMap = new HashMap();
        if (StringUtils.isNotBlank(proid)) {
            String ppzt = gdFwService.getPpztByQlid(proid, Constants.BDCLX_TDFW, "");
            resultMap.put("ppzt", ppzt);
        }
        return resultMap;
    }

    /**
     * @param proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description
     */
    @ResponseBody
    @RequestMapping(value = "getGdFwxx")
    public HashMap getGdFwxx(String proid) {
        HashMap resultMap = new HashMap();
        if (StringUtils.isNotBlank(proid)) {
            String qlr = gdQlrService.getGdQlrsByProid(proid, Constants.QLRLX_QLR);
            String zl = gdXmService.getGdXmZLByGdProid(proid);
            if (StringUtils.isNotBlank(qlr))
                resultMap.put("qlr", qlr);
            if (StringUtils.isNotBlank(zl))
                resultMap.put("zl", zl);
        }
        return resultMap;
    }

    /**
     * lst 获取要注销的项目的权利状态 判断是否可注销
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "isCancel")
    public HashMap isCancel(Model model, Project project, String sqlxMc, String lx, @RequestParam(value = "gdFwWay", required = false) String gdFwWay) {
        HashMap resultMap = new HashMap();
        boolean isCancel = true;
        String msg = "";
        String checkModel = "";
        //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断
        if (StringUtils.isBlank(project.getQllx())) {
            String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(qllxdm);
        }
        //获取权利类型和登记事由、申请类型
        if (StringUtils.isNotBlank(sqlxMc)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
            if (CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                if (StringUtils.isNotBlank(project.getQllx())) {
                    String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
                    project.setQllx(qllxdm);
                } else {
                    if (map.get(ParamsConstants.QLLXDM_CAPITAL) != null)
                        project.setQllx(map.get(ParamsConstants.QLLXDM_CAPITAL).toString());
                }


                if (map.get(ParamsConstants.SQLXDM_CAPITAL) != null)
                    project.setSqlx(map.get(ParamsConstants.SQLXDM_CAPITAL).toString());
                if (StringUtils.equals(lx, Constants.BDCLX_TDFW))
                    project.setXmly(Constants.XMLY_FWSP);
                else if (StringUtils.equals(lx, Constants.BDCLX_TD))
                    project.setXmly(Constants.XMLY_TDSP);
                if (StringUtils.isBlank(project.getBdclx()))
                    project.setBdclx(lx);
            }
        }
        if (StringUtils.isNotBlank(project.getSqlx())) {
            String yqllxdm = bdcZdGlService.getYqllxBySqlx(project.getSqlx());
            if (StringUtils.equals(lx, "TDFW")) {
                isCancel = bdcCheckCancelService.checkCancel(project.getSqlx(), yqllxdm, project.getDjlx(), project.getGdproid());
            }
            //ZDD 只验证alert的
            List<Map<String, Object>> checkMsg = null;
            if (isCancel) {
                checkMsg = projectCheckInfoService.checkXmByProject(project);
                if (checkMsg != null && checkMsg.size() > 0) {
                    for (Map<String, Object> map : checkMsg) {
                        if (map.containsKey(ParamsConstants.CHECKMODEL_HUMP) && map.get(ParamsConstants.CHECKMODEL_HUMP).toString().toUpperCase().equals(ParamsConstants.ALERT_CAPITAL)) {
                            isCancel = false;
                            checkModel = ParamsConstants.ALERT_CAPITAL;
                            msg = CommonUtil.formatEmptyValue(map.get(ParamsConstants.CHECKMSG_HUMP));
                            break;
                        }
                    }
                    if (isCancel) {
                        for (Map<String, Object> map : checkMsg) {
                            if (map.containsKey(ParamsConstants.CHECKMODEL_HUMP) && map.get(ParamsConstants.CHECKMODEL_HUMP).toString().toUpperCase().equals("CONFIRM")) {
                                isCancel = false;
                                checkModel = "CONFIRM";
                                if (StringUtils.isBlank(msg))
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
        }
        return resultMap;
    }


    @ResponseBody
    @RequestMapping(value = "creatCsdj")
    public Project creatCsdj(Model model, Project project, String sqlxMc, String lx, @RequestParam(value = "dyid", required = false) String dyid, String tdids, @RequestParam(value = "ygid", required = false) String ygid, @RequestParam(value = "cfid", required = false) String cfid, @RequestParam(value = "yyid", required = false) String yyid, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdFwWay", required = false) String gdFwWay, @RequestParam(value = "step", required = false) String step, @RequestParam(value = "tdid", required = false) String gdid) {
        Project returnProject = null;
        String proid= UUIDGenerator.generate18();
        if (StringUtils.isNotBlank(lx)) {
            project.setBdclx(lx);
        } else {
            project.setBdclx(Constants.BDCLX_TDFW);
        }
        if (StringUtils.isNotBlank(tdids))
            project.setTdids(tdids);
        project.setUserId(super.getUserId());
       String gdproids=project.getGdproid();
        if (StringUtils.isBlank(gdproids) && StringUtils.isNotBlank(gdproid)){
            gdproids = gdproid;
        }
        //jyl 根据工作流wiid  查找是否已经存在  如果存在 则删除对应的记录
        if (StringUtils.isNotBlank(project.getWiid())) {
            List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(xmList)) {
                for (BdcXm bdcXm : xmList) {
                    delProjectDefaultServiceImpl.delBdcBdxx(bdcXm);
                    delProjectDefaultServiceImpl.delBdcXm(bdcXm.getProid());
                }
            }
        }
        //zq 初始化批量的过渡参数，转换为bdcxmrel对象
        projectService.initGdDataToBdcXmRel(project, gdproids, "", lx);
        List<BdcXmRel> bdcXmRelList = null;
        bdcXmRelList = project.getBdcXmRelList();
        //zhengqi按照bdcxmrel循环批量初始化数据
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmReltemp : bdcXmRelList) {
                //jyl组织单个不动产单元信息的project
                List<BdcXmRel> tempbdcXmRelList = new ArrayList<BdcXmRel>();
                project.setProid(proid);
                tempbdcXmRelList.add(bdcXmReltemp);
                project.setBdcXmRelList(tempbdcXmRelList);
                project.setYqlid(bdcXmReltemp.getYqlid());
                project.setGdproid(bdcXmReltemp.getYproid());
                if (StringUtils.isNotBlank(bdcXmReltemp.getQjid())) {
                    project.setDjId(bdcXmReltemp.getQjid());
                    DjsjBdcdy djsjBdcdy = djsjFwService.getDjsjBdcdyByDjid(bdcXmReltemp.getQjid());
                    if (djsjBdcdy != null && StringUtils.isNotBlank(djsjBdcdy.getBdcdyh()) && StringUtils.isBlank(project.getBdcdyh())) {
                        project.setBdcdyh(djsjBdcdy.getBdcdyh());
                        project.setBdcdyhs(null);
                    }
                }
                //获取权利类型和登记事由、申请类型
                if (StringUtils.isNotBlank(sqlxMc)) {
                    List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
                    if (CollectionUtils.isNotEmpty(mapList)) {
                        Map map = mapList.get(0);
                        String djsy = "";
                        if (map.get(ParamsConstants.QLLXDM_CAPITAL) != null)
                            project.setQllx(map.get(ParamsConstants.QLLXDM_CAPITAL).toString());
                        if (map.get(ParamsConstants.SQLXDM_CAPITAL) != null) {
                            project.setSqlx(map.get(ParamsConstants.SQLXDM_CAPITAL).toString());
                            djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                        }
                        if (StringUtils.isNotBlank(djsy)) {
                            project.setDjsy(djsy);
                        } else {
                            if (map.get("DJSYDM") != null)
                                project.setDjsy(map.get("DJSYDM").toString());
                        }
                    }
                }
                //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断
                if (StringUtils.isBlank(project.getQllx())) {
                    String bdcdyh = "";
                    if (StringUtils.isNotBlank(project.getBdcdyh())) {
                        bdcdyh = project.getBdcdyh();
                    } else if (CollectionUtils.isNotEmpty(project.getBdcdyhs())&&project.getBdcdyhs().get(0).split(Constants.SPLIT_STR).length > 0) {
                        bdcdyh = project.getBdcdyhs().get(0).split(Constants.SPLIT_STR)[0];
                    }
                    String qllxdm = bdcdyService.getQllxFormBdcdy(bdcdyh);
                    project.setQllx(qllxdm);
                }
                //lj 遗失补发换证登记，通过过渡证书类型来判断权利类型
                if ((project.getSqlx().equals(Constants.SQLX_YSBZ_DM)
                        || project.getSqlx().equals(Constants.SQLX_YSBZ_ZM_DM)
                        || project.getSqlx().equals(Constants.SQLX_HZ_DM))
                        && StringUtils.isNotBlank(project.getGdproid())) {
                    Object obj = gdFwService.makeSureQllxByGdproid(gdproid);
                    if (obj instanceof GdFwsyq) {
                        project.setQllx(Constants.QLLX_GYTD_FWSUQ);
                    } else if (obj instanceof GdDy) {
                        project.setQllx(Constants.QLLX_DYAQ);
                    } else if (obj instanceof GdYg) {
                        project.setQllx(Constants.QLLX_YGDJ);
                    } else if (obj instanceof GdYy) {
                        project.setQllx(Constants.QLLX_YYDJ);
                    }
                }

                //zhouwanqing 区分过度还是新建
                if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW))
                    project.setXmly(Constants.XMLY_FWSP);
                else
                    project.setXmly(Constants.XMLY_TDSP);
                returnProject = projectService.creatProjectEvent(projectService.getCreatProjectService((BdcXm) project), project);
                //获取哪个登记类型service
                List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
                BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
                if (CollectionUtils.isEmpty(bdcSjxxList)) {
                    bdcSjdService.createSjxxByBdcxm(bdcXm);
                }
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);

                //lj 匹配创建合并流程，存在多个项目
                List<BdcXm> bdcXmList = null;
                if (bdcXm != null && StringUtils.isNotBlank(project.getWiid())) {
                    HashMap map = new HashMap();
                    map.put("wiid", bdcXm.getWiid());
                    bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
                }
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm xm : bdcXmList) {
                        //纯土地不匹配不动产单元，不传入原权利
                        if (!CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                            turnProjectDefaultService.saveQllxVo(xm);
                        }
                    }
                }
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
        return returnProject;
    }

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 获取过渡房屋匹配状态
     */
    @ResponseBody
    @RequestMapping("/getGdfwPpzt")
    public String getGdfwPpzt(@RequestParam(value = "gdproid", required = false) String gdproid) {
        String ppzt = "";
        if (StringUtils.isNotBlank(gdproid)) {
            GdXm gdXm = gdXmService.getGdXm(gdproid);
            if (gdXm != null && StringUtils.isNotBlank(gdXm.getPpzt()))
                ppzt = gdXm.getPpzt();
        }
        return ppzt;
    }

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 修改过渡房屋匹配状态
     */
    @ResponseBody
    @RequestMapping("/changeGdFwPpzt")
    public void changeGdFwPpzt(@RequestParam(value = "ppzt", required = false) String ppzt, @RequestParam(value = "gdproid", required = false) String gdproid) {
        String[] ppztArr = ppzt.split(",");
        Boolean isAllPp = true;
        List<String> ppztList = Arrays.asList(ppztArr);
        List<String> list = new ArrayList<String>();
        for (String ppztPar : ppztArr) {
            if (Constants.GD_PPZT_BFPP.equals(ppztPar)) {
                list.add(ppztPar);
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            gdXmService.updateGdxmPpzt(gdproid, Constants.GD_PPZT_WPP);
            return;
        }
        if (ppztList.size() != list.size()) {
            isAllPp = false;
        }
        if (isAllPp) {
            gdXmService.updateGdxmPpzt(gdproid, Constants.GD_PPZT_WCPP);
            return;
        } else {
            gdXmService.updateGdxmPpzt(gdproid, Constants.GD_PPZT_BFPP);
            return;
        }
    }

    /**
     * @param
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @rerutn
     * @description 更新匹配状态
     */
    @ResponseBody
    @RequestMapping(value = "updateGdPpzt")
    public HashMap updateGdPpzt(@RequestParam(value = "ppzt", required = false) String ppzt, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdproids", required = false) String gdproids, @RequestParam(value = "djlx", required = false) String djlx) {
        HashMap result = new HashMap();
        String msg = "更新成功";
        try {
            /**
             * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
             * @description 判断是否是需要修改匹配状态的登记类型
             */
            if (CommonUtil.indexOfStrs(Constants.EDIT_PPZT_DJLX, djlx)) {
                Boolean isPp = false;
                GdXm gdXm = gdXmService.getGdXm(gdproid);
                if (StringUtils.equals(gdXm.getPpzt(), Constants.GD_XM_YPPWFZ) || StringUtils.equals(gdXm.getPpzt(), Constants.GD_XM_BFPPWFZ)) {
                    isPp = true;
                }
                if (isPp) {
                    if (StringUtils.isNotBlank(gdproids) && gdproids.split(",").length > 1) {
                        String[] gdproidArr = gdproids.split(",");
                        for (String gdproidTemp : gdproidArr) {
                            gdXmService.updateGdxmPpzt(gdproidTemp, ppzt);
                        }
                    } else {
                        gdXmService.updateGdxmPpzt(gdproid, ppzt);
                    }
                }
            }
        } catch (Exception e) {
            msg = "更新失败";
            logger.error("BdcSjglConntroller.updateGdPpzt",e);
        } finally {
            result.put(ParamsConstants.RESULT_LOWERCASE, msg);
        }
        return result;
    }

    /**
     * 跳转过度数据管理的页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/toGdsjgl")
    public String toGdsjgl(Model model) {
        List<Map> fwytList = bdcZdGlService.getZdYt();
        List<Map> fwjgList = bdcZdGlService.getZdFwjg();
        List<GdZdFcxtDjlx> djlxList = gdXmService.getGdZdFcxtDjlx();
        String gdTabOrder = AppConfig.getProperty("gdTab.order");
        List<String> gdTabOrderList = new ArrayList<String>();
        if (StringUtils.isNotBlank(gdTabOrder) && gdTabOrder.split(",").length > 0) {
            for (String gdTab : gdTabOrder.split(","))
                gdTabOrderList.add(gdTab);
        }
        String gdTabLoadData = AppConfig.getProperty("gdTab.loadData");
        String tddcbcxUrl = AppConfig.getProperty("tddcbcx.url");
        String tdzscxUrl = AppConfig.getProperty("tdzscx.url");
        if (StringUtils.isNotBlank(tddcbcxUrl))
            model.addAttribute("tddcbcxUrl", tddcbcxUrl);
        if (StringUtils.isNotBlank(tdzscxUrl))
            model.addAttribute("tdzscxUrl", tdzscxUrl);
        model.addAttribute("gdTabOrderList", gdTabOrderList);
        model.addAttribute("gdTabOrder", gdTabOrder);
        model.addAttribute("gdTabLoadData", gdTabLoadData);
        model.addAttribute("fwytList", JSONObject.toJSONString(fwytList));
        model.addAttribute("fwjgList", JSONObject.toJSONString(fwjgList));
        model.addAttribute("djlxList", djlxList);

        return "sjgl/gdsjglLyg";
    }

    @RequestMapping(value = "showZsRel")
    public String showZsRel(Model model, String fwid, String tdid, String bdclx) {
        model.addAttribute("fwid", fwid);
        model.addAttribute("tdid", tdid);
        model.addAttribute(ParamsConstants.BDCLX_LOWERCASE, bdclx);
        return "sjgl/fwZsRel";
    }

    @RequestMapping(value = "showZsQl")
    public String showZsQl(Model model, String qlid, String showpage, String bdcid) {
        List<HashMap> qlList = new ArrayList<HashMap>();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        BdcDyaq bdcDyaq = entityMapper.selectByPrimaryKey(BdcDyaq.class, qlid);
        String proid = "";
        //先判断是不是抵押，若是则取其proid，不是则根据gdproid获得proid
        if (bdcDyaq != null) {
            proid = bdcDyaq.getProid();
        } else if (StringUtils.isNotBlank(bdcid)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.getBdcXmRelByName("yproid", bdcid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList))
                proid = bdcXmRelList.get(0).getProid();
        }
        if (StringUtils.isBlank(proid))
            proid = qlid;

        //根据showpage判断显示哪个页面
        if (StringUtils.equals(showpage, "showql") || StringUtils.equals(showpage, "all")) {
            HashMap map = new HashMap();
            map.put("qlid", qlid);
            List<GdFwsyq> fwsyqList = gdFwService.andEqualQueryGdFwsyq(map);
            List<GdTdsyq> gdTdsyqList = gdTdService.andEqualQueryGdTdsyq(map);
            map.clear();
            map.put("dyid", qlid);
            List<GdDy> gdDyList = gdFwService.andEqualQueryGdDy(map);
            map.clear();
            map.put("cfid", qlid);
            List<GdCf> gdCfList = gdFwService.andEqualQueryGdCf(map);
            map.clear();
            map.put("ygid", qlid);
            List<GdYg> gdYgList = gdFwService.andEqualQueryGdYg(map);
            map.clear();
            map.put("yyid", qlid);
            List<GdYy> gdYyList = gdFwService.andEqualQueryGdYy(map);


            String url = AppConfig.getProperty("qllxcpt.filepath");
            HashMap<String, String> qlMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(fwsyqList)) {
                url = url.replace(PARAMETER_TABLENAME, Constants.GDQL_FWSYQ_CPT);
                qlMap.put("mc", Constants.GDQL_FWSYQ);
            } else if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                url = url.replace(PARAMETER_TABLENAME, Constants.GDQL_TDSYNQ_CPT);
                qlMap.put("mc", Constants.GDQL_TDSYNQ);
            } else if (CollectionUtils.isNotEmpty(gdDyList)) {
                url = url.replace(PARAMETER_TABLENAME, Constants.GDQL_DY_CPT);
                qlMap.put("mc", Constants.GDQL_DY);
            } else if (CollectionUtils.isNotEmpty(gdCfList)) {
                url = url.replace(PARAMETER_TABLENAME, Constants.GDQL_CF_CPT);
                qlMap.put("mc", Constants.GDQL_CF);
            } else if (CollectionUtils.isNotEmpty(gdYgList)) {
                url = url.replace(PARAMETER_TABLENAME, Constants.GDQL_YG_CPT);
                qlMap.put("mc", Constants.GDQL_YG);
            } else if (CollectionUtils.isNotEmpty(gdYyList)) {
                url = url.replace(PARAMETER_TABLENAME, Constants.GDQL_YY_CPT);
                qlMap.put("mc", Constants.GDQL_YY);
            }
            if (bdcDyaq != null && StringUtils.isNotBlank(bdcDyaq.getProid())) {
                url = url.replace(PARAMETER_TABLENAME, "bdcqzms");
                qlMap.put("mc", "抵押");
                url += "&proid=" + proid;
            }


            url += "&qlid=" + qlid;
            url = PlatformUtil.initOptProperties(url);
            qlMap.put("tableName", url);
            qlList.add(qlMap);
        }
        //添加附件管理页面
        if (StringUtils.equals(showpage, "fjgl") || StringUtils.equals(showpage, "all")) {
            //附记管理页面
            String fjurl = PlatformUtil.initOptProperties(AppConfig.getProperty("platform.url")) + "/fc.action?readOnly=true&from=task&proid=" + proid;
            hashMap.put("mc", "附件管理");
            hashMap.put("tableName", fjurl);
            qlList.add(hashMap);
        }

        model.addAttribute("qllist", qlList);
        return "query/showQlLyg";
    }

    @ResponseBody
    @RequestMapping(value = "getGdZsZt")
    public HashMap<String, String> getGdZsZt(String qlid, String zslx) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        HashMap map = new HashMap();
        map.put("QLID", qlid);
        String zsZt = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(zslx)) {
            try {
                zslx = URLDecoder.decode(zslx, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new AppException("不支持的编码");
            }
            if (StringUtils.contains(zslx, Constants.GDQL_TXZ)) {
                map.put("ZSLX", Constants.GDQL_DY_CPT);
            }
            if (StringUtils.contains(zslx, Constants.GDQL_YG)) {
                map.put("ZSLX", Constants.GDQL_YG_CPT);
            }
            if (StringUtils.contains(zslx, Constants.GDQL_YY)) {
                map.put("ZSLX", Constants.GDQL_YY_CPT);
            }
            if (StringUtils.contains(zslx, Constants.GDQL_CF)) {
                map.put("ZSLX", Constants.GDQL_CF_CPT);
            }
            if (StringUtils.contains(zslx, Constants.GDQL_FWSYQ)) {
                map.put("ZSLX", Constants.GDQL_FWSYQ_CPT);
            }
            if (StringUtils.contains(zslx, Constants.GDQL_TDZ)) {
                map.put("ZSLX", Constants.GDQL_TDSYQ_CPT);
            }
            if (StringUtils.isNotBlank(gdqlService.getGdZsZt(map))) {
                zsZt = gdqlService.getGdZsZt(map);
            }
        } else {
            String gdId = gdqlService.getGdIdsByQlId(map, "GDID");
            if (StringUtils.isNotBlank(gdId)) {
                resultMap.put("GDID", gdId.substring(0, gdId.length() - 1));
            }
        }
        resultMap.put("ZSZT", zsZt);
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "getFwZsJsonByPage")
    public Object getFwZsJsonByPage(String fwid, String tdid, Pageable pageable) {
        HashMap map = new HashMap();
        if (StringUtils.isNotBlank(fwid))
            map.put("GDID", fwid);
        if (StringUtils.isNotBlank(tdid))
            map.put("GDID", tdid);
        String qlids = gdqlService.getGdIdsByQlId(map, "QLID");
        map.clear();
        map.put("qlidsIn", qlids.split(","));
        return repository.selectPaging("getGdZsJgJsonByPage", map, pageable);
    }

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 查看档案信息
     */
    @ResponseBody
    @RequestMapping(value = "showDnxx")
    public String showDnxx(String qlid, String bdclx) {
        String fd = "";
        String sjh = "";
        String type = "";
        String username = super.getUserVo().getLoginName();
        String url = AppConfig.getProperty("fwdncx.url");
        if (StringUtils.isNotBlank(qlid) && StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            sjh = StringUtils.substring(qlid, 2, qlid.length());
            if (StringUtils.equals(StringUtils.substring(qlid, 0, 1), "D"))
                type = "D";
            else
                type = "C";
            HashMap map = new HashMap();
            map.put("qlid", qlid);
            List<String> qhList = gdFwService.getCqqidByGdProid(map);
            if (CollectionUtils.isNotEmpty(qhList))
                fd = qhList.get(0);
            else
                url = "error";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdfcrq = new SimpleDateFormat("yyyy-MM-dd");
            String crq = sdfcrq.format(new Date());
            String key = fd + "@" + sjh + "@" + username + "@" + crq + "@" + (c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY)) + "@" + (c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE)) + "@" + "LYGGTIntface";
            key = StringUtils.upperCase(Codecs.md5Hex(StringUtils.upperCase(key)));
            url += "?TYPE=" + type + "&FD=" + fd + "&SJH=" + sjh + "&USERNAME=" + username + "&Key=" + key;
        }
        return url;
    }

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据权利ID查询djh
     */
    @ResponseBody
    @RequestMapping(value = "getDjhByQlid")
    public String getDjhByQlid(String qlid) {
        String djh = "";
        List<GdTd> gdTdList = gdTdService.getGdTdListByQlid(qlid);
        if (CollectionUtils.isNotEmpty(gdTdList)&&StringUtils.isNotBlank(gdTdList.get(0).getDjh())) {
            djh = gdTdList.get(0).getDjh();
        }
        return djh;
    }
}
