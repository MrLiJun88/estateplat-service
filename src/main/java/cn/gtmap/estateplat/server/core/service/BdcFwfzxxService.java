package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcFwfzxx;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产登记房屋分幢信息
 */
public interface BdcFwfzxxService {
    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过多幢qlid获取分幢信息
     */
    public List<BdcFwfzxx> getBdcFwfzxxListByQlid(String qlid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 原项目的分幢信息转为当前项目的分幢信息
     */
    public void yBdcFwfzxxTurnProjectBdcFwfzxx(List<BdcFwfzxx> ybdcFwfzxxList, String proid, String qlid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过项目id删除房屋分幢信息数据
     */
    public void deleteProjectBdcFwfzxx(String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过fwfzxxid获取分幢信息
     */
    BdcFwfzxx getBdcFwfzxxListByfzxxid(String fwfzxxid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据proid查找房屋分幢信息
     */
    List<BdcFwfzxx> queryBdcFwfzxxListByProid(String proid);
}
