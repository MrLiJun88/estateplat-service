package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-03
 * @description
 */
@Service
public class BdcQtZxService implements BdcQtDqService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;


    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (StringUtils.isNotBlank(projectPar.getYxmid()) && StringUtils.isNotBlank(projectPar.getProid())) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(projectPar.getProid());
            BdcXm yBdcXm = bdcXmService.getBdcXmByProid(projectPar.getYxmid());
            if (bdcXm != null && yBdcXm != null) {
                bdcXm.setYbdcqzh(yBdcXm.getYbdcqzh());
                if (StringUtils.equals(yBdcXm.getQllx(), Constants.QLLX_DYAQ) || StringUtils.equals(yBdcXm.getQllx(), Constants.QLLX_YGDJ)) {
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(projectPar.getYxmid());
                    String ybdcqz = "";
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        for (BdcZs bdcZs : bdcZsList) {
                            if (StringUtils.isBlank(ybdcqz)) {
                                ybdcqz = bdcZs.getBdcqzh();
                            } else {
                                ybdcqz += "," + bdcZs.getBdcqzh();
                            }
                        }
                    }
                    bdcXm.setYbdcqzmh(ybdcqz);
                    bdcXmService.saveBdcXm(bdcXm);
                }
                projectPar.setSfdyYzh(false);
            }
        }
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "zx";
    }
}
