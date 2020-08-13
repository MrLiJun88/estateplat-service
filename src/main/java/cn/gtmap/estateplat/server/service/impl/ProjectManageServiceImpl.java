package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.acceptance.YcslxxModel;
import cn.gtmap.estateplat.model.register.*;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQlrMapper;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.*;
import cn.gtmap.estateplat.server.service.etl.IntegrationPlatformService;
import cn.gtmap.estateplat.server.utils.*;
import cn.gtmap.estateplat.service.server.ProjectManageService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.UUID;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;


/**
 * @version 1.0, 2017/3/28
 * @author<a href = "mailto；liuxing@gtmap.cn">liuxing</a>
 * @description
 */
@Service
public class ProjectManageServiceImpl implements ProjectManageService {

    @Autowired
    private DjxxMapper djxxMapper;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private ProjectCheckInfoService projectCheckInfoService;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcQlrMapper bdcQlrMapper;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcFwFsssService bdcFwFsssService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private AutoWorkFlowService autoWorkFlowService;
    @Autowired
    private IntegrationPlatformService integrationPlatformService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private GdCfService gdCfService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String etlUrl = AppConfig.getProperty("etl.url");
    private static final String CONFIGURATION_PARAMETER_QJ_NAME = "qj.name";
    private static final String PARAMETER_LOCATION = "location";
    private static final String PARAMETER_EXCELNAME = "excelName";
    private static final String PARAMETER_SHEETNAME = "sheetName";
    private static final String PARAMETER_DATANAME = "dataName";

    @Override
    public String createProject(List<DjModel> djModelList, String userid) {
        String msg = "";
        String xtly = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(djModelList)) {
            try {
                List<SqxxModel> sqxxModels;
                for (DjModel djModel : djModelList) {
                    Project project = null;
                    sqxxModels = djModel.getSqxxModelList();
                    if (CollectionUtils.isNotEmpty(sqxxModels)) {
                        xtly = sqxxModels.get(0).getXtly();
                        project = new Project();
                        if(StringUtils.equals(xtly,Constants.XTLY_JCPTSL)) {
                            String bdclx = Constants.BDCLX_TDFW;
                            getSingleProject(sqxxModels.get(0), project, userid, bdclx);
                            project.setBh(sqxxModels.get(0).getSqh());
                        }else{
                            //单个流程
                            if (sqxxModels.size() == 1) {
                                String bdclx = Constants.BDCLX_TDFW;
                                if (StringUtils.isNotBlank(xtly) && StringUtils.equals(xtly, Constants.XTLY_YHSL) && StringUtils.isNotBlank(sqxxModels.get(0).getBdcdyh())) {
                                    //银行创建
                                    String bdclxTemp = bdcdyService.getTdDjsjBdclxByBdcdyh(sqxxModels.get(0).getBdcdyh());
                                    if (StringUtils.isNotBlank(bdclxTemp) && StringUtils.equals(bdclxTemp, Constants.BDCLX_TD)) {
                                        bdclx = Constants.BDCLX_TD;
                                    }
                                }
                                getSingleProject(sqxxModels.get(0), project, userid, bdclx);
                            } else {
                                //多个流程
                                getPlProject(sqxxModels, project, userid, Constants.BDCLX_TDFW);
                            }
                        }
                    }
                    if(StringUtils.equals(xtly,Constants.XTLY_JCPTSL)){
                        return createJcptProject(project, djModel, xtly);
                    }else{
                        msg = createProject(project, djModel, xtly);
                    }
                    stringBuilder.append(msg + Constants.SPLIT_STR);
                }
                if (null != stringBuilder && StringUtils.isNotBlank(stringBuilder.toString()) && !StringUtils.equals(xtly, "1")) {
                    msg = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                }
            } catch (Exception e) {
                logger.error("ProjectManageServiceImpl.createProject",e);
                msg = e.getMessage();
            }
        }
        return msg;
    }


    public void setSqlxToProject(SqxxModel sqxxModel, Project project) {
        String djsydm = null;
        if (StringUtils.isNotBlank(sqxxModel.getSqdjlxmc())) {
            String wdid = getSysWorkFlowDefineIdByWorkFlowName(sqxxModel.getSqdjlxmc());
            if (StringUtils.isBlank(wdid) && StringUtils.isNotBlank(sqxxModel.getSqdjlx()))
                wdid = bdcZdGlService.getWdidBySqlxdm(sqxxModel.getSqdjlx());
            project.setWorkFlowDefId(wdid);
            if (StringUtils.isBlank(sqxxModel.getSqdjlx())) {
                List<Map> maps = bdcXmService.getAllLxByWfName(sqxxModel.getSqdjlxmc());
                if (CollectionUtils.isNotEmpty(maps)) {
                    String sqlx = (String) maps.get(0).get(ParamsConstants.SQLXDM_CAPITAL);
                    if (StringUtils.isNotBlank(sqlx)) {
                        project.setSqlx(sqlx);
                        sqxxModel.setSqdjlx(sqlx);
                    }
                    if (maps.get(0).get(ParamsConstants.DJSYDM_CAPITAL) != null && StringUtils.isNotBlank(maps.get(0).get(ParamsConstants.DJSYDM_CAPITAL).toString())) {
                        djsydm = maps.get(0).get(ParamsConstants.DJSYDM_CAPITAL).toString();
                    }
                }
            } else {
                project.setSqlx(sqxxModel.getSqdjlx());
            }
        } else {
            if (StringUtils.isNotBlank(sqxxModel.getSqdjlx())) {
                project.setSqlx(sqxxModel.getSqdjlx());
                String wdid = bdcZdGlService.getWdidBySqlxdm(sqxxModel.getSqdjlx());
                project.setWorkFlowDefId(wdid);
                String sqlxmc = bdcZdGlService.getSqlxMcByDm(sqxxModel.getSqdjlx());
                if (StringUtils.isNotBlank(sqlxmc)) {
                    List<Map> maps = bdcXmService.getAllLxByWfName(sqlxmc);
                    if (CollectionUtils.isNotEmpty(maps) && maps.get(0) != null && maps.get(0).get(ParamsConstants.DJSYDM_CAPITAL) != null && StringUtils.isNotBlank(maps.get(0).get(ParamsConstants.DJSYDM_CAPITAL).toString())) {
                        djsydm = maps.get(0).get(ParamsConstants.DJSYDM_CAPITAL).toString();
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(project.getSqlx())) {
            String djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
            if (StringUtils.isNotBlank(djsy)) {
                project.setDjsy(djsy);
            } else {
                project.setDjsy(djsydm);
            }
        }
        if (StringUtils.isNotBlank(sqxxModel.getQllx())) {
            project.setQllx(sqxxModel.getQllx());
        }
        if (StringUtils.isNotBlank(sqxxModel.getDjlx())) {
            project.setDjlx(sqxxModel.getDjlx());
        }
    }

    public String getDjidByBdcdyh(String bdcdyh, String bdclx) {
        String djid = "";
        if (StringUtils.isNotBlank(bdcdyh)) {
            if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                djid = djxxMapper.getFwDjidByBdcdyh(AppConfig.getProperty(CONFIGURATION_PARAMETER_QJ_NAME), bdcdyh);
                if (StringUtils.isBlank(djid)) {
                    djid = djxxMapper.getYcFwDjidByBdcdyh(AppConfig.getProperty(CONFIGURATION_PARAMETER_QJ_NAME), bdcdyh);
                }
            } else {
                HashMap map = new HashMap();
                map.put("hhSearch", bdcdyh);
                map.put("bdclx", bdclx);
                djid = djxxMapper.getDjid(map);
            }
        }

        return djid;
    }


    /**
     * @param workFlowName
     * @return SysWorkFlowDefineId
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据流程名称获取流程定义ID
     */
    private String getSysWorkFlowDefineIdByWorkFlowName(String workFlowName) {
        StringBuilder workFlowDefineId = new StringBuilder();
        List<PfWorkFlowDefineVo> pfWorkFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineList();
        if (pfWorkFlowDefineVoList != null && !pfWorkFlowDefineVoList.isEmpty()) {
            //循环比较,流程名称一致时获取流程定义ID
            for (PfWorkFlowDefineVo pfWorkFlowDefineVo : pfWorkFlowDefineVoList) {
                if (StringUtils.equals(pfWorkFlowDefineVo.getWorkflowName(), workFlowName)) {
                    workFlowDefineId.append(pfWorkFlowDefineVo.getWorkflowDefinitionId());
                }
            }
        }
        return workFlowDefineId.toString();
    }

    /**
     * @param djModel
     * @param project
     * @return Msg
     * @author <a href="mailto:liuxing@gtmap.cn">liuxing</a>
     * @description 根据流程名称获取流程定义ID
     */
    public String getQlrFromDjmodel(DjModel djModel, Project project, String gyfs) {
        List<InsertVo> qlrs = new ArrayList<InsertVo>();
        if (djModel != null && project != null&&StringUtils.isNotBlank(project.getWiid())) {
            List<BdcXm> bdcXmList = null;
            if (StringUtils.isNotBlank(project.getWiid())) {
                bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
            }
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                String sfzjzl;
                for (BdcXm xm : bdcXmList) {
                    List<QlrModel> qlrModels = djModel.getQlrLisr();
                    if (CollectionUtils.isNotEmpty(qlrModels)) {
                        for (QlrModel qlrModel : qlrModels) {
                            sfzjzl = bdcZdGlService.getZjzlByMc(qlrModel.getQlrsfzjzl());
                            BdcQlr bdcQlr = new BdcQlr();
                            if (StringUtils.isBlank(qlrModel.getQlrid())) {
                                bdcQlr.setQlrid(UUIDGenerator.generate());
                            } else {
                                bdcQlr.setQlrid(qlrModel.getQlrid());
                            }
                            bdcQlr.setProid(xm.getProid());
                            bdcQlr.setQlrmc(qlrModel.getQlrmc());
                            bdcQlr.setXb(qlrModel.getXb());
                            bdcQlr.setQlrlx(qlrModel.getQlrlx());
                            bdcQlr.setQlbl(qlrModel.getQlbl());
                            bdcQlr.setQlrsfzjzl(sfzjzl);
                            bdcQlr.setQlrzjh(qlrModel.getQlrzjh());
                            bdcQlr.setQlrtxdz(qlrModel.getQlrtxdz());
                            bdcQlr.setQlrlxdh(qlrModel.getQlrlxdh());
                            bdcQlr.setQlrdlr(qlrModel.getDlrmc());
                            bdcQlr.setQlrdlrdh(qlrModel.getDlrdh());
                            bdcQlr.setQlrfddbr(qlrModel.getFddbrhfzr());
                            bdcQlr.setQlrfddbrdh(qlrModel.getFddbrhfzrdh());
                            bdcQlr.setGyfs(gyfs);
                            qlrs.add(bdcQlr);
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(qlrs)) {
                    entityMapper.batchSaveSelective(qlrs);
                }
            }
        }
        return "";
    }


    public void setUserInFToProject(Project project, String userid) {
        if (StringUtils.isNotBlank(userid)) {
            project.setUserId(userid);
            //获取dwdm
            List<PfOrganVo> pfOrganVoList = sysUserService.getOrganListByUser(userid);
            if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
                project.setDwdm(pfOrganVoList.get(0).getRegionCode());
            }
            //获取创建人
            PfUserVo pfUserVo = sysUserService.getUserVo(userid);
            if (pfUserVo != null) {
                project.setCjr(pfUserVo.getUserName());
            }
        }
    }

    //信息验证
    @Override
    public String validateProject(List<DjModel> djModelList) {
        String msg = "";
        if (CollectionUtils.isNotEmpty(djModelList)) {
            String xtly = djModelList.get(0).getSqxxModelList().get(0).getXtly();
            try {
                if (StringUtils.equals(xtly, Constants.XTLY_YHSL)) {
                    //银行创建
                    msg = valiteYhLogic(djModelList);
                } else if (StringUtils.equals(xtly, "3")) {
                    msg = valiteYhLogic(djModelList);
                } else if (StringUtils.equals(xtly, Constants.XTLY_PLSL)) {
                    msg = valiteYhLogic(djModelList);
                }else if(StringUtils.equals(xtly, Constants.XTLY_JCPTSL)){
                    msg = valiteYhLogic(djModelList);
                } else {
                    //zip创建
                    msg = valiteFiled(djModelList);
                    if (StringUtils.isBlank(msg)) {
                        msg = valiteLogic(djModelList);
                    }
                }
            } catch (Exception e) {
                logger.error("ProjectManageServiceImpl.validateProject",e);
                msg = e.getMessage();
            }
        }
        return msg;
    }

    @Override
    public String createProjectToYcsl(List<YcslxxModel> ycslxxModelList, String userid) {
        return null;
    }

    @Override
    public String validateProjectToYcsl(List<YcslxxModel> ycslxxModelList) {
        return null;
    }

    //验证字段
    private String valiteFiled(List<DjModel> djModelList) {
        String[] loc;
        List<HashMap> maps = new ArrayList<HashMap>();
        String msg = "";
        HashMap checkData = checkData(djModelList);
        if (checkData != null && checkData.size() > 0) {
            if (checkData.get("sqxx") != null) {
                Map<String, String> mapSqxx = (Map) checkData.get("sqxx");
                for (String key : mapSqxx.keySet()) {
                    HashMap map = new HashMap();
                    loc = key.split("\\$");
                    map.put(PARAMETER_EXCELNAME, mapSqxx.get(key));//excelName 即表单的名称，以sqh表示
                    map.put(PARAMETER_SHEETNAME, "基本信息");
                    map.put(PARAMETER_LOCATION, "第" + (Integer.valueOf(loc[0]) + 1) + "条记录");//location 表示第几条记录
                    map.put(PARAMETER_DATANAME, loc[1]);//表示字段名
                    maps.add(map);
                }
            }
            if (checkData.get("qlr") != null) {
                Map<String, String> mapQlr = (Map) checkData.get("qlr");
                for (String key : mapQlr.keySet()) {
                    HashMap map = new HashMap();
                    loc = key.split("\\$");
                    map.put(PARAMETER_EXCELNAME, mapQlr.get(key));
                    map.put(PARAMETER_SHEETNAME, "权利人");
                    map.put(PARAMETER_LOCATION, "第" + (Integer.valueOf(loc[0]) + 1) + "条记录");//location 表示第几条记录
                    map.put(PARAMETER_DATANAME, loc[1]);
                    maps.add(map);
                }
            }
            if (null != checkData.get("ywr")) {
                Map<String, String> mapQlr = (Map) checkData.get("ywr");
                for (String key : mapQlr.keySet()) {
                    HashMap map = new HashMap();
                    loc = key.split("\\$");
                    map.put(PARAMETER_EXCELNAME, mapQlr.get(key));
                    map.put(PARAMETER_SHEETNAME, "义务人");
                    map.put(PARAMETER_LOCATION, "第" + (Integer.valueOf(loc[0]) + 1) + "条记录");//location 表示第几条记录
                    map.put(PARAMETER_DATANAME, loc[1]);
                    maps.add(map);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(maps)) {
            msg = JSON.toJSON(maps).toString();
        }
        return msg;
    }

    //逻辑验证
    private String valiteLogic(List<DjModel> djModelList) {
        String msg = "";
        String[] loc;
        List<HashMap> maps = new ArrayList<HashMap>();
        HashMap checkLogic = checkLogic(djModelList);
        if (null != checkLogic) {
            String location;
            for (Object key : checkLogic.keySet()) {
                location = (String) key;
                HashMap map = new HashMap();
                loc = location.split("\\$");
                map.put(PARAMETER_SHEETNAME, "基本信息");
                map.put(PARAMETER_EXCELNAME, loc[2]);
                map.put(PARAMETER_LOCATION, "第" + (Integer.valueOf(loc[1]) + 1) + "条记录");//location表示第几条记录
                map.put(ParamsConstants.CHECKMSG_HUMP, checkLogic.get(key));
                map.put("bdcdyh", loc[0]);
                maps.add(map);
            }
        }
        if (CollectionUtils.isNotEmpty(maps)) {
            msg = JSON.toJSON(maps).toString();
        }
        return msg;
    }

    //银行逻辑验证
    private String valiteYhLogic(List<DjModel> djModelList) {
        String msg = "";
        HashMap checkMaps = getCheckInfo(djModelList);
        if (checkMaps != null && checkMaps.size() != 0) {
            if (checkMaps.containsKey(ParamsConstants.ALERT_LOWERCASE)) {
                msg = "alert/" + checkMaps.get(ParamsConstants.ALERT_LOWERCASE);
            } else if (checkMaps.containsKey(ParamsConstants.CONFIRM_LOWERCAS)) {
                msg = "confirm/" + checkMaps.get(ParamsConstants.CONFIRM_LOWERCAS);
            }
        }
        return msg;
    }

    /**
     * @param djModels
     * @return HashMap
     * @author <a href="mailto:liuxing@gtmap.cn">liuxing</a>
     * @description 检查读取到的sqxx和qlr数据与格式
     */
    public HashMap checkData(List<DjModel> djModels) {
        HashMap hashMap = new HashMap();
        if (CollectionUtils.isNotEmpty(djModels)) {
            BeanMap beanMap;
            Map<String, String> mapSqxx = Maps.newHashMap();
            Map<String, String> mapQlr = Maps.newHashMap();
            DjModel djModel;
            String location;
            for (int dj = 0; dj < djModels.size(); dj++) {
                djModel = djModels.get(dj);
                if (djModel != null) {
                    List<SqxxModel> sqxxModels = djModel.getSqxxModelList();
                    List<QlrModel> qlrModels = djModel.getQlrLisr();
                    SqxxModel sqxxModel;
                    QlrModel qlrModel;
                    //sqxx中是否有空值
                    if (CollectionUtils.isNotEmpty(sqxxModels)) {
                        for (int sq = 0; sq < sqxxModels.size(); sq++) {
                            sqxxModel = sqxxModels.get(sq);
                            if (null != sqxxModel) {
                                beanMap = BeanMap.create(sqxxModel);
                                //判断申请类型确定哪些字段可以为空
                                for (Object key : beanMap.keySet()) {
                                    if (CommonUtil.indexOfStrs(getCheckNullParamBySqlx(sqxxModel.getSqdjlx()), key.toString()) && beanMap.get(key) == null) {
                                        location = String.valueOf(sq) + Constants.SPLIT_STR + key.toString() + Constants.SPLIT_STR + String.valueOf(dj);
                                        mapSqxx.put(location, sqxxModel.getSqh());
                                    }
                                }
                            }
                        }
                        //验证信息放入里面
                        if (null != mapSqxx && mapSqxx.size() > 0) {
                            hashMap.put("sqxx", mapSqxx);
                        }
                    }

                    //qlr中是否有空值或格式不当的值
                    if (CollectionUtils.isNotEmpty(qlrModels)) {
                        for (int qlr = 0; qlr < qlrModels.size(); qlr++) {
                            qlrModel = qlrModels.get(qlr);
                            if (qlrModel != null) {
                                beanMap = BeanMap.create(qlrModel);
                                for (Object key : beanMap.keySet()) {
                                    //空值或者身份证位数不对
                                    if (CommonUtil.indexOfStrs(Constants.REGISTER_KEY_QLR, key.toString()) && beanMap.get(key) == null || StringUtils.equals(CommonUtil.formatEmptyValue(beanMap.get(key)), "身份证") && qlrModel.getQlrzjh().length() != 18) {
                                        location = String.valueOf(qlr) + Constants.SPLIT_STR + key.toString() + Constants.SPLIT_STR + String.valueOf(dj);
                                        mapQlr.put(location, qlrModel.getQlrmc());
                                    }
                                    if (null != mapQlr && mapQlr.size() > 0) {
                                        hashMap.put(qlrModel.getQlrlx(), mapQlr);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    /**
     * @param djModels
     * @return HashMap
     * @author <a href="mailto:liuxing@gtmap.cn">liuxing</a>
     * @description 检查DjModel，与sever中强制检查逻辑相同
     */
    public HashMap checkLogic(List<DjModel> djModels) {
        HashMap logicMap = new HashMap();
        String location = "";
        String checkmsg = "";
        List<Map<String, Object>> checkMsg;
        if (CollectionUtils.isNotEmpty(djModels)) {
            List<SqxxModel> sqxxModels;
            DjModel djModel;
            for (int dj = 0; dj < djModels.size(); dj++) {
                djModel = djModels.get(dj);
                String wdid;
                String sqlx;
                String djid;
                List<Map> maps;
                Project project = new Project();
                sqxxModels = djModel.getSqxxModelList();
                if (CollectionUtils.isNotEmpty(sqxxModels)) {
                    SqxxModel sqxxModel;
                    for (int sq = 0; sq < sqxxModels.size(); sq++) {
                        sqxxModel = sqxxModels.get(sq);
                        if (StringUtils.isNotBlank(sqxxModel.getSqdjlxmc())) {
                            wdid = getSysWorkFlowDefineIdByWorkFlowName(sqxxModel.getSqdjlxmc());
                            project.setWorkFlowDefId(wdid);
                            maps = bdcXmService.getAllLxByWfName(sqxxModel.getSqdjlxmc());
                            if (CollectionUtils.isNotEmpty(maps)) {
                                sqlx = (String) maps.get(0).get(ParamsConstants.SQLXDM_CAPITAL);
                                if (StringUtils.isNotBlank(sqlx)) {
                                    project.setSqlx(sqlx);
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getBdcdyh())) {
                            project.setBdcdyh(sqxxModel.getBdcdyh());
                            project.setBdclx("TDFW");
                            djid = djxxMapper.getFwDjidByBdcdyh(AppConfig.getProperty(CONFIGURATION_PARAMETER_QJ_NAME), sqxxModel.getBdcdyh());
                            project.setDjId(djid);
                        }
                        if (StringUtils.isNotBlank(project.getSqlx())) {
                            String djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                            project.setDjsy(djsy);
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getQllx())) {
                            project.setQllx(sqxxModel.getQllx());
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getDjlx())) {
                            project.setDjlx(sqxxModel.getDjlx());
                        }
                        if (null != project) {
                            checkMsg = projectCheckInfoService.checkXmByProject(project);
                            if (CollectionUtils.isNotEmpty(checkMsg)) {
                                checkmsg = (String) checkMsg.get(0).get(ParamsConstants.CHECKMSG_HUMP);
                                location = sqxxModel.getBdcdyh() + Constants.SPLIT_STR + String.valueOf(sq) + Constants.SPLIT_STR + sqxxModel.getSqh() + Constants.SPLIT_STR + String.valueOf(dj);
                                logicMap.put(location, checkmsg);
                            }
                        }

                    }
                }
            }
        }
        return logicMap;
    }

    public HashMap getCheckInfo(List<DjModel> djModels) {
        HashMap hashMap = new HashMap();
        StringBuilder confirm = new StringBuilder();
        List<Map<String, Object>> checkMsg;
        if (CollectionUtils.isNotEmpty(djModels)) {
            List<SqxxModel> sqxxModels;
            DjModel djModel;
            for (int dj = 0; dj < djModels.size(); dj++) {
                djModel = djModels.get(dj);
                String wdid;
                String sqlx;
                String djid;
                List<Map> maps;
                Project project = new Project();
                sqxxModels = djModel.getSqxxModelList();
                if (CollectionUtils.isNotEmpty(sqxxModels)) {
                    SqxxModel sqxxModel;
                    for (int sq = 0; sq < sqxxModels.size(); sq++) {
                        sqxxModel = sqxxModels.get(sq);
                        if (StringUtils.equals(Constants.XTLY_JYSL, sqxxModel.getXtly())) {
                            project.setXtly(Constants.XTLY_JYSL);
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getSqdjlx())) {
                            HashMap map = new HashMap();
                            map.put("dm", sqxxModel.getSqdjlx());
                            List<BdcZdSqlx> bdcZdSqlxList = bdcZdGlService.getBdcSqlxByMap(map);
                            if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
                                BdcZdSqlx bdcZdSqlx = bdcZdSqlxList.get(0);
                                if (StringUtils.isNotBlank(bdcZdSqlx.getMc())) {
                                    sqxxModel.setSqdjlxmc(bdcZdSqlx.getMc());
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getSqdjlxmc())) {
                            wdid = getSysWorkFlowDefineIdByWorkFlowName(sqxxModel.getSqdjlxmc());
                            project.setWorkFlowDefId(wdid);
                            maps = bdcXmService.getAllLxByWfName(sqxxModel.getSqdjlxmc());
                            if (CollectionUtils.isNotEmpty(maps)) {
                                sqlx = (String) maps.get(0).get(ParamsConstants.SQLXDM_CAPITAL);
                                if (StringUtils.isNotBlank(sqlx)) {
                                    project.setSqlx(sqlx);
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getBdcdyh())) {
                            project.setBdcdyh(sqxxModel.getBdcdyh());
                            project.setBdclx("TDFW");
                            djid = djxxMapper.getFwDjidByBdcdyh(AppConfig.getProperty(CONFIGURATION_PARAMETER_QJ_NAME), sqxxModel.getBdcdyh());
                            project.setDjId(djid);
                        }
                        if (StringUtils.isNotBlank(project.getSqlx())) {
                            String djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                            project.setDjsy(djsy);
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getQllx())) {
                            project.setQllx(sqxxModel.getQllx());
                        }
                        if (StringUtils.isNotBlank(sqxxModel.getDjlx())) {
                            project.setDjlx(sqxxModel.getDjlx());
                        }
                        // xusong 在这里传入proid 是为了避免在同一个项目 && 导入同样的不动产单元被禁止
                        if (StringUtils.isNotBlank(sqxxModel.getProid())) {
                            project.setProid(sqxxModel.getProid());
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(sqxxModel.getProid());
                            if (bdcXm != null) {
                                project.setWiid(bdcXm.getWiid());
                            }
                        }
                        if(StringUtils.equals(Constants.XTLY_JCPTSL,sqxxModel.getXtly())) {
                            if(StringUtils.equals(sqxxModel.getXmly(),Constants.XMLY_BDC)) {
                                project.setYxmid(sqxxModel.getYproid());
                            }else{
                                project.setGdproid(sqxxModel.getYproid());
                                project.setYqlid(sqxxModel.getYqlid());
                            }
                        }else{
                            String yxmid = "";
                            if (StringUtils.isNotBlank(sqxxModel.getYbdcqzh())) {
                                project.setYbdcqzh(sqxxModel.getYbdcqzh());
                                yxmid = bdcZsService.getProidByBdczqh(sqxxModel.getYbdcqzh());
                                if (StringUtils.isNotBlank(yxmid)) {
                                    project.setYxmid(yxmid);
                                } else {
                                    getGdproidForProject(project, sqxxModel);
                                }
                            }
                        }

                        if (StringUtils.isNotBlank(sqxxModel.getJybh())){
                            project.setJybh(sqxxModel.getJybh());
                        }
                        if (null != project) {
                            checkMsg = projectCheckInfoService.checkXmByProject(project);
                            if (CollectionUtils.isNotEmpty(checkMsg)) {
                                for (Map<String, Object> map : checkMsg) {
                                    String info = CommonUtil.formatEmptyValue(map.get(ParamsConstants.CHECKMSG_HUMP));
                                    if (StringUtils.isNotBlank(sqxxModel.getJybh())) {
                                        info = "交易编号" + sqxxModel.getJybh() + ":" + info;
                                    }
                                    String checkModel = CommonUtil.formatEmptyValue(map.get("checkModel"));
                                    if (StringUtils.equals(checkModel, ParamsConstants.ALERT_LOWERCASE)) {
                                        hashMap.put(ParamsConstants.ALERT_LOWERCASE, info);
                                        break;
                                    } else {
                                        if (StringUtils.isNotBlank(confirm)) {
                                            confirm.append("\n").append(info);
                                        } else {
                                            confirm.append(info);
                                        }
                                    }
                                }
                                if (hashMap == null || hashMap.size() == 0) {
                                    hashMap.put(ParamsConstants.CONFIRM_LOWERCAS, confirm);
                                }
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    private void getGdproidForProject(Project project, SqxxModel sqxxModel){
        if(sqxxModel.getYbdcqzh().contains("、")){
            String[] ybdcqzhArray = sqxxModel.getYbdcqzh().split("、");
            if(ybdcqzhArray!=null && ybdcqzhArray.length>0){
                List<String> ygdproidAllList = new ArrayList<String>();
                for(String ybdcqzh : ybdcqzhArray){
                    List<String> ygdproidList = null;
                    if(StringUtils.isNotBlank(sqxxModel.getXtly()) && StringUtils.equals(sqxxModel.getXtly(),Constants.XTLY_YHSL)
                            && StringUtils.isNotBlank(sqxxModel.getSqdjlx()) &&
                            (StringUtils.equals(sqxxModel.getSqdjlx(), Constants.SQLX_DYZX) || StringUtils.equals(sqxxModel.getSqdjlx(), Constants.SQLX_DYBG))){
                        ygdproidList = gdXmService.getDyGdproidByGdzh(ybdcqzh);
                    }else if(StringUtils.isNotBlank(sqxxModel.getBdcdyh()) && sqxxModel.getBdcdyh().length() == 28 && StringUtils.equals(sqxxModel.getBdcdyh().substring(19, 20), Constants.DZWTZM_F)){
                        ygdproidList = gdXmService.getFwGdproidByGdzh(ybdcqzh);
                    }else if(StringUtils.isNotBlank(sqxxModel.getBdcdyh()) && sqxxModel.getBdcdyh().length() == 28 && StringUtils.equals(sqxxModel.getBdcdyh().substring(19, 20), Constants.DZWTZM_W)){
                        ygdproidList = gdXmService.getTdGdproidByGdzh(ybdcqzh);
                    }
                    if(CollectionUtils.isNotEmpty(ygdproidList)){
                        ygdproidAllList.add(ygdproidList.get(0));
                    }
                }
                if(CollectionUtils.isNotEmpty(ygdproidAllList)){
                    project.setGdproid(PublicUtil.join("、", ygdproidAllList));
                }
            }
        }else {
            List<String> ygdproidList = null;
            if(StringUtils.isNotBlank(sqxxModel.getXtly()) && StringUtils.equals(sqxxModel.getXtly(),Constants.XTLY_YHSL)
                    && StringUtils.isNotBlank(sqxxModel.getSqdjlx()) &&
                    (StringUtils.equals(sqxxModel.getSqdjlx(), Constants.SQLX_DYZX) || StringUtils.equals(sqxxModel.getSqdjlx(), Constants.SQLX_DYBG))){
                ygdproidList = gdXmService.getDyGdproidByGdzh(project.getYbdcqzh());
            }else if(StringUtils.isNotBlank(sqxxModel.getBdcdyh()) && sqxxModel.getBdcdyh().length() == 28 && StringUtils.equals(sqxxModel.getBdcdyh().substring(19, 20), Constants.DZWTZM_F)){
                ygdproidList = gdXmService.getFwGdproidByGdzh(project.getYbdcqzh());
            }else if(StringUtils.isNotBlank(sqxxModel.getBdcdyh()) && sqxxModel.getBdcdyh().length() == 28 && StringUtils.equals(sqxxModel.getBdcdyh().substring(19, 20), Constants.DZWTZM_W)){
                ygdproidList = gdXmService.getTdGdproidByGdzh(project.getYbdcqzh());
            }
            if (CollectionUtils.isNotEmpty(ygdproidList)) {
                project.setGdproid(ygdproidList.get(0));
            }
        }



    }


    public String[] getCheckNullParamBySqlx(String sqlx) {
        String[] prams = null;
        if (StringUtils.equals(sqlx, Constants.SQLX_SPFMMZYDJ_DM)) {
            prams = Constants.REGISTER_KEY_SPFMMZY;
        } else {
            prams = Constants.REGISTER_KEY_DYDJ;
        }
        return prams;
    }

    public void changeMctoDm(List<DjModel> djModelList) {
        if (CollectionUtils.isNotEmpty(djModelList)) {
            for (DjModel djModel : djModelList) {
                List<SqxxModel> sqxxModelList = djModel.getSqxxModelList();
                for (SqxxModel sqxxModel : sqxxModelList) {
                    //djlx获取dm
                    if (StringUtils.isNotBlank(sqxxModel.getDjlxmc())) {
                        sqxxModel.setDjlx(bdcZdGlService.getDjlxDmByMc(sqxxModel.getDjlxmc()));
                    }
                    //sqlx获取dm
                    if (StringUtils.isNotBlank(sqxxModel.getSqdjlxmc())) {
                        sqxxModel.setSqdjlx(bdcZdGlService.getSqlxDmByMc(sqxxModel.getSqdjlxmc()));
                    }
                    //qllx 获取dm
                    if (StringUtils.isNotBlank(sqxxModel.getQllxmc())) {
                        sqxxModel.setQllx(bdcZdGlService.getQllxDmByMC(sqxxModel.getQllxmc()));
                    }
                    //共有方式dm
                    if (StringUtils.isNotBlank(sqxxModel.getGyfsmc())) {
                        sqxxModel.setGyfs(bdcZdGlService.getGyfsDmByMc(sqxxModel.getGyfsmc()));
                    }
                    //贷款方式dm
                    if (StringUtils.isNotBlank(sqxxModel.getDkfsmc())) {
                        sqxxModel.setDkfs(bdcZdGlService.getDkfsDmByMc(sqxxModel.getDkfsmc()));
                    }
                }
                List<QlrModel> qlrLisrList = djModel.getQlrLisr();
                for (QlrModel qlrModel : qlrLisrList) {
                    if (StringUtils.isNotBlank(qlrModel.getQlrsfzjzl())) {
                        qlrModel.setQlrsfzjzl(bdcZdGlService.getZjzlByMc(qlrModel.getQlrsfzjzl()));
                    }
                }
            }
        }
    }


    @Override
    public void createProject(String wdid, String wiid, String proid, String userid) {

    }


    public void getSingleProject(SqxxModel sqxxModel, Project project, String userid, String bdclx) {
        setSqlxToProject(sqxxModel, project);
        if (StringUtils.isNotBlank(sqxxModel.getBdcdyh())) {
            project.setBdcdyh(sqxxModel.getBdcdyh());
            project.setBdclx(bdclx);
            project.setDjId(getDjidByBdcdyh(sqxxModel.getBdcdyh(), bdclx));
            project.setSqfbcz(sqxxModel.getSffbcz());
        }
        setUserInFToProject(project, userid);
        //暂时写死为来自不动产
        project.setXmly(Constants.XMLY_BDC);
        setYxmxxToProject(sqxxModel, project, bdclx);
    }

    public void getPlProject(List<SqxxModel> sqxxModelList, Project project, String userid, String bdclx) {
        //目前只支持一致的申请类型
        setSqlxToProject(sqxxModelList.get(0), project);
        setUserInFToProject(project, userid);
        project.setBdclx(bdclx);
        project.setWiid(UUIDGenerator.generate());
        project.setXmly(Constants.XMLY_BDC);
        //其余批量流程
        List<String> djidList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        for (SqxxModel sqxxModel : sqxxModelList) {
            //创建项目
            if (StringUtils.isNotBlank(sqxxModel.getYbdcqzh())) {
                setYxmxxToProject(sqxxModel, project, bdclx);
            }
            if (StringUtils.isNotBlank(sqxxModel.getBdcdyh())) {
                bdcdyhList.add(sqxxModel.getBdcdyh());
                String djid = getDjidByBdcdyh(sqxxModel.getBdcdyh(), bdclx);
                project.setDjId(djid);
                djidList.add(djid);
                BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                bdcXmRelList.add(bdcXmRel);
            }
        }
        project.setDjIds(djidList);
        project.setBdcdyhs(bdcdyhList);
        project.setDjId(null);
        project.setBdcdyh(null);
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            project.setBdcXmRelList(bdcXmRelList);
        }

    }

    public void setYxmxxToProject(SqxxModel sqxxModel, Project project, String bdclx) {
        if (StringUtils.equals(sqxxModel.getXtly(), "3")) {
            project.setProid(sqxxModel.getProid());
            if (StringUtils.isNotBlank(project.getProid())) {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
                if (bdcXm != null) {
                    project.setBh(bdcXm.getBh());
                }
            }
            if (StringUtils.isNotBlank(sqxxModel.getYbdcqzh())) {
                project.setXmly(Constants.XMLY_BDC);
                project.setYbdcqzh(sqxxModel.getYbdcqzh());
                project.setYxmid(sqxxModel.getYproid());
            } else if (StringUtils.isNotBlank(sqxxModel.getYfczh())) {
                project.setXmly(Constants.XMLY_FWSP);
                project.setYbdcqzh(sqxxModel.getYfczh());
                project.setGdproid(sqxxModel.getYproid());
                project.setYqlid(sqxxModel.getYqlid());
            }
        } else if (StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_PLSL)) {
            if (StringUtils.isNotBlank(sqxxModel.getYbdcqzh())) {
                project.setXmly(Constants.XMLY_BDC);
                project.setYbdcqzh(sqxxModel.getYbdcqzh());
                project.setYxmid(sqxxModel.getYproid());
            } else if (StringUtils.isNotBlank(sqxxModel.getYfczh())) {
                project.setXmly(Constants.XMLY_FWSP);
                project.setYbdcqzh(sqxxModel.getYfczh());
                project.setGdproid(sqxxModel.getYproid());
                project.setYqlid(sqxxModel.getYqlid());
            }
        }else if(StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_JCPTSL)){
            if(StringUtils.equals(sqxxModel.getXmly(),Constants.XMLY_BDC)) {
                project.setXmly(Constants.XMLY_BDC);
                project.setYxmid(sqxxModel.getYproid());
            }else {
                project.setXmly(Constants.XMLY_FWSP);
                project.setGdproid(sqxxModel.getYproid());
                project.setYqlid(sqxxModel.getYqlid());
            }
        } else {
            if (StringUtils.isNotBlank(sqxxModel.getYbdcqzh())) {
                project.setYbdcqzh(sqxxModel.getYbdcqzh());
                String[] ybdcqzhArray = null;
                // 根据BdcXmServiceImpl.findTdzh()中，不同的产权证号是用‘、’相区别的
                if (sqxxModel.getYbdcqzh().contains("/")) {
                    ybdcqzhArray = sqxxModel.getYbdcqzh().split("/");
                } else if (sqxxModel.getYbdcqzh().contains("、")) {
                    ybdcqzhArray = sqxxModel.getYbdcqzh().split("、");
                } else {
                    ybdcqzhArray = sqxxModel.getYbdcqzh().split(",|，");
                }
                if(sqxxModel instanceof SqFyxxModel && StringUtils.isBlank(sqxxModel.getBdcdyh())){
                    List<GdTdsyq> gdTdsyqList = gdTdService.getTdsyqByTdzh(ybdcqzhArray[0]);
                    List<GdFwsyq> gdFwsyqList = gdFwService.queryFwsyqByCqzh(ybdcqzhArray[0]);
                    if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                        bdclx = Constants.BDCLX_TD;
                        project.setBdclx(bdclx);
                    }
                    if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                        bdclx = Constants.BDCLX_TDFW;
                        project.setBdclx(bdclx);
                    }
                }
                String yCfwh = null;
                if(sqxxModel instanceof SqFyxxModel){
                    yCfwh = ((SqFyxxModel) sqxxModel).getYcfwh();
                }
                if(StringUtils.isNotBlank(yCfwh)){
                    if(StringUtils.isNotBlank(sqxxModel.getBdcdyh())){
                        HashMap map = new HashMap();
                        map.put("bdcdyh",sqxxModel.getBdcdyh());
                        map.put("cfwh",yCfwh);
                        List<BdcCf> bdcCfList = bdcCfService.getBdcCfByCfwhAndBdcdyh(map);
                        if(CollectionUtils.isNotEmpty(bdcCfList)){
                            BdcCf bdcCf = bdcCfList.get(0);
                            if(StringUtils.isNotBlank(bdcCf.getProid())){
                                project.setYxmid(bdcCf.getProid());
                            }
                        }
                        List<GdCf> gdCfList = gdCfService.getGdCfByCfwhAndBdcdyh(map);
                        if(CollectionUtils.isNotEmpty(gdCfList)){
                            GdCf gdCf = gdCfList.get(0);
                            project.setGdproid(gdCf.getProid());
                            project.setYqlid(gdCf.getCfid());
                        }
                    }else{
                        Map gdCfMap = new HashMap();
                        gdCfMap.put("fczh",ybdcqzhArray[0]);
                        gdCfMap.put("tdzh",ybdcqzhArray[0]);
                        gdCfMap.put("cfwh",yCfwh);
                        List<GdCf> fwGdCfList = gdCfService.getGdCfAndCfwhByFczh(gdCfMap);
                        if(CollectionUtils.isNotEmpty(fwGdCfList)){
                            GdCf fwGdCf = fwGdCfList.get(0);
                            project.setGdproid(fwGdCf.getProid());
                            project.setYqlid(fwGdCf.getCfid());
                        }
                        List<GdCf> tdGdCfList = gdCfService.getGdCfAndCfwhByTdzh(gdCfMap);
                        if(CollectionUtils.isNotEmpty(tdGdCfList)){
                            GdCf tdGdCf = tdGdCfList.get(0);
                            project.setGdproid(tdGdCf.getProid());
                            project.setYqlid(tdGdCf.getCfid());
                        }
                    }
                }else{
                    BdcZs bdcZs = bdcZsService.queryBdcZsByBdcqzh(ybdcqzhArray[0]);
                    if (bdcZs != null) {
                        //新建
                        String yproid = bdcXmZsRelService.getProidByZsid(bdcZs.getZsid());
                        project.setYxmid(yproid);
                    } else {
                        //过度
                        String sqlxmc = "";
                        if (StringUtils.isNotBlank(sqxxModel.getSqdjlx())) {
                            sqlxmc = bdcZdGlService.getSqlxMcByDm(sqxxModel.getSqdjlx());
                        }
                        if (StringUtils.indexOf(sqlxmc, "注销") > -1 || StringUtils.indexOf(sqlxmc, "抵押权变更") > -1) {
                            HashMap map = new HashMap();
                            map.put("dydjzmh", ybdcqzhArray[0]);
                            List<GdDy> gdDyList = gdFwService.andEqualQueryGdDy(map);
                            if (CollectionUtils.isNotEmpty(gdDyList)) {
                                project.setGdproid(CommonUtil.formatEmptyValue(gdDyList.get(0).getProid()));
                                project.setYqlid(CommonUtil.formatEmptyValue(gdDyList.get(0).getDyid()));
                            }
                        } else {
                            if (StringUtils.equals(bdclx, "TD")) {
                                List<GdTdsyq> gdTdsyqList = gdTdService.getTdsyqByTdzh(ybdcqzhArray[0]);
                                if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                                    // 当项目来源为互联网+，且上一手为所有权时，直接通过不动产单元号获取相关联的信息。在这里这样处理更多的是为了解决bdcXmRel的问题。
                                    if (StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_YHSL) && StringUtils.isNotBlank(sqxxModel.getBdcdyh())) {
                                        Map<String, String> yXmInfoMap = bdcGdDyhRelService.getTdYqlidsAndYxmidsByBdcdyh(sqxxModel.getBdcdyh());
                                        if (yXmInfoMap != null && yXmInfoMap.get(ParamsConstants.YQLIDS_LOWERCASE) != null && yXmInfoMap.get(ParamsConstants.YXMIDS_LOWERCASE) != null) {
                                            project.setYqlid(yXmInfoMap.get(ParamsConstants.YQLIDS_LOWERCASE));
                                            project.setGdproid(yXmInfoMap.get(ParamsConstants.YXMIDS_LOWERCASE));
                                        } else {
                                            project.setGdproid(CommonUtil.formatEmptyValue(gdTdsyqList.get(0).getProid()));
                                            project.setYqlid(CommonUtil.formatEmptyValue(gdTdsyqList.get(0).getQlid()));
                                        }
                                    } else {
                                        project.setGdproid(CommonUtil.formatEmptyValue(gdTdsyqList.get(0).getProid()));
                                        project.setYqlid(CommonUtil.formatEmptyValue(gdTdsyqList.get(0).getQlid()));
                                    }
                                }
                            } else {
                                HashMap map = new HashMap();
                                map.put("fczh", ybdcqzhArray[0]);
                                List<GdFwsyq> gdFwsyqList = gdFwService.queryFwsyqByCqzh(ybdcqzhArray[0]);
                                if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                                    // 当项目来源为互联网+，且上一手为所有权时，直接通过不动产单元号获取相关联的信息。在这里这样处理更多的是为了解决bdcXmRel的问题。
                                    if (StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_YHSL) && StringUtils.isNotBlank(sqxxModel.getBdcdyh())) {
                                        Map<String, String> yXmInfoMap = bdcGdDyhRelService.getYqlidsAndYxmidsByBdcdyh(sqxxModel.getBdcdyh());
                                        if (yXmInfoMap != null && yXmInfoMap.get(ParamsConstants.YQLIDS_LOWERCASE) != null && yXmInfoMap.get(ParamsConstants.YXMIDS_LOWERCASE) != null) {
                                            project.setYqlid(yXmInfoMap.get(ParamsConstants.YQLIDS_LOWERCASE));
                                            project.setGdproid(yXmInfoMap.get(ParamsConstants.YXMIDS_LOWERCASE));
                                        } else {
                                            project.setGdproid(CommonUtil.formatEmptyValue(gdFwsyqList.get(0).getProid()));
                                            project.setYqlid(CommonUtil.formatEmptyValue(gdFwsyqList.get(0).getQlid()));
                                        }
                                    } else {
                                        project.setGdproid(CommonUtil.formatEmptyValue(gdFwsyqList.get(0).getProid()));
                                        project.setYqlid(CommonUtil.formatEmptyValue(gdFwsyqList.get(0).getQlid()));
                                    }
                                }
                            }

                        }
                    }
                }
            } else if (StringUtils.isNotBlank(sqxxModel.getBdcdyh())) {
                //或者通过不动产单元号查找
                String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(sqxxModel.getBdcdyh());
                if (StringUtils.isNotBlank(bdcdyid)) {
                    HashMap map = new HashMap();
                    map.put("bdcdyid", bdcdyid);
                    map.put("qszt", 1);
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcq(map);
                    if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        project.setYxmid(bdcFdcqList.get(0).getProid());
                    }
                }
            }
            //通过这个取原项目
            if (StringUtils.isNotBlank(project.getYqlid())) {
                if (StringUtils.equals(bdclx, "TDFW")) {
                    project.setXmly(Constants.XMLY_FWSP);
                } else {
                    project.setXmly(Constants.XMLY_TDSP);
                }
            } else {
                project.setXmly(Constants.XMLY_BDC);
            }
        }
    }

    //抽取相同的创建项目
    public String createProject(Project project, DjModel djModel, String xtly) {
        String msg = "";
        String mainproid = "";
        StringBuilder msgBuf = new StringBuilder();
        if (null != project) {
            if (StringUtils.isBlank(project.getProid())) {
                project.setProid(UUIDGenerator.generate18());
            }

            if (StringUtils.isBlank(project.getQllx()) && StringUtils.isNotBlank(project.getBdcdyh())) {
                project.setQllx(bdcdyService.getQllxFormBdcdy(project.getBdcdyh()));
            }

            CreatProjectService creatProjectService = projectService.getCreatProjectService((BdcXm) project);
            //创建工作流项目
            final Project returnProject = creatProjectService.creatWorkFlow(project);
            //创建文件中心相应的节点
            creatProjectNode(returnProject.getProid());
            mainproid = returnProject.getProid();
            //根据已有信息初始化不动产项目信息
            List<InsertVo> list = creatProjectService.initVoFromOldData(project);
            //保存不动产项目信息
//                        通过获取接口进行过滤
            //权利人按录入为准
            list = getBdcxxFromDjModel(list, djModel);
            creatProjectService.saveOrUpdateProjectData(list);
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(returnProject.getProid());
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            //获取项目和SqxxModel的关系
            HashMap hashMap = getRelateBdcXmAndSqxxModel(bdcXmList, djModel.getSqxxModelList(), xtly);
            saveQyQlr(djModel, bdcXmList, hashMap, xtly);


            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);

            if (bdcXm != null) {
                //初始化收件单
                bdcSjdService.createSjxxByBdcxm(bdcXm);
            }
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm xm : bdcXmList) {
                    //qijiadong 未匹配不动产单元不生成权利
                    String sqlx = xm.getSqlx();
                    List<String> bppSqlxdmList = ReadXmlProps.getUnBdcdySqlxDm();
                    if(!(bppSqlxdmList.contains(sqlx) || CommonUtil.indexOfStrs(Constants.SQLX_NOBDCDY_GDQL, sqlx)) && !(StringUtils.equals(sqlx,Constants.SQLX_SFCD) && StringUtils.isBlank(xm.getBdcdyid()))) {
                        turnProjectDefaultService.saveQllxVo(xm);
                    }
                    saveQlxxFromDjModel(xm, djModel, hashMap, xtly);
                    List<SqxxModel> sqxxModelList = djModel.getSqxxModelList();
                    if (CollectionUtils.isNotEmpty(sqxxModelList)) {
                        SqxxModel sqxxModel = sqxxModelList.get(0);
                        if (StringUtils.isNotBlank(sqxxModel.getSqh())) {
                            if (StringUtils.equals(xtly, Constants.XTLY_YHSL)) {
                                xm.setYhsqywh(sqxxModel.getSqh());
                                if (StringUtils.equals(xm.getQllx(), Constants.QLLX_DYAQ)) {
                                    xm.setSfhcdzzz(sqxxModel.getSfhcdzzz());
                                }
                            } else if (StringUtils.equals(xtly, Constants.XTLY_PLSL)) {
                                xm.setWwslbh(sqxxModel.getSqh());
                            } else {
                                xm.setSpxtywh(sqxxModel.getSqh());
                            }
                            entityMapper.saveOrUpdate(xm, xm.getProid());
                        }
                    }
                    msgBuf.append(xm.getProid() + Constants.SPLIT_STR);


                    //jyl 初始化主房的子户室做附属设施
                    QllxVo qllxVo = qllxService.makeSureQllx(xm);
                    BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(xm.getBdcdyid());
                    if (qllxVo instanceof BdcFdcq&&bdcdy != null) {
                        bdcFwFsssService.initializeBdcFwfsss(bdcdy.getBdcdyh(), xm);
                    }
                    bdcSpxxService.dealWithSpxxZdzhmj(xm);
                    //法院查封获取产权权利人作为查封权利人
                    if(StringUtils.isNotBlank(bdcXm.getQllx()) && StringUtils.equals(bdcXm.getQllx(),Constants.QLLX_CFDJ)){
                        if(StringUtils.isNotBlank(bdcXm.getBdcdyid()) && StringUtils.isNotBlank(bdcXm.getProid())){
                            List<String> proidList = bdcXmService.getXsCqProid(bdcXm.getBdcdyid());
                            if(CollectionUtils.isNotEmpty(proidList)){
                                String xsCqProid = proidList.get(0);
                                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(xsCqProid);
                                List<BdcQlr> cfBdcQlrList = new ArrayList<BdcQlr>();
                                if(CollectionUtils.isNotEmpty(bdcQlrList)){
                                    for(BdcQlr bdcQlr : bdcQlrList){
                                        bdcQlr.setQlrid(UUIDGenerator.generate18());
                                        bdcQlr.setProid(bdcXm.getProid());
                                        cfBdcQlrList.add(bdcQlr);
                                    }
                                }
                                if(CollectionUtils.isNotEmpty(cfBdcQlrList)){
                                    entityMapper.insertBatchSelective(cfBdcQlrList);
                                }
                            }else{
                                String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
                                if(StringUtils.isNotBlank(bdcdyh)){
                                    List<String> xsGdCqProidList = bdcXmService.getXsGdCqProidByBdcdyh(bdcdyh);
                                    if(CollectionUtils.isNotEmpty(xsGdCqProidList)){
                                        String xsGdCqProid = xsGdCqProidList.get(0);
                                        List<GdQlr> gdQlrList = gdQlrService.queryGdQlrListByProid(xsGdCqProid,Constants.QLRLX_QLR);
                                        List<BdcQlr> cfBdcQlrList = new ArrayList<BdcQlr>();
                                        if(CollectionUtils.isNotEmpty(gdQlrList)){
                                            List<BdcQlr> bdcQlrList = gdQlrService.readGdQlrs(gdQlrList);
                                            if(CollectionUtils.isNotEmpty(bdcQlrList)){
                                                for(BdcQlr bdcQlr : bdcQlrList){
                                                    bdcQlr.setProid(bdcXm.getProid());
                                                    cfBdcQlrList.add(bdcQlr);
                                                }
                                            }
                                        }
                                        if(CollectionUtils.isNotEmpty(cfBdcQlrList)){
                                            entityMapper.insertBatchSelective(cfBdcQlrList);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //更新不动产工作流项目remark和工程名称
                    creatProjectService.updateWorkFlow(bdcXm);
                }
            }

            if (null != msgBuf && StringUtils.isNotBlank(msgBuf.toString()) && !StringUtils.equals(xtly, Constants.XTLY_YHSL)) {
                msg = msgBuf.toString().substring(0, msgBuf.length() - 1);
            }
            //组织回传json
            if (bdcXm != null && StringUtils.equals(xtly, Constants.XTLY_YHSL)) {
                HashMap map = new HashMap();
                map.put("proid", mainproid);
                map.put("taskid", returnProject.getTaskid());
                map.put("wiid", bdcXm.getWiid());
                msg = JSON.toJSONString(map);
            }
        }
        return msg;
    }

    //集成平台创建项目
    public String createJcptProject(Project project, DjModel djModel, String xtly) {
        String msg = "";
        String mainproid = "";
        if (null != project) {
            if (StringUtils.isBlank(project.getProid())) {
                project.setProid(UUIDGenerator.generate18());
            }

            if (StringUtils.isBlank(project.getQllx()) && StringUtils.isNotBlank(project.getBdcdyh())) {
                project.setQllx(bdcdyService.getQllxFormBdcdy(project.getBdcdyh()));
            }

            CreatProjectService creatProjectService = projectService.getCreatProjectService((BdcXm) project);
            //创建工作流项目
            final Project returnProject = creatProjectService.creatWorkFlow(project);
            //创建文件中心相应的节点
            creatProjectNode(returnProject.getProid());
            mainproid = returnProject.getProid();
            //根据已有信息初始化不动产项目信息
            List<InsertVo> list = creatProjectService.initVoFromOldData(project);
            creatProjectService.saveOrUpdateProjectData(list);
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(returnProject.getProid());
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);


            if (bdcXm != null) {
                //初始化收件单
                bdcSjdService.createSjxxByBdcxm(bdcXm);
            }
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm xm : bdcXmList) {
                    turnProjectDefaultService.saveQllxVo(xm);
                    SqxxModel sqxxModel = null;
                    if(StringUtils.equals(xm.getQllx(),Constants.QLLX_DYAQ)) {
                        if (CollectionUtils.isNotEmpty(djModel.getSqxxModelList())) {
                            for(SqxxModel sqxxModelTemp:djModel.getSqxxModelList()) {
                                if(StringUtils.equals(sqxxModelTemp.getQllx(),Constants.QLLX_DYAQ)){
                                    sqxxModel = sqxxModelTemp;
                                    break;
                                }
                            }
                        }
                    }else{
                        if (CollectionUtils.isNotEmpty(djModel.getSqxxModelList())) {
                            for(SqxxModel sqxxModelTemp:djModel.getSqxxModelList()) {
                                if(!StringUtils.equals(sqxxModelTemp.getQllx(),Constants.QLLX_DYAQ)){
                                    sqxxModel = sqxxModelTemp;
                                    break;
                                }
                            }
                        }
                    }
                    // 删除原有的所有权利人义务人，因为二者全部来自于集成平台
                    bdcQlrMapper.delQlrByProids(new String[]{xm.getProid()});
                    initBdcXmJbxx(xm,djModel,sqxxModel);
                    //更新不动产工作流项目remark和工程名称
                    creatProjectService.updateWorkFlow(bdcXm);
                    QllxVo qllxModel = qllxService.makeSureQllx(xm);
                    QllxVo qllxVo = qllxService.queryQllxVo(qllxModel, xm.getProid());
                    QllxVo saveQllxVo = getQllxVoFromSqxxModel(sqxxModel, qllxVo);
                    if (saveQllxVo != null) {
                        entityMapper.saveOrUpdate(saveQllxVo, saveQllxVo.getQlid());
                    }

                    if (qllxVo instanceof BdcFdcq || qllxVo instanceof BdcFdcqDz) {
                        updateXzzrnxByJybh(sqxxModel.getJybh(), xm.getProid());
                    }
                    initBdcXmSpxx(xm, sqxxModel);
                    initBdcJyxxAndJyht(xm,sqxxModel);
                    //jyl 初始化主房的子户室做附属设施
                    BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(xm.getBdcdyid());
                    if (qllxModel instanceof BdcFdcq&&bdcdy != null) {
                        bdcFwFsssService.initializeBdcFwfsss(bdcdy.getBdcdyh(), xm);
                    }
                    bdcSpxxService.dealWithSpxxZdzhmj(xm);
                    modifyBdcFdcqGyqk(xm.getProid());
                    // 生成qlqtzk和fj
                    bdcZsService.saveBdcXtConfigQlqtzkAndFj(xm);
                }
            }

            //收件自动转发到受理（初审）
            autoWorkFlowService.autoTurnProjectByBdcXm(bdcXm,Constants.WORKFLOW_SJ,Constants.WORKFLOW_SLCS,project.getUserId());
            //上传人员照片附件
            integrationPlatformService.uploadRyzpFileFromJcptToBdcdj(mainproid);

            //组织回传json
            if (bdcXm != null && StringUtils.equals(xtly, Constants.XTLY_JCPTSL)) {
                PfTaskVo pfTaskVo = PlatformUtil.getPfTaskVoByWiid(bdcXm.getWiid());
                HashMap map = new HashMap();
                map.put("proid", mainproid);
                if(pfTaskVo != null) {
                    map.put("taskid", pfTaskVo.getTaskId());
                }
                map.put("wiid", bdcXm.getWiid());
                msg = JSON.toJSONString(map);
            }
        }
        return msg;
    }



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXm
     * @param djModel
     * @param sqxxModel
     * @description 初始化项目基本信息
     */
    void initBdcXmJbxx(BdcXm bdcXm,DjModel djModel,SqxxModel sqxxModel){
        if(bdcXm != null&&djModel != null&&sqxxModel != null){
            intBdcXmByDjModel(djModel,bdcXm,sqxxModel);
            intBdcQlrByDjModel(djModel,bdcXm,sqxxModel);
        }
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param djModel
     * @param xm
     * @param sqxxModel
     * @description 初始化项目信息
     */
    public void intBdcXmByDjModel(DjModel djModel, BdcXm xm, SqxxModel sqxxModel){
        if (null != xm&&sqxxModel != null) {
            if(StringUtils.isBlank(sqxxModel.getSqh())) {
                xm.setWwslbh(sqxxModel.getSqh());
            }else{
                xm.setWwslbh(xm.getBh());
            }
            xm.setSqfbcz(sqxxModel.getSffbcz());
            xm.setDjzx(sqxxModel.getDjzx());
            entityMapper.saveOrUpdate(xm,xm.getProid());
        }
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param djModel
     * @param xm
     * @param sqxxModel
     * @description 初始化权利人信息
     */
    public void intBdcQlrByDjModel(DjModel djModel, BdcXm xm, SqxxModel sqxxModel) {
        if (null != djModel&&null != xm&&sqxxModel != null) {
            List<QlrModel> qlrModels = djModel.getQlrLisr();
            if (CollectionUtils.isNotEmpty(qlrModels)) {
                for (QlrModel qlrModel : qlrModels) {
                    if (StringUtils.equals(qlrModel.getSqid(), sqxxModel.getSqid())) {
                        initBdcQlr(qlrModel, xm);
                    }
                }
            }
        }
    }

    public void initBdcQlr(QlrModel qlrModel, BdcXm xm) {
        String sfzjzl = qlrModel.getQlrsfzjzl();
        if (StringUtils.isNotBlank(sfzjzl) && !NumberUtils.isNumber(sfzjzl)) {
            sfzjzl = bdcQlrMapper.getZjzlByMc(qlrModel.getQlrsfzjzl());
        }
        BdcQlr bdcQlr = new BdcQlr();
        bdcQlr.setQlrid(UUIDGenerator.generate());
        bdcQlr.setProid(xm.getProid());
        bdcQlr.setGyfs(qlrModel.getGyfs());
        bdcQlr.setQlrmc(qlrModel.getQlrmc());
        bdcQlr.setQlrlx(qlrModel.getQlrlx());
        bdcQlr.setXb(qlrModel.getXb());
        bdcQlr.setQlbl(qlrModel.getQlbl());
        bdcQlr.setQlrsfzjzl(sfzjzl);
        bdcQlr.setQlrzjh(qlrModel.getQlrzjh());
        bdcQlr.setQlrtxdz(qlrModel.getQlrtxdz());
        bdcQlr.setQlrlxdh(qlrModel.getQlrlxdh());
        bdcQlr.setQlrdlr(qlrModel.getDlrmc());
        bdcQlr.setQlrdlrdh(qlrModel.getDlrdh());
        bdcQlr.setQlrdlrzjh(qlrModel.getQlrdlrzjh());
        bdcQlr.setQlrdlrzjzl(qlrModel.getQlrdlrzjzl());
        bdcQlr.setQlrfddbr(qlrModel.getFddbrhfzr());
        bdcQlr.setQlrfddbrdh(qlrModel.getFddbrhfzrdh());
        bdcQlr.setSfzxxly(Constants.SFZXXLY_JCPT);
        entityMapper.saveOrUpdate(bdcQlr,bdcQlr.getQlrid());
    }

    public void setZlToSpxx(SqxxModel sqxxModel, BdcXm bdcXm) {
        if (StringUtils.isNotBlank(sqxxModel.getZl())) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
            if (bdcSpxx != null) {
                bdcSpxx.setZl(sqxxModel.getZl());
                entityMapper.updateByPrimaryKeySelective(bdcSpxx);
            }
            bdcXm.setZl(sqxxModel.getZl());
            if (StringUtils.equals(sqxxModel.getDyfsmc(), Constants.DYFS_YBDY_MC)) {
                bdcXm.setDjzx("801");
            } else if (StringUtils.equals(sqxxModel.getDyfsmc(), Constants.DYFS_ZGEDY_MC)) {
                bdcXm.setDjzx("802");
            } else {
                bdcXm.setDjzx("801");
            }
            entityMapper.updateByPrimaryKeySelective(bdcXm);
        }
    }


    public void setDjzxToBdcXm(SqxxModel sqxxModel, BdcXm bdcXm) {
        if (sqxxModel != null && bdcXm != null) {
            if (StringUtils.equals(sqxxModel.getDyfsmc(), Constants.DYFS_YBDY_MC)) {
                bdcXm.setDjzx("801");
            } else if (StringUtils.equals(sqxxModel.getDyfsmc(), Constants.DYFS_ZGEDY_MC)) {
                bdcXm.setDjzx("802");
            } else {
                bdcXm.setDjzx("801");
            }
            if(sqxxModel instanceof  SqFyxxModel){
                bdcXm.setDjzx("");
                String cflxMc = ((SqFyxxModel) sqxxModel).getCflx();
                String sqlxdm = sqxxModel.getSqdjlx();
                if(StringUtils.isNotBlank(cflxMc) && !StringUtils.equals(sqlxdm,Constants.SQLX_JF_SFCZ)){
                    HashMap djzxMap = new HashMap();
                    djzxMap.put("djzxmc", cflxMc);
                    List<HashMap> djzxHashMapList = bdcZdGlService.getDjzx(djzxMap);
                    if(CollectionUtils.isNotEmpty(djzxHashMapList)) {
                        HashMap djzxHashMap = djzxHashMapList.get(0);
                        if(djzxHashMap.get("DM") != null){
                            bdcXm.setDjzx(djzxHashMap.get("DM").toString());
                            if(StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_JF)){
                                bdcXm.setDjyy(Constants.DJZXMC_JF);
                            }else{
                                bdcXm.setDjyy(cflxMc);
                            }
                        }
                    }
                }
            }
            entityMapper.updateByPrimaryKeySelective(bdcXm);
        }
    }

    public void getBdcData(List<DjModel> djModelList) {
        if (CollectionUtils.isNotEmpty(djModelList)) {
            for (DjModel djModel : djModelList) {
                List<SqxxModel> sqxxModelList = djModel.getSqxxModelList();
                if (CollectionUtils.isNotEmpty(sqxxModelList)) {
                    for (SqxxModel sqxxModel : sqxxModelList) {
                        if (StringUtils.isNotBlank(sqxxModel.getYbdcqzh())&&
                                (StringUtils.isBlank(sqxxModel.getBdcdyh()) || StringUtils.isBlank(sqxxModel.getZl()))) {
                            String bdcqzh = sqxxModel.getYbdcqzh();
                            if (sqxxModel.getYbdcqzh().contains("/")) {
                                bdcqzh = sqxxModel.getYbdcqzh().split("/")[0];
                            }
                            List<BdcXm> bdcXmList = bdcXmService.getBdcXmByBdcqzh(bdcqzh);
                            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                                //不动产单元号
                                if (StringUtils.isBlank(sqxxModel.getBdcdyh())) {
                                    BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(bdcXmList.get(0).getBdcdyid());
                                    if (bdcdy != null) {
                                        sqxxModel.setBdcdyh(bdcdy.getBdcdyh());
                                    } else {
                                        throw new AppException("未找到不动产单元号！");
                                    }
                                }
                                //坐落
                                if (StringUtils.isBlank(sqxxModel.getZl())) {
                                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXmList.get(0).getProid());
                                    if (bdcSpxx != null) {
                                        sqxxModel.setZl(bdcSpxx.getZl());
                                    }
                                }
                            } else {
                                throw new AppException("未找到项目！");
                            }
                        }
                    }
                }
            }
        }
    }

    public Integer creatProjectNode(final String proid) {
        Space rootSpace = fileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF", true);
        com.gtis.fileCenter.model.Node sournode = fileCenterNodeServiceImpl.getNode(rootSpace.getId(), proid, true);

        return sournode.getId();
    }

    /**
     * 通过djModel获取不动产信息
     *
     * @param list
     * @param djModel
     * @return
     */
    public List getBdcxxFromDjModel(List list, DjModel djModel) {
        List saveList = new ArrayList();
        // 添加逻辑，当为互联网+报件时，在这里不保存任何权利人数据。
        String xtly = null;
        if(djModel != null && CollectionUtils.isNotEmpty(djModel.getSqxxModelList()) && djModel.getSqxxModelList().get(0) != null){
            xtly = djModel.getSqxxModelList().get(0).getXtly();
        }
        Boolean uPpCf = false;
        if(djModel != null && CollectionUtils.isNotEmpty(djModel.getSqxxModelList())){
            SqxxModel sqxxModel = djModel.getSqxxModelList().get(0);
            if(sqxxModel instanceof SqFyxxModel){
                if(StringUtils.isBlank(sqxxModel.getBdcdyh())){
                    uPpCf = true;
                }
            }
        }
        if(CollectionUtils.isNotEmpty(list)) {
            for(Object o : list) {
                if((o instanceof BdcQlr)) {
                    BdcQlr bdcQlr = (BdcQlr) o;
                    if((StringUtils.equals(bdcQlr.getQlrlx(),Constants.QLRLX_YWR)
                            && (StringUtils.isBlank(xtly) || !StringUtils.equals(xtly, Constants.XTLY_YHSL)))
                            || uPpCf){
                        saveList.add(o);
                    }
                } else {
                    saveList.add(o);
                }
            }
        }
        return saveList;
    }

    public HashMap getRelateBdcXmAndSqxxModel(List<BdcXm> bdcXmList, List<SqxxModel> sqxxModelList, String xtly) {
        HashMap hashMap = new HashMap();
        if (CollectionUtils.isNotEmpty(bdcXmList) && CollectionUtils.isNotEmpty(sqxxModelList)) {
            if (bdcXmList.size() == 2 && sqxxModelList.size() == 1) {
                if (StringUtils.equals(xtly, Constants.XTLY_YHSL)) {
                    //针对转移抵押流程获取银行信息情况
                    SqxxModel zySqxxModel = null;
                    SqxxModel dySqxxModel = sqxxModelList.get(0);
                    for (BdcXm bdcXm : bdcXmList) {
                        if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                            hashMap.put(bdcXm.getProid(), dySqxxModel);
                            dySqxxModel.setQllx(Constants.QLLX_DYAQ);

                        } else {
                            if (zySqxxModel == null) {
                                zySqxxModel = new SqxxModel();
                                zySqxxModel.setXzzrnx(dySqxxModel.getXzzrnx());
                            }
                            hashMap.put(bdcXm.getProid(), zySqxxModel);
                        }
                    }
                } else if (StringUtils.equals(xtly, Constants.XTLY_PLSL)) {
                    SqxxModel zySqxxModel = sqxxModelList.get(0);
                    SqxxModel dySqxxModel = sqxxModelList.get(0);
                    for (BdcXm bdcXm : bdcXmList) {
                        if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                            hashMap.put(bdcXm.getProid(), dySqxxModel);
                            dySqxxModel.setQllx(Constants.QLLX_DYAQ);
                        } else {
                            hashMap.put(bdcXm.getProid(), zySqxxModel);
                        }
                    }
                } else {
                    //针对转移抵押流程获取交易信息情况
                    SqxxModel zySqxxModel = sqxxModelList.get(0);
                    SqxxModel dySqxxModel = null;
                    for (BdcXm bdcXm : bdcXmList) {
                        if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                            if (dySqxxModel == null) {
                                dySqxxModel = new SqxxModel();
                            }
                            dySqxxModel.setXtly(xtly);
                            dySqxxModel.setQllx(Constants.QLLX_DYAQ);
                            hashMap.put(bdcXm.getProid(), dySqxxModel);
                        } else {
                            hashMap.put(bdcXm.getProid(), zySqxxModel);
                        }
                    }
                }
            } else {
                for (BdcXm bdcXm : bdcXmList) {
                    String sqlxdm = bdcXm.getSqlx();
                    String bdcdyid = bdcXm.getBdcdyid();
                    if (StringUtils.isNotBlank(bdcdyid)) {
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                        String bdcdyh = "";
                        if (bdcBdcdy != null) {
                            bdcdyh = bdcBdcdy.getBdcdyh();
                        }
                        for (SqxxModel sqxxModel : sqxxModelList) {
                            if ((StringUtils.equals(sqxxModel.getBdcdyh(), bdcdyh) || StringUtils.contains(bdcXm.getYbdcqzh(), sqxxModel.getYbdcqzh())) && StringUtils.equals(sqxxModel.getSqdjlx(), sqlxdm)) {
                                hashMap.put(bdcXm.getProid(), sqxxModel);
                                break;
                            }
                        }
                    } else {
                        for (SqxxModel sqxxModel : sqxxModelList) {
                            if ((StringUtils.equals(sqlxdm, Constants.SQLX_GDDYZX_DM)
                                    || StringUtils.equals(sqlxdm, Constants.SQLX_FWJF_DM)
                                    || StringUtils.equals(sqlxdm, Constants.SQLX_TDJF_DM)
                                    || StringUtils.equals(sqlxdm, Constants.SQLX_JF_SFCZ)) && StringUtils.equals(sqlxdm, bdcXm.getSqlx())) {
                                hashMap.put(bdcXm.getProid(), sqxxModel);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    public void saveQyQlr(DjModel djModel, List<BdcXm> bdcXmList, HashMap hashMap, String xtly) {
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            if (bdcXmList.size() == 1) {
                //单个流程
                BdcXm bdcXm = bdcXmList.get(0);
                SqxxModel sqxxModel = (SqxxModel) hashMap.get(bdcXm.getProid());
                if (sqxxModel != null) {
                    getQlrFromDjmodel(djModel, bdcXmList.get(0), sqxxModel);
                } else {
                    getQlrFromDjmodel(djModel, bdcXmList.get(0));
                }

                if (StringUtils.equals(xtly, Constants.XTLY_YHSL)&&
                        StringUtils.isNotBlank(bdcXm.getQllx())
                        && (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ) || StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_CFDJ))) {
                    //获取登记子项
                    setDjzxToBdcXm(sqxxModel, bdcXm);
                }

            } else {
                for (BdcXm bdcXm : bdcXmList) {
                    if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) {
                        //商品房流程
                        getQlrFromDjmodel(djModel, bdcXm);
                    } else {
                        //银行批量创建
                        if (hashMap.containsKey(bdcXm.getProid())) {
                            SqxxModel sqxxModel = (SqxxModel) hashMap.get(bdcXm.getProid());
                            if (sqxxModel != null) {
                                if(StringUtils.isNotBlank(xtly) && StringUtils.equals(xtly, Constants.XTLY_YHSL)){
                                    bdcQlrMapper.delQlrByProids(new String[]{bdcXm.getProid()});
                                    getQlrFromDjmodelForYhHb(djModel, bdcXm, sqxxModel);
                                }else {
                                    getQlrFromDjmodel(djModel, bdcXm, sqxxModel);
                                }

                            }

                            if (StringUtils.equals(xtly, Constants.XTLY_YHSL) && StringUtils.isNotBlank(bdcXm.getQllx()) && StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                                //获取登记子项
                                setDjzxToBdcXm(sqxxModel, bdcXm);
                            }
                        }
                    }
                }
            }

        }
    }


    /**
     * 通过登记vo保存权利信息
     *
     * @param bdcXm
     * @param djModel
     */
    public void saveQlxxFromDjModel(BdcXm bdcXm, DjModel djModel, HashMap hashMap, String xtly) {
        if (bdcXm != null && djModel != null) {
            String proid = bdcXm.getProid();
            if (hashMap.containsKey(proid)) {
                SqxxModel sqxxModel = (SqxxModel) hashMap.get(proid);
                QllxVo qllxModel = qllxService.makeSureQllx(bdcXm);
                QllxVo qllxVo = qllxService.queryQllxVo(qllxModel, proid);
                QllxVo saveQllxVo = null;
                saveQllxVo = getQllxVoFromSqxxModel(sqxxModel, qllxVo);
                if(saveQllxVo == null && (StringUtils.equals(sqxxModel.getSqdjlx(),Constants.SQLX_JF)
                        ||StringUtils.equals(sqxxModel.getSqdjlx(),Constants.SQLX_JF_SFCZ))){
                    List<BdcXm> bdcXmList = bdcXmService.getYbdcXmListByProid(proid);
                    if(CollectionUtils.isNotEmpty(bdcXmList)){
                        BdcXm yBdcXm = bdcXmList.get(0);
                        qllxModel = qllxService.makeSureQllx(yBdcXm);
                        if(qllxModel instanceof  BdcCf){
                            saveQllxVo = qllxService.queryQllxVo(qllxModel, yBdcXm.getProid());
                        }
                    }
                }
                if(StringUtils.equals(bdcXm.getQllx(),Constants.QLLX_CFDJ)){
                    if(saveQllxVo != null){
                        saveQllxVo = getCfQlInfoFromSqxxModel(proid,sqxxModel,saveQllxVo);
                    }else{
                        getGdCfQlInfoFromSqxxModel(proid,sqxxModel);
                    }
                }
                if (saveQllxVo != null) {
                    entityMapper.saveOrUpdate(saveQllxVo, saveQllxVo.getQlid());
                }
            }

        }
    }


    public QllxVo getCfQlInfoFromSqxxModel(String proid, SqxxModel sqxxModel,QllxVo qllxVo) {
        if(StringUtils.isNotBlank(proid) && sqxxModel instanceof SqFyxxModel){
            SqFyxxModel sqFyxxModel = (SqFyxxModel) sqxxModel;
            if((StringUtils.equals(sqxxModel.getSqdjlx(),Constants.SQLX_JF)
            || StringUtils.equals(sqxxModel.getSqdjlx(),Constants.SQLX_JF_SFCZ))){
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                    String yproid = null;
                    String yqlid = null;
                    if(StringUtils.isNotBlank(bdcXmRel.getYproid())){
                        yproid = bdcXmRel.getYproid();
                    }
                    if(StringUtils.isNotBlank(bdcXmRel.getYqlid())){
                        yqlid = bdcXmRel.getYqlid();
                    }
                    BdcCf yBdcCf = null;
                    if(StringUtils.isNotBlank(yproid)){
                        yBdcCf = bdcCfService.selectCfByProid(yproid);
                    }
                    if(yBdcCf != null){
                        yBdcCf.setJfjg(sqFyxxModel.getJfjg());
                        yBdcCf.setJfsj(sqFyxxModel.getJfsj());
                        yBdcCf.setJfwh(sqFyxxModel.getJfwh());
                        yBdcCf.setJfwj(sqFyxxModel.getJfwj());
                        entityMapper.saveOrUpdate(yBdcCf,yBdcCf.getQlid());
                    }else{
                        if(StringUtils.isNotBlank(yqlid)){
                            GdCf gdCf = gdCfService.getGdCfByCfid(yqlid);
                            gdCf.setJfjg(sqFyxxModel.getJfjg());
                            gdCf.setJfrq(sqFyxxModel.getJfsj());
                            gdCf.setJfwh(sqFyxxModel.getJfwh());
                            gdCf.setJfwj(sqFyxxModel.getJfwj());
                            entityMapper.saveOrUpdate(gdCf,gdCf.getCfid());
                        }
                    }
                    if(qllxVo instanceof BdcCf){
                        BdcCf bdcCf = (BdcCf)qllxVo;
                        bdcCf.setJfjg(sqFyxxModel.getJfjg());
                        bdcCf.setJfsj(sqFyxxModel.getJfsj());
                        bdcCf.setJfwh(sqFyxxModel.getJfwh());
                        bdcCf.setJfwj(sqFyxxModel.getJfwj());
                    }
                }
            }else{
                BdcCf bdcCf = (BdcCf)qllxVo;
                bdcCf.setCffw(sqFyxxModel.getCffw());
                bdcCf.setCfjg(sqFyxxModel.getCfjg());
                bdcCf.setCfjsqx(sqFyxxModel.getCfjsrq());
                bdcCf.setCfksqx(sqFyxxModel.getCfksrq());
                bdcCf.setCfwh(sqFyxxModel.getCfwh());
                bdcCf.setCfwj(sqFyxxModel.getCfwj());
                qllxVo = bdcCf;
            }
        }
        return qllxVo;
    }

    /**
     * 权利信息从申请信息读取
     *
     * @param sqxxModel
     * @param qllxVo
     * @return
     */

    public QllxVo getQllxVoFromSqxxModel(SqxxModel sqxxModel, QllxVo qllxVo) {
        if (StringUtils.equals(sqxxModel.getQllx(), Constants.QLLX_DYAQ) && qllxVo instanceof BdcDyaq) {
            BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
            bdcDyaq.setBdbzzqse(sqxxModel.getBdbzzqse());
            bdcDyaq.setZjgcdyfw(sqxxModel.getDyfw());
            bdcDyaq.setDkfs(sqxxModel.getDkfs());
            if (StringUtils.isNotBlank(sqxxModel.getDysw())) {
                bdcDyaq.setDysw(CommonUtil.formatObjectToInteger(sqxxModel.getDysw()));
            }
            if (bdcDyaq.getDysw() == null) {
                bdcDyaq.setDysw(1);
            }
            bdcDyaq.setDyfs(sqxxModel.getDyfs());
            bdcDyaq.setZwlxksqx(sqxxModel.getZwlxqxksrq());
            bdcDyaq.setZwlxjsqx(sqxxModel.getZwlxqxjsrq());
            bdcDyaq.setZwr(sqxxModel.getZwr());
            bdcDyaq.setZgzqqdse(sqxxModel.getZgzqqdse());
            bdcDyaq.setZgzqqdss(sqxxModel.getZgzqqdss());
            bdcDyaq.setDbfw(sqxxModel.getDbfw());
            bdcDyaq.setFwpgjg(sqxxModel.getFwpgjg());
            bdcDyaq.setFwdyjg(sqxxModel.getFwdyjg());
            bdcDyaq.setFwdymj(sqxxModel.getFwdymj());
            bdcDyaq.setTdpgjg(sqxxModel.getTdpgjg());
            bdcDyaq.setTddyjg(sqxxModel.getTddyjg());
            bdcDyaq.setTddymj(sqxxModel.getTddymj());
            bdcDyaq.setFj(sqxxModel.getFj());
            if (StringUtils.isNotBlank(sqxxModel.getDysw())) {
                Integer dysw = null;
                try {
                    dysw = Integer.parseInt(sqxxModel.getDysw());
                } catch (NumberFormatException e) {
                    logger.error("ProjectManageServiceImpl.getQllxVoFromSqxxModel",e);
                }
                if (dysw != null) {
                    bdcDyaq.setDysw(dysw);
                }
            }
            qllxVo = bdcDyaq;
        } else if (StringUtils.equals(sqxxModel.getQllx(), Constants.QLLX_YGDJ) && qllxVo instanceof BdcYg) {
            BdcYg bdcYg = (BdcYg) qllxVo;
            bdcYg.setQdjg(sqxxModel.getBdbzzqse());
            bdcYg.setDyfs(sqxxModel.getDyfs());
            bdcYg.setZwlxksqx(sqxxModel.getZwlxqxksrq());
            bdcYg.setZwlxjsqx(sqxxModel.getZwlxqxjsrq());
            qllxVo = bdcYg;
        } else if (qllxVo instanceof BdcFdcq) {
            BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
            if(sqxxModel.getJyjg() != null){
                bdcFdcq.setJyjg(sqxxModel.getJyjg());
            }
            if(StringUtils.isNotBlank(sqxxModel.getFwxz())){
                bdcFdcq.setFwxz(sqxxModel.getFwxz());
            }
            if(StringUtils.isNotBlank(sqxxModel.getFwjg())){
                bdcFdcq.setFwjg(sqxxModel.getFwjg());
            }
            if(StringUtils.isNotBlank(sqxxModel.getZcs())) {
                bdcFdcq.setMyzcs(sqxxModel.getZcs());
            }
            if(sqxxModel.getTdsyksqx() != null) {
                bdcFdcq.setTdsyksqx(sqxxModel.getTdsyksqx());
            }
            if(sqxxModel.getTdsyksqx() != null) {
                bdcFdcq.setTdsyjsqx(sqxxModel.getTdsyksqx());
            }
            if(StringUtils.isNotBlank(sqxxModel.getCqly())){
                bdcFdcq.setCqly(sqxxModel.getCqly());
            }
            if (sqxxModel.getXzzrnx() != null) {
                bdcFdcq.setXzzrnx(sqxxModel.getXzzrnx());
            }
            qllxVo = bdcFdcq;
        }else if (qllxVo instanceof BdcFdcqDz) {
            BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
            bdcFdcqDz.setFdcjyjg(sqxxModel.getJyjg());
            bdcFdcqDz.setFwxz(sqxxModel.getFwxz());
            bdcFdcqDz.setTdsyksqx(sqxxModel.getTdsyksqx());
            bdcFdcqDz.setTdsyjsqx(sqxxModel.getTdsyjsqx());
            bdcFdcqDz.setCqly(sqxxModel.getCqly());
            if (sqxxModel.getXzzrnx() != null) {
                bdcFdcqDz.setXzzrnx(sqxxModel.getXzzrnx());
            }
            qllxVo = bdcFdcqDz;
        }
        return qllxVo;
    }

    /**
     * @param djModel
     * @param xm
     * @return Msg
     * @author <a href="mailto:liuxing@gtmap.cn">liuxing</a>
     * @description 根据流程名称获取流程定义ID
     */
    public void getQlrFromDjmodel(DjModel djModel, BdcXm xm) {
        if (null != djModel && null != xm) {
            List<QlrModel> qlrModels = djModel.getQlrLisr();
            if (CollectionUtils.isNotEmpty(qlrModels)) {
                for (QlrModel qlrModel : qlrModels) {
                    saveQlr(qlrModel, xm, null);
                }
            }
        }
    }

    public void getQlrFromDjmodelForYhHb(DjModel djModel, BdcXm xm, SqxxModel sqxxModel) {
        if (null != djModel && null != xm) {
            List<QlrModel> qlrModels = djModel.getQlrLisr();
            if (CollectionUtils.isNotEmpty(qlrModels)) {
                for (QlrModel qlrModel : qlrModels) {
                    if(StringUtils.isNotBlank(qlrModel.getQllx())){
                        if(StringUtils.equals(qlrModel.getQllx(), xm.getQllx())){
                            saveQlr(qlrModel, xm, sqxxModel);
                        }
                    }else {
                        saveQlr(qlrModel, xm, sqxxModel);
                    }
                }
            }
        }
    }

    public void getQlrFromDjmodel(DjModel djModel, BdcXm xm, SqxxModel sqxxModel) {
        if (null != djModel && null != xm) {
            List<QlrModel> qlrModels = djModel.getQlrLisr();
            if (CollectionUtils.isNotEmpty(qlrModels)) {
                for (QlrModel qlrModel : qlrModels) {
                    if (StringUtils.equals(qlrModel.getSqid(), sqxxModel.getSqid())) {
                        saveQlr(qlrModel, xm, sqxxModel);
                    }
                }
            }
        }
    }

    public void saveQlr(QlrModel qlrModel, BdcXm xm, SqxxModel sqxxModel) {
        String sfzjzl = qlrModel.getQlrsfzjzl();
        if (StringUtils.isNotBlank(sfzjzl) && !NumberUtils.isNumber(sfzjzl)) {
            sfzjzl = bdcQlrMapper.getZjzlByMc(qlrModel.getQlrsfzjzl());
        }
        BdcQlr bdcQlr = new BdcQlr();
        bdcQlr.setQlrid(UUIDGenerator.generate());
        bdcQlr.setProid(xm.getProid());
        bdcQlr.setQlrmc(qlrModel.getQlrmc());
        bdcQlr.setQlrlx(qlrModel.getQlrlx());
        bdcQlr.setXb(qlrModel.getXb());
        bdcQlr.setQlbl(qlrModel.getQlbl());
        bdcQlr.setQlrsfzjzl(sfzjzl);
        bdcQlr.setQlrzjh(qlrModel.getQlrzjh());
        bdcQlr.setQlrtxdz(qlrModel.getQlrtxdz());
        bdcQlr.setQlrlxdh(qlrModel.getQlrlxdh());
        bdcQlr.setQlrdlr(qlrModel.getDlrmc());
        bdcQlr.setQlrdlrdh(qlrModel.getDlrdh());
        bdcQlr.setQlrfddbr(qlrModel.getFddbrhfzr());
        bdcQlr.setQlrfddbrdh(qlrModel.getFddbrhfzrdh());
        bdcQlr.setXb(qlrModel.getXb());
        if (StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_YHSL)) {
            bdcQlr.setSfzxxly(Constants.SFZXXLY_HLW);
        } else if (StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_JYSL)) {
            bdcQlr.setSfzxxly(Constants.SFZXXLY_JY);
        } else if (StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_JCPTSL)) {
            bdcQlr.setSfzxxly(Constants.SFZXXLY_JCPT);
        } else {
            bdcQlr.setSfzxxly(Constants.SFZXXLY_LR);
        }
        String sqlxdm = "";
        if (StringUtils.isNotBlank(xm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(xm.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        if (StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_PLSL) || StringUtils.equals(sqxxModel.getXtly(), Constants.XTLY_JYSL)) {
            if (StringUtils.equals(sqxxModel.getSqdjlx(), Constants.SQLX_CLF) || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD)) {
                if (StringUtils.isNotBlank(qlrModel.getQllx())) {
                    if (StringUtils.equals(xm.getQllx(), Constants.QLLX_DYAQ) &&
                            StringUtils.equals(qlrModel.getQllx(), Constants.QLLX_DYAQ)) {
                        entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                    }
                } else {
                    if (!StringUtils.equals(xm.getQllx(), Constants.QLLX_DYAQ) && !StringUtils.equals(qlrModel.getQllx(), Constants.QLLX_DYAQ)) {
                        if (StringUtils.isNotBlank(sqxxModel.getGyfs())) {
                            bdcQlr.setGyfs(sqxxModel.getGyfs());
                        } else {
                            bdcQlr.setGyfs("4");
                        }
                        entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                    }
                }
            } else {
                if (StringUtils.isNotBlank(sqxxModel.getGyfs())) {
                    bdcQlr.setGyfs(sqxxModel.getGyfs());
                } else {
                    bdcQlr.setGyfs("4");
                }
                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
            }

        } else {
            if (StringUtils.isNotBlank(sqxxModel.getGyfs())) {
                bdcQlr.setGyfs(sqxxModel.getGyfs());
            } else {
                bdcQlr.setGyfs("4");
            }
            entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
        }
    }

    private void initBdcXmSpxx(BdcXm bdcXm, SqxxModel sqxxModel){
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())
                && sqxxModel != null && StringUtils.isNotBlank(sqxxModel.getZl())){
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
            bdcSpxx.setZl(sqxxModel.getZl());
            entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
        }
    }

    /**
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @param
     * @return
     * @Description: 保存bdcFdcq的共有方式
     */
    private void modifyBdcFdcqGyqk(String proid){
        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
        if(CollectionUtils.isNotEmpty(bdcFdcqList)){
            BdcFdcq bdcFdcq = bdcFdcqList.get(0);
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            if(CollectionUtils.isNotEmpty(bdcQlrList)){
                if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                    bdcFdcq.setGyqk(Constants.GYQK_QTGY);
                }else {
                    bdcFdcq.setGyqk(bdcQlrList.get(0).getGyfs());
                }
                bdcFdcqService.saveBdcFdcq(bdcFdcq);
            }
        }
    }

    @Override
    public Map<String, Object> gxWwCreateProject(List<WwDjModel> var1, String var2) {
        return null;
    }

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 将集成平台传过来的交易编号和合同编号保存到登记对应表中
     */
    public void initBdcJyxxAndJyht(BdcXm bdcXm, SqxxModel sqxxModel){
        if(sqxxModel != null && bdcXm != null){
            BdcJyht bdcJyhtTemp = new BdcJyht();
            String jyhtid = UUID.hex32();
            Date drsj = CalendarUtil.getCurHMSDate();
            bdcJyhtTemp.setJyhtid(jyhtid);
            bdcJyhtTemp.setHtbh(sqxxModel.getFdcjyhth());
            bdcJyhtTemp.setJybh(sqxxModel.getJybh());
            bdcJyhtTemp.setDrsj(drsj);
            entityMapper.saveOrUpdate(bdcJyhtTemp,bdcJyhtTemp.getJyhtid());
            BdcJyxx bdcJyxxTemp = new BdcJyxx();
            String jyxxid = UUID.hex32();
            bdcJyxxTemp.setJyxxid(jyxxid);
            bdcJyxxTemp.setHtbh(sqxxModel.getFdcjyhth());
            bdcJyxxTemp.setJybh(sqxxModel.getJybh());
            bdcJyxxTemp.setProid(bdcXm.getProid());
            bdcJyxxTemp.setDrsj(drsj);
            entityMapper.saveOrUpdate(bdcJyxxTemp,bdcJyxxTemp.getJyxxid());
        }
    }

    public void getGdCfQlInfoFromSqxxModel(String proid, SqxxModel sqxxModel){
        if(StringUtils.isNotBlank(proid) && sqxxModel instanceof SqFyxxModel){
            SqFyxxModel sqFyxxModel = (SqFyxxModel) sqxxModel;
            if(StringUtils.equals(sqxxModel.getSqdjlx(),Constants.SQLX_FWJF_DM)
                    || StringUtils.equals(sqxxModel.getSqdjlx(),Constants.SQLX_TDJF_DM)
            || (StringUtils.equals(sqxxModel.getSqdjlx(),Constants.SQLX_JF_SFCZ)
                    && StringUtils.isBlank(sqFyxxModel.getBdcdyh()))){
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                    String yqlid = null;
                    if(StringUtils.isNotBlank(bdcXmRel.getYqlid())){
                        yqlid = bdcXmRel.getYqlid();
                    }
                    if(StringUtils.isNotBlank(yqlid)){
                        GdCf gdCf = gdCfService.getGdCfByCfid(yqlid);
                        gdCf.setJfjg(sqFyxxModel.getJfjg());
                        gdCf.setJfrq(sqFyxxModel.getJfsj());
                        gdCf.setJfwh(sqFyxxModel.getJfwh());
                        gdCf.setJfwj(sqFyxxModel.getJfwj());
                        entityMapper.saveOrUpdate(gdCf,gdCf.getCfid());
                    }
                }
            }else{
                List<GdCf> gdCfList = gdFwService.getGdcfByGdproid(proid, null);
                if(CollectionUtils.isNotEmpty(gdCfList)){
                    GdCf gdcf = gdCfList.get(0);
                    gdcf.setCffw(sqFyxxModel.getCffw());
                    gdcf.setCfjg(sqFyxxModel.getCfjg());
                    gdcf.setCfjsrq(sqFyxxModel.getCfjsrq());
                    gdcf.setCfksrq(sqFyxxModel.getCfksrq());
                    gdcf.setCfwh(sqFyxxModel.getCfwh());
                    gdcf.setCfwj(sqFyxxModel.getCfwj());
                    entityMapper.saveOrUpdate(gdcf,gdcf.getCfid());
                }
            }
        }
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过交易编号获取限制转让年限保存到权利表
     */
    private void updateXzzrnxByJybh(String jybh, String proid) {
        if (StringUtils.isNotBlank(jybh) && StringUtils.isNotBlank(proid)) {
            CloseableHttpClient httpclient = null;// 定义httpclient
            CloseableHttpResponse httpResponse = null;// 定义返回结果
            try {
                String url = etlUrl + "/JiaoYiXx/updateXzzrnxByJybh?jybh=" + jybh + "&proid=" + proid;
                httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(url);
                httpclient.execute(httpGet);
            } catch (Exception e) {
                logger.error("ProjectManageServiceImpl.updateXzzrnxByJybh", e);
            } finally {
                if (httpResponse != null) {
                    try {
                        httpResponse.close();
                    } catch (IOException e) {
                        logger.error("ProjectManageServiceImpl.updateXzzrnxByJybh", e);
                    }
                }
                if (httpclient != null) {
                    try {
                        httpclient.close();
                    } catch (IOException e) {
                        logger.error("ProjectManageServiceImpl.updateXzzrnxByJybh", e);
                    }
                }
            }
        }
    }
}