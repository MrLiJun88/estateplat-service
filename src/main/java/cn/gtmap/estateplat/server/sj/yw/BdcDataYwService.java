package cn.gtmap.estateplat.server.sj.yw;

import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.sj.InterfaceCode;
import cn.gtmap.estateplat.server.sj.InterfacerSx;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/13 0013
 * @description
 */
public interface BdcDataYwService extends InterfaceCode, InterfacerSx {
    /**
     * @return 不动产数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化获取不动产数据
     */
    List<InsertVo> initbdcData(ProjectPar projectPar, List<InsertVo> insertVoList);
}
