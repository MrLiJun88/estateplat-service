package cn.gtmap.estateplat.server.thread;

import cn.gtmap.estateplat.model.server.core.BdcXm;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
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
public class BdcPreviewInfoForCreateBdczqhThread implements Runnable {

   /**
    * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
    * @description  验证service
    */
    private BdcZsService bdcZsService;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description  验证项目service
     */
    private BdcXm bdcXm;

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
    private Map result;

    /**
     * 构造函数
     */
    public BdcPreviewInfoForCreateBdczqhThread(BdcXm bdcXm, BdcZsService bdcZsService){
        this.bdcZsService=bdcZsService;
        this.bdcXm=bdcXm;
    }

    @Override
    public void run() {
        result=bdcZsService.getInfoForCreateBdcqzh(bdcXm);
    }

    public Map getResult() {
        return result;
    }
}
