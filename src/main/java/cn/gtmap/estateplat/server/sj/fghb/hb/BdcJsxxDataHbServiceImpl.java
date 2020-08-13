package cn.gtmap.estateplat.server.sj.fghb.hb;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcJsxx;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
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
 * @description 数据合并 bdc_jsxx合并
 */
@Service
public class BdcJsxxDataHbServiceImpl implements BdcDataHbService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public void combineData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy bdcBdcdy = dataMap.get("bdcBdcdy") != null ? (BdcBdcdy)dataMap.get("bdcBdcdy") : null;
            String combineCqProid = dataMap.get("combineCqProid") != null ? dataMap.get("combineCqProid").toString() : null;
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(combineCqProid);
            if(bdcBdcdy != null && CollectionUtils.isNotEmpty(bdcZsList)){
                List<String> bdcdyidList = dataMap.get("bdcdyidList") != null ? (List<String>)dataMap.get("bdcdyidList") : null;
                if(CollectionUtils.isNotEmpty(bdcdyidList)){
                    for(String bdcdyid : bdcdyidList){
                        if(StringUtils.isNotBlank(bdcdyid)){
                            BdcBdcdy bdcBdcdyTemp = bdcdyService.queryBdcdyById(bdcdyid);
                            if(bdcBdcdyTemp != null){
                                Example example = new Example(BdcJsxx.class);
                                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcBdcdyTemp.getBdcdyh());
                                List<BdcJsxx> bdcJsxxList = entityMapper.selectByExample(example);
                                if(CollectionUtils.isNotEmpty(bdcJsxxList)){
                                    for(BdcJsxx bdcJsxx : bdcJsxxList){
                                        Boolean updateJsxx = false;
                                        for(BdcZs bdcZs : bdcZsList){
                                            if(StringUtils.equals(bdcZs.getZsid(),bdcJsxx.getZsid())){
                                                updateJsxx = true;
                                            }
                                        }
                                        if(updateJsxx){
                                            bdcJsxx.setBdcdyh(bdcBdcdy.getBdcdyh());
                                            entityMapper.saveOrUpdate(bdcJsxx,bdcJsxx.getJsid());
                                        }else{
                                            entityMapper.deleteByPrimaryKey(BdcJsxx.class,bdcJsxx.getJsid());
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
        return null;
    }
}
