package cn.gtmap.estateplat.server.core.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2017/3/1
 * @description 匹配检查服务接口
 */
public interface BdcCheckMatchDataService {
    /**
     * @param
     * @return
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 检查数据是否可以匹配
     */
    Map checkMatchData(String qlid, String gdid, String bdcdyh, String tdid, String tdqlid, String bdclx);

    /**
     * @param
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 跨宗数据匹配验证
     **/
    HashMap checkMulMatchData(String qlid, String gdid, String bdcdyh, String tdqlid, String bdclx);

}
