package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcZjjzwxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 不动产在建建筑物信息
 */
public interface BdcZjjzwxxMapper {


    /**
     * 获取在建建筑物抵押清单
     * @param map 查询条件
     * @return 在建建筑物抵押清单
     */
    List<BdcZjjzwxx> getBdcZjjzwxx(Map map);


    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param bdcdyh 不动产单元号
     * @return 处于现势抵押状态的不动产单元数量
     * @description 根据不动产单元号查询是否有现势的在建工程抵押
     */
    int getDyBdcZjjzwxxByBdcdyh(String bdcdyh);

    /**
     * @param proid
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 获得与proid同流程里的再见建筑物清单
     */
    List<BdcZjjzwxx> getSameWFZjjzwxx(String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 更新抵押物清单
     */
    public void updateZjjzwDyzt(Map map);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/15 15:13
      * @description 根据proid查询BdcZjjzwxx
      */
    BdcZjjzwxx getBdcZjjzwxxByProid(String proid);
}
