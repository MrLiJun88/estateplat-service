package cn.gtmap.estateplat.server.sj.yw;

import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description 业务员数据工作流参数
 */
public interface ProjectParService {
    /**
     * @param project 项目新建的基本信息参数
     * @return 流程参数类
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化流程参数类
     */
    List<ProjectPar> initProjectPar(Project project) throws InvocationTargetException, IllegalAccessException;

    /**
     * @param projectPar 流程参数类
     * @param blmc       变量名称
     * @return projectPar 流程参数类
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化权籍参数
     */
    ProjectPar getQjsj(ProjectPar projectPar, String blmc);
}
