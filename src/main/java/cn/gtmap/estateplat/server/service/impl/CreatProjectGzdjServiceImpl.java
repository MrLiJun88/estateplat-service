package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 * 更正登记服务  创建项目时与变更登记逻辑一致
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-4-8
 */
public class CreatProjectGzdjServiceImpl extends CreatProjectBgdjServiceImpl {
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
    private BdcdyService bdcdyService;
    @Autowired
    private EntityMapper entityMapper;
    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        List<BdcQlr> ybdcYwrList;
        String pplx = AppConfig.getProperty("sjpp.type");
        List<InsertVo> list = new ArrayList<InsertVo>();
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else {
            throw new AppException(4005);
        }
        String proid = project.getProid();
        if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
            for (int i = 0; i < project.getBdcXmRelList().size(); i++) {
                List<BdcXm> bdcXmList = null;
                if (StringUtils.isNotBlank(project.getBdcXmRelList().get(i).getYproid())) {
                    bdcXmList = bdcXmService.getBdcXmListByProid(project.getBdcXmRelList().get(i).getYproid());
                }
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXmList.get(0).getBdcdyid());
                    project.setBdcdyh(bdcBdcdy.getBdcdyh());
                    project.setBdcdyid(bdcXmList.get(0).getBdcdyid());
                    project.setYxmid(project.getBdcXmRelList().get(i).getYproid());
                    project.setProid(proid);
                }
                ybdcQlrList = getYbdcQlrList(project);
                ybdcYwrList = getYbdcYwrList(project);
                //zdd 项目信息
                BdcXm bdcxm = bdcXmService.newBdcxm(project);
                //zdd 更正登记读取原项目的权利类型
                if (StringUtils.isNotBlank(project.getYxmid()) && !StringUtils.equals(project.getSqlx(), Constants.SQLX_YZX)) {
                    BdcXm ybdcxm = bdcXmService.getBdcXmByProid(project.getYxmid());
                    bdcxm = readYbdcxm(bdcxm, ybdcxm);
                    if (ybdcxm != null && StringUtils.isNotBlank(ybdcxm.getQllx()))
                        project.setQllx(ybdcxm.getQllx());
                    //lx 抵押权时dydjlx赋为更正登记
                    if (StringUtils.equals(bdcxm.getDjlx(), Constants.DJLX_GZDJ_DM) && (StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_DYAQ)
                            || StringUtils.equals(ybdcxm.getSqlx(), Constants.SQLX_YG_DY) || StringUtils.equals(ybdcxm.getSqlx(), Constants.SQLX_YG_BDCDY) || StringUtils.equals(ybdcxm.getSqlx(), Constants.SQLX_YG_DY_XS))) {
                        bdcxm.setDydjlx(Constants.DJLX_GZDJ_DM);
                    }
                }
                list.add(bdcxm);
                //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
                if (CollectionUtils.isNotEmpty(bdcXmRelList))
                    list.addAll(bdcXmRelList);
                //zdd 获取地籍数据
                InitVoFromParm initVoFromParm = super.getDjxx(xmxx);
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
                        bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                        list.add(bdcTd);
                    }
                }

                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
                bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
                if (StringUtils.isNotBlank(project.getYqlid()) && StringUtils.equals(project.getBdclx(), "TD")) {
                    GdTdsyq gdTdsyqList = gdTdService.getGdTdsyqByQlid(project.getYqlid());
                    if (gdTdsyqList != null && gdTdsyqList.getTdzmj() != null) {
                        bdcSpxx.setZdzhmj(gdTdsyqList.getTdzmj());
                    }
                }
                if (bdcSpxx != null) {
                    list.add(bdcSpxx);
                }
                //zdd 不动产单元
                BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
                if (bdcdy != null) {
                    bdcxm.setBdcdyid(bdcdy.getBdcdyid());
                    list.add(bdcdy);
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
                // jyl 房产已过户，土地没有过户时，房产的义务人要取过来
                String fwghTdwgh = AppConfig.getProperty("fwghTdwgh");
                //zhouwanqing 当生成证书是证明时需要继承原义务人
                if (StringUtils.equals(fwghTdwgh, "true") || StringUtils.equals(pplx, Constants.PPLX_GC) || StringUtils.equals(project.getQllx(), Constants.QLLX_DYAQ) || StringUtils.equals(project.getQllx(), Constants.QLLX_YGDJ)
                        || StringUtils.equals(project.getQllx(), Constants.QLLX_DYQ) || StringUtils.equals(project.getQllx(), Constants.QLLX_YYDJ) && StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                    List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                    if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                        for (BdcQlr bdcYwr : tempBdcYwrList) {
                            bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
                        for (BdcQlr bdcYwr : ybdcYwrList) {
                            bdcYwr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcYwr, tempBdcYwrList, project.getProid());
                            list.add(bdcYwr);
                        }
                    }
                }
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        } else {
            ybdcQlrList = getYbdcQlrList(project);
            ybdcYwrList = getYbdcYwrList(project);
            //zdd 项目信息
            BdcXm bdcxm = bdcXmService.newBdcxm(project);
            //zdd 更正登记读取原项目的权利类型
            if (StringUtils.isNotBlank(project.getYxmid()) && !StringUtils.equals(project.getSqlx(), Constants.SQLX_YZX)) {
                BdcXm ybdcxm = bdcXmService.getBdcXmByProid(project.getYxmid());
                bdcxm = readYbdcxm(bdcxm, ybdcxm);
                if (ybdcxm != null && StringUtils.isNotBlank(ybdcxm.getQllx()))
                    project.setQllx(ybdcxm.getQllx());
                //lx 抵押权时dydjlx赋为更正登记
                if (StringUtils.equals(bdcxm.getDjlx(), Constants.DJLX_GZDJ_DM) && (StringUtils.equals(ybdcxm.getQllx(), Constants.QLLX_DYAQ)
                        || StringUtils.equals(ybdcxm.getSqlx(), Constants.SQLX_YG_DY) || StringUtils.equals(ybdcxm.getSqlx(), Constants.SQLX_YG_DY_XS) || StringUtils.equals(ybdcxm.getSqlx(), Constants.SQLX_YG_BDCDY))) {
                    bdcxm.setDydjlx(Constants.DJLX_GZDJ_DM);
                }
            }
            list.add(bdcxm);
            //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                list.addAll(bdcXmRelList);
            }
            //zdd 获取地籍数据
            InitVoFromParm initVoFromParm = super.getDjxx(xmxx);
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
                    bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                    list.add(bdcTd);
                }
            }

            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
            bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
            if (StringUtils.isNotBlank(project.getYqlid()) && StringUtils.equals(project.getBdclx(), "TD")) {
                GdTdsyq gdTdsyqList = gdTdService.getGdTdsyqByQlid(project.getYqlid());
                if (gdTdsyqList != null && gdTdsyqList.getTdzmj() != null) {
                    bdcSpxx.setZdzhmj(gdTdsyqList.getTdzmj());
                }
            }
            if (bdcSpxx != null) {
                list.add(bdcSpxx);
            }
            //zdd 不动产单元
            BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
            if (bdcdy != null) {
                bdcxm.setBdcdyid(bdcdy.getBdcdyid());
                list.add(bdcdy);
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
            // jyl 房产已过户，土地没有过户时，房产的义务人要取过来
            String fwghTdwgh = AppConfig.getProperty("fwghTdwgh");
            //zhouwanqing 当生成证书是证明时需要继承原义务人
            if (StringUtils.equals(fwghTdwgh, "true") || StringUtils.equals(pplx, Constants.PPLX_GC) || StringUtils.equals(project.getQllx(), Constants.QLLX_DYAQ) || StringUtils.equals(project.getQllx(), Constants.QLLX_YGDJ)
                    || StringUtils.equals(project.getQllx(), Constants.QLLX_DYQ) || StringUtils.equals(project.getQllx(), Constants.QLLX_YYDJ) && StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
                    for (BdcQlr bdcYwr : ybdcYwrList) {
                        bdcYwr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcYwr, tempBdcYwrList, project.getProid());
                        list.add(bdcYwr);
                    }
                }
            }
        }
        return list;
    }
}
