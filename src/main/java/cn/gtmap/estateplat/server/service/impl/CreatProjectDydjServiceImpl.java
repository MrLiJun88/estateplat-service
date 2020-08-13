package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 * 抵押登记创建项目服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-26
 */
public class CreatProjectDydjServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;

    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project = null;
        List<BdcQlr> ybdcQlrList = null;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        } else
            throw new AppException(4005);
        String pplx = AppConfig.getProperty("sjpp.type");
        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        //zdd 此段代码需要优化  应该在service外面对 project的qllx赋值
        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            list.addAll(bdcXmRelList);
        }
        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm = null;
        if (StringUtils.equals(project.getSfyc(), "1")) {
            initVoFromParm = super.getDjxxByDjsjFwychs(xmxx);
        } else {
            initVoFromParm = super.getDjxx(xmxx);
        }
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }
            // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
            BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
            if (bdcTd == null) {
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                list.add(bdcTd);
            }
        }

        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null) {
            list.add(bdcSpxx);
        }
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
            //sc根据不动产单元号获取预告项目，如果存在把权利人获取过来      预购商品房抵押登记
            if (CommonUtil.indexOfStrs(Constants.SQLX_FWDYSCDJ_DM, bdcxm.getSqlx())) {
                HashMap map = new HashMap();
                //zdd 找到权利状态正常且预告登记类型为预售商品房抵押权预告登记的预告记录
                map.put("bdcdyid", bdcdy.getBdcdyid());
                map.put("qszt", Constants.QLLX_QSZT_XS);
                List<QllxVo> bdcYgList = qllxService.andEqualQueryQllx(new BdcYg(), map);

                if (CollectionUtils.isNotEmpty(bdcYgList)) {
                    //先获取该不动产单元做过的所有预告登记，再取得预告登记种类为抵押预告的预告登记，再取得其proid对应的权利人和义务人
                    for (int i = 0; i < bdcYgList.size(); i++) {
                        QllxVo qllxVo = bdcYgList.get(i);
                        BdcYg bdcYg = (BdcYg) qllxVo;
                        String ygdjzl = bdcYg.getYgdjzl();
                        if (StringUtils.isNotBlank(ygdjzl) &&
                                CommonUtil.indexOfStrs(Constants.YG_YGDJZL_DY, ygdjzl)) {

                            List<BdcQlr> ybdcDyQlrList = bdcQlrService.queryBdcQlrByProid(bdcYg.getProid());
                            if (CollectionUtils.isNotEmpty(ybdcDyQlrList)) {
                                List<BdcQlr> tempBdcQlrs = new ArrayList<BdcQlr>();
                                List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
                                if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                                    for (BdcQlr bdcQlr : tempBdcQlrList) {
                                        bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                                    }
                                }
                                for (BdcQlr bdcQlr : ybdcDyQlrList) {
                                    bdcQlr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcQlr, tempBdcQlrs, project.getProid());
                                    tempBdcQlrs.add(bdcQlr);
                                }
                                list.addAll(tempBdcQlrs);
                            }
                        }
                    }


                }
            }
        }
        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcYwrList = initBdcQlrFromDjsj(initVoFromParm.getProject(), initVoFromParm.getDjsjFwQlrList(), initVoFromParm.getDjsjLqxx(), initVoFromParm.getDjsjZdxxList(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getCbzdDcb(), Constants.QLRLX_YWR);
        //zdd 不动产单元
        //zdd 权利人信息
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC) && StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
            List<BdcQlr> qlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(qlrList)) {
                list.addAll(qlrList);
            }
        } else {
            //不是过渡数据执行
            if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                List<BdcQlr> tempBdcYwrs = new ArrayList<BdcQlr>();
                for (BdcQlr bdcQlr : ybdcQlrList) {
                    bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                    tempBdcYwrs.add(bdcQlr);
                }
                bdcYwrList = tempBdcYwrs;
            }
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                list.addAll(bdcYwrList);
            }
        }
        //在建工程对原先的土地抵押进行注销
        String isCancelYtdDyForZjgcDy = AppConfig.getProperty("isCancelYtdDyForZjgcDy");
        if ((StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_ZJJZWDY_DDD_FW_DM) || StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_ZJJZWDY_FW_DM)) && StringUtils.equals(isCancelYtdDyForZjgcDy, "true")) {
            checkYtdHasDy(project, list);
        }

        return list;
    }

    /**
     * 原土地抵押
     *
     * @param project
     * @param list
     */
    public void checkYtdHasDy(Project project, List<InsertVo> list) {
        String bdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
        if (StringUtils.isNotBlank(bdcdyh)) {
            GdDy gdDy = null;
            BdcDyaq bdcDyaq = getBdcDyaqByBdcdyh(bdcdyh);
            if (bdcDyaq == null) {
                gdDy = getGdDyByBdcdyh(bdcdyh);
            }
            if (bdcDyaq != null || gdDy != null) {
                setYcqzhBdcXm(list, bdcDyaq, gdDy);
                initBdcXmRel(project, bdcDyaq, gdDy, list);
            }
        }
    }

    /**
     * @param list
     * @param bdcDyaq
     * @param gdDy
     */
    public void setYcqzhBdcXm(List<InsertVo> list, BdcDyaq bdcDyaq, GdDy gdDy) {
        StringBuilder ybdcqzh = new StringBuilder();
        if(bdcDyaq != null){
            if(StringUtils.isNotBlank(bdcDyaq.getProid())){
                BdcXm bdcXm = bdcXmService.getBdcXmByProid(bdcDyaq.getProid());
                if(bdcXm != null&&StringUtils.isNotBlank(bdcXm.getYbdcqzh())) {
                    ybdcqzh.append(bdcXm.getYbdcqzh());
                }
            }

        }else{
            List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
            if(gdDy != null){
                List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdDy.getDyid());
                if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                    for(GdBdcQlRel gdBdcQlRel:gdBdcQlRelList){
                        if(StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                            List<GdTdsyq> gdTdsyqListTmep = gdTdService.getGdTdsyqByTdid(gdBdcQlRel.getBdcid());
                            if(CollectionUtils.isNotEmpty(gdTdsyqListTmep)){
                                gdTdsyqList.addAll(gdTdsyqListTmep);
                            }
                        }
                    }
                }
            }

            if(CollectionUtils.isNotEmpty(gdTdsyqList)){
                for(GdTdsyq gdTdsyq:gdTdsyqList){
                    if(gdTdsyq != null&& gdTdsyq.getIszx() != 1) {
                        if(StringUtils.isBlank(ybdcqzh)){
                            ybdcqzh.append(gdTdsyq.getTdzh());
                        }else{
                            ybdcqzh.append(",").append(gdTdsyq.getTdzh());
                        }
                    }
                }
            }
        }


        for (InsertVo insertVo : list) {
            if (insertVo instanceof BdcXm) {
                BdcXm bdcXm = (BdcXm) insertVo;
                bdcXm.setYbdcqzh(ybdcqzh.toString());
            }
        }
    }

    /**
     * 根据原土地bdcdyh获取bdc_dyaq
     *
     * @param bdcdyh
     * @return
     */
    public BdcDyaq getBdcDyaqByBdcdyh(String bdcdyh) {
        BdcDyaq bdcDyaq = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("bdcDyh", bdcdyh);
        param.put("qszt",Constants.QLLX_QSZT_XS);
        List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(param);
        if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
            bdcDyaq = bdcDyaqList.get(0);
        }
        return bdcDyaq;
    }

    /**
     * 根据原土地bdcdyh获取gd_dy
     *
     * @param bdcdyh
     * @return
     */
    public GdDy getGdDyByBdcdyh(String bdcdyh) {
        GdDy gdDy = null;
        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(bdcdyh);
        if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
            BdcGdDyhRel bdcGdDyhRel = bdcGdDyhRelList.get(0);
            String tdid = StringUtils.isNotBlank(bdcGdDyhRel.getTdid())?bdcGdDyhRel.getTdid():bdcGdDyhRel.getGdid();
            if (StringUtils.isNotBlank(tdid)) {
                List<GdDy> gdDyList = gdTdService.queryTddyqByTdid(tdid,Constants.GDQL_ISZX_WZX);
                if (CollectionUtils.isNotEmpty(gdDyList)) {
                    for (GdDy dy : gdDyList) {
                        gdDy = dy;
                        break;
                    }
                }
            }
        }
        return gdDy;
    }

    /**
     * 创建bdc_xm_rel
     *
     * @param project
     * @param bdcDyaq
     * @param gdDy
     * @param list
     */
    public void initBdcXmRel(Project project, BdcDyaq bdcDyaq, GdDy gdDy, List<InsertVo> list) {
        String yqlid = null != bdcDyaq ? bdcDyaq.getQlid() : gdDy.getDyid();
        String yproid = null != bdcDyaq ? bdcDyaq.getProid() : gdDy.getProid();
        BdcXmRel bdcXmRel = new BdcXmRel();
        bdcXmRel.setRelid(UUIDGenerator.generate18());
        bdcXmRel.setProid(project.getProid());
        bdcXmRel.setYqlid(yqlid);
        bdcXmRel.setYproid(yproid);
        bdcXmRel.setQjid(project.getDjId());
        bdcXmRel.setYdjxmly(null != bdcDyaq ? Constants.XMLY_BDC : Constants.XMLY_FWSP);
        list.add(bdcXmRel);
    }

    public List<InsertVo> initVoFromOldDataMul(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        } else {
            throw new AppException(4005);
        }

        List<InsertVo> list = new ArrayList<InsertVo>();
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        //zdd 此段代码需要优化  应该在service外面对 project的qllx赋值
        list.add(bdcxm);
        //zdd 需要完善   一个项目有可能有多个项目关系表
        BdcXmRel bdcXmRel = bdcXmRelService.creatBdcXmRelFromProjectMul(project);
        list.add(bdcXmRel);
        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm = super.getDjxx(xmxx);
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }

            // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
            BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
            if (bdcTd == null) {
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                list.add(bdcTd);
            }
        }

        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null) {
            list.add(bdcSpxx);
        }
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdyMul(initVoFromParm, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
            //sc根据不动产单元号获取预告项目，如果存在把权利人获取过来      预购商品房抵押登记
            if (CommonUtil.indexOfStrs(Constants.SQLX_FWDYSCDJ_DM, bdcxm.getSqlx())) {
                HashMap map = new HashMap();
                //zdd 找到权利状态正常且预告登记类型为预售商品房抵押权预告登记的预告记录
                map.put("bdcdyid", bdcdy.getBdcdyid());
                map.put("qszt", Constants.QLLX_QSZT_XS);
                map.put("ygdjzl", "3");
                List<QllxVo> bdcYgList = qllxService.andEqualQueryQllx(new BdcYg(), map);
                if (CollectionUtils.isNotEmpty(bdcYgList)) {
                    List<BdcQlr> ybdcDyQlrList = bdcQlrService.queryBdcQlrByProid(bdcYgList.get(0).getProid());
                    if (CollectionUtils.isNotEmpty(ybdcDyQlrList)) {
                        List<BdcQlr> tempBdcQlrs = new ArrayList<BdcQlr>();
                        for (BdcQlr bdcQlr : ybdcDyQlrList) {
                            bdcQlr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcQlr, tempBdcQlrs, project.getProid());
                            tempBdcQlrs.add(bdcQlr);
                        }
                        list.addAll(tempBdcQlrs);
                    }
                }
            }
        }
        //zdd 地籍数据权利人初始化
        List<BdcQlr> bdcYwrList = initBdcQlrFromDjsj(initVoFromParm.getProject(), initVoFromParm.getDjsjFwQlrList(), initVoFromParm.getDjsjLqxx(), initVoFromParm.getDjsjZdxxList(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getCbzdDcb(), Constants.QLRLX_YWR);
        //zdd 不动产单元
        //zdd 权利人信息
        //不是过渡数据执行
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            //获取现在项目上一手义务人
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            //多个不动产不需要删除上一手义务人
            List<BdcQlr> tempBdcYwrs = new ArrayList<BdcQlr>();
            //当前选中的权利人
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                tempBdcYwrs.add(bdcQlr);
            }
            bdcYwrList = tempBdcYwrs;
        }
        if (CollectionUtils.isNotEmpty(bdcYwrList)) {
            //多个不动产不需要删除上一手义务人
            for (BdcQlr bdcQlr : bdcYwrList) {
                Example example = new Example(BdcQlr.class);
                example.createCriteria().andEqualTo("proid", project.getProid()).andEqualTo("qlr", bdcQlr.getQlrmc()).andEqualTo("qlrzjh", bdcQlr.getQlrzjh());
                entityMapper.deleteByExample(BdcQlr.class, example);
            }
            list.addAll(bdcYwrList);
        }
        return list;
    }
}
