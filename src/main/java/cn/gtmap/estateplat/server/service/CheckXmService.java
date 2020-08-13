package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.model.server.core.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lisongtao@gtmap.cn">lisongtao</a>
 * @version 1.0  2018/6/15.
 * @description
 */
public interface CheckXmService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 根据project中的权利类型、申请类型和需要过滤的验证类型做验证获取验证结果列表
     */
    List<Map<String, Object>> checkXmByProject(Project project);

}
