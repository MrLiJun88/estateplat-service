package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcZj;
import cn.gtmap.estateplat.model.server.core.BdcZjmx;


import java.util.List;

/**
 * * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/7/19
 * @description 质检服务
 */
public interface BdcZjService {


    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过proid查询质检
     */
    public BdcZj getBdcZjByProid(String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过proid查询质检明细
     */
    public List<BdcZjmx> getBdcZjmxListByProid(String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过proid删除质检
     */
    public void delBdcZjByProid(String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过proid删除质检明细
     */
    public void delBdcZjmxByProid(String proid);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 通过id删除质检明细
     */
    public void delBdcZjmxByid(String id);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 保存质检明细
     */
    public void saveBdcZjmx(List<BdcZjmx> bdcZjmxList);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 保存质检状态
     */
    public void saveBdcZjzt(String proid, String zjzt);

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @param
     * @return
     * @description 生成bdc_xm_rel
     */
    public void initBdcXmRelForBdcZj(String proid, String yxmid);

    /**
     * @author
     * @param
     * @return
     * @description 初始质检信息
     */
    public void initBdcZjxx(String proid, String yxmid,String wiid);
}

