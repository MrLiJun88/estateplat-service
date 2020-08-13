package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/5/1 0001
 * @description
 */
@Service
public class BdcDatabcsjService implements BdcDataYwService {
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private SyncRyzdService syncRyzdService;

    /**
     * @param projectPar
     * @param insertVoList
     * @return 不动产数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化获取不动产数据
     */
    @Override
    public List<InsertVo> initbdcData(ProjectPar projectPar, List<InsertVo> insertVoList) {
        if (projectPar != null && StringUtils.isNotBlank(projectPar.getProid()) && StringUtils.isNotBlank(projectPar.getBdcdyh())) {
            boolean sfYdhDfw = bdcdyService.sfYdhDfw(projectPar.getBdcdyh());
            if (sfYdhDfw) {
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(projectPar.getProid());
                if (bdcSpxx != null) {
                    projectPar.setBdcSpxx(bdcSpxx);
                }
                if (projectPar.isSfdyYzh()) {
                    initYbdcqzh(projectPar);
                }
                initMj(projectPar);
            }
            syncRyzdService.synchronizationField(projectPar.getWiid());
        }
        return insertVoList;
    }

    private void initYbdcqzh(ProjectPar projectPar) {
        if (projectPar != null && CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(projectPar.getProid());
            if (bdcXm != null) {
                if (!CommonUtil.indexOfStrs(Constants.SQLX_INITYBDCQZH_NO, bdcXm.getSqlx())) {
                    String ybdcqz = "";
                    for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmRel.getYproid());
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                if (StringUtils.isBlank(ybdcqz)) {
                                    ybdcqz = bdcZs.getBdcqzh();
                                } else {
                                    ybdcqz += "," + bdcZs.getBdcqzh();
                                }
                            }
                        }
                    }
                    bdcXm.setYbdcqzh(ybdcqz);
                    bdcXmService.saveBdcXm(bdcXm);
                }
            }
        }
    }

    private void initMj(ProjectPar projectPar) {
        if (projectPar != null && CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList()) && projectPar.getBdcXmRelList().size() > 1 && projectPar.getBdcSpxx() != null) {
            Double fwmj = 0.0;
            Double tdsyqmj = 0.0;
            Double fttdmj = 0.0;
            Double dytdmj = 0.0;
            for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                if (ybdcXm != null) {
                    QllxVo yQllxVo = qllxService.queryQllxVo(ybdcXm);
                    if (yQllxVo != null && yQllxVo instanceof BdcFdcq) {
                        BdcFdcq bdcFdcq = (BdcFdcq) yQllxVo;
                        if (bdcFdcq.getJzmj() != null) {
                            fwmj += bdcFdcq.getJzmj();
                        }
                    }
                    if (yQllxVo != null && yQllxVo instanceof BdcFdcqDz) {
                        BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) yQllxVo;
                        List<BdcFwfzxx> bdcFwfzxxList = bdcFwfzxxService.queryBdcFwfzxxListByProid(bdcFdcqDz.getProid());
                        if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
                            for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                                if (bdcFwfzxx.getJzmj() != null) {
                                    fwmj += bdcFwfzxx.getJzmj();
                                }
                            }
                        }
                    }
                    if (yQllxVo != null && yQllxVo instanceof BdcJsydzjdsyq) {
                        BdcJsydzjdsyq bdcJsydzjdsyq = (BdcJsydzjdsyq) yQllxVo;
                        if (bdcJsydzjdsyq != null && bdcJsydzjdsyq.getSyqmj() != null) {
                            tdsyqmj += bdcJsydzjdsyq.getSyqmj();
                            if (bdcJsydzjdsyq.getFttdmj() != null) {
                                fttdmj += bdcJsydzjdsyq.getFttdmj();
                            }
                            if (bdcJsydzjdsyq.getDytdmj() != null) {
                                dytdmj += bdcJsydzjdsyq.getDytdmj();
                            }
                        }
                    }
                }
            }
            if (fwmj > 0) {
                projectPar.getBdcSpxx().setMj(fwmj);
                projectPar.getBdcSpxx().setFzmj(fwmj);
                projectPar.getBdcSpxx().setScmj(fwmj);
            }
            //初始化空
            if (tdsyqmj == 0) {
                tdsyqmj = null;
            }
            projectPar.getBdcSpxx().setZdzhmj(tdsyqmj);
            bdcSpxxService.saveBdcSpxx(projectPar.getBdcSpxx());
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(projectPar.getProid());
            if (bdcXm != null) {
                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                if (qllxVo != null && qllxVo instanceof BdcDyaq) {
                    BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                    bdcDyaq.setFwdymj(fwmj);
                    bdcDyaq.setFttdmj(fttdmj);
                    bdcDyaq.setDytdmj(dytdmj);
                    qllxService.saveQllxVo(qllxVo);
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
        return "bcsj";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 7;
    }
}
