package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcXtZsQlqtzkMapper;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZsCreatZsInfoService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lst
 * Date: 15-12-16
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class BdcZsCreatZsInfoServiceImpl implements BdcZsCreatZsInfoService {

    @Autowired
    BdcXtZsQlqtzkMapper bdcXtZsQlqtzkMapper;
    @Autowired
    BdcQlrService bdcQlrService;

    @Override
    @Transactional(readOnly = true)
    public BdcDyZs setQygyr(BdcXm bdcXm, String czr, BdcDyZs bdcDyZs) {
        String qlqtzk = "";
        String qlr = "";
        if (StringUtils.isNotBlank(bdcDyZs.getQlqtzk())) {
            qlqtzk = bdcDyZs.getQlqtzk();
        }
        StringBuilder qygyr = new StringBuilder();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        //qijiadong 证书qlqtzk中展示的其余共有人是除开zs表权利人字段的人
        qlr = bdcDyZs.getQlr();
        if (StringUtils.isNotBlank(qlr)) {
            String[] qlrs = qlr.split(ParamsConstants.KONGGE);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlrTemp : bdcQlrList) {
                    if (!CommonUtil.indexOfStrs(qlrs,bdcQlrTemp.getQlrmc())) {
                        qygyr.append(bdcQlrTemp.getQlrmc()).append(ParamsConstants.KONGGE);
                    }
                }
            }
        }
        //qijiadong 权利人为两个或以上只要求生成一本证书的时候，在证书的权利其他状况栏去掉其余共有人
        if (StringUtils.isNotBlank(bdcXm.getProid())) {
            List<BdcQlr> bdcCzrList = bdcQlrService.getBdcCzrListByProid(bdcXm.getProid());
            if (bdcCzrList.size() > 1) {
                HashMap mapxt = new HashMap();
                List<BdcXtZsQlqtzk> bdcXtZsQlqtzkList = bdcXtZsQlqtzkMapper.getBdcXtZsQlqtzk(mapxt);
                if (CollectionUtils.isNotEmpty(bdcXtZsQlqtzkList)) {
                    for (int i = 0; i < bdcXtZsQlqtzkList.size(); i++) {
                        String qlqtzkTemp = bdcXtZsQlqtzkList.get(i).getQlqtzkmb();
                        if (qlqtzkTemp.indexOf("qygyr") > -1&&StringUtils.isNotBlank(qygyr)) {
                            qlqtzk = qlqtzkTemp.replace("qygyr", qygyr) + "\n" + qlqtzk;
                            break;
                        }
                    }
                }
            }
        }
        bdcDyZs.setQlqtzk(qlqtzk);
        return bdcDyZs;
    }

    @Override
    public BdcDyZs setCzr(BdcXm bdcXm, String czr, BdcDyZs bdcDyZs) {
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        //共有方式按份共有中有共同共有
        if (bdcQlrService.isAfgyContainGtgy(bdcQlrList)) {
            String[] qlrs = czr.split(" ");
            czr = qlrs[0];
        }

        String[] qlrs = czr.split("、");
        String qlqtzk = "";
        if (StringUtils.isNotBlank(bdcDyZs.getQlqtzk())) {
            qlqtzk = bdcDyZs.getQlqtzk();
        }
        if (StringUtils.isNotBlank(czr)) {
            HashMap mapxt = new HashMap();
            mapxt.put("qllxdm",bdcXm.getQllx());
            List<BdcXtZsQlqtzk> bdcXtZsQlqtzkList = bdcXtZsQlqtzkMapper.getBdcXtZsQlqtzk(mapxt);
            if (CollectionUtils.isNotEmpty(bdcXtZsQlqtzkList)) {
                for (int i = 0; i < bdcXtZsQlqtzkList.size(); i++) {
                    String qlqtzkTemp = bdcXtZsQlqtzkList.get(i).getQlqtzkmb();
                    if (StringUtils.isNotBlank(qlqtzkTemp) && qlqtzkTemp.indexOf("czr") > -1&&StringUtils.isNotBlank(czr)) {
                        qlqtzk = qlqtzkTemp.replace("czr", czr) + "\n" + qlqtzk;
                    }
                    if (CollectionUtils.isNotEmpty(bdcQlrList) && StringUtils.isNotBlank(bdcQlrList.get(0).getGyfs()) && bdcQlrList.get(0).getGyfs().equals("2")&&
                            StringUtils.isNotBlank(qlqtzkTemp) && qlqtzkTemp.indexOf("gyfe") > -1) {
                        String gyfe = "";
                        for (int j = 0; j < qlrs.length; j++) {
                            for (BdcQlr bdcQlr : bdcQlrList) {
                                if (StringUtils.equals(bdcQlr.getQlrmc(), qlrs[j]) && StringUtils.isNotBlank(bdcQlr.getQlbl())) {
                                    if (j == 0) {
                                        gyfe = PublicUtil.percentage(bdcQlr.getQlbl());
                                    } else {
                                        gyfe = gyfe + "、" + PublicUtil.percentage(bdcQlr.getQlbl());
                                    }
                                }
                            }
                        }
                        qlqtzk = qlqtzk + qlqtzkTemp.replace("gyfe", gyfe);
                    }
                }
            }
        }
        bdcDyZs.setQlqtzk(qlqtzk);
        return bdcDyZs;
    }
}
