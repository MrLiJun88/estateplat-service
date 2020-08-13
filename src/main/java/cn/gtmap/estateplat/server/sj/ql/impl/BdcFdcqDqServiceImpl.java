package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/18 0018
 * @description
 */
@Service
public class BdcFdcqDqServiceImpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    @Resource(name = "dozerQlMapper")
    private DozerBeanMapper dozerMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private DjsjFwMapper djsjFwMapper;

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
        BdcFdcq bdcFdcq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcFdcq) {
                bdcFdcq = (BdcFdcq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcFdcq = new BdcFdcq();
        }
        if (bdcFdcq != null) {
            if (projectPar != null) {
                if (projectPar.getBdcXm() != null) {
                    bdcFdcq.setProid(projectPar.getBdcXm().getProid());
                    bdcFdcq.setYwh(projectPar.getBdcXm().getBh());
                    bdcFdcq.setQllx(projectPar.getBdcXm().getQllx());
                }
                if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                    bdcFdcq.setBdcdyid(projectPar.getBdcdyid());
                }
            }
            if (StringUtils.isBlank(bdcFdcq.getQlid())) {
                bdcFdcq.setQlid(UUIDGenerator.generate18());
            }
            if (StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_SPFSCKFSZC_DM)) {
                bdcFdcq.setFzlx(Constants.FZLX_FZS);
            } else if (StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_SPFXZBG_DM) || StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM)) {
                bdcFdcq.setFzlx(Constants.FZLX_FZM);
            }
            bdcFdcq.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcFdcq);
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
        boolean isAdd = true;
        BdcFdcq bdcFdcq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcFdcq) {
                bdcFdcq = (BdcFdcq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcFdcq = new BdcFdcq();
        }
        if (bdcFdcq != null) {
            if (projectPar.getDjsjFwxx() == null) {
                projectParService.getQjsj(projectPar, "djsjFwxx");
            }
            if (CollectionUtils.isEmpty(projectPar.getDjsjZdxxList())) {
                projectParService.getQjsj(projectPar, "djsjZdxxList");
            }
            if (projectPar.getDjsjFwxx() != null) {
                DjsjFwxx djsjFwxx = projectPar.getDjsjFwxx();
                if (StringUtils.isNotBlank(djsjFwxx.getDycs())) {
                    if (NumberUtils.isNumber(djsjFwxx.getDycs())) {
                        if (bdcFdcq.getSzc() == null) {
                            bdcFdcq.setSzc((int) Double.parseDouble(djsjFwxx.getDycs()));
                        }
                    } else {
                        if (bdcFdcq.getSzmyc() == null) {
                            bdcFdcq.setSzmyc(djsjFwxx.getDycs());
                        }
                    }
                }
                dozerMapper.map(djsjFwxx, bdcFdcq);
                if (CollectionUtils.isNotEmpty(djsjFwxx.getFwzbxxList())) {
                    DjsjFwzbxx djsjFwzbxx = djsjFwxx.getFwzbxxList().get(0);
                    bdcFdcq.setJgsj(djsjFwzbxx.getJgsj());
                    if (djsjFwzbxx.getJgsj() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        bdcFdcq.setJznd(Integer.parseInt(sdf.format(djsjFwzbxx.getJgsj())));
                    }
                    if (StringUtils.isNotBlank(djsjFwzbxx.getFwjg())) {
                        bdcFdcq.setFwjg(djsjFwzbxx.getFwjg());
                    }
                    if (bdcFdcq.getJzmj() == null) {
                        bdcFdcq.setJzmj(djsjFwzbxx.getJzmj());
                    }
                    if (bdcFdcq.getScmj() == null) {
                        bdcFdcq.setScmj(djsjFwzbxx.getJzmj());
                    }
                    if (bdcFdcq.getZcs() == null) {
                        bdcFdcq.setZcs(djsjFwzbxx.getZcs());
                    }
                    if (bdcFdcq.getMyzcs() == null) {
                        bdcFdcq.setMyzcs(String.valueOf(djsjFwzbxx.getZcs()));
                    }
                }
                //处理附属设施
                StringBuilder fsssBuiler = new StringBuilder();
                if (djsjFwxx.getScglmj() != null && djsjFwxx.getScglmj() != 0.0) {
                    fsssBuiler.append("阁楼：").append(djsjFwxx.getScglmj()).append("㎡");
                }
                if (StringUtils.isNotBlank(fsssBuiler.toString())) {
                    fsssBuiler.append(",");
                }
                if (djsjFwxx.getScdxsmj() != null && djsjFwxx.getScdxsmj() != 0.0) {
                    fsssBuiler.append("地下室：").append(djsjFwxx.getScdxsmj()).append("㎡");
                }
                if (StringUtils.isNotBlank(fsssBuiler.toString())) {
                    bdcFdcq.setFsss(fsssBuiler.toString());
                }

                if (CollectionUtils.isNotEmpty(projectPar.getDjsjZdxxList())) {
                    dozerMapper.map(projectPar.getDjsjZdxxList().get(0), bdcFdcq);
                }

                if(StringUtils.isNotBlank(projectPar.getQjid())) {
                    List<String> fwlxList = djsjFwMapper.getBdcfwlxById(projectPar.getQjid());
                    String bdcdyFwlx = "";
                    if (CollectionUtils.isNotEmpty(fwlxList)) {
                        bdcdyFwlx = fwlxList.get(0);
                        if(StringUtils.equals(bdcdyFwlx,Constants.DJSJ_FWHS_DM)){
                            bdcFdcq.setTdsyjsqx(djsjFwxx.getZzrq());
                        }
                    }

                }
            }

            if (isAdd) {
                qllxVoList.add(bdcFdcq);
            }
        }
        return qllxVoList;
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
        boolean isAdd = true;
        BdcFdcq bdcFdcq = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcFdcq) {
                bdcFdcq = (BdcFdcq) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcFdcq = new BdcFdcq();
        }
        if (bdcFdcq != null) {
            if (projectPar != null && CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                List<BdcXmRel> bdcXmRelList = projectPar.getBdcXmRelList();
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                            BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                            if (ybdcxm != null) {
                                QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
                                if (yqllxVo != null) {
                                    if (yqllxVo.getClass() == BdcYg.class) {
                                        // 商品买卖转移登记继承预售商品房预告登记信息
                                        BdcYg bdcYg = (BdcYg) yqllxVo;
                                        bdcFdcq.setFdcjyhth(bdcYg.getJyhth());
                                        bdcFdcq.setJyjg(bdcYg.getJyje());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isAdd) {
                qllxVoList.add(bdcFdcq);
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
        return "bdc_fdcq";
    }
}
