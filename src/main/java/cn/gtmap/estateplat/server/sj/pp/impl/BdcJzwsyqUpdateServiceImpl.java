package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJzwgy;
import cn.gtmap.estateplat.model.server.core.BdcJzwsyq;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.sj.pp.BdcdyPicUpdateService;
import cn.gtmap.estateplat.server.utils.Constants;
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
 * @description 匹配不动产单元 更新bdc_jzwsyq表数据
 */
@Service
public class BdcJzwsyqUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    BdcDjsjService djsjService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            Example example = new Example(BdcJzwsyq.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, fwBdcdyid);
            List<BdcJzwsyq> bdcJzwsyqList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJzwsyqList)) {
                List<BdcJzwsyq> updateBdcJzwsyqList = new ArrayList<>();
                for (BdcJzwsyq bdcJzwsyq : bdcJzwsyqList) {
                    bdcJzwsyq.setBdcdyid(bdcdyid);
                    bdcJzwsyq.setBdcdyh(bdcdyh);
                    updateBdcJzwsyqList.add(bdcJzwsyq);
                }
                entityMapper.batchSaveSelective(updateBdcJzwsyqList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {

    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        Example example = new Example(BdcJzwsyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcJzwsyq> bdcJzwsyqList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcJzwsyqList)) {
            BdcJzwsyq bdcJzwsyq = bdcJzwsyqList.get(0);
            bdcJzwsyq.setBdcdyid(bdcdyid);
            bdcJzwsyq.setBdcdyh(bdcdyh);
            entityMapper.saveOrUpdate(bdcJzwsyq, bdcJzwsyq.getQlid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_jzwsyq";
    }
}
