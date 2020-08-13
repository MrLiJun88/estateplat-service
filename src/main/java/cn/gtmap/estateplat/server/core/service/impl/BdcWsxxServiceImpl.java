package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcWsxxService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020/3/21
 * @description 不动产完税信息
 */
@Repository
public class BdcWsxxServiceImpl implements BdcWsxxService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<BdcWsxx> getBdcWsxxListByProid(String proid) {
        List<BdcWsxx> bdcWsxxList = null;
        if(StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcWsxx.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            bdcWsxxList = entityMapper.selectByExample(example);
        }
        return bdcWsxxList;
    }

    @Override
    public List<BdcWsmx> getBdcWsmxListByWsxxid(String wsxxid) {
        List<BdcWsmx> bdcWsmxList = null;
        if(StringUtils.isNotBlank(wsxxid)) {
            Example example = new Example(BdcWsmx.class);
            example.createCriteria().andEqualTo(ParamsConstants.WSXXID_LOWERCASE, wsxxid);
            bdcWsmxList = entityMapper.selectByExample(example);
        }
        return bdcWsmxList;
    }
}