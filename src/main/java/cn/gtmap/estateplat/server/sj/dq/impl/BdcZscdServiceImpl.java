package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcBdcZsSdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsCdService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
  * @Time 2020/7/28 14:32
  * @description
  */
@Service
public class BdcZscdServiceImpl implements BdcQtDqService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsCdService bdcZsCdService;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (StringUtils.isNotBlank(projectPar.getYxmid())) {
            if (StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_ZSCDCX)) {
                if (StringUtils.isNotBlank(projectPar.getYxmid())) {
                    BdcXm yBdcXm = bdcXmService.getBdcXmByProid(projectPar.getYxmid());
                    if (yBdcXm != null && StringUtils.isNotBlank(yBdcXm.getWiid())) {
                        Map paramMap = new HashMap();
                        paramMap.put("cdxmid", yBdcXm.getProid());
                        List<BdcZsCd> bdcZsCdList=bdcZsCdService.getBdcZscdList(paramMap);
                        if (CollectionUtils.isNotEmpty(bdcZsCdList)) {
                            for (BdcZsCd bdcZsCd : bdcZsCdList) {
                                bdcZsCd.setJccdxmid(projectPar.getProid());
                                if (projectPar.getBdcXm() != null && StringUtils.isNotBlank(projectPar.getBdcXm().getBh())) {
                                    bdcZsCd.setJccdywh(projectPar.getBdcXm().getBh());
                                }
                                bdcZsCd.setCdzt(Constants.ISCD_NEGETIVE);
                                entityMapper.saveOrUpdate(bdcZsCd, bdcZsCd.getCdid());
                            }
                        }
                    }
                }
            } else {
                if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                    for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmRel.getYproid());
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                BdcZsCd bdcZsCd = new BdcZsCd();
                                bdcZsCd.setCdid(UUIDGenerator.generate18());
                                bdcZsCd.setBdcdyh(projectPar.getBdcdyh());
                                bdcZsCd.setBdcdyid(projectPar.getBdcdyid());
                                bdcZsCd.setCqzh(bdcZs.getBdcqzh());
                                bdcZsCd.setProid(projectPar.getYxmid());
                                bdcZsCd.setCdxmid(projectPar.getProid());
                                bdcZsCd.setCdjbr(PlatformUtil.getCurrentUserName(PlatformUtil.getCurrentUserId()));
                                bdcZsCd.setCdsj(new Date());
                                bdcZsCd.setCdzt(Constants.ISCD_POSITIVE);
                                if (projectPar.getBdcXm() != null && StringUtils.isNotBlank(projectPar.getBdcXm().getBh())) {
                                    bdcZsCd.setCdywh(projectPar.getBdcXm().getBh());
                                }
                                if (CollectionUtils.isNotEmpty(projectPar.getBdcQlrList())) {
                                    String bxzr = "";
                                    for (BdcQlr bdcQlr : projectPar.getBdcQlrList()) {
                                        if (StringUtils.isNotBlank(bxzr)) {
                                            bxzr += "," + bdcQlr.getQlrmc();
                                        } else {
                                            bxzr = bdcQlr.getQlrmc();
                                        }
                                    }
                                    if (StringUtils.isNotBlank(bxzr)) {
                                        bdcZsCd.setCdzyr(bxzr);
                                    }
                                }
                                entityMapper.saveOrUpdate(bdcZsCd, bdcZsCd.getCdid());
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "zscd";
    }
}
