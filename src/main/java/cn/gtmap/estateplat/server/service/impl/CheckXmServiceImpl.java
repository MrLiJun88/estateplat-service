package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.model.server.core.BdcXtCheckinfo;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.CheckXmService;
import cn.gtmap.estateplat.server.service.ProjectCheckInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lisongtao@gtmap.cn">lisongtao</a>
 * @version 1.0  2018/6/15.
 * @description
 */
@Service
public class CheckXmServiceImpl implements CheckXmService {

    @Autowired
    private ProjectCheckInfoService projectCheckInfoService;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description
     */
    private Logger logger = LoggerFactory.getLogger(CheckXmServiceImpl.class);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 根据project中的权利类型、申请类型和需要过滤的验证类型做验证获取验证结果列表
     */
    @Override
    public List<Map<String, Object>> checkXmByProject(Project project) {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<BdcXtCheckinfo> bdcXtCheckinfoList = projectCheckInfoService.getBdcXtCheckinfoByProject(project);
        if (CollectionUtils.isNotEmpty(bdcXtCheckinfoList)) {
            logger.info("项目验证的规则数：{} {}",bdcXtCheckinfoList.size(),project.getProid());
            resultList = projectCheckInfoService.checkXm(bdcXtCheckinfoList, project);
        }
        return resultList;
    }
}
