/*
package cn.gtmap.estateplat.server.service;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.BdcBaseUnitTest;
import cn.gtmap.estateplat.server.core.mapper.BdcZdQllxMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZsMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZsbhMapper;
import cn.gtmap.estateplat.server.core.mapper.QllxParentMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.web.edit.ZsBhGlConntroller;
import cn.gtmap.estateplat.server.web.main.WfProjectController;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextListener;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Member;
import java.util.*;

*/
/**
 * @version 1.0, 2017/1/18
 * @author<a href = "mailto；liuxing@gtmap.cn">lx</a>
 * @description
 *//*

public class BdcDefaultServiceTest extends BdcBaseUnitTest {

    protected String platformUrl;
    protected String bdcdjUrl;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcFdcqService bdcFdcqService;
    @Autowired
    BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcZsMapper bdcZsMapper;
    @Autowired
    BdczsBhService bdczsBhService;
    @Autowired
    BdcZsService bdcZsService;
    @Autowired
    BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    BdcSjxxService bdcSjxxService;
    @Autowired
    BdcdjbService bdcdjbService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    private BdcZdQllxMapper bdcZdQllxMapper;
    @Autowired
    private QllxParentMapper qllxParentMapper;
    @Autowired
    private BdcZsbhMapper bdcZsbhMapper;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    ZsBhGlConntroller zsBhGlController;
    @Autowired
    WfProjectController wfProjectController;



   @Before
    public void before(){
        RequestContextListener listener = new RequestContextListener();
        MockServletContext context = new MockServletContext();
        MockHttpServletRequest request = new MockHttpServletRequest(context);
        listener.requestInitialized(new ServletRequestEvent(context, request));
        //Member member = memberService.findById(Long.valueOf(1));
        MockHttpSession session = new MockHttpSession();
        //session.setAttribute(Constants.LOGIN_SESSION_NAME, member.getId());
        session.setAttribute("platformUrl", platformUrl);
        session.setAttribute("bdcdjUrl",bdcdjUrl);
        request.setSession(session);
        request.setMethod("GET");
        request.setRequestURI(platformUrl);
    }
    @Test
    public void processTest(){
        String Msg = "";
        HashMap<String, String> next = new HashMap<String, String>();
        Project project = null;

        //参数初始化
        project = initScParam();

        if (project != null) {

            //创建
            next = createAndCheckXm(project,next);
            //缮证项目状态检查
            if (next.containsValue("创建项目成功")) {
                Msg = next.get("createAndCheckXm");
                next = checkXmzt(project, next, "缮证");

                //原权利权属状态检查
                if (next.containsValue("缮证项目状态更新成功")) {
                    Msg = Msg + "->" + next.get("checkSzXmzt");
                    next = checkQszt(project, next);

                    //证书生成情况检查
                    if (next.containsValue("原权利权属状态更新成功")) {
                        Msg = Msg + "->" + next.get("checkQszt");
                        next = saveAndCheckZs(project, next);

                        //登簿人和缮证人检查
                        if (next.containsValue("生成证书成功")) {
                            Msg = Msg + "->" + next.get("saveAndCheckZs");
                            next = checkDbrAndSzr(project, next);

                            //证书编号检查
                            if (next.containsValue("登记簿更新登簿人成功") && next.containsValue("权利表更新登簿人成功")
                                    && next.containsValue("产权证更新缮证人成功")) {
                                for (String key : next.keySet()) {
                                    if (key.equals("checkDjbDbr") || key.equals("checkQlDbr") || key.equals("checkSzr"))
                                        Msg = Msg + "->" + next.get(key);
                                }
                                next = checkZsbh(project, next);

                                //办结项目状态检查
                                if (next.containsValue("编号使用情况更新成功")) {
                                    Msg = Msg + "->" + next.get("checkZsbh");
                                    next = checkXmzt(project, next, "办结");

                                    if (next.containsValue("办结项目状态更新成功")) {
                                        Msg = Msg + "->" + next.get("checkBjXmzt");
                                    } else {
                                        Msg = Msg + "->" + next.get("checkBjXmzt");
                                    }
                                } else {
                                    Msg = Msg + "->" + next.get("checkZsbh");
                                }

                            } else {
                                for (String key : next.keySet()) {
                                    if (key.equals("checkDjbDbr") || key.equals("checkQlDbr") || key.equals("checkSzr"))
                                        Msg = Msg + "->" + next.get(key);
                                }
                            }

                        } else {
                            Msg = Msg + "->" + next.get("saveAndCheckZs");
                        }

                    } else {
                        Msg = Msg + "->" + next.get("checkQszt");
                    }

                } else {
                    Msg = Msg + "->" + next.get("checkSzXmzt");
                }

            } else {
                Msg = next.get("createAndCheckXm");
            }
        }
        System.out.println(Msg);
        //endAndCheck(project, next);
    }

    //参数初始化
    public Object initParam(String sqlx,String qllx,String djlx){
        Object Obj = new Object();
        if (djlx == Constants.DJLX_CSDJ_DM){

        }
        return Obj;
    }

    //首次登记
    public Project initScParam(){
        Project project = new Project();
        project.setDjId("");
        project.setBdcdyh("320684413219GB00029F00010002");
        project.setBdclx("TDFW");
        project.setProid("12MG08354F6K1902");
        project.setYxmid("12KE10175B6K130A");
        project.setYbdcdyid("0CJD23320OWY22EL");
        project.setYbdcqzh("苏(2017)海门市不动产权第0000183号");
        project.setUserId("0");
        return project;
    }

    //创建检查不动产项目
    public HashMap createAndCheckXm(Project project, HashMap next){
        */
/*this.platformUrl = AppConfig.getPlatFormUrl();
        this.bdcdjUrl = AppConfig.getProperty("bdcdj.url");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI(platformUrl);
        request.setAttribute("platformUrl", platformUrl);
        //request.setAttribute("bdcdjUrl",bdcdjUrl);*//*


        String msg = wfProjectController.createWfProject(project,"");
        if (msg.equals("成功")){
            Map<String,String> map = new HashMap<String,String>();
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
            if (bdcXm != null) {
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(project.getProid());
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
                BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());

                if (bdcSpxx != null && bdcXmRelList != null && bdcQlrList != null && bdcdy != null) {
                    String qllx = bdcXm.getQllx();
                    BdcZdQllx bdcZdQllx = bdcZdQllxMapper.queryBdcZdQllxByDm(qllx);
                    String tableName = bdcZdQllx.getTableName();
                    if (tableName.toUpperCase().equals(Constants.NAME_FDCQ) && bdcdy.getBdcdyfwlx().equals(Constants.DJSJ_FWDZ_DM)) {
                        tableName = Constants.NAME_FDCQDZ;
                    }
                    map.put("proid",project.getProid());
                    map.put("tableName",tableName);
                    List<QllxParent> qllxVo = qllxParentMapper.queryQllxVo(map);
                    //QllxVo Vo = bdcZdQllxMapper.queryQlxx(map);
                    if(CollectionUtils.isNotEmpty(qllxVo)){
                        next.put("createAndCheckXm","创建项目成功");
                    }else{
                        next.put("createAndCheckXm","创建项目失败");
                    }
                }else{
                    next.put("createAndCheckXm", "创建项目失败");
                }

            }
        }
        return next;
    }

    //创建检查不动产证书
    public HashMap saveAndCheckZs(Project project, HashMap next){

        try{
            wfProjectController.turnProjectEvent(project.getProid(),"",project.getUserId(),project.getTaskid(),"");
            //验证bdc_zs，bdc_xmzsrel，bdc_zs_qlr_rel表记录生成情况；并且获取证书编号
            Model model = null;
            List<BdcXmzsRel> xmzsRelList = bdcXmZsRelService.queryBdcXmZsRelByProid(project.getProid());
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
            if(CollectionUtils.isNotEmpty(xmzsRelList)){
                for (BdcXmzsRel bdcXmzsRel : xmzsRelList){
                    zsBhGlController.getZsbH(model, "zs", bdcXmzsRel.getZsid(), bdcXm.getDwdm());
                }
                List<BdcZs> zsList = bdcZsService.queryBdcZsByProid(project.getProid());
                List<BdcZsQlrRel> zsQlrRelList = bdcZsQlrRelService.queryBdcZsQlrRelByProid(project.getProid());
                if (CollectionUtils.isNotEmpty(zsList) && CollectionUtils.isNotEmpty(zsQlrRelList)){
                    next.put("saveAndCheckZs", "生成证书成功");
                }else {
                    next.put("saveAndCheckZs", "生成证书失败");
                }
            }else {
                next.put("saveAndCheckZs", "生成证书失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return next;
    }

    //检查权属状态
    public HashMap checkQszt(Project project, HashMap next){
        try{
            wfProjectController.changeYqlZtEvent(project.getProid(),"",project.getUserId(),project.getTaskid(),null);
            BdcXm xm = bdcXmService.getBdcXmByProid(project.getProid());
            //当前只验证新建换证
            if (xm != null && StringUtils.equals(xm.getXmly(),Constants.XMLY_BDC)) {
                BdcXm bdcXm2 = bdcXmService.getBdcXmByProid(project.getYxmid());
                List<BdcXm> xmList2 = null;
                if (bdcXm2 != null && StringUtils.isNotBlank(bdcXm2.getWiid())) {
                    HashMap map = new HashMap();
                    map.put("wiid", bdcXm2.getWiid());
                    xmList2 = bdcXmService.andEqualQueryBdcXm(map);
                }
                if (CollectionUtils.isNotEmpty(xmList2)) {
                    for (BdcXm bdcxm : xmList2) {
                        QllxVo qllxVo = qllxService.makeSureQllx(bdcxm);
                        HashMap map = new HashMap();
                        map.put("proid", bdcxm.getProid());
                        List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
                        if (CollectionUtils.isNotEmpty(qllxVos) && qllxVos.get(0).getQszt() == Constants.QLLX_QSZT_HR) {
                            next.put("checkQszt", "原权利权属状态更新成功");
                        } else {
                            next.put("checkQszt", "原权利权属状态更新失败");
                        }
                    }
                }
            }else{
                next.put("checkQszt", "原权利权属状态更新失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return next;
    }

    //检查证书编号
    public HashMap checkZsbh(Project project, HashMap next){
        //发证
        wfProjectController.turnProjectEventFz(project.getProid(), "", project.getUserId(), "", "", "");
        //验证发证节点证书编号使用情况的处理
        HashMap map = new HashMap();
        map.put("proid",project.getProid());
        List<BdcZsbh> zsbhs = bdcZsbhMapper.getZsbhByMap(map);
        if (CollectionUtils.isNotEmpty(zsbhs)) {
            for (BdcZsbh zsbh : zsbhs) {
                if (StringUtils.equals(zsbh.getSyqk(),Constants.BDCZSBH_SYQK_YSY))
                    next.put("checkZsbh", "编号使用情况更新成功");
                else
                    next.put("checkZsbh", "编号使用情况更新失败");
            }
        }
        return next;

    }
    //检查项目状态
    public HashMap checkXmzt(Project project, HashMap next, String node){
        try {
            if (StringUtils.equals(node, "缮证")) {
                //缮证
                wfProjectController.turnProjectEventChangeXmzt(project.getProid(), "");

            } else if (StringUtils.equals(node, "办结")) {
                //办结
                wfProjectController.endProjectEvent(project.getProid(), "", project.getUserId(), project.getTaskid());
            }
            //验证bdc_xm记录xmzt的变化
            BdcXm bdcXm1 = bdcXmService.getBdcXmByProid(project.getProid());
            List<BdcXm> xmList1 = new ArrayList<BdcXm>();
            if (bdcXm1 != null) {
                //lst 添加批量操作 对原逻辑不影响
                if (StringUtils.isNotBlank(bdcXm1.getWiid())) {
                    Example example = new Example(BdcXm.class);
                    example.createCriteria().andEqualTo("wiid", bdcXm1.getWiid());
                    xmList1 = entityMapper.selectByExample(example);
                } else {
                    xmList1.add(bdcXm1);
                }
            }
            if (CollectionUtils.isNotEmpty(xmList1)) {
                for (BdcXm xmTemp : xmList1) {
                    if (StringUtils.equals(node,"缮证")) {
                        if (StringUtils.equals(xmTemp.getXmzt(), Constants.XMZT_SZ))
                            next.put("checkSzXmzt", "缮证项目状态更新成功");
                        else
                            next.put("checkSzXmzt", "缮证项目状态更新失败");
                    }else if (StringUtils.equals(node, "办结")){
                        if (StringUtils.equals(xmTemp.getXmzt(), Constants.XMZT_BJ))
                            next.put("checkBjXmzt", "办结项目状态更新成功");
                        else
                            next.put("checkBjXmzt", "办结项目状态更新失败");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return next;
    }

    //检查登簿人和缮证人
    public HashMap checkDbrAndSzr(Project project, HashMap next){

        //换证的缮证节点活动ID为“021E1149C37449DB96E12307DA7DABA3”
        wfProjectController.turnProjectEventDbr(project.getProid(),"","0",project.getTaskid(),"","","");
        //验证是否为权利信息表和登记簿加上了登簿人和登记时间
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
        List<BdcXm> xmList = new ArrayList<BdcXm>();
        if (bdcXm != null) {

            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                Example example = new Example(BdcXm.class);
                example.createCriteria().andEqualTo("wiid", bdcXm.getWiid());
                xmList = entityMapper.selectByExample(example);
            } else {
                xmList.add(bdcXm);
            }
        }
        if (CollectionUtils.isNotEmpty(xmList)) {
            for (BdcXm xmTemp : xmList) {
                Map<String, String> map = new HashMap<String, String>();
                String qllx = xmTemp.getQllx();
                BdcZdQllx bdcZdQllx = bdcZdQllxMapper.queryBdcZdQllxByDm(qllx);
                String tableName = bdcZdQllx.getTableName();
                BdcBdcdy bdcdy = bdcdyService.queryBdcdyById(xmTemp.getBdcdyid());
                List<BdcBdcdjb> bdcBdcdjbList = bdcdjbService.selectBdcdjb(StringUtils.substring(bdcdy.getBdcdyh(), 0, 19));
                if (CollectionUtils.isNotEmpty(bdcBdcdjbList)) {
                    BdcBdcdjb bdcBdcdjb = bdcBdcdjbList.get(0);
                    if (StringUtils.isNotBlank(bdcBdcdjb.getDbr()) &&
                            StringUtils.isNotBlank(bdcBdcdjb.getDjsj().toString())) {
                        next.put("checkDjbDbr","登记簿更新登簿人成功");
                    }else {
                        next.put("checkDjbDbr", "登记簿更新登簿人失败");
                    }
                }else{
                    next.put("checkDjbDbr", "登记簿无记录");
                }
                if (bdcdy != null && tableName.toUpperCase().equals(Constants.NAME_FDCQ) && bdcdy.getBdcdyfwlx().equals(Constants.DJSJ_FWDZ_DM)) {
                    tableName = Constants.NAME_FDCQDZ;
                }
                map.put("proid", xmTemp.getProid());
                map.put("tableName", tableName);
                List<QllxParent> qllxVoList = qllxParentMapper.queryQllxVo(map);
                if (CollectionUtils.isNotEmpty(qllxVoList)) {
                    for (int a = 0; a < qllxVoList.size(); a++) {
                        if (StringUtils.isNotBlank(qllxVoList.get(a).getDbr())
                                && StringUtils.isNotBlank(qllxVoList.get(a).getDjsj().toString())) {
                            next.put("checkQlDbr", "权利表更新登簿人成功");
                        }else {
                            next.put("checkQlDbr", "权利表更新登簿人成功");
                        }
                    }
                }else{
                    next.put("checkQlDbr", tableName + "表中无记录");
                }
            }
        }
        wfProjectController.turnProjectEventSzr(project.getProid(),"","0",project.getTaskid(),"","");
        List<BdcZs> zsList = bdcZsService.queryBdcZsByProid(project.getProid());
        if (CollectionUtils.isNotEmpty(zsList)){
            for (BdcZs bdcZs : zsList){
                if (StringUtils.isNotBlank(bdcZs.getSzr()))
                    next.put("checkSzr", "产权证更新缮证人成功");
                else{
                    next.put("checkSzr", "产权证更新缮证人失败");
                }
            }
        }else {
            next.put("checkSzr", "证书表中无记录");
        }
        return next;
    }
    */
/*//*
/办结检查
    public HashMap endAndCheck(Project project, HashMap next){
        try{
            //办结
            wfProjectController.endProjectEvent(project.getProid(),"",project.getUserId(),project.getTaskid());

        }catch (Exception e){
        }
        return next;
    }*//*

}
*/
