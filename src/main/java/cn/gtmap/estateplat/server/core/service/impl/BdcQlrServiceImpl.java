package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQlrMapper;
import cn.gtmap.estateplat.server.core.model.OntBdcXm;
import cn.gtmap.estateplat.server.core.model.OntQlr;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by lst on 2015/3/17.
 */
@Repository
public class BdcQlrServiceImpl implements BdcQlrService {
    @Autowired
    BdcQlrMapper bdcQlrMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    GdFwService gdFwService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String CONFIGURATION_PARAMETER_BDCQLR_QLBL_FS = "bdcqlr.qlbl.fs";

    @Override
    public String combinationQlr(final List<BdcQlr> bdcQlrList) {
        StringBuilder qlrBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                if (StringUtils.isBlank(qlrBuilder.toString())) {
                    qlrBuilder.append(bdcQlr.getQlrmc());
                } else {
                    qlrBuilder.append("、").append(bdcQlr.getQlrmc());
                }
            }
        }
        return qlrBuilder.toString();
    }

    @Override
    public String combinationYwr(final List<BdcQlr> bdcYwrList) {
        StringBuilder qlrBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(bdcYwrList)) {
            for (BdcQlr bdcQlr : bdcYwrList) {
                if (StringUtils.isBlank(qlrBuilder.toString())) {
                    qlrBuilder.append(bdcQlr.getQlrmc());
                } else {
                    qlrBuilder.append("、").append(bdcQlr.getQlrmc());
                }
            }
        }
        return qlrBuilder.toString();
    }

    @Override
    public List<BdcQlr> queryBdcQlrByProid(final String proid) {
        List<BdcQlr> list = new ArrayList<BdcQlr>();
        List<BdcQlr> tempList = queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(tempList)) {
            for (int i = 0; i < tempList.size(); i++) {
                BdcQlr bdcQlr = tempList.get(i);
                if (StringUtils.isNotBlank(bdcQlr.getQlrlx()) && StringUtils.equals(bdcQlr.getQlrlx(), Constants.QLRLX_QLR)) {
                    list.add(bdcQlr);
                }
            }
        }
        return list;
    }

    @Override
    public List<BdcQlr> queryBdcYwrByProid(final String proid) {
        List<BdcQlr> list = new ArrayList<BdcQlr>();
        List<BdcQlr> tempList = queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(tempList)) {
            for (BdcQlr bdcQlr : tempList) {
                if (StringUtils.isNotBlank(bdcQlr.getQlrlx()) && StringUtils.equals(bdcQlr.getQlrlx(), Constants.QLRLX_YWR)) {
                    list.add(bdcQlr);
                }
            }
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcQlr> queryBdcQlrYwrByProid(final String proid) {
        List<BdcQlr> list = new ArrayList<BdcQlr>();
        List<BdcQlr> tempList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(proid))
            tempList = bdcQlrMapper.queryBdcQlrByProid(proid);
        if (CollectionUtils.isNotEmpty(tempList)) {
            for (BdcQlr bdcQlr : tempList) {
                if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                    if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                        bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                    else
                        bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                }
                list.add(bdcQlr);
            }
        }
        return list;
    }

    @Override
    public BdcQlr getBdcQlrFromProject(final Project project, BdcQlr qlr) {
        if (project == null) {
            return qlr;
        }
        //存入不动产单元的实体类中
        if (qlr == null) {
            qlr = new BdcQlr();
        }
        if (StringUtils.isBlank(qlr.getQlrid()))
            qlr.setQlrid(UUIDGenerator.generate());
        qlr.setProid(project.getProid());
        return qlr;
    }

    @Override
    public BdcQlr getBdcQlrFromFw(final DjsjFwxx fwxx, BdcQlr qlr) {
        if (fwxx == null) {
            return qlr;
        }
        //存入不动产单元的实体类中
        if (qlr == null) {
            qlr = new BdcQlr();
        }
        qlr.setQlrmc(fwxx.getQlr());
        qlr.setQlrsfzjzl(fwxx.getQlrzjlx());
        qlr.setQlrzjh(fwxx.getQlrzjh());

        if (StringUtils.isNotBlank(fwxx.getDh()))
            qlr.setQlrlxdh(fwxx.getDh());
        if (StringUtils.isNotBlank(fwxx.getDz()))
            qlr.setQlrtxdz(fwxx.getDz());
        if (StringUtils.isNotBlank(fwxx.getYb()))
            qlr.setQlryb(fwxx.getYb());

        //zwq 获取FW_FCQLR里的gyfs
        qlr.setGyfs(fwxx.getGyfs());

        /*sc:Dwxz 地基库QLRXZ没值，DWXZ是qlrxz 表示很困惑**/
        if (fwxx.getDwxz() != null) {
            qlr.setQlrxz(fwxx.getDwxz().toString());
        }
        if (StringUtils.isBlank(qlr.getQlrxz())) {
            if (StringUtils.equals(qlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(qlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                qlr.setQlrxz(Constants.QLRXZ_QY);
            else
                qlr.setQlrxz(Constants.QLRXZ_GR);
        }
        return qlr;
    }

    @Override
    public List<BdcQlr> getBdcQlrFromLq(final DjsjLqxx lqxx, BdcQlr qlr) {
        List<NydQlr> nydQlrList;
        List<BdcQlr> qlrList = new ArrayList<BdcQlr>();
        if (lqxx != null && StringUtils.isNotBlank(lqxx.getDjh())) {
            nydQlrList = getNydQlrByDjh(lqxx.getDjh());
            if (CollectionUtils.isNotEmpty(nydQlrList)) {
                for (int i = 0; i < nydQlrList.size(); i++) {
                    BdcQlr bdcQlr = new BdcQlr();
                    if (StringUtils.isNotBlank(qlr.getProid()))
                        bdcQlr.setProid(qlr.getProid());
                    if (StringUtils.isNotBlank(nydQlrList.get(i).getSfldsyqr()) && StringUtils.equals(nydQlrList.get(i).getSfldsyqr(), "1")) {
                        bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlr()))
                            bdcQlr.setQlrmc(nydQlrList.get(i).getQlr());
                        if (StringUtils.isNotBlank(qlr.getQlrid()))
                        /**
                         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                         * @description 如果多个权利人权利人ID相同的话只能保存一个，所以这个setUUID
                         */
                            bdcQlr.setQlrid(UUIDGenerator.generate18());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrsfzjzl()))
                            bdcQlr.setQlrsfzjzl(nydQlrList.get(i).getQlrsfzjzl());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrzjh()))
                            bdcQlr.setQlrzjh(nydQlrList.get(i).getQlrzjh());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrtxdz()))
                            bdcQlr.setQlrtxdz(nydQlrList.get(i).getQlrtxdz());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrfddbr()))
                            bdcQlr.setQlrfddbr(nydQlrList.get(i).getQlrfddbr());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrfddbrdh()))
                            bdcQlr.setQlrfddbrdh(nydQlrList.get(i).getQlrfddbrdh());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrdlr()))
                            bdcQlr.setQlrdlr(nydQlrList.get(i).getQlrdlr());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrdlrdh()))
                            bdcQlr.setQlrdlrdh(nydQlrList.get(i).getQlrdlrdh());
                        if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                            if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                                bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                            else
                                bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                        }
                        qlrList.add(bdcQlr);
                    }
                }
            }
        }

        return qlrList;
    }

    @Override
    public List<BdcQlr> getBdcQlrFromTt(final DjsjNydDcb djsjNydDcb, BdcQlr qlr) {
        List<NydQlr> nydQlrList;
        List<BdcQlr> qlrList = new ArrayList<BdcQlr>();
        if (djsjNydDcb != null && StringUtils.isNotBlank(djsjNydDcb.getDjh())) {
            nydQlrList = getNydQlrByDjh(djsjNydDcb.getDjh());
            if (CollectionUtils.isNotEmpty(nydQlrList)) {
                for (int i = 0; i < nydQlrList.size(); i++) {
                    BdcQlr bdcQlr = new BdcQlr();
                    if (StringUtils.isNotBlank(qlr.getProid()))
                        bdcQlr.setProid(qlr.getProid());
                    if (StringUtils.isNotBlank(nydQlrList.get(i).getSfldsyqr()) && StringUtils.equals(nydQlrList.get(i).getSfldsyqr(), "1")) {
                        bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlr()))
                            bdcQlr.setQlrmc(nydQlrList.get(i).getQlr());
                        if (StringUtils.isNotBlank(qlr.getQlrid()))
                        /**
                         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                         * @description 如果多个权利人权利人ID相同的话只能保存一个，所以这个setUUID
                         */
                            bdcQlr.setQlrid(UUIDGenerator.generate18());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrsfzjzl()))
                            bdcQlr.setQlrsfzjzl(nydQlrList.get(i).getQlrsfzjzl());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrzjh()))
                            bdcQlr.setQlrzjh(nydQlrList.get(i).getQlrzjh());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrtxdz()))
                            bdcQlr.setQlrtxdz(nydQlrList.get(i).getQlrtxdz());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrfddbr()))
                            bdcQlr.setQlrfddbr(nydQlrList.get(i).getQlrfddbr());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrfddbrdh()))
                            bdcQlr.setQlrfddbrdh(nydQlrList.get(i).getQlrfddbrdh());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrdlr()))
                            bdcQlr.setQlrdlr(nydQlrList.get(i).getQlrdlr());
                        if (StringUtils.isNotBlank(nydQlrList.get(i).getQlrdlrdh()))
                            bdcQlr.setQlrdlrdh(nydQlrList.get(i).getQlrdlrdh());
                        if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                            if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                                bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                            else
                                bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                        }
                        qlrList.add(bdcQlr);
                    }
                }
            }
        }

        return qlrList;
    }

    @Override
    public List<BdcQlr> getBdcQlrFromCb(final DjsjCbzdDcb cbzdDcb, BdcQlr qlr) {
        List<BdcQlr> qlrList = new ArrayList<BdcQlr>();
        List<NydQlr> nydQlrList;
        if (cbzdDcb != null) {
            nydQlrList = getNydQlrByDjh(cbzdDcb.getDjh());
            if (CollectionUtils.isNotEmpty(nydQlrList)) {
                for (int i = 0; i < nydQlrList.size(); i++) {
                    BdcQlr bdcQlr = new BdcQlr();
                    NydQlr nydQlr = nydQlrList.get(i);
                    if (StringUtils.isNotBlank(qlr.getProid()))
                        bdcQlr.setProid(qlr.getProid());
                    if (StringUtils.isNotBlank(qlr.getQlrid()))
                        bdcQlr.setQlrid(qlr.getQlrid());
                    if (StringUtils.isNotBlank(nydQlr.getQlr()))
                        bdcQlr.setQlrmc(nydQlr.getQlr());
                    if (StringUtils.isNotBlank(nydQlr.getQlrsfzjzl()))
                        bdcQlr.setQlrsfzjzl(nydQlr.getQlrsfzjzl());
                    if (StringUtils.isNotBlank(nydQlr.getQlrzjh()))
                        bdcQlr.setQlrzjh(nydQlr.getQlrzjh());
                    if (StringUtils.isNotBlank(nydQlr.getQlrtxdz()))
                        bdcQlr.setQlrtxdz(nydQlr.getQlrtxdz());
                    if (StringUtils.isNotBlank(nydQlr.getQlrfddbr()))
                        bdcQlr.setQlrfddbr(nydQlr.getQlrfddbr());
                    if (StringUtils.isNotBlank(nydQlr.getQlrfddbrdh()))
                        bdcQlr.setQlrfddbrdh(nydQlr.getQlrfddbrdh());
                    if (StringUtils.isNotBlank(nydQlr.getQlrdlr()))
                        bdcQlr.setQlrdlr(nydQlr.getQlrdlr());
                    if (StringUtils.isNotBlank(nydQlr.getQlrdlrdh()))
                        bdcQlr.setQlrdlrdh(nydQlr.getQlrdlrdh());
                    bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                    if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                        if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                            bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                        else
                            bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                    }
                    qlrList.add(bdcQlr);
                }
            }
        }
        return qlrList;
    }

    @Override
    public List<BdcQlr> getBdcQlrFromQszd(final DjsjQszdDcb djsjQszdDcb, BdcQlr qlr) {
        List<BdcQlr> qlrList = new ArrayList<BdcQlr>();
        List<DjsjQszdQlr> qszdQlrList;
        String djh = "";
        if (StringUtils.isNotBlank(djsjQszdDcb.getDjh()))
            djh = djsjQszdDcb.getDjh();
        else
            djh = StringUtils.substring(djsjQszdDcb.getBdcdyh(), 0, 19);
        qszdQlrList = getQszdQlrDjh(djh);
        if (CollectionUtils.isNotEmpty(qszdQlrList)) {
            for (int i = 0; i < qszdQlrList.size(); i++) {
                BdcQlr bdcQlr = new BdcQlr();
                DjsjQszdQlr qszdQlr = qszdQlrList.get(0);
                if (StringUtils.isNotBlank(qlr.getQlrid()))
                    bdcQlr.setQlrid(qlr.getQlrid());
                if (StringUtils.isNotBlank(qlr.getProid()))
                    bdcQlr.setProid(qlr.getProid());
                if (StringUtils.isNotBlank(qszdQlr.getQlrmc()))
                    bdcQlr.setQlrmc(qszdQlr.getQlrmc());
                if (StringUtils.isNotBlank(qszdQlr.getQlrzjlx()))
                    bdcQlr.setQlrsfzjzl(qszdQlr.getQlrzjlx());
                if (StringUtils.isNotBlank(qszdQlr.getQlrzjh()))
                    bdcQlr.setQlrzjh(qszdQlr.getQlrzjh());
                if (StringUtils.isNotBlank(qszdQlr.getTxdz()))
                    bdcQlr.setQlrtxdz(qszdQlr.getTxdz());
                if (StringUtils.isNotBlank(qszdQlr.getFrdbxm()))
                    bdcQlr.setQlrfddbr(qszdQlr.getFrdbxm());
                if (StringUtils.isNotBlank(qszdQlr.getFrdbxm()))
                    bdcQlr.setQlrfddbr(qszdQlr.getFrdbxm());
                if (StringUtils.isNotBlank(qszdQlr.getDlrxm()))
                    bdcQlr.setQlrdlr(qszdQlr.getDlrxm());
                if (StringUtils.isNotBlank(qszdQlr.getDlrdhhm()))
                    bdcQlr.setQlrdlrdh(qszdQlr.getDlrdhhm());
                bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                    if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                        bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                    else
                        bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                }
                qlrList.add(bdcQlr);
            }
        }
        return qlrList;
    }

    @Override
    public BdcQlr getBdcQlrFromZd(final DjsjZdxx zdxx, BdcQlr qlr) {
        if (zdxx == null) {
            return qlr;
        }
        //存入不动产单元的实体类中
        if (qlr == null) {
            qlr = new BdcQlr();
        }
        qlr.setQlrmc(zdxx.getQlrmc());
        qlr.setQlrfddbrdh(zdxx.getFrdbdhhm());
        qlr.setQlrdlr(zdxx.getDlrxm());
        qlr.setQlrdlrdh(zdxx.getDlrdhhm());
        qlr.setQlrfddbr(zdxx.getFrdbxm());
        qlr.setQlrsfzjzl(zdxx.getQlrzjlx());
        qlr.setQlrtxdz(zdxx.getTxdz());
        qlr.setQlrzjh(zdxx.getQlrzjh());
        if (StringUtils.isBlank(qlr.getQlrxz())) {
            if (StringUtils.equals(qlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(qlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                qlr.setQlrxz(Constants.QLRXZ_QY);
            else
                qlr.setQlrxz(Constants.QLRXZ_GR);
        }
        return qlr;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcQlr> queryBdcQlrList(final Map map) {
        return bdcQlrMapper.queryBdcQlrList(map);
    }

    @Override
    @Transactional
    public void delBdcQlrByProid(final String proid) {
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcQlr.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            entityMapper.deleteByExample(BdcQlr.class, example);
        }
    }


    @Transactional
    public void delBdcQlrByQlrid(final String qlrid) {
        if (StringUtils.isNotBlank(qlrid)) {
            entityMapper.deleteByPrimaryKey(BdcQlr.class, qlrid);
        }
    }

    @Override
    @Transactional
    public void delBdcQlrByQlrid(final String qlrid, final String qlrlx) {
        if (StringUtils.isNotBlank(qlrid)) {
            Example example = new Example(BdcQlr.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("qlrid", qlrid);
            if (StringUtils.isNotBlank(qlrlx)) {
                criteria.andEqualTo(ParamsConstants.QLRLX_LOWERCASE, qlrlx);
            }
            entityMapper.deleteByExample(BdcQlr.class, example);
        }
    }

    @Override
    public BdcQlr qlrTurnProjectYwr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid) {
        bdcQlr.setProid(proid);
        bdcQlr.setQlrlx(Constants.QLRLX_YWR);
        //防止当前项目已经插入
        String qlrid = "";
        if (StringUtils.isBlank(qlrid))
            qlrid = UUIDGenerator.generate18();
        bdcQlr.setQlrid(qlrid);
        if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
            if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                bdcQlr.setQlrxz(Constants.QLRXZ_QY);
            else
                bdcQlr.setQlrxz(Constants.QLRXZ_GR);
        }
        return bdcQlr;
    }

    @Override
    public BdcQlr qlrTurnProjectJkr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid) {
        bdcQlr.setProid(proid);
        bdcQlr.setGyfs("");
        bdcQlr.setQlrlx(Constants.QLRLX_JKR);
        //防止当前项目已经插入
        String qlrid = "";

        if (StringUtils.isBlank(qlrid))
            qlrid = UUIDGenerator.generate18();
        bdcQlr.setQlrid(qlrid);
        if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
            if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                bdcQlr.setQlrxz(Constants.QLRXZ_QY);
            else
                bdcQlr.setQlrxz(Constants.QLRXZ_GR);
        }
        return bdcQlr;
    }

    @Override
    public BdcQlr qlrTurnProjectQlr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid) {
        bdcQlr.setProid(proid);
        bdcQlr.setQlrlx(Constants.QLRLX_QLR);
        //防止当前项目已经插入
        String qlrid = "";
        if (CollectionUtils.isNotEmpty(compareBdcqlrs)) {
            for (BdcQlr bdcQlrTemp : compareBdcqlrs) {
                if (bdcQlrTemp.getQlrmc() != null && bdcQlr.getQlrmc() != null && bdcQlrTemp.getQlrmc().equals(bdcQlr.getQlrmc()) &&
                        bdcQlrTemp.getQlrzjh() != null && bdcQlr.getQlrzjh() != null && bdcQlrTemp.getQlrzjh().equals(bdcQlrTemp.getQlrzjh()))
                    qlrid = bdcQlrTemp.getQlrid();
            }
        }
        if (StringUtils.isBlank(qlrid))
            qlrid = UUIDGenerator.generate18();
        bdcQlr.setQlrid(qlrid);
        if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
            if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                bdcQlr.setQlrxz(Constants.QLRXZ_QY);
            else
                bdcQlr.setQlrxz(Constants.QLRXZ_GR);
        }
        return bdcQlr;
    }

    @Override
    public BdcQlr bdcQlrTurnProjectBdcQlr(BdcQlr bdcQlr, List<BdcQlr> compareBdcqlrs, String proid) {
        bdcQlr.setProid(proid);

        //防止当前项目已经插入
        String qlrid = "";
        if (StringUtils.isBlank(qlrid))
            qlrid = UUIDGenerator.generate18();
        bdcQlr.setQlrid(qlrid);
        if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
            if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                bdcQlr.setQlrxz(Constants.QLRXZ_QY);
            else
                bdcQlr.setQlrxz(Constants.QLRXZ_GR);
        }
        return bdcQlr;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NydQlr> getNydQlrByDjh(final String djh) {
        return bdcQlrMapper.getNydQlrByDjh(djh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DjsjQszdQlr> getQszdQlrDjh(final String djh) {
        return bdcQlrMapper.getQszdQlrDjh(djh);
    }

    @Override
    public List<BdcQlr> getBdcQlrFromGdxx(List<BdcQlr> ybdcQlrList, Project project) {
        List<BdcQlr> bdcQlrList = null;
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            List<BdcQlr> tempBdcQlrs = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                    if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                        bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                    else
                        bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                }
                tempBdcQlrs.add(bdcQlr);
            }
            bdcQlrList = tempBdcQlrs;
        }
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList)
                    delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
            }
        }
        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> getBdcDyQlrFromGdxx(List<BdcQlr> ybdcDyQlrList, Project project) {
        List<BdcQlr> bdcQlrList = null;
        if (CollectionUtils.isNotEmpty(ybdcDyQlrList)) {
            List<BdcQlr> tempBdcQlrList = queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList)
                    delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_DYQR);
            }
            List<BdcQlr> tempBdcQlrs = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : ybdcDyQlrList) {
                bdcQlr = qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                bdcQlr.setQlrxz(Constants.QLRLX_DYQR);
                tempBdcQlrs.add(bdcQlr);
            }
            bdcQlrList = tempBdcQlrs;
        }

        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> setQlrxzByZjlx(final String proid) {
        List<BdcQlr> bdcQlrList = queryBdcQlrYwrByProid(proid);
        if (bdcQlrList != null) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                    bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                else
                    bdcQlr.setQlrxz(Constants.QLRXZ_GR);
            }
        }
        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> getBdcYwrFromGdxx(List<BdcQlr> ybdcYwrList, Project project) {
        List<BdcQlr> bdcYwrList = null;
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList)
                    delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
            }
            List<BdcQlr> tempBdcYwrs = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : ybdcYwrList) {
                bdcQlr = qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                    if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                        bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                    else
                        bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                }
                tempBdcYwrs.add(bdcQlr);
            }
            bdcYwrList = tempBdcYwrs;
        }
        if (CollectionUtils.isNotEmpty(bdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList)
                    delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
            }
        }
        return bdcYwrList;
    }

    @Override
    public List<BdcQlr> changeBdcQlrlxFromQlrList(List<BdcQlr> bdcQlrList, String qlrlx) {
        List<BdcQlr> returnBdcQlrList = null;
        if (CollectionUtils.isNotEmpty(bdcQlrList) && StringUtils.isNotBlank(qlrlx)) {
            returnBdcQlrList = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlr.setQlrlx(qlrlx);
                if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                    if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                        bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                    else
                        bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                }
                returnBdcQlrList.add(bdcQlr);
            }
        }
        return returnBdcQlrList;
    }

    @Override
    public List<BdcQlr> changeGdqlrYsjToZdsj(List<BdcQlr> bdcQlrList) {
        List<BdcQlr> returnBdcQlrList = null;
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            returnBdcQlrList = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : bdcQlrList) {
                if (StringUtils.isNotBlank(bdcQlr.getQlrsfzjzl())) {
                    String sfzjzldm = bdcQlrMapper.changQlrsfzjzlToDm(bdcQlr.getQlrsfzjzl());
                    if (StringUtils.isNotBlank(sfzjzldm)) {
                        bdcQlr.setQlrsfzjzl(sfzjzldm);
                    }
                }
                if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                    if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                        bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                    else
                        bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                }
                returnBdcQlrList.add(bdcQlr);
            }
        }
        return returnBdcQlrList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getGyfsByProid(final String proid) {
        return bdcQlrMapper.getGyfsByProid(proid);
    }

    @Transactional(readOnly = true)
    public List<Map> getBdcdyidByProid(final Map map) {
        return bdcQlrMapper.getBdcdyidByProid(map);
    }

    @Override
    public BdcQlr ppxmQlr(BdcQlr bdcQlr, List<BdcQlr> yqlrList) {
        if (bdcQlr != null && CollectionUtils.isNotEmpty(yqlrList)) {
            for (BdcQlr ybdcQlr : yqlrList) {
                if (StringUtils.equals(ybdcQlr.getQlrmc(), bdcQlr.getQlrmc())) {
                    bdcQlr.setQlrlxdh(ybdcQlr.getQlrlxdh());
                }
            }
        }
        return bdcQlr;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGyqk(final String proid) {
        String gyqk = "";
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.setMaximumFractionDigits(2);
        List<BdcQlr> bdcQlrList = queryBdcQlrByProid(proid);
        //共有方式按份共有中有共同共有
        if (isAfgyContainGtgy(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                if (bdcQlr != null) {
                    if (StringUtils.equals(bdcQlr.getGyfs(), "1")) continue;
                    String qlrString = "";
                    if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                        qlrString = bdcQlr.getQlrmc();
                    }

                    if (StringUtils.isNotBlank(bdcQlr.getQygyr())) {
                        HashMap gyrMap = new HashMap();
                        gyrMap.put(ParamsConstants.PROID_LOWERCASE, bdcQlr.getProid());
                        gyrMap.put(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
                        gyrMap.put("qygyr", bdcQlr.getQygyr());
                        List<BdcQlr> gyrList = queryBdcQlrList(gyrMap);
                        if (CollectionUtils.isNotEmpty(gyrList)) {
                            for (BdcQlr gyr : gyrList) {
                                if (StringUtils.isNotBlank(qlrString)) {
                                    qlrString += "、" + gyr.getQlrmc();
                                } else {
                                    qlrString = gyr.getQlrmc();
                                }
                            }
                        }
                        qlrString = "(" + qlrString + ":" + Constants.GYFS_GTGY_MC + ")";
                    }

                    String qlbl = bdcQlr.getQlbl();
                    if (StringUtils.isBlank(gyqk)) {
                        if (StringUtils.equals(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCQLR_QLBL_FS), "true")) {
                            //权利比例后去掉% 与房屋共有情况保持一致
                            if (NumberUtils.isNumber(bdcQlr.getQlbl())) {
                                gyqk = qlrString + Constants.GYFS_AFGY_MC + PublicUtil.percentage(qlbl);
                            } else {
                                gyqk = qlrString + Constants.GYFS_AFGY_MC + PublicUtil.percentage(bdcQlr.getQlbl());
                            }
                        } else {
                            gyqk = qlrString + Constants.GYFS_AFGY_MC + PublicUtil.percentage(qlbl);
                        }
                    } else {
                        if (StringUtils.equals(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCQLR_QLBL_FS), "true")) {
                            if (NumberUtils.isNumber(bdcQlr.getQlbl())) {
                                gyqk = gyqk + "," + qlrString + Constants.GYFS_AFGY_MC + PublicUtil.percentage(qlbl);
                            } else {
                                gyqk = gyqk + "," + qlrString + Constants.GYFS_AFGY_MC + PublicUtil.percentage(bdcQlr.getQlbl());
                            }
                        } else {
                            gyqk = gyqk + "," + qlrString + Constants.GYFS_AFGY_MC + PublicUtil.percentage(qlbl);
                        }
                    }
                }
            }
        } else {
            if (CollectionUtils.isNotEmpty(bdcQlrList) && bdcQlrList.get(0).getGyfs().equals("2")) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    String qlbl = bdcQlr.getQlbl();
                    if (bdcQlr != null) {
                        if (StringUtils.isBlank(gyqk)) {
                            if (StringUtils.equals(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCQLR_QLBL_FS), "true")) {
                                //权利比例后去掉% 与房屋共有情况保持一致
                                if (NumberUtils.isNumber(bdcQlr.getQlbl())) {
                                    gyqk = bdcQlr.getQlrmc() + ":" + PublicUtil.percentage(qlbl);
                                } else {
                                    gyqk = bdcQlr.getQlrmc() + ":" + PublicUtil.percentage(bdcQlr.getQlbl());
                                }
                            } else {
                                gyqk = bdcQlr.getQlrmc() + ":" + PublicUtil.percentage(qlbl);
                            }
                        } else {
                            if (StringUtils.equals(AppConfig.getProperty(CONFIGURATION_PARAMETER_BDCQLR_QLBL_FS), "true")) {
                                if (NumberUtils.isNumber(bdcQlr.getQlbl())) {
                                    gyqk = gyqk + " " + bdcQlr.getQlrmc() + ":" + PublicUtil.percentage(qlbl);
                                } else {
                                    gyqk = gyqk + " " + bdcQlr.getQlrmc() + ":" + PublicUtil.percentage(bdcQlr.getQlbl());
                                }
                            } else {
                                gyqk = gyqk + " " + bdcQlr.getQlrmc() + ":" + PublicUtil.percentage(qlbl);
                            }
                        }
                    }
                }
            } else if (CollectionUtils.isNotEmpty(bdcQlrList) && bdcQlrList.get(0).getGyfs().equals("1") && bdcQlrList.size() >= 2) {
                String qlrString = "";
                for (BdcQlr bdcQlr : bdcQlrList) {
                    if (StringUtils.isBlank(qlrString)) {
                        qlrString = bdcQlr.getQlrmc();
                    } else {
                        qlrString += "、" + bdcQlr.getQlrmc();
                    }
                }
                gyqk += qlrString;
            }
        }

        if (StringUtils.isNotBlank(gyqk) && CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlr.setGyqk(gyqk);
                saveBdcQlr(bdcQlr);
            }
        }

        return gyqk;
    }

    /**
     * @param proids 项目ids
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 根据项目类型数组删除权利人信息
     */
    @Override
    @Transactional
    public void delQlrByProids(final String[] proids) {
        bdcQlrMapper.delQlrByProids(proids);
    }

    /**
     * @param qlr 权利人对象
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 插入不动产权利人信息
     */
    @Override
    @Transactional
    public void saveDjBdcQlrxx(final BdcQlr qlr) {
        bdcQlrMapper.saveDjBdcQlrxx(qlr);
    }

    /**
     * @param qlrsfzjzl
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 把权利人里面的过渡数据转换为字典数据
     */
    @Override
    @Transactional
    public String changQlrsfzjzlToDm(final String qlrsfzjzl) {
        return bdcQlrMapper.changQlrsfzjzlToDm(qlrsfzjzl);
    }

    @Override
    public List<String> getQlrmcByProid(final String proid) {
        return bdcQlrMapper.getQlrmcByProid(proid);
    }


    /**
     * @param proid
     * @param qlrlx
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取权利人名称
     */
    @Override
    public String getQlrmcByProid(String proid, String qlrlx) {
        List<BdcQlr> bdcQlrList = Lists.newArrayList();
        if (StringUtils.equals(qlrlx, Constants.QLRLX_QLR)) {
            bdcQlrList = queryBdcQlrByProid(proid);
        }
        if (StringUtils.equals(qlrlx, Constants.QLRLX_YWR)) {
            bdcQlrList = queryBdcQlrYwrByProid(proid);
        }
        return combinationQlr(bdcQlrList);
    }

    @Override
    public BdcQlr getBdcQlrFromZh(final DjsjZhxx zhxx, BdcQlr qlr) {
        if (zhxx == null) {
            return qlr;
        }
        //存入不动产单元的实体类中
        if (qlr == null) {
            qlr = new BdcQlr();
        }
        qlr.setQlrmc(zhxx.getQlrmc());
        qlr.setQlrfddbrdh(zhxx.getFrdbdhhm());
        qlr.setQlrlxdh(zhxx.getQlrlxdh());
        qlr.setQlrdlr(zhxx.getDlrxm());
        qlr.setQlrdlrdh(zhxx.getDlrdhhm());
        qlr.setQlrfddbr(zhxx.getFrdbxm());
        qlr.setQlrsfzjzl(zhxx.getQlrzjlx());
        qlr.setQlrtxdz(zhxx.getTxdz());
        qlr.setQlrzjh(zhxx.getQlrzjh());
        if (StringUtils.isBlank(qlr.getQlrxz())) {
            if (StringUtils.equals(qlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(qlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                qlr.setQlrxz(Constants.QLRXZ_QY);
            else
                qlr.setQlrxz(Constants.QLRXZ_GR);
        }
        return qlr;
    }

    @Override
    public List<HashMap> getQlrByXmList(List<BdcXm> bdcXmList) {
        List<HashMap> hashMapList = new ArrayList<HashMap>();
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                StringBuilder qlr = new StringBuilder();
                HashMap hashMap = new HashMap();
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
                    List<BdcQlr> bdcQlrList = queryBdcQlrByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                        int i = 0;
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                                if (i == 0) {
                                    qlr.append(bdcQlr.getQlrmc());

                                } else {
                                    qlr.append(",").append(bdcQlr.getQlrmc());
                                }
                                i++;
                            }
                        }
                    }
                    hashMap.put("ID", bdcXm.getProid());
                    hashMap.put("QLR", qlr);
                    hashMapList.add(hashMap);
                }
            }
        }
        return hashMapList;
    }

    /**
     * @author <a href="mailto:zhanglili@gtmap.cn">zhanglili</a>
     * @description 通过proid和qlrlx删除
     */

    @Override
    @Transactional
    public void delBdcQlrByProid(final String proid, final String qlrlx) {
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcQlr.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            if (StringUtils.isNotBlank(qlrlx)) {
                criteria.andEqualTo(ParamsConstants.QLRLX_LOWERCASE, qlrlx);
            }
            entityMapper.deleteByExample(BdcQlr.class, example);
        }
    }

    @Override
    public List<BdcQlr> queryBdcJkrByProid(final String proid) {
        List<BdcQlr> list = new ArrayList<BdcQlr>();
        List<BdcQlr> tempList = queryBdcQlrYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(tempList)) {
            for (BdcQlr bdcQlr : tempList) {
                if (StringUtils.isNotBlank(bdcQlr.getQlrlx()) && StringUtils.equals(bdcQlr.getQlrlx(), Constants.QLRLX_JKR)) {
                    list.add(bdcQlr);
                }
            }
        }
        return list;
    }

    @Override
    public void saveQlrsByProidsAndQlrlx(List<BdcQlr> bdcQlrList, List<String> proidList, String qlrlx) {
        String updateQlr = "true";
        List<BdcQlr> bdcQlrListForUpdate = new ArrayList<BdcQlr>();
        if (CollectionUtils.isNotEmpty(bdcQlrList) && CollectionUtils.isNotEmpty(proidList)) {
            for (String proid : proidList) {
                List<BdcZsQlrRel> bdcZsQlrRelList = bdcZsQlrRelService.queryBdcZsQlrRelByProid(proid);
                //生成证书权利人关系以后，不能再更新权利人
                if (CollectionUtils.isEmpty(bdcZsQlrRelList)) {
                    //苏州新建商品房首次登记不发证时权利人变更为全体业主 不通过收件单、申请书、审批表更新权利人
                    List<BdcQlr> oldBdcQlrList = queryBdcQlrByProid(proid);
                    if (CollectionUtils.isNotEmpty(oldBdcQlrList)) {
                        BdcQlr oldBdcQlr = oldBdcQlrList.get(0);
                        if (StringUtils.isNotBlank(oldBdcQlr.getQlrmc()) &&
                                StringUtils.equals(oldBdcQlr.getQlrmc(), Constants.SPFSC_BFZ_QLRMC)) {
                            updateQlr = ParamsConstants.FALSE_LOWERCASE;
                        }
                    }
                    if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                        //当更改主proid权利人为全体业主时 不通过收件单、申请书、审批表更新权利人
                        BdcQlr primaryBdcQlr = bdcQlrList.get(0);
                        if (StringUtils.isNotBlank(primaryBdcQlr.getQlrmc()) &&
                                StringUtils.equals(primaryBdcQlr.getQlrmc(), Constants.SPFSC_BFZ_QLRMC)) {
                            updateQlr = ParamsConstants.FALSE_LOWERCASE;
                        }
                    }
                    if (!StringUtils.equals(updateQlr, ParamsConstants.FALSE_LOWERCASE)) {
                        //先删除之前的权利人，防止重复插入
                        delBdcQlrByProid(proid, qlrlx);
                        for (BdcQlr bdcQlr : bdcQlrList) {
                            try {
                                BdcQlr tempQlr = new BdcQlr();
                                BeanUtils.copyProperties(tempQlr, bdcQlr);
                                tempQlr.setQlrid(UUIDGenerator.generate18());
                                tempQlr.setProid(proid);
                                tempQlr.setQlrlx(qlrlx);
                                bdcQlrListForUpdate.add(tempQlr);
                            } catch (IllegalAccessException e) {
                                logger.error("BdcQlrServiceImpl.saveQlrsByProidsAndQlrlx", e);
                            } catch (InvocationTargetException e) {
                                logger.error("BdcQlrServiceImpl.saveQlrsByProidsAndQlrlx", e);
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(bdcQlrListForUpdate)) {
            for (BdcQlr bdcQlr : bdcQlrListForUpdate) {
                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
            }
        }
    }


    /**
     * @param proid
     * @return List<BdcQlr>
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据OntQlr初始化不动产权利人
     */
    @Override
    public List<BdcQlr> initBdcQlrFromOntQlr(final String proid) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        List<OntBdcXm> ontBdcXmList = (List<OntBdcXm>) session.getAttribute("ontBdcXm_" + proid);
        List<OntQlr> ontQlrList = (List<OntQlr>) session.getAttribute("ontQlr_" + proid);
        List<OntQlr> ontYwrList = (List<OntQlr>) session.getAttribute("ontYwr_" + proid);
        if (ontBdcXmList != null && !ontBdcXmList.isEmpty()) {
            OntBdcXm ontBdcXm = ontBdcXmList.get(0);
            String gyfs = ontBdcXm.getGyfs();
            initComplexYgQlr(bdcQlrList, ontQlrList, gyfs, proid);
            initComplexYgQlr(bdcQlrList, ontYwrList, gyfs, proid);
        }
        return bdcQlrList;
    }

    private List<BdcQlr> initComplexYgQlr(List<BdcQlr> bdcQlrList, List<OntQlr> ontQlrList, String gyfs, String proid) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        if (ontQlrList != null && !ontQlrList.isEmpty()) {
            if (session.getAttribute("lclx_" + proid) != null) {
                for (OntQlr ontQlr : ontQlrList) {
                    //通过子流程名称添加权利人
                    if (StringUtils.equals(session.getAttribute("lclx_" + proid).toString(), "YG")) {
                        if (StringUtils.indexOf(ontQlr.getZlc(), "抵押") == -1) {
                            BdcQlr bdcQlr = initOntBdcQlr(ontQlr, gyfs, proid);
                            bdcQlrList.add(bdcQlr);
                        }
                    } else if (StringUtils.equals(session.getAttribute("lclx_" + proid).toString(), "YGDY") &&
                            StringUtils.indexOf(ontQlr.getZlc(), "抵押") > -1) {
                        gyfs = Constants.GYFS_DDGY_DM;
                        BdcQlr bdcQlr = initOntBdcQlr(ontQlr, gyfs, proid);
                        bdcQlrList.add(bdcQlr);
                    }
                }
            } else {
                for (OntQlr ontQlr : ontQlrList) {
                    BdcQlr bdcQlr = initOntBdcQlr(ontQlr, gyfs, proid);
                    bdcQlrList.add(bdcQlr);
                }
            }
        }
        return bdcQlrList;
    }

    private BdcQlr initOntBdcQlr(OntQlr ontQlr, String gyfs, String proid) {
        BdcQlr bdcQlr = new BdcQlr();
        bdcQlr.setQlrid(UUIDGenerator.generate18());
        if (StringUtils.isNotBlank(proid)) {
            bdcQlr.setProid(proid);
        }
        if (StringUtils.isNotBlank(ontQlr.getQlrmc())) {
            bdcQlr.setQlrmc(ontQlr.getQlrmc());
        }
        if (StringUtils.isNotBlank(ontQlr.getQlrsfzjzl())) {
            bdcQlr.setQlrsfzjzl(ontQlr.getQlrsfzjzl());
        }
        if (StringUtils.isNotBlank(ontQlr.getQlrzjh())) {
            bdcQlr.setQlrzjh(ontQlr.getQlrzjh());
        }
        if (StringUtils.isNotBlank(ontQlr.getQlrtxdz())) {
            bdcQlr.setQlrtxdz(ontQlr.getQlrtxdz());
        }
        if (StringUtils.isNotBlank(ontQlr.getQlrlxdh())) {
            bdcQlr.setQlrlxdh(ontQlr.getQlrlxdh());
        }
        if (StringUtils.isNotBlank(gyfs)) {
            bdcQlr.setGyfs(gyfs);
        }
        if (StringUtils.isNotBlank(ontQlr.getGybl())) {
            bdcQlr.setQlbl(ontQlr.getGybl());
        }
        if (StringUtils.isNotBlank(ontQlr.getQlrlx())) {
            bdcQlr.setQlrlx(ontQlr.getQlrlx());
        }
        return bdcQlr;
    }

    @Override
    public void saveBdcQlr(BdcQlr bdcQlr) {
        entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
    }

    @Override
    public List<BdcQlr> getsfcdQlrByBdcdyh(String bdcdyh) {
        List<BdcQlr> bdcQlrLst = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            List<BdcXm> bdcXmLst = bdcXmService.queryBdcxmByBdcdyh(bdcdyh);
            if (CollectionUtils.isNotEmpty(bdcXmLst)) {
                String proid = "";
                for (BdcXm bdcXm : bdcXmLst) {
                    String sqlx = bdcXm.getSqlx();
                    if (StringUtils.isNotBlank(sqlx) && StringUtils.equals(sqlx, Constants.SQLX_SFCD) || StringUtils.equals(sqlx, Constants.SQLX_SFCD_PL)) {
                        proid = bdcXm.getProid();
                        break;
                    }
                }
                if (StringUtils.isNotBlank(proid)) {
                    bdcQlrLst = bdcQlrMapper.queryBdcQlrByProid(proid);
                }
            }
        }

        return bdcQlrLst;
    }

    @Override
    public List<BdcQlr> getBdcQlrFromGzw(final DjsjGzwxx gzwxx, BdcQlr qlr) {
        List<DjsjGzwQlr> gzwQlrList;
        List<BdcQlr> qlrList = new ArrayList<BdcQlr>();
        if (gzwxx != null && StringUtils.isNotBlank(gzwxx.getGzwDcbIndex())) {
            gzwQlrList = getGzwQlrByDcbIndex(gzwxx.getGzwDcbIndex());
            if (CollectionUtils.isNotEmpty(gzwQlrList)) {
                for (int i = 0; i < gzwQlrList.size(); i++) {
                    BdcQlr bdcQlr = new BdcQlr();
                    if (StringUtils.isNotBlank(qlr.getProid()))
                        bdcQlr.setProid(qlr.getProid());

                    bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                    if (StringUtils.isNotBlank(gzwQlrList.get(i).getQlr()))
                        bdcQlr.setQlrmc(gzwQlrList.get(i).getQlr());
                    if (StringUtils.isNotBlank(qlr.getQlrid()))
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 如果多个权利人权利人ID相同的话只能保存一个，所以这个setUUID
                     */
                        bdcQlr.setQlrid(UUIDGenerator.generate18());
                    if (StringUtils.isNotBlank(gzwQlrList.get(i).getQlrzjlx()))
                        bdcQlr.setQlrsfzjzl(gzwQlrList.get(i).getQlrzjlx());
                    if (StringUtils.isNotBlank(gzwQlrList.get(i).getQlrzjh()))
                        bdcQlr.setQlrzjh(gzwQlrList.get(i).getQlrzjh());
                    if (StringUtils.isNotBlank(gzwQlrList.get(i).getDz()))
                        bdcQlr.setQlrtxdz(gzwQlrList.get(i).getDz());
                    if (StringUtils.isBlank(bdcQlr.getQlrxz())) {
                        if (StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_ZZJG) || StringUtils.equals(bdcQlr.getQlrsfzjzl(), Constants.QLRZJHLX_YYZZ))
                            bdcQlr.setQlrxz(Constants.QLRXZ_QY);
                        else
                            bdcQlr.setQlrxz(Constants.QLRXZ_GR);
                    }
                    qlrList.add(bdcQlr);
                }
            }
        }
        return qlrList;
    }

    @Transactional(readOnly = true)
    public List<DjsjGzwQlr> getGzwQlrByDcbIndex(final String dcbIndex) {
        return bdcQlrMapper.getGzwQlrByDcbIndex(dcbIndex);
    }

    @Override
    public List<BdcQlr> getBdcQlrByQlrmcZjh(String qlrmc, String zjlx, String zjh) {
        List<BdcQlr> bdcQlrList = null;
        if (StringUtils.isNotBlank(qlrmc)) {
            Example example = new Example(BdcQlr.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("qlrmc", qlrmc);
            if (StringUtils.isNotBlank(zjlx)) {
                criteria.andEqualTo("qlrsfzjzl", zjlx);
            }
            if (StringUtils.isNotBlank(zjh)) {
                criteria.andEqualTo("qlrzjh", zjh);
            }
            bdcQlrList = entityMapper.selectByExample(BdcQlr.class, example);
        }
        return bdcQlrList;
    }

    @Override
    public BdcQlr getBdcQlrByQlrid(final String qlrid) {
        BdcQlr bdcQlr = null;
        if (StringUtils.isNotBlank(qlrid))
            bdcQlr = entityMapper.selectByPrimaryKey(BdcQlr.class, qlrid);
        return bdcQlr;
    }

    @Override
    public List<Map> ywrMcAndZjhByProid(String proid) {
        return bdcQlrMapper.ywrMcAndZjhByProid(proid);
    }

    @Override
    public List<Map> qlrMcAndZjhByProid(String proid) {
        return bdcQlrMapper.qlrMcAndZjhByProid(proid);
    }

    @Override
    public List<BdcQlr> getBdcQlrListByBdcqzh(String bdcqzh, String qlrlx) {
        List<BdcQlr> bdcQlrList = null;
        if (StringUtils.isNotBlank(bdcqzh)) {
            String proid = bdcZsService.getProidByBdcqzh(bdcqzh);
            if (StringUtils.isNotBlank(proid)) {
                HashMap hashMap = new HashMap();
                hashMap.put(ParamsConstants.PROID_LOWERCASE, proid);
                if (StringUtils.isNotBlank(qlrlx))
                    hashMap.put(ParamsConstants.QLRLX_LOWERCASE, qlrlx);
                bdcQlrList = queryBdcQlrList(hashMap);
            }
        }
        return bdcQlrList;
    }

    @Override
    public Boolean isAfgyContainGtgy(List<BdcQlr> bdcQlrList) {
        Boolean isAfgyContainGtgy = false;
        if (CollectionUtils.isNotEmpty(bdcQlrList) && bdcQlrList.size() > 2) {
            List<String> gyfsList = new ArrayList<String>();
            for (BdcQlr bdcQlr : bdcQlrList) {
                if (!gyfsList.contains(bdcQlr.getGyfs()) && StringUtils.isNotBlank(bdcQlr.getGyfs()))
                    gyfsList.add(bdcQlr.getGyfs());
            }
            if (gyfsList.size() == 2 && gyfsList.contains(Constants.GYFS_AFGY_DM) && gyfsList.contains(Constants.GYFS_GTGY_DM))
                isAfgyContainGtgy = true;
        }
        return isAfgyContainGtgy;
    }


    @Override
    public Boolean isFbczContainBcz(List<BdcQlr> bdcQlrList) {
        Boolean isFbczContainBcz = false;
        Integer isCzr = 0;
        Integer isNotCzr = 0;
        for (BdcQlr bdcQlr : bdcQlrList) {
            if (StringUtils.equals(bdcQlr.getSfczr(), "0")) {
                isNotCzr++;
            } else {
                isCzr++;
            }
        }
        if (isNotCzr > 0 && isCzr > 0) {
            isFbczContainBcz = true;
        }
        return isFbczContainBcz;
    }

    @Override
    public List<BdcQlr> getBdcQlrListByBdcXmList(List<BdcXm> bdcXmList) {
        List<BdcQlr> bdcQlrList = null;
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            bdcQlrList = bdcQlrMapper.getBdcQlrListByBdcXmList(bdcXmList);
        }
        return bdcQlrList;
    }

    @Override
    public void batchDelBdcQlrByBdcXmList(List<BdcXm> bdcXmList) {
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            bdcQlrMapper.batchDelBdcQlrByBdcXmList(bdcXmList);
        }
    }

    @Override
    public String getXsQlQlrByProid(String proid, Boolean gdCq) {
        String qlrmc = "";
        if (StringUtils.isNotBlank(proid)) {
            if (gdCq) {
                List<String> cqQlidList = bdcXmRelService.getXsSyqQlidListByBdcXmid(proid);
                if (CollectionUtils.isNotEmpty(cqQlidList)) {
                    String cqQlid = cqQlidList.get(0);
                    HashMap qlrMap = gdFwService.getGdqlr(cqQlid);
                    if (qlrMap != null && qlrMap.get("QLR") != null) {
                        qlrmc = qlrMap.get("QLR").toString();
                    }
                }
            } else {
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                    HashMap cqMap = new HashMap();
                    cqMap.put("bdcdyid", bdcXm.getBdcdyid());
                    List<String> xsProidList = bdcXmService.getXsQlProid(cqMap);
                    if (CollectionUtils.isNotEmpty(xsProidList)) {
                        List<BdcQlr> bdcQlrList = queryBdcQlrByProid(xsProidList.get(0));
                        qlrmc = combinationQlr(bdcQlrList);
                    }
                }
            }
        }
        return qlrmc;
    }

    @Override
    public List<BdcQlr> getBdcCzrListByProid(String proid) {
        List<BdcQlr> bdcCzrList = null;
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcQlr.class);
            example.createCriteria().andEqualTo("proid", proid).andEqualTo("sfczr", "1").andEqualTo("qlrlx", "qlr");
            bdcCzrList = entityMapper.selectByExample(example);
        }
        return bdcCzrList;
    }

    @Override
    public List<BdcQlr> getBdcQlrByProid(String proid, String qlrlx) {
        List<BdcQlr> bdcQlrList = Lists.newArrayList();
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcQlr.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proid", proid);
            if (StringUtils.isNotBlank(qlrlx)) {
                criteria.andEqualTo("qlrlx", qlrlx);
            }
            bdcQlrList = entityMapper.selectByExample(example);
        }
        if (CollectionUtils.isEmpty(bdcQlrList)) {
            bdcQlrList = Collections.EMPTY_LIST;
        }
        return bdcQlrList;
    }

    @Override
    public Boolean checkSameQlr(List<BdcQlr> bdcQlrList, List<BdcQlr> compareQlrList) {
        Boolean sameQlr = false;
        if (CollectionUtils.isNotEmpty(bdcQlrList) && CollectionUtils.isNotEmpty(compareQlrList)
                && bdcQlrList.size() == compareQlrList.size()) {
            int i = 0;
            for (BdcQlr bdcQlr : bdcQlrList) {
                String name = bdcQlr.getQlrmc();
                String zjh = bdcQlr.getQlrzjh();
                int j = 0;
                for (BdcQlr compareQlr : compareQlrList) {
                    String compareName = compareQlr.getQlrmc();
                    String compareZjh = compareQlr.getQlrzjh();
                    if (StringUtils.equals(name, compareName) && StringUtils.equals(zjh, compareZjh)) {
                        break;
                    }
                    j++;
                }
                if (j == compareQlrList.size()) {
                    break;
                }
                i++;
            }
            if (i == bdcQlrList.size()) {
                sameQlr = true;
            }
        }
        return sameQlr;
    }
}
