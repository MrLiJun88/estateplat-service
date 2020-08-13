package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-02
 * @description
 */
public class BdcXmServiceTest extends BdcBaseUnitTest {
    @Autowired
    private BdcXmService bdcXmService;

    @Test
    public void getBdcXmListByWiidOrdeBy() {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid("452F5515CPMVA91B");
        bdcXmService.getBdcXmListByWiidOrdeBy(bdcXm);
    }
}
