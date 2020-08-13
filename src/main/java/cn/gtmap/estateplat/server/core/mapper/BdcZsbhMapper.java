package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.BdcZsbh;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunchao
 * Date: 15-9-18
 * Time: 下午3:53
 *
 * @description 证书编号（印制号）
 */
@Repository
public interface BdcZsbhMapper {
    List<BdcZsbh> getBdcZsBhListByBhfw(Map map);

    String getZsbh(String zslx);

    /**
     * 用印号分配到乡镇
     *
     * @param hashMap
     * @return
     */
    String getZsbhByDwdm(HashMap hashMap);

    String getMaxZsbh(String zslx);

    String getzfbl(String zslx);

    /**
     * 获取证书编号预警内容
     *
     * @param zslx
     * @return
     */
    Map getZsYjByZslx(String zslx);

    //通过proid，bdcdyh以及wiid查询证书编号
    List<BdcZsbh> getZsbhByMap(HashMap hashMap);

    /**
     * 更新证书编号
     *
     * @param zsbh
     */
    void updateZsSyData(BdcZsbh zsbh);

    /**
     * 获取编号证书使用情况
     *
     * @param map
     * @return
     */
    List<Map> getBdcZsbhSyListByPage(Map map);



    //根据起止箱号获取编号
    List<BdcZsbh> getBdcZsBhListByQzBh(Map map);


    //根据箱号获取编号数量
    int getCountBdcZsBhByXh(int xh);


    /**
     * @param map
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据map批量选择证书编号
     */
    List<BdcZsbh> batchSelectBdcZsbh(Map map);


    /**
     * @param bdcZsbhList
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据bdcZsbhList批量更新证书编号
     */
    void batchUpdateBdcZsbh(List<BdcZsbh> bdcZsbhList);
}
