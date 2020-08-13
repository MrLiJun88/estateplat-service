package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/18 0018
 * @description
 */
@Service
public class BdcCfDqServiceImpl implements BdcQlDqService {
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcCf bdcCf = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcCf) {
                bdcCf = (BdcCf) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcCf = new BdcCf();
        }
        if (bdcCf != null) {
            //读取项目里的逻辑
            if (projectPar != null) {
                BdcXmRel bdcXmRel = null;
                BdcXm bdcXm = null;
                if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                    bdcXmRel = projectPar.getBdcXmRelList().get(0);
                }
                if (bdcXmRel != null) {
                    if (StringUtils.isBlank(bdcCf.getQlid())) {
                        bdcCf.setQlid(UUIDGenerator.generate18());
                    }
                    if (projectPar.getBdcXm() != null) {
                        bdcXm = projectPar.getBdcXm();
                        bdcCf.setBdcdyid(projectPar.getBdcXm().getBdcdyid());
                        bdcCf.setProid(projectPar.getBdcXm().getProid());
                        bdcCf.setYwh(projectPar.getBdcXm().getBh());
                        bdcCf.setQllx(projectPar.getBdcXm().getQllx());
                    }
                    if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        BdcCf bdcCfFromYxm = bdcCfService.selectCfByProid(bdcXmRel.getYproid());
                        //zdd 查封从原项目读取信息
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmRel.getYproid());
                        //zwq 续查封等获取查封的权利信息
                        bdcCf = qllxService.getBdcCfFromCf(bdcCf, bdcCfFromYxm, bdcQlrList, bdcXm, bdcXmRel);

                    } else if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        List<BdcCf> bdcCfList = bdcCfService.getCfByBdcdyid(bdcCf.getBdcdyid());
                        if (CollectionUtils.isEmpty(bdcCfList)) {
                            bdcCf.setCflx(Constants.CFLX_ZD_YCF);
                        } else {
                            bdcCf.setCflx(Constants.CFLX_ZD_LHYCF);
                        }
                        //jiangganzhi 关联附属设施 附属设施登记子项要跟随主房登记子项
                        HashMap map = new HashMap();
                        map.put("wiid", bdcXm.getWiid());
                        List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(map);
                        if (CollectionUtils.isNotEmpty(bdcXmList)) {
                            BdcXm zfBdcXm = bdcXmList.get(0);
                            BdcCf zfBdcCf = bdcCfService.selectCfByProid(zfBdcXm.getProid());
                            if (zfBdcCf != null && StringUtils.isNotBlank(zfBdcCf.getCflx()) && !StringUtils.equals(zfBdcCf.getCflx(), Constants.CFLX_ZD_YCF)) {
                                bdcCf.setCflx(zfBdcCf.getCflx());
                            }
                        }
                        //当查封类型是预查封时，被查封权利人继承收件单上权利人
                        String bzxr = "";
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmRel.getProid());
                        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                            for (int i = 0; i < bdcQlrList.size(); i++) {
                                if (i == 0) {
                                    bzxr = bdcQlrList.get(i).getQlrmc();
                                } else {
                                    if (StringUtils.isNotBlank(bzxr) && StringUtils.isNotBlank(bdcQlrList.get(i).getQlrmc())) {
                                        bzxr = bzxr + "、" + bdcQlrList.get(i).getQlrmc();
                                    } else if (StringUtils.isBlank(bzxr)) {
                                        bzxr = bdcQlrList.get(i).getQlrmc();
                                    }
                                }
                            }
                        }
                        bdcCf.setBzxr(bzxr);
                    }
                    if (StringUtils.isBlank(projectPar.getBdcdyh()) || StringUtils.isNotBlank(projectPar.getBdcdyh()) && bdcCfService.queryYcfByBdcdyh(projectPar.getBdcdyh()).size() == 0) {
                        bdcCf.setCfksqx(DateUtils.now());
                    }
                    if (StringUtils.equals(Constants.CFLX_LHCF, bdcCf.getCflx()) || StringUtils.equals(Constants.CFLX_ZD_LHYCF, bdcCf.getCflx())) {
                        bdcCf.setCfksqx(null);
                    }
                    if (StringUtils.equals(Constants.DJZX_XF, bdcXm.getDjzx())) {
                        bdcCf.setCfksqx(DateUtils.now());
                        bdcCf.setCfjsqx(null);
                        bdcCf.setCflx(Constants.CFLX_XF);
                    }
                    if (StringUtils.equals(Constants.CFLX_XF, bdcCf.getCflx())) {
                        BdcCf ybdcCf = bdcCfService.selectCfByProid(bdcXmRel.getYproid());
                        if (ybdcCf != null) {
                            bdcCf.setCfsj(ybdcCf.getCfsj());
                        }
                    } else {
                        bdcCf.setCfsj(new Date());
                    }
                    //查封创建即生效，不需要等项目办结
                    bdcCf.setQszt(Constants.QLLX_QSZT_XS);
                    bdcXm.setQszt(Constants.QLLX_QSZT_XS);
                    bdcXm.setXmzt(Constants.XMZT_SZ);
                    projectPar.setBdcXm(bdcXm);
                }
            }

            if (isAdd) {
                qllxVoList.add(bdcCf);
            }
        }
        return qllxVoList;
    }

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息从权籍中
     */
    @Override
    public List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        //jiangganzhi 查封权利信息无需从权籍获取
        return null;
    }


    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息从预告中
     */
    @Override
    public List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        //jiangganzhi 查封权利信息无需从bdc_yg获取
        return null;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_cf";
    }
}
