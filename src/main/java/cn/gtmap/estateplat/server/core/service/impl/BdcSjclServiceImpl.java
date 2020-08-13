package cn.gtmap.estateplat.server.core.service.impl;/*
 * @author <a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
 * @version 1.0, 2017/3/7
 * @description 
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcSjcl;
import cn.gtmap.estateplat.server.core.mapper.BdcSjclMapper;
import cn.gtmap.estateplat.server.core.service.BdcSjclService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BdcSjclServiceImpl  implements BdcSjclService{
    @Autowired
    private BdcSjclMapper bdcSjclMapper;
    @Autowired
    private EntityMapper entityMapper;

    @Override
   public Integer getSjclMaxXh( String sjxxid){
       return  bdcSjclMapper.getSjclMaxXh(sjxxid);
    }
    @Override
   public  void saveBdcSjcl(BdcSjcl bdcSjcl){
        entityMapper.saveOrUpdate(bdcSjcl,bdcSjcl.getSjclid());
    }
    @Override
    public void delSjclxx(String sjclid){
        entityMapper.deleteByPrimaryKey(BdcSjcl.class,sjclid);
    }

    @Override
    public BdcSjcl getBdcSjclById(String sjclid){
        return  entityMapper.selectByPrimaryKey(BdcSjcl.class,sjclid);
    }

    @Override
    public List<BdcSjcl> queryBdcSjclBySjxxid(String sjxxid) {
        List<BdcSjcl>  bdcSjclList=null;
        if(StringUtils.isNoneBlank(sjxxid)) {
            Example example = new Example(BdcSjcl.class);
            example.createCriteria().andEqualTo("sjxxid", sjxxid);
            bdcSjclList = entityMapper.selectByExample(example);
        }
        return bdcSjclList;
    }
    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param map
     * @return
     * @description  根据t条件获取不动产系统配置收件材料信息
     */
    @Override
    public  List<BdcSjcl> getbdcXtSjcl(Map map){
        return bdcSjclMapper.getbdcXtSjcl(map);
    }

    @Override
    public void delSjclBySjxxidAndClmc(String sjxxid, String clmc) {
        if(StringUtils.isNoneBlank(sjxxid)&&StringUtils.isNoneBlank(clmc)) {
            Example example = new Example(BdcSjcl.class);
            example.createCriteria().andEqualTo("sjxxid", sjxxid).andEqualTo("clmc", clmc);
            entityMapper.deleteByExample(BdcSjcl.class,example);
        }
    }
}
