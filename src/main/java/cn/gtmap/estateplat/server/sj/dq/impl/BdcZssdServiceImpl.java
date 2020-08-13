package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcBdcZsSdService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
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

/**
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0, 2020-03-26
 * @description
 */
@Service
public class BdcZssdServiceImpl implements BdcQtDqService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcBdcZsSdService bdcBdcZsSdService;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        if (StringUtils.isNotBlank(projectPar.getYxmid())) {
            if (StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_JCXZ)) {
                if (StringUtils.isNotBlank(projectPar.getYxmid())) {
                    BdcXm yBdcXm = bdcXmService.getBdcXmByProid(projectPar.getYxmid());
                    if (yBdcXm != null && StringUtils.isNotBlank(yBdcXm.getWiid())) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("sdlcgzlid", yBdcXm.getWiid());
                        List<BdcBdcZsSd> bdcBdcZsSdList = bdcBdcZsSdService.getBdcZsSdList(hashMap);
                        if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                            for (BdcBdcZsSd bdcZsSd : bdcBdcZsSdList) {
                                bdcZsSd.setJslcgzlid(projectPar.getWiid());
                                if (projectPar.getBdcXm() != null && StringUtils.isNotBlank(projectPar.getBdcXm().getBh())) {
                                    bdcZsSd.setJsywh(projectPar.getBdcXm().getBh());
                                }
                                entityMapper.saveOrUpdate(bdcZsSd, bdcZsSd.getSdid());
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
                                BdcBdcZsSd bdcBdcZsSd = new BdcBdcZsSd();
                                bdcBdcZsSd.setSdid(UUIDGenerator.generate18());
                                bdcBdcZsSd.setCqzh(bdcZs.getBdcqzh());
                                bdcBdcZsSd.setZsid(bdcZs.getZsid());
                                bdcBdcZsSd.setBdclx(projectPar.getBdclx());
                                bdcBdcZsSd.setProid(projectPar.getYxmid());
                                bdcBdcZsSd.setSdlcgzlid(projectPar.getWiid());
                                bdcBdcZsSd.setSdr(PlatformUtil.getCurrentUserName(PlatformUtil.getCurrentUserId()));
                                bdcBdcZsSd.setSdsj(new Date());
                                bdcBdcZsSd.setXzzt(Constants.SDZT_SD);
                                if (projectPar.getBdcXm() != null && StringUtils.isNotBlank(projectPar.getBdcXm().getBh())) {
                                    bdcBdcZsSd.setSdywh(projectPar.getBdcXm().getBh());
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
                                        bdcBdcZsSd.setBzxr(bxzr);
                                    }
                                }
                                entityMapper.saveOrUpdate(bdcBdcZsSd, bdcBdcZsSd.getSdid());
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "zssd";
    }
}
