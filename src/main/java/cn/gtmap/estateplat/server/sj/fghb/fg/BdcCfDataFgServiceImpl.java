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
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/17
 * @description 数据拆分 bdc_cf拆分
 */
@Service
public class BdcCfDataFgServiceImpl implements BdcDataFgService {
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
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
                List<BdcCf> dzBdcCfList = bdcCfService.queryCfByBdcdyid(dzBdcBdcdy.getBdcdyid());
                if(CollectionUtils.isNotEmpty(dzBdcCfList)){
                    for(BdcCf dzBdcCf : dzBdcCfList){
                        String cfXmProid = UUIDGenerator.generate18();

                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(dzBdcCf.getProid());
                        if(bdcXm != null){
                            bdcXm.setProid(cfXmProid);
                            bdcXm.setBdcdyid(bdcBdcdy.getBdcdyid());
                            bdcXm.setBdcdyh(bdcBdcdy.getBdcdyh());
                            bdcXm.setZl(bdcFwfzxx.getXmmc());
                            bdcXm.setSfyzdf("1");
                            entityMapper.insertSelective(bdcXm);
                        }

                        Example example = new Example(BdcFwfsss.class);
                        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh())
                                .andEqualTo(ParamsConstants.PROID_LOWERCASE,dzBdcCf.getProid());
                        List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(example);
                        if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                            for(BdcFwfsss bdcFwfsss : bdcFwfsssList){
                                bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                                bdcFwfsss.setZfbdcdyh(bdcBdcdy.getBdcdyh());
                                bdcFwfsss.setProid(cfXmProid);
                                entityMapper.insertSelective(bdcFwfsss);
                            }
                        }

                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(dzBdcCf.getProid());
                        if(bdcSpxx != null){
                            bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                            bdcSpxx.setBdcdyh(bdcBdcdy.getBdcdyh());
                            bdcSpxx.setProid(cfXmProid);
                            bdcSpxx.setZl(bdcFwfzxx.getXmmc());
                            bdcSpxx.setYt(bdcFwfzxx.getGhyt());
                            bdcSpxx.setMj(bdcFwfzxx.getJzmj());
                            entityMapper.insertSelective(bdcSpxx);
                        }

                        BdcXmRel bdcXmRel = new BdcXmRel();
                        bdcXmRel.setRelid(UUIDGenerator.generate18());
                        bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                        bdcXmRel.setYproid(splitCqProid);
                        bdcXmRel.setProid(cfXmProid);
                        entityMapper.insertSelective(bdcXmRel);

                        List<BdcQlr> bdcQlrYwrList = bdcQlrService.queryBdcQlrYwrByProid(dzBdcCf.getProid());
                        if(CollectionUtils.isNotEmpty(bdcQlrYwrList)){
                            for(BdcQlr bdcQlr : bdcQlrYwrList){
                                bdcQlr.setQlrid(UUIDGenerator.generate18());
                                bdcQlr.setProid(cfXmProid);
                                entityMapper.insertSelective(bdcQlr);
                            }
                        }

                        dzBdcCf.setProid(cfXmProid);
                        dzBdcCf.setQlid(UUIDGenerator.generate18());
                        dzBdcCf.setBdcdyid(bdcBdcdy.getBdcdyid());
                        dzBdcCf.setBdcdyh(bdcBdcdy.getBdcdyh());
                        entityMapper.insertSelective(dzBdcCf);
                    }
                }
            }
        }
    }

    @Override
    public void deleteSplitOldData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy dzBdcBdcdy = dataMap.get("dzBdcBdcdy") != null ? (BdcBdcdy) dataMap.get("dzBdcBdcdy") : null;
            if(dzBdcBdcdy != null){
                if(StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyid())){
                    List<BdcCf> dzBdcCfList = bdcCfService.queryCfByBdcdyid(dzBdcBdcdy.getBdcdyid());
                    if(CollectionUtils.isNotEmpty(dzBdcCfList)){
                        for(BdcCf dzBdcCf : dzBdcCfList){
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(dzBdcCf.getProid());
                            if(bdcXm != null){
                                entityMapper.deleteByPrimaryKey(BdcXm.class,bdcXm.getProid());
                            }

                            Example example = new Example(BdcFwfsss.class);
                            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh())
                                    .andEqualTo(ParamsConstants.PROID_LOWERCASE,dzBdcCf.getProid());
                            List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(example);
                            if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                                for(BdcFwfsss bdcFwfsss : bdcFwfsssList){
                                    entityMapper.deleteByPrimaryKey(BdcFwfsss.class,bdcFwfsss.getFwfsssid());
                                }
                            }

                            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(dzBdcCf.getProid());
                            if(bdcSpxx != null){
                                entityMapper.deleteByPrimaryKey(BdcSpxx.class,bdcSpxx.getSpxxid());
                            }

                            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(dzBdcCf.getProid());
                            if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                                for(BdcXmRel bdcXmRel : bdcXmRelList){
                                    entityMapper.deleteByPrimaryKey(BdcXmRel.class,bdcXmRel.getRelid());
                                }
                            }

                            List<BdcQlr> bdcQlrYwrList = bdcQlrService.queryBdcQlrYwrByProid(dzBdcCf.getProid());
                            if(CollectionUtils.isNotEmpty(bdcQlrYwrList)){
                                for(BdcQlr bdcQlr : bdcQlrYwrList){
                                    entityMapper.deleteByPrimaryKey(BdcQlr.class,bdcQlr.getQlrid());
                                }
                            }

                            entityMapper.deleteByPrimaryKey(BdcCf.class,dzBdcCf.getQlid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_cf";
    }
}
