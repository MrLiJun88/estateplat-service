package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcQllxService;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.thread.BdcComplexScZsPreviewThread;
import cn.gtmap.estateplat.server.thread.BdcPreviewInfoForCreateBdczqhThread;
import cn.gtmap.estateplat.server.thread.BdcThreadEngine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
 * @Time 2020/8/5 15:13
 * @description 一个流程  多个不动产单元  权利人相同  产生一本证书逻辑（商品房首次批量发证）
 */
public class TurnComplexScdjCybzProjectServiceImpl extends TurnProjectDefaultServiceImpl {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public List<BdcZs> saveBdcZs(final BdcXm bdcXm, final String previewZs) {
        List<BdcXm> bdcXmList;
        List<BdcZs> list = new ArrayList<BdcZs>();

        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            bdcXmList = bdcXmService.getBdcXmListOrderByBdcdyh(map);

            if (CollectionUtils.isNotEmpty(bdcXmList)) {

                list = bdcZsService.createBdcZs(bdcXm, previewZs, this.userid);
                for (BdcXm bdcxmTemp : bdcXmList) {
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                    //zdd 生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp, list, bdcQlrList);
                    //zdd 生成项目证书关系表
                    bdcXmZsRelService.creatBdcXmZsRel(list, bdcxmTemp.getProid());
                }
            }

        }
        return list;
    }

    @Override
    public void completeZsInfo(BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                bdcZsService.completeZsInfo(bdcXm, bdcZsList.get(0), this.userid, null);
            }
        }
    }

}
