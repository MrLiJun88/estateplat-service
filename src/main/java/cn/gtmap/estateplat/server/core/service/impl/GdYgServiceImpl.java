package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.BdcGdDyhRel;
import cn.gtmap.estateplat.model.server.core.GdBdcQlRel;
import cn.gtmap.estateplat.model.server.core.GdYg;
import cn.gtmap.estateplat.server.core.service.BdcGdDyhRelService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.GdYgService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/8/19
 * @description
 */
@Service
public class GdYgServiceImpl implements GdYgService {
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcGdDyhRelService gdDyhRelService;
    @Override
    public List<GdYg> queryGdYgByBdcId(final String bdcId) {
        List<GdYg> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(bdcId)) {
            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(bdcId, null);
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                    GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class,gdBdcQlRel.getQlid());
                    if (gdYg != null)
                        list.add(gdYg);
                }
            }
        }
        return list;
    }

    @Override
    public List<GdYg> getGdygListByBdcdyh(String bdcdyh){
        List<GdYg> gdYgList = new ArrayList<GdYg>();
        if(StringUtils.isNotBlank(bdcdyh)){
            List<BdcGdDyhRel> gdDyhRelList = gdDyhRelService.getGdDyhRelByDyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                for (BdcGdDyhRel gdDyhRel : gdDyhRelList) {
                    if (StringUtils.isNotBlank(gdDyhRel.getGdid())) {
                        List<GdYg> gdYgListTmep = queryGdYgByBdcId(gdDyhRel.getGdid());
                        if (CollectionUtils.isNotEmpty(gdYgListTmep)) {
                            for (GdYg gdYg : gdYgListTmep) {
                                if (gdYg.getIszx() != 1) {
                                    gdYgList.add(gdYg);
                                }
                            }
                        }
                    }
                }
            }
        }
        return  gdYgList;
    }


    @Override
    public GdYg queryGdYgByYgid(final String ygid) {
        return StringUtils.isNotBlank(ygid) ? entityMapper.selectByPrimaryKey(GdYg.class, ygid) : null;
    }
}
