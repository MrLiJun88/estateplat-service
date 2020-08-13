package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.BdcXmRel;
import cn.gtmap.estateplat.model.server.core.BdcXmzsRel;
import cn.gtmap.estateplat.model.server.core.GdLsCfBh;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * 不动产登记项目关系
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-20
 */
@Repository
public interface BdcXmRelMapper {
    /**
     * zdd 根据项目信息关系表查找记录
     *
     * @param bdcXmRel
     * @return
     */
    public List<BdcXmRel> queryBdcXmRelMapper(BdcXmRel bdcXmRel);

    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @param proid 不动产登记项目ID
     * @return 上一手不动产登记项目ID
     * @description 根据不动产登记项目ID获取上一手不动产登记项目ID
     */

    public List<String> getYproid(String proid);

    /**
     * zx 根据项目id获取该项目之前的历史关系
     *
     * @param proid
     * @return
     */
    public List<BdcXmRel> getHisXmRelList(String proid);

    /**
     * zx 根据项目id获取该项目之后的历史关系
     *
     * @param proid
     * @return
     */
    public List<BdcXmRel> getAfterHisXmRelList(String proid);

    /**
     * sc 根据原项目ID和不动产单元号获取项目关系表
     *
     * @param map
     * @return
     */
    public List<BdcXmRel> getBdcXmRelByYproidAndBdcdyh(Map map);

    /**
     * zx 获取项目所有关系
     *
     * @param map
     * @return
     */
    public List<BdcXmRel> getAllHisRelList(Map map);

    /**
     * lx 通过yproid获取项目所有关系
     *
     * @param map
     * @return
     */
    public List<BdcXmRel> getAllHisRelListByYproid(Map map);

    /**
     *根据不动产权证号获取bdc_xmzs_rel数据
     * @param bdcqzh
     * @return
     */
    public List<BdcXmzsRel> getProidByBdcqzh(String bdcqzh);

    /**
     * @param proid
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 获得与proid同流程的所有xmrel
     */
    List<BdcXmRel> getSameWFXmRelByproid(String proid);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @version
     * @param bdcXmList
     * @return
     * @description 批量删除不动产项目关系表
     */
    void batchDelBdcXmRel(List<BdcXm> bdcXmList);


    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 根据proid获取不动产上下手全部项目关系
     */
    List<BdcXmRel> getAllHisXmRelByProid(String proid);

    /**
     * @author <a href="mailto:jiangganzhi@gtmap.cn">jiangganzhi</a>
     * @return
     * @description 根据proid获取不动产上下手全部项目关系
     */
    List<GdLsCfBh> getAllGdLsCfBhByCfid(String cfid);
}
