package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.GdXm;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/8
 * @description
 */
public class GdXmServiceTest extends BdcBaseUnitTest {
    @Autowired
    GdXmService gdXmService;

    @Test
    public void getBdcCflxMc(){
        GdXm gdXm = gdXmService.getGdXm("ffff");
        Assert.assertNull(gdXm);
    }
}
