package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.server.core.mapper.ZjgGkcxMapper;
import cn.gtmap.estateplat.server.core.service.ZjgGkcxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 张家港公开查询
 * Created by wangfeike on 2016/6/2
 */
@Service

public class ZjgGkcxServiceImpl implements ZjgGkcxService {

    @Autowired
    ZjgGkcxMapper zjgGkcxMapper;

    @Override
    public List<Map> getCfxxByZsid(Map map) {
        return zjgGkcxMapper.getCfxxByZsid(map);
    }

    @Override
    public List<Map> getDyaxxByZsid(Map map) {
        return zjgGkcxMapper.getDyaxxByZsid(map);
    }
}