package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcFdcq;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.DjsjFwHs;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记房产产权（独幢）权利服务
 * User: Administrator
 * Date: 15-4-15
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */
public interface BdcFdcqService {
    /**
     * 获取房地产权登记信息（独幢、层、套、间房屋）
     *
     * @param map
     * @return
     */
    List<BdcFdcq>  getBdcFdcq(final Map map);

    /**
     * sc: 根据proid获取房屋自然栋栋号
     *
     * @param proid
     * @return
     */
    String getzrddh(final String proid);

    /**
     * sc :根据Proid获取房屋权利信息，房屋性质取字典表
     *
     * @param proid 不动产登记项目ID
     * @return
     */
    List<BdcFdcq> getBdcFdcqByProid(final String proid);


    /**
     * zx :删除不动产房地产独幢权利信息
     *
     * @param proid 不动产登记项目ID
     * @return
     */
    void delBdcFdcqByProid(final String proid);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param bdcFdcq
     * @return
     * @description 保存房地产权信息
     */
    void saveBdcFdcq(BdcFdcq bdcFdcq);

    /**
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @param model,qlid,bdcXm
     * @return
     * @description 初始化不动产房地产权信息页面
     */
    Model initBdcFdcqForPl(Model model, String qlid, BdcXm bdcXm);

    /**
     *
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
     * @param bdcXmList
     * @param qszt
     * @param qlqssj
     * @return
     * @description 批量改变权属状态
     */
    void batchChangeQllxZt(List<BdcXm> bdcXmList, Integer qszt, Date qlqssj);

    /**
     * @author
     * @param
     * @return
     * @description 获取审批表打印页面的土地房屋面积等信息
     */
    HashMap<String, String> getTdAndFwSjxx(String proid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过fwhs中的数据更新fdcq数据
     */
    void updateFdcqByDjsjfwhs(DjsjFwHs djsjFwHs,BdcFdcq bdcFdcq);
}
