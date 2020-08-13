package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * .
 * <p/>
 * 预告登记创建服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class CreatProjectYgdjServiceImpl extends CreatProjectDefaultServiceImpl {

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
    QllxService qllxService;

    @Override
    public synchronized List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        List<BdcQlr> ybdcYwrList;

        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
            ybdcYwrList = getYbdcYwrList(project);
        } else
            throw new AppException(4005);
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
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
        String pplx = AppConfig.getProperty("sjpp.type");
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC) && StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
            List<BdcQlr> bdcQlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(bdcQlrList))
                list.addAll(bdcQlrList);
        } else {

            //结果匹配和新建权利人获取
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                if (CommonUtil.indexOfStrs(Constants.SQLX_YGDJ_DM, project.getSqlx())) {

                    for (BdcQlr bdcQlr : ybdcQlrList) {
                        bdcQlr.setProid(project.getProid());
                        list.add(bdcQlr);
                    }
                    if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
                        for (BdcQlr bdcYwr : ybdcYwrList) {
                            bdcYwr.setProid(project.getProid());
                            list.add(bdcYwr);
                        }
                    }


                } else {
                    for (BdcQlr bdcQlr : ybdcQlrList) {
                        bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                        list.add(bdcQlr);
                    }
                }


            }
            //考虑多个权利人情况
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                bdcQlrService.delBdcQlrByProid(project.getProid());
            }

            //zdd 地籍数据权利人初始化
            List<BdcQlr> bdcQlrList = initBdcQlrFromDjsj(initVoFromParm.getProject(),initVoFromParm.getDjsjFwQlrList(),initVoFromParm.getDjsjLqxx(),initVoFromParm.getDjsjZdxxList(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getCbzdDcb(), Constants.QLRLX_QLR);
            if (CollectionUtils.isNotEmpty(bdcQlrList)&& !bdcxm.getSqlx().equals("707")) {
                List<BdcQlr> tempBdcQlrs = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcQlrs)) {
                    for (BdcQlr qlr : tempBdcQlrs) {
                        bdcQlrService.delBdcQlrByQlrid(qlr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                for (BdcQlr qlr : bdcQlrList) {
                    qlr.setQlrlx(Constants.QLRLX_YWR);
                    list.add(qlr);
                }
            }
        }

        List<BdcQlr> bdcQlrList = bdcQlrService.initBdcQlrFromOntQlr(project.getProid());
        if (!bdcQlrList.isEmpty()) {
            Iterator<InsertVo> iterator = list.iterator();
            while (iterator.hasNext()) {
                InsertVo insertVo = iterator.next();
                if (insertVo instanceof BdcQlr)
                    iterator.remove();
            }
            for (BdcQlr bdcQlr : bdcQlrList) {
                list.add(bdcQlr);
            }
        }
        return list;
    }

}
