package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcSpxxDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcYwrDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/18
 * @description 读取预告信息
 */
@Service
public class BdcDataDqYgServiceImpl implements BdcQlrDqService, BdcYwrDqService, BdcSpxxDqService {
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    /**
     * @param projectPar
     * @return 权利人
     * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取流程创建时权利人
     */
    @Override
    public List<BdcQlr> getCreateQlr(ProjectPar projectPar) {
        return null;
    }

    /**
     * @param projectPar
     * @return 义务人
     * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取流程创建时义务人
     */
    @Override
    public List<BdcQlr> getCreateYwr(ProjectPar projectPar) {
        return null;
    }

    /**
     * @param projectPar 流程参数类
     * @return 不动产登记项目审批信息
     * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 初始化数据审批信息
     */
    @Override
    public BdcSpxx getCreateBdcSpxx(ProjectPar projectPar, BdcSpxx bdcSpxx) {
        if (bdcSpxx == null) {
            bdcSpxx = new BdcSpxx();
        }
        //通过projectPar初始化bdcSpxx
        bdcSpxx = bdcSpxxService.getBdcSpxxFromProjectPar(projectPar, bdcSpxx);
        bdcSpxx = bdcSpxxService.getBdcSpxxFromYg(projectPar.getBdcdyh(), bdcSpxx);
        //zwq 若审批信息中有mc,将mc转为dm
        bdcSpxx = bdcZdGlService.changeToDm(bdcSpxx);
        return bdcSpxx;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "yg";
    }
}
