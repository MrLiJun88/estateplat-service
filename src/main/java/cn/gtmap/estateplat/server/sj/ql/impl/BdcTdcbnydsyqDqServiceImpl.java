package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
import cn.gtmap.estateplat.model.server.core.DjsjCbzdCbf;
import cn.gtmap.estateplat.model.server.core.DjsjCbzdFbf;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/21
 * @description 土地承包经营权、农用地使用权
 */
@Service
public class BdcTdcbnydsyqDqServiceImpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcDjsjService bdcDjsjService;

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcTdcbnydsyq bdcTdcbnydsyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcTdcbnydsyq) {
                bdcTdcbnydsyq = (BdcTdcbnydsyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcTdcbnydsyq = new BdcTdcbnydsyq();
        }
        if (bdcTdcbnydsyq != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcTdcbnydsyq.setProid(projectPar.getBdcXm().getProid());
                    bdcTdcbnydsyq.setYwh(projectPar.getBdcXm().getBh());
                    bdcTdcbnydsyq.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcTdcbnydsyq.setBdcdyid(projectPar.getBdcdyid());
                }
            }
            if (StringUtils.isBlank(bdcTdcbnydsyq.getQlid())) {
                bdcTdcbnydsyq.setQlid(UUIDGenerator.generate18());
            }
            bdcTdcbnydsyq.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcTdcbnydsyq);
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
        BdcTdcbnydsyq bdcTdcbnydsyq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcTdcbnydsyq) {
                bdcTdcbnydsyq = (BdcTdcbnydsyq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcTdcbnydsyq = new BdcTdcbnydsyq();
        }
        if (bdcTdcbnydsyq != null) {
            if (projectPar.getDjsjCbzdDcb() == null) {
                projectParService.getQjsj(projectPar, "djsjCbzdDcb");
            }
            if (projectPar.getDjsjCbzdDcb() != null) {
                List<DjsjCbzdCbf> djsjCbzdCbfList = null;
                if (StringUtils.isNotBlank(projectPar.getDjsjCbzdDcb().getZddcbIndex())) {
                    djsjCbzdCbfList = bdcDjsjService.getDjsjCbzdCbfByDcbid(projectPar.getDjsjCbzdDcb().getZddcbIndex());
                }
                List<DjsjCbzdFbf> djsjCbzdFbfList = null;
                if (StringUtils.isNotBlank(projectPar.getDjsjCbzdDcb().getZddcbIndex())) {
                    djsjCbzdFbfList = bdcDjsjService.getDjsjCbzdFbfByDcbid(projectPar.getDjsjCbzdDcb().getZddcbIndex());
                }
                bdcTdcbnydsyq = qllxService.getBdcTdcbnydsyqFromTdcb(projectPar.getDjsjCbzdDcb(), bdcTdcbnydsyq, djsjCbzdCbfList, djsjCbzdFbfList);
            }
            if (isAdd) {
                qllxVoList.add(bdcTdcbnydsyq);
            }
        }
        return qllxVoList;
    }

    @Override
    public List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        return null;
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_tdcbnydsyq";
    }
}
