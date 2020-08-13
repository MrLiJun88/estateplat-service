package cn.gtmap.estateplat.server.core.mapper;


import cn.gtmap.estateplat.model.server.core.BdcXtYh;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @description 银行配置信息
 * Date: 15-9-18
 * Time: 下午1:50
 */
@Repository
public interface BdcXtYhMapper {
    HashMap getSjdBz(@Param(value="sfxmmc")String sfxmmc,
                     @Param(value="qlrlx")String qlrlx);

    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:获取银行信息
    *@Date 18:18 2017/4/24
    */
    List<BdcXtYh> getBankListByPage();
    /**
    *@Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
    *@Description:根据yhmc获取银行信息
    *@Date 10:59 2017/4/25
    */
    List<BdcXtYh> getBankListByYhmc(@Param(value="yhmc")String yhmc);
}
