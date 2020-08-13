package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @version 1.0, 2016/5/29
 * @author<a href="mailto:zhoudefu@gtmap.cn>zhoudefu</a>
 * @discription 换证抵押流程
 */

public class CreatComplexHzDyProjectServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Resource(name = "creatProjectGzdjServiceImpl")
    private CreatProjectGzdjServiceImpl creatProjectGzdjService;
    @Resource(name = "creatProjectDydjServiceImpl")
    private CreatProjectDydjServiceImpl creatProjectDydjService;


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else
            throw new AppException(4005);
        String workflowProid = "";
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }
        creatProjectNode(workflowProid);
        /**
         * 根据工作流wiid，查找是否存在记录，若存在则删除对应记录
         */
        if (StringUtils.isNotBlank(project.getWiid())) {
            /**
             * 调用收件单服务根据wiid删除收件单信息
             */
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }
        String sqlx1 = null;
        String sqlx2 = null;
        if (StringUtils.isNotBlank(project.getSqlx())) {
            String sqlxdm = "";
            /**
             * 获取平台的申请类型代码,主要为了合并
             */
            if (StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            /**
             * 获取hb-sqlx.xml中的申请类型配置信息
             */
            HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(sqlxdm);
            if (hbSqlxMap != null) {
                sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
                sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
            }
        }

        /**
         * 创建xm,xmrel,bdcdy,spxx,djb,qlr信息
         */

        /**
         * 目前换证流程只考虑从过渡选择数据
         */
        if (!(project.getXmly().equals(Constants.XMLY_BDC))) {
            String syqQlid = "";
            if (StringUtils.isNotBlank(project.getGdproid())) {
                /**
                 * 获取产权证权利信息
                 */
                if (project.getBdclx().equals(Constants.BDCLX_TDFW)) {
                    List<GdFwsyq> fwsyqList = gdFwService.getGdFwsyqListByGdproid(project.getGdproid(), 0);
                    if (CollectionUtils.isNotEmpty(fwsyqList)) {
                        for (GdFwsyq fdfwsyq : fwsyqList) {
                            if (StringUtils.isBlank(syqQlid)) {
                                syqQlid = fdfwsyq.getQlid();
                            } else {
                                syqQlid += "," + fdfwsyq.getQlid();
                            }
                        }
                    }
                }
                project.setYqlid(syqQlid);
                project.setDjlx(Constants.DJLX_QTDJ_DM);
                List<InsertVo> syqVoList = initSyqFromGd(project, sqlx1);
                //将对象保存
                super.insertProjectData(syqVoList);

                /**
                 * 创建抵押流程
                 */
                String bdcdyid = "";
                if (CollectionUtils.isNotEmpty(syqVoList)) {
                    for (InsertVo insertVo : syqVoList) {
                        if (insertVo instanceof BdcBdcdy)
                            bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                    }
                }
                List<InsertVo> dyListnew = null;
                project.setProid(UUIDGenerator.generate());
                project.setBdcdyid(bdcdyid);
                dyListnew = initDyFromBdc(project, sqlx2);
                if (CollectionUtils.isNotEmpty(dyListnew)) {
                    insertVoList.addAll(dyListnew);
                }
            }
        } else {
            String syqQlid = "";
            if (StringUtils.isNotBlank(project.getYxmid())) {
                if (project.getBdclx().equals(Constants.BDCLX_TDFW)) {
                    List<BdcFdcq> fdcqList = bdcFdcqService.getBdcFdcqByProid(project.getYxmid());
                    if (CollectionUtils.isNotEmpty(fdcqList)) {
                        syqQlid = fdcqList.get(0).getQlid();
                    }
                }
                project.setYqlid(syqQlid);
                project.setDjlx(Constants.DJLX_QTDJ_DM);
                List<InsertVo> fdcqVoList = initSyqFromBdc(project, sqlx1);
                super.insertProjectData(fdcqVoList);
                /**
                 * 创建抵押流程
                 */
                String bdcdyid = "";
                if (CollectionUtils.isNotEmpty(fdcqVoList)) {
                    for (InsertVo insertVo : fdcqVoList) {
                        if (insertVo instanceof BdcBdcdy)
                            bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                    }
                }

                List<InsertVo> dyListnew = null;
                project.setProid(UUIDGenerator.generate());
                project.setBdcdyid(bdcdyid);
                dyListnew = initDyFromBdc(project, sqlx2);
                if (CollectionUtils.isNotEmpty(dyListnew)) {
                    insertVoList.addAll(dyListnew);
                }
            }

        }
        return insertVoList;
    }

    /**
     * 走过渡创建的换证登记
     *
     * @param project
     * @param sqlx1
     * @return
     */
    public List<InsertVo> initSyqFromGd(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            /**
             * 调用换证登记服务，与更正登记逻辑一致
             */
            List<InsertVo> list = creatProjectGzdjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;
                    /**
                     *创建收件信息
                     */
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    /**
                     * 修改申请类型
                     */
                    if (StringUtils.isNotBlank(sqlx1)) {
                        bdcXm.setSqlx(sqlx1);
                    } else {
                        bdcXm.setSqlx(Constants.SQLX_HZ_DM);
                    }
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

    /**
     * 不动产创建的换证登记
     *
     * @param project
     * @param sqlx1
     * @return
     */
    public List<InsertVo> initSyqFromBdc(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            /**
             * 调用换证登记服务，与更正登记逻辑一致
             */
            String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(qllxdm);
            if (StringUtils.isNotBlank(project.getYxmid())) {
                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(project.getYxmid());
                if (ybdcXm != null) {
                    project.setDjsy(ybdcXm.getDjsy());
                }
            } else if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(project.getBdcXmRelList().get(0).getYproid());
                if (ybdcXm != null) {
                    project.setDjsy(ybdcXm.getDjsy());
                }
            }
            List<InsertVo> list = creatProjectGzdjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;
                    /**
                     *创建收件信息
                     */
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    /**
                     * 修改申请类型
                     */
                    if (StringUtils.isNotBlank(sqlx1)) {
                        bdcXm.setSqlx(sqlx1);
                    } else {
                        bdcXm.setSqlx(Constants.SQLX_HZ_DM);
                    }
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

    /**
     * 创建抵押流程
     *
     * @param project
     * @return
     */
    public List<InsertVo> initDyFromBdc(Project project, String sqlx2) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            if (StringUtils.isNotBlank(sqlx2)) {
                project.setSqlx(sqlx2);
            } else {
                project.setSqlx(Constants.SQLX_FWDY_DM);
            }
            project.setQllx(Constants.QLLX_DYAQ);
            project.setDjlx(Constants.DJLX_DYDJ_DM);
            project.setDjsy(Constants.DJSY_DYAQ);
            List<InsertVo> list = creatProjectDydjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;
                    /**
                     * 创建收件信息
                     */
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }
}
