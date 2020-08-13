package cn.gtmap.estateplat.server.sj.fghb.hb;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcZsCd;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataHbService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2020/3/16
 * @description 数据合并 bdc_zscd合并
 */
@Service
public class BdcZscdDataHbServiceImpl implements BdcDataHbService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public void combineData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy bdcBdcdy = dataMap.get("bdcBdcdy") != null ? (BdcBdcdy)dataMap.get("bdcBdcdy") : null;
            String combineCqProid = dataMap.get("combineCqProid") != null ? dataMap.get("combineCqProid").toString() : null;
            if(bdcBdcdy != null && StringUtils.isNotBlank(combineCqProid)){
                List<String> bdcdyidList = dataMap.get("bdcdyidList") != null ? (List<String>)dataMap.get("bdcdyidList") : null;
                if(CollectionUtils.isNotEmpty(bdcdyidList)){
                    List<BdcZsCd> bdcZsCdAllList = new ArrayList<>();
                    for(String bdcdyid : bdcdyidList){
                        if(StringUtils.isNotBlank(bdcdyid)){
                            BdcBdcdy bdcBdcdyTemp = bdcdyService.queryBdcdyById(bdcdyid);
                            if(bdcBdcdyTemp != null){
                                Example example = new Example(BdcZsCd.class);
                                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcBdcdyTemp.getBdcdyh());
                                List<BdcZsCd> bdcZsCdList = entityMapper.selectByExample(example);
                                if(CollectionUtils.isNotEmpty(bdcZsCdList)){
                                    bdcZsCdAllList.addAll(bdcZsCdList);
                                }
                            }
                        }
                    }
                    if(CollectionUtils.isNotEmpty(bdcZsCdAllList)){
                        Boolean delCd = false;
                        for(BdcZsCd bdcZsCd : bdcZsCdAllList){
                            if(!delCd){
                                bdcZsCd.setBdcdyh(bdcBdcdy.getBdcdyh());
                                bdcZsCd.setBdcdyid(bdcBdcdy.getBdcdyid());
                                bdcZsCd.setProid(combineCqProid);
                                entityMapper.saveOrUpdate(bdcZsCd,bdcZsCd.getCdid());
                                delCd = true;
                            }else{
                                entityMapper.deleteByPrimaryKey(BdcZsCd.class,bdcZsCd.getCdid());
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
        return "bdc_zscd";
    }
}
