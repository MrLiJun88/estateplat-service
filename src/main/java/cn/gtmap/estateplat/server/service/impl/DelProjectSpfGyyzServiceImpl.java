package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-5
 */
public class DelProjectSpfGyyzServiceImpl extends DelProjectScdjServiceImpl {
    @Autowired
    private BdcXmService bdcXmService;

    @Override
    public void delBdcBdxx(BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    super.delBdcBdxx(bdcXmTemp);
                }
            }
        }
    }

}
