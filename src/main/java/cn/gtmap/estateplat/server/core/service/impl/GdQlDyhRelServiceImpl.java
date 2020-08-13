package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.GdBdcQlRel;
import cn.gtmap.estateplat.model.server.core.GdQlDyhRel;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @version 1.0, 2017/9/4.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description
 */
@Service
public class GdQlDyhRelServiceImpl implements GdQlDyhRelService {
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    BdcCheckMatchDataServiceImpl bdcCheckMatchDataService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<String> saveGdQlDyhRels(String fwid, String bdclx, String qlid, String tdqlid, String bdcdyh, String djid) {
        List<String> qlidList = new ArrayList<String>();
        List<GdQlDyhRel> gdQlDyhRelList = new ArrayList<GdQlDyhRel>();
        List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(fwid);
        if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {

                GdQlDyhRel gdQlDyhRel = new GdQlDyhRel();
                String aqlid = gdBdcQlRel.getQlid();
                qlidList.add(aqlid);
                if(StringUtils.isNotBlank(aqlid)) {
                        gdQlDyhRel.setBdclx(bdclx);
                        Boolean isSameTypeZs = isSameTypeZs(aqlid,tdqlid);
                        if(isSameTypeZs){
                             gdQlDyhRel.setTdqlid(tdqlid);
                        }
                        gdQlDyhRel.setQlid(aqlid);
                        gdQlDyhRel.setBdcdyh(bdcdyh);
                        gdQlDyhRel.setDjid(djid);
                        //zwq tdqlid多个情况处理,即多幢的处理
                        if(StringUtils.isNotBlank(gdQlDyhRel.getTdqlid()) && tdqlid.contains(",")) {
                            gdQlDyhRelList.addAll(getGdQlDyhRelByTdQlid(gdQlDyhRel));
                        }
                        else {
                            gdQlDyhRelList.add(gdQlDyhRel);
                        }
                }
            }
        }


        if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            for(GdQlDyhRel gdQlDyhRel : gdQlDyhRelList)
                bdcGdDyhRelService.addGdQlDyhRelByGdid(gdQlDyhRel, fwid);
        }

        return qlidList;
    }

    @Override
    public List<String> saveGdQlDyhRelsForMul(String fwid, String bdclx, String qlid, String tdqlid, String bdcdyh, String djid) {
        List<String> qlidList = new ArrayList<String>();
        List<GdQlDyhRel> gdQlDyhRelList = new ArrayList<GdQlDyhRel>();
        List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByBdcid(fwid);
        if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                Boolean needAdd = true;
                if(StringUtils.isNotBlank(gdBdcQlRel.getQlid())){
                    HashMap map = new HashMap();
                    map.put("qlid",gdBdcQlRel.getQlid());
                    List<String> qlztList = gdXmService.getQlzt(map);
                    if(CollectionUtils.isNotEmpty(qlztList)){
                        String qlzt = qlztList.get(0);
                        if(StringUtils.isNotBlank(qlzt) && StringUtils.equals(qlzt, Constants.SXZT_ISSX)){
                            needAdd = false;
                        }
                    }
                }
                if(needAdd){
                    GdQlDyhRel gdQlDyhRel = new GdQlDyhRel();
                    String aqlid = gdBdcQlRel.getQlid();
                    qlidList.add(aqlid);
                    if(StringUtils.isNotBlank(aqlid)) {
                        List<GdQlDyhRel> gdQlDyhRelListTemp = bdcGdDyhRelService.getGdQlDyhRel(bdcdyh,aqlid,tdqlid);
                        if(CollectionUtils.isEmpty(gdQlDyhRelListTemp)){
                            gdQlDyhRel.setBdclx(bdclx);
                            Boolean isSameTypeZs = isSameTypeZs(aqlid,tdqlid);
                            if(isSameTypeZs){
                                gdQlDyhRel.setTdqlid(tdqlid);
                            }
                            gdQlDyhRel.setQlid(aqlid);
                            gdQlDyhRel.setBdcdyh(bdcdyh);
                            gdQlDyhRel.setDjid(djid);
                            //zwq tdqlid多个情况处理,即多幢的处理
                            if(StringUtils.isNotBlank(gdQlDyhRel.getTdqlid()) && tdqlid.contains(",")) {
                                gdQlDyhRelList.addAll(getGdQlDyhRelByTdQlid(gdQlDyhRel));
                            }
                            else {
                                gdQlDyhRelList.add(gdQlDyhRel);
                            }
                        }
                    }
                }
            }
        }


        if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            for(GdQlDyhRel gdQlDyhRel : gdQlDyhRelList)
                bdcGdDyhRelService.addGdQlDyhRelByGdid(gdQlDyhRel, fwid);
        }

        return qlidList;
    }

    //判断证书类型是否一致
    public Boolean isSameTypeZs(String qlid,String tdqlid){
            Boolean isSameTypeZs = true;
            HashMap hashMap = null;
            if(StringUtils.isNotBlank(qlid)&&StringUtils.isNotBlank(tdqlid)){
                hashMap = bdcCheckMatchDataService.checkZslx(qlid,tdqlid);
                if(hashMap!=null&&hashMap.get("flag")!=null&&StringUtils.isNotBlank(hashMap.get("flag").toString())&&StringUtils.equals(hashMap.get("flag").toString(),"false")){
                    isSameTypeZs = false;
                }
            }
            return isSameTypeZs;
    }

    //多幢匹配多个土地情况，即多幢跨宗的情况
    public List<GdQlDyhRel> getGdQlDyhRelByTdQlid(GdQlDyhRel gdQlDyhRel) {
        List<GdQlDyhRel> gdQlDyhRelList = new ArrayList<GdQlDyhRel>();
        try {
            if(gdQlDyhRel != null) {
                String tdqlid = gdQlDyhRel.getTdqlid();
                if(StringUtils.isNotBlank(tdqlid)) {
                    String[] qlidArray = tdqlid.split(",");
                    for(String qlid : qlidArray) {
                        GdQlDyhRel dyhRel = new GdQlDyhRel();
                        BeanUtils.copyProperties(dyhRel, gdQlDyhRel);
                        dyhRel.setTdqlid(qlid);
                        gdQlDyhRelList.add(dyhRel);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("GdQlDyhRelServiceImpl.getGdQlDyhRelByTdQlid",e);
        }
        return gdQlDyhRelList;
    }
}
