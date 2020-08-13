package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcBdcdyMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/03/08
 * @description  土地分割、合并换证登记
 */
public class CreatComplexFgHbHzServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private BdcZsService bdcZsService;



    @Autowired
    private BdcBdcdyMapper bdcBdcdyMapper;

    @Resource(name = "creatProjectScdjService")
    private CreatProjectScdjServiceImpl creatProjectScdjService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String ySyqProid = "";
    private String ySyqQlid = "";


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else
            throw new AppException(4005);
        String workflowProid = "";
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }
        creatProjectNode(workflowProid);
        /**
         * 根据工作流wiid，查找是否存在记录，若存在则删除对应记录
         */
        if (StringUtils.isNotBlank(project.getWiid())) {
            /**
             * 调用收件单服务根据wiid删除收件单信息
             */
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }
        String sqlx1 = null;
        String sqlx2 = null;
        if (StringUtils.isNotBlank(project.getSqlx())) {
            String sqlxdm = "";
            /**
             * 获取平台的申请类型代码,主要为了合并
             */
            if (org.apache.commons.lang3.StringUtils.isNotBlank(project.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(project.getWiid());
                if (pfWorkFlowInstanceVo != null && org.apache.commons.lang3.StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            /**
             * 获取hb-sqlx.xml中的申请类型配置信息
             */
            HashMap hbSqlxMap = ReadXmlProps.getHbSqlx(sqlxdm);
            if (hbSqlxMap != null) {
                sqlx1 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx1"));
                sqlx2 = CommonUtil.formatEmptyValue(hbSqlxMap.get("sqlx2"));
            }
        }

        List<BdcXmRel> bdcXmRelList = project.getBdcXmRelList();
        String proid = project.getProid();
        //zdd 将不动产单元编号前19位作为键   登记薄ID作为值
        if(CollectionUtils.isNotEmpty(bdcXmRelList)) {
            //先创建剩余宗地项目
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                String isSyzd = Constants.ISSYZD_NO;
                if(StringUtils.isNotBlank(bdcXmRel.getQjid())){
                    isSyzd = bdcDjsjService.getDjsjZdIsSyzdByDjid(bdcXmRel.getQjid());
                }

                if(StringUtils.equals(isSyzd,Constants.ISSYZD_YES)){
                    project.setDjId(bdcXmRel.getQjid());
                    project.setYxmid(bdcXmRel.getYproid());
                    project.setGdproid(bdcXmRel.getYproid());
                    project.setXmly(bdcXmRel.getYdjxmly());
                    project.setYqlid(bdcXmRel.getYqlid());
                    //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                    project.setProid(proid);

                    //zdd 不动产单元号获取
                    if (StringUtils.isNotBlank(project.getDjId())) {
                        HashMap map = new HashMap();
                        if (StringUtils.isNotBlank(project.getBdclx()))
                            map.put("bdclx", project.getBdclx());
                        map.put("id", project.getDjId());
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if (CollectionUtils.isNotEmpty(bdcdyList)
                                &&bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL) && bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                            project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                            if (bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL) != null) {
                                project.setBdclx(bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL).toString());
                                project.setZl(bdcdyList.get(0).get("TDZL").toString());
                            }
                        }
                    }

                    project.setQllx(bdcdyService.getQllxFormBdcdy(project.getBdcdyh()));
                    project.setDjlx(Constants.DJLX_BGDJ_DM);
                    List<InsertVo> list = initSyzd(project,sqlx1,sqlx2);
                    if(CollectionUtils.isNotEmpty(list))
                        insertVoList.addAll(list);
                    break;
                }
            }

            //后创建分割出去的宗地项目
            for (BdcXmRel bdcXmRel : bdcXmRelList) {
                String isSyzd = Constants.ISSYZD_NO;
                if(StringUtils.isNotBlank(bdcXmRel.getQjid())){
                    isSyzd = bdcDjsjService.getDjsjZdIsSyzdByDjid(bdcXmRel.getQjid());
                }

                if(StringUtils.equals(isSyzd,Constants.ISSYZD_NO)) {
                    project.setDjId(bdcXmRel.getQjid());
                    project.setYxmid(bdcXmRel.getYproid());
                    project.setYqlid(bdcXmRel.getYqlid());
                    project.setGdproid(bdcXmRel.getYproid());
                    project.setXmly(bdcXmRel.getYdjxmly());

                    //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                    //jiangganzhi 判断主Proid是否已经生成项目 如没有生成项目 用主Proid生成项目
                    BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(proid);
                    if(bdcXmTemp != null && StringUtils.isNotBlank(bdcXmTemp.getProid())){
                        project.setProid(UUIDGenerator.generate18());
                    }else{
                        project.setProid(proid);
                    }

                    //zdd 不动产单元号获取
                    if (StringUtils.isNotBlank(project.getDjId())) {
                        HashMap map = new HashMap();
                        if (StringUtils.isNotBlank(project.getBdclx())) {
                            map.put("bdclx", project.getBdclx());
                        }
                        map.put("id", project.getDjId());
                        List<Map> bdcdyList = bdcdyService.getDjBdcdyListByPage(map);
                        if (CollectionUtils.isNotEmpty(bdcdyList)&&
                                bdcdyList.get(0).containsKey(ParamsConstants.BDCDYH_CAPITAL)&&
                                bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL) != null) {
                            project.setBdcdyh(bdcdyList.get(0).get(ParamsConstants.BDCDYH_CAPITAL).toString());
                            if (bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL) != null) {
                                project.setBdclx(bdcdyList.get(0).get(ParamsConstants.BDCLX_CAPITAL).toString());
                                project.setZl(bdcdyList.get(0).get("TDZL").toString());
                            }
                        }
                    }

                    project.setBdcdyid("");
                    project.setXmly(Constants.XMLY_BDC);
                    project.setQllx(bdcdyService.getQllxFormBdcdy(project.getBdcdyh()));
                    project.setSqlx(Constants.SQLX_GYJSYDSYQSCDJ_DM);
                    project.setDjlx(Constants.DJLX_CSDJ_DM);
                    List<InsertVo> list = creatProjectScdjService.initVoFromOldData(project);

                    for (InsertVo vo : list) {
                        if (vo instanceof BdcXm) {
                            BdcXm bdcXm = (BdcXm) vo;
                            /**
                             *创建收件信息
                             */
                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                            if (bdcSjxx != null) {
                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                bdcSjxx.setProid(project.getProid());
                                bdcSjxx.setWiid(project.getWiid());
                                insertVoList.add(bdcSjxx);
                            }
                        }

                        if (vo instanceof BdcXmRel) {
                            BdcXmRel bdcXmRelTemp = (BdcXmRel) vo;
                            bdcXmRelTemp.setYproid(this.ySyqProid);
                            bdcXmRelTemp.setYqlid(this.ySyqQlid);
                        }
                    }
                    insertVoList.addAll(list);
                }
            }

        }

        return insertVoList;
    }


    /**
     * 创建剩余宗地项目
     *
     * @param project
     * @param sqlx1
     * @param sqlx2
     * @return
     */
    public List<InsertVo> initSyzd(Project project,String sqlx1,String sqlx2){
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if(StringUtils.isNotBlank(project.getBdcdyh())) {
            String zddDjh = StringUtils.substring(project.getBdcdyh(), 0, 19);
            HashMap hashMap = new HashMap();
            hashMap.put("djh", zddDjh);
            List<String> yZdDjhList = bdcBdcdyMapper.getYdjhByDjh(hashMap);
            if (CollectionUtils.isNotEmpty(yZdDjhList)) {
                List<BdcJsydzjdsyq> bdcJsydzjdsyqList = null;
                String bdcdyid = "";
                String syqQlid = "";
                String syqProid = "";
                for (String yZdDjh : yZdDjhList) {
                    String yZdBdcdyh = yZdDjh + "W00000000";
                    if (StringUtils.isNotBlank(yZdBdcdyh)&&StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                        bdcdyid = bdcdyService.getBdcdyidByBdcdyh(yZdBdcdyh);
                        if (StringUtils.isNotBlank(bdcdyid)) {
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                            map.put("qszt", 1);
                            bdcJsydzjdsyqList = bdcJsydzjdsyqService.getBdcJsydzjdsyqList(map);
                            if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                                break;
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                    BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqList.get(0);
                    if (bdcJsydzjdsyq != null) {

                        /**
                         * 创建土地所有权流程
                         */
                        this.ySyqQlid = syqQlid = bdcJsydzjdsyq.getQlid();
                        this.ySyqProid = syqProid = bdcJsydzjdsyq.getProid();

                        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcJsydzjdsyq.getProid());
                        if(CollectionUtils.isNotEmpty(bdcZsList)){
                            BdcZs bdcZs = bdcZsList.get(0);
                            if(bdcZs != null&&StringUtils.isNotBlank(bdcZs.getBdcqzh())){
                                project.setYbdcqzh(bdcZs.getBdcqzh());
                            }
                        }
                        project.setYqlid(syqQlid);
                        project.setYxmid(syqProid);
                        project.setDjlx(Constants.DJLX_BGDJ_DM);
                        List<InsertVo> syqVoList = initSyqFromBdc(project, sqlx1);
                        //将对象保存
                        super.insertProjectData(syqVoList);

                        /**
                         * 创建抵押流程
                         */
                        if (StringUtils.isNotBlank(bdcdyid)) {
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                            map.put("qszt", 1);
                            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                for (BdcDyaq bdcdyaq : bdcDyaqList) {
                                    project.setYxmid(bdcdyaq.getProid());
                                    project.setProid(UUIDGenerator.generate18());

                                    BdcXm bdcDayqXm = bdcXmService.getBdcXmByProid(bdcdyaq.getProid());
                                    if(bdcDayqXm != null&&StringUtils.isNotBlank(bdcDayqXm.getYbdcqzh())){
                                        project.setYbdcqzh(bdcDayqXm.getYbdcqzh());
                                    }

                                    List<InsertVo> dyListnew = initDyFromBdc(project, sqlx2);
                                    if (CollectionUtils.isNotEmpty(dyListnew)) {
                                        insertVoList.addAll(dyListnew);
                                    }
                                }
                            }
                        }

                    }
                } else {
                    List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
                    GdTdsyq gdTdsyq = null;
                    Boolean tdsyqIsXs = false;
                    String yZdBdcdyh = "";
                    for (String yZdDjh : yZdDjhList) {
                        yZdBdcdyh = yZdDjh + "W00000000";
                        if (StringUtils.isNotBlank(yZdBdcdyh)) {
                            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(yZdBdcdyh);
                            if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                                for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                                    if (bdcGdDyhRel != null && StringUtils.isNotBlank(bdcGdDyhRel.getGdid())) {
                                        List<GdBdcQlRel> gdBdcQlRelTempList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcGdDyhRel.getGdid());
                                        if (CollectionUtils.isNotEmpty(gdBdcQlRelTempList)){
                                            gdBdcQlRelList.addAll(gdBdcQlRelTempList);
                                        }
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                                for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                    gdTdsyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRel.getQlid());
                                    if (gdTdsyq != null && gdTdsyq.getIszx() == 0) {
                                        tdsyqIsXs = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (tdsyqIsXs) {
                            break;
                        }
                    }

                    if (gdTdsyq != null && gdTdsyq.getIszx() == 0) {

                        /**
                         * 创建土地所有权流程
                         */
                        this.ySyqQlid = syqQlid = gdTdsyq.getQlid();
                        this.ySyqProid = syqProid = gdTdsyq.getProid();
                        project.setYqlid(syqQlid);
                        project.setGdproid(syqProid);
                        project.setDjlx(Constants.DJLX_BGDJ_DM);

                        if(StringUtils.isNotBlank(gdTdsyq.getTdzh()))
                            project.setYbdcqzh(gdTdsyq.getTdzh());

                        List<InsertVo> syqVoList = initSyqFromGd(project, sqlx1);
                        //将对象保存
                        super.insertProjectData(syqVoList);


                        /**
                         * 创建抵押流程
                         */
                        //不动产抵押权
                        bdcdyid = bdcdyService.getBdcdyidByBdcdyh(yZdBdcdyh);
                        if (StringUtils.isNotBlank(bdcdyid)) {
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                            map.put("qszt", 1);
                            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                for (BdcDyaq bdcdyaq : bdcDyaqList) {
                                    project.setYxmid(bdcdyaq.getProid());
                                    project.setProid(UUIDGenerator.generate());

                                    if(StringUtils.isNotBlank(gdTdsyq.getTdzh())){
                                        project.setYbdcqzh(gdTdsyq.getTdzh());
                                    }

                                    List<InsertVo> dyListnew = initDyFromBdc(project, sqlx2);
                                    if (CollectionUtils.isNotEmpty(dyListnew)) {
                                        insertVoList.addAll(dyListnew);
                                    }
                                }
                            }

                        }

                        //过渡抵押权
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                GdDy gdDy = gdFwService.getGdDyByDyid(gdBdcQlRel.getQlid(), 0);
                                if (gdDy != null) {
                                    String dyQlid = gdDy.getDyid();
                                    String ydyGdproid = gdDy.getProid();
                                    project.setProid(UUIDGenerator.generate());
                                    project.setYqlid(dyQlid);
                                    project.setYxmid(ydyGdproid);

                                    if(StringUtils.isNotBlank(gdTdsyq.getTdzh())){
                                        project.setYbdcqzh(gdTdsyq.getTdzh());
                                    }

                                    List<InsertVo> dyVoList = initDyFromGd(project, sqlx1);
                                    if (CollectionUtils.isNotEmpty(dyVoList)) {
                                        insertVoList.addAll(dyVoList);
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }
        return insertVoList;
    }


    /**
     * 走过渡创建的换证登记
     *
     * @param project
     * @param sqlx1
     * @return
     */
    public List<InsertVo> initSyqFromGd(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            project.setXmly(Constants.XMLY_TDSP);
            List<InsertVo> list = creatProjectScdjService.initVoFromOldData(project);

            String djh = StringUtils.substring(project.getBdcdyh(), 0, 19);
            BdcTd bdcTd = bdcTdService.selectBdcTd(djh);
            for (InsertVo vo : list) {
                if (vo instanceof BdcTd) {
                    bdcTd = (BdcTd) vo;
                    break;
                }
            }

            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;

                    /**
                     * 项目表赋值ybdcqzh
                     */
                    if(StringUtils.isNotBlank(project.getYbdcqzh()))
                        bdcXm.setYbdcqzh(project.getYbdcqzh());

                    /**
                     *创建收件信息
                     */
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    /**
                     * 修改申请类型
                     */
                    if (StringUtils.isNotBlank(sqlx1)) {
                        bdcXm.setSqlx(sqlx1);
                    } else {
                        bdcXm.setSqlx(Constants.SQLX_HZ_DM);
                    }
                }

                if (vo instanceof BdcSpxx) {
                    BdcSpxx bdcSpxx = (BdcSpxx) vo;

                    /**
                     * 修改bdc_spxx中zdzhmj改为新不动产单元所在宗地的宗地面积
                     */
                    if(bdcSpxx != null){
                        if(bdcTd != null)
                            bdcSpxx.setZdzhmj(bdcTd.getZdmj());
                        bdcSpxx.setBdcdyh(project.getBdcdyh());
                    }

                }

                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

    /**
     * 不动产创建的换证登记
     *
     * @param project
     * @param sqlx1
     * @return
     */
    public List<InsertVo> initSyqFromBdc(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            /**
             * 调用换证登记服务，与更正登记逻辑一致
             */
            String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(qllxdm);
            if(StringUtils.isNotBlank(project.getYxmid())){
                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(project.getYxmid());
                if(ybdcXm != null){
                    project.setDjsy(ybdcXm.getDjsy());
                }
            }else if(CollectionUtils.isNotEmpty(project.getBdcXmRelList())){
                BdcXm ybdcXm = bdcXmService.getBdcXmByProid(project.getBdcXmRelList().get(0).getYproid());
                if(ybdcXm != null){
                    project.setDjsy(ybdcXm.getDjsy());
                }
            }

            project.setXmly(Constants.XMLY_BDC);
            List<InsertVo> list = creatProjectScdjService.initVoFromOldData(project);

            String djh = StringUtils.substring(project.getBdcdyh(), 0, 19);
            BdcTd bdcTd = bdcTdService.selectBdcTd(djh);
            for (InsertVo vo : list) {
                if (vo instanceof BdcTd) {
                    bdcTd = (BdcTd) vo;
                    break;
                }
            }


            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;

                    /**
                     * 项目表赋值ybdcqzh
                     */
                    if(StringUtils.isNotBlank(project.getYbdcqzh()))
                        bdcXm.setYbdcqzh(project.getYbdcqzh());

                    /**
                     * 创建收件信息
                     */
                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                    /**
                     * 修改申请类型
                     */
                    if (StringUtils.isNotBlank(sqlx1)) {
                        bdcXm.setSqlx(sqlx1);
                    } else {
                        bdcXm.setSqlx(Constants.SQLX_HZ_DM);
                    }
                }



                if (vo instanceof BdcSpxx) {
                    BdcSpxx bdcSpxx = (BdcSpxx) vo;

                    /**
                     * 修改bdc_spxx中zdzhmj改为新不动产单元所在宗地的宗地面积
                     */
                    if(bdcSpxx != null){
                        if(bdcTd != null)
                            bdcSpxx.setZdzhmj(bdcTd.getZdmj());
                        bdcSpxx.setBdcdyh(project.getBdcdyh());
                    }

                }

                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }


    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从不动产登记库生成抵押情况，如果不存在抵押则做抵押首次
     */
    public List<InsertVo> initDyFromBdc(Project project, String sqlx2) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //如果正式库不存在抵押，则创建一个新的抵押权利，不管是新建还是过渡过来的项目，相当于直接拿着所有权证创建抵押
        //只需要修改proid,djlx,sqlx,qllx
        Project dyProject = new Project();
        try {
            BeanUtils.copyProperties(dyProject, project);
            dyProject.setDjlx(Constants.DJLX_DYDJ_DM);
            if (StringUtils.isNotBlank(sqlx2)) {
                dyProject.setSqlx(sqlx2);
            } else {
                if (StringUtils.equals(dyProject.getBdclx(), Constants.BDCLX_TDFW))
                    dyProject.setSqlx(Constants.SQLX_FWDYBG_DM);
                else if (StringUtils.equals(dyProject.getBdclx(), Constants.BDCLX_TD))
                    dyProject.setSqlx(Constants.SQLX_GYJSDYBG_DM);
            }
            dyProject.setQllx(Constants.QLLX_DYAQ);
            List<InsertVo> dylist = creatProjectDyBgdjService.initVoFromOldData(dyProject);


            String djh = StringUtils.substring(project.getBdcdyh(), 0, 19);
            BdcTd bdcTd = bdcTdService.selectBdcTd(djh);

            for (InsertVo vo : dylist) {
               if (vo instanceof BdcXm) {

                   BdcXm bdcXm = (BdcXm) vo;
                   /**
                    * 项目表赋值ybdcqzh
                    */
                   if(StringUtils.isNotBlank(dyProject.getYbdcqzh()))
                       bdcXm.setYbdcqzh(dyProject.getYbdcqzh());

                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        insertVoList.add(bdcSjxx);
                    }
                }

                if (vo instanceof BdcSpxx) {
                    BdcSpxx bdcSpxx = (BdcSpxx) vo;

                    /**
                     * 修改bdc_spxx中zdzhmj改为新不动产单元所在宗地的宗地面积及bdcdyh给为新不动产单元的不动产单元号
                     */
                    if(bdcSpxx != null){
                        if(bdcTd != null)
                            bdcSpxx.setZdzhmj(bdcTd.getZdmj());
                        bdcSpxx.setBdcdyh(project.getBdcdyh());
                    }

                }

                /**
                 * 修改bdc_bdcdy中bdcdyh改为新不动产单元的不动产单元号
                 */
                if(vo instanceof BdcBdcdy){
                    BdcBdcdy bdcBdcdy = (BdcBdcdy) vo;
                    if(!StringUtils.equals(bdcBdcdy.getBdcdyh(),project.getBdcdyh()))
                        bdcBdcdy.setBdcdyh(project.getBdcdyh());
                }

                insertVoList.add(vo);
            }
        } catch (Exception e) {
            logger.error("CreatComplexFgHbHzServiceImpl.initDyFromBdc",e);
        }

        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从过渡库生成抵押情况
     */
    public List<InsertVo> initDyFromGd(Project project, String sqlx2) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //需要考虑过程数据和成果数据的权利人问题
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            project.setDjlx(Constants.DJLX_CSDJ_DM);
            if (StringUtils.isNotBlank(sqlx2))
                project.setSqlx(sqlx2);
            else {
                if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW))
                    project.setSqlx(Constants.SQLX_FWDY_DM);
                else if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD))
                    project.setSqlx(Constants.SQLX_TDDY_DM);
            }
            project.setQllx(Constants.QLLX_DYAQ);
            project.setDjsy(Constants.DJSY_DYAQ);
            List<InsertVo> list = creatProjectDyBgdjService.initVoFromOldData(project);

            String djh = StringUtils.substring(project.getBdcdyh(), 0, 19);
            BdcTd bdcTd = bdcTdService.selectBdcTd(djh);

            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {

                    BdcXm bdcXm = (BdcXm) vo;
                    /**
                     * 项目表赋值ybdcqzh
                     */
                    if(StringUtils.isNotBlank(project.getYbdcqzh()))
                        bdcXm.setYbdcqzh(project.getYbdcqzh());

                    BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                    if (bdcSjxx != null) {
                        bdcSjxx.setSjxxid(UUIDGenerator.generate());
                        bdcSjxx.setProid(project.getProid());
                        bdcSjxx.setWiid(project.getWiid());
                        insertVoList.add(bdcSjxx);
                    }
                }

                if (vo instanceof BdcSpxx) {
                    BdcSpxx bdcSpxx = (BdcSpxx) vo;

                    /**
                     * 修改bdc_spxx中zdzhmj改为新不动产单元所在宗地的宗地面积及bdcdyh给为新不动产单元的不动产单元号
                     */
                    if(bdcSpxx != null){
                        if(bdcTd != null)
                            bdcSpxx.setZdzhmj(bdcTd.getZdmj());
                        bdcSpxx.setBdcdyh(project.getBdcdyh());
                    }

                }

                /**
                 * 修改bdc_bdcdy中bdcdyh改为新不动产单元的不动产单元号
                 */
                if(vo instanceof BdcBdcdy){
                    BdcBdcdy bdcBdcdy = (BdcBdcdy) vo;
                    if(!StringUtils.equals(bdcBdcdy.getBdcdyh(),project.getBdcdyh())) {
                        bdcBdcdy.setBdcdyh(project.getBdcdyh());
                    }
                }

                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }

}
