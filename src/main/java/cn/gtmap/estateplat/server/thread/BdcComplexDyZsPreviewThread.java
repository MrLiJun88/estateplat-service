package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcQlrService;
import cn.gtmap.estateplat.server.core.service.BdcXmZsRelService;
import cn.gtmap.estateplat.server.core.service.BdcZsQlrRelService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2019/3/28
 * @description 批量抵押生成证书预览多线程
 */
public class BdcComplexDyZsPreviewThread implements Runnable{
     private BdcQlrService bdcQlrService;
     private BdcZsQlrRelService bdcZsQlrRelService;
     private BdcZsService bdcZsService;
     private BdcXmZsRelService bdcXmZsRelService;
     private BdcXm bdcXm;
     private String previewZs;
     private String userid;

    public BdcComplexDyZsPreviewThread(BdcXm bdcXm,String previewZs,String userid) {
        this.bdcQlrService = (BdcQlrService)Container.getBean("bdcQlrServiceImpl");
        this.bdcZsQlrRelService = (BdcZsQlrRelService)Container.getBean("bdcZsQlrRelServiceImpl");
        this.bdcZsService = (BdcZsService)Container.getBean("bdcZsServiceImpl");
        this.bdcXmZsRelService = (BdcXmZsRelService)Container.getBean("bdcXmZsRelServiceImpl");
        this.bdcXm = bdcXm;
        this.previewZs = previewZs;
        this.userid = userid;
    }

    @Override
    public void run() {
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        List<BdcZs> list = new ArrayList<BdcZs>();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        //创建证书
        if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
            List<BdcZs> bdcZsList  = bdcZsService.createBdcZs(bdcXm,previewZs,userid);
            if(CollectionUtils.isNotEmpty(bdcZsList)) {
                list.addAll(bdcZsList);
            }
        }else{
            List<BdcZs> listTemp = bdcZsService.creatDyBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
            if (CollectionUtils.isNotEmpty(listTemp)) {
                list.addAll(listTemp);
            }
            //zdd 生成权利人证书关系表
            bdcZsQlrRelService.creatBdcZsQlrRel(bdcXm,listTemp,bdcQlrList);
            //zdd 生成项目证书关系表
            bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcXm.getProid());
        }
    }
}
