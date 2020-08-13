package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.server.core.service.BdcBtxyzService;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2019/4/12
 * @description 必填项验证 多线程
 */
public class BtxyzThread extends CommonThread {
    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>·
     * @description 必填项验证
     */
    private BdcBtxyzService bdcBtxyzService;

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 参数项目ID集合
     */
    private List<String> proids;

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 参数 验证项
     */
    private Map<String, List<Map>> validateTableMap;
    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 参数 sqlMap
     */
    private Map<String, String> tableSqlMap;

    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 返回值
     */
    private List<Map> resultList;

    public BtxyzThread(BdcBtxyzService bdcBtxyzService, List<String> proids, Map<String, List<Map>> validateTableMap, Map<String, String> tableSqlMap) {
        this.bdcBtxyzService = bdcBtxyzService;
        this.proids = proids;
        this.validateTableMap = validateTableMap;
        this.tableSqlMap = tableSqlMap;
    }

    /**
     * 默认执行方法
     */
    @Override
    public void execute() throws Exception {
        resultList = bdcBtxyzService.btxyzThread(proids, validateTableMap, tableSqlMap);
    }

    public List<Map> getResultList() {
        return resultList;
    }
}
