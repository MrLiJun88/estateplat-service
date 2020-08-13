package cn.gtmap.estateplat.server.sj.pp.impl;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcFwzl;
import cn.gtmap.estateplat.model.server.core.BdcJjdXx;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
import cn.gtmap.estateplat.server.core.service.BdcJjdService;
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
 * @description 匹配不动产单元 更新bdc_jjd_xx表数据
 */
@Service
public class BdcJjdxxUpdateServiceImpl implements BdcdyPicUpdateService {
    //bdcdyh bdcdyhs
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcJjdService bdcJjdService;
    @Autowired
    private BdcBdcdyMapper bdcBdcdyMapper;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcJjdXx.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcJjdXx> bdcJjdXxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJjdXxList)) {
                List<BdcJjdXx> updateBdcJjdXxList = new ArrayList<>();
                for (BdcJjdXx bdcJjdXx : bdcJjdXxList) {
                    bdcJjdXx.setBdcdyh(bdcdyh);
                    updateBdcJjdXxList.add(bdcJjdXx);
                }
                entityMapper.batchSaveSelective(updateBdcJjdXxList);
            } else {
                example.clear();
                example.createCriteria().andLike(ParamsConstants.BDCDYHS_LOWERCASE, "%" + fwBdcdyh + "%");
                bdcJjdXxList = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(bdcJjdXxList)) {
                    List<BdcJjdXx> updateBdcJjdXxList = new ArrayList<>();
                    for (BdcJjdXx bdcJjdXx : bdcJjdXxList) {
                        String bdcdyhs = bdcJjdXx.getBdcdyhs();
                        if (StringUtils.contains(bdcdyhs, fwBdcdyh)) {
                            bdcdyhs = StringUtils.replace(bdcdyhs, fwBdcdyh, bdcdyh);
                            bdcJjdXx.setBdcdyhs(bdcdyhs);
                            updateBdcJjdXxList.add(bdcJjdXx);
                        }
                    }
                    entityMapper.batchSaveSelective(updateBdcJjdXxList);
                }
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {
        if (StringUtils.isNotBlank(tdBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcJjdXx.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, tdBdcdyh);
            List<BdcJjdXx> bdcJjdXxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJjdXxList)) {
                List<BdcJjdXx> updateBdcJjdXxList = new ArrayList<>();
                for (BdcJjdXx bdcJjdXx : bdcJjdXxList) {
                    bdcJjdXx.setBdcdyh(bdcdyh);
                    updateBdcJjdXxList.add(bdcJjdXx);
                }
                entityMapper.batchSaveSelective(updateBdcJjdXxList);
            } else {
                example.clear();
                example.createCriteria().andLike(ParamsConstants.BDCDYHS_LOWERCASE, "%" + tdBdcdyh + "%");
                bdcJjdXxList = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(bdcJjdXxList)) {
                    List<BdcJjdXx> updateBdcJjdXxList = new ArrayList<>();
                    for (BdcJjdXx bdcJjdXx : bdcJjdXxList) {
                        String bdcdyhs = bdcJjdXx.getBdcdyhs();
                        if (StringUtils.contains(bdcdyhs, tdBdcdyh)) {
                            bdcdyhs = StringUtils.replace(bdcdyhs, tdBdcdyh, bdcdyh);
                            bdcJjdXx.setBdcdyhs(bdcdyhs);
                            updateBdcJjdXxList.add(bdcJjdXx);
                        }
                    }
                    entityMapper.batchSaveSelective(updateBdcJjdXxList);
                }
            }
        }
    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        BdcJjdXx bdcJjdXx = bdcJjdService.getBdcJjdXxByProid(proid);
        if (bdcJjdXx != null) {
            if (StringUtils.isNotBlank(bdcJjdXx.getBdcdyh())) {
                bdcJjdXx.setBdcdyh(bdcdyh);
            } else {
                String bdcdyhs = bdcJjdXx.getBdcdyhs();
                String xbdcdyh = bdcBdcdyMapper.getBdcdyhByProid(proid);
                if (StringUtils.contains(bdcdyhs, xbdcdyh)) {
                    bdcdyhs = StringUtils.replace(bdcdyhs, xbdcdyh, bdcdyh);
                    bdcJjdXx.setBdcdyhs(bdcdyhs);
                }
            }
            entityMapper.saveOrUpdate(bdcJjdXx, bdcJjdXx.getJjdxxid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_jjd_xx";
    }
}
