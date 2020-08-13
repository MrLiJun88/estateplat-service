package cn.gtmap.estateplat.server.sj.fghb.fg;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataFgService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
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
 * @version 1.0, 2020/3/17
 * @description 数据拆分 bdc_yy拆分
 */
@Service
public class BdcYyDataFgServiceImpl implements BdcDataFgService {
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcYyService bdcYyService;
    @Autowired
    private BdcXmRelService bdcXmRelService;

    @Override
    public void splitData(Map dataMap) {
        if(dataMap != null){
            String splitCqProid = dataMap.get("splitCqProid") != null ? dataMap.get("splitCqProid").toString() : null;
            BdcFwfzxx bdcFwfzxx = dataMap.get("bdcFwfzxx") != null ? (BdcFwfzxx)dataMap.get("bdcFwfzxx") : null;
            BdcBdcdy bdcBdcdy = dataMap.get("bdcBdcdy") != null ? (BdcBdcdy) dataMap.get("bdcBdcdy") : null;
            BdcBdcdy dzBdcBdcdy = dataMap.get("dzBdcBdcdy") != null ? (BdcBdcdy) dataMap.get("dzBdcBdcdy") : null;
            BdcFdcqDz bdcFdcqDz = dataMap.get("bdcFdcqDz") != null ? (BdcFdcqDz) dataMap.get("bdcFdcqDz") : null;
            splitDataFromOldData(splitCqProid,bdcFwfzxx,bdcBdcdy,dzBdcBdcdy,bdcFdcqDz);
        }
    }

    @Override
    public void splitDataFromOldData(String splitCqProid, BdcFwfzxx bdcFwfzxx, BdcBdcdy bdcBdcdy, BdcBdcdy dzBdcBdcdy, BdcFdcqDz bdcFdcqDz) {
        if(bdcFwfzxx != null && dzBdcBdcdy != null && StringUtils.isNotBlank(splitCqProid) && bdcBdcdy != null && bdcFdcqDz != null){
            if(StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyid())){
                Map queryMap = new HashMap();
                queryMap.put("bdcdyid",dzBdcBdcdy.getBdcdyid());
                List<BdcYy> dzBdcYyList = bdcYyService.queryBdcYy(queryMap);
                if(CollectionUtils.isNotEmpty(dzBdcYyList)){
                    for(BdcYy dzBdcYy : dzBdcYyList){
                        String yyXmProid = UUIDGenerator.generate18();

                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(dzBdcYy.getProid());
                        if(bdcXm != null){
                            bdcXm.setProid(yyXmProid);
                            bdcXm.setBdcdyid(bdcBdcdy.getBdcdyid());
                            bdcXm.setBdcdyh(bdcBdcdy.getBdcdyh());
                            bdcXm.setZl(bdcFwfzxx.getXmmc());
                            bdcXm.setSfyzdf("1");
                            entityMapper.insertSelective(bdcXm);
                        }

                        Example example = new Example(BdcFwfsss.class);
                        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh())
                                .andEqualTo(ParamsConstants.PROID_LOWERCASE,dzBdcYy.getProid());
                        List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(example);
                        if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                            for(BdcFwfsss bdcFwfsss : bdcFwfsssList){
                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                bdcFwfsss.setZfbdcdyh(bdcBdcdy.getBdcdyh());
                                bdcFwfsss.setProid(yyXmProid);
                                entityMapper.insertSelective(bdcFwfsss);
                            }
                        }

                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(dzBdcYy.getProid());
                        if(bdcSpxx != null){
                            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                            bdcSpxx.setBdcdyh(bdcBdcdy.getBdcdyh());
                            bdcSpxx.setProid(yyXmProid);
                            bdcSpxx.setZl(bdcFwfzxx.getXmmc());
                            bdcSpxx.setYt(bdcFwfzxx.getGhyt());
                            bdcSpxx.setMj(bdcFwfzxx.getJzmj());
                            entityMapper.insertSelective(bdcSpxx);
                        }

                        BdcXmRel bdcXmRel = new BdcXmRel();
                        if(bdcXmRel != null){
                            bdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                            bdcXmRel.setYproid(splitCqProid);
                            bdcXmRel.setProid(yyXmProid);
                            entityMapper.insertSelective(bdcXmRel);

                        }

                        List<BdcQlr> bdcQlrYwrList = bdcQlrService.queryBdcQlrYwrByProid(dzBdcYy.getProid());
                        if(CollectionUtils.isNotEmpty(bdcQlrYwrList)){
                            for(BdcQlr bdcQlr : bdcQlrYwrList){
                                bdcQlr.setQlrid(UUIDGenerator.generate18());
                                bdcQlr.setProid(yyXmProid);
                                entityMapper.insertSelective(bdcQlr);
                            }
                        }

                        dzBdcYy.setProid(yyXmProid);
                        dzBdcYy.setQlid(UUIDGenerator.generate18());
                        dzBdcYy.setBdcdyid(bdcBdcdy.getBdcdyid());
                        dzBdcYy.setBdcdyh(bdcBdcdy.getBdcdyh());
                        entityMapper.insertSelective(dzBdcYy);
                    }
                }
            }
        }
    }

    @Override
    public void deleteSplitOldData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy dzBdcBdcdy = dataMap.get("dzBdcBdcdy") != null ? (BdcBdcdy) dataMap.get("dzBdcBdcdy") : null;
            if(dzBdcBdcdy != null && StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyid())){
                Map queryMap = new HashMap();
                queryMap.put("bdcdyid",dzBdcBdcdy.getBdcdyid());
                List<BdcYy> dzBdcYyList = bdcYyService.queryBdcYy(queryMap);
                if(CollectionUtils.isNotEmpty(dzBdcYyList)){
                    for(BdcYy dzBdcYy : dzBdcYyList){
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(dzBdcYy.getProid());
                        if(bdcXm != null){
                            entityMapper.deleteByPrimaryKey(BdcXm.class,bdcXm.getProid());
                        }

                        Example example = new Example(BdcFwfsss.class);
                        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh())
                                .andEqualTo(ParamsConstants.PROID_LOWERCASE,dzBdcYy.getProid());
                        List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(example);
                        if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                            for(BdcFwfsss bdcFwfsss : bdcFwfsssList){
                               entityMapper.deleteByPrimaryKey(BdcFwfsss.class,bdcFwfsss.getFwfsssid());
                            }
                        }

                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(dzBdcYy.getProid());
                        if(bdcSpxx != null){
                           entityMapper.deleteByPrimaryKey(BdcSpxx.class,bdcSpxx.getSpxxid());
                        }

                        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(dzBdcYy.getProid());
                        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                            for(BdcXmRel bdcXmRel : bdcXmRelList){
                                entityMapper.deleteByPrimaryKey(BdcXmRel.class,bdcXmRel.getRelid());
                            }
                        }

                        List<BdcQlr> bdcQlrYwrList = bdcQlrService.queryBdcQlrYwrByProid(dzBdcYy.getProid());
                        if(CollectionUtils.isNotEmpty(bdcQlrYwrList)){
                            for(BdcQlr bdcQlr : bdcQlrYwrList){
                               entityMapper.deleteByPrimaryKey(BdcQlr.class,bdcQlr.getQlrid());
                            }
                        }

                        entityMapper.deleteByPrimaryKey(BdcYy.class,dzBdcYy.getQlid());
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
