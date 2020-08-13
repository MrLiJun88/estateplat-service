package cn.gtmap.estateplat.server.core.mapper;

import cn.gtmap.estateplat.model.server.core.GdFwsyq;

import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/5
 * @description 过渡房屋所有权
 */
public interface GdFwsyqMapper {

    /**
    * @author <a href="mailto:xusong@gtmap.cn">xusong</a>
    * @param bdcdyh 不动产单元号
    * @return
    * @Description: 根据项不动产单元号获取过渡房屋所有权
    */
    List<GdFwsyq> getGdFwsyqListByBdcdyh(String bdcdyh);
}
