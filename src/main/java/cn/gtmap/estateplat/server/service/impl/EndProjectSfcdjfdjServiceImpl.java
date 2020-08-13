package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.vo.UserInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 司法裁定解封登记
 */
public class EndProjectSfcdjfdjServiceImpl extends EndProjectDefaultServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    GdCfService gdCfService;
    @Autowired
    BdcZsCdService bdcZsCdService;

    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);
    }

    @Override
    public void changeYqllxzt(final BdcXm bdcXm) {
        List<BdcXmRel> bdcXmRelList = null;
        if (bdcXm != null) {
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            List<BdcQlr> bdcQlrLst = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
            List<BdcQlr> bdcYwrLst = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
            StringBuilder qlrmc = new StringBuilder();
            StringBuilder ywrmc = new StringBuilder();
            if (CollectionUtils.isNotEmpty(bdcQlrLst)) {
                for (BdcQlr bdcQlr : bdcQlrLst) {
                    qlrmc.append(bdcQlr.getQlrmc()).append(" ");
                }
            }
            if (CollectionUtils.isNotEmpty(bdcYwrLst)) {
                for (BdcQlr bdcQlr : bdcYwrLst) {
                    ywrmc.append(bdcQlr.getQlrmc()).append(" ");
                }
            }

            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                if (Constants.SQLX_ZX_SFCD.equals(bdcXm.getSqlx())) {//司法裁定注销
                    completeSfcdzxBdcCf(bdcXmRelList);
                } else {
                    //bdcXmRelList的大小判断为1，这个逻辑只进一次，进两次get(0)拿到的可能不是选择的那条数据
                    if (bdcXmRelList.size() == 1) {
                        BdcXmRel bdcXmRel = bdcXmRelList.get(0);
                        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                            //匹配不动产单元情况下司法裁定对不动产查封解封
                            completeSfcdBdcJf(bdcXm, bdcXmRel, ywrmc);
                            //根据不动产单元号查询过渡查封
                            completeSfcdGdJf(bdcXm, bdcXmRel, ywrmc);
                            //更新产权附记
//                            updateCqFj(bdcXm.getBdcdyid(), ywrmc, bdcXm, bdcXmRel);
                        } else {
                            //处理未匹配不动产单元司法裁定解封
                            dealNoPpSfCd(bdcXm, ywrmc);
                        }
                    }
                }
            }
        }
    }

    private void dealNoPpSfCd(BdcXm bdcXm, StringBuilder ywrmc) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD_PL)) && StringUtils.isBlank(bdcXm.getBdcdyid())) {
            List<String> qlidList = bdcXmRelService.getAllGdCfQlidListByBdcXmid(bdcXm.getProid());
            List<String> syqQlidList = bdcXmRelService.getXsSyqQlidListByBdcXmid(bdcXm.getProid());
            List<BdcXmRel> bdcXmRelListTemp = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            String yqlid = "";
            String qjid = "";
            if (CollectionUtils.isNotEmpty(bdcXmRelListTemp)) {
                BdcXmRel bdcXmRelTemp = bdcXmRelListTemp.get(0);
                if (StringUtils.isNotBlank(bdcXmRelTemp.getYqlid())) {
                    yqlid = bdcXmRelTemp.getYqlid();
                }
                if (StringUtils.isNotBlank(bdcXmRelTemp.getQjid())) {
                    qjid = bdcXmRelTemp.getQjid();
                }
            }
            //对项目原权利进行单独处理
            if (StringUtils.isNotBlank(yqlid)) {
                GdCf gdCf = gdFwService.getGdCfByCfid(yqlid, 1);
                if (gdCf == null) {
                    gdCf = gdFwService.getGdCfByCfid(yqlid, 0);
                }
                if (gdCf != null && StringUtils.isNotBlank(gdCf.getCfid())) {
                    gdCf.setIssx(Constants.SXZT_ISSX_WSX);
                    gdCf.setIscd(Constants.SXZT_ISSX);
                    gdCf.setIsjf(0);
                    gdCf.setFj(dealSfcdFj(gdCf.getFj(), ywrmc));
                    entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                    gdFwService.changeGdqlztByQlid(gdCf.getCfid(), Constants.QLLX_QSZT_LS.toString());
                    bdcZsCdService.updateBdcZscdByBdcXm(bdcXm);
                }
            }
            if (CollectionUtils.isNotEmpty(qlidList)) {
                for (String qlid : qlidList) {
                    GdCf gdCf = gdFwService.getGdCfByCfid(qlid, 0);
                    if (gdCf != null && StringUtils.isNotBlank(gdCf.getCfid()) && !StringUtils.equals(gdCf.getCfid(), yqlid)) {
                        gdCf.setIsjf(1);
                        gdCf.setIssx(Constants.SXZT_ISSX);
                        gdCf.setJfywh(bdcXm.getBh());
/*                            gdCf.setJfdbsj(new Date(System.currentTimeMillis()));
                            UserInfo user = SessionUtil.getCurrentUser();
                            String username = "";
                            if (user != null) {
                                username = user.getUsername();
                            }
                            gdCf.setJfdbr(username);*/
                        entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                        gdFwService.changeGdqlztByQlid(gdCf.getCfid(), Constants.QLLX_QSZT_XS.toString());
                        //生成对应的xmrel关系，保证推到共享库中的权利状态与不动产中的权利状态保持一致
                        BdcXmRel newBdcXmRel = new BdcXmRel();
                        newBdcXmRel.setRelid(UUIDGenerator.generate18());
                        if (StringUtils.isNotBlank(bdcXm.getProid())) {
                            newBdcXmRel.setProid(bdcXm.getProid());
                        }
                        newBdcXmRel.setYqlid(gdCf.getCfid());
                        if (StringUtils.isNotBlank(qjid)) {
                            newBdcXmRel.setQjid(qjid);
                        }
                        if (StringUtils.isNotBlank(gdCf.getProid())) {
                            newBdcXmRel.setYproid(gdCf.getProid());
                        }
                        entityMapper.saveOrUpdate(newBdcXmRel, newBdcXmRel.getRelid());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(syqQlidList)) {
                for (String syqQlid : syqQlidList) {
                    GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(syqQlid);
                    GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(syqQlid);
                    if (gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getQlid())) {
                        gdFwsyq.setFj(dealSfcdFj(gdFwsyq.getFj(), ywrmc));
                        entityMapper.saveOrUpdate(gdFwsyq, gdFwsyq.getQlid());
                    }
                    if (gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getQlid())) {
                        gdTdsyq.setFj(dealSfcdFj(gdTdsyq.getFj(), ywrmc));
                        entityMapper.saveOrUpdate(gdTdsyq, gdTdsyq.getQlid());
                    }
                }
            }
        }
    }

    public void dealTdzCf(String fwQlid, String bh, String proid, BdcXmRel bdcXmRel) {
        List<GdQlDyhRel> gdQlDyhRelList = new ArrayList<GdQlDyhRel>();
        if (StringUtils.isNotBlank(fwQlid)) {
            gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel("", fwQlid, "");
        }
        if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            for (GdQlDyhRel gdQlDyhRel : gdQlDyhRelList) {
                List<GdCf> gdCfList = new ArrayList<GdCf>();
                if (StringUtils.isNotBlank(gdQlDyhRel.getTdqlid())) {
                    gdCfList = gdCfService.getGdCfListByQlid(gdQlDyhRel.getTdqlid(), 0);
                }
                if (CollectionUtils.isNotEmpty(gdCfList)) {
                    for (GdCf gdCf : gdCfList) {
                        gdCf.setIsjf(1);
                        gdCf.setJfywh(bh);
                        gdCf.setIscd("");
                        gdCf.setJfdbsj(new Date(System.currentTimeMillis()));
                        UserInfo user = SessionUtil.getCurrentUser();
                        String username = "";
                        if (user != null) {
                            username = user.getUsername();
                        }
                        gdCf.setJfdbr(username);
                        entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                        gdFwService.changeGdqlztByQlid(gdCf.getCfid(), Constants.QLLX_QSZT_XS.toString());
                        //生成对应的xmrel关系，保证推到共享库中的权利状态与不动产中的权利状态保持一致
                        BdcXmRel newBdcXmRel = new BdcXmRel();
                        newBdcXmRel.setRelid(UUIDGenerator.generate18());
                        if (StringUtils.isNotBlank(proid)) {
                            newBdcXmRel.setProid(proid);
                        }
                        newBdcXmRel.setYqlid(gdCf.getCfid());
                        if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                            newBdcXmRel.setQjid(bdcXmRel.getQjid());
                        }
                        if (StringUtils.isNotBlank(gdCf.getProid())) {
                            newBdcXmRel.setYproid(gdCf.getProid());
                        }
                        entityMapper.saveOrUpdate(newBdcXmRel, newBdcXmRel.getRelid());
                    }
                }
            }
        }
    }

    public String dealSfcdFj(String fjStr, StringBuilder ywrmc) {
        StringBuilder fj = new StringBuilder();
        if (StringUtils.isNotBlank(fjStr) && !StringUtils.equals(fjStr, "null")) {
            fj = new StringBuilder(fjStr);
            if (!StringUtils.contains(fjStr, "由于办理司法裁定解封")) {
                fj = fj.append("\n由于办理司法裁定解封");
            }
        } else {
            fj = fj.append("由于办理司法裁定解封");
        }
        if (!StringUtils.contains(fjStr, "裁定转移人")) {
            fj.append("\n裁定转移人:" + ywrmc);
        }
        return fj.toString();
    }


    /**
     * @author jiangganzhi
     * @description 更新产权附记, 记录裁定情况
     */
    public void updateCqFj(String bdcdyid, StringBuilder ywrmc, BdcXm bdcXm, BdcXmRel bdcXmRel) {
        //根据不动产单元ID找到现势的产权信息
        List<BdcFdcq> bdcFdcqLst = qllxService.getFdcqByBdcdyId(bdcdyid);
        BdcFdcqDz bdcFdcqDz = qllxService.getFdcqDzByBdcdyId(bdcdyid);
        List<BdcJsydzjdsyq> bdcJsydzjdsyqLst = qllxService.getJsydzjdsyqByBdcdyId(bdcdyid);
        if (CollectionUtils.isNotEmpty(bdcFdcqLst)) {
            for (BdcFdcq bdcFdcq : bdcFdcqLst) {
                bdcFdcq.setFj(dealSfcdFj(bdcFdcq.getFj(), ywrmc));
                entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
            }
        }
        if (null != bdcFdcqDz) {
            bdcFdcqDz.setFj(dealSfcdFj(bdcFdcqDz.getFj(), ywrmc));
            entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
        }
        if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqLst)) {
            for (BdcJsydzjdsyq bdcJsydzjdsyq : bdcJsydzjdsyqLst) {
                bdcJsydzjdsyq.setFj(dealSfcdFj(bdcJsydzjdsyq.getFj(), ywrmc));
                entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
            }
        }
        //根据不动产单元号查询过渡查封
        List<GdFwsyq> gdFwsyqList = gdXmService.getGdFwsyqListByBdcdyid(bdcdyid);
        //根据不动产单元号查询过渡查封
        List<GdTdsyq> gdTdsyqList = gdXmService.getGdTdsyqListByBdcdyid(bdcdyid);

        if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
            for (GdFwsyq gdFwsyq : gdFwsyqList) {
                gdFwsyq.setFj(dealSfcdFj(gdFwsyq.getFj(), ywrmc));
                entityMapper.saveOrUpdate(gdFwsyq, gdFwsyq.getQlid());
                dealTdzCf(gdFwsyq.getQlid(), bdcXm.getBh(), bdcXm.getProid(), bdcXmRel);
            }
        }

        if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
            for (GdTdsyq gdTdsyq : gdTdsyqList) {
                gdTdsyq.setFj(dealSfcdFj(gdTdsyq.getFj(), ywrmc));
                entityMapper.saveOrUpdate(gdTdsyq, gdTdsyq.getQlid());
            }
        }
    }

    /**
     * @author jiangganzhi
     * @description 匹配不动产单元情况下司法裁定对不动产查封解封
     */
    public void completeSfcdBdcJf(BdcXm bdcXm, BdcXmRel bdcXmRel, StringBuilder ywrmc) {
        List<BdcCf> bdcCfLst = bdcCfService.getCfByBdcdyid(bdcXm.getBdcdyid());
        if (CollectionUtils.isNotEmpty(bdcCfLst)) {
            for (BdcCf bdcCf : bdcCfLst) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD_PL)) &&
                        (StringUtils.equals(bdcXmRel.getYproid(), bdcCf.getProid()) ||
                                StringUtils.equals(bdcCf.getProid(), bdcXm.getProid()))) {
                    bdcCf.setIscd(Constants.SXZT_ISSX);
                    bdcCf.setIssx(Constants.SXZT_ISSX_WSX);
                    bdcCf.setQszt(Constants.QLLX_QSZT_XS);
                    bdcCf.setFj(dealSfcdFj(bdcCf.getFj(), ywrmc));
                    entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                } else {
                    //将所有查封致为失效状态
                    bdcCf.setQszt(Constants.QLLX_QSZT_HR);
                    bdcCf.setJfywh(bdcXm.getBh());
                    bdcCf.setJfdjsj(CalendarUtil.getCurHMSDate());
                    bdcCf.setJfsj(CalendarUtil.getCurHMSDate());
                    UserInfo user = SessionUtil.getCurrentUser();
                    String username = "";
                    if (user != null) {
                        username = user.getUsername();
                    }
                    bdcCf.setJfdbr(username);
                    bdcCf.setIssx(Constants.SXZT_ISSX);
                    entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                    //生成对应的xmrel关系，保证推到共享库中的权利状态与不动产中的权利状态保持一致
                    BdcXmRel newBdcXmRel = new BdcXmRel();
                    newBdcXmRel.setRelid(UUIDGenerator.generate18());
                    newBdcXmRel.setProid(bdcXm.getProid());
                    newBdcXmRel.setYqlid(bdcCf.getQlid());
                    newBdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        newBdcXmRel.setQjid(bdcXmRel.getQjid());
                    }
                    if (StringUtils.isNotBlank(bdcCf.getProid())) {
                        newBdcXmRel.setYproid(bdcCf.getProid());
                    }
                    entityMapper.saveOrUpdate(newBdcXmRel, newBdcXmRel.getRelid());
                }
            }
            bdcZsCdService.updateBdcZscdByBdcXm(bdcXm);
        }
    }

    /**
     * @author jiangganzhi
     * @description 匹配不动产单元情况下司法裁定对过渡查封解封
     */
    public void completeSfcdGdJf(BdcXm bdcXm, BdcXmRel bdcXmRel, StringBuilder ywrmc) {
        List<GdCf> gdCfList = gdXmService.getGdCfListByBdcdyid(bdcXm.getBdcdyid());
        if (CollectionUtils.isNotEmpty(gdCfList)) {
            for (GdCf gdCf : gdCfList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD_PL)) &&
                        StringUtils.equals(bdcXmRel.getYproid(), gdCf.getProid())) {
                    gdCf.setIssx(Constants.SXZT_ISSX_WSX);
                    gdCf.setIscd(Constants.GDQL_ISZX_YZX.toString());
                    gdCf.setIsjf(1);
                    gdCf.setFj(dealSfcdFj(gdCf.getFj(), ywrmc));
                    entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                    gdFwService.changeGdqlztByQlid(gdCf.getCfid(), Constants.GDQL_ISZX_YZX.toString());
                    //生成对应的xmrel关系，保证推到共享库中的权利状态与不动产中的权利状态保持一致
                    BdcXmRel newBdcXmRel = new BdcXmRel();
                    newBdcXmRel.setRelid(UUIDGenerator.generate18());
                    newBdcXmRel.setProid(bdcXm.getProid());
                    newBdcXmRel.setYqlid(gdCf.getCfid());
                    if (StringUtils.equals(gdCf.getBdclx(), Constants.BDCLX_TD)) {
                        newBdcXmRel.setYdjxmly(Constants.XMLY_TDSP);
                    } else {
                        newBdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                    }
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        newBdcXmRel.setQjid(bdcXmRel.getQjid());
                    }
                    if (StringUtils.isNotBlank(gdCf.getProid())) {
                        newBdcXmRel.setYproid(gdCf.getProid());
                    }
                    entityMapper.saveOrUpdate(newBdcXmRel, newBdcXmRel.getRelid());
                } else {
                    gdCf.setIsjf(1);
                    gdCf.setIssx(Constants.GDQL_ISZX_YZX.toString());
                    gdCf.setJfywh(bdcXm.getBh());
/*                    gdCf.setJfdbsj(new Date(System.currentTimeMillis()));
                    UserInfo user = SessionUtil.getCurrentUser();
                    String username = "";
                    if (user != null) {
                        username = user.getUsername();
                    }
                    gdCf.setJfdbr(username);*/
                    entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                    gdFwService.changeGdqlztByQlid(gdCf.getCfid(), Constants.GDQL_ISZX_YZX.toString());
                    //生成对应的xmrel关系，保证推到共享库中的权利状态与不动产中的权利状态保持一致
                    BdcXmRel newBdcXmRel = new BdcXmRel();
                    newBdcXmRel.setRelid(UUIDGenerator.generate18());
                    newBdcXmRel.setProid(bdcXm.getProid());
                    newBdcXmRel.setYqlid(gdCf.getCfid());
                    if (StringUtils.equals(gdCf.getBdclx(), Constants.BDCLX_TD)) {
                        newBdcXmRel.setYdjxmly(Constants.XMLY_TDSP);
                    } else {
                        newBdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                    }
                    if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                        newBdcXmRel.setQjid(bdcXmRel.getQjid());
                    }
                    if (StringUtils.isNotBlank(gdCf.getProid())) {
                        newBdcXmRel.setYproid(gdCf.getProid());
                    }
                    entityMapper.saveOrUpdate(newBdcXmRel, newBdcXmRel.getRelid());
                }
            }
            bdcZsCdService.updateBdcZscdByBdcXm(bdcXm);
        }
    }

    private void completeSfcdzxBdcCf(List<BdcXmRel> bdcXmRelList) {

        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRel.getYproid());
                    if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
                        bdcCf.setQszt(Constants.QLLX_QSZT_XS);
                        bdcCf.setIssx(Constants.SXZT_ISSX_WSX);
                        bdcCf.setIscd(Constants.ISCD_NEGETIVE);
//                        bdcCf.setCdywh(bdcXm.getBh());
                        bdcCf.setJfywh("");
                        bdcCf.setJfdjsj(null);
                        bdcCf.setJfsj(null);
                        entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                    }
                }
            }
        }
    }
}
