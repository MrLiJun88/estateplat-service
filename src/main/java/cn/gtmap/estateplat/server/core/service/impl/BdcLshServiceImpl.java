package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcLshMapper;
import cn.gtmap.estateplat.server.core.service.BdcLshService;
import cn.gtmap.estateplat.server.core.service.DwxxService;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/1/17
 * @description
 */
@Service
public class BdcLshServiceImpl implements BdcLshService {
    @Autowired
    BdcLshMapper bdcLshMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    DwxxService dwxxService;

    private static final String PARAMETER_BHLXDM = "bhlxdm";

    @Override
    public Integer getMaxLsh(Map map) {
        return bdcLshMapper.getMaxLsh(map);
    }

    @Override
    public String getLsh(String bhlxmc, String nf, String qh) {
        String lsh = "";
        if (StringUtils.isNotBlank(bhlxmc) && StringUtils.isNotBlank(nf) && StringUtils.isNotBlank(qh)) {
            String bhlxdm = getBhlxDmBybhlxMc(bhlxmc);
            Integer len = getLshwsByBhlxdm(bhlxdm);
            if (StringUtils.isNotBlank(bhlxdm)) {
                HashMap map = new HashMap();
                map.put("nf", nf);
                map.put("qh", qh);
                map.put(PARAMETER_BHLXDM, bhlxdm);
                Integer maxLsh = getMaxLsh(map);
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
                if (StringUtils.isNotBlank(lsh)) {
                    List<BdcLsh> bdcLshList = bdcLshMapper.getBdcLshList(map);
                    if (CollectionUtils.isNotEmpty(bdcLshList)) {
                        BdcLsh bdcLsh = bdcLshList.get(0);
                        bdcLsh.setLsh(lsh);
                        entityMapper.saveOrUpdate(bdcLsh, bdcLsh.getLshid());
                    } else {
                        BdcLsh bdcLsh = new BdcLsh();
                        bdcLsh.setLshid(UUIDGenerator.generate18());
                        bdcLsh.setLsh(lsh);
                        bdcLsh.setNf(nf);
                        bdcLsh.setBhlxdm(bhlxdm);
                        bdcLsh.setQh(qh);
                        entityMapper.saveOrUpdate(bdcLsh, bdcLsh.getLshid());
                    }
                }
            }
        }
        return lsh;
    }

    @Override
    public String getQh(String userid) {
        String qh = "";
        if (StringUtils.isNotBlank(userid)) {
            String userDwdm = PlatformUtil.getCurrentUserDwdmByUserid(userid);
            Dwxx dwxx = dwxxService.getDwxxByDwdm(userDwdm);
            if (dwxx != null && StringUtils.isNotBlank(dwxx.getDwmc())) {
                Example bdcQhExample = new Example(BdcZdQh.class);
                bdcQhExample.createCriteria().andEqualTo("mc", dwxx.getDwmc());
                List<BdcZdQh> bdcZdQhList = entityMapper.selectByExample(bdcQhExample);
                if (CollectionUtils.isNotEmpty(bdcZdQhList)) {
                    BdcZdQh bdcZdQh = bdcZdQhList.get(0);
                    if (bdcZdQh != null && StringUtils.isNotBlank(bdcZdQh.getDm())) {
                        qh = bdcZdQh.getDm();
                    }
                }
            }
        }
        return qh;
    }

    @Override
    public synchronized String getQhByDwdm(String dwdm) {
        String qh = "";
        if (StringUtils.isNotBlank(dwdm)) {
            Dwxx dwxx = dwxxService.getDwxxByDwdm(dwdm);
            if (dwxx != null && StringUtils.isNotBlank(dwxx.getDwmc())) {
                Example bdcQhExample = new Example(BdcZdQh.class);
                bdcQhExample.createCriteria().andEqualTo("mc", dwxx.getDwmc());
                List<BdcZdQh> bdcZdQhList = entityMapper.selectByExample(bdcQhExample);
                if (CollectionUtils.isNotEmpty(bdcZdQhList)) {
                    BdcZdQh bdcZdQh = bdcZdQhList.get(0);
                    if (bdcZdQh != null && StringUtils.isNotBlank(bdcZdQh.getDm()))
                        qh = bdcZdQh.getDm();
                }
            }
        }
        return qh;
    }

    @Override
    public String getBh(String bhlxmc, String nf, String qh, String lsh) {
        String bh = "";
        if (StringUtils.isNotBlank(bhlxmc)) {
            String bhlxdm = getBhlxDmBybhlxMc(bhlxmc);
            String bhmb = getBhMbByBhlxdm(bhlxdm);
            if (StringUtils.isNotBlank(bhmb)) {
                bh = StringUtils.replace(bhmb, "nf", nf);
                bh = StringUtils.replace(bh, "qh", qh);
                bh = StringUtils.replace(bh, "lsh", lsh);
            } else {
                bh = StringUtils.trim(nf + qh + lsh);
            }
        }

        return bh;
    }

    @Override
    public synchronized List<BdcLsh> getBdcLshList(Map map) {
        return bdcLshMapper.getBdcLshList(map);
    }

    @Override
    public String getBhlxDmBybhlxMc(String bhlxMc) {
        String bhlxdm = "";
        Example bdcBhlxExample = new Example(BdcZdBhlx.class);
        bdcBhlxExample.createCriteria().andEqualTo("mc", bhlxMc);
        List<BdcZdBhlx> bdcZdBhlxList = entityMapper.selectByExample(bdcBhlxExample);
        if (CollectionUtils.isNotEmpty(bdcZdBhlxList)) {
            BdcZdBhlx bdcZdBhlx = bdcZdBhlxList.get(0);
            if (bdcZdBhlx != null && StringUtils.isNotBlank(bdcZdBhlx.getDm())) {
                bhlxdm = bdcZdBhlx.getDm();
            }
        }
        return bhlxdm;
    }

    @Override
    public Integer getLshwsByBhlxdm(String bhlxdm) {
        Integer lshws = 0;
        if (StringUtils.isNotBlank(bhlxdm)) {
            Example bdcXtBhExample = new Example(BdcXtBh.class);
            bdcXtBhExample.createCriteria().andEqualTo(PARAMETER_BHLXDM, bhlxdm);
            List<BdcXtBh> bdcXtBhList = entityMapper.selectByExample(bdcXtBhExample);
            if (CollectionUtils.isNotEmpty(bdcXtBhList)) {
                BdcXtBh bdcXtBh = bdcXtBhList.get(0);
                if (bdcXtBh != null && bdcXtBh.getLshws() != null) {
                    lshws = bdcXtBh.getLshws();
                }
            }
        }
        return lshws;
    }

    @Override
    public String getBhMbByBhlxdm(String bhlxdm) {
        String bhMb = "";
        Example bdcXtBhExample = new Example(BdcXtBh.class);
        bdcXtBhExample.createCriteria().andEqualTo(PARAMETER_BHLXDM, bhlxdm);
        List<BdcXtBh> bdcXtBhList = entityMapper.selectByExample(bdcXtBhExample);
        if (CollectionUtils.isNotEmpty(bdcXtBhList)) {
            BdcXtBh bdcXtBh = bdcXtBhList.get(0);
            if (bdcXtBh != null && bdcXtBh.getLshws() != null)
                bhMb = bdcXtBh.getBh();
        }
        return bhMb;
    }

    @Override
    public String getBh(String userid, String bhlxmc) {
        String nf = CalendarUtil.getCurrYear();
        String qh = getQh(userid);
        String lsh = getLsh(bhlxmc, nf, qh);
        return getBh(bhlxmc, nf, qh, lsh);
    }
}
