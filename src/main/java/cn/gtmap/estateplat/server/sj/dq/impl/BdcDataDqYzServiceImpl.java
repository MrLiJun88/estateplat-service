package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcYwrDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/21 0021
 * @descriptionc 读取原值信息
 */
@Service
public class BdcDataDqYzServiceImpl implements BdcQlrDqService, BdcYwrDqService {
    @Autowired
    private BdcQlrService bdcQlrService;

    /**
     * @param projectPar
     * @return 权利人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时权利人
     */
    @Override
    public List<BdcQlr> getCreateQlr(ProjectPar projectPar) {
        List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrByProid(projectPar.getYxmid(), Constants.QLRLX_QLR);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlr.setQlrid(UUIDGenerator.generate18());
                bdcQlr.setProid(projectPar.getProid());
                bdcQlr.setQlrdlr(null);
                bdcQlr.setQlrdljg(null);
                bdcQlr.setQlrdlrdh(null);
                bdcQlr.setQlrdlrzjh(null);
                bdcQlr.setQlrdlrzjzl(null);
            }
        }
        if (CollectionUtils.isEmpty(bdcQlrList)) {
            bdcQlrList = Collections.EMPTY_LIST;
        }
        return bdcQlrList;
    }

    /**
     * @param projectPar
     * @return 义务人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时义务人
     */
    @Override
    public List<BdcQlr> getCreateYwr(ProjectPar projectPar) {
        List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrByProid(projectPar.getYxmid(), Constants.QLRLX_YWR);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlr.setQlrid(UUIDGenerator.generate18());
                bdcQlr.setProid(projectPar.getProid());
                bdcQlr.setQlrdlr(null);
                bdcQlr.setQlrdljg(null);
                bdcQlr.setQlrdlrdh(null);
                bdcQlr.setQlrdlrzjh(null);
                bdcQlr.setQlrdlrzjzl(null);
            }
        }
        if (CollectionUtils.isEmpty(bdcQlrList)) {
            bdcQlrList = Collections.EMPTY_LIST;
        }
        return bdcQlrList;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "yz";
    }
}
