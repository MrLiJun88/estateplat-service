package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJsydzjdsyq;
import cn.gtmap.estateplat.model.server.core.BdcJzwgy;
import cn.gtmap.estateplat.server.core.service.BdcJzwgyService;
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
 * @description 匹配不动产单元 更新bdc_jzwgy表数据
 */
@Service
public class BdcJzwgyUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyid) && paramMap != null) {
            String bdcdyid = paramMap.get(ParamsConstants.BDCDYID_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYID_LOWERCASE).toString() : null;
            Example example = new Example(BdcJzwgy.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, fwBdcdyid);
            List<BdcJzwgy> bdcJzwgyList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcJzwgyList)) {
                List<BdcJzwgy> updateBdcJzwgyList = new ArrayList<>();
                for (BdcJzwgy bdcJzwgy : bdcJzwgyList) {
                    bdcJzwgy.setBdcdyid(bdcdyid);
                    updateBdcJzwgyList.add(bdcJzwgy);
                }
                entityMapper.batchSaveSelective(updateBdcJzwgyList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {

    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        Example example = new Example(BdcJzwgy.class);
        example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
        List<BdcJzwgy> bdcJzwgyList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bdcJzwgyList)) {
            BdcJzwgy bdcJzwgy = bdcJzwgyList.get(0);
            bdcJzwgy.setBdcdyid(bdcdyid);
            entityMapper.saveOrUpdate(bdcJzwgy, bdcJzwgy.getQlid());
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_jzwgy";
    }
}
