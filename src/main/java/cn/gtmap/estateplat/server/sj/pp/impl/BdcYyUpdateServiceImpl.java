package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcYg;
import cn.gtmap.estateplat.model.server.core.BdcYy;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import cn.gtmap.estateplat.server.core.service.BdcDjsjService;
import cn.gtmap.estateplat.server.core.service.BdcYyService;
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
 * @description 匹配不动产单元 更新BDC_YY表数据
 */
@Service
public class BdcYyUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    BdcDjsjService djsjService;
    @Autowired
    private BdcYyService bdcYyService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            Example example = new Example(BdcYy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, fwBdcdyid);
            List<BdcYy> bdcYyList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcYyList)) {
                List<BdcYy> updateBdcYyList = new ArrayList<>();
                for (BdcYy bdcYy : bdcYyList) {
                    bdcYy.setBdcdyid(bdcdyid);
                    bdcYy.setBdcdyh(bdcdyh);
                    updateBdcYyList.add(bdcYy);
                }
                entityMapper.batchSaveSelective(updateBdcYyList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyid) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            String bdcdybh = null;
            Example example = new Example(BdcYy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, tdBdcdyid);
            List<BdcYy> bdcYyList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcYyList)) {
                List<BdcYy> updateBdcYyList = new ArrayList<>();
                for (BdcYy bdcYy : bdcYyList) {
                    bdcYy.setBdcdyid(bdcdyid);
                    bdcYy.setBdcdyh(bdcdyh);
                    if (StringUtils.contains(bdcdyh, Constants.DZWTZM_W)) {
                        if (bdcdyh.length() > 19) {
                            if (StringUtils.isBlank(bdcdybh)) {
                                bdcdybh = bdcdyh.substring(0, 19);
                            } else {
                                bdcdybh = bdcdybh + "," + bdcdyh.substring(0, 19);
                            }
                            bdcYy.setBdcdybh(bdcdybh);
                        }
                    }
                    updateBdcYyList.add(bdcYy);
                }
                entityMapper.batchSaveSelective(updateBdcYyList);
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        BdcYy bdcYy = bdcYyService.getBdcYyByProid(proid);
        if (bdcYy != null) {
            bdcYy.setBdcdyid(bdcdyid);
            bdcYy.setBdcdyh(bdcdyh);
            bdcYy.setBdcdybh(StringUtils.substring(bdcdyh, 0, 19));
            entityMapper.saveOrUpdate(bdcYy, bdcYy.getQlid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_yy";
    }
}
