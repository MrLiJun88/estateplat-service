package cn.gtmap.estateplat.server.service.impl;/*
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2016-05-18
 * @description 
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TurnComplexDyBgdjServiceImpl extends TurnProjectDefaultServiceImpl  {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        //zx初始化权利信息时删除项目id
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        qllxService.delQllxByproid(qllxVo, bdcXm.getProid());
        //zdd 转移登记 需要继承原权利信息
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        //参数修改为wiid @gtsy
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                qllxVo = qllxService.makeSureQllx(bdcXm);
                if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                    if (ybdcxm != null) {
                        QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);
                        if (yqllxVo == null) {
                            yqllxVo = qllxService.makeSureQllx(bdcXm);
                        }
                        yqllxVo.setQlid(UUIDGenerator.generate18());
                        yqllxVo.setProid(bdcXm.getProid());
                        yqllxVo.setYwh(bdcXm.getBh());
                        yqllxVo.setDbr(null);
                        yqllxVo.setDjsj(null);
                        //zdd 不应该继承原来项目的附记
                        yqllxVo.setFj("");
                        //zdd 前后的权利类型相同
                        yqllxVo.setQszt(0);
                        qllxVo = yqllxVo;
                    }
                }

                bdcXm.setBdcdyid(bdcXm.getBdcdyid());
                qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
                entityMapper.insertSelective(qllxVo);
            //根据过渡的抵押权信息里面的抵押方式，确定bdcxm里面的登记子项的取值
            if(qllxVo instanceof BdcDyaq&&bdcXmRel!=null){
                List<GdDy> gddyList = gdFwService.getGdDyListByGdproid(bdcXmRel.getYproid(), 0);
                if(CollectionUtils.isNotEmpty(gddyList)){
                    GdDy gdDy = gddyList.get(0);
                    if(StringUtils.isNotEmpty(gdDy.getDyfs())){
                        if (StringUtils.equals(gdDy.getDyfs(),"一般抵押")){
                            bdcXm.setDjzx(Constants.DJZX_YBDYQ);
                        }
                        if(StringUtils.equals(gdDy.getDyfs(),"最高额抵押")){
                            bdcXm.setDjzx(Constants.DJZX_ZGEDY);
                        }
                        bdcXmService.saveBdcXm(bdcXm);
                    }
                }
            }
        }
        return qllxVo;
    }

    public List<BdcZs> saveBdcZs(final BdcXm bdcXm,final String previewZs) {
        List<BdcXm> bdcXmList;
        List<BdcZs> list = new ArrayList<BdcZs>();
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        /*
         *zdd 业务中如果权利人相同，将此方法放在项目表循环外   如果不同放在循环内
         */
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                //在循环外 只生成一本证书 循环内生成多个项目证书关系  权利人证书关系
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                    List<BdcZs> bdcZsList  = createBdcZs(bdcXm,previewZs);
                    if(CollectionUtils.isNotEmpty(bdcZsList)) {
                        list.addAll(bdcZsList);
                    }
                }else{
                    list = bdcZsService.creatDyBdcqz(bdcXm, bdcQlrList,boolPreviewZs);
                }

                for (BdcXm bdcxmTemp : bdcXmList) {
                    //zdd 生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp,list,bdcQlrList);
                    //zdd 生成项目证书关系表
                    bdcXmZsRelService.creatBdcXmZsRel(list, bdcxmTemp.getProid());
                }
            }
        }
        return list;
    }
}
