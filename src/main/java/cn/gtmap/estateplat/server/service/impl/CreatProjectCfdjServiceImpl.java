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
 * 查封登记创建服务（不生成权利人）
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class CreatProjectCfdjServiceImpl extends CreatProjectDefaultServiceImpl {
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
        } else
            throw new AppException(4005);

        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if (StringUtils.isNotBlank(project.getYxmid())) {
            BdcXm ybdcxm = bdcXmService.getBdcXmByProid(project.getYxmid());
            bdcxm = readYbdcxm(bdcxm, ybdcxm);
        }
        bdcxm.setXmzt(Constants.XMZT_SZ);
        insertVoList.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            insertVoList.addAll(bdcXmRelList);
        }

        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                insertVoList.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }
            // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
            BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
            if (bdcTd == null) {
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                insertVoList.add(bdcTd);
            }
        }
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null) {
            insertVoList.add(bdcSpxx);
        }
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            insertVoList.add(bdcdy);
        }

        //zdd 不动产单元
        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcQlrList = null;
        if (StringUtils.equals(project.getXmly(), Constants.XMLY_BDC) || StringUtils.isBlank(project.getXmly())) {
            bdcQlrList = initBdcQlrFromDjsj(initVoFromParm.getProject(), initVoFromParm.getDjsjFwQlrList(),initVoFromParm.getDjsjLqxx(),initVoFromParm.getDjsjZdxxList(),initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getCbzdDcb(), Constants.QLRLX_QLR);
        }
        //zdd 权利人信息
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            List<BdcQlr> tempBdcQlrs = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                tempBdcQlrs.add(bdcQlr);
            }
            bdcQlrList = tempBdcQlrs;
        }
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
        }
        /**
         * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
         * @description 配置项  如果配置查封的权利人为执行人，走下面方法,取原权利人为义务人；否则不走
         */
        String cfqlr = AppConfig.getProperty("cfdj.qlr");
        if (StringUtils.isNotBlank(cfqlr) && StringUtils.equals(cfqlr, Constants.CFQLR_COURT)) {
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                List<BdcQlr> tempBdcYwrs = new ArrayList<BdcQlr>();
                for (BdcQlr bdcYwr : ybdcQlrList) {
                    bdcYwr = bdcQlrService.qlrTurnProjectYwr(bdcYwr, tempBdcYwrList, project.getProid());
                    tempBdcYwrs.add(bdcYwr);
                }
                bdcQlrList = tempBdcYwrs;
            }
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            insertVoList.addAll(bdcQlrList);
        }
        return insertVoList;
    }


}
