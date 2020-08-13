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
import java.util.List;

/**
 * .
 * <p/>
 * zdd 与使用权（所有权）注销登记相比   他项权利的注销需要读取原义务人
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-29
 */
public class CreatProjectZxTxdjServiceImpl extends CreatProjectDefaultServiceImpl {

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
        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
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
        //存入不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        list.add(bdcdy);
        if (bdcdy != null)
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());

        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
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
