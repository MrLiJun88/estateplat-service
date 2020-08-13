package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcQllxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * zdd 房屋所有权抵押权预转现创建服务  适用于单个预告登记的预转现  也适用于多个预告登记的预转现
 * Created by zdd on 2016/2/2.
 */
public class CreatComplexYzxdjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    ProjectService projectService;
    @Autowired
    BdcQllxMapper bdcQllxMapper;
    @Autowired
    BdcSqlxDjyyRelService bdcSqlxDjyyRelService;

    /**
     * zdd 此处后续优化 可以通过参数确定调用哪一个服务
     */
    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectService creatProjectDydjServiceImpl;
    //zdd 更正登记可以继承权利人义务人
    @Resource(name = "creatProjectGzdjServiceImpl")
    CreatProjectService creatProjectGzdjServiceImpl;
    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;
    @Resource(name = "creatProjectZydjService")
    CreatProjectZydjServiceImpl creatProjectZydjService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    QllxService qllxService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    GdFwService gdFwService;



    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project = null;
        if (xmxx instanceof Project)
            project = (Project) xmxx;
        else
            throw new AppException(4005);
        //lst根据工作流wiid  查找是否已经存在  如果存在 则删除对应的记录
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (StringUtils.isNotBlank(project.getWiid())) {
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }
        String sqlx1 = null;
        String sqlx2 = null;
        if (StringUtils.isNotBlank(project.getSqlx())) {
            String sqlxdm = "";
            //获取平台的申请类型代码,主要为了合并
            if (StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(sqlxdm);
            if (hbSqlxMap != null) {
                sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
                sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
            }
        }
        if (StringUtils.isNotBlank(project.getProid()) && project.getBdcXmRelList() != null &&CollectionUtils.isNotEmpty(project.getBdcXmRelList())&& project.getXmly().equals(Constants.XMLY_BDC)) {
            List<BdcXmRel> bdcXmRelList = project.getBdcXmRelList();
            String workflowProid = "";
            if (StringUtils.isNotBlank(project.getWorkflowProid())) {
                workflowProid = project.getWorkflowProid();
            } else if (StringUtils.isNotBlank(project.getProid())) {
                workflowProid = project.getProid();
            }

            creatProjectNode(workflowProid);
            String proid = project.getProid();

            //zdd 将不动产单元编号前19位作为键   登记薄ID作为值
            HashMap<String, String> djbidMap = new HashMap<String, String>();
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                //zdd 将后台传过来的项目关系信息  初始化到project对象中   便于后面创建单个项目
                if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                    project.setDjId(bdcXmRel.getQjid());
                }
                if (StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                    project.setYxmid(bdcXmRel.getYproid());
                }
                if (StringUtils.isNotBlank(bdcXmRel.getYdjxmly())) {
                    project.setXmly(bdcXmRel.getYdjxmly());
                }
                if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                    project.setYqlid(bdcXmRel.getYqlid());
                }

                //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                project.setProid(proid);

                //zdd 不动产单元号获取
                if (StringUtils.isNotBlank(project.getDjId())) {
                    HashMap map = new HashMap();
                    if (StringUtils.isNotBlank(project.getBdclx()))
                        map.put("bdclx", project.getBdclx());
                    map.put("id", project.getDjId());
                    List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                    if (CollectionUtils.isNotEmpty(bdcdyList)&&bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                        project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                    }
                } else if (StringUtils.isNotBlank(project.getYxmid())&&
                        StringUtils.isNotBlank(project.getXmly()) &&project.getXmly().equals(Constants.XMLY_BDC)) {
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                    project.setBdcdyh(bdcBdcdy.getBdcdyh());
                    djbidMap.put(bdcBdcdy.getBdcdyh().substring(0, 19), bdcBdcdy.getDjbid());
                }
                //zdd 批量查封  登记薄可能不一致
                if (StringUtils.isNotBlank(project.getBdcdyh())) {
                    project.setZdzhh(project.getBdcdyh().substring(0, 19));
                    if (djbidMap.containsKey(project.getBdcdyh().substring(0, 19))) {
                        project.setDjbid(djbidMap.get(project.getBdcdyh().substring(0, 19)));
                    } else {
                        String djbid = UUIDGenerator.generate18();
                        djbidMap.put(project.getBdcdyh().substring(0, 19), djbid);
                        project.setDjbid(djbid);
                    }
                }
                List<InsertVo> list = null;
                //zdd 判断是抵押预转现还是房屋所有权预转现
                String ygdjlx = "";
                Double dyje = null;
                if (StringUtils.isNotBlank(project.getBdcdyh())) {
                    List<QllxVo> bdcYgList = qllxService.getQllxByBdcdyh(new BdcYg(), project.getBdcdyh());
                    if (CollectionUtils.isNotEmpty(bdcYgList)) {
                        for (Object bdcYg : bdcYgList) {
                            if (bdcYg != null) {
                                BdcYg yg = (BdcYg) bdcYg;
                                ygdjlx = yg.getYgdjzl();
                                dyje = yg.getQdjg();
                                //根据预告类型赋值权利，如果预告类型为空，根据取得价格判断
                                if (StringUtils.isNotBlank(ygdjlx) && (ygdjlx.equals(Constants.YGDJZL_YGSPFDY) || ygdjlx.equals(Constants.YGDJZL_QTYGSPFDY))) {
                                    project.setQllx(Constants.QLLX_DYAQ);
                                } else if (StringUtils.isBlank(ygdjlx) && dyje != null) {
                                    project.setQllx(Constants.QLLX_DYAQ);
                                } else {
                                    project.setQllx(Constants.QLLX_GYTD_FWSUQ);
                                }
                                List<BdcQlr> ybdcQlrList = null;

                                project = (Project) xmxx;
                                ybdcQlrList = getYbdcQlrList(project);
                                project.setYxmid(yg.getProid());
                                List<InsertVo> bdclist = creatProjectGzdjServiceImpl.initVoFromOldData(project);
                                //预转现的权利人读取预告的权利人
                                List<BdcQlr> ygQlrLst = bdcQlrService.queryBdcQlrByProid(((BdcYg) bdcYg).getProid());
                                List<BdcQlr> ygYwrLst = bdcQlrService.queryBdcYwrByProid(((BdcYg) bdcYg).getProid());
                                List<InsertVo> removeQlr = new ArrayList<InsertVo>();
                                for(InsertVo vo : bdclist){
                                    if(vo instanceof BdcQlr){
                                        removeQlr.add(vo);
                                    }
                                }
                                if(CollectionUtils.isNotEmpty(removeQlr)){
                                    bdclist.removeAll(removeQlr);
                                }

                                if(CollectionUtils.isNotEmpty(ygQlrLst)){
                                    for(BdcQlr bdcQlr : ygQlrLst){
                                        bdcQlr.setProid(project.getProid());
                                        bdcQlr.setQlrid(UUIDGenerator.generate18());
                                        bdclist.add(bdcQlr);
                                    }
                                }

                                if (CollectionUtils.isNotEmpty(bdclist)) {
                                    list = new ArrayList<InsertVo>();
                                    String qllx = "";
                                    //创建收件信息
                                    for (InsertVo vo : bdclist) {
                                        if (vo instanceof BdcXm) {
                                            qllx = ((BdcXm) vo).getQllx();
                                            if (qllx.equals(Constants.QLLX_GYTD_FWSUQ)) {
                                                if (StringUtils.isNotBlank(sqlx1)) {
                                                    ((BdcXm) vo).setSqlx(sqlx1);
                                                }else {
                                                    ((BdcXm) vo).setSqlx(Constants.SQLX_SPFMMZYDJ_DM);
                                                }
                                                ((BdcXm) vo).setDjlx(Constants.DJLX_ZYDJ_DM);
                                            } else {
                                                if (StringUtils.isNotBlank(sqlx2)) {
                                                    ((BdcXm) vo).setSqlx(sqlx2);
                                                }else {
                                                    ((BdcXm) vo).setSqlx(Constants.SQLX_FWDY_DM);
                                                }
                                                ((BdcXm) vo).setDjsy(Constants.DJSY_DYAQ);
                                                ((BdcXm) vo).setDjlx(Constants.DJLX_DYDJ_DM);
                                            }
                                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                                            if (bdcSjxx != null) {
                                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                                list.add(bdcSjxx);
                                            }
                                        }
                                    }
                                    //当权利类型为房屋所有权排除义务人，将开发商放到 义务人中
                                    if (StringUtils.equals(Constants.QLLX_GYTD_FWSUQ, qllx)) {
                                        for (InsertVo vo : bdclist) {
                                            if (vo instanceof BdcQlr) {
                                                if (!StringUtils.equals(((BdcQlr) vo).getQlrlx(), Constants.QLRLX_YWR)) {
                                                    list.add(vo);
                                                }
                                            } else {
                                                list.add(vo);
                                            }
                                        }
                                        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                                            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
                                            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                                                for (BdcQlr bdcYwr : tempBdcYwrList) {
                                                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                                                }
                                            }
                                            for (BdcQlr bdcYwr : ybdcQlrList) {
                                                bdcYwr.setQlrlx(Constants.QLRLX_YWR);
                                                bdcYwr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcYwr, tempBdcYwrList, project.getProid());
                                                list.add(bdcYwr);
                                            }
                                        }

                                    } else {
                                        //抵押需要将预告抵押的抵押人带入
                                        if(CollectionUtils.isNotEmpty(ygYwrLst)){
                                            for(BdcQlr bdcQlr : ygYwrLst){
                                                bdcQlr.setProid(project.getProid());
                                                bdcQlr.setQlrid(UUIDGenerator.generate18());
                                                bdclist.add(bdcQlr);
                                            }
                                        }
                                        list.addAll(bdclist);
                                    }
                                }

                                insertVoList.addAll(list);
                                project.setProid(UUIDGenerator.generate18());
                            }
                        }
                    } else {

                        List<Map> gdygLst = qllxService.getGdYgByBdcdyh(project.getBdcdyh());
                        BdcXm bdcxm = null;
                        BdcBdcdjb bdcdjb = null;
                        if(CollectionUtils.isNotEmpty(gdygLst)) {
                            for (Map gdYg : gdygLst) {
                                if (gdYg != null) {
                                    list=new ArrayList<InsertVo>();
                                    ygdjlx = (String) gdYg.get("YGDJZL");
                                    if(null != gdYg.get("QDJG")){
                                        BigDecimal bd = (BigDecimal)gdYg.get("QDJG");
                                        dyje = bd.doubleValue();
                                    }

                                    //根据预告类型赋值权利，如果预告类型为空，根据取得价格判断
                                    if (StringUtils.isNotBlank(ygdjlx) && (ygdjlx.equals(Constants.YGDJZL_YGSPFDY) || ygdjlx.equals(Constants.YGDJZL_QTYGSPFDY))) {
                                        project.setQllx(Constants.QLLX_DYAQ);
                                    } else if (StringUtils.isBlank(ygdjlx) && dyje != null) {
                                        project.setQllx(Constants.QLLX_DYAQ);
                                    } else {
                                        project.setQllx(Constants.QLLX_GYTD_FWSUQ);
                                    }
                                    List<BdcQlr> ybdcQlrList = null;
                                    List<BdcQlr> ybdcYwrList = null;
                                    if (xmxx instanceof Project) {
                                        project = (Project) xmxx;
                                        List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid((String) gdYg.get("YGID"), Constants.QLRLX_QLR);
                                        ybdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
                                        List<GdQlr> gdYwrs = gdXmService.getGdqlrByQlid((String) gdYg.get("YGID"), Constants.QLRLX_YWR);
                                        ybdcYwrList = gdQlrService.readGdQlrs(gdYwrs);
                                    }
                                    bdcxm = bdcXmService.newBdcxm(project);

                                    if (StringUtils.isNotBlank(ygdjlx) && (ygdjlx.equals(Constants.YGDJZL_YGSPFDY) || ygdjlx.equals(Constants.YGDJZL_QTYGSPFDY))) {
                                        bdcxm.setDjlx(Constants.DJLX_DYDJ_DM);
                                        bdcxm.setQllx(Constants.QLLX_DYAQ);
                                        bdcxm.setSqlx(Constants.SQLX_FWDY_DM);
                                        bdcxm.setDydjlx(Constants.DJLX_CSDJ_DM);
                                        bdcxm.setDjsy(Constants.DJSY_DYAQ);
                                    } else {
                                        bdcxm.setDjlx(Constants.DJLX_ZYDJ_DM);
                                        bdcxm.setQllx(Constants.QLLX_GYTD_FWSUQ);
                                        bdcxm.setSqlx(Constants.SQLX_SPFMMZYDJ_DM);
                                        bdcxm.setDjsy(CommonUtil.formatEmptyValue(bdcQllxMapper.queryDjsyByQllx(bdcxm.getQllx())).replace("[", "").replace("]", ""));
                                        bdcxm.setDjyy(bdcSqlxDjyyRelService.getDjyyBySqlx(bdcxm.getSqlx()));
                                    }
                                    list.add(bdcxm);
                                    bdcXmRel = bdcXmRelService.creatBdcXmRelFromProject(project);
                                    list.add(bdcXmRel);
                                    InitVoFromParm initVoFromParm =super.getDjxx(project);
                                    BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
                                    if (bdcdy != null) {
                                        bdcxm.setBdcdyid(bdcdy.getBdcdyid());
                                        list.add(bdcdy);
                                    }
                                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
                                    bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
                                    if (bdcSpxx != null)
                                        list.add(bdcSpxx);

                                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcxm);
                                    if (bdcSjxx != null) {
                                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                        insertVoList.add(bdcSjxx);
                                    }

                                    /**
                                     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
                                     * @description 当预告在过渡时，权利人义务人都取预告证明的
                                     */
                                    if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
                                        for (BdcQlr bdcQlr : ybdcQlrList) {
                                            bdcQlrService.qlrTurnProjectQlr(bdcQlr, null, project.getProid());
                                            list.add(bdcQlr);
                                        }
                                    }
                                    if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
                                        for (BdcQlr bdcYwr : ybdcYwrList) {
                                            bdcQlrService.qlrTurnProjectYwr(bdcYwr, null, project.getProid());
                                            list.add(bdcYwr);
                                        }
                                    }

                                    insertVoList.addAll(list);
                                    project.setProid(UUIDGenerator.generate18());
                                }
                            }
                        }

                    }
                }

            }
        } else { //从匹配选择过渡证
            String pplx = AppConfig.getProperty("sjpp.type");
            if (StringUtils.isNotBlank(sqlx1)) {
                project.setSqlx(sqlx1);
            }else {
                project.setSqlx(Constants.SQLX_SPFMMZYDJ_DM);
            }
            List<InsertVo> dyList = null;
            if (StringUtils.isNotBlank(pplx) && pplx.equals(Constants.PPLX_GC)) {
                //过程
                String syqQlid = "";
                String dyQlid = "";
                if (StringUtils.isNotBlank(project.getGdproid())) {
                    List<GdFwsyq> fwsyqList = gdFwService.getGdFwsyqListByGdproid(project.getGdproid(), 0);
                    if (CollectionUtils.isNotEmpty(fwsyqList))
                        syqQlid = fwsyqList.get(0).getQlid();
                    List<GdDy> gddyList = gdFwService.getGdDyListByGdproid(project.getGdproid(), 0);
                    if (CollectionUtils.isNotEmpty(gddyList))
                        dyQlid = gddyList.get(0).getDyid();
                }

                project.setYqlid(syqQlid);
                project.setDjlx(Constants.DJLX_ZYDJ_DM);
                List<InsertVo> fwsyqVoList = initSyqFromGd(project, sqlx1);
                //zx只能先保存
                super.insertProjectData(fwsyqVoList);

                String bdcdyid = "";
                if (CollectionUtils.isNotEmpty(fwsyqVoList)) {
                    for (InsertVo insertVo : fwsyqVoList) {
                        if (insertVo instanceof BdcBdcdy) {
                            bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                        }
                    }
                }
                project.setProid(UUIDGenerator.generate());
                project.setYqlid(dyQlid);
                project.setBdcdyid(bdcdyid);
                dyList = initDyFromGd(project, sqlx2, null);

            } else {
                String ygdyGdproid = "";
                String fwsyqQlid = "";
                String ygQlid = "";
                String ygProid = "";
                String ygDyQlid = "";
                if (StringUtils.isNotBlank(project.getGdproid())) {
                    List<GdFwsyq> gdFwsyqList = gdFwService.getGdFwsyqListByGdproid(project.getGdproid(), 0);
                    if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                        fwsyqQlid = gdFwsyqList.get(0).getQlid();
                    }

                    if (StringUtils.isNotBlank(fwsyqQlid)) {
                        List<GdFw> gdFwList = null;
                        if (StringUtils.isNotBlank(fwsyqQlid)) {
                            gdFwList = gdFwService.getGdFwByQlid(fwsyqQlid);
                        }

                        if (CollectionUtils.isNotEmpty(gdFwList)) {
                            String fwid = gdFwList.get(0).getFwid();
                            List<GdBdcQlRel> gdBdcQlRelList = gdFwService.getGdBdcQlRelByBdcidOrQlid(fwid, null);
                            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                    GdYg gdYg = gdFwService.getGdYgByYgid(gdBdcQlRel.getQlid(), 0);
                                    if (gdYg != null) {
                                        if (Constants.YGDJZL_MM.contains(gdYg.getYgdjzl())){
                                            ygQlid = gdYg.getYgid();
                                            ygProid = gdYg.getProid();
                                        }else if (Constants.YGDJZL_DY.contains(gdYg.getYgdjzl())) {
                                            ygDyQlid = gdYg.getYgid();
                                            ygdyGdproid = gdYg.getProid();
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
                project.setGdproid(ygProid);
                project.setDjlx(Constants.DJLX_ZYDJ_DM);
                List<InsertVo> fwsyqVoList = initSyqFromGdCg(project, sqlx1, ygQlid);
                //zx只能先保存
                super.insertProjectData(fwsyqVoList);

                String bdcdyid = "";
                if (CollectionUtils.isNotEmpty(fwsyqVoList)) {
                    for (InsertVo insertVo : fwsyqVoList) {
                        if (insertVo instanceof BdcBdcdy)
                            bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                    }
                }
                project.setProid(UUIDGenerator.generate());
                project.setGdproid(ygdyGdproid);
                project.setBdcdyid(bdcdyid);
                dyList = initDyFromGd(project, sqlx2, ygDyQlid);
            }
            if (CollectionUtils.isNotEmpty(dyList)){
                insertVoList.addAll(dyList);
            }

        }
        return insertVoList;
    }

    @Override
    @Transactional
    public void saveOrUpdateProjectData(final List<InsertVo> list) {
        //存储分组后的List数据
        List<List<InsertVo>> newList = new ArrayList<List<InsertVo>>();
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        List<String> zdzhhList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        List<String> bdcTdList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            InsertVo vo = list.get(i);
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                logger.error("CreatComplexYzxdjServiceImpl.saveOrUpdateProjectData",e);
            }
            if (StringUtils.isNotBlank(id)&&entityMapper.selectByPrimaryKey(vo.getClass(), id) != null) {
                entityMapper.updateByPrimaryKeySelective(vo);
                continue;
            }
            //zdd 特殊处理BdcBdcdy  不动产单元号不能重复
            if (vo instanceof BdcBdcdy) {
                BdcBdcdy bdcdy = (BdcBdcdy) vo;
                if (bdcdyhList.contains(bdcdy.getBdcdyh())) {
                    continue;
                } else {
                    bdcdyhList.add(bdcdy.getBdcdyh());
                }

                //zdd 特殊处理BdcBdcdjb
            } else if (vo instanceof BdcBdcdjb) {
                BdcBdcdjb bdcdjb = (BdcBdcdjb) vo;
                if (zdzhhList.contains(bdcdjb.getZdzhh())) {
                    continue;
                } else {
                    zdzhhList.add(bdcdjb.getZdzhh());
                }
            } else if (vo instanceof BdcTd) {
                BdcTd bdcTd = (BdcTd) vo;
                if (bdcTdList.contains(bdcTd.getZdzhh())) {
                    continue;
                } else {
                    bdcTdList.add(bdcTd.getZdzhh());
                }
            }
            //如果没有值
            if (map.get(vo.getClass().getSimpleName()) == null) {
                voList = new ArrayList<InsertVo>();
                map.put(vo.getClass().getSimpleName(), voList);
                newList.add(voList);
            } else {
                voList = map.get(vo.getClass().getSimpleName());
            }
            voList.add(vo);
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            for (int j = 0; j < newList.size(); j++) {
                entityMapper.insertBatchSelective(newList.get(j));
            }
        }
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从过渡库创建转移登记
     */
    public List<InsertVo> initSyqFromGd(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            //房屋所有权证办理不动产权证走转移
            List<InsertVo> list = creatProjectZydjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                String qllxdm = project.getQllx();
                //修改申请类型为买卖转移登记
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    if (StringUtils.isNotBlank(sqlx1)) {
                        ((BdcXm) vo).setSqlx(sqlx1);
                    } else {
                        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                            if (qllxdm.equals(Constants.QLLX_GYTD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("225");
                            } else if (qllxdm.equals(Constants.QLLX_ZJD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("229");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDFWSUQ)) {
                                ((BdcXm) vo).setSqlx("213");
                            }
                        } else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                            if (qllxdm.equals(Constants.QLLX_JTTD_SUQ)) {
                                ((BdcXm) vo).setSqlx("214");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("217");
                            } else if (qllxdm.equals(Constants.QLLX_GYTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("215");
                            }
                        }
                    }
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从过渡库创建变更登记
     */
    public List<InsertVo> initSyqFromGdCg(Project project, String sqlx1,String ygQlid) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            //房屋所有权证办理不动产权证走转移
            List<InsertVo> list = creatProjectZydjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                String qllxdm = project.getQllx();
                //修改申请类型为买卖转移登记
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    if (StringUtils.isNotBlank(sqlx1))
                        ((BdcXm) vo).setSqlx(sqlx1);
                    else {
                        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                            if (qllxdm.equals(Constants.QLLX_GYTD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("225");
                            } else if (qllxdm.equals(Constants.QLLX_ZJD_FWSUQ)) {
                                ((BdcXm) vo).setSqlx("229");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDFWSUQ)) {
                                ((BdcXm) vo).setSqlx("213");
                            }
                        } else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                            if (qllxdm.equals(Constants.QLLX_JTTD_SUQ)) {
                                ((BdcXm) vo).setSqlx("214");
                            } else if (qllxdm.equals(Constants.QLLX_JTTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("217");
                            } else if (qllxdm.equals(Constants.QLLX_GYTD_JSYDSYQ)) {
                                ((BdcXm) vo).setSqlx("215");
                            }
                        }
                    }
                }
                insertVoList.add(vo);
            }
            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(ygQlid, Constants.QLRLX_QLR);
            if (CollectionUtils.isNotEmpty(gdQlrList)) {
                List<BdcQlr> bdcQlrList = gdQlrService.readGdQlrs(gdQlrList);
                for (BdcQlr bdcQlr : bdcQlrList) {
                    bdcQlr.setProid(project.getProid());
                    insertVoList.add(bdcQlr);
                }
            }

        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 在过程时，从过渡库生成抵押情况
     */
    public List<InsertVo> initDyFromGd(Project project, String sqlx2, String ygdyQlid) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //需要考虑过程数据和成果数据的权利人问题
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            project.setDjlx(Constants.DJLX_DYDJ_DM);
            if (StringUtils.isNotBlank(sqlx2)) {
                project.setSqlx(sqlx2);
            }else {
                if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                    project.setSqlx(Constants.SQLX_FWDY_DM);
                }
                else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                    project.setSqlx(Constants.SQLX_TDDY_DM);
                }
            }
            project.setQllx(Constants.QLLX_DYAQ);
            project.setDjsy(Constants.DJSY_DYAQ);
            List<InsertVo> list = creatProjectDyBgdjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                //修改申请类型为买卖转移登记
                if (vo instanceof BdcXm) {
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                } else if (vo instanceof BdcQlr) {
                    continue;
                }
                insertVoList.add(vo);
            }
            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(ygdyQlid, Constants.QLRLX_QLR);
            if (CollectionUtils.isNotEmpty(gdQlrList)) {
                List<BdcQlr> bdcQlrList = gdQlrService.readGdQlrs(gdQlrList);
                for (BdcQlr bdcQlr : bdcQlrList) {
                    bdcQlr.setProid(project.getProid());
                    insertVoList.add(bdcQlr);
                }
            }
            List<GdQlr> gdYwrList = gdQlrService.queryGdQlrs(ygdyQlid, Constants.QLRLX_YWR);
            if (CollectionUtils.isNotEmpty(gdYwrList)) {
                List<BdcQlr> bdcQlrList = gdQlrService.readGdQlrs(gdYwrList);
                for (BdcQlr bdcQlr : bdcQlrList) {
                    bdcQlr.setProid(project.getProid());
                    insertVoList.add(bdcQlr);
                }
            }
        }
        return insertVoList;
    }

}
