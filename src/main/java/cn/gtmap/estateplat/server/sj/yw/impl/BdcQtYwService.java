package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.BdcXtYwmx;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.sj.InterfaceCodeBeanFactory;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2020/3/12 0018
 * @description
 */
@Service
public class BdcQtYwService implements BdcDataYwService {

    @Autowired
    private Set<BdcQtDqService> bdcQtDqServiceList;

    /**
     * @param projectPar
     * @param insertVoList
     * @return 不动产数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化获取不动产数据
     */
    @Override
    public List<InsertVo> initbdcData(ProjectPar projectPar, List<InsertVo> insertVoList) {
        if (CollectionUtils.isEmpty(insertVoList)) {
            insertVoList = Lists.newArrayList();
        }
        BdcXtYwmx bdcXtYwmx = projectPar.getBdcXtYwmx();
        if (bdcXtYwmx != null && StringUtils.isNotBlank(bdcXtYwmx.getQtdq())) {
            List<BdcQtDqService> bdcQtDqServices = InterfaceCodeBeanFactory.getBeans(bdcQtDqServiceList, bdcXtYwmx.getQtdq());
            for (BdcQtDqService bdcQtDqService : bdcQtDqServices) {
                bdcQtDqService.createQtxx(projectPar);
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
        return "qt";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 6;
    }
}
