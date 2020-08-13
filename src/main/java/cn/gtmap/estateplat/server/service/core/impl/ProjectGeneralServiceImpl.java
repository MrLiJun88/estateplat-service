package cn.gtmap.estateplat.server.service.core.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.core.ProjectGeneralService;
import cn.gtmap.estateplat.utils.DateUtils;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/4/20
 * @description 项目通用操作管理
 */
@Service
public class ProjectGeneralServiceImpl implements ProjectGeneralService {
    @Autowired
    protected SysUserService sysUserService;
    @Autowired
    private BdcZsbhService bdcZsbhService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcZsService bdcZsService;

    /**
     * @param xmxx 项目信息
     * @return 返回工作流实例
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @description 根据项目信息创建工作流项目
     */
    @Override
    public PfWorkFlowInstanceVo createWorkflowInstance(Xmxx xmxx) {
        return null;
    }

    /**
     * @param wiid 工作流实例ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @description 删除工作流实例
     */
    @Override
    public void deleteWorkflowInstance(String wiid) {
        
    }

    /**
     * @param xmxx
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 创建不动产登记项目，包括项目信息BDC_XM,收件信息BDC_SJXX,收件材料BDC_SJCL,审批信息BDC_SPXX,
     */
    @Transactional
    @Override
    public void createProject(Xmxx xmxx) {
        Project project = null;
        if (xmxx instanceof Project)
            project = (Project) xmxx;
        if (project != null && StringUtils.isNotBlank(project.getUserId())) {
            PfUserVo userVo = sysUserService.getUserVo(project.getUserId());
            String userName = "";
            if (userVo != null && StringUtils.isNotBlank(userVo.getUserName()))
                userName = userVo.getUserName();
            BdcXm bdcXm = bdcXmService.creatBdcXm(project.getProid(), userName, project.getWorkFlowDefId(), project.getWiid());  // 创建项目
            bdcSjdService.createSjxxByBdcxm(bdcXm);   // 初始化收件单
        }
    }

    /**
     * @param wiid   工作流实例ID
     * @param taskId 任务ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @description 自动生成项目审批签名
     */
    @Override
    public void createProjectSignature(String wiid, String taskId) {

    }

    /**
     * @param wiid   工作流实例ID
     * @param taskId 任务ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @description 删除生成项目审批签名
     */
    @Override
    public void deleteProjectSignature(String wiid, String taskId) {

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
     * 缮证人和时间、抵押注销登簿人和注销登簿时间
     */
    @Override
    public void transmitProject(String wiid) {

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
    public void completeProject(String wiid) {

    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 删除项目信息，包括项目所涉及的BDC_XM,BDC_SJXX,BDC_SJCL,BDC_SPXX，BDC_QLR,BDC_ZS,BDC_BDCDY,BDC_DJB
     */
    @Transactional
    @Override
    public void deleteProject(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                String proid = bdcXm.getProid();
                //证书编号
                delZsbh(proid);
                //删除不动产项目相关信息
                deleteBdcXmxx(bdcXm);
                //BDC_XM项目主表信息
                bdcXmService.delBdcXmByProid(proid);
                //项目附件信息
                delProjectNode(proid);

            }
        }
    }

    /**
     * @param bdcXm 不动产项目
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 删除不动产项目相关信息，包括项目所涉及的BDC_XM_REL,BDC_SJXX,BDC_SJCL,BDC_SPXX，BDC_QLR,BDC_ZS,BDC_BDCDY,BDC_DJB
     */
    private void deleteBdcXmxx(BdcXm bdcXm) {
        String proid = bdcXm.getProid();
        //zdd 删除项目关系表
        bdcXmRelService.delBdcXmRelByProid(proid);
        //zdd 删除收件单信息表
        List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(bdcXm.getWiid());
        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
            for (BdcSjxx bdcSjxx : bdcSjxxList) {
                bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
            }
        }
        //zdd 删除审批信息
        bdcSpxxService.delBdcSpxxByProid(proid);
        //zdd 删除权利人证书关系信息表以及权利人信息
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
                bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
            }
        }
        //zdd 删除项目证书关系表 删除证书信息
        List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
            for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                bdcXmZsRelService.delBdcXmZsRelByXmzsgxid(bdcXmzsRel.getXmzsgxid());
                bdcZsService.delBdcZsByZsid(bdcXmzsRel.getZsid());
            }
        }
        //zdd 删除不动产单元
        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            List<BdcXm> bdcXmListTemp = null;
            HashMap map = new HashMap();
            map.put("bdcdyid", bdcXm.getBdcdyid());
            bdcXmListTemp = bdcXmService.andEqualQueryBdcXm(map);
            if (bdcXmListTemp != null && bdcXmListTemp.size() == 1) {
                bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
            }
        }
    }

    /**
     * @param proid 项目id
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 删除项目证书编号
     */
    private void delZsbh(String proid) {
        List<BdcZs> zsList = bdcZsService.queryBdcZsByProid(proid);
        if (CollectionUtils.isNotEmpty(zsList)) {
            for (BdcZs bdcZs : zsList) {
                HashMap map = new HashMap();
                map.put("zsid", bdcZs.getZsid());
                List<BdcZsbh> bdcZsbhList = bdcZsbhService.getBdcZsBhListByBhfw(map);
                if (CollectionUtils.isNotEmpty(bdcZsbhList)) {
                    for (BdcZsbh bdcZsbh : bdcZsbhList) {
                        bdcZsbh.setLqr("");
                        bdcZsbh.setLqrid("");
                        bdcZsbh.setZsid("");
                        bdcZsbh.setSyqk("0");
                        entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
                    }
                }
            }
        }
    }

    /**
     * @param proid 项目id
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 删除项目附件信息
     */
    private void delProjectNode(String proid) {
        Space space = fileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF");
        com.gtis.fileCenter.model.Node tempNode = fileCenterNodeServiceImpl.getNode(space.getId(), proid, true);
        fileCenterNodeServiceImpl.remove(tempNode.getId());
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 更新项目状态，主要是项目办理状态
     */
    @Transactional
    @Override
    public void updateProjectStatus(String wiid) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                bdcXm.setBjsj(DateUtils.now());
                bdcXm.setXmzt("1");
                entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                qllxService.endQllxZt(bdcXm);
            }
        }
    }

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 生成项目的证书信息
     */
    @Override
    public void generateProjectZs(String wiid,String previewZs) {
        List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                //创建证书  同时保存权利人证书关系表
                List<BdcZs> list = bdcZsService.creatBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
                if (CollectionUtils.isNotEmpty(list)) {
                    //zdd 生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRel(bdcXm, list, bdcQlrList);
                    //zdd 生成项目证书关系表
                    bdcXmZsRelService.creatBdcXmZsRel(list, bdcXm.getProid());
                } else {
                    throw new AppException(3006);
                }
            }
        }
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
}
