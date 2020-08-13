package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSjxx;
import cn.gtmap.estateplat.server.core.mapper.BdcDjsjdxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcSjxxService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2016/12/8
 * @description
 */
@Service
public class BdcSjxxServiceImpl implements BdcSjxxService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcDjsjdxxMapper bdcDjsjdxxMapper;

    @Override
    public  BdcSjxx queryBdcSjxxByWiid(String wiid) {
        BdcSjxx bdcSjxx=null;
        if(StringUtils.isNoneBlank(wiid)) {
            Example example = new Example(BdcSjxx.class);
            example.createCriteria().andEqualTo("wiid", wiid);
            List<BdcSjxx>  bdcSjxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcSjxxList))
                bdcSjxx = bdcSjxxList.get(0);
        }
        return bdcSjxx;
    }
    @Override
    public  BdcSjxx queryBdcSjxxByProid(String proid) {
        BdcSjxx bdcSjxx=null;
        if(StringUtils.isNoneBlank(proid)) {
            Example example = new Example(BdcSjxx.class);
            example.createCriteria().andEqualTo("proid", proid);
            List<BdcSjxx>  bdcSjxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcSjxxList))
                bdcSjxx = bdcSjxxList.get(0);
        }
        return bdcSjxx;
    }

    @Override
    public void  saveBdcSjxx(BdcSjxx bdcSjxx){
        entityMapper.saveOrUpdate(bdcSjxx,bdcSjxx.getSjxxid());
    }

    @Override
    public List<Map> getSjclWithProidByPage(Map map) {
        return bdcDjsjdxxMapper.getSjclWithProidByPage(map);
    }

    @Override
    public List<Map> getSjclWithProidAndDjzxByPage(Map map) {
        return bdcDjsjdxxMapper.getSjclWithProidAndDjzxByPage(map);
    }

    @Override
    public String getSjclXhBySjxxid(String sjxxid) {
        return  bdcDjsjdxxMapper.getSjclXhBySjxxid(sjxxid);
    }

    @Override
    public BdcSjxx saveBdcSjxx(String proid) {
        Example example = new Example(BdcSjxx.class);
        example.createCriteria().andEqualTo("proid", proid);
        List<BdcSjxx> bdcSjxxList = entityMapper.selectByExample(BdcSjxx.class, example);
        BdcSjxx bdcSjxx = null;
        if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
            bdcSjxx = bdcSjxxList.get(0);
        }

        if (bdcSjxx == null) {
            bdcSjxx = new BdcSjxx();
            bdcSjxx.setSjxxid(UUIDGenerator.generate18());
            bdcSjxx.setProid(proid);
            entityMapper.saveOrUpdate(bdcSjxx, bdcSjxx.getSjxxid());
        }
        return bdcSjxx;
    }
}
