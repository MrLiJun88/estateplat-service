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
 * @description 数据合并 bdc_fdcq合并
 */
@Service
public class BdcFdcqDataHbServiceImpl implements BdcDataHbService {
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
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public void combineData(Map dataMap) {
        if(dataMap != null){
            List<String> bdcdyidList = dataMap.get("bdcdyidList") != null ? (List<String>)dataMap.get("bdcdyidList") : null;
            String combineCqProid = dataMap.get("combineCqProid") != null ? dataMap.get("combineCqProid").toString() : null;
            if(CollectionUtils.isNotEmpty(bdcdyidList)){
                for(String bdcdyid : bdcdyidList){
                    if(StringUtils.isNotBlank(bdcdyid)){
                        Map queryMap = new HashMap();
                        queryMap.put("bdcdyid",bdcdyid);
                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcq(queryMap);
                        if(CollectionUtils.isNotEmpty(bdcFdcqList)){
                            for(BdcFdcq bdcFdcq : bdcFdcqList){
                                if(StringUtils.equals(bdcFdcq.getProid(),combineCqProid)){
                                    //作为合并记录
                                    combineXmInfo(combineCqProid,dataMap);
                                }else{
                                    //删除其他产权登记相关记录
                                    deleteXmInfo(bdcFdcq.getProid());
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
            //删除bdcfdcq
            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(delProid);
            if(CollectionUtils.isNotEmpty(bdcFdcqList)){
                for(BdcFdcq bdcFdcq : bdcFdcqList){
                    entityMapper.deleteByPrimaryKey(BdcFdcq.class,bdcFdcq.getQlid());
                }
            }
            //删除bdczs
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(delProid);
            if(CollectionUtils.isNotEmpty(bdcZsList)){
                for(BdcZs bdcZs : bdcZsList){
                    entityMapper.deleteByPrimaryKey(BdcZs.class,bdcZs.getZsid());
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

            Double jzmj = dataMap.get("jzmj") != null ? (Double)dataMap.get("jzmj") : null;
            Double tnjzmj = dataMap.get("tnjzmj") != null ? (Double)dataMap.get("tnjzmj") : null;
            Double ftjzmj = dataMap.get("ftjzmj") != null ? (Double)dataMap.get("ftjzmj") : null;
            Double dytdmj = dataMap.get("dytdmj") != null ? (Double)dataMap.get("dytdmj") : null;
            Double fttdmj = dataMap.get("fttdmj") != null ? (Double)dataMap.get("fttdmj") : null;

            List<BdcXmRel> bdcXmRelList = dataMap.get("bdcXmRelList") != null ? (List<BdcXmRel>)dataMap.get("bdcXmRelList") : null;
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
                    entityMapper.saveOrUpdate(bdcSpxx,bdcSpxx.getSpxxid());
                }
                List<BdcXmRel> bdcXmRelListTemp = bdcXmRelService.queryBdcXmRelByProid(combineProid);
                if(CollectionUtils.isNotEmpty(bdcXmRelListTemp)){
                    //删除原bdcxmrel
                    for(BdcXmRel bdcXmRel : bdcXmRelListTemp){
                        entityMapper.deleteByPrimaryKey(BdcXmRel.class,bdcXmRel.getRelid());
                    }
                }
                if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                    entityMapper.insertBatchSelective(bdcXmRelList);
                }
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(combineProid);
                if(CollectionUtils.isNotEmpty(bdcFdcqList)){
                    BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                    bdcFdcq.setJzmj(jzmj);
                    bdcFdcq.setTnjzmj(tnjzmj);
                    bdcFdcq.setFtjzmj(ftjzmj);
                    bdcFdcq.setFttdmj(fttdmj);
                    bdcFdcq.setDytdmj(dytdmj);
                    bdcFdcq.setBdcdyid(bdcBdcdy.getBdcdyid());
                    bdcFdcq.setBdcdyh(bdcBdcdy.getBdcdyh());
                    entityMapper.saveOrUpdate(bdcFdcq,bdcFdcq.getQlid());
                }
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(combineProid);
                if(CollectionUtils.isNotEmpty(bdcZsList)){
                    for(BdcZs bdcZs : bdcZsList){
                        bdcZs.setBdcdyh(bdcBdcdy.getBdcdyh());
                        entityMapper.saveOrUpdate(bdcZs,bdcZs.getZsid());
                    }
                }
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_fdcq";
    }
}
