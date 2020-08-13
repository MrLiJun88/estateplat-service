package cn.gtmap.estateplat.server.core.service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/11/13
 * @description 不动产验证接口
 */
public interface BdcCheckService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 组织examine验证数据
     */
    List<Map<String, Object>> organizeExamineData(List<String> cfQlidList);

}
