package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXmcqRel;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-03
 * @description 不动产系统业务模型服务
 */
public interface BdcXmcqRelService {
    /**
     * @param proid 项目Id
     * @return 不动产系统业务模型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据项目Id获取不动产系统业务模型
     */
    List<BdcXmcqRel> queryBdcXmcqRelByProid(String proid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 保存不动产系统业务模型
     */
    void saveBdcXmcqRel(List<BdcXmcqRel> BdcXmcqRelList);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据wiid删除不动产系统业务模型
     */
    void deleteBdcXmcqRelByWiid(String wiid);
}
