package cn.gtmap.estateplat.server.sj.yw.impl;

import cn.gtmap.estateplat.model.server.core.InsertVo;
import cn.gtmap.estateplat.model.server.core.Project;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.sj.sc.DeleteBdcDataService;
import cn.gtmap.estateplat.server.sj.yw.BdcDataYwService;
import cn.gtmap.estateplat.server.sj.yw.BdcTsYwService;
import cn.gtmap.estateplat.server.sj.yw.ProjectParService;
import cn.gtmap.estateplat.server.sj.yw.WfProjectService;
import cn.gtmap.estateplat.server.thread.InitDataThread;
import cn.gtmap.estateplat.server.thread.ThreadEngine;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020/2/14 0014
 * @description
 */
@Service
public class WfProjectServiceImpl implements WfProjectService {
    @Autowired
    private ProjectParService projectParService;
    @Autowired
    private List<BdcDataYwService> bdcDataYwServiceList;
    @Autowired
    private List<BdcTsYwService> bdcTsYwServiceList;
    @Autowired
    private DeleteBdcDataService deleteBdcDataService;
    @Autowired
    @Resource(name = "creatProjectDefaultService")
    private CreatProjectService creatProjectService;
    @Autowired
    private ThreadEngine threadEngine;

    /**
     * @param project 项目
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 初始化数据
     */
    @Override
    public void initData(Project project) throws InvocationTargetException, IllegalAccessException {
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 初始化后面需要的参数
         */
        CommonUtil.sort(bdcDataYwServiceList, "getSxh", null);
        List<ProjectPar> projectParList = projectParService.initProjectPar(project);
        if (CollectionUtils.isNotEmpty(projectParList)) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 先删除数据
             */
            deleteBdcDataService.deleteBdcData(projectParList.get(0));
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 处理特殊业务，先保存
             */
            for (int j = 0; j < bdcTsYwServiceList.size(); j++) {
                List<InsertVo> insertVoList = bdcTsYwServiceList.get(j).initTsYw(projectParList);
                creatProjectService.insertProjectData(insertVoList);
            }
            if (projectParList.size() > 2) {
                List<InitDataThread> initDataThreadList = new ArrayList<>();
                for (int i = 0; i < projectParList.size(); i++) {
                    InitDataThread initDataThread = new InitDataThread(this, projectParList.get(i));
                    initDataThreadList.add(initDataThread);
                }
                threadEngine.excuteThread(initDataThreadList, true);
            } else if (projectParList.size() == 2) {
                //组合流程有执行先后顺序
                for (ProjectPar ProjectPar : projectParList) {
                    initDataThread(ProjectPar);
                }
            } else {
                initDataThread(projectParList.get(0));
            }
        }

    }

    @Override
    public void initDataThread(ProjectPar projectPar) {
        List<InsertVo> insertVoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
            insertVoList.addAll(projectPar.getBdcXmRelList());
        }
        for (int j = 0; j < bdcDataYwServiceList.size(); j++) {
            if (!CommonUtil.indexOfStrs(Constants.INIT_YW_NOBC, bdcDataYwServiceList.get(j).getIntetfacaCode())) {
                insertVoList = bdcDataYwServiceList.get(j).initbdcData(projectPar, insertVoList);
            }
        }
        creatProjectService.insertProjectData(insertVoList);
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 将特殊业务放到保存数据后执行
         */
        for (int j = 0; j < bdcDataYwServiceList.size(); j++) {
            if (CommonUtil.indexOfStrs(Constants.INIT_YW_NOBC, bdcDataYwServiceList.get(j).getIntetfacaCode())) {
                insertVoList = bdcDataYwServiceList.get(j).initbdcData(projectPar, insertVoList);
            }
        }
    }
}
