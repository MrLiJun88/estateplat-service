package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
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
 * 司法裁定转移
 *
 * hqz
 */

public class CreatProjectSfZydjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    QllxService qllxService;
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

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }else
            throw new AppException(4005);
        List<InsertVo> list = new ArrayList<InsertVo>();
        String pplx = AppConfig.getProperty("sjpp.type");
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        list.add(bdcxm);

        //zdd 需要完善   一个项目有可能有多个项目关系表
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            list.addAll(bdcXmRelList);
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
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null) {
            list.add(bdcSpxx);
        }
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
        }
        QllxVo qllxVo = qllxService.makeSureQllx(bdcxm);
        String zsFont = qllxService.makeSureBdcqzlx(qllxVo);

        //zhouwanqing 过程匹配，则权利人和义务人都不需要改变
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
            List<BdcQlr> bdcQlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                list.addAll(bdcQlrList);
            }
        } else {
            //hqz 司法转移权利人读取司法裁定权利人
            if ((StringUtils.equals(Constants.SQLX_ZY_SFCD, project.getSqlx()) || StringUtils.equals(Constants.SQLX_YFZYDYDJ_DM, project.getSqlx())) && StringUtils.isNotBlank(project.getBdcdyh())) {
                List<BdcQlr> bdcQlrList = bdcQlrService.getsfcdQlrByBdcdyh(project.getBdcdyh());
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for(BdcQlr bdcQlr : bdcQlrList){
                        if(StringUtils.equals(bdcQlr.getQlrlx(),Constants.QLRLX_YWR)){
                            bdcQlr.setQlrid(UUIDGenerator.generate());
                            bdcQlr.setProid(project.getProid());
                            bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                            list.add(bdcQlr);
                        }
                    }
                }
            }

            //zdd 如果为证书  则将原权利人转为义务人  如果证明 则将义务人转为义务人
            if (StringUtils.isNotBlank(zsFont) && zsFont.equals(Constants.BDCQZS_BH_FONT)) {
                ybdcQlrList = getYbdcQlrList(project);
            } else {
                ybdcQlrList = getYbdcYwrList(project);
            }
            //zdd 权利人信息
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                for (BdcQlr bdcQlr : ybdcQlrList) {
                    bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                    list.add(bdcQlr);
                }
            }
        }

        List<BdcQlr> bdcQlrList = bdcQlrService.initBdcQlrFromOntQlr(project.getProid());
        if (!bdcQlrList.isEmpty()) {
            Iterator<InsertVo> iterator = list.iterator();
            while (iterator.hasNext()) {
                InsertVo insertVo = iterator.next();
                if (insertVo instanceof BdcQlr) {
                    iterator.remove();
                }
            }
            for (BdcQlr bdcQlr : bdcQlrList) {
                list.add(bdcQlr);
            }
        }
        return list;
    }
}
