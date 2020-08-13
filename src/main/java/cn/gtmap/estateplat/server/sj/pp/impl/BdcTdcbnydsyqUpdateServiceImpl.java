package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcTdcbnydsyq;
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
 * @description 匹配不动产单元 更新bdc_tdcbnydsyq表数据
 */
@Service
public class BdcTdcbnydsyqUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    BdcDjsjService djsjService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {

    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            String bdcdybh = null;
            Example example = new Example(BdcTdcbnydsyq.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, tdBdcdyid);
            List<BdcTdcbnydsyq> bdcTdcbnydsyqList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcTdcbnydsyqList)) {
                List<BdcTdcbnydsyq> updateBdcTdcbnydsyqList = new ArrayList<>();
                for (BdcTdcbnydsyq bdcTdcbnydsyq : bdcTdcbnydsyqList) {
                    bdcTdcbnydsyq.setBdcdyid(bdcdyid);
                    bdcTdcbnydsyq.setBdcdyh(bdcdyh);
                    if (StringUtils.contains(bdcdyh, Constants.DZWTZM_W)) {
                        if (bdcdyh.length() > 19) {
                            if (StringUtils.isBlank(bdcdybh)) {
                                bdcdybh = bdcdyh.substring(0, 19);
                            } else {
                                bdcdybh = bdcdybh + "," + bdcdyh.substring(0, 19);
                            }
                            bdcTdcbnydsyq.setBdcdybh(bdcdybh);
                        }
                    }
                    updateBdcTdcbnydsyqList.add(bdcTdcbnydsyq);
                }
                entityMapper.batchSaveSelective(updateBdcTdcbnydsyqList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        Example example = new Example(BdcTdcbnydsyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcTdcbnydsyq> bdcTdcbnydsyqList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcTdcbnydsyqList)) {
            BdcTdcbnydsyq bdcTdcbnydsyq = bdcTdcbnydsyqList.get(0);
            bdcTdcbnydsyq.setBdcdyid(bdcdyid);
            bdcTdcbnydsyq.setBdcdyh(bdcdyh);
            bdcTdcbnydsyq.setBdcdybh(StringUtils.substring(bdcdyh, 0, 19));
            entityMapper.saveOrUpdate(bdcTdcbnydsyq, bdcTdcbnydsyq.getQlid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_tdcbnydsyq";
    }
}
