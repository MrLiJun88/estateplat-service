package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/24
 * @description
 */
@Service
public class BdcNoQlDqServiceImpl implements BdcQlDqService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 处理注销类流程
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        if (projectPar != null && projectPar.getBdcXm() != null) {
            List<BdcXm> ybdcXmList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                    if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                        if (ybdcXm != null) {
                            ybdcXmList.add(ybdcXm);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(ybdcXmList)) {
                for (BdcXm ybdcXm : ybdcXmList) {
                    if (ybdcXm != null && StringUtils.isNotBlank(ybdcXm.getProid())) {
                        QllxVo qllxVo = qllxService.makeSureQllx(ybdcXm);
                        qllxVo = qllxService.queryQllxVo(qllxVo, ybdcXm.getProid());
                        if (qllxVo != null) {
                            if (qllxVo instanceof BdcCf) {
                                BdcCf bdcCf = (BdcCf) qllxVo;
                                if (Constants.SQLX_JF_SFCZ.equals(projectPar.getBdcXm().getSqlx())){
                                    bdcCf.setCdywh(projectPar.getBdcXm().getBh());
                                }
                                bdcCf.setJfywh(projectPar.getBdcXm().getBh());
                                bdcCf.setJfsj(new Date());
                                entityMapper.updateByPrimaryKeySelective(bdcCf);
                            } else if (qllxVo instanceof BdcDyaq) {
                                BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                                bdcDyaq.setZxdyywh(projectPar.getBdcXm().getBh());
                                bdcDyaq.setZxdyyy(projectPar.getBdcXm().getDjyy());
                                entityMapper.updateByPrimaryKeySelective(bdcDyaq);
                            } else if (qllxVo instanceof BdcYy) {
                                BdcYy bdcYy = (BdcYy) qllxVo;
                                bdcYy.setZxyyh(projectPar.getBdcXm().getBh());
                                bdcYy.setZxyyyy(projectPar.getBdcXm().getDjyy());
                                entityMapper.updateByPrimaryKeySelective(bdcYy);
                            }
                        }
                    }
                }
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

    @Override
    public String getIntetfacaCode() {
        return "noql";
    }
}
