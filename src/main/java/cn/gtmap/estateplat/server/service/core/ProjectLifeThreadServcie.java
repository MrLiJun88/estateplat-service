package cn.gtmap.estateplat.server.service.core;

import cn.gtmap.estateplat.model.server.core.Xmxx;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2018/10/18
 * @description 项目生命周期线程服务
 */
public interface ProjectLifeThreadServcie {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmxx
     * @return
     * @description 使用多线程初始化商品房首次登记项目
     */
    void initCommodityHouseFirstRegistrationProject(Xmxx xmxx);

    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param
    * @return
    * @Description: 使用多线程初始化批量抵押多抵多
    */
    void initBuildingConstructionMortgage(Xmxx xmxx);

}
