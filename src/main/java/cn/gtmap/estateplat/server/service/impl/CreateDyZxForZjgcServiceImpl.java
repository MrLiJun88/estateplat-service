package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @version 1.0 16-10-12
 * @author: bianwen
 * @description  在建工程抵押注销，通过配置项zjgcdy.fw区分以土地和以房屋为主
 */
public class CreateDyZxForZjgcServiceImpl extends CreateDyBgForZjgcServiceImpl {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcdjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Autowired
    ProjectService projectService;
    @Autowired
    BdcSjdService bdcSjdService;




    public List initVo(Xmxx xmxx,String zjgcdyFw){
        List<BdcQlr> ybdcQlrList=null;
        List<BdcQlr> ybdcYwrList=null;
        List<InsertVo> list=new ArrayList<InsertVo>();
        Project project=null;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
            ybdcYwrList = getYbdcYwrList(project);
        }else {
            throw new AppException(4005);
        }
        InitVoFromParm initVoFromParm=null;
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if(StringUtils.equals(zjgcdyFw,"true")){
            initVoFromParm=super.getDjxxByDjsjFwychs(xmxx);
        }
        else {
            initVoFromParm=super.getDjxx(xmxx);
        }
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            List<BdcBdcdjb> bdcdjbTemps = bdcdjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);

                BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
                if (bdcTd == null) {
                    bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(),initVoFromParm.getProject(), bdcxm.getQllx());
                    list.add(bdcTd);
                }
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }
        }
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm,bdcSpxx);
        if (bdcSpxx != null) {
            list.add(bdcSpxx);
        }
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        list.add(bdcdy);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
        }
        list.add(bdcxm);

        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);

        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }

        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcQlr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            for (BdcQlr bdcQlr : ybdcYwrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        return list;
    }
}
