package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
 * @version 1.1, 2016/3/11.
 * @description 从新建走，选多个不动产单元或多个预告证明的批量预告登记、批量预告抵押登记
 */
public class TurnComplexYgdjPlProjectServiceImpl extends TurnProjectDydjServiceImpl {

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
       /*
         *zdd 业务中如果权利人相同，将此方法放在项目表循环外   如果不同放在循环内
         */
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                //在循环外 只生成一本证书 循环内生成多个项目证书关系  权利人证书关系
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                    list = createBdcZs(bdcXm,previewZs);
                }else{
                    list = bdcZsService.creatDyBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
                }

                for (BdcXm bdcxmTemp : bdcXmList) {
                    //zdd 生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp,list,bdcQlrList);
                    //zdd 生成项目证书关系表
                    bdcXmZsRelService.creatBdcXmZsRel(list, bdcxmTemp.getProid());
                }
            }
        }
        return list;
    }
}
