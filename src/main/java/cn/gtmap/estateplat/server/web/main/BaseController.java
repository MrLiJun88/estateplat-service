package cn.gtmap.estateplat.server.web.main;


import cn.gtmap.estateplat.core.i18n.NLS;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.ExUtils;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.*;
import com.gtis.spring.Container;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by jane on 14-1-7.
 */
@Controller
public abstract class BaseController {
    public static final String RET = "ret";
    public static final String MSG = "msg";
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected String platformUrl;
    protected String fcxtUrl;
    protected String bdcdjUrl;
    protected String archiveUrl;
    protected String etlUrl;
    protected String ompUrl;
    protected String analysisUrl;

    protected String fileCenterUrl;
    protected String reportUrl;
    protected String portalUrl;
    protected String casUrl;
    protected String sysVersion;
    protected String dwdm;
    protected String qxdm;
    protected String formUrl;
    protected String sflw;
    @Autowired
    protected SysUserService sysUserService;
    @Autowired
    protected BdcZdGlService bdcZdGlService;



    protected static void success(RedirectAttributes ra, String msg) {
        ra.addFlashAttribute(RET, true);
        ra.addFlashAttribute(MSG, msg);
    }

    protected static void success(RedirectAttributes ra) {
        ra.addFlashAttribute(RET, true);
    }

    protected static void fail(Model model, String msg) {
        model.addAttribute(RET, false);
        model.addAttribute(MSG, msg);
    }

    protected static void fail(Model model, Exception ex) {
        model.addAttribute(RET, false);
        model.addAttribute(MSG, ex.getMessage());
    }

    protected static void fail(Model model) {
        model.addAttribute(RET, false);
    }

    @SuppressWarnings("unchecked")
    protected static Map<String, Object> success() {
        return (Map) Collections.singletonMap(RET, true);
    }

    protected static Map<String, Object> fail(String message) {
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put(RET, false);
        map.put(MSG, message);
        return map;
    }

    protected static Map<String, Object> fail(Exception ex) {
        return ExUtils.toMap(ex);
    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.platformUrl = AppConfig.getPlatFormUrl();
        this.fcxtUrl = AppConfig.getProperty("fcxt.url");
        this.bdcdjUrl = AppConfig.getProperty("bdcdj.url");
        this.etlUrl = AppConfig.getProperty("etl.url");
        this.archiveUrl = AppConfig.getProperty("archive.url");
        this.fileCenterUrl = AppConfig.getFileCenterUrl();
        this.ompUrl = AppConfig.getProperty("omp.url");
        this.reportUrl = AppConfig.getProperty("report.url");
        this.portalUrl = AppConfig.getProperty("portal.url");
        this.casUrl = AppConfig.getProperty("cas.url");
        this.sysVersion = AppConfig.getProperty("sys.version");
        this.formUrl = AppConfig.getProperty("form.url");
        this.analysisUrl = AppConfig.getProperty("analysis.url");
        this.sflw = AppConfig.getProperty("sflw");
        this.dwdm= AppConfig.getProperty("dwdm");
        this.qxdm= AppConfig.getProperty("qxdm");
        request.setAttribute("archiveUrl", archiveUrl);
        request.setAttribute("ompUrl", ompUrl);
        request.setAttribute("reportUrl", reportUrl);
        request.setAttribute("portalUrl", portalUrl);
        request.setAttribute("casUrl", casUrl);
        request.setAttribute("sysVersion", sysVersion);
        request.setAttribute("fileCenterUrl", fileCenterUrl);
        request.setAttribute("platformUrl", platformUrl);
        request.setAttribute("bdcdjUrl", bdcdjUrl);
        request.setAttribute("fcxtUrl", fcxtUrl);
        request.setAttribute("etlUrl", etlUrl);
        request.setAttribute("sflw", sflw);
        request.setAttribute("dwdm", dwdm);
        request.setAttribute("qxdm", qxdm);
        request.setAttribute("formUrl", formUrl);
        request.setAttribute("analysisUrl", analysisUrl);
    }

    protected final String getMsg(String key) {
        return NLS.get(key);
    }

    protected final String getMsg(String key, Object... args) {
        return NLS.get(key, args);
    }

    /**
     * 获取指定部门内的所有人员
     *
     * @param organName
     * @return
     */
    protected final List<PfUserVo> getUserListByOrgan(String organName) {
        return sysUserService.getUsersByOrganName(organName);
    }

    /**
     * 获取id的人员姓名
     *
     * @param id
     * @return
     */
    protected final String getUserNameById(String id) {
        PfUserVo userVo = sysUserService.getUserVo(id);
        if (userVo != null) {
            return userVo.getUserName();
        }
        return "";
    }

    /**
     * 获取id的人员姓名
     *
     * @param id
     * @return
     */
    protected final String getDeptNameById(String id) {
        PfOrganVo organVo = sysUserService.getOrganVo(id);
        if (organVo != null)
            return organVo.getOrganName();
        return "";
    }

    /**
     * 获取当前登录人员id
     *
     * @return
     */
    protected final String getUserId() {
        UserInfo userInfo = SessionUtil.getCurrentUser();
        if (userInfo != null)
            return userInfo.getId();
        return null;
    }

    /**
     * 获取当前登录人员名字
     *
     * @return
     */
    protected final String getUserName() {
        UserInfo userInfo = SessionUtil.getCurrentUser();
        if (userInfo != null)
            return userInfo.getUsername();
        return null;
    }

    /**
     * 获取当前用户的部门
     *
     * @return
     */
    protected final List<PfOrganVo> getOrganName() {
        UserInfo userInfo = SessionUtil.getCurrentUser();
        return userInfo.getLstOragn();
    }

    /**
     * 获取当前登入人员所在部门ID
     *
     * @return
     */
    protected final String getUserDptId() {
        if (CollectionUtils.isEmpty(getOrganName())) {
            return null;
        }
        return ((getOrganName()).get(0)).getOrganId();
    }

    /**
     * 通过proid获取工作流实例
     *
     * @param proid
     * @return
     */
    protected final PfWorkFlowInstanceVo getWorkFlowInstance(String proid) {
        SysWorkFlowInstanceService workFlowDAO = (SysWorkFlowInstanceService) Container
                .getBean("SysWorkFlowInstanceService");
        return workFlowDAO.getWorkflowInstanceByProId(proid);
    }

    /**
     * 通过taskid获取工作流实例的所属流程名称
     *
     * @param taskid
     * @return
     */
    protected String getActivityName(String taskid) {
        SysTaskService sysTaskService = (SysTaskService) Container.getBean("TaskService");
        PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
        PfActivityVo pfActivityVo = sysTaskService.getActivity(pfTaskVo.getActivityId());
        return pfActivityVo.getActivityName();
    }

    /**
     * 获取操作平台用户dao
     *
     * @return
     */
    protected final SysUserService getUserDAO() {
        return (SysUserService) Container.getBean("SysUserServiceImpl");
    }

    /**
     * 获取所有人员
     *
     * @return
     */
    protected final List getAllUsers() {
        return this.getUserDAO().getAllUsers();
    }

    /**
     * 获取所有人员姓名
     *
     * @return
     */
    protected final List<String> getAllUserName() {
        List<String> list = new ArrayList<String>();
        List users = this.getAllUsers();
        for (Iterator i = users.iterator(); i.hasNext(); ) {
            PfUserVo vo = (PfUserVo) i.next();
            if (vo != null)
                list.add(vo.getUserName());
        }
        return list;
    }

    /**
     * 根据人员姓名获取人员id，重名未考虑
     *
     * @param userName
     * @return
     */
    protected final String getUserIdByUserName(String userName) {
        String userId = "";
        if (StringUtils.isBlank(userName)) {
            return userId;
        }
        List users = this.getAllUsers();
        for (Iterator i = users.iterator(); i.hasNext(); ) {
            PfUserVo vo = (PfUserVo) i.next();
            if (vo != null && userName.equals(vo.getUserName()))
                userId = vo.getUserId();
        }
        return userId;
    }

    /**
     * 获取当前登录人员编号
     *
     * @return
     */
    protected final String getUserNo(String userName) {
        String userNo = "";
        PfUserVo pfUserVo = sysUserService.getUserVo(getUserIdByUserName(userName));
        if (pfUserVo != null) {
            userNo = pfUserVo.getUserNo();
        }

        return userNo;
    }

    /**
     * 获取所有部门
     *
     * @return
     */
    protected final List<PfOrganVo> getDptList() {
        return this.getUserDAO().getOrganList();
    }

    /**
     * 获取所有部门 名称
     *
     * @return
     */
    protected final List<String> getUnitList() {

        List<String> list = new ArrayList<String>();

        for (Iterator i = getDptList().iterator(); i.hasNext(); ) {
            PfOrganVo vo = (PfOrganVo) i.next();
            list.add(vo.getOrganName());
        }
        return list;
    }

    /**
     * 获取指定部门内的所有人员
     *
     * @param organId
     * @return
     */
    protected final List<PfUserVo> getUserListByOrganId(String organId) {
        return this.getUserDAO().getUserListByOragn(organId);
    }

    /**
     * 获取指定部门内所有人员姓名
     *
     * @param organId
     * @return
     */
    protected final List<String> getUserNameListByOrgan(String organId) {

        List<String> list = new ArrayList<String>();
        for (Iterator i = getUserListByOrganId(organId).iterator(); i.hasNext(); ) {
            PfUserVo vo = (PfUserVo) i.next();
            list.add(vo.getUserName());
        }
        return list;
    }

    /**
     * 用于保存 部门和人员 的联动下拉框
     *
     * @return
     */
    protected final Map getUnitUserMap() {
        Map<String, List<?>> map = new HashMap<String, List<?>>();
        List units = this.getDptList();
        for (Iterator i = units.iterator(); i.hasNext(); ) {
            PfOrganVo vo = (PfOrganVo) i.next();
            String name = vo.getOrganName();
            List users = this.getUserNameListByOrgan(vo.getOrganId());
            map.put(name, users);
        }
        return map;
    }

    protected final Map getUnitAndUserMap() {
        Map<Map<String, String>, Map<String, String>> map = new HashMap<Map<String, String>, Map<String, String>>();
        List units = this.getDptList();
        for (Iterator i = units.iterator(); i.hasNext(); ) {
            PfOrganVo vo = (PfOrganVo) i.next();
            String organName = vo.getOrganName();
            String organId = vo.getOrganId();
            Map<String, String> organMap = new HashMap<String, String>();
            organMap.put(organId, organName);
            List users = this.getUserListByOrganId(organId);
            for (Iterator ite = users.iterator(); i.hasNext(); ) {
                PfUserVo user = (PfUserVo) ite.next();
                Map<String, String> userMap = new HashMap<String, String>();
                String userName = user.getUserName();
                String userId = user.getUserId();
                userMap.put(userName, userId);
                map.put(organMap, userMap);
            }

        }
        return map;
    }

    /**
     * 取Organ与User的映射关系集合
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final Map getOrganUserSetMap() {
        Map setMap = new HashMap();
        Map<String, PfOrganVo> organMap = new HashMap<String, PfOrganVo>();
        Map<String, PfUserVo> userMap = new HashMap<String, PfUserVo>();
        Map<String, Map> userIdOrganIdMap = new HashMap<String, Map>();
        Map<String, Map> organIdUserIdMap = new HashMap<String, Map>();

        setMap.put("organMap", organMap);
        setMap.put("userMap", userMap);
        setMap.put("userIdOrganIdMap", userIdOrganIdMap);
        setMap.put("organIdUserIdMap", organIdUserIdMap);

        List units = this.getDptList();
        for (Iterator i = units.iterator(); i.hasNext(); ) {
            PfOrganVo voOrgan = (PfOrganVo) i.next();
            String organId = voOrgan.getOrganId();
            organMap.put(organId, voOrgan);
            List users = this.getUserListByOrganId(organId);

            for (int num = 0; num < users.size(); num++) {
                PfUserVo voUser = (PfUserVo) users.get(num);
                String userId = voUser.getUserId();
                userMap.put(userId, voUser);

                Map tempUserOrganMap = userIdOrganIdMap.get(userId);
                if (tempUserOrganMap == null) {
                    tempUserOrganMap = new HashMap();
                    userIdOrganIdMap.put(userId, tempUserOrganMap);
                }
                tempUserOrganMap.put(organId, organId);

                Map tempOrganUserMap = organIdUserIdMap.get(organId);
                if (tempOrganUserMap == null) {
                    tempOrganUserMap = new HashMap();
                    organIdUserIdMap.put(organId, tempOrganUserMap);
                }
                tempOrganUserMap.put(userId, userId);
            }
        }
        return setMap;
    }

    protected final List<PfOrganVo> getOrgainList() throws Exception {
        return getUserDAO().getAllOrganList();
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    protected final PfUserVo getUserVo() {
        String userid = getUserId();
        PfUserVo pfUserVo = getUserDAO().getUserVo(userid);
        if (pfUserVo == null)
            pfUserVo = new PfUserVo();
        return pfUserVo;
    }


    /**
     * 根据用户id获取roleId
     *
     * @return
     */
    protected final List<PfRoleVo> getRoleListByUser(String userId) {
        return this.getUserDAO().getRoleListByUser(userId);
    }


    /**
     * 根据用户id获取roleIds
     *
     * @return
     */
    protected final String getRoleIdsByUser(String userId) {
        List<PfRoleVo> roleVoList = this.getUserDAO().getRoleListByUser(userId);
        StringBuilder roleIds = new StringBuilder();
        if (CollectionUtils.isNotEmpty(roleVoList)) {
            for (PfRoleVo pfRoleVo : roleVoList) {
                if (StringUtils.isBlank(roleIds)) {
                    roleIds.append(pfRoleVo.getRoleId());
                }else {
                    roleIds.append(",").append(pfRoleVo.getRoleId());
                }
            }
        }
        return String.valueOf(roleIds);
    }

    /**
     * 根据部门名称获取部门ID
     *
     * @return
     */
    protected final String getDeptIdByDeptName(String deptName) {
        String deptId = "";
        List<PfOrganVo> pfOrganVoList = getDptList();
        if (StringUtils.isNotBlank(deptName)) {
            for (PfOrganVo pfOrganVo : pfOrganVoList) {
                if (pfOrganVo != null && StringUtils.equals(pfOrganVo.getOrganName(), deptName)) {
                    deptId = pfOrganVo.getOrganId();
                    break;
                }
            }
        }
        return deptId;
    }

    /**
     * 获取当前登录人员所在部门名称
     *
     * @param userId
     * @return
     */
    protected final String getUserDptName(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        List<PfOrganVo> pfOrganVoList = getUserDAO().getOrganListByUser(userId);
        if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
            PfOrganVo pfOrganVo = pfOrganVoList.get(0);
            if (pfOrganVo != null) {
                return pfOrganVo.getOrganName();
            }
        }
        return null;
    }

    /**
     * 获取当前登录人员所在部门ID
     *
     * @param userId
     * @return
     */
    protected final String getUserDptId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        List<PfOrganVo> pfOrganVoList = getUserDAO().getOrganListByUser(userId);
        if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
            PfOrganVo pfOrganVo = pfOrganVoList.get(0);
            if (pfOrganVo != null) {
                return pfOrganVo.getOrganId();
            }
        }
        return null;
    }


    /**
     * 根据人员姓名获取部门
     *
     * @param userName
     * @return
     */
    protected final PfOrganVo getDeptByUserName(String userName) {
        PfOrganVo pfOrganVo = null;
        String userId = "";
        if (StringUtils.isBlank(userName)) {
            return pfOrganVo;
        }
        List users = this.getAllUsers();
        for (Iterator i = users.iterator(); i.hasNext(); ) {
            PfUserVo vo = (PfUserVo) i.next();
            if (vo != null && userName.equals(vo.getUserName()))
                userId = vo.getUserId();
        }
        if (StringUtils.isNotBlank(userId)) {
            String deptId = getUserDptId(userId);
            String deptName = getUserDptName(userId);
            pfOrganVo = new PfOrganVo();
            pfOrganVo.setOrganId(deptId);
            pfOrganVo.setOrganName(deptName);
        }

        return pfOrganVo;
    }

    /**
     * 获取页面控件的授权信息
     */
    protected final String checkPlatformAuthor(HttpServletRequest request, String key) {
        String readyTag = "false";
        String userId = getUserId();
        String roleIds = getRoleIdsByUser(userId);
        String proid = request.getParameter(ParamsConstants.PROID_LOWERCASE);
        String taskid = request.getParameter(ParamsConstants.TASKID_LOWERCASE);
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put(ParamsConstants.PROID_LOWERCASE, request.getParameter(ParamsConstants.PROID_LOWERCASE));
        paraMap.put("rid", request.getParameter("rid"));
        paraMap.put(ParamsConstants.TASKID_LOWERCASE, request.getParameter(ParamsConstants.TASKID_LOWERCASE));

        paraMap.put("roles", roleIds);

        String pageEnterType = PlatformUtil.getPageEnterType(proid, taskid);
        if (pageEnterType.equals(PlatformUtil.PAGE_ENTER_FROM_TASKLIST)) {
            paraMap.put("from", PlatformUtil.PAGE_ENTER_FROM_TASKLIST);
            request.setAttribute("from",
                    PlatformUtil.PAGE_ENTER_FROM_TASKLIST);


        } else {
            paraMap.put("from", PlatformUtil.PAGE_ENTER_FROM_PROJECTLIST);
            request.setAttribute("from",
                    PlatformUtil.PAGE_ENTER_FROM_PROJECTLIST);

        }

        List<PfPartitionInfoVo> tempPartionInfoList = PlatformUtil
                .getAuthorList(paraMap);
        for (int i = 0; i < tempPartionInfoList.size(); i++) {
            PfPartitionInfoVo tempPfPartitionInfoVo = tempPartionInfoList
                    .get(i);
            if (tempPfPartitionInfoVo != null) {
                String elementId = tempPfPartitionInfoVo.getElementId();

                int operateType = tempPfPartitionInfoVo.getOperateType();
                if (elementId == null) {
                    elementId = "";
                }
                if (StringUtils.equalsIgnoreCase(elementId, key)) {
                    if (operateType == 0) {// 完全控制
                        readyTag = "true";

                    } else {
                        readyTag = "false";

                    }
                }
            }

        }
        return readyTag;
    }

    /**
     * 获取单位代码
     *
     * @return
     */
    protected final String getCurrentUserDwdm() {
        return sysUserService.getUserRegionCode(getUserId());
    }

    /**
     * 获取查询的用户的行政区条件
     *
     * @return
     */
    protected final String getWhereXzqdm() {
        String userDwdm = getCurrentUserDwdm();
        if (StringUtils.isNotBlank(userDwdm)) {
            while (StringUtils.isNotBlank(userDwdm) && userDwdm.endsWith("0")) {
                userDwdm = userDwdm.substring(0, userDwdm.length() - 1);
            }
        }
        return userDwdm;
    }

}
