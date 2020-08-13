package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcDyaqService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public class TurnComplexScYtdDyProjectServiceImpl extends TurnComplexHzDyProjectServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdFwService gdfwService;
    @Autowired
    private BdcDyaqService bdcDyaqService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;
        if (bdcXm != null) {
            qllxVo = qllxService.makeSureQllx(bdcXm);
            deleteQl(bdcXm, qllxVo);
            QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (bdcXmRel != null) {
                        qllxVo = qllxService.makeSureQllx(bdcXm);
                        if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                            qllxVo = inheritBdcDyaq(qllxVo, bdcXmRel);
                            qllxVo = initQllxVo(bdcXm, qllxVo);
                        }
                        qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
                        if (qllxVo != null) {
                            if (qllxVoTemp == null) {
                                entityMapper.insertSelective(qllxVo);
                            } else {
                                qllxVo.setQlid(qllxVoTemp.getQlid());
                                entityMapper.updateByPrimaryKeySelective(qllxVo);
                            }
                            inheritQl(qllxVo, qllxVoTemp, bdcXmRel);
                            break;
                        }
                    }
                }
            }
        }
        return qllxVo;
    }

    /**
     * @param bdcXm
     * @param qllxVo
     * @return
     */
    public QllxVo initQllxVo(BdcXm bdcXm, QllxVo qllxVo) {
        QllxVo yqllxVo = qllxVo;
        List<QllxVo> yqllxList = qllxService.queryQllx(bdcXm);
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
        String isJcFj = AppConfig.getProperty("isJcFj");
        if (StringUtils.isBlank(isJcFj) || StringUtils.equals(isJcFj, "false")) {
            yqllxVo.setFj("");
        }
        yqllxVo.setQszt(0);
        //zdd 前后的权利类型相同
        yqllxVo.setQszt(0);
        qllxVo = yqllxVo;
        return qllxVo;
    }

    /**
     * @param bdcXm
     * @param qllxVo
     */
    public void deleteQl(BdcXm bdcXm, QllxVo qllxVo) {
        //zdd 此处代码解决一个项目  多次选择不同种类的不动产信息时  上一次生成的权利信息未删除问题
        List<QllxParent> qllxParentList = qllxService.queryQllxByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(qllxParentList)) {
            for (int i = 0; i < qllxParentList.size(); i++) {
                QllxParent qllxVotemp = qllxParentList.get(i);
                if (StringUtils.isNotBlank(qllxVotemp.getQllx())) {
                    QllxVo hisQllx = qllxService.makeSureQllx(qllxVotemp.getQllx());
                    if (!hisQllx.getClass().equals(qllxVo.getClass())) {
                        qllxService.delQllxByproid(hisQllx, bdcXm.getProid());
                    }
                }
            }
        }
    }

    /**
     * @param qllxVo
     * @param qllxVoTemp
     */
    public void inheritQl(QllxVo qllxVo, QllxVo qllxVoTemp, BdcXmRel bdcXmRel) {
        //hqz（房地产权登记信息（项目内多幢房屋））获取原权利登记信息里的项目列表并继承到当前权利登记信息里
        if (qllxVo instanceof BdcFdcqDz) {
            BdcFdcqDz fdcq = (BdcFdcqDz) qllxVo;
            List<BdcFwfzxx> fwfzxxs = fdcq.getFwfzxxList();
            if (CollectionUtils.isNotEmpty(fwfzxxs) &&
                    !StringUtils.equals(fwfzxxs.get(0).getQlid(), qllxVo.getQlid())) {
                for (BdcFwfzxx fwfzxx : fwfzxxs) {
                    fwfzxx.setQlid(qllxVo.getQlid());
                    if (qllxVoTemp == null) {
                        fwfzxx.setFzid(UUIDGenerator.generate());
                    }
                    entityMapper.saveOrUpdate(fwfzxx, fwfzxx.getFzid());
                }
            }
        }

    }

    /**
     * @param qllxVo
     * @param bdcXmRel
     * @return
     */
    public QllxVo inheritBdcDyaq(QllxVo qllxVo, BdcXmRel bdcXmRel) {
        if (qllxVo instanceof BdcDyaq) {
            BdcDyaq ydyaq=bdcDyaqService.queryBdcDyaqByProid(bdcXmRel.getYproid());
            if (null != ydyaq && 1==ydyaq.getQszt()) {
                qllxVo = ydyaq;
            } else {
                List<GdDy> gdDyList = gdfwService.getGdDyListByGdproid(bdcXmRel.getYproid(), 0);
                if (CollectionUtils.isNotEmpty(gdDyList)) {
                    GdDy gdDy = gdDyList.get(0);
                    qllxVo=gdfwService.readBdcDyaqFromGdDy(gdDy,(BdcDyaq)qllxVo,null);
                }
            }
        }
        return qllxVo;
    }
}
