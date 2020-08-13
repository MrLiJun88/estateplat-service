package cn.gtmap.estateplat.server.sj.ql.impl;

import cn.gtmap.estateplat.model.server.core.BdcLq;
import cn.gtmap.estateplat.model.server.core.DjsjNydDcb;
import cn.gtmap.estateplat.model.server.core.NydQlr;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
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
import java.util.List;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/2/21
 * @description 不动产林权登记权利读取
 */
@Service
public class BdcLqDqServiceImpl implements BdcQlDqService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private DjSjMapper djSjMapper;
    @Autowired
    private QllxService qllxService;
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
        BdcLq bdcLq = null;
        if(CollectionUtils.isNotEmpty(qllxVoList)){
            if (qllxVoList.get(0) instanceof BdcLq) {
                bdcLq = (BdcLq) qllxVoList.get(0);
                isAdd = false;
            }
        }else {
            qllxVoList = Lists.newArrayList();
            bdcLq = new BdcLq();
        }
        if(bdcLq != null && projectPar != null){
            if(StringUtils.isBlank(bdcLq.getQlid())){
                bdcLq.setQlid(UUIDGenerator.generate18());
            }
            if (projectPar.getBdcXm() != null) {
                bdcLq.setBdcdyid(projectPar.getBdcXm().getBdcdyid());
                bdcLq.setProid(projectPar.getBdcXm().getProid());
                bdcLq.setYwh(projectPar.getBdcXm().getBh());
                bdcLq.setQllx(projectPar.getBdcXm().getQllx());
            }
            bdcLq.setQszt(Constants.QLLX_QSZT_LS);
            if (isAdd) {
                qllxVoList.add(bdcLq);
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
        BdcLq bdcLq = null;
        if(CollectionUtils.isNotEmpty(qllxVoList)){
            if (qllxVoList.get(0) instanceof BdcLq) {
                bdcLq = (BdcLq) qllxVoList.get(0);
                isAdd = false;
            }
        }else {
            qllxVoList = Lists.newArrayList();
            bdcLq = new BdcLq();
        }
        if(bdcLq != null && projectPar != null){
            if (projectPar.getDjsjLqxx() == null) {
                projectParService.getQjsj(projectPar, "djsjLqxx");
            }
            if(projectPar.getDjsjLqxx() != null){
                dozerMapper.map(projectPar.getDjsjLqxx(), bdcLq);

                StringBuilder lmsyqr = new StringBuilder();
                StringBuilder lmsuqr = new StringBuilder();
                String djh = "";
                if (StringUtils.isNotBlank(projectPar.getDjsjLqxx().getDjh())) {
                    djh = projectPar.getDjsjLqxx().getDjh();
                } else if (StringUtils.isNotBlank(projectPar.getDjsjLqxx().getBdcdyh())) {
                    djh = StringUtils.substring(projectPar.getDjsjLqxx().getBdcdyh(), 0, 19);
                }
                List<NydQlr> nydQlrList = bdcQlrService.getNydQlrByDjh(djh);
                if (CollectionUtils.isNotEmpty(nydQlrList)) {
                    for (int i = 0; i < nydQlrList.size(); i++) {
                        NydQlr nydQlr = nydQlrList.get(i);
                        if (StringUtils.equals(nydQlr.getSflmsuqr(), "1")) {
                            if (i != nydQlrList.size() - 1) {
                                lmsuqr.append(nydQlr.getQlr()).append(" ");
                            } else {
                                lmsuqr.append(nydQlr.getQlr());
                            }
                        }
                        if (StringUtils.equals(nydQlr.getSflmsyqr(), "1")) {
                            if (i != nydQlrList.size() - 1) {
                                lmsyqr.append(nydQlr.getQlr()).append(" ");
                            } else {
                                lmsyqr.append(nydQlr.getQlr());
                            }
                        }
                    }
                }
                String fbfmc = djSjMapper.getFbfmcByDjh(djh);
                List<DjsjNydDcb> djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(djh);
                DjsjNydDcb djsjNydDcb = null;
                if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                    djsjNydDcb = djsjNydDcbList.get(0);
                }
                if (StringUtils.isNotBlank(fbfmc)) {
                    bdcLq.setFbfmc(fbfmc);
                } else if (CollectionUtils.isNotEmpty(djsjNydDcbList) && StringUtils.isNotBlank(djsjNydDcbList.get(0).getTdsyzmc())) {
                    bdcLq.setFbfmc(djsjNydDcbList.get(0).getTdsyzmc());
                }
                if (projectPar.getDjsjLqxx().getMj() != null && projectPar.getDjsjLqxx().getMj() != 0) {
                    bdcLq.setSyqmj(projectPar.getDjsjLqxx().getMj());
                } else {
                    if (djsjNydDcb != null) {
                        if (djsjNydDcb.getFzmj() != null && djsjNydDcb.getFzmj() != 0) {
                            bdcLq.setSyqmj(djsjNydDcb.getFzmj());
                        } else {
                            bdcLq.setSyqmj(djsjNydDcb.getScmj());
                        }
                        if (djsjNydDcb.getQsrq() != null) {
                            bdcLq.setLdsyksqx(djsjNydDcb.getQsrq());
                        }
                        if (djsjNydDcb.getZzrq() != null) {
                            bdcLq.setLdsyjsqx(djsjNydDcb.getZzrq());
                        }
                    }

                }
                if (djsjNydDcb != null) {
                    if (djsjNydDcb.getQsrq() != null) {
                        bdcLq.setLdsyksqx(djsjNydDcb.getQsrq());
                    }
                    if (djsjNydDcb.getZzrq() != null) {
                        bdcLq.setLdsyjsqx(djsjNydDcb.getZzrq());
                    }
                }
                bdcLq.setLmsuqr(lmsuqr.toString());
                bdcLq.setLmsyqr(lmsyqr.toString());
                String gyqk = projectPar.getDjsjLqxx().getGyqk();
                if (StringUtils.isNotBlank(gyqk)) {
                    if (!NumberUtils.isNumber(gyqk)) {
                        gyqk = qllxService.changeGyqkMcToDm(gyqk);
                    }
                    bdcLq.setGyqk(gyqk);
                }
                String isg = null;
                if (StringUtils.isNotBlank(projectPar.getDjsjLqxx().getBdcdyh()) && StringUtils.length(projectPar.getDjsjLqxx().getBdcdyh()) > 13) {
                    isg = StringUtils.substring(projectPar.getDjsjLqxx().getBdcdyh(), 12, 13);
                }
                if (StringUtils.equals(isg, "G")) {
                    bdcLq.setLdsyqxz("国家所有");
                } else {
                    bdcLq.setLdsyqxz("集体所有");
                }
            }
            if (isAdd) {
                qllxVoList.add(bdcLq);
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
        return "bdc_lq";
    }
}
