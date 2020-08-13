package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.GdXm;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

import static org.junit.Assert.*;

/**
 * @version 1.0, 2017/9/6.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description
 */
public class GdXmMapperTest extends BdcBaseUnitTest {
    @Autowired
    GdXmMapper gdXmMapper;
    @Autowired
    EntityMapper entityMapper;

    @Transactional
    @Test
    public void updateGdXmppzt() throws Exception {
        String[] qlidArray = {"FDY-2011051651", "FDY-2012023740", "FWSYQ-2011031486", "FWSYQ-2010103580", "FWSYQ-20170400324"};
        System.out.println(new Date());
        System.out.println(new Date());
        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, "FWSYQ-2010103580");
        Assert.assertEquals(gdXm.getPpzt(), "0");
    }
}