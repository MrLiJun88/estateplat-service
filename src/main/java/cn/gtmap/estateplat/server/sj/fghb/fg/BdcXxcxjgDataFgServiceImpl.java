package cn.gtmap.estateplat.server.sj.fghb.fg;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcFwfzxx;
import cn.gtmap.estateplat.model.server.core.BdcXxcxjg;
import cn.gtmap.estateplat.server.sj.fghb.BdcDataFgService;
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
 * @description 数据拆分 bdc_xxcgjg拆分
 */
@Service
public class BdcXxcxjgDataFgServiceImpl implements BdcDataFgService {
    @Autowired
    private EntityMapper entityMapper;

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
        if(dzBdcBdcdy != null && StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyid()) && StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyh())
                && bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyid()) && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())){
            Example example = new Example(BdcXxcxjg.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh());
            List<BdcXxcxjg> bdcXxcxjgList = entityMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(bdcXxcxjgList)){
                for(BdcXxcxjg bdcXxcxjg : bdcXxcxjgList){
                    bdcXxcxjg.setJgid(UUIDGenerator.generate18());
                    bdcXxcxjg.setBdcdyh(bdcBdcdy.getBdcdyh());
                    entityMapper.insertSelective(bdcXxcxjg);
                }
            }
        }
    }

    @Override
    public void deleteSplitOldData(Map dataMap) {
        if(dataMap != null){
            BdcBdcdy dzBdcBdcdy = dataMap.get("dzBdcBdcdy") != null ? (BdcBdcdy) dataMap.get("dzBdcBdcdy") : null;
            if(dzBdcBdcdy != null && StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyid()) && StringUtils.isNotBlank(dzBdcBdcdy.getBdcdyh())){
                Example example = new Example(BdcXxcxjg.class);
                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, dzBdcBdcdy.getBdcdyh());
                List<BdcXxcxjg> bdcXxcxjgList = entityMapper.selectByExample(example);
                if(CollectionUtils.isNotEmpty(bdcXxcxjgList)){
                    for(BdcXxcxjg bdcXxcxjg : bdcXxcxjgList){
                       entityMapper.deleteByPrimaryKey(BdcXxcxjg.class,bdcXxcxjg.getJgid());
                    }
                }
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_xxcgjg";
    }
}
