package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcFdcqDzService;
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
 * @description 匹配不动产单元 更新bdc_fdcq_dz表数据
 */
@Service
public class BdcFdcqdzUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    BdcDjsjService djsjService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            Example example = new Example(BdcFdcqDz.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, fwBdcdyid);
            List<BdcFdcqDz> bdcFdcqDzList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcFdcqDzList)) {
                List<BdcFdcqDz> updateBdcFdcqDzList = new ArrayList<>();
                for (BdcFdcqDz bdcFdcqDz : bdcFdcqDzList) {
                    bdcFdcqDz.setBdcdyid(bdcdyid);
                    bdcFdcqDz.setBdcdyh(bdcdyh);
                    updateBdcFdcqDzList.add(bdcFdcqDz);
                }
                entityMapper.batchSaveSelective(updateBdcFdcqDzList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {

    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(proid);
        if (bdcFdcqDz != null) {
            bdcFdcqDz.setBdcdyid(bdcdyid);
            bdcFdcqDz.setBdcdyh(bdcdyh);
            entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_fdcq_dz";
    }
}
