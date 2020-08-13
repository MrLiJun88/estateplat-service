package cn.gtmap.estateplat.server.service.core.config;

import cn.gtmap.estateplat.model.server.core.Project;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2019/11/25
 * @description 验证节点配置接口
 */
public interface ValidateNodeConfigService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param project
     * @param checkCode 验证代码
     * @return Boolean
     * @description 判断当前节点是否验证
     */
    Boolean nodeValidateEnable(Project project, String checkCode);
}
