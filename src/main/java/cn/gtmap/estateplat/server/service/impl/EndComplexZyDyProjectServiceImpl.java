package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.1.0, 2016/4/14.
 * @description 带抵押的转移流程
 */
public class EndComplexZyDyProjectServiceImpl extends EndProjectZydjServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcJsydsyqLhxxService bdcJsydsyqLhxxService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        //主要是针对抵押在正式库的数据，前面流程代码中，新抵押的原项目都取原抵押项目，方便继承数据和注销老权利
        //现在把抵押的原项目修改为转移以后的项目
        //zx更新抵押上一手关系
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            if (qllxVo instanceof BdcDyaq) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    if (ybdcXm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcXm);
                        if (yqllxVo instanceof BdcDyaq) {
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                        }
                    }
                    if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                        GdDy gdDy = entityMapper.selectByPrimaryKey(GdDy.class, bdcXmRel.getYqlid());
                        GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, bdcXmRel.getYqlid());
                        if ((gdDy != null && gdDy.getIsjy() != 2) || (gdYg != null && gdYg.getIszx() != 2)) {
                            super.changeGdsjQszt(bdcXmRel, 1);
                        }
                    }
                }
                if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                    List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                    String yproid = "";
                    for (BdcXm bdcXm1 : bdcXmList) {
                        QllxVo qllxVo1 = qllxService.makeSureQllx(bdcXm1);
                        if (!(qllxVo1 instanceof BdcDyaq)) {
                            yproid = bdcXm1.getProid();
                        }
                    }
                    if (StringUtils.isNotBlank(yproid) && CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        /**
                         * 此处之前的写法是将BdcXmRel的yproid直接替换为转移项目的proid,
                         * 这样就会忽略掉原抵押,导致删除退回的时候原抵押的qszt改变不了，现修改为新增一个BdcXmRel
                         * 插入新的xmrel之前根据产权的yproid和抵押的proid去判断是否已经存在，避免登簿和办结重复插入
                         */
                        HashMap queryMap = new HashMap();
                        queryMap.put("yproid", yproid);
                        queryMap.put("proid", bdcXm.getProid());
                        List<BdcXmRel> bdcXmRels = bdcXmRelService.andEqualQueryBdcXmRel(queryMap);
                        BdcXmRel bdcXmRel = null;
                        for (BdcXmRel bdcXmRelTemp : bdcXmRelList) {
                            if (StringUtils.equals(bdcXmRelTemp.getYdjxmly(), Constants.XMLY_BDC)) {
                                if (StringUtils.isNotBlank(bdcXmRelTemp.getYproid()) && !StringUtils.equals(yproid, bdcXmRelTemp.getYproid())) {
                                    QllxVo qllxVoTemp = qllxService.getQllxVoByProid(bdcXmRelTemp.getYproid());
                                    if (qllxVoTemp instanceof BdcFdcq || qllxVoTemp instanceof BdcFdcqDz || qllxVoTemp instanceof BdcJsydzjdsyq || qllxVoTemp instanceof BdcTdsyq) {
                                        bdcXmRel = bdcXmRelTemp;
                                        entityMapper.deleteByPrimaryKey(BdcXmRel.class, bdcXmRelTemp.getRelid());
                                    }
                                }
                            } else {
                                if (StringUtils.isNotBlank(bdcXmRelTemp.getYqlid())) {
                                    GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(bdcXmRelTemp.getYqlid());
                                    if (gdFwsyq != null) {
                                        bdcXmRel = bdcXmRelTemp;
                                        entityMapper.deleteByPrimaryKey(BdcXmRel.class, bdcXmRelTemp.getRelid());
                                    } else {
                                        GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(bdcXmRelTemp.getYqlid());
                                        if (gdTdsyq != null) {
                                            bdcXmRel = bdcXmRelTemp;
                                            entityMapper.deleteByPrimaryKey(BdcXmRel.class, bdcXmRelTemp.getRelid());
                                        }
                                    }
                                }
                            }
                        }
                        if (bdcXmRel == null) {
                            bdcXmRel = bdcXmRelList.get(0);
                        }
                        if (CollectionUtils.isEmpty(bdcXmRels)) {
                            createBdcXmRel(bdcXmRel, yproid);
                        }
                    }
                }
            } else if (qllxVo instanceof BdcFdcq) {
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                        if (ybdcXm != null) {
                            QllxVo yqllxVo = qllxService.queryQllxVo(ybdcXm);
                            if (yqllxVo != null && (yqllxVo instanceof BdcFdcq || yqllxVo instanceof BdcYg || yqllxVo instanceof BdcJsydzjdsyq)) {
                                qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                            }
                        }
                        if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                            GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, bdcXmRel.getYqlid());
                            GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, bdcXmRel.getYqlid());
                            if ((gdFwsyq != null && gdFwsyq.getIszx() != 2) || (gdYg != null && gdYg.getIszx() != 2)) {
                                super.changeGdsjQszt(bdcXmRel, 1);
                            }
                        }
                    }
                }
                // 司法裁定办结后需要将查封全注销
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZY_SFCD)) {
                    bdcCfService.updateAdjudicationState(bdcXm, bdcXmRelList);
                }
            } else if (qllxVo instanceof BdcFdcqDz) {
                if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                    for (BdcXmRel bdcXmRel : bdcXmRelList) {
                        BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                        if (ybdcXm != null) {
                            QllxVo yqllxVo = qllxService.queryQllxVo(ybdcXm);
                            if (yqllxVo != null && (yqllxVo instanceof BdcFdcqDz || yqllxVo instanceof BdcYg)) {
                                qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                            }
                        }
                        if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                            GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, bdcXmRel.getYqlid());
                            GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, bdcXmRel.getYqlid());
                            if ((gdFwsyq != null && gdFwsyq.getIszx() != 2) || (gdYg != null && gdYg.getIszx() != 2)) {
                                super.changeGdsjQszt(bdcXmRel, 1);
                            }
                        }
                    }
                }
            } else if (qllxVo instanceof BdcJsydzjdsyq) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    if (ybdcXm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcXm);
                        if (yqllxVo != null && yqllxVo instanceof BdcJsydzjdsyq) {
                            qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                        }
                    }
                    if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                        GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, bdcXmRel.getYqlid());
                        if (gdTdsyq != null && gdTdsyq.getIszx() != 2) {
                            super.changeGdsjQszt(bdcXmRel, 1);
                        }
                    }
                }
            }
        }
        /**
         * @author bianwen
         * @description 修改当前权利状态
         */
        qllxService.endQllxZt(bdcXm);
    }

    /**
     * 新增一个BdcXmRel
     *
     * @param bdcXmRel
     * @param yproid
     */
    public void createBdcXmRel(BdcXmRel bdcXmRel, String yproid) {
        if (null != bdcXmRel && StringUtils.isNotBlank(yproid)) {
            bdcXmRel.setRelid(UUIDGenerator.generate18());
            bdcXmRel.setYproid(yproid);
            bdcXmRel.setYqlid("");
            bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
            entityMapper.saveOrUpdate(bdcXmRel, bdcXmRel.getRelid());
        }
    }

    @Override
    public void doExtraWork(BdcXm bdcXm) {
        String sqlxdm = bdcZdGlService.getWorkFlowSqlxdm(bdcXm.getProid());
        if (org.apache.commons.lang.StringUtils.isNotBlank(sqlxdm) && sqlxdm.equals(Constants.SQLX_JSYDSYQ_LHDJ)) {
            QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
            if (!(qllxVo instanceof BdcDyaq)) {
                bdcJsydsyqLhxxService.updateDjsjFwLhxx(bdcXm.getProid());
            }
        }
    }

}
