package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSjglXm;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.server.core.service.BdcSpxxService;
import cn.gtmap.estateplat.server.sj.pp.BdcdyPicUpdateService;
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
 * @version 1.0, 2020/2/27
 * @description 匹配不动产单元 更新bdc_spxx表数据
 */
@Service
public class BdcSpxxUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcSpxx.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
                List<BdcSpxx> updateBdcSpxxList = new ArrayList<>();
                for (BdcSpxx bdcSpxx : bdcSpxxList) {
                    bdcSpxx.setBdcdyh(bdcdyh);
                    updateBdcSpxxList.add(bdcSpxx);
                }
                entityMapper.batchSaveSelective(updateBdcSpxxList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcSpxx.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, tdBdcdyh);
            List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
                List<BdcSpxx> updateBdcSpxxList = new ArrayList<>();
                for (BdcSpxx bdcSpxx : bdcSpxxList) {
                    bdcSpxx.setBdcdyh(bdcdyh);
                    updateBdcSpxxList.add(bdcSpxx);
                }
                entityMapper.batchSaveSelective(updateBdcSpxxList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        if (bdcSpxx != null) {
            bdcSpxx.setBdcdyh(bdcdyh);
            entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_spxx";
    }
}
