package cn.gtmap.estateplat.server.sj.yw;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description 工作流流转
 */
public interface WfProjectService {
    /**
     * @param project 项目
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据
     */
    void initData(Project project) throws InvocationTargetException, IllegalAccessException;

    /**
     *@auther <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
     *@description 多线程初始化
     */
    void initDataThread(ProjectPar projectPar);
}
