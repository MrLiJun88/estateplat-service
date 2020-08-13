package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcJsydzjdsyqMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.EndProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * **********************在不清楚业务逻辑时  请不要修改默认方法************************
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-25
 */
public class EndProjectDefaultServiceImpl implements EndProjectService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    BdcJsydzjdsyqMapper bdcJsydzjdsyqMapper;
    @Autowired
    BdcCfService bdcCfService;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    private BdcJsydsyqLhxxService bdcJsydsyqLhxxService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private BdcZsCdService bdcZsCdService;

    @Override
    public void changeXmzt(BdcXm bdcXm) {
        bdcXmService.endBdcXm(bdcXm);
        //zdd 修改权利状态
//        qllxService.endQllxZt(bdcXm);
    }

    @Override
    public void batchChangeXmzt(List<BdcXm> bdcXmList) {
        bdcXmService.batchChangeXmzt(bdcXmList, Constants.XMZT_BJ);
    }

    @Override
    public void backXmzt(BdcXm bdcXm) {
        qllxService.backQllxZt(bdcXm);
        if (!StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_YGDJ_DM)) {
            backYgQszt(bdcXm);
        }
    }

    @Override
    public void backYqllxzt(BdcXm bdcXm) {
        //zdd 如果存在过渡数据  需要注销过渡权属状态
        List<BdcXmRel> bdcXmRelList = null;
        String pplx = AppConfig.getProperty("sjpp.type");
        if (bdcXm != null)
            bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx()) || (StringUtils.equals(pplx, Constants.PPLX_GC) && StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_FWSP)))
                    changeGdsjQszt(bdcXmRel, 0);
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    backYgQszt(bdcXm);
                }
            }
            bdcTdService.backGySjydZt(bdcXm);
        }

        //还原上一手证书状态为现实
        if ((!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx()) && StringUtils.equals(Constants.XMLY_BDC, bdcXm.getXmly()))
                && CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_XS);
                }
            }
        }
        // 司法裁定转移流程将是否裁定状态改为“是”
        if (null != bdcXm && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZY_SFCD)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYdjxmly()) && StringUtils.equals(bdcXmRel.getYdjxmly(), Constants.XMLY_BDC)) {
                    BdcCf bdcCf = bdcCfService.selectCfByProid(bdcXmRel.getYproid());
                    if (null != bdcCf) {
                        bdcCf.setIscd(Constants.ISCD_POSITIVE);
                        bdcCfService.saveBdcCf(bdcCf);
                    }
                } else {
                    GdCf gdCf = gdCfService.getGdCfByCfid(bdcXmRel.getYqlid());
                    if (null != gdCf) {
                        gdCf.setIscd(Constants.ISCD_POSITIVE);
                        gdCf.setIsjf(0);
                        gdCfService.saveGdCf(gdCf);
                    }
                }
            }
            bdcZsCdService.updateBdcZscdByBdcXm(bdcXm);
        }
        bdcTdService.changeGySjydZt(bdcXm, new ArrayList<String>());
    }


    @Override
    public void changeYqllxzt(BdcXm bdcXm) throws Exception {
        if (bdcXm == null) {
            throw new NullPointerException();
        }
        //zdd 如果存在过渡数据  需要注销过渡权属状态
        String pplx = AppConfig.getProperty("sjpp.type");
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        try {
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx()) || (StringUtils.equals(pplx, Constants.PPLX_GC) && StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_FWSP))) {
                        changeGdsjQszt(bdcXmRel, 1);
                    }
                    if (!CommonUtil.indexOfStrs(Constants.UNCHANGE_YGQSZT_SQLX, bdcXm.getSqlx()) && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        changeYgQszt(bdcXm);
                    }
                }

                //预告查封转查封
                String ycfTurnToCfEnable = StringUtils.deleteWhitespace(AppConfig.getProperty("ycfTurnToCf.enable"));
                if (StringUtils.equals(ycfTurnToCfEnable, ParamsConstants.TRUE_LOWERCASE)
                        && StringUtils.isNotBlank(bdcXm.getProid()) && Constants.SQLX_PLFZ_DM.equals(bdcXm.getSqlx())) {
                    BdcBdcdy bdcbdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                    if (bdcbdcdy != null && StringUtils.isNotBlank(bdcbdcdy.getBdcdyh())) {
                        // 预查封
                        List<BdcCf> bdcCfList = bdcCfService.queryYcfByBdcdyh(bdcXm.getBdcdyh());
                        Boolean ycfchangecf = false;
                        if (CollectionUtils.isNotEmpty(bdcCfList) && StringUtils.isNotBlank(bdcCfList.get(0).getBzxr())) {
                            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                            // 其中一个权利人 在该物的预查封被执行人里面就转
                            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                for (BdcQlr bdcQlr : bdcQlrList) {
                                    if (bdcQlr != null && StringUtils.isNotBlank(bdcQlr.getQlrmc()) && bdcCfList.get(0).getBzxr().contains(bdcQlr.getQlrmc())) {
                                        ycfchangecf = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (ycfchangecf) {
                            bdcCfService.ycfChangeCf(bdcXm, bdcCfList.get(0));
                        }
                        // 轮候预查封
                        Example example = new Example(BdcCf.class);
                        example.createCriteria().andEqualTo("bdcdyid", bdcXm.getBdcdyid()).andEqualTo("cflx", Constants.CFLX_ZD_LHYCF);
                        List<BdcCf> bdcLhycfList = entityMapper.selectByExample(example);
                        if (CollectionUtils.isNotEmpty(bdcLhycfList)) {
                            for (BdcCf bdcCf : bdcLhycfList) {
                                Boolean lhycfchangelhcf = false;
                                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                    for (BdcQlr bdcQlr : bdcQlrList) {
                                        if (bdcQlr != null && StringUtils.isNotBlank(bdcQlr.getQlrmc()) && StringUtils.isNotBlank(bdcCf.getBzxr()) && bdcCf.getBzxr().contains(bdcQlr.getQlrmc())) {
                                            lhycfchangelhcf = true;
                                            break;
                                        }
                                    }
                                }
                                if (lhycfchangelhcf) {
                                    StringBuilder fj = new StringBuilder();
                                    if (StringUtils.isNotBlank(bdcCf.getFj())) {
                                        fj.append(bdcCf.getFj());
                                    }
                                    fj.append(CalendarUtil.formatDateToString(new Date())).append("轮候预查封转轮候查封");
                                    bdcCf.setCflx(Constants.CFLX_LHCF);
                                    bdcCf.setFj(fj.toString());
                                    entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("EndProjectService.changeYqllxzt", e);
        }

        //改变上一手证书状态为历史
        if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFSCKFSZC_DM)
                || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GYJSYD_GZW_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)
                || (!CommonUtil.indexOfStrs(Constants.UNCHANGE_QSZT_SQLX, bdcXm.getSqlx()) && StringUtils.equals(Constants.XMLY_BDC, bdcXm.getXmly()))
                && CollectionUtils.isNotEmpty(bdcXmRelList)) {
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && !StringUtils.equalsIgnoreCase(Constants.BDC_KFJD_FQKF, bdcXm.getKfjd())) {
                    qllxService.changeQllxZt(bdcXmRel.getYproid(), Constants.QLLX_QSZT_HR);
                }
            }
        }
        /**
         * @author bianwen
         * @description 修改当前权利状态
         */
        qllxService.endQllxZt(bdcXm);
    }

    @Override
    public void batchChangeYqllxzt(List<BdcXm> bdcXmList) throws Exception {
        if (CollectionUtils.isEmpty(bdcXmList)) {
            throw new NullPointerException();
        }
        qllxService.batchEndQllxZt(bdcXmList);
    }


    /**
     * zdd 修改过渡数据的权属状态
     *
     * @param bdcXmRel
     * @param qszt
     */
    protected void changeGdsjQszt(BdcXmRel bdcXmRel, Integer qszt) {
        try {
            if (bdcXmRel != null && !StringUtils.equalsIgnoreCase("1",bdcXmRel.getYdjxmly()) && StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                if (bdcXmRel.getYqlid().indexOf(',') > -1) {
                    String[] qlids = bdcXmRel.getYqlid().split(",");
                    for (String qlid : qlids) {
                        gdXmService.updateGdQszt(qlid, qszt);
                    }
                } else {
                    gdXmService.updateGdQszt(bdcXmRel.getYqlid(), qszt);
                }
            }
        } catch (Exception e) {
            logger.error("EndProjectService.changeGdsjQszt", e);
        }
    }

    @Override
    public void changeYgQszt(BdcXm bdcXm) {
        try {
            if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    List<BdcYg> list = bdcYgService.getBdcYgList(bdcBdcdy.getBdcdyh(), Constants.QLLX_QSZT_XS.toString());
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (BdcYg bdcYg : list) {
                            //产权项目注销预告，抵押项目注销预抵押
                            if ((bdcXm.getQllx().equals(Constants.QLLX_GYTD_FWSUQ) && !CommonUtil.indexOfStrs(Constants.YG_YGDJZL_DY, bdcYg.getYgdjzl())) ||
                                    (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) && CommonUtil.indexOfStrs(Constants.YG_YGDJZL_DY, bdcYg.getYgdjzl()))) {
                                bdcYg.setQszt(Constants.QLLX_QSZT_HR);
                                String fj = bdcYg.getFj();
                                if (StringUtils.isNotBlank(fj)) {
                                    //此方法如果多权利人附记预转现可能会多次赋值
                                    if ((StringUtils.indexOf(fj, "预转现") == -1)) {
                                        if (bdcYg.getDjsj() != null)
                                            bdcYg.setFj(fj + "\n" + CalendarUtil.formateToStrChinaYMDDate(bdcYg.getDjsj()) + "\n预转现");
                                        else
                                            bdcYg.setFj(fj + "\n预转现");
                                    }
                                } else {
                                    if (bdcYg.getDjsj() != null)
                                        bdcYg.setFj(CalendarUtil.formateToStrChinaYMDDate(bdcYg.getDjsj()) + "\n预转现");
                                    else
                                        bdcYg.setFj("\n预转现");
                                }
                                entityMapper.updateByPrimaryKeySelective(bdcYg);
                            }
                        }
                    }
                    /**
                     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
                     * @description 注销过渡预告权属状态
                     */
                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.getGdBdcQlRelByBdcdyh(bdcBdcdy.getBdcdyh());
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                            GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, gdBdcQlRel.getQlid());
                            if (gdYg != null && gdYg.getIszx() != null && gdYg.getIszx() == 0 &&
                                    ((bdcXm.getQllx().equals(Constants.QLLX_GYTD_FWSUQ) && !CommonUtil.indexOfStrs(Constants.YG_YGDJZL_DY, gdYg.getYgdjzl())) ||
                                            (bdcXm.getQllx().equals(Constants.QLLX_DYAQ) && CommonUtil.indexOfStrs(Constants.YG_YGDJZL_DY, gdYg.getYgdjzl())))) {
                                gdYg.setIszx(1);
                                entityMapper.saveOrUpdate(gdYg, gdYg.getYgid());
                                gdFwService.changeGdqlztByQlid(gdYg.getYgid(), Constants.QLLX_QSZT_XS.toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("EndProjectService.changeYgQszt", e);
        }

    }

    @Override
    public void backYgQszt(BdcXm bdcXm) {
        try {
            if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    List<BdcYg> list = bdcYgService.getBdcYgList(bdcBdcdy.getBdcdyh(), Constants.QLLX_QSZT_HR.toString());
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (BdcYg bdcYg : list) {
                            String ygdjProid = bdcYg.getProid();
                            if (StringUtils.equals(ygdjProid, bdcXm.getProid())) {
                                continue;
                            }

                            List<BdcQlr> ygdjqlrList = bdcQlrService.queryBdcQlrByProid(ygdjProid);
                            List<HashMap<String, String>> ygdjQlrs = new ArrayList<HashMap<String, String>>();
                            if (CollectionUtils.isNotEmpty(ygdjqlrList)) {
                                for (BdcQlr ygdjqlr : ygdjqlrList) {
                                    HashMap<String, String> qlrmap = new HashMap<String, String>();
                                    qlrmap.put("qlr", ygdjqlr.getQlrmc());
                                    qlrmap.put("zjh", ygdjqlr.getQlrzjh());
                                    ygdjQlrs.add(qlrmap);
                                }
                            }

                            List<BdcQlr> qlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                            if (CollectionUtils.isNotEmpty(qlrList)) {
                                for (BdcQlr bdcQlr : qlrList) {
                                    HashMap<String, String> qlrmap = new HashMap<String, String>();
                                    qlrmap.put("qlr", bdcQlr.getQlrmc());
                                    qlrmap.put("zjh", bdcQlr.getQlrzjh());
                                    if (ygdjQlrs.contains(qlrmap)) {
                                        //zdd 预告登记权利人信息与当前权利人信息一致   还原原预告登记
                                        bdcYg.setQszt(Constants.QLLX_QSZT_XS);
                                        String fj = bdcYg.getFj();
                                        //退回附记
                                        if (StringUtils.isNotBlank(fj)) {
                                            //此方法如果多权利人附记预转现可能会多次赋值
                                            if ((StringUtils.indexOf(fj, "预转现") > -1)) {
                                                if (bdcYg.getDjsj() != null) {
                                                    fj = fj.replace(CalendarUtil.formateToStrChinaYMDDate(bdcYg.getDjsj()), "");
                                                    fj = fj.replace("预转现", "");
                                                    bdcYg.setFj(fj);
                                                } else {
                                                    fj = fj.replace("预转现", "");
                                                    bdcYg.setFj(fj);
                                                }

                                            }
                                        } else {
                                            if (bdcYg.getDjsj() != null) {
                                                fj = fj.replace(CalendarUtil.formateToStrChinaYMDDate(bdcYg.getDjsj()), "");
                                                fj = fj.replace("预转现", "");
                                                bdcYg.setFj(fj);
                                            } else {
                                                fj = fj.replace("预转现", "");
                                                bdcYg.setFj(fj);
                                            }
                                        }
                                        entityMapper.updateByPrimaryKeySelective(bdcYg);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("EndProjectService.backYgQszt", e);
        }

    }

    @Override
    public void changeYyQszt(final BdcXm bdcXm) {
        if (StringUtils.isNotBlank(bdcXm.getProid())) {
            qllxService.changeQllxZt(bdcXm.getProid(), Constants.QLLX_QSZT_XS);
        }
    }

    @Override
    public void doExtraWork(BdcXm bdcXm) {
        if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) {
            /**
             * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
             * @description 更新权籍量化关系表
             */
            bdcJsydsyqLhxxService.updateFwljzGcjd(bdcXm.getProid(), Constants.GCJD_SD);
        }
    }
}
