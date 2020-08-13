package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/*
 * @author <a href="mailto:liangqing@gtmap.cn;">liangqing</a>
 * @version 1.0,2016/4/29
 * @description  针对一个流程，多个不动产单元的转移登记，创建生成多本证的业务逻辑
 */

public class TurnComplexZydjProjectServiceImpl extends TurnProjectZydjServiceImpl {
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;

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
