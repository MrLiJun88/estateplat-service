package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcSdService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @version 1.0, 2018-08-10
 * @description
 */
public class DelProjectBdcDjServiceImpl extends DelProjectDefaultServiceImpl{
    @Autowired
    private BdcSdService bdcSdService;

    @Override
    public void changeYqllxzt(BdcXm bdcXm) {
       if(null!=bdcXm&&StringUtils.isNotBlank(bdcXm.getWiid())){
           bdcSdService.deleteSdxx(bdcXm.getWiid());
       }
    }
}
