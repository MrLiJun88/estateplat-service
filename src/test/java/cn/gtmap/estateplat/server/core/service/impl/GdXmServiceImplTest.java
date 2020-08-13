package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.service.GdXmService;
import cn.gtmap.estateplat.service.config.QlztService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @version 1.0, 2017/9/6.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description
 */
public class GdXmServiceImplTest extends BdcBaseUnitTest {
    @Autowired
    GdXmService gdXmService;
    @Autowired
    QlztService qlztService;

    @Transactional
    @Test
    public void updateGdxmPpztByQlids() throws Exception {
        String[] qlidArray = {"FDY-2011051651", "FDY-2012023740", "FWSYQ-2011031486", "FWSYQ-2010103580", "FWSYQ-20170400324"};
        System.out.println(new Date());
        gdXmService.updateGdxmPpztByQlids(Arrays.asList(qlidArray), "2");
        System.out.println(new Date());
    }


    @Test
    public void qlztService() throws Exception {
        qlztService.updateKsYsByBdcdyh("aqdasd");
    }

}