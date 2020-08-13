package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description 不动产登记权籍系统宗地信息服务
 * Created by lst on 2015/3/17.
 */
public interface BdcDjsjService {

    /**
     * 通过id读取林权信息
     *
     * @param id
     * @return
     */
    DjsjLqxx getDjsjLqxx(final String id);

    /**
     * 通过id读取水产养殖权信息
     *
     * @param id
     * @return
     */
    DjsjNydDcb getDjsjTtxx(final String id);
    /**
     * 通过不动产的单元号查询qsxz
     *
     * @param bdcdyh
     * @return
     */
    String getDjsjQsxz(final String bdcdyh);

    /**
     * 通过id读取宗地信息 由于关联权利人表  可能是多个
     *
     * @param id
     * @return
     */
    List<DjsjZdxx> getDjsjZdxx(final String id);

    /**
     * 获取宗地信息（土地用途等字段按照字典表转换）
     * @param id
     * @return
     */
    List<DjsjZdxx> getDjsjZdxxWithZd(final String id);

    List<DjsjZdxx> getDjsjNydxx(final String id);

    List<DjsjZdxx> getDjsjNydxxByDjh(final String djh);

    List<DjsjQszdDcb> getDjsjQszdDcb(final String djh);
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

    List<DjsjFwxx> getDjsjFwQlr(final String id);

    List<DjsjQszdDcb> getDjsjQszdDcbByQszdDjdcbIndex(final String qszdDjdcbIndex);

    /**
     * 根据不动产单元号获取地籍房屋字表
     *
     * @param bdcdyh
     * @return
     */
    String getBdcdyfwlxByBdcdyh(final String bdcdyh);

    /**
     * 通过id读取海域信息
     *
     * @param id
     * @return
     */
    DjsjZhxx getDjsjZhxx(final String id);

    /**
     * 通过djh读取林权信息
     *
     * @param djh
     * @return
     */
    List<DjsjLqxx> getDjsjLqxxByDjh(@Param("djh") final String djh);

    List<DJsjZhjnbdyjlb> getDJsjZhjnbdyjlb(final String zhdm);

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @param     djh
     * @rerutn
     * @description 通过djh获取老地籍号
     */
    public List<String> getOldDjh(final String djh);

    /**
     * @param djh
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn Integer
     * @description 根据地籍号查询不动产数量
     */
    public Integer queryBdcdyCountByDjh(final String djh);

    /**
     * 通过不动产单元编号和不动产类型查询地级数据的djid
     *
     * @param bdcdyh
     * @param bdclx
     * @return
     */
    String getDjidByBdcdyh(String bdcdyh,String bdclx);

    /**
     * @author bianwen
     * @description  获取构筑物信息
     */
    DjsjGzwxx getDjsjGzwxx(final String id);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     *
     * @param djid
     * @rerutn
     * @description 通过djid获取宗地是否为剩余宗地
     */
    String getDjsjZdIsSyzdByDjid(final String djid);
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:通过proid获取土地调查表信息
    *@Date 15:28 2017/4/20
    */
    List<DjsjZdxx> getDjsjZdxxByProid(final String proid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取权籍承包宗地调查表信息
     */
    DjsjCbzdDcb getDjsjCbzdDcbByDjid(final String djid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过djh获取农用地调查表
     */
    List<DjsjNydDcb> getDjsjNydDcbByDjh(final String djh);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 通过djh获取权籍权属宗地宗地面积
     */
    BdcQszdZdmj getBdcQszdZdmj(final String djh);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取权籍承包宗地承包方
     */
    List<DjsjCbzdCbf> getDjsjCbzdCbfByDcbid(final String dcbid);

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 获取权籍承包宗地发包方
     */
    List<DjsjCbzdFbf> getDjsjCbzdFbfByDcbid(final String dcbid);

    /**
     * @param bdcdyh
     * @param djid
     * @param bdclx
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @description 初始化地籍信息
     */
    InitVoFromParm getDjxx(final String bdcdyh,final String djid,final String bdclx);

    /**
     * @param bdcdyh
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @rerutn
     * @description 根据bdcdyh获取房屋户室信息
     */
    List<DjsjFwHs> getDjsjFwHsByBdcdyh(final String bdcdyh);
}
