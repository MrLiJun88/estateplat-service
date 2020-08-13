package cn.gtmap.estateplat.server;

import com.gtis.config.EgovConfigLoader;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/8
 * @description 不动产登记测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:conf/spring/app-config.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class BdcBaseUnitTest extends AbstractTransactionalJUnit4SpringContextTests {
    static {
        EgovConfigLoader.load();
    }
}
//AbstractJUnit4SpringContextTests
//AbstractTransactionalJUnit4SpringContextTests