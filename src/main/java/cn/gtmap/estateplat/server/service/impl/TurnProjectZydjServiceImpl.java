package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 转移登记转发服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-2
 */
public class TurnProjectZydjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcYgService bdcYgService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private GdYgService gdYgService;
    @Autowired
    private GdTdService gdTdService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;
        qllxVo = qllxService.makeSureQllx(bdcXm);
        String yqlid = "";
        String sqlxdm = "";
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        //zdd 转移登记 需要继承原权利信息
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        BdcXmRel bdcXmRel = null;
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            bdcXmRel = bdcXmRelList.get(0);
        }

        //过度不走下面这块
        if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)) {
            if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                if (ybdcxm != null) {
                    QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
                    if(yqllxVo!=null){
                        /**
                         * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
                         * @description 权利一致
                         */
                        if (qllxVo.getClass().equals(yqllxVo.getClass())){
                            yqlid = yqllxVo.getQlid();
                            yqllxVo.setQlid(UUIDGenerator.generate18());
                            yqllxVo.setProid(bdcXm.getProid());
                            yqllxVo.setYwh(bdcXm.getBh());
                            yqllxVo.setDbr(null);
                            yqllxVo.setDjsj(null);
                            //姜堰需要继承上一手附记，其他地方不配置不影响
                            String isJcFj = AppConfig.getProperty("isJcFj");
                            if (StringUtils.isBlank(isJcFj)||StringUtils.equals(isJcFj, ParamsConstants.FALSE_LOWERCASE)){
                                yqllxVo.setFj("");
                            }
                            //转移是否继承上一手权利其他状况，其他地方不配置不影响
                            String isJcQlqtzk = AppConfig.getProperty("isJcQlqtzk");
                            if (StringUtils.equals(isJcQlqtzk, ParamsConstants.FALSE_LOWERCASE)){
                                yqllxVo.setQlqtzk("");
                            }
                            yqllxVo.setQszt(0);
                            qllxVo = yqllxVo;
                        }
                        if(yqllxVo.getClass() == BdcYg.class && qllxVo.getClass() == BdcFdcq.class){
                            /**
                             * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
                             * @description 商品买卖转移登记继承预售商品房预告登记信息
                             */
                            BdcYg bdcYg = (BdcYg) yqllxVo;
                            BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                            if (StringUtils.isNotBlank(bdcYg.getGhyt())) {
                                bdcFdcq.setGhyt(bdcYg.getGhyt());
                            }
                            if (StringUtils.isNotBlank(bdcYg.getGyqk())) {
                                bdcFdcq.setGyqk(bdcYg.getGyqk());
                            }
                            if (StringUtils.isNotBlank(bdcYg.getFwxz())) {
                                bdcFdcq.setFwxz(bdcYg.getFwxz());
                            }
                            if (bdcYg.getQdjg() != null) {
                                bdcFdcq.setJyjg(bdcYg.getQdjg());
                            }
                            if (bdcYg.getSzc() != null) {
                                bdcFdcq.setSzc(bdcYg.getSzc());
                            }
                            if (bdcYg.getSzmyc() != null){
                                bdcFdcq.setSzmyc(bdcYg.getSzmyc());
                            }
                            if (bdcYg.getZcs() != null) {
                                bdcFdcq.setZcs(bdcYg.getZcs());
                            }
                            if (bdcYg.getJzmj() != null) {
                                bdcFdcq.setJzmj(bdcYg.getJzmj());
                                bdcFdcq.setScmj(bdcYg.getJzmj());
                            }
                            qllxVo = bdcFdcq;
                        }
                    }
                }
            }
        } else {
            //过渡读取过渡数据
            String gdQlid = bdcXmRel.getYqlid();
            String isJcFj = AppConfig.getProperty("isJcFj");
            if(StringUtils.isNotBlank(gdQlid)){
                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdQlid);
                if(null != gdFwsyq&&qllxVo instanceof BdcFdcq){
                    BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                    bdcFdcq.setTdsyksqx(gdFwsyq.getTdsyksrq());
                    bdcFdcq.setTdsyjsqx(gdFwsyq.getTdsyjsrq());
                    bdcFdcq.setFj(gdFwsyq.getFj());
                    if (StringUtils.isBlank(isJcFj)||StringUtils.equals(isJcFj, ParamsConstants.FALSE_LOWERCASE)){
                        bdcFdcq.setFj("");
                    }
                }
                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(gdQlid);
                if(gdTdsyq != null&&qllxVo instanceof BdcJsydzjdsyq){
                    BdcJsydzjdsyq bdcJsydzjdsyq = (BdcJsydzjdsyq) qllxVo;
                    bdcJsydzjdsyq.setFj(gdTdsyq.getFj());
                    if (StringUtils.isBlank(isJcFj)||StringUtils.equals(isJcFj, ParamsConstants.FALSE_LOWERCASE)){
                        gdTdsyq.setFj("");
                    }
                }
            }
        }
        qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);

        if (qllxVo instanceof BdcFdcqDz&&StringUtils.isNotBlank(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWFGHBZY_DM)) {
            List<BdcFwfzxx> ybdcFwfzxxList = bdcFwfzxxService.getBdcFwfzxxListByQlid(yqlid);
            bdcFwfzxxService.yBdcFwfzxxTurnProjectBdcFwfzxx(ybdcFwfzxxList, qllxVo.getProid(), qllxVo.getQlid());
        }

        QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
        if (qllxVoTemp == null) {
            qllxVo = qllxService.initQllxVoFromOntBdcXm(qllxVo,bdcXm.getProid());
            //做预购商品房预告登记和抵押预告登记，再做新建商品房首次登记，最后商品房买卖转移登记和抵押登记时将原来的抵押预告继承，注销预告权利
            String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
            List<BdcYg> bdcYgList = new ArrayList<BdcYg>();
            if(StringUtils.isNotBlank(bdcdyh)){
                 bdcYgList = bdcYgService.getBdcYgList(bdcdyh, "1");
            }
            if(StringUtils.equals(sqlxdm,Constants.SQLX_CLF)&&CollectionUtils.isNotEmpty(bdcYgList)){
                for(BdcYg bdcYg : bdcYgList){
                    if(StringUtils.equals(bdcYg.getYgdjzl(),Constants.YGDJZL_YGSPFDY)){
                        if (qllxVo instanceof BdcDyaq) {
                            bdcYg.setQszt(Constants.QLLX_QSZT_HR);
                            entityMapper.saveOrUpdate(bdcYg,bdcYg.getQlid());
                            BdcDyaq bdcDyaq = (BdcDyaq)qllxVo;
                            if(null != bdcYg.getZwlxksqx())
                                bdcDyaq.setZwlxksqx(bdcYg.getZwlxksqx());
                            if(null != bdcYg.getZwlxjsqx())
                                bdcDyaq.setZwlxjsqx(bdcYg.getZwlxjsqx());

                            qllxVo = bdcDyaq;
                        }
                    }
                }
            }
            entityMapper.insertSelective(qllxVo);
        } else {
            qllxVo.setQlid(qllxVoTemp.getQlid());
            qllxVo = qllxService.initQllxVoFromOntBdcXm(qllxVo,bdcXm.getProid());
            entityMapper.updateByPrimaryKeySelective(qllxVo);
        }
        //转移登记、变更登记、更正登记、换证登记不继承交易价格
        qllxService.noInheritJyjgByQllxVo(qllxVo);
        return qllxVo;
    }
}
