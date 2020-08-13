package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcSpxxDqService;
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
 * @version 1.0, 2020/2/13 0013
 * @description 不动产数据获取---->上一手
 */
@Service
public class BdcDataDqSysServiceImpl implements BdcQlrDqService, BdcYwrDqService, BdcSpxxDqService {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    /**
     * @param projectPar
     * @return 权利人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时权利人
     */
    @Override
    public List<BdcQlr> getCreateQlr(ProjectPar projectPar) {
        return null;
    }

    /**
     * @param projectPar
     * @return 义务人
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取流程创建时义务人
     */
    @Override
    public List<BdcQlr> getCreateYwr(ProjectPar projectPar) {
        List<BdcQlr> bdcQlrList = bdcQlrService.getBdcQlrByProid(projectPar.getYxmid(), Constants.QLRLX_QLR);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlr.setQlrid(UUIDGenerator.generate18());
                bdcQlr.setProid(projectPar.getProid());
                bdcQlr.setQlrlx(Constants.QLRLX_YWR);
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
     * @param projectPar 流程参数类
     * @return 不动产登记项目审批信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据过去项目信息
     */
    @Override
    public BdcSpxx getCreateBdcSpxx(ProjectPar projectPar, BdcSpxx bdcSpxx) {
        return bdcSpxxService.getBdcSpxxFromYProjectPar(projectPar, bdcSpxx);
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "sys";
    }

}
