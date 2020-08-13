package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @version 1.0, 2016/5/31
 * @author<a href="mailto:jhj@gtmap.cn>zhoudefu</a>
 * @discription 换证抵押转发创建证书、证明
 */

public class TurnComplexHzDyProjectServiceImpl extends TurnProjectGzdjServiceImpl {
    @Autowired
    private BdcQlrService bdcQlrServicee;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcDyaqService bdcDyaqService;

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        List<BdcXm> bdcXmList;
        Boolean boolPreviewZs = false;
        if(org.apache.commons.lang3.StringUtils.isNotBlank(previewZs) && org.apache.commons.lang3.StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if(CollectionUtils.isNotEmpty(bdcXmList)) {
                List<BdcZs> bdcZsListTemp = null;
                StringBuilder ybdcqzh = new StringBuilder();
                Map<String, String> ybdcqzhMap = new HashMap<String, String>();
                for(BdcXm bdcxmTemp : bdcXmList) {
                    if(!bdcxmTemp.getQllx().equals(Constants.QLLX_DYAQ)) {
                        List<BdcQlr> bdcQlrList = bdcQlrServicee.queryBdcQlrByProid(bdcxmTemp.getProid());
                        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList) || bdcQlrService.isFbczContainBcz(bdcQlrList)) {
                            bdcZsListTemp = createBdcZs(bdcxmTemp,previewZs);
                            if(CollectionUtils.isNotEmpty(bdcZsListTemp)) {
                                bdcZsList.addAll(bdcZsListTemp);
                            }
                        } else {
                            bdcZsListTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList,boolPreviewZs);
                            if(CollectionUtils.isNotEmpty(bdcZsListTemp)) {
                                bdcZsList.addAll(bdcZsListTemp);
                            }
                            bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, bdcZsListTemp, bdcQlrList);
                            bdcXmZsRelService.creatBdcXmZsRel(bdcZsListTemp, bdcxmTemp.getProid());
                        }

                        for(BdcZs bdcZs : bdcZsListTemp) {
                            if (StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                                if(StringUtils.isNotBlank(String.valueOf(ybdcqzh))) {
                                    ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                }
                                else {
                                    ybdcqzh.append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                        ybdcqzhMap.put(bdcxmTemp.getBdcdyid(), String.valueOf(ybdcqzh));
                    }
                }
                //新生成的抵押证明的原证号是新产生所有权的不动产权证号,抵押的转移、变更yfczh为新产生所有权的不动产权证号
                for(BdcXm bdcxmTemp : bdcXmList) {
                    if(bdcxmTemp.getQllx().equals(Constants.QLLX_DYAQ)) {
                        for(Entry<String, String> Vo : ybdcqzhMap.entrySet()) {
                            if(StringUtils.equals(Vo.getKey(), bdcxmTemp.getBdcdyid())&&StringUtils.isNotBlank(Vo.getValue())&&StringUtils.isNotBlank(bdcxmTemp.getSqlx())) {
                                bdcxmTemp.setYbdcqzh(Vo.getValue());
                                if(bdcxmTemp.getSqlx().equals(Constants.SQLX_FWDYBG_DM)) {
                                    bdcxmTemp.setYfczh(Vo.getValue());
                                }
                                entityMapper.saveOrUpdate(bdcxmTemp, bdcxmTemp.getProid());
                            }
                        }

                        BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(bdcxmTemp.getProid());
                        if(bdcDyaq != null) {
                            String qlqtzk = bdcZsService.getQlqtzkByReplaceBdcqzh(bdcDyaq.getQlqtzk(), String.valueOf(ybdcqzh));
                            if(StringUtils.isNotBlank(qlqtzk)) {
                                bdcDyaq.setQlqtzk(qlqtzk);
                                bdcDyaqService.saveBdcDyaq(bdcDyaq);
                            }
                        }

                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                        //创建证书
                        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)) {
                            bdcZsListTemp = createBdcZs(bdcXm,previewZs);
                            if(CollectionUtils.isNotEmpty(bdcZsListTemp)) {
                                bdcZsList.addAll(bdcZsListTemp);
                            }
                        } else {
                            List<BdcZs> listTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList,boolPreviewZs);
                            if(CollectionUtils.isNotEmpty(listTemp)) {
                                bdcZsList.addAll(listTemp);
                            }
                            //zdd 生成权利人证书关系表
                            bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, listTemp, bdcQlrList);
                            //zdd 生成项目证书关系表
                            bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcxmTemp.getProid());
                        }
                    }
                }
            }
        }
        return bdcZsList;
    }
}
