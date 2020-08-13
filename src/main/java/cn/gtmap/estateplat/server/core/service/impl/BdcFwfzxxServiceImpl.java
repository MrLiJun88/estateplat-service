package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcFdcqDz;
import cn.gtmap.estateplat.model.server.core.BdcFwfzxx;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.QllxVo;
import cn.gtmap.estateplat.server.core.service.BdcFdcqDzService;
import cn.gtmap.estateplat.server.core.service.BdcFwfzxxService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.QllxService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2016/9/21
 * @description
 */
@Service
public class BdcFwfzxxServiceImpl implements BdcFwfzxxService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;

    @Override
    public List<BdcFwfzxx> getBdcFwfzxxListByQlid(String qlid) {
        List<BdcFwfzxx> bdcFwfzxxList = null;
        if (StringUtils.isNotBlank(qlid)) {
            Example example = new Example(BdcFwfzxx.class);
            example.createCriteria().andEqualTo("qlid", qlid);
            bdcFwfzxxList = entityMapper.selectByExample(example);
        }
        return bdcFwfzxxList;
    }

    @Override
    public void yBdcFwfzxxTurnProjectBdcFwfzxx(List<BdcFwfzxx> ybdcFwfzxxList, String proid ,String qlid) {
        deleteProjectBdcFwfzxx(proid);
        if (CollectionUtils.isNotEmpty(ybdcFwfzxxList)) {
            for (BdcFwfzxx bdcFwfzxx : ybdcFwfzxxList) {
                bdcFwfzxx.setFzid(UUIDGenerator.generate18());
                bdcFwfzxx.setQlid(qlid);
                entityMapper.saveOrUpdate(bdcFwfzxx, bdcFwfzxx.getFzid());
            }
        }
    }

    @Override
    public void deleteProjectBdcFwfzxx(String proid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
        if (qllxVo instanceof BdcFdcqDz) {
            Example example = new Example(BdcFwfzxx.class);
            example.createCriteria().andEqualTo("qlid",qllxVo.getQlid());
            List<BdcFwfzxx> bdcFwfzxxList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
                for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList){
                    entityMapper.deleteByPrimaryKey(bdcFwfzxx.getClass(),bdcFwfzxx.getFzid());
                }
            }
        }
    }

    @Override
    public BdcFwfzxx getBdcFwfzxxListByfzxxid(String fwfzxxid) {
        BdcFwfzxx bdcFwfzxx = null;
        if (StringUtils.isNotBlank(fwfzxxid)) {
            Example example = new Example(BdcFwfzxx.class);
            example.createCriteria().andEqualTo("fzid", fwfzxxid);
            List<BdcFwfzxx> bdcFwfzxxList = entityMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(bdcFwfzxxList)){
                bdcFwfzxx = bdcFwfzxxList.get(0);
            }
        }
        return bdcFwfzxx;
    }

    @Override
    public List<BdcFwfzxx> queryBdcFwfzxxListByProid(String proid) {
        List<BdcFwfzxx> bdcFwfzxxList = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(proid);
            if (bdcFdcqDz != null && StringUtils.isNotBlank(bdcFdcqDz.getQlid())) {
                bdcFwfzxxList = getBdcFwfzxxListByQlid(bdcFdcqDz.getQlid());
            }
        }
        return bdcFwfzxxList;
    }
}
