package cn.gtmap.estateplat.server.core.service.impl;


import cn.gtmap.estateplat.model.server.core.BdcXtSjglResource;
import cn.gtmap.estateplat.server.core.mapper.BdcXtSjglRelationMapper;
import cn.gtmap.estateplat.server.core.service.BdcXtSjglRelationService;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lst
 * @version V1.0, 15-3-21
 */
@Service
public class BdcXtSjglRelationServiceImpl implements BdcXtSjglRelationService {

    @Autowired
    private BdcXtSjglRelationMapper bdcXtSjglRelationMapper;


    @Override
    public List<BdcXtSjglResource> getResourceByDjlx(final String djlxId) {
        return bdcXtSjglRelationMapper.getResourceByDjlx(djlxId);
    }

    public List<BdcXtSjglResource> initPropertyUrl(List<BdcXtSjglResource> resourceList) {

        List<BdcXtSjglResource> returnResourceList = new ArrayList<BdcXtSjglResource>();
        if (CollectionUtils.isNotEmpty(resourceList)) {
            for (BdcXtSjglResource bdcXtSjglResource : resourceList) {
                if (StringUtils.isNotBlank(bdcXtSjglResource.getResourceUrl())) {
                    String tmpUrl = bdcXtSjglResource.getResourceUrl();
                    bdcXtSjglResource.setResourceUrl(PlatformUtil.initOptProperties(tmpUrl));
                }
                returnResourceList.add(bdcXtSjglResource);
            }

        }
        return returnResourceList;
    }

    @Override
    public Integer getMaxXh(final String djlxId) {
        return bdcXtSjglRelationMapper.getMaxXh(djlxId);
    }


}
