package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmcqRel;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXmcqRelService;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-03
 * @description 项目产权关系表生成
 */
@Service
public class BdcQtXmcqRelDqService implements BdcQtDqService {
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmcqRelService bdcXmcqRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (projectPar != null && StringUtils.isNotBlank(projectPar.getBdcdyid())) {
            List<String> proidList = bdcXmService.getXsCqProid(projectPar.getBdcdyid());
            if (CollectionUtils.isNotEmpty(proidList)) {
                List<BdcXmcqRel> bdcXmcqRelList = Lists.newArrayList();
                for (String proid : proidList) {
                    BdcXmcqRel bdcXmcqRel = new BdcXmcqRel();
                    bdcXmcqRel.setRelid(UUIDGenerator.generate18());
                    bdcXmcqRel.setProid(projectPar.getProid());
                    bdcXmcqRel.setCqproid(proid);
                    bdcXmcqRelList.add(bdcXmcqRel);
                }
                bdcXmcqRelService.saveBdcXmcqRel(bdcXmcqRelList);

                if (CollectionUtils.isNotEmpty(bdcXmcqRelList)) {
                    BdcXm bdcXm = projectPar.getBdcXm();
                    String sqlxdm = StringUtils.EMPTY;
                    //获取平台的申请类型代码,主要为了合并
                    if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                        if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                            sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                        }
                    }
                    if (!CommonUtil.indexOfStrs(Constants.SQLX_INITYBDCQZH_NO, sqlxdm) && !StringUtils.equals(Constants.QLLX_DYAQ, bdcXm.getQllx())) {
                        String ybdcqzh = bdcXm.getYbdcqzh();
                        for (BdcXmcqRel bdcXmcqRel : bdcXmcqRelList) {
                            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmcqRel.getCqproid());
                            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                                for (BdcZs bdcZs : bdcZsList) {
                                    String[] ybdcqzhs = StringUtils.split(ybdcqzh, ",");
                                    if (!CommonUtil.indexOfStrs(ybdcqzhs, bdcZs.getBdcqzh())) {
                                        if (StringUtils.isNotBlank(ybdcqzh)) {
                                            ybdcqzh += "," + bdcZs.getBdcqzh();
                                        } else {
                                            ybdcqzh = bdcZs.getBdcqzh();
                                        }
                                    }
                                }
                            }
                        }
                        bdcXm.setYbdcqzh(ybdcqzh);
                        entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                    }
                }
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
        return "xmcq";
    }
}
