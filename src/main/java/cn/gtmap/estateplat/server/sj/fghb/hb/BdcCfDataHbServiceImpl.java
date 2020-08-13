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

import java.util.List;
import java.util.Map;
/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/16
 * @description 数据合并 bdc_cf合并
 */
@Service
public class BdcCfDataHbServiceImpl implements BdcDataHbService {
    @Autowired
    private BdcCfService bdcCfService;
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

    @Override
    public void combineData(Map dataMap) {
        if(dataMap != null){
            //取出map中的bdcdyid 循环找到对应bdc_cf相关数据
            if(dataMap.get("bdcdyidList") != null){
                List<String> bdcdyidList = (List<String>)dataMap.get("bdcdyidList");
                String combineCfProid = null;
                if(CollectionUtils.isNotEmpty(bdcdyidList)){
                    for(String bdcdyid : bdcdyidList){
                        if(StringUtils.isNotBlank(bdcdyid)){
                            List<BdcCf> bdcCfList = bdcCfService.queryCfByBdcdyid(bdcdyid);
                            if(CollectionUtils.isNotEmpty(bdcCfList)){
                                if(StringUtils.isBlank(combineCfProid)){
                                    //取查出的第一个bdccf作为合并后的查封记录
                                    combineCfProid = bdcCfList.get(0).getProid();
                                    combineXmInfo(combineCfProid,dataMap);
                                }
                                for(BdcCf bdcCf : bdcCfList){
                                    if(!StringUtils.equals(bdcCf.getProid(),combineCfProid)){
                                        //删除剩余查封登记相关记录
                                        deleteXmInfo(bdcCf.getProid());
                                    }
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
            //删除bdccf
            BdcCf bdcCf = bdcCfService.selectCfByProid(delProid);
            if(bdcCf != null){
                entityMapper.deleteByPrimaryKey(BdcCf.class,bdcCf.getQlid());
            }
        }
    }

    @Override
    public void combineXmInfo(String combineProid,Map dataMap) {
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
                BdcCf bdcCf = bdcCfService.selectCfByProid(combineProid);
                if(bdcCf != null){
                    bdcCf.setBdcdyh(bdcBdcdy.getBdcdyh());
                    bdcCf.setBdcdyid(bdcBdcdy.getBdcdyid());
                    entityMapper.saveOrUpdate(bdcCf,bdcCf.getQlid());
                }
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_cf";
    }
}
