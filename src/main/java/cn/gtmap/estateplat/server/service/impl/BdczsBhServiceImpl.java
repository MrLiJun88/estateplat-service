package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcDelZszhMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZsMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.BdczsBhService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-19
 */
public class BdczsBhServiceImpl implements BdczsBhService {
    @Autowired
    public BdcZsService bdcZsService;
    @Autowired
    public BdcXtConfigService bdcXtConfigService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcDelZszhMapper bdcDelZszhMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    public BdcZsMapper bdcZsMapper;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcLshService bdcLshService;
    @Autowired
    private BdcFdcqService bdcFdcqSeice;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;

    @Override
    public String getProvinceShortName(BdcXtConfig bdcXtConfig) {
        String provinceShortName = "";
        if (bdcXtConfig != null) {
            provinceShortName = bdcXtConfig.getSqsjc();
        }
        return provinceShortName;
    }

    @Override
    public String getBhYear(BdcXtConfig bdcXtConfig) {
        String year = "";
        if (bdcXtConfig != null) {
            year = bdcXtConfig.getNf();
        }
        return year;
    }

    @Override
    public String getXzqShortName(BdcXtConfig bdcXtConfig) {
        String xzq = "";
        if (bdcXtConfig != null) {
            xzq = bdcXtConfig.getSzsxqc();
        }
        return xzq;
    }

    @Override
    public String getLsh(BdcXtConfig bdcXtConfig, String zsFont, int zsIndex, BdcXm bdcXm) {
        String lsh = "";
        Integer len = 0;
        if (bdcXtConfig != null) {
            len = bdcXtConfig.getBdcqzhws();
        }

        //证号流水号开头区号
        String startQh = StringUtils.deleteWhitespace(AppConfig.getProperty("zslsh.start.qh"));

        //zdd 获取当前最大流水号
        //sc 根据当前年份过滤
        HashMap map = new HashMap();
        map.put("zstype", zsFont);
        map.put("nf", CalendarUtil.getCurrYear());
        map.put("dwdm", bdcXm.getDwdm());
        if (StringUtils.isNotBlank(startQh)) {
            map.put("startQh", startQh);
        }
        Integer maxLsh = bdcZsService.getMaxLsh(map);
        if (maxLsh == null) {
            maxLsh = 0;
        }
        ++maxLsh;
        maxLsh = maxLsh + zsIndex;
        lsh = maxLsh.toString();

        if (StringUtils.isNotBlank(startQh)) {
            int qhLength = startQh.length();
            if (lsh.indexOf(startQh) == -1) {
                if (lsh.length() < len) {
                    if (len > qhLength) {
                        do {
                            lsh = "0" + lsh;
                        } while (lsh.length() < len - qhLength);
                        lsh = startQh + lsh;
                    }
                } else {
                    if (!StringUtils.equals(startQh, lsh.substring(0, qhLength))) {
                        lsh = startQh + lsh.substring(qhLength, lsh.length());
                    }
                }
            } else {
                lsh = lsh.substring(qhLength, lsh.length());
                if (lsh.length() < len - qhLength) {
                    do {
                        lsh = "0" + lsh;
                    } while (lsh.length() < len - qhLength);
                }
                lsh = startQh + lsh;
            }

        } else {
            if (lsh.length() < len) {
                do {
                    lsh = "0" + lsh;
                } while (lsh.length() < len);
            }
        }
        return lsh;
    }

    public String getMaxLshBySl(BdcXtConfig bdcXtConfig, String zsFont, BdcXm bdcXm) {
        Integer maxLsh = null;
        Integer len = 0;
        String lsh = "";
        //根据行政代码获取不动产权证的对应的序列的最大流水号
        if (bdcXm != null && StringUtils.isNotBlank(zsFont) && StringUtils.equals(zsFont, Constants.BDCQZS_BH_FONT)) {
            HashMap map = new HashMap();
            String sqZscqzLsh = Constants.BDCQZS_BH_XL;
            if (StringUtils.isNotBlank(bdcXm.getSsxz()) && bdcXm.getSsxz().length() != 6) {
                sqZscqzLsh = sqZscqzLsh + "_" + bdcXm.getSsxz();
            }
            map.put("BdcqzLsh", sqZscqzLsh);
            maxLsh = getMaxLshByXl(map);
        }  //根据行政代码获取不动产权证明的对应的序列的最大流水号
        else if (bdcXm != null && StringUtils.isNotBlank(zsFont) && StringUtils.equals(zsFont, Constants.BDCQZM_BH_FONT)) {
            HashMap map = new HashMap();
            String sqZsczmLsh = Constants.BDCQZM_BH_XL;
            if (StringUtils.isNotBlank(bdcXm.getSsxz())) {
                sqZsczmLsh = sqZsczmLsh + "_" + bdcXm.getSsxz();
            }
            map.put("BdcqzLsh", sqZsczmLsh);
            maxLsh = getMaxLshByXl(map);
        }
        if (maxLsh != null) {
            lsh = maxLsh.toString();
        }
        if (bdcXtConfig != null && bdcXtConfig.getBdcqzhws() != null) {
            len = bdcXtConfig.getBdcqzhws();
        }

        String startQh = StringUtils.deleteWhitespace(AppConfig.getProperty("zslsh.start.qh"));
        if (StringUtils.isNotBlank(startQh)) {
            int qhLength = startQh.length();
            if (lsh.length() < len) {
                if (len > qhLength) {
                    do {
                        if (lsh.length() == len - qhLength) {
                            lsh = startQh + lsh;
                        } else {
                            lsh = "0" + lsh;
                        }
                    } while (lsh.length() < len);
                }
            } else {
                if (!StringUtils.equals(startQh, lsh.substring(0, qhLength))) {
                    lsh = startQh + lsh.substring(qhLength, lsh.length());
                }
            }
        } else {
            if (lsh.length() < len) {
                do {
                    lsh = "0" + lsh;
                } while (lsh.length() < len);
            }
        }

        return lsh;
    }

    public Integer getMaxLshByXl(HashMap map) {
        return bdcZsMapper.getMaxLshByXl(map);
    }


    public String getBdczsBh(BdcXtConfig bdcXtConfig) {
        String provinceShortName = getProvinceShortName(bdcXtConfig);
        String year = getBhYear(bdcXtConfig);
        String xzq = getXzqShortName(bdcXtConfig);
        String lsh = getLsh(bdcXtConfig, Constants.BDCQZS_BH_FONT, 0, new BdcXm());
        return provinceShortName + "" + year + "" + xzq + Constants.BDCQZS_BH_FONT + "第" + lsh + "号";
    }

    public String getBdczmBh(BdcXtConfig bdcXtConfig) {
        String provinceShortName = getProvinceShortName(bdcXtConfig);
        String year = getBhYear(bdcXtConfig);
        String xzq = getXzqShortName(bdcXtConfig);
        String lsh = getLsh(bdcXtConfig, Constants.BDCQZM_BH_FONT, 0, new BdcXm());
        return provinceShortName + Constants.BDCQ_BH_LEFT_BRACKET + year + Constants.BDCQ_BH_RIGHT_BRACKET + xzq + Constants.BDCQZM_BH_FONT + "第" + lsh + "号";
    }

    @Override
    public BdcZs creatBdcqzBh(BdcXm bdcXm, BdcZs bdcZs, int zsIndex, String zstype) {
        String bdcqzh = "";
        if (bdcZs == null) {
            bdcZs = new BdcZs();
        }
        if (StringUtils.isBlank(bdcZs.getBdcqzh())) {
            BdcXtConfig bdcXtConfig = bdcXtConfigService.queryBdczsBhConfig(bdcXm);
            String nf = getBhYear(bdcXtConfig);
            String sqsjc = getProvinceShortName(bdcXtConfig);
            String szsxqc = getXzqShortName(bdcXtConfig);
            String zsFont = "";
            if (StringUtils.isNotBlank(zstype)) {
                zsFont = zstype;
            } else {
                zsFont = getZsTpye(bdcXm);
            }
            String bhVersion = AppConfig.getProperty("scdjxxb.bh.version");
            if (StringUtils.equals(zsFont, Constants.BDCQSCDJZ_BH_FONT) && StringUtils.equals(bhVersion, "sz")) {
                //不动产首次信息表添加编号
                nf = CalendarUtil.getCurrYear();
                String lsh = "";
                String bh = "";
                List<BdcFdcq> bdcFdcqList = bdcFdcqSeice.getBdcFdcqByProid(bdcXm.getProid());
                BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqService.getBdcJsydzjdsyq(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqList) || bdcJsydzjdsyq != null) {
                    String zxzh = CollectionUtils.isNotEmpty(bdcFdcqList) ? bdcFdcqList.get(0).getZxzh() : bdcJsydzjdsyq.getZxzh();
                    Map map = new HashMap();
                    map.put("nf", nf);
                    map.put("zstype", Constants.BDCQSCDJZ_BH_FONT);
                    map.put("zxzh", zxzh);
                    String bhlxdm = bdcLshService.getBhlxDmBybhlxMc(Constants.BHLX_BDCSCXXB_MC);
                    Integer len = bdcLshService.getLshwsByBhlxdm(bhlxdm);
                    Integer maxLsh = bdcZsMapper.getMaxScdjzLsh(map);
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
                bdcZs.setZstype(Constants.BDCQSCDJZ_BH_FONT);
                bdcZs.setNf(nf);
                if (StringUtils.isNotBlank(bh)) {
                    bdcZs.setZhlsh(lsh);
                    bdcZs.setBdcqzh(bh);
                    bdcZs.setBdcqzhjc(bdcZs.getNf() + bdcZs.getZhlsh());
                }
            } else if (StringUtils.isNotBlank(zsFont)) {
                HashMap map = new HashMap();
                map.put("isuse", "0");
                map.put("zstype", zsFont);
                map.put("nf", nf);
                //不动产权证号配置到各乡镇，各乡镇可能不同可能相同,就需要将该项目办理乡镇的行政代码存储到bdcZs里的dwdm
                String bdcqzhFilterZhXzdm = AppConfig.getProperty("bdcqzh.filterZh.xzdm");
                if (StringUtils.equals(bdcqzhFilterZhXzdm, "true")) {
                    map.put("dwdm", bdcXm.getSsxz());
                } else {
                    map.put("dwdm", bdcXm.getDwdm());
                }
                List<BdcDelZszh> bdcDelZszhList = bdcDelZszhMapper.getBdcDelZszhList(map);
                if (CollectionUtils.isNotEmpty(bdcDelZszhList)) {
                    BdcDelZszh bdcDelZszh = bdcDelZszhList.get(0);
                    //取删除的证号
                    bdcZs.setZstype(zsFont);
                    bdcZs.setZhlsh(bdcDelZszh.getZhlsh());
                    bdcZs.setNf(bdcDelZszh.getNf());
                    bdcZs.setSqsjc(bdcDelZszh.getSqsjc());
                    bdcZs.setSzsxqc(bdcDelZszh.getSzsxqc());
                    bdcZs.setBdcqzh(bdcDelZszh.getBdcqzh());
                    bdcZs.setBdcqzhjc(bdcZs.getNf() + bdcZs.getZhlsh());
                    bdcDelZszh.setIsuse("1");
                    entityMapper.saveOrUpdate(bdcDelZszh, bdcDelZszh.getZszhid());
                } else {
                    String lsh = "";
                    String useSequence = AppConfig.getProperty("use.sequence");
                    if (StringUtils.equals(useSequence, "true")) {
                        //根据序列来获取最大流水号
                        lsh = getMaxLshBySl(bdcXtConfig, zsFont, bdcXm);
                    } else {
                        lsh = getLsh(bdcXtConfig, zsFont, zsIndex, bdcXm);
                    }
                    //各乡镇代码前缀是否插入到不动产权证号
                    String bdcqzhInsertZhXzdm = AppConfig.getProperty("bdcqzh.insertZh.xzdm");
                    if (StringUtils.equals(bdcqzhInsertZhXzdm, "true")) {
                        if (StringUtils.isNotBlank(bdcXm.getSsxz()) && bdcXm.getSsxz().length() == 9) {
                            lsh = bdcXm.getSsxz().substring(7) + lsh;
                        } else {
                            lsh = "00" + lsh;
                        }
                    }
                    bdcqzh = sqsjc + Constants.BDCQ_BH_LEFT_BRACKET + nf + Constants.BDCQ_BH_RIGHT_BRACKET + szsxqc + zsFont + "第" + lsh + "号";
                    bdcZs.setZstype(zsFont);
                    bdcZs.setZhlsh(lsh);
                    bdcZs.setNf(nf);
                    bdcZs.setSqsjc(sqsjc);
                    bdcZs.setSzsxqc(szsxqc);
                    bdcZs.setBdcqzh(bdcqzh);
                    bdcZs.setBdcqzhjc(bdcZs.getNf() + bdcZs.getZhlsh());
                }
            }
        }
        return bdcZs;
    }

    /**
     * @param bdcXm
     * @return 证书类型
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取证书类型
     */
    @Override
    public String getZsTpye(BdcXm bdcXm) {
        String zsFont = "";
        if (bdcXm != null) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            zsFont = qllxService.makeSureBdcqzlx(qllxVo);
            //lcl 从配置文件获取新增证书类型，只针对商品房及业主共有部分首次登记流程
            if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM)||StringUtils.equals(bdcXm.getSqlx(),Constants.SQLX_SPFGYSCDJ_CYBZ_DM)) {
                List<BdcFdcq> bdcFdcqLst = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqLst) && StringUtils.equals(bdcFdcqLst.get(0).getFzlx(), Constants.FZLX_FZM)) {
                    String zsFont1 = AppConfig.getProperty("spfscdj.zstype");
                    if (StringUtils.isNotBlank(zsFont1)) {
                        zsFont = zsFont1;
                    }
                }
            }
            /**
             * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
             * @Time 2020/6/10 15:13
             * @description 土地首次登记信息表补登记 生成证明单
             */
            if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_TDSCDJXXBBDJ_DM)) {
                String zsFont1 = AppConfig.getProperty("spfscdj.zstype");
                if (StringUtils.isNotBlank(zsFont1)) {
                    zsFont = zsFont1;
                }
            }
        }
        return zsFont;
    }
}
