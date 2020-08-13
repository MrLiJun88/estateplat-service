package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 *
 * @version 1.0 16-10-27
 * @author: bianwen
 * @description 在建工程抵押变更
 */
public class CreateDyBgForZjgcServiceImpl extends CreatProjectDefaultServiceImpl {
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
    private BdcSjdService bdcSjdService;
    @Autowired
    private QllxService qllxService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx){
        String zjgcdyFw= "";
        Project project=null;
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
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if (bdcxm != null && StringUtils.isNotBlank(project.getYxmid()) && StringUtils.equals(bdcxm.getDjlx(), Constants.DJLX_DYDJ_DM)) {
            bdcXmService.getYBdcXmDjzx(project.getYxmid(), bdcxm);
        }
        list.add(bdcxm);

        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            list.addAll(bdcXmRelList);

        if(CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM,bdcxm.getSqlx())){
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
        //zdd 人方信息
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList)
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
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



    public List<InsertVo> copyBdcxxListFromBdcxm(BdcXm bdcXm,BdcXm ybdcXm) {
        List<InsertVo> bdcxxList = new ArrayList<InsertVo>();
        String proid = UUIDGenerator.generate();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            try{
                BdcXm newBdcxm=(BdcXm) BeanUtils.cloneBean(bdcXm);
                List<BdcSjxx>  bdcSjxxList = bdcSjdService.queryBdcSjdByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                    for (BdcSjxx bdcSjxx : bdcSjxxList) {
                        bdcSjxx.setProid(proid);
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcxxList.add(bdcSjxx);
                    }
                }

                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(ybdcXm.getProid());
                if (bdcSpxx != null) {
                    bdcSpxx.setProid(proid);
                    bdcSpxx.setSpxxid(UUIDGenerator.generate());
                    bdcxxList.add(bdcSpxx);
                }
                List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                    for (BdcQlr bdcQlr : bdcYwrList) {
                        bdcQlr.setProid(proid);
                        bdcQlr.setQlrid(UUIDGenerator.generate());
                        bdcxxList.add(bdcQlr);
                    }
                }
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for (BdcQlr bdcQlr : bdcQlrList) {
                        bdcQlr.setProid(proid);
                        bdcQlr.setQlrid(UUIDGenerator.generate());
                        bdcxxList.add(bdcQlr);
                    }
                }
                List<BdcXmRel> xmRelList=bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if(CollectionUtils.isNotEmpty(xmRelList)){
                    for(BdcXmRel xmrel:xmRelList){
                        xmrel.setProid(proid);
                        xmrel.setYproid(ybdcXm.getProid());
                        xmrel.setRelid(UUIDGenerator.generate());
                        bdcxxList.add(xmrel);
                    }
                }
                newBdcxm.setProid(proid);
                newBdcxm.setBdcdyid(ybdcXm.getBdcdyid());
                bdcxxList.add(newBdcxm);

            }
            catch (Exception e){
                logger.error("CreateDyBgForZjgcServiceImpl.copyBdcxxListFromBdcxm",e);
            }
        }
        return bdcxxList;
    }
}
