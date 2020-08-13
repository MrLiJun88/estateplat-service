package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
 * @version 1.0, 2017/5/10
 * @description
 */
public class DelProjectCxSqServiceImpl extends DelProjectDefaultServiceImpl {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void delBdcBdxx(BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNoneBlank(bdcXm.getProid())) {
            delBdcXm(bdcXm.getProid());
            delSqr(bdcXm.getProid());
            delQlr(bdcXm.getProid());
            delCxSq(bdcXm.getProid());
        }
    }

    private void delSqr(String proid) {
        if(StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcSqr.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            entityMapper.deleteByExampleNotNull(example);
        }
    }

    private void delQlr(String proid) {
        if(StringUtils.isNotBlank(proid)){
            Example example = new Example(BdcQlr.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            List<BdcQlr> bdcQlrList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    if (StringUtils.isNoneBlank(bdcQlr.getQlrid())) {
                        delJtcy(bdcQlr.getQlrid());
                    }
                }
                entityMapper.deleteByExampleNotNull(example);
            }
        }
    }

    private void delJtcy(String qlrid) {
        if(StringUtils.isNotBlank(qlrid)){
            Example example = new Example(BdcJtcy.class);
            example.createCriteria().andEqualTo("qlrid", qlrid);
            entityMapper.deleteByExampleNotNull(example);
        }
    }

    private void delCxSq(String proid) {
        if(StringUtils.isNotBlank(proid)){
            Example example = new Example(BdcCxsq.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            entityMapper.deleteByExampleNotNull(example);
        }
    }
}
