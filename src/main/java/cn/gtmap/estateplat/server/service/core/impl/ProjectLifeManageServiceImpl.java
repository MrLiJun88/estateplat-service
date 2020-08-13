package cn.gtmap.estateplat.server.service.core.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcFwFsssService;
import cn.gtmap.estateplat.server.core.service.BdcSqrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.service.core.ProjectCustomService;
import cn.gtmap.estateplat.server.service.core.ProjectGeneralService;
import cn.gtmap.estateplat.server.service.core.ProjectLifeManageService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/4/20
 * @description 项目生命周期管理服务
 */
@Service
public class ProjectLifeManageServiceImpl implements ProjectLifeManageService {
    @Autowired
    private ProjectCustomServiceContext projectCustomServiceContext;
    @Autowired
    ProjectGeneralService projectGeneralService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSqrService bdcSqrService;
    @Autowired
    private BdcFwFsssService bdcFwFsssService;

    /**
     * @param xmxx 项目信息
     * @return 返回工作流实例
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @description 根据项目信息创建工作流项目
     */
    @Override
    public PfWorkFlowInstanceVo createWorkflowInstance(Xmxx xmxx) {
        //创建工作流项目前先验证是否所有限制条件都不满足
        return null;
    }

    /**
     * @param xmxx 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 创建不动产登记项目
     */
    @Override
    public void createProject(Xmxx xmxx) {
        projectGeneralService.createProject(xmxx);
    }

    /**
     * @param xmxx 项目信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 初始化不动产登记项目
     */
    @Override
    public void initializeProject(Xmxx xmxx) {
        Project project = (Project) xmxx;
        ProjectCustomService projectCustomService = projectCustomServiceContext.getDjServiceByWiid(project.getWiid());
        projectCustomService.initializeProject(xmxx);
    }

    /**
     * @param xmxx 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始化不动产登记项目（批量界面）
     */
    @Override
    public void initializeProjectForPl(Xmxx xmxx) {
        Project project = (Project) xmxx;
        List<InsertVo> insertVoList = new LinkedList<InsertVo>();
        //获取哪个登记类型service
        CreatProjectService creatProjectService = projectService.getCreatProjectService(project);
        if (project != null && StringUtils.isNotBlank(project.getProid()) && CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
            List<BdcXmRel> bdcXmRelList = project.getBdcXmRelList();
            String proid = project.getProid();
            //zdd 将不动产单元编号前19位作为键   登记薄ID作为值
            HashMap<String, String> djbidMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    //jyl组织单个不动产单元信息的project
                    List<BdcXmRel> tempLdcXmRelList = new ArrayList<BdcXmRel>();
                    bdcXmRel.setProid(proid);
                    tempLdcXmRelList.add(bdcXmRel);
                    project.setBdcXmRelList(tempLdcXmRelList);
                    //zdd 将后台传过来的项目关系信息  初始化到project对象中   便于后面创建单个项目
                    project.setDjId(bdcXmRel.getQjid());
                    project.setYxmid(bdcXmRel.getYproid());
                    project.setXmly(bdcXmRel.getYdjxmly());
                    project.setYqlid(bdcXmRel.getYqlid());
                    //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                    project.setProid(proid);
                    //zdd 不动产单元号获取
                    if (StringUtils.isNotBlank(project.getDjId())) {
                        HashMap map = new HashMap();
                        if (StringUtils.isNotBlank(project.getBdclx()))
                            map.put("bdclx", project.getBdclx());
                        map.put("id", project.getDjId());
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if (CollectionUtils.isNotEmpty(bdcdyList)) {
                            if (bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null)
                                project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                            if (bdcdyList.get(0).containsKey(ParamsConstants.BDCLX_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL) != null)
                                project.setBdclx(bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL).toString());
                        }
                    } else if (StringUtils.isNotBlank(project.getYxmid())) {
                        //否则来源不动产自身
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                        if (bdcBdcdy != null) {
                            project.setBdclx(bdcBdcdy.getBdclx());
                            project.setBdcdyh(bdcBdcdy.getBdcdyh());
                            if(StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()))
                                djbidMap.put(bdcBdcdy.getBdcdyh().substring(0, 19), bdcBdcdy.getDjbid());
                        }
                    }
                    //登记薄可能不一致
                    if (StringUtils.isNotBlank(project.getBdcdyh())) {
                        project.setZdzhh(project.getBdcdyh().substring(0, 19));
                        if (djbidMap.containsKey(project.getBdcdyh().substring(0, 19))) {
                            project.setDjbid(djbidMap.get(project.getBdcdyh().substring(0, 19)));
                        } else {
                            String djbid = UUIDGenerator.generate18();
                            djbidMap.put(project.getBdcdyh().substring(0, 19), djbid);
                            project.setDjbid(djbid);
                        }
                    }
                    List<InsertVo> list = null;
                    list = creatProjectService.initVoFromOldData(project);
                    insertVoList.addAll(list);
                    //zdd 从新赋值proid
                    proid = UUIDGenerator.generate18();
                }
            }
        }
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            List<InsertVo> voList = new LinkedList<InsertVo>();
            //更新申请人
            for(InsertVo vo:insertVoList){
                if(vo instanceof BdcQlr){
                    BdcSqr bdcSqr=bdcSqrService.initBdcSqrListFromBdcQlr((BdcQlr) vo,project.getWiid());
                    voList.add(bdcSqr);
                }
                voList.add(vo);
            }
            creatProjectService.saveOrUpdateProjectData(voList);
        }
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(project.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(project.getWiid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm xm : bdcXmList) {
                //jyl 初始化主房的子户室做附属设施
                if (StringUtils.isNotBlank(xm.getBdcdyid()) && StringUtils.isNotBlank(xm.getWiid())) {
                    BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(xm.getBdcdyid());
                    bdcFwFsssService.intiBdcFwfsssForZhs(bdcdy.getBdcdyh(), xm);
                }
            }
        }
    }

    /**
     * @param xmxx 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始化不动产登记项目（增加权利）
     */
    @Override
    public void initializeProjectForAddQl(Xmxx xmxx) {
        Project project = (Project) xmxx;
        List<InsertVo> insertVoList = new LinkedList<InsertVo>();
        //获取哪个登记类型service
        CreatProjectService creatProjectService = projectService.getCreatProjectServiceForAddQl(project);
        //获取哪个登记类型service
        TurnProjectService turnProjectDefaultService = projectService.getTurnProjectServiceForAddQl(project);
        if (project != null && StringUtils.isNotBlank(project.getProid()) && CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
            List<BdcXmRel> bdcXmRelList = project.getBdcXmRelList();
            String proid = project.getProid();
            //zdd 将不动产单元编号前19位作为键   登记薄ID作为值
            HashMap<String, String> djbidMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    //jyl组织单个不动产单元信息的project
                    List<BdcXmRel> tempLdcXmRelList = new ArrayList<BdcXmRel>();
                    bdcXmRel.setProid(proid);
                    tempLdcXmRelList.add(bdcXmRel);
                    project.setBdcXmRelList(tempLdcXmRelList);
                    //zdd 将后台传过来的项目关系信息  初始化到project对象中   便于后面创建单个项目
                    project.setDjId(bdcXmRel.getQjid());
                    project.setYxmid(bdcXmRel.getYproid());
                    project.setXmly(bdcXmRel.getYdjxmly());
                    project.setYqlid(bdcXmRel.getYqlid());
                    //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                    project.setProid(proid);
                    //zdd 不动产单元号获取
                    if (StringUtils.isNotBlank(project.getDjId())) {
                        HashMap map = new HashMap();
                        if (StringUtils.isNotBlank(project.getBdclx()))
                            map.put("bdclx", project.getBdclx());
                        map.put("id", project.getDjId());
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if (CollectionUtils.isNotEmpty(bdcdyList)) {
                            if (bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null)
                                project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                            if (bdcdyList.get(0).containsKey(ParamsConstants.BDCLX_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL) != null)
                                project.setBdclx(bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL).toString());
                        }
                    } else if (StringUtils.isNotBlank(project.getYxmid())) {
                        //否则来源不动产自身
                        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                        if (bdcBdcdy != null) {
                            project.setBdclx(bdcBdcdy.getBdclx());
                            project.setBdcdyh(bdcBdcdy.getBdcdyh());
                            project.setZdzhh(bdcBdcdy.getBdcdyh().substring(0, 19));
                            project.setYbdcdyid(bdcBdcdy.getBdcdyid());
                            djbidMap.put(bdcBdcdy.getBdcdyh().substring(0, 19), bdcBdcdy.getDjbid());
                        }
                    } else if (StringUtils.isNotBlank(project.getYqlid())) {
                        //
                    }
                    //登记薄可能不一致
                    if (StringUtils.isNotBlank(project.getBdcdyh())) {
                        project.setZdzhh(project.getBdcdyh().substring(0, 19));
                        if (djbidMap.containsKey(project.getBdcdyh().substring(0, 19))) {
                            project.setDjbid(djbidMap.get(project.getBdcdyh().substring(0, 19)));
                        } else {
                            String djbid = UUIDGenerator.generate18();
                            djbidMap.put(project.getBdcdyh().substring(0, 19), djbid);
                            project.setDjbid(djbid);
                        }
                    }
                    List<InsertVo> list = null;
                    list = creatProjectService.initVoFromOldData(project);
                    insertVoList.addAll(list);
                    //zdd 从新赋值proid
                    proid = UUIDGenerator.generate18();
                }
            }
        }
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            creatProjectService.saveOrUpdateProjectData(insertVoList);
        }
        List<BdcXm> bdcXmList = null;
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
        if (bdcXm != null && StringUtils.isNotBlank(project.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm xm : bdcXmList) {
                if(StringUtils.equals(xm.getProid(),project.getProid())) {
                    turnProjectDefaultService.saveQllxVo(xm);
                }
            }
        }
    }

    /**
     * @param wiid       工作流项目ID
     * @param activityId 退回后活动ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 撤回或取回不动产登记项目，
     * 如果已生成证书，返回到缮证前，则删除证书、更新占用的证书编号信息（使用状态、使用人和使用时间等），上一手权利权属状态。
     * 如果已生成电子签名，返回到审核前，则删除签名
     * 如果已登簿，返回到登簿前，则删除登簿信息
     */
    @Override
    public void retreatProject(String wiid, String activityId) {

    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 流转不动产登记项目，证书编号使用情况和状态，不动产登记项目状态，创建项目证书、登簿情况（人和时间）、
     * 缮证人和时间、抵押注销登簿人和注销登簿时间以及验证项目
     */
    @Override
    public void transmitProject(String wiid) {
        validateProject(wiid, "");
    }

    /**
     * @param wiid   工作流项目ID
     * @param taskId 当前工作流活动任务ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 验证不动产登记项目，包括必填项，存储值的正确性和限制条件等
     */
    @Override
    public boolean validateProject(String wiid, String taskId) throws AppException {
        return false;
    }

    /**
     * @param wiid 工作流项目ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @description 对不动产登记项目进行登簿操作，包括修改不动产权利权属状态、上一手权属状态，登簿人和时间等
     */
    @Override
    public void registerProject(String wiid) {

    }

    /**
     * @param wiid 工作流项目ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @description 对不动产登记项目的证书进行缮证，生成证书编号，缮证人和时间等
     */
    @Override
    public void certificateProject(String wiid) {

    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 办完不动产登记项目，更新项目状态、项目权属状态、上一手权属状态（包括过渡数据权属状态）、权利附记信息、
     * 证书编号使用情况、自动归档
     */
    @Override
    public void completeProject(String wiid) throws Exception {
        projectGeneralService.updateProjectStatus(wiid);
        //更新权属状态、上一手权属状态（包括过渡数据权属状态）
        ProjectCustomService projectCustomService = projectCustomServiceContext.getDjServiceByWiid(wiid);
        projectCustomService.updateProjectQszt(wiid);
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 删除不动产登记项目，包括项目所占用的证书编号，上一手权属状态（包括过渡数据权属状态），不动产项目项目，BDC_XM,BDC_SJXX,BDC_SJCL,
     * BDC_SPXX,不动产权利信息，BDC_QLR,BDC_ZS,BDC_BDCDY,BDC_DJB，不动产项目附件信息、工作流项目信息
     */
    @Override
    public void deleteProject(String wiid) {
        //根据类型删除，改变上一手权属状态，删除本项目权利信息
        ProjectCustomService projectCustomService = projectCustomServiceContext.getDjServiceByWiid(wiid);
        projectCustomService.revertYqlxx(wiid);
        projectCustomService.deleteProjectQlxx(wiid);
        //删除统一的项目相关信息
        projectGeneralService.deleteProject(wiid);
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 生成项目的证书信息
     */
    @Override
    public void generateProjectZs(String wiid,String previewZs) throws AppException {
        projectGeneralService.generateProjectZs(wiid,previewZs);
    }
}
