package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过渡房屋
 * Created by lst on 2015/3/24
 */
public interface GdFwService {

    /**
     * zdd 根据主键查找房屋信息
     *
     * @param fwId
     * @return
     */
    GdFw queryGdFw(final String fwId);

    /**
     * zdd 读取BdcFdcq从过度房屋信息中
     *
     * @param gdFwList
     * @param bdcFdcq
     * @return
     */

    BdcFdcq readBdcFdcqFromGdxx(List<GdFw> gdFwList, List<GdTd> gdTdList, BdcFdcq bdcFdcq, GdFwsyq gdFwsyq, GdTdsyq gdTdsyq, BdcXm bdcXm, GdYg gdYg);


    /**
     * zx 首先根据gdqlid查找权利人  如果gdqlid没值  使用fwid查找
     *
     * @param fwid
     * @param gdqlid
     * @return
     */
    List<BdcQlr> readQlrFromGdFw(final String fwid, final String gdqlid);

    /**
     * zdd 过度房屋权利ID 查找对应义务人信息
     *
     * @param fwid
     * @return
     */
    List<BdcQlr> readYwrFromGdFw(final String fwid, final String gdqlid);

    /**
     * zdd 根据map参数条件查询过度信息查封信息
     *
     * @param map
     * @return
     */
    List<GdCf> andEqualQueryGdCf(final Map<String, Object> map);

    /**
     * zx 根据项目id参数条件查询过度信息查封信息
     *
     * @param gdproid
     * @return
     */
    List<GdCf> getGdcfByGdproid(final String gdproid, Integer isjf);

    /**
     * @param
     * @return
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 通过fwid获取档案号
     */
    String getDahByFwid(String fwid);

    /**
     * zdd 根据map参数条件查询过度信息抵押信息
     *
     * @param map
     * @return
     */
    List<GdDy> andEqualQueryGdDy(final Map<String, Object> map);

    /**
     * zx 根据过渡项目id条件查询过度信息抵押信息
     *
     * @param gdproid
     * @return
     */
    List<GdDy> getGdDyListByGdproid(final String gdproid, Integer isjy);

    /**
     * zdd 根据map参数条件查询过度信息抵押信息
     *
     * @param map
     * @return
     */
    List<GdFwsyq> andEqualQueryGdFwsyq(final Map<String, Object> map);

    /**
     * zdd 根据map参数条件查询过度信息预告信息
     *
     * @param map
     * @return
     */
    List<GdYg> andEqualQueryGdYg(final Map<String, Object> map);

    /**
     * zx 根据map参数条件查询过度信息异议信息
     *
     * @param map
     * @return
     */
    List<GdYy> andEqualQueryGdYy(final Map<String, Object> map);

    /**
     * zx 根据gdproid查询过度信息异议信息
     *
     * @param gdproid
     * @return
     */
    List<GdYy> getGdYyListByGdproid(final String gdproid, final Integer iszx);

    /**
     * zdd 根据过度ID 注销对应的查封权属状态
     *
     * @param gdid
     * @param qszt
     */
    void changeGdCfQszt(final String gdid, final Integer qszt);

    /**
     * zdd 根据过度ID 注销对应的抵押权属状态
     *
     * @param qlid
     * @param qszt
     */
    void changeGdDyQszt(final String qlid, final Integer qszt);

    /**
     * zdd 根据过度ID 注销对应的预告权属状态
     *
     * @param gdid
     * @param qszt
     */
    void changeGdYgQszt(final String gdid, final Integer qszt);

    /**
     * 根据过渡项目id房屋获取房屋使用权信息
     *
     * @param gdproid
     * @return
     */
    List<GdFwsyq> queryGdFwsyqByGdproid(final String gdproid);


    List<String> getFwidByDah(final String dah);

    /**
     * 获取过渡抵押信息
     *
     * @param dyid
     */
    GdDy getGdDyByDyid(final String dyid, final Integer iszx);

    /**
     * 从过渡抵押信息获取不动产抵押权信息
     *
     * @param gdDy
     * @param bdcDyaq
     */
    BdcDyaq readBdcDyaqFromGdDy(GdDy gdDy, BdcDyaq bdcDyaq, GdYg gdYg);


    /**
     * 从过渡项目读取数据到Spxx并保存
     * sc
     *
     * @param spxx
     * @param gdproid
     */
    void saveSpxxFromGdXm(BdcSpxx spxx, String gdproid);


    /**
     * 获取过渡预告信息
     *
     * @param ygid
     */
    GdYg getGdYgByYgid(String ygid, Integer iszx);


    /**
     * 获取过渡查封信息
     *
     * @param cfid
     */
    GdCf getGdCfByCfid(final String cfid, final Integer iszx);


    /**
     * 获取过渡异议信息
     *
     * @param yyid
     */
    GdYy getGdYyByYyid(final String yyid, final Integer iszx);

    /**
     * 查询过渡房屋
     *
     * @param map
     * @return
     */
    List<GdFw> getGdFw(final Map map);

    /**
     * 根据权利id查询过渡房屋
     *
     * @param qlid
     * @return
     */
    List<GdFw> getGdFwByQlid(final String qlid);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 根据qlid查询过度房屋，这个为了qlid是逗号隔开这种情况
     **/
    List<GdFw> getGdFwBySplitQlid(String qlid, String split);

    /**
     * 根据项目id查询过渡房屋
     *
     * @param proid
     * @return
     */
    List<GdFw> getGdFwByProid(final String proid, final String qlids);

    /**
     * 根据项目id获取档案号
     *
     * @param proid
     * @return
     */
    String getGdFwDahsByProid(final String proid);

    /**
     * 根据项目id获取房屋id
     *
     * @param proid
     * @return
     */
    String getGdFwidsByProid(final String proid);

    /**
     * 根据项目id获取过渡房屋权利ids
     *
     * @param proid
     * @return
     */
    String getGdFwQlidsByProid(final String proid);

    /**
     * 根据不动产类型和项目id获取他的权利
     *
     * @param gdproid
     * @param bdclx
     * @return
     */
    List<Map> getGdFwQlListByGdproid(final String gdproid, final String bdclx);


    /**
     * 更新过渡项目匹配状态
     *
     * @param gdproid
     * @param ppzt
     */
    void updateGdXmPpzt(final String gdproid, final String ppzt);

    /**
     * 根据项目id获取项目
     *
     * @param gdproid
     * @return
     */
    GdXm getGdXmByGdproid(final String gdproid);

    /**
     * 根据房屋项目id获取土地使用权
     *
     * @param gdproid
     * @return
     */
    List<GdTdsyq> queryTdsyqByGdproid(final String gdproid);


    /**
     * 根据权利id获取过渡权利人
     *
     * @param qlid
     * @param proid
     * @return
     */
    List<BdcQlr> readQlrFromGdFwByGdqlid(final String qlid, final String proid);

    /**
     * 根据权利id获取过渡义务人
     *
     * @param qlid
     * @param proid
     * @return
     */
    List<BdcQlr> readYwrFromGdFwByGdqlid(String qlid, String proid);

    /**
     * sc 根据fwid获取房产证号
     *
     * @param fwid
     * @return
     */
    String getFczhByFwid(String fwid);

    /**
     * sc 根据fwid获取房产证号
     *
     * @param gdproid
     * @return
     */
    String getFczhByGdproid(String gdproid);

    /**
     * 根据过渡proid获取过渡fwzl
     *
     * @param proid
     * @return
     */
    List<String> getGdfwZlByproid(String proid);

    /**
     * zdd 根据不动产单元号  获取权利信息
     *
     * @param map
     * @return
     */
    List<DjbQlPro> getGdqlByBdcdyh(Map<String, String> map);

    /**
     * zx 根据过渡项目id条件查询过度房屋所有权信息
     *
     * @param gdProid
     * @return
     */
    List<GdFwsyq> getGdFwsyqListByGdproid(String gdProid, Integer iszx);

    /**
     * zx 根据过渡项目id条件查询过度预告信息
     *
     * @param gdProid
     * @return
     */
    List<GdYg> getGdYgListByGdproid(String gdProid, Integer iszx);

    /**
     * zx 根据过渡项目id条件查询过度权利信息
     *
     * @param gdProid
     * @param iszx
     * @param bdclx
     * @return
     */
    List getGdQlListByGdproid(String gdProid, Integer iszx, String bdclx);

    /**
     * zx 根据过渡项目id条件查询过渡产权证号
     *
     * @param gdProid
     * @param iszx
     * @param bdclx
     * @return
     */
    String getGdCqzhsByGdproid(String gdProid, Integer iszx, String bdclx, List gdQlList);

    /**
     * zx 根据过渡项目id条件查询权利状态
     *
     * @param gdProid
     * @param iszx
     * @param bdclx
     * @return
     */
    String getQlztByGdproid(String gdProid, Integer iszx, String bdclx, List qlxxList);

    /**
     * zx 根据过渡不动产id或者权利id获取权利ids
     *
     * @param gdBdcid
     * @return
     */
    List<GdBdcQlRel> getGdBdcQlRelByBdcidOrQlid(String gdBdcid, String qlid);

    /**
     * zx 根据过渡不动产id获取权利
     *
     * @param gdBdcid
     * @param bdclx
     * @return
     */
    List getGdQlByBdcid(String gdBdcid, String bdclx);

    /**
     * zx 根据过渡项目id条件查询过渡坐落
     *
     * @param gdProid
     * @param iszx
     * @param bdclx
     * @return
     */
    String getGdZlsByGdproid(String gdProid, Integer iszx, String bdclx, List gdQlList);

    /**
     * zx模糊查询预告权利
     *
     * @param map
     * @return
     */
    List<GdYg> andLikeQueryGdYg(Map<String, Object> map);

    /**
     * zx模糊查询查封权利
     *
     * @param map
     * @return
     */
    List<GdCf> andLikeQueryGdCf(Map<String, Object> map);

    /**
     * zx模糊查询抵押权利
     *
     * @param map
     * @return
     */
    List<GdDy> andLikeQueryGdDy(Map<String, Object> map);

    /**
     * zx模糊查询房屋所有权权利
     *
     * @param map
     * @return
     */
    List<GdFwsyq> andLikeQueryGdFwsyq(Map<String, Object> map);

    /**
     * zx根据产权证号获取过渡权利
     *
     * @param cqzh
     * @param bdclx
     * @return
     */
    List getGdQlByCqzh(String cqzh, String bdclx);

    /**
     * 根据房屋坐落 查询过渡房屋
     *
     * @param fwzl
     * @return
     */
    String[] getFwQlidByFwzl(String fwzl);

    /**
     * zx根据权利id获取权利状态
     *
     * @param qlid
     * @param iszx
     * @param bdclx
     * @param gdQlList
     * @return
     */
    String getQlztByQlid(String qlid, Integer iszx, String bdclx, List gdQlList);

    /**
     * zx权利id获取房屋所有权
     *
     * @param qlid
     * @return
     */
    GdFwsyq getGdFwsyqByQlid(String qlid);

    /**
     * hqz 根据预告权利找到现实的首次登记证书
     *
     * @param ygid
     * @return
     */
    GdFwsyq getGdFwsyqByYgQlid(String ygid);

    /**
     * zx权利id获取权利
     *
     * @param qlid
     * @return
     */
    List getGdQlListByQlid(String qlid, Integer iszx, String bdclx);


    /**
     * 根据房屋权利id获取土地使用权
     *
     * @param qlid
     * @return
     */
    List<GdTdsyq> queryTdsyqByQlid(String qlid);

    /**
     * 根据不动产id获取权利状态
     *
     * @param bdcid
     * @param iszx
     * @param bdclx
     * @return
     */
    String getQlztByBdcid(String bdcid, Integer iszx, String bdclx);

    /**
     * zx根据权利id获取匹配状态
     *
     * @param qlid
     * @param bdclx
     * @param proid 查询不匹配不动产单元时更新的ppzt
     * @return
     */
    String getPpztByQlid(String qlid, String bdclx, String proid);

    /**
     * sc根据项目id获取匹配状态
     *
     * @param proid
     * @param bdclx
     * @return
     */
    String getPpztByProid(String proid, String bdclx);

    /**
     * 根据项目id查询过渡房屋 验证多房屋sql
     *
     * @param proid
     * @return
     */
    List<GdFw> getGdFwByProidForCheckFwDz(String proid, String isck);

    /**
     * lj 通过gdproid确定过渡权利类型
     */
    Object makeSureQllxByGdproid(String gdproid);

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/3/16
     * @param:map
     * @return:List<GdFw>
     * @description:根据gd_xm的proid获取相应的gd_fw
     */
    List<GdFw> getGdFwByGdProid(HashMap hashMap);

    /**
     * @param tdid
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @rerutn
     * @description 通过房屋土地id查询不动产单元号
     */
    String getBdcdyhByFwtdid(String tdid);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 查询过渡房屋权利
     */
    List<HashMap> getGdFwQl(HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 查询过渡房屋权利
     */
    List<GdFwQl> getGdFwQlByMap(HashMap map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据qlid获取权利人
     */
    HashMap<String, String> getGdqlr(String qlid);

    /**
     * @param map
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据GdProid获取CQQID
     */
    List<String> getCqqidByGdProid(HashMap map);

    /**
     * @param bdcdyh
     * @param fwid
     * @return
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据不动产单元号或者房屋id查找gd_fw与djsj_bdcdy_tdfw的关系
     */
    HashMap<String, String> getBdcdyhAndFwid(String bdcdyh, String fwid);

    /**
     * @param fwid 房屋ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据过渡房屋ID获取房屋所有权
     */
    List<GdFwsyq> queryFwsyqByFwid(String fwid);

    /**
     * @param fwid     房屋ID
     * @param fsssFlag 附属设施标记
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据过渡房屋ID获取房屋所有权
     */
    void setFsss(String fwid, String fsssFlag);

    /**
     * @param map
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<GdFw>
     * @description 过滤过度房屋
     */
    List<GdFw> glGdFw(HashMap map);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn String
     * @description 获取需要排除的mc
     */
    String getGdFwExclx(HashMap map);

    /**
     * @param map
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @rerutn List<GdFw>
     * @description 过滤过度相同的房屋
     */
    List<GdFw> glGdFwRemoveSameFw(HashMap map);

    /**
     * @param gdFw
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @rerutn List<GdFw>
     * @description 根据过度房屋获取不动产单元号
     */
    HashMap getBdcdyh(GdFw gdFw);

    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn String
     * @description 根据不动产单元号获取房产证号
     */
    List<HashMap> getFczhByBdcdyh(String bdcdyh);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 通过qlid查询fwid
     **/
    List<String> getFwidByQlid(String qlid);


    List<Map> selectGdfwNopp();
    List<Map>  selectGdfwPpTdNoBdcdy();

    /**
     * lj 通过fwid和权属状态查询房屋所有权
     *
     * @return
     */
    List<GdFwsyq> queryGdFwsyqByFwidAndQszt(String fwid, String qszt);

    /**
     * @param cqzh 产权证号
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据产权证号获取房屋所有权
     */
    List<GdFwsyq> queryFwsyqByCqzh(String cqzh);

    void changeGdqlztByQlid(String qlid,String qszt);

    /**
     * @author <a href="mailto:tanyue@gtmap.cn">tanyue</a>
     * @param map
     * @return tdqlid
     * @description 通过bdcdyh,qllx去查不动产单元匹配的相同权利匹配的土地权利
     */
    List<String> getTdQlidByQllx(Map map);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过权籍bdcdyh查找fwid
     */
    List<String> getFwidByBdcdyh(String bdcdyh);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 根据fwidList查询显示fwsyq的proid
     */
    List<String> getXsFwsyqProidByfwid(List<String> fwidList);
}
