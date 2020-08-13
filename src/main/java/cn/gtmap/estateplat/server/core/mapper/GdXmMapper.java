package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: sunchao
 * Date: 15-7-30
 * Time: 上午10:19
 *
 * @description 过渡项目
 */
@Repository
public interface GdXmMapper {
    /**
     * 根据房屋Id获取过渡项目表信息
     * sc
     *
     * @param fwid
     * @return
     */
    GdXm getGdXmByfwid(String fwid);

    GdXm getGdXmBytdid(String tdid);

    /**
     * 根据权利ID获取所有的权力
     *
     * @param qlid
     * @return
     */
    Map getGdql(String qlid);


    /**
     * sc 获取过度项目和不动产关系
     *
     * @param map
     * @return
     */
    List<GdBdcQlRel> getGdBdcQlRelByQlidAndBdcId(Map map);

    /*
   * zwq 获取过渡抵押的数据
   *
   *  @param bdcid
   *  @return GdDy
   * */
    GdDy getGdDyFromBdcid(String bdcid);

    /*
   * 获取过渡查封数据
   * */
    List<GdCf> getGdCfList(Map map);

    /*
   * 获取过渡抵押数据
   * */
    List<GdDy> getGdDyList(Map map);

    /**
     * 根据过度proid查询匹配的不动产单元
     *
     * @param gdproid
     * @return
     */
    String getBdcdyhsByGdProid(String gdproid);

    List<String> getDjidByGdid(String gdid);

    List<String> getQlidByGdproid(String gdproid);

    List<GdDyhRel> getGdDyhRelByMap(HashMap hashMap);

    /**
     * @param qlid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据权利id获取GdProid
     */
    List<String> getGdproidByQild(String qlid);

    /**
     * @param gdProid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据过渡Proid获取原证号
     */
    public String getYzhByGdproid(String gdProid);

    /**
     * @param qlid
     * @author <a href="mailto:wangchangzhou@gtmap.cn">wangchangzhou</a>
     * @rerutn
     * @description 获取权利人
     */
    String getQlrByqlid(String qlid);

    /**
     * @param qlid
     * @author <a href="mailto:wangchangzhou@gtmap.cn">wangchangzhou</a>
     * @rerutn
     * @description 获取义务人
     */
    String getYwrByqlid(String qlid);

    /**
     * @param qlid
     * @return String
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据qlid获取gdproid
     */
    String getGdproidByQlid(String qlid);

    /**
     * @param map
     * @return String
     * yzk
     * @description 根据bdcdyh和yqlid获取tdqlid
     */
    List<String> getTdqlidByBdcdyhAndYqlid(Map map);

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
     * 获取匹配该不动产单元的所有过渡查封
     *
     * @param bdcdyid 不动产单元id
     * @return 过渡查封集合
     */
    List<GdFwsyq> getGdFwsyqListByBdcdyid(final String bdcdyid);

    /**
     * hqz
     * 获取匹配该不动产单元的所有过渡查封
     *
     * @param bdcdyid 不动产单元id
     * @return 过渡查封集合
     */
    List<GdTdsyq> getGdTdsyqListByBdcdyid(final String bdcdyid);

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
}
