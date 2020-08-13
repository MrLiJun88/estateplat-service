package cn.gtmap.estateplat.server.service.core.impl;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcTdService;
import cn.gtmap.estateplat.server.core.service.BdcdjbService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.core.ProjectLifeThreadServcie;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/10/18
 * @description 项目生命周期线程服务
 */
@Service
public class ProjectLifeThreadServcieImpl implements ProjectLifeThreadServcie {
    @Resource(name = "creatComplexProjectThreadServiceImpl")
    private CreatProjectService creatProjectService;
    @Resource(name = "creatProjectDydjThreadServiceImpl")
    private CreatProjectService creatDyProjectService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcdjbService;

    @Override
    public void initCommodityHouseFirstRegistrationProject(Xmxx xmxx) {
        List<String> zdzhhList = bdcTdService.getZdzhhList(xmxx);
        if(CollectionUtils.isNotEmpty(zdzhhList)){
            bdcTdService.initBdcTdAhead(xmxx, zdzhhList);
            bdcdjbService.initBdcBdcdjb(xmxx, zdzhhList);
        }
        creatProjectService.initVoFromOldData(xmxx);
    }

    @Override
    public void initBuildingConstructionMortgage(Xmxx xmxx) {
        List<String> zdzhhList = bdcTdService.getZdzhhList(xmxx);
        if(CollectionUtils.isNotEmpty(zdzhhList)){
            bdcTdService.initBdcTdAhead(xmxx, zdzhhList);
            bdcdjbService.initBdcBdcdjb(xmxx, zdzhhList);
        }
        creatDyProjectService.initVoFromOldData(xmxx);
    }

}
