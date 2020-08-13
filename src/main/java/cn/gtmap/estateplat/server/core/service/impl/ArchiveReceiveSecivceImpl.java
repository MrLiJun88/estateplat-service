package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcJjd;
import cn.gtmap.estateplat.server.core.service.ArchiveReceiveSecivce;
import cn.gtmap.estateplat.server.core.service.BdcJjdService;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 归档反馈服务
 * User: apple
 * Date: 15-12-11
 * Time: 上午10:22
 * To change this template use File | Settings | File Templates.
 */
public class ArchiveReceiveSecivceImpl implements ArchiveReceiveSecivce {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcJjdService bdcJjdService;

    @Override
    @Transactional
    public void receive(final String gdxx) {
        List<BdcJjd> bdcJjdList = JSONArray.parseArray(gdxx, BdcJjd.class);
        if (CollectionUtils.isNotEmpty(bdcJjdList)) {
            for (BdcJjd bdcJjd : bdcJjdList) {
                if (StringUtils.isNotBlank(bdcJjd.getJjdbh())) {
                    BdcJjd bdcJjdTemp = getBdcjjd(bdcJjd.getJjdbh());
                    if (bdcJjdTemp != null) {
                        bdcJjdTemp.setSjr(bdcJjd.getSjr());
                        bdcJjdTemp.setJjsfcg(bdcJjd.getJjsfcg());
                        bdcJjdTemp.setThyy(bdcJjd.getThyy());
                        entityMapper.saveOrUpdate(bdcJjdTemp, bdcJjdTemp.getJjdid());
                    }
                }
            }
        }
    }
    @Transactional(readOnly = true)
    public BdcJjd getBdcjjd(final String jjdbh) {
        if(StringUtils.isNotBlank(jjdbh)) {
            Example example = new Example(BdcJjd.class);
            example.createCriteria().andEqualTo("jjdbh", jjdbh);
            List<BdcJjd> bdcJjdList = entityMapper.selectByExample(BdcJjd.class, example);
            if (CollectionUtils.isNotEmpty(bdcJjdList)) {
                return bdcJjdList.get(0);
            }
        }
        return null;
    }
}
