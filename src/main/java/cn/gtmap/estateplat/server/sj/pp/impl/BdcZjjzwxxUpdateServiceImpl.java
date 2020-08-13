package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.model.server.core.BdcZjjzwxx;
import cn.gtmap.estateplat.server.core.service.BdcZjjzwxxService;
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
 * @description 匹配不动产单元 更新BDC_ZJJZWXX表数据
 */
@Service
public class BdcZjjzwxxUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcZjjzwxx.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcZjjzwxx> bdcZjjzwxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcZjjzwxxList)) {
                List<BdcZjjzwxx> updateBdcZjjzwxxList = new ArrayList<>();
                for (BdcZjjzwxx bdcZjjzwxx : updateBdcZjjzwxxList) {
                    bdcZjjzwxx.setBdcdyh(bdcdyh);
                    updateBdcZjjzwxxList.add(bdcZjjzwxx);
                }
                entityMapper.batchSaveSelective(updateBdcZjjzwxxList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {

    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        BdcZjjzwxx bdcZjjzwxx = bdcZjjzwxxService.getBdcZjjzwxxByProid(proid);
        if (bdcZjjzwxx != null) {
            bdcZjjzwxx.setBdcdyh(bdcdyh);
            entityMapper.saveOrUpdate(bdcZjjzwxx, bdcZjjzwxx.getZjwid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_zjjzwxx";
    }
}
