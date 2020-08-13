package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.server.core.service.BdcZsbhService;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;

/**
 * @author <a href="mailto:chenjia@gtmap.cn">chenjia</a>
 * @version 1.0  2018/6/14.
 * @description 修改原权利状态多线程
 */
public class BdcChangeYqlztThread implements Runnable {

    private BdcXm bdcXm;

    private BdcZsbhService bdcZsbhService;

    private ProjectService projectService;

    private EndProjectService endProjectDefaultServiceImpl;

    /**
     * 构造函数
     */
    public BdcChangeYqlztThread(BdcZsbhService bdcZsbhService, ProjectService projectService, EndProjectService endProjectDefaultServiceImpl, BdcXm bdcXm) {
        this.bdcZsbhService = bdcZsbhService;
        this.projectService = projectService;
        this.bdcXm = bdcXm;
        this.endProjectDefaultServiceImpl = endProjectDefaultServiceImpl;
    }

    @Override
    public void run() {
        bdcZsbhService.changeZsSyqk(bdcXm);
        try {
            projectService.changeQlztProjectEvent(endProjectDefaultServiceImpl, bdcXm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
