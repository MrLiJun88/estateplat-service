package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcQrd;
import cn.gtmap.estateplat.server.core.model.vo.Xgsj;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-03-30
 * @description 不动产确认单服务
 */
public interface BdcQrdService {
    /**
     * @param wiid 工作流实例ID
     * @return 不动产确认单
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 查询不动产确认单
     */
    BdcQrd queryBdcQrd(String wiid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 保存不动产确认单
     */
    void saveBdcQrd(BdcQrd bdcQrd);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化不动产确认单
     */
    BdcQrd initBdcQrd(String wiid);

    /**
     * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
     * @description 初始化修改输出模型
     */
    Map initXgsj(String wiid);
}
