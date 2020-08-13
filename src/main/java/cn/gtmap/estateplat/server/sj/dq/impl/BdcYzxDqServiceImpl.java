package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.sj.dq.BdcQlrDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcQtDqService;
import cn.gtmap.estateplat.server.sj.dq.BdcYwrDqService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/4/19
 * @description 预转现信息特殊读取
 */
@Service
public class BdcYzxDqServiceImpl implements BdcQtDqService {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;
    @Resource(name = "bdcDataDqYzServiceImpl")
    private BdcQlrDqService bdcDataQlrYzServiceImpl;
    @Resource(name = "bdcDataDqYzServiceImpl")
    private BdcYwrDqService bdcDataYwrYzServiceImpl;
    @Resource(name = "creatProjectDefaultService")
    private CreatProjectService creatProjectService;

    @Override
    public void createQtxx(ProjectPar projectPar) {
        List<InsertVo> insertVoList = Lists.newArrayList();
        if (projectPar != null) {
            if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                List<String> yxscqproidList = bdcXmService.getXsCqProidBybdcdyid(projectPar.getBdcdyid());
                if (CollectionUtils.isNotEmpty(yxscqproidList)) {
                    String xsCqProid = yxscqproidList.get(0);
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(xsCqProid);
                    //预转现坐落取首次登记坐落
                    if (bdcSpxx != null && projectPar.getBdcSpxx() != null) {
                        if (StringUtils.isNotBlank(bdcSpxx.getZl())) {
                            projectPar.getBdcSpxx().setZl(bdcSpxx.getZl());
                            entityMapper.saveOrUpdate(projectPar.getBdcSpxx(), projectPar.getBdcSpxx().getSpxxid());
                        }
                    }
                }
                List<QllxVo> bdcYgList = qllxService.getQllxByBdcdyh(new BdcYg(), projectPar.getBdcdyh());
                if (CollectionUtils.isNotEmpty(bdcYgList)) {
                    for (QllxVo bdcYg : bdcYgList) {
                        if (projectPar.getSx() == 1 && Constants.YGDJZL_MM.contains(((BdcYg) bdcYg).getYgdjzl())) {
                            projectPar.setYxmid(bdcYg.getProid());
                            List<BdcQlr> bdcQlrList = bdcDataQlrYzServiceImpl.getCreateQlr(projectPar);
                            List<BdcQlr> bdccYwrList = bdcDataYwrYzServiceImpl.getCreateYwr(projectPar);
                            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                insertVoList.addAll(bdcQlrList);
                            }
                            if (CollectionUtils.isNotEmpty(bdccYwrList)) {
                                insertVoList.addAll(bdccYwrList);
                            }
                        }
                        if (projectPar.getSx() == 2 && Constants.YGDJZL_DY.contains(((BdcYg) bdcYg).getYgdjzl())) {
                            projectPar.setYxmid(bdcYg.getProid());
                            List<BdcQlr> bdcQlrList = bdcDataQlrYzServiceImpl.getCreateQlr(projectPar);
                            List<BdcQlr> bdccYwrList = bdcDataYwrYzServiceImpl.getCreateYwr(projectPar);
                            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                insertVoList.addAll(bdcQlrList);
                            }
                            if (CollectionUtils.isNotEmpty(bdccYwrList)) {
                                insertVoList.addAll(bdccYwrList);
                            }
                        }
                    }
                }
            }
        }
        creatProjectService.insertProjectData(insertVoList);
    }

    @Override
    public String getIntetfacaCode() {
        return "yzx";
    }
}
