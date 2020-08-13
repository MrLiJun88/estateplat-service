package cn.gtmap.estateplat.server.service.sjgl;

import cn.gtmap.estateplat.server.core.model.vo.SjglRequest;

import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-13
 * @description
 */
public interface SjglService {
    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取单元信息
     */
    Map getDyh(SjglRequest sjglRequest);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利信息信息
     */
    Map getQl(SjglRequest sjglRequest);
}
