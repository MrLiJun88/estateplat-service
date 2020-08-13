package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.mapper.BdcPpdMapper;
import cn.gtmap.estateplat.server.core.service.BdcPpdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
 * @version 1.0, 2018/4/8
 * @description 获取匹配单信息
 */
@Service
public class BdcPpdServiceImpl implements BdcPpdService{
    @Autowired
    private BdcPpdMapper bdcPpdMapper;

    @Override
    public List<HashMap> getPpdxxMapByBdcdyh(HashMap map) {
        return bdcPpdMapper.getPpdxxMapByBdcdyh(map);
    }

    @Override
    public List<HashMap> getYwlxMapByQlid(HashMap map) {
        return bdcPpdMapper.getYwlxMapByQlid(map);
    }
}
