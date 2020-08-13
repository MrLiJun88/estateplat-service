package cn.gtmap.estateplat.server.core.service;

import cn.gtmap.estateplat.model.server.core.BdcSpfZdHjgx;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 *不动产商品房与宗地核减关系Mapper
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2016/9/13
 */
public interface BdcSpfZdHjgxService{
    /**
     * 获取商品房核减信息
     * @param proid
     * @return
     */
    public List<BdcSpfZdHjgx> getBdcZdFwRelList(String proid) ;

    /**
     * 更新剩余抵押金额和剩余分摊土地面积
     * @param proid
     */
    public void updateSydyjeAndSyfttdmj(String proid) ;
    /**
     * 更新项目核减抵押金额
     * @param proid
     */
    public void updateXmhjdyje(String proid) ;

    /**
     * 根据不动产单元号获取商品房核减信息
     * @param fwbdcdyh
     * @return
     */
    public List<BdcSpfZdHjgx> getBdcZdFwRelListByBdcdyh(String fwbdcdyh) ;
}
