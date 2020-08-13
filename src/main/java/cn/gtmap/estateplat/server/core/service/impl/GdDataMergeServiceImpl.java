package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/9/1
 * @description 合并过渡数据
 */
@Service
public class GdDataMergeServiceImpl implements GdDataMergeService {
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;
    @Autowired
    EntityMapper entityMapper;

    private static final String PARAMETER_ISMERGE = "isMerge";

    @Override
    public HashMap checkGdDataMerge(String qlids,String bdclx) {
        HashMap resultMap = new HashMap();
        String isMerge = "yes";
        String msg = "选择的数据正确，确认合并数据？";

        //验证是不是多条数据，多条数据都针对同一个bdcid
        Boolean bdcidIsEqual = true;
        List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
        if(StringUtils.isNotBlank(qlids)){
            String[] qlidArr = qlids.split(",");
            for (String qlid : qlidArr) {
                if (StringUtils.isNotBlank(qlid)) {
                    if(StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                        GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(qlid);
                        if(gdFwsyq == null){
                            isMerge = "no";
                            msg = "选择的数据中存在非产权数据！";
                            resultMap.put(PARAMETER_ISMERGE, isMerge);
                            resultMap.put("msg", msg);
                            return resultMap;
                        }
                    }else if(StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
                        GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(qlid);
                        if(gdTdsyq == null){
                            isMerge = "no";
                            msg = "选择的数据中存在非产权数据！";
                            resultMap.put(PARAMETER_ISMERGE, isMerge);
                            resultMap.put("msg", msg);
                            return resultMap;
                        }
                    }

                    List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                    if(CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)){
                        gdBdcQlRelList.addAll(gdBdcQlRelListTemp);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(gdBdcQlRelList) && gdBdcQlRelList.size() > 1) {
            String bdcid = "";
            for (GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                if(gdBdcQlRel != null){
                    if(StringUtils.isBlank(bdcid)){
                        bdcid = gdBdcQlRel.getBdcid();
                    }else{
                        if(!StringUtils.equals(bdcid,gdBdcQlRel.getBdcid())) {
                            bdcidIsEqual = false;
                            break;
                        }
                    }
                }
            }
        }else{
            isMerge = "no";
            msg = "请至少选择两条数据！";
            resultMap.put(PARAMETER_ISMERGE, isMerge);
            resultMap.put("msg", msg);
            return resultMap;
        }

        if(bdcidIsEqual) {
            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){

                //验证选择的数据是否已经匹配过的数据
                for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                    if(StringUtils.isNotBlank(gdBdcQlRel.getQlid())){
                        String ppzt = gdFwService.getPpztByQlid(gdBdcQlRel.getQlid(), gdBdcQlRel.getBdclx(), gdBdcQlRel.getBdcid());
                        if(StringUtils.equals(ppzt, Constants.GD_PPZT_WCPP) || StringUtils.equals(ppzt,Constants.GD_PPZT_BFPP)){
                            isMerge = "no";
                            msg = "选择的数据中有已匹配的数据，请先取消匹配！";
                            resultMap.put(PARAMETER_ISMERGE, isMerge);
                            resultMap.put("msg", msg);
                            return resultMap;
                        }
                    }
                }

                //验证选择的数据是否已办理过项目
                for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                    if(StringUtils.isNotBlank(gdBdcQlRel.getQlid())){
                        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByYqlid(gdBdcQlRel.getQlid());
                        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
                            isMerge = "no";
                            msg = "选择的数据中有已创建项目的数据，无法合并！";
                            resultMap.put(PARAMETER_ISMERGE, isMerge);
                            resultMap.put("msg", msg);
                            return resultMap;
                        }
                    }
                }
            }
        }else{
            isMerge = "no";
            msg = "请选择分别持证的数据！";
            resultMap.put(PARAMETER_ISMERGE, isMerge);
            resultMap.put("msg", msg);
            return resultMap;
        }

        resultMap.put(PARAMETER_ISMERGE, isMerge);
        resultMap.put("msg", msg);
        return resultMap;
    }

    @Transactional
    @Override
    public void mergeGdData(String qlids, String bdclx) throws IllegalAccessException, InvocationTargetException {
        GdBdcQlRel gdBdcQlRelNew = new GdBdcQlRel();
        GdFwsyq gdFwsyqNew = new GdFwsyq();
        GdTdsyq gdTdsyqNew = new GdTdsyq();
        GdXm gdXmNew = new GdXm();
        List<GdQlr> gdQlrNewList = new ArrayList<GdQlr>();
        GdFwQl gdFwQlNew = new GdFwQl();
        GdTdQl gdTdQlNew = new GdTdQl();

        List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
        if(StringUtils.isNotBlank(qlids)){
            String[] qlidArr = qlids.split(",");
            for (String qlid : qlidArr) {
                if (StringUtils.isNotBlank(qlid)) {
                    List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByQlid(qlid);
                    if(CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)){
                        gdBdcQlRelList.addAll(gdBdcQlRelListTemp);
                    }
                }
            }
        }

        String bdcid = "";
        StringBuilder fczh = new StringBuilder();
        StringBuilder tdzh = new StringBuilder();
        StringBuilder qlr = new StringBuilder();
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                if(gdBdcQlRel != null&&StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                    if(StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
                        GdFwsyq gdFwsyq = entityMapper.selectByPrimaryKey(GdFwsyq.class, gdBdcQlRel.getQlid());
                        GdFwQl gdFwQl= entityMapper.selectByPrimaryKey(GdFwQl.class, gdBdcQlRel.getQlid());
                        if(gdFwsyq != null){
                            if(StringUtils.isBlank(gdXmNew.getProid())){
                                GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdFwsyq.getProid());
                                BeanUtils.copyProperties(gdXmNew, gdXm);
                            }
                            if(StringUtils.isBlank(gdFwsyqNew.getQlid())){
                                BeanUtils.copyProperties(gdFwsyqNew, gdFwsyq);
                            }

                            if(StringUtils.isNotBlank(gdFwsyq.getFczh())){
                                if(StringUtils.isBlank(fczh)) {
                                    fczh.append(StringUtils.deleteWhitespace(gdFwsyq.getFczh()));
                                }else {
                                    fczh.append(",").append(StringUtils.deleteWhitespace(gdFwsyq.getFczh()));
                                }
                            }

                            entityMapper.deleteByPrimaryKey(GdFwsyq.class, gdFwsyq.getQlid());
                            entityMapper.deleteByPrimaryKey(GdXm.class, gdFwsyq.getProid());
                        }

                        if(gdFwQl != null){
                            if(StringUtils.isBlank(gdFwQlNew.getQlid())){
                                BeanUtils.copyProperties(gdFwQlNew, gdFwQl);
                            }
                            entityMapper.deleteByPrimaryKey(GdFwQl.class,gdFwQl.getQlid());
                        }

                    }else if(StringUtils.equals(bdclx, Constants.BDCLX_TD)){
                        GdTdsyq gdTdsyq = entityMapper.selectByPrimaryKey(GdTdsyq.class, gdBdcQlRel.getQlid());
                        GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class, gdBdcQlRel.getQlid());
                        if(gdTdsyq != null){
                            if(StringUtils.isBlank(gdXmNew.getProid())){
                                GdXm gdXm = entityMapper.selectByPrimaryKey(GdXm.class, gdTdsyq.getProid());
                                BeanUtils.copyProperties(gdXmNew, gdXm);
                            }
                            if(StringUtils.isBlank(gdTdsyqNew.getQlid())){
                                BeanUtils.copyProperties(gdTdsyqNew, gdTdsyq);
                            }

                            if(StringUtils.isNotBlank(gdTdsyq.getTdzh())){
                                if(StringUtils.isBlank(tdzh)) {
                                    tdzh.append(StringUtils.deleteWhitespace(gdTdsyq.getTdzh()));
                                }else {
                                    tdzh.append(",").append(StringUtils.deleteWhitespace(gdTdsyq.getTdzh()));
                                }
                            }
                            entityMapper.deleteByPrimaryKey(GdTdsyq.class, gdTdsyq.getQlid());
                            entityMapper.deleteByPrimaryKey(GdXm.class, gdTdsyq.getProid());
                        }

                        if(StringUtils.isBlank(gdFwQlNew.getQlid())){
                            if(gdFwQlNew == null){
                                BeanUtils.copyProperties(gdTdQlNew, gdTdQl);
                            }
                            entityMapper.deleteByPrimaryKey(GdTdQl.class,gdTdQl.getQlid());
                        }
                    }

                    if(StringUtils.isBlank(bdcid) && StringUtils.isNotBlank(gdBdcQlRel.getBdcid()))
                        bdcid = gdBdcQlRel.getBdcid();

                    Example example = new Example(GdQlr.class);
                    example.createCriteria().andEqualTo("qlid", gdBdcQlRel.getQlid());
                    List<GdQlr> gdQlrList = entityMapper.selectByExampleNotNull(example);
                    if(CollectionUtils.isNotEmpty(gdQlrList)){
                        for(GdQlr gdQlr:gdQlrList){
                            gdQlrNewList.add(gdQlr);
                            if(StringUtils.equals(gdQlr.getQlrlx(), Constants.QLRLX_QLR) && StringUtils.isNotBlank(gdQlr.getQlr())){
                                if(StringUtils.isBlank(qlr)){
                                    qlr.append(StringUtils.deleteWhitespace(gdQlr.getQlr()));
                                } else{
                                    qlr.append(",").append(StringUtils.deleteWhitespace(gdQlr.getQlr()));
                                }
                            }
                        }
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                entityMapper.deleteByPrimaryKey(GdBdcQlRel.class, gdBdcQlRel.getRelid());
                Example example = new Example(GdQlr.class);
                example.createCriteria().andEqualTo("qlid", gdBdcQlRel.getQlid());
                entityMapper.deleteByExampleNotNull(example);
            }
            //重置gd_bdc_ql_rel
            gdBdcQlRelNew.setRelid(UUIDGenerator.generate18());
            gdBdcQlRelNew.setQlid(UUIDGenerator.generate18());
            gdBdcQlRelNew.setBdcid(bdcid);
            gdBdcQlRelNew.setBdclx(bdclx);
            entityMapper.saveOrUpdate(gdBdcQlRelNew,gdBdcQlRelNew.getRelid());
        }

        //重置gd_xm
        if(gdXmNew != null){
            gdXmNew.setProid(gdBdcQlRelNew.getQlid());
            entityMapper.saveOrUpdate(gdXmNew,gdXmNew.getProid());
        }

        if(StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            //重置gd_fwsyq
            if(gdFwsyqNew != null){
                gdFwsyqNew.setProid(gdBdcQlRelNew.getQlid());
                gdFwsyqNew.setQlid(gdBdcQlRelNew.getQlid());
                if(StringUtils.isNotBlank(fczh))
                    gdFwsyqNew.setFczh(fczh.toString());
                entityMapper.saveOrUpdate(gdFwsyqNew,gdFwsyqNew.getQlid());
            }
            //重置gd_fw_ql
            if(gdFwQlNew != null){
                gdFwQlNew.setProid(gdBdcQlRelNew.getQlid());
                gdFwQlNew.setQlid(gdBdcQlRelNew.getQlid());
                if(StringUtils.isNotBlank(fczh))
                    gdFwQlNew.setFczh(fczh.toString());
                if(StringUtils.isNotBlank(qlr))
                    gdFwQlNew.setQlr(qlr.toString());
                entityMapper.saveOrUpdate(gdFwQlNew,gdFwQlNew.getQlid());
            }

        }else if(StringUtils.equals(bdclx, Constants.BDCLX_TD)) {
            //重置gd_tdsyq
            if(gdTdsyqNew != null){
                gdTdsyqNew.setProid(gdBdcQlRelNew.getQlid());
                gdTdsyqNew.setQlid(gdBdcQlRelNew.getQlid());
                if(StringUtils.isNotBlank(tdzh))
                    gdTdsyqNew.setTdzh(tdzh.toString());
                entityMapper.saveOrUpdate(gdTdsyqNew,gdTdsyqNew.getQlid());
            }
            //重置gd_td_ql
            if(gdTdQlNew != null){
                gdTdQlNew.setProid(gdBdcQlRelNew.getQlid());
                gdTdQlNew.setQlid(gdBdcQlRelNew.getQlid());
                if(StringUtils.isNotBlank(tdzh))
                    gdTdQlNew.setTdzh(tdzh.toString());
                if(StringUtils.isNotBlank(qlr))
                    gdTdQlNew.setQlr(qlr.toString());
                entityMapper.saveOrUpdate(gdTdQlNew,gdTdQlNew.getQlid());
            }
        }

        //重置gd_qlr
        if(CollectionUtils.isNotEmpty(gdQlrNewList)){
            for(GdQlr gdQlr:gdQlrNewList){
                gdQlr.setQlrid(UUIDGenerator.generate18());
                gdQlr.setProid(gdBdcQlRelNew.getQlid());
                gdQlr.setQlid(gdBdcQlRelNew.getQlid());
                entityMapper.saveOrUpdate(gdQlr,gdQlr.getQlrid());
            }
        }
    }

}
