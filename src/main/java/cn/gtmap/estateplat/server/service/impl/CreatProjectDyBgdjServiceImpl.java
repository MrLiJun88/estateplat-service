package cn.gtmap.estateplat.server.service.impl;


import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 抵押权（地役权）变更登记创建项目服务  同普通变更登记最大的区别在于人方信息全部读取过来
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-31
 */
public class CreatProjectDyBgdjServiceImpl extends CreatProjectDefaultServiceImpl {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private GdYgService gdYgService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcZsService bdcZsService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        List<BdcQlr> ybdcYwrList;
        List<BdcQlr> ybdcjKrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
            ybdcYwrList = getYbdcYwrList(project);
            ybdcjKrList = getYbdcJkrList(project);
        }else
            throw new AppException(4005);
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if (bdcxm != null && StringUtils.isNotBlank(project.getYxmid()) && StringUtils.equals(bdcxm.getDjlx(),Constants.DJLX_DYDJ_DM)) {
            bdcXmService.getYBdcXmDjzx(project.getYxmid(), bdcxm);
        }
        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);

        //zdd 获取地籍数据

        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }

            // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
            BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
            if (bdcTd == null) {
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(),initVoFromParm.getProject(), bdcxm.getQllx());
                list.add(bdcTd);
            }
        }

        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null)
            list.add(bdcSpxx);
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
        }
        //zdd 人方信息
        String sqlxdm = "";
        if (StringUtils.isNotBlank(bdcxm.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcxm.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(project.getBdcdyh(), "1");
        List<GdYg> gdYgList = gdYgService.getGdygListByBdcdyh(project.getBdcdyh());
        List<BdcQlr> ygbdcQlrList = new ArrayList<BdcQlr>();
        List<BdcQlr> ygbdcYwrList = new ArrayList<BdcQlr>();
        List<GdQlr> gdQlrList = new ArrayList<GdQlr>();
        if(StringUtils.equals(sqlxdm, Constants.SQLX_CLF)){
            StringBuilder ybdcqzh = new StringBuilder();
            if(CollectionUtils.isNotEmpty(bdcYgList)){
                String proid = "";
                for (BdcYg bdcYg : bdcYgList) {
                    if (StringUtils.isNotBlank(bdcYg.getYgdjzl()) &&StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPFDY)) {
                        //增加ybdcqzh
                        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcYg.getProid());
                        if(CollectionUtils.isNotEmpty(bdcZsList)){
                            for(BdcZs bdcZs:bdcZsList){
                                if(StringUtils.isNotBlank(bdcZs.getBdcdyh())){
                                    if (StringUtils.isBlank(ybdcqzh)) {
                                        ybdcqzh.append(bdcZs.getBdcqzh());
                                    } else {
                                        ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                    }
                                }
                            }
                        }

                        //增加bdc_xm_rel
                        BdcXmRel bdcXmRel = new BdcXmRel();
                        bdcXmRel.setRelid(UUIDGenerator.generate18());
                        bdcXmRel.setProid(project.getProid());
                        bdcXmRel.setYqlid(bdcYg.getQlid());
                        bdcXmRel.setYproid(bdcYg.getProid());
                        bdcXmRel.setQjid(project.getDjId());
                        bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                        list.add(bdcXmRel);

                        proid = bdcYg.getProid();
                        if (StringUtils.isNotBlank(proid)) {
                            ygbdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                            ygbdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
                        }
                        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
                        List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                        if(CollectionUtils.isNotEmpty(ygbdcQlrList)){
                            for (BdcQlr bdcQlr : ygbdcQlrList) {
                                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, bdcQlrList, project.getProid());
                                list.add(bdcQlr);
                            }
                            for (BdcQlr bdcQlr : ygbdcYwrList) {
                                bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, bdcYwrList, project.getProid());
                                list.add(bdcQlr);
                            }
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(gdYgList)){
                for(GdYg gdYg : gdYgList){
                    if(StringUtils.isNotBlank(gdYg.getYgdjzl()) && StringUtils.equals(gdYg.getYgdjzl(),Constants.YGDJZL_YGSPFDYAQ_MC)){
                        if (StringUtils.isNotBlank(gdYg.getYgdjzmh())) {
                            if (StringUtils.isBlank(ybdcqzh)) {
                                ybdcqzh.append(gdYg.getYgdjzmh());
                            } else {
                                ybdcqzh.append(",").append(gdYg.getYgdjzmh());
                            }
                        }

                        //增加bdc_xm_rel
                        BdcXmRel bdcXmRel = new BdcXmRel();
                        bdcXmRel.setRelid(UUIDGenerator.generate18());
                        bdcXmRel.setProid(project.getProid());
                        bdcXmRel.setYqlid(gdYg.getYgid());
                        bdcXmRel.setYproid(gdYg.getProid());
                        bdcXmRel.setQjid(project.getDjId());
                        bdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                        list.add(bdcXmRel);

                        //继承预告权利人
                        gdQlrList = gdQlrService.queryGdQlrs(gdYg.getYgid(), Constants.QLRLX_QLR);
                        if(CollectionUtils.isNotEmpty(gdQlrList)){
                            List<BdcQlr> bdcQlrList = gdQlrService.readGdQlrs(gdQlrList);
                            if(CollectionUtils.isNotEmpty(bdcQlrList)){
                                for(BdcQlr bdcQlr : bdcQlrList){
                                    bdcQlr.setProid(project.getProid());
                                    list.add(bdcQlr);
                                }
                            }
                        }
                    }
                }
            }
            for (InsertVo insertVo : list) {
                if (insertVo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) insertVo;
                    if(StringUtils.isBlank(bdcXm.getYbdcqzh())){
                        bdcXm.setYbdcqzh(ybdcqzh.toString());
                    }else{
                        bdcXm.setYbdcqzh(bdcXm.getYbdcqzh() + "," + ybdcqzh);
                    }
                }
            }
        }
        if(CollectionUtils.isEmpty(ygbdcQlrList) && CollectionUtils.isEmpty(ygbdcYwrList) && CollectionUtils.isEmpty(gdQlrList)){
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                    for (BdcQlr bdcQlr : tempBdcQlrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                    }
                }
                for (BdcQlr bdcQlr : ybdcQlrList) {
                    bdcQlr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcQlr, tempBdcQlrList, project.getProid());
                    list.add(bdcQlr);
                }
            }
            if (CollectionUtils.isNotEmpty(ybdcYwrList)&& !StringUtils.equals(sqlxdm,Constants.SQLX_DYBG_DM) && !StringUtils.equals(sqlxdm,Constants.SQLX_DYHZ_DM)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                for (BdcQlr bdcYwr : ybdcYwrList) {
                    bdcYwr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcYwr, tempBdcYwrList, project.getProid());
                    list.add(bdcYwr);
                }
            }
            if (CollectionUtils.isNotEmpty(ybdcjKrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_JKR);
                    }
                }
                for (BdcQlr bdcYwr : ybdcjKrList) {
                    bdcYwr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcYwr, tempBdcYwrList, project.getProid());
                    list.add(bdcYwr);
                }
            }
        }
        return list;
    }
}
