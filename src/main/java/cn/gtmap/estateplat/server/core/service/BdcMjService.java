package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description
 */
public interface BdcMjService {
    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 同步面积字段
     */
    void synMj(ProjectPar projectPar, BdcSpxx bdcSpxx);
}
