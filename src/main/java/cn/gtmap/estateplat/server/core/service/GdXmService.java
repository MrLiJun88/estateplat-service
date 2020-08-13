package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-8-28
 * Time: 上午9:14
 *
 * @description 过渡项目服务
 */
public interface GdXmService {
    /**
     * zdd 通过主键获取过渡项目
     *
     * @param gdproid
     * @return
     */
    GdXm getGdXm(final String gdproid);

    /**
     * zdd 根据过渡项目ID找到过渡不动产ID
     *
     * @param gdproid
     * @return
     */
    List<String> getGdBdcidByProid(final String gdproid);

    /**
     * zdd 根据过渡权利ID找到过渡权利人
     *
     * @param qlid
     * @param qlrlx
     * @return
     */
    List<GdQlr> getGdqlrByQlid(final String qlid, final String qlrlx);

    /**
     * zdd 根据过渡项目  修改过渡项目的匹配状态
     *
     * @param gdproid
     * @param ppzt
     */
    void updateGdxmPpzt(final String gdproid, final String ppzt);

    /**
     * zdd 根据过渡权利ID 修改权属状态
     *
     * @param qlid
     * @param qszt
     */
    void updateGdQszt(final String qlid, final Integer qszt);

    /**
     * hqz 根据bdc对应的qlids  批量修改过渡项目的匹配状态
     *
     * @param qlids
     * @param ppzt
     */
    void updateGdxmPpztByQlids(final List<String> qlids, final String ppzt);


    /**
     * 根据权利ID获取所有的权力
     *
     * @param qlid
     * @return
     */
    Map getGdql(final String qlid);

    /**
     * sc 获取过度项目的土地ID
     *
     * @param proid
     * @return
     */
    Map getTdidByGdproid(final String proid);

    /**
     * sc 获取过度项目和不动产关系
     *
     * @param map
     * @return
     */
    List<GdBdcQlRel> getGdBdcQlRelByQlidAndBdcId(final Map map);


    Date getRqFromYt(final String qsrq, final String zzrq, final String yt, final String syqlx);


    /*
    * zwq 获取gd_dy数据
    * */
    GdDy getGdDy(final String bdcid);

    /*
   * 获取过渡查封数据
   * */
    List<GdCf> getGdCfList(final Map map);

    List<GdDy> getGdDyList(final Map map);


    /*
   * 获取过渡关联数据，判断是否查封，是否抵押
   * */
    List<String> getCheckGdXX(final String fwid, final String tableName);

    /**
     * 根据过度proid查询匹配的不动产单元
     *
     * @param gdproid
     * @return
     */
    String getBdcdyhsByGdProid(final String gdproid);

    List<String> getDjidByGdid(final String gdid);

    String deleteGdXm(final String proid);

    /**
     * 删除房屋查封相关的记录
     */
    String deleteGdCf(final String proid);

    /**
     * 删除过渡与项目关联，这里暂时只写了房屋项目的
     *
     * @param gdproid
     * @param tdid
     * @param cqid
     * @param lqid
     * @return
     */
    String deleteXmgl(final String gdproid, final String tdid, final String cqid, final String lqid);

    //zwq 通过proid获取qlid和权利
    String getQlidByGdproid(final String gdproid);

    List<GdDyhRel> getGdDyhRelByMap(HashMap hashMap);

    /**
     * @param bdcid,obj,iszx
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn Boolean
     * @description 根据不动产id查询是否存在过度抵押、查封
     */
    Object queryDyAndCfByBdcid(final String bdcid, Object obj, Integer iszx);

    /**
     * @param qlid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据权利id获取GdProid
     */
    List<String> getGdproidByQild(final String qlid);

    /**
     * @param qlr
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据权利人获取GdProids
     */
    List<String> getGdProidByQlr(final String qlr);

    /**
     * @param fwzl
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据房屋坐落获取GdProids
     */
    List<String> getGdProidByFwzl(final String fwzl);

    /**
     * @param gdProid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据Proid获取fwzl
     */
    String getGdXmZLByGdProid(final String gdProid);

    /**
     * @param project
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据过渡Proid获取原证号
     */
    String getYzhByGdproid(Project project);

    /**
     * lst 获取过渡数据登记类型字典
     *
     * @return
     */
    List<GdZdFcxtDjlx> getGdZdFcxtDjlx();

    /**
     * 插入或更新GD_BDCSD表
     *
     * @param gdBdcSd
     */
    int saveOrupdateGdBdcSd(GdBdcSd gdBdcSd);

    /**
     * 根据qlid获取过渡锁定表中的数据
     *
     * @param colName
     * @param colValue
     * @param sdzt
     */
    List<GdBdcSd> getGdBdcSd(final String colName, final String colValue, final int sdzt);

    /**
     * 根据qlid和证号获取过渡锁定表中的数据
     *
     * @param cqzh
     * @param qlid
     * @param sdzt
     */
    List<GdBdcSd> getGdBdcSdByCqzhAndQlid(final String cqzh, final String qlid, final int sdzt);

    /**
     * @param qlid
     * @return String
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据qlid获取gdproid
     */
    String getGdproidByQlid(String qlid);


    /**
     * hqz
     * 获取匹配该不动产单元的所有过渡查封
     *
     * @param bdcdyid 不动产单元id
     * @return 过渡查封集合
     */
    List<GdCf> getGdCfListByBdcdyid(final String bdcdyid);

    /**
     * hqz
     * 获取匹配该不动产单元的所有过渡房屋所有权
     *
     * @param bdcdyid 不动产单元id
     * @return 过渡查封集合
     */
    List<GdFwsyq> getGdFwsyqListByBdcdyid(final String bdcdyid);

    /**
     * hqz
     * 获取匹配该不动产单元的所有过渡土地所有权
     *
     * @param bdcdyid 不动产单元id
     * @return 过渡查封集合
     */
    List<GdTdsyq> getGdTdsyqListByBdcdyid(final String bdcdyid);

    /**
     * @param gdproid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn List<String>
     * @description 根据proid获取qlid
     */
    public List<String> getQlidsByGdproid(String gdproid);


    /**
     * @param bdcdyid
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 根据bdcdyid获取过渡房屋抵押权
     */
    List<GdDy> getGdFwDybyBdcdyid(String bdcdyid);

    /**
     * @param bdcdyid
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 根据bdcdyid获取过渡土地抵押权
     */
    List<GdDy> getGdTdDybyBdcdyid(String bdcdyid);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 苏州除抵押或换证外，其余取房产证和土地证，所以通过不动产单元号查询匹配权利，然后获取相应的原证号,
     **/
    String getGdYzhByBdcdyh(Project project);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param      gdzh
     * @rerutn
     * @description   根据过渡证号获取过渡权利项目id
     */
    List<String> getXmidByGdzh(String gdzh);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @param      map
     * @rerutn
     * @description   获取权利的注销状态
     */
    List<String> getQlzt(HashMap map);

    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param
    * @return
    * @Description: 通过过渡证号获取房屋gdproid
    */
    List<String> getFwGdproidByGdzh(String gdzh);

    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param
    * @return
    * @Description: 通过过渡证号获取土地gdproid
    */
    List<String> getTdGdproidByGdzh(String gdzh);

    /**
     * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
     * @param
     * @return
     * @Description: 通过过渡证号获取抵押gdproid
     */
    List<String> getDyGdproidByGdzh(String gdzh);

    /**
     * @param qlid
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据gdqlid获取proid
    */
    String getGdProidByQlid(final String qlid);
}
