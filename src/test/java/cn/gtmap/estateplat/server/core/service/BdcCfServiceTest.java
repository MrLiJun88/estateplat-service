package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/8
 * @description 查封服务单元测试用例
 */
public class BdcCfServiceTest extends BdcBaseUnitTest {
    @Autowired
    BdcCfService bdcCfService;

    @Test
    public void getBdcCflxMc(){
        List<String> cflxMcList =bdcCfService.getBdcCflxMc();
        Assert.assertNotNull(cflxMcList);
    }

}
