package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.1.0, 2016/04/14.
 * @description 土地分割、合并换证登记
 */
public class TurnComplexFgHbHzProjectServiceImpl extends TurnProjectBgdjServiceImpl {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;


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

                //分割出去宗地的首次和剩余宗地的变更
                for (BdcXm bdcxmTemp : bdcXmList) {
                    if (!bdcxmTemp.getQllx().equals(Constants.QLLX_DYAQ)) {
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                        //创建证书
                        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                            listTemp  = createBdcZs(bdcxmTemp,previewZs);
                            if(CollectionUtils.isNotEmpty(listTemp)) {
                                list.addAll(listTemp);
                            }
                        } else{
                            listTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList,boolPreviewZs);
                            if (CollectionUtils.isNotEmpty(listTemp)) {
                                list.addAll(listTemp);
                                //zdd 生成权利人证书关系表
                                bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, listTemp, bdcQlrList);
                                //zdd 生成项目证书关系表
                                bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcxmTemp.getProid());
                            }
                        }

                        if (StringUtils.equals(bdcxmTemp.getDjlx(),Constants.DJLX_BGDJ_DM)){
                            for (BdcZs bdcZs : listTemp) {
                                if (StringUtils.isNotBlank(ybdcqzh)) {
                                    ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                }
                                else {
                                    ybdcqzh.append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                    }
                }

                //剩余宗地的抵押变更
                for (BdcXm bdcxmTemp : bdcXmList) {
                    if (bdcxmTemp.getQllx().equals(Constants.QLLX_DYAQ)) {
                        //抵押证明的原证号是新产生的不动产权证
                        bdcxmTemp.setYbdcqzh(String.valueOf(ybdcqzh));
                        entityMapper.saveOrUpdate(bdcxmTemp,bdcxmTemp.getProid());
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                            listTemp  = createBdcZs(bdcxmTemp,previewZs);
                            if(CollectionUtils.isNotEmpty(listTemp)) {
                                list.addAll(listTemp);
                            }
                        }else{
                            //创建证书
                            listTemp = bdcZsService.creatBdcqz(bdcxmTemp, bdcQlrList,boolPreviewZs);
                            if (CollectionUtils.isNotEmpty(listTemp)) {
                                list.addAll(listTemp);
                            }
                            //zdd 生成权利人证书关系表
                            bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp,listTemp,bdcQlrList);
                            //zdd 生成项目证书关系表
                            bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcxmTemp.getProid());
                        }
                    }
                }
            }
        }
        return list;
    }
}
