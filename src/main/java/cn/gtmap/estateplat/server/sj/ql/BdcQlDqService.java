package cn.gtmap.estateplat.server.sj.ql;

import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.sj.InterfaceCode;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/17 0017
 * @description 不动产权利数据读取
 */
public interface BdcQlDqService extends InterfaceCode {
    /**
     * @param projectPar 流程参数类
     * @return 不动产权利
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息从项目上
     */
    List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList);

    /**
     * @param projectPar 流程参数类
     * @return 不动产权利
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息从权籍中
     */
    List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList);

    /**
     * @param projectPar 流程参数类
     * @return 不动产权利
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息从预告中
     */
    List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList);
}
