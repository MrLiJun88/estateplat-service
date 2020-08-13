package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * 变更登记转发服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class TurnProjectBgdjServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcComplexFgHbHzService bdcComplexFgHbHzService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
        //zdd 转移登记 需要继承原权利信息
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        for (BdcXmRel bdcXmRel : bdcXmRelList) {
            qllxVo = qllxService.makeSureQllx(bdcXm);
            if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)) {
                    List<QllxVo> yqllxList = qllxService.queryQllx(bdcXm);
                    QllxVo yqllxVo = null;
                    if (CollectionUtils.isNotEmpty(yqllxList)) {
                        yqllxVo = yqllxList.get(0);
                    }
                    if (yqllxVo == null) {
                        yqllxVo = qllxService.makeSureQllx(bdcXm);
                    }
                    yqllxVo.setQlid(UUIDGenerator.generate18());
                    yqllxVo.setProid(bdcXm.getProid());
                    yqllxVo.setYwh(bdcXm.getBh());
                    yqllxVo.setDbr(null);
                    yqllxVo.setDjsj(null);
                    //zdd 不应该继承原来项目的附记
                    //姜堰需要继承上一手附记，其他地方不配置不影响
                    String isJcFj = AppConfig.getProperty("isJcFj");
                    if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcFj, ParamsConstants.FALSE_LOWERCASE)) {
                        yqllxVo.setFj("");
                    }
                    //zdd 前后的权利类型相同
                    yqllxVo.setQszt(0);
                    qllxVo = yqllxVo;
                }else{
                    if(StringUtils.isNotBlank(bdcXmRel.getYqlid())){
                        //过渡读取过渡数据
                        String isJcFj = AppConfig.getProperty("isJcFj");
                        String gdQlid = bdcXmRel.getYqlid();
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
                }
            }

            qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);

            if (qllxVo instanceof BdcFdcqDz&&StringUtils.isNotBlank(bdcXm.getSqlx()) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWFGHBBG_DM)&&StringUtils.isNotBlank(bdcXm.getXmly())&&StringUtils.equals(bdcXm.getXmly(),Constants.XMLY_BDC)) {
                /**
                 *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                 *@description 禅道16600 多幢重新读取权籍信息生成bdc_fdcq_dz和bdc_fwfzxx
                 */
                BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                qllxVo = bdcFdcqDzService.reGenerateBdcFdcqDzFromQj(bdcXm,bdcFdcqDz);
            }

            if (qllxVo != null) {
                if (qllxVoTemp == null) {
                    entityMapper.insertSelective(qllxVo);
                } else {
                    qllxVo.setQlid(qllxVoTemp.getQlid());
                    entityMapper.updateByPrimaryKeySelective(qllxVo);
                }
            }
            //土地分割合并换证登记 剩余宗地的抵押关联剩余宗地的换证项目
            if(StringUtils.isNotBlank(bdcXm.getWiid())){
                String isTdFgHbHzDj = bdcComplexFgHbHzService.isTdFgHbHzDj(bdcXm.getWiid());
                if(StringUtils.isNotBlank(isTdFgHbHzDj) && StringUtils.equals(isTdFgHbHzDj, "true")&&StringUtils.isNotBlank(bdcXmRel.getYproid())){
                    BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(bdcXmRel.getYproid());
                    if(bdcDyaq != null){
                        BdcXmRel dyBdcXmRel = bdcXmRel;
                        dyBdcXmRel.setRelid(UUIDGenerator.generate18());
                        entityMapper.saveOrUpdate(dyBdcXmRel,dyBdcXmRel.getRelid());
                        if(StringUtils.isNotBlank(bdcXm.getBdcdyid())){
                            String sytdProid = bdcComplexFgHbHzService.getSytdProid(bdcXm.getBdcdyid(),bdcXm.getWiid());
                            if(StringUtils.isNotBlank(sytdProid)){
                                HashMap map = new HashMap();
                                map.put("proid",sytdProid);
                                List<BdcJsydzjdsyq> bdcJsydzjdsyqList = bdcJsydzjdsyqService.getBdcJsydzjdsyqList(map);
                                if(CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)){
                                    bdcXmRel.setYqlid(bdcJsydzjdsyqList.get(0).getQlid());
                                }
                                bdcXmRel.setYproid(sytdProid);
                                entityMapper.saveOrUpdate(bdcXmRel,bdcXmRel.getRelid());
                            }
                            entityMapper.saveOrUpdate(bdcXm,bdcXm.getProid());
                        }
                    }
                }
            }
            break;
        }
        //转移登记、变更登记、更正登记、换证登记不继承交易价格
        qllxService.noInheritJyjgByQllxVo(qllxVo);
        return qllxVo;
    }


}
