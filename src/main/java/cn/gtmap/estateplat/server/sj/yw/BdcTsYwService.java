package cn.gtmap.estateplat.server.sj.yw;

import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.sj.InterfaceCode;
import cn.gtmap.estateplat.server.sj.InterfacerSx;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-28
 * @description 不动特殊业务服务
 */
public interface BdcTsYwService extends InterfaceCode, InterfacerSx {
    /**
     * @param projectParList 流程参数类
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化特殊业务
     */
    List<InsertVo> initTsYw(List<ProjectPar> projectParList);
}
