package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcBdcZsSd;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.GdBdcSd;
import cn.gtmap.estateplat.model.server.core.Project;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @version 1.0, 2018-08-07
 * @description 锁定服务
 */
public interface BdcSdService {

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param project
     * @description 冻结流程根据project插入更新锁定信息
    */
    void handleSdxxThroughWorkflow(final Project project,final String sqlxdm);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param queryMap
     * @description 查询不动产锁定的信息
    */
    List<BdcBdcZsSd> queryBdcZsSdByMap(Map<String,Object> queryMap);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param queryMap
     * @description 查询过渡锁定的信息
     */
    List<GdBdcSd> queryGdBdcSdByMap(Map<String,Object> queryMap);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param  bdcXm
     * @description 根据proid去改变锁定状态
    */
    void changeSdZt(BdcXm bdcXm);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param project
     * @description 处理不动产的锁定信息
     */
    void handleBdcSdxx(final Project project,final String sdrName,final String sqlxdm);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param project
     * @description 处理过渡的锁定信息
     */
    void handleGdSdxx(final Project project,final String sdrName,final String sqlxdm);


    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param wiid
     * @description 锁定流程删除流程就删除锁定数据
    */
    void deleteSdxx(final String wiid);

    /**
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @param wiid
     * @description 解冻流程删除项目将信息还原成冻结状态
    */
    void backSdStatus(final String wiid);
}
