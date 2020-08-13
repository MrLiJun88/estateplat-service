package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfUserVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.web.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description
 */
@Service
public class ProjectParServiceImpl implements ProjectParService {
    private static String LCLX = "lclx";
    // 流程类型--单一
    private static String LCLX_DY = "lclx_dy";
    // 流程类型--批量
    private static String LCLX_PL = "lclx_pl";
    // 流程类型--组合
    private static String LCLX_ZH = "lclx_zh";
    @Resource(name = "dozerQlMapper")
    private DozerBeanMapper dozerMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXtYwmxService bdcXtYwmxService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcPpgxService bdcPpgxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private DjsjFwMapper djsjFwMapper;


    @Override
    public List<ProjectPar> initProjectPar(Project project) throws InvocationTargetException, IllegalAccessException {
        if (project == null) {
            throw new NullPointerException("项目数据为空，请检查");
        }
        ProjectPar projectPar = readProjectParFormXm(project);
        //督查随机数值
        projectPar.setDcsjzs((int) (Math.random() * 100) + 1);
        List<BdcXtYwmx> bdcXtYwmxList = bdcXtYwmxService.getBdcXtYwmxBySqlx(projectPar.getSqlx());
        if (CollectionUtils.isNotEmpty(bdcXtYwmxList)) {
            projectPar.setBdcXtYwmx(bdcXtYwmxList.get(0));
        }
        List<ProjectPar> projectParList = getProjectParByPllc(projectPar, project);
        String lclx = "";
        if (CollectionUtils.isNotEmpty(projectParList) && projectParList.size() > 1) {
            lclx = LCLX_PL;
        } else {
            HashMap<String, String> lclxMap = getlclx(projectPar);
            if (lclxMap.containsKey(LCLX)) {
                lclx = lclxMap.get(LCLX);
            }
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 单一流程
             */
            if (StringUtils.equals(lclx, LCLX_DY) && !CommonUtil.indexOfStrs(Constants.BATCH_OPERATION_SQLX_DM, projectPar.getSqlx()) && CollectionUtils.isEmpty(projectParList)) {
                projectPar.setSx(1);
                projectParList.add(projectPar);
            }
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 组合流程
             */
            if (StringUtils.equals(lclx, LCLX_ZH)) {
                projectParList = getProjectParByZhlc(projectPar, lclxMap, bdcXtYwmxList);
            }
        }
        getProjectPar(projectParList);
        return projectParList;
    }


    /**
     * @param projectPar 流程参数类
     * @param blmc       变量名称
     * @return projectPar 流程参数类
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化权籍参数
     */
    @Override
    public ProjectPar getQjsj(ProjectPar projectPar, String blmc) {
        if (StringUtils.isBlank(projectPar.getQjid()) && StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            projectPar.setQjid(bdcDjsjService.getDjidByBdcdyh(projectPar.getBdcdyh(), projectPar.getBdclx()));
        }
        switch (blmc) {
            case "djsjFwQlrList":
                //权籍房屋权利人集合
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    //先从projectPar获取是否已存在djsjFwxx
                    if (projectPar.getDjsjFwxx() != null) {
                        List<DjsjFwxx> djsjFwQlrList = bdcDjsjService.getDjsjFwQlr(projectPar.getDjsjFwxx().getId());
                        if (CollectionUtils.isNotEmpty(djsjFwQlrList)) {
                            projectPar.setDjsjFwQlrList(djsjFwQlrList);
                        }
                    } else {
                        DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(projectPar.getQjid());
                        if (djsjFwxx != null) {
                            projectPar.setDjsjFwxx(djsjFwxx);
                            List<DjsjFwxx> djsjFwQlrList = bdcDjsjService.getDjsjFwQlr(djsjFwxx.getId());
                            if (CollectionUtils.isNotEmpty(djsjFwQlrList)) {
                                projectPar.setDjsjFwQlrList(djsjFwQlrList);
                            }
                        }
                    }
                }
                break;

            case "djsjZdxxList":
                //权籍宗地信息集合
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxx(projectPar.getQjid());
                    //取农用地调查表信息
                    if (CollectionUtils.isEmpty(djsjZdxxList)) {
                        djsjZdxxList = bdcDjsjService.getDjsjNydxx(projectPar.getQjid());
                    }
                    if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                        projectPar.setDjsjZdxxList(djsjZdxxList);
                    }
                }
                break;

            case "djsjCbzdDcb":
                //权籍承包宗地信息
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    DjsjCbzdDcb djsjCbzdDcb = bdcDjsjService.getDjsjCbzdDcbByDjid(projectPar.getQjid());
                    if (djsjCbzdDcb != null) {
                        projectPar.setDjsjCbzdDcb(djsjCbzdDcb);
                    }
                }
                break;

            case "djsjQszdDcbList":
                //权籍权属宗地信息集合
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    List<DjsjQszdDcb> djsjQszdDcbList = bdcDjsjService.getDjsjQszdDcb(projectPar.getQjid());
                    if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
                        projectPar.setDjsjQszdDcbList(djsjQszdDcbList);
                    }
                }
                break;

            case "djsjFwxx":
                //权籍房屋信息
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(projectPar.getQjid());
                    if (djsjFwxx != null) {
                        projectPar.setDjsjFwxx(djsjFwxx);
                        //添加相应土地信息
                        if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && StringUtils.length(djsjFwxx.getBdcdyh()) > 19) {
                            List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
                            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                                projectPar.setDjsjZdxxList(djsjZdxxList);
                            }
                        }
                    }
                }
                break;

            case "djsjNydDcbList":
                //权籍农用地调查表信息集合
                if (projectPar.getDjsjCbzdDcb() == null && StringUtils.isNotBlank(projectPar.getQjid())) {
                    DjsjCbzdDcb djsjCbzdDcb = bdcDjsjService.getDjsjCbzdDcbByDjid(projectPar.getQjid());
                    if (djsjCbzdDcb != null) {
                        projectPar.setDjsjCbzdDcb(djsjCbzdDcb);
                    }
                }
                if (projectPar.getDjsjCbzdDcb() != null && StringUtils.isNotBlank(projectPar.getDjsjCbzdDcb().getDjh())) {
                    List<DjsjNydDcb> djsjNydDcbList = bdcDjsjService.getDjsjNydDcbByDjh(projectPar.getDjsjCbzdDcb().getDjh());
                    if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                        projectPar.setDjsjNydDcbList(djsjNydDcbList);
                    }
                }
                break;

            case "djsjQszdDcb":
                //权籍权属宗地信息
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    List<DjsjQszdDcb> djsjQszdDcbList = bdcDjsjService.getDjsjQszdDcb(projectPar.getQjid());
                    if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
                        projectPar.setDjsjQszdDcb(djsjQszdDcbList.get(0));
                    }
                }
                break;

            case "djsjZdxx":
                //权籍宗地信息
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxx(projectPar.getQjid());
                    if (CollectionUtils.isEmpty(djsjZdxxList)) {
                        djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(projectPar.getDjh());
                    }
                    //取农用地调查表信息
                    if (CollectionUtils.isEmpty(djsjZdxxList)) {
                        djsjZdxxList = bdcDjsjService.getDjsjNydxx(projectPar.getQjid());
                    }
                    if (CollectionUtils.isEmpty(djsjZdxxList)
                            && StringUtils.isNotBlank(projectPar.getBdcdyh())
                            && projectPar.getBdcdyh().length() > 19) {
                        djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(projectPar.getBdcdyh().substring(0, 19));
                    }
                    if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                        projectPar.setDjsjZdxx(djsjZdxxList.get(0));
                    }
                }
                break;

            case "djsjLqxx":
                //权籍林权信息
                if (StringUtils.isNotBlank(projectPar.getQjid()) && Constants.BDCLX_TDSL.equals(projectPar.getBdclx())) {
                    DjsjLqxx djsjLqxx = bdcDjsjService.getDjsjLqxx(projectPar.getQjid());
                    if (djsjLqxx != null) {
                        projectPar.setDjsjLqxx(djsjLqxx);
                    }
                }
                break;

            case "djsjZhxx":
                //权籍宗海信息
                if (Constants.BDCLX_HY.equals(projectPar.getBdclx())) {
                    DjsjZhxx djsjZhxx = null;
                    List<DjsjZhxx> djsjZhxxList = null;
                    if (StringUtils.isNotBlank(projectPar.getBdcdyh()) && StringUtils.length(projectPar.getBdcdyh()) > 19) {
                        djsjZhxxList = bdcDjsjService.getDjsjZhxxForDjh(StringUtils.substring(projectPar.getBdcdyh(), 0, 19));
                    }
                    if (CollectionUtils.isNotEmpty(djsjZhxxList)) {
                        djsjZhxx = djsjZhxxList.get(0);
                    }
                    if (djsjZhxx == null && StringUtils.isNotBlank(projectPar.getQjid())) {
                        djsjZhxx = bdcDjsjService.getDjsjZhxx(projectPar.getQjid());
                    }
                    if (djsjZhxx != null) {
                        projectPar.setDjsjZhxx(djsjZhxx);
                    }
                }
                break;

            case "djsjQszdZdmj":
                //权籍权属宗地宗地面积
                if (StringUtils.isNotBlank(projectPar.getBdcdyh()) && StringUtils.length(projectPar.getBdcdyh()) > 19) {
                    BdcQszdZdmj bdcQszdZdmj = bdcDjsjService.getBdcQszdZdmj(StringUtils.substring(projectPar.getBdcdyh(), 0, 19));
                    if (bdcQszdZdmj != null) {
                        projectPar.setBdcQszdZdmj(bdcQszdZdmj);
                    }
                }
                break;

        }
        return projectPar;
    }

    private ProjectPar readProjectParFormXm(Project project) {
        ProjectPar projectPar = new ProjectPar();
        projectPar.setSfdyYzh(true);
        dozerMapper.map(project, projectPar);
        projectPar.setYbdcqzh("");
        if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            projectPar.setDjh(StringUtils.substring(projectPar.getBdcdyh(), 0, 19));
        }
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
        if (bdcXm == null || StringUtils.isBlank(bdcXm.getWiid())) {
            throw new NullPointerException("项目数据为空，请检查");
        }
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
        if (pfWorkFlowInstanceVo == null) {
            throw new NullPointerException("项目数据为空，请检查");
        }
        if (StringUtils.isNotBlank(project.getUserId())) {
            PfUserVo pfUserVo = sysUserService.getUserVo(project.getUserId());
            projectPar.setCjr(pfUserVo.getUserName());
        }
        if (StringUtils.isBlank(projectPar.getCjr())) {
            if (StringUtils.isNotBlank(bdcXm.getCjr())) {
                projectPar.setCjr(bdcXm.getCjr());
            } else {
                if (SessionUtil.getCurrentUser() != null) {
                    projectPar.setCjr(SessionUtil.getCurrentUser().getUsername());
                }
            }
        }
        if (bdcXm.getCjsj() != null) {
            projectPar.setCjsj(bdcXm.getCjsj());
        } else {
            projectPar.setCjsj(new Date());
        }
        projectPar.setBh(bdcXm.getBh());
        projectPar.setDwdm(bdcXm.getDwdm());
        projectPar.setWiid(pfWorkFlowInstanceVo.getWorkflowIntanceId());
        projectPar.setWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
        String sqlx = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
        projectPar.setSqlx(sqlx);
        projectPar.setDsqlx(sqlx);
        projectPar.setDjlx(bdcZdGlService.getDjlxBySqlx(sqlx));
        projectPar.setQllx(bdcZdGlService.getQllxBySqlx(sqlx));
        projectPar.setXmly(Constants.XMLY_BDC);
        if (StringUtils.equals(project.getXtly(), Constants.XTLY_JCPTSL)) {
            projectPar.setXtly(Constants.XTLY_JCPTSL);
        }
        return projectPar;
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程类型
     */
    private HashMap getlclx(ProjectPar projectPar) {
        HashMap<String, String> map = Maps.newHashMap();
        if (StringUtils.isEmpty(projectPar.getSqlx())) {
            throw new NullPointerException("申请类型为空请检查");
        }
        HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(projectPar.getSqlx());
        String sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
        String sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
        if (StringUtils.isNotBlank(sqlx1) && StringUtils.isNotBlank(sqlx2)) {
            map.put(LCLX, LCLX_ZH);
            map.put("sqlx1", sqlx1);
            map.put("sqlx2", sqlx2);
        } else {
            map.put(LCLX, LCLX_DY);
        }
        return map;
    }


    private List<ProjectPar> getProjectParByZhlc(ProjectPar projectPar, HashMap<String, String> lclxMap, List<BdcXtYwmx> bdcXtYwmxList) throws InvocationTargetException, IllegalAccessException {
        List<ProjectPar> projectParList = Lists.newArrayList();
        if (lclxMap.containsKey("sqlx1") && lclxMap.containsKey("sqlx2")) {
            String sqlx1 = lclxMap.get("sqlx1");
            String sqlx2 = lclxMap.get("sqlx2");
            String zhSqlx = projectPar.getSqlx();
            projectPar.setSqlx(sqlx1);
            projectPar.setQllx(bdcZdGlService.getQllxBySqlx(sqlx1));
            ProjectPar projectPar2 = new ProjectPar();
            BeanUtils.copyProperties(projectPar2, projectPar);
            projectPar2.setSqlx(sqlx2);
            projectPar2.setQllx(bdcZdGlService.getQllxBySqlx(sqlx2));
            projectPar2.setProid(UUIDGenerator.generate18());
            projectPar2.setYxmid(projectPar.getProid());
            projectPar.setSx(1);
            projectPar2.setSx(2);
            if (StringUtils.equals(zhSqlx, Constants.SQLX_YG_DY)) {
                projectPar.setDjlx(bdcZdGlService.getDjlxDmBySqlxdm(projectPar.getSqlx()));
                projectPar2.setDjlx(bdcZdGlService.getDjlxDmBySqlxdm(projectPar2.getSqlx()));
            }
            if (CollectionUtils.isNotEmpty(bdcXtYwmxList) && bdcXtYwmxList.size() > 1) {
                projectPar2.setBdcXtYwmx(bdcXtYwmxList.get(1));
            }
            projectParList.add(projectPar);
            projectParList.add(projectPar2);
        }
        return projectParList;
    }

    private List<ProjectPar> getProjectParByPllc(ProjectPar projectPar, Project project) throws InvocationTargetException, IllegalAccessException {
        List<ProjectPar> projectParList = Lists.newArrayList();
        List<DjsjFwHs> djsjFwHsList = Lists.newArrayList();
        List<DjsjZdxx> djsjZdxxList = Lists.newArrayList();
        List<DjsjFwXmxx> djsjFwXmxxList = Lists.newArrayList();
        List<Map> ljzList = Lists.newArrayList();
        List<String> djidList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("fw_dcb_indexs", project.getDcbIndexs());
            List<DjsjFwHs> djsjFwHss = djsjFwService.getDjsjFwHs(map);
            if (CollectionUtils.isEmpty(djsjFwHss)) {
                djsjFwHss = djsjFwService.getDjsjFwYcHs(map);
            }
            if (CollectionUtils.isNotEmpty(djsjFwHss)) {
                djsjFwHsList.addAll(djsjFwHss);
            }
            if (CollectionUtils.isEmpty(djsjFwHss)) {
                for (String dcbIndex : project.getDcbIndexs()) {
                    Map ljzMap = getLjz(dcbIndex);
                    if (MapUtils.isNotEmpty(ljzMap)) {
                        ljzList.add(ljzMap);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(project.getDcbIndex())) {
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("fw_dcb_index", project.getDcbIndex());
            List<DjsjFwHs> djsjFwHss = djsjFwService.getDjsjFwHs(map);
            if (CollectionUtils.isEmpty(djsjFwHss)) {
                djsjFwHss = djsjFwService.getDjsjFwYcHs(map);
            }
            if (CollectionUtils.isNotEmpty(djsjFwHss)) {
                djsjFwHsList.addAll(djsjFwHss);
            }
            if (CollectionUtils.isEmpty(djsjFwHss)) {
                Map ljzMap = getLjz(project.getDcbIndex());
                if (MapUtils.isNotEmpty(ljzMap)) {
                    ljzList.add(ljzMap);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(project.getDjIds())) {
            List<String> djids = new ArrayList<>();
            for (String djid : project.getDjIds()) {
                if (StringUtils.contains(djid, Constants.SPLIT_STR)) {
                    for (String id : StringUtils.split(djid, Constants.SPLIT_STR)) {
                        if (!djids.contains(id)) {
                            djids.add(id);
                        }
                    }
                } else {
                    if (!djids.contains(djid)) {
                        djids.add(djid);
                    }
                }
            }
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("fw_hs_indexs", djids);
            List<DjsjFwHs> djsjFwHss = djsjFwService.getDjsjFwHs(map);
            if (CollectionUtils.isEmpty(djsjFwHss)) {
                djsjFwHss = djsjFwService.getDjsjFwYcHs(map);
            }
            if (CollectionUtils.isNotEmpty(djsjFwHss)) {
                djsjFwHsList.addAll(djsjFwHss);
            }
        }
        // 土地
        if (CollectionUtils.isNotEmpty(project.getDjIds())) {
            for (String djid : project.getDjIds()) {
                List<DjsjZdxx> djsjZdxxListTemp = bdcDjsjService.getDjsjZdxx(djid);
                if (CollectionUtils.isNotEmpty(djsjZdxxListTemp)) {
                    djsjZdxxList.addAll(djsjZdxxListTemp);
                }
            }
        }
        // 多幢
        if (CollectionUtils.isNotEmpty(project.getDjIds())) {
            for (String djid : project.getDjIds()) {
                HashMap map = new HashMap();
                map.put("fw_xmxx_index", djid);
                List<DjsjFwXmxx> djsjFwXmxxListTemp = djsjFwMapper.getDjsjFwXmxx(map);
                djsjFwXmxxList.addAll(djsjFwXmxxListTemp);
            }
        }

        List<DjsjFwHs> djsjFwHsQcList = Lists.newArrayList();
        List<Map> ljzQcList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
            Map<String, DjsjFwHs> djsjFwHsMap = Maps.newHashMap();
            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                djsjFwHsMap.put(djsjFwHs.getFwHsIndex(), djsjFwHs);
            }
            for (String key : djsjFwHsMap.keySet()) {
                djsjFwHsQcList.add(djsjFwHsMap.get(key));
            }
        }
        if (CollectionUtils.isNotEmpty(ljzList)) {
            Map<String, Map> ljzListMap = Maps.newHashMap();
            for (Map ljzMap : ljzList) {
                ljzListMap.put(ljzMap.get("fw_xmxx_index").toString(), ljzMap);
            }
            for (String key : ljzListMap.keySet()) {
                ljzQcList.add(ljzListMap.get(key));
            }
        }
        int sx = 1;
        if (CollectionUtils.isNotEmpty(djsjFwHsQcList)) {
            for (DjsjFwHs djsjFwHs : djsjFwHsQcList) {
                djidList.add(djsjFwHs.getFwHsIndex());
                ProjectPar newProjectPar = new ProjectPar();
                BeanUtils.copyProperties(newProjectPar, projectPar);
                if (sx == 1) {
                    newProjectPar.setProid(projectPar.getzProid());
                } else {
                    newProjectPar.setProid(UUIDGenerator.generate18());
                }
                newProjectPar.setBdcdyh(djsjFwHs.getBdcdyh());
                newProjectPar.setDjh(StringUtils.substring(djsjFwHs.getBdcdyh(), 0, 19));
                newProjectPar.setQjid(djsjFwHs.getFwHsIndex());
                newProjectPar.setSx(sx);
                sx++;
                projectParList.add(newProjectPar);
            }
        }

        if (CollectionUtils.isNotEmpty(djsjFwXmxxList)) {
            for (DjsjFwXmxx djsjFwXmxx : djsjFwXmxxList) {
                djidList.add(djsjFwXmxx.getFwXmxxIndex());
                ProjectPar newProjectPar = new ProjectPar();
                BeanUtils.copyProperties(newProjectPar, projectPar);
                if (sx == 1) {
                    newProjectPar.setProid(projectPar.getzProid());
                } else {
                    newProjectPar.setProid(UUIDGenerator.generate18());
                }
                newProjectPar.setBdcdyh(djsjFwXmxx.getBdcdyh());
                newProjectPar.setDjh(StringUtils.substring(djsjFwXmxx.getBdcdyh(), 0, 19));
                newProjectPar.setQjid(djsjFwXmxx.getFwXmxxIndex());
                newProjectPar.setSx(sx);
                sx++;
                projectParList.add(newProjectPar);
            }
        }
        if (CollectionUtils.isNotEmpty(ljzQcList)) {
            for (Map ljzMap : ljzQcList) {
                ProjectPar newProjectPar = new ProjectPar();
                BeanUtils.copyProperties(newProjectPar, projectPar);
                if (sx == 1) {
                    newProjectPar.setProid(projectPar.getzProid());
                } else {
                    newProjectPar.setProid(UUIDGenerator.generate18());
                }
                newProjectPar.setBdcdyh(CommonUtil.formatEmptyValue(ljzMap.get("bdcdyh")));
                newProjectPar.setDjh(StringUtils.substring(newProjectPar.getBdcdyh(), 0, 19));
                newProjectPar.setQjid(CommonUtil.formatEmptyValue(ljzMap.get("fw_xmxx_index")));
                newProjectPar.setSx(sx);
                sx++;
                projectParList.add(newProjectPar);
            }
        }
        // 土地
        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            for (DjsjZdxx djsjZdxx : djsjZdxxList) {
                djidList.add(djsjZdxx.getZdDjdcbIndex());
                ProjectPar newProjectPar = new ProjectPar();
                BeanUtils.copyProperties(newProjectPar, projectPar);
                if (sx == 1) {
                    newProjectPar.setProid(projectPar.getzProid());
                } else {
                    newProjectPar.setProid(UUIDGenerator.generate18());
                }
                newProjectPar.setBdcdyh(djsjZdxx.getBdcdyh());
                newProjectPar.setDjh(StringUtils.substring(djsjZdxx.getBdcdyh(), 0, 19));
                newProjectPar.setQjid(djsjZdxx.getZdDjdcbIndex());
                newProjectPar.setSx(sx);
                sx++;
                projectParList.add(newProjectPar);
            }
        }
        if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
            for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                if (StringUtils.isBlank(bdcXmRel.getQjid()) || StringUtils.isNotBlank(bdcXmRel.getQjid()) && !djidList.contains(bdcXmRel.getQjid())) {
                    ProjectPar newProjectPar = new ProjectPar();
                    BeanUtils.copyProperties(newProjectPar, projectPar);
                    if (sx == 1) {
                        newProjectPar.setProid(projectPar.getzProid());
                    } else {
                        newProjectPar.setProid(UUIDGenerator.generate18());
                    }
                    newProjectPar.setYxmid(bdcXmRel.getYproid());
                    newProjectPar.setQjid(CommonUtil.formatEmptyValue(bdcXmRel.getQjid()));
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXmRel.getYproid());
                        newProjectPar.setBdcdyh(bdcBdcdy.getBdcdyh());
                        newProjectPar.setBdcdyid(bdcBdcdy.getBdcdyid());
                        newProjectPar.setDjh(StringUtils.substring(newProjectPar.getBdcdyh(), 0, 19));
                    }
                    newProjectPar.setSx(sx);
                    sx++;
                    projectParList.add(newProjectPar);
                }
            }
        }

        return projectParList;
    }

    private Map getLjz(String dcbIndex) {
        Map ljzMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(dcbIndex)) {
            HashMap map = new HashMap();
            map.put("fw_dcb_index", dcbIndex);
            List<Map> ljzList = djsjFwService.getLpbList(map);
            if (CollectionUtils.isNotEmpty(ljzList)) {
                ljzMap = ljzList.get(0);
            }
        }
        return ljzMap;
    }


    private void getProjectPar(List<ProjectPar> projectParList) {
        if (CollectionUtils.isNotEmpty(projectParList)) {
            for (ProjectPar projectPar : projectParList) {
                getBdcXmRelFormProject(projectPar);
                sfYdhDfw(projectPar);
            }
            Collections.sort(projectParList);
        }
    }

    private void initYzh(ProjectPar projectPar, String yProid) {
        if (StringUtils.isNotBlank(yProid)) {
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(yProid);
            String ybdcqz = "";
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    if (StringUtils.isBlank(ybdcqz)) {
                        ybdcqz = bdcZs.getBdcqzh();
                    } else {
                        ybdcqz += "," + bdcZs.getBdcqzh();
                    }
                }
            }
            BdcPpgx bdcPpgx = bdcPpgxService.getBdcPpgxByFwproid(yProid);
            if (bdcPpgx != null && StringUtils.isNotBlank(bdcPpgx.getTdproid())) {
                List<BdcZs> bdcTdZsList = bdcZsService.queryBdcZsByProid(bdcPpgx.getTdproid());
                if (CollectionUtils.isNotEmpty(bdcTdZsList)) {
                    for (BdcZs bdcZs : bdcTdZsList) {
                        if (StringUtils.isBlank(ybdcqz)) {
                            ybdcqz = bdcZs.getBdcqzh();
                        } else {
                            ybdcqz += "," + bdcZs.getBdcqzh();
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(ybdcqz)) {
                projectPar.setYbdcqzh(ybdcqz);
            } else {
                projectPar.setYbdcqzh("");
            }
        }
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 判断是否是一单元多房 并初始化数据
     */
    private ProjectPar sfYdhDfw(ProjectPar projectPar) {
        String bdcfwlx = bdcdyService.getBdcdyfwlxBybdcdyh(projectPar.getBdcdyh());
        if (StringUtils.equals(bdcfwlx, Constants.BDCFWLX_DZ_DM)) {
            BdcPpgx fwBdcPpgx = bdcPpgxService.getBdcPpgxByFwproid(projectPar.getYxmid());
            if (fwBdcPpgx != null && StringUtils.isNotBlank(fwBdcPpgx.getBdcdyh())) {
                List<BdcPpgx> bdcPpgxList = bdcPpgxService.getBdcPpgxByBdcdyh(fwBdcPpgx.getBdcdyh());
                if (CollectionUtils.isNotEmpty(bdcPpgxList)) {
                    List<QllxVo> qllxVoList = qllxService.queryFdcqByBdcdyh(projectPar.getBdcdyh());
                    if (CollectionUtils.isNotEmpty(qllxVoList)) {
                        try {
                            for (BdcPpgx bdcPpgx : bdcPpgxList) {
                                if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                                    BdcXmRel bdcXmRel = projectPar.getBdcXmRelList().get(0);
                                    if (StringUtils.isNotBlank(bdcPpgx.getFwproid()) && !StringUtils.equals(bdcPpgx.getFwproid(), bdcXmRel.getYproid())) {
                                        BdcXmRel newBdcXmRel = new BdcXmRel();
                                        BeanUtils.copyProperties(newBdcXmRel, bdcXmRel);
                                        newBdcXmRel.setRelid(UUIDGenerator.generate18());
                                        newBdcXmRel.setYproid(bdcPpgx.getFwproid());
                                        projectPar.getBdcXmRelList().add(newBdcXmRel);
                                    }
                                    if (StringUtils.isNotBlank(bdcPpgx.getTdproid()) && !StringUtils.equals(bdcPpgx.getTdproid(), bdcXmRel.getYproid())) {
                                        BdcXmRel newBdcXmRel = new BdcXmRel();
                                        BeanUtils.copyProperties(newBdcXmRel, bdcXmRel);
                                        newBdcXmRel.setRelid(UUIDGenerator.generate18());
                                        newBdcXmRel.setYproid(bdcPpgx.getTdproid());
                                        projectPar.getBdcXmRelList().add(newBdcXmRel);
                                    }
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                        List<BdcXmRel> bdcXmRelList = Lists.newArrayList();
                        Map<String, BdcXmRel> bdcXmRelMap = Maps.newHashMap();
                        for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                            bdcXmRelMap.put(bdcXmRel.getYproid(), bdcXmRel);
                        }
                        for (String key : bdcXmRelMap.keySet()) {
                            bdcXmRelList.add(bdcXmRelMap.get(key));
                        }
                        projectPar.setBdcXmRelList(bdcXmRelList);
                    }
                }
            }
        }
        return projectPar;
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 组织项目关系
     */
    private boolean getBdcXmRelFormProject(ProjectPar projectPar) {
        boolean sfdqyzh = true;
        List<BdcXmRel> bdcXmRelList = Lists.newArrayList();
        List<BdcSqlxYwmxRel> bdcSqlxYwmxRelList = bdcXtYwmxService.getBdcSqlxYwmxRelBySqlx(projectPar.getDsqlx());
        if (CollectionUtils.isNotEmpty(bdcSqlxYwmxRelList) && bdcSqlxYwmxRelList.size() > 1) {
            for (BdcSqlxYwmxRel bdcSqlxYwmxRel : bdcSqlxYwmxRelList) {
                if (CommonUtil.indexOfStrs(Constants.SYSLY_YGS, bdcSqlxYwmxRel.getSysly())) {
                    List<QllxVo> bdcYgList = qllxService.getQllxByBdcdyh(new BdcYg(), projectPar.getBdcdyh());
                    if (CollectionUtils.isNotEmpty(bdcYgList))
                        for (QllxVo bdcYg : bdcYgList) {
                            if (projectPar.getSx() == 1 && (StringUtils.equals(bdcSqlxYwmxRel.getSysly(), Constants.SYSLY_YG) || StringUtils.equals(bdcSqlxYwmxRel.getSysly(), Constants.SYSLY_YG_ZX)) && Constants.YGDJZL_MM.contains(((BdcYg) bdcYg).getYgdjzl())) {
                                if (StringUtils.equals(bdcSqlxYwmxRel.getSysly(), Constants.SYSLY_YG)) {
                                    bdcXmRelList.add(getBdcXmRel(projectPar, StringUtils.EMPTY));
                                    bdcXmRelList.add(getBdcXmRel(projectPar, bdcYg.getProid()));
                                    initYzh(projectPar, projectPar.getYxmid());
                                }
                                if (StringUtils.equals(bdcSqlxYwmxRel.getSysly(), Constants.SYSLY_YG_ZX)) {
                                    bdcXmRelList.add(getBdcXmRel(projectPar, bdcYg.getProid()));
                                    projectPar.setYxmid(bdcYg.getProid());
                                    initYzh(projectPar, bdcYg.getProid());
                                }
                                projectPar.setSfdyYzh(false);
                                sfdqyzh = false;
                            }
                            if (projectPar.getSx() == 2 && Constants.YGDJZL_DY.contains(((BdcYg) bdcYg).getYgdjzl())) {
                                if (StringUtils.equals(bdcSqlxYwmxRel.getSysly(), Constants.SYSLY_YDY)) {
                                    bdcXmRelList.add(getBdcXmRel(projectPar, bdcYg.getProid()));
                                    bdcXmRelList.add(getBdcXmRel(projectPar, projectPar.getzProid()));
                                } else if (StringUtils.equals(bdcSqlxYwmxRel.getSysly(), Constants.SYSLY_YDY_ZX)) {
                                    initYzh(projectPar, bdcYg.getProid());
                                    projectPar.setYxmid(bdcYg.getProid());
                                    bdcXmRelList.add(getBdcXmRel(projectPar, bdcYg.getProid()));
                                }
                                projectPar.setSfdyYzh(false);
                                sfdqyzh = false;
                            }
                        }
                }
                if (projectPar.getSx() == 2 && StringUtils.equals(bdcSqlxYwmxRel.getSysly(), Constants.SYSLY_DY)) {
                    List<QllxVo> bdcDyaqList = qllxService.getQllxByBdcdyh(new BdcDyaq(), projectPar.getBdcdyh());
                    if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                        bdcXmRelList.add(getBdcXmRel(projectPar, bdcDyaqList.get(0).getProid()));
                        bdcXmRelList.add(getBdcXmRel(projectPar, projectPar.getzProid()));
                        projectPar.setYxmid(bdcDyaqList.get(0).getProid());
                        projectPar.setSfdyYzh(false);
                        sfdqyzh = false;
                    }
                }
            }
        }
        if (sfdqyzh) {
            BdcXmRel bdcXmRel = getBdcXmRel(projectPar, StringUtils.EMPTY);
            initYzh(projectPar, bdcXmRel.getYproid());
            bdcXmRelList.add(bdcXmRel);
        }
        BdcXmRel ppBdcXmRel = getPpBdcXmRel(projectPar);
        if (ppBdcXmRel != null) {
            bdcXmRelList.add(ppBdcXmRel);
        }
        if (Constants.SQLX_ZX_SFCD.equals(projectPar.getSqlx())) {
            List<BdcXmRel> sfcdBdcXmRelList = getSfcdBdcXmRelList(bdcXmRelList.get(0));
            if (CollectionUtils.isNotEmpty(sfcdBdcXmRelList)) {
                bdcXmRelList.addAll(sfcdBdcXmRelList);
            }
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            projectPar.setBdcXmRelList(bdcXmRelList);
        }
        return sfdqyzh;
    }

    private BdcXmRel getBdcXmRel(ProjectPar projectPar, String yProid) {
        BdcXmRel bdcXmRel = new BdcXmRel();
        bdcXmRel.setRelid(UUIDGenerator.generate18());
        bdcXmRel.setQjid(projectPar.getQjid());
        if (StringUtils.isNotBlank(yProid)) {
            bdcXmRel.setYproid(yProid);
        } else {
            bdcXmRel.setYproid(projectPar.getYxmid());
        }
        bdcXmRel.setProid(projectPar.getProid());
        bdcXmRel.setYdjxmly(projectPar.getXmly());
        return bdcXmRel;
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取匹配土地项目关系
     */
    private BdcXmRel getPpBdcXmRel(ProjectPar projectPar) {
        BdcXmRel bdcXmRel = null;
        BdcPpgx bdcPpgx = bdcPpgxService.getBdcPpgxByFwproid(projectPar.getYxmid());
        if (bdcPpgx != null && StringUtils.isNotBlank(bdcPpgx.getTdproid())) {
            bdcXmRel = new BdcXmRel();
            bdcXmRel.setRelid(UUIDGenerator.generate18());
            bdcXmRel.setProid(projectPar.getProid());
            bdcXmRel.setYproid(bdcPpgx.getTdproid());
            bdcXmRel.setYdjxmly(projectPar.getXmly());
        }
        return bdcXmRel;
    }

    /**
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @description 获取司法裁定项目关系
     */
    private List<BdcXmRel> getSfcdBdcXmRelList(BdcXmRel bdcXmRel) {
        List<BdcXmRel> bdcXmRelList = Lists.newArrayList();
        if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
            BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRel.getYproid());
            if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getJfywh())) {
                HashMap map = new HashMap();
                map.put("jfywh", bdcCf.getJfywh());
                map.put("issx", Constants.SXZT_ISSX);
                List<BdcCf> bdcCfList = bdcCfService.andEqualQueryCf(map, null);
                if (CollectionUtils.isNotEmpty(bdcCfList)) {
                    for (BdcCf bdcCftemp : bdcCfList) {
                        if (!(StringUtils.equals(bdcXmRel.getYproid(), bdcCftemp.getProid()))) {
                            BdcXmRel newBdcXmRel = new BdcXmRel();
                            newBdcXmRel.setRelid(UUIDGenerator.generate18());
                            newBdcXmRel.setProid(bdcXmRel.getProid());
                            newBdcXmRel.setYqlid(bdcCftemp.getQlid());
                            newBdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                            newBdcXmRel.setQjid(bdcXmRel.getQjid());
                            newBdcXmRel.setYproid(bdcCftemp.getProid());
                            bdcXmRelList.add(newBdcXmRel);
                        }
                    }
                }
            }
        }
        return bdcXmRelList;
    }
}
