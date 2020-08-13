package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJjdXx;
import cn.gtmap.estateplat.model.server.core.BdcJsxx;
import cn.gtmap.estateplat.server.core.service.BdcJsxxService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
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
 * @description 匹配不动产单元 更新bdc_jsxx表数据
 */
@Service
public class BdcJsxxUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcJsxx.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcJsxx> bdcJsxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJsxxList)) {
                List<BdcJsxx> updateBdcJsxxList = new ArrayList<>();
                for (BdcJsxx bdcJsxx : bdcJsxxList) {
                    bdcJsxx.setBdcdyh(bdcdyh);
                    updateBdcJsxxList.add(bdcJsxx);
                }
                entityMapper.batchSaveSelective(updateBdcJsxxList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcJsxx.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, tdBdcdyh);
            List<BdcJsxx> bdcJsxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJsxxList)) {
                List<BdcJsxx> updateBdcJsxxList = new ArrayList<>();
                for (BdcJsxx bdcJsxx : bdcJsxxList) {
                    bdcJsxx.setBdcdyh(bdcdyh);
                    updateBdcJsxxList.add(bdcJsxx);
                }
                entityMapper.batchSaveSelective(updateBdcJsxxList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        String xbdcdyh = bdcdyService.getBdcdyhByProid(proid);
        Example example = new Example(BdcJsxx.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, xbdcdyh);
        List<BdcJsxx> bdcJsxxList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcJsxxList)) {
            List<BdcJsxx> updateBdcJsxxList = new ArrayList<>();
            for (BdcJsxx bdcJsxx : bdcJsxxList) {
                bdcJsxx.setBdcdyh(bdcdyh);
                updateBdcJsxxList.add(bdcJsxx);
            }
            entityMapper.batchSaveSelective(updateBdcJsxxList);
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_jsxx";
    }
}
