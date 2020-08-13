package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJsydzjdsyq;
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
 * @description 匹配不动产单元 更新bdc_jsydzjdsyq表数据
 */
@Service
public class BdcJsydzjdsyqUpdateServiceImpl implements BdcdyPicUpdateService {
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
            Example example = new Example(BdcJsydzjdsyq.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, tdBdcdyid);
            List<BdcJsydzjdsyq> bdcJsydzjdsyqList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                List<BdcJsydzjdsyq> updateBdcJsydzjdsyqList = new ArrayList<>();
                for (BdcJsydzjdsyq bdcJsydzjdsyq : bdcJsydzjdsyqList) {
                    bdcJsydzjdsyq.setBdcdyid(bdcdyid);
                    bdcJsydzjdsyq.setBdcdyh(bdcdyh);
                    if (StringUtils.contains(bdcdyh, Constants.DZWTZM_W)) {
                        if (bdcdyh.length() > 19) {
                            if (StringUtils.isBlank(bdcdybh)) {
                                bdcdybh = bdcdyh.substring(0, 19);
                            } else {
                                bdcdybh = bdcdybh + "," + bdcdyh.substring(0, 19);
                            }
                            bdcJsydzjdsyq.setBdcdybh(bdcdybh);
                        }
                    }
                    updateBdcJsydzjdsyqList.add(bdcJsydzjdsyq);
                }
                entityMapper.batchSaveSelective(updateBdcJsydzjdsyqList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        Example example = new Example(BdcJsydzjdsyq.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcJsydzjdsyq> bdcJsydzjdsyqList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
            BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqList.get(0);
            bdcJsydzjdsyq.setBdcdyid(bdcdyid);
            bdcJsydzjdsyq.setBdcdyh(bdcdyh);
            bdcJsydzjdsyq.setBdcdybh(bdcdyh.substring(0, 19));
            entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_jsydzjdsyq";
    }
}
