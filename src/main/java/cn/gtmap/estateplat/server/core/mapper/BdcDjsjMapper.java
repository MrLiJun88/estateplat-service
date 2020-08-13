package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 读取房屋、林权、宗地信息等权籍数据信息
 * Created by lst on 2015/3/17.
 */

public interface BdcDjsjMapper {
    /**
     * 通过id读取林权信息
     *
     * @param id
     * @return
     */
    DjsjLqxx getDjsjLqxx(@Param("id") final String id);

    /**
     * 通过id读取水产养殖信息
     *
     * @param id
     * @return
     */
    DjsjNydDcb getDjsjTtxx(@Param("id") final String id);

    /**
     * 通过不动产的单元号查询qsxz
     *
     * @param bdcdyh
     * @return
     */
    String getDjsjQsxz(@Param("bdcdyh") final String bdcdyh);

    /**
     * 通过djh读取林权信息
     *
     * @param djh
     * @return
     */
    List<DjsjLqxx> getDjsjLqxxByDjh(@Param("djh") final String djh);

    /**
     * 通过id读取宗地信息
     *
     * @param id
     * @return
     */
    List<DjsjZdxx> getDjsjZdxx(@Param("id") final String id);

    /**
     * 通过id读取农用地信息
     *
     * @param id
     * @return
     */
    List<DjsjZdxx> getDjsjNydxx(@Param("id") final String id);

    /**
     * 通过djh读取农用地信息
     *
     * @param djh
     * @return
     */
    List<DjsjZdxx> getDjsjNydxxByDjh(@Param("djh") final String djh);

    /**
     * 通过id读取房屋信息
     *
     * @param bdcdyh
     * @return
     */
    List<DjsjFwxx> getDjsjFwQlr(@Param("id") final String bdcdyh);

    /**
     * 通过djh读取宗地信息
     *
     * @param djh
     * @return
     */
    List<DjsjZdxx> getDjsjZdxxForDjh(final String djh);

    /**
     * 通过djh读取宗海信息
     *
     * @param djh
     * @return
     */
    List<DjsjZhxx> getDjsjZhxxForDjh(final String djh);
    /**
     * 通过id读取宗海信息
     *
     * @param id
     * @return
     */
    DjsjZhxx getDjsjZhxx(@Param("id") final String id);

    /**
     * @param zhdm 宗海代码
     * @return 宗海内部单元记录表
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据宗海代码获取宗海内部单元记录表
     */

    List<DJsjZhjnbdyjlb> getDJsjZhjnbdyjlb(final String zhdm);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 通过djid获取老地籍号
     */
    List<String> getOldDjh(final String djh);

    /**
     * @param djh
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn Integer
     * @description 根据地籍号查询不动产数量
     */
    Integer queryBdcdyCountByDjh(final String djh);

    DjsjGzwxx getDjsjGzwxx(@Param("id") final String id);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param djid
     * @rerutn
     * @description 通过djid获取宗地是否为剩余宗地
     */
     String getDjsjZdIsSyzdByDjid(final String djid);

    /**
     * 获取宗地信息（土地用途等字段按照字典表转换）
     * @param id
     * @return
     */
    List<DjsjZdxx> getDjsjZdxxWithZd(final String id);
}
