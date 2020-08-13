package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:wangming@gtmap.cn">  wangming </a>
 * @version 1.0, 2016/12/20
 * @description
 */
public class TurnComplexScdjAndPldyProjectServiceImpl extends TurnProjectDefaultServiceImpl{
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcFdcqService bdcFdcqService;

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        if(StringUtils.equals(Constants.QLLX_GYTD_FWSUQ,bdcXm.getQllx())) {
            return super.saveQllxVo(bdcXm);
        }else if(StringUtils.equals(Constants.QLLX_DYAQ,bdcXm.getQllx())) {
            QllxVo qllxVo = null;
            qllxVo = qllxService.makeSureQllx(bdcXm);
            qllxService.delQllxByproid(qllxVo, bdcXm.getProid());
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            BdcXmRel bdcXmRel = CollectionUtils.isNotEmpty(bdcXmRelList) ? bdcXmRelList.get(0) : null;
            //原在建工程抵押
            BdcXm ybdcxm = bdcXmService.getYzjgcXm(bdcXm.getProid());
            if (ybdcxm != null) {
                QllxVo yqllxVo = null == qllxService.queryQllxVo(ybdcxm) ? qllxService.queryQllxVo(ybdcxm) : qllxService.makeSureQllx(bdcXm);
                yqllxVo.setQlid(UUIDGenerator.generate18());
                yqllxVo.setProid(bdcXm.getProid());
                yqllxVo.setYwh(bdcXm.getBh());
                yqllxVo.setDbr(null);
                yqllxVo.setDjsj(null);
                yqllxVo.setFj("");
                yqllxVo.setQszt(0);
                qllxVo = yqllxVo;
            }
            qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
            entityMapper.insertSelective(qllxVo);
            return qllxVo;
        }
        return null;
    }

    @Override
    public List<BdcZs> saveBdcZs(BdcXm bdcXm,final String previewZs) {
        List<BdcXm> bdcXmList = null;
        List<BdcZs> list = new ArrayList<BdcZs>();
        List<BdcXm> bdcZsXmList = new ArrayList<BdcXm>();
        List<BdcXm> bdcZmXmList = new ArrayList<BdcXm>();
        HashMap bdcdyidBdcqzhMap = new HashMap();
        Boolean boolPreviewZs = false;
        if(StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs,"true")){
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (StringUtils.isNotBlank(bdcXm.getWiid())) {
            //jyl  过滤不动产附属设施的不动产项目。
            HashMap map = new HashMap();
            map.put("wiid", bdcXm.getWiid());
            bdcXmList = bdcXmService.getBdcXmList(map);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                bdcZsXmList = getXmListByQllx(bdcXmList, Constants.QLLX_GYTD_FWSUQ);
                bdcZmXmList = getXmListByQllx(bdcXmList, Constants.QLLX_DYAQ);
            }
        }
        //生成首次证书
        if (CollectionUtils.isNotEmpty(bdcZsXmList)) {
            for (BdcXm bdcXmTemp : bdcZsXmList) {
                //获取当前项目的权利人
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmTemp.getProid());
                //创建证书
                List<BdcZs> bdcZsList = null;
                if(bdcQlrService.isAfgyContainGtgy(bdcQlrList)){
                    bdcZsList  = createBdcZs(bdcXmTemp,previewZs);
                    if(CollectionUtils.isNotEmpty(bdcZsList)) {
                        list.addAll(bdcZsList);
                    }
                }else{
                    bdcZsList = bdcZsService.creatBdcqz(bdcXmTemp, bdcQlrList,boolPreviewZs);
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        list.addAll(bdcZsList);
                    }

                    bdcZsQlrRelService.creatBdcZsQlrRel(bdcXmTemp, bdcZsList, bdcQlrList);
                    bdcXmZsRelService.creatBdcXmZsRel(bdcZsList, bdcXmTemp.getProid());
                }

                StringBuilder ybdcqzh = new StringBuilder();
                BdcBdcdy bdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXmTemp.getProid());
                if(CollectionUtils.isNotEmpty(bdcZsList)){
                    for(BdcZs bdcZs:bdcZsList){
                        if(StringUtils.isNotBlank(ybdcqzh)) {
                            ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                        }
                        else {
                            ybdcqzh.append(bdcZs.getBdcqzh());
                        }
                    }
                }
                bdcdyidBdcqzhMap.put(bdcdy.getBdcdyid(),ybdcqzh);
            }
        }

        //生成预抵押证明
        if(CollectionUtils.isNotEmpty(bdcZmXmList)){
            BdcXm bdcZmxm = bdcZmXmList.get(0);
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcZmxm.getProid());
            list = bdcZsService.creatDyBdcqz(bdcZmxm, bdcQlrList,boolPreviewZs);
            for (BdcXm bdcxmTemp : bdcZmXmList) {
                List<BdcQlr> bdcQlrListRel = bdcQlrService.queryBdcQlrByProid(bdcxmTemp.getProid());
                bdcZsQlrRelService.creatBdcZsQlrRel(bdcxmTemp,list,bdcQlrListRel);
                bdcXmZsRelService.creatBdcXmZsRel(list, bdcxmTemp.getProid());
                BdcBdcdy bdcdy = bdcdyService.queryBdcBdcdyByProid(bdcxmTemp.getProid());
                bdcxmTemp.setYbdcqzh(bdcdyidBdcqzhMap.get(bdcdy.getBdcdyid())==null?"":bdcdyidBdcqzhMap.get(bdcdy.getBdcdyid()).toString());
                entityMapper.saveOrUpdate(bdcxmTemp,bdcxmTemp.getProid());
            }
        }
        return list;
    }

    /**
    * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
    * @param
    * @return
    * @description 根据权利来筛选项目
    */
    private List<BdcXm> getXmListByQllx(List<BdcXm> bdcXmList, String qllx) {
        List<BdcXm> filterXmList = new ArrayList<BdcXm>();
        if(CollectionUtils.isNotEmpty(bdcXmList)){
            for(BdcXm bdcXm : bdcXmList){
                if(StringUtils.equals(qllx,bdcXm.getQllx())){
                    filterXmList.add(bdcXm);
                }
            }
        }
        return filterXmList;
    }

}
