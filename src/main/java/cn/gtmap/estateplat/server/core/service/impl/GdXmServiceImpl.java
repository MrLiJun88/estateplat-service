package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.GdTdMapper;
import cn.gtmap.estateplat.server.core.mapper.GdXmMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 过渡项目
 * User: Administrator
 * Date: 15-8-28
 * Time: 上午9:15
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GdXmServiceImpl implements GdXmService {
    @Autowired
    GdXmMapper gdXmMapper;
    @Autowired
    GdTdMapper gdTdMapper;
    @Autowired
    ProjectService projectService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdDyhRelService gdDyhRelService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private GdDyService gdDyService;
    @Autowired
    private GdYgService gdYgService;
    @Autowired
    private GdYyService gdYyService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map getGdql(final String qlid) {
        return gdXmMapper.getGdql(qlid);
    }

    @Override
    public Map getTdidByGdproid(final String proid) {
        return null;
    }

    @Override
    public List<GdBdcQlRel> getGdBdcQlRelByQlidAndBdcId(final Map map) {
        return gdXmMapper.getGdBdcQlRelByQlidAndBdcId(map);
    }
    /*
        * zwq
        *根据起始日期或终止日期和使用期限反推空的时间
        * */


    public Date getRqFromYt(final String qsrq, final String zzrq, final String yt, final String syqlx) {
        String syqx = null;
        Date date = null;
        //根据yt获取syqx
        if(StringUtils.isNotBlank(yt)) {
            syqx = gdTdMapper.getGdTdSyqx(yt);
        }
        if(StringUtils.isBlank(syqx) || StringUtils.isBlank(qsrq)) {
            return null;
        }

        try {
            //zzrq为空
            if(StringUtils.isBlank(zzrq)&&StringUtils.isNotBlank(syqx) && StringUtils.equals(syqlx, Constants.GDTD_SYQLX_CR) && StringUtils.length(qsrq) > 4) {
                date = CalendarUtil.addYears(CalendarUtil.formatDate(qsrq), Integer.parseInt(syqx));
            }
        } catch (Exception e) {
            logger.error("GdXmServiceImpl.getRqFromYt",e);
        }

        return date;
    }

    @Override
    public GdXm getGdXm(final String gdproid) {
        GdXm gdXm = null;
        if(StringUtils.isNotBlank(gdproid)) {
            Example example = new Example(GdXm.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(ParamsConstants.PROID_LOWERCASE, gdproid);
            List<GdXm> gdXmList = entityMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(gdXmList)) {
                gdXm = gdXmList.get(0);
            }
        }
        return gdXm;
    }

    @Override
    public GdDy getGdDy(final String bdcid) {
        return gdXmMapper.getGdDyFromBdcid(bdcid);
    }

    @Override
    public List<GdCf> getGdCfList(final Map map) {
        return gdXmMapper.getGdCfList(map);
    }

    @Override
    public List<GdDy> getGdDyList(final Map map) {
        return gdXmMapper.getGdDyList(map);
    }

    @Override
    public List<String> getCheckGdXX(final String fwid, final String tableName) {
        List<String> proidList = null;
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCID_LOWERCASE, fwid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            StringBuilder qlids = new StringBuilder();
            for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                if(StringUtils.isBlank(qlids)) {
                    qlids.append(gdBdcQlRel.getQlid());
                }else {
                    qlids.append(",").append(gdBdcQlRel.getQlid());
                }
            }
            HashMap param = new HashMap();
            param.put("qlids", qlids.toString().split(","));
            if(Constants.GDQL_CF_CPT.equals(tableName)) {
                List<GdCf> gdCfList = getGdCfList(param);
                if(CollectionUtils.isNotEmpty(gdCfList)) {
                    proidList = new ArrayList<String>();
                    for(int i = 0; i < gdCfList.size(); i++) {
                        proidList.add(gdCfList.get(i).getCfid());
                    }
                }
            } else if(Constants.GDQL_DY_CPT.equals(tableName)) {
                List<GdDy> gdDyList = getGdDyList(param);
                if(CollectionUtils.isNotEmpty(gdDyList)) {
                    proidList = new ArrayList<String>();
                    for(int i = 0; i < gdDyList.size(); i++) {
                        proidList.add(gdDyList.get(i).getDyid());
                    }
                }
            }

        }
        return proidList;
    }

    @Override
    public String getBdcdyhsByGdProid(final String gdproid) {
        return gdXmMapper.getBdcdyhsByGdProid(gdproid);
    }

    @Override
    public List<String> getDjidByGdid(final String gdid) {
        return gdXmMapper.getDjidByGdid(gdid);
    }

    @Override
    public String deleteGdXm(final String proid) {
        String msg = "删除失败";
        try {
            GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, proid);
            if(gdXm != null) {
                entityMapper.deleteByPrimaryKey(GdXm.class, proid);
                //删除权利
                //房屋所有权
                Example examplefwsyq = new Example(GdFwsyq.class);
                Example.Criteria criteriafwsyq = examplefwsyq.createCriteria();
                criteriafwsyq.andEqualTo(ParamsConstants.BDCID_LOWERCASE, proid);
                List<GdFwsyq> fdFwsyqList = entityMapper.selectByExample(GdFwsyq.class, examplefwsyq);
                if(CollectionUtils.isNotEmpty(fdFwsyqList)) {
                    for(GdFwsyq gdFwsyq : fdFwsyqList) {
                        entityMapper.deleteByPrimaryKey(GdFwsyq.class, gdFwsyq.getQlid());
                        deleteFwAndQlrAndRel(gdFwsyq.getQlid());
                    }
                }

                //抵押
                Example exampleDy = new Example(GdDy.class);
                Example.Criteria criteriaDy = exampleDy.createCriteria();
                criteriaDy.andEqualTo(ParamsConstants.BDCID_LOWERCASE, proid);
                List<GdDy> gdDyList = entityMapper.selectByExample(GdDy.class, exampleDy);
                if(CollectionUtils.isNotEmpty(gdDyList)) {
                    for(GdDy gdDy : gdDyList) {
                        entityMapper.deleteByPrimaryKey(GdDy.class, gdDy.getDyid());
                        deleteFwAndQlrAndRel(gdDy.getDyid());
                    }
                }

                //查封
                Example exampleCf = new Example(GdCf.class);
                Example.Criteria criteriaCf = exampleCf.createCriteria();
                criteriaCf.andEqualTo(ParamsConstants.BDCID_LOWERCASE, proid);
                List<GdCf> gdCfList = entityMapper.selectByExample(GdCf.class, exampleCf);
                if(CollectionUtils.isNotEmpty(gdCfList)) {
                    for(GdCf gdCf : gdCfList) {
                        entityMapper.deleteByPrimaryKey(GdCf.class, gdCf.getCfid());
                        deleteFwAndQlrAndRel(gdCf.getCfid());
                    }
                }

                //预告
                Example exampleYg = new Example(GdYg.class);
                Example.Criteria criteriaYg = exampleYg.createCriteria();
                criteriaYg.andEqualTo(ParamsConstants.BDCID_LOWERCASE, proid);
                List<GdYg> gdYgList = entityMapper.selectByExample(GdYg.class, exampleYg);
                if(CollectionUtils.isNotEmpty(gdYgList)) {
                    for(GdYg gdYg : gdYgList) {
                        entityMapper.deleteByPrimaryKey(GdYg.class, gdYg.getYgid());
                        deleteFwAndQlrAndRel(gdYg.getYgid());
                    }
                }

                //异议
                Example exampleYy = new Example(GdYy.class);
                Example.Criteria criteriaYy = exampleYy.createCriteria();
                criteriaYy.andEqualTo(ParamsConstants.BDCID_LOWERCASE, proid);
                List<GdYy> gdYyList = entityMapper.selectByExample(GdYy.class, exampleYy);
                if(CollectionUtils.isNotEmpty(gdYyList)) {
                    for(GdYy gdYy : gdYyList) {
                        entityMapper.deleteByPrimaryKey(GdYy.class, gdYy.getYyid());
                        deleteFwAndQlrAndRel(gdYy.getYyid());
                    }

                }
                msg = "删除成功";
            }
        } catch (Exception e) {
            logger.error("GdXmServiceImpl.deleteGdXm",e);
        }

        return msg;
    }

    @Override
    public String deleteGdCf(final String proid) {
        String msg = "删除失败";
        try {
            GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, proid);
            if(gdXm != null) {
                entityMapper.deleteByPrimaryKey(GdXm.class, proid);
            }
            //删除查封权利
            Example exampleCf = new Example(GdCf.class);
            Example.Criteria criteriaCf = exampleCf.createCriteria();
            criteriaCf.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
            List<GdCf> gdCfList = entityMapper.selectByExample(GdCf.class, exampleCf);
            if(CollectionUtils.isNotEmpty(gdCfList) && StringUtils.isNotBlank(gdCfList.get(0).getCfid())) {
                GdFwQl gdFwQl = entityMapper.selectByPrimaryKey(GdFwQl.class, gdCfList.get(0).getCfid());
                GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class, gdCfList.get(0).getCfid());
                List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdCfList.get(0).getCfid());
                if(gdFwQl != null) {
                    entityMapper.deleteByPrimaryKey(GdFwQl.class, gdCfList.get(0).getCfid());
                } else if(gdTdQl != null) {
                    entityMapper.deleteByPrimaryKey(GdTdQl.class, gdCfList.get(0).getCfid());
                }
                if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                    entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRelList.get(0).getRelid());
                }
                for(GdCf gdCf : gdCfList) {
                    if(StringUtils.isNotBlank(gdCf.getCfid())) {
                        entityMapper.deleteByPrimaryKey(GdCf.class, gdCf.getCfid());
                        Example exampleQlr = new Example(GdQlr.class);
                        Example.Criteria criteriaYgQlr = exampleQlr.createCriteria();
                        criteriaYgQlr.andEqualTo("qlid", gdCf.getCfid());
                        List<GdQlr> gdQlrList = entityMapper.selectByExample(GdQlr.class, exampleQlr);
                        if(CollectionUtils.isNotEmpty(gdQlrList)) {
                            for(GdQlr gdQlr : gdQlrList) {
                                entityMapper.deleteByPrimaryKey(GdQlr.class, gdQlr.getQlrid());
                            }
                        }
                    }
                }
            }
            msg = "删除成功";
        } catch (Exception e) {
            logger.error("GdXmServiceImpl.deleteGdCf",e);
        }
        return msg;
    }

    @Override
    public String deleteXmgl(String gdproid, String tdid, String cqid, String lqid) {
        String msg = "删除失败";
        try {
            if(StringUtils.isNotBlank(gdproid)) {
                String proid = bdcXmService.getProidByGdproid(gdproid);
                if(StringUtils.isNotBlank(proid)) {
                    BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, proid);
                    bdcXm.setYbdcqzh("");
                    bdcXm.setXmmc("");
                    bdcXm.setBdclx("");
                    bdcXm.setXmly("");
                    entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                    DelProjectService delProjectService = projectService.getDelProjectService(bdcXm);
                    delProjectService.delZsbh(proid);
                    delProjectService.delBdcBdxx(bdcXm);
                    delProjectService.delProjectNode(proid);
                    //删除项目关系
                    Example exampleXmrel = new Example(BdcXmRel.class);
                    Example.Criteria criteriaXmrel = exampleXmrel.createCriteria();
                    criteriaXmrel.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
                    entityMapper.deleteByExample(BdcXmRel.class, exampleXmrel);
                    QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                    //删除项目关系
                    Example exampleQl = new Example(qllxVo.getClass());
                    Example.Criteria criteriaQl = exampleQl.createCriteria();
                    criteriaQl.andEqualTo(ParamsConstants.PROID_LOWERCASE, proid);
                    entityMapper.deleteByExample(qllxVo.getClass(), exampleQl);
                    GdXm gdXm = getGdXm(gdproid);
                    if(gdXm != null) {
                        gdXm.setPpzt("2");
                        entityMapper.saveOrUpdate(gdXm, gdXm.getProid());
                    }
                    msg = "删除成功";
                } else {
                    msg = "该项目没有匹配不动产项目";
                }
            }
        } catch (Exception e) {
            logger.error("GdXmServiceImpl.deleteXmgl",e);
        }
        return msg;
    }

    private void deleteFwAndQlrAndRel(String qlid) {
        Example exampleQlr = new Example(GdQlr.class);
        Example.Criteria criteriaYgQlr = exampleQlr.createCriteria();
        criteriaYgQlr.andEqualTo("qlid", qlid);
        List<GdQlr> gdQlrList = entityMapper.selectByExample(GdQlr.class, exampleQlr);
        if(CollectionUtils.isNotEmpty(gdQlrList)) {
            for(GdQlr gdQlr : gdQlrList) {
                entityMapper.deleteByPrimaryKey(GdQlr.class, gdQlr.getQlrid());
            }
        }
        Example example = new Example(GdBdcQlRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("qlid", qlid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRel.getRelid());
                Example exampleFw = new Example(GdFw.class);
                Example.Criteria criteriaFw = exampleFw.createCriteria();
                criteriaFw.andEqualTo("qlid", qlid);
                List<GdFw> gdFwList = entityMapper.selectByExample(GdFw.class, exampleFw);
                if(CollectionUtils.isNotEmpty(gdFwList)) {
                    for(GdFw gdFw : gdFwList) {
                        entityMapper.deleteByPrimaryKey(GdFw.class, gdFw.getFwid());
                    }
                }
            }
        }
    }

    @Override
    public List<String> getGdBdcidByProid(final String gdproid) {
        List<String> stringList = null;
        String qlid = getQlidByGdproid(gdproid);
        Example example = new Example(GdBdcQlRel.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("qlid", qlid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            stringList = new ArrayList<String>();
            for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                stringList.add(gdBdcQlRel.getBdcid());
            }
        }
        return stringList;
    }

    @Override
    public List<GdQlr> getGdqlrByQlid(final String qlid, final String qlrlx) {
        List<GdQlr> gdQlrList = null;
        if(StringUtils.isNotBlank(qlid)) {
            gdQlrList = gdQlrService.queryGdQlrs(qlid, qlrlx);
        }
        return gdQlrList;
    }

    @Override
    public void updateGdxmPpzt(final String gdproid, final String ppzt) {
        if(StringUtils.isNotBlank(gdproid) && StringUtils.isNotBlank(ppzt)) {
            GdXm gdXm = getGdXm(gdproid);
            if(gdXm != null) {
                gdXm.setPpzt(ppzt);
                entityMapper.saveOrUpdate(gdXm, gdXm.getProid());
            }
        }
    }

    @Override
    public void updateGdxmPpztByQlids(final List<String> qlids, final String ppzt) {
        if(CollectionUtils.isNotEmpty(qlids)){
            for(String qlid : qlids){
                GdXm gdXm = getGdXm(getGdProidByQlid(qlid));
                if(null != gdXm) {
                    gdXm.setPpzt(ppzt);
                    entityMapper.saveOrUpdate(gdXm, gdXm.getProid());
                }
            }
        }
    }

    @Override
    public void updateGdQszt(final String qlid, final Integer qszt) {
        if(StringUtils.isNotBlank(qlid) && qszt != null) {
            //房屋
            Example fwsyqExample = new Example(GdFwsyq.class);
            fwsyqExample.createCriteria().andEqualTo("qlid", qlid);
            List<GdFwsyq> gdFwsyqList = entityMapper.selectByExample(fwsyqExample);
            if(CollectionUtils.isNotEmpty(gdFwsyqList)) {
                for(GdFwsyq gdFwsyq : gdFwsyqList) {
                    gdFwsyq.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdFwsyq, gdFwsyq.getQlid());
                    //zhouwanqing 如果匹配过度土地则把过度土地注销
                    List<GdTdsyq> gdTdsyqList = gdTdService.getGdTdsyqByFwQlid(gdFwsyq.getQlid());
                    if(CollectionUtils.isNotEmpty(gdTdsyqList)) {
                        for(GdTdsyq gdTdsyq : gdTdsyqList) {
                            gdTdsyq.setIszx(qszt);
                            entityMapper.saveOrUpdate(gdTdsyq, gdTdsyq.getQlid());
                            gdFwService.changeGdqlztByQlid(gdTdsyq.getQlid(),qszt.toString());
                        }
                    }
                }
            }
            //土地
            Example tdsyqExample = new Example(GdTdsyq.class);
            tdsyqExample.createCriteria().andEqualTo("qlid", qlid);
            List<GdTdsyq> gdTdsyqList = entityMapper.selectByExample(tdsyqExample);
            if(CollectionUtils.isNotEmpty(gdTdsyqList)) {
                for(GdTdsyq gdTdsyq : gdTdsyqList) {
                    gdTdsyq.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdTdsyq, gdTdsyq.getQlid());
                }
            }
            //抵押
            Example dyExample = new Example(GdDy.class);
            dyExample.createCriteria().andEqualTo("dyid", qlid);
            List<GdDy> gdDyList = entityMapper.selectByExample(dyExample);
            if(CollectionUtils.isNotEmpty(gdDyList)) {
                for(GdDy gdDy : gdDyList) {
                    gdDy.setIsjy(qszt);
                    entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
                    HashMap map = new HashMap();
                    if(StringUtils.isNotBlank(gdDy.getDyid())) {
                        map.put("qlid", gdDy.getDyid());
                    }
                    List<GdDy> gdTdDyList = gdTdService.getTdDyByFczh(map);
                    if(CollectionUtils.isNotEmpty(gdTdDyList)) {
                        for(GdDy gdDy1 : gdTdDyList) {
                            gdDy1.setIsjy(qszt);
                            entityMapper.saveOrUpdate(gdDy1, gdDy1.getDyid());
                        }
                    }
                }
            }
            //查封
            Example cfExample = new Example(GdCf.class);
            cfExample.createCriteria().andEqualTo("cfid", qlid);
            List<GdCf> gdCfList = entityMapper.selectByExample(cfExample);
            if(CollectionUtils.isNotEmpty(gdCfList)) {
                for(GdCf gdCf : gdCfList) {
                    gdCf.setIsjf(qszt);
                    entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                    HashMap map = new HashMap();
                    if(StringUtils.isNotBlank(gdCf.getCfid())) {
                        map.put("qlid", gdCf.getCfid());
                    }
                    List<GdCf> gdTdCfList = gdTdService.getTdCfByFczh(map);
                    if(CollectionUtils.isNotEmpty(gdTdCfList)) {
                        for(GdCf gdCf1 : gdTdCfList) {
                            gdCf1.setIsjf(qszt);
                            entityMapper.saveOrUpdate(gdCf1, gdCf1.getCfid());
                            gdFwService.changeGdqlztByQlid(gdCf1.getCfid(),qszt.toString());
                        }
                    }

                    String cfwh = gdCf.getCfwh();
                    if(StringUtils.isNotBlank(cfwh)){
                        List<GdBdcQlRel> gdBdcQlRels = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                        if(CollectionUtils.isNotEmpty(gdBdcQlRels)){
                            for (GdBdcQlRel gdBdcQlRel: gdBdcQlRels) {
                                String bdcid = gdBdcQlRel.getBdcid();
                                if(StringUtils.isNotBlank(bdcid)){
                                    List<GdDyhRel> gdDyhRels = gdDyhRelService.queryGdDyhRelListByBdcid(bdcid);
                                    if(CollectionUtils.isNotEmpty(gdDyhRels)) {
                                        for (GdDyhRel gdDyhRel : gdDyhRels) {
                                            final String tdid = gdDyhRel.getTdid();
                                            if (StringUtils.isNotBlank(tdid)) {
                                                List<GdCf> gdCfs = gdCfService.getGdCfListByBdcid(tdid, 0);
                                                if (CollectionUtils.isNotEmpty(gdCfs)) {
                                                    for (GdCf gdCf1 : gdCfs) {
                                                        if (StringUtils.isNotBlank(gdCf1.getCfwh())
                                                                && StringUtils.equals(gdCf1.getCfwh(), cfwh)) {
                                                            gdCf1.setIsjf(qszt);
                                                            entityMapper.saveOrUpdate(gdCf1, gdCf1.getCfid());
                                                            gdFwService.changeGdqlztByQlid(gdCf1.getCfid(),qszt.toString());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //预告
            Example ygExample = new Example(GdYg.class);
            ygExample.createCriteria().andEqualTo("ygid", qlid);
            List<GdYg> gdYgList = entityMapper.selectByExample(ygExample);
            if(CollectionUtils.isNotEmpty(gdYgList)) {
                for(GdYg gdYg : gdYgList) {
                    gdYg.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdYg, gdYg.getYgid());
                }
            }
            //异议
            Example yyExample = new Example(GdYy.class);
            yyExample.createCriteria().andEqualTo("yyid", qlid);
            List<GdYy> gdYyList = entityMapper.selectByExample(yyExample);
            if(CollectionUtils.isNotEmpty(gdYyList)) {
                for(GdYy gdYy : gdYyList) {
                    gdYy.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdYy, gdYy.getYyid());
                }
            }

            //林权
            Example lqExample = new Example(GdLq.class);
            lqExample.createCriteria().andEqualTo("lqid", qlid);
            List<GdLq> gdLqList = entityMapper.selectByExample(lqExample);
            if(CollectionUtils.isNotEmpty(gdLqList)) {
                for(GdLq gdLq : gdLqList) {
                    gdLq.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdLq, gdLq.getLqid());
                }
            }
            gdFwService.changeGdqlztByQlid(qlid,qszt.toString());
        }
    }

    @Override
    public String getQlidByGdproid(String gdproid) {
        List<String> list = gdXmMapper.getQlidByGdproid(gdproid);
        return CollectionUtils.isNotEmpty(list) ? list.get(0):"";
    }

    @Override
    public List<GdDyhRel> getGdDyhRelByMap(HashMap hashMap) {
        return gdXmMapper.getGdDyhRelByMap(hashMap);
    }

    public Object queryDyAndCfByBdcid(String bdcid, Object obj, Integer iszx) {
        Object gdQl = null;
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo(ParamsConstants.BDCID_LOWERCASE, bdcid);
        List<GdBdcQlRel> gdBdcQlRelLsit = entityMapper.selectByExample(GdBdcQlRel.class, example);
        if(CollectionUtils.isNotEmpty(gdBdcQlRelLsit)) {
            for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelLsit) {
                if(obj instanceof GdDy) {
                    GdDy gdDy = entityMapper.selectByPrimaryKey(GdDy.class, gdBdcQlRel.getQlid());
                    if(gdDy != null && gdDy.getIsjy() != null && gdDy.getIsjy().equals(iszx)) {
                        gdQl = gdDy;
                        break;
                    }
                }
                if(obj instanceof GdCf) {
                    GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class, gdBdcQlRel.getQlid());
                    if(gdCf != null && gdCf.getIsjf() != null && gdCf.getIsjf().equals(iszx)) {
                        gdQl = gdCf;
                        break;
                    }
                }
            }
        }
        return gdQl;
    }

    @Override
    public List<String> getGdproidByQild(String qlid) {
        return gdXmMapper.getGdproidByQild(qlid);
    }

    @Override
    public List<String> getGdProidByQlr(String qlr) {
        List<String> proidList = null;
        Example example = new Example(GdQlr.class);
        example.createCriteria().andEqualTo("qlr", qlr);
        List<GdQlr> gdQlrList = entityMapper.selectByExample(GdQlr.class, example);
        if(CollectionUtils.isNotEmpty(gdQlrList)) {
            proidList = new ArrayList<String>();
            for(GdQlr gdQlr : gdQlrList) {
                List<String> proids = getGdproidByQild(gdQlr.getQlid());
                if(CollectionUtils.isNotEmpty(proids)) {
                    proidList.addAll(proids);
                }
            }
        }

        return proidList;
    }

    @Override
    public List<String> getGdProidByFwzl(String fwzl) {
        List<String> proidList = null;
        String[] qlidItems = gdFwService.getFwQlidByFwzl(fwzl);
        if(qlidItems != null) {
            proidList = new ArrayList<String>();
            for(String qlid : qlidItems) {
                List<String> proids = getGdproidByQild(qlid);
                if(CollectionUtils.isNotEmpty(proids)) {
                    proidList.addAll(proids);
                }
            }
        }

        return proidList;
    }

    @Override
    public String getGdXmZLByGdProid(String gdProid) {
        String zls = "";
        List<String> qlidList = gdXmMapper.getQlidByGdproid(gdProid);
        if(CollectionUtils.isNotEmpty(qlidList)) {
            HashMap map = new HashMap();
            map.put("qlids", qlidList.toArray());
            List<GdFw> gdFwList = gdFwService.getGdFw(map);
            if(CollectionUtils.isNotEmpty(gdFwList)) {
                if(CollectionUtils.size(gdFwList) > 3) {
                    for(GdFw gdFw : gdFwList) {
                        if(StringUtils.isNotBlank(zls) && StringUtils.isNotBlank(gdFw.getFwzl()))
                            zls = zls + "," + gdFw.getFwzl();
                        else if(StringUtils.isNotBlank(gdFw.getFwzl()))
                            zls = gdFw.getFwzl();
                    }
                    if(StringUtils.isNotBlank(zls)) {
                        zls += "等";
                    }
                } else {
                    for(GdFw gdFw : gdFwList) {
                        if(StringUtils.isNotBlank(zls) && StringUtils.isNotBlank(gdFw.getFwzl()))
                            zls = zls + "," + gdFw.getFwzl();
                        else if(StringUtils.isNotBlank(gdFw.getFwzl()))
                            zls = gdFw.getFwzl();
                    }
                }
            }
        }
        return zls;
    }

    @Override
    public String getYzhByGdproid(Project project) {
        String bdclx = project.getBdclx();
        String sqlx = project.getSqlx();
        String gdProid = StringUtils.deleteWhitespace(project.getGdproid());
        String qlid = project.getYqlid();
        String ybdcqzh = gdXmMapper.getYzhByGdproid(gdProid);
        if(!CommonUtil.indexOfStrs(Constants.DJLX_DYAQ_ZXDJ_SQLXDM, sqlx)) {
            List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
            List<GdDy> gdDyList = new ArrayList<GdDy>();
            if(StringUtils.equals(bdclx, Constants.BDCLX_TDFW) && StringUtils.isNotBlank(qlid)) {
                List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel("", qlid, "");
                if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                    for(GdQlDyhRel gdQlDyhRel : gdQlDyhRelList) {
                        String tdQlid = gdQlDyhRel.getTdqlid();
                        if(StringUtils.isNotBlank(tdQlid)) {
                            GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(tdQlid);
                            GdDy gdDy = gdTdService.getGddyqByQlid(tdQlid,null);
                            if(null != gdTdsyq) {
                                gdTdsyqList.add(gdTdsyq);
                            }
                            if(null != gdDy) {
                                gdDyList.add(gdDy);
                            }
                        }
                    }
                }

            }else if(StringUtils.equals(bdclx, Constants.BDCLX_TD) && StringUtils.isNotBlank(qlid)){
                GdTdsyq  gdTdsyq = gdTdService.getGdTdsyqByQlid(qlid);
                if(gdTdsyq == null) {
                    ybdcqzh = "";
                }
                List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                if(CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                    for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                        List<GdTdsyq> gdTdsyqListTemp = gdTdService.queryGdTdsyqByTdidAndQszt(gdBdcQlRel.getBdcid(),"0");
                        if(CollectionUtils.isNotEmpty(gdTdsyqListTemp)) {
                            gdTdsyqList.addAll(gdTdsyqListTemp);
                        }
                    }
                }

            }

            if(CollectionUtils.isNotEmpty(gdTdsyqList)) {
                for(GdTdsyq gdTdsyq : gdTdsyqList) {
                    if(StringUtils.isNotBlank(gdTdsyq.getTdzh())) {
                        if(StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh = gdTdsyq.getTdzh();
                        }
                        //防止土地证号重复
                        else if(StringUtils.indexOf(ybdcqzh, gdTdsyq.getTdzh()) == -1) {
                            ybdcqzh += "," + gdTdsyq.getTdzh();
                        }
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(gdDyList)) {
                for(GdDy gdDy : gdDyList) {
                    if(StringUtils.isNotBlank(gdDy.getDydjzmh())) {
                        if(StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh = gdDy.getDydjzmh();
                        } else {
                            ybdcqzh += "," + gdDy.getDydjzmh();
                        }
                    }
                }
            }
        }
        return ybdcqzh;
    }

    /**
     * 查询过渡数据登记类型字典
     */
    @Override
    public List<GdZdFcxtDjlx> getGdZdFcxtDjlx() {
        return entityMapper.select(new GdZdFcxtDjlx());
    }

    /**
     * 保存过渡锁定数据
     *
     * @param gdBdcSd
     * @return
     */
    @Override
    public int saveOrupdateGdBdcSd(GdBdcSd gdBdcSd) {
        int result = 0;
        if(gdBdcSd != null) {
            result = entityMapper.saveOrUpdate(gdBdcSd, gdBdcSd.getSdid());
        }
        return result;
    }

    /**
     * 根据产权证号获得锁定数据
     *
     * @param colName
     * @param colValue
     * @param sdzt
     * @return
     */
    @Override
    public List<GdBdcSd> getGdBdcSd(String colName, String colValue, int sdzt) {
        Example example = new Example(GdBdcSd.class);
        example.createCriteria().andEqualTo(colName, colValue).andEqualTo("xzzt", sdzt);
        return entityMapper.selectByExample(GdBdcSd.class, example);
    }

    @Override
    public List<GdBdcSd> getGdBdcSdByCqzhAndQlid(String cqzh, String qlid, int sdzt) {
        Example example = new Example(GdBdcSd.class);
        if(StringUtils.isNotBlank(qlid)){
            example.createCriteria().andEqualTo("cqzh", cqzh).andEqualTo("xzzt", sdzt).andEqualTo("qlid", qlid);
        }else{
            example.createCriteria().andEqualTo("cqzh", cqzh).andEqualTo("xzzt", sdzt);
        }
        return entityMapper.selectByExample(GdBdcSd.class, example);
    }

    @Override
    public String getGdproidByQlid(String qlid) {
        return gdXmMapper.getGdproidByQlid(qlid);
    }

    @Override
    public List<GdCf> getGdCfListByBdcdyid(String bdcdyid) {
        return gdXmMapper.getGdCfListByBdcdyid(bdcdyid);
    }

    public List<GdFwsyq> getGdFwsyqListByBdcdyid(final String bdcdyid) {
        return gdXmMapper.getGdFwsyqListByBdcdyid(bdcdyid);
    }

    public List<GdTdsyq> getGdTdsyqListByBdcdyid(final String bdcdyid) {
        return gdXmMapper.getGdTdsyqListByBdcdyid(bdcdyid);
    }

    @Override
    public List<String> getQlidsByGdproid(String gdproid) {
        return gdXmMapper.getQlidByGdproid(gdproid);
    }

    @Override
    public List<GdDy> getGdFwDybyBdcdyid(String bdcdyid) {
        return gdXmMapper.getGdFwDybyBdcdyid(bdcdyid);
    }

    @Override
    public List<GdDy> getGdTdDybyBdcdyid(String bdcdyid) {
        return gdXmMapper.getGdTdDybyBdcdyid(bdcdyid);
    }

    @Override
    public String getGdYzhByBdcdyh(Project project) {
        String ybdcqzh = "";
        if(!CommonUtil.indexOfStrs(Constants.DJLX_DYAQ_ZXDJ_SQLXDM, project.getSqlx())) {
            if(StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW) && StringUtils.isNotBlank(project.getYqlid())&&StringUtils.isNotBlank(project.getBdcdyh())) {
                String bdcdyh = project.getBdcdyh();
                HashSet<String> tdzhList = new HashSet<String>();
                HashSet<String> fczhList = new HashSet<String>();
                List<HashMap> gdFwQlMapList = gdFwService.getFczhByBdcdyh(bdcdyh);
                if(CollectionUtils.isNotEmpty(gdFwQlMapList)) {
                    for(HashMap gdFwQlMap : gdFwQlMapList) {
                        String gdFwQlid = "";
                        if(StringUtils.isNotBlank(CommonUtil.formatEmptyValue(gdFwQlMap.get("QLID"))))
                            gdFwQlid = gdFwQlMap.get("QLID").toString();
                        if(StringUtils.isNotBlank(CommonUtil.formatEmptyValue(gdFwQlMap.get("FCZH"))))
                            fczhList.add(CommonUtil.formatEmptyValue(gdFwQlMap.get("FCZH")));
                        List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(bdcdyh, gdFwQlid, "");
                        if(CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                            //跨宗的土地有多个土地证
                            for(GdQlDyhRel gdQlDyhRel : gdQlDyhRelList) {
                                if(StringUtils.isNotBlank(gdQlDyhRel.getTdqlid())) {
                                    GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(gdQlDyhRel.getTdqlid());
                                    if(gdTdsyq != null)
                                        tdzhList.add(gdTdsyq.getTdzh());
                                }
                            }

                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(fczhList)) {
                    ybdcqzh = "房产证号:" + PublicUtil.join(",", new ArrayList<String>(fczhList));
                }
                if(CollectionUtils.isNotEmpty(tdzhList)) {
                    ybdcqzh = ybdcqzh + ";" + "土地证号:" + PublicUtil.join(",", new ArrayList<String>(tdzhList));
                }
            }
        } else {
            //抵押注销获取原抵押证明
            String gdProid = StringUtils.deleteWhitespace(project.getGdproid());
            ybdcqzh = gdXmMapper.getYzhByGdproid(gdProid);
            //房产土地匹配的情况下，同时获取房产和土地的原抵押证明
            //通过bdcdyh和yqlid获取土地原抵押权利ID
            Map map = Maps.newHashMap();
            map.put("bdcdyh",project.getBdcdyh());
            map.put("yqlid",project.getYqlid());
            List<String> tdqlidList = gdXmMapper.getTdqlidByBdcdyhAndYqlid(map);
            if(CollectionUtils.isNotEmpty(tdqlidList)){
                GdDy gddy= null;
                for(String tdqlid:tdqlidList) {
                    if(StringUtils.isNotBlank(tdqlid)) {
                        gddy = gdTdService.getGddyqByQlid(tdqlid,null);
                        if(gddy != null&&StringUtils.isNotBlank(gddy.getDydjzmh())){
                            if(StringUtils.isNotBlank(ybdcqzh)) {
                                ybdcqzh = ybdcqzh + "," + gddy.getDydjzmh();
                            }else {
                                ybdcqzh = gddy.getDydjzmh();
                            }
                        }
                    }
                }
            }

        }
        return ybdcqzh;
    }

    @Override
    public List<String> getXmidByGdzh(String gdzh) {return  gdXmMapper.getXmidByGdzh(gdzh);}

    @Override
    public List<String> getQlzt(HashMap map) {
        return gdXmMapper.getQlzt(map);
    }

    @Override
    public List<String> getFwGdproidByGdzh(String gdzh) {
        return gdXmMapper.getFwGdproidByGdzh(gdzh);
    }

    @Override
    public List<String> getTdGdproidByGdzh(String gdzh) {
        return gdXmMapper.getTdGdproidByGdzh(gdzh);
    }

    @Override
    public List<String> getDyGdproidByGdzh(String gdzh) {
        return gdXmMapper.getDyGdproidByGdzh(gdzh);
    }

    @Override
    public String getGdProidByQlid(final String qlid){
        String proid = "";
        if(StringUtils.isNotBlank(qlid)){
            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(qlid);
            if(null != gdFwsyq){
                proid = gdFwsyq.getProid();
            }else{
                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(qlid);
                if(null != gdTdsyq){
                    proid = gdTdsyq.getProid();
                }else{
                    GdDy gdDy = gdDyService.getGdDyByDyDyid(qlid);
                    if(null != gdDy){
                        proid = gdDy.getProid();
                    } else{
                       GdCf gdCf = gdCfService.getGdCfByCfid(qlid);
                       if(null != gdCf){
                           proid = gdCf.getProid();
                       }else {
                           GdYg gdYg = gdYgService.queryGdYgByYgid(qlid);
                           if(gdYg != null){
                               proid = gdYg.getProid();
                           }else{
                               GdYy gdYy = gdYyService.queryGdYyByYyid(qlid);
                               if(null != gdYy){
                                   proid = gdYy.getProid();
                               }
                           }
                       }
                    }
                }
            }
        }
        return proid;
    }
}
