package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 *
 * @version 1.0 16-10-31
 * @author: bianwen
 * @description 在建工程抵押转移
 */
public class CreatDyZyForZjgcServiceImpl extends CreateDyBgForZjgcServiceImpl {
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
    private QllxService qllxService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx){
        String zjgcdyFw="";
        List<InsertVo> list = new ArrayList<InsertVo>();
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }else
            throw new AppException(4005);

        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if (bdcxm != null && StringUtils.isNotBlank(project.getYxmid()) && StringUtils.equals(bdcxm.getDjlx(), Constants.DJLX_DYDJ_DM)) {
            bdcXmService.getYBdcXmDjzx(project.getYxmid(), bdcxm);
        }
        list.add(bdcxm);

        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);

        if(StringUtils.equals(bdcxm.getSqlx(),Constants.SQLX_ZJJZW_ZY_FW_DM)){
            zjgcdyFw="true";
        }
        InitVoFromParm initVoFromParm=null;
        if(StringUtils.equals(zjgcdyFw,"true")){
            initVoFromParm=super.getDjxxByDjsjFwychs(xmxx);
        }
        else {
            initVoFromParm=super.getDjxx(xmxx);
        }
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }

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

        List<BdcQlr> ybdcYwrList = getYbdcYwrList(project);
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            for (BdcQlr bdcYwr : ybdcYwrList) {
                bdcYwr = bdcQlrService.qlrTurnProjectYwr(bdcYwr, tempBdcYwrList, project.getProid());
                list.add(bdcYwr);
            }
        }
        if(StringUtils.equals(zjgcdyFw,"true") && StringUtils.isNotBlank(project.getYxmid())){
            saveOrUpdateProjectData(list);
            list.clear();

            String proids=bdcXmService.getProidsByProid(project.getYxmid());
            HashMap param=new HashMap();
            param.put("proids",StringUtils.split(proids, Constants.SPLIT_STR));
            List<BdcXm> ybdcxmList=bdcXmService.getBdcXmList(param);
            if(CollectionUtils.isNotEmpty(ybdcxmList)){
                for(BdcXm ybdcXm:ybdcxmList){
                    QllxVo qllxVo=qllxService.queryQllxVo(ybdcXm);
                    if(qllxVo!=null && StringUtils.equals(qllxVo.getQszt().toString(),"1")&& !StringUtils.equals(ybdcXm.getBdcdyid(),bdcxm.getBdcdyid())){
                        list.addAll(copyBdcxxListFromBdcxm(bdcxm,ybdcXm));
                    }
                }
            }
        }
        return list;
    }

}
