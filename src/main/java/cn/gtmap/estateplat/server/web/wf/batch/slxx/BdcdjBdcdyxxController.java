package cn.gtmap.estateplat.server.web.wf.batch.slxx;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.core.support.mybatis.page.model.Page;
import cn.gtmap.estateplat.core.support.mybatis.page.repository.Repo;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.core.ProjectLifeManageService;
import cn.gtmap.estateplat.server.service.impl.DelProjectDefaultServiceImpl;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.web.main.BaseController;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
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
 * @author <a href="mailto:qiuchuanghe@gtmap.cn">qiuchuanghe</a>
 * @version 1.0, 16-12-12
 * @description       不动产登记不动产单元信息
 */
@Controller
@RequestMapping("/bdcdjBdcdyxx")
public class BdcdjBdcdyxxController extends BaseController {
    @Autowired
    private Repo repository;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcSqlxQllxRelService bdcSqlxQllxRelService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectLifeManageService projectLifeManageService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    private BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    private BdcSjxxService bdcSjxxService;
    @Autowired
    QllxService qllxService;
    @Resource(name = "delProjectDefaultServiceImpl")
    DelProjectDefaultServiceImpl delProjectDefaultServiceImpl;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdXmService gdXmService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, BdcBdcdy bdcBdcdy, @RequestParam(value = "wiid", required = false) String wiid,@RequestParam(value = "from", required = false) String from,@RequestParam(value = "taskid", required = false) String taskid,@RequestParam(value = "rid", required = false) String rid, @RequestParam(value = "bdcdyid", required = false) String bdcdyid) {
        BdcXm bdcXm = null;
        BdcSpxx bdcSpxx = null;
        String proid="";
        if (StringUtils.isNotBlank(wiid)&&StringUtils.isNotBlank(bdcdyid)) {
            List<BdcXm> bdcXmByBdcdyidList = bdcXmService.getBdcXmListByWiidAndBdcdyid(wiid, bdcdyid);
            if (CollectionUtils.isNotEmpty(bdcXmByBdcdyidList)) {
                bdcXm = bdcXmByBdcdyidList.get(0);
                if (bdcXm != null) {
                    proid=bdcXm.getProid();
                    bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                    if (bdcSpxx == null) {
                        bdcSpxx = new BdcSpxx();
                        bdcSpxx.setSpxxid(UUIDGenerator.generate18());
                        bdcSpxx.setProid(bdcXm.getProid());
                        System.out.println("理论上，bdcSpxx不能为空的，检查为什么！");
                    }
                    bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
                    if (bdcBdcdy == null) {
                        bdcBdcdy = new BdcBdcdy();
                        bdcBdcdy.setBdcdyid(UUIDGenerator.generate18());
                        System.out.println("理论上，bdcBdcdy不能为空的，检查为什么！");
                    }
                }
            }
        }
        if (bdcXm == null)
            bdcXm = new BdcXm();
        if (bdcSpxx == null)
            bdcSpxx = new BdcSpxx();
        if (bdcBdcdy == null)
            bdcBdcdy = new BdcBdcdy();
        model.addAttribute("bdcXm", bdcXm);
        model.addAttribute("bdcSpxx", bdcSpxx);
        model.addAttribute("bdcBdcdy", bdcBdcdy);
        if (StringUtils.isNotBlank(proid)) {
            model.addAttribute("proid", proid);
        }
        List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
        List<HashMap> zdList = bdcZdGlService.getZdzhytZdb(new HashMap());
        List<HashMap> yhlxList = bdcZdGlService.getBdcZdYhlx(new HashMap());
        List<HashMap> gzwlxList = bdcZdGlService.getGjzwLxZdb(new HashMap());
        List<HashMap> lzList = bdcZdGlService.getBdcZdLz(new HashMap());
        List<Map> qlxzList = bdcZdGlService.getZdQlxz();
        List<HashMap>  dybdclxList= bdcZdGlService.getBdcZdDybdclx(new HashMap());
        model.addAttribute("fwytList", fwytList);
        model.addAttribute("zdList", zdList);
        model.addAttribute("yhlxList", yhlxList);
        model.addAttribute("gzwlxList", gzwlxList);
        model.addAttribute("lzList", lzList);
        model.addAttribute("qlxzList", qlxzList);
        model.addAttribute("from", from);
        model.addAttribute("taskid", taskid);
        model.addAttribute("rid", rid);
        model.addAttribute("wiid", wiid);
        model.addAttribute("dybdclxList", dybdclxList);
        return "wf/batch/slxx/bdcdyXxxx";
    }

    /**
     * @param
     * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
     * @rerutn
     * @description 不动产单元信息
     */
    @ResponseBody
    @RequestMapping("/getBdcdyxxPagesJson")
    public Object getBdcdyxxPagesJson(Pageable pageable, String wiid) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(wiid))
            map.put("wiid", wiid);
        Page<HashMap> dataPaging = repository.selectPaging("getBdcdyxxByPage", map, pageable);
        return dataPaging;
    }

    /**
     * @param
     * @author <a href="mailto:qiuchuanghe@gtmap.cn">qiuchuanghe</a>
     * @rerutn
     * @description 不动产单元信息
     */
    @RequestMapping(value = "addBdcdy", method = RequestMethod.GET)
    public String addBdcdy(Model model, String proid, @RequestParam(value = "wiid", required = false) String wiid) {
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(wiid)) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
        }
        BdcXm bdcXm = null;
        if (bdcXmList != null && bdcXmList.size() > 0) {
            bdcXm = bdcXmList.get(0);
        } else {
            bdcXm = bdcXmService.getBdcXmByProid(proid);
        }
        model.addAttribute("proid", proid);
        List<Map> bdclxList = bdcZdGlService.getZdBdclx();
        List<BdcSqlxQllxRel> bdcSqlxQllxRelList = null;
        String yqllxdm = "";
        String bdclxdm = "";
        String qlxzdm = "";
        Integer bdcdyly = Constants.BDCDYLY_ALL;
        String zdtzm = "";  //宗地宗海号  或者 不动产单元  的13、14位 为zdtzm
        String workFlowName = PlatformUtil.getWorkFlowNameByProid(proid);
        String qllx = "";
        String dyfs = "";
        String ysqlxdm = "";
        if (StringUtils.isNotBlank(workFlowName)) {
            List<Map> mapList = bdcXmService.getAllLxByWfName(workFlowName);
            if (mapList != null && mapList.size() > 0) {
                Map map = mapList.get(0);
                if (map.get("QLLXDM") != null)
                    qllx = CommonUtil.formatEmptyValue(map.get("QLLXDM"));
            }
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getSqlx())) {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sqlxdm", bdcXm.getSqlx());
            if (StringUtils.isNotBlank(qllx))
                queryMap.put("qllxdm", qllx);
            bdcSqlxQllxRelList = bdcSqlxQllxRelService.andEqualQueryBdcSqlxQllxRel(queryMap);
        }
        if (bdcSqlxQllxRelList != null && bdcSqlxQllxRelList.size() > 0) {
            BdcSqlxQllxRel bdcSqlxQllxRel = bdcSqlxQllxRelList.get(0);
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYqllxdm()))
                yqllxdm = bdcSqlxQllxRel.getYqllxdm();
            if (bdcSqlxQllxRel.getBdcdyly() != null)
                bdcdyly = bdcSqlxQllxRel.getBdcdyly();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getZdtzm()))
                zdtzm = bdcSqlxQllxRel.getZdtzm();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getDyfs()))
                dyfs = bdcSqlxQllxRel.getDyfs();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getBdclx()))
                bdclxdm = bdcSqlxQllxRel.getBdclx();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getQlxzdm()))
                qlxzdm = bdcSqlxQllxRel.getQlxzdm();
            if (StringUtils.isNotBlank(bdcSqlxQllxRel.getYsqlxdm()))
                ysqlxdm = bdcSqlxQllxRel.getYsqlxdm();
        }
        String sqlx = "";
        if (null != bdcXm) {
            String sqlxdm = bdcXm.getSqlx();
            if (StringUtils.equals(sqlxdm, Constants.SQLX_JF)) {
                sqlx = "JF";
            }
        }
        List<BdcZdSqlx> bdcZdSqlxList=null;
        if(StringUtils.isNotBlank(bdcXm.getSqlx())){
            Example example = new Example(BdcZdSqlx.class);
            example.createCriteria().andEqualTo("dm",bdcXm.getSqlx());
            bdcZdSqlxList= entityMapper.selectByExample(BdcZdSqlx.class,example);
        }
        model.addAttribute("dyfs", dyfs);
        model.addAttribute("yqllxdm", yqllxdm);
        model.addAttribute("bdclxdm", bdclxdm);
        model.addAttribute("bdcdyly", bdcdyly);
        model.addAttribute("zdtzm", zdtzm);
        model.addAttribute("qlxzdm", qlxzdm);
        model.addAttribute("bdclxList", bdclxList);
        model.addAttribute("ysqlxdm", ysqlxdm);
        model.addAttribute("sqlx", sqlx);
        model.addAttribute("djlx", bdcXm.getDjlx());
        model.addAttribute("wiid", bdcXm.getWiid());
        if(CollectionUtils.isNotEmpty(bdcZdSqlxList)) {
            model.addAttribute("sqlxmc", bdcZdSqlxList.get(0).getMc());
            model.addAttribute("workFlowDefId",bdcZdSqlxList.get(0).getWdid());
        }
        if (CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, bdcXm.getSqlx())) {
            model.addAttribute("plChoseOne", Constants.PL_CHOSE_ONE);
        }
        return "wf/batch/slxx/bdcdyxx";
    }

    /**
     * zdd
     *
     * @param project
     * @return
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
                projectLifeManageService.initializeProjectForPl(project);
                msg = "成功";
            }
        }
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/createCsdj")
    public String creatCsdj(Project project, String lx, @RequestParam(value = "qlid", required = false) String qlid, @RequestParam(value = "gdproid", required = false) String gdproid, @RequestParam(value = "gdproids", required = false) String gdproids, @RequestParam(value = "qlids", required = false) String qlids) {
        String msg = "失败";
        BdcXm bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        if (bdcXm != null) {
            //yinyao 针对单个流程重复选的时候执行下面方法；
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
                project = (Project) bdcXmService.getProjectFromBdcXm(bdcXm, project);
            }
        }
        project.setBdcdyid(null);
        List<BdcXmRel> bdcXmRelList = new ArrayList<BdcXmRel>();
        //jyl  新增的批量的添加不动产单元保留原的
        if (StringUtils.isNotBlank(bdcXm.getXmzt()) && StringUtils.isNotBlank(bdcXm.getDwdm())) {
            project.setProid(UUIDGenerator.generate18());
            if (project.getBdcXmRelList() != null && project.getBdcXmRelList().size() > 0) {
                for (int i = 0; i < project.getBdcXmRelList().size(); i++) {
                    project.getBdcXmRelList().get(i).setProid(project.getProid());
                }
            }
        }
        //zq 初始化批量的过渡参数，转换为bdcxmrel对象
        projectService.initGdDataToBdcXmRelForPl(project, gdproids, qlids);
        if(StringUtils.isNotBlank(gdproids))project.setYxmid(gdproids);
        if(StringUtils.isNotBlank(qlids))project.setYqlid(qlids);
        else project.setYqlid(qlid);
        project.setUserId(super.getUserId());
        //zx过渡数据创建多幢时记录权利id和过渡项目id
        if (StringUtils.indexOf(project.getBdcdyh(), ",") > -1) {
            project.setYqlid(qlids);
            project.setGdproid(gdproids);
        } else
            project.setGdproid(gdproid);
        if (StringUtils.isNotBlank(project.getGdproid())) {
            String bdcdys = gdXmService.getBdcdyhsByGdProid(project.getGdproid());
            if (StringUtils.isNotBlank(bdcdys)) {
                List<String> bdcdyList = new ArrayList<String>();
                bdcdyList.add(bdcdys);
                project.setBdcdyhs(bdcdyList);
            }
        }
        if (StringUtils.isBlank(project.getBdclx()) && StringUtils.isNotBlank(lx)) {
            project.setBdclx(lx);
        }
        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW))
            project.setXmly(Constants.XMLY_FWSP);
        else
            project.setXmly(Constants.XMLY_TDSP);
        bdcXm = entityMapper.selectByPrimaryKey(BdcXm.class, project.getProid());
        if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh())){
            project.setBh(bdcXm.getBh());
        }
        //林权独立
        if (StringUtils.isNotBlank(project.getBdclx()) && Constants.BDCLX_TDSL.equals(project.getBdclx())) {
            projectLifeManageService.initializeProject(project);
        } else {
            projectLifeManageService.initializeProjectForPl(project);
            msg = "成功";
        }
        return msg;
    }



    /**
     * @param
     * @author <a href="mailto:qiuchuanghe@gtmap.cn">qiuchuanghe</a>
     * @rerutn
     * @description 保存不动产单元信息
     */
    @ResponseBody
    @RequestMapping(value = "/saveBdcdyxx", method = RequestMethod.POST)
    public Map saveBdcdyxx(Model model, BdcBdcdy bdcdy, BdcSpxx bdcSpxx, BdcXm bdcXm) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (bdcdy != null && StringUtils.isNotBlank(bdcdy.getBdcdyid()) && bdcSpxx != null && StringUtils.isNotBlank(bdcSpxx.getSpxxid()) && bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())&&StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcdyService.saveBdcdy(bdcdy);
            //jyl 同一流程实例里同一个不动产单元对应的不动产项目和不动产审批信息上在不动产单元页面的信息应该一致，保持同步。
            List<BdcXm> bdcXmByBdcdyidList = bdcXmService.getBdcXmListByWiidAndBdcdyid(bdcXm.getWiid(), bdcdy.getBdcdyid());
            if(CollectionUtils.isNotEmpty(bdcXmByBdcdyidList)){
                for(BdcXm bdcXmTemp:bdcXmByBdcdyidList){
                    bdcXm.setProid(bdcXmTemp.getProid());
                    bdcXmService.saveBdcXm(bdcXm);
                    BdcSpxx bdcSpxxTemp=bdcSpxxService.queryBdcSpxxByProid(bdcXmTemp.getProid());
                    if(bdcSpxxTemp!=null){
                        bdcSpxx.setSpxxid(bdcSpxxTemp.getSpxxid());
                        bdcSpxx.setProid(bdcSpxxTemp.getProid());
                        bdcSpxxService.saveBdcSpxx(bdcSpxx);
                    }
                }
            }
            returnvalue = "success";
        }
        map.put("msg", returnvalue);
        return map;
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 删除不动产单元信息
     */
    @ResponseBody
    @RequestMapping(value = "/delBdcdyxx", method = RequestMethod.POST)
    public Map delBdcdyxx(Model model, String bdcdyid,String wiid) {
        Map map = Maps.newHashMap();
        String returnvalue = "fail";
        if (StringUtils.isNotBlank(bdcdyid)&&StringUtils.isNotBlank(wiid)) {
            //组织同bdcdyid和wiid下的所有不动产项目
            List<BdcXm> bdcXmByBdcdyidList = null;
                bdcXmByBdcdyidList = bdcXmService.getBdcXmListByWiidAndBdcdyid(wiid, bdcdyid);
            if (bdcXmByBdcdyidList != null && bdcXmByBdcdyidList.size() > 0) {
                for (BdcXm bdcXmTemp : bdcXmByBdcdyidList) {
                   String proid = bdcXmTemp.getProid();
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proid);
                    //缮证后就不能删除了。
                    if (bdcZsList == null || bdcZsList.size() == 0) {
                        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
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
                                        bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
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
                                String proidAll = bdcXmService.getProidsByProidAndBdcdyid(wfProid, bdcdyid);
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
                                                bdcZsQlrRelService.delBdcZsQlrRelByQlrid(bdcQlr.getQlrid());
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
        }
        map.put("msg", returnvalue);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getGdFczxxPagesJson")
    public Object getGdFczxxPagesJson(Pageable pageable, String bdcdyh,  String fczh, String qlr, String fwzl, String dcxc) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        }else {
            if (StringUtils.isNotBlank(fczh)) {
                map.put("fczh", fczh);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put("bdcdyh", bdcdyh);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if (StringUtils.isNotBlank(fwzl)) {
                map.put("fwzl", fwzl);
            }
        }
        Page<HashMap> dataPaging = repository.selectPaging("getGdFczxxByPage", map, pageable);
        return dataPaging;
    }

    @ResponseBody
    @RequestMapping("/getGdTdzxxPagesJson")
    public Object getGdTdzxxPagesJson(Pageable pageable, String bdcdyh, String tdzh, String qlr,  String dcxc,String tdzl) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dcxc)) {
            map.put("dcxc", StringUtils.deleteWhitespace(dcxc));
        }else {
            if (StringUtils.isNotBlank(tdzh)) {
                map.put("tdzh", tdzh);
            }
            if (StringUtils.isNotBlank(bdcdyh)) {
                map.put("bdcdyh", bdcdyh);
            }
            if (StringUtils.isNotBlank(qlr)) {
                map.put("qlr", qlr);
            }
            if (StringUtils.isNotBlank(tdzl)) {
                map.put("tdzl", tdzl);
            }
        }
        Page<HashMap> dataPaging = repository.selectPaging("getGdTdzxxByPage", map, pageable);
        return dataPaging;
    }
}

