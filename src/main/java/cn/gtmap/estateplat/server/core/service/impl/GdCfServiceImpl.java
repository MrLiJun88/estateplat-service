package cn.gtmap.estateplat.server.core.service.impl;/*
 *@author:<a herf="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 *@version:1.0,${data} 
 *@description:
*/

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdCfMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GdCfServiceImpl implements GdCfService {
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    GdCfMapper gdCfMapper;
    @Autowired
    BdcXmRelService bdcXmRelService;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<GdCf> getGdCfListByQlid(final String qlid, final Integer iszx) {
        HashMap hashMap = new HashMap();
        List<GdCf> gdCfList = new ArrayList<GdCf>();
        hashMap.put("qlid", qlid);
        List<GdBdcQlRel> gdBdcQlRelList = gdXmService.getGdBdcQlRelByQlidAndBdcId(hashMap);
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            String id = gdBdcQlRelList.get(0).getBdcid();
            if (StringUtils.isNotBlank(id)) {
                hashMap.clear();
                hashMap.put("bdcid", id);
                List<GdBdcQlRel> gdQlRelLists = gdXmService.getGdBdcQlRelByQlidAndBdcId(hashMap);
                if (CollectionUtils.isNotEmpty(gdQlRelLists)) {
                    for (GdBdcQlRel gdBdcQlRel : gdQlRelLists) {
                        if (StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                            GdCf gdCf = gdFwService.getGdCfByCfid(gdBdcQlRel.getQlid(), iszx);
                            if (gdCf != null)
                                gdCfList.add(gdCf);
                        }
                    }
                }
            }
        }
        return gdCfList;
    }

    @Override
    public List<GdCf> getGdCfListByAnyGdProid(String gdProid) {
        List<GdCf> gdCfListAll = new ArrayList<GdCf>();
        if(org.apache.commons.lang3.StringUtils.isNotBlank(gdProid)){
            String gdQl = "";
            HashMap gdQlMap = new HashMap();
            gdQlMap.put("proid",gdProid);
            List<HashMap> gdFwQlMapList = gdFwService.getGdFwQl(gdQlMap);
            List<HashMap> gdTdQlMapList = gdTdService.getGdTdQl(gdQlMap);
            if(CollectionUtils.isNotEmpty(gdFwQlMapList)){
                HashMap gdFwQlMap = gdFwQlMapList.get(0);
                if(gdFwQlMap != null && gdFwQlMap.get("QLID") != null&&StringUtils.isNotBlank(gdFwQlMap.get("QLID").toString())){
                    gdQl = gdFwQlMap.get("QLID").toString();
                }
            }
            if(CollectionUtils.isNotEmpty(gdTdQlMapList)){
                HashMap gdTdQlMap = gdTdQlMapList.get(0);
                if(gdTdQlMap != null && gdTdQlMap.get("QLID") != null&&StringUtils.isNotBlank(gdTdQlMap.get("QLID").toString())){
                    gdQl = gdTdQlMap.get("QLID").toString();
                }
            }
            List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdQl);
            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList){
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(gdBdcQlRel.getBdcid())){
                        String syqQlid = "";
                        List<GdFwsyq> gdFwsyqList = gdFwService.queryFwsyqByFwid(gdBdcQlRel.getBdcid());
                        List<GdTdsyq> gdTdsyqList = gdTdService.queryTdsyqByTdid(gdBdcQlRel.getBdcid());
                        if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                            for(GdFwsyq gdFwsyq : gdFwsyqList){
                                if(gdFwsyq.getIszx() != null && gdFwsyq.getIszx() == 0){
                                    syqQlid = gdFwsyq.getQlid();
                                    break;
                                }
                            }
                        }
                        if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                            for(GdTdsyq gdTdsyq : gdTdsyqList){
                                if(gdTdsyq.getIszx() != null && gdTdsyq.getIszx() == 0){
                                    syqQlid = gdTdsyq.getQlid();
                                    break;
                                }
                            }
                        }
                        if(org.apache.commons.lang3.StringUtils.isNotBlank(syqQlid)){
                            List<GdCf> gdCfList = getGdCfListByQlid(syqQlid,0);
                            if(CollectionUtils.isNotEmpty(gdCfList)){
                                for(GdCf gdCf : gdCfList){
                                    gdCfListAll.add(gdCf);
                                }
                            }
                        }
                    }
                }
            }
        }
        return gdCfListAll;
    }

    @Override
    public List<GdCf> getGdCfListByBdcid(final String bdcid, final Integer iszx) {
        List<GdCf> gdCfList = null;
        if (StringUtils.isNotBlank(bdcid)) {
            List<String> cfQlidList = null;
            HashMap hashMap = Maps.newHashMap();
            hashMap.put("bdcid", bdcid);
            List<GdBdcQlRel> gdQlRelList = gdXmService.getGdBdcQlRelByQlidAndBdcId(hashMap);
            if (CollectionUtils.isNotEmpty(gdQlRelList)) {
                gdCfList = new ArrayList<GdCf>();
                for (GdBdcQlRel gdBdcQlRel : gdQlRelList) {
                    if (StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                        List<GdCf> gdCfListTemp = getGdCfListByQlid(gdBdcQlRel.getQlid(),iszx);
                        if(CollectionUtils.isNotEmpty(gdCfListTemp)) {
                            gdCfList.addAll(gdCfListTemp);
                        }
                    }
                }
            }
        }
        return gdCfList;
    }

    @Override
    public List<GdCf> queryGdCfListByQueryMap(final Map map){
        List<GdCf> gdCfList = null;
        if(map != null&&map.containsKey("cfQlidList")){
            List<String> cfQlidList = (List<String>) map.get("cfQlidList");
            if(CollectionUtils.isNotEmpty(cfQlidList)) {
                gdCfList = gdCfMapper.queryGdCfListByQueryMap(map);
            }
        }
        return gdCfList;
    }

    @Override
    public GdCf getGdCfByCfid(final String cfid){
        return StringUtils.isNotBlank(cfid)?entityMapper.selectByPrimaryKey(GdCf.class,cfid):null;
    }

    @Override
    public void saveGdCf(GdCf gdCf){
        if(null != gdCf){
            if(StringUtils.isNotBlank(gdCf.getCfid())){
                gdCf.setCfid(UUIDGenerator.generate18());
            }
            entityMapper.saveOrUpdate(gdCf,gdCf.getCfid());
        }
    }

    @Override
    public void updateGdCfInfo(BdcXm bdcXm, BdcCf bdcCf) {
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid()) && bdcCf != null){
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                for(BdcXmRel bdcXmRel : bdcXmRelList){
                    GdCf gdCf = getGdCfByCfid(bdcXmRel.getYqlid());
                    if(gdCf != null){
                        gdCf.setJfywh(bdcCf.getJfywh());
                        gdCf.setJfwh(bdcCf.getJfwh());
                        gdCf.setJfwj(bdcCf.getJfwj());
                        gdCf.setJfrq(bdcCf.getJfsj());
                        gdCf.setJfjg(bdcCf.getJfjg());
                        gdCf.setJfdbr(bdcCf.getJfdbr());
                        gdCf.setJfdbsj(bdcCf.getJfdjsj());
                        entityMapper.saveOrUpdate(gdCf,gdCf.getCfid());
                    }
                }
            }
        }
    }

    @Override
    public String deleteGdJf(String proid) {
        String msg = "删除失败";
        try {
            //删除房屋解封信息
            Example exampleCf = new Example(GdCf.class);
            Example.Criteria criteriaCf = exampleCf.createCriteria();
            criteriaCf.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            List<GdCf> gdCfList = entityMapper.selectByExample(GdCf.class, exampleCf);
            for(GdCf gdCf : gdCfList) {
                if(org.apache.commons.lang3.StringUtils.isNotBlank(gdCf.getCfid())) {
                    gdCf.setJfjg("");
                    gdCf.setJfr("");
                    gdCf.setJfwj("");
                    gdCf.setJfwh("");
                    gdCf.setJfdbr("");
                    gdCf.setJfywh("");
                    gdCf.setJfrq(null);
                    gdCf.setJfdbsj(null);
                    entityMapper.updateByPrimaryKeyNull(gdCf);
                }
            }
            msg = "删除成功";
        } catch (Exception e) {
            logger.error("GdXmServiceImpl.deleteGdCf",e);
        }
        return msg;
    }

    @Override
    public List<GdCf> getGdCfByCfwhAndBdcdyh(Map map) {
        return gdCfMapper.getGdCfByCfwhAndBdcdyh(map);
    }

    @Override
    public List<GdCf> getGdCfAndCfwhByFczh(Map map) {
        return gdCfMapper.getGdCfAndCfwhByFczh(map);
    }

    @Override
    public List<GdCf> getGdCfAndCfwhByTdzh(Map map) {
        return gdCfMapper.getGdCfAndCfwhByTdzh(map);
    }
}
