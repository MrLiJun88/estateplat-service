package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lst on 2015/3/24
 *
 * @description 过渡宗地服务
 */
public interface GdTdService {

    /**
     * 获取土地数据信息
     *
     * @param map
     * @return
     */
    List<Map> getGdTdJson(final HashMap map);

    /**
     * zdd  根据主键查找土地证信息
     *
     * @param tdId
     * @return
     */
    GdTd queryGdTd(final String tdId);

    /**
     * zx  根据土地证号查找土地证信息
     *
     * @param tdzh
     * @return
     */
    GdTd queryGdTdByTdzh(final String tdzh);

    /**
     * zx  从土地信息获取不动产土地所有权信息(过渡目前没有考虑到所有权暂时先写，方法实现没有)
     *
     * @param gdTd
     * @param bdcTdsyq
     * @return
     */
    BdcTdsyq readBdcTdsyqFromGdTd(GdTd gdTd, GdTdsyq gdTdsyq, BdcTdsyq bdcTdsyq);

    /**
     * zx  从土地信息获取不动产建设用地使用权、宅基地使用权登记信息
     *
     * @param gdTd
     * @param bdcJsydzjdsyq
     * @return
     */
    BdcJsydzjdsyq readBdcJsydzjdsyqFromGdTd(GdTd gdTd, BdcJsydzjdsyq bdcJsydzjdsyq);

    /**
     * 根据房屋id获取土地使用权
     *
     * @param fwid
     * @return
     */
    List<GdTdsyq> queryTdsyqByFwid(final String fwid);

    /**
     * 获取土地ID获取土地所有权
     *
     * @param tdid
     * @return
     */
    List<GdTdsyq> queryTdsyqByTdid(final String tdid);

    /**
     * 获取土地ID获取土地抵押权
     *
     * @param tdid
     * @return
     */
    List<GdDy> queryTddyqByTdid(final String tdid, final Integer iszx);

    /**
     * zdd 先根据qlid查找权利人  再根据过渡土地ID查找
     *
     * @param tdid
     * @param qlid
     * @return
     */
    List<BdcQlr> getQlrFromGdTd(final String tdid, final String qlid);

    List<BdcQlr> getYwrFromGdTd(final String tdid, final String qlid);

    /**
     * 根据权利人名称 查询过渡房屋土地数据
     *
     * @param qlrName
     * @return
     */
    List<GdQlr> getQlrByName(final String qlrName);

    /**
     * 根据权利人名称 查询过渡房屋土地数据 返回数组
     *
     * @param qlrName
     * @return
     */
    String[] getQlrByNameToArr(final String qlrName);

    /**
     * 根据qlid获取权利人数据 并处理返回一个权利人字符串
     *
     * @param qlid
     * @return
     */
    String getqlrByQlid(final String qlid);


    /**
     * 根据项目id和项目id获取过渡土地证号
     *
     * @param proid
     * @return
     */
    String getTdzhsByProid(final String proid);

    /**
     * 根绝土地ID获取新老地号对照表新地籍号
     *
     * @param tdid
     * @return
     */
    String getDhDzbDjhByTdid(final String tdid);

    /**
     * 根绝土地ID获取宗地调查表新地籍号
     *
     * @param tdid
     * @return
     */
    String getZdDcbDjhByTdid(final String tdid);

    /**
     * 根据获取tdzdz获取新djh
     *
     * @param tdzdz
     * @return
     */
    String getNewDjh(final String tdzdz,final String tdid);

    /**
     * 根据土地ID获取土地证
     *
     * @param tdid
     * @return
     */
    String getTdzhByTdid(final String tdid);

    /**
     * 根据土地ID获取不动产单元号
     *
     * @param map
     * @return
     */
    String getBdcdyhByTdids(final Map map);

    List<GdTd> getGdTdyTdids(final Map map);

    /*
    * zwq 获取土地证书状态
    * */
    String getTdzsZt(final String bdcid,final String dyid);

    /**
     * zx 根据过渡项目id条件查询过度房屋所有权信息
     *
     * @param gdProid
     * @return
     */
    List<GdTdsyq> getGdTdsyqListByGdproid(final String gdProid, final Integer iszx);

    /**
     * zx 查询过渡土地所有权信息
     *
     * @param map
     * @return
     */
    List<GdTdsyq> andEqualQueryGdTdsyq(final Map<String, Object> map);

    /**
     * zx 根据权利ID获取过渡土地信息
     *
     * @param qlid
     * @return
     */
    List<GdTd> getGdTdListByQlid(final String qlid);

    /**
     * zx 根据项目ID获取过渡土地信息
     *
     * @param proid
     * @return
     */
    List<GdTd> getGdTdListByProid(final String proid, String qlids);

    /**
     * zx 模糊查询过渡土地所有权
     *
     * @param map
     * @return
     */
    List<GdTdsyq> andLikeQueryGdTdsyq(final Map<String, Object> map);

    /**
     * 根据权利ID获取土地所有权
     *
     * @param qlid
     * @return
     */
    GdTdsyq getGdTdsyqByQlid(final String qlid);

    /**
     * zx根据房屋权利id获取过渡土地
     *
     * @param qlid
     * @return
     */
    List<GdTd> getGdTdListByFwQlid(final String qlid);


    /*
   * zwq 根据房产证号查询土地证号
   * */
    List<GdTdsyq> getTdsyqByFczh(final Map hashMap);

    /**
     * hqz 获取房屋匹配的过度土地权利ID
     *
     * @param qlid
     * @return
     */
    String getGdTdQlidByFwQlid(String qlid);


    /**
     * yl根据土地坐落获取过渡土地
     *
     * @param zl
     * @return
     */
    List<String> getGdTdListByZl(final String zl);


    /**
     * @param djh
     * @author <a href="mailto:sunchao@gtmap.cn">sc</a>
     * @rerutn
     * @description 根据土地地籍号获取过渡土地权利id
     */
    List<String> getGdTdQlidByDjh(final String djh);

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/4/5
     * @param:
     * @return:null
     * @description:通过过度房屋所有权的qlid来查询相对应的gdtdsyq
     */
    List<GdTdsyq> getGdTdsyqByFwQlid(final String qlid);

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/4/6
     * @param:
     * @return:null
     * @description:通过fwid查询关联的gd_td
     */
    List<GdTd> getGdTdByGdDyhFwid(String fwid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 查询过渡土地权利
     */
    List<HashMap> getGdTdQl(final Map map);
    /**
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @param
     * @rerutn
     * @description 根据过渡房产证号查询过渡土地他项证
     */
    List<GdDy> getTdDyByFczh(final Map hashMap);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param qlid 权利ID
     * @return
     * @description 获取权利ID对应的过渡土地对象
     */
    List<GdTd> getGdTdByQlid(final String qlid);
    /**
     * @author:<a href="mailto:zx@gtmap.cn">zx</a>
     * @data:2016/4/6
     * @param:
     * @return:
     * @description:获取地抵押gd_td
     */
    GdDy getGddyqByQlid(final String qlid, final Integer iszx);

    /**
     * 根据地籍号获取土地数据
     *
     * @param djh
     * @return
     */
    GdTd getGdTdByDjh(final String djh);

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description     根据土地id查询土地所有权
     */
    List<GdTdsyq> getGdTdsyqByTdid(String tdid);


    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description  根据不动产单元号查询土地证号
     */
    String getTdzhByBdcdyh(String bdcdyh);


    /**
     * 获取房屋匹配的过度土地权利ID
     *
     * @param qlid
     * @return
     */
    List<String> getGdTdQlidsByFwQlid(String qlid);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param
     * @rerutn
     * @description 根据过渡房产证号查询土地查封
     */
    List<GdCf> getTdCfByFczh(Map hashMap);


    /**
     * @param
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 根据tdid查询土地抵押
     */
    List<GdDy> getGdTdDyByTdid(String tdid);


    List<Map> selectGdtdNopp();

    /**
     * lj 通过tdid和权属状态查询土地所有权
     * @return
     */
    List<GdTdsyq> queryGdTdsyqByTdidAndQszt(String tdid,String qszt);


    /*
   * zwq 根据土地证号查询土地证号
   * */
    List<GdTdsyq> getTdsyqByTdzh(final String tdzh);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 过度信息录入验证土地证号是否重复
     */
    Map validateTdzh(final String tdzh);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param
     * @return
     * @description 通过正则去掉汉字的土地证号搜土地所有权
     */
    List<GdTdsyq> getTdsyqByCqzhjc(final String tdzh);
}
