package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcFdcqService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcYgService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.ql.BdcQlDqService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/20
 * @description 不动产预告登记权利读取
 */
@Service
public class BdcYgDqServiceImpl implements BdcQlDqService {
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    @Resource(name = "dozerQlMapper")
    private DozerBeanMapper dozerMapper;

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取权利信息从项目上
     */
    @Override
    public List<QllxVo> readQlFormXm(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcYg bdcYg = null;
        BdcXmRel bdcXmRel = null;
        BdcXm bdcXm = null;
        if(CollectionUtils.isNotEmpty(qllxVoList)){
            if (qllxVoList.get(0) instanceof BdcYg) {
                bdcYg = (BdcYg) qllxVoList.get(0);
                isAdd = false;
            }
        }else {
            qllxVoList = Lists.newArrayList();
            bdcYg = new BdcYg();
        }
        if(bdcYg != null && projectPar != null){
            if(StringUtils.isBlank(bdcYg.getQlid())){
                bdcYg.setQlid(UUIDGenerator.generate18());
            }
            bdcYg.setQszt(Constants.QLLX_QSZT_LS);
            if (projectPar.getBdcXm() != null) {
                bdcXm = projectPar.getBdcXm();
                bdcYg.setBdcdyid(projectPar.getBdcXm().getBdcdyid());
                bdcYg.setProid(projectPar.getBdcXm().getProid());
                bdcYg.setYwh(projectPar.getBdcXm().getBh());
                bdcYg.setQllx(projectPar.getBdcXm().getQllx());
            }
            if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                bdcXmRel = projectPar.getBdcXmRelList().get(0);
            }
            if(bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())){
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXmRel.getYproid());
                BdcFdcq bdcFdcq = null;
                if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    bdcFdcq = bdcFdcqList.get(0);
                }
                if (bdcFdcq != null) {
                    /**
                     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                     * @description 整数存szc，非整数存szmyc
                     */
                    if (bdcFdcq.getSzc() != null) {
                        bdcYg.setSzc(bdcFdcq.getSzc());
                    }
                    if (bdcFdcq.getSzmyc() != null) {
                        bdcYg.setSzmyc(bdcFdcq.getSzmyc());
                    }

                    if (bdcFdcq.getZcs() != null) {
                        bdcYg.setZcs(bdcFdcq.getZcs());
                    }
                    bdcYg.setJzmj(bdcFdcq.getJzmj());
                    bdcYg.setGyqk(bdcFdcq.getGyqk());
                    bdcYg.setFwxz(bdcFdcq.getFwxz());
                }
                List<BdcQlr> qlrList = bdcQlrService.queryBdcQlrByProid(bdcXmRel.getYproid());
                if (CollectionUtils.isNotEmpty(qlrList) && StringUtils.isNotBlank(qlrList.get(0).getQlrmc())) {
                    bdcYg.setTdqlr(qlrList.get(0).getQlrmc());
                }
                //sc继承原预告的信息
                BdcYg yBdcyg = bdcYgService.getBdcYgByProid(bdcXmRel.getYproid());
                if (yBdcyg != null) {
                    bdcYg.setJzmj(yBdcyg.getJzmj());
                    bdcYg.setFwxz(yBdcyg.getFwxz());
                    bdcYg.setSzc(yBdcyg.getSzc());
                    bdcYg.setSzmyc(yBdcyg.getSzmyc());
                    bdcYg.setZcs(yBdcyg.getZcs());
                    bdcYg.setTdqlr(yBdcyg.getTdqlr());
                    bdcYg.setQdjg(yBdcyg.getQdjg());
                    bdcYg.setZwlxksqx(yBdcyg.getZwlxksqx());
                    bdcYg.setZwlxjsqx(yBdcyg.getZwlxjsqx());
                }
            }
            if(bdcXm != null){
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPF)) {
                    bdcYg.setYgdjzl(Constants.YGDJZL_YGSPF);
                    //预购商品房，土地使用权人默认全体业主
                    bdcYg.setTdqlr(Constants.TDSYQR_QTYZ);
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_YGSPFDY)) {
                    bdcYg.setYgdjzl(Constants.YGDJZL_YGSPFDY);
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_FWZYYG) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_GYJSYT) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_JSYTJT))) {
                    bdcYg.setYgdjzl(Constants.YGDJZL_QTBDCMMYG);
                } else if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_BDCDY))//lcl增加不动产抵押预告登记判断
                {
                    bdcYg.setYgdjzl(Constants.YGDJZL_YGSPFDY);
                }
            }
            if (isAdd) {
                qllxVoList.add(bdcYg);
            }
        }
        return qllxVoList;
    }

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取权利信息从权籍中
     */
    @Override
    public List<QllxVo> readQlFormQj(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        boolean isAdd = true;
        BdcYg bdcYg = null;
        if (CollectionUtils.isNotEmpty(qllxVoList)) {
            if (qllxVoList.get(0) instanceof BdcYg) {
                bdcYg = (BdcYg) qllxVoList.get(0);
                isAdd = false;
            }
        } else {
            qllxVoList = Lists.newArrayList();
            bdcYg = new BdcYg();
        }
        if (bdcYg != null) {
            if (projectPar.getDjsjFwxx() == null) {
                projectParService.getQjsj(projectPar, "djsjFwxx");
            }
//            if (CollectionUtils.isEmpty(projectPar.getDjsjZdxxList())) {
//                projectParService.getQjsj(projectPar, "djsjZdxxList");
//            }
            if (projectPar.getDjsjFwxx() != null) {
                dozerMapper.map(projectPar.getDjsjFwxx(), bdcYg);
                List<DjsjFwzbxx> djsjFwzbxxList = projectPar.getDjsjFwxx().getFwzbxxList();
                if(CollectionUtils.isNotEmpty(djsjFwzbxxList)){
                    DjsjFwzbxx djsjFwzbxx = djsjFwzbxxList.get(0);
                    dozerMapper.map(djsjFwzbxx, bdcYg);
                }
            }
//            if (CollectionUtils.isNotEmpty(projectPar.getDjsjZdxxList())) {
//                dozerMapper.map(projectPar.getDjsjZdxxList().get(0), bdcYg);
//            }
            if (isAdd) {
                qllxVoList.add(bdcYg);
            }
        }
        return qllxVoList;
    }

    /**
     * @param projectPar 流程参数类
     * @param qllxVoList
     * @return 不动产权利
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取权利信息从预告中
     */
    @Override
    public List<QllxVo> readQlFormYg(ProjectPar projectPar, List<QllxVo> qllxVoList) {
        return null;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return "bdc_yg";
    }
}
