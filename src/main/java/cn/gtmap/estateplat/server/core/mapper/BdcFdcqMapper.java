package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.model.server.core.BdcXm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by lst on 2015/3/18
 * @description 房地产权登记信息（独幢、层、套、间房屋）
 */

public interface BdcFdcqMapper {
    /**
     * 获取房地产权登记信息（独幢、层、套、间房屋）
     *
     * @param map
     * @return
     */
    List<BdcFdcq>  getBdcFdcq(Map map);

    /**
     * sc: 根据proid获取房屋自然栋栋号
     *
     * @param proid
     * @return
     */
    String getzrddh(String proid);

    /**
     * sc :更具Proid获取房屋权利信息，房屋性质取字典表
     *
     * @param proid
     * @return
     */
    List<BdcFdcq> getBdcFdcqByProid(String proid);

    /**
     *
     * @param proid
     * @return
     */
    Map<String,Object> getTdsyqx(String proid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map
     * @return
     * @description 获取商品房首次登记户室的发证类型
     */
    String getSpfscdjHsFzlx(HashMap map);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param bdcXmList
     * @return
     * @description 批量删除房地产权
     */
    void batchDelBdcFdcqByBdcXmList(List<BdcXm> bdcXmList);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param map
     * @return
     * @description 批量改变权属状态
     */
    void batchChangeQllxZt(Map map);

}
