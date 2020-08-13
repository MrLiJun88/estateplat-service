package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcCf;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.BdcSpxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/8
 * @description 测试entiryMapper
 */
public class EntityMapperServiceTest extends BdcBaseUnitTest {
    @Autowired
    EntityMapper entityMapper;

    @Test
    public void testSelect(){
        try{
            Example example = new Example(BdcSpxx.class);
//            example.createCriteria().andEqualTo("proid","055L2336PGJ8A51E");
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proid","");
//            criteria.andIsNull("proid");
//            example.setOrderByClause("SPXXID");
//            BdcSpxx bdcXmList=entityMapper.selectByPrimaryKey(BdcSpxx.class,"05IM0453IMHFV706");
//            bdcXmList.setZl("dccccccc");
//            entityMapper.saveOrUpdate(bdcXmList, null);
            List<BdcSpxx> bdcXmList=null;
//            if(CollectionUtils.isNotEmpty( example.getOredCriteria()) && CollectionUtils.isNotEmpty(example.getOredCriteria().get(0).getAllCriteria()))
                bdcXmList=entityMapper.selectByExample(BdcSpxx.class, example);


//            if(CollectionUtils.isNotEmpty(bdcXmList)){
                System.out.println(bdcXmList);
//            }
        }catch (Exception e){
            System.out.println(e);
        }

    }
//    @Test
//    public void testUpdate(){
//
//    }
//    @Test
//    public void testInsert(){
//
//    }
//    @Test
//    public void testDelete(){
//
//    }
}
