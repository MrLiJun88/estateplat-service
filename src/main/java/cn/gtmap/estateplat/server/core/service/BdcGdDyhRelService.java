package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcGdDyhRel;
import cn.gtmap.estateplat.model.server.core.GdFw;
import cn.gtmap.estateplat.model.server.core.GdQlDyhRel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 不动产登记过渡匹配数据关系表 林权/房产  不动产单元
 *
 * @author lst
 * @version V1.0, 15-4-20
 */
public interface BdcGdDyhRelService {

    /**
     * lst 保存匹配关系
     *
     * @param bdcGdDyhRel
     * @return
     */
    void addGdDyhRel(BdcGdDyhRel bdcGdDyhRel);

    /**
     * @param gdQlDyhRel
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn
     * @description 过渡权利与不动产单元之间的关系，以权利主进行插入
     */
    void addGdQlDyhRel(GdQlDyhRel gdQlDyhRel);


    /**
     * lst 查询匹配关系
     * 用其中一个值查询
     *
     * @param
     * @return
     */
    List<BdcGdDyhRel> getGdDyhRelByDjid(final String dyh, final String djid, final String gdid);

    /**
     * lst 查询匹配关系
     * 用其中一个值查询
     *
     * @param
     * @return
     */
    List<BdcGdDyhRel> getGdDyhRel(final String dyh, final String gdid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据过渡id查询关系
     */
    List<BdcGdDyhRel> getGdDyhRelByGdId(final String gdid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据不动产单元号查询关系
     */
    List<BdcGdDyhRel> getGdDyhRelByDyh(final String bdcydh);


    /**
     * @param
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn
     * @description 查询匹配关系
     */
    List<BdcGdDyhRel> getGdDyhRelList(final String gdids);

    /**
     * @param djid 权籍id inqlid 在所选权利内
     * @author <a href="mailto:zhangqiang@gtmap.cn">zhangqiang</a>
     * @rerutn String
     * @description 多个房屋匹配一个不动产单元，查出fwid集合
     */
    String getGdDyhRelQlidsByDjids(final String djid, final String qlids);

    /**
     * @param qlids
     * @return String
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 多个房屋匹配一个不动产单元，查出Proid集合
     */
    String getProidsByDjids(String qlids);

    List<BdcGdDyhRel> getTdDyhRelBytdids(final String tdids);

    /**
     * @param tdid
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @rerutn
     * @description 通过房屋土地证id查询bdcdyh
     */
    List<BdcGdDyhRel> getGdDyhRel(final String tdid);

    /**
     * @param dyh，qlid，tdqlid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据参数查询gddyhRel
     */
    List<GdQlDyhRel> getGdQlDyhRel(final String dyh, final String qlid, final String tdqlid);

    /**
     * @param proid,bdcdyh
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @rerutn null
     * @description 根据proid和不动产单元号更新GdDyhRel
     */
    void updateGdDyhRelByProidAndBdcdyh(final String proid, final String bdcdyh);

    /**
     * zx 通过过渡项目id获取查询匹配关系
     * 用其中一个值查询
     *
     * @param
     * @return
     */
    List<BdcGdDyhRel> getGdDyhRelByGdproid(final String gdproid);

    /**
     * zx 删除匹配关系
     *
     * @param
     * @return
     */
    void deleteGdPpgx(final String qlid, final String bdclx);

    /**
     * @author bianwen
     * @description 插入权利匹配关系时根据bdcid进行判断
     */
    void addGdQlDyhRelByGdid(GdQlDyhRel gdQlDyhRel,String gdid);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 根据参数生成相应的gdQlDyhRel数据，并返回权利数据，用于匹配
     **/
     void saveGdDyhRels(String fwid, String tdid, String tdqlid, String bdcdyh, String djid);

     /**
      *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
      *@params
      *@return
      *@description 根据bdcdyh选取对应的过渡库中的yqlid
      */
     Map<String,String> getYqlidsAndYxmidsByBdcdyh(String bdcydh);

    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param
    * @return
    * @Description: 根据bdcdyh选取对应的过渡库中净地的yqlid
    */
    Map<String,String> getTdYqlidsAndYxmidsByBdcdyh(String bdcdyh);
}
