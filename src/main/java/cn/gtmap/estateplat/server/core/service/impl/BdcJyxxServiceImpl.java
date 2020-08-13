package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcJyxxService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/4/20
 * @description
 */
@Service
public class BdcJyxxServiceImpl implements BdcJyxxService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void delAllBdcJyxx(String proid) {
        if(StringUtils.isNotBlank(proid)) {
            //删除现势交易信息
            Example bdcJyxxexample = new Example(BdcJyxx.class);
            bdcJyxxexample.createCriteria().andEqualTo("proid", proid);
            List<BdcJyxx> bdcJyxxList = entityMapper.selectByExample(bdcJyxxexample);
            if(CollectionUtils.isNotEmpty(bdcJyxxList)) {
                for(BdcJyxx bdcJyxx:bdcJyxxList) {
                    if(StringUtils.isNotBlank(bdcJyxx.getJybh())) {
                        //删除交易合同信息
                        Example bdcJyhtExample = new Example(BdcJyht.class);
                        bdcJyhtExample.createCriteria().andEqualTo("jybh", bdcJyxx.getJybh());
                        if(bdcJyxx.getDrsj() != null) {
                            bdcJyhtExample.createCriteria().andEqualTo("drsj", bdcJyxx.getDrsj());
                        }
                        entityMapper.deleteByExample(bdcJyhtExample);

                        //删除交易申请人信息
                        Example bdcJysqrExample = new Example(BdcJysqr.class);
                        bdcJysqrExample.createCriteria().andEqualTo("jybh", bdcJyxx.getJybh());
                        if(bdcJyxx.getDrsj() != null) {
                            bdcJysqrExample.createCriteria().andEqualTo("drsj", bdcJyxx.getDrsj());
                        }
                        entityMapper.deleteByExample(bdcJysqrExample);
                    }
                    //删除交易信息
                    entityMapper.deleteByPrimaryKey(BdcJyxx.class,bdcJyxx.getJyxxid());
                }
            }

            //删除历史交易信息
            Example bdcLsJyxxexample = new Example(BdcLsJyxx.class);
            bdcLsJyxxexample.createCriteria().andEqualTo("proid", proid);
            List<BdcLsJyxx> bdcLsJyxxList = entityMapper.selectByExample(bdcLsJyxxexample);
            if(CollectionUtils.isNotEmpty(bdcLsJyxxList)) {
                for(BdcLsJyxx bdcLsJyxx:bdcLsJyxxList) {
                    if(StringUtils.isNotBlank(bdcLsJyxx.getJybh())) {
                        //删除历史交易合同信息
                        Example bdcLsJyhtExample = new Example(BdcLsJyht.class);
                        bdcLsJyhtExample.createCriteria().andEqualTo("jybh", bdcLsJyxx.getJybh());
                        if(bdcLsJyxx.getDrsj() != null) {
                            bdcLsJyhtExample.createCriteria().andEqualTo("drsj", bdcLsJyxx.getDrsj());
                        }
                        entityMapper.deleteByExample(bdcLsJyhtExample);

                        //删除历史交易申请人信息
                        Example bdcLsJysqrExample = new Example(BdcLsJysqr.class);
                        bdcLsJysqrExample.createCriteria().andEqualTo("jybh", bdcLsJyxx.getJybh());
                        if(bdcLsJyxx.getDrsj() != null) {
                            bdcLsJysqrExample.createCriteria().andEqualTo("drsj", bdcLsJyxx.getDrsj());
                        }
                        entityMapper.deleteByExample(bdcLsJysqrExample);
                    }
                    //删除历史交易信息
                    entityMapper.deleteByPrimaryKey(BdcLsJyxx.class,bdcLsJyxx.getLsjyxxid());
                }
            }
        }
    }

}
