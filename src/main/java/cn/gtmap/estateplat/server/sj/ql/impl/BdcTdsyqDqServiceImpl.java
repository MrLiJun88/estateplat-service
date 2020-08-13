package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.BdcTdsyq;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/21
 * @description 土地所有权
 */
@Service
public class BdcTdsyqDqServiceImpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    @Resource(name = "dozerQlMapper")
    private DozerBeanMapper dozerMapper;

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcTdsyq bdcTdsyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcTdsyq) {
                bdcTdsyq = (BdcTdsyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcTdsyq = new BdcTdsyq();
        }
        if (bdcTdsyq != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcTdsyq.setProid(projectPar.getBdcXm().getProid());
                    bdcTdsyq.setYwh(projectPar.getBdcXm().getBh());
                    bdcTdsyq.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcTdsyq.setBdcdyid(projectPar.getBdcdyid());
                }
            }
            if (StringUtils.isBlank(bdcTdsyq.getQlid())) {
                bdcTdsyq.setQlid(UUIDGenerator.generate18());
            }
            bdcTdsyq.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcTdsyq);
            }
        }
        return qllxVoList;
    }

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取权利信息从权籍
     */
    @Override
    public List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcTdsyq bdcTdsyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcTdsyq) {
                bdcTdsyq = (BdcTdsyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcTdsyq = new BdcTdsyq();
        }
        if (bdcTdsyq != null) {
            if (projectPar.getBdcQszdZdmj() == null) {
                projectParService.getQjsj(projectPar, "djsjQszdZdmj");
            }
            if (projectPar.getBdcQszdZdmj() != null) {
                dozerMapper.map(projectPar.getBdcQszdZdmj(), bdcTdsyq);
            }
            if (isAdd) {
                qllxVoList.add(bdcTdsyq);
            }
        }
        return qllxVoList;
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
        return "bdc_tdsyq";
    }
}
