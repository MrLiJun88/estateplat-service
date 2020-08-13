package cn.gtmap.estateplat.server.service.core;

import cn.gtmap.estateplat.model.server.core.Xmxx;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/4/19
 * @description 不动产登记服务
 */
public interface ProjectCustomService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 初始化项目，根据不动产单元号或者证书（明）信息初始化BDC_XM,收件信息C_SJXX，收件材料C_SJCl，审批信息C_SPXX，
     * 不动产权利信息，不动产单元BDC_BDCDY、不动产登记簿BDC_、权利人信息
     * @param xmxx 信息信息
     */
    void initializeProject(Xmxx xmxx);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 更新项目权属状态，包括本次项目权利状态、上一手权属状态（包括过渡数据权属状态），用于项目登簿时
     * @param wiid
     */
    void updateProjectQszt(String wiid) throws Exception;

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/28
     * @param wiid 工作流项目ID
     * @return
     * @description 撤回项目的权属状态，上一手权属状态（包括过渡数据权属状态）,用于项目从登簿环节撤回、退回或删除
     */
    void revertProjectQszt(String wiid);

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 删除不动产登记项目权利信息，各权利删除对应的信息有所不同
     */
    void deleteProjectQlxx(String wiid);

    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 还原上一手权利信息，包括权利状态等，主要用于删除项目，撤销或退回项目
     */
    void revertYqlxx(String wiid);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/28
     * @param wiid 工作流项目ID
     * @return
     * @description 生成证书信息，包括附记、权利其他状况等
     */
    void generateProjectZs(String wiid);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/8/10
     * @param
     * @return
     * @description 项目业务编号
     */
    String getProjectCode();
}
