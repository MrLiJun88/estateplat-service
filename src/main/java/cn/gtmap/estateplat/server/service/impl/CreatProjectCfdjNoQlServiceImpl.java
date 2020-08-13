package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/* @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
        * @version 1.0, 2016/4/27
        * @description 纯土地不匹配不动产单元，不传入原权利
        * 查封登记创建服务（不生成权利人）
        */
public class CreatProjectCfdjNoQlServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    private BdcZdGlService bdcZdGlService;


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        } else {
            throw new AppException(4005);
        }

        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if(bdcxm != null) {
            bdcxm.setXmzt(Constants.XMZT_SZ);
        }
        if (StringUtils.isNotBlank(project.getYxmid())) {
            BdcXm ybdcxm = bdcXmService.getBdcXmByProid(project.getYxmid());
            bdcxm = readYbdcxm(bdcxm, ybdcxm);
        }
        insertVoList.add(bdcxm);
        //zdd 需要完善   一个项目有可能有多个项目关系表
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            insertVoList.addAll(bdcXmRelList);
        }

        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null){
            insertVoList.add(bdcSpxx);
        }

        //初始化过度查封信息及相关联表单
        if(CollectionUtils.isNotEmpty(bdcXmRelList)){
            for(BdcXmRel bdcXmRel:bdcXmRelList){
                if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYqlid())) {

                    //初始化过渡项目
                    GdXm gdxm = new GdXm();
                    gdxm.setProid(bdcXmRel.getProid());
                    gdxm.setSlbh(bdcxm.getBh());
                    gdxm.setZl(bdcxm.getZl());
                    gdxm.setPpzt(Constants.GD_PPZT_WPP);
                    gdxm.setBdclx(bdcxm.getBdclx());
                    gdxm.setDjlx(bdcZdGlService.getDjlxMcByDm(bdcxm.getDjlx(),bdcZdGlService.getBdcZdDjlx(null)));
                    gdxm.setDwdm(bdcxm.getDwdm());
                    if (StringUtils.equals(project.getBdclx(),Constants.BDCLX_TDFW)) {
                        gdxm.setXmly(Constants.XMLY_FWSP);
                    }else{
                        gdxm.setXmly(Constants.XMLY_TDSP);
                    }
                    entityMapper.saveOrUpdate(gdxm,gdxm.getProid());

                    //初始化过渡不动产和权利关系
                    GdCf gdCf = null;
                    if (StringUtils.isNotBlank(bdcXmRel.getYqlid()) && !StringUtils.equals(bdcxm.getXmly(), Constants.XMLY_BDC) && !StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_TDCF_DM_NEW) && !StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_FWCF_DM)) {
                        gdCf = gdFwService.getGdCfByCfid(bdcXmRel.getYqlid(), 0);
                    }
                    //获取过渡权利人
                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRel.getYqlid());
                    String cfid = UUIDGenerator.generate();
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (GdBdcQlRel gdBdcQlRel:gdBdcQlRelList) {
                            if (gdBdcQlRel != null && StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                                gdBdcQlRel.setRelid(UUIDGenerator.generate());
                                gdBdcQlRel.setQlid(cfid);
                                entityMapper.insertSelective(gdBdcQlRel);
                            }
                        }
                    }

                    //初始化过渡查封
                    if(null == gdCf){
                        gdCf = new GdCf();
                    }else{
                        gdCf.setCflx(Constants.CFLX_GD_XF);
                    }
                    //初始化过渡查封的查封类型
                    if(StringUtils.isNotBlank(bdcxm.getDjzx())){
                        if(StringUtils.equals(bdcxm.getDjzx(),Constants.DJZX_LH)){
                            gdCf.setCflx(Constants.CFLX_GD_LHCF);
                        }
                        if(StringUtils.equals(bdcxm.getDjzx(),Constants.DJZX_XF)){
                            gdCf.setCflx(Constants.CFLX_GD_XF);
                        }
                        if(StringUtils.equals(bdcxm.getDjzx(),Constants.DJZX_CF)){
                            gdCf.setCflx(Constants.CFLX_GD_CF);
                        }
                        if(StringUtils.equals(bdcxm.getDjzx(),Constants.DJZX_YCF)){
                            gdCf.setCflx(Constants.CFLX_GD_YCF);
                        }
                    }
                    if(StringUtils.isNotBlank(bdcxm.getBdclx())){
                        gdCf.setBdclx(bdcxm.getBdclx());
                    }
                    if(StringUtils.isNotBlank(bdcxm.getDjlx())){
                        gdCf.setDjlx(bdcxm.getDjlx());
                    }
                    gdCf.setCfid(cfid);
                    gdCf.setProid(bdcXmRel.getProid());
                    gdCf.setIsjf(0);
                    gdCf.setCfksrq(gdCf.getCfjsrq());
                    gdCf.setCfjsrq(null);
                    //qijiadong 为gdcf的yqzh赋值
                    if (StringUtils.isNotBlank(bdcXmRel.getYqlid()) && !StringUtils.equals(bdcxm.getXmly(), Constants.XMLY_BDC)) {
                        if (StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_TDCF_DM_NEW)) {
                            GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(bdcXmRel.getYqlid());
                            if (gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getTdzh())) {
                                gdCf.setYqzh(gdTdsyq.getTdzh());
                            }
                        }
                        if (StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_FWCF_DM)) {
                            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(bdcXmRel.getYqlid());
                            if (gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getFczh())) {
                                gdCf.setYqzh(gdFwsyq.getFczh());
                            }
                        }
                    }
                    entityMapper.insertSelective(gdCf);
                    List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid(bdcXmRel.getYqlid(), Constants.QLRLX_QLR);
                    if (CollectionUtils.isNotEmpty(gdQlrs)) {
                        for (GdQlr gdQlr : gdQlrs) {
                            gdQlr.setQlrid(UUIDGenerator.generate());
                            gdQlr.setQlid(gdCf.getCfid());
                            gdQlr.setProid(bdcXmRel.getProid());
                            entityMapper.insertSelective(gdQlr);
                        }
                    }
                    if (project.getBdclx().equals(Constants.BDCLX_TDFW)) {
                        GdFwQl gdFwQl = new GdFwQl();
                        gdFwQl.setQlid(gdCf.getCfid());
                        gdFwQl.setProid(gdCf.getProid());
                        gdFwQl.setZslx(Constants.GDQL_CFWH_ZSLX);
                        gdFwQl.setFwzl(bdcSpxx.getZl());
                        gdFwQl.setIszx("0");
                        entityMapper.saveOrUpdate(gdFwQl, gdFwQl.getQlid());
                    } else {
                        GdTdQl gdTdQl = new GdTdQl();
                        gdTdQl.setQlid(gdCf.getCfid());
                        gdTdQl.setProid(gdCf.getProid());
                        gdTdQl.setZslx(Constants.GDQL_CFWH_ZSLX);
                        gdTdQl.setTdzl(bdcSpxx.getZl());
                        gdTdQl.setIszx("0");
                        entityMapper.saveOrUpdate(gdTdQl, gdTdQl.getQlid());
                    }
                }
            }
        }

        //查封没有匹配不动产单元时，bdcdyid为过渡表里面的bdcid
        if(CollectionUtils.isNotEmpty(bdcXmRelList)&&StringUtils.isNotBlank(bdcXmRelList.get(0).getYqlid())){
            List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(bdcXmRelList.get(0).getYqlid());
            if(StringUtils.isNotBlank(gdBdcQlRelList.get(0).getBdcid())){
                bdcxm.setBdcdyid(gdBdcQlRelList.get(0).getBdcid());
            }
        }

        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcQlrList = null;
        if (StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
            bdcQlrList = initBdcQlrFromDjsj(initVoFromParm.getProject(), initVoFromParm.getDjsjFwQlrList(),initVoFromParm.getDjsjLqxx(),initVoFromParm.getDjsjZdxxList(),initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getCbzdDcb(), Constants.QLRLX_QLR);
        }
        //zdd 权利人信息
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            List<BdcQlr> tempBdcQlrs = new ArrayList<BdcQlr>();
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                tempBdcQlrs.add(bdcQlr);
            }
            bdcQlrList = tempBdcQlrs;
        }
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            insertVoList.addAll(bdcQlrList);
        }
        return insertVoList;
    }
}
