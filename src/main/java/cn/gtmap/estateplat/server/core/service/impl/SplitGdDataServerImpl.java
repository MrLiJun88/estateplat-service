package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.cadastral.SyncIsfsssData;
import cn.gtmap.estateplat.server.model.cadastral.SyncIsfsssParam;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2016-11-9
 * @description 拆分过度数据
 */
@Repository
public class SplitGdDataServerImpl implements SplitGdDataServer {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    private CadastralService cadastralService;
    private static final String PARAMETER_ISSPLIT = "isSplit";
    private static final Log log = LogFactory.getLog(SplitGdDataServerImpl.class);
    @Override
    public HashMap checkSplit(String qlid) {
        HashMap resultMap = new HashMap();
        resultMap.put(PARAMETER_ISSPLIT, "no");
        resultMap.put("msg", "该数据不允许拆分,请选正确的数据.");
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo("qlid", qlid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExampleNotNull(example);

        if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
            GdBdcQlRel gdBdcQlRel = gdBdcQlRelList.get(0);
            //验证是否已经匹配过的数据
            String ppzt = gdFwService.getPpztByQlid(qlid, gdBdcQlRel.getBdclx(), gdBdcQlRel.getBdcid());
            String splitMatchedGdData = AppConfig.getProperty("sjpp.splitMatchedGdData");
            if((StringUtils.equals(ppzt,Constants.GD_PPZT_WCPP) || StringUtils.equals(ppzt,Constants.GD_PPZT_BFPP))
                    && !StringUtils.equals(splitMatchedGdData, ParamsConstants.TRUE_LOWERCASE)){
                resultMap.put(PARAMETER_ISSPLIT, "no");
                resultMap.put("msg", "该数据已匹配，请先取消匹配.");
                return resultMap;
            }

            //验证该数据是否已办理过项目
            List<BdcXmRel> bdcXmRels = bdcXmRelService.queryBdcXmRelByYqlid(gdBdcQlRel.getQlid());
            if(CollectionUtils.isNotEmpty(bdcXmRels)){
                resultMap.put(PARAMETER_ISSPLIT, "no");
                resultMap.put("msg", "该数据已创建项目，无法拆分.");
                return resultMap;
            }

        }

        //qijiadong 锁定不允许拆分
        List<GdBdcSd> list = gdXmService.getGdBdcSd("qlid", StringUtils.isNotBlank(qlid) ? qlid : "", Constants.SDZT_SD);
        if (CollectionUtils.isNotEmpty(list)) {
            resultMap.put(PARAMETER_ISSPLIT, "no");
            resultMap.put("msg", "该数据已锁定，无法拆分.");
            return resultMap;
        }

        //验证是不是一条数据，一条数据不允许拆分
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList) && gdBdcQlRelList.size() > 1) {
            resultMap.put(PARAMETER_ISSPLIT, "yes");
            resultMap.put("msg", "将拆成" + gdBdcQlRelList.size() + "条数据");
        }

        return resultMap;
    }

    @Override
    @Transactional
    public void splitGdData(String qlid, String bdclx) throws InvocationTargetException, IllegalAccessException {
        List<GdBdcQlRel> gdBdcQlRelNewList = new ArrayList<GdBdcQlRel>();
        List<GdFwsyq> gdFwsyqNewList = new ArrayList<GdFwsyq>();
        List<GdTdsyq> gdTdsyqNewList = new ArrayList<GdTdsyq>();
        List<GdYg> gdYgNewList = new ArrayList<GdYg>();
        List<GdCf> gdCfNewList = new ArrayList<GdCf>();
        List<GdDy> gdDyNewList = new ArrayList<GdDy>();
        List<GdXm> gdXmNewList = new ArrayList<GdXm>();
        List<GdQlr> gdQlrNewList = new ArrayList<GdQlr>();
        List<GdFwQl> gdFwQlNewList = new ArrayList<GdFwQl>();
        List<GdTdQl> gdTdQlNewList = new ArrayList<GdTdQl>();
        List<GdFw> gdFwList = new ArrayList<GdFw>();
        Example example = new Example(GdBdcQlRel.class);
        example.createCriteria().andEqualTo("qlid", qlid);
        List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExampleNotNull(example);
        GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, qlid);
        GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, qlid);
        GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class, qlid);
        GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class, qlid);
        GdDy gdDy = entityMapper.selectByPrimaryKey(GdDy.class, qlid);
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 重置gd_bdc_ql_rel
         */
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRel.getRelid());
                gdBdcQlRel.setQlid(UUIDGenerator.generate18());
                gdBdcQlRelNewList.add(gdBdcQlRel);
            }
        }

        if (CollectionUtils.isNotEmpty(gdBdcQlRelList) && StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 删除gd_fw_ql
             */
            entityMapper.deleteByPrimaryKey(GdFwQl.class, qlid);
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 删除gd_qlr
             */
            example = new Example(GdQlr.class);
            example.createCriteria().andEqualTo("qlid", qlid);
            List<GdQlr> gdQlrList = entityMapper.selectByExampleNotNull(example);
            entityMapper.deleteByExampleNotNull(example);
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                StringBuilder qlrMcs = new StringBuilder();
                if (CollectionUtils.isNotEmpty(gdQlrList)) {
                    for (GdQlr gdQlr : gdQlrList) {
                        GdQlr gdQlrNew = new GdQlr();
                        BeanUtils.copyProperties(gdQlrNew, gdQlr);
                        if (StringUtils.isBlank(qlrMcs)) {
                            qlrMcs.append(gdQlr.getQlr());
                        }else{
                            qlrMcs.append(",").append(gdQlr.getQlr());
                        }
                        gdQlrNew.setProid(gdBdcQlRel.getQlid());
                        gdQlrNew.setQlid(gdBdcQlRel.getQlid());
                        gdQlrNew.setQlrid(UUIDGenerator.generate18());
                        gdQlrNewList.add(gdQlrNew);
                    }
                }
                GdFw gdFw = entityMapper.selectByPrimaryKey(GdFw.class, gdBdcQlRel.getBdcid());
                if(null!=gdFw){
                    gdFw.setIsfsss("");
                    entityMapper.saveOrUpdate(gdFw,gdFw.getFwid());
                    gdFwList.add(gdFw);
                }
                if (gdFw != null) {
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 重置gd_fw_ql
                     */
                    GdFwQl gdFwQl = new GdFwQl();
                    gdFwQl.setQlid(gdBdcQlRel.getQlid());
                    gdFwQl.setProid(gdBdcQlRel.getQlid());
                    gdFwQl.setFwzl(gdFw.getFwzl());
                    gdFwQl.setQlr(qlrMcs.toString());
                    if (gdFwsyq != null) {
                        GdFwsyq gdFwsyqNew = new GdFwsyq();
                        BeanUtils.copyProperties(gdFwsyqNew, gdFwsyq);
                        gdFwQl.setFczh(gdFwsyq.getFczh());
                        gdFwQl.setDjlx(gdFwsyq.getDjlx());
                        gdFwQl.setZslx(Constants.GDQL_FWSYQ_ZSLX);
                        gdFwsyqNew.setQlid(gdBdcQlRel.getQlid());
                        gdFwsyqNew.setProid(gdBdcQlRel.getQlid());
                        gdFwsyqNewList.add(gdFwsyqNew);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdFwsyq.getProid());
                        if (gdXm != null) {
                            gdXm.setProid(gdBdcQlRel.getQlid());
                            gdXm.setZl(gdFw.getFwzl());
                            gdXmNewList.add(gdXm);
                        }
                    }
                    if (gdYg != null) {
                        GdYg gdYgNew = new GdYg();
                        BeanUtils.copyProperties(gdYgNew, gdYg);
                        gdFwQl.setFczh(gdYg.getYgdjzmh());
                        gdFwQl.setDjlx(gdYg.getDjlx());
                        gdFwQl.setZslx(Constants.GDQL_YGZM_ZSLX);
                        gdYgNew.setYgid(gdBdcQlRel.getQlid());
                        gdYgNew.setProid(gdBdcQlRel.getQlid());
                        gdYgNewList.add(gdYgNew);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdYg.getProid());
                        if (gdXm != null) {
                            gdXm.setProid(gdBdcQlRel.getQlid());
                            gdXm.setZl(gdFw.getFwzl());
                            gdXmNewList.add(gdXm);
                        }
                    }
                    if (gdCf != null) {
                        GdCf gdCfNew = new GdCf();
                        BeanUtils.copyProperties(gdCfNew, gdCf);
                        gdFwQl.setFczh(gdCf.getCfwh());
                        gdFwQl.setDjlx(gdCf.getDjlx());
                        gdFwQl.setZslx(Constants.GDQL_CFWH_ZSLX);
                        gdCfNew.setCfid(gdBdcQlRel.getQlid());
                        gdCfNew.setProid(gdBdcQlRel.getQlid());
                        gdCfNewList.add(gdCfNew);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdCf.getProid());
                        if (gdXm != null) {
                            gdXm.setProid(gdBdcQlRel.getQlid());
                            gdXm.setZl(gdFw.getFwzl());
                            gdXmNewList.add(gdXm);
                        }
                    }
                    if (gdDy != null) {
                        GdDy gdDyNew = new GdDy();
                        BeanUtils.copyProperties(gdDyNew, gdDy);
                        gdFwQl.setFczh(gdDy.getDydjzmh());
                        gdFwQl.setDjlx(gdDy.getDjlx());
                        gdFwQl.setZslx(Constants.GDQL_TXZM_ZSLX);
                        gdDyNew.setDyid(gdBdcQlRel.getQlid());
                        gdDyNew.setProid(gdBdcQlRel.getQlid());
                        gdDyNewList.add(gdDyNew);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdDy.getProid());
                        if (gdXm != null) {
                            gdXm.setProid(gdBdcQlRel.getQlid());
                            gdXm.setZl(gdFw.getFwzl());
                            gdXmNewList.add(gdXm);
                        }
                    }

                    gdFwQlNewList.add(gdFwQl);
                }
            }
        } else if (CollectionUtils.isNotEmpty(gdBdcQlRelList) && StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 删除gd_td_ql
             */
            entityMapper.deleteByPrimaryKey(GdTdQl.class, qlid);
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 删除gd_qlr
             */
            example = new Example(GdQlr.class);
            example.createCriteria().andEqualTo("qlid", qlid);
            List<GdQlr> gdQlrList = entityMapper.selectByExampleNotNull(example);
            entityMapper.deleteByExampleNotNull(example);
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                StringBuilder qlrMcs = new StringBuilder();
                if (CollectionUtils.isNotEmpty(gdQlrList)) {
                    for (GdQlr gdQlr : gdQlrList) {
                        GdQlr gdQlrNew = new GdQlr();
                        BeanUtils.copyProperties(gdQlrNew, gdQlr);
                        if (StringUtils.isBlank(qlrMcs)){
                            qlrMcs.append(gdQlr.getQlr());
                        }else{
                            qlrMcs.append(",").append(gdQlr.getQlr());
                        }
                        gdQlrNew.setProid(gdBdcQlRel.getQlid());
                        gdQlrNew.setQlid(gdBdcQlRel.getQlid());
                        gdQlrNew.setQlrid(UUIDGenerator.generate18());
                        gdQlrNewList.add(gdQlrNew);
                    }
                }
                GdTd gdTd = entityMapper.selectByPrimaryKey(GdTd.class, gdBdcQlRel.getBdcid());
                if (gdTd != null) {
                    /**
                     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
                     * @description 重置gd_fw_ql
                     */
                    GdTdQl gdTdQl = new GdTdQl();
                    gdTdQl.setQlid(gdBdcQlRel.getQlid());
                    gdTdQl.setProid(gdBdcQlRel.getQlid());
                    gdTdQl.setTdzl(gdTd.getZl());
                    gdTdQl.setQlr(qlrMcs.toString());
                    if (gdTdsyq != null) {
                        GdTdsyq gdTdsyqNew = new GdTdsyq();
                        BeanUtils.copyProperties(gdTdsyqNew, gdTdsyq);
                        gdTdQl.setTdzh(gdTdsyq.getTdzh());
                        gdTdQl.setDjlx(gdTdsyq.getDjlx());
                        if (StringUtils.equals(gdTdsyq.getIssynq(), "1")) {
                            gdTdQl.setZslx(Constants.GDQL_TDSYONGQ_ZSLX);
                        }else {
                            gdTdQl.setZslx(Constants.GDQL_TDSYQ_ZSLX);
                        }
                        gdTdsyqNew.setQlid(gdBdcQlRel.getQlid());
                        gdTdsyqNew.setProid(gdBdcQlRel.getQlid());
                        gdTdsyqNewList.add(gdTdsyqNew);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdTdsyq.getProid());
                        if (gdXm != null) {
                            gdXm.setProid(gdBdcQlRel.getQlid());
                            gdXm.setZl(gdTd.getZl());
                            gdXmNewList.add(gdXm);
                        }
                    }
                    if (gdYg != null) {
                        gdTdQl.setTdzh(gdYg.getYgdjzmh());
                        gdTdQl.setDjlx(gdYg.getDjlx());
                        gdTdQl.setZslx(Constants.GDQL_YGZM_ZSLX);
                        gdYg.setYgid(gdBdcQlRel.getQlid());
                        gdYg.setProid(gdBdcQlRel.getQlid());
                        gdYgNewList.add(gdYg);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdYg.getProid());
                        gdXm.setProid(gdBdcQlRel.getQlid());
                        gdXm.setZl(gdTd.getZl());
                        gdXmNewList.add(gdXm);
                    }
                    if (gdCf != null) {
                        GdCf gdCfNew = new GdCf();
                        BeanUtils.copyProperties(gdCfNew, gdCf);
                        gdTdQl.setTdzh(gdCf.getCfwh());
                        gdTdQl.setDjlx(gdCf.getDjlx());
                        gdTdQl.setZslx(Constants.GDQL_CFWH_ZSLX);
                        gdCfNew.setCfid(gdBdcQlRel.getQlid());
                        gdCfNew.setProid(gdBdcQlRel.getQlid());
                        gdCfNewList.add(gdCfNew);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdCf.getProid());
                        if (gdXm != null) {
                            gdXm.setProid(gdBdcQlRel.getQlid());
                            gdXm.setZl(gdTd.getZl());
                            gdXmNewList.add(gdXm);
                        }
                    }
                    if (gdDy != null) {
                        GdDy gdDyNew = new GdDy();
                        BeanUtils.copyProperties(gdDyNew, gdDy);
                        gdTdQl.setTdzh(gdDy.getDydjzmh());
                        gdTdQl.setDjlx(gdDy.getDjlx());
                        gdTdQl.setZslx(Constants.GDQL_TXZM_ZSLX);
                        gdDyNew.setDyid(gdBdcQlRel.getQlid());
                        gdDyNew.setProid(gdBdcQlRel.getQlid());
                        gdDyNewList.add(gdDyNew);
                        GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdDy.getProid());
                        if (gdXm != null) {
                            gdXm.setProid(gdBdcQlRel.getQlid());
                            gdXm.setZl(gdTd.getZl());
                            gdXmNewList.add(gdXm);
                        }
                    }
                    gdTdQlNewList.add(gdTdQl);
                }
            }
        }
        /**
         * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
         * @description 删除权利和项目表
         */
        if (gdFwsyq != null) {
            entityMapper.deleteByPrimaryKey(GdFwsyq.class, gdFwsyq.getQlid());
            entityMapper.deleteByPrimaryKey(GdXm.class, gdFwsyq.getProid());
        }
        if (gdTdsyq != null) {
            entityMapper.deleteByPrimaryKey(GdTdsyq.class, gdTdsyq.getQlid());
            entityMapper.deleteByPrimaryKey(GdXm.class, gdTdsyq.getProid());
        }
        if (gdCf != null) {
            entityMapper.deleteByPrimaryKey(GdCf.class, gdCf.getCfid());
            entityMapper.deleteByPrimaryKey(GdXm.class, gdCf.getProid());
        }
        if (gdDy != null) {
            entityMapper.deleteByPrimaryKey(GdDy.class, gdDy.getDyid());
            entityMapper.deleteByPrimaryKey(GdXm.class, gdDy.getProid());
        }
        if (gdYg != null) {
            entityMapper.deleteByPrimaryKey(GdYg.class, gdYg.getYgid());
            entityMapper.deleteByPrimaryKey(GdXm.class, gdYg.getProid());
        }
        if (CollectionUtils.isNotEmpty(gdBdcQlRelNewList)) {
            for (GdBdcQlRel gdBdcQlRelNew : gdBdcQlRelNewList) entityMapper.insertSelective(gdBdcQlRelNew);
        }
        if (CollectionUtils.isNotEmpty(gdFwsyqNewList)) {
            for (GdFwsyq gdFwsyqNew : gdFwsyqNewList) entityMapper.insertSelective(gdFwsyqNew);
        }
        if (CollectionUtils.isNotEmpty(gdTdsyqNewList)) {
            for (GdTdsyq gdTdsyqNew : gdTdsyqNewList) entityMapper.insertSelective(gdTdsyqNew);
        }
        if (CollectionUtils.isNotEmpty(gdYgNewList)) {
            for (GdYg gdYgNew : gdYgNewList) entityMapper.insertSelective(gdYgNew);
        }
        if (CollectionUtils.isNotEmpty(gdCfNewList)) {
            for (GdCf gdCfNew : gdCfNewList) entityMapper.insertSelective(gdCfNew);
        }
        if (CollectionUtils.isNotEmpty(gdDyNewList)) {
            for (GdDy gdDyNew : gdDyNewList) entityMapper.insertSelective(gdDyNew);
        }
        if (CollectionUtils.isNotEmpty(gdXmNewList)) {
            for (GdXm gdXmNew : gdXmNewList) entityMapper.insertSelective(gdXmNew);
        }
        if (CollectionUtils.isNotEmpty(gdQlrNewList)) {
            for (GdQlr gdQlrNew : gdQlrNewList) entityMapper.insertSelective(gdQlrNew);
        }
        if (CollectionUtils.isNotEmpty(gdFwQlNewList)) {
            for (GdFwQl gdFwQlNew : gdFwQlNewList) entityMapper.insertSelective(gdFwQlNew);
        }
        if (CollectionUtils.isNotEmpty(gdTdQlNewList)) {
            for (GdTdQl gdTdQlNew : gdTdQlNewList) entityMapper.insertSelective(gdTdQlNew);
        }
        String splitMatchedGdData = AppConfig.getProperty("sjpp.splitMatchedGdData");
        if(StringUtils.equals(splitMatchedGdData,ParamsConstants.TRUE_LOWERCASE)){
            /*
            * jiangganzhi
            * 苏州拆分数据只会选择无限制权利的房屋产权，对有限制权利的产权不会进行拆分操作
            * 故此处拿新生成的gdfwsyq作为补全gdqldyhrel的权利，同时删除原权利对应的gdqldyhrel
            */
            completeSplitGdData(qlid,gdFwList);
        }
    }

    @Override
    public void completeSplitGdData(String yqlid,List<GdFw> gdFwList) {
        if(StringUtils.isNotBlank(yqlid) && CollectionUtils.isNotEmpty(gdFwList)){
            List<SyncIsfsssData> syncIsfsssDataList = new ArrayList<SyncIsfsssData>();
            //循环过渡房屋获取gddyhrel，根据匹配的单元号和拆分前qlid获取该房屋所对应的gdqldyhrel
            for(GdFw gdFw : gdFwList){
                SyncIsfsssData syncIsfsssData = new SyncIsfsssData();
                syncIsfsssData.setDah(gdFw.getDah());
                syncIsfsssData.setIsfsss(Constants.ISFWFSSS_NO);
                syncIsfsssDataList.add(syncIsfsssData);
                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdFw.getFwid());
                if(CollectionUtils.isNotEmpty(bdcGdDyhRelList)){
                    for(BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList){
                        //因为是拆分后的权利
                        // 一个房屋如果存在多个gddyhrel则是进行了多对多匹配（除数据问题，不存在匹配多个不动产单元，考虑匹配多本土地证情况）
                        // 此时gddyhrel有几条 则需增加几条gdqldyhrel
                        if(StringUtils.isNotBlank(bdcGdDyhRel.getBdcdyh()) && StringUtils.isNotBlank(bdcGdDyhRel.getGdid())){
                            //删除gdqldyhrel
                            List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(bdcGdDyhRel.getBdcdyh(), yqlid, "");
                            if(CollectionUtils.isNotEmpty(gdQlDyhRelList)){
                                for(GdQlDyhRel gdQlDyhRel : gdQlDyhRelList){
                                    entityMapper.deleteByPrimaryKey(GdQlDyhRel.class,gdQlDyhRel.getRelid());
                                }
                            }
                            String bdcdyh = bdcGdDyhRel.getBdcdyh();
                            String djid = bdcGdDyhRel.getDjid();
                            //根据gddyhrel的gdid和tdid找到房产和土地对应的现势权利作为gdqldyhrel的qlid和tdqlid
                            String qlid = null;
                            String tdqlid = null;
                            List<GdFwsyq> gdFwsyqList = gdFwService.queryGdFwsyqByFwidAndQszt(bdcGdDyhRel.getGdid(),ParamsConstants.GDQL_QSZT_XS);
                            if(CollectionUtils.isNotEmpty(gdFwsyqList)){
                                qlid = gdFwsyqList.get(0).getQlid();
                            }
                            if(StringUtils.isNotBlank(bdcGdDyhRel.getTdid())){
                                List<GdTdsyq> gdTdsyqList = gdTdService.queryGdTdsyqByTdidAndQszt(bdcGdDyhRel.getTdid(),ParamsConstants.GDQL_QSZT_XS);
                                if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                                    tdqlid = gdTdsyqList.get(0).getQlid();
                                }
                            }
                            GdQlDyhRel gdQlDyhRel = new GdQlDyhRel();
                            gdQlDyhRel.setRelid(UUIDGenerator.generate18());
                            gdQlDyhRel.setQlid(qlid);
                            gdQlDyhRel.setTdqlid(tdqlid);
                            gdQlDyhRel.setDjid(djid);
                            gdQlDyhRel.setBdclx(Constants.BDCLX_TDFW);
                            gdQlDyhRel.setBdcdyh(bdcdyh);
                            entityMapper.saveOrUpdate(gdQlDyhRel,gdQlDyhRel.getRelid());
                        }
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(syncIsfsssDataList)) {
                //同步权籍isFsss字段
                SyncIsfsssParam syncIsfsssParam = new SyncIsfsssParam();
                syncIsfsssParam.setData(syncIsfsssDataList);
                cadastralService.syncIsfsssForCadastral(syncIsfsssParam);
            }
        }
    }
}
