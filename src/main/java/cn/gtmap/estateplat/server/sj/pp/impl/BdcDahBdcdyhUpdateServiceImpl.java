package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcDahBdcdy;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
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
 * @description 匹配不动产单元 更新bdc_dah_bdcdyh表数据
 */
@Service
public class BdcDahBdcdyhUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcBdcdyMapper bdcBdcdyMapper;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcDahBdcdy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcDahBdcdy> bdcDahBdcdyList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcDahBdcdyList)) {
                List<BdcDahBdcdy> updateBdcDahBdcdyList = new ArrayList<>();
                for (BdcDahBdcdy bdcDahBdcdy : bdcDahBdcdyList) {
                    bdcDahBdcdy.setBdcdyh(bdcdyh);
                    updateBdcDahBdcdyList.add(bdcDahBdcdy);
                }
                entityMapper.batchSaveSelective(updateBdcDahBdcdyList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcDahBdcdy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, tdBdcdyh);
            List<BdcDahBdcdy> bdcDahBdcdyList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcDahBdcdyList)) {
                List<BdcDahBdcdy> updateBdcDahBdcdyList = new ArrayList<>();
                for (BdcDahBdcdy bdcDahBdcdy : bdcDahBdcdyList) {
                    bdcDahBdcdy.setBdcdyh(bdcdyh);
                    updateBdcDahBdcdyList.add(bdcDahBdcdy);
                }
                entityMapper.batchSaveSelective(updateBdcDahBdcdyList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        String xbdcdyh = bdcBdcdyMapper.getBdcdyhByProid(proid);
        if (StringUtils.isNotBlank(xbdcdyh)) {
            Example example = new Example(BdcDahBdcdy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, xbdcdyh);
            List<BdcDahBdcdy> bdcDahBdcdyList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcDahBdcdyList)) {
                List<BdcDahBdcdy> updateBdcDahBdcdyList = new ArrayList<>();
                for (BdcDahBdcdy bdcDahBdcdy : bdcDahBdcdyList) {
                    bdcDahBdcdy.setBdcdyh(bdcdyh);
                    updateBdcDahBdcdyList.add(bdcDahBdcdy);
                }
                entityMapper.batchSaveSelective(updateBdcDahBdcdyList);
            }
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_dah_bdcdyh";
    }
}
