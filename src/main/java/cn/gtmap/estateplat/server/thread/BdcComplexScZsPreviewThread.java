package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2019/3/28
 * @description 商品房首次证书预览生成多线程
 */
public class BdcComplexScZsPreviewThread implements Runnable{
    private BdcXm bdcXm;
    private BdcFdcqService bdcFdcqService;
    private BdcQlrService bdcQlrService;
    private BdcZsService bdcZsService;
    private String userid;
    private String previewZs;
    private BdcZsQlrRelService bdcZsQlrRelService;
    private BdcXmZsRelService bdcXmZsRelService;

    public BdcComplexScZsPreviewThread(BdcXm bdcXm,String previewZs,String userid) {
        this.bdcXm = bdcXm;
        this.bdcFdcqService = (BdcFdcqService) Container.getBean("bdcFdcqServiceImpl");
        this.bdcQlrService = (BdcQlrService) Container.getBean("bdcQlrServiceImpl");
        this.bdcZsService = (BdcZsService) Container.getBean("bdcZsServiceImpl");
        this.userid = userid;
        this.previewZs = previewZs;
        this.bdcZsQlrRelService = (BdcZsQlrRelService) Container.getBean("bdcZsQlrRelServiceImpl");
        this.bdcXmZsRelService = (BdcXmZsRelService) Container.getBean("bdcXmZsRelServiceImpl");;
    }

    @Override
    public void run() {
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        List<BdcZs> list = new ArrayList<BdcZs>();
        HashMap fdcqMap = new HashMap();
        fdcqMap.put("proid", bdcXm.getProid());
        List<BdcFdcq> bdcFdcqLst = bdcFdcqService.getBdcFdcq(fdcqMap);
        if (CollectionUtils.isNotEmpty(bdcFdcqLst) && !StringUtils.equals(bdcFdcqLst.get(0).getFzlx(), Constants.FZLX_BFZ)) {
            //创建证书
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
            if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                List<BdcZs> bdcZsList  = bdcZsService.createBdcZs(bdcXm,previewZs,userid);
                if(CollectionUtils.isNotEmpty(bdcZsList)) {
                    list.addAll(bdcZsList);
                }
            } else{
                List<BdcZs> listTemp = bdcZsService.creatBdcqz(bdcXm, bdcQlrList, this.userid,boolPreviewZs);
                if (CollectionUtils.isNotEmpty(listTemp)) {
                    list.addAll(listTemp);
                }

                //zdd 生成权利人证书关系表
                bdcZsQlrRelService.creatBdcZsQlrRel(bdcXm, listTemp, bdcQlrList);
                //zdd 生成项目证书关系表
                bdcXmZsRelService.creatBdcXmZsRel(listTemp, bdcXm.getProid());
            }
        }
    }
}
