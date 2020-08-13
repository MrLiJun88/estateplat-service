package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcDyForQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by lst on 2015/3/12.
 * @description 不动产单元mapper
 */
@Repository
public interface BdcDyMapper {
    /**
     * 插入不动产单元数据
     *
     * @param bdcdy
     */
    void saveDjBdcdyxx(final BdcBdcdy bdcdy);

    List<BdcBdcdy> queryBdcBdcdy(final Map map);

    /**
     * sc 根据宗地宗海号查询不动产信息
     *
     * @param djbid
     * @return
     */
    List<BdcDyForQuery> queryBdcdyByZdzhh(final String djbid);

    /**
     * sc: 根据proid获取宗地号
     *
     * @param proid
     * @return
     */
    String getZhhByProid(final String proid);

    /**
     * 匹配不动产单元和房产证
     *
     * @param map
     * @return
     */
    List<Map> queryBdcdyhByDah(final Map map);

    /**
     * 根据proid获取bdclx
     *
     * @param proid
     * @return
     */
    String getBdclxByPorid(final String proid);

    /**
     * 根据bdcdyh获取不动产单元信息
     *
     * @param bdcdyh
     * @return
     */
    BdcBdcdy getBdcdyhByDyh(@Param("bdcdyh") final String bdcdyh);


    /**
     * 根据登记项目ID查询不动产单元
     * @param proid
     * @return
     */
    BdcBdcdy getBdcdyByProid(final String proid);


    /**
     * 根据不动产单元号
     * @param bdcdyid
     * @return
     */
    HashMap<String,String> getBdcZsByBdcdyid(final String bdcdyid);

}
