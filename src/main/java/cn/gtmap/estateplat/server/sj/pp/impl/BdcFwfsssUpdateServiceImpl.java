package cn.gtmap.estateplat.server.sj.pp.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcFwfsss;
import cn.gtmap.estateplat.server.core.service.BdcFwFsssService;
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
 * @description 匹配不动产单元 更新bdc_fwfsss表数据
 */
@Service
public class BdcFwfsssUpdateServiceImpl implements BdcdyPicUpdateService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcFwFsssService bdcFwFsssService;

    @Override
    public void updateFwMatchBdcdyInfo(Map paramMap, String fwBdcdyid, String fwBdcdyh) {
        if (StringUtils.isNotBlank(fwBdcdyh) && paramMap != null) {
            String bdcdyh = paramMap.get(ParamsConstants.BDCDYH_LOWERCASE) != null ? paramMap.get(ParamsConstants.BDCDYH_LOWERCASE).toString() : null;
            Example example = new Example(BdcFwfsss.class);
            example.createCriteria().andEqualTo(ParamsConstants.ZFBDCDYH_LOWERCASE, fwBdcdyh);
            List<BdcFwfsss> bdcFwfsssList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                List<BdcFwfsss> updateBdcFwfsssList = new ArrayList<>();
                for (BdcFwfsss bdcFwfsss : updateBdcFwfsssList) {
                    bdcFwfsss.setZfbdcdyh(bdcdyh);
                    updateBdcFwfsssList.add(bdcFwfsss);
                }
                entityMapper.batchSaveSelective(updateBdcFwfsssList);
            }
        }
    }

    @Override
    public void updateTdMatchBdcdyInfo(Map paramMap, String tdBdcdyid, String tdBdcdyh) {

    }

    @Override
    public void cxMatchBdcdyInfo(String proid, String bdcdyid, String bdcdyh) {
        List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssListByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
            List<BdcFwfsss> updateBdcFwfsssList = new ArrayList<>();
            for (BdcFwfsss bdcFwfsss : updateBdcFwfsssList) {
                bdcFwfsss.setZfbdcdyh(bdcdyh);
                updateBdcFwfsssList.add(bdcFwfsss);
            }
            entityMapper.batchSaveSelective(updateBdcFwfsssList);
        }
    }

    @Override
    public String getIntetfacaCode() {
        return "bdc_fwfsss";
    }
}
