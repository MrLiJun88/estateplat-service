package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 异议登记   不记录原权利信息
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-2
 */
public class CreatProjectYydjServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcSpxxService bdcSpxxService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        }else
            throw new AppException(4005);
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        //异议登记继承原项目登记事由
        if (StringUtils.isNotEmpty(project.getYxmid())){
            BdcXm ybdcxm= bdcXmService.getBdcXmByProid(project.getYxmid());
            if (ybdcxm !=null){
                bdcxm.setDjsy(ybdcxm.getDjsy());
            }
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
        //zdd 不动产单元
        String pplx= AppConfig.getProperty("sjpp.type");
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)&&StringUtils.equals(project.getBdclx(),Constants.BDCLX_TDFW)) {
            List<BdcQlr> bdcQlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(bdcQlrList))
                list.addAll(bdcQlrList);
        } else {
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcQlr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_YWR);
                    }

                }
                List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                    for (BdcQlr bdcQlr : tempBdcQlrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                    }
                }
                for (BdcQlr bdcQlr : ybdcQlrList) {
                    bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                    list.add(bdcQlr);
                }
            }

            //zdd 抵押权 地役权变更登记  继承原义务人
            if (StringUtils.isNotBlank(bdcxm.getQllx())&&(bdcxm.getQllx().equals("17") || bdcxm.getQllx().equals("18")) || bdcxm.getQllx().equals("20")) {
                List<BdcQlr> ybdcYwrList = null;
                if (StringUtils.equals(project.getSqlx(), Constants.SQLX_ZXYY_DM)) {
                    String yYyProid = project.getYxmid();
                    Project project1 = project;
                    project1.setYxmid(yYyProid);
                    ybdcYwrList = getYbdcYwrList(project1);
                } else {
                    ybdcYwrList = getYbdcYwrList(project);
                }
                if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
                    List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                    if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                        for (BdcQlr bdcYwr : tempBdcYwrList) {
                            bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                        }
                    }
                    //当原项目是转移时，不继承义务人
                    String yproid = project.getYxmid();
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(yproid);
                    if (!bdcXm.getDjlx().equals(Constants.DJLX_ZYDJ_DM)) {
                        for (BdcQlr bdcYwr : ybdcYwrList) {
                            bdcYwr = bdcQlrService.qlrTurnProjectYwr(bdcYwr, tempBdcYwrList, project.getProid());
                            list.add(bdcYwr);
                        }
                    }

                }
            }
        }
        return list;
    }


}
