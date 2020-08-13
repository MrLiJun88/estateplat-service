package cn.gtmap.estateplat.server.sj.zs.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcLshMapper;
import cn.gtmap.estateplat.server.core.service.BdcFdcqService;
import cn.gtmap.estateplat.server.core.service.BdcLshService;
import cn.gtmap.estateplat.server.core.service.BdcXtConfigService;
import cn.gtmap.estateplat.server.service.BdczsBhService;
import cn.gtmap.estateplat.server.sj.zs.BdcZsGetbhService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-04
 * @description 获取证书流水号
 */
@Service
public class BdcZsGetbhServiceImpl implements BdcZsGetbhService {
    @Autowired
    private BdcXtConfigService bdcXtConfigService;
    @Autowired
    private BdcLshService bdcLshService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdczsBhService bdczsBhService;
    @Autowired
    private BdcLshMapper bdcLshMapper;
    @Autowired
    private EntityMapper entityMapper;
    private String spfscdjFont = AppConfig.getProperty("spfscdj.zstype");

    /**
     * @param bdcZsList
     * @param font
     * @param bdcXm
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取证书流水号
     */
    @Override
    public synchronized List<BdcZs> getbh(List<BdcZs> bdcZsList, String font, BdcXm bdcXm) {
        if (CollectionUtils.isNotEmpty(bdcZsList) && bdcXm != null && StringUtils.isNotBlank(font)) {
            String zhqc = "";
            String bhlxdm = bdcLshService.getBhlxDmBybhlxMc(font);
            BdcXtConfig bdcXtConfig = bdcXtConfigService.queryBdczsBhConfig(bdcXm);
            String sqsjc = bdczsBhService.getProvinceShortName(bdcXtConfig);
            String szsxqc = bdczsBhService.getXzqShortName(bdcXtConfig);
            if (StringUtils.equals(font, spfscdjFont)) {
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    zhqc = bdcFdcqList.get(0).getZxzh();
                }
            }
            Integer len = bdcLshService.getLshwsByBhlxdm(bhlxdm);
            Integer maxLsh = getMaxLsh(bhlxdm, zhqc);
            String lsh = "";
            for (int i = 0; i < bdcZsList.size(); i++) {
                BdcZs bdcZs = bdcZsList.get(i);
                if (StringUtils.isBlank(bdcZs.getBdcqzh())) {
                    maxLsh++;
                    lsh = getLsh(maxLsh, len);
                    initBdcZs(bdcZs, CalendarUtil.getCurrYear(), font, sqsjc, szsxqc, zhqc, lsh);
                }
                lsh = getLsh(maxLsh, len);
            }
            saveBdcLsh(bhlxdm, zhqc, lsh);
        }
        return bdcZsList;
    }

    private Integer getMaxLsh(String bhlxdm, String zhqc) {
        Integer maxLsh = null;
        if (StringUtils.isNotBlank(bhlxdm)) {
            String nf = CalendarUtil.getCurrYear();
            Map map = Maps.newHashMap();
            map.put("bhlxdm", bhlxdm);
            if (StringUtils.isNotBlank(zhqc)) {
                map.put("zhqc", zhqc);
            } else {
                map.put("nf", nf);
            }
            maxLsh = bdcLshService.getMaxLsh(map);
            if (maxLsh == null) {
                maxLsh = 0;
            }
        }
        return maxLsh;
    }

    private String getLsh(Integer xh, Integer len) {
        String lsh = xh.toString();
        if (lsh.length() < len) {
            do {
                lsh = "0" + lsh;
            } while (lsh.length() < len);
        }
        return lsh;
    }

    private void saveBdcLsh(String bhlxdm, String zhqc, String lsh) {
        String nf = CalendarUtil.getCurrYear();
        Map map = Maps.newHashMap();
        map.put("bhlxdm", bhlxdm);
        if (StringUtils.isNotBlank(zhqc)) {
            map.put("zhqc", zhqc);
        } else {
            map.put("nf", nf);
        }
        List<BdcLsh> bdcLshList = bdcLshMapper.getBdcLshList(map);
        if (CollectionUtils.isNotEmpty(bdcLshList)) {
            BdcLsh bdcLsh = bdcLshList.get(0);
            bdcLsh.setLsh(lsh);
            bdcLsh.setZhqc(zhqc);
            entityMapper.saveOrUpdate(bdcLsh, bdcLsh.getLshid());
        } else {
            BdcLsh bdcLsh = new BdcLsh();
            bdcLsh.setLshid(UUIDGenerator.generate18());
            bdcLsh.setLsh(lsh);
            bdcLsh.setNf(nf);
            bdcLsh.setBhlxdm(bhlxdm);
            bdcLsh.setZhqc(zhqc);
            entityMapper.saveOrUpdate(bdcLsh, bdcLsh.getLshid());
        }
    }

    private BdcZs initBdcZs(BdcZs bdcZs, String nf, String font, String sqsjc, String szsxqc, String zhqc, String lsh) {
        String bdcqzh = "";
        if (StringUtils.isNotBlank(zhqc)) {
            bdcqzh = zhqc + lsh;
        } else {
            bdcqzh = sqsjc + Constants.BDCQ_BH_LEFT_BRACKET + nf + Constants.BDCQ_BH_RIGHT_BRACKET + szsxqc + font + "第" + lsh + "号";
        }
        bdcZs.setZstype(font);
        bdcZs.setZhlsh(lsh);
        bdcZs.setNf(nf);
        bdcZs.setSqsjc(sqsjc);
        bdcZs.setSzsxqc(szsxqc);
        bdcZs.setBdcqzh(bdcqzh);
        bdcZs.setCzrq(CommonUtil.getCurrDate());
        bdcZs.setBdcqzhjc(bdcZs.getNf() + bdcZs.getZhlsh());
        return bdcZs;
    }

    /**
     * @return 接口标识码，同一个接口中的标识码不能出现重复
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取实现类的标识码
     */
    @Override
    public String getIntetfacaCode() {
        return null;
    }
}
