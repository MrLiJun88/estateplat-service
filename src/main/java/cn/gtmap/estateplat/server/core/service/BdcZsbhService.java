package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcZs;
import cn.gtmap.estateplat.model.server.core.BdcZsbh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-9-18
 * Time: 下午3:55
 *
 * @description 不动产登记证书编号（证书印制号）服务
 */
public interface BdcZsbhService {
    List<BdcZsbh> getBdcZsBhListByBhfw(Map map);

    String checkAndSaveZsh(List<BdcZsbh> bdcZsbhList, BdcZsbh bdcZsbh, String qsbh, String jsbh);

    String getMaxZsbh(final String zslx);

    String getZsbh(final String zslx);

    /**
     * 用印号分配到乡镇
     *
     * @param hashMap
     * @return
     */
    String getZsbhByDwdm(HashMap hashMap);


    String getzfbl(final String zslx);

    Map getZsYjByZslx(final String zslx);

    /**
     * 查询最小编号并且保存
     *
     * @param zslx
     * @param zsid
     * @return
     */
    String getZsBh(final String zslx, final String zsid);

    void changeZsBhZt(BdcXm bdcXm, BdcZsbh bdcZsbh);

    void workFlowBackZsBh(final String proid);

    /*
   * zwq 验证证书编号
   * */
    String checkZsbh(final String proid, final String zslx, final String zsbh, final String lqr, final String lqrid, final String zsid);

    List<BdcZsbh> getZsBhByMap(final HashMap hashMap);

    /*
   * zwq 办结的时候改变证书编号的状态，由已使用变为已打证
   * */
    void changeZsSyqk(BdcXm bdcXm);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcZsbhList办结的时候批量改变证书编号的状态，由已使用变为已打证
     */
    void batchChangeZsSyqk(List<BdcXm> bdcXmList);


    /*
     *  根据起止箱号获取编号
     */
    List<BdcZsbh> getBdcZsBhListByQzBh(Map map);

    /*
    * 验证证书编号
    */
    String checkZsbhNew(String proid, String zslx, String zsbh, String organName, String zsid,String syr);



   //根据箱号获取编号数量
    int getCountBdcZsBhByXh(int xh);

    /**
     * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
     * @param zsbhid
     * @description 销毁证书编号
     */
    void zsbhXh(String zsbhid,String xhr,String xhrid,String xhjzr);

    /**
     * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
     * @param
     * @description 取消销毁证书
     */
    void qxZsbhXh(String zsbhid);


    /**
     * @param bdcZsList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcZsList批量选择证书编号
     */
    List<BdcZsbh> batchSelectBdcZsbh(List<BdcZs> bdcZsList);


    /**
     * @param bdcZsbhList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcZsbhList批量更新证书编号
     */
    void batchUpdateBdcZsbh(List<BdcZsbh> bdcZsbhList);
}
