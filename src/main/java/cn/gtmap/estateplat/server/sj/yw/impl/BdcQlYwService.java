package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcFwfzxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.sj.InterfaceCodeBeanFactory;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.ql.impl.BdcNoQlDqServiceImpl;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/18 0018
 * @description
 */
@Service
public class BdcQlYwService implements BdcDataYwService {
    @Autowired
    private Set<BdcQlDqService> bdcQlDqServiceList;
    @Autowired
    private QllxService qllxService;
    @Resource(name = "bdcNoQlDqServiceImpl")
    BdcNoQlDqServiceImpl bdcNoQlDqServiceImpl;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private EntityMapper entityMapper;

    /**
     * @param projectPar
     * @param insertVoList
     * @return 不动产数据
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化获取不动产数据
     */
    @Override
    public List<InsertVo> initbdcData(ProjectPar projectPar, List<InsertVo> insertVoList) {
        if (CollectionUtils.isEmpty(insertVoList)) {
            insertVoList = Lists.newArrayList();
        }
        QllxVo qllxVo = qllxService.makeSureQllx(projectPar);
        BdcQlDqService bdcQlDqService = InterfaceCodeBeanFactory.getBean(bdcQlDqServiceList, qllxService.getTableName(qllxVo));
        //注销登记不生成权利
        if (CommonUtil.indexOfStrs(Constants.DJLX_DY_ZXDJ_SQLXDM, projectPar.getSqlx())) {
            bdcQlDqService = bdcNoQlDqServiceImpl;
        }
        if (bdcQlDqService != null) {
            List<QllxVo> qllxVoList = Lists.newArrayList();
            BdcXtYwmx bdcXtYwmx = projectPar.getBdcXtYwmx();
            if (bdcXtYwmx != null && StringUtils.isNotBlank(bdcXtYwmx.getQldq())) {
                String qldqs = bdcXtYwmx.getQldq();
                String[] qldqArray = StringUtils.split(qldqs, ",");
                if (qldqArray != null && qldqArray.length > 0) {
                    for (int i = 0; i < qldqArray.length; i++) {
                        String qldq = qldqArray[i];
                        switch (qldq) {
                            case "sys":
                                qllxVoList = readQlFromSys(projectPar, qllxVoList);
                                break;
                            case "xm":
                                qllxVoList = bdcQlDqService.readQlFormXm(projectPar, qllxVoList);
                                break;
                            case "qj":
                                qllxVoList = bdcQlDqService.readQlFormQj(projectPar, qllxVoList);
                                break;
                            case "yg":
                                qllxVoList = bdcQlDqService.readQlFormYg(projectPar, qllxVoList);
                                break;
                        }

                    }
                }
            }

            insertVoList.addAll(qllxVoList);
        }
        return insertVoList;
    }

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 读取上一手权利
     */
    private List<QllxVo> readQlFromSys(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        if (projectPar != null && projectPar.getBdcXm() != null) {
            QllxVo qllxVo = qllxService.makeSureQllx(projectPar.getBdcXm());
            String yqlid = "";
            if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList()) && StringUtils.isNotBlank(projectPar.getBdcXmRelList().get(0).getYproid())) {
                BdcXm ybdcxm = bdcXmService.getBdcXmByProid(projectPar.getBdcXmRelList().get(0).getYproid());
                QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
                if (yqllxVo != null && qllxVo.getClass().equals(yqllxVo.getClass())) {
                    yqlid = yqllxVo.getQlid();
                    yqllxVo.setQlid(UUIDGenerator.generate18());
                    yqllxVo.setProid(projectPar.getProid());
                    yqllxVo.setYwh(projectPar.getBh());
                    yqllxVo.setDbr(null);
                    yqllxVo.setDjsj(null);
                    yqllxVo.setQszt(Constants.QLLX_QSZT_LS);
                    //是否继承上一手附记
                    String isJcFj = AppConfig.getProperty("isJcFj");
                    if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcFj, ParamsConstants.FALSE_LOWERCASE)) {
                        yqllxVo.setFj("");
                    }
                    //是否继承上一手权利其他状况
                    String isJcQlqtzk = AppConfig.getProperty("isJcQlqtzk");
                    if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcQlqtzk, ParamsConstants.FALSE_LOWERCASE)) {
                        yqllxVo.setQlqtzk("");
                    }
                    qllxVo = yqllxVo;
                    qllxVoList.clear();
                    qllxVoList.add(qllxVo);
                }
            }
            //房地一体
            if (qllxVo instanceof BdcFdcq && CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList()) && projectPar.getBdcXmRelList().size() > 1) {
                BdcJsydzjdsyq bdcJsydzjdsyq = new BdcJsydzjdsyq();
                BdcFdcq bdcFdcq = new BdcFdcq();
                for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    QllxVo yqllxVoTemp = qllxService.queryQllxVo(ybdcxm);
                    if (yqllxVoTemp instanceof BdcFdcq) {
                        bdcFdcq = (BdcFdcq) yqllxVoTemp;
                    }
                    if (yqllxVoTemp instanceof BdcJsydzjdsyq) {
                        bdcJsydzjdsyq = (BdcJsydzjdsyq) yqllxVoTemp;
                    }
                }
                bdcFdcq.setQlid(UUIDGenerator.generate18());
                bdcFdcq.setProid(projectPar.getProid());
                bdcFdcq.setYwh(projectPar.getBh());
                bdcFdcq.setDjsj(null);
                bdcFdcq.setJyjg(null);
                bdcFdcq.setFdcjyhth(null);
                bdcFdcq.setDbr(null);
                bdcFdcq.setQszt(Constants.QLLX_QSZT_LS);
                //是否继承上一手附记
                String isJcFj = AppConfig.getProperty("isJcFj");
                if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcFj, ParamsConstants.FALSE_LOWERCASE)) {
                    bdcFdcq.setFj("");
                }
                //是否继承上一手权利其他状况
                String isJcQlqtzk = AppConfig.getProperty("isJcQlqtzk");
                if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcQlqtzk, ParamsConstants.FALSE_LOWERCASE)) {
                    bdcFdcq.setQlqtzk("");
                }
                if ((bdcFdcq.getFttdmj() == null || bdcFdcq.getFttdmj() == 0) && (bdcJsydzjdsyq.getFttdmj() != null && bdcJsydzjdsyq.getFttdmj() > 0)) {
                    bdcFdcq.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                }
                if ((bdcFdcq.getDytdmj() == null || bdcFdcq.getDytdmj() == 0) && (bdcJsydzjdsyq.getDytdmj() != null && bdcJsydzjdsyq.getDytdmj() > 0)) {
                    bdcFdcq.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                }
                if (bdcFdcq.getTdsyksqx() == null && bdcJsydzjdsyq.getSyksqx() != null) {
                    bdcFdcq.setTdsyksqx(bdcJsydzjdsyq.getSyksqx());
                }
                if (bdcFdcq.getTdsyjsqx() == null && bdcJsydzjdsyq.getSyjsqx() != null) {
                    bdcFdcq.setTdsyjsqx(bdcJsydzjdsyq.getSyjsqx());
                }
                qllxVoList.clear();
                qllxVoList.add(bdcFdcq);
            } else if (qllxVo instanceof BdcFdcqDz && CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList()) && projectPar.getBdcXmRelList().size() > 1) {
                BdcJsydzjdsyq bdcJsydzjdsyq = new BdcJsydzjdsyq();
                BdcFdcqDz bdcFdcqDz = new BdcFdcqDz();
                for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    QllxVo yqllxVoTemp = qllxService.queryQllxVo(ybdcxm);
                    if (yqllxVoTemp instanceof BdcFdcqDz) {
                        bdcFdcqDz = (BdcFdcqDz) yqllxVoTemp;
                    }
                    if (yqllxVoTemp instanceof BdcFdcq) {
                        BdcFdcq bdcFdcq=(BdcFdcq) yqllxVoTemp;
                        bdcFdcqDz.setBdcdyid(bdcFdcq.getBdcdyid());
                    }
                    if (yqllxVoTemp instanceof BdcJsydzjdsyq) {
                        bdcJsydzjdsyq = (BdcJsydzjdsyq) yqllxVoTemp;
                    }
                }
                bdcFdcqDz.setQlid(UUIDGenerator.generate18());
                bdcFdcqDz.setProid(projectPar.getProid());
                bdcFdcqDz.setYwh(projectPar.getBh());
                bdcFdcqDz.setDjsj(null);
                bdcFdcqDz.setDbr(null);
                bdcFdcqDz.setFdcjyjg(null);
                bdcFdcqDz.setFdcjyhth(null);
                bdcFdcqDz.setQszt(Constants.QLLX_QSZT_LS);
                //是否继承上一手附记
                String isJcFj = AppConfig.getProperty("isJcFj");
                if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcFj, ParamsConstants.FALSE_LOWERCASE)) {
                    bdcFdcqDz.setFj("");
                }
                //是否继承上一手权利其他状况
                String isJcQlqtzk = AppConfig.getProperty("isJcQlqtzk");
                if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcQlqtzk, ParamsConstants.FALSE_LOWERCASE)) {
                    bdcFdcqDz.setQlqtzk("");
                }
                if ((bdcFdcqDz.getFttdmj() == null || bdcFdcqDz.getFttdmj() == 0) && (bdcJsydzjdsyq.getFttdmj() != null && bdcJsydzjdsyq.getFttdmj() > 0)) {
                    bdcFdcqDz.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                }
                if ((bdcFdcqDz.getDytdmj() == null || bdcFdcqDz.getDytdmj() == 0) && (bdcJsydzjdsyq.getDytdmj() != null && bdcJsydzjdsyq.getDytdmj() > 0)) {
                    bdcFdcqDz.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                }
                if (bdcFdcqDz.getTdsyksqx() == null && bdcJsydzjdsyq.getSyksqx() != null) {
                    bdcFdcqDz.setTdsyksqx(bdcJsydzjdsyq.getSyksqx());
                }
                if (bdcFdcqDz.getTdsyjsqx() == null && bdcJsydzjdsyq.getSyjsqx() != null) {
                    bdcFdcqDz.setTdsyjsqx(bdcJsydzjdsyq.getSyjsqx());
                }
                qllxVoList.clear();
                qllxVoList.add(bdcFdcqDz);
            } else if (qllxVo instanceof BdcDyaq && CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList()) && projectPar.getBdcXmRelList().size() > 1) {
                BdcJsydzjdsyq bdcJsydzjdsyq = new BdcJsydzjdsyq();
                BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    QllxVo yqllxVoTemp = qllxService.queryQllxVo(ybdcxm);
                    if (yqllxVoTemp instanceof BdcJsydzjdsyq) {
                        bdcJsydzjdsyq = (BdcJsydzjdsyq) yqllxVoTemp;
                    }
                }
                if (bdcJsydzjdsyq != null) {
                    if ((bdcDyaq.getFttdmj() == null || bdcDyaq.getFttdmj() == 0) && (bdcJsydzjdsyq.getFttdmj() != null && bdcJsydzjdsyq.getFttdmj() > 0)) {
                        bdcDyaq.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                    }
                    if ((bdcDyaq.getDytdmj() == null || bdcDyaq.getDytdmj() == 0) && (bdcJsydzjdsyq.getDytdmj() != null && bdcJsydzjdsyq.getDytdmj() > 0)) {
                        bdcDyaq.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                    }
                }
            }
            if (qllxVo instanceof BdcFdcqDz) {
                BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                List<BdcFwfzxx> bdcFwfzxxList = bdcFwfzxxService.getBdcFwfzxxListByQlid(yqlid);
                if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
                    for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                        bdcFwfzxx.setFzid(UUIDGenerator.generate18());
                        bdcFwfzxx.setQlid(bdcFdcqDz.getQlid());
                        entityMapper.saveOrUpdate(bdcFwfzxx, bdcFwfzxx.getFzid());
                    }
                    bdcFdcqDz.setFwfzxxList(bdcFwfzxxList);
                    qllxVoList.clear();
                    qllxVoList.add(bdcFdcqDz);
                }
            }
        }
        return qllxVoList;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_ql";
    }

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 顺序号
     */
    @Override
    public Integer getSxh() {
        return 5;
    }
}
