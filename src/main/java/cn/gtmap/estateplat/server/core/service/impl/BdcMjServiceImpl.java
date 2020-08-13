package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.server.core.service.BdcMjService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description
 */
@Service
public class BdcMjServiceImpl implements BdcMjService {
    /**
     * @param projectPar
     * @param bdcSpxx
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 同步面积字段
     */
    @Override
    public void synMj(ProjectPar projectPar, BdcSpxx bdcSpxx) {
        if (projectPar != null) {
            if (projectPar.getJzmj() != null) {
                bdcSpxx.setMj(projectPar.getJzmj());
                bdcSpxx.setScmj(projectPar.getJzmj());
                bdcSpxx.setFzmj(projectPar.getJzmj());
            }
        }
    }
}
