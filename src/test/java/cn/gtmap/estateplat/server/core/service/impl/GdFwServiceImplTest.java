package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @version 1.0, 2017/9/6.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description
 */
public class GdFwServiceImplTest extends BdcBaseUnitTest {
    @Autowired
    GdFwService gdFwService;

    @Transactional
    @Test
    public void getPpztByQlid() throws Exception {
        String qlid = "FWSYQ-2011031486";
        String bdclx = Constants.BDCLX_TDFW;
        String proid = "FWSYQ-2011031486";
        System.out.println(new Date());
        gdFwService.getPpztByQlid(qlid, bdclx, proid);
        System.out.println(new Date());
    }

}