package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.BdcQlr;
import cn.gtmap.estateplat.model.server.core.GdFw;
import cn.gtmap.estateplat.model.server.core.GdQlr;
import cn.gtmap.estateplat.model.server.core.GdTd;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.core.service.GdQlrService;
import cn.gtmap.estateplat.server.core.mapper.GdQlrMapper;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 过渡权利人
 * <p/>
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-10
 */
@Service
public class GdQlrServiceImpl implements GdQlrService {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    GdQlrMapper gdQlrMapper;
    @Autowired
    BdcZdGlService bdcZdGlService;

    @Override
    public List<GdQlr> queryGdQlrs(String qlid, String qlrlx){
        List<GdQlr> list= null;
        if (StringUtils.isNotBlank(qlid)) {
            //zq 合并流程 qlid会以逗号相隔展示
            String []qlidarr = qlid.split(",");
            for(String qlidtemp:qlidarr){
                Example qlrTemp = new Example(GdQlr.class);
                Example.Criteria criteria = qlrTemp.createCriteria();
                criteria.andEqualTo("qlid", qlidtemp);
                if (StringUtils.isNotBlank(qlrlx)) {
                    criteria.andEqualTo(ParamsConstants.QLRLX_LOWERCASE, qlrlx);
                }
                list = new ArrayList<GdQlr>();
                if(CollectionUtils.isNotEmpty( qlrTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qlrTemp.getOredCriteria().get(0).getAllCriteria())) {
                    List<GdQlr> listtemp = entityMapper.selectByExample(qlrTemp);

                    if (CollectionUtils.isNotEmpty(listtemp)) {
                        List<GdQlr> gdqlrs = new LinkedList<GdQlr>();
                        for (GdQlr gdqlr:listtemp){
                            String gdqlrstr = gdqlr.getQlr();
                            if(StringUtils.isNotBlank(gdqlrstr)){
                                gdqlrstr = gdqlrstr.trim();
                                if(gdqlrstr.indexOf(',')>0 || gdqlrstr.indexOf('、')>0
                                        || gdqlrstr.indexOf(' ')>0 || gdqlrstr.indexOf('/')>0 || gdqlrstr.indexOf(';')>0){
                                    String zjhstr = gdqlr.getQlrzjh();
                                    if(StringUtils.isNotBlank(zjhstr)){
                                        zjhstr = zjhstr.trim();
                                    }else{
                                        zjhstr = "";
                                    }
                                    String[] qlrarr = gdSeg(gdqlrstr);
                                    String[] zjharr = new String[qlrarr.length];
                                    String[] zjharr1 = gdSeg(zjhstr);
                                    for(int t = 0; t<zjharr1.length; t++){
                                        zjharr[t]= zjharr1[t];
                                    }
                                    if(qlrarr.length==zjharr1.length){
                                        for(int a = 0; a<qlrarr.length; a++){
                                            GdQlr gdqlrtemp = new GdQlr();
                                            gdqlrtemp.setQlrid(gdqlr.getQlrid());
                                            gdqlrtemp.setQlrsfzjzl(gdqlr.getQlrsfzjzl());
                                            gdqlrtemp.setQlid(gdqlr.getQlid());
                                            gdqlrtemp.setProid(gdqlr.getProid());
                                            gdqlrtemp.setQlrlx(gdqlr.getQlrlx());
                                            gdqlrtemp.setQlr(qlrarr[a]);
                                            gdqlrtemp.setQlrzjh(zjharr[a]);
                                            gdqlrs.add(gdqlrtemp);
                                        }
                                    }
                                }
                            }
                        }
                        if(gdqlrs.size() == 0) {
                            list.addAll(listtemp);
                        }else{
                            list.addAll(gdqlrs);
                        }
                    }
                }
            }
        }
        return list;
    }

    //将过渡权利人和证件号分割
    public String[] gdSeg (String str){
        String[] gdstr_1 = str.split(",");
        String[] gdstr_2 = str.split(" +");
        String[] gdstr_3 = str.split("、");
        String[] gdstr_4 = str.split("/");
        String[] gdstr_5 = str.split(";");
        String[] gdstr = gdstr_1;
        if(gdstr.length < gdstr_2.length) {
            gdstr = gdstr_2;
        }
        if(gdstr.length < gdstr_3.length) {
            gdstr = gdstr_3;
        }
        if(gdstr.length < gdstr_4.length) {
            gdstr = gdstr_4;
        }
        if(gdstr.length < gdstr_5.length) {
            gdstr = gdstr_5;
        }
        return gdstr;
    }
    @Override
    public List<BdcQlr> readGdQlrs(List<GdQlr> gdQlrs) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (CollectionUtils.isNotEmpty(gdQlrs)) {
            for (GdQlr gdQlr : gdQlrs) {
                BdcQlr bdcQlr = null;
                if (gdQlr != null) {
                    bdcQlr = new BdcQlr();
                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                    if (gdQlr.getSxh() == 0) {
                        bdcQlr.setSxh(2);
                    } else {
                        bdcQlr.setSxh(gdQlr.getSxh());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        bdcQlr.setQlrmc(gdQlr.getQlr());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrzjh())) {
                        bdcQlr.setQlrzjh(gdQlr.getQlrzjh());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrsfzjzl())){
                        String qlrsfzjzl = bdcZdGlService.getZjzlByMc(gdQlr.getQlrsfzjzl());
                        bdcQlr.setQlrsfzjzl(qlrsfzjzl);
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrlx())) {
                        bdcQlr.setQlrlx(gdQlr.getQlrlx());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrxz())) {
                        bdcQlr.setQlrxz(gdQlr.getQlrxz());
                    }
                    //zhouwanqing 如果是2个权利人，则共同共有，有比例的话是按份共有，只有一个权利人的话是单独所有
                    if (gdQlrs.size() > 1) {

                        if (StringUtils.isNotBlank(gdQlr.getQlbl())) {
                            Double qlbl = CommonUtil.getGybl(gdQlr.getQlbl());
                            if (qlbl != null) {
                                //继承分数
                                if(StringUtils.equals(AppConfig.getProperty("bdcqlr.qlbl.fs"),"true")) {
                                    bdcQlr.setQlbl(gdQlr.getQlbl());
                                } else {
                                    bdcQlr.setQlbl(qlbl.toString());
                                }
                                if (CommonUtil.getGybl(gdQlr.getQlbl()) < 1) {
                                    bdcQlr.setGyfs("2");
                                }
                            }
                        } else {
                            bdcQlr.setGyfs("1");
                        }
                    } else {
                        bdcQlr.setGyfs("0");
                    }
                    bdcQlrList.add(bdcQlr);
                }
            }
        }

        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> readGdFwQlrs(List<GdQlr> gdQlrs, GdFw gdFw, String proid) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (CollectionUtils.isNotEmpty(gdQlrs)) {
            for (GdQlr gdQlr : gdQlrs) {
                BdcQlr bdcQlr = null;
                if (gdQlr != null) {
                    bdcQlr = new BdcQlr();
                    if (gdQlr.getSxh() == 0) {
                        bdcQlr.setSxh(2);
                    } else {
                        bdcQlr.setSxh(gdQlr.getSxh());
                    }
                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                    if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        bdcQlr.setQlrmc(gdQlr.getQlr());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrzjh())) {
                        bdcQlr.setQlrzjh(gdQlr.getQlrzjh());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrsfzjzl())) {
                        bdcQlr.setQlrsfzjzl(gdQlr.getQlrsfzjzl());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrlx())) {
                        bdcQlr.setQlrlx(gdQlr.getQlrlx());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrxz())) {
                        bdcQlr.setQlrxz(gdQlr.getQlrxz());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlbl())) {
                        bdcQlr.setQlbl(CommonUtil.getGybl(gdQlr.getQlbl()).toString());
                        if (gdQlrs.size() > 1) {
                            if (CommonUtil.getGybl(gdQlr.getQlbl()) < 1) {
                                bdcQlr.setGyfs("2");
                            } else {
                                bdcQlr.setGyfs("1");
                            }
                        } else {
                            bdcQlr.setGyfs("0");
                        }
                    }
                    if (gdFw != null && StringUtils.isNotBlank(gdFw.getGyqk())) {
                        bdcQlr.setGyfs(gdFw.getGyqk());
                    }
                    bdcQlr.setProid(proid);
                    bdcQlrList.add(bdcQlr);
                }
            }
        }

        return bdcQlrList;
    }

    @Override
    public List<BdcQlr> readGdTdQlrs(List<GdQlr> gdQlrs, GdTd gdTd) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (CollectionUtils.isNotEmpty(gdQlrs)) {
            for (GdQlr gdQlr : gdQlrs) {
                BdcQlr bdcQlr = null;
                if (gdQlr != null) {
                    bdcQlr = new BdcQlr();
                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                    if (gdQlr.getSxh() == 0) {
                        bdcQlr.setSxh(2);
                    } else {
                        bdcQlr.setSxh(gdQlr.getSxh());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        bdcQlr.setQlrmc(gdQlr.getQlr());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrzjh())) {
                        bdcQlr.setQlrzjh(gdQlr.getQlrzjh());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrsfzjzl())) {
                        bdcQlr.setQlrsfzjzl(gdQlr.getQlrsfzjzl());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrlx())) {
                        bdcQlr.setQlrlx(gdQlr.getQlrlx());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlrxz())) {
                        bdcQlr.setQlrxz(gdQlr.getQlrxz());
                    }
                    if (StringUtils.isNotBlank(gdQlr.getQlbl())) {
                        bdcQlr.setQlbl(CommonUtil.getGybl(gdQlr.getQlbl()).toString());
                        if (gdQlrs.size() > 1) {
                            if (CommonUtil.getGybl(gdQlr.getQlbl()) < 1) {
                                bdcQlr.setGyfs("2");
                            } else {
                                bdcQlr.setGyfs("1");
                            }
                        } else {
                            bdcQlr.setGyfs("0");
                        }
                    }
                    if (gdTd != null && StringUtils.isNotBlank(gdTd.getGyqk())) {
                        bdcQlr.setGyfs(gdTd.getGyqk());
                    }
                    bdcQlrList.add(bdcQlr);
                }
            }
        }


        return bdcQlrList;
    }

    @Override
    public void delGdQlrByQlid(String qlid) {
        gdQlrMapper.delGdQlrByQlid(qlid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdQlr> queryGdQlrListByProid(String proid, String qlrlx) {
        List<GdQlr> list = null;
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(qlrlx)) {
            Example qlrTemp = new Example(GdQlr.class);
            Example.Criteria criteria = qlrTemp.createCriteria();
            criteria.andEqualTo("proid", proid).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, qlrlx);
            list = entityMapper.selectByExample(GdQlr.class, qlrTemp);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGdQlrsByProid(String proid, String qlrlx) {
        String qlrs = "";
        List<GdQlr> gdQlrList = queryGdQlrListByProid(proid, qlrlx);
        if (CollectionUtils.isNotEmpty(gdQlrList)) {
            if (CollectionUtils.size(gdQlrList) > 3) {
                for (GdQlr gdQlr : gdQlrList) {
                    if (StringUtils.isNotBlank(qlrs) && StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = qlrs + "," + gdQlr.getQlr();
                    } else if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = gdQlr.getQlr();
                    }
                }
                if (StringUtils.isNotBlank(qlrs)) {
                    qlrs += "等";
                }
            } else {
                for (GdQlr gdQlr : gdQlrList) {
                    if (StringUtils.isNotBlank(qlrs) && StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = qlrs + "," + gdQlr.getQlr();
                    } else if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = gdQlr.getQlr();
                    }
                }
            }
        }
        return qlrs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GdQlr> queryGdQlrsByQlr(String qlr, String qlrlx) {
        List<GdQlr> list = null;
        if (StringUtils.isNotBlank(qlr)) {
            Example qlrTemp = new Example(GdQlr.class);
            Example.Criteria criteria = qlrTemp.createCriteria();
            criteria.andLike("qlr", "%" + qlr + "%");
            if (StringUtils.isNotBlank(qlrlx)) {
                criteria.andEqualTo(ParamsConstants.QLRLX_LOWERCASE, qlrlx);
            }
            if(CollectionUtils.isNotEmpty( qlrTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qlrTemp.getOredCriteria().get(0).getAllCriteria())) {
                list = entityMapper.selectByExample(GdQlr.class, qlrTemp);
            }
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public String getGdQlrsByQlid(String qlid, String qlrlx) {
        String qlrs = "";
        List<GdQlr> gdQlrList = queryGdQlrs(qlid, qlrlx);
        if (CollectionUtils.isNotEmpty(gdQlrList)) {
            if (CollectionUtils.size(gdQlrList) > 3) {
                for (GdQlr gdQlr : gdQlrList) {
                    if (StringUtils.isNotBlank(qlrs) && StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = qlrs + "," + gdQlr.getQlr();
                    } else if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = gdQlr.getQlr();
                    }
                }
                if (StringUtils.isNotBlank(qlrs)) {
                    qlrs += "等";
                }
            } else {
                for (GdQlr gdQlr : gdQlrList) {
                    if (StringUtils.isNotBlank(qlrs) && StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = qlrs + "," + gdQlr.getQlr();
                    } else if (StringUtils.isNotBlank(gdQlr.getQlr())) {
                        qlrs = gdQlr.getQlr();
                    }
                }
            }
        }
        return qlrs;
    }

    @Override
    public String combinationQlr(List<GdQlr> gdQlrList) {
        StringBuilder qlr = new StringBuilder();
        if (CollectionUtils.isNotEmpty(gdQlrList)) {
            for (GdQlr gdQlr : gdQlrList) {
                if (StringUtils.isBlank(qlr)) {
                    qlr.append(gdQlr.getQlr());
                } else {
                    qlr.append("、").append(gdQlr.getQlr());
                }
            }
        }
        return qlr.toString();
    }
}
