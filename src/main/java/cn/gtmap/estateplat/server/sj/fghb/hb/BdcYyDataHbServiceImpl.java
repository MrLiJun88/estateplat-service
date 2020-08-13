package cn.gtmap.estateplat.server.sj.fghb.hb;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataHbService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/16
 * @description 数据合并 bdc_yy合并
 */
@Service
public class BdcYyDataHbServiceImpl implements BdcDataHbService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcYyService bdcYyService;

    @Override
    public void combineData(Map dataMap) {
        if(dataMap != null){
            List<String> bdcdyidList = dataMap.get("bdcdyidList") != null ? (List<String>)dataMap.get("bdcdyidList") : null;
            String combineYyProid = null;
            if(CollectionUtils.isNotEmpty(bdcdyidList)){
                for(String bdcdyid : bdcdyidList){
                    if(StringUtils.isNotBlank(bdcdyid)){
                        Map queryMap = new HashMap();
                        queryMap.put("bdcdyid",bdcdyid);
                        List<BdcYy> bdcYyList = bdcYyService.queryBdcYy(queryMap);
                        if(CollectionUtils.isNotEmpty(bdcYyList)){
                            if(StringUtils.isBlank(combineYyProid)){
                                //取查出的第一个bdcyy作为合并后的异议记录
                                combineYyProid = bdcYyList.get(0).getProid();
                                combineXmInfo(combineYyProid,dataMap);
                            }
                            for(BdcYy bdcYy : bdcYyList){
                                if(!StringUtils.equals(bdcYy.getProid(),combineYyProid)){
                                    //删除剩余异议登记相关记录
                                    deleteXmInfo(bdcYy.getProid());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void deleteXmInfo(String delProid) {
        if(StringUtils.isNotBlank(delProid)){
            //删除bdcxm
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(delProid);
            if(bdcXm != null){
                entityMapper.deleteByPrimaryKey(BdcXm.class,bdcXm.getProid());
            }
            //删除bdcspxx
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(delProid);
            if(bdcSpxx != null){
                entityMapper.deleteByPrimaryKey(BdcSpxx.class,bdcSpxx.getSpxxid());
            }
            //删除bdcxmrel
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(delProid);
            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                for(BdcXmRel bdcXmRel : bdcXmRelList){
                    entityMapper.deleteByPrimaryKey(BdcXmRel.class,bdcXmRel.getRelid());
                }
            }
            //删除bdcqlr
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(delProid);
            if(CollectionUtils.isNotEmpty(bdcQlrList)){
                for(BdcQlr bdcQlr : bdcQlrList){
                    entityMapper.deleteByPrimaryKey(BdcQlr.class,bdcQlr.getQlrid());
                }
            }
            //删除bdcyy
            Map queryMap = new HashMap();
            queryMap.put("proid",delProid);
            List<BdcYy> bdcYyList = bdcYyService.queryBdcYy(queryMap);
            if(CollectionUtils.isNotEmpty(bdcYyList)){
                for(BdcYy bdcYy : bdcYyList){
                    entityMapper.deleteByPrimaryKey(BdcYy.class,bdcYy.getQlid());
                }
            }
        }
    }

    @Override
    public void combineXmInfo(String combineProid, Map dataMap) {
        if(StringUtils.isNotBlank(combineProid) && dataMap != null){
            String ybdcqzh = dataMap.get("ybdcqzh") != null ? dataMap.get("ybdcqzh").toString() : null;
            String ybh = dataMap.get("ybh") != null ? dataMap.get("ybh").toString() : null;
            Double mj = dataMap.get("mj") != null ? (Double)dataMap.get("mj") : null;
            Double zdzhmj = dataMap.get("zdzhmj") != null ? (Double)dataMap.get("zdzhmj") : null;
            String combineCqProid = dataMap.get("combineCqProid") != null ? dataMap.get("combineCqProid").toString() : null;
            BdcBdcdy bdcBdcdy = dataMap.get("bdcBdcdy") != null ? (BdcBdcdy)dataMap.get("bdcBdcdy") : null;
            if(bdcBdcdy != null){
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(combineProid);
                if(bdcXm != null){
                    bdcXm.setYbdcqzh(ybdcqzh);
                    bdcXm.setYbh(ybh);
                    bdcXm.setBdcdyh(bdcBdcdy.getBdcdyh());
                    bdcXm.setBdcdyid(bdcBdcdy.getBdcdyid());
                    bdcXm.setSfyzdf("1");
                    entityMapper.saveOrUpdate(bdcXm,bdcXm.getProid());
                }
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(combineProid);
                if(bdcSpxx != null){
                    if(mj != null){
                        bdcSpxx.setMj(mj);
                    }
                    if(zdzhmj != null){
                        bdcSpxx.setZdzhmj(zdzhmj);
                    }
                    bdcSpxx.setBdcdyh(bdcBdcdy.getBdcdyh());
                    BdcSpxx combineCqBdcSpxx = bdcSpxxService.queryBdcSpxxByProid(combineCqProid);
                    if(combineCqBdcSpxx != null){
                        bdcSpxx.setZdzhyt(combineCqBdcSpxx.getZdzhyt());
                        bdcSpxx.setYt(combineCqBdcSpxx.getYt());
                        bdcSpxx.setZdzhyt(combineCqBdcSpxx.getZdzhyt());
                    }
                    entityMapper.saveOrUpdate(bdcSpxx,bdcSpxx.getSpxxid());
                }
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(combineProid);
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    //删除原bdcxmrel
                    for(BdcXmRel bdcXmRel : bdcXmRelList){
                        entityMapper.deleteByPrimaryKey(BdcXmRel.class,bdcXmRel.getRelid());
                    }
                    //根据合并后产权proid生成新bdcxmrel
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRel.setYproid(combineCqProid);
                    bdcXmRel.setProid(combineProid);
                    bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                    entityMapper.saveOrUpdate(bdcXmRel,bdcXmRel.getRelid());
                }
                Map queryMap = new HashMap();
                queryMap.put("proid",combineProid);
                List<BdcYy> bdcYyList = bdcYyService.queryBdcYy(queryMap);
                if(CollectionUtils.isNotEmpty(bdcYyList)){
                    for(BdcYy bdcYy : bdcYyList){
                        bdcYy.setBdcdyh(bdcBdcdy.getBdcdyh());
                        bdcYy.setBdcdyid(bdcBdcdy.getBdcdyid());
                        entityMapper.saveOrUpdate(bdcYy,bdcYy.getQlid());
                    }
                }
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_yy";
    }
}
