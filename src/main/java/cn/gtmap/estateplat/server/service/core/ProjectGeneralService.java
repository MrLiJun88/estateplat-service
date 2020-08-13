package cn.gtmap.estateplat.server.service.core;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.Xmxx;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/4/20
 * @description
 */
public interface ProjectGeneralService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @param xmxx 项目信息
     * @return 返回工作流实例
     * @description 根据项目信息创建工作流项目
     */
    PfWorkFlowInstanceVo createWorkflowInstance(Xmxx xmxx);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @param wiid 工作流实例ID
     * @return
     * @description 删除工作流实例
     */
    void deleteWorkflowInstance(String wiid);

    /**
     * @param xmxx
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 创建不动产登记项目，包括项目信息BDC_XM,收件信息BDC_SJXX,收件材料BDC_SJCL,审批信息BDC_SPXX,
     */
    void createProject(Xmxx xmxx);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @param wiid 工作流实例ID
     * @param taskId 任务ID
     * @return
     * @description 自动生成项目审批签名
     */
    void createProjectSignature(String wiid,String taskId);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @param wiid 工作流实例ID
     * @param taskId 任务ID
     * @return
     * @description 删除生成项目审批签名
     */
    void deleteProjectSignature(String wiid,String taskId);


    /**
     * @param wiid       工作流项目ID
     * @param activityId 退回后活动ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 撤回或取回不动产登记项目，
     * 如果已生成证书，返回到缮证前，则删除证书、更新占用的证书编号信息（使用状态、使用人和使用时间等），上一手权利权属状态。
     * 如果已生成电子签名，返回到审核前，则删除签名
     * 如果已登簿，返回到登簿前，则删除登簿信息
     */
    void retreatProject(String wiid, String activityId);


    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 流转不动产登记项目，证书编号使用情况和状态，不动产登记项目状态，创建项目证书、登簿情况（人和时间）、
     * 缮证人和时间、抵押注销登簿人和注销登簿时间
     */
    void transmitProject(String wiid);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/27
     * @param wiid 工作流项目ID
     * @return
     * @description 对不动产登记项目的证书进行缮证，生成证书编号，缮证人和时间等
     */
    void certificateProject(String wiid);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 办完不动产登记项目，更新项目状态、项目权属状态、上一手权属状态（包括过渡数据权属状态）、权利附记信息、
     * 证书编号使用情况、自动归档
     * @param wiid 工作流项目ID
     */
    void completeProject(String wiid);


    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 删除项目信息，包括项目所涉及的BDC_XM,BDC_SJXX,BDC_SJCL,BDC_SPXX，BDC_QLR,BDC_ZS,BDC_BDCDY,BDC_DJB
     */
    void deleteProject(String wiid);

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 更新项目状态，主要是项目办理状态
     */
    void updateProjectStatus(String wiid);

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 生成项目的证书信息
     */
    void generateProjectZs(String wiid,String previewZs);


    /**
     * @param wiid   工作流项目ID
     * @param taskId 当前工作流活动任务ID
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 验证不动产登记项目，包括必填项，存储值的正确性和限制条件等
     */
    boolean validateProject(String wiid, String taskId) throws AppException;
}
