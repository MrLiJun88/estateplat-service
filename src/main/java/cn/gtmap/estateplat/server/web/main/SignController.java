package cn.gtmap.estateplat.server.web.main;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtOpinion;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.SignService;
import cn.gtmap.estateplat.server.model.AutoSignWfNodeName;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.ReadJsonUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.*;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by zx on 2015/4/19.
 * 创建签名
 */
@Controller
@RequestMapping("/sign")
public class SignController extends BaseController {
    @Autowired
    SysWorkFlowInstanceService workFlowInstanceService;
    @Autowired
    SysSignService sysSignService;
    @Autowired
    SysWorkFlowDefineService workFlowDefineService;
    @Autowired
    SysTaskService sysTaskService;
    @Autowired
    SysOpinionService opinionService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    SignService signService;

    private static final String PARAMETER_SIGNVO_PROID = "signVo.proId";
    private static final String PARAMETER_SIGNVO_SIGNKEY = "signVo.signKey";
    private static final String PARAMETER_SIGNVO_SIGNID = "signVo.signId";
    private static final String PARAMETER_SIGNVO_SIGNOPINION = "signVo.signOpinion";

    /**
     * 开始进入签名
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String sign(Model model, HttpServletRequest request, @RequestParam(value = "opinionType", required = false) String opinionType, @RequestParam(value = "signNoOptinion", required = false) String signNoOptinion) throws Exception {
        PfSignVo signVo = new PfSignVo();
        String taskid = "";
        String proids = "";
        if (request != null) {
            signVo.setProId(request.getParameter(PARAMETER_SIGNVO_PROID));
            signVo.setSignKey(request.getParameter(PARAMETER_SIGNVO_SIGNKEY));
            signVo.setSignId(request.getParameter(PARAMETER_SIGNVO_SIGNID));
            taskid = request.getParameter(ParamsConstants.TASKID_LOWERCASE);
        }
        if (StringUtils.isNotBlank(signVo.getSignId())) {
            signVo = sysSignService.getSign(signVo.getSignId());
        }
        if (signVo == null) {
            signVo = new PfSignVo();
            if (request != null) {
                signVo.setProId(request.getParameter(PARAMETER_SIGNVO_PROID));
                signVo.setSignKey(request.getParameter(PARAMETER_SIGNVO_SIGNKEY));
                signVo.setSignId(request.getParameter(PARAMETER_SIGNVO_SIGNID));

            }
        }
        if (StringUtils.isNotBlank(signVo.getSignKey()))
            signVo.setSignKey(URLDecoder.decode(signVo.getSignKey(), Constants.DEFAULT_CHARSET));
        //获取签名意见类型
        if (StringUtils.isBlank(opinionType) && StringUtils.isNotBlank(signVo.getProId())) {
            PfWorkFlowInstanceVo workflowInstance = workFlowInstanceService.getWorkflowInstanceByProId(signVo.getProId());
            if (workflowInstance != null) {
                PfWorkFlowDefineVo workFlowDefineVo = workFlowDefineService.getWorkFlowDefine(workflowInstance.getWorkflowDefinitionId());
                opinionType = workFlowDefineVo.getWorkflowName();
            }
        }


        if (signVo.getSignDate() == null) {
            signVo.setSignDate(new Date(System.currentTimeMillis()));
        }
        if(request!=null) {
            if (StringUtils.isNotBlank(request.getParameter(PARAMETER_SIGNVO_PROID))) {
                proids = bdcXmService.getProidsByProid(request.getParameter(PARAMETER_SIGNVO_PROID));
            }
            signVo.setSignOpinion(request.getParameter(PARAMETER_SIGNVO_SIGNOPINION));
        }
        model.addAttribute("opinionType", opinionType);
        model.addAttribute("signVo", signVo);
        model.addAttribute(ParamsConstants.USERID_HUMP, super.getUserId());
        model.addAttribute(ParamsConstants.TASKID_LOWERCASE, taskid);
        model.addAttribute(ParamsConstants.PROIDS_LOWERCASE, proids);
        model.addAttribute("splitStr", Constants.SPLIT_STR);
        if (StringUtils.isNotBlank(signNoOptinion) && StringUtils.equals(signNoOptinion, "true"))
            return "/main/signNo";
        else
            return "/main/sign";
    }

    @ResponseBody
    @RequestMapping(value = "/autosign", method = RequestMethod.POST)
    public HashMap autosign(Model model, HttpServletRequest request, @RequestParam(value = "opinionType", required = false) String opinionType) throws Exception {
        HashMap msg= Maps.newHashMap();

        if (request != null && request.getParameter(PARAMETER_SIGNVO_PROID) != null) {

            PfSignVo signVo = new PfSignVo();
            signVo.setProId(request.getParameter(PARAMETER_SIGNVO_PROID));
            if(StringUtils.isNotBlank(request.getParameter(PARAMETER_SIGNVO_SIGNKEY))){
                signVo.setSignKey(URLDecoder.decode(request.getParameter(PARAMETER_SIGNVO_SIGNKEY), Constants.DEFAULT_CHARSET));
            }
            signVo.setSignId(request.getParameter(PARAMETER_SIGNVO_SIGNID));
            if(StringUtils.isNotBlank(request.getParameter(PARAMETER_SIGNVO_SIGNOPINION))){
                signVo.setSignOpinion(URLDecoder.decode(request.getParameter(PARAMETER_SIGNVO_SIGNOPINION),Constants.DEFAULT_CHARSET));
            }
            signVo.setSignType("1");
            signVo.setUserId(super.getUserId());
            signVo.setSignName(super.getUserName());
            signVo.setSignDate(Calendar.getInstance().getTime());

            List<PfSignVo> signList = sysSignService.getSignList(signVo.getSignKey(), request.getParameter(PARAMETER_SIGNVO_PROID));
            if (CollectionUtils.isNotEmpty(signList)) {
                PfSignVo sign = signList.get(0);
                sign.setSignType("1");
                sign.setSignKey(signVo.getSignKey());
                sign.setUserId(signVo.getUserId());
                sign.setSignName(signVo.getSignName());
                sign.setSignOpinion(signVo.getSignOpinion());
                sign.setSignDate(signVo.getSignDate());
                sysSignService.updateAutoSign(sign);
            } else {
                if (signVo.getSignDate() == null) {
                    signVo.setSignDate(Calendar.getInstance().getTime());
                }
                sysSignService.insertAutoSign(signVo);
            }
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 初审意见更新
             */
            if(StringUtils.isNotBlank(request.getParameter("signVo.csOpinion"))){
                List<PfSignVo> csSignList = sysSignService.getSignList(Constants.SHRLX_CSR_MC, request.getParameter(PARAMETER_SIGNVO_PROID));
                if(CollectionUtils.isNotEmpty(csSignList)){
                    PfSignVo csSignVo = csSignList.get(0);
                    if(StringUtils.isNotBlank(csSignVo.getSignId())){
                        csSignVo.setSignOpinion(URLDecoder.decode(request.getParameter("signVo.csOpinion"),Constants.DEFAULT_CHARSET));
                        sysSignService.updateAutoSign(csSignVo);
                    }
                }
            }
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 复审意见更新
             */
            if(StringUtils.isNotBlank(request.getParameter("signVo.fsOpinion"))){
                List<PfSignVo> fsSignList = sysSignService.getSignList(Constants.SHRLX_FSR_MC, request.getParameter(PARAMETER_SIGNVO_PROID));
                if(CollectionUtils.isNotEmpty(fsSignList)){
                    PfSignVo fsSignVo = fsSignList.get(0);
                    if(StringUtils.isNotBlank(fsSignVo.getSignId())){
                        fsSignVo.setSignOpinion(URLDecoder.decode(request.getParameter("signVo.fsOpinion"),Constants.DEFAULT_CHARSET));
                        sysSignService.updateAutoSign(fsSignVo);
                    }
                }
            }
        }

        msg.put("msg","ok");
        return msg;
    }

    /**
     * 签名图片
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void image(Model model, HttpServletResponse resp, HttpServletRequest request) throws Exception {
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", -1);
        PfSignVo signVo = new PfSignVo();

        if (request != null) {
            signVo.setSignId(request.getParameter(PARAMETER_SIGNVO_SIGNID));
        }
        if (StringUtils.isNotBlank(signVo.getSignId())) {

            PfSignVo sign = sysSignService.getSignImage(signVo.getSignId());
            if (sign == null) {
                IOUtils.write("", resp.getOutputStream());
            } else {
                if (sign.getSignImage() != null && sign.getSignImage().length > 1000) {
                    IOUtils.write(sign.getSignImage(), resp.getOutputStream());
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateSignOpinon", method = RequestMethod.POST)
    public void updateSignOpinon(Model model, HttpServletRequest request) throws Exception {
        if (request != null && StringUtils.isNotBlank(request.getParameter(ParamsConstants.PROID_LOWERCASE))) {
            String proid = request.getParameter(ParamsConstants.PROID_LOWERCASE);
            String userId = request.getParameter(ParamsConstants.USERID_HUMP);
            String slyj = request.getParameter("slyj");
            String slSignId = request.getParameter("slSignId");
            String slSignKey = request.getParameter("slSignKey");
            String csyj = request.getParameter("csyj");
            String csSignId = request.getParameter("csSignId");
            String csSignKey = request.getParameter("csSignKey");
            String csyj2 = request.getParameter("csyj2");
            String csSignId2 = request.getParameter("csSignId2");
            String csSignKey2 = request.getParameter("csSignKey2");
            String fsyj = request.getParameter("fsyj");
            String fsSignId = request.getParameter("fsSignId");
            String fsSignKey = request.getParameter("fsSignKey");
            String fsyj2 = request.getParameter("fsyj2");
            String fsSignId2 = request.getParameter("fsSignId2");
            String fsSignKey2 = request.getParameter("fsSignKey2");
            String hdyj = request.getParameter("hdyj");
            String hdSignId = request.getParameter("hdSignId");
            String hdSignKey = request.getParameter("hdSignKey");
            String proids = request.getParameter(ParamsConstants.PROIDS_LOWERCASE);
            if (StringUtils.isNotBlank(request.getParameter(ParamsConstants.PROID_LOWERCASE))) {
                proids = bdcXmService.getProidsByProid(request.getParameter(PARAMETER_SIGNVO_PROID));
                if (StringUtils.isBlank(proids))
                    proids = request.getParameter(ParamsConstants.PROID_LOWERCASE);
            }
            String[] proidStr = null;
            if (StringUtils.isNotBlank(proids))
                proidStr = proids.split("["+Constants.SPLIT_STR+"]");
            if (StringUtils.isNotBlank(slyj) && StringUtils.isNotBlank(slSignId)) {
                if (proidStr != null && proidStr.length > 0) {
                    //处理多个项目
                    for (String mulProid : proidStr) {
                        PfSignVo slSignVo = new PfSignVo();

                        slSignVo.setProId(mulProid);
                        slSignVo.setSignKey(slSignKey);
                        slSignVo.setSignId(slSignId);
                        slSignVo.setSignOpinion(slyj);
                        slSignVo.setSignType("1");
                        slSignVo.setUserId(userId);
                        slSignVo.setSignName(super.getUserNameById(userId));

                        updateSign(slSignVo);
                    }
                } else {
                    PfSignVo slSignVo = new PfSignVo();

                    slSignVo.setProId(proid);
                    slSignVo.setSignKey(slSignKey);
                    slSignVo.setSignId(slSignId);
                    slSignVo.setSignOpinion(slyj);
                    slSignVo.setSignType("1");
                    slSignVo.setUserId(userId);
                    slSignVo.setSignName(super.getUserNameById(userId));

                    updateSign(slSignVo);
                }
            }
            if (StringUtils.isNotBlank(csyj)  && StringUtils.isNotBlank(csSignId)) {
                if (proidStr != null && proidStr.length > 0) {
                    //处理多个项目
                    for (String mulProid : proidStr) {
                        PfSignVo csSignVo = new PfSignVo();

                        csSignVo.setProId(mulProid);
                        csSignVo.setSignKey(csSignKey);
                        csSignVo.setSignId(csSignId);
                        csSignVo.setSignOpinion(csyj);
                        csSignVo.setSignType("1");
                        csSignVo.setUserId(userId);
                        csSignVo.setSignName(super.getUserNameById(userId));
                        updateSign(csSignVo);
                    }
                } else {
                    PfSignVo csSignVo = new PfSignVo();

                    csSignVo.setProId(proid);
                    csSignVo.setSignKey(csSignKey);
                    csSignVo.setSignId(csSignId);
                    csSignVo.setSignOpinion(csyj);
                    csSignVo.setSignType("1");
                    csSignVo.setUserId(userId);
                    csSignVo.setSignName(super.getUserNameById(userId));
                    updateSign(csSignVo);
                }
            }
            //zwq初审意见2
            if (StringUtils.isNotBlank(csyj2)  && StringUtils.isNotBlank(csSignId2)) {
                if (proidStr != null && proidStr.length > 0) {
                    //处理多个项目
                    for (String mulProid : proidStr) {
                        PfSignVo csSignVo = new PfSignVo();

                        csSignVo.setProId(mulProid);
                        csSignVo.setSignKey(csSignKey2);
                        csSignVo.setSignId(csSignId2);
                        csSignVo.setSignOpinion(csyj2);
                        csSignVo.setSignType("1");
                        csSignVo.setUserId(userId);
                        csSignVo.setSignName(super.getUserNameById(userId));
                        updateSign(csSignVo);
                    }
                } else {
                    PfSignVo csSignVo = new PfSignVo();

                    csSignVo.setProId(proid);
                    csSignVo.setSignKey(csSignKey2);
                    csSignVo.setSignId(csSignId2);
                    csSignVo.setSignOpinion(csyj2);
                    csSignVo.setSignType("1");
                    csSignVo.setUserId(userId);
                    csSignVo.setSignName(super.getUserNameById(userId));
                    updateSign(csSignVo);
                }
            }

            if (StringUtils.isNotBlank(fsyj)  && StringUtils.isNotBlank(fsSignId)) {
                if (proidStr != null && proidStr.length > 0) {
                    //处理多个项目
                    for (String mulProid : proidStr) {
                        PfSignVo fsSignVo = new PfSignVo();

                        fsSignVo.setProId(mulProid);
                        fsSignVo.setSignKey(fsSignKey);
                        fsSignVo.setSignId(fsSignId);
                        fsSignVo.setSignOpinion(fsyj);
                        fsSignVo.setSignType("1");
                        fsSignVo.setUserId(userId);
                        fsSignVo.setSignName(super.getUserNameById(userId));
                        updateSign(fsSignVo);
                    }
                } else {
                    PfSignVo fsSignVo = new PfSignVo();

                    fsSignVo.setProId(proid);
                    fsSignVo.setSignKey(fsSignKey);
                    fsSignVo.setSignId(fsSignId);
                    fsSignVo.setSignOpinion(fsyj);
                    fsSignVo.setSignType("1");
                    fsSignVo.setUserId(userId);
                    fsSignVo.setSignName(super.getUserNameById(userId));
                    updateSign(fsSignVo);
                }
            }

            //复审意见2
            if (StringUtils.isNotBlank(fsyj2)  && StringUtils.isNotBlank(fsSignId2)) {
                if (proidStr != null && proidStr.length > 0) {
                    //处理多个项目
                    for (String mulProid : proidStr) {
                        PfSignVo fsSignVo = new PfSignVo();

                        fsSignVo.setProId(mulProid);
                        fsSignVo.setSignKey(fsSignKey2);
                        fsSignVo.setSignId(fsSignId2);
                        fsSignVo.setSignOpinion(fsyj2);
                        fsSignVo.setSignType("1");
                        fsSignVo.setUserId(userId);
                        fsSignVo.setSignName(super.getUserNameById(userId));
                        updateSign(fsSignVo);
                    }
                } else {
                    PfSignVo fsSignVo = new PfSignVo();

                    fsSignVo.setProId(proid);
                    fsSignVo.setSignKey(fsSignKey2);
                    fsSignVo.setSignId(fsSignId2);
                    fsSignVo.setSignOpinion(fsyj2);
                    fsSignVo.setSignType("1");
                    fsSignVo.setUserId(userId);
                    fsSignVo.setSignName(super.getUserNameById(userId));
                    updateSign(fsSignVo);
                }
            }


            if (StringUtils.isNotBlank(hdyj)  && StringUtils.isNotBlank(hdSignId)) {
                if (proidStr != null && proidStr.length > 0) {
                    //处理多个项目
                    for (String mulProid : proidStr) {
                        PfSignVo hdSignVo = new PfSignVo();

                        hdSignVo.setProId(mulProid);
                        hdSignVo.setSignKey(hdSignKey);
                        hdSignVo.setSignId(hdSignId);
                        hdSignVo.setSignOpinion(hdyj);
                        hdSignVo.setSignType("1");
                        hdSignVo.setUserId(userId);
                        hdSignVo.setSignName(super.getUserNameById(userId));
                        updateSign(hdSignVo);
                    }
                } else {
                    PfSignVo hdSignVo = new PfSignVo();

                    hdSignVo.setProId(proid);
                    hdSignVo.setSignKey(hdSignKey);
                    hdSignVo.setSignId(hdSignId);
                    hdSignVo.setSignOpinion(hdyj);
                    hdSignVo.setSignType("1");
                    hdSignVo.setUserId(userId);
                    hdSignVo.setSignName(super.getUserNameById(userId));
                    updateSign(hdSignVo);
                }
            }
        }
    }

    public void updateSign(PfSignVo signVo) {
        List<PfSignVo> signList = sysSignService.getSignList(signVo.getSignKey(), signVo.getProId());
        if (CollectionUtils.isNotEmpty(signList)) {
            PfSignVo sign = signList.get(0);
            sign.setSignOpinion(signVo.getSignOpinion());
            if (sign.getSignDate() == null) {
                signVo.setSignDate(Calendar.getInstance().getTime());
                sign.setSignDate(signVo.getSignDate());
            }
            sysSignService.updateAutoSign(sign);
        } else {
            if (StringUtils.isBlank(signVo.getSignId())) {
                signVo.setSignId(UUIDGenerator.generate());
            }
            if (signVo.getSignDate() == null) {
                signVo.setSignDate(Calendar.getInstance().getTime());
            }
            sysSignService.insertAutoSign(signVo);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public List<BdcXtOpinion> menu(Model model, HttpServletRequest request) throws Exception {
        List<BdcXtOpinion> lstOpinions = null;
        if (request != null) {
            String opinionType = request.getParameter("opinionType");
            String taskid = request.getParameter(ParamsConstants.TASKID_LOWERCASE);
            String userId = request.getParameter(ParamsConstants.USERID_HUMP);
            String activityName = null;
            if (StringUtils.isNotBlank(taskid)&&sysTaskService.getTask(taskid)!=null) {
                activityName = sysTaskService.getActivity(sysTaskService.getTask(taskid).getActivityId()).getActivityName();
            }

            //根据流程名称和节点名称获取右击意见
            if(StringUtils.isNotBlank(activityName)){
                Example bdcOpinion = new Example(BdcXtOpinion.class);
                bdcOpinion.createCriteria().andEqualTo("workflowname", opinionType).andEqualTo("userid",userId).andEqualTo("activitytype", activityName).andEqualTo("isuse", "1").andEqualTo("isrightclick", "1");
                lstOpinions=entityMapper.selectByExample(BdcXtOpinion.class,bdcOpinion);
            } else{
                Example bdcOpinion = new Example(BdcXtOpinion.class);
                bdcOpinion.createCriteria().andEqualTo("workflowname", opinionType).andEqualTo("isuse", "1").andEqualTo("userid",userId).andEqualTo("isrightclick", "1");
                lstOpinions=entityMapper.selectByExample(BdcXtOpinion.class,bdcOpinion);
            }
        }
        if (lstOpinions == null)
            lstOpinions = new ArrayList<BdcXtOpinion>();
        return lstOpinions;
    }

    @ResponseBody
    @RequestMapping(value = "/delSignIds", method = RequestMethod.GET)
    public HashMap getSignIds(Model model, HttpServletRequest request) throws Exception {
        HashMap data=Maps.newHashMap();
        if (request != null && StringUtils.isNotBlank(request.getParameter(ParamsConstants.PROID_LOWERCASE))) {
            String proid = request.getParameter(ParamsConstants.PROID_LOWERCASE);
            String proids = "";
            if (StringUtils.isNotBlank(request.getParameter(ParamsConstants.PROID_LOWERCASE))) {
                proids = bdcXmService.getProidsByProid(request.getParameter(PARAMETER_SIGNVO_PROID));
            }
            if (StringUtils.isBlank(proids))
                proids = proid;
            if (StringUtils.isNotBlank(proids)) {
                String[] proidStr = StringUtils.split(proids,Constants.SPLIT_STR);
                for (String proId : proidStr) {
                    String signKey = request.getParameter("signKey");
                    List<PfSignVo> signList = sysSignService.getSignList(signKey, proId);
                    if (CollectionUtils.isNotEmpty(signList)) {
                        for (PfSignVo signVo : signList) {
                            sysSignService.deleteSign(signVo.getSignId());
                        }
                    }
                }
            }

        }
        data.put("msg","true");
        return data;
    }


    /**
     * Esp手写板签名信息保存
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/signNoEsp", method = RequestMethod.GET)
    public String signNoEsp(Model model, HttpServletRequest request) throws Exception {
        PfSignVo signVo = new PfSignVo();
        String taskid = "";
        String proids = "";
        if (request != null) {
            signVo.setProId(request.getParameter(PARAMETER_SIGNVO_PROID));
            signVo.setSignKey(request.getParameter(PARAMETER_SIGNVO_SIGNKEY));
            signVo.setSignId(request.getParameter(PARAMETER_SIGNVO_SIGNID));
            taskid = request.getParameter(ParamsConstants.TASKID_LOWERCASE);
        }
        if (StringUtils.isNotBlank(signVo.getSignId())) {
            signVo = sysSignService.getSign(signVo.getSignId());
        }
        if (signVo == null) {
            signVo = new PfSignVo();
            if (request != null) {
                signVo.setProId(request.getParameter(PARAMETER_SIGNVO_PROID));
                signVo.setSignKey(request.getParameter(PARAMETER_SIGNVO_SIGNKEY));
                signVo.setSignId(request.getParameter(PARAMETER_SIGNVO_SIGNID));

            }
        }
        if (StringUtils.isNotBlank(signVo.getSignKey()))
            signVo.setSignKey(URLDecoder.decode(signVo.getSignKey(), Constants.DEFAULT_CHARSET));

        if (signVo.getSignDate() == null) {
            signVo.setSignDate(new Date(System.currentTimeMillis()));
        }
        if (request != null && StringUtils.isNotBlank(request.getParameter(PARAMETER_SIGNVO_PROID))) {
            proids = bdcXmService.getProidsByProid(request.getParameter(PARAMETER_SIGNVO_PROID));
        }

        model.addAttribute("signVo", signVo);
        model.addAttribute(ParamsConstants.USERID_HUMP, SessionUtil.getCurrentUserId());
        model.addAttribute(ParamsConstants.TASKID_LOWERCASE, taskid);
        model.addAttribute(ParamsConstants.PROIDS_LOWERCASE, proids);
        model.addAttribute("splitStr", Constants.SPLIT_STR);
        return "/main/signNoEsp";
    }

    /**
     * Esp手写板签名信息保存
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/saveSignEsp", method = RequestMethod.POST)
    public HashMap saveSignEsp(HttpServletRequest request) throws Exception {
        HashMap data = Maps.newHashMap();
        data.put("msg", "success");
        String signkey = request.getParameter(PARAMETER_SIGNVO_SIGNKEY);
        String image = request.getParameter("base64Image");
        String proId = request.getParameter(PARAMETER_SIGNVO_PROID);
        String userId = request.getParameter("signVo.userId");
        String signId = request.getParameter(PARAMETER_SIGNVO_SIGNID);
        try {
            PfSignVo sign = null;
            List<PfSignVo> pfSignVoList = sysSignService.getSignList(signkey, proId);
            if (CollectionUtils.isNotEmpty(pfSignVoList)) {
                sign = pfSignVoList.get(0);
            }
            if (sign != null) {
                sign.setSignDate(Calendar.getInstance().getTime());
                sysSignService.updateSign(sign);
                sysSignService.UpdateCustomSignPic(signId, image);
            } else {
                PfSignVo signVo = new PfSignVo();
                signVo.setSignType("0");
                signVo.setUserId(userId);
                signVo.setProId(proId);
                signVo.setSignKey(signkey);
                signVo.setSignDate(Calendar.getInstance().getTime());
                signVo.setSignName(SessionUtil.getCurrentUser().getUsername());
                sysSignService.addCustomSign(signVo, image);
            }
            if (StringUtils.isNotBlank(signId) && StringUtils.isBlank(image)) {
                sysSignService.deleteSign(signId);
            }
        } catch (Exception e) {
            data.put("msg", "error");
            logger.error("SignController.saveSignEsp",e);
        }
        return data;
    }

    @ResponseBody
    @RequestMapping(value = "/delSignEsp", method = RequestMethod.POST)
    public HashMap delSignEsp(String signId, HttpServletRequest request) {
        HashMap data = Maps.newHashMap();
        data.put("msg", "success");
        try {
            String proId = request.getParameter(PARAMETER_SIGNVO_PROID);
            String signkey = request.getParameter(PARAMETER_SIGNVO_SIGNKEY);
            if (StringUtils.indexOf(proId, ",") > -1) {
                proId = StringUtils.split(proId, ",")[0];
            }
            if (StringUtils.isNotBlank(proId) && StringUtils.isNotBlank(signkey)) {
                List<PfSignVo> pfSignVoList = sysSignService.getSignList(signkey, proId);
                if (CollectionUtils.isNotEmpty(pfSignVoList)) {
                    for (PfSignVo pfSignVo : pfSignVoList) {
                        sysSignService.deleteSign(pfSignVo.getSignId());
                    }
                }
            }
        } catch (Exception e) {
            data.put("msg", "error");
            logger.error("SignController.delSignEsp",e);
        }
        return data;
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 自动签名
     */
    @ResponseBody
    @RequestMapping(value = "/automaticSignature", method = RequestMethod.POST)
    public HashMap automaticSignature(String wiid, String proid) {
        HashMap data = Maps.newHashMap();
        data.put("msg", "success");
        try {
            String userid = super.getUserId();
            List<PfActivityVo> pfActivityVoList = sysTaskService.getWorkFlowInstanceActivityList(wiid);
            if (CollectionUtils.isNotEmpty(pfActivityVoList)) {
                PfActivityVo pfActivityVo = pfActivityVoList.get(0);
                String workflowNodeName = pfActivityVo.getActivityName();
                List<AutoSignWfNodeName> autoSignWfNodeNameList = ReadJsonUtil.getWorkFlowNodeName();
                if (CollectionUtils.isNotEmpty(autoSignWfNodeNameList) && StringUtils.isNotBlank(autoSignWfNodeNameList.get(0).getTotalNodeName())){
                    String[] totalNodeNameArray = autoSignWfNodeNameList.get(0).getTotalNodeName().split(",");
                    if (CommonUtil.indexOfStrs(totalNodeNameArray,workflowNodeName)){
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                            //分割合并流程每个项目都需要签字
                            if(CommonUtil.indexOfStrs(Constants.SQLX_SIGNMUL,bdcXm.getSqlx())){
                                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                                if (CollectionUtils.isNotEmpty(bdcXmList)){
                                    for (BdcXm bdcXmTemp: bdcXmList){
                                        signService.autoSignBeforeTurn(bdcXmTemp.getProid(),userid,workflowNodeName);
                                    }
                                }
                            }else{
                                signService.autoSignBeforeTurn(proid,userid,workflowNodeName);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            data.put("msg", "error");
            logger.error("SignController.automaticSignature",e);
        }
        return data;
    }

}
