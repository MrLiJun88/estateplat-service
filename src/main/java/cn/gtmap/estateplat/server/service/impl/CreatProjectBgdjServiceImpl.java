package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * 变更登记创建项目服务
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-29
 */
public class CreatProjectBgdjServiceImpl extends CreatProjectDefaultServiceImpl {

    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private DjsjFwMapper djsjFwMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private DjsjZdbgjlbService djsjZdbgjlbService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            ybdcQlrList = getYbdcQlrList(project);
        }else {
            throw new AppException(4005);
        }
        List<InsertVo> list = new ArrayList<InsertVo>();
        String pplx = AppConfig.getProperty("sjpp.type");

        //分割合并变更流程是否根据权籍不动产单元历史关系表自动关联分割或合并前证书
        String autoRelateZs = AppConfig.getProperty("fghb.autoRelateZs");
        if(StringUtils.equals(autoRelateZs,"true") && StringUtils.isNotBlank(project.getSqlx()) && (StringUtils.equals(project.getSqlx(),Constants.SQLX_FWFGHBBG_DM)||StringUtils.equals(project.getSqlx(),Constants.SQLX_TDFGHBBG_DM))){
            //lj 在此处关联老证
            initBdcXmRelList(project);
        }

        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        if(StringUtils.isBlank(bdcxm.getZl())){
            bdcxm.setZl(project.getZl());
        }
        list.add(bdcxm);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            list.addAll(bdcXmRelList);
        }

        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm=super.getDjxx(xmxx);
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
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
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
        }

        //商品房首次登记（开发商自持），信息表变更登记，选择逻辑幢、楼盘表时对bdc_xm_rel数据进行特殊处理
        List<InsertVo> listTemp = new ArrayList<InsertVo>();
        InsertVo insertVoTemp = null;
        if(StringUtils.isNotBlank(bdcxm.getSqlx()) && (StringUtils.equals(bdcxm.getSqlx(),Constants.SQLX_SPFSCKFSZC_DM)||StringUtils.equals(bdcxm.getSqlx(),Constants.SQLX_SPFXZBG_DM))
                && bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyid())){
            for(InsertVo insertVo:list){
                if(insertVo instanceof BdcXmRel){
                    insertVoTemp = insertVo;
                    BdcXmRel bdcXmRel = (BdcXmRel) insertVo;
                    if(bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getQjid()) && StringUtils.isBlank(bdcXmRel.getYproid())){
                        HashMap map = new HashMap();
                        map.put(ParamsConstants.BDCDYID_LOWERCASE,bdcdy.getBdcdyid());
                        map.put("qszt",Constants.QLLX_QSZT_XS);
                        List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcq(map);
                        if(CollectionUtils.isNotEmpty(bdcFdcqList)){
                           BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                            if(bdcFdcq != null){
                                bdcXmRel.setYproid(bdcFdcq.getProid());
                                bdcXmRel.setQjid("");
                                bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcFdcq.getProid());
                                if(CollectionUtils.isNotEmpty(bdcZsList)){
                                    BdcZs bdcZs = bdcZsList.get(0);
                                    if(bdcZs != null) {
                                        bdcxm.setYbdcqzh(bdcZs.getBdcqzh());
                                    }
                                }
                            }
                        }else{
                            HashMap hashMap = new HashMap();
                            hashMap.put("bdcdyh",bdcdy.getBdcdyh());
                            List<String> ybdcdyhList = djsjFwMapper.getYbdcdyhByBdcdyh(hashMap);
                            if(ybdcdyhList != null && ybdcdyhList.size() > 0){
                                if(ybdcdyhList.size() == 1) {
                                    String ybdcdyh = ybdcdyhList.get(0);
                                    String ybdcdyid = bdcdyService.getBdcdyidByBdcdyh(ybdcdyh);
                                    if (StringUtils.isNotBlank(ybdcdyid)) {
                                        map.put(ParamsConstants.BDCDYID_LOWERCASE, ybdcdyid);
                                        bdcFdcqList = bdcFdcqService.getBdcFdcq(map);
                                        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                                            BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                                            bdcXmRel.setYproid(bdcFdcq.getProid());
                                            bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                                            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcFdcq.getProid());
                                            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                                                BdcZs bdcZs = bdcZsList.get(0);
                                                if (bdcZs != null) {
                                                    bdcxm.setYbdcqzh(bdcZs.getBdcqzh());
                                                }
                                            }
                                        }
                                    }

                                } else{
                                    BdcXmRel bdcXmRelTemp;
                                    for(String ybdcdyh:ybdcdyhList){
                                        String ybdcdyid = bdcdyService.getBdcdyidByBdcdyh(ybdcdyh);
                                        if(StringUtils.isNotBlank(ybdcdyid)){
                                            bdcXmRelTemp = new BdcXmRel();
                                            bdcXmRelTemp.setRelid(UUIDGenerator.generate18());
                                            bdcXmRelTemp.setQjid(bdcXmRel.getQjid());
                                            bdcXmRelTemp.setProid(bdcXmRel.getProid());
                                            map.put(ParamsConstants.BDCDYID_LOWERCASE,ybdcdyid);
                                            bdcFdcqList = bdcFdcqService.getBdcFdcq(map);
                                            if(CollectionUtils.isNotEmpty(bdcFdcqList)){
                                                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                                                bdcXmRelTemp.setYproid(bdcFdcq.getProid());
                                                bdcXmRelTemp.setYdjxmly(Constants.XMLY_BDC);
                                                listTemp.add(bdcXmRelTemp);
                                                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcFdcq.getProid());
                                                if(CollectionUtils.isNotEmpty(bdcZsList)){
                                                    BdcZs bdcZs = bdcZsList.get(0);
                                                    if(StringUtils.isBlank(bdcxm.getYbdcqzh())) {
                                                        bdcxm.setYbdcqzh(bdcZs.getBdcqzh());
                                                    } else {
                                                        bdcxm.setYbdcqzh(bdcxm.getYbdcqzh() + "," + bdcZs.getBdcqzh());
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
        //避免商品房（开发商自持）删除bdcxmrel
        if(CollectionUtils.isNotEmpty(listTemp)){
            list.remove(insertVoTemp);
            list.addAll(listTemp);
        }

        //zdd 权利人信息
        List<BdcQlr> tempBdcQlrList = null;
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }

        }

        //zdd 抵押权 地役权变更登记  继承原义务人
        if (StringUtils.equals(pplx,Constants.PPLX_GC)||StringUtils.isNotBlank(bdcxm.getQllx()) && (bdcxm.getQllx().equals("17") || bdcxm.getQllx().equals("18"))&&StringUtils.equals(project.getBdclx(),Constants.BDCLX_TDFW)) {
            List<BdcQlr> ybdcYwrList = getYbdcYwrList(project);
            if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                for (BdcQlr bdcYwr : ybdcYwrList) {
                    bdcYwr = bdcQlrService.qlrTurnProjectYwr(bdcYwr, tempBdcYwrList, project.getProid());
                    list.add(bdcYwr);
                }

            }
        }
        return list;
    }



    private void initBdcXmRelList(Project project) {
        //lj 需要把当前地籍号匹配的证，原地籍号匹配的证通通注销
        //先找当前不动产单元匹配的证，如果是合并，则有可能是当前不动产单元匹配两本老证，如果是分割，则查不到数据
        initBdcXmRelListFromGd(project,project.getBdcdyh());

        //查找原地籍号，查找原地籍号对应的不动产单元关联的证
        String bh = StringUtils.left(project.getBdcdyh(),19);
        List<String> bdcdyhList = djsjZdbgjlbService.getBdcdyhListByBh(bh);
        if (CollectionUtils.isNotEmpty(bdcdyhList)) {
            for (String bdcdyh : bdcdyhList) {
                initBdcXmRelListFromGd(project, bdcdyh);
                initBdcXmRelListFromBdc(project, bdcdyh);
            }
        }
    }

    private void initBdcXmRelListFromGd(Project project,String bdcdyh) {
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        //涉及的老证
        StringBuilder yzh = new StringBuilder();
        HashMap map = new HashMap();
        map.put("bdcdyh",bdcdyh);
        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRel(bdcdyh,"");
        List<GdFwsyq> gdFwsyqList = new ArrayList<GdFwsyq>();
        List<GdTdsyq> gdTdsyqList = new ArrayList<GdTdsyq>();
        List<String> fwsyqQlid = new ArrayList<String>();
        List<String> tdsyqQlid = new ArrayList<String>();
        BdcGdDyhRel thisbdcGdDyhRel = null;
        if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
            for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                thisbdcGdDyhRel = bdcGdDyhRelList.get(0);

                List<GdFwsyq> tempgdFwsyqList = gdFwService.queryGdFwsyqByFwidAndQszt(bdcGdDyhRel.getGdid(),"0");
                //只对不重复的syq生成xmrel
                if(CollectionUtils.isNotEmpty(tempgdFwsyqList))  {
                    for (GdFwsyq gdFwsyq : tempgdFwsyqList) {
                        if (!fwsyqQlid.contains(gdFwsyq.getQlid())) {
                            gdFwsyqList.add(gdFwsyq);
                            fwsyqQlid.add(gdFwsyq.getQlid());
                        }
                    }
                }

                //由于gdid无法确定是房产证还是土地证，所以都查一遍
                List<GdTdsyq> tempgdTdsyqList = gdTdService.queryGdTdsyqByTdidAndQszt(bdcGdDyhRel.getGdid(),"0");
                //只对不重复的syq生成xmrel
                if(CollectionUtils.isNotEmpty(tempgdTdsyqList)) {
                    for (GdTdsyq gdTdsyq : tempgdTdsyqList) {
                        if (!tdsyqQlid.contains(gdTdsyq.getQlid())) {
                            gdTdsyqList.add(gdTdsyq);
                            tdsyqQlid.add(gdTdsyq.getQlid());
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
            for (GdFwsyq gdFwsyq : gdFwsyqList) {
                BdcXmRel bdcXmRel = new BdcXmRel();
                bdcXmRel.setYproid(gdFwsyq.getProid());
                bdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                bdcXmRel.setYqlid(gdFwsyq.getQlid());
                if(thisbdcGdDyhRel!=null&&StringUtils.isNotBlank(thisbdcGdDyhRel.getDjid())) {
                    bdcXmRel.setQjid(thisbdcGdDyhRel.getDjid());
                }
                bdcXmRelList.add(bdcXmRel);
                if(StringUtils.isNotBlank(gdFwsyq.getFczh())) {
                    if(StringUtils.isBlank(yzh)) {
                        yzh.append(gdFwsyq.getFczh());
                    } else{
                        yzh.append("、").append(gdFwsyq.getFczh());
                    }
                }
                project.setYqlid(gdFwsyq.getQlid());
                project.setXmly(Constants.XMLY_FWSP);
            }
        }
        if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
            for (GdTdsyq gdTdsyq : gdTdsyqList) {
                BdcXmRel bdcXmRel = new BdcXmRel();
                bdcXmRel.setYproid(gdTdsyq.getProid());
                bdcXmRel.setYdjxmly(Constants.XMLY_TDSP);
                bdcXmRel.setYqlid(gdTdsyq.getQlid());
                if(thisbdcGdDyhRel!=null&&StringUtils.isNotBlank(thisbdcGdDyhRel.getDjid())){
                    bdcXmRel.setQjid(thisbdcGdDyhRel.getDjid());
                }
                bdcXmRelList.add(bdcXmRel);
                if(StringUtils.isNotBlank(gdTdsyq.getTdzh())) {
                    if(StringUtils.isBlank(yzh)) {
                        yzh.append(gdTdsyq.getTdzh());
                    } else{
                        yzh.append("、").append(gdTdsyq.getTdzh());
                    }
                }
            }
        }
        List<BdcXmRel> newList = new ArrayList<BdcXmRel>();
        newList.addAll(project.getBdcXmRelList());
        newList.addAll(bdcXmRelList);
        project.setBdcXmRelList(newList);
        String newzh = "";
        if(StringUtils.isNotBlank(project.getYbdcqzh())){
            if(StringUtils.isNotBlank(yzh) && !StringUtils.contains(project.getYbdcqzh(),yzh)){
                newzh = project.getYbdcqzh()+ " "+yzh;
            }
        }else{
            newzh = yzh.toString();
        }
        if(StringUtils.isNotBlank(newzh)){
            project.setYbdcqzh(newzh);
        }
    }

    private void initBdcXmRelListFromBdc(Project project,String bdcdyh){
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        StringBuilder yzh = new StringBuilder();
        List<BdcXm> bdcXmList = bdcXmService.queryBdcxmByBdcdyh(bdcdyh);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                QllxVo qllxVolReal=qllxService.queryQllxVo(bdcXm);
                if (!(qllxVolReal instanceof BdcDyaq)&&qllxVolReal!=null&&qllxVolReal.getQszt()!=null&&qllxVolReal.getQszt()==1) {
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setYproid(bdcXm.getProid());
                    if (qllxVolReal != null)
                        bdcXmRel.setYqlid(qllxVolReal.getQlid());
                    bdcXmRel.setYdjxmly("1");
                    bdcXmRelList.add(bdcXmRel);
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        for (BdcZs bdcZs : bdcZsList) {
                            if(StringUtils.isNotBlank(bdcZs.getBdcqzh())) {
                                yzh.append(",").append(bdcZs.getBdcqzh());
                            }else{
                                yzh.append(bdcZs.getBdcqzh());
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            List<BdcXmRel> newList = new ArrayList<BdcXmRel>();
            newList.addAll(project.getBdcXmRelList());
            newList.addAll(bdcXmRelList);
            project.setBdcXmRelList(newList);
            String newzh = "";
            if(StringUtils.isNotBlank(project.getYbdcqzh())){
                if(StringUtils.isNotBlank(yzh) && !StringUtils.contains(project.getYbdcqzh(),yzh)){
                    newzh = project.getYbdcqzh()+ ","+yzh;
                }
            }else{
                newzh = yzh.toString();
            }
            if(StringUtils.isNotBlank(newzh)){
                project.setYbdcqzh(newzh);
            }
        }
    }
}
