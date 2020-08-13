package cn.gtmap.estateplat.server.service.split;

import cn.gtmap.estateplat.model.server.core.BdcFglssj;
import cn.gtmap.estateplat.server.core.model.vo.BdcSplitData;
import cn.gtmap.estateplat.server.core.model.vo.SplitNum;

import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-04-01
 * @description 拆分数据数据层
 */
public interface BdcSplitDao {

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 拆分数据，数据层
     */
    Integer bdcSplitData(List<BdcSplitData> ysjList, List<BdcSplitData> bdcSplitDataList, String fgid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 撤回分割
     */
    void revokeBdcSplit(List<BdcSplitData> ysjList, List<BdcSplitData> bdcSplitDataList);

    /**
     * @param bdcSplitDataList 不动产拆分对象
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 删除原数据
     */
    void deleteYsz(List<BdcSplitData> bdcSplitDataList);

    /**
     * @param bdcSplitDataList 不动产拆分对象
     * @return 新增次数
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 以Json格式插入原数据
     */
    Integer insertYsz(List<BdcSplitData> bdcSplitDataList, String fgid);

    /**
     * @param bdcSplitDataList 不动产拆分对象
     * @return 新增次数
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 插入新数据
     */
    Integer insertXsz(List<BdcSplitData> bdcSplitDataList);

    /**
     * @auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     * @description 初始化分割前所有数据
     */
    List<BdcSplitData> initYsjDataList(String proid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化需要循环的数据
     */
    List<SplitNum> initSplitNum(List<SplitNum> splitNumList, String proid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取拆分前数据
     */
    List<BdcSplitData> getYszByFgid(String fgid);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取拆分后数据
     */
    List<BdcSplitData> getCfhByFgid(String fgid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据fgid获取分割历史数据
     */
    List<BdcFglssj> getBdcFglssjByFgid(String fgid);
}
