package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version 1.1.0, 2016/3/12.
 * @description 房屋所有权、抵押权同时预转现登记流程
 */
public class TurnComplexYzxdjProjectServiceImpl extends TurnProjectDefaultServiceImpl{

    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    GdFwService gdFwService;

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcXm> bdcXmList;
        List<BdcZs> list = new ArrayList<BdcZs>();
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcZs> listTemp = null;
                StringBuilder ybdcqzh = new StringBuilder();
                String syqProid = "";
                String syqQlid = "";
                for (BdcXm bdcxmTemp : bdcXmList) {
                    if(StringUtils.isBlank(bdcxmTemp.getQllx())){
                        continue;
                    }
                    if (!bdcxmTemp.getQllx().equals(Constants.QLLX_DYAQ)) {
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                        //创建证书
                        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                            listTemp  = createBdcZs(bdcxmTemp,previewZs);
                            if(CollectionUtils.isNotEmpty(listTemp))
                                list.addAll(listTemp);
                        }else{
                            listTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList,boolPreviewZs);
                            //zdd 生成权利人证书关系表
                            bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, listTemp, bdcQlrList);
                            //zdd 生成项目证书关系表
                            bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcxmTemp.getProid());
                        }

                        if (CollectionUtils.isNotEmpty(listTemp)) {
                            list.addAll(listTemp);
                            for (BdcZs bdcZs : listTemp) {
                                if (StringUtils.isNotBlank(ybdcqzh)) {
                                    ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                }
                                else {
                                    ybdcqzh.append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                        syqProid = bdcxmTemp.getProid();
                        QllxVo qllxVo = qllxService.queryQllxVo(bdcxmTemp);
                        if (qllxVo != null){
                            syqQlid = qllxVo.getQlid();
                        }
                    }
                }
                for (BdcXm bdcxmTemp : bdcXmList) {
                    if(StringUtils.isBlank(bdcxmTemp.getQllx())){
                        continue;
                    }
                    if (bdcxmTemp.getQllx().equals(Constants.QLLX_DYAQ)) {
                        //抵押证明的原证号是新产生的不动产权证
                        bdcxmTemp.setYbdcqzh(ybdcqzh.toString());
                        entityMapper.saveOrUpdate(bdcxmTemp,bdcxmTemp.getProid());
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                        //创建证书
                        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                            listTemp  = createBdcZs(bdcxmTemp,previewZs);
                            if(CollectionUtils.isNotEmpty(listTemp)) {
                                list.addAll(listTemp);
                            }
                        }else{
                            listTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList,boolPreviewZs);
                            if (CollectionUtils.isNotEmpty(listTemp)) {
                                list.addAll(listTemp);
                            }
                            //zdd 生成权利人证书关系表
                            bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp,listTemp,bdcQlrList);
                            //zdd 生成项目证书关系表
                            bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcxmTemp.getProid());
                        }

//                        Example example = new Example(BdcXmRel.class);
//                        example.createCriteria().andEqualTo("proid", bdcxmTemp.getProid());
//                        List<BdcXmRel> bdcXmRels = entityMapper.selectByExample(example);
//                        if (CollectionUtils.isNotEmpty(bdcXmRels)) {
//                            BdcXmRel bdcXmRel = bdcXmRels.get(0);
//                            if (StringUtils.isNotBlank(syqProid)) {
//                                bdcXmRel.setYproid(syqProid);
//                                bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
//                            }
//                            if (StringUtils.isNotBlank(syqQlid)){
//                                bdcXmRel.setYqlid(syqQlid);
//                            }
//                            entityMapper.saveOrUpdate(bdcXmRel,bdcXmRel.getRelid());
//                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;
        if(bdcXm != null&&StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            String bdcdyh = bdcBdcdy.getBdcdyh();
            qllxVo = qllxService.makeSureQllx(bdcXm);
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            BdcXmRel bdcXmRel = null;
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                bdcXmRel = bdcXmRelList.get(0);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                //首次证在不动产，分预告在过渡和预告在不动产两种
                if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)) {
                    BdcXm ybdcxm = null;
                    if(bdcXmRel != null){
                        ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    }
                    if (ybdcxm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
                        if (yqllxVo instanceof BdcFdcq && (qllxVo instanceof BdcFdcq)) {  //初始化房地产权
                            qllxVo = yqllxVo;
                            BdcFdcq fdcq = (BdcFdcq) qllxVo;
                            fdcq.setQlid(UUIDGenerator.generate18());
                            fdcq.setProid(bdcXm.getProid());
                            fdcq.setYwh(bdcXm.getBh());
                            fdcq.setDbr(null);
                            fdcq.setDjsj(null);
                        }
                    }
                    if (qllxVo instanceof BdcDyaq) { //初始化抵押权
                        List<QllxVo> bdcYgList = qllxService.getQllxByBdcdyh(new BdcYg(), bdcdyh);
                        if (CollectionUtils.isNotEmpty(bdcYgList)) { //预告抵押存在不动产登记
                            BdcYg bdcYg;
                            for (int i = 0; i < bdcYgList.size(); i++) {
                                if (bdcYgList.get(i) instanceof BdcYg) {
                                    bdcYg = (BdcYg) bdcYgList.get(i);
                                    if (((StringUtils.isNotBlank(bdcYg.getYgdjzl()) && (bdcYg.getYgdjzl().equals(Constants.YGDJZL_YGSPFDY))) || (StringUtils.isBlank(bdcYg.getYgdjzl()) && bdcYg.getQdjg() != null))) {
                                        BdcDyaq dyaq = (BdcDyaq) qllxVo;
                                        dyaq.setQlid(UUIDGenerator.generate18());
                                        dyaq.setProid(bdcXm.getProid());
                                        dyaq.setYwh(bdcXm.getBh());
                                        dyaq.setDbr(null);
                                        dyaq.setDjsj(null);
                                        dyaq.setQllx(bdcXm.getQllx());
                                        dyaq.setBdcdyid(bdcXm.getBdcdyid());
                                        dyaq.setFj(bdcYg.getFj());
                                        dyaq.setGyqk(bdcYg.getGyqk());
                                        dyaq.setBdbzzqse(bdcYg.getQdjg());
                                        dyaq.setZwlxksqx(bdcYg.getZwlxksqx());
                                        dyaq.setZwlxjsqx(bdcYg.getZwlxjsqx());
                                    }
                                }
                            }



                        }else{//预告存在过渡
                            List<Map> gdygLst = qllxService.getGdYgByBdcdyh(bdcdyh);
                            if(CollectionUtils.isNotEmpty(gdygLst)){
                                String ygdjlx;
                                Double dyje = null;
                                for(Map gdYg : gdygLst){
                                    if (gdYg != null) {
                                        ygdjlx = (String) gdYg.get("YGDJZL");
                                        if(null != gdYg.get("QDJG")){
                                            BigDecimal bd = (BigDecimal)gdYg.get("QDJG");
                                            dyje = bd.doubleValue();
                                        }
                                        if (StringUtils.isNotBlank(ygdjlx) && ((ygdjlx.equals(Constants.YGDJZL_YGSPFDY) || ygdjlx.equals(Constants.YGDJZL_QTYGSPFDY))
                                                || (StringUtils.isBlank(ygdjlx) && dyje != null)) ) {
                                            BdcDyaq dyaq = (BdcDyaq) qllxVo;
                                            dyaq.setQlid(UUIDGenerator.generate18());
                                            dyaq.setProid(bdcXm.getProid());
                                            dyaq.setYwh(bdcXm.getBh());
                                            dyaq.setDbr(null);
                                            dyaq.setDjsj(null);
                                            dyaq.setQllx(bdcXm.getQllx());
                                            dyaq.setBdcdyid(bdcXm.getBdcdyid());
                                            dyaq.setFj((String) gdYg.get("FJ"));
                                            dyaq.setBdbzzqse(dyje);
                                            dyaq.setZwlxksqx((Date)gdYg.get("DYKSRQ"));
                                            dyaq.setZwlxjsqx((Date)gdYg.get("DYJSRQ"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else { //首次证在过渡，预告一定在过渡
                    if(null != bdcXmRel && StringUtils.isNotBlank(bdcXmRel.getYqlid())){
                        String gdQlid = bdcXmRel.getYqlid();
                        if(StringUtils.isNotBlank(gdQlid)) {
                            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdQlid);
                            if (null != gdFwsyq&&qllxVo instanceof BdcFdcq) {
                                BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                                bdcFdcq.setTdsyksqx(gdFwsyq.getTdsyksrq());
                                bdcFdcq.setTdsyjsqx(gdFwsyq.getTdsyjsrq());
                            }
                            if (qllxVo instanceof BdcDyaq) {
                                List<Map> gdygLst = qllxService.getGdYgByBdcdyh(bdcdyh);
                                if(CollectionUtils.isNotEmpty(gdygLst)){
                                    String ygdjlx;
                                    Double dyje = null;
                                    for(Map gdYg : gdygLst){
                                        if (gdYg != null) {
                                            ygdjlx = (String) gdYg.get("YGDJZL");
                                            if(null != gdYg.get("QDJG")){
                                                BigDecimal bd = (BigDecimal)gdYg.get("QDJG");
                                                dyje = bd.doubleValue();
                                            }
                                            if (StringUtils.isNotBlank(ygdjlx) && ((ygdjlx.equals(Constants.YGDJZL_YGSPFDY) || ygdjlx.equals(Constants.YGDJZL_QTYGSPFDY))
                                                    || (StringUtils.isBlank(ygdjlx) && dyje != null)) ) {
                                                BdcDyaq dyaq = (BdcDyaq) qllxVo;
                                                dyaq.setQlid(UUIDGenerator.generate18());
                                                dyaq.setProid(bdcXm.getProid());
                                                dyaq.setYwh(bdcXm.getBh());
                                                dyaq.setDbr(null);
                                                dyaq.setDjsj(null);
                                                dyaq.setQllx(bdcXm.getQllx());
                                                dyaq.setBdcdyid(bdcXm.getBdcdyid());
                                                dyaq.setFj((String) gdYg.get("FJ"));
                                                dyaq.setBdbzzqse(dyje);
                                                dyaq.setZwlxksqx((Date)gdYg.get("DYKSRQ"));
                                                dyaq.setZwlxjsqx((Date)gdYg.get("DYJSRQ"));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
                    }

                }

                QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
                if (qllxVoTemp == null) {
                    qllxVo = qllxService.initQllxVoFromOntBdcXm(qllxVo,bdcXm.getProid());
                    entityMapper.insertSelective(qllxVo);
                } else {
                    qllxVo.setQlid(qllxVoTemp.getQlid());
                    qllxVo = qllxService.initQllxVoFromOntBdcXm(qllxVo,bdcXm.getProid());
                    entityMapper.updateByPrimaryKeySelective(qllxVo);
                }
            }
        }
        return qllxVo;
    }
}
