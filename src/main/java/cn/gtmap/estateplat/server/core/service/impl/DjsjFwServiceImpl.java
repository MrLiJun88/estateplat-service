package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.BdcXmRelService;
import cn.gtmap.estateplat.server.core.service.DjsjFwService;
import cn.gtmap.estateplat.server.service.currency.CurrencyService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取房屋信息
 * Created by lst on 2015/3/17.
 */
@Repository
public class DjsjFwServiceImpl implements DjsjFwService {
    @Autowired
    DjsjFwMapper djsjFwMapper;
    @Autowired
    DjSjMapper djSjMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    DjxxMapper djxxMapper;
    @Autowired
    CurrencyService currencyService;

    private static final String PARAMETER_FWDCBINDEX = "fw_dcb_index";

    @Override
    public DjsjFwxx getDjsjFwxx(final String djid) {
        DjsjFwxx djsjFwxx = null;
        String dwdm = AppConfig.getProperty("dwdm");
        if (StringUtils.isNotBlank(djid)) {
            List<String> fwlxList = djsjFwMapper.getBdcfwlxById(djid);
            String bdcdyFwlx = "";
            if (CollectionUtils.isNotEmpty(fwlxList))
                bdcdyFwlx = fwlxList.get(0);
            if (StringUtils.equalsIgnoreCase(bdcdyFwlx, Constants.DJSJ_FWDZ_DM)) {
                //多幢
                HashMap map = new HashMap();
                map.put("fw_xmxx_index", djid);
                List<DjsjFwXmxx> djsjFwXmxxList = djsjFwMapper.getDjsjFwXmxx(map);
                if (CollectionUtils.isNotEmpty(djsjFwXmxxList)) {
                    djsjFwxx = new DjsjFwxx();
                    DjsjFwXmxx djsjFwXmxx = djsjFwXmxxList.get(0);
                    djsjFwxx.setBdcdyfwlx(bdcdyFwlx);
                    djsjFwxx.setFwlx(djsjFwXmxx.getFwlx());
                    djsjFwxx.setFwxz(djsjFwXmxx.getFwxz());
                    djsjFwxx.setBdcdyh(djsjFwXmxx.getBdcdyh());
                    djsjFwxx.setCqly(djsjFwXmxx.getCqly());
                    djsjFwxx.setFttdmj(djsjFwXmxx.getFttdmj());
                    djsjFwxx.setDytdmj(djsjFwXmxx.getDytdmj());
                    djsjFwxx.setId(djid);
                    djsjFwxx.setGyqk(djsjFwXmxx.getGyqk());
                    djsjFwxx.setZl(djsjFwXmxx.getZl());
                    djsjFwxx.setLszd(djsjFwXmxx.getLszd());
                    djsjFwxx.setJyjg(djsjFwXmxx.getJyjg());

                    HashMap ljzMap = new HashMap();
                    ljzMap.put("fw_xmxx_index", djid);
                    List<DjsjFwLjz> fwLjzList = djsjFwMapper.getDjsjFwLjz(ljzMap);
                    if (CollectionUtils.isNotEmpty(fwLjzList)) {
                        List<DjsjFwzbxx> fwzbxxList = new ArrayList<DjsjFwzbxx>();
                        double dzwmj = 0;
                        for (DjsjFwLjz djsjFwLjz : fwLjzList) {
                            if (StringUtils.isNotBlank(djsjFwLjz.getFwDcbIndex())) {
                                HashMap hsMap = new HashMap();
                                hsMap.put(PARAMETER_FWDCBINDEX, djsjFwLjz.getFwDcbIndex());
                                List<DjsjFwHs> fwHsList = djsjFwMapper.getDjsjFwHs(hsMap);
                                boolean isFwSchs = true;
                                if (CollectionUtils.isEmpty(fwHsList)) {
                                    fwHsList = djsjFwMapper.getDjsjFwYcHs(hsMap);
                                    isFwSchs = false;
                                }

                                if (CollectionUtils.isNotEmpty(fwHsList)) {
                                    for (DjsjFwHs djsjFwHs : fwHsList) {
                                        DjsjFwzbxx djsjFwzbxx = new DjsjFwzbxx();
                                        djsjFwzbxx.setDh(djsjFwLjz.getDh());
                                        djsjFwzbxx.setFwjg(djsjFwLjz.getFwjg());
                                        djsjFwzbxx.setGhyt(djsjFwHs.getGhyt());
                                        djsjFwzbxx.setJgsj(djsjFwLjz.getJgrq());
                                        djsjFwzbxx.setXmmc(djsjFwLjz.getFwmc());
                                        djsjFwzbxx.setZcs(djsjFwLjz.getFwcs());

                                        djsjFwzbxx.setZts(CommonUtil.formatEmptyValue(djsjFwLjz.getZts()));
                                        if (isFwSchs) {
                                            djsjFwzbxx.setJzmj(djsjFwHs.getScjzmj());
                                            dzwmj = dzwmj + djsjFwHs.getScjzmj();
                                        } else {
                                            djsjFwzbxx.setJzmj(djsjFwHs.getYcjzmj());
                                            dzwmj = dzwmj + djsjFwHs.getYcjzmj();
                                        }
                                        djsjFwxx.setFwyt(djsjFwLjz.getFwyt());

                                        if (StringUtils.equals(dwdm, Constants.DWDM_SZ)) {
                                            djsjFwxx.setFcdah(djsjFwHs.getFwbm());
                                            djsjFwzbxx.setBdcdybh(djsjFwHs.getFwbm());
                                        } else {
                                            djsjFwxx.setFcdah(djsjFwHs.getFcdah());
                                            djsjFwzbxx.setBdcdybh(djsjFwHs.getFcdah());
                                        }

                                        fwzbxxList.add(djsjFwzbxx);
                                    }
                                } else {
                                    DjsjFwzbxx djsjFwzbxx = new DjsjFwzbxx();
                                    djsjFwzbxx.setDh(djsjFwLjz.getDh());
                                    djsjFwzbxx.setFwjg(djsjFwLjz.getFwjg());
                                    djsjFwzbxx.setGhyt(djsjFwLjz.getFwyt());
                                    djsjFwzbxx.setJgsj(djsjFwLjz.getJgrq());
                                    djsjFwzbxx.setXmmc(djsjFwLjz.getFwmc());
                                    djsjFwzbxx.setZcs(djsjFwLjz.getFwcs());

                                    djsjFwzbxx.setZts(CommonUtil.formatEmptyValue(djsjFwLjz.getZts()));
                                    djsjFwzbxx.setJzmj(djsjFwLjz.getScjzmj());
                                    djsjFwxx.setFwyt(djsjFwLjz.getFwyt());

                                    fwzbxxList.add(djsjFwzbxx);
                                }
                            } else {
                                DjsjFwzbxx djsjFwzbxx = new DjsjFwzbxx();
                                djsjFwzbxx.setDh(djsjFwLjz.getDh());
                                djsjFwzbxx.setFwjg(djsjFwLjz.getFwjg());
                                djsjFwzbxx.setGhyt(djsjFwLjz.getFwyt());
                                djsjFwzbxx.setJgsj(djsjFwLjz.getJgrq());
                                djsjFwzbxx.setXmmc(djsjFwLjz.getFwmc());
                                djsjFwzbxx.setZcs(djsjFwLjz.getFwcs());

                                djsjFwzbxx.setZts(CommonUtil.formatEmptyValue(djsjFwLjz.getZts()));
                                djsjFwzbxx.setJzmj(djsjFwLjz.getScjzmj());
                                djsjFwxx.setFwyt(djsjFwLjz.getFwyt());

                                fwzbxxList.add(djsjFwzbxx);
                            }
                        }

                        djsjFwxx.setFwzbxxList(fwzbxxList);
                        djsjFwxx.setJzmj(dzwmj);
                    }
                }
            } else if (StringUtils.equalsIgnoreCase(bdcdyFwlx, Constants.DJSJ_FW_DM)) {
                //独幢
                HashMap ljzMap = new HashMap();
                ljzMap.put(PARAMETER_FWDCBINDEX, djid);
                List<DjsjFwLjz> fwLjzList = djsjFwMapper.getDjsjFwLjz(ljzMap);
                if (CollectionUtils.isNotEmpty(fwLjzList)) {
                    DjsjFwLjz djsjFwLjz = fwLjzList.get(0);
                    djsjFwxx = new DjsjFwxx();
                    djsjFwxx.setBdcdyfwlx(bdcdyFwlx);
                    djsjFwxx.setBdcdyh(djsjFwLjz.getBdcdyh());

                    djsjFwxx.setId(djid);
                    djsjFwxx.setZl(djsjFwLjz.getZldz());
                    djsjFwxx.setLszd(djsjFwLjz.getLszd());

                    DjsjFwzbxx djsjFwzbxx = new DjsjFwzbxx();
                    djsjFwzbxx.setDh(djsjFwLjz.getDh());
                    djsjFwzbxx.setFwjg(djsjFwLjz.getFwjg());
                    djsjFwzbxx.setGhyt(djsjFwLjz.getFwyt());
                    djsjFwzbxx.setJgsj(djsjFwLjz.getJgrq());
                    djsjFwzbxx.setZcs(djsjFwLjz.getFwcs());
                    djsjFwxx.setFwyt(djsjFwLjz.getFwyt());
                    //zwq 独幢的时候无法获取定着物面积
                    djsjFwxx.setJzmj(djsjFwLjz.getScjzmj());
                    djsjFwzbxx.setJzmj(djsjFwLjz.getScjzmj());

                    List<DjsjFwHs> djsjFwhsList = djsjFwMapper.getDjsjFwHs(ljzMap);
                    if (CollectionUtils.isNotEmpty(djsjFwhsList)) {
                        DjsjFwHs djsjFwHs = djsjFwhsList.get(0);
                        djsjFwxx.setCqly(djsjFwHs.getCqly());
                        djsjFwxx.setGyqk(djsjFwHs.getGyqk());
                        // zwq独幢的时候,dzwyt取fw_ljz的fwyt
                        djsjFwxx.setFwxz(djsjFwHs.getFwxz());
                        djsjFwxx.setDycs(djsjFwHs.getDycs());

                        if (StringUtils.equals(dwdm, Constants.DWDM_SZ))
                            djsjFwxx.setFcdah(djsjFwHs.getFwbm());
                        else
                            djsjFwxx.setFcdah(djsjFwHs.getFcdah());

                        djsjFwxx.setFwlx(djsjFwHs.getFwlx());
                    }

                    List<DjsjFwHs> djsjFwhsMjList = djsjFwMapper.getDjsjFwHsSumMj(ljzMap);
                    if (CollectionUtils.isNotEmpty(djsjFwhsMjList)) {
                        double dytdmj = 0;
                        double fttdmj = 0;
                        double tnjzmj = 0;
                        for (DjsjFwHs djsjFwHs : djsjFwhsMjList) {
                            dytdmj = dytdmj + djsjFwHs.getDytdmj();
                            fttdmj = fttdmj + djsjFwHs.getFttdmj();
                            tnjzmj = tnjzmj + djsjFwHs.getSctnjzmj();
                            djsjFwxx.setJyjg(djsjFwHs.getJyjg());
                            // zwq独幢的时候,dzwmj取fw_ljz的scmj
                        }
                        djsjFwxx.setFttdmj(fttdmj);
                        djsjFwxx.setTnjzmj(tnjzmj);
                    }

                    //独幢房屋独用土地面积读取DJSJ_ZD_DCB.fzmj
                    if (StringUtils.isNotBlank(djsjFwLjz.getBdcdyh()) && djsjFwLjz.getBdcdyh().length() > 19) {
                        List<DjsjZdxx> djsjZdxxList = djSjMapper.getDjsjZdxxByDjh(djsjFwLjz.getBdcdyh().substring(0, 19));
                        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                            DjsjZdxx djsjZdxx = djsjZdxxList.get(0);
                            if (djsjZdxx != null && djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() > 0) {
                                double dytdmj = djsjZdxx.getFzmj();
                                djsjFwxx.setDytdmj(dytdmj);
                            }
                        }
                    }

                    List<DjsjFwzbxx> fwzbxxList = new ArrayList<DjsjFwzbxx>();
                    fwzbxxList.add(djsjFwzbxx);
                    djsjFwxx.setFwzbxxList(fwzbxxList);
                }
            } else if (StringUtils.equalsIgnoreCase(bdcdyFwlx, Constants.DJSJ_FWHS_DM)) {
                //戶室
                HashMap hsMap = new HashMap();
                hsMap.put("fw_hs_index", djid);
                List<DjsjFwHs> fwHsList = djsjFwMapper.getDjsjFwHs(hsMap);
                if (CollectionUtils.isEmpty(fwHsList)) {
                    fwHsList = djsjFwMapper.getDjsjFwYcHs(hsMap);
                }
                if (CollectionUtils.isNotEmpty(fwHsList)) {
                    DjsjFwHs djsjFwHs = fwHsList.get(0);
                    djsjFwxx = new DjsjFwxx();
                    djsjFwxx.setBdcdyfwlx(bdcdyFwlx);
                    djsjFwxx.setFwlx(djsjFwHs.getFwlx());
                    djsjFwxx.setBdcdyh(djsjFwHs.getBdcdyh());

                    djsjFwxx.setId(djid);
                    djsjFwxx.setZl(djsjFwHs.getZl());
                    djsjFwxx.setLszd(djsjFwHs.getLszd());
                    djsjFwxx.setCqly(djsjFwHs.getCqly());
                    djsjFwxx.setGyqk(djsjFwHs.getGyqk());
                    djsjFwxx.setFwxz(djsjFwHs.getFwxz());

                    djsjFwxx.setDycs(djsjFwHs.getDycs());
                    djsjFwxx.setJyjg(djsjFwHs.getJyjg());
                    djsjFwxx.setJzmj(djsjFwHs.getScjzmj());
                    //zwq 从fw_hs获取ycjzmj
                    djsjFwxx.setYcjzmj(djsjFwHs.getYcjzmj());
                    djsjFwxx.setFtjzmj(djsjFwHs.getScftjzmj());
                    djsjFwxx.setDytdmj(djsjFwHs.getDytdmj());
                    djsjFwxx.setFttdmj(djsjFwHs.getFttdmj());

                    if (StringUtils.equals(dwdm, Constants.DWDM_SZ))
                        djsjFwxx.setFcdah(djsjFwHs.getFwbm());
                    else
                        djsjFwxx.setFcdah(djsjFwHs.getFcdah());

                    djsjFwxx.setFwyt(djsjFwHs.getGhyt());
                    djsjFwxx.setGhyt(djsjFwHs.getGhyt());
                    djsjFwxx.setTnjzmj(djsjFwHs.getSctnjzmj());
                    djsjFwxx.setCg(djsjFwHs.getCg());

                    //从fw_hs获取tdyt，qsrq，zzrq
                    djsjFwxx.setTdyt(djsjFwHs.getTdyt());
                    djsjFwxx.setQsrq(djsjFwHs.getQsrq());
                    djsjFwxx.setZzrq(djsjFwHs.getZzrq());
                    djsjFwxx.setScglmj(djsjFwHs.getScglmj());
                    djsjFwxx.setScdxsmj(djsjFwHs.getScdxsmj());
                    djsjFwxx.setTdsyqlx(djsjFwHs.getTdsyqlx());

                    djsjFwxx.setSxh(djsjFwHs.getSxh());
                    HashMap ljzMap = new HashMap();
                    ljzMap.put(PARAMETER_FWDCBINDEX, djsjFwHs.getFwDcbIndex());
                    List<DjsjFwLjz> djsjFwLjzList = djsjFwMapper.getDjsjFwLjz(ljzMap);
                    List<DjsjFwzbxx> fwzbxxList = new ArrayList<DjsjFwzbxx>();
                    if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
                        DjsjFwLjz djsjFwLjz = djsjFwLjzList.get(0);
                        DjsjFwzbxx djsjFwzbxx = new DjsjFwzbxx();
                        djsjFwzbxx.setFwjg(djsjFwLjz.getFwjg());
                        djsjFwzbxx.setJgsj(djsjFwLjz.getJgrq());
                        djsjFwzbxx.setZcs(djsjFwLjz.getFwcs());
                        djsjFwzbxx.setJzmj(djsjFwHs.getScjzmj());
                        djsjFwzbxx.setGhyt(djsjFwHs.getGhyt());
                        djsjFwzbxx.setDh(djsjFwLjz.getDh());
                        fwzbxxList.add(djsjFwzbxx);
                    }

                    djsjFwxx.setFwzbxxList(fwzbxxList);
                }
            }

        }
        return djsjFwxx;
    }

    @Override
    public DjsjBdcdy getDjsjBdcdyByDjid(final String djid) {
        DjsjBdcdy djsjBdcdy = null;
        if (StringUtils.isNotBlank(djid)) {
            Example example = new Example(DjsjBdcdy.class);
            example.createCriteria().andEqualTo("id", djid);
            List<DjsjBdcdy> list = entityMapper.selectByExample(DjsjBdcdy.class, example);
            if (CollectionUtils.isNotEmpty(list)) {
                djsjBdcdy = list.get(0);
            }
        }
        return djsjBdcdy;
    }

    @Override
    public List<DjsjFwXmxx> getDjsjFwXmxx(Map map) {
        return djsjFwMapper.getDjsjFwXmxx(map);
    }

    @Override
    public List<DjsjFwLjz> getDjsjFwLjz(Map map) {
        return djsjFwMapper.getDjsjFwLjz(map);
    }

    @Override
    public List<DjsjFwHs> getDjsjFwHs(Map map) {
        return djsjFwMapper.getDjsjFwHs(map);
    }

    @Override
    public List<Map> getDjsjFwHsByMap(Map map) {
        return djsjFwMapper.getDjsjFwHsByMap(map);
    }

    @Override
    public List<DjsjFwHs> getDjsjFwYcHs(Map map) {
        return djsjFwMapper.getDjsjFwYcHs(map);
    }

    @Override
    public String getFwlxByProid(final String proid) {
        String fwlx = "";
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
        DjsjFwxx djsjFwxx = null;
        if (CollectionUtils.isNotEmpty(bdcXmRelList) && StringUtils.isNotBlank(bdcXmRelList.get(0).getQjid())) {
            djsjFwxx = getDjsjFwxx(bdcXmRelList.get(0).getQjid());
            if (djsjFwxx != null)
                fwlx = djsjFwxx.getBdcdyfwlx();
        }
        return fwlx;
    }

    @Override
    public String getDjidByBdcdyh(String bdcdyh) {
        String djid = "";
        if (StringUtils.isNotBlank(bdcdyh)) {
            djid = djsjFwMapper.getDjidByBdcdyh(bdcdyh);
        }
        return djid;
    }

    @Override
    public String getFwlxByDjid(final String djid) {
        String fwlx = "";
        if (StringUtils.isNotBlank(djid)) {
            DjsjFwxx djsjFwxx = null;
            djsjFwxx = getDjsjFwxx(djid);
            if (djsjFwxx != null)
                fwlx = djsjFwxx.getBdcdyfwlx();
        }
        return fwlx;
    }


    /**
     * @param
     * @return
     * @author bianwen
     * @description 根据不动产单元取预测户室（在建工程抵押）
     */
    @Override
    public List<Map> getDjsjYcFwHsForZjgcdy(HashMap paraMap) {
        return djsjFwMapper.getDjsjYcFwHsForZjgcdy(paraMap);  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param djid 地籍信息主键
     * @author bianwen
     * @rerutn
     * @description 针对选择预测户室数据，根据房屋预测户室获取房屋类型
     */
    @Override
    public DjsjFwxx getDjsjFwxxByFwychs(String djid) {
        DjsjFwxx djsjFwxx = null;
        String dwdm = AppConfig.getProperty("dwdm");
        if (StringUtils.isNotBlank(djid)) {
            //戶室
            HashMap hsMap = new HashMap();
            hsMap.put("fw_hs_index", djid);
            List<DjsjFwHs> fwHsList = djsjFwMapper.getDjsjFwYcHs(hsMap);
            if (CollectionUtils.isNotEmpty(fwHsList)) {
                DjsjFwHs djsjFwHs = fwHsList.get(0);
                if (djsjFwHs != null && StringUtils.isNotBlank(djsjFwHs.getFwDcbIndex())) {
                    HashMap ljzMap = new HashMap();
                    ljzMap.put(PARAMETER_FWDCBINDEX, djsjFwHs.getFwDcbIndex());
                    List<DjsjFwLjz> djsjFwLjzList = djsjFwMapper.getDjsjFwLjz(ljzMap);
                    if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
                        DjsjFwLjz djsjFwLjz = djsjFwLjzList.get(0);
                        if (djsjFwLjz != null && StringUtils.equals(djsjFwLjz.getBdcdyfwlx(), Constants.DJSJ_FWHS_DM)) {
                            djsjFwxx = new DjsjFwxx();
                            djsjFwxx.setBdcdyfwlx(Constants.DJSJ_FWHS_DM);
                            djsjFwxx.setFwlx(djsjFwHs.getFwlx());
                            djsjFwxx.setBdcdyh(djsjFwHs.getBdcdyh());

                            djsjFwxx.setId(djid);
                            djsjFwxx.setZl(djsjFwHs.getZl());
                            djsjFwxx.setLszd(djsjFwHs.getLszd());
                            djsjFwxx.setCqly(djsjFwHs.getCqly());
                            djsjFwxx.setGyqk(djsjFwHs.getGyqk());
                            djsjFwxx.setFwxz(djsjFwHs.getFwxz());

                            djsjFwxx.setDycs(djsjFwHs.getDycs());
                            djsjFwxx.setJyjg(djsjFwHs.getJyjg());
                            djsjFwxx.setJzmj(null);
                            //zwq 从fw_hs获取ycjzmj
                            djsjFwxx.setYcjzmj(djsjFwHs.getYcjzmj());
                            djsjFwxx.setFtjzmj(djsjFwHs.getYcftjzmj());
                            djsjFwxx.setDytdmj(djsjFwHs.getDytdmj());
                            djsjFwxx.setFttdmj(djsjFwHs.getFttdmj());

                            if (StringUtils.equals(dwdm, Constants.DWDM_SZ))
                                djsjFwxx.setFcdah(djsjFwHs.getFwbm());
                            else
                                djsjFwxx.setFcdah(djsjFwHs.getFcdah());

                            djsjFwxx.setFwyt(djsjFwHs.getGhyt());
                            djsjFwxx.setTnjzmj(djsjFwHs.getYctnjzmj());
                            djsjFwxx.setCg(djsjFwHs.getCg());

                            List<DjsjFwzbxx> fwzbxxList = new ArrayList<DjsjFwzbxx>();
                            DjsjFwzbxx djsjFwzbxx = new DjsjFwzbxx();
                            djsjFwzbxx.setFwjg(djsjFwLjz.getFwjg());
                            djsjFwzbxx.setJgsj(djsjFwLjz.getJgrq());
                            djsjFwzbxx.setZcs(djsjFwLjz.getFwcs());
                            djsjFwzbxx.setJzmj(djsjFwHs.getYcjzmj());
                            djsjFwzbxx.setGhyt(djsjFwHs.getGhyt());
                            fwzbxxList.add(djsjFwzbxx);

                            djsjFwxx.setFwzbxxList(fwzbxxList);
                        }
                    }
                }
            }

        }
        return djsjFwxx;
    }

    @Override
    public List<Map> getLpbList(Map map) {
        return djxxMapper.getLpbList(map);
    }

    @Override
    public List<DjsjFwHs> getDjsjFwHsListByBdcdyh(String bdcdyh) {
        List<DjsjFwHs> djsjFwHsList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            Example example = new Example(DjsjFwHs.class);
            example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            djsjFwHsList = entityMapper.selectByExampleNotNull(example);
        }
        return djsjFwHsList;
    }

    @Override
    public List<DjsjFwHs> getDzFwHsListByBdcdyh(String bdcdyh) {
        List<DjsjFwHs> djsjFwHsList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            Map queryMap = new HashMap();
            queryMap.put("bdcdyh", bdcdyh);
            djsjFwHsList = djsjFwMapper.getDzFwHs(queryMap);
        }
        return djsjFwHsList;
    }

    @Override
    public List<DjsjFwHs> getDjsjFwHsByFwbm(String fwbm) {
        List<DjsjFwHs> djsjFwHsList = null;
        if (StringUtils.isNotBlank(fwbm)) {
            djsjFwHsList = djsjFwMapper.getDjsjFwHsByFwbm(fwbm);
        }
        return djsjFwHsList;
    }

    @Override
    public Map getFwBazt(List<DjsjFwHs> djsjFwHsList) {
        Map map = new HashMap();
        if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
            List<Map> mapList = new ArrayList<>();
            for (DjsjFwHs djsjFwHs : djsjFwHsList) {
                if (StringUtils.isNotBlank(djsjFwHs.getFwbm())) {
                    Map checkMap = new HashMap();
                    checkMap.put("houseCode", djsjFwHs.getFwbm());
                    mapList.add(checkMap);
                }
            }
            if (CollectionUtils.isNotEmpty(mapList)) {
                String result = currencyService.checkHouseZt(mapList);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.containsKey("statuscode") && StringUtils.equals(String.valueOf(jsonObject.get("statuscode")), "0") && jsonObject.containsKey("housecodelist")) {
                        List<JSONObject> housecodelist = (List<JSONObject>) jsonObject.get("housecodelist");
                        if (CollectionUtils.isNotEmpty(housecodelist)) {
                            for (JSONObject object : housecodelist) {
                                if (object.containsKey("housecode") && StringUtils.isNotBlank((CharSequence) object.get("housecode")) && object.containsKey("housestatus") && StringUtils.isNotBlank((CharSequence) object.get("housestatus"))) {
                                    map.put(object.get("housecode"), object.get("housestatus"));
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }
}
