package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

/**
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2017-03-15
 * @description 批量界面流程转发服务
 */
public class TurnProjectArbitraryServiceImpl extends TurnProjectDefaultServiceImpl {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    ProjectService projectService;

    private static final String PARAMETER_QLLXNOTDY = "qllxNotDy";
    private static final String PARAMETER_QLLXDY = "qllxDy";


    /**
     * @param bdcqzFlag  (1：一证多房；0：一证一房)
     * @param bdcqzmFlag (1：多抵一；0：多抵多)
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 生成证书(任意流程)
     */
    @Override
    public List<BdcZs> saveBdcZsArbitrary(final BdcXm bdcXm, final String bdcqzFlag, final String bdcqzmFlag) {
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            //创建证书时先删除证书和权利人和证书关系
            bdcZsService.delBdcZsByWiid(bdcXm.getWiid());
            //jyl 列出同一流程实例中所有的持证人，持证人肯定是权利人，产权证和证明的分开列，先生成产权证再生成证明，方便证明的原证号处理
            //jyl 产权证持证人
            HashMap bdcqzCzrMap = new HashMap();
            bdcqzCzrMap.put(ParamsConstants.WIID_LOWERCASE, bdcXm.getWiid());
            bdcqzCzrMap.put("sfczr", Constants.CZR);
            bdcqzCzrMap.put(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
            bdcqzCzrMap.put(PARAMETER_QLLXNOTDY, true);
            List<BdcQlr> bdcqzCzrList = bdcQlrService.queryBdcQlrList(bdcqzCzrMap);
            //jyl 产权证明持证人
            HashMap bdcqzmCzrMap = new HashMap();
            bdcqzmCzrMap.put(ParamsConstants.WIID_LOWERCASE, bdcXm.getWiid());
            bdcqzmCzrMap.put("sfczr", Constants.CZR);
            bdcqzmCzrMap.put(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
            bdcqzmCzrMap.put(PARAMETER_QLLXDY, true);
            List<BdcQlr> bdcqzmCzrList = bdcQlrService.queryBdcQlrList(bdcqzmCzrMap);
            //jyl 一证多房生成证书
            if (StringUtils.isNotBlank(bdcqzFlag) && StringUtils.equals(bdcqzFlag, "1") && CollectionUtils.isNotEmpty(bdcqzCzrList)) {
                //jyl 存放已经生成证书的持证人
                List<BdcQlr> yscCzrList = new ArrayList<BdcQlr>();
                for (BdcQlr bdcqzCzr : bdcqzCzrList) {
                    if (CollectionUtils.isEmpty(yscCzrList)) {
                        //jyl 同一权利人对应的非抵押权的项目
                        HashMap bdcXmMap = new HashMap();
                        bdcXmMap.put(ParamsConstants.WIID_LOWERCASE, bdcXm.getWiid());
                        bdcXmMap.put(PARAMETER_QLLXNOTDY, true);
                        bdcXmMap.put(ParamsConstants.QLRMC_LOWERCASE, bdcqzCzr.getQlrmc());
                        bdcXmMap.put(ParamsConstants.QLRZJH_LOWERCASE, bdcqzCzr.getQlrzjh());
                        List<BdcXm> bdcXmByQlrList = bdcXmService.getBdcXmByQlr(bdcXmMap);
                        List<BdcZs> bdcZstempList = creatZsManyToOne(bdcqzCzr, bdcXmByQlrList);
                        if (CollectionUtils.isNotEmpty(bdcZstempList)) {
                            //jyl 生成证书成功
                            yscCzrList.add(bdcqzCzr);
                            bdcZsList.addAll(bdcZstempList);
                        }
                    } else {
                        Boolean creat = judgeCreatZs(yscCzrList, bdcqzCzr);
                        if (creat) {
                            //jyl 同一权利人对应的非抵押权的项目
                            HashMap bdcXmMap = new HashMap();
                            bdcXmMap.put(ParamsConstants.WIID_LOWERCASE, bdcXm.getWiid());
                            bdcXmMap.put(PARAMETER_QLLXNOTDY, true);
                            bdcXmMap.put(ParamsConstants.QLRMC_LOWERCASE, bdcqzCzr.getQlrmc());
                            bdcXmMap.put(ParamsConstants.QLRZJH_LOWERCASE, bdcqzCzr.getQlrzjh());
                            List<BdcXm> bdcXmByQlrList = bdcXmService.getBdcXmByQlr(bdcXmMap);
                            List<BdcZs> bdcZstempList = creatZsManyToOne(bdcqzCzr, bdcXmByQlrList);
                            if (CollectionUtils.isNotEmpty(bdcZstempList)) {
                                //jyl 生成证书成功
                                yscCzrList.add(bdcqzCzr);
                                bdcZsList.addAll(bdcZstempList);
                            }
                        }
                    }
                }
            } else {
                List<BdcZs> bdcZstempList = creatZsOneToOne(bdcqzCzrList);
                if (CollectionUtils.isNotEmpty(bdcZstempList)) {
                    bdcZsList.addAll(bdcZstempList);
                }
            }
            if (StringUtils.isNotBlank(bdcqzmFlag) && StringUtils.equals(bdcqzmFlag, "1") && CollectionUtils.isNotEmpty(bdcqzmCzrList)) {
                //jyl 存放已经生成证书的持证人
                List<BdcQlr> yscCzrList = new ArrayList<BdcQlr>();
                for (BdcQlr bdcqzmCzr : bdcqzmCzrList) {
                    if (CollectionUtils.isEmpty(yscCzrList)) {
                        //jyl 同一权利人对应的非抵押权的项目
                        HashMap bdcXmMap = new HashMap();
                        bdcXmMap.put(ParamsConstants.WIID_LOWERCASE, bdcXm.getWiid());
                        bdcXmMap.put(PARAMETER_QLLXDY, true);
                        bdcXmMap.put(ParamsConstants.QLRMC_LOWERCASE, bdcqzmCzr.getQlrmc());
                        bdcXmMap.put(ParamsConstants.QLRZJH_LOWERCASE, bdcqzmCzr.getQlrzjh());
                        List<BdcXm> bdcXmByQlrList = bdcXmService.getBdcXmByQlr(bdcXmMap);
                        List<BdcZs> bdcZstempList = creatZsManyToOne(bdcqzmCzr, bdcXmByQlrList);
                        if (CollectionUtils.isNotEmpty(bdcZstempList)) {
                            //jyl 生成证书成功
                            yscCzrList.add(bdcqzmCzr);
                            bdcZsList.addAll(bdcZstempList);
                        }
                    } else {
                        Boolean creat = judgeCreatZs(yscCzrList, bdcqzmCzr);
                        if (creat) {
                            //jyl 同一权利人对应的非抵押权的项目
                            HashMap bdcXmMap = new HashMap();
                            bdcXmMap.put(ParamsConstants.WIID_LOWERCASE, bdcXm.getWiid());
                            bdcXmMap.put(PARAMETER_QLLXDY, true);
                            bdcXmMap.put(ParamsConstants.QLRMC_LOWERCASE, bdcqzmCzr.getQlrmc());
                            bdcXmMap.put(ParamsConstants.QLRZJH_LOWERCASE, bdcqzmCzr.getQlrzjh());
                            List<BdcXm> bdcXmByQlrList = bdcXmService.getBdcXmByQlr(bdcXmMap);
                            List<BdcZs> bdcZstempList = creatZsManyToOne(bdcqzmCzr, bdcXmByQlrList);
                            if (CollectionUtils.isNotEmpty(bdcZstempList)) {
                                //jyl 生成证书成功
                                yscCzrList.add(bdcqzmCzr);
                                bdcZsList.addAll(bdcZstempList);
                            }
                        }
                    }
                }
            } else {
                List<BdcZs> bdcZstempList = creatZsOneToOne(bdcqzmCzrList);
                if (CollectionUtils.isNotEmpty(bdcZstempList)) {
                    bdcZsList.addAll(bdcZstempList);
                }
            }
        }
        return bdcZsList;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 生成证书(多个不动产单元一个证书)
     */
    private List<BdcZs> creatZsManyToOne(BdcQlr bdcQlr, List<BdcXm> bdcXmList) {
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        //jyl 列出该权利人下的其余共有人，非持证人肯定是权利人，为了生成后续的BdcZsQlrRel
        List<BdcQlr> gyrList = null;
        if (StringUtils.isNotBlank(bdcQlr.getQygyr())) {
            HashMap gyrMap = new HashMap();
            gyrMap.put("proid", bdcQlr.getProid());
            gyrMap.put(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
            gyrMap.put("qygyr", bdcQlr.getQygyr());
            gyrList = bdcQlrService.queryBdcQlrList(gyrMap);
        }
        BdcXm bdcxmTemp = bdcXmService.getBdcXmByProid(bdcQlr.getProid());
        if (StringUtils.equals(Constants.QLLX_DYAQ, bdcxmTemp.getQllx())&&CollectionUtils.isNotEmpty(bdcXmList)) {
            StringBuilder yzh = new StringBuilder();
            for (BdcXm bdcXmTemp : bdcXmList) {
                //jyl 确定转移抵押里的抵押原不动产权证
                //jyl 判断是不是转移抵押
                if (StringUtils.equals(Constants.QLLX_DYAQ, bdcXmTemp.getQllx())) {
                    List<BdcXm> bdcXms = bdcXmService.getBdcXmListByWiidAndBdcdyid(bdcXmTemp.getWiid(), bdcXmTemp.getBdcdyid());
                    if (CollectionUtils.isNotEmpty(bdcXms)) {
                        for (BdcXm bdcXm : bdcXms) {
                            if (!StringUtils.equals(Constants.QLLX_DYAQ, bdcXm.getQllx())) {
                                String yzhTemp = bdcZsService.getYzhByProidAndBdcid(bdcXmTemp.getProid(), bdcXmTemp.getBdcdyid());
                                if(StringUtils.isNotBlank(yzhTemp)){
                                    if(StringUtils.isNotBlank(String.valueOf(yzh))){
                                        yzh.append(",").append(yzhTemp);
                                    }else{
                                        yzh.append(yzhTemp);
                                    }
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
            for (BdcXm bdcXmTemp : bdcXmList) {
                bdcXmTemp.setYbdcqzh(String.valueOf(yzh));
                bdcXmService.saveBdcXm(bdcXmTemp);
            }
        }
        BdcZs bdcZs = bdcZsService.creatBdcqzArbitrary(bdcxmTemp, bdcQlr, null);
        if (bdcZs != null) {
            bdcZsList.add(bdcZs);
            //生成权利人证书关系表
            bdcZsQlrRelService.creatBdcZsQlrRelArbitrary(bdcZs, bdcQlr, gyrList);
            //生成项目证书关系表
            bdcXmZsRelService.creatBdcXmZsRelArbitrary(bdcZs.getZsid(), bdcXmList);
        }
        return bdcZsList;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 生成证书(一个不动产单元一个证书)
     */
    private List<BdcZs> creatZsOneToOne(List<BdcQlr> czrList) {
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        if (CollectionUtils.isNotEmpty(czrList)) {
            //jyl 每个持证人都要生成一本证
            for (BdcQlr bdcQlr : czrList) {
                //jyl 为多抵一预留
                List<BdcXm> bdcXmList = new ArrayList<BdcXm>();
                //jyl 列出该权利人下的其余共有人，非持证人肯定是权利人，为了生成后续的BdcZsQlrRel
                List<BdcQlr> gyrList = null;
                if (StringUtils.isNotBlank(bdcQlr.getQygyr())) {
                    HashMap gyrMap = new HashMap();
                    gyrMap.put("proid", bdcQlr.getProid());
                    gyrMap.put(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
                    gyrMap.put("qygyr", bdcQlr.getQygyr());
                    gyrList = bdcQlrService.queryBdcQlrList(gyrMap);
                }
                BdcXm bdcxmTemp = bdcXmService.getBdcXmByProid(bdcQlr.getProid());
                //jyl 确定转移抵押里的抵押原不动产权证
                //jyl 判断是不是转移抵押
                if (StringUtils.equals(Constants.QLLX_DYAQ, bdcxmTemp.getQllx())) {
                    List<BdcXm> bdcXms = bdcXmService.getBdcXmListByWiidAndBdcdyid(bdcxmTemp.getWiid(), bdcxmTemp.getBdcdyid());
                    if (CollectionUtils.isNotEmpty(bdcXms)) {
                        for (BdcXm bdcXm : bdcXms) {
                            if (!StringUtils.equals(Constants.QLLX_DYAQ, bdcxmTemp.getQllx())) {
                                String yzh = bdcZsService.getYzhByProidAndBdcid(bdcxmTemp.getProid(), bdcxmTemp.getBdcdyid());
                                if (StringUtils.isNotBlank(yzh)) {
                                    bdcxmTemp.setYbdcqzh(yzh);
                                    bdcXmService.saveBdcXm(bdcxmTemp);
                                }
                            }
                        }
                    }
                }
                bdcXmList.add(bdcxmTemp);
                BdcZs bdcZs = bdcZsService.creatBdcqzArbitrary(bdcxmTemp, bdcQlr, null);
                if (bdcZs != null) {
                    bdcZsList.add(bdcZs);
                    //生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRelArbitrary(bdcZs, bdcQlr, gyrList);
                    //生成项目证书关系表
                    bdcXmZsRelService.creatBdcXmZsRelArbitrary(bdcZs.getZsid(), bdcXmList);
                }
            }
        }
        return bdcZsList;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 判断该持证人是否能生成证书
     */
    private Boolean judgeCreatZs(List<BdcQlr> yscCzrList, BdcQlr czr) {
        Boolean flag = true;
        for (BdcQlr yscCzr : yscCzrList) {
            if (StringUtils.equals(yscCzr.getQlrmc(), czr.getQlrmc()) && StringUtils.equals(yscCzr.getQlrdlrzjh(), czr.getQlrdlrzjh())) {
                //jyl 相同表示已经生成证书了，不用再生成了
                flag = false;
            }
        }
        return flag;
    }
}
