package cn.gtmap.estateplat.server.service.exchange.transition.impl;

import cn.gtmap.estateplat.model.exchange.transition.*;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.server.service.exchange.transition.ReadQzToBbcService;
import cn.gtmap.estateplat.service.exchange.QzDataService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 获取前置机的原业务号
 * Created by zhx on 2015/12/30.
 */
@Service
public class EtlBdcXmRelServiceImpl implements ReadQzToBbcService {
    @Autowired
    QzDataService qzDataService;

    @Override
    public <T> List<T> getBdcData(QzHead qzHead) {
        List<BdcXmRel> bdcXmRels = null;
        if (qzHead != null && qzHead.getData() != null) {
            bdcXmRels = new ArrayList<BdcXmRel>();
            BdcXmRel bdcXmRel = null;
            String yywh = null;
            List<QzCfdj> qzCfdjList = qzHead.getData().getQz_cfdjs();
            if (CollectionUtils.isNotEmpty(qzCfdjList)&&qzCfdjList.get(0).getYywh() != null) {
                yywh = qzCfdjList.get(0).getYywh();
            }
            List<QzNydsyq> qzNydsyqList = qzHead.getData().getQz_nydsyqs();
            if (CollectionUtils.isNotEmpty(qzNydsyqList)&&qzNydsyqList.get(0).getYywh() != null) {
                yywh = qzNydsyqList.get(0).getYywh();
            }
            List<QzDyaq> qzDyaqList = qzHead.getData().getQz_dyaqs();
            if (CollectionUtils.isNotEmpty(qzDyaqList)&&qzDyaqList.get(0).getYywh() != null) {
                yywh = qzDyaqList.get(0).getYywh();
            }
            List<QzDYiq> qzDYiqList = qzHead.getData().getQz_dyiqs();
            if (CollectionUtils.isNotEmpty(qzDYiqList)&&qzDYiqList.get(0).getYywh() != null) {
                yywh = qzDYiqList.get(0).getYywh();
            }
            List<QzFdcq1> qzFdcq1List = qzHead.getData().getQz_fdcq1s();
            if (CollectionUtils.isNotEmpty(qzFdcq1List)&&qzFdcq1List.get(0).getYywh() != null) {
                yywh = qzFdcq1List.get(0).getYywh();
            }
            List<QzFdcq2> qzFdcq2List = qzHead.getData().getQz_fdcq2s();
            if (CollectionUtils.isNotEmpty(qzFdcq2List)&&qzFdcq2List.get(0).getYywh() != null) {
                yywh = qzFdcq2List.get(0).getYywh();
            }
            List<QzFdcq3> qzFdcq3List = qzHead.getData().getQz_fdcq3s();
            if (CollectionUtils.isNotEmpty(qzFdcq3List)&&qzFdcq3List.get(0).getYywh() != null) {
                yywh = qzFdcq3List.get(0).getYywh();
            }
            List<QzGjzwsyq> qzGjzwsyqList = qzHead.getData().getQz_gjzwsyqs();
            if (CollectionUtils.isNotEmpty(qzGjzwsyqList)&&qzGjzwsyqList.get(0).getYywh() != null) {
                yywh = qzGjzwsyqList.get(0).getYywh();
            }
            List<QzHysyq> qzHysyqList = qzHead.getData().getQz_hysyqs();
            if (CollectionUtils.isNotEmpty(qzHysyqList)&&qzHysyqList.get(0).getYywh() != null) {
                yywh = qzHysyqList.get(0).getYywh();
            }
            List<QzJsydsyq> qzJsydsyqList = qzHead.getData().getQz_jsydsyqs();
            if (CollectionUtils.isNotEmpty(qzJsydsyqList)&&qzJsydsyqList.get(0).getYywh() != null) {
                yywh = qzJsydsyqList.get(0).getYywh();
            }
            List<QzTdsyq> qzTdsyqList = qzHead.getData().getQz_tdsyqs();
            if (CollectionUtils.isNotEmpty(qzTdsyqList)&&qzTdsyqList.get(0).getYywh() != null) {
                yywh = qzTdsyqList.get(0).getYywh();
            }
            List<QzYgdj> qzYgdjList = qzHead.getData().getQz_ygdjs();
            if (CollectionUtils.isNotEmpty(qzYgdjList)&&qzYgdjList.get(0).getYywh() != null) {
                yywh = qzYgdjList.get(0).getYywh();
            }
            List<QzYydj> qzYydjList = qzHead.getData().getQz_yydjs();
            if (CollectionUtils.isNotEmpty(qzYydjList)&&qzYydjList.get(0).getYywh() != null) {
                yywh = qzYydjList.get(0).getYywh();
            }
            if (yywh != null && !yywh.equals("")) {
                HashMap map = new HashMap();
                map.put("yxmbh", yywh);
                //根据原业务号去前置机项目关系表查找Yproid放到业务库项目关系里
                List<QzXmgx> qzxmgx = qzDataService.queryQzDataByMap(QzXmgx.class, map);
                if (CollectionUtils.isNotEmpty(qzxmgx)) {
                    String xmbh = qzxmgx.get(0).getXmbh();
                    if (xmbh != null && !xmbh.equals("")) {
                        bdcXmRel = new BdcXmRel();
                        bdcXmRel.setYproid(xmbh);
                        bdcXmRels.add(bdcXmRel);
                    }
                }
            }
        }
        return (List<T>) bdcXmRels;
    }
}
