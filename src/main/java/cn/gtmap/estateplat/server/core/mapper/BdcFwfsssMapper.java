package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcFwfsss;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2017-04-13
 * @description 房屋附属设施
 */
public interface BdcFwfsssMapper {
    /**
     * @author <a href="mailto:lijian@gtmap.cn">juyulin</a>
     * @rerutn String
     * @description 通过fwid初始化房屋附属设施
     */
    BdcFwfsss intiFwfsssByFwid(String fwid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param zfbdcdyh 主房的不动产单元号
     * @rerutn
     * @description 初始化子户室作为附属设施
     */
    List<BdcFwfsss> initBdcFwfsssFromZhsWithZhsIndex(final String zfbdcdyh);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @param map
     * @rerutn
     * @description 获取附属设施
     */
    List<BdcFwfsss> getBdcFwfsssList(Map map);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map
     * @rerutn
     * @description 批量删除附属设施
     */
    void batchDelBdcFwfsss(Map map);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@params
     *@return
     *@description 获取权籍中子户室
     */
    List<BdcFwfsss> getDjsjFwzhsListByFwbm(String fwdm);

    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param zfbdcdyh 主房不动产单元号
    * @return
    * @Description: 除了吴江地区外初始化子户室作为附属设施
    */
    List<BdcFwfsss> initBdcFwfsssFromZhsWithZhsFwbm(final String zfbdcdyh);
}
