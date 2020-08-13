package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2016/12/15
 * @description
 */
public class CreatProjectBdcdyDjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcSdService bdcSdService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        String sqlxdm = "";
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        } else {
            throw new AppException(4005);
        }
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if (bdcxm != null) {
            if(StringUtils.isNotBlank(bdcxm.getSqlx())){
                sqlxdm = bdcxm.getSqlx();
            }
            list.add(bdcxm);
        }
        // 项目关系
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            list.addAll(bdcXmRelList);
        }

        // 审批信息
        InitVoFromParm initVoFromParm = super.getDjxx(xmxx);
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null) {
            list.add(bdcSpxx);
        }
        // 处理锁定和解锁信息
        bdcSdService.handleSdxxThroughWorkflow(project, sqlxdm);
        //zdd 权利人信息
        List<BdcQlr> tempBdcQlrList = null;
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList)
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        return list;
    }
}
