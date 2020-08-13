package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.yw.WfProjectService;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/2/25
 * @description 初始化数据 多线程
 */
public class InitDataThread extends CommonThread {

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 初始化数据
     */
    private WfProjectService wfProjectService;
    /**
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化参数
     */
    private ProjectPar projectPar;

    public InitDataThread(WfProjectService wfProjectService, ProjectPar projectPar) {
        this.wfProjectService = wfProjectService;
        this.projectPar = projectPar;
    }

    @Override
    public void execute() throws Exception {
        wfProjectService.initDataThread(projectPar);
    }
}
