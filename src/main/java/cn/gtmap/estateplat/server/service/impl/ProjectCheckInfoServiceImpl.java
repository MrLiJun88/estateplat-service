package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.BdcForceValidate;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectCheckInfoService;
import cn.gtmap.estateplat.server.service.core.ProjectValidateService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-31
 * @description 验证不动产项目服务
 */
@Service
public class ProjectCheckInfoServiceImpl implements ProjectCheckInfoService {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXtCheckinfoService bdcXtCheckinfoService;
    @Autowired
    BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcZsbhService bdcZsbhService;
    @Autowired
    private SysTaskService sysTaskService;

    private Map<String, ProjectValidateService> projectValidateMap;
    private Map<String, BdcForceValidate> projectForceValidateMap;
    private Map<String, String> sqlxNoCheckMap;

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String PARAMETER_MYDYDSXL = "没有对应的实现类！";
    private static final String PARAMETER_YZ_CHECKCODE = "验证checkcode";

    @Override
    public List<Map<String, Object>> checkXm(String proid, boolean isEndPorject, String checkModel,String userid,String taskid) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        Example example = new Example(BdcXtCheckinfo.class);
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        String sqlxdm = "";
        String  workflowDefinitionId = "";
        //获取平台的申请类型代码,主要为了合并
        if(StringUtils.isNotBlank(bdcXm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
            if(pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                workflowDefinitionId = pfWorkFlowInstanceVo.getWorkflowDefinitionId();
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        if(StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.isNotBlank(bdcXm.getQllx())) {
            List<BdcXtCheckinfo> list = null;
            Example.Criteria criteria = example.createCriteria();
            if(!bdcXm.getSqlx().equals(sqlxdm)) {
                //在不确定权利类型的流程中，使用申请类型验证，考虑到合并流程，此处的申请类型使用创建项目时的申请类型
                criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, sqlxdm);
            }else {
                if(isEndPorject)
                    criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, bdcXm.getSqlx()).andEqualTo(ParamsConstants.QLLXDM_LOWERCASE, bdcXm.getQllx()).andEqualTo(ParamsConstants.CHECKMODEL_HUMP, "continue");
                else {
                    criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, bdcXm.getSqlx()).andEqualTo(ParamsConstants.QLLXDM_LOWERCASE, bdcXm.getQllx());
                }
            }
            //zhouwanqing 验证全部和转发2个验证项目
            criteria.andNotEqualNvlTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_BDCDY, Constants.XT_CHECKTYPE_ALL);
            if(StringUtils.isNotBlank(checkModel)) {
                criteria.andEqualTo(ParamsConstants.CHECKMODEL_HUMP, checkModel);
            }
            list = bdcXtCheckinfoService.getXtCheckinfo(example);
            Project tempProject = bdcXmService.getProjectFromBdcXm(bdcXm, null);
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                if(StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                    tempProject.setDjId(bdcXmRel.getQjid());
                }
                if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    tempProject.setYxmid(bdcXmRel.getYproid());
                }
                BdcBdcdy bdcdy = null;
                if(StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                    bdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                }
                if(bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
                    tempProject.setBdcdyh(bdcdy.getBdcdyh());
                }
                if(StringUtils.isNotBlank(userid)){
                    tempProject.setUserId(userid);
                }
                if(StringUtils.isNotBlank(taskid)) {
                    tempProject.setTaskid(taskid);
                }
                if(StringUtils.isNotBlank(workflowDefinitionId)) {
                    tempProject.setWorkFlowDefId(workflowDefinitionId);
                }
                resultList = checkXm(list, tempProject);
            }
        }
        return resultList;
    }
    @Override
    public List<Map<String, Object>> checkXmByProject(Project project) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if(project != null && StringUtils.isNotBlank(project.getSqlx())) {
            Example example = new Example(BdcXtCheckinfo.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNoneBlank(project.getQllx())) {
                criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, project.getSqlx()).andEqualTo(ParamsConstants.QLLXDM_LOWERCASE, project.getQllx()).andNotEqualTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_ZF);
            }else {
                criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, project.getSqlx()).andNotEqualTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_ZF);
            }
            List<BdcXtCheckinfo> list = bdcXtCheckinfoService.getXtCheckinfo(example);
            if(CollectionUtils.isEmpty(list)) {
                //在不确定权利类型的流程中，使用申请类型验证，考虑到合并流程，此处的申请类型使用创建项目时的申请类型
                Example newExample = new Example(BdcXtCheckinfo.class);
                //从匹配创建项目，不验证转发
                newExample.createCriteria().andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, project.getSqlx()).andNotEqualTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_ZF);
                list = bdcXtCheckinfoService.getXtCheckinfo(newExample);
            }
            resultList = checkXm(list, project);
        }
        return resultList;
    }

    /**
     * 验证证书数量是否充足，作废比例过高预警
     *
     * @return
     */
    @Override
    public Map<String, Object> checkBdcdyZsbhsl(Double sybl, Double bfbl) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        try {
            Map zsMap = bdcZsbhService.getZsYjByZslx(Constants.BDCQZS_BH_DM);
            Long zsSysl = 0L;
            Double zsSybl = 0D;
            Double zsBfbl = 0D;
            if(StringUtils.isNotBlank(zsMap.get("WSY").toString()))
                zsSysl = Long.parseLong(zsMap.get("WSY").toString());
            if(StringUtils.isNotBlank(zsMap.get("SYBL").toString()))
                zsSybl = Double.parseDouble(zsMap.get("SYBL").toString());
            if(StringUtils.isNotBlank(zsMap.get("BFBL").toString()))
                zsBfbl = Double.parseDouble(zsMap.get("BFBL").toString());
            Map zmsMap = bdcZsbhService.getZsYjByZslx(Constants.BDCQZM_BH_DM);
            Long zmsSysl = 0L;
            Double zmsSybl = 0D;
            Double zmsBfbl = 0D;
            if(StringUtils.isNotBlank(zmsMap.get("WSY").toString()))
                zmsSysl = Long.parseLong(zmsMap.get("WSY").toString());
            if(StringUtils.isNotBlank(zmsMap.get("SYBL").toString()))
                zmsSybl = Double.parseDouble(zmsMap.get("SYBL").toString());
            if(StringUtils.isNotBlank(zmsMap.get("BFBL").toString()))
                zmsBfbl = Double.parseDouble(zmsMap.get("BFBL").toString());
            if(zsSybl < sybl)
                list.add("剩余证书低于" + sybl * 100 + "%，还剩" + zsSysl + "个,请添加");
            if(zmsSybl < sybl)
                list.add("剩余证明书低于" + sybl * 100 + "%，还剩" + zmsSysl + "个,请添加");
            if(zsBfbl > bfbl)
                list.add("证书编号作废已达到" + zsBfbl * 100 + "%");
            if(zmsBfbl > bfbl)
                list.add("证明书编号作废已达到" + zmsBfbl * 100 + "%");
        } catch (Exception e) {
            logger.error("ProjectCheckInfoServiceImpl.checkBdcdyZsbhsl",e);
        }
        map.put("info", list);
        return map;
    }

    /**
     * @param checkList 验证项
     * @param project   不动产登记项目
     * @return 验证结果
     * @description 根据验证项，验证当前项目是否符合要求
     */
    @Override
    public List<Map<String, Object>> checkXm(List<BdcXtCheckinfo> checkList, Project project) {
        List<Map<String, Object>> resultList = null;
        if(StringUtils.equals(AppConfig.getProperty("dwdm"),Constants.DWDM_SZ)){
            resultList = checkXmAll(checkList, project);
        }else{
            resultList = checkXmNew(checkList, project);
        }
        return resultList;
    }

    /**
     * @param checkList 验证项
     * @param project   不动产登记项目
     * @return 验证结果
     * @description 根据验证项，验证当前项目是否符合要求
     */
    public List<Map<String, Object>> checkXmNew(List<BdcXtCheckinfo> checkList, Project project) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if(checkList != null) {
            checkList = initForceValidateDm(checkList, project);
            for(int i = 0; i < checkList.size(); i++) {
                BdcXtCheckinfo bdcXtCheckinfo = checkList.get(i);
                ProjectValidateService projectValidateService = null;
                if(bdcXtCheckinfo.getCheckCode() != null) {
                    projectValidateService = this.getProjectValidateServiceByCheckCode(bdcXtCheckinfo.getCheckCode().toString());
                }

                if(projectValidateService == null) {
                    throw new NullPointerException(PARAMETER_YZ_CHECKCODE + bdcXtCheckinfo.getCheckCode() + PARAMETER_MYDYDSXL);
                }
                HashMap param = new HashMap();
                param.put("project", project);
                param.put(ParamsConstants.BDCXTCHECKINFO_HUMP, bdcXtCheckinfo);
                Map<String, Object> returnMap = projectValidateService.validate(param);
                if(returnMap != null&&returnMap.containsKey("info") && returnMap.get("info") != null) {
                    returnMap.put(ParamsConstants.CHECKMODEL_HUMP, bdcXtCheckinfo.getCheckModel());
                    returnMap.put(ParamsConstants.CHECKMSG_HUMP, bdcXtCheckinfo.getCheckMsg());
                    returnMap.put(ParamsConstants.CREATESQLXDM_HUMP, bdcXtCheckinfo.getCreateSqlxdm());
                    returnMap.put(ParamsConstants.CHECKPORIDS_HUMP, returnMap.get("info"));
                    returnMap.put(ParamsConstants.CHECKCODE_HUMP, bdcXtCheckinfo.getCheckCode());
                    if(StringUtils.isNotBlank(AppConfig.getProperty("mulBdcdyTip"))&&Boolean.parseBoolean(StringUtils.deleteWhitespace(AppConfig.getProperty("mulBdcdyTip")))) {
                        //苏州用于多个不动产单元号提示信息展示的区分
                        returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, project.getBdcdyh());
                    }
                    resultList.add(returnMap);
                    if(!StringUtils.equals(project.getSqlx(), Constants.SQLX_CF)
                            && !StringUtils.equals(project.getSqlx(), Constants.SQLX_PLCF)
                            && !StringUtils.equals(returnMap.get(ParamsConstants.CHECKMODEL_HUMP).toString(), "confirm")) {
                        break;
                    }
                }
            }
        }
        resultList = checkIsCd(project, resultList);
        return resultList;
    }


    /**
     * @param checkList 验证项
     * @param project   不动产登记项目
     * @return 验证结果
     * @description 根据验证项，验证当前项目是否符合要求
     */
    public List<Map<String, Object>> checkXmAll(List<BdcXtCheckinfo> checkList, Project project) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if(checkList != null) {
            checkList = initForceValidateDm(checkList, project);
            for(int i = 0; i < checkList.size(); i++) {
                long str=System.currentTimeMillis();
                Map<String, Object> returnMap = new HashMap<String, Object>();
                BdcXtCheckinfo bdcXtCheckinfo = checkList.get(i);
                ProjectValidateService projectValidateService = null;
                if(bdcXtCheckinfo.getCheckCode() != null) {
                    projectValidateService = this.getProjectValidateServiceByCheckCode(bdcXtCheckinfo.getCheckCode().toString());
                }

                if(projectValidateService == null) {
                    throw new NullPointerException(PARAMETER_YZ_CHECKCODE + bdcXtCheckinfo.getCheckCode() + PARAMETER_MYDYDSXL);
                }
                HashMap param = new HashMap();
                param.put("project", project);
                param.put(ParamsConstants.BDCXTCHECKINFO_HUMP, bdcXtCheckinfo);
                if(getActivityAndCheckCodeIscheck(project.getProid(),projectValidateService.getCode())) {
                    returnMap = projectValidateService.validate(param);
                }
                if(returnMap.containsKey("info") && returnMap.get("info") != null) {
                    returnMap.put(ParamsConstants.CHECKMODEL_HUMP, bdcXtCheckinfo.getCheckModel());
                    String checkMsg = null;
                    if (returnMap.containsKey("replace") && returnMap.get("replace") != null) {
                        if (StringUtils.isNotBlank(returnMap.get("replace").toString())) {
                            checkMsg = returnMap.get("replace").toString();
                        }
                    } else if (returnMap.containsKey("detail") && returnMap.get("detail") != null) {
                        if (StringUtils.isNotBlank(returnMap.get("detail").toString())) {
                            checkMsg = bdcXtCheckinfo.getCheckMsg() + returnMap.get("detail").toString();
                        }
                    } else {
                        checkMsg = bdcXtCheckinfo.getCheckMsg();
                    }
                    returnMap.put(ParamsConstants.CHECKMSG_HUMP,checkMsg);
                    returnMap.put(ParamsConstants.CREATESQLXDM_HUMP, bdcXtCheckinfo.getCreateSqlxdm());
                    returnMap.put(ParamsConstants.CHECKPORIDS_HUMP, returnMap.get("info"));
                    returnMap.put(ParamsConstants.CHECKCODE_HUMP, bdcXtCheckinfo.getCheckCode());
                    if(StringUtils.isNotBlank(project.getBdcdyh())){
                        returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, project.getBdcdyh());
                    }else{
                        if(StringUtils.isNotBlank(project.getYbdcqzh())){
                            returnMap.put(ParamsConstants.BDCDYH_LOWERCASE, project.getYbdcqzh());
                        }
                    }
                    resultList.add(returnMap);
                }
            }
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> checkXmByMap(HashMap hashMap) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        if(hashMap.containsKey(ParamsConstants.BDCXTCHECKINFOLIST_HUMP) && hashMap.get(ParamsConstants.BDCXTCHECKINFOLIST_HUMP) != null) {
            List<BdcXtCheckinfo> bdcXtCheckinfoList = (List<BdcXtCheckinfo>) hashMap.get(ParamsConstants.BDCXTCHECKINFOLIST_HUMP);
            String sqlx = (String) hashMap.get("sqlx");
            for(BdcXtCheckinfo bdcXtCheckinfo : bdcXtCheckinfoList) {
                ProjectValidateService projectValidateService = null;
                if(bdcXtCheckinfo.getCheckCode() != null) {
                    projectValidateService = this.getProjectValidateServiceByCheckCode(bdcXtCheckinfo.getCheckCode().toString());
                }
                if(projectValidateService == null) {
                    throw new NullPointerException(PARAMETER_YZ_CHECKCODE + bdcXtCheckinfo.getCheckCode() + PARAMETER_MYDYDSXL);
                }
                hashMap.put(ParamsConstants.BDCXTCHECKINFO_HUMP, bdcXtCheckinfo);
                Map<String, Object> returnMap = projectValidateService.validate(hashMap);
                if(returnMap != null&&returnMap.containsKey("info") && returnMap.get("info") != null) {
                    returnMap.put(ParamsConstants.CHECKMODEL_HUMP, bdcXtCheckinfo.getCheckModel());
                    returnMap.put(ParamsConstants.CHECKMSG_HUMP, bdcXtCheckinfo.getCheckMsg());
                    returnMap.put(ParamsConstants.CREATESQLXDM_HUMP, bdcXtCheckinfo.getCreateSqlxdm());
                    returnMap.put(ParamsConstants.CHECKPORIDS_HUMP, returnMap.get("info"));
                    returnMap.put(ParamsConstants.CHECKCODE_HUMP, bdcXtCheckinfo.getCheckCode());
                    resultList.add(returnMap);
                    if(!StringUtils.equals(sqlx, Constants.SQLX_CF)
                            && !StringUtils.equals(sqlx, Constants.SQLX_PLCF)
                            && !StringUtils.equals(returnMap.get(ParamsConstants.CHECKMODEL_HUMP).toString(), "confirm")) {
                        break;
                    }
                    resultList.add(returnMap);
                }
            }
        }
        return resultList;
    }

    @Override
    public List<BdcXtCheckinfo> getBdcXtCheckinfoByProject(Project project) {
        List<BdcXtCheckinfo> bdcXtCheckinfoList = null;
        Example example = new Example(BdcXtCheckinfo.class);
        if(project != null && StringUtils.isNotBlank(project.getSqlx())) {
            String sqlxdm = project.getSqlx();
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNoneBlank(project.getQllx())) {
                criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, project.getSqlx()).andEqualTo(ParamsConstants.QLLXDM_LOWERCASE, project.getQllx()).andNotEqualTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_ZF);
            } else {
                criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, project.getSqlx()).andNotEqualTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_ZF);
            }
            bdcXtCheckinfoList = bdcXtCheckinfoService.getXtCheckinfo(example);
            if(CollectionUtils.isEmpty(bdcXtCheckinfoList)) {
                //在不确定权利类型的流程中，使用申请类型验证，考虑到合并流程，此处的申请类型使用创建项目时的申请类型
                Example newExample = new Example(BdcXtCheckinfo.class);
                //从匹配创建项目，不验证转发
                newExample.createCriteria().andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, sqlxdm).andNotEqualTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_ZF);
                bdcXtCheckinfoList = bdcXtCheckinfoService.getXtCheckinfo(newExample);
            }
        }
        return bdcXtCheckinfoList;
    }


    /**
     * 查封和批量查封优先显示裁定
     *
     * @param project
     * @param resultList
     * @return
     */
    public List<Map<String, Object>> checkIsCd(Project project, List<Map<String, Object>> resultList) {
        if(CollectionUtils.isNotEmpty(resultList)&&(StringUtils.equals(project.getSqlx(), Constants.SQLX_CF)
                || StringUtils.equals(project.getSqlx(), Constants.SQLX_PLCF))) {
            List<Map<String, Object>> cdResultList = Lists.newArrayList();
            List<Map<String, Object>> cfResultList = Lists.newArrayList();
            List<Map<String, Object>> restResultList = Lists.newArrayList();
            for(Map<String, Object> resultMap : resultList) {
                Integer checkCode = (Integer) resultMap.get(ParamsConstants.CHECKCODE_HUMP);
                if(136 == checkCode) {
                    cdResultList.add(resultMap);
                }
                if(101 == checkCode) {
                    cfResultList.add(resultMap);
                }
                if((136 != checkCode) && (101 != checkCode)&&CollectionUtils.isEmpty(restResultList)) {
                    restResultList.add(resultMap);
                }
            }
            resultList = CollectionUtils.isNotEmpty(cdResultList) ?
                    cdResultList : (CollectionUtils.isNotEmpty(cfResultList) ? cfResultList : restResultList);
        }
        return resultList;
    }

    /**
     * @param checkList，project
     * @return List<BdcXtCheckinfo>
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @description 初始化强制验证模板
     */
    public List<BdcXtCheckinfo> initForceValidateDm(List<BdcXtCheckinfo> checkList, Project project) {
        if(projectForceValidateMap != null) {
            for(Map.Entry<String, BdcForceValidate> entry : projectForceValidateMap.entrySet()) {
                BdcForceValidate bdcForceValidate = entry.getValue();
                if((bdcForceValidate != null&&StringUtils.isBlank(bdcForceValidate.getEsqlxdm())||(bdcForceValidate != null&&StringUtils.isNotBlank(bdcForceValidate.getEsqlxdm())
                        && !CommonUtil.indexOfStrs(bdcForceValidate.getEsqlxdm().split(","), project.getSqlx())
                        && !CommonUtil.indexOfStrs(bdcForceValidate.getEsqlxdm().split(","), project.getDjzx())))) {
                    BdcXtCheckinfo bdcXtCheckinfo = new BdcXtCheckinfo();
                    bdcXtCheckinfo.setSqlxdm(project.getSqlx());
                    bdcXtCheckinfo.setCheckCode(Integer.valueOf(entry.getKey()));
                    bdcXtCheckinfo.setCheckMsg(bdcForceValidate.getCheckmsg());
                    bdcXtCheckinfo.setQllxdm(project.getQllx());
                    bdcXtCheckinfo.setCheckModel(Constants.CHECKMODEL_ALERT);
                    checkList.add(bdcXtCheckinfo);
                }
            }
        }
        checkList = sortCheckListByCheckModel(checkList);
        return checkList;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 因为验证一条不满足就break, 所以要将confirm都放在后面
     */
    private List<BdcXtCheckinfo> sortCheckListByCheckModel(List<BdcXtCheckinfo> checkList) {
        List<BdcXtCheckinfo> alertCheckList = new ArrayList<BdcXtCheckinfo>();
        List<BdcXtCheckinfo> confirmCheckList = new ArrayList<BdcXtCheckinfo>();
        for(BdcXtCheckinfo checkinfo : checkList) {
            if(StringUtils.equals(Constants.CHECKMODEL_ALERT, checkinfo.getCheckModel())) {
                alertCheckList.add(checkinfo);
            } else {
                confirmCheckList.add(checkinfo);
            }
        }
        checkList = alertCheckList;
        checkList.addAll(confirmCheckList);
        return checkList;
    }

    public ProjectValidateService getProjectValidateServiceByCheckCode(String checkcode) {
        return projectValidateMap.get(checkcode);
    }

    public Map<String, ProjectValidateService> getProjectValidateMap() {
        return projectValidateMap;
    }

    public void setProjectValidateMap(Map<String, ProjectValidateService> projectValidateMap) {
        this.projectValidateMap = projectValidateMap;
    }

    public Map<String, BdcForceValidate> getProjectForceValidateMap() {
        return projectForceValidateMap;
    }

    public void setProjectForceValidateMap(Map<String, BdcForceValidate> projectForceValidateMap) {
        this.projectForceValidateMap = projectForceValidateMap;
    }

    public Map<String, String> getSqlxNoCheckMap() {
        return sqlxNoCheckMap;
    }

    public void setSqlxNoCheckMap(Map<String, String> sqlxNoCheckMap) {
        this.sqlxNoCheckMap = sqlxNoCheckMap;
    }


    /**
     * @param bdcXm
     * @return Boolean
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 判断节点是否进行server验证
     */
    private Boolean getActivityIscheck(BdcXm bdcXm) {
        Boolean activityIscheck = true;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<PfActivityVo> pfActivityVoList = sysTaskService.getWorkFlowInstanceActivityList(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(pfActivityVoList)) {
                String sqlxNoCheck = "";
                if (sqlxNoCheckMap != null&&sqlxNoCheckMap.get(bdcXm.getSqlx()) != null) {
                    sqlxNoCheck = sqlxNoCheckMap.get(bdcXm.getSqlx());
                }
                if (StringUtils.isBlank(sqlxNoCheck)&&sqlxNoCheckMap!=null&&sqlxNoCheckMap.get(ParamsConstants.DEFAULT_LOWERCASE) != null) {
                    sqlxNoCheck = sqlxNoCheckMap.get(ParamsConstants.DEFAULT_LOWERCASE);
                }
                PfActivityVo pfActivityVo = pfActivityVoList.get(0);
                if (StringUtils.indexOf(sqlxNoCheck, pfActivityVo.getActivityName()) > -1) {
                    activityIscheck = false;
                }
            }
        }
        return activityIscheck;
    }

    /**
     * @param proid
     * @param checkCode
     * @return Boolean
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 判断当前节点及当前验证代码是否进行server验证
     */
    private Boolean getActivityAndCheckCodeIscheck(String proid,String checkCode) {
        Boolean activityAndCheckCodeIscheck = true;
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(checkCode)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<PfActivityVo> pfActivityVoList = sysTaskService.getWorkFlowInstanceActivityList(bdcXm.getWiid());
                if (CollectionUtils.isNotEmpty(pfActivityVoList)) {
                    String sqlxNoCheck = "";
                    String exceptCheckCode = "";
                    String zsbhNoCheck = "";
                    if (sqlxNoCheckMap != null&&sqlxNoCheckMap.get(bdcXm.getSqlx()) != null) {
                        sqlxNoCheck = sqlxNoCheckMap.get(bdcXm.getSqlx());
                        exceptCheckCode = sqlxNoCheckMap.get("exceptCheckCode");
                    }

                    if (sqlxNoCheckMap != null&&StringUtils.isBlank(sqlxNoCheck) && sqlxNoCheckMap.get(ParamsConstants.DEFAULT_LOWERCASE) != null) {
                        sqlxNoCheck = sqlxNoCheckMap.get(ParamsConstants.DEFAULT_LOWERCASE);
                        exceptCheckCode = sqlxNoCheckMap.get("exceptCheckCode");
                        zsbhNoCheck = sqlxNoCheckMap.get("zsbhNoCheck");
                    }

                    PfActivityVo pfActivityVo = pfActivityVoList.get(0);
                    if (StringUtils.indexOf(sqlxNoCheck, pfActivityVo.getActivityName()) > -1 && StringUtils.indexOf(exceptCheckCode, checkCode) == -1) {
                        activityAndCheckCodeIscheck = false;
                    }
                    if(StringUtils.indexOf(zsbhNoCheck,pfActivityVo.getActivityName())>-1 && StringUtils.indexOf(exceptCheckCode,checkCode) > -1){
                        activityAndCheckCodeIscheck = false;
                    }
                }
            }
        }
        return activityAndCheckCodeIscheck;
    }

    @Override
    public List<Map<String, Object>> checkXmComfirmItems(String proid,String taskid){
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        List<Map<String, Object>> resultList = Lists.newArrayList();
        String sqlxdm = "";
        // 获取平台的申请类型代码,主要为了合并
        if(StringUtils.isNotBlank(bdcXm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
            if(pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }

        if(StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.isNotBlank(bdcXm.getQllx())) {
            Example example = new Example(BdcXtCheckinfo.class);
            List<BdcXtCheckinfo> list = null;
            if(!bdcXm.getSqlx().equals(sqlxdm)) {
                // 在不确定权利类型的流程中，使用申请类型验证，考虑到合并流程，此处的申请类型使用创建项目时的申请类型
                example.createCriteria().andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, sqlxdm).andEqualTo(ParamsConstants.CHECKMODEL_HUMP,ParamsConstants.CONFIRM_LOWERCAS);
                list = bdcXtCheckinfoService.getXtCheckinfo(example);
            }else{
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo(ParamsConstants.SQLXDM_LOWERCASE, bdcXm.getSqlx()).andEqualTo(ParamsConstants.QLLXDM_LOWERCASE, bdcXm.getQllx());
                // 只验证转发的comfirm验证项目
                criteria.andEqualTo(ParamsConstants.CHECKTYPE_HUMP, Constants.XT_CHECKTYPE_ZF);
                criteria.andEqualTo(ParamsConstants.CHECKMODEL_HUMP,ParamsConstants.CONFIRM_LOWERCAS);
                list = bdcXtCheckinfoService.getXtCheckinfo(example);
            }
            Project tempProject = bdcXmService.getProjectFromBdcXm(bdcXm, null);
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
                BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                if(StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    tempProject.setYxmid(bdcXmRel.getYproid());
                }
                if(StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                    tempProject.setDjId(bdcXmRel.getQjid());
                }
                if(StringUtils.isNotBlank(taskid)){
                    tempProject.setTaskid(taskid);
                }
                BdcBdcdy bdcdy = null;
                if(StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                    bdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                }
                if(bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
                    tempProject.setBdcdyh(bdcdy.getBdcdyh());
                }
                resultList = checkXm(list, tempProject);
            }
        }
        return resultList;
    }
}
