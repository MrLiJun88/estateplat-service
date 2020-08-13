package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcBdcdy;
import cn.gtmap.estateplat.model.server.core.BdcXm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 不动产单元信息
 * Created by sc on 2015/9/01
 */
@Repository
public interface BdcBdcdyMapper {

    /**
     * sc查询不动产单元
     * @param map
     */
    //    List<BdcBdcdy> getBdcdy(HashMap map);

    /**
     * zx根据不动产单元号获取不动产单元id
     * zdd 现有逻辑一定返回一个
     *
     * @param bdcdyh
     */
    String getBdcdyidByBdcdyh(final String bdcdyh);

    /**
     * hqz根据不动产单元号查询出不动产类型（TD TDFW）
     *
     * @param bdcdyh 不动产单元号
     * @return
     */
    String getBdcdylxByBdcdyh(final String bdcdyh);

    /**
     * 生成不动产单元号
     *
     * @param map
     * @return
     */
    String creatBdcdyh(final Map map);

    /**
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据proid获取不动产单元号
     */
    String getBdcdyhByProid(@Param(value = "proid") final String proid);

    List<HashMap> queryBdcdyFwByPage(HashMap map);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据不权利人查询查询参数
     */
    public List<String> getDjhByQlr(final HashMap map);

    /**
     * @param
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 根据不动产单元号查询CQQID
     */
    public List<String> getCqqidByBdcdy(final String bdcdyh);


    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 根据bdcdyid或proid查询权利表
     */
    List<HashMap> getBdcQlxxList(HashMap map);

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 根据bdcdyh查询不动产单元房屋类型
     */
    String getBdcdyfwlxBybdcdyh(String bdcdyh);

    /**
     * @param map
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 根据宗地变更后的地籍号查询变更前的原地籍号
     */
    List<String> getYdjhByDjh(HashMap map);


    /**
     * @param wiid
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据wiid获取本实例所有不动产单元id和对应的坐落
     */
    public List<HashMap> getBdcdyidAndZlByWiid(final String wiid);


    public List<String> selectAllBdcdyh();

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 根据bdcXmList获取不动产单元号
     */
    List<String> getBdcdyhByBdcXmList(List<BdcXm> bdcXmList);

    /**
     * @param bdcXmList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @rerutn
     * @description 根据bdcXmList批量删除不动产单元
     */
    void batchDelBdcBdcdyBdcXmList(List<BdcXm> bdcXmList);

    /**
     * 通过不动产单元号查询不动产类型
     *
     * @param bdcdyh
     * @return
     */
    String getTdDjsjBdclxByBdcdyh(final String bdcdyh);

    /**
     * @param map
     * @author: <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据bdclx和zl去找登记库的bdcdyh
     */
    List<BdcBdcdy> getBdcdyInfoByQueryMap(final Map map);


    Integer getMaxLshByDjh(String djh);

    /**
     * @param bdcdyh 不动产单元号
     * @return 产权proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取产权proid
     */
    List<String> getCqproidByBdcdyh(String bdcdyh);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/5/4 16:37
      * @description 根据proid查询bdcdy
      */
    BdcBdcdy getBdcdyByProid(@Param("proid") String proid);
}
