package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcDyq;
import cn.gtmap.estateplat.model.server.core.BdcDywqd;
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
 * @description 匹配不动产单元 更新bdc_dywqd表数据
 */
@Service
public class BdcDywqdUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;


    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcDywqd.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcDywqd> bdcDywqdList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcDywqdList)) {
                List<BdcDywqd> updateBdcDywqdList = new ArrayList<>();
                for (BdcDywqd bdcDywqd : bdcDywqdList) {
                    bdcDywqd.setBdcdyh(bdcdyh);
                    updateBdcDywqdList.add(bdcDywqd);
                }
                entityMapper.batchSaveSelective(updateBdcDywqdList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcDywqd.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, tdBdcdyh);
            List<BdcDywqd> bdcDywqdList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcDywqdList)) {
                List<BdcDywqd> updateBdcDywqdList = new ArrayList<>();
                for (BdcDywqd bdcDywqd : bdcDywqdList) {
                    bdcDywqd.setBdcdyh(bdcdyh);
                    updateBdcDywqdList.add(bdcDywqd);
                }
                entityMapper.batchSaveSelective(updateBdcDywqdList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        Example example = new Example(BdcDywqd.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcDywqd> bdcDywqdList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcDywqdList)) {
            BdcDywqd bdcDywqd = bdcDywqdList.get(0);
            bdcDywqd.setBdcdyh(bdcdyh);
            entityMapper.saveOrUpdate(bdcDywqd, bdcDywqd.getDywid());
        }
    }


    @Override
    public String getIntetfacaCode() {
        return "bdc_dywqd";
    }
}
