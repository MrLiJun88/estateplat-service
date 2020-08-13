package cn.gtmap.estateplat.server.service.currency;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhengqi@gtmap.cn">zhengqi</a>
 * @version 1.0, 2020/3/16
 * @description 获取c包接口
 */
public interface CurrencyService {

    void saveJyxx(String wiid);

    String checkJyzt(String fwbm, String tslx);

    void tsJyFwzt(String wiid, String tszt);

    String checkHouseZt(List<Map> mapList);

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @description 完成业务环节
     */
    void finishStep(String proid, String userid);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/6/22 14:55
      * @description 更新互联网受理状态
      */
    void updateSlzt(String proid, String slzt);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @Time 2020/7/7 17:03
      * @description 项目删除  更新djzt
      */
    void changeDjzt(String wwslbh);
}
