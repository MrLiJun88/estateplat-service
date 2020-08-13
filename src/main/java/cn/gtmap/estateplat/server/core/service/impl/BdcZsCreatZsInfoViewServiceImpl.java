package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcDyZs;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXtZsQlqtzk;
import cn.gtmap.estateplat.server.core.mapper.BdcXtZsQlqtzkMapper;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcZsCreatZsInfoService;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
public class BdcZsCreatZsInfoViewServiceImpl implements BdcZsCreatZsInfoService {

    @Autowired
    BdcXtZsQlqtzkMapper bdcXtZsQlqtzkMapper;
    @Autowired
    BdcQlrService bdcQlrService;


    @Override
    public BdcDyZs setQygyr(BdcXm bdcXm, String czr, BdcDyZs bdcDyZs) {
        String qlqtzk = "";
        if (StringUtils.isNotBlank(bdcDyZs.getQlqtzk())) {
            qlqtzk = bdcDyZs.getQlqtzk();
        }
        List<BdcQlr> qlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        StringBuilder qygyr = new StringBuilder();
        if (qlrList.size() > 1) {
            for (BdcQlr bdcQlr : qlrList) {
                if (!StringUtils.equals(bdcQlr.getQlrmc(), czr)) {
                    qygyr.append(bdcQlr.getQlrmc()).append(" ");
                }
            }
        } else {
            return bdcDyZs;
        }
        if (StringUtils.isNotBlank(bdcXm.getProid())) {
            List<BdcQlr> bdcCzrList = bdcQlrService.getBdcCzrListByProid(bdcXm.getProid());
            if (bdcCzrList.size() > 1) {
                HashMap mapxt = new HashMap();
                mapxt.put("qllxdm", bdcXm.getQllx());
                List<BdcXtZsQlqtzk> bdcXtZsQlqtzkList = bdcXtZsQlqtzkMapper.getBdcXtZsQlqtzk(mapxt);
                if (CollectionUtils.isNotEmpty(bdcXtZsQlqtzkList)) {
                    for (int i = 0; i < bdcXtZsQlqtzkList.size(); i++) {
                        String qlqtzkTemp = bdcXtZsQlqtzkList.get(i).getQlqtzkmb();
                        if (qlqtzkTemp.indexOf("qygyr") > -1 && StringUtils.isNotBlank(qygyr)) {
                            qlqtzk = qlqtzkTemp.replace("qygyr", qygyr) + "\n" + qlqtzk;
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
        String[] qlrs=czr.split("、");
        String qlqtzk = "";
        if (StringUtils.isNotBlank(bdcDyZs.getQlqtzk())) {
            qlqtzk = bdcDyZs.getQlqtzk();
        }
        if(StringUtils.isNotBlank(czr)){
            HashMap mapxt = new HashMap();
            List<BdcXtZsQlqtzk> bdcXtZsQlqtzkList = bdcXtZsQlqtzkMapper.getBdcXtZsQlqtzk(mapxt);
            if (CollectionUtils.isNotEmpty(bdcXtZsQlqtzkList)) {
                for (int i = 0; i < bdcXtZsQlqtzkList.size(); i++) {
                    String qlqtzkTemp = bdcXtZsQlqtzkList.get(i).getQlqtzkmb();
                    if (qlqtzkTemp.indexOf("czr") > -1&&StringUtils.isNotBlank(czr)) {
                        qlqtzk = qlqtzkTemp.replace("czr", czr) + "\n" + qlqtzk;
                    }
                    if(bdcQlrList.get(0).getGyfs().equals("2")&&qlqtzkTemp.indexOf("gyfe") > -1){
                        String gyfe="";
                        for(int j=0;j<qlrs.length;j++){
                            for(BdcQlr bdcQlr:bdcQlrList){
                                if(StringUtils.equals(bdcQlr.getQlrmc(),qlrs[j])&&StringUtils.isNotBlank(bdcQlr.getQlbl())){
                                    if(j==0){
                                        gyfe= PublicUtil.percentage(bdcQlr.getQlbl());
                                    }else{
                                        gyfe=gyfe+"、"+PublicUtil.percentage(bdcQlr.getQlbl());
                                    }
                                }
                            }
                        }
                        qlqtzk=qlqtzk+qlqtzkTemp.replace("gyfe", gyfe);
                    }
                }
            }
        }
        bdcDyZs.setQlqtzk(qlqtzk);
        return bdcDyZs;
    }
}
