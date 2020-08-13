package cn.gtmap.estateplat.server.sj.fghb.hb;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcDahBdcdy;
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
 * @description 数据合并 bdc_dah_bdcdy合并
 */
@Service
public class BdcDahBdcdyhDataHbServiceImpl implements BdcDataHbService {
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void combineData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy bdcBdcdy = dataMap.get("bdcBdcdy") != null ? (BdcBdcdy)dataMap.get("bdcBdcdy") : null;
            if(bdcBdcdy != null){
                List<String> bdcdyidList = dataMap.get("bdcdyidList") != null ? (List<String>)dataMap.get("bdcdyidList") : null;
                if(CollectionUtils.isNotEmpty(bdcdyidList)){
                    for(String bdcdyid : bdcdyidList){
                        if(StringUtils.isNotBlank(bdcdyid)){
                            BdcBdcdy ybdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                            if(ybdcBdcdy != null){
                                Example example = new Example(BdcDahBdcdy.class);
                                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, ybdcBdcdy.getBdcdyh());
                                List<BdcDahBdcdy> bdcDahBdcdyList = entityMapper.selectByExample(example);
                                if(CollectionUtils.isNotEmpty(bdcDahBdcdyList)){
                                    for(BdcDahBdcdy bdcDahBdcdy : bdcDahBdcdyList){
                                        bdcDahBdcdy.setBdcdyh(bdcBdcdy.getBdcdyh());
                                        entityMapper.saveOrUpdate(bdcDahBdcdy,bdcDahBdcdy.getDahid());
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
        return "bdc_dah_bdcdy";
    }
}
