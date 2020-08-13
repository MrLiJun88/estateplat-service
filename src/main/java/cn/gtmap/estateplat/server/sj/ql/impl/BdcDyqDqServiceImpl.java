package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.BdcDyq;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/20
 * @description 地役权
 */
@Service
public class BdcDyqDqServiceImpl implements BdcQlDqService {

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcDyq bdcDyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcDyq) {
                bdcDyq = (BdcDyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcDyq = new BdcDyq();
        }
        if (bdcDyq != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcDyq.setProid(projectPar.getBdcXm().getProid());
                    bdcDyq.setYwh(projectPar.getBdcXm().getBh());
                    bdcDyq.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcDyq.setBdcdyid(projectPar.getBdcdyid());
                }
            }
            if (StringUtils.isBlank(bdcDyq.getQlid())) {
                bdcDyq.setQlid(UUIDGenerator.generate18());
            }
            bdcDyq.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcDyq);
            }
        }
        return qllxVoList;
    }

    @Override
    public List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        return null;
    }


    @Override
    public List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        return null;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_dyq";
    }
}
