package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXymx;
import cn.gtmap.estateplat.server.core.mapper.BdcXymxMapper;
import cn.gtmap.estateplat.server.core.service.BdcXymxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Service
public class BdcXymxServiceImpl implements BdcXymxService {
    @Autowired
    private BdcXymxMapper bdcXymxMapper;
    @Autowired
    EntityMapper entityMapper;

    @Override
    public List<Map> getBdcXymxListByPage(Map map) {
        return bdcXymxMapper.getBdcXymxListByPage(map);
    }

    @Override
    public List<BdcXymx> getBdcXymxByXyglid(String xyglid) {
        List<BdcXymx> bdcXymxList = null;
        if(StringUtils.isNotBlank(xyglid)) {
            Example example = new Example(BdcXymx.class);
            example.createCriteria().andEqualTo("xyglid", xyglid);
            bdcXymxList = entityMapper.selectByExample(example);
        }
        return bdcXymxList;
    }

    @Override
    public HashMap getXsBdcXyxxByZjh(String qlrzjh,String qlrmc) {
        return bdcXymxMapper.getXsBdcXyxxByZjh(qlrzjh,qlrmc);
    }
}
