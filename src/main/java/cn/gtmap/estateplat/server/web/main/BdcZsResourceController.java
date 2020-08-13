package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * 通过后台代码控制项目中应该打开的证书资源url
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-30
 */
@Controller
@RequestMapping("/bdcZsResource")
public class BdcZsResourceController extends BaseController {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private  BdcQlrService bdcQlrService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;

    private static final String PARAMETER_ZSID = "&zsid=";
    private static final String PARAMETER_PROID = "&proid=";
    private static final String PARAMETER_BDCDYID = "&bdcdyid=";
    private static final String PARAMETER_RID = "&rid=";
    private static final String PARAMETER_FROM = "&from=";
    private static final String PARAMETER_WIID = "&wiid=";
    private static final String PARAMETER_TASKID = "&taskid=";
    private static final String PARAMETER_SHOWTOOLBAR = "&__showtoolbar__=";
    private static final String PARAMETER_WORKFLOWPROID = "workflowProid";
    private static final String PARAMETER_BDCZSRESOURCE = "/bdcZsResource";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void index(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, String updateZs, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "zsid", required = false) String zsid, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, HttpServletResponse response) throws Exception {
        String zsUrl = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            String zsFont = qllxService.makeSureBdcqzlx(qllxVo);
            //zdd 如果项目中已经存在不动产权证书  则不更新  防止将用户信息冲掉
            if (StringUtils.isNotBlank(updateZs) && bdcZsService.queryBdcZsByProid(proid) == null) {
                TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                turnProjectService.saveBdcZs(bdcXm);
                //zdd 数据管理中生成证书即为项目办结
                EndProjectService endProjectService = projectService.getEndProjectService(bdcXm);
                projectService.endProjectEvent(endProjectService, bdcXm);
            }
            String tableName = "";
            if (Constants.BDCQZM_BH_FONT.equals(zsFont)) {
                zsUrl = AppConfig.getProperty("bdczmcpt.filepath");
                tableName = "bdcqzms";
            } else {
                zsUrl = AppConfig.getProperty("bdczscpt.filepath");
                tableName = "bdc_bdcqz";
            }
            zsUrl = PlatformUtil.initOptProperties(zsUrl);
            //参数修改为wiid @gtsy
            //jyl 过滤不动产房屋附属设施
            List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdyFilterBdcFwfsss(bdcXm.getWiid());
            if (bdcBdcdyList != null && bdcBdcdyList.size() > 1 && StringUtils.isBlank(getSimpleCpt)) {
                zsUrl = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/mul/" + tableName + "&op=write&cptName=" + tableName + "&ywType=server";
            }
            if (StringUtils.equals(getSimpleCpt, "true"))
                zsUrl = zsUrl + PARAMETER_ZSID + zsid;

            //zwq  证明单的时候用getSimpleCpt识别，url是bdc_fwcq_zmd
            if (StringUtils.equals(getSimpleCpt, "zmd")) {
                tableName = "bdc_fwcq_zmd";
                zsUrl = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/mul/" + tableName + "&op=write&cptName=" + tableName + "&ywType=server";
                zsUrl = zsUrl + PARAMETER_BDCDYID + bdcdyid;
            }
            if (StringUtils.equals(getSimpleCpt, "zs")) {
                tableName = "bdc_bdcqz";
                zsUrl = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/mul/" + tableName + "&op=write&cptName=" + tableName + "&ywType=server";
                zsUrl = zsUrl + PARAMETER_BDCDYID + bdcdyid;
            }
        }
        if (__showtoolbar__ == null)
            __showtoolbar__ = "";
        if (StringUtils.isNotBlank(taskid))
            response.sendRedirect(zsUrl + PARAMETER_PROID + proid + PARAMETER_RID + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__);
        else
            response.sendRedirect(zsUrl + PARAMETER_PROID + proid + PARAMETER_SHOWTOOLBAR + __showtoolbar__);
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public void edit(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, String updateZs, HttpServletResponse response) throws Exception {
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            String zsFont = qllxService.makeSureBdcqzlx(qllxVo);
            //zdd 如果项目中已经存在不动产权证书  则不更新  防止将用户信息冲掉
            if (StringUtils.isNotBlank(updateZs) && bdcZsService.queryBdcZsByProid(proid) == null) {
                TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                turnProjectService.saveBdcZs(bdcXm);
                //zdd 数据管理中生成证书即为项目办结
                EndProjectService endProjectService = projectService.getEndProjectService(bdcXm);
                projectService.endProjectEvent(endProjectService, bdcXm);
            }
            if (Constants.BDCQZM_BH_FONT.equals(zsFont)) {
                reportUrl = AppConfig.getProperty("bdczmEditcpt.filepath");
            } else {
                reportUrl = AppConfig.getProperty("bdczsEditcpt.filepath");
            }
            reportUrl = PlatformUtil.initOptProperties(reportUrl);
        }
        if (__showtoolbar__ == null)
            __showtoolbar__ = "";
        response.sendRedirect(reportUrl + PARAMETER_PROID + proid + PARAMETER_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__);
    }

    /**
     * zdd 获取原项目的不动产权证书（证明）
     *
     * @param proid
     * @param updateZs
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "ybdcZsResource", method = RequestMethod.GET)
    public String ybdcZsResource(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, String updateZs, Model model, HttpServletResponse response) throws Exception {
        String url = "";
        String yproid = "";
        //获取过渡数据的原不动产权证
        String bdcdyid = "";
        BdcXm xbdcXm = null;
        if (StringUtils.isNotBlank(proid))
            xbdcXm = bdcXmService.getBdcXmByProid(proid);
        if (xbdcXm != null) {
            //判断是新建还是匹配需要原不动产权证
            try {
                if (StringUtils.isNotBlank(proid)) {

                    List<BdcXmRel> list = bdcXmRelService.queryBdcXmRelByProid(proid);
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (BdcXmRel bdcXmRel : list) {
                            if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                                yproid = bdcXmRel.getYproid();
                                bdcdyid = xbdcXm.getBdcdyid();
                                break;
                            }
                        }
                    } else {
                        response.sendRedirect(bdcdjUrl + "/index/toError");
                    }

                    if (StringUtils.isNotBlank(yproid)) {
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(yproid);
                        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                        String zsFont = qllxService.makeSureBdcqzlx(qllxVo);
                        //zdd 如果项目中已经存在不动产权证书  则不更新  防止将用户信息冲掉
                        if (StringUtils.isNotBlank(updateZs) && bdcZsService.queryBdcZsByProid(yproid) == null) {
                            TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                            turnProjectService.saveBdcZs(bdcXm);
                            //zdd 数据管理中生成证书即为项目办结
                            EndProjectService endProjectService = projectService.getEndProjectService(bdcXm);
                            projectService.endProjectEvent(endProjectService, bdcXm);
                        }
                        if (Constants.BDCQZM_BH_FONT.equals(zsFont)) {
                            reportUrl = AppConfig.getProperty("bdczmcpt.filepath");
                        } else {
                            reportUrl = AppConfig.getProperty("bdczscpt.filepath");
                        }
                        reportUrl = PlatformUtil.initOptProperties(reportUrl);
                    }

                }
                if (__showtoolbar__ == null)
                    __showtoolbar__ = "";
                url = reportUrl + PARAMETER_PROID + yproid + PARAMETER_BDCDYID + bdcdyid + PARAMETER_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__;

            } catch (Exception e) {
                url = bdcdjUrl + "/index/toError";
                logger.error("BdcZsResourceController.ybdcZsResource",e);
            }
            response.sendRedirect(url);
            return null;
        } else {
            url = bdcdjUrl + "/index/toError";
            response.sendRedirect(url);
            return null;
        }
    }


    @RequestMapping(value = "printZs", method = RequestMethod.GET)
    public void printZs(@RequestParam(value = "zsid", required = false) String zsid, String updateZs, HttpServletResponse response) throws Exception {
        String zsUrl = "";
        if (StringUtils.isNotBlank(zsid)) {
            String proid = bdcXmZsRelService.getProidByZsid(zsid);
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                String zsFont = qllxService.makeSureBdcqzlx(qllxVo);
                //zdd 如果项目中已经存在不动产权证书  则不更新  防止将用户信息冲掉
                if (StringUtils.isNotBlank(updateZs) && bdcZsService.queryBdcZsByProid(proid) == null) {
                    TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                    turnProjectService.saveBdcZs(bdcXm);
                    //zdd 数据管理中生成证书即为项目办结
                    EndProjectService endProjectService = projectService.getEndProjectService(bdcXm);
                    projectService.endProjectEvent(endProjectService, bdcXm);
                }
                if (Constants.BDCQZM_BH_FONT.equals(zsFont)) {
                    zsUrl = AppConfig.getProperty("bdczmPrintcpt.filepath");
                } else {
                    zsUrl = AppConfig.getProperty("bdczsPrintcpt.filepath");
                }
                zsUrl = PlatformUtil.initOptProperties(zsUrl);
                //参数修改为wiid @gtsy
                List<BdcBdcdy> bdcBdcdyList = bdcdyService.queryBdcBdcdy(bdcXm.getWiid());
                if (bdcBdcdyList != null && bdcBdcdyList.size() > 1 && Constants.BDCQZM_BH_FONT.equals(zsFont)) {
                    zsUrl = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\print\\mul\\bdcqzmsPrint&op=write&cptName=bdcqzmsPrint&ywType=server";

                }
                response.sendRedirect(zsUrl + PARAMETER_PROID + proid + PARAMETER_ZSID + zsid);
            }
        }

    }

    /*
    * zwq
    * 将图片在证书或者证明书上显示的方法
    * 2015-9-22
    * */
    @RequestMapping(value = "displayZsZm", method = RequestMethod.GET)
    public void displayZsZm(@RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "qszt", required = false) String qszt, @RequestParam(value = "cfdyzt", required = false) String cfdyzt, @RequestParam(value = "iszs", required = false) String iszs, @RequestParam(value = "zsid", required = false) String zsid, @RequestParam(value = "zstype", required = false) String zstype, HttpServletResponse response) throws Exception {
        String url = "";
        String number = "0";
        if (StringUtils.isNotBlank(proid)) {
            if (StringUtils.isNotBlank(iszs)) {
                if (StringUtils.equals(iszs, "1")) {
                    /**
                     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                     * @description 芜湖县 商品房首次登记证
                     */
                    if (zstype.contains(Constants.BDCQSCDJZ_BH_FONT)) {
                        url = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\edit\\mul\\bdc_fwcq_zmd&op=write&cptName=bdc_fwcq_zmd&ywType=server";
                    } else {
                        url = reportUrl + "/ReportServer?reportlet=edit%2Fbdc_bdcqz.cpt&op=write" + "&ywType=server";
                    }
                } else {
                    /**
                     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
                     * @description 如果无法通过proid查询出项目, 抛出异常, 如果项目的wiid为空, 抛出异常
                     */
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                    if (bdcXm == null) {
                        throw new AppException("未能通过项目ID查询到不动产项目", AppException.ENTITY_NOT_FOUND, new Object());
                    }
                    if (StringUtils.isBlank(bdcXm.getWiid())) {
                        throw new AppException("不动产项目工作流定义ID(wiid)为空", AppException.ENTITY_NO_ID, new Object());
                    }
                    url = bdcdjUrl + "/sysResource/filterCpt?cptName=bdcqzms&path=edit&mulPath=mul&op=write&wiid=" + bdcXm.getWiid();
                }
            }

            if (StringUtils.isBlank(qszt))
                qszt = "0";
            if (StringUtils.isBlank(cfdyzt))
                cfdyzt = "0";

            if (StringUtils.equals(qszt, "1")) {
                number = qszt;
            } else if (StringUtils.equals(cfdyzt, "2") || StringUtils.equals(cfdyzt, "3")) {
                number = cfdyzt;
            }

            response.sendRedirect(url + PARAMETER_PROID + proid + "&number=" + number + PARAMETER_ZSID + zsid + "&isyl=true");
        }
    }


    /**
     * zdd 一个流程多个项目的  证书信息列表展现
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
    public String list(Model model, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "isyl", required = false) String isyl) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String createSqlxdm = "";
        //获取平台的申请类型代码,主要为了合并
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                createSqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        String djlxdm = bdcZdGlService.getDjlxDmBySqlxdm(createSqlxdm);
        model.addAttribute("djlx", djlxdm);

        if (StringUtils.isNotBlank(bdcXm.getSqlx())) {
            model.addAttribute("sqlx", bdcXm.getSqlx());
        } else {
            model.addAttribute("sqlx", "");
        }
        if(StringUtils.isNotBlank(createSqlxdm)){
            model.addAttribute("createSqlxdm",createSqlxdm);
        }
        model.addAttribute(PARAMETER_WORKFLOWPROID, proid);
        model.addAttribute("rid", rid);
        model.addAttribute(ParamsConstants.TASKID_LOWERCASE, taskid);
        model.addAttribute("from", from);
        model.addAttribute("wiid", wiid);
        model.addAttribute("isyl", isyl);
        return "main/wfBdcZsList";
    }

    /**
     * zdd 一个流程中多个项目
     *
     * @param sidx
     * @param sord
     * @param wiid
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBdcZsListByPage")
    public Object getQllxListByPage(Pageable pageable, String sidx, String sord, String wiid, @RequestParam(value = "qlr", required = false) String qlr, @RequestParam(value = "fzlx", required = false) String fzlx) {
        //lst 如果没有工作流项目ID   则不显示数据
        if (StringUtils.isBlank(wiid))
            wiid = "wiid";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("wiid", wiid);
        map.put("zl", qlr);
        map.put("fzlx", fzlx);
        return repository.selectPaging("getBdcZsListByPage", map, pageable);
    }

    /*
    * zwq 不动产房屋产权证明单
    *
    *
    *
    * */

    @RequestMapping(value = "zmd", method = RequestMethod.GET)
    public String displayZmd(Model model, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        model.addAttribute(PARAMETER_WORKFLOWPROID, proid);
        model.addAttribute("rid", rid);
        model.addAttribute(ParamsConstants.TASKID_LOWERCASE, taskid);
        model.addAttribute("from", from);
        model.addAttribute("wiid", wiid);
        List<String> proidList = new ArrayList<String>();
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (int i = 0; i < bdcXmList.size(); i++) {
                proidList.add(bdcXmList.get(i).getProid());
            }
        }
        model.addAttribute("proidList", proidList);
        model.addAttribute("reportUrl", reportUrl);
        return "main/wfBdcFwcqZmd";
    }

    /**
     * 查询zmd台账的数据  增加判断数据是否为 null
     *
     * @param pageable
     * @param workflowProid
     * @param zl
     * @return
     * @author wuhongrui
     * @description
     */

    @ResponseBody
    @RequestMapping("/getZmdData")
    public Object getZmdDataJosn(Pageable pageable, String workflowProid, @RequestParam(value = "zl", required = false) String zl, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "fzlx", required = false) String fzlx) {
        if (StringUtils.isBlank(workflowProid)) {
            workflowProid = PARAMETER_WORKFLOWPROID;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("proid", workflowProid);
        map.put("zl", zl);
        if (StringUtils.isNotBlank(bdcdyh)) {
            bdcdyh = StringUtils.deleteWhitespace(bdcdyh);
            map.put("bdcdyh", bdcdyh);
        }
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(workflowProid);
        if(bdcXm!=null) {
            if(!StringUtils.equals(Constants.SQLX_TDSCDJXXBBDJ_DM,bdcXm.getSqlx())) {
                //liujie 过滤掉不发证的不动产单元
                map.put("fzlx", fzlx);
            }
        }
        return repository.selectPaging("getZmdDataListByPage", map, pageable, "getZmdDataIsNull");
    }


    /**
     * lst 验证不动产权证号是否重复
     *
     * @param zsid
     * @param bdcqzh
     * @return
     */
    @ResponseBody
    @RequestMapping("/hasBdcqzh")
    public HashMap<String, Object> hasBdcqzh(String zsid, String bdcqzh) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Example example = new Example(BdcZs.class);
        example.createCriteria().andNotEqualTo("zsid", zsid).andEqualTo("bdcqzh", bdcqzh);
        List<BdcZs> list = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            map.put("result", false);
        } else {
            map.put("result", true);
        }
        return map;
    }

    @RequestMapping(value = "ylzs", method = RequestMethod.GET)
    public void ylzs(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, String updateZs, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "zsid", required = false) String zsid, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, HttpServletResponse response) throws Exception {
        String zsUrl = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            //zdd 如果项目中已经存在不动产权证书  则不更新  防止将用户信息冲掉
            if (StringUtils.isNotBlank(updateZs) && bdcZsService.queryBdcZsByProid(proid) == null) {
                TurnProjectService turnProjectService = projectService.getTurnProjectService(bdcXm);
                turnProjectService.saveBdcZs(bdcXm);
                //zdd 数据管理中生成证书即为项目办结
                EndProjectService endProjectService = projectService.getEndProjectService(bdcXm);
                projectService.endProjectEvent(endProjectService, bdcXm);
            }
            String tableName = "";
            QllxVo qlvo = qllxService.makeSureQllx(bdcXm);
            String zsFont = qllxService.makeSureBdcqzlx(qlvo);
            if (Constants.BDCQZM_BH_FONT.equals(zsFont)) {
                zsUrl = AppConfig.getProperty("bdczmViewcpt.filepath");
            } else {
                zsUrl = AppConfig.getProperty("bdczsViewcpt.filepath");
            }
            zsUrl = PlatformUtil.initOptProperties(zsUrl);


            if (StringUtils.equals(getSimpleCpt, "true"))
                zsUrl = zsUrl + PARAMETER_ZSID + zsid;

            //zwq  证明单的时候用getSimpleCpt识别，url是bdc_fwcq_zmd
            if (StringUtils.equals(getSimpleCpt, "zmd")) {
                tableName = "bdc_fwcq_zmd";
                zsUrl = reportUrl + "/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/mul/" + tableName + "&op=write&cptName=" + tableName + "&ywType=server";
                zsUrl = zsUrl + PARAMETER_BDCDYID + bdcdyid;
            }
        }
        if (__showtoolbar__ == null)
            __showtoolbar__ = "";
        if (StringUtils.isNotBlank(taskid))
            response.sendRedirect(zsUrl + PARAMETER_PROID + proid + PARAMETER_RID + rid + PARAMETER_TASKID + taskid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__);
        else
            response.sendRedirect(zsUrl + PARAMETER_PROID + proid + PARAMETER_SHOWTOOLBAR + __showtoolbar__);
    }

    /*
   * zwq 不动产房屋产权证明单
   *
   *
   *
   * */

    @RequestMapping(value = "mulzs", method = RequestMethod.GET)
    public String mulzs(Model model, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid) {
        model.addAttribute(PARAMETER_WORKFLOWPROID, proid);
        model.addAttribute("rid", rid);
        model.addAttribute(ParamsConstants.TASKID_LOWERCASE, taskid);
        model.addAttribute("from", from);
        model.addAttribute("wiid", wiid);
        model.addAttribute("reportUrl", reportUrl);
        return "main/wfBdcZSMul";
    }

    @RequestMapping("openBdcZs")
    public String openBdcZs(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "updateQllx", required = false) String updateQllx, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, @RequestParam(value = "isyl", required = false) String isyl, HttpServletResponse response) throws Exception {
            String url = "";
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);

            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<String> zsidList = new ArrayList<String>();
                for (BdcXm bdcXm : bdcXmList) {
                    List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
                        for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                            if (!zsidList.contains(bdcXmzsRel.getZsid()))
                                zsidList.add(bdcXmzsRel.getZsid());
                        }
                    }
                }
                if (zsidList.size() > 1) {
                    url = bdcdjUrl + "/bdcZsResource/list?isyl=" + isyl;
                    url = url + PARAMETER_PROID + proid + "&ywType=server&rid=" + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
                } else {
                    url = bdcdjUrl + PARAMETER_BDCZSRESOURCE;
                    url = url + "?proid=" + proid + "&ywType=server&rid=" + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
                }
            } else {
                url = bdcdjUrl + "/index/toError";
            }
        }
        return "redirect:" + url;
    }

    @RequestMapping("sendUrl")
    public void sendUrl(@RequestParam(value = "__showtoolbar__", required = false) String __showtoolbar__, @RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "updateQllx", required = false) String updateQllx, @RequestParam(value = "getSimpleCpt", required = false) String getSimpleCpt, @RequestParam(value = "bdcdyh", required = false) String bdcdyh, @RequestParam(value = "bdcdyid", required = false) String bdcdyid, HttpServletResponse response) throws Exception {
        String url = "";
        if (StringUtils.isNoneBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                if (bdcXmList.size() == 1) {
                    url = bdcdjUrl + PARAMETER_BDCZSRESOURCE;
                } else {
                    url = bdcdjUrl + "/bdcZsResource/list";
                }
                if (StringUtils.equals(bdcXmList.get(0).getDjlx(), Constants.DJLX_DYDJ_DM)) {
                    List<BdcZs> bdcZsList = bdcZsService.getPlZsByWiid(wiid);
                    if (CollectionUtils.isNotEmpty(bdcZsList)){
                        if (bdcZsList.size() == 1) {
                            url = bdcdjUrl + PARAMETER_BDCZSRESOURCE;
                        } else {
                            url = bdcdjUrl + "/bdcZsResource/zmsList";
                        }
                    }
                }
            } else {
                url = bdcdjUrl + "/index/toError";
            }
        }
        url = url + "?proid=" + proid + "&ywType=server&rid=" + rid + PARAMETER_FROM + from + PARAMETER_WIID + wiid + PARAMETER_SHOWTOOLBAR + __showtoolbar__ + PARAMETER_TASKID + taskid;
        response.sendRedirect(url);
    }


    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @param proid
     * @return qlr
     * @description 根据proid获取qlr
     */
    @ResponseBody
    @RequestMapping(value = "getBdcQlr", method = RequestMethod.GET)
    public String getBdcQlr(String proid) {
        StringBuilder qlr = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    if (bdcQlr != null && StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                        qlr.append(bdcQlr.getQlrmc());
                        qlr.append(",");
                    }
                }
                if (StringUtils.isNotBlank(qlr)) {
                    qlr = new StringBuilder(StringUtils.substring(qlr.toString(), 0, qlr.length() - 1));
                }
            }
        }
        return qlr.toString();
    }

}
