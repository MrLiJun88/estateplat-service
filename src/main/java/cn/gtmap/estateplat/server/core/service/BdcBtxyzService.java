package cn.gtmap.estateplat.server.core.service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2019/4/12
 * @description
 */
public interface BdcBtxyzService {
    List<Map> btxyz(String proid, String workflowId, String workflowNodeId);

    /**
     * @param proids           项目ID集合
     * @param validateTableMap 验证项
     * @param tableSqlMap       sqlMap集合
     * @return 验证结果
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 多线程验证
     */
    List<Map> btxyzThread(List<String> proids, Map<String, List<Map>> validateTableMap, Map<String, String> tableSqlMap);
}
