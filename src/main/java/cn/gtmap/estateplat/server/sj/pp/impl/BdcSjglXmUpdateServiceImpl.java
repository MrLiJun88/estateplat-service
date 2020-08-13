package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJzwsyq;
import cn.gtmap.estateplat.model.server.core.BdcSjglXm;
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
 * @description 匹配不动产单元 更新bdc_sjgl_xm表数据
 */
@Service
public class BdcSjglXmUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcSjglXm.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcSjglXm> bdcSjglXmList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcSjglXmList)) {
                List<BdcSjglXm> updateBdcSjglXmList = new ArrayList<>();
                for (BdcSjglXm bdcSjglXm : bdcSjglXmList) {
                    bdcSjglXm.setBdcdyh(bdcdyh);
                    updateBdcSjglXmList.add(bdcSjglXm);
                }
                entityMapper.batchSaveSelective(updateBdcSjglXmList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcSjglXm.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, tdBdcdyh);
            List<BdcSjglXm> bdcSjglXmList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcSjglXmList)) {
                List<BdcSjglXm> updateBdcSjglXmList = new ArrayList<>();
                for (BdcSjglXm bdcSjglXm : bdcSjglXmList) {
                    bdcSjglXm.setBdcdyh(bdcdyh);
                    updateBdcSjglXmList.add(bdcSjglXm);
                }
                entityMapper.batchSaveSelective(updateBdcSjglXmList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        Example example = new Example(BdcSjglXm.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcSjglXm> bdcSjglXmList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcSjglXmList)) {
            BdcSjglXm bdcSjglXm = bdcSjglXmList.get(0);
            bdcSjglXm.setBdcdyh(bdcdyh);
            entityMapper.saveOrUpdate(bdcSjglXm, bdcSjglXm.getProid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_sjgl_xm";
    }
}
