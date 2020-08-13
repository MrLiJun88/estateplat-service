package cn.gtmap.estateplat.server.sj.fghb.hb;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcBdcdySd;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataHbService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/16
 * @description 数据合并 bdc_bdcdysd合并
 */
@Service
public class BdcdySdDataHbServiceImpl implements BdcDataHbService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public void combineData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy bdcBdcdy = dataMap.get("bdcBdcdy") != null ? (BdcBdcdy)dataMap.get("bdcBdcdy") : null;
            if(bdcBdcdy != null){
                List<String> bdcdyidList = dataMap.get("bdcdyidList") != null ? (List<String>)dataMap.get("bdcdyidList") : null;
                if(CollectionUtils.isNotEmpty(bdcdyidList)){
                    Boolean delSd = false;
                    for(String bdcdyid : bdcdyidList){
                        if(StringUtils.isNotBlank(bdcdyid)){
                            BdcBdcdy bdcBdcdyTemp = bdcdyService.queryBdcdyById(bdcdyid);
                            if(bdcBdcdyTemp != null){
                                Example example = new Example(BdcBdcdySd.class);
                                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcBdcdyTemp.getBdcdyh());
                                List<BdcBdcdySd> bdcBdcdySdList = entityMapper.selectByExample(example);
                                if(CollectionUtils.isNotEmpty(bdcBdcdySdList)){
                                    for(BdcBdcdySd bdcBdcdySd : bdcBdcdySdList){
                                        if(delSd){
                                            entityMapper.deleteByPrimaryKey(BdcBdcdySd.class,bdcBdcdy.getBdcdyid());
                                        }else{
                                            bdcBdcdySd.setBdcdyh(bdcBdcdy.getBdcdyh());
                                            bdcBdcdySd.setBdcdyid(bdcBdcdy.getBdcdyid());
                                            entityMapper.saveOrUpdate(bdcBdcdySd,bdcBdcdySd.getBdcdyid());
                                            delSd = true;
                                        }
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

    }

    @Override
    public void combineXmInfo(String combineProid, Map dataMap) {

    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_bdcdysd";
    }
}
