package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcSpxxDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcXmDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcYwrDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/13 0013
 * @description 不读取数据
 */
@Service
public class BdcDataDqBdqServiceImpl implements BdcQlrDqService, BdcYwrDqService, BdcXmDqService, BdcSpxxDqService {

    /**
     * @param projectPar
     * @return 义务人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时义务人
     */
    @Override
    public List<BdcQlr> getCreateQlr(ProjectPar projectPar) {
        return null;
    }

    /**
     * @param projectPar
     * @return 权利人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时权利人
     */
    @Override
    public List<BdcQlr> getCreateYwr(ProjectPar projectPar) {
        return null;
    }

    /**
     * @param projectPar 流程参数类
     * @return 不动产项目
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据过去项目信息
     */
    @Override
    public BdcXm getCreateBdcXm(ProjectPar projectPar) {
        return null;
    }

    /**
     * @param projectPar 流程参数类
     * @param bdcSpxx
     * @return 不动产登记项目审批信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据过去项目信息
     */
    @Override
    public BdcSpxx getCreateBdcSpxx(ProjectPar projectPar, BdcSpxx bdcSpxx) {
        return null;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdq";
    }
}
