package cn.gtmap.estateplat.server.service.core;

import cn.gtmap.estateplat.model.server.core.Project;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/7/29
 * @description 项目限制条件验证服务
 */
public interface ProjectValidateService {
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @param param 项目信息
     * @return
     * @description 验证项目有效性
     */
    Map<String, Object> validate(HashMap param);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @param
     * @return 获取此验证逻辑的代码编号
     * @description 获取此验证逻辑的代码编号
     */
    String getCode();

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/29
     * @param
     * @return 获取此验证逻辑的描述信息
     * @description 获取此验证逻辑的描述信息
     */
    String getDescription();
}
