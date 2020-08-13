package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 * 通过后台代码控制项目中应该打开的权利类型资源url
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-25
 */
@Controller
@RequestMapping("/qllxResource")
public class QllxResourceController extends BaseController {

    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private Repo repository;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcdjbService bdcdjbService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcQlrService bdcQlrService;

    private static final String CONFIGURATION_PARAMETER_QLLXCPT_FILEPATH = "qllxcpt.filepath";
    private static final String PARAMETER_GD_ZXCF = "gd_zxcf";
    private static final String PARAMETER_GD_ZXJY = "gd_zxjy";
    private static final String PARAMETER_TABLENAME = "${tableName}";
    private static final String PARAMETER_QLID = "&qlid=";
    private static final String PARAMETER_PRO_ID = "&pro_id=";
    private static final String PARAMETER_TASKID = "&taskid=";
    private static final String PARAMETER_RID = "&rid=";
    private static final String PARAMETER_FROM = "&from=";
    private static final String PARAMETER_WIID =  "&wiid=";
    private static final String PARAMETER_BDCLX =  "&bdclx=";
    private static final String PARAMETER_BDCID =  "&bdcid=";
    private static final String PARAMETER_PROID =  "&proid=";
    private static final String PARAMETER_DJBID =  "&djbid=";
    private static final String PARAMETER_ISJF =  "&isjf=";
    private static final String PARAMETER_BDCDYH =  "&bdcdyh=";
    private static final String PARAMETER_BDCDYID =  "&bdcdyid=";
    private static final String PARAMETER_SHOWTOOLBAR =  "&__showtoolbar__=";
    private static final String PARAMETER_WORKFLOWPROID =  "workflowProid";
    private static final String PARAMETER_YQLLIST = "yqlList";
    private static final String PARAMETER_YWTYPE_RID = "&ywType=server&rid=";
    private static final String PARAMETER_ISYQLXX_TRUE = "&isYqlxx=true";
    private static final String PARAMETER_OP_WRITE_CPTNAME = "&op=write&cptName=";
    private static final String PARAMETER_MAIN_WFQLLXLIST_FTL = "main/wfQllxList";


    private static final String PARAMETER_INDEX_TOERROR_URL =  "/index/toError";
    private static final String PARAMETER_GDXXLR_INDEXQL_BDCLX_URL =  "/gdXxLr/indexql?bdclx=";
    private static final String PARAMETER_GDXXLR_INDEXQL_BDCLX_TD_PROID_URL =  "/gdXxLr/indexql?bdclx=td&proid=";
    private static final String PARAMETER_GDXXLR_INDEXQL_BDCLX_FW_PROID_URL =  "/gdXxLr/indexql?bdclx=fw&proid=";
    private static final String PARAMETER_REPORTSERVER_EDIT_MUL_URL =  "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\edit\\mul\\";
    private static final String PARAMETER_REPORTSERVER_EDIT_URL =  "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\edit\\";

    /**
     * 根据项目ID获取帆软qllx资源 需要opt配置qllxcpt.filepath
     *
     * @param proid      项目ID
     * @param updateQllx 是否更新qllx表
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public void index(Model model, @RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "updateQllx", required = false) String updateQllx, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, HttpServletResponse response) throws Exception {
        String url = "";
        List<BdcBdcdy> bdcBdcdyList = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            QllxVo vo = qllxService.makeSureQllx(bdcXm);
            //如果无法确定权利类型，则返回错误页面
            if (vo == null || bdcBdcdy == null) {
                response.sendRedirect(bdcdjUrl + PARAMETER_INDEX_TOERROR_URL);
                return;
            }
            //zdd 防止冲掉用户填写的信息  所以如果没生成过权利类型   重新生成
            if (StringUtils.isNotBlank(updateQllx) && qllxService.queryQllxVo(vo, proid) == null) {
                TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                turnProjectService.saveQllxVo(bdcXm);
            }
            url = AppConfig.getProperty(CONFIGURATION_PARAMETER_QLLXCPT_FILEPATH);
            //过渡土地走查封不匹配不动产单元
            if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)
                    || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM)) {
                String tableName = PARAMETER_GD_ZXCF;
                String bdcid = bdcXm.getBdcdyid();
                String bdclx = "";
                String isjf = "";
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)) {
                    bdclx = "TD";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        BdcXmRel xmRel = bdcXmRelList.get(0);
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(xmRel.getProid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM)) {
                    bdclx = "TDFW";
                    isjf = "0";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        BdcXmRel xmRel = bdcXmRelList.get(0);
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(xmRel.getProid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM)) {
                    bdclx = "TDFW";
                    isjf = "1";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM)) {
                    bdclx = "TD";
                    isjf = "1";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                }
            } else {
                String tableName = qllxService.getTableName(vo).toLowerCase();
                url = url.replace(PARAMETER_TABLENAME, tableName);
                url = PlatformUtil.initOptProperties(url);
                //jyl 过滤不动产房屋附属设施
                bdcBdcdyList = bdcdyService.queryBdcBdcdyFilterBdcFwfsss(bdcXm.getWiid());
                if (bdcBdcdyList != null && bdcBdcdyList.size() > 1 && StringUtils.isBlank(getSimpleCpt)) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_MUL_URL + tableName + PARAMETER_OP_WRITE_CPTNAME + tableName;
                } else if (StringUtils.isBlank(getSimpleCpt)) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + tableName + PARAMETER_OP_WRITE_CPTNAME + tableName;
                }
            }
            if (StringUtils.equals(getSimpleCpt, "true"))
                url = url + PARAMETER_BDCDYH + bdcdyh + PARAMETER_BDCDYID + bdcdyid;
        }
        if (__showtoolbar__ == null)
            __showtoolbar__ = "";
        if (StringUtils.isNotBlank(taskid))
            url = url + PARAMETER_PROID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
        else
            url = url + PARAMETER_PROID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__;
        response.sendRedirect(url);
    }

    /**
     * @param proid      项目ID
     * @param updateQllx 是否更新qllx表
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 根据项目ID获取帆软qllx资源 需要opt配置qllxcpt.filepath(考虑附属设施)
     */
    @RequestMapping(value = "qlxxIncludeFsss", method = RequestMethod.GET)
    public void qlxxIncludeFsss(Model model, @RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "updateQllx", required = false) String updateQllx, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, HttpServletResponse response) throws Exception {
        String url = "";
        List<BdcBdcdy> bdcBdcdyList = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
            QllxVo vo = qllxService.makeSureQllx(bdcXm);
            //如果无法确定权利类型，则返回错误页面
            if (vo == null || bdcBdcdy == null) {
                response.sendRedirect(bdcdjUrl + PARAMETER_INDEX_TOERROR_URL);
                return;
            }
            //zdd 防止冲掉用户填写的信息  所以如果没生成过权利类型   重新生成
            if (StringUtils.isNotBlank(updateQllx) && qllxService.queryQllxVo(vo, proid) == null) {
                TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                turnProjectService.saveQllxVo(bdcXm);
            }
            url = AppConfig.getProperty(CONFIGURATION_PARAMETER_QLLXCPT_FILEPATH);
            //过渡土地走查封不匹配不动产单元
            if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)
                    || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM)) {
                String tableName = PARAMETER_GD_ZXCF;
                String bdcid = bdcXm.getBdcdyid();
                String bdclx = "";
                String isjf = "";
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)) {
                    bdclx = "TD";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        BdcXmRel xmRel = bdcXmRelList.get(0);
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(xmRel.getProid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM)) {
                    bdclx = "TDFW";
                    isjf = "0";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        BdcXmRel xmRel = bdcXmRelList.get(0);
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(xmRel.getProid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM)) {
                    bdclx = "TDFW";
                    isjf = "1";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM)) {
                    bdclx = "TD";
                    isjf = "1";
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            GdCf gdCf = gdCfList.get(0);
                            url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                        }
                        url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE + PARAMETER_ISJF + isjf;
                    }
                }
            } else {
                String tableName = qllxService.getTableName(vo).toLowerCase();
                url = url.replace(PARAMETER_TABLENAME, tableName);
                url = PlatformUtil.initOptProperties(url);
                //jyl 过滤不动产房屋附属设施
                bdcBdcdyList = bdcdyService.queryBdcBdcdyFilterBdcFwfsss(bdcXm.getWiid());
                if (bdcBdcdyList != null && bdcBdcdyList.size() > 1 && StringUtils.isBlank(getSimpleCpt)) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_MUL_URL + tableName + PARAMETER_OP_WRITE_CPTNAME + tableName;
                } else if (StringUtils.isBlank(getSimpleCpt)) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + tableName + PARAMETER_OP_WRITE_CPTNAME + tableName;
                }
            }
            if (StringUtils.equals(getSimpleCpt, "true"))
                url = url + PARAMETER_BDCDYH + bdcdyh + PARAMETER_BDCDYID + bdcdyid;
        }
        if (__showtoolbar__ == null)
            __showtoolbar__ = "";
        if (StringUtils.isNotBlank(taskid))
            url = url + PARAMETER_PROID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
        else
            url = url + PARAMETER_PROID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__;
        List<BdcBdcdy> bdcBdcdyAllList = bdcdyService.queryBdcBdcdy(wiid);
        //jyl 附属设施信息权利也要显示
        if (CollectionUtils.isNotEmpty(bdcBdcdyAllList) && CollectionUtils.isNotEmpty(bdcBdcdyList) && bdcBdcdyList.size() == 1 && bdcBdcdyAllList.size() > 1) {
            url = bdcdjUrl + "/qllxResource/list?workflowProid=" + proid + PARAMETER_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid;
        }
        response.sendRedirect(url);
    }

    /**
     * zdd 一个流程多个项目的  权利信息列表展现
     *
     * @param model
     * @param rid
     * @param taskid
     * @param from
     * @param wiid
     * @param proid
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        model.addAttribute(PARAMETER_WORKFLOWPROID, proid);
        model.addAttribute("rid", rid);
        model.addAttribute(ParamsConstants.TASKID_LOWERCASE, taskid);
        model.addAttribute("from", from);
        model.addAttribute("wiid", wiid);
        return PARAMETER_MAIN_WFQLLXLIST_FTL;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 用于展示原权利LIST
     */
    @RequestMapping(value = "yqlList", method = RequestMethod.GET)
    public String yqlList(Model model, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) throws IOException {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        /**
         * jiangganzhi  分割（合并）变更登记 关联房产证土地证显示过渡权利
         */
        if (StringUtils.isNotBlank(bdcXm.getSqlx()) && CommonUtil.indexOfStrs(Constants.SQLX_YQLXXLB,bdcXm.getSqlx())) {
            String proids = "";
            String qlids = "";
            Boolean isGdQl = false;
            List<String> proidList = new ArrayList<String>();
            List<String> qlidList = new ArrayList<String>();
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm1 : bdcXmList) {
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm1.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        List<GdTdsyq> gdTdsyqList = null;
                        List<GdFwsyq> gdFwsyqList = null;
                        for (BdcXmRel bdcXmRel : bdcXmRelList) {
                            gdTdsyqList = gdTdService.getGdTdsyqListByGdproid(bdcXmRel.getYproid(), 0);
                            gdFwsyqList = gdFwService.getGdFwsyqListByGdproid(bdcXmRel.getYproid(), 0);
                            if (CollectionUtils.isNotEmpty(gdTdsyqList) || CollectionUtils.isNotEmpty(gdFwsyqList)) {
                                isGdQl = true;
                            }
                            if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                                String[] yproids = bdcXmRel.getYproid().split(",");
                                for (String yProid : yproids) {
                                    proidList.add(yProid);
                                }
                                StringBuilder tdProids = new StringBuilder();
                                if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                                    for (GdTdsyq gdTdsyq : gdTdsyqList) {
                                        tdProids.append(gdTdsyq.getProid()).append(",");
                                    }
                                }
                                if (StringUtils.isNotBlank(tdProids)) {
                                    String[] yTdProids = tdProids.toString().split(",");
                                    for (String yTdproid : yTdProids) {
                                        proidList.add(yTdproid);
                                    }
                                }
                            }
                            if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                                if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                                    String[] yqlids = bdcXmRel.getYqlid().split(",");
                                    for (String yqlid : yqlids)
                                        qlidList.add(yqlid);
                                }
                                StringBuilder tdQlids = new StringBuilder();
                                if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                                    for (GdTdsyq gdTdsyq : gdTdsyqList) {
                                        tdQlids.append(gdTdsyq.getQlid()).append(",");
                                    }
                                }
                                if (StringUtils.isNotBlank(tdQlids)) {
                                    String[] yTdQlids = tdQlids.toString().split(",");
                                    for (String yTdQlid : yTdQlids) {
                                        qlidList.add(yTdQlid);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isGdQl) {
                proids = StringUtils.join(proidList, ",");
                if (CollectionUtils.isNotEmpty(qlidList)) {
                    qlids = StringUtils.join(qlidList, ",");
                }
                String url = bdcdjUrl + "/gdXxLr/indexql?bdclx=fw&proids=" + proids + "&qlids=" + qlids + PARAMETER_WIID + wiid;
                response.sendRedirect(url);
            } else {
                model.addAttribute(PARAMETER_WORKFLOWPROID, proid);
                model.addAttribute("rid", rid);
                model.addAttribute("taskid", taskid);
                model.addAttribute("from", from);
                model.addAttribute("wiid", wiid);
                boolean xsql = StringUtils.equals(Constants.SQLX_PLDYZX, bdcXm.getSqlx()) && (StringUtils.equals(Constants.SJLY_GD_XMLY, bdcXm.getXmly()) || StringUtils.equals(Constants.SJLY_GDFW_XMLY, bdcXm.getXmly()));
                if (!xsql) {
                    model.addAttribute(PARAMETER_YQLLIST, "true");
                }
                return PARAMETER_MAIN_WFQLLXLIST_FTL;
            }
        } else {
            model.addAttribute(PARAMETER_WORKFLOWPROID, proid);
            model.addAttribute("rid", rid);
            model.addAttribute("taskid", taskid);
            model.addAttribute("from", from);
            model.addAttribute("wiid", wiid);
            model.addAttribute("djlx", bdcXm.getDjlx());
            model.addAttribute("sqlx", bdcXm.getSqlx());
            boolean xsql = StringUtils.equals(Constants.SQLX_PLDYZX, bdcXm.getSqlx()) && (StringUtils.equals(Constants.SJLY_GD_XMLY, bdcXm.getXmly()) || StringUtils.equals(Constants.SJLY_GDFW_XMLY, bdcXm.getXmly()));
            if (!xsql) {
                model.addAttribute(PARAMETER_YQLLIST, "true");
            }
            return PARAMETER_MAIN_WFQLLXLIST_FTL;
        }
        return null;
    }

    /**
     * zdd 注销登记需要展现原权利信息
     *
     * @param proid
     * @throws Exception
     */
    @RequestMapping(value = "yqllxResource", method = RequestMethod.GET)
    public void yqllxResource(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "isYqlxx", required = false) String isYqlxx, @RequestParam(value = "isYdyawqd", required = false) String isYdyawqd, HttpServletResponse response) throws Exception {
        String url = "";
        String yproid = "";
        String bdclx = "";
        try {
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);//根据不动产项目id获取不动产单元
                BdcXmRel xmRel = new BdcXmRel();
                List<BdcXmRel> list = bdcXmRelService.queryBdcXmRelByProid(proid);
                String bdcdyh = "";
                String bdcdyid = "";

                if (CollectionUtils.isNotEmpty(list)) {
                    xmRel = list.get(0);//获取项目与原项目关系
                    for (BdcXmRel bdcXmRel : list) {
                        //zdd 如果是过渡数据匹配  此处存在BUG
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                            yproid = bdcXmRel.getYproid();//获取原不动产项目id
                            break;
                        }
                    }
                }
                BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
                if (bdcXmTemp != null)
                    bdcdyid = bdcXmTemp.getBdcdyid();//获取不动产单元id
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);//根据不动产单元id获取不动产单元
                if (bdcBdcdy != null) {
                    bdcdyh = bdcBdcdy.getBdcdyh();//获取不动产单元号
                }

                QllxVo vo = null;
                //如果原不动产项目idid不为空，则获取原不动产项目
                if (StringUtils.isNotBlank(yproid) && StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_BDC)) {
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);//根据原不动产项目id获取原不动产项目
                    //如果原不动产项目为空，则在权利表中查找proid为yproid对应的权利类型
                    if (ybdcXm == null) {
                        vo = qllxService.getQllxVoByProid(yproid);
                    } else {
                        //如果不为空，则根据项目直接获取到qllx
                        vo = qllxService.makeSureQllx(ybdcXm);
                    }
                } else {
                    vo = qllxService.makeSureQllx(bdcXm);
                }
                String sqlxmc = "";
                HashMap map = new HashMap();
                map.put("dm", bdcXm.getSqlx());
                List<BdcZdSqlx> bdcZdSqlxList = bdcZdGlService.getBdcSqlxByMap(map);
                if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
                    sqlxmc = bdcZdSqlxList.get(0).getMc();
                }
                String tableName = qllxService.getTableName(vo).toLowerCase();//获取tableName
                List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdy(wiid);
                if (bdcBdcdyList != null && bdcBdcdyList.size() > 1) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_MUL_URL + PARAMETER_TABLENAME + PARAMETER_OP_WRITE_CPTNAME + PARAMETER_TABLENAME;
                } else if (!CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, bdcXm.getSqlx())) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + PARAMETER_TABLENAME + PARAMETER_OP_WRITE_CPTNAME + PARAMETER_TABLENAME;
                }

                if (StringUtils.isNotBlank(xmRel.getYproid())) {
                    if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                        //zwq 匹配显示原权利信息
                        //不传原权利的原权利显示
                        if (StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDYBG_YBZS_SQLXDM)) {
                            tableName = PARAMETER_GD_ZXJY;
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            url = url + PARAMETER_QLID + xmRel.getYqlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM)) {
                            tableName = "gd_fwsyq";
                            String bdcid = bdcXm.getBdcdyid();
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(bdcXmRelList.get(0).getYqlid());
                                if (gdFwsyq != null) {
                                    url = url + PARAMETER_QLID + gdFwsyq.getQlid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM)) {
                            tableName = PARAMETER_GD_ZXCF;
                            String bdcid = bdcXm.getBdcdyid();
                            bdclx = "TDFW";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                                if (CollectionUtils.isNotEmpty(gdCfList)) {
                                    GdCf gdCf = gdCfList.get(0);
                                    url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)) {
                            tableName = "gd_tdsynq";
                            String bdcid = bdcXm.getBdcdyid();
                            bdclx = "TD";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(bdcXmRelList.get(0).getYqlid());
                                if (gdTdsyq != null) {
                                    url = url + PARAMETER_QLID + gdTdsyq.getQlid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM)) {
                            tableName = PARAMETER_GD_ZXCF;
                            String bdcid = bdcXm.getBdcdyid();
                            bdclx = "TD";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                                if (CollectionUtils.isNotEmpty(gdCfList)) {
                                    GdCf gdCf = gdCfList.get(0);
                                    url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDSYQ_ZX_DM)) {
                            tableName = "gd_zxtdsyq";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(xmRel.getYqlid());
                            if (gdTdsyq == null) {
                                gdTdsyq = new GdTdsyq();
                            }
                            url = url + PARAMETER_QLID + gdTdsyq.getQlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDDY_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWDY_ZX_DM)) {
                            tableName = PARAMETER_GD_ZXJY;
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            url = url + PARAMETER_QLID + xmRel.getYqlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        } else {
                            tableName = PARAMETER_GD_ZXCF;
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            url = url + PARAMETER_QLID + xmRel.getYqlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        }
                    }//注销登记不匹配不动产单元
                    else if (CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, bdcXm.getSqlx())) {
                        if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_FWSP)) {
                            url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_FW_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                        } else if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_TDSP)) {
                            url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_TD_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                        } else {
                            bdclx = bdcXm.getBdclx();
                            url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_URL + bdclx + PARAMETER_PROID + xmRel.getYproid() + PARAMETER_WIID + wiid;
                        }
                    } else {
                        //zhouwanqing 根据xmly获取过度或者匹配的原权利类型
                        if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_BDC) || StringUtils.indexOf(sqlxmc, "注销") > -1 || StringUtils.indexOf(sqlxmc, "解封") > -1 && StringUtils.isNotBlank(bdcdyid)) {
                            /**
                             * @author bianwen
                             * @description 在建工程抵押注销，“原抵押物清单”资源
                             */
                            if (StringUtils.equals(isYdyawqd, "true")) {
                                url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + PARAMETER_TABLENAME + PARAMETER_OP_WRITE_CPTNAME + PARAMETER_TABLENAME;
                                tableName = "bdc_dyawqd";
                            }
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_BDC))
                                url = url + PARAMETER_PROID + xmRel.getYproid();
                            else
                                url = url + PARAMETER_PROID + xmRel.getProid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_ISYQLXX_TRUE;
                        } else {
                            //zwq 匹配显示原权利信息
                            if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_FWSP)) {
                                url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_FW_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                            } else if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_TDSP)) {
                                url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_TD_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                            } else {
                                bdclx = bdcXm.getBdclx();
                                url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_URL + bdclx + PARAMETER_PROID + xmRel.getYproid() + PARAMETER_WIID + wiid;
                            }
                        }
                    }

                } else {
                    url = bdcdjUrl + PARAMETER_INDEX_TOERROR_URL;
                }
                url = PlatformUtil.initOptProperties(url);
                if (StringUtils.isNotBlank(bdcdyh)) {
                    url = url + PARAMETER_BDCDYH + bdcdyh + PARAMETER_BDCDYID + bdcdyid;
                }
            }
        } catch (Exception e) {
            url = bdcdjUrl + PARAMETER_INDEX_TOERROR_URL;
            logger.error("QllxResourceController.yqllxResource",e);
        }
        response.sendRedirect(url);
    }

    /**
     * @param proid 项目ID
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 根据项目ID获取原权利信息(考虑附属设施)
     */
    @RequestMapping(value = "yqllxResourceIncludeFsss", method = RequestMethod.GET)
    public void yqllxResourceIncludeFsss(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "isYqlxx", required = false) String isYqlxx, @RequestParam(value = "isYdyawqd", required = false) String isYdyawqd, HttpServletResponse response) throws Exception {
        String url = "";
        String yproid = "";
        String bdclx = "";
        try {
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);//根据不动产项目id获取不动产单元
                BdcXmRel xmRel = new BdcXmRel();
                List<BdcXmRel> list = bdcXmRelService.queryBdcXmRelByProid(proid);
                String bdcdyh = "";
                String bdcdyid = "";

                if (CollectionUtils.isNotEmpty(list)) {
                    xmRel = list.get(0);//获取项目与原项目关系
                    for (BdcXmRel bdcXmRel : list) {
                        //zdd 如果是过渡数据匹配  此处存在BUG
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                            yproid = bdcXmRel.getYproid();//获取原不动产项目id
                            break;
                        }
                    }
                }
                BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
                if (bdcXmTemp != null)
                    bdcdyid = bdcXmTemp.getBdcdyid();//获取不动产单元id
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);//根据不动产单元id获取不动产单元
                if (bdcBdcdy != null) {
                    bdcdyh = bdcBdcdy.getBdcdyh();//获取不动产单元号
                }

                QllxVo vo = null;
                //如果原不动产项目idid不为空，则获取原不动产项目
                if (StringUtils.isNotBlank(yproid) && StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_BDC)) {
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);//根据原不动产项目id获取原不动产项目
                    //如果原不动产项目为空，则在权利表中查找proid为yproid对应的权利类型
                    if (ybdcXm == null) {
                        vo = qllxService.getQllxVoByProid(yproid);
                    } else {
                        //如果不为空，则根据项目直接获取到qllx
                        vo = qllxService.makeSureQllx(ybdcXm);
                    }
                } else {
                    vo = qllxService.makeSureQllx(bdcXm);
                }
                String sqlxmc = "";
                HashMap map = new HashMap();
                map.put("dm", bdcXm.getSqlx());
                List<BdcZdSqlx> bdcZdSqlxList = bdcZdGlService.getBdcSqlxByMap(map);
                if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
                    sqlxmc = bdcZdSqlxList.get(0).getMc();
                }
                String tableName = qllxService.getTableName(vo).toLowerCase();//获取tableName
                List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdyFilterBdcFwfsss(wiid);
                if (bdcBdcdyList != null && bdcBdcdyList.size() > 1) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_MUL_URL + PARAMETER_TABLENAME + PARAMETER_OP_WRITE_CPTNAME + PARAMETER_TABLENAME;
                } else if (!CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, bdcXm.getSqlx())) {
                    url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + PARAMETER_TABLENAME + PARAMETER_OP_WRITE_CPTNAME + PARAMETER_TABLENAME;
                }

                if (StringUtils.isNotBlank(xmRel.getYproid())) {
                    if (CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, bdcXm.getSqlx())) {
                        //zwq 匹配显示原权利信息
                        //不传原权利的原权利显示
                        if (StringUtils.equals(bdcXm.getSqlx(), Constants.DJLX_PLDYBG_YBZS_SQLXDM)) {
                            tableName = PARAMETER_GD_ZXJY;
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            url = url + PARAMETER_QLID + xmRel.getYqlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWCF_DM)) {
                            tableName = "gd_fwsyq";
                            String bdcid = bdcXm.getBdcdyid();
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(bdcXmRelList.get(0).getYqlid());
                                if (gdFwsyq != null) {
                                    url = url + PARAMETER_QLID + gdFwsyq.getQlid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWJF_DM)) {
                            tableName = PARAMETER_GD_ZXCF;
                            String bdcid = bdcXm.getBdcdyid();
                            bdclx = "TDFW";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                                if (CollectionUtils.isNotEmpty(gdCfList)) {
                                    GdCf gdCf = gdCfList.get(0);
                                    url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_OLD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)) {
                            tableName = "gd_tdsynq";
                            String bdcid = bdcXm.getBdcdyid();
                            bdclx = "TD";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(bdcXmRelList.get(0).getYqlid());
                                if (gdTdsyq != null) {
                                    url = url + PARAMETER_QLID + gdTdsyq.getQlid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDJF_DM)) {
                            tableName = PARAMETER_GD_ZXCF;
                            String bdcid = bdcXm.getBdcdyid();
                            bdclx = "TD";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(bdcXmRelList.get(0).getYproid(), null);
                                if (CollectionUtils.isNotEmpty(gdCfList)) {
                                    GdCf gdCf = gdCfList.get(0);
                                    url = url + PARAMETER_QLID + gdCf.getCfid() + PARAMETER_PROID + gdCf.getProid();
                                }
                                url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_BDCLX + bdclx + PARAMETER_BDCID + bdcid + PARAMETER_ISYQLXX_TRUE;
                            }
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDSYQ_ZX_DM)) {
                            tableName = "gd_zxtdsyq";
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(xmRel.getYqlid());
                            if (gdTdsyq == null) {
                                gdTdsyq = new GdTdsyq();
                            }
                            url = url + PARAMETER_QLID + gdTdsyq.getQlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDDY_ZX_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWDY_ZX_DM)) {
                            tableName = PARAMETER_GD_ZXJY;
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            url = url + PARAMETER_QLID + xmRel.getYqlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        } else {
                            tableName = PARAMETER_GD_ZXCF;
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            url = url + PARAMETER_QLID + xmRel.getYqlid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_ISYQLXX_TRUE;
                        }
                    }//注销登记不匹配不动产单元
                    else if (CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, bdcXm.getSqlx())) {
                        if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_FWSP)) {
                            url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_FW_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                        } else if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_TDSP)) {
                            url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_TD_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                        } else {
                            bdclx = bdcXm.getBdclx();
                            url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_URL + bdclx + PARAMETER_PROID + xmRel.getYproid() + PARAMETER_WIID + wiid;
                        }
                    } else {
                        //zhouwanqing 根据xmly获取过度或者匹配的原权利类型
                        if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_BDC) || StringUtils.indexOf(sqlxmc, "注销") > -1 || StringUtils.indexOf(sqlxmc, "解封") > -1 && StringUtils.isNotBlank(bdcdyid)) {
                            /**
                             * @author bianwen
                             * @description 在建工程抵押注销，“原抵押物清单”资源
                             */
                            if (StringUtils.equals(isYdyawqd, "true")) {
                                url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + PARAMETER_TABLENAME + PARAMETER_OP_WRITE_CPTNAME + PARAMETER_TABLENAME;
                                tableName = "bdc_dyawqd";
                            }
                            url = url.replace(PARAMETER_TABLENAME, tableName);
                            url = PlatformUtil.initOptProperties(url);
                            if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_BDC))
                                url = url + PARAMETER_PROID + xmRel.getYproid();
                            else
                                url = url + PARAMETER_PROID + xmRel.getProid();
                            url = url + PARAMETER_PRO_ID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_ISYQLXX_TRUE;
                        } else {
                            //zwq 匹配显示原权利信息
                            if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_FWSP)) {
                                url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_FW_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                            } else if (StringUtils.equals(xmRel.getYdjxmly(), Constants.XMLY_TDSP)) {
                                url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_TD_PROID_URL + xmRel.getYproid() + PARAMETER_WIID + wiid;
                            } else {
                                bdclx = bdcXm.getBdclx();
                                url = bdcdjUrl + PARAMETER_GDXXLR_INDEXQL_BDCLX_URL + bdclx + PARAMETER_PROID + xmRel.getYproid() + PARAMETER_WIID + wiid;
                            }
                        }
                    }

                } else {
                    url = bdcdjUrl + PARAMETER_INDEX_TOERROR_URL;
                }
                url = PlatformUtil.initOptProperties(url);
                if (StringUtils.isNotBlank(bdcdyh)) {
                    url = url + PARAMETER_BDCDYH + bdcdyh + PARAMETER_BDCDYID + bdcdyid;
                }
                //jyl 附属设施信息权利也要显示
                List<BdcBdcdy> bdcBdcdyAllList = bdcdyService.queryBdcBdcdy(wiid);
                if (CollectionUtils.isNotEmpty(bdcBdcdyAllList) && CollectionUtils.isNotEmpty(bdcBdcdyList) && bdcBdcdyList.size() == 1 && bdcBdcdyAllList.size() > 1) {
                    url = bdcdjUrl + "/qllxResource/yqlList?proid=" + proid + PARAMETER_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid;
                }
                if (CollectionUtils.isNotEmpty(bdcBdcdyList) && bdcBdcdyList.size() > 1) {
                    url = bdcdjUrl + "/qllxResource/yqlList?proid=" + proid + PARAMETER_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid;
                }
            }
        } catch (Exception e) {
            url = bdcdjUrl + PARAMETER_INDEX_TOERROR_URL;
            logger.error("QllxResourceController.yqllxResourceIncludeFsss",e);
        }

        response.sendRedirect(url);
    }


    /**
     * zx 转移登记需要展现原权利信息
     *
     * @param proid
     * @throws Exception
     */
    @RequestMapping(value = "yDjbqllxResource", method = RequestMethod.GET)
    public void yDjbqllxResource(@RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, HttpServletResponse response) throws Exception {
        String url = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                QllxVo vo = qllxService.makeSureQllx(bdcXm);
                if (vo == null) {
                    response.sendRedirect(bdcdjUrl + PARAMETER_INDEX_TOERROR_URL);
                    return;
                }
                String tableName = qllxService.getTableName(vo).toLowerCase();
                url = AppConfig.getProperty("qllxDjbCpt.filepath");
                url = url.replace(PARAMETER_TABLENAME, tableName);
                url = PlatformUtil.initOptProperties(url);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    url = url + PARAMETER_BDCDYH + bdcBdcdy.getBdcdyh();
                }
            } else
                url = bdcdjUrl + PARAMETER_INDEX_TOERROR_URL;
        }
        response.sendRedirect(url);
    }

    /**
     * zx 转移登记需要展现原权利信息
     *
     * @param proid
     * @throws Exception
     */
    @RequestMapping(value = "djbQllxByProid", method = RequestMethod.GET)
    public void djbQllxByProid(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, HttpServletResponse response) throws Exception {
        String url = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                QllxVo vo = qllxService.makeSureQllx(bdcXm);
                String tableName = qllxService.getTableName(vo).toLowerCase();
                url = AppConfig.getProperty("qllxDjbCpt.filepath");
                url = url.replace(PARAMETER_TABLENAME, tableName);
                url = PlatformUtil.initOptProperties(url);
            }
            BdcBdcdy bdcBdcdy = null;
            if (bdcXm != null&&StringUtils.isEmpty(bdcdyh)) {
                bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            }

            if (bdcBdcdy != null)
                bdcdyh = bdcBdcdy.getBdcdyh();
            if (StringUtils.isNotBlank(bdcdyh))
                url = url + PARAMETER_BDCDYH + bdcdyh;
        }
        response.sendRedirect(url);

    }

    /**
     * zdd 一个流程中多个项目
     *
     * @param sidx
     * @param sord
     * @param sidx
     * @param sord
     * @param wiid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getQllxListByPage")
    public Object getQllxListByPage(Pageable pageable, String sidx, String sord, String wiid, String djlx, String sqlx,
                                    @RequestParam(value = "qlr", required = false) String qlr,
                                    @RequestParam(value = "yqlList", required = false) String yqlList) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Boolean needFilterQszt = true;
        if (StringUtils.isNotBlank(wiid)) {
            map.put("wiid", wiid);
        }
        if (StringUtils.isNotBlank(qlr)) {
            map.put("zl", qlr);
        }
        if (StringUtils.isNotBlank(yqlList)) {
            map.put(PARAMETER_YQLLIST, yqlList);
        }
        if (StringUtils.isNotBlank(djlx)) {
            map.put("djlx", djlx);
        }
        if (StringUtils.isNotBlank(sqlx) && Constants.SQLX_ZX_SFCD.equals(sqlx)) {
            needFilterQszt = false;
        }
        map.put("needFilterQszt", needFilterQszt.toString());
        return repository.selectPaging("getQlxxListByPage", map, pageable);
    }


    /**
     * @throws Exception
     * @author <a href=mailto:lijian@gtmap.cn>lijian</a>
     * @description 注销权利时展现原不动产单元列表
     */
    @RequestMapping("getBdcdyList")
    public void getBdcdyList(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "isYqlxx", required = false) String isYqlxx, HttpServletResponse response) throws Exception {
        String url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + "bdc_fdcq_spb_list_pl" + PARAMETER_OP_WRITE_CPTNAME + "bdc_fdcq_spb_list_pl";
        if (StringUtils.isBlank(isYqlxx))
            url += PARAMETER_PROID + proid;
        else if (StringUtils.isNotBlank(isYqlxx)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                String yproid = bdcXmRelList.get(0).getYproid();
                url += PARAMETER_PROID + yproid + PARAMETER_PRO_ID + proid;
            }
        }
        url = url + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
        response.sendRedirect(url);
    }

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 查看url
     **/
    @ResponseBody
    @RequestMapping(value = "getViewUrl", method = RequestMethod.POST)
    public String getViewUrl(@RequestParam(value = "proid", required = false) String proids) {
        String wiid = "";
        String[] proifArray = proids.split(",");
        String url = "";
        String cqzh = null;
        for(String proid : proifArray){
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    cqzh = bdcZsList.get(0).getBdcqzh();
                    List<BdcBdcZsSd> bdcBdcZsSdList = bdcdyService.getBdcBdcZsSdByCqzh(cqzh);
                    if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                        url = bdcdjUrl + "/bdcSjSd?sdlx=" + "bdcqz" + PARAMETER_PROID + bdcXm.getProid() + "&bdcqzh=" + bdcZsList.get(0).getBdcqzh();
                    }
                }
                if(StringUtils.isBlank(url)){
                    wiid = bdcXm.getWiid();
                    if (StringUtils.equals(bdcXm.getXmzt(), Constants.XMZT_LS) || StringUtils.equals(bdcXm.getXmzt(), Constants.XMZT_SZ)) {
                        //查询不到登记簿显示项目
                        if (StringUtils.isBlank(url)) {
                            url = portalUrl + "/projectHandle?wiid=" + wiid;
                        }
                    } else {
                        if (Boolean.parseBoolean(AppConfig.getProperty("viewDjb"))) {
                            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                                List<BdcBdcdjb> bdcdjbs = bdcdjbService.selectBdcdjb(bdcBdcdy.getBdcdyh().substring(0, 19));
                                if (CollectionUtils.isNotEmpty(bdcdjbs)) {
                                    if (StringUtils.isNotBlank(bdcXm.getQllx()) && StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_YGDJ)) {
                                        url = reportUrl + "/ReportServer?reportlet=bdcdj_djb%2Fbdc_yg.cpt&op=write&bdcdyh=" + bdcBdcdy.getBdcdyh() +
                                                PARAMETER_DJBID + bdcdjbs.get(0).getDjbid();
                                    } else if (StringUtils.isNotBlank(bdcXm.getQllx()) && StringUtils.equals(Constants.QLLX_DYAQ, bdcXm.getQllx())) {
                                        url = reportUrl + "/ReportServer?reportlet=bdcdj_djb%2Fbdc_dyaq.cpt&op=write&bdcdyh=" + bdcBdcdy.getBdcdyh() +
                                                PARAMETER_DJBID + bdcdjbs.get(0).getDjbid();
                                    } else if (StringUtils.isNotBlank(bdcXm.getQllx()) && StringUtils.equals(Constants.QLLX_CFDJ, bdcXm.getQllx())) {
                                        url = reportUrl + "/ReportServer?reportlet=bdcdj_djb%2Fbdc_cf.cpt&op=write&bdcdyh=" + bdcBdcdy.getBdcdyh() +
                                                PARAMETER_DJBID + bdcdjbs.get(0).getDjbid();
                                    } else if (StringUtils.isNotBlank(bdcXm.getQllx()) && StringUtils.equals(Constants.QLLX_YYDJ, bdcXm.getQllx())) {
                                        url = reportUrl + "/ReportServer?reportlet=bdcdj_djb%2Fbdc_yy.cpt&op=write&bdcdyh=" + bdcBdcdy.getBdcdyh() +
                                                PARAMETER_DJBID + bdcdjbs.get(0).getDjbid();
                                    } else {
                                        url = reportUrl + "/ReportServer?reportlet=bdcdj_djb%2Fbdc_fdcq.cpt&op=write&bdcdyh=" + bdcBdcdy.getBdcdyh() +
                                                PARAMETER_DJBID + bdcdjbs.get(0).getDjbid();
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                BdcBdcdySd bdcBdcdySd = bdcdyService.queryBdcdySdById(proid);
                List<BdcBdcdySd> bdcBdcdySdList = bdcdyService.queryBdcdySdByBdcdyh(proid);
                BdcBdcZsSd bdcBdcZsSd=entityMapper.selectByPrimaryKey(BdcBdcZsSd.class, proid);
                if(bdcBdcZsSd!=null){
                    url = bdcdjUrl + "/bdcSjSd?sdlx=" + "bdcqz" + PARAMETER_PROID + bdcBdcZsSd.getProid() + "&bdcqzh=" + bdcBdcZsSd.getCqzh();
                }else if (bdcBdcdySd != null || CollectionUtils.isNotEmpty(bdcBdcdySdList)) {
                    if (bdcBdcdySd != null&&StringUtils.isNotBlank(bdcBdcdySd.getXzzt()) && StringUtils.equals(bdcBdcdySd.getXzzt(), Constants.XZZT_SD)) {
                        url = bdcdjUrl + "/bdcSjSd/bdcdySdxxList?bh=" + bdcBdcdySd.getBh() + PARAMETER_BDCDYH + bdcBdcdySd.getBdcdyh();
                    }
                    if (CollectionUtils.isNotEmpty(bdcBdcdySdList)) {
                        BdcBdcdySd bdcdySd = bdcBdcdySdList.get(0);
                        url = bdcdjUrl + "/bdcSjSd/bdcdySdxxList?bh=" + bdcdySd.getBh() + PARAMETER_BDCDYH + bdcdySd.getBdcdyh();
                    }
                }else{
                    //展现过渡权利，proid对应过渡权利的proid
                    List list = gdFwService.getGdQlListByGdproid(proid, 0, null);
                    List<GdFwsyq> gdFwsyqList = gdFwService.getGdFwsyqListByGdproid(proid,0);
                    List<GdTdsyq> gdTdsyqList = gdTdService.getGdTdsyqListByGdproid(proid, 0);
                    GdBdcSd gdBdcSd=entityMapper.selectByPrimaryKey(GdBdcSd.class, proid);
                    if (CollectionUtils.isNotEmpty(gdFwsyqList) || CollectionUtils.isNotEmpty(gdTdsyqList)) {
                        if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                            cqzh = gdFwsyqList.get(0).getFczh();
                        } else {
                            cqzh = gdTdsyqList.get(0).getTdzh();
                        }
                        List<GdBdcSd> gdBdcSdList = bdcdyService.getGdBdcSdByCqzh(cqzh);
                        if (CollectionUtils.isNotEmpty(gdBdcSdList)) {
                            url = bdcdjUrl + "/bdcSjSd?sdlx=" + "bdcqz" + PARAMETER_PROID + proids + "&bdcqzh=" + gdBdcSdList.get(0).getCqzh();
                        }
                    }
                    if(gdBdcSd!=null){
                        url = bdcdjUrl + "/bdcSjSd?sdlx=" + "bdcqz" + PARAMETER_PROID + proids + "&bdcqzh=" + gdBdcSd.getCqzh();
                    }
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (Object gdql : list) {
                            if (gdql instanceof GdDy) {
                                url = formUrl + "/gdQlxx/gdDyxx?qlid=" + ((GdDy) gdql).getDyid();
                                break;
                            } else if (gdql instanceof GdYg) {
                                url = formUrl + "/gdQlxx/gdYgxx?qlid=" + ((GdYg) gdql).getYgid();
                                break;
                            } else if (gdql instanceof GdCf) {
                                url = formUrl + "/gdQlxx/gdCfxx?qlid=" + ((GdCf) gdql).getCfid();
                                break;
                            }
                        }
                    }
                }
            }
            if(StringUtils.isNotBlank(url)){
                break;
            }
        }

        return url;
    }

    @RequestMapping("sendUrl")
    public void sendUrl(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "updateQllx", required = false) String updateQllx, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, HttpServletResponse response) throws Exception {
        String url = "";
        if (StringUtils.isNoneBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            List<BdcBdcdy> bdcBdcdyList = null;
            if (StringUtils.isNoneBlank(wiid)) {
                bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                bdcBdcdyList = bdcdyService.queryBdcBdcdy(wiid);
            }
            String djlx = "";
            List<String> qllxList = new ArrayList<String>();
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                djlx = bdcXmList.get(0).getDjlx();
                for (BdcXm bdcXm : bdcXmList) {
                    if (CollectionUtils.isNotEmpty(qllxList)) {
                        if (!qllxList.contains(bdcXm.getQllx())) {
                            qllxList.add(bdcXm.getQllx());
                        }
                    } else {
                        qllxList.add(bdcXm.getQllx());
                    }
                }
            }
            if ((StringUtils.equals(djlx, Constants.DJLX_CFDJ_DM) || StringUtils.equals(djlx, Constants.SQLX_PLJF)) && CollectionUtils.isNotEmpty(bdcBdcdyList) && qllxList.size() == 1) {
                if (StringUtils.isNotBlank(proid)) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(proid);
                    QllxVo vo = qllxService.makeSureQllx(bdcXm);
                    //如果无法确定权利类型，则返回错误页面
                    if (vo == null || bdcBdcdy == null) {
                        response.sendRedirect(bdcdjUrl + PARAMETER_INDEX_TOERROR_URL);
                        return;
                    }
                    //zdd 防止冲掉用户填写的信息  所以如果没生成过权利类型   重新生成
                    if (StringUtils.isNotBlank(updateQllx) && qllxService.queryQllxVo(vo, proid) == null) {
                        TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                        turnProjectService.saveQllxVo(bdcXm);
                    }
                    url = AppConfig.getProperty(CONFIGURATION_PARAMETER_QLLXCPT_FILEPATH);
                    String tableName = qllxService.getTableName(vo).toLowerCase();
                    url = url.replace(PARAMETER_TABLENAME, tableName);
                    url = PlatformUtil.initOptProperties(url);
                    if (bdcBdcdyList != null && bdcBdcdyList.size() > 1 && StringUtils.isBlank(getSimpleCpt)) {
                        url = reportUrl + PARAMETER_REPORTSERVER_EDIT_MUL_URL + tableName + PARAMETER_OP_WRITE_CPTNAME + tableName;
                    } else if (StringUtils.isBlank(getSimpleCpt)) {
                        url = reportUrl + PARAMETER_REPORTSERVER_EDIT_URL + tableName + PARAMETER_OP_WRITE_CPTNAME + tableName;
                    }

                    if (StringUtils.equals(getSimpleCpt, "true"))
                        url = url + PARAMETER_BDCDYH + bdcdyh + PARAMETER_BDCDYID + bdcdyid;
                }
                if (__showtoolbar__ == null)
                    __showtoolbar__ = "";
                if (StringUtils.isNotBlank(taskid))
                    url = url + PARAMETER_PROID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
                else
                    url = url + PARAMETER_PROID + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__;
                response.sendRedirect(url);
            } else {
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    if (bdcXmList.size() == 1) {
                        url = bdcdjUrl + "/qllxResource/qlxxIncludeFsss";
                    } else {
                        if (StringUtils.equals(bdcXmList.get(0).getDjlx(), Constants.DJLX_DYDJ_DM))
                            url = bdcdjUrl + "/qllxResource/dyList";
                        else
                            url = bdcdjUrl + "/qllxResource/list";
                    }
                } else {
                    url = bdcdjUrl + PARAMETER_INDEX_TOERROR_URL;
                }
                url = url + "?proid=" + proid + PARAMETER_YWTYPE_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
                response.sendRedirect(url);
            }
        }
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 在权利列表删除一个权利
     */
    @ResponseBody
    @RequestMapping(value = "delBdcQllxVo")
    public HashMap delBdcQllxVo(String wiid, String proid) {
        HashMap map = new HashMap();
        String msg = "";
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.size() < 2) {
            msg = "当前项目只有一条权利，不得删除";
            map.put("flag","false");
            map.put("msg",msg);
        }
        if (StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            projectService.delProjectEvent(projectService.getDelProjectService(bdcXm), bdcXm);
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
            if (pfWorkFlowInstanceVo != null && pfWorkFlowInstanceVo.getProId().equals(proid)) {
                //如果删除的是当前工作流实例的主proid，则更新平台实例proid
                bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    String newProid = bdcXmList.get(0).getProid();
                    pfWorkFlowInstanceVo.setProId(newProid);
                    sysWorkFlowInstanceService.deleteWorkFlowIntance(wiid);
                    sysWorkFlowInstanceService.createWorkFlowIntance(pfWorkFlowInstanceVo);
                    Space space = fileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF");
                    com.gtis.fileCenter.model.Node tempNode = fileCenterNodeServiceImpl.getNode(space.getId(), proid, true);
                    tempNode.setName(newProid);
                    fileCenterNodeServiceImpl.save(tempNode);
                }
            }
            map.put("flag","true");
            map.put("msg","删除成功");
        }
        return map;
    }

    /**
     * @param proid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 异步获取权利人名称
    */
    @ResponseBody
    @RequestMapping(value = "/getQlrmcByProid")
    public Map getQlrmcByProid(String proid){
        Map resultMap = Maps.newHashMap();
        if(StringUtils.isNoneBlank(proid)){
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            resultMap.put("qlrmc",bdcQlrService.combinationQlr(bdcQlrList));
        }
        return resultMap;
    }
}
