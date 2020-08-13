package cn.gtmap.estateplat.server.sj.dq;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.server.sj.InterfaceCode;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/12 0012
 * @description 不动产权利人业务接口
 */
public interface BdcQlrDqService extends InterfaceCode {
    /**
     * @return 义务人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时义务人
     */
    List<BdcQlr> getCreateQlr(ProjectPar projectPar);
}
