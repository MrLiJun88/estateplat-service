package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2017/4/10
 * @description 
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSfxm;
import cn.gtmap.estateplat.server.core.mapper.BdcSfxmMapper;
import cn.gtmap.estateplat.server.core.service.BdcSfxmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BdcSfxmServiceImpl implements BdcSfxmService{
    @Autowired
    private BdcSfxmMapper bdcSfxmMapper;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public  List<BdcSfxm> getSfXm(String qlrlx, String sfxxid){
        HashMap map=new HashMap();
        map.put("qlrlx",qlrlx);
        map.put("sfxxid",sfxxid);
        return  bdcSfxmMapper.getSfXm(map);
    }

    @Override
    public BdcSfxm getBdcSfxmBySfxmid(String sfxmid) {
        BdcSfxm bdcSfxm = null;
        if (StringUtils.isNotBlank(sfxmid)) {
            bdcSfxm = entityMapper.selectByPrimaryKey(BdcSfxm.class, sfxmid);
        }
        return bdcSfxm;
    }

    @Override
    public void saveBdcSfxmBySfxmid(BdcSfxm bdcSfxm) {
        if(bdcSfxm!=null&&StringUtils.isNotBlank(bdcSfxm.getSfxmid())) {
            entityMapper.saveOrUpdate(bdcSfxm, bdcSfxm.getSfxmid());
        }
    }

    @Override
    public List<BdcSfxm> getBdcSfxmListBySfxxid(String sfxxid) {
        List<BdcSfxm> bdcSfxmList = null;
        if (StringUtils.isNotBlank(sfxxid)) {
            Example example = new Example(BdcSfxm.class);
            example.createCriteria().andEqualTo("sfxxid", sfxxid);
            bdcSfxmList = entityMapper.selectByExample(example);
        }
        return bdcSfxmList;
    }

    @Override
    public List<BdcSfxm> queryBdcSfXm(HashMap map) {
        return bdcSfxmMapper.getSfXm(map);
    }
}
