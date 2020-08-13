package cn.gtmap.estateplat.server.thread;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.CheckXmService;
import cn.gtmap.estateplat.server.service.ProjectCheckInfoService;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lisongtao@gtmap.cn">lisongtao</a>
 * @version 1.0  2018/6/14.
 * @description 检测的线程类处理
 */
public class BdcCheckThread implements Runnable {

   /**
    * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
    * @description  验证service
    */
    private ProjectCheckInfoService projectCheckInfoService;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description  验证项目service
     */
    private CheckXmService checkXmService;

    /**
     * 是否是转发前验证
     */
    private Boolean beforeCheck;

    /**
     * project参数
     */
    private Project project;

    /**
     * 返回值
     */
    private List<Map<String, Object>> result;

    /**
     * 构造函数
     */
    public BdcCheckThread(CheckXmService checkXmService, ProjectCheckInfoService projectCheckInfoService, Boolean beforeCheck, Project project){
        this.beforeCheck=beforeCheck;
        this.projectCheckInfoService=projectCheckInfoService;
        this.project=project;
        this.checkXmService=checkXmService;

    }

    @Override
    public void run() {
        if(beforeCheck) {
            if(project!=null && StringUtils.isNotBlank(project.getProid())) {
                result = projectCheckInfoService.checkXm(project.getProid(), false,ParamsConstants.ALERT_LOWERCASE,"","");
            }
        }else{
            result = checkXmService.checkXmByProject(project);
        }
    }

    public List<Map<String, Object>> getResult() {
        return result;
    }
}
