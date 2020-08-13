package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.sj.InterfaceCodeBeanFactory;
import cn.gtmap.estateplat.server.sj.dq.BdcSpxxDqService;
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
 * @version 1.0, 2020/2/14 0014
 * @description
 */
@Service
public class BdcSpxxYwServiceImpl implements BdcDataYwService {
    @Autowired
    private Set<BdcSpxxDqService> bdcSpxxDqServiceList;

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
        List<BdcSpxxDqService> bdcSpxxDqServices = InterfaceCodeBeanFactory.getBeans(bdcSpxxDqServiceList, projectPar.getBdcXtYwmx().getSpxxdq());
        if (CollectionUtils.isNotEmpty(bdcSpxxDqServices)) {
            BdcSpxx bdcSpxx = new BdcSpxx();
            for (int i = 0; i < bdcSpxxDqServices.size(); i++) {
                bdcSpxx = bdcSpxxDqServices.get(i).getCreateBdcSpxx(projectPar,bdcSpxx);
            }
            if (bdcSpxx != null) {
                projectPar.setBdcSpxx(bdcSpxx);
                insertVoList.add(bdcSpxx);
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
        return "bdc_spxx";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 3;
    }
}
