package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmcqRel;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcXmcqRelService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-03
 * @description 不动产系统业务模型服务
 */
@Service
public class BdcXmcqRelServiceImpl implements BdcXmcqRelService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmService bdcXmService;

    /**
     * @param proid 项目Id
     * @return 不动产系统业务模型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据项目Id获取不动产系统业务模型
     */
    @Override
    public List<BdcXmcqRel> queryBdcXmcqRelByProid(String proid) {
        List<BdcXmcqRel> BdcXmcqRelList = Lists.newArrayList();
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcXmcqRel.class);
            example.createCriteria().andEqualTo("proid", proid);
            BdcXmcqRelList = entityMapper.selectByExample(example);
        }
        if (CollectionUtils.isEmpty(BdcXmcqRelList)) {
            BdcXmcqRelList = Collections.EMPTY_LIST;
        }
        return BdcXmcqRelList;
    }

    @Override
    public void saveBdcXmcqRel(List<BdcXmcqRel> BdcXmcqRelList) {
        if (CollectionUtils.isNotEmpty(BdcXmcqRelList)) {
            entityMapper.insertBatchSelective(BdcXmcqRelList);
        }
    }

    /**
     * @param wiid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据wiid删除项目产权关系表
     */
    @Override
    public void deleteBdcXmcqRelByWiid(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    Example example = new Example(BdcXmcqRel.class);
                    example.createCriteria().andEqualTo("proid", bdcXm.getProid());
                    entityMapper.deleteByExample(example);
                }
            }
        }
    }
}
