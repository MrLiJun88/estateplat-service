package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.thread.BdcComplexDyZsPreviewThread;
import cn.gtmap.estateplat.server.thread.BdcThreadEngine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.aliyun.mns.sample.HttpEndpoint.logger;

/**
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version 1.1, 2016/3/11.
 * @description 一个流程  多个不动产单元   权利人与义务人相同  产生多本抵押证书的业务逻辑
 */
public class TurnComplexDydjProjectServiceImpl extends TurnProjectDydjServiceImpl {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcThreadEngine bdcThreadEngine;
    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        long startTime = System.currentTimeMillis();
        List<BdcXm> bdcXmList = null;
        List<BdcZs> list = new ArrayList<BdcZs>();
        /*
         *zdd 业务中如果权利人相同，将此方法放在项目表循环外   如果不同放在循环内
         */
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            List<BdcComplexDyZsPreviewThread> bdcComplexDyZsPreviewThreadList = new ArrayList<BdcComplexDyZsPreviewThread>();
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcxmTemp : bdcXmList) {
                     BdcComplexDyZsPreviewThread bdcComplexDyZsPreviewThread = new BdcComplexDyZsPreviewThread(bdcxmTemp,previewZs,this.userid);
                     bdcComplexDyZsPreviewThreadList.add(bdcComplexDyZsPreviewThread);
                }
            }
            bdcThreadEngine.excuteThread(bdcComplexDyZsPreviewThreadList);
        }
        logger.info("批量抵押证书生成时间："+ (System.currentTimeMillis() - startTime) + "ms");
        return list;
    }
}
