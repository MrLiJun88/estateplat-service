package cn.gtmap.estateplat.server.sj.dq;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description 不动产项目数据读取
 */
public interface BdcSpxxDqService {
    /**
     * @param projectPar 流程参数类
     * @return 不动产登记项目审批信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据过去项目信息
     */
    BdcSpxx getCreateBdcSpxx(ProjectPar projectPar, BdcSpxx bdcSpxx);
}

