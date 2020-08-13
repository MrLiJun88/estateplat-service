package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-12
 * @description 获取在建建筑物
 */
@Service
public class BdcZjjzwDqServiceImpl implements BdcQtDqService {

    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        bdcZjjzwxxService.createZjjzwxx(projectPar.getProid(), projectPar.getSx() + "", projectPar.getBdcdyh());
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "jzw";
    }
}
