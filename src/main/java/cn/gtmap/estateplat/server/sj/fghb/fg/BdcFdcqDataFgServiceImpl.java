package cn.gtmap.estateplat.server.sj.fghb.fg;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataFgService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/17
 * @description 数据拆分 bdc_fdcq拆分
 */
@Service
public class BdcFdcqDataFgServiceImpl implements BdcDataFgService {
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcXmZsRelService bdcXmZsRelService;

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
            if(StringUtils.isNotBlank(bdcFdcqDz.getProid())){
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcFdcqDz.getProid());
                if(bdcXm != null){
                    bdcXm.setProid(splitCqProid);
                    bdcXm.setBdcdyid(bdcBdcdy.getBdcdyid());
                    bdcXm.setBdcdyh(bdcBdcdy.getBdcdyh());
                    bdcXm.setZl(bdcFwfzxx.getXmmc());
                    bdcXm.setSfyzdf("1");
                    entityMapper.insertSelective(bdcXm);
                }

                Example example = new Example(BdcFwfsss.class);
                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh())
                        .andEqualTo(ParamsConstants.PROID_LOWERCASE,bdcFdcqDz.getProid());
                List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                    for(BdcFwfsss bdcFwfsss : bdcFwfsssList){
                        bdcFwfsss.setFwfsssid(UUIDGenerator.generate18());
                        bdcFwfsss.setZfbdcdyh(bdcBdcdy.getBdcdyh());
                        bdcFwfsss.setProid(splitCqProid);
                        entityMapper.insertSelective(bdcFwfsss);
                    }
                }

                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcFdcqDz.getProid());
                if(bdcSpxx != null){
                    bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                    bdcSpxx.setBdcdyh(bdcBdcdy.getBdcdyh());
                    bdcSpxx.setProid(splitCqProid);
                    bdcSpxx.setZl(bdcFwfzxx.getXmmc());
                    bdcSpxx.setYt(bdcFwfzxx.getGhyt());
                    bdcSpxx.setMj(bdcFwfzxx.getJzmj());
                    entityMapper.insertSelective(bdcSpxx);
                }

                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcFdcqDz.getProid());
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    for(BdcXmRel bdcXmRel : bdcXmRelList){
                        bdcXmRel.setRelid(UUIDGenerator.generate18());
                        bdcXmRel.setProid(splitCqProid);
                        entityMapper.insertSelective(bdcXmRel);
                    }
                }

                List<BdcQlr> bdcQlrYwrList = bdcQlrService.queryBdcQlrYwrByProid(bdcFdcqDz.getProid());
                if(CollectionUtils.isNotEmpty(bdcQlrYwrList)){
                    for(BdcQlr bdcQlr : bdcQlrYwrList){
                        bdcQlr.setQlrid(UUIDGenerator.generate18());
                        bdcQlr.setProid(splitCqProid);
                        entityMapper.insertSelective(bdcQlr);
                    }
                }

                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcFdcqDz.getProid());
                if(CollectionUtils.isNotEmpty(bdcZsList)){
                    for(BdcZs bdcZs : bdcZsList){
                        bdcZs.setZsid(UUIDGenerator.generate18());
                        bdcZs.setBdcdyh(bdcBdcdy.getBdcdyh());
                        entityMapper.insertSelective(bdcZs);

                        BdcXmzsRel bdcXmzsRel = new BdcXmzsRel();
                        bdcXmzsRel.setXmzsgxid(UUIDGenerator.generate18());
                        bdcXmzsRel.setProid(splitCqProid);
                        bdcXmzsRel.setZsid(bdcZs.getZsid());
                        entityMapper.insertSelective(bdcXmzsRel);
                    }
                }

                bdcFdcqDzToBdcFdcq(splitCqProid,bdcFdcqDz,bdcFwfzxx,bdcBdcdy);
            }
        }
    }

    @Override
    public void deleteSplitOldData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy dzBdcBdcdy = dataMap.get("dzBdcBdcdy") != null ? (BdcBdcdy) dataMap.get("dzBdcBdcdy") : null;
            BdcFdcqDz bdcFdcqDz = dataMap.get("bdcFdcqDz") != null ? (BdcFdcqDz) dataMap.get("bdcFdcqDz") : null;
            if(dzBdcBdcdy != null && StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyh()) && StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyid())
            && bdcFdcqDz != null && StringUtils.isNotBlank(bdcFdcqDz.getProid())){
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcFdcqDz.getProid());
                if(bdcXm != null){
                    entityMapper.deleteByPrimaryKey(BdcXm.class,bdcXm.getProid());
                }

                Example example = new Example(BdcFwfsss.class);
                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh())
                        .andEqualTo(ParamsConstants.PROID_LOWERCASE,bdcFdcqDz.getProid());
                List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(bdcFwfsssList)){
                    for(BdcFwfsss bdcFwfsss : bdcFwfsssList){
                       entityMapper.deleteByPrimaryKey(BdcFwfsss.class,bdcFwfsss.getFwfsssid());
                    }
                }

                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcFdcqDz.getProid());
                if(bdcSpxx != null){
                   entityMapper.deleteByPrimaryKey(BdcSpxx.class,bdcSpxx.getSpxxid());
                }

                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcFdcqDz.getProid());
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    for(BdcXmRel bdcXmRel : bdcXmRelList){
                       entityMapper.deleteByPrimaryKey(BdcXmRel.class,bdcXmRel.getRelid());
                    }
                }

                List<BdcQlr> bdcQlrYwrList = bdcQlrService.queryBdcQlrYwrByProid(bdcFdcqDz.getProid());
                if(CollectionUtils.isNotEmpty(bdcQlrYwrList)){
                    for(BdcQlr bdcQlr : bdcQlrYwrList){
                        entityMapper.deleteByPrimaryKey(BdcQlr.class,bdcQlr.getQlrid());
                    }
                }

                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcFdcqDz.getProid());
                if(CollectionUtils.isNotEmpty(bdcZsList)){
                    for(BdcZs bdcZs : bdcZsList){
                        entityMapper.deleteByPrimaryKey(BdcZs.class,bdcZs.getZsid());
                    }
                }


                List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(bdcFdcqDz.getProid());
                if(CollectionUtils.isNotEmpty(bdcXmzsRelList)){
                    for(BdcXmzsRel bdcXmzsRel : bdcXmzsRelList){
                        entityMapper.deleteByPrimaryKey(BdcXmzsRel.class,bdcXmzsRel.getXmzsgxid());
                    }
                }

                entityMapper.deleteByPrimaryKey(BdcFdcqDz.class,bdcFdcqDz.getQlid());
            }
        }
    }

    private void bdcFdcqDzToBdcFdcq(String cqProid,BdcFdcqDz bdcFdcqDz,BdcFwfzxx bdcFwfzxx,BdcBdcdy bdcBdcdy){
        if(bdcFdcqDz != null && bdcFwfzxx != null){
            BdcFdcq bdcFdcq = new BdcFdcq();
            bdcFdcq.setQlid(UUIDGenerator.generate18());
            bdcFdcq.setProid(cqProid);
            bdcFdcq.setBdcdyh(bdcBdcdy.getBdcdyh());
            bdcFdcq.setBdcdyid(bdcBdcdy.getBdcdyid());
            bdcFdcq.setHtje(bdcFdcqDz.getHtje());
            bdcFdcq.setGhyt(bdcFwfzxx.getGhyt());
            bdcFdcq.setFwsyqr(bdcFdcqDz.getFwsyqr());
            bdcFdcq.setFwjg(bdcFwfzxx.getFwjg());
            bdcFdcq.setDjsj(bdcFdcqDz.getDjsj());
            bdcFdcq.setDbr(bdcFdcqDz.getDbr());
            bdcFdcq.setBz(bdcFdcqDz.getBz());
            bdcFdcq.setFj(bdcFdcqDz.getFj());
            bdcFdcq.setCxzt(bdcFdcqDz.getCxzt());
            bdcFdcq.setJzmj(bdcFwfzxx.getJzmj());
            bdcFdcq.setFtjzmj(bdcFdcqDz.getFtjzmj());
            bdcFdcq.setFttdmj(bdcFdcqDz.getFttdmj());
            bdcFdcq.setDytdmj(bdcFdcqDz.getDytdmj());
            bdcFdcq.setTnjzmj(bdcFdcqDz.getTnjzmj());
            bdcFdcq.setBdcqzh(bdcFdcqDz.getBdcqzh());
            bdcFdcq.setFwxz(bdcFwfzxx.getFwxz());
            bdcFdcq.setGyqk(bdcFdcqDz.getGyqk());
            bdcFdcq.setJgsj(bdcFwfzxx.getJgsj());
            bdcFdcq.setMyzcs(bdcFwfzxx.getZcs());
            bdcFdcq.setQllx(bdcFdcqDz.getQllx());
            bdcFdcq.setQlqtzk(bdcFdcqDz.getQlqtzk());
            bdcFdcq.setQszt(bdcFdcqDz.getQszt());
            if(NumberUtils.isNumber(bdcFwfzxx.getSzc())){
                bdcFdcq.setSzc(Integer.parseInt(bdcFwfzxx.getSzc()));
            }
            bdcFdcq.setSzmyc(bdcFwfzxx.getSzc());
            bdcFdcq.setJyjg(bdcFdcqDz.getFdcjyjg());
            bdcFdcq.setTdsyjsqx(bdcFdcqDz.getTdsyjsqx());
            bdcFdcq.setTdsyksqx(bdcFdcqDz.getTdsyksqx());
            bdcFdcq.setTdsyqmj(bdcFdcqDz.getTdsyqmj());
            bdcFdcq.setTdsyqr(bdcFdcqDz.getTdsyqr());
            bdcFdcq.setTdycsynx(bdcFdcqDz.getTdycsynx());
            bdcFdcq.setZrzh(bdcFwfzxx.getZh());
            bdcFdcq.setYwh(bdcFdcqDz.getYwh());
            if(NumberUtils.isNumber(bdcFwfzxx.getZcs())){
                bdcFdcq.setZcs(Integer.parseInt(bdcFwfzxx.getZcs()));
            }
            bdcFdcq.setQljssj(bdcFdcqDz.getQljssj());
            bdcFdcq.setQlqssj(bdcFdcqDz.getQlqssj());
            entityMapper.insertSelective(bdcFdcq);
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_fdcq";
    }
}
