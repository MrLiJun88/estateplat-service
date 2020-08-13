package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.mapper.BdcJsxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcJsxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
@Repository
public class BdcJsxxServiceImpl implements BdcJsxxService {
    @Autowired
    private BdcJsxxMapper bdcJsxxMapper;

    @Override
    public List<Map> getBdcJsxxListByPage(Map map) {
        return bdcJsxxMapper.getBdcJsxxListByPage(map);
    }

}
