package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDelZszhMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZsMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.BdczsBhService;
import cn.gtmap.estateplat.server.sj.zs.BdcZsTyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 不动产证书
 * Created by lst on 2015/3/17.
 */
@Repository
public class BdcZsServiceImpl implements BdcZsService {

    @Autowired
    public BdcZsMapper bdcZsMapper;
    @Autowired
    public BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    public BdczsBhService bdczsBhService;

    @Autowired
    BdcZdQllxService bdcZdQllxService;

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    QllxService qllxService;

    @Autowired
    BdcFdcqService bdcFdcqSeice;

    @Autowired
    BdcZsCreatZsInfoServiceImpl bdcZsCreatZsInfoService;

    @Autowired
    BdcZsCreatZsInfoViewServiceImpl bdcZsCreatZsInfoVieService;
    @Autowired
    BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    private SysSignService sysSignService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BdcDelZszhMapper bdcDelZszhMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcXtQlqtzkConfigService bdcXtQlqtzkConfigService;

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcZdGlMapper bdcZdGlMapper;
    @Autowired
    private BdcLshService bdcLshService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private BdcFwfzxxService bdcFwfzxxService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcZsTyService bdcZsTyService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String CONFIGURATION_PARAMETER_SCDJXXB_BH_VERSION = "scdjxxb.bh.version";
    private static final String CONFIGURATION_PARAMETER_SYS_VERSION = "sys.version";

    @Override
    public void delBdcZsByZsid(final String zsid) {
        if (StringUtils.isNotBlank(zsid)) {
            BdcZs bdcZs = queryBdcZsByZsid(zsid);
            if (bdcZs != null && StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                HashMap map = new HashMap();
                map.put(ParamsConstants.BDCQZH_LOWERCASE, bdcZs.getBdcqzh());
                map.put("dwdm", bdcZs.getDwdm());
                map.put("nf", bdcZs.getNf());
                map.put("isuse", "0");
                List<BdcDelZszh> bdcDelZszhList = bdcDelZszhMapper.getBdcDelZszhList(map);
                if (CollectionUtils.isEmpty(bdcDelZszhList)) {
                    BdcDelZszh bdcDelZszh = getBdcDelZszhFromBdcZs(null, bdcZs);
                    entityMapper.insertSelective(bdcDelZszh);
                }
            }
            entityMapper.deleteByPrimaryKey(BdcZs.class, zsid);
        }
    }

    @Override
    public BdcDyZs creatBdcZs(BdcXm bdcXm, String czr, String bdcqzh, String userId, Boolean previewZs) {
        BdcDyZs bdcDyZs = creatBdcZs(bdcXm, czr, bdcqzh, previewZs);
        if (!previewZs) {
            String bhVersion = AppConfig.getProperty(CONFIGURATION_PARAMETER_SCDJXXB_BH_VERSION);
            if (bdcDyZs != null && StringUtils.equals(bdcDyZs.getZstype(), Constants.BDCQSCDJZ_BH_FONT) && StringUtils.equals(bhVersion, "sz")) {
                //不动产首次信息表添加编号
                String nf = CalendarUtil.getCurrYear();
                String qh = bdcLshService.getQh(userId);
                String lsh = "";
                String bh = "";
                List<BdcFdcq> bdcFdcqList = bdcFdcqSeice.getBdcFdcqByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    String zxzh = bdcFdcqList.get(0).getZxzh();
                    Map map = new HashMap();
                    map.put("nf", nf);
                    map.put("zstype", Constants.BDCQSCDJZ_BH_FONT);
                    map.put("zxzh", zxzh);
                    String bhlxdm = bdcLshService.getBhlxDmBybhlxMc(Constants.BHLX_BDCSCXXB_MC);
                    Integer len = bdcLshService.getLshwsByBhlxdm(bhlxdm);
                    Integer maxLsh = getMaxScdjzLsh(map);
                    if (maxLsh == null) {
                        maxLsh = 0;
                    }
                    ++maxLsh;
                    lsh = maxLsh.toString();
                    if (lsh.length() < len) {
                        do {
                            lsh = "0" + lsh;
                        } while (lsh.length() < len);
                    }
                    bh = zxzh + lsh;
                }
                if (StringUtils.isNotBlank(bh)) {
                    bdcDyZs.setZhlsh(lsh);
                    bdcDyZs.setBh(bh);
                    bdcDyZs.setBdcqzh(bh);
                }
            }
        }
        return bdcDyZs;
    }

    @Override
    public BdcDyZs creatBdcZs(BdcXm bdcXm, String czr, String bdcqzh, Boolean previewZs) {
        BdcDyZs bdcDyZs = new BdcDyZs();
        if (bdcXm != null) {
//            zx判断提前生成证书
            //重新生成其他权利状况
            bdcDyZs = getZsQlqtzk(bdcXm, new BdcDyZs());
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
            List<BdcFdcq> bdcFdcqList = bdcFdcqSeice.getBdcFdcq(param);
            if (!StringUtils.equals(bdcXm.getSftqsczs(), "1") && !previewZs) {
                //生成证号
                if (StringUtils.isBlank(bdcqzh)) {
                    BdcZs bdcZs = bdczsBhService.creatBdcqzBh(bdcXm, bdcDyZs, 0, null);
                    bdcDyZs = new BdcDyZs();
                    try {
                        BeanUtils.copyProperties(bdcDyZs, bdcZs);
                    } catch (IllegalAccessException e) {
                        logger.error("BdcZsServiceImpl.creatBdcZs", e);
                    } catch (InvocationTargetException e) {
                        logger.error("BdcZsServiceImpl.creatBdcZs", e);
                    }
                } else {
                    bdcDyZs.setBdcqzh(bdcqzh);
                }
            }
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            String zsFont = qllxService.makeSureBdcqzlx(qllxVo);

            if ((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM)) && CollectionUtils.isNotEmpty(bdcFdcqList)) {
                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                String fzlx = bdcFdcq.getFzlx();
                //首次信息表表变更登记，选择不动产单元，自动生成发证类型（信息表）
                if (StringUtils.isBlank(fzlx) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM)) {
                    bdcFdcq.setFzlx(Constants.FZLX_FZM);
                    entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                }
                //获取首次信息表变更登记自动生成的发证类型（信息表）
                fzlx = bdcFdcq.getFzlx();
                if (StringUtils.equals(fzlx, Constants.FZLX_FZM)) {
                    String zsFont1 = AppConfig.getProperty("spfscdj.zstype");
                    if (StringUtils.isNotBlank(zsFont1) && (StringUtils.equals(zsFont1, Constants.BDCQSCDJZ_BH_FONT))) {
                        zsFont = zsFont1;
                    }
                }
            }
            bdcDyZs.setZstype(zsFont);
            //根据zstype判断是否需要持证人
            if (StringUtils.equals(bdcDyZs.getZstype(), Constants.BDCQZS_BH_FONT) &&
                    !StringUtils.equals(AppConfig.getProperty("dwdm"), "320500")) {
                bdcZsCreatZsInfoService.setCzr(bdcXm, czr, bdcDyZs);
            }

            //不动产权证号配置到各乡镇，各乡镇可能不同可能相同,就需要将该项目办理乡镇的行政代码存储到bdcZs里的dwdm
            String bdcqzhFilterZhXzdm = AppConfig.getProperty("bdcqzh.filterZh.xzdm");
            if (StringUtils.equals(bdcqzhFilterZhXzdm, "true"))
                bdcDyZs.setDwdm(bdcXm.getSsxz());
            else
                bdcDyZs.setDwdm(bdcXm.getDwdm());
            bdcDyZs.setZslx(bdcXm.getQllx());
            bdcDyZs.setCzr(czr);
            if (!previewZs) {
                bdcDyZs.setCzrq(CommonUtil.getCurrDate());
            }
            //补充增加证书信息字段
            //qlr
            if (StringUtils.equals(bdcXm.getSqfbcz(), "是")) {
                bdcDyZs.setQlr(czr);
            } else {
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    StringBuilder qlr = new StringBuilder();
                    for (BdcQlr bdcQlr : bdcQlrList) {
                        if (StringUtils.isNotBlank(qlr)) {
                            qlr.append(" ").append(bdcQlr.getQlrmc());
                        } else {
                            qlr.append(bdcQlr.getQlrmc());
                        }
                    }
                    bdcDyZs.setQlr(qlr.toString());
                }
            }
            //qijiadong 获取qlqtzk的其余共有人
            if (CollectionUtils.isEmpty(bdcFdcqList) || !((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) && StringUtils.equals(bdcFdcqList.get(0).getFzlx(), Constants.FZLX_FZM))) {
                Boolean isAfgyContainGtgy = bdcQlrService.isAfgyContainGtgy(bdcQlrList);
                String qllx = bdcXm.getQllx();
                if ((StringUtils.equals(bdcXm.getSqfbcz(), Constants.BDCXM_FBCZ) || isAfgyContainGtgy) && !StringUtils.equals(qllx, Constants.QLLX_DYAQ) && !StringUtils.equals(qllx, Constants.QLLX_YGDJ) && !StringUtils.equals(qllx, Constants.QLLX_YYDJ)) {
                    bdcZsCreatZsInfoService.setQygyr(bdcXm, czr, bdcDyZs);
                }
            }
            //ywr
            List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                StringBuilder ywr = new StringBuilder();
                for (BdcQlr bdcQlr : bdcYwrList) {
                    if (StringUtils.isNotBlank(ywr)) {
                        ywr.append(" ").append(bdcQlr.getQlrmc());
                    } else {
                        ywr.append(bdcQlr.getQlrmc());
                    }
                }
                bdcDyZs.setYwr(ywr.toString());
            }
            HashMap map = Maps.newHashMap();
            //只生成一本证书的情况根据wiid查询
            if (StringUtils.equals(Constants.DJLX_PLDY_YBZS_SQLXDM, bdcXm.getSqlx())) {
                map.put("onezsproid", bdcXm.getProid());
            } else {
                map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
            }
            List<HashMap> bdczsViewList = bdcZsMapper.getViewBdcqzList(map);
            String fj = "";
            if (CollectionUtils.isNotEmpty(bdczsViewList)) {
                fj = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("FJ"));
                for (HashMap bdczsView : bdczsViewList) {
                    String tempFj = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsView.get("FJ"));
                    if (StringUtils.isNotBlank(fj) && !StringUtils.contains(fj, tempFj)) {
                        fj += "\n" + tempFj;
                    }
                }
                qllxVo = qllxService.queryQllxVo(bdcXm);
                if (qllxVo != null) {
                    if (qllxVo instanceof BdcFdcq) {
                        if (StringUtils.isNotBlank(((BdcFdcq) qllxVo).getFsss())) {
                            fj += "\n" + ((BdcFdcq) qllxVo).getFsss();
                        }
                    } else if (qllxVo instanceof BdcFdcqDz) {
                        List<BdcFwfzxx> bdcFwfzxxList = bdcFwfzxxService.getBdcFwfzxxListByQlid(qllxVo.getQlid());
                        String fzxxFj = "";
                        if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
                            for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                                if (StringUtils.isNotBlank(bdcFwfzxx.getFsss())) {
                                    if (StringUtils.isNotBlank(fzxxFj)) {
                                        fzxxFj += "," + bdcFwfzxx.getFsss();
                                    } else {
                                        fzxxFj = bdcFwfzxx.getFsss();
                                    }
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(fzxxFj)) {
                            fj += "\n" + fzxxFj;
                        }
                    }
                }
            }
            if (StringUtils.equals(bdcDyZs.getZstype(), Constants.BDCQZM_BH_FONT)) {
                //不动产权证明
                //zl、bdcdyh、mj、syqx
                if (CollectionUtils.isNotEmpty(bdczsViewList)) {
                    bdcDyZs.setZl(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZL")));
                    bdcDyZs.setBdcdyh(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)));
                    bdcDyZs.setFj(fj);
                }
                bdcDyZs.setZmqlsx(bdcXm.getQllx());
            } else {
                //gyqk
                if (CollectionUtils.isNotEmpty(bdcQlrList) && StringUtils.isNotBlank(bdcQlrList.get(0).getGyfs())) {
                    List<String> gyfsList = new ArrayList<String>();
                    for (BdcQlr bdcQlr : bdcQlrList) {
                        if (!gyfsList.contains(bdcQlr.getGyfs()))
                            gyfsList.add(bdcQlr.getGyfs());
                    }
                    if (CollectionUtils.isNotEmpty(gyfsList) && gyfsList.size() == 2 && gyfsList.contains(Constants.GYFS_AFGY_DM) && gyfsList.contains(Constants.GYFS_GTGY_DM)) {
                        bdcDyZs.setGyqk(Constants.GYFS_AFGY_DM);
                    } else {
                        bdcDyZs.setGyqk(bdcQlrList.get(0).getGyfs());
                    }
                }
//                else
//                    bdcDyZs.setGyqk(Constants.GYFS_DDGY_DM);
                //zl、bdcdyh、mj、syqx
                if (CollectionUtils.isNotEmpty(bdczsViewList)) {
                    bdcDyZs.setZl(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZL")));
                    bdcDyZs.setBdcdyh(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)));
                    bdcDyZs.setQlxz(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("QLXZ")));
                    bdcDyZs.setYt(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("YT")));
                    String sfpdgyz = AppConfig.getProperty("sfpdgyz");
                    String zdMj = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZSMJ"));
                    if (StringUtils.isNotBlank(sfpdgyz) && Boolean.parseBoolean(sfpdgyz)) {
                        String zdLb = bdcTdService.getZdLb(bdcXm.getProid());
                        //jyl 南通市证书面积显示方式太特殊
                        String sysVersion = AppConfig.getProperty(CONFIGURATION_PARAMETER_SYS_VERSION);
                        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NT)) {
                            zdMj = bdcTdService.changeMjByNt(zdMj, bdcXm.getBdclx(), bdcXm.getProid());
                        } else {
                            zdMj = bdcTdService.changeMjByZdLb(zdLb, zdMj, bdcXm.getBdclx());
                        }
                    }

                    //针对宅基地证书的面积，做特殊处理
                    String zjdzspzmj = AppConfig.getProperty("zjdzs.sfpzmj.enable");
                    String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
                    if (StringUtils.equals("true", zjdzspzmj) && ((StringUtils.isNoneBlank(bdcdyh) && (bdcdyh.indexOf("JC") != -1)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && cn.gtmap.estateplat.utils.CommonUtil.indexOfStrs(Constants.SQLX_ZJDMJ, bdcXm.getSqlx())))) {
                        zdMj = getZjdzsmj(bdcXm.getProid(), bdcXm.getBdclx(), zdMj);
                    }
                    bdcDyZs.setMj(zdMj);
                    /**
                     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
                     * @description 盐城需求，层高小于2.2米的车库不显示面积，不保存面积
                     */
                    if (AppConfig.getProperty("dwdm").equals("320900") && CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                        if (cn.gtmap.estateplat.utils.CommonUtil.indexOfStrs(Constants.GHYT_CK_DM, bdcFdcq.getGhyt()) && bdcFdcq.getCg() != null && bdcFdcq.getCg() < 2.2) {
                            bdcDyZs.setMj("");
                        }
                    }
                    String djsy = bdcXm.getDjsy();
                    String syksqx = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYKSQX"));
                    String syjsqx = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYJSQX"));
                    String syksqx2 = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYKSQX2"));
                    String syjsqx2 = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYJSQX2"));
                    String syksqx3 = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYKSQX3"));
                    String syjsqx3 = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYJSQX3"));
                    String djsymc = "";
                    String syqx = "";
                    if (StringUtils.isNotBlank(syksqx2) || StringUtils.isNotBlank(syjsqx2) || StringUtils.isNotBlank(syksqx3) || StringUtils.isNotBlank(syjsqx3)) {
                        syqx = "详见附记";
                    } else {
                        if (StringUtils.isNotBlank(djsy) && StringUtils.length(djsy) > 0 && djsy.indexOf('/') > -1)
                            djsymc = bdcZdGlMapper.getDjsyByDm(djsy.substring(0, djsy.indexOf('/')));
                        else if (StringUtils.isNotBlank(djsy))
                            djsymc = bdcZdGlMapper.getDjsyByDm(djsy);
                        if (StringUtils.isNotBlank(syjsqx)) {
                            syqx = djsymc + " " + CalendarUtil.formateToStrChinaYMDDate(CalendarUtil.formatDate(syjsqx)) + "止";
                        }
                    }
                    bdcDyZs.setSyqx(syqx);
                    bdcDyZs.setFj(fj);
                }
            }
            bdcDyZs.setBdcqzhjc(bdcDyZs.getNf() + bdcDyZs.getZhlsh());
            bdcDyZs.setQllx(bdcXm.getQllx());
            bdcDyZs.setEwmnr(bdcDyZs.getBdcqzh());
        }
        bdcZsTyService.initCfzs(bdcDyZs, bdcXm);
        return bdcDyZs;
    }

    private String getZjdzsmj(String proid, String bdclx, String zdmj) {
        StringBuilder zjdmj = new StringBuilder();
        //对可能出现的null值赋0
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
        Double zdzhmj = bdcSpxx.getZdzhmj() != null ? bdcSpxx.getZdzhmj() : 0;
        Double zdpzmj = bdcSpxx.getZdpzmj() != null ? bdcSpxx.getZdpzmj() : 0;
        Double mj = bdcSpxx.getMj() != null ? bdcSpxx.getMj() : 0;
        Double fwpzjzmj = bdcSpxx.getFwpzjzmj() != null ? bdcSpxx.getFwpzjzmj() : 0;


        //比较，取非0数中的较小值，相等时取（宗地/房屋）批准面积
        if (zdzhmj != 0) {
            if (zdpzmj != 0) {
                if (zdzhmj < zdpzmj) {
                    zjdmj.append(Constants.ZJDMJ_TD[0].replace("@zdzhmj", zdzhmj.toString()));
                } else {
                    zjdmj.append(Constants.ZJDMJ_TD[1].replace("@zdpzmj", zdpzmj.toString()));
                }
            } else {
                zjdmj.append(Constants.ZJDMJ_TD[0].replace("@zdzhmj", zdzhmj.toString()));
            }
        } else if (zdpzmj != 0) {
            zjdmj.append(Constants.ZJDMJ_TD[1].replace("@zdpzmj", zdpzmj.toString()));
        }

        if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            if (zjdmj.length() > 0) {
                zjdmj.append("/");
            }

            if (mj != 0) {
                if (fwpzjzmj != 0) {
                    if (mj < fwpzjzmj) {
                        zjdmj.append(Constants.ZJDMJ_TDFW[0].replace("@scmj", mj.toString()));
                    } else if (fwpzjzmj != 0) {
                        zjdmj.append(Constants.ZJDMJ_TDFW[1].replace("@fwpzjzmj", fwpzjzmj.toString()));
                    }
                } else {
                    zjdmj.append(Constants.ZJDMJ_TDFW[0].replace("@scmj", mj.toString()));
                }
            } else if (fwpzjzmj != 0) {
                zjdmj.append(Constants.ZJDMJ_TDFW[1].replace("@fwpzjzmj", fwpzjzmj.toString()));
            } else {
                zjdmj.deleteCharAt(zjdmj.length() - 1);
            }
        }

        if (zjdmj.length() > 0) {
            zdmj = zjdmj.toString();
        }
        return zdmj;
    }

    @Override
    public Integer getMaxLsh(final Map map) {
        return bdcZsMapper.getMaxLsh(map);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BdcZs queryBdcZsByZsid(final String zsid) {
        return entityMapper.selectByPrimaryKey(BdcZs.class, zsid);
    }


    @Override
    public List<BdcZs> getYbdcqz(BdcXm bdcXm) {
        List<BdcZs> bdcZsList = null;
        HashMap map = new HashMap();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {

            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            if (bdcBdcdy != null) {

                map.put("bdcdyh", bdcBdcdy.getBdcdyh());
                map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
                bdcZsList = bdcZsMapper.getYbdcqzByProidAndBdcdyh(map);

            }
        }
        return bdcZsList;
    }

    @Override
    public BdcDyZs getQtqkforView(final String proid) {
        BdcDyZs bdcZs = new BdcDyZs();
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        bdcZs = getZsQlqtzView(bdcXm, bdcZs);
        return bdcZs;
    }

    @Override
    public String getZsFjView(final String proid) {
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
        return getZsFjView(bdcXm);
    }

    @Override
    public List<BdcZs> creatBdcqz(BdcXm bdcXm, List<BdcQlr> bdcQlrList, Boolean previewZs) {
        return creatBdcqz(bdcXm, bdcQlrList, "", previewZs);
    }

    //synchronized关键字与Transactional主键不能同时使用
    @Override
    public List<BdcZs> creatBdcqz(BdcXm bdcXm, List<BdcQlr> bdcQlrList, String userId, Boolean previewZs) {
        //zx创建证书时先删除证书和权利人和证书关系
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            delBdcZsByProid(bdcXm.getProid());
        }
        List<BdcZs> list = new ArrayList<BdcZs>();
        if (previewZs) {
            if (bdcXm != null && "是".equals(bdcXm.getSqfbcz())) {
                // 生成多本证书
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for (int i = 0; i < bdcQlrList.size(); i++) {
                        BdcQlr bdcQlr = bdcQlrList.get(i);
                        BdcDyZs bdcDyZs = creatBdcZs(bdcXm, bdcQlr.getQlrmc(), "", previewZs);
                        if (bdcDyZs == null)
                            continue;
                        bdcDyZs.setZsid(UUIDGenerator.generate18());
                        BdcZs bdcZs = getBdcZsFromBdcDyZs(bdcDyZs);
                        entityMapper.insertSelective(bdcZs);
                        BdcZs bdcZs1 = getBdcZsFromBdcDyZs(bdcDyZs);
                        list.add(bdcZs1);

                    }
                }
            } else {
                StringBuilder czr = new StringBuilder();
                if (bdcQlrList != null) {
                    if (bdcQlrList.size() == 1) {
                        czr.append(bdcQlrList.get(0).getQlrmc());
                    } else if (bdcQlrList.size() > 1) {
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            if (!StringUtils.equals(Constants.CZR, bdcQlr.getSfczr())) {
                                continue;
                            }
                            if (StringUtils.isBlank(czr)) {
                                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                    czr.append(bdcQlr.getQlrmc());
                                }
                            } else {
                                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                    czr = czr.append("、").append(bdcQlr.getQlrmc());
                                }
                            }
                        }
                    }

                }
                String bdcZsId = "";
                String bdcqzh = "";
                BdcDyZs bdcZs = creatBdcZs(bdcXm, czr.toString(), bdcqzh, userId, previewZs);
                bdcZsId = UUIDGenerator.generate18();
                bdcZs.setZsid(bdcZsId);
                BdcZs bdcZs1 = getBdcZsFromBdcDyZs(bdcZs);
                entityMapper.insertSelective(bdcZs1);
                list.add(bdcZs1);
            }
        } else {
            synchronized (this) {
                if (bdcXm != null && "是".equals(bdcXm.getSqfbcz())) {
                    // 生成多本证书
                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                        for (int i = 0; i < bdcQlrList.size(); i++) {
                            BdcQlr bdcQlr = bdcQlrList.get(i);
                            BdcDyZs bdcDyZs = creatBdcZs(bdcXm, bdcQlr.getQlrmc(), "", previewZs);
                            if (bdcDyZs == null)
                                continue;
                            bdcDyZs.setZsid(UUIDGenerator.generate18());
                            BdcZs bdcZs = getBdcZsFromBdcDyZs(bdcDyZs);
                            entityMapper.insertSelective(bdcZs);
                            BdcZs bdcZs1 = getBdcZsFromBdcDyZs(bdcDyZs);
                            list.add(bdcZs1);

                        }
                    }
                } else {
                    StringBuilder czr = new StringBuilder();
                    if (bdcQlrList != null) {
                        if (bdcQlrList.size() == 1) {
                            czr.append(bdcQlrList.get(0).getQlrmc());
                        } else if (bdcQlrList.size() > 1) {
                            for (BdcQlr bdcQlr : bdcQlrList) {
                                if (!StringUtils.equals(Constants.CZR, bdcQlr.getSfczr())) {
                                    continue;
                                }
                                if (StringUtils.isBlank(czr)) {
                                    if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                        czr.append(bdcQlr.getQlrmc());
                                    }
                                } else {
                                    if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                        czr = czr.append("、").append(bdcQlr.getQlrmc());
                                    }
                                }
                            }
                        }

                    }
                    String bdcZsId = "";
                    String bdcqzh = "";
                    BdcDyZs bdcZs = creatBdcZs(bdcXm, czr.toString(), bdcqzh, userId, previewZs);
                    bdcZsId = UUIDGenerator.generate18();
                    bdcZs.setZsid(bdcZsId);
                    BdcZs bdcZs1 = getBdcZsFromBdcDyZs(bdcZs);
                    entityMapper.insertSelective(bdcZs1);
                    list.add(bdcZs1);
                }
            }
        }
        return list;
    }

    @Override
    public List<BdcZs> creatBdcqz(BdcXm bdcXm, BdcQlr bdcQlr, String userId) {
        //zx创建证书时先删除证书和权利人和证书关系
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            delBdcZsByProid(bdcXm.getProid());
        }
        List<BdcZs> list = new ArrayList<BdcZs>();
        synchronized (this) {
            StringBuilder czr = new StringBuilder(bdcQlr.getQlrmc());
            if (StringUtils.isNotBlank(bdcQlr.getQygyr())) {
                czr.append(",").append(bdcQlr.getQygyr());
            }
            String bdcZsId = "";
            String bdcqzh = "";
            BdcDyZs bdcZs = creatBdcZs(bdcXm, czr.toString(), bdcqzh, userId, null);
            bdcZsId = UUIDGenerator.generate18();
            bdcZs.setZsid(bdcZsId);
            BdcZs bdcZs1 = getBdcZsFromBdcDyZs(bdcZs);
            entityMapper.insertSelective(bdcZs1);
            list.add(bdcZs1);

        }
        return list;

    }

    @Override
    public BdcDyZs getZsQlqtzk(BdcXm bdcXm, BdcDyZs bdcZs) {
        try {
            if (bdcXm != null && bdcXm.getSqlx() != null) {
                //获取权利
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                    qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
                }

                //证书里面的qlqtzk直接获取权利里面的
                if (qllxVo != null) {
                    if (StringUtils.isNotEmpty(qllxVo.getQlqtzk())) {
                        bdcZs.setQlqtzk(qllxVo.getQlqtzk());
                    }
                }
            }
            if (bdcXm != null) {
                QllxVo qllxVo1 = qllxService.queryQllxVo(bdcXm);
                if (qllxVo1 != null) {
                    if (qllxVo1 instanceof BdcHysyq)
                        bdcZs.setFzrq(((BdcHysyq) qllxVo1).getDjsj());
                    else if (qllxVo1 instanceof BdcJsydzjdsyq)
                        bdcZs.setFzrq(((BdcJsydzjdsyq) qllxVo1).getDjsj());
                    else if (qllxVo1 instanceof BdcJzwgy)
                        bdcZs.setFzrq(((BdcJzwgy) qllxVo1).getDjsj());
                    else if (qllxVo1 instanceof BdcJzwsyq)
                        bdcZs.setFzrq(((BdcJzwsyq) qllxVo1).getDjsj());
                    else if (qllxVo1 instanceof BdcQsq)
                        bdcZs.setFzrq(((BdcQsq) qllxVo1).getDjsj());
                    else if (qllxVo1 instanceof BdcDyaq)
                        bdcZs.setFzrq(((BdcDyaq) qllxVo1).getDjsj());
                }

            }
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid()))
                bdcZs.setBdcdyid(bdcXm.getBdcdyid());
        } catch (Exception e) {
            logger.error("BdcZsServiceImpl.getZsQlqtzk", e);
        }
        return bdcZs;
    }

    @Override
    public List<BdcZs> queryBdcZsByProid(final String proid) {
        List<BdcZs> list = null;
        if (StringUtils.isNotBlank(proid)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            list = bdcZsMapper.queryBdcZs(map);
        }
        return list;
    }

    @Override
    public String getYbdcqzhByProid(final String proid) {
        return bdcZsMapper.getYbdcqzhByProid(proid);
    }

    @Override
    public List<Map> getGdzsByProid(final String proid) {
        return bdcZsMapper.getGdzsByProid(proid);
    }

    @Override
    public void updateSzrByProid(final String proid, final String userId, final String defaultUserId, final String signKey) {
        String getUserId = "";
        String getUserName = "";
        if (StringUtils.isNotBlank(signKey)) {
            List<PfSignVo> pfSignVoList = sysSignService.getSignList(signKey, proid);
            if (CollectionUtils.isNotEmpty(pfSignVoList)) {
                getUserId = pfSignVoList.get(0).getUserId();
            }
        } else if (StringUtils.isNotBlank(defaultUserId)) {
            getUserId = defaultUserId;
        } else if (StringUtils.isNotBlank(userId)) {
            getUserId = userId;
        }
        if (StringUtils.isNotBlank(getUserId)) {
            PfUserVo pfUserVo = sysUserService.getUserVo(getUserId);
            if (pfUserVo != null) {
                getUserName = pfUserVo.getUserName();

            }
        }
        if (StringUtils.isNotBlank(proid)) {
            List<BdcZs> bdcZsList = queryBdcZsByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                Date curHMSDate = CalendarUtil.getCurHMSDate();
                for (BdcZs bdcZs : bdcZsList) {
                    if (bdcZs != null) {
                        bdcZs.setSzrq(curHMSDate);
                        bdcZs.setSzr(getUserName);
                        entityMapper.updateByPrimaryKeySelective(bdcZs);
                    }
                }
            }
        }
    }

    public BdcDelZszh getBdcDelZszhFromBdcZs(BdcDelZszh bdcDelZszh, BdcZs bdcZs) {
        if (bdcZs != null) {
            if (bdcDelZszh == null)
                bdcDelZszh = new BdcDelZszh();
            bdcDelZszh.setBdcqzh(bdcZs.getBdcqzh());
            bdcDelZszh.setDwdm(bdcZs.getDwdm());
            bdcDelZszh.setNf(bdcZs.getNf());
            bdcDelZszh.setSqsjc(bdcZs.getSqsjc());
            bdcDelZszh.setSzsxqc(bdcZs.getSzsxqc());
            bdcDelZszh.setZslx(bdcZs.getZslx());
            bdcDelZszh.setGxrq(new Date());
            bdcDelZszh.setZhlsh(bdcZs.getZhlsh());
            bdcDelZszh.setZstype(bdcZs.getZstype());
            if (StringUtils.isBlank(bdcDelZszh.getZszhid()))
                bdcDelZszh.setZszhid(UUIDGenerator.generate());
        }
        return bdcDelZszh;
    }

    @Override
    public void delBdcZsByProid(final String proid) {
        if (StringUtils.isNotBlank(proid)) {
            List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
                for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                    if (bdcXmzsRel != null && StringUtils.isNotBlank(bdcXmzsRel.getZsid())) {
                        bdcZsQlrRelService.delZsQlrRelByZsid(bdcXmzsRel.getZsid());
                        delBdcZsByZsid(bdcXmzsRel.getZsid());
                    }
                }
            }
            bdcXmZsRelService.delBdcXmZsRelByProid(proid);

        }
    }

    public BdcZs getBdcZsFromBdcDyZs(BdcDyZs bdcDyZs) {
        BdcZs bdcZs = new BdcZs();
        if (bdcDyZs != null) {
            bdcZs.setSzr(bdcDyZs.getSzr());
            bdcZs.setBdcqzh(bdcDyZs.getBdcqzh());
            bdcZs.setBdcqzhjc(bdcDyZs.getNf() + bdcDyZs.getZhlsh());
            bdcZs.setBh(bdcDyZs.getBh());
            bdcZs.setCzr(bdcDyZs.getCzr());
            bdcZs.setCzrq(bdcDyZs.getCzrq());
            bdcZs.setDwdm(bdcDyZs.getDwdm());
            bdcZs.setFzrq(bdcDyZs.getFzrq());
            bdcZs.setNf(bdcDyZs.getNf());
            bdcZs.setSqsjc(bdcDyZs.getSqsjc());
            bdcZs.setQlqtzk(bdcDyZs.getQlqtzk());
            bdcZs.setSzsxqc(bdcDyZs.getSzsxqc());
            bdcZs.setZhlsh(bdcDyZs.getZhlsh());
            bdcZs.setZslx(bdcDyZs.getZslx());
            bdcZs.setZstype(bdcDyZs.getZstype());
            bdcZs.setZsid(bdcDyZs.getZsid());
            bdcZs.setSyqx(bdcDyZs.getSyqx());
            bdcZs.setQlxz(bdcDyZs.getQlxz());
            bdcZs.setQllx(bdcDyZs.getQllx());
            bdcZs.setQlr(bdcDyZs.getQlr());
            bdcZs.setMj(bdcDyZs.getMj());
            bdcZs.setFj(bdcDyZs.getFj());
            bdcZs.setYwr(bdcDyZs.getYwr());
            bdcZs.setYt(bdcDyZs.getYt());
            bdcZs.setBdcdyh(bdcDyZs.getBdcdyh());
            bdcZs.setGyqk(bdcDyZs.getGyqk());
            bdcZs.setZmqlsx(bdcDyZs.getZmqlsx());
            bdcZs.setZl(bdcDyZs.getZl());
            bdcZs.setEwmnr(bdcDyZs.getEwmnr());

        }
        return bdcZs;
    }

    @Override
    public List<BdcZs> getPlZsByProid(final String proid) {
        return bdcZsMapper.getPlZsByProid(proid);
    }

    @Override
    public List<BdcZs> getPlZsByWiid(final String wiid) {
        return StringUtils.isNotBlank(wiid) ? bdcZsMapper.getPlZsByWiid(wiid) : null;
    }

    @Override
    public List<Map> getPlZs(Map map) {
        return bdcZsMapper.getPlZs(map);
    }

    @Override
    public void saveBdcZsBh(BdcZsbh bdcZsbh) {
        entityMapper.saveOrUpdate(bdcZsbh, bdcZsbh.getZsbhid());
    }

    @Override
    public String getYzhByProidAndBdcid(final String proid, final String bdcdyid) {
        HashMap map = new HashMap();
        map.put(ParamsConstants.PROID_LOWERCASE, proid);
        map.put("bdcdyid", bdcdyid);
        return bdcZsMapper.getYzhByProidAndBdcid(map);
    }

    @Override
    public String getYzh(BdcXm bdcXm, String bdcdyid) {
        String ybdcqzh = "";
        if (bdcXm != null && StringUtils.isNotBlank(bdcdyid)) {
            ybdcqzh = getYzhByProidAndBdcid(bdcXm.getProid(), bdcdyid);
        }
        return ybdcqzh;
    }

    public List<BdcZs> creatDyBdcqz(BdcXm bdcXm, List<BdcQlr> bdcQlrList, Boolean previewZs) {
        //zx创建证书时先删除证书和权利人和证书关系
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            delBdcZsByProid(bdcXm.getProid());
        }
        //zwq (涟水)换证登记保留不动产权证号的功能
        Boolean ishz = false;
        List<BdcZs> bdcZsList = null;
        String sysVersion = AppConfig.getProperty(CONFIGURATION_PARAMETER_SYS_VERSION);
        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_LS)) {
            if (bdcXm != null && StringUtils.equals(bdcXm.getBlyzh(), "是")) {
                ishz = true;
            }
            bdcZsList = getYbdcqz(bdcXm);
        }
        List<BdcZs> list = new ArrayList<BdcZs>();
        synchronized (this) {
            if (bdcXm != null && "是".equals(bdcXm.getSqfbcz())) {
                // 生成多本证书
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for (BdcQlr bdcQlr : bdcQlrList) {
                        BdcDyZs bdcDyZs = creatBdcZs(bdcXm, bdcQlr.getQlrmc(), null, previewZs);
                        if (bdcDyZs == null)
                            continue;
                        bdcDyZs.setZsid(UUIDGenerator.generate18());
                        //zwq (涟水)通过持证人判断证书号
                        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_LS) && ishz && CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                if (StringUtils.equals(bdcQlr.getQlrmc(), bdcZs.getCzr()) && StringUtils.isNotBlank(bdcZs.getBdcqzh()))
                                    bdcDyZs.setBdcqzh(bdcZs.getBdcqzh());
                            }
                        }

                        BdcZs bdcZs = getBdcZsFromBdcDyZs(bdcDyZs);
                        entityMapper.insertSelective(bdcZs);

                        BdcZs bdcZs1 = getBdcZsFromBdcDyZs(bdcDyZs);
                        list.add(bdcZs1);
                    }
                }
            } else {
                StringBuilder czr = new StringBuilder();
                if (bdcQlrList != null) {
                    if (bdcQlrList.size() == 1) {
                        czr.append(bdcQlrList.get(0).getQlrmc());
                    } else if (bdcQlrList.size() > 1) {
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            if (StringUtils.isBlank(czr)) {
                                if (StringUtils.isNotBlank(bdcQlr.getQlrmc()))
                                    czr.append(bdcQlr.getQlrmc());
                            } else {
                                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                    czr.append("、").append(bdcQlr.getQlrmc());
                                }
                            }
                        }
                    }

                }
                String bdcZsId = "";
                String bdcqzh = "";

                BdcDyZs bdcZs = creatBdcZs(bdcXm, czr.toString(), bdcqzh, previewZs);
                System.out.println("不动产权证号:" + bdcZs.getBdcqzh());
                bdcZsId = UUIDGenerator.generate18();
                bdcZs.setZsid(bdcZsId);
                //zwq (涟水)替换为原证号
                if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_LS) && ishz && CollectionUtils.isNotEmpty(bdcZsList)
                        && StringUtils.isNotBlank(bdcZsList.get(0).getBdcqzh())) {
                    bdcZs.setBdcqzh(bdcZsList.get(0).getBdcqzh());
                }
                BdcZs bdcZs1 = getBdcZsFromBdcDyZs(bdcZs);
                entityMapper.insertSelective(bdcZs1);
                list.add(bdcZs1);
            }
        }
        return list;

    }


    public BdcDyZs getZsQlqtzView(BdcXm bdcXm, BdcDyZs bdcZs) {
        try {
            if (bdcXm.getSqlx() != null) {
                //获取权利
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                if (qllxVo != null && StringUtils.isNotEmpty(qllxVo.getQlqtzk())) {
                    bdcZs.setQlqtzk(qllxVo.getQlqtzk());
                } else {
                    BdcXtQlqtzkConfig bdcXtQlqtzkConfig = new BdcXtQlqtzkConfig();
                    bdcXtQlqtzkConfig.setSqlxdm(bdcXm.getSqlx());
                    BdcBdcdy bdcBdcdy = null;
                    if (StringUtils.isNotBlank(bdcXm.getBdcdyid()))
                        bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                    if (StringUtils.isNotBlank(bdcXm.getProid())) {
                        qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());

                    }
                    //房地产权添加fwlx过滤
                    if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyfwlx()))
                        bdcXtQlqtzkConfig.setQllxzlx(bdcBdcdy.getBdcdyfwlx());

                    //获取模板
                    List<BdcXtQlqtzkConfig> listQlqtzk = bdcXtQlqtzkConfigService.getQlqtzk(bdcXtQlqtzkConfig);
                    if (CollectionUtils.isNotEmpty(listQlqtzk)) {
                        for (BdcXtQlqtzkConfig qlqtzkConfig : listQlqtzk) {
                            //zhouwanqing 若配置权利类型代码则需要根据权利类型来过滤
                            if ((StringUtils.isBlank(qlqtzkConfig.getQllxdm()) || StringUtils.equals(qlqtzkConfig.getQllxdm(), bdcXm.getQllx())) && qllxVo != null) {
                                bdcZs.setQlqtzk(bdcXtQlqtzkConfigService.replaceMb(qlqtzkConfig.getQlqtzkmb(), qlqtzkConfig.getQtdb(), bdcXm, bdcBdcdy));
                                if (StringUtils.isNotBlank(qllxVo.getFj()))
                                    qllxVo.setFj(qllxVo.getFj() + "\\n" + bdcXtQlqtzkConfigService.replaceMb(qlqtzkConfig.getFjmb(), qlqtzkConfig.getFjdb(), bdcXm, bdcBdcdy));
                                else
                                    qllxVo.setFj(bdcXtQlqtzkConfigService.replaceMb(qlqtzkConfig.getFjmb(), qlqtzkConfig.getFjdb(), bdcXm, bdcBdcdy));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("BdcZsServiceImpl.getZsQlqtzView", e);
        }
        return bdcZs;
    }


    public String getZsFjView(BdcXm bdcXm) {
        String fj = "";
        try {
            if (bdcXm.getSqlx() != null) {
                //获取权利
                QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);

                BdcXtQlqtzkConfig bdcXtQlqtzkConfig = new BdcXtQlqtzkConfig();
                bdcXtQlqtzkConfig.setSqlxdm(bdcXm.getSqlx());
                BdcBdcdy bdcBdcdy = null;
                if (StringUtils.isNotBlank(bdcXm.getBdcdyid()))
                    bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                    qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());

                }
                //房地产权添加fwlx过滤
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyfwlx()))
                    bdcXtQlqtzkConfig.setQllxzlx(bdcBdcdy.getBdcdyfwlx());

                //获取模板
                List<BdcXtQlqtzkConfig> listQlqtzk = bdcXtQlqtzkConfigService.getQlqtzk(bdcXtQlqtzkConfig);
                if (CollectionUtils.isNotEmpty(listQlqtzk)) {
                    for (BdcXtQlqtzkConfig qlqtzkConfig : listQlqtzk) {
                        //zhouwanqing 若配置权利类型代码则需要根据权利类型来过滤
                        if (StringUtils.isBlank(qlqtzkConfig.getQllxdm()) || StringUtils.equals(qlqtzkConfig.getQllxdm(), bdcXm.getQllx())) {
                            String replaceMb = bdcXtQlqtzkConfigService.replaceMb(qlqtzkConfig.getFjmb(), qlqtzkConfig.getFjdb(), bdcXm, bdcBdcdy);
                            if (qllxVo != null && StringUtils.isNotBlank(qllxVo.getFj())) {
                                if (StringUtils.isNotBlank(replaceMb))
                                    fj = qllxVo.getFj() + "\\n" + replaceMb;
                                else
                                    fj = qllxVo.getFj();
                            } else
                                fj = bdcXtQlqtzkConfigService.replaceMb(qlqtzkConfig.getFjmb(), qlqtzkConfig.getFjdb(), bdcXm, bdcBdcdy);
                        }
                    }
                } else if (qllxVo != null && StringUtils.isNotBlank(qllxVo.getFj())) {
                    fj = qllxVo.getFj();
                }
            }
        } catch (Exception e) {
            logger.error("BdcZsServiceImpl.getZsFjView", e);
        }
        return fj;
    }

    @Override
    public void deleteHblcYbdcqzh(final String proid) {
        boolean ishblc = false;
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> bdcxmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcxmList)) {
                String sqlx = bdcxmList.get(0).getSqlx();
                for (BdcXm bdcXm1 : bdcxmList) {
                    if (!StringUtils.equals(sqlx, bdcXm1.getSqlx()))
                        ishblc = true;
                }
                for (BdcXm bdcXm1 : bdcxmList) {
                    if (ishblc && (StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_YG_YGSPFDY) || StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_FWDY_DM) || StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_FWDY_XS_DM))) {
                        bdcXm1.setYbdcqzh("");
                        entityMapper.saveOrUpdate(bdcXm1, bdcXm1.getProid());
                    }
                }
            }
        }
    }

    @Override
    public String getTdzhByQlid(final String qlid) {
        return StringUtils.isNotBlank(qlid) ? bdcZsMapper.getTdzhByQlid(qlid) : "";
    }

    @Override
    public String getFczhByQlid(final String qlid) {
        return StringUtils.isNotBlank(qlid) ? bdcZsMapper.getFczhByQlid(qlid) : "";
    }

    @Override
    public void delBdcZsBdcqzhAndZsbhByProid(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            List<BdcZs> bdcZsList = getPlZsByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    bdcZs.setBdcqzh("");
                    bdcZs.setNf("");
                    bdcZs.setZhlsh("");
                    bdcZs.setBh("");
                    bdcZs.setBdcqzhjc("");
                    entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                }
            }
        }
    }

    @Override
    public void updateDyzt(Map map) {
        bdcZsMapper.updateDyzt(map);
    }

    @Override
    public BdcZs creatBdcqzCrossSharingMode(BdcXm bdcXm, BdcQlr bdcQlr, String userId, Boolean previewZs) {
        BdcZs bdcZs1 = null;
        synchronized (this) {
            StringBuilder czr = new StringBuilder(bdcQlr.getQlrmc());
            String bdcZsId = "";
            String bdcqzh = "";
            BdcDyZs bdcZs = creatBdcZsArbitrary(bdcXm, czr.toString(), bdcqzh, userId, previewZs);
            bdcZsId = UUIDGenerator.generate18();
            bdcZs.setZsid(bdcZsId);
            bdcZs1 = getBdcZsFromBdcDyZs(bdcZs);
            entityMapper.insertSelective(bdcZs1);
        }
        return bdcZs1;
    }

    @Override
    public BdcZs creatBdcqzArbitrary(BdcXm bdcXm, BdcQlr bdcQlr, String userId) {
        BdcZs bdcZs1 = null;
        synchronized (this) {
            StringBuilder czr = new StringBuilder(bdcQlr.getQlrmc());
            String bdcZsId = "";
            String bdcqzh = "";
            BdcDyZs bdcZs = creatBdcZsArbitrary(bdcXm, czr.toString(), bdcqzh, userId, null);
            bdcZsId = UUIDGenerator.generate18();
            bdcZs.setZsid(bdcZsId);
            bdcZs1 = getBdcZsFromBdcDyZs(bdcZs);
            entityMapper.insertSelective(bdcZs1);
        }
        return bdcZs1;
    }

    @Override
    public BdcDyZs creatBdcZsArbitrary(BdcXm bdcXm, String czr, String bdcqzh, String userId, Boolean previewZs) {
        BdcDyZs bdcDyZs = creatBdcZsArbitrary(bdcXm, czr, bdcqzh, previewZs);
        if (!previewZs) {
            String bhVersion = AppConfig.getProperty(CONFIGURATION_PARAMETER_SCDJXXB_BH_VERSION);
            if (bdcDyZs != null && StringUtils.equals(bdcDyZs.getZstype(), Constants.BDCQSCDJZ_BH_FONT) && StringUtils.equals(bhVersion, "sz")) {
                //不动产首次信息表添加编号
                String nf = CalendarUtil.getCurrYear();
                String qh = bdcLshService.getQh(userId);
                String lsh = bdcLshService.getLsh(Constants.BHLX_BDCSCXXB_MC, nf, qh);
                String bh = bdcLshService.getBh(Constants.BHLX_BDCSCXXB_MC, nf, qh, lsh);
                if (StringUtils.isNotBlank(bh)) {
                    bdcDyZs.setZhlsh(lsh);
                    bdcDyZs.setBh(bh);
                    bdcDyZs.setBdcqzh(bh);
                    bdcDyZs.setBdcqzhjc(bdcDyZs.getNf() + bdcDyZs.getZhlsh());
                }
            }
        }
        return bdcDyZs;
    }

    @Override
    public BdcDyZs creatBdcZsArbitrary(BdcXm bdcXm, String czr, String bdcqzh, Boolean previewZs) {
        BdcDyZs bdcDyZs = new BdcDyZs();
        if (bdcXm != null) {
//            zx判断提前生成证书
            //重新生成其他权利状况
            bdcDyZs = getZsQlqtzk(bdcXm, new BdcDyZs());
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
            List<BdcFdcq> bdcFdcqList = bdcFdcqSeice.getBdcFdcq(param);
            //生成证号
            if (!previewZs) {
                if (StringUtils.isBlank(bdcqzh)) {
                    BdcZs bdcZs = bdczsBhService.creatBdcqzBh(bdcXm, bdcDyZs, 0, null);
                    bdcDyZs = new BdcDyZs();
                    try {
                        BeanUtils.copyProperties(bdcDyZs, bdcZs);
                    } catch (IllegalAccessException e) {
                        logger.error("BdcZsServiceImpl.creatBdcZsArbitrary", e);
                    } catch (InvocationTargetException e) {
                        logger.error("BdcZsServiceImpl.creatBdcZsArbitrary", e);
                    }
                } else {
                    bdcDyZs.setBdcqzh(bdcqzh);
                }
            }

            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            String zsFont = qllxService.makeSureBdcqzlx(qllxVo);
            if ((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM)) &&
                    CollectionUtils.isNotEmpty(bdcFdcqList)) {
                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                String fzlx = bdcFdcq.getFzlx();
                //首次信息表表变更登记，选择不动产单元，自动生成发证类型（信息表）
                if (StringUtils.isBlank(fzlx) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM)) {
                    bdcFdcq.setFzlx(Constants.FZLX_FZM);
                    entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                }
                if (StringUtils.equals(fzlx, Constants.FZLX_FZM)) {
                    String zsFont1 = AppConfig.getProperty("spfscdj.zstype");
                    if (StringUtils.isNotBlank(zsFont1) && (StringUtils.equals(zsFont1, Constants.BDCQSCDJZ_BH_FONT))) {
                        zsFont = zsFont1;
                    }
                }
            }
            bdcDyZs.setZstype(zsFont);
            //不动产权证号配置到各乡镇，各乡镇可能不同可能相同,就需要将该项目办理乡镇的行政代码存储到bdcZs里的dwdm
            String bdcqzhFilterZhXzdm = AppConfig.getProperty("bdcqzh.filterZh.xzdm");
            if (StringUtils.equals(bdcqzhFilterZhXzdm, "true"))
                bdcDyZs.setDwdm(bdcXm.getSsxz());
            else
                bdcDyZs.setDwdm(bdcXm.getDwdm());
            bdcDyZs.setZslx(bdcXm.getQllx());
            bdcDyZs.setCzr(czr);
            if (!previewZs) {
                bdcDyZs.setCzrq(CommonUtil.getCurrDate());
            }

            //补充增加证书信息字段
            //qlr
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
            //苏州交叉共有情况下生成一本证时权利人设置为全部权利人
            if (StringUtils.equals(AppConfig.getProperty("dwdm"), "320500")) {
                int czrCount = 0;
                StringBuilder qlr = new StringBuilder();
                StringBuilder gyCzr = new StringBuilder(czr);
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for (BdcQlr bdcQlr : bdcQlrList) {
                        if (StringUtils.isNotBlank(qlr)) {
                            qlr.append(" ").append(bdcQlr.getQlrmc());
                        } else {
                            qlr.append(bdcQlr.getQlrmc());
                        }

                        if (StringUtils.equals(bdcQlr.getSfczr(), Constants.CZR)) {
                            czrCount++;
                        }
                        // 交叉共有情况下，只有共有人不是持证人的情况下才把共用人和持证人合并存入权利人
                        if (StringUtils.isNotBlank(bdcQlr.getQlrmc()) && StringUtils.equals(czr, bdcQlr.getQlrmc()) && StringUtils.isNotBlank(bdcQlr.getQygyr())) {
                            for (BdcQlr bdcQlrTemp : bdcQlrList) {
                                if (StringUtils.equals(bdcQlrTemp.getQlrmc(), bdcQlr.getQygyr()) && !StringUtils.equals(bdcQlrTemp.getSfczr(), Constants.CZR)) {
                                    gyCzr.append(" ").append(bdcQlr.getQygyr());
                                }
                            }
                        }
                    }
                }
                if (czrCount == 1) {
                    bdcDyZs.setQlr(qlr.toString());
                } else {
                    bdcDyZs.setQlr(gyCzr.toString());
                }
            } else {
                bdcDyZs.setQlr(czr);
            }
            //qijiadong 获取qlqtzk的其余共有人
            if ((CollectionUtils.isEmpty(bdcFdcqList) || !StringUtils.equals(bdcFdcqList.get(0).getFzlx(), Constants.FZLX_FZM)) &&
                    StringUtils.isNotBlank(bdcXm.getBdcdyid()) && !cn.gtmap.estateplat.utils.CommonUtil.indexOfStrs(Constants.QLLX_WCZR, bdcXm.getQllx())) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null && (StringUtils.equals(bdcBdcdy.getBdcdyfwlx(), Constants.DJSJ_FWHS_DM) || StringUtils.equals(bdcBdcdy.getBdcdyfwlx(), Constants.DJSJ_FWDZ_DM))) {
                    bdcZsCreatZsInfoService.setQygyr(bdcXm, czr, bdcDyZs);
                    if (!StringUtils.equals(AppConfig.getProperty("dwdm"), "320500")) {
                        bdcZsCreatZsInfoService.setCzr(bdcXm, czr, bdcDyZs);
                    }
                }
            }
            //ywr
            List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                StringBuilder ywr = new StringBuilder();
                for (BdcQlr bdcQlr : bdcYwrList) {
                    if (StringUtils.isNotBlank(ywr)) {
                        ywr.append(" ").append(bdcQlr.getQlrmc());
                    } else {
                        ywr.append(bdcQlr.getQlrmc());
                    }
                }
                bdcDyZs.setYwr(ywr.toString());
            }
            HashMap map = Maps.newHashMap();
            //只生成一本证书的情况根据wiid查询
            if (cn.gtmap.estateplat.utils.CommonUtil.indexOfStrs(Constants.DJLX_YBZS_SQLXDM, bdcXm.getSqlx())) {
                map.put("onezsproid", bdcXm.getProid());
            } else {
                map.put(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
            }
            List<HashMap> bdczsViewList = bdcZsMapper.getViewBdcqzList(map);
            String fj = "";
            if (CollectionUtils.isNotEmpty(bdczsViewList)) {
                fj = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("FJ"));
                for (HashMap bdczsView : bdczsViewList) {
                    String tempFj = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsView.get("FJ"));
                    if (StringUtils.isNotBlank(fj) && !StringUtils.contains(fj, tempFj)) {
                        fj += "\n" + tempFj;
                    }
                }
                qllxVo = qllxService.queryQllxVo(bdcXm);
                if (qllxVo != null) {
                    if (qllxVo instanceof BdcFdcq) {
                        if (StringUtils.isNotBlank(((BdcFdcq) qllxVo).getFsss())) {
                            fj += "\n" + ((BdcFdcq) qllxVo).getFsss();
                        }
                    } else if (qllxVo instanceof BdcFdcqDz) {
                        List<BdcFwfzxx> bdcFwfzxxList = bdcFwfzxxService.getBdcFwfzxxListByQlid(qllxVo.getQlid());
                        String fzxxFj = "";
                        if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
                            for (BdcFwfzxx bdcFwfzxx : bdcFwfzxxList) {
                                if (StringUtils.isNotBlank(bdcFwfzxx.getFsss())) {
                                    if (StringUtils.isNotBlank(fzxxFj)) {
                                        fzxxFj += "," + bdcFwfzxx.getFsss();
                                    } else {
                                        fzxxFj = bdcFwfzxx.getFsss();
                                    }
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(fzxxFj)) {
                            fj += "\n" + fzxxFj;
                        }
                    }
                }
            }
            if (StringUtils.equals(bdcDyZs.getZstype(), Constants.BDCQZM_BH_FONT)) {
                //不动产权证明
                //zl、bdcdyh、mj、syqx
                if (CollectionUtils.isNotEmpty(bdczsViewList)) {
                    bdcDyZs.setZl(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZL")));
                    bdcDyZs.setBdcdyh(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)));
                    bdcDyZs.setFj(fj);
                }
                bdcDyZs.setZmqlsx(bdcXm.getQllx());
            } else {
                //gyqk
                if (CollectionUtils.isNotEmpty(bdcQlrList) && StringUtils.isNotBlank(bdcQlrList.get(0).getGyfs())) {
                    List<String> gyfsList = new ArrayList<String>();
                    for (BdcQlr bdcQlr : bdcQlrList) {
                        if (!gyfsList.contains(bdcQlr.getGyfs()))
                            gyfsList.add(bdcQlr.getGyfs());
                    }
                    if (CollectionUtils.isNotEmpty(gyfsList) && gyfsList.size() == 2 && gyfsList.contains("2") && gyfsList.contains("1")) {
                        bdcDyZs.setGyqk("2");
                    } else {
                        bdcDyZs.setGyqk(bdcQlrList.get(0).getGyfs());
                    }

                } else {
                    bdcDyZs.setGyqk(Constants.GYFS_DDGY_DM);
                }
                //zl、bdcdyh、mj、syqx
                if (CollectionUtils.isNotEmpty(bdczsViewList)) {
                    String zdMj = "";
                    if (StringUtils.equals(Constants.SQLX_SPFGYSCDJ_CYBZ_DM, bdcXm.getSqlx())) {
                        bdcDyZs.setZl(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZL")) + "等");
                        bdcDyZs.setBdcdyh(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)) + "等");
                        Double fwmj = 0.0;
                        for (HashMap hashMap : bdczsViewList) {
                            String dzwmj = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(hashMap.get("DZWMJ"));
                            if (StringUtils.isNotBlank(dzwmj)) {
                                fwmj=new BigDecimal(fwmj).add(new BigDecimal(Double.parseDouble(dzwmj))).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                            }
                        }
                        zdMj = getYzdfZsmj(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZDZHMJ")), fwmj.toString());
                        bdcDyZs.setMj(zdMj);
                    } else {
                        bdcDyZs.setZl(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZL")));
                        bdcDyZs.setBdcdyh(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get(ParamsConstants.BDCDYH_CAPITAL)));
                        String zdLb = bdcTdService.getZdLb(bdcXm.getProid());
                        //jyl 南通市证书面积显示方式太特殊

                        String sysVersion = AppConfig.getProperty(CONFIGURATION_PARAMETER_SYS_VERSION);
                        if (StringUtils.equals(sysVersion, Constants.SYS_VERSION_NT)) {
                            zdMj = bdcTdService.changeMjByNt(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZSMJ")), bdcXm.getBdclx(), bdcXm.getProid());
                        } else {
                            zdMj = bdcTdService.changeMjByZdLb(zdLb, cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("ZSMJ")), bdcXm.getBdclx());
                        }

                        //针对宅基地证书的面积，做特殊处理
                        String zjdzspzmj = AppConfig.getProperty("zjdzs.sfpzmj.enable");
                        String bdcdyh = bdcdyService.getBdcdyhByProid(bdcXm.getProid());
                        if ((StringUtils.equals("true", zjdzspzmj) && StringUtils.isNoneBlank(bdcdyh) && (bdcdyh.indexOf("JC") != -1)) || (StringUtils.isNotBlank(bdcXm.getSqlx()) && cn.gtmap.estateplat.utils.CommonUtil.indexOfStrs(Constants.SQLX_ZJDMJ, bdcXm.getSqlx()))) {
                            zdMj = getZjdzsmj(bdcXm.getProid(), bdcXm.getBdclx(), zdMj);
                        }

                        bdcDyZs.setMj(zdMj);

                    }
                    bdcDyZs.setQlxz(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("QLXZ")));
                    bdcDyZs.setYt(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("YT")));

                    String djsy = bdcXm.getDjsy();
                    String syksqx = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYKSQX"));
                    String syjsqx = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdczsViewList.get(0).get("SYJSQX"));
                    String djsymc = "";
                    String syqx = "";
                    if (StringUtils.isNotBlank(djsy) && StringUtils.length(djsy) > 0 && djsy.indexOf('/') > -1)
                        djsymc = bdcZdGlMapper.getDjsyByDm(djsy.substring(0, djsy.indexOf('/')));
                    else if (StringUtils.isNotBlank(djsy))
                        djsymc = bdcZdGlMapper.getDjsyByDm(djsy);
                    if (StringUtils.isNotBlank(syjsqx)) {
                        syqx = djsymc + " " + CalendarUtil.formateToStrChinaYMDDate(CalendarUtil.formatDate(syjsqx)) + "止";
                    }
                    bdcDyZs.setSyqx(syqx);
                    bdcDyZs.setFj(fj);
                }
            }

            bdcDyZs.setBdcqzhjc(bdcDyZs.getNf() + bdcDyZs.getZhlsh());
            bdcDyZs.setQllx(bdcXm.getQllx());
            bdcDyZs.setEwmnr(bdcDyZs.getBdcqzh());
        }

        return bdcDyZs;
    }

    private String getYzdfZsmj(String zdzhmj, String fwmj) {
        StringBuilder yzdfMj = new StringBuilder();
        yzdfMj.append(Constants.YZDFMJ_TDFW[0].replace("@zdzhmj", zdzhmj));
        yzdfMj.append(Constants.YZDFMJ_TDFW[1].replace("@fwmj", fwmj));
        return yzdfMj.toString();
    }

    @Override
    public void delBdcZsByWiid(final String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    List<BdcXmzsRel> bdcXmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmzsRelList)) {
                        for (BdcXmzsRel bdcXmzsRel : bdcXmzsRelList) {
                            if (bdcXmzsRel != null && StringUtils.isNotBlank(bdcXmzsRel.getZsid())) {
                                bdcZsQlrRelService.delZsQlrRelByZsid(bdcXmzsRel.getZsid());
                                delBdcZsByZsid(bdcXmzsRel.getZsid());
                            }
                        }
                    }
                    bdcXmZsRelService.delBdcXmZsRelByProid(bdcXm.getProid());
                }
            }
        }
    }

    @Override
    public String getYtdbdcqzhByZdbdcdyh(String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcZsMapper.getYtdbdcqzhByZdbdcdyh(bdcdyh) : "";
    }

    @Override
    public String getYtdzhByZdbdcdyh(String bdcdyh) {
        return bdcZsMapper.getYtdzhByZdbdcdyh(bdcdyh);
    }

    @Override
    public String getProidByBdcqzh(String bdcqzh) {
        if (StringUtils.isNotBlank(bdcqzh)) {
            Example example = new Example(BdcZs.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCQZH_LOWERCASE, bdcqzh);
            List<BdcZs> bdcZsList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                String zsid = bdcZsList.get(0).getZsid();
                return bdcXmZsRelService.getProidByZsid(zsid);
            }
        }
        return null;
    }

    @Override
    public String getCombineBdcqzhByProid(String proid) {
        StringBuilder ybdcqzhs = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            List<BdcZs> bdcZsList = queryBdcZsByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    if (StringUtils.isBlank(ybdcqzhs)) {
                        ybdcqzhs.append(bdcZs.getBdcqzh());
                    } else {
                        ybdcqzhs.append(",").append(bdcZs.getBdcqzh());
                    }
                }
            }
        }
        return ybdcqzhs.toString();
    }

    @Override
    public Integer getBdcdyCountByZsid(String zsid) {
        return bdcZsMapper.getBdcdyCountByZsid(zsid);
    }


    @Override
    public BdcZs queryBdcZsByBdcqzh(String bdcqzh) {
        BdcZs bdcZs = null;
        if (StringUtils.isNotBlank(bdcqzh)) {
            Example example = new Example(BdcZs.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCQZH_LOWERCASE, bdcqzh);
            List<BdcZs> bdcZsList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcZsList))
                bdcZs = bdcZsList.get(0);
        }
        return bdcZs;
    }

    @Override
    public String getProidByBdczqh(String bdcqzh) {
        return bdcZsMapper.getProidByBdczqh(bdcqzh);
    }

    @Transactional
    @Override
    public void batchDelBdcZs(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            List<BdcZs> bdcZsList = getPlZsByWiid(wiid);

            List<BdcDelZszh> bdcDelZszhArrayList = new ArrayList<BdcDelZszh>();
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    if (bdcZs != null && StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                        HashMap map = new HashMap();
                        map.put(ParamsConstants.BDCQZH_LOWERCASE, bdcZs.getBdcqzh());
                        map.put("dwdm", bdcZs.getDwdm());
                        map.put("nf", bdcZs.getNf());
                        map.put("isuse", "0");
                        List<BdcDelZszh> bdcDelZszhList = bdcDelZszhMapper.getBdcDelZszhList(map);
                        if (CollectionUtils.isEmpty(bdcDelZszhList)) {
                            BdcDelZszh bdcDelZszh = getBdcDelZszhFromBdcZs(null, bdcZs);
                            bdcDelZszhArrayList.add(bdcDelZszh);
                        }
                    }
                }
            }

            //批量操作数据库
            batchInsertBdcDelZszhByBdcDelZszhList(bdcDelZszhArrayList);
            batchDelBdcZsByBdcZsList(bdcZsList);
            bdcZsQlrRelService.batchDelBdcZsQlrRelByBdcZsList(bdcZsList);
            batchDelBdcXmZsRelByBdcXmList(bdcXmList);
        }
    }

    @Transactional
    @Override
    public void batchDelBdcZsByBdcZsList(List<BdcZs> bdcZsList) {
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            bdcZsMapper.batchDelBdcZsByBdcZsList(bdcZsList);
        }
    }

    @Override
    public void batchDelBdcZsByZsidList(List<String> zsidList) {
        if (CollectionUtils.isNotEmpty(zsidList)) {
            bdcZsMapper.batchDelBdcZsByZsidList(zsidList);
        }
    }

    @Transactional
    @Override
    public void batchDelBdcXmZsRelByBdcXmList(List<BdcXm> bdcXmList) {
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            bdcZsMapper.batchDelBdcXmZsRelByBdcXmList(bdcXmList);
        }
    }

    @Transactional
    @Override
    public void batchInsertBdcDelZszhByBdcDelZszhList(List<BdcDelZszh> bdcDelZszhList) {
        if (CollectionUtils.isNotEmpty(bdcDelZszhList)) {
            bdcZsMapper.batchInsertBdcDelZszhByBdcDelZszhList(bdcDelZszhList);
        }
    }

    @Override
    public List<String> getZsidListByBdcXmList(List<BdcXm> bdcXmList) {
        List<String> zsidList = null;
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            zsidList = bdcZsMapper.getZsidListByBdcXmList(bdcXmList);
        }
        return zsidList;
    }

    @Override
    public void batchUpateBdcZsLzrqByzsidList(List<BdcXm> bdcXmList, Date lzrq) {
        List<String> zsidList = getZsidListByBdcXmList(bdcXmList);
        if (CollectionUtils.isNotEmpty(zsidList) && lzrq != null) {
            HashMap map = Maps.newHashMap();
            map.put("zsidList", zsidList);
            map.put("lzrq", lzrq);
            bdcZsMapper.batchUpateBdcZsLzrqByzsidList(map);
        }
    }

    @Override
    public HashMap getBdcXtConfigQlqtzkAndFj(BdcXm bdcXm) {
        HashMap result = Maps.newHashMap();
        String qlqtzk = "";
        String fj = "";
        if (null != bdcXm && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.isNoneBlank(bdcXm.getProid())) {
            try {
                BdcXtQlqtzkConfig bdcXtQlqtzkConfig = new BdcXtQlqtzkConfig();
                bdcXtQlqtzkConfig.setSqlxdm(bdcXm.getSqlx());
                BdcBdcdy bdcBdcdy = null;
                if (StringUtils.isNotBlank(bdcXm.getBdcdyid()))
                    bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                //房地产权添加fwlx过滤
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyfwlx()))
                    bdcXtQlqtzkConfig.setQllxzlx(bdcBdcdy.getBdcdyfwlx());
                //获取模板
                List<BdcXtQlqtzkConfig> bdcXtQlqtzkConfigList = bdcXtQlqtzkConfigService.getQlqtzk(bdcXtQlqtzkConfig);
                if (CollectionUtils.isNotEmpty(bdcXtQlqtzkConfigList)) {
                    for (BdcXtQlqtzkConfig bdcXtQlqtzkConfigTemp : bdcXtQlqtzkConfigList) {
                        qlqtzk = bdcXtQlqtzkConfigService.replaceMbUndisplayNull(bdcXtQlqtzkConfigTemp.getQlqtzkmb(), bdcXtQlqtzkConfigTemp.getQtdb(), bdcXm, bdcBdcdy);
                        fj = bdcXtQlqtzkConfigService.replaceMbUndisplayNull(bdcXtQlqtzkConfigTemp.getFjmb(), bdcXtQlqtzkConfigTemp.getFjdb(), bdcXm, bdcBdcdy);

                        if (StringUtils.isNotBlank(bdcXtQlqtzkConfigTemp.getQllxdm())) {
                            if (StringUtils.equals(bdcXm.getQllx(), bdcXtQlqtzkConfigTemp.getQllxdm())) {
                                break;
                            }
                        } else {
                            if (StringUtils.isNotBlank(qlqtzk) && StringUtils.isNotBlank(fj)) {
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                LoggerFactory.getLogger(getClass()).error("Unexpected error in getBdcXtConfigQlqtzk function ", e);
            }
        }
        result.put(ParamsConstants.QLQTZK_LOWERCASE, qlqtzk);
        result.put("fj", fj);
        return result;
    }


    @Override
    public void saveBdcXtConfigQlqtzkAndFj(BdcXm bdcXm) {
        if (bdcXm != null) {
            QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
            if (qllxVo != null) {
                //默认权利其他状况和附记在权利生成的时候取值和赋值
                HashMap result = getBdcXtConfigQlqtzkAndFj(bdcXm);
                if (result != null) {
                    if (StringUtils.isNotBlank(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(result.get(ParamsConstants.QLQTZK_LOWERCASE)))) {
                        qllxVo.setQlqtzk(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(result.get(ParamsConstants.QLQTZK_LOWERCASE)));
                    }
                    if (StringUtils.isNotBlank(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(result.get("fj")))) {
                        qllxVo.setFj(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(result.get("fj")));
                    }
                }
                if (qllxVo instanceof BdcJsydzjdsyq) {
                    BdcJsydzjdsyq bdcJsydzjdsyq = (BdcJsydzjdsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
                } else if (qllxVo instanceof BdcFdcq) {
                    BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                    entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                } else if (qllxVo instanceof BdcCf) {
                    BdcCf bdcCf = (BdcCf) qllxVo;
                    entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                } else if (qllxVo instanceof BdcDyaq) {
                    BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                    entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                } else if (qllxVo instanceof BdcDyq) {
                    BdcDyq bdcDyq = (BdcDyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcDyq, bdcDyq.getQlid());
                } else if (qllxVo instanceof BdcHysyq) {
                    BdcHysyq bdcHysyq = (BdcHysyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcHysyq, bdcHysyq.getQlid());
                } else if (qllxVo instanceof BdcJzwgy) {
                    BdcJzwgy bdcJzwgy = (BdcJzwgy) qllxVo;
                    entityMapper.saveOrUpdate(bdcJzwgy, bdcJzwgy.getQlid());
                } else if (qllxVo instanceof BdcJzwsyq) {
                    BdcJzwsyq bdcJzwsyq = (BdcJzwsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcJzwsyq, bdcJzwsyq.getQlid());
                } else if (qllxVo instanceof BdcLq) {             //林权
                    BdcLq bdcLq = (BdcLq) qllxVo;
                    entityMapper.saveOrUpdate(bdcLq, bdcLq.getQlid());
                } else if (qllxVo instanceof BdcQsq) {
                    BdcQsq bdcQsq = (BdcQsq) qllxVo;
                    entityMapper.saveOrUpdate(bdcQsq, bdcQsq.getQlid());
                } else if (qllxVo instanceof BdcTdcbnydsyq) {                                      //土地承包经营权
                    BdcTdcbnydsyq bdcTdcbnydsyq = (BdcTdcbnydsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcTdcbnydsyq, bdcTdcbnydsyq.getQlid());
                } else if (qllxVo instanceof BdcTdsyq) {         //土地所有权
                    BdcTdsyq bdcTdsyq = (BdcTdsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcTdsyq, bdcTdsyq.getQlid());
                } else if (qllxVo instanceof BdcYg) {
                    BdcYg bdcYg = (BdcYg) qllxVo;
                    entityMapper.saveOrUpdate(bdcYg, bdcYg.getQlid());
                } else if (qllxVo instanceof BdcYy) {
                    BdcYy bdcYy = (BdcYy) qllxVo;
                    entityMapper.saveOrUpdate(bdcYy, bdcYy.getQlid());
                } else if (qllxVo instanceof BdcFdcqDz) {
                    BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                    entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
                }
            }
        }
    }

    @Override
    public String getQlqtzkByReplaceBdcqzh(String qlqtzk, String bdcqzh) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(qlqtzk) && StringUtils.isNotBlank(bdcqzh)) {
            String[] qlqtzkArray = qlqtzk.split("\n");
            Boolean zmhExist = false;
            if (org.apache.commons.lang.StringUtils.indexOf(qlqtzk, Constants.BDCZMH_ZWMC) > -1) {
                zmhExist = true;
            }
            if (qlqtzkArray != null && qlqtzkArray.length > 0) {
                if (org.apache.commons.lang.StringUtils.indexOf(qlqtzk, Constants.BDCQZH_ZWMC) > -1 || org.apache.commons.lang.StringUtils.indexOf(qlqtzk, Constants.BDCZMH_ZWMC) > -1) {
                    for (String qlqtzkItem : qlqtzkArray) {
                        if (StringUtils.isNotBlank(qlqtzkItem)) {
                            if (StringUtils.indexOf(qlqtzkItem, Constants.BDCQZH_ZWMC) > -1 || StringUtils.indexOf(qlqtzkItem, Constants.BDCZMH_ZWMC) > -1) {
                                if (StringUtils.isNotBlank(stringBuilder.toString())) {
                                    if (zmhExist) {
                                        stringBuilder.append("\n").append(Constants.BDCZMH_ZWMC).append(":").append(bdcqzh);
                                    } else {
                                        stringBuilder.append("\n").append(Constants.BDCQZH_ZWMC).append(":").append(bdcqzh);
                                    }
                                } else {
                                    if (zmhExist) {
                                        stringBuilder.append(Constants.BDCZMH_ZWMC).append(":").append(bdcqzh);
                                    } else {
                                        stringBuilder.append(Constants.BDCQZH_ZWMC).append(":").append(bdcqzh);
                                    }
                                }

                            } else {
                                if (StringUtils.isNotBlank(stringBuilder.toString())) {
                                    stringBuilder.append("\n").append(qlqtzkItem);
                                } else {
                                    stringBuilder.append(qlqtzkItem);
                                }
                            }
                        }
                    }
                } else {
                    if (zmhExist) {
                        stringBuilder.append(Constants.BDCZMH_ZWMC).append(":").append(bdcqzh).append("\n").append(qlqtzk);
                    } else {
                        stringBuilder.append(Constants.BDCQZH_ZWMC).append(":").append(bdcqzh).append("\n").append(qlqtzk);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void completeZsInfo(BdcXm bdcXm, BdcZs bdcZs, String userId, String zsType) {
        synchronized (this) {
            bdcZs.setCzrq(CommonUtil.getCurrDate());
            BdcZs saveBdcZs = bdczsBhService.creatBdcqzBh(bdcXm, bdcZs, 0, zsType);
            if (saveBdcZs != null) {
                //qijiadong昆山需要在缮证时，将证号反填至bdc_xm表的bz字段中
                if (StringUtils.equals(AppConfig.getProperty("updata.bdcqzh.into.bdcXmBz"), ParamsConstants.TRUE_LOWERCASE)) {
                    if (StringUtils.isBlank(bdcXm.getBz())) {
                        bdcXm.setBz(saveBdcZs.getBdcqzh());
                    } else {
                        bdcXm.setBz(bdcXm.getBz() + "," + saveBdcZs.getBdcqzh());
                    }
                }
                bdcXmService.saveBdcXm(bdcXm);
                entityMapper.saveOrUpdate(saveBdcZs, saveBdcZs.getZsid());
            }
        }
    }

    @Override
    public void dealHbZsInfo(List<BdcZs> dyaqBdcZsList, List<BdcZs> cqBdcZsList, List<BdcXm> dyaqXmList, List<BdcXm> cqXmList) {
        if (CollectionUtils.isNotEmpty(dyaqBdcZsList) && CollectionUtils.isNotEmpty(cqBdcZsList) && CollectionUtils.isNotEmpty(cqXmList)) {
            BdcXm bdcXmTemp = cqXmList.get(0);
            String bh = StringUtils.isNotBlank(bdcXmTemp.getBh()) ? bdcXmTemp.getBh() : "";
            logger.error("进入dealHbZsInfo方法判断！xmbh: " + bh);
            HashMap bdcdyZsMap = new HashMap();
            String ybdcqzh = "";
            StringBuilder ybdcqzhBuilder = new StringBuilder();
            for (BdcXm cqBdcXm : cqXmList) {
                for (BdcZs bdcZs : cqBdcZsList) {
                    if (StringUtils.isNotBlank(ybdcqzhBuilder)) {
                        ybdcqzhBuilder.append(",").append(bdcZs.getBdcqzh());
                    } else {
                        ybdcqzhBuilder.append(bdcZs.getBdcqzh());
                    }
                }
                String ybdcqzhTemp = ybdcqzhBuilder.toString();
                if (StringUtils.isNotBlank(ybdcqzhTemp)) {
                    ybdcqzh = ybdcqzhTemp;
                }
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(cqBdcXm.getProid());
                if (StringUtils.isNoneBlank(bdcSpxx.getBdcdyh())) {
                    bdcdyZsMap.put(bdcSpxx.getBdcdyh(), ybdcqzh);
                }
            }
            for (BdcXm dyaqBdcXm : dyaqXmList) {
                //抵押证明的原证号是新产生的不动产权证
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(dyaqBdcXm.getProid());
                if (StringUtils.isNoneBlank(bdcSpxx.getBdcdyh())) {
                    ybdcqzh = cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(bdcdyZsMap.get(bdcSpxx.getBdcdyh()));
                }
                dyaqBdcXm.setYbdcqzh(ybdcqzh);
                if (dyaqBdcXm.getSqlx().equals(Constants.SQLX_FWDYBG_DM)) {
                    dyaqBdcXm.setYfczh(ybdcqzh);
                }
                entityMapper.saveOrUpdate(dyaqBdcXm, dyaqBdcXm.getProid());

                BdcDyaq bdcDyaq = bdcDyaqService.queryBdcDyaqByProid(dyaqBdcXm.getProid());
                String qlqtzk = null;
                if (bdcDyaq != null) {
                    qlqtzk = getQlqtzkByReplaceBdcqzh(bdcDyaq.getQlqtzk(), ybdcqzh);
                    if (StringUtils.isNotBlank(qlqtzk)) {
                        bdcDyaq.setQlqtzk(qlqtzk);
                        bdcDyaqService.saveBdcDyaq(bdcDyaq);
                    }
                } else {
                    BdcYg bdcYg = bdcYgService.getBdcYgByProid(dyaqBdcXm.getProid());
                    if (bdcYg != null) {
                        qlqtzk = getQlqtzkByReplaceBdcqzh(bdcYg.getQlqtzk(), ybdcqzh);
                        if (StringUtils.isNotBlank(qlqtzk)) {
                            bdcYg.setQlqtzk(qlqtzk);
                            bdcYgService.saveYgxx(bdcYg);
                        }
                    }
                }
                List<BdcZs> dyaqZsList = queryBdcZsByProid(dyaqBdcXm.getProid());
                if (CollectionUtils.isNotEmpty(dyaqZsList)) {
                    for (BdcZs dyaqZs : dyaqZsList) {
                        dyaqZs.setQlqtzk(qlqtzk);
                        entityMapper.saveOrUpdate(dyaqZs, dyaqZs.getZsid());
                    }
                }
            }
        }
    }

    @Override
    public List<BdcZs> getPlZsByMap(Map map) {
        List<BdcZs> bdcZsList = null;
        if (map != null && !map.isEmpty()) {
            bdcZsList = bdcZsMapper.getPlZsByMap(map);
        }
        return bdcZsList;
    }

    @Override
    public void saveZs(BdcZs bdcZs) {
        //jiangganzhi 冗余字段增加字段不保存
        Example example = new Example(BdcZs.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("zsid", bdcZs.getZsid());
        List<BdcZs> bdcZsList = entityMapper.selectByExample(BdcZs.class, example);
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            BdcZs bdczsTmp = bdcZsList.get(0);
            if (StringUtils.isBlank(bdczsTmp.getQlrzjh())) {
                bdcZs.setQlrzjh(null);
            } else {
                bdcZs.setQlrzjh(bdczsTmp.getQlrzjh());
            }

            if (StringUtils.isBlank(bdczsTmp.getQlrzjzl())) {
                bdcZs.setQlrzjzl(null);
            } else {
                bdcZs.setQlrzjzl(bdczsTmp.getQlrzjzl());
            }

            if (StringUtils.isBlank(bdczsTmp.getYwrzjh())) {
                bdcZs.setYwrzjh(null);
            } else {
                bdcZs.setYwrzjh(bdczsTmp.getYwrzjh());
            }

            if (StringUtils.isBlank(bdczsTmp.getYwrzjzl())) {
                bdcZs.setYwrzjzl(null);
            } else {
                bdcZs.setYwrzjzl(bdczsTmp.getYwrzjzl());
            }
        }

        entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
    }

    @Override
    public List<BdcZs> getAllBdcZsWithSameWiidByZsid(String zsid) {
        List<BdcZs> bdcZsList = null;
        if (StringUtils.isNotBlank(zsid)) {
            String wiid = getXmWiidByZsid(zsid);
            bdcZsList = getPlZsByWiid(wiid);
        }
        return bdcZsList;
    }

    @Override
    public String getXmWiidByZsid(String zsid) {
        return StringUtils.isNotBlank(zsid) ? bdcZsMapper.getXmWiidByZsid(zsid) : "";
    }

    @Override
    public List<BdcZs> createBdcZs(BdcXm bdcXm, String previewZs, String userid) {
        Boolean boolPreviewZs = false;
        if (StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs, "true")) {
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        HashMap czrMap = new HashMap();
        czrMap.put("proid", bdcXm.getProid());
        czrMap.put("sfczr", Constants.CZR);
        czrMap.put("qlrlx", Constants.QLRLX_QLR);
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        List<BdcQlr> czrList = bdcQlrService.queryBdcQlrList(czrMap);
        if (CollectionUtils.isNotEmpty(czrList)) {
            //jyl 每个持证人都要生成一本证
            for (BdcQlr bdcQlr : czrList) {
                //jyl 为多抵一预留
                List<BdcXm> bdcXmList = new ArrayList<BdcXm>();
                //jyl 列出该权利人下的其余共有人，非持证人肯定是权利人，为了生成后续的BdcZsQlrRel
                List<BdcQlr> gyrList = null;
                if (StringUtils.isNotBlank(bdcQlr.getQygyr())) {
                    HashMap gyrMap = new HashMap();
                    gyrMap.put("proid", bdcQlr.getProid());
                    gyrMap.put("qlrlx", Constants.QLRLX_QLR);
                    gyrMap.put("qygyr", bdcQlr.getQygyr());
                    gyrList = bdcQlrService.queryBdcQlrList(gyrMap);
                }
                BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(bdcQlr.getProid());
                bdcXmList.add(bdcXmTemp);
                BdcZs bdcZs = creatBdcqzCrossSharingMode(bdcXmTemp, bdcQlr, userid, boolPreviewZs);
                List<BdcZs> bdcZsListTemp = new ArrayList<BdcZs>();
                bdcZsList.add(bdcZs);
                if (bdcZs != null && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_CYBZ_DM)) {
                    bdcZsListTemp.add(bdcZs);

                    //生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRelArbitrary(bdcZs, bdcQlr, gyrList);
                    //生成项目证书关系表
                    bdcXmZsRelService.creatBdcXmZsRelArbitrary(bdcZs.getZsid(), bdcXmList);
                    //多个权利人，只有一个持证人的情况,生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRelForOtherQlrExceptCzr(bdcZs, czrList, bdcQlrList, bdcXm);
                }
            }
        }
        return bdcZsList;
    }

    @Override
    public void saveBdcXtConfigFj(BdcXm bdcXm) {
        if (bdcXm != null) {
            QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
            if (qllxVo != null) {
                HashMap result = getBdcXtConfigFj(bdcXm);
                if (result != null) {
                    if (StringUtils.isNotBlank(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(result.get("fj")))) {
                        if (!StringUtils.equalsIgnoreCase(String.valueOf(result.get("fj")), qllxVo.getFj())) {
                            qllxVo.setFj(cn.gtmap.estateplat.utils.CommonUtil.formatEmptyValue(result.get("fj")));
                        }
                    }
                }
                if (qllxVo instanceof BdcJsydzjdsyq) {
                    BdcJsydzjdsyq bdcJsydzjdsyq = (BdcJsydzjdsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
                } else if (qllxVo instanceof BdcFdcq) {
                    BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                    entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                } else if (qllxVo instanceof BdcCf) {
                    BdcCf bdcCf = (BdcCf) qllxVo;
                    entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                } else if (qllxVo instanceof BdcDyaq) {
                    BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                    entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                } else if (qllxVo instanceof BdcDyq) {
                    BdcDyq bdcDyq = (BdcDyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcDyq, bdcDyq.getQlid());
                } else if (qllxVo instanceof BdcHysyq) {
                    BdcHysyq bdcHysyq = (BdcHysyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcHysyq, bdcHysyq.getQlid());
                } else if (qllxVo instanceof BdcJzwgy) {
                    BdcJzwgy bdcJzwgy = (BdcJzwgy) qllxVo;
                    entityMapper.saveOrUpdate(bdcJzwgy, bdcJzwgy.getQlid());
                } else if (qllxVo instanceof BdcJzwsyq) {
                    BdcJzwsyq bdcJzwsyq = (BdcJzwsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcJzwsyq, bdcJzwsyq.getQlid());
                } else if (qllxVo instanceof BdcLq) {             //林权
                    BdcLq bdcLq = (BdcLq) qllxVo;
                    entityMapper.saveOrUpdate(bdcLq, bdcLq.getQlid());
                } else if (qllxVo instanceof BdcQsq) {
                    BdcQsq bdcQsq = (BdcQsq) qllxVo;
                    entityMapper.saveOrUpdate(bdcQsq, bdcQsq.getQlid());
                } else if (qllxVo instanceof BdcTdcbnydsyq) {          //土地承包经营权
                    BdcTdcbnydsyq bdcTdcbnydsyq = (BdcTdcbnydsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcTdcbnydsyq, bdcTdcbnydsyq.getQlid());
                } else if (qllxVo instanceof BdcTdsyq) {         //土地所有权
                    BdcTdsyq bdcTdsyq = (BdcTdsyq) qllxVo;
                    entityMapper.saveOrUpdate(bdcTdsyq, bdcTdsyq.getQlid());
                } else if (qllxVo instanceof BdcYg) {
                    BdcYg bdcYg = (BdcYg) qllxVo;
                    entityMapper.saveOrUpdate(bdcYg, bdcYg.getQlid());
                } else if (qllxVo instanceof BdcYy) {
                    BdcYy bdcYy = (BdcYy) qllxVo;
                    entityMapper.saveOrUpdate(bdcYy, bdcYy.getQlid());
                } else if (qllxVo instanceof BdcFdcqDz) {
                    BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                    entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
                }
            }
        }
    }

    @Override
    public HashMap getBdcXtConfigFj(BdcXm bdcXm) {
        HashMap result = Maps.newHashMap();
        String fj = "";
        if (null != bdcXm && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.isNoneBlank(bdcXm.getProid())) {
            try {
                BdcXtQlqtzkConfig bdcXtQlqtzkConfig = new BdcXtQlqtzkConfig();
                bdcXtQlqtzkConfig.setSqlxdm(bdcXm.getSqlx());
                BdcBdcdy bdcBdcdy = null;
                if (StringUtils.isNotBlank(bdcXm.getBdcdyid()))
                    bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                //房地产权添加fwlx过滤
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyfwlx()))
                    bdcXtQlqtzkConfig.setQllxzlx(bdcBdcdy.getBdcdyfwlx());
                //获取模板
                List<BdcXtQlqtzkConfig> bdcXtQlqtzkConfigList = bdcXtQlqtzkConfigService.getQlqtzk(bdcXtQlqtzkConfig);
                if (CollectionUtils.isNotEmpty(bdcXtQlqtzkConfigList)) {
                    for (BdcXtQlqtzkConfig bdcXtQlqtzkConfigTemp : bdcXtQlqtzkConfigList) {
                        fj = bdcXtQlqtzkConfigService.replaceMbUndisplayNull(bdcXtQlqtzkConfigTemp.getFjmb(), bdcXtQlqtzkConfigTemp.getFjdb(), bdcXm, bdcBdcdy);
                        if (StringUtils.isNotBlank(bdcXtQlqtzkConfigTemp.getQllxdm())) {
                            if (StringUtils.equals(bdcXm.getQllx(), bdcXtQlqtzkConfigTemp.getQllxdm())) {
                                break;
                            }
                        } else {
                            if (StringUtils.isNotBlank(fj)) {
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                LoggerFactory.getLogger(getClass()).error("Unexpected error in getBdcXtConfigQlqtzk function ", e);
            }
        }
        result.put("fj", fj);
        return result;
    }

    @Override
    public Integer getMaxScdjzLsh(final Map map) {
        return bdcZsMapper.getMaxScdjzLsh(map);
    }

    /**
     * @param proid 项目ID
     * @return 产权证号
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据项目ID获取产权证号
     */
    @Override
    public Map queryBdcqzhByProid(String proid) {
        Map zsmap = null;
        if (StringUtils.isNotBlank(proid)) {
            zsmap = bdcZsMapper.queryBdcqzhByProid(proid);
        }
        return zsmap;
    }

    @Override
    public List<BdcZs> queryBdcZsByBdcdyh(String bdcdyh) {
        return bdcZsMapper.queryBdcZsByBdcdyh(bdcdyh);
    }

    @Override
    public Map getInfoForCreateBdcqzh(BdcXm bdcXm) {
        Map map = new HashMap();
        if (bdcXm != null) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            String zsFont = qllxService.makeSureBdcqzlx(qllxVo);
            //lcl 从配置文件获取新增证书类型，只针对商品房及业主共有部分首次登记流程
            if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_CYBZ_DM)) {
                List<BdcFdcq> bdcFdcqLst = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqLst) && StringUtils.equals(bdcFdcqLst.get(0).getFzlx(), Constants.FZLX_FZM)) {
                    String zsFont1 = AppConfig.getProperty("spfscdj.zstype");
                    if (StringUtils.isNotBlank(zsFont1)) {
                        zsFont = zsFont1;
                    }
                }
            }
            if (StringUtils.isNotBlank(zsFont)) {
                map.put(bdcXm.getProid(), zsFont);
            }
        }
        return map;
    }

    /**
     * @param bdcZsList
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 批量更新证书
     */
    @Override
    public void updateBdcZs(List<BdcZs> bdcZsList) {
        if (CollectionUtils.isNotEmpty(bdcZsList)) {
            for (BdcZs bdcZs : bdcZsList) {
                entityMapper.updateByPrimaryKeySelective(bdcZs);
            }
        }
    }

    @Override
    public String getProidByBdcqzhjc(String bdcqzhjc, String zstype) {
        if (StringUtils.isNotBlank(bdcqzhjc)) {
            Example example = new Example(BdcZs.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("bdcqzhjc", bdcqzhjc);
            if (StringUtils.isNotBlank(zstype)) {
                criteria.andEqualTo("zstype", zstype);
            }
            List<BdcZs> bdcZsList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                String zsid = bdcZsList.get(0).getZsid();
                return bdcXmZsRelService.getProidByZsid(zsid);
            }
        }
        return null;
    }

    @Override
    public List<BdcZs> getBdcZsListByWiidOrderByZl(String wiid) {
        return StringUtils.isNotBlank(wiid) ? bdcZsMapper.getBdcZsListByWiidOrderByZl(wiid) : null;
    }
}
