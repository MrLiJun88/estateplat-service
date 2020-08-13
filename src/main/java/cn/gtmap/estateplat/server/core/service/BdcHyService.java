package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcHy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjZhxx;
import cn.gtmap.estateplat.model.server.core.Project;


/**
 * 宗海信息
 * @author zhx
 * @version V1.0, 2016-1-5
 */
public interface BdcHyService {
    /**
     * zhx根据宗地宗海号查找bdc_td
     *
     * @param zdzhh
     * @return
     */
    BdcHy selectBdcHy(final String zdzhh);

    /**
     * zhx 将地籍库里面的信息继承到bdc_td里面
     *
     * @param djsjZhxx
     * @param bdcHy
     * @return
     */
    BdcHy getBdcHyFromZhxx(final DjsjZhxx djsjZhxx, final Project project, BdcHy bdcHy);

    /**
     * zhx 将地籍库里面的信息继承到bdc_hy里面
     *
     * @param djsjZhxx
     * @param bdcHy
     * @return
     */
    BdcHy getBdcHyFromDjxx(final DjsjZhxx djsjZhxx, final Project project, BdcHy bdcHy, final String qllx);


    /**
     * zhx 删除项目对应的宗地信息
     *
     * @param proid
     */
    void deleteBdcHyByZdzhh(final String proid);

    /**
     * zhx 改变海域使用权状态
     *
     * @param bdcXm
     */
    void changeHysyqZt(BdcXm bdcXm);

    /**
     * zhx 恢复状态
     *
     * @param bdcXm
     */
    void changeBackHysyqZt(BdcXm bdcXm);
}
