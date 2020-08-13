package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 过渡土地
 * Created by lst on 2015/3/23
 */
@Repository
public interface GdTdMapper {

    /**
     * 获取土地数据信息
     *
     * @param map
     * @return
     */
    List<Map> getGdTdJsonByPage(Map map);

    GdTd getGdTdByTdid(String tdzh);

    String getGdTdzhTdid(String tdid);

    List<GdQlr> getqlrBytdid(String tdid);

    List<GdQlr> getQlrByName(String name);

    String getGdTdSyqx(String dldm);

    List<GdTdsyq> getGdTdSyq(String tdid);

    /**
     * 根绝土地ID获取新老地号对照表新地籍号
     *
     * @param tdid
     * @return
     */
    String getDhDzbDjhByTdid(String tdid);

    /**
     * 根绝土地ID获取宗地调查表新地籍号
     *
     * @param tdid
     * @return
     */
    String getZdDcbDjhByTdid(String tdid);

    /**
     * 根据土地ID获取土地证
     *
     * @param tdid
     * @return
     */
    String getTdzhByTdid(String tdid);

    /**
     * 根据土地ID获取不动产单元号
     *
     * @param map
     * @return
     */
    String getBdcdyhByTdids(Map map);

    /**
     * 根据tdid获取过渡土地
     *
     * @param map
     * @return
     */
    List<GdTd> getGdTdyTdids(Map map);


    /*
    * zwq 根据房产证号查询土地证号
    * */
    List<GdTdsyq> getTdsyqByFczh(Map hashMap);

    /**
     * yl根据土地坐落获取过渡土地
     *
     * @param zl
     * @return
     */
    List<String> getGdTdQlidByZl(String zl);

    /**
     * hqz 获取房屋匹配的过度土地权利ID
     *
     * @param qlid
     * @return
     */
    String getGdTdQlidByFwQlid(String qlid);

    /**
     * @param djh
     * @author <a href="mailto:sunchao@gtmap.cn">sc</a>
     * @rerutn
     * @description 根据土地地籍号获取过渡土地权利id
     */
    List<String> getGdTdQlidByDjh(String djh);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 查询过渡房屋权利
     */
    List<HashMap> getGdTdQl(final Map map);

    /**
     * @param
     * @author <a href="mailto:liuzhiqiang@gtmap.cn">liuzhiqiang</a>
     * @rerutn
     * @description 根据过渡房产证号查询土地他项证
     */
    List<GdDy> getTdDyByFczh(Map hashMap);

    /**
     * @param qlid 权利ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 获取权利ID对应的过渡土地对象
     */
    List<GdTd> getGdTdByQlid(String qlid);

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 更具权利ID更新补录数据的审核状态
     */
    void updateSfsh(String qlid);

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 根据土地id查询土地所有权
     */
    List<GdTdsyq> getGdTdsyqByTdid(@Param(value = "tdid") String tdid);


    /**
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 根据不动产单元号查询土地证号
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
     * @param
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
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

    List<GdTdsyq> getGdTdSyqList(HashMap map);
}
