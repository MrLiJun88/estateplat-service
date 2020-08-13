package cn.gtmap.estateplat.server.web.main;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.vo.PfBusinessVo;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 * 结果数据管理
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version V1.0, 15-3-10
 */
@Controller
@RequestMapping("/bdcJgSjglNt")
public class BdcJgSjglForNtConntroller extends BaseController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private QllxService qllxService;


    /**
     * 土地/林权/不动产单元匹配页面
     * 根据登记类型和不动产类型获取申请类型
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getSqlxByDjlx")
    public List<Map> getSqlxByBdclxDjlx(Model model, String djlx, String bdclx, String businessId) {
        List<Map> pfWorkFlowDefineVoMap = new ArrayList<Map>();
        if (StringUtils.isEmpty(businessId)) {
            List<PfBusinessVo> pfBusinessVoList = sysWorkFlowDefineService.getBusinessList();
            if (CollectionUtils.isNotEmpty(pfBusinessVoList)) {
                for (PfBusinessVo pfBusinessVotemp : pfBusinessVoList){
                    if (pfBusinessVotemp.getBusinessName().equals(djlx)){
                        businessId = pfBusinessVotemp.getBusinessId();
                    }
                }
                if (StringUtils.isEmpty(businessId)) {
                    businessId = pfBusinessVoList.get(0).getBusinessId();
                }
            }
        }
        List<PfWorkFlowDefineVo> pfWorkFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineByBusiness(businessId);
        if (CollectionUtils.isNotEmpty(pfWorkFlowDefineVoList)) {
            for (int i = 0; i < pfWorkFlowDefineVoList.size(); i++) {
                HashMap map = new HashMap();
                map.put("wfId", pfWorkFlowDefineVoList.get(i).getWorkflowDefinitionId());
                map.put("wfName", pfWorkFlowDefineVoList.get(i).getWorkflowName());
                pfWorkFlowDefineVoMap.add(map);
            }
        }
        return pfWorkFlowDefineVoMap;
    }

    /**
     * 土地/林权/不动产单元匹配页面
     * 获取登记子项
     *
     * @param wfid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDjzxByWfid")
    public List<HashMap> getDjzx(String wfid) {
        List<HashMap> djzx = new ArrayList<HashMap>();
        if (StringUtils.isNoneBlank(wfid)) {
            HashMap map = new HashMap();
            map.put("wdid", wfid);
            djzx = bdcZdGlService.getDjzxBywdid(map);
        }
        return djzx;
    }

    /**
     * 获取过渡房产登记对应的不动产申请类型
     *
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getGdFcDjlxToSqlxWfid")
    public HashMap getGdFcDjlxToSqlxWfid(Model model, @RequestParam(value = "djzx", required = false) String djzx) {
        HashMap map = new HashMap();
        if (StringUtils.isNoneBlank(djzx) && StringUtils.equals(djzx, "undefined")) {
            HashMap map1 = new HashMap();
            map1.put("djzx", djzx);
            List<HashMap> djzxMap = bdcZdGlService.getDjzx(map1);
            if (CollectionUtils.isNotEmpty(djzxMap)) {
                map = djzxMap.get(0);
                if (djzxMap.get(0).get("WFID") != null) {
                    PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(djzxMap.get(0).get("WFID").toString());
                    if (pfWorkFlowDefineVo != null) {
                        map.put("businessId", pfWorkFlowDefineVo.getBusinessId());
                    }
                }
            }

        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "createCsdj")
    public Project creatCsdj(Model model, Project project, String sqlxMc, String bdclx,
                             @RequestParam(value = "qlid", required = false) String qlid,
                             @RequestParam(value = "gdproid", required = false) String gdproid,
                             @RequestParam(value = "gdproids", required = false) String gdproids,
                             @RequestParam(value = "qlids", required = false) String qlids) {
        Project returnProject = null;
        String proid= UUIDGenerator.generate18();
        if (StringUtils.isBlank(qlids) && StringUtils.isNotBlank(qlid)){
            qlids = qlid;
        }
        if (StringUtils.isBlank(gdproids) && StringUtils.isNotBlank(gdproid)){
            gdproids = gdproid;
        }
        //zq 初始化批量的过渡参数，转换为bdcxmrel对象
        projectService.initGdDataToBdcXmRel(project, gdproids, qlids, bdclx);
        project.setUserId(super.getUserId());
        List<BdcXmRel> bdcXmRelList = null;
        bdcXmRelList = project.getBdcXmRelList();
        if (StringUtils.isNotBlank(project.getXmmc())) {
            String[] xmmcs = project.getXmmc().split(",");
            if (xmmcs != null && xmmcs.length > 10)
                project.setXmmc(xmmcs[0] + "等");
        }

        //zhengqi按照bdcxmrel循环批量初始化数据
        if (CollectionUtils.isNotEmpty(bdcXmRelList)){
            for(BdcXmRel bdcXmReltemp:bdcXmRelList) {
                //jyl组织单个不动产单元信息的project
                List<BdcXmRel> tempbdcXmRelList = new ArrayList<BdcXmRel>();
                project.setProid(proid);
                tempbdcXmRelList.add(bdcXmReltemp);
                project.setBdcXmRelList(tempbdcXmRelList);
                project.setYqlid(bdcXmReltemp.getYqlid());
                project.setGdproid(bdcXmReltemp.getYproid());
                if (StringUtils.isNotBlank(bdcXmReltemp.getQjid())) {
                    project.setDjId(bdcXmReltemp.getQjid());
                    DjsjBdcdy djsjBdcdy = djsjFwService.getDjsjBdcdyByDjid(bdcXmReltemp.getQjid());
                    if (djsjBdcdy != null && StringUtils.isNotBlank(djsjBdcdy.getBdcdyh()) && StringUtils.isBlank(project.getBdcdyh())) {
                        project.setBdcdyh(djsjBdcdy.getBdcdyh());
                        project.setBdcdyhs(null);
                    }
                }
                //确定sqlx
                String sqlx = "";
                if (StringUtils.isNoneBlank(project.getWorkFlowDefId())) {
                    Example example = new Example(BdcZdSqlx.class);
                    example.createCriteria().andEqualTo("wdid", project.getWorkFlowDefId());
                    List<BdcZdSqlx> bdcZdSqlxList = entityMapper.selectByExample(BdcZdSqlx.class, example);
                    if (CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
                        project.setSqlx(bdcZdSqlxList.get(0).getDm());
                        sqlx = bdcZdSqlxList.get(0).getDm();
                    }
                }
                //确定djlx
                if (StringUtils.isNoneBlank(project.getSqlx())) {
                    Example example = new Example(BdcDjlxSqlxRel.class);
                    example.createCriteria().andEqualTo("sqlxdm", project.getSqlx());
                    List<BdcDjlxSqlxRel> bdcDjlxSqlxRelList = entityMapper.selectByExample(BdcDjlxSqlxRel.class, example);
                    if (CollectionUtils.isNotEmpty(bdcDjlxSqlxRelList)) {
                        project.setDjlx(bdcDjlxSqlxRelList.get(0).getDjlxdm());
                    }
                }
                //获取权利类型和登记事由、申请类型
                if (StringUtils.isNotBlank(project.getDjzx())) {
                    List<Map> mapList = bdcXmService.getAllLxByWfName(project.getDjzx());
                    if (CollectionUtils.isEmpty(mapList)&&project != null&&StringUtils.isNotBlank(project.getWorkFlowDefId())) {
                        mapList = bdcXmService.getAllLxByWdid(project.getWorkFlowDefId());
                    }
                    if (CollectionUtils.isNotEmpty(mapList)) {
                        Map map = mapList.get(0);
                        String djsy = "";
                        if (map.get(ParamsConstants.QLLXDM_CAPITAL) != null)
                            project.setQllx(map.get(ParamsConstants.QLLXDM_CAPITAL).toString());
                        if (map.get(ParamsConstants.SQLXDM_CAPITAL) != null) {
                            project.setSqlx(map.get(ParamsConstants.SQLXDM_CAPITAL).toString());
                            djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                        }
                        if (StringUtils.isNotBlank(djsy)) {
                            project.setDjsy(djsy);
                        } else {
                            if (map.get(ParamsConstants.DJSYDM_CAPITAL) != null)
                                project.setDjsy(map.get(ParamsConstants.DJSYDM_CAPITAL).toString());
                        }
                    }
                }
                //zhouwanqing通过gdproid获取yqlid,yqlid用于权利人获取
                if (StringUtils.isNotBlank(project.getYqlid())&&StringUtils.isNotBlank(project.getGdproid())) {
                    String fwFilterFsssGhyt = AppConfig.getProperty("fw.filterFsss.ghyt");
                    String isNotPc = null;
                    if (!StringUtils.equals(fwFilterFsssGhyt, "true")) {
                        isNotPc = "true";
                    }
                    List<GdFw> gdFwList = gdFwService.getGdFwByProidForCheckFwDz(project.getGdproid(), isNotPc);//根据过渡项目id获取权利Id
                    if (CollectionUtils.isNotEmpty(gdFwList)) {
                        project.setYqlid(gdFwList.get(0).getQlid());
                    }
                }

                if (StringUtils.isBlank(project.getQllx())&&(
                        StringUtils.equals(project.getSqlx(), Constants.SQLX_YSBZ_DM) ||
                        StringUtils.equals(project.getSqlx(), Constants.SQLX_YSBZ_ZM_DM) ||
                        StringUtils.equals(project.getSqlx(), Constants.SQLX_GZ_DM) ||
                        StringUtils.equals(project.getSqlx(), Constants.SQLX_HZ_DM))) {
                    // crj  遗失补证 ，换证，更正 登记 qllx获取
                    String yqlid = project.getYqlid();
                    if (StringUtils.isBlank(yqlid) && StringUtils.isNotBlank(qlids)) {
                        yqlid = qlids.split(",")[0];
                    }
                    String qllxdm = qllxService.getQllxdmFromGdQl(yqlid);
                    project.setQllx(qllxdm);
                }
                //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断
                if (StringUtils.isBlank(project.getQllx())) {
                    String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
                    project.setQllx(qllxdm);
                }
                if (StringUtils.isNotBlank(bdclx)) {
                    project.setBdclx(bdclx);
                } else {
                    project.setBdclx(Constants.BDCLX_TDFW);
                }
                if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW))
                    project.setXmly(Constants.XMLY_FWSP);
                else
                    project.setXmly(Constants.XMLY_TDSP);
                returnProject = projectService.creatProjectEvent(projectService.getCreatProjectService((BdcXm) project), project);
                //初始化附属设施,proid随便一个只要在同一个wiid里就好，再流程中再确认proid
                projectService.initBdcFwfsss(project, qlids);
                BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
                //lj 匹配创建合并流程，存在多个项目
                List<BdcXm> bdcXmList = null;
                if (bdcXm != null && StringUtils.isNotBlank(project.getWiid())) {
                    HashMap map = new HashMap();
                    map.put("wiid", bdcXm.getWiid());
                    bdcXmList = bdcXmService.andEqualQueryBdcXm(map);
                }
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm xm : bdcXmList) {
                        if (sqlx != null&&(StringUtils.equals(sqlx, "219") || StringUtils.equals(sqlx, "218") || StringUtils.equals(sqlx,Constants.SQLX_CLF_ZDYD))) {
                            projectService.initBdcFwfsss(xm);
                        }
                        if (!CommonUtil.indexOfStrs(Constants.DJLX_CTD_NOQL_SQLXDM, xm.getSqlx())) {
                             turnProjectDefaultService.saveQllxVo(xm);
                        }
                    }
                }
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        }
        return returnProject;
    }
}
