package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import cn.gtmap.estateplat.model.server.core.GdBdcSd;
import cn.gtmap.estateplat.server.core.service.BdcBdcdysdService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
 * @version 1.0, 2018/7/9
 * @description 不动产单元锁定
 */
@Service
public class BdcBdcdysdServiceImpl implements BdcBdcdysdService{
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void updateXzyyAndSdsj(String sdids, String xzyy) {
        String[] sdidsTemp = null;
        if(StringUtils.isNotBlank(sdids)){
            sdidsTemp = sdids.split(",");
        }
        if(sdidsTemp != null && sdidsTemp.length > 0){
            for(String sdid : sdidsTemp){
                if(StringUtils.isNotBlank(sdid)){
                    Example example = new Example(BdcBdcZsSd.class);
                    example.createCriteria().andEqualTo("sdid",sdid);
                    List<BdcBdcZsSd> bdcBdcZsSdList = entityMapper.selectByExample(example);
                    if(CollectionUtils.isNotEmpty(bdcBdcZsSdList)){
                        for(BdcBdcZsSd bdcBdcZsSd : bdcBdcZsSdList){
                            bdcBdcZsSd.setXzyy(xzyy);
                            bdcBdcZsSd.setSdsj(new Date());
                            entityMapper.saveOrUpdate(bdcBdcZsSd,bdcBdcZsSd.getSdid());
                        }
                    }else{
                        Example gdExample = new Example(GdBdcSd.class);
                        gdExample.createCriteria().andEqualTo("sdid",sdid);
                        List<GdBdcSd> gdBdcSdList = entityMapper.selectByExample(gdExample);
                        if(CollectionUtils.isNotEmpty(gdBdcSdList)){
                            for(GdBdcSd gdBdcSd : gdBdcSdList){
                                gdBdcSd.setXzyy(xzyy);
                                gdBdcSd.setSdsj(new Date());
                                entityMapper.saveOrUpdate(gdBdcSd,gdBdcSd.getSdid());
                            }
                        }
                    }
                }
            }
        }
    }
}
