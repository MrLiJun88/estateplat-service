package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcDywqd;
import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcFdcqService;
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
 * @description 匹配不动产单元 更新bdc_fdcq表数据
 */
@Service
public class BdcFdcqUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    BdcDjsjService djsjService;
    @Autowired
    private BdcFdcqService bdcFdcqService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            Example example = new Example(BdcFdcq.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, fwBdcdyid);
            List<BdcFdcq> bdcFdcqList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                List<BdcFdcq> updateBdcFdcqList = new ArrayList<>();
                for (BdcFdcq bdcFdcq : bdcFdcqList) {
                    bdcFdcq.setBdcdyid(bdcdyid);
                    bdcFdcq.setBdcdyh(bdcdyh);
                    updateBdcFdcqList.add(bdcFdcq);
                }
                entityMapper.batchSaveSelective(updateBdcFdcqList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {

    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
            List<BdcFdcq> updateBdcFdcqList = new ArrayList<>();
            for (BdcFdcq bdcFdcq : bdcFdcqList) {
                bdcFdcq.setBdcdyid(bdcdyid);
                bdcFdcq.setBdcdyh(bdcdyh);
                updateBdcFdcqList.add(bdcFdcq);
            }
            entityMapper.batchSaveSelective(updateBdcFdcqList);
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_fdcq";
    }
}
