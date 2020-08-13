package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
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
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version 1.1.0, 2016/3/11.
 * @description 一个流程  多个不动产单元  权利人相同  产生多本证书逻辑（商品房首次批量发证）
 */
public class TurnComplexScdjProjectServiceImpl extends TurnProjectDefaultServiceImpl {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcThreadEngine bdcThreadEngine;
    @Autowired
    private BdcZsService bdcZsService;
    @Override
    public List<BdcZs> saveBdcZs(final BdcXm bdcXm, final String previewZs) {
        List<BdcXm> bdcXmList;
        List<BdcZs> list = new ArrayList<BdcZs>();
        /*
         *zdd 业务中如果权利人相同，将此方法放在项目表循环外   如果不同放在循环内
         */

        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcComplexScZsPreviewThread> bdcComplexScZsPreviewThreadList = new ArrayList<BdcComplexScZsPreviewThread>();
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            bdcXmList = bdcXmService.getBdcXmListOrderByBdcdyh(map);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcxmTemp : bdcXmList) {
                    BdcComplexScZsPreviewThread bdcComplexScZsPreviewThread = new BdcComplexScZsPreviewThread(bdcxmTemp,previewZs,this.userid);
                    bdcComplexScZsPreviewThreadList.add(bdcComplexScZsPreviewThread);
                }
            }
            bdcThreadEngine.excuteThread(bdcComplexScZsPreviewThreadList);
        }
        return list;
    }
    @Override
    public void completeZsInfo(BdcXm bdcXm) {
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmList(map);
            if(CollectionUtils.isNotEmpty(bdcXmList)){
                List<BdcZs> bdcZsList = null;
                Map zstypeMap=new HashMap();
                if(bdcXmList.size()>10){
                    List<BdcPreviewInfoForCreateBdczqhThread> bdcPreviewInfoForCreateBdczqhThreadList = new ArrayList<BdcPreviewInfoForCreateBdczqhThread>();
                    for(BdcXm xm:bdcXmList){
                        BdcPreviewInfoForCreateBdczqhThread bdcPreviewInfoForCreateBdczqhThread=new BdcPreviewInfoForCreateBdczqhThread(xm,bdcZsService);
                        bdcPreviewInfoForCreateBdczqhThreadList.add(bdcPreviewInfoForCreateBdczqhThread);
                    }

                    bdcThreadEngine.excuteThread(bdcPreviewInfoForCreateBdczqhThreadList);
                    for (BdcPreviewInfoForCreateBdczqhThread bdcPreviewInfoForCreateBdczqhThread : bdcPreviewInfoForCreateBdczqhThreadList) {
                        Map result = bdcPreviewInfoForCreateBdczqhThread.getResult();
                        if (MapUtils.isNotEmpty(result)) {
                            zstypeMap.putAll(result);
                        }
                    }
                }
                for(BdcXm bdcXmTemp : bdcXmList){
                    bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmTemp.getProid());
                    if(CollectionUtils.isNotEmpty(bdcZsList)){
                        for(BdcZs bdcZs : bdcZsList){
                            if(zstypeMap.containsKey(bdcXm.getProid())&&StringUtils.isNotBlank((CharSequence) zstypeMap.get(bdcXm.getProid()))){
                                bdcZsService.completeZsInfo(bdcXmTemp,bdcZs,this.userid,String.valueOf(zstypeMap.get(bdcXm.getProid())));
                            }else{
                                bdcZsService.completeZsInfo(bdcXmTemp,bdcZs,this.userid,null);
                            }
                        }
                    }
                }
            }
        }
    }
}
