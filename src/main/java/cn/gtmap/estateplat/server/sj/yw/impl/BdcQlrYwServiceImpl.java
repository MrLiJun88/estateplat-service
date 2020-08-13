package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.sj.InterfaceCodeBeanFactory;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcYwrDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/13 0013
 * @description 权利人读取逻辑
 */
@Service
public class BdcQlrYwServiceImpl implements BdcDataYwService {
    @Autowired
    private Set<BdcQlrDqService> bdcQlrDqServiceList;
    @Autowired
    private Set<BdcYwrDqService> bdcYwrDqServiceList;

    /**
     * @return 不动产数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化获取不动产数据
     */
    @Override
    public List<InsertVo> initbdcData(ProjectPar projectPar, List<InsertVo> insertVoList) {
        if (CollectionUtils.isEmpty(insertVoList)) {
            insertVoList = Lists.newArrayList();
        }
        BdcQlrDqService bdcQlrDqService = InterfaceCodeBeanFactory.getBean(bdcQlrDqServiceList, projectPar.getBdcXtYwmx().getQlrdq());
        if (bdcQlrDqService != null) {
            List<BdcQlr> bdcQlrList = bdcQlrDqService.getCreateQlr(projectPar);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                projectPar.setBdcQlrList(bdcQlrList);
                insertVoList.addAll(bdcQlrList);
            }
        }
        BdcYwrDqService bdcYwrDqService = InterfaceCodeBeanFactory.getBean(bdcYwrDqServiceList, projectPar.getBdcXtYwmx().getYwrdq());
        if (bdcYwrDqService != null) {
            List<BdcQlr> bdcYwrList = bdcYwrDqService.getCreateYwr(projectPar);
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                insertVoList.addAll(bdcYwrList);
            }
        }
        return insertVoList;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_qlr";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 4;
    }
}
