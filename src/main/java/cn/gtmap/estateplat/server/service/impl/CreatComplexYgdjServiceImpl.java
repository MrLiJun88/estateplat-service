package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.model.OntBdcXm;
import cn.gtmap.estateplat.server.core.model.OntQlr;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.alibaba.fastjson.JSONArray;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 * zdd 商品房预告登记与预告抵押登记合并办理服务L
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-8-19
 */
public class CreatComplexYgdjServiceImpl extends CreatComplexProjectServiceImpl {
    @Autowired
    BdcXmService bdcXmService;
    /**
     * zdd 此处后续优化 可以通过参数确定调用哪一个服务
     */
    @Resource(name = "creatProjectYgdjServiceImpl")
    CreatProjectService creatProjectYgdjServiceImpl;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    @Override
    public synchronized List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        Project project;
        if (xmxx instanceof Project)
            project = (Project) xmxx;
        else
            throw new AppException(4005);
        //zdd 预告合并登记选择同一个不动产单元 所以登记薄ID 与 不动产单元ID 是一致的
        //如果登记簿id等于空给默认值
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (StringUtils.isBlank(project.getDjbid())) {
            project.setDjbid(UUIDGenerator.generate18());
        }
        //如果不动产单元id等于空给默认值
        if (StringUtils.isBlank(project.getBdcdyid())) {
            project.setBdcdyid(UUIDGenerator.generate18());
        }
        if (StringUtils.isNotBlank(project.getWiid())) {
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList))
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
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
        if (StringUtils.isNotBlank(project.getProid()) && project.getXmly().equals(Constants.XMLY_BDC)) {
            String workflowProid = "";
            if (StringUtils.isNotBlank(project.getWorkflowProid())) {
                workflowProid = project.getWorkflowProid();
            } else if (StringUtils.isNotBlank(project.getProid())) {
                workflowProid = project.getProid();
            }
            creatProjectNode(workflowProid);
            String proid = project.getProid();

            /**
             * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
             * @description 获取OntBdcXmList, OntQlrList, OntYwrList
             */
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            List<OntBdcXm> ontBdcXmList = (List<OntBdcXm>) session.getAttribute("ontBdcXm_" + proid);
            List<OntQlr> ontQlrList = (List<OntQlr>) session.getAttribute("ontQlr_" + proid);
            List<OntQlr> ontYwrList = (List<OntQlr>) session.getAttribute("ontYwr_" + proid);
            for (int i = 0; i < 2; i++) {
                //外网收件,预告抵押合并登记时,赋值当前的流程类型,以便添加权利人
                session.setAttribute("lclx_" + proid, "YG");
                //预告抵押的原项目id为预告的项目id
                if (i == 1) {
                    project.setYxmid(project.getProid());
                    if (ontBdcXmList != null) {
                        session.setAttribute("ontBdcXm_" + proid, ontBdcXmList);
                    }
                    if (ontQlrList != null) {
                        session.setAttribute("ontQlr_" + proid, ontQlrList);
                    }
                    if (ontYwrList != null) {
                        session.setAttribute("ontYwr_" + proid, ontYwrList);
                    }
                    session.setAttribute("lclx_" + proid, "YGDY");
                }
                //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                project.setProid(proid);
                //zdd 不动产单元号获取  暂时只考虑房屋的
                DjsjFwxx djsjFwxx = djsjFwService.getDjsjFwxx(project.getDjId());
                if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                    project.setBdcdyh(djsjFwxx.getBdcdyh());
                }

                project.setDjlx(Constants.DJLX_YGDJ_DM);

                List<InsertVo> bdcList = creatProjectYgdjServiceImpl.initVoFromOldData(project);
                if (i == 0) {
                    String buildingContractUrl = AppConfig.getProperty("building-contract.url");
                    if (StringUtils.isNotBlank(buildingContractUrl)) {
                        HttpClient client = null;
                        PostMethod method = null;
                        try {
                            String url = buildingContractUrl + "/htbaServerClient/getQlr";
                            client = new HttpClient();
                            client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 3000);
                            client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 3000);
                            method = new PostMethod(url);
                            method.setRequestHeader("Connection", "close");
                            method.addParameter("bdcdyh", project.getBdcdyh());
                            client.executeMethod(method);
                            String qlrJson = method.getResponseBodyAsString();
                            if (StringUtils.isNotBlank(qlrJson)) {
                                List<BdcQlr> bdcQlrList = JSONArray.parseArray(qlrJson, BdcQlr.class);
                                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                    for (BdcQlr bdcQlr : bdcQlrList) {
                                        bdcQlr.setProid(proid);
                                        insertVoList.add(bdcQlr);
                                    }
                                }
                            }

                        } catch (IOException e) {
                           logger.error("CreatComplexYgdjServiceImpl.initVoFromOldData",e);
                        }finally {
                            if(method != null) {
                                method.releaseConnection();
                            }
                            if(client != null) {
                                ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
                            }
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(bdcList)) {
                    //创建收件信息
                    for (InsertVo vo : bdcList) {
                        if (vo instanceof BdcXm) {
                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                            if (bdcSjxx != null) {
                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                insertVoList.add(bdcSjxx);
                            }

                            if (i == 0) {
                                if (StringUtils.isNotBlank(sqlx1)) {
                                    ((BdcXm) vo).setSqlx(sqlx1);
                                }else {
                                    ((BdcXm) vo).setSqlx(Constants.SQLX_YG_YGSPF);
                                }
                                ((BdcXm) vo).setDjsy(Constants.DJSY_GYJSYDSHQ + "/" + Constants.DJSY_FWSYQ);
                            } else {
                                if (StringUtils.isNotBlank(sqlx2)) {
                                    ((BdcXm) vo).setSqlx(sqlx2);
                                }else {
                                    ((BdcXm) vo).setSqlx(Constants.SQLX_YG_YGSPFDY);
                                }
                                ((BdcXm) vo).setDjsy(Constants.DJSY_DYAQ);
                            }
                            insertVoList.add(vo);
                        } else if (vo instanceof BdcYg) {
                            if (i == 0) {
                                ((BdcYg) vo).setYgdjzl(Constants.YGDJZL_YGSPF);
                            }else {
                                ((BdcYg) vo).setYgdjzl(Constants.YGDJZL_YGSPFDY);
                            }
                            insertVoList.add(vo);
                        } else if (vo instanceof BdcQlr) {
                            /**
                             * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                             * @description 初始化预告抵押后，将预告抵押的义务人初始为空
                             */
                            if (i == 0) {
                                insertVoList.add(vo);
                            }
                            if (i == 1 && ontBdcXmList != null) {
                                insertVoList.add(vo);
                            }
                        } else {
                            insertVoList.add(vo);
                        }
                    }
                }
                //zdd 从新赋值proid
                proid = UUIDGenerator.generate18();
            }
        } else { //从匹配选择过渡证
            String ygQlid = "";
            String dyYgQlid = "";

            if (StringUtils.isNotBlank(project.getGdproid())) {
                List<GdYg> gdYgList = gdFwService.getGdYgListByGdproid(project.getGdproid(), 0);

                if (CollectionUtils.isNotEmpty(gdYgList)) {
                    for (GdYg yg : gdYgList) {
                        if (yg != null && (StringUtils.equals(yg.getYgdjzl(), Constants.YGDJZL_YGSPFDY) || StringUtils.equals(yg.getYgdjzl(), Constants.YGDJZL_QTYGSPFDY))) {
                            dyYgQlid = yg.getYgid();
                        } else if (yg != null) {
                            ygQlid = yg.getYgid();
                        }
                    }
                }
            }
            project.setYqlid(ygQlid);
            project.setDjlx(Constants.DJLX_YGDJ_DM);
            List<InsertVo> ygVoList = initYgFromGd(project, sqlx1);
            //zx只能先保存
            super.insertProjectData(ygVoList);

            String bdcdyid = "";
            if (CollectionUtils.isNotEmpty(ygVoList)) {
                for (InsertVo insertVo : ygVoList) {
                    if (insertVo instanceof BdcBdcdy) {
                        bdcdyid = ((BdcBdcdy) insertVo).getBdcdyid();
                    }
                }
            }
            String pplx = AppConfig.getProperty("sjpp.type");
            if (StringUtils.isNotBlank(sqlx2)) {
                project.setSqlx(sqlx2);
            }else {
                project.setSqlx(Constants.SQLX_YG_YGSPFDY);
            }
            project.setDjlx(Constants.DJLX_YGDJ_DM);
            project.setDjsy(Constants.DJSY_DYAQ);
            project.setYqlid(dyYgQlid);
            List<InsertVo> dyList = null;
            if (StringUtils.isNotBlank(pplx) && pplx.equals(Constants.PPLX_GC)) {
                //过程
                project.setProid(UUIDGenerator.generate());
                project.setYqlid(dyYgQlid);
                project.setBdcdyid(bdcdyid);
                dyList = initDyFromGd(project);

            }
            if (CollectionUtils.isNotEmpty(dyList)) {
                insertVoList.addAll(dyList);
            }
        }

        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 从过渡库创建预告登记
     */
    public List<InsertVo> initYgFromGd(Project project, String sqlx1) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            if (StringUtils.isNotBlank(sqlx1)){
                project.setSqlx(sqlx1);
            }else {
                project.setSqlx(Constants.SQLX_YG_YGSPF);
            }
            project.setDjsy(Constants.DJSY_GYJSYDSHQ + "/" + Constants.DJSY_FWSYQ);
            List<InsertVo> bdcList = creatProjectYgdjServiceImpl.initVoFromOldData(project);
            if (CollectionUtils.isNotEmpty(bdcList)) {
                for (InsertVo vo : bdcList) {
                    //修改申请类型为买卖转移登记
                    if (vo instanceof BdcXm) {
                        BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                        if (bdcSjxx != null) {
                            bdcSjxx.setSjxxid(UUIDGenerator.generate());
                            bdcSjxx.setProid(project.getProid());
                            bdcSjxx.setWiid(project.getWiid());
                            insertVoList.add(bdcSjxx);
                        }

                    } else if (vo instanceof BdcYg) {
                        ((BdcYg) vo).setYgdjzl(Constants.YGDJZL_YGSPF);
                    }
                    insertVoList.add(vo);
                }
            }
        }
        return insertVoList;
    }

    /**
     * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
     * @description 在过程时，从过渡库生成抵押情况
     */
    public List<InsertVo> initDyFromGd(Project project) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        //需要考虑过程数据和成果数据的权利人问题
        if (project != null && StringUtils.isNotBlank(project.getProid()) && project.getDjId() != null) {
            project.setSqlx(Constants.SQLX_YG_YGSPFDY);
            project.setDjsy(Constants.DJSY_DYAQ);
            List<InsertVo> list = creatProjectYgdjServiceImpl.initVoFromOldData(project);
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
                } else if (vo instanceof BdcYg) {
                    ((BdcYg) vo).setYgdjzl(Constants.YGDJZL_YGSPFDY);
                }
                insertVoList.add(vo);
            }
        }
        return insertVoList;
    }
}
