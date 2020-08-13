package cn.gtmap.estateplat.server.sj.dq.impl;

import cn.gtmap.estateplat.model.server.core.BdcBdcdjb;
import cn.gtmap.estateplat.model.server.core.BdcTd;
import cn.gtmap.estateplat.model.server.core.BdcXtConfig;
import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.server.core.service.BdcTdService;
import cn.gtmap.estateplat.server.core.service.BdcXtConfigService;
import cn.gtmap.estateplat.server.core.service.BdcdjbService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.BdcTsYwService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-28
 * @description 登记簿特殊处理
 */
@Service
public class BdcTsYwDjbTdServiceImpl implements BdcTsYwService {
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    private BdcXtConfigService bdcXtConfigService;
    private String[] QLLX_NYDXX = {"9", "10", "11"};
    private String[] QLLX_ZDXX = {"1", "2", "3", "5", "7"};
    private String[] QLLX_FWXX = {"4", "6", "8"};
    private String[] QLLX_LQXX = {"10", "12"};
    private String[] QLLX_ZHXX = {"13", "14", "15", "16"};

    /**
     * @param projectParList 流程参数类
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化特殊业务
     */
    @Override
    public List<InsertVo> initTsYw(List<ProjectPar> projectParList) {
        List<InsertVo> insertVoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(projectParList)) {
            Map<String, BdcBdcdjb> djbMap = Maps.newHashMap();
            Map<String, BdcTd> tdmap = Maps.newHashMap();
            for (ProjectPar projectPar : projectParList) {
                if (StringUtils.isNotBlank(projectPar.getDjh())) {
                    String djh = projectPar.getDjh();
                    if (djbMap.containsKey(djh)) {
                        BdcBdcdjb bdcBdcdjb = djbMap.get(djh);
                        projectPar.setDjbid(bdcBdcdjb.getDjbid());
                    } else {
                        BdcBdcdjb bdcBdcdjb = initBdcdjb(projectPar);
                        if (bdcBdcdjb != null) {
                            djbMap.put(djh, bdcBdcdjb);
                            insertVoList.add(bdcBdcdjb);
                        }
                    }
                    if (!tdmap.containsKey(djh)) {
                        BdcTd bdcTd = initBdcTd(projectPar);
                        if (bdcTd != null) {
                            tdmap.put(djh, bdcTd);
                            insertVoList.add(bdcTd);
                        }
                    }
                }
            }
        }
        return insertVoList;
    }


    private BdcBdcdjb initBdcdjb(ProjectPar projectPar) {
        BdcBdcdjb bdcBdcdjb = null;
        List<BdcBdcdjb> bdcBdcdjbList = bdcDjbService.selectBdcdjb(projectPar.getDjh());
        if (CollectionUtils.isEmpty(bdcBdcdjbList)) {
            bdcBdcdjb = bdcDjbService.getBdcdjbFromProjectPar(projectPar, bdcBdcdjb);
            if (CommonUtil.indexOfStrs(QLLX_NYDXX, projectPar.getQllx())) {
                if (projectPar.getDjsjQszdDcb() == null) {
                    projectParService.getQjsj(projectPar, "djsjQszdDcb");
                }
                if (CollectionUtils.isEmpty(projectPar.getDjsjNydDcbList())) {
                    projectParService.getQjsj(projectPar, "djsjNydDcbList");
                }
                bdcBdcdjb = bdcDjbService.getBdcdjbFromQsdcb(projectPar.getDjsjQszdDcb(), bdcBdcdjb);
                bdcBdcdjb = bdcDjbService.getBdcdjbFromNydZdxx(projectPar.getDjsjNydDcbList(), bdcBdcdjb);
            }
            if (CommonUtil.indexOfStrs(QLLX_ZDXX, projectPar.getQllx())) {
                if (projectPar.getDjsjZdxx() == null) {
                    projectParService.getQjsj(projectPar, "djsjZdxx");
                }
                bdcBdcdjb = bdcDjbService.getBdcdjbFromZdxx(projectPar.getDjsjZdxx(), bdcBdcdjb);
            }
            if (CommonUtil.indexOfStrs(QLLX_FWXX, projectPar.getQllx())) {
                if (projectPar.getDjsjFwxx() == null) {
                    projectParService.getQjsj(projectPar, "djsjFwxx");
                }
                bdcBdcdjb = bdcDjbService.getBdcdjbFromFwxx(projectPar.getDjsjFwxx(), bdcBdcdjb);
            }
            if (CommonUtil.indexOfStrs(QLLX_LQXX, projectPar.getQllx())) {
                if (projectPar.getDjsjLqxx() == null) {
                    projectParService.getQjsj(projectPar, "djsjLqxx");
                }
                bdcBdcdjb = bdcDjbService.getBdcdjbFromLqxx(projectPar.getDjsjLqxx(), bdcBdcdjb);
            }
            if (CommonUtil.indexOfStrs(QLLX_ZHXX, projectPar.getQllx())) {
                if (projectPar.getDjsjZhxx() == null) {
                    projectParService.getQjsj(projectPar, "djsjZhxx");
                }
                bdcBdcdjb = bdcDjbService.getBdcdjbFromZhxx(projectPar.getDjsjZhxx(), bdcBdcdjb);
            }
            if (StringUtils.isBlank(bdcBdcdjb.getDjjg())) {
                if (StringUtils.isNotBlank(projectPar.getDwdm())) {
                    BdcXtConfig bdcXtConfig = bdcXtConfigService.queryBdcXtConfigByDwdm(projectPar.getDwdm());
                    if (bdcXtConfig != null && StringUtils.isNotBlank(bdcXtConfig.getDjjg())) {
                        bdcBdcdjb.setDjjg(bdcXtConfig.getDjjg());
                    }
                }
            }
        } else {
            bdcBdcdjb = bdcBdcdjbList.get(0);
        }
        if (StringUtils.isBlank(bdcBdcdjb.getDjjg())) {
            if (StringUtils.isNotBlank(projectPar.getDwdm())) {
                BdcXtConfig bdcXtConfig = bdcXtConfigService.queryBdcXtConfigByDwdm(projectPar.getDwdm());
                if (bdcXtConfig != null && StringUtils.isNotBlank(bdcXtConfig.getDjjg())) {
                    bdcBdcdjb.setDjjg(bdcXtConfig.getDjjg());
                }
            }
        }
        projectPar.setBdcBdcdjb(bdcBdcdjb);
        projectPar.setDjbid(bdcBdcdjb.getDjbid());
        return bdcBdcdjb;
    }

    private BdcTd initBdcTd(ProjectPar projectPar) {
        BdcTd bdcTd = bdcTdService.selectBdcTd(projectPar.getDjh());
        if (bdcTd == null) {
            if (CommonUtil.indexOfStrs(QLLX_ZDXX, projectPar.getQllx()) || CommonUtil.indexOfStrs(QLLX_FWXX, projectPar.getQllx())||StringUtils.isBlank(projectPar.getQllx())) {
                if (projectPar.getDjsjZdxx() == null) {
                    projectParService.getQjsj(projectPar, "djsjZdxx");
                }
                bdcTd = bdcTdService.getBdcTdFromDjsjZdxx(projectPar.getDjsjZdxx(), projectPar);
            }
            if (CommonUtil.indexOfStrs(QLLX_NYDXX, projectPar.getQllx())) {
                if (CollectionUtils.isEmpty(projectPar.getDjsjQszdDcbList())) {
                    projectParService.getQjsj(projectPar, "djsjQszdDcbList");
                }
                if (CollectionUtils.isEmpty(projectPar.getDjsjNydDcbList())) {
                    projectParService.getQjsj(projectPar, "djsjNydDcbList");
                }
                bdcTd = bdcTdService.getBdcTdFromQszdDcb(projectPar.getDjsjQszdDcbList(), projectPar);
                bdcTd = bdcTdService.getBdcTdFromNydDcb(projectPar.getDjsjNydDcbList(), projectPar);
            }
        }
        if (bdcTd != null) {
            projectPar.setBdcTd(bdcTd);
        }
        return bdcTd;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "djb,td";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 1;
    }
}
