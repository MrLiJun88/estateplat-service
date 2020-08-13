package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQllxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:qijiadong@gtmap.cn">qijiadong</a>
 * @version 1.0, 2020/1/2
 * @description 转移登记和其他转移登记合并
 */
public class CreatProjectZyAndQtzyHbServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Autowired
    EntityMapper entityMapper;
    @Resource(name = "creatProjectZydjService")
    CreatProjectZydjServiceImpl creatProjectZydjService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    QllxParentService qllxParentService;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    BdcQllxMapper bdcQllxMapper;
    @Autowired
    BdcSqlxDjyyRelService bdcSqlxDjyyRelService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else {
            throw new AppException(4005);
        }
        String workflowProid = "";
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }
        creatProjectNode(workflowProid);

        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //lst根据工作流wiid  查找是否已经存在  如果存在 则删除对应的记录
        if (StringUtils.isNotBlank(project.getWiid())) {
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }
        String sqlx1 = null;
        String sqlx2 = null;
        String sqlxdm = "";
        String zyProid = project.getProid();

        if (StringUtils.isNotBlank(project.getSqlx())) {
            //获取平台的申请类型代码
            if (StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(sqlxdm);
            if (hbSqlxMap != null) {
                sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
                sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
            }
        }

        // 转移登记
        project.setSqlx(sqlx1);
        project.setDjlx(Constants.DJLX_ZYDJ_DM);
        List<InsertVo> zyList = creatProjectZydjService.initVoFromOldData(project);
        String bdcdyid = "";
        if (CollectionUtils.isNotEmpty(zyList)) {
            for (InsertVo insertVo : zyList) {
                if (insertVo instanceof BdcBdcdy) {
                    bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                }
            }
        }
        // 其他转移登记
        project.setProid(UUIDGenerator.generate());
        project.setBdcdyid(bdcdyid);
        project.setSqlx(sqlx2);
        project.setDjlx(Constants.DJLX_ZYDJ_DM);
        List<InsertVo> qtzyList = creatProjectZydjService.initVoFromOldData(project);
        // 转移bdcxmrel中的yproid为变更proid
        if (CollectionUtils.isNotEmpty(qtzyList)) {
            for (InsertVo vo : qtzyList) {
                if (vo instanceof BdcXmRel) {
                    ((BdcXmRel) vo).setYproid(zyProid);
                }
                // 其他转移创建时间慢1,用于缮证改变qlzt排序
                if (vo instanceof BdcXm) {
                    ((BdcXm) vo).setCjsj(DateUtils.addSeconds(((BdcXm) vo).getCjsj(),1));
                    ((BdcXm) vo).setYbdcqzh("");
                }
            }
        }
        insertVoList.addAll(zyList);
        insertVoList.addAll(qtzyList);

        //赋值登记事由和登记原因
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            for (InsertVo vo : insertVoList) {
                if (vo instanceof BdcXm) {
                    ((BdcXm) vo).setDjsy(CommonUtil.formatEmptyValue(bdcQllxMapper.queryDjsyByQllx(((BdcXm) vo).getQllx()).get(0)));
                    ((BdcXm) vo).setDjyy(bdcSqlxDjyyRelService.getDjyyBySqlx(((BdcXm) vo).getSqlx()));
                }
            }
        }
        return insertVoList;
    }
}
