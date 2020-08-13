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
import java.util.Iterator;
import java.util.List;

/**
 * .
 * <p/>
 * 注销登记业务逻辑同变更登记相同
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class CreatProjectZxdjServiceImpl extends CreatProjectDefaultServiceImpl {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        List<BdcQlr> ybdcYwrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
            ybdcYwrList = getYbdcYwrList(project);
        }else {
            throw new AppException(4005);
        }

        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        //注销异议登记继承原项目登记事由
        if (StringUtils.isNotEmpty(project.getYxmid()) && StringUtils.equals(bdcxm.getSqlx(),Constants.SQLX_ZXYY_DM)){
            BdcXm ybdcxm= bdcXmService.getBdcXmByProid(project.getYxmid());
            if (ybdcxm !=null){
                bdcxm.setDjsy(ybdcxm.getDjsy());
            }
        }
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
        bdcSpxx = initBdcSpxx(initVoFromParm,bdcSpxx);
        if (bdcSpxx != null) {
            list.add(bdcSpxx);
        }
        //存入不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        list.add(bdcdy);
        if (bdcdy != null){
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
        }
        list.add(bdcxm);

        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            list.addAll(bdcXmRelList);
        }
        /**
          * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
          * @description  依职权或嘱托注销不要权利人信息
          */
        if(StringUtils.equals(Constants.SQLX_YZQHZT_ZX_DM,project.getSqlx())){
            return list;
        }
        //zdd 权利人信息
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

        //lx 抵押预告登记不动产义务人继承过渡数据权利人
        if (StringUtils.equals(project.getSqlx(), Constants.SQLX_YG_YGSPFDY)
                || StringUtils.equals(project.getSqlx(), Constants.SQLX_YG_BDCDY) || StringUtils.equals(project.getSqlx(), Constants.SQLX_YG_FWZYYG)
                    || StringUtils.equals(project.getSqlx(), Constants.SQLX_YG_GYJSYT) || StringUtils.equals(project.getSqlx(), Constants.SQLX_YG_YGSPF)
                &&CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                for (BdcQlr bdcYwr : bdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList){
                if(list.contains(bdcQlr)){
                    list.remove(bdcQlr);
                }
            }
            for (BdcQlr bdcYwr : ybdcQlrList) {
                bdcYwr = bdcQlrService.qlrTurnProjectYwr(bdcYwr, bdcQlrList, project.getProid());
                list.add(bdcYwr);
            }
        }

        //zwq 注销异议登记义务人继承异议登记义务人
        if (StringUtils.equals(project.getSqlx(), Constants.SQLX_ZXYY_DM)&&CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                for (BdcQlr bdcYwr : bdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }

            for (BdcQlr bdcYwr : ybdcYwrList) {
                bdcYwr = bdcQlrService.qlrTurnProjectYwr(bdcYwr, bdcYwrList, project.getProid());
                list.add(bdcYwr);
            }
        }

        List<BdcQlr> bdcQlrList = bdcQlrService.initBdcQlrFromOntQlr(project.getProid());
        if(!bdcQlrList.isEmpty()){
            Iterator<InsertVo> iterator = list.iterator();
            while(iterator.hasNext()){
                InsertVo insertVo = iterator.next();
                if(insertVo instanceof BdcQlr) {
                    iterator.remove();
                }
            }
            for(BdcQlr bdcQlr : bdcQlrList){
                list.add(bdcQlr);
            }
        }
        return list;
    }
}
