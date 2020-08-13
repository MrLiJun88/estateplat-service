package cn.gtmap.estateplat.server.web;


import cn.gtmap.estateplat.model.exchange.national.QlfQlZxdj;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.service.SyncQlztService;
import cn.gtmap.estateplat.server.rabbitmq.service.RabbitmqSendMessageService;
import cn.gtmap.estateplat.server.service.config.ConfigSyncQlztService;
import cn.gtmap.estateplat.service.exchange.share.RealEstateShareService;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2019/2/25.
 */
public class RabbitMqTest extends BdcBaseUnitTest{

    @Autowired
    private  SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private RealEstateShareService realEstateShareService;
    @Autowired
    private RabbitmqSendMessageService rabbitmqSendMessageService;
    @Autowired
    SyncQlztService syncQlztService;
    @Autowired
    private ConfigSyncQlztService configSyncQlztService;
    @Test
    public void rabbitMqTest(){
        String proid="2C3D44402JEMY10E";

        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateCxZsZmByProid.queue"));
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateBdcdyZtByBdcdyh.queue"));
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxlistbywiid.queue.send"));
        syncQlztService.updateQlztByProid(proid);

        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateCxZsZmByProid.queue"));
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateBdcdyZtByBdcdyh.queue"));
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidforback.queue.send"));
        syncQlztService.updateQlztByProidForBack(proid);

        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.getcqztbywiid.queue.send"));
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateCxZsZmByCqzh.queue"));
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.getgxyproidsbywiidfordelet.queue.send"));
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateBdcdyZtByBdcdyh.queue"));
        configSyncQlztService.syncBdcRelateQlzt(proid);
        System.out.println("****"+AppConfig.getProperty("bdcdj.rabbitmq.bdc.updateZszmZtByDeleteEnvent.queue"));
        syncQlztService.updateQlztForDelete(proid);
    }
}
