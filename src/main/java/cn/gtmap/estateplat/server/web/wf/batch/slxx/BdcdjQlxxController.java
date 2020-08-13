package cn.gtmap.estateplat.server.web.wf.batch.slxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcZdGlMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.service.core.ProjectLifeManageService;
import cn.gtmap.estateplat.server.service.impl.DelProjectDefaultServiceImpl;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.web.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 16-12-14
 * @description       不动产登记权利信息
 */

@Controller
@RequestMapping("/bdcdjQlxx")
public class BdcdjQlxxController extends BaseController {
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Autowired
    BdcDyqService bdcDyqService;
    @Autowired
    BdcTdcbnydsyqService bdcTdcbnydsyqService;
    @Autowired
    BdcTdsyqService bdcTdsyqService;
    @Autowired
    BdcYgService bdcYgService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    BdcZdGlMapper bdcZdGlMapper;
    @Autowired
    ProjectService projectService;
    @Resource(name = "delProjectDefaultServiceImpl")
    DelProjectDefaultServiceImpl delProjectDefaultServiceImpl;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcDjsjService bdcDjsjService;
    @Autowired
    private Repo repository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcLqService bdcLqService;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private ProjectLifeManageService projectLifeManageService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private BdcHysyqService bdcHysyqService;
    @Autowired
    private BdcJzwsyqService bdcJzwsyqService;
    @Autowired
    private BdcYyService bdcYyService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcSjxxService bdcSjxxService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        String path = "wf/core/" + dwdm + "/qlxx/bdcFdcqxx";
        String mjdw = "";
        model.addAttribute("mjdw", mjdw);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                model.addAttribute("wiid", bdcXm.getWiid());
                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                if (qllxVo instanceof BdcDyaq) {
                    model = bdcDyaqService.initBdcDyaqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcDyaqxx";
                } else if (qllxVo instanceof BdcFdcq) {
                    model = bdcFdcqService.initBdcFdcqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcFdcqxx";
                } else if (qllxVo instanceof BdcJsydzjdsyq) {
                    model = bdcJsydzjdsyqService.initBdcJsydzjdsyqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcJsydZjdSyqxx";
                } else if (qllxVo instanceof BdcCf) {
                    model = bdcCfService.initBdcCfForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcCfxx";
                } else if (qllxVo instanceof BdcDyq) {
                    model = bdcDyqService.initBdcDyqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcDyqxx";
                } else if (qllxVo instanceof BdcHysyq) {
                    model = bdcHysyqService.initBdcHysyqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcHysyqxx";
                } else if (qllxVo instanceof BdcJzwsyq) {
                    model = bdcJzwsyqService.initBdcJzwsyqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcJzwsyqxx";
                } else if (qllxVo instanceof BdcLq) {
                    model = bdcLqService.initBdcLqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcLqxx";
                } else if (qllxVo instanceof BdcQsq) {
                    BdcQsq bdcQsq = entityMapper.selectByPrimaryKey(BdcQsq.class, qllxVo.getQlid());
                    model.addAttribute("bdcQsq", bdcQsq);
                    path = "wf/core/" + dwdm + "/qlxx/bdcQsqxx";
                } else if (qllxVo instanceof BdcTdcbnydsyq) {
                    bdcTdcbnydsyqService.initBdcTdcbnydsyqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcTdcbNydsyq";
                } else if (qllxVo instanceof BdcTdsyq) {
                    bdcTdsyqService.initBdcTdsyqForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcTdsyqxx";
                } else if (qllxVo instanceof BdcYg) {
                    bdcYgService.initBdcYgForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcYgxx";
                } else if (qllxVo instanceof BdcYy) {
                    bdcYyService.initBdcYyForPl(model, qllxVo.getQlid(), bdcXm);
                    path = "wf/core/" + dwdm + "/qlxx/bdcYyxx";
                } else if (qllxVo instanceof BdcFdcqDz) {
                    List<BdcZdDjlx> djlxList = bdcZdGlService.getBdcDjlx();
                    //处理证件类型
                    List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
                    BdcFdcqDz bdcFdcqDz = entityMapper.selectByPrimaryKey(BdcFdcqDz.class, qllxVo.getQlid());
                    model.addAttribute("djlxList", djlxList);
                    model.addAttribute("zjlxList", zjlxList);
                    model.addAttribute("bdcFdcqDz", bdcFdcqDz);
                    model.addAttribute("qlid", qllxVo.getQlid());
                    List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
                    String fwytData = "";
                    if (CollectionUtils.isNotEmpty(fwytList)) {
                        for (BdcZdFwyt bdcZdFwyt : fwytList) {
                            if (StringUtils.isBlank(fwytData)) {
                                fwytData = bdcZdFwyt.getDm() + ":" + bdcZdFwyt.getMc();
                            } else {
                                fwytData = fwytData + ";" + bdcZdFwyt.getDm() + ":" + bdcZdFwyt.getMc();
                            }
                        }
                    }
                    model.addAttribute("fwytListJosn", JSONObject.toJSONString(fwytList));
                    List<Map> fwjgList = bdcZdGlService.getZdFwjg();
                    String fwjgData = "";
                    if (CollectionUtils.isNotEmpty(fwjgList)) {
                        for (int i = 0; i < fwjgList.size(); i++) {
                            if (StringUtils.isBlank(fwjgData)) {
                                fwjgData = fwjgList.get(i).get("DM").toString() + ":" + fwjgList.get(i).get("MC").toString();
                            } else {
                                fwjgData = fwjgData + ";" + fwjgList.get(i).get("DM").toString() + ":" + fwjgList.get(i).get("MC").toString();
                            }
                        }
                    }
                    model.addAttribute("fwjgListJosn", JSONObject.toJSONString(fwjgList));
                    model.addAttribute("fwytData", fwytData);
                    model.addAttribute("fwjgData", fwjgData);
                    path = "wf/core/" + dwdm + "/qlxx/bdcFdcqDzxx";
                }else if (qllxVo==null) {
                    model = bdcFdcqService.initBdcFdcqForPl(model, "", bdcXm);
                    path = "wf/core/"+dwdm+"/qlxx/bdcFdcqxx";
                }
                BdcSpxx bdcSpxx = null;
                BdcBdcdy bdcBdcdy = null;
                if(qllxVo!=null && StringUtils.isNotBlank(qllxVo.getProid())) {
                    bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(qllxVo.getProid());
                    bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(qllxVo.getProid());
                }
                if(bdcSpxx==null)
                    bdcSpxx=new BdcSpxx();
                if(bdcBdcdy==null)
                    bdcBdcdy=new BdcBdcdy();

                model.addAttribute("bdcSpxx", bdcSpxx);
                model.addAttribute("bdcBdcdy", bdcBdcdy);
                model.addAttribute("bdcXm", bdcXm);
            }
        }
        return path;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 关联权利人
     */
    @RequestMapping(value = "/glSqr", method = RequestMethod.GET)
    public String glSqr(Model model, @RequestParam(value = "proid", required = false) String proid, @RequestParam(value = "wiid", required = false) String wiid
            , @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "qllxdm", required = false) String qllxdm, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        model.addAttribute("proid", proid);
        model.addAttribute("wiid", wiid);
        model.addAttribute("qlid", qlid);
        model.addAttribute("qllxdm", qllxdm);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        //身份证件种类
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        model.addAttribute("zjlxListJosn", JSONObject.toJSONString(zjlxList));
        return "wf/batch/slxx/glSqr";
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 权利的权利人义务人信息
     */
    @ResponseBody
    @RequestMapping("/getQlrxxPagesJson")
    public Object getQlrxxPagesJson(Pageable pageable, String proid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(proid)) {
            map.put("proid", proid);
        }
        Page<HashMap> dataPaging = repository.selectPaging("getQlrxxPagesJson", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 增加权利
     */
    @RequestMapping(value = "addQl", method = RequestMethod.GET)
    public String addQl(Model model, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "qllx", required = false) String qllx, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "taskid", required = false) String taskid, @RequestParam(value = "rid", required = false) String rid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<HashMap> bdcdyList = new ArrayList<HashMap>();
            bdcdyList = bdcdyService.getBdcdyidAndZlByWiid(wiid);
            model.addAttribute("bdcdyList", bdcdyList);
            model.addAttribute("wiid", wiid);
            //初始化bdcXm的信息
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                BdcXm bdcXm = bdcXmList.get(0);
                model.addAttribute("bdclx", bdcXm.getBdclx());
            }
            model.addAttribute("proid", UUIDGenerator.generate18());
        }
        model.addAttribute("qllx", qllx);
        List<Map> djlxList = bdcZdGlService.getDjlxByBdclx(Constants.BDCLX_TDFW);
        List<Map> sqlxList = bdcZdGlService.getSqlxByBdclxDjlx(Constants.BDCLX_TDFW, Constants.DJLX_CSDJ_DM);
        model.addAttribute("sqlxList", sqlxList);
        model.addAttribute("djlxList", djlxList);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        return "wf/batch/slxx/addQl";
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据不动产单元获取不动产类型
     */
    @ResponseBody
    @RequestMapping(value = "getBdclxByBdcdyid")
    public HashMap getBdclxByBdcdyid(Model model, String bdcdyid) {
        HashMap resultMap = new HashMap();
        String bdclx = "TDFW";
        if (StringUtils.isNotBlank(bdcdyid)) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdclx())) {
                bdclx = bdcBdcdy.getBdclx();
            }
        }
        resultMap.put("bdclx", bdclx);
        return resultMap;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 创建 不动产项目
     */
    @ResponseBody
    @RequestMapping(value = "/creatBdcXm")
    public HashMap creatBdcXm(Project project, String sqlxMc) {
        HashMap resultMap = new HashMap();
        String msg = "创建失败";
        String userName = SessionUtil.getCurrentUser().getUsername();
        BdcXm ybdcXm = null;
        BdcXm bdcXm = null;
        if (StringUtils.isNotBlank(project.getProid()))
            bdcXm = bdcXmService.getBdcXmByProid(project.getProid());
        if (bdcXm == null) {
            //获取要继承的原项目
            if (StringUtils.isNotBlank(project.getYxmid()))
                ybdcXm = bdcXmService.getBdcXmByProid(project.getYxmid());
            //获取权利类型和登记事由、申请类型
            if (StringUtils.isNotBlank(sqlxMc)) {
                List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
                if (mapList != null && mapList.size() > 0) {
                    Map map = mapList.get(0);
                    String djsy = "";
                    if (map.get("QLLXDM") != null)
                        project.setQllx(map.get("QLLXDM").toString());
                    if (map.get("SQLXDM") != null) {
                        project.setSqlx(map.get("SQLXDM").toString());
                        djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                    }
                    if (StringUtils.isNotBlank(djsy)) {
                        project.setDjsy(djsy);
                    } else {
                        if (map.get("DJSYDM") != null)
                            project.setDjsy(map.get("DJSYDM").toString());
                    }
                }
            }
            //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断
            if (StringUtils.isBlank(project.getQllx())) {
                String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
                project.setQllx(qllxdm);
            }
            /**
             * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
             * @description 更正登记，遗失补发换证登记，通过过渡证书类型来判断权利类型
             */
            if ((project.getDjlx().equals(Constants.DJLX_GZDJ_DM)
                    || project.getSqlx().equals(Constants.SQLX_YSBZ_DM)
                    || project.getSqlx().equals(Constants.SQLX_YSBZ_ZM_DM)
                    || project.getSqlx().equals(Constants.SQLX_HZ_DM)) && ybdcXm != null) {
                QllxVo qllxVo = qllxService.queryQllxVo(ybdcXm);
                if (qllxVo instanceof BdcFdcq) {
                    project.setQllx(Constants.QLLX_GYTD_FWSUQ);
                } else if (qllxVo instanceof BdcTdcbnydsyq) {
                    project.setQllx(Constants.QLLX_GYTD_JSYDSYQ);
                } else if (qllxVo instanceof BdcDyaq) {
                    project.setQllx(Constants.QLLX_DYAQ);
                } else if (qllxVo instanceof BdcYg) {
                    project.setQllx(Constants.QLLX_YGDJ);
                } else if (qllxVo instanceof BdcYy) {
                    project.setQllx(Constants.QLLX_YYDJ);
                }
            }
            if (StringUtils.isNotBlank(project.getProid()) && StringUtils.isNotBlank(project.getWiid()) && StringUtils.isNotBlank(project.getDjlx()) && StringUtils.isNotBlank(project.getSqlx()) && StringUtils.isNotBlank(project.getQllx()) && ybdcXm != null) {
                bdcXm = bdcXmService.creatBdcXm(project, ybdcXm, userName);
                if (bdcXm != null) {
                    // jyl 收件信息（一个项目一个收件信息)
                    BdcSjxx bdcSjxx=bdcSjxxService.queryBdcSjxxByProid(bdcXm.getProid());
                    //jyl 当项目没有对应的收件信息初始化
                    if(bdcSjxx==null){
                        BdcSjxx bdcSjxxtemp=bdcSjxxService.queryBdcSjxxByWiid(bdcXm.getWiid());
                        if(bdcSjxxtemp!=null){
                            bdcSjxxtemp.setProid(bdcXm.getProid());
                            bdcSjxxtemp.setSjxxid(UUIDGenerator.generate18());
                            bdcSjxxService.saveBdcSjxx(bdcSjxxtemp);
                        }
                    }
                    msg = "创建成功";
                }
            }
        } else {
            msg = "创建成功";
        }
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 新增权利信息
     */
    @ResponseBody
    @RequestMapping(value = "/creatQlxx")
    public HashMap creatQlxx(Project project, String sqlxMc) {
        HashMap resultMap = new HashMap();
        String msg = "增加失败";
        String userName = SessionUtil.getCurrentUser().getUsername();
        BdcXm ybdcXm = null;
        //获取权利类型和登记事由、申请类型
        if (StringUtils.isNotBlank(sqlxMc)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(sqlxMc);
            if (mapList != null && mapList.size() > 0) {
                Map map = mapList.get(0);
                String djsy = "";
                if (map.get("QLLXDM") != null)
                    project.setQllx(map.get("QLLXDM").toString());
                if (map.get("SQLXDM") != null) {
                    project.setSqlx(map.get("SQLXDM").toString());
                    djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(project.getSqlx());
                }
                if (StringUtils.isNotBlank(djsy)) {
                    project.setDjsy(djsy);
                } else {
                    if (map.get("DJSYDM") != null)
                        project.setDjsy(map.get("DJSYDM").toString());
                }
            }
        }
        //sc 其他更正登记类型，没有配置申请类型权利类型关系表 没有权利类型根据不动产单元号判断
        if (StringUtils.isBlank(project.getQllx())) {
            String qllxdm = bdcdyService.getQllxFormBdcdy(project.getBdcdyh());
            project.setQllx(qllxdm);
        }
        //jyl 当bdcdyid不为空的时候说明是新增权利
        if (StringUtils.isNotBlank(project.getBdcdyid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiidAndBdcdyid(project.getWiid(), project.getBdcdyid());
            //0:需要新增不动产项目；1:不需要新增
            String flag = "0";
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                //jyl 蓝底不动产项目
                ybdcXm = bdcXmList.get(0);
                for (BdcXm bdcXm1 : bdcXmList) {
                    //jyl 当不动产项目中的登记类型和申请类型还是任意组合的就要在本身上初始化，而不是再创建一个新的不动产项目继承
                    if (StringUtils.equals(bdcXm1.getDjlx(), Constants.DJLX_HBDJ_DM) && StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_HDDJ_RYZH)) {
                        project.setProid(bdcXm1.getProid());
                        bdcXm1.setDjlx(project.getDjlx());
                        bdcXm1.setQllx(project.getQllx());
                        bdcXm1.setSqlx(project.getSqlx());
                        bdcXm1.setDjsy(project.getDjsy());
                        entityMapper.saveOrUpdate(bdcXm1, bdcXm1.getProid());
                        // jyl 收件信息（一个项目一个收件信息)
                        BdcSjxx bdcSjxx=bdcSjxxService.queryBdcSjxxByProid(bdcXm1.getProid());
                        //jyl 当项目没有对应的收件信息初始化
                        if(bdcSjxx==null){
                            BdcSjxx bdcSjxxtemp=bdcSjxxService.queryBdcSjxxByWiid(bdcXm1.getWiid());
                            if(bdcSjxxtemp!=null){
                                bdcSjxxtemp.setProid(bdcXm1.getProid());
                                bdcSjxxtemp.setSjxxid(UUIDGenerator.generate18());
                                bdcSjxxService.saveBdcSjxx(bdcSjxxtemp);
                            }
                        }
                        //jyl 生成权利
                        TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
                        QllxVo qllxVo = turnProjectDefaultService.saveQllxVo(bdcXm1);
                        if (qllxVo != null) {
                            msg = "创建成功";
                        }
                        flag = "1";
                    }
                }
            }
            if (StringUtils.equals(flag, "0")) {
                BdcXm bdcXm = null;
                if (StringUtils.isNotBlank(project.getProid()) && StringUtils.isNotBlank(project.getWiid()) && StringUtils.isNotBlank(project.getDjlx()) && StringUtils.isNotBlank(project.getSqlx()) && StringUtils.isNotBlank(project.getQllx()) && ybdcXm != null) {
                    bdcXm = bdcXmService.creatBdcXm(project, ybdcXm, userName);
                }
                // jyl 收件信息（一个项目一个收件信息)
                BdcSjxx bdcSjxx=bdcSjxxService.queryBdcSjxxByProid(bdcXm.getProid());
                //jyl 当项目没有对应的收件信息初始化
                if(bdcSjxx==null){
                    BdcSjxx bdcSjxxtemp=bdcSjxxService.queryBdcSjxxByWiid(bdcXm.getWiid());
                    if(bdcSjxxtemp!=null){
                        bdcSjxxtemp.setProid(bdcXm.getProid());
                        bdcSjxxtemp.setSjxxid(UUIDGenerator.generate18());
                        bdcSjxxService.saveBdcSjxx(bdcSjxxtemp);
                    }
                }
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                if (bdcSpxx != null) {
                    bdcSpxx.setProid(project.getProid());
                    bdcSpxx.setSpxxid(UUIDGenerator.generate());
                    bdcSpxxService.saveBdcSpxx(bdcSpxx);
                }
                List<BdcXmRel> xmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(xmRelList)) {
                    for (BdcXmRel xmrel : xmRelList) {
                        xmrel.setProid(project.getProid());
                        xmrel.setRelid(UUIDGenerator.generate());
                        bdcXmRelService.saveBdcXmRel(xmrel);
                    }
                }
                //jyl 生成权利
                TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(project);
                turnProjectDefaultService.saveQllxVo(bdcXm);
            }
        }
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 创建项目
     */
    @ResponseBody
    @RequestMapping(value = "/initVoFromOldData")
    public String createWfProject(Project project) {
        String msg = "失败";
        if (project != null && StringUtils.isNotBlank(project.getProid())) {
            //初始化整理project
            project = projectService.initProject(project);
            project.setUserId(super.getUserId());
            //林权独立
            if (StringUtils.isNotBlank(project.getBdclx()) && Constants.BDCLX_TDSL.equals(project.getBdclx())) {
                projectLifeManageService.initializeProject(project);
            } else {
                projectLifeManageService.initializeProjectForAddQl(project);
                msg = "成功";
            }
        }
        return msg;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 删除权利信息
     */
    @ResponseBody
    @RequestMapping(value = "/delQl", method = RequestMethod.POST)
    public Map delQl(Model model, String proid, String wiid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(wiid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
                //缮证后就不能删除了。
                if (bdcZsList == null || bdcZsList.size() == 0) {
                    if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                        String wfProid = PlatformUtil.getProIDFromPlatform(bdcXm.getWiid());
                        String flag = "";
                        // bianwen判断需删除的对应的proid是不是流程的proid,
                        // 不相等可以直接删除所有的项目信息若相等，
                        // 则要将剩余的数据任选一条复制过来
                        if (StringUtils.equals(proid, wfProid)) {
                            flag = "true";
                        } else {
                            // 删除项目关系表
                            bdcXmRelService.delBdcXmRelByProid(proid);
                            //删除收件单信息表
                            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByProid(proid);
                            if (bdcSjxxList != null && bdcSjxxList.size() > 0) {
                                for (BdcSjxx bdcSjxx : bdcSjxxList) {
                                    bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                                    bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
                                }
                            }
                            //删除审批信息
                            bdcSpxxService.delBdcSpxxByProid(proid);
                            //删除权利人证书关系信息表以及权利人信息
                            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid);
                            if (bdcQlrList != null && bdcQlrList.size() > 0) {
                                for (BdcQlr bdcQlr : bdcQlrList) {
                                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
                                }
                            }
                            //删除权利类型信息
                            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
                            qllxService.delQllxByproid(qllxVo, proid);
                            bdcdyService.delDjbAndTd(bdcXm);
                            if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                                List<BdcXm> bdcXmList = null;
                                HashMap mapTemp = new HashMap();
                                mapTemp.put("bdcdyid", bdcXm.getBdcdyid());
                                bdcXmList = bdcXmService.andEqualQueryBdcXm(mapTemp);
                                if (bdcXmList != null && bdcXmList.size() == 1) {
                                    bdcdyService.delBdcdyById(bdcXm.getBdcdyid());
                                }
                            }
                            bdcXmService.delBdcXmByProid(proid);
                            delProjectDefaultServiceImpl.delProjectNode(proid);
                        }

                        if (StringUtils.equals(flag, "true")) {
                            String proidAll = bdcXmService.getProidsByProid(wfProid);
                            String proid1 = "";
                            if (StringUtils.isNotBlank(proidAll)) {
                                for (String proidStr : StringUtils.split(proidAll, Constants.SPLIT_STR)) {
                                    if (!StringUtils.equals(proidStr, wfProid)) {
                                        proid1 = proidStr;
                                        break;
                                    }
                                }
                                BdcXm xm1 = bdcXmService.getBdcXmByProid(proid1);
                                BdcXm wfxm = bdcXmService.getBdcXmByProid(wfProid);
                                try {
                                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid1);
                                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                        for (BdcXmRel bdcxmrel : bdcXmRelList) {
                                            bdcxmrel.setProid(wfProid);
                                            bdcXmRelService.delBdcXmRelByProid(wfProid);
                                            bdcXmRelService.saveBdcXmRel(bdcxmrel);
                                        }
                                    }
                                    List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByProid(proid1);
                                    List<BdcSjxx> wfbdcSjxxList = bdcSjdService.queryBdcSjdByProid(wfProid);
                                    if (wfbdcSjxxList != null && wfbdcSjxxList.size() > 0) {
                                        for (BdcSjxx bdcSjxx : wfbdcSjxxList) {
                                            bdcSjdService.delSjclListBySjxxid(bdcSjxx.getSjxxid());
                                            bdcSjdService.delBdcSjxxBySjxxid(bdcSjxx.getSjxxid());
                                        }
                                    }
                                    if (bdcSjxxList != null && bdcSjxxList.size() > 0) {
                                        for (BdcSjxx bdcSjxx : bdcSjxxList) {
                                            bdcSjxx.setProid(wfProid);
                                            bdcSjxxService.saveBdcSjxx(bdcSjxx);
                                        }
                                    }
                                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid1);
                                    if (bdcSpxx != null) {
                                        bdcSpxx.setProid(wfProid);
                                        bdcSpxxService.delBdcSpxxByProid(wfProid);
                                        bdcSpxxService.saveBdcSpxx(bdcSpxx);
                                    }
                                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(proid1);
                                    List<BdcQlr> wfbdcQlrList = bdcQlrService.queryBdcQlrYwrByProid(wfProid);
                                    if (wfbdcQlrList != null && wfbdcQlrList.size() > 0) {
                                        for (BdcQlr bdcQlr : wfbdcQlrList) {
                                            bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid());
                                        }
                                    }
                                    if (bdcQlrList != null && bdcQlrList.size() > 0) {
                                        for (BdcQlr bdcQlr : bdcQlrList) {
                                            bdcQlr.setProid(wfProid);
                                            bdcQlrService.saveBdcQlr(bdcQlr);
                                        }
                                    }
                                    //删除权利类型信息
                                    QllxVo qllxVo = qllxService.makeSureQllx(wfxm);
                                    qllxService.delQllxByproid(qllxVo, wfProid);
                                    QllxVo qllxVoTemp = qllxService.makeSureQllx(xm1);
                                    qllxVoTemp = qllxService.queryQllxVo(qllxVoTemp, xm1.getProid());
                                    if (qllxVoTemp != null) {
                                        qllxVoTemp.setProid(wfProid);
                                        qllxService.saveQllxVo(qllxVoTemp);
                                    }
                                    bdcdyService.delDjbAndTd(wfxm);
                                    if (StringUtils.isNotBlank(wfxm.getBdcdyid())) {
                                        List<BdcXm> bdcXmList = null;
                                        HashMap mapTemp = new HashMap();
                                        mapTemp.put("bdcdyid", wfxm.getBdcdyid());
                                        bdcXmList = bdcXmService.andEqualQueryBdcXm(mapTemp);
                                        if (bdcXmList != null && bdcXmList.size() == 1) {
                                            bdcdyService.delBdcdyById(wfxm.getBdcdyid());
                                        }
                                    }
                                    delProjectDefaultServiceImpl.delProjectNode(wfProid);
                                    BdcXm xm = (BdcXm) BeanUtils.cloneBean(xm1);
                                    xm.setProid(wfProid);
                                    bdcXmService.saveBdcXm(xm);
                                    bdcXmService.delBdcXmByProid(xm1.getProid());
                                } catch (Exception e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }

                            }
                        }
                    }
                } else {
                    returnvalue = "sz";
                }
                returnvalue = "success";
            }
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @Author:<a href="mailto:huangzijian@gtmap.cn">huangzijian</a>
     * @Description:产权信息页面土地开始日期变动获取土地结束日期
     * @Date 11:02 2017/4/20
     */
    @ResponseBody
    @RequestMapping(value = "/getTdsyjsqx", method = RequestMethod.POST)
    public Map getTdsyjsqx(String tdsyksqx, String proid, String wiid) {
        HashMap map = new HashMap();
        Integer syqx = null;
        String dldm = "";
        Date tdsyjsqx = null;
        if (StringUtils.isNotBlank(proid)) {
            List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxByProid(proid);
            if (CollectionUtils.isNotEmpty(djsjZdxxList) && djsjZdxxList.size() > 0) {
                dldm = djsjZdxxList.get(0).getTdyt();
            }
            if (StringUtils.isNotBlank(dldm)) {
                syqx = Integer.parseInt(bdcZdGlService.getTdsyqxByDm(dldm));
            }
            if (StringUtils.isNotBlank(tdsyksqx) && syqx != null) {
                tdsyjsqx = CalendarUtil.addYears(CalendarUtil.formatDate(tdsyksqx), syqx);
            }
        }
        map.put("tdsyjsqx",  CalendarUtil.formatDateToString(tdsyjsqx));
        return map;
    }
}
