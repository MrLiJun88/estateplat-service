package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBdcdySd;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产单元锁定
 */
public interface BdcBdcdysdMapper {
    /**
     * 新增不动产单元锁定
     * @param sd
     */
    void insertBdcbdcdySd(BdcBdcdySd sd);

    /**
     *  更新
     */

    void updateBdcbdcdySd(BdcBdcdySd sd);

    /**
     * 获取所有数据
     * @param map
     * @return
     */
    List<BdcBdcdySd> getBdcdySdListByMap(Map map);

    /**
     * 根据不动产单元id查询
     * @param bdcdyid
     * @return
     */
    BdcBdcdySd findBdcBdcdySd(String bdcdyid);

    /**
     *根据bdcdyh查询
     * @param bdcdyh
     * @return
     */
    BdcBdcdySd findBdcBdcdySdByBdcdyh(String bdcdyh);
}
