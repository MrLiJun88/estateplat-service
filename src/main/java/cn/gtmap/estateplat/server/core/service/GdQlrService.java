package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.GdFw;
import cn.gtmap.estateplat.model.server.core.GdQlr;
import cn.gtmap.estateplat.model.server.core.GdTd;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-10
 * @description 过渡权利人服务
 */
public interface GdQlrService {
    /**
     * zdd 根据权利ID  以及权利人类型 查找GDQLR
     *
     * @param qlid
     * @param qlrlx
     * @return
     */
    List<GdQlr> queryGdQlrs(String qlid, String qlrlx);

    /**
     * zdd 读取GdQlr到BdcQLr
     *
     * @param gdQlrs
     * @return
     */
    List<BdcQlr> readGdQlrs(List<GdQlr> gdQlrs);

    /**
     * zx 读取GdFwQlr到BdcQLr
     *
     * @param gdQlrs
     * @param gdFw
     * @param proid
     * @return
     */
    List<BdcQlr> readGdFwQlrs(List<GdQlr> gdQlrs, GdFw gdFw, String proid);

    /**
     * zx 读取GdTdQlr到BdcQLr
     *
     * @param gdQlrs
     * @return
     */
    List<BdcQlr> readGdTdQlrs(List<GdQlr> gdQlrs, GdTd gdTd);

    /**
     * 根据权利Id删除过渡权利人
     *
     * @param qlid
     */
    void delGdQlrByQlid(String qlid);

    /**
     * 根据项目id和权利人类型获取权利人
     * @param proid
     * @param qlrlx
     * @return
     */
    List<GdQlr> queryGdQlrListByProid(String proid, String qlrlx);

    /**
     * 根据项目id和权利人类型获取权利人
     * @param proid
     * @param qlrlx
     * @return
     */
    String getGdQlrsByProid(String proid, String qlrlx);

    /**
     * 根据权利人查询权利人信息
     * @param qlr
     * @param qlrlx
     * @return
     */
    List<GdQlr> queryGdQlrsByQlr(String qlr, String qlrlx);

    /**
     * 根据权利id和权利人类型获取权利人
     * @param qlid
     * @param qlrlx
     * @return
     */
    String getGdQlrsByQlid(String qlid, String qlrlx);

    /**
     * zx合并权利人
     * @param gdQlrList
     * @return
     */
    String combinationQlr(List<GdQlr> gdQlrList);
}
