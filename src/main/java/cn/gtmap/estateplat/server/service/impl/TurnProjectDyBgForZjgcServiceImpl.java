package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @version 1.0 16-10-27
 * @author: bianwen
 * @description 在建工程抵押变更
 */
public class TurnProjectDyBgForZjgcServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        String zjgcdyFw = "";
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, bdcXm.getSqlx())) {
            zjgcdyFw = "true";
        }
        QllxVo qllxVo = null;
        qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, bdcXm.getProid());
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                qllxVo = qllxService.makeSureQllx(bdcXm);
                if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    if (ybdcxm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
                        if (yqllxVo == null) {
                            yqllxVo = qllxService.makeSureQllx(bdcXm);
                        }
                        yqllxVo.setQlid(UUIDGenerator.generate18());
                        yqllxVo.setProid(bdcXm.getProid());
                        yqllxVo.setYwh(bdcXm.getBh());
                        yqllxVo.setDbr(null);
                        yqllxVo.setDjsj(null);
                        yqllxVo.setFj("");
                        yqllxVo.setQszt(0);
                        qllxVo = yqllxVo;
                    }
                }

                qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
                entityMapper.insertSelective(qllxVo);

                List<BdcZjjzwxx> bdcZjjzwxxList = initZjjzwxx(bdcXmRel, zjgcdyFw);
                if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                    entityMapper.batchSaveSelective(bdcZjjzwxxList);
                }
            }
        }
        return qllxVo;
    }

    public List<BdcZjjzwxx> initZjjzwxx(BdcXmRel bdcXmRel, String zjgcdyFw) {
        List<BdcZjjzwxx> zjjzwList = new ArrayList<BdcZjjzwxx>();
        if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
            Example example = new Example(BdcZjjzwxx.class);
            example.createCriteria().andEqualTo("proid", bdcXmRel.getYproid()).andEqualTo("dyzt", "0");
            List<BdcZjjzwxx> yBdcZjjzwList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(yBdcZjjzwList)) {
                for (BdcZjjzwxx zjjzwxx : yBdcZjjzwList) {
                    zjjzwxx.setProid(bdcXmRel.getProid());
                    zjjzwxx.setZjwid(UUIDGenerator.generate());
                    zjjzwxx.setDyzt("0");
                    zjjzwList.add(zjjzwxx);
                }
            }
        }
        return zjjzwList;
    }

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        String zjgcdyFw = "";
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, bdcXm.getSqlx())) {
            zjgcdyFw = "true";
        }
        List<BdcZs> list = null;
        if (StringUtils.equals(zjgcdyFw, "true")) {
            list = saveBdcZsForFw(bdcXm,previewZs);
        } else {
            list = saveBdcZsForTd(bdcXm,previewZs);
        }
        return list;
    }

    public List<BdcZs> saveBdcZsForTd(final BdcXm bdcXm,final String previewZs) {
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        List<BdcZs> list = bdcZsService.creatBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
        bdcZsQlrRelService.creatBdcZsQlrRel(bdcXm, list, bdcQlrList);
        bdcXmZsRelService.creatBdcXmZsRel(list, bdcXm.getProid());
        return list;
    }

    public List<BdcZs> saveBdcZsForFw(final BdcXm bdcXm,final String previewZs) {
        List<BdcXm> bdcXmList;
        List<BdcZs> list = new ArrayList<BdcZs>();
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                if (bdcQlrService.isAfgyContainGtgy(bdcQlrList)) {
                    list = createBdcZs(bdcXm,previewZs);
                } else {
                    list = bdcZsService.creatDyBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
                    for (BdcXm bdcxmTemp : bdcXmList) {
                        bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, list, bdcQlrList);
                        bdcXmZsRelService.creatBdcXmZsRel(list, bdcxmTemp.getProid());
                    }
                }

            }
        }
        return list;
    }
}
