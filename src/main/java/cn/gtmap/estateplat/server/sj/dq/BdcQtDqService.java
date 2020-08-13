package cn.gtmap.estateplat.server.sj.dq;

import cn.gtmap.estateplat.server.sj.InterfaceCode;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-12
 * @description 读取其他信息
 */
public interface BdcQtDqService extends InterfaceCode {
    void createQtxx(ProjectPar projectPar);
}
