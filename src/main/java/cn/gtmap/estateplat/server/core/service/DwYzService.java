package cn.gtmap.estateplat.server.core.service;


import cn.gtmap.estateplat.model.server.core.Project;

import java.util.HashMap;

/**
 * user:zwq
 * Date:2015-9-16
 * @description 计量单位验证服务
 */
public interface DwYzService {
    //验证新建和过渡流程单位是否是标准单位
    HashMap<String, Object> checkDw(Project project,final String xx,final String checktype,final String qllx);

}
