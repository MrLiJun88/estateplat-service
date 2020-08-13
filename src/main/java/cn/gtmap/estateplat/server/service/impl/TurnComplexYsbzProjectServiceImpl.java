package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0, 2016/6/23
 * @author<a href="mailto:jhj@gtmap.cn>jhj</a>
 * @discription 批量换证转发
 */

public class TurnComplexYsbzProjectServiceImpl extends TurnProjectGzdjServiceImpl {

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
                for (BdcXm bdcXmTemp : bdcXmList) {
                    //获取当前项目的权利人
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmTemp.getProid());
                    //创建证书
                    if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                        List<BdcZs> bdcZsList  = createBdcZs(bdcXmTemp,previewZs);
                        if(CollectionUtils.isNotEmpty(bdcZsList))
                            list.addAll(bdcZsList);
                    } else{
                        List<BdcZs> bdcZsList = bdcZsService.creatBdcqz(bdcXmTemp, bdcQlrList,boolPreviewZs);
                        if (CollectionUtils.isNotEmpty(bdcZsList))
                            list.addAll(bdcZsList);

                        //zdd 生成权利人证书关系表
                        bdcZsQlrRelService.creatBdcZsQlrRel(bdcXmTemp, bdcZsList, bdcQlrList);
                        //zdd 生成项目证书关系表
                        bdcXmZsRelService.creatBdcXmZsRel(bdcZsList, bdcXmTemp.getProid());
                    }
                }
            }
        }
        return list;
    }
}
