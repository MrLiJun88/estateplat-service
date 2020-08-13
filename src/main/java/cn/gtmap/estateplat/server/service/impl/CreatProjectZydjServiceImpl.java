package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * .
 * <p/>
 * 转移登记创建项目实现类
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-16
 */

public class CreatProjectZydjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    QllxService qllxService;
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
    private BdcYgService bdcYgService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Autowired
    private BdcGdDyhRelService gdDyhRelService;
    @Autowired
    private GdYgService gdYgService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private DjsjZdbgjlbService djsjZdbgjlbService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdFwService gdFwService;

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        List<BdcQlr> ybdcQlrList;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }else {
            throw new AppException(4005);
        }
        List<InsertVo> list = new ArrayList<InsertVo>();
        String pplx = AppConfig.getProperty("sjpp.type");
        //分割合并转移流程是否根据权籍不动产单元历史关系表自动关联分割或合并前证书
        String autoRelateZs = AppConfig.getProperty("fghb.autoRelateZs");
        if(StringUtils.equals(autoRelateZs,"true") && StringUtils.isNotBlank(project.getSqlx()) && (StringUtils.equals(project.getSqlx(),Constants.SQLX_FWFGHBZY_DM)||StringUtils.equals(project.getSqlx(),Constants.SQLX_TDFGHBZY_DM))){
            //lj 在此处关联老证
            initBdcXmRelList(project);
        }
        //zdd 项目信息
        BdcXm bdcxm = bdcXmService.newBdcxm(project);
        list.add(bdcxm);
        String createSqlxdm = "";
        //获取平台的申请类型代码,主要为了合并
        if (StringUtils.isNotBlank(project.getWiid())) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
            if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                createSqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
            }
        }
        //zdd 需要完善   一个项目有可能有多个项目关系表
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
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(),initVoFromParm.getDjsjQszdDcbList(),initVoFromParm.getDjsjNydDcbList(),initVoFromParm.getProject(), bdcxm.getQllx());
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
        if (bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyh())) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            list.add(bdcdy);
        }
        QllxVo qllxVo = qllxService.makeSureQllx(bdcxm);
        String zsFont = qllxService.makeSureBdcqzlx(qllxVo);

        //zhouwanqing 过程匹配，则权利人和义务人都不需要改变
        if (StringUtils.equals(pplx, Constants.PPLX_GC) && !StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
            List<BdcQlr> bdcQlrList = keepQlrByGcPp(project);
            if (CollectionUtils.isNotEmpty(bdcQlrList))
                list.addAll(bdcQlrList);
        } else {
            //zwq 商品房买卖转移登记权利人取预告的权利人
            //sunhao 显示需要继承权利人预告的所有转移流程中的申请书上的权利人等信息
            List<BdcQlr> ygbdcQlrList = new ArrayList<BdcQlr>();
            List<BdcQlr> ygbdcYwrList = new ArrayList<BdcQlr>();
            List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(project.getBdcdyh(), "1");
            List<GdYg> gdYgList = gdYgService.getGdygListByBdcdyh(project.getBdcdyh());
            if (CommonUtil.indexOfStrs(Constants.SQLX_ZYDJ_DM, project.getSqlx()) || StringUtils.equals(createSqlxdm,Constants.SQLX_CLF)) {
                StringBuilder ybdcqzh = new StringBuilder();
                if (CollectionUtils.isNotEmpty(bdcYgList)) {
                    String proid = "";
                    for (BdcYg bdcYg : bdcYgList) {
                        if (StringUtils.isNotBlank(bdcYg.getYgdjzl()) && StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPF)) {
                            //增加ybdcqzh
                            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcYg.getProid());
                            if(CollectionUtils.isNotEmpty(bdcZsList)){
                                for(BdcZs bdcZs:bdcZsList){
                                    if(StringUtils.isNotBlank(bdcZs.getBdcdyh())){
                                        if (StringUtils.isBlank(ybdcqzh)) {
                                            ybdcqzh.append(bdcZs.getBdcqzh());
                                        } else {
                                            ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                        }
                                    }
                                }
                            }

                            //增加bdc_xm_rel
                            BdcXmRel bdcXmRel = new BdcXmRel();
                            bdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRel.setProid(project.getProid());
                            bdcXmRel.setYqlid(bdcYg.getQlid());
                            bdcXmRel.setYproid(bdcYg.getProid());
                            bdcXmRel.setQjid(project.getDjId());
                            bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                            list.add(bdcXmRel);

                            proid = bdcYg.getProid();
                            if (StringUtils.isNotBlank(proid)) {
                                ygbdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                                ygbdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
                            }
                            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
                            List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                            if(CollectionUtils.isNotEmpty(ygbdcQlrList)){
                                for (BdcQlr bdcQlr : ygbdcQlrList) {
                                    bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, bdcQlrList, project.getProid());
                                    list.add(bdcQlr);
                                }
                                for (BdcQlr bdcQlr : ygbdcYwrList) {
                                    bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, bdcYwrList, project.getProid());
                                    list.add(bdcQlr);
                                }
                            }
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(gdYgList)){
                    for(GdYg gdYg : gdYgList){
                        if(StringUtils.isNotBlank(gdYg.getYgdjzl()) && StringUtils.equals(gdYg.getYgdjzl(),Constants.YGDJZL_YGSPF_MC)){
                            if (StringUtils.isNotBlank(gdYg.getYgdjzmh())) {
                                if (StringUtils.isBlank(ybdcqzh)) {
                                    ybdcqzh.append(gdYg.getYgdjzmh());
                                } else {
                                    ybdcqzh.append(",").append(gdYg.getYgdjzmh());
                                }
                            }

                            //增加bdc_xm_rel
                            BdcXmRel bdcXmRel = new BdcXmRel();
                            bdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRel.setProid(project.getProid());
                            bdcXmRel.setYqlid(gdYg.getYgid());
                            bdcXmRel.setYproid(gdYg.getProid());
                            bdcXmRel.setQjid(project.getDjId());
                            bdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                            list.add(bdcXmRel);

                            //继承预告权利人
                            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(gdYg.getYgid(), Constants.QLRLX_QLR);
                            if(CollectionUtils.isNotEmpty(gdQlrList)){
                                List<BdcQlr> bdcQlrList = gdQlrService.readGdQlrs(gdQlrList);
                                if(CollectionUtils.isNotEmpty(bdcQlrList)){
                                    for(BdcQlr bdcQlr : bdcQlrList){
                                        bdcQlr.setProid(project.getProid());
                                        list.add(bdcQlr);
                                    }
                                }
                            }
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(gdYgList) || CollectionUtils.isNotEmpty(bdcYgList)){
                    for (InsertVo insertVo : list) {
                        if (insertVo instanceof BdcXm) {
                            BdcXm bdcXm = (BdcXm) insertVo;
                            if(StringUtils.isBlank(bdcXm.getYbdcqzh())){
                                bdcXm.setYbdcqzh(ybdcqzh.toString());
                            }else{
                                bdcXm.setYbdcqzh(bdcXm.getYbdcqzh() + "," + ybdcqzh);
                            }
                        }
                    }
                }
            }

            //zdd 如果为证书  则将原权利人转为义务人  如果证明 则将义务人转为义务人
            if (StringUtils.isNotBlank(zsFont) && zsFont.equals(Constants.BDCQZS_BH_FONT)) {
                ybdcQlrList = getYbdcQlrList(project);
            } else {
                ybdcQlrList = getYbdcYwrList(project);
            }
            //zdd 权利人信息 
            if(CollectionUtils.isEmpty(ygbdcQlrList) && CollectionUtils.isEmpty(ygbdcYwrList)&&CollectionUtils.isNotEmpty(ybdcQlrList)){
                List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                    for (BdcQlr bdcYwr : tempBdcYwrList) {
                        bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                    }
                }
                for (BdcQlr bdcQlr : ybdcQlrList) {
                    bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                    list.add(bdcQlr);
                }
            }
        }

        List<BdcQlr> bdcQlrList = bdcQlrService.initBdcQlrFromOntQlr(project.getProid());
        if (!bdcQlrList.isEmpty()) {
            Iterator<InsertVo> iterator = list.iterator();
            while (iterator.hasNext()) {
                InsertVo insertVo = iterator.next();
                if (insertVo instanceof BdcQlr)
                    iterator.remove();
            }
            for (BdcQlr bdcQlr : bdcQlrList) {
                list.add(bdcQlr);
            }
        }
        return list;
    }




    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param project
     * @param list
     * @description 处理预告信息
     */
    public void handleYgxx(Project project, List<InsertVo> list) {
        if (StringUtils.isNotBlank(project.getBdcdyh())) {
            List<GdYg> gdYgList = new ArrayList<GdYg>();
            List<BdcGdDyhRel> gdDyhRelList = gdDyhRelService.getGdDyhRelByDyh(project.getBdcdyh());
            if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                for (BdcGdDyhRel gdDyhRel : gdDyhRelList) {
                    if (StringUtils.isNotBlank(gdDyhRel.getGdid())) {
                        List<GdYg> gdYgListTmep = gdYgService.queryGdYgByBdcId(gdDyhRel.getGdid());
                        if (CollectionUtils.isNotEmpty(gdYgListTmep)) {
                            for (GdYg gdYg : gdYgListTmep) {
                                if (gdYg.getIszx() != 1) {
                                    gdYgList.add(gdYg);
                                }
                            }
                        }
                    }
                }
            }

            List<BdcYg> bdcYgList = bdcYgService.getBdcYgList(project.getBdcdyh(), Constants.QLLX_QSZT_XS.toString());

            if (CollectionUtils.isNotEmpty(gdYgList) || CollectionUtils.isNotEmpty(bdcYgList)) {
                addYgxx(list, project, gdYgList, bdcYgList);
            }
        }

    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param list
     * @param project
     * @param gdYgList
     * @param bdcYgList
     * @description 增加预告信息
     */
    public void addYgxx(List<InsertVo> list, Project project, List<GdYg> gdYgList, List<BdcYg> bdcYgList) {
        StringBuilder ybdcqzh = new StringBuilder();
        if (CollectionUtils.isNotEmpty(gdYgList)) {
            for (GdYg gdYg : gdYgList) {
                //增加ybdcqzh
                if(gdYg != null && StringUtils.equals(StringUtils.deleteWhitespace(gdYg.getYgdjzl()), Constants.YGDJZL_YGSPF_MC)) {
                    if (StringUtils.isNotBlank(gdYg.getYgdjzmh())) {
                        if (StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh.append(gdYg.getYgdjzmh());
                        } else {
                            ybdcqzh.append(",").append(gdYg.getYgdjzmh());
                        }
                    }

                    //增加bdc_xm_rel
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setYqlid(gdYg.getYgid());
                    bdcXmRel.setYproid(gdYg.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYdjxmly(Constants.XMLY_FWSP);
                    list.add(bdcXmRel);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(bdcYgList)) {
            for(BdcYg bdcYg:bdcYgList){
                if(bdcYg != null && StringUtils.indexOf(Constants.YGDJZL_MM,bdcYg.getYgdjzl())> -1){
                    //增加ybdcqzh
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcYg.getProid());
                    if(CollectionUtils.isNotEmpty(bdcZsList)){
                        for(BdcZs bdcZs:bdcZsList){
                            if(StringUtils.isNotBlank(bdcZs.getBdcdyh())){
                                if (StringUtils.isBlank(ybdcqzh)) {
                                    ybdcqzh.append(bdcZs.getBdcqzh());
                                } else {
                                    ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                                }
                            }
                        }
                    }

                    //增加bdc_xm_rel
                    BdcXmRel bdcXmRel = new BdcXmRel();
                    bdcXmRel.setRelid(UUIDGenerator.generate18());
                    bdcXmRel.setProid(project.getProid());
                    bdcXmRel.setYqlid(bdcYg.getQlid());
                    bdcXmRel.setYproid(bdcYg.getProid());
                    bdcXmRel.setQjid(project.getDjId());
                    bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                    list.add(bdcXmRel);
                }
            }
        }

        for (InsertVo insertVo : list) {
            if (insertVo instanceof BdcXm) {
                BdcXm bdcXm = (BdcXm) insertVo;
                if(StringUtils.isBlank(bdcXm.getYbdcqzh())){
                    bdcXm.setYbdcqzh(ybdcqzh.toString());
                }else{
                    bdcXm.setYbdcqzh(bdcXm.getYbdcqzh() + "," + ybdcqzh);
                }
            }
        }
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
        /**
         *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
         *@description 土地分割转移，查找原地籍号对应的纯土地的不动产单元号关联的土地证
         */
        if(StringUtils.isNotBlank(project.getSqlx())&&StringUtils.equals(project.getSqlx(),Constants.SQLX_TDFGHBZY_DM)){
            String tdBh = StringUtils.left(project.getBdcdyh(),19);
            List<String> tdBdcdyhList = djsjZdbgjlbService.getTdBdcdyhListByBh(tdBh);
            if(CollectionUtils.isNotEmpty(tdBdcdyhList)){
                for(String tdBdcdyh : tdBdcdyhList){
                    initBdcXmRelListFromGd(project,tdBdcdyh);
                    initBdcXmRelListFromBdc(project,tdBdcdyh);
                }
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
                if(CollectionUtils.isNotEmpty(tempgdFwsyqList)) {
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
                if(thisbdcGdDyhRel!=null&&StringUtils.isNotBlank(thisbdcGdDyhRel.getDjid())){
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
                if(thisbdcGdDyhRel!= null&&StringUtils.isNotBlank(thisbdcGdDyhRel.getDjid())){
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
                    if (qllxVolReal != null) {
                        bdcXmRel.setYqlid(qllxVolReal.getQlid());
                    }
                    bdcXmRel.setYdjxmly("1");
                    bdcXmRelList.add(bdcXmRel);
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        for (BdcZs bdcZs : bdcZsList) {
                            if(StringUtils.isNotBlank(yzh)) {
                                yzh.append(",").append(bdcZs.getBdcqzh());
                            }else{
                                yzh.append(bdcZs.getBdcqzh());
                            }
                        }
                    }
                    project.setYxmid(bdcXm.getProid());
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
