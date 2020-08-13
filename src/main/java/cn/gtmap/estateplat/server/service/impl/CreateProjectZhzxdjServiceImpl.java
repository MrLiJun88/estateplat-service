package cn.gtmap.estateplat.server.service.impl;/* 
 * @author <a href="mailto:lizhi@gtmap.cn">lizhi</a>
 * @version 1.0, 16-12-16
 * @description      不动产土地逐户注销登记
 */

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjxxMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateProjectZhzxdjServiceImpl extends CreatProjectDefaultServiceImpl implements CreatProjectService {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    BdcSjdService bdcSjdService;
    @Autowired
    DjxxMapper djxxMapper;
    @Autowired
    QllxService qllxService;
    @Autowired
    DjsjFwService djsjFwService;
    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;

    private static final String PARAMETER_DEFAULT_BDCDYH = "W00000000";


    @Override
    @Transactional
    public List<InsertVo> initVoFromOldData(Xmxx xmxx) {
        List<InsertVo> insertVoList = new LinkedList<InsertVo>();
        Project project = null;
        if (xmxx instanceof Project)
            project = (Project) xmxx;
        String zdbdcdyh = "";
        String zddjh = "";
        String yproid = "";
        String gdproid = "";
        String yqlid = "";
        String yxmly = "";
        String djid = "";
        if (project != null && StringUtils.isNotBlank(project.getProid()) &&CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
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
                if (StringUtils.isNotBlank(bdcXmRel.getQjid()))
                    project.setDjId(bdcXmRel.getQjid());
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()))
                    project.setYxmid(bdcXmRel.getYproid());
                if (StringUtils.isNotBlank(bdcXmRel.getYdjxmly()))
                    project.setXmly(bdcXmRel.getYdjxmly());
                if (StringUtils.isNotBlank(bdcXmRel.getYqlid()))
                    project.setYqlid(bdcXmRel.getYqlid());
                if (StringUtils.isNotBlank(bdcXmRel.getYproid()))
                    project.setGdproid(bdcXmRel.getYproid());
                //zdd 第一次为创建项目自带的proid  后面每循环一次  自动生成uuid
                project.setProid(proid);
                //zdd 不动产单元号获取
                if (StringUtils.isNotBlank(project.getDjId())) {
                    HashMap<String, String> ychsMap = new HashMap<String, String>();
                    ychsMap.put("fw_hs_index", project.getDjId());
                    List<DjsjFwHs> djsjFwYcHsList = djsjFwService.getDjsjFwYcHs(ychsMap);
                    if (CollectionUtils.isNotEmpty(djsjFwYcHsList)) {
                        if (StringUtils.isNotBlank(djsjFwYcHsList.get(0).getBdcdyh()))
                            project.setBdcdyh(djsjFwYcHsList.get(0).getBdcdyh());
                    }else{
                        List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(ychsMap);
                        if (CollectionUtils.isNotEmpty(djsjFwHsList)&&StringUtils.isNotBlank(djsjFwHsList.get(0).getBdcdyh())) {
                            project.setBdcdyh(djsjFwHsList.get(0).getBdcdyh());
                        }
                    }
                } else if (StringUtils.isNotBlank(project.getYxmid())
                        &&StringUtils.isNotBlank(project.getXmly()) &&project.getXmly().equals(Constants.XMLY_BDC)) {
                    //否则来源不动产自身
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(project.getYxmid());
                    if (bdcBdcdy != null) {
                        project.setBdcdyh(bdcBdcdy.getBdcdyh());
                        djbidMap.put(bdcBdcdy.getBdcdyh().substring(0, 19), bdcBdcdy.getDjbid());
                    }
                }
                InitVoFromParm initVoFromParm =super.getDjxxByDjsjFwychs(xmxx);
                DjsjZdxx djsjZdxx = initVoFromParm.getDjsjZdxx();
                DjsjFwxx djsjFwxx = initVoFromParm.getDjsjFwxx();
                if (djsjZdxx != null) {
                    zdbdcdyh = djsjZdxx.getDjh() + PARAMETER_DEFAULT_BDCDYH;
                    zddjh = djsjZdxx.getDjh();
                    if (djsjFwxx != null) {
                        BdcSpfZdHjgx bsz = new BdcSpfZdHjgx();
                        bsz.setRelid(UUIDGenerator.generate18());
                        bsz.setProid(proid);
                        bsz.setZdbdcdyh(djsjZdxx.getDjh() + PARAMETER_DEFAULT_BDCDYH);
                        bsz.setDjh(djsjZdxx.getDjh());
                        bsz.setFwbdcdyh(djsjFwxx.getBdcdyh());
                        bsz.setFwhjtdmj(djsjFwxx.getFttdmj());
                        bsz.setFwzl(djsjFwxx.getZl());
                        bsz.setTdzl(djsjZdxx.getTdzl());
                        bsz.setHjsj(CalendarUtil.getCurDate());
                        bsz.setZdbdcdyh(djsjZdxx.getBdcdyh());
                        insertVoList.add(bsz);
                    }
                }
            }

            List<BdcXm> bdcXmList = null;
            if (org.apache.commons.lang.StringUtils.isNotBlank(zdbdcdyh)) {
                bdcXmList = bdcXmService.queryBdcxmByBdcdyh(zdbdcdyh);
            } else if (org.apache.commons.lang.StringUtils.isNotBlank(zddjh)) {
                bdcXmList = bdcXmService.queryBdcxmByBdcdyh(zddjh + PARAMETER_DEFAULT_BDCDYH);
            }
            Map djMap = Maps.newHashMap();
            djMap.put("bdclx", Constants.BDCLX_TD);
            djMap.put("hhSearch", zddjh + PARAMETER_DEFAULT_BDCDYH);
            djid = djxxMapper.getDjid(djMap);
            BdcDyaq bdcDyaq = null;
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    QllxVo qllxVo = qllxService.queryQllxVo(new BdcDyaq(), bdcXm.getProid());
                    if (qllxVo instanceof BdcDyaq&&((BdcDyaq) qllxVo).getQszt() != Constants.QLLX_QSZT_HR) {
                        bdcDyaq = (BdcDyaq) qllxVo;
                        yproid = bdcDyaq.getProid();
                        yxmly = "1";
                    }
                }
            }
            if (bdcDyaq == null && org.apache.commons.lang.StringUtils.isNotBlank(zddjh)) {
                List<String> qlids = gdTdService.getGdTdQlidByDjh(zddjh);
                if (CollectionUtils.isNotEmpty(qlids)) {
                    for (String qlid : qlids) {
                        GdDy gdDy = gdTdService.getGddyqByQlid(qlid,Constants.GDQL_ISZX_WZX);
                        if (gdDy != null && gdDy.getIsjy() != 1) {
                            gdproid = gdDy.getProid();
                            yxmly = "2";
                            yqlid = gdDy.getDyid();
                        }
                    }
                }
            }
        }
        BdcXm bdcxm = bdcXmService.newBdcxm(project);

        project.setBdclx(Constants.BDCLX_TD);
        project.setDjId(djid);
        project.setXmly(yxmly);
        project.setYqlid(yqlid);
        project.setYxmid(yproid);
        project.setBdcdyh(zddjh + PARAMETER_DEFAULT_BDCDYH);
        project.setZdzhh(zddjh);
        project.setGdproid(gdproid);
        //zx完善项目关系列表，一个项目对应多个项目关系，处理成多条记录
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.creatBdcXmRelListFromProject(project);
        if(CollectionUtils.isNotEmpty(bdcXmRelList))
            insertVoList.addAll(bdcXmRelList);
        //zdd 获取地籍数据
        InitVoFromParm initVoFromParm = super.getDjxx(project);
        BdcBdcdjb bdcdjb = null;
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                insertVoList.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }
            // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
            BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
            if (bdcTd == null) {
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), initVoFromParm.getProject(), bdcxm.getQllx());
                insertVoList.add(bdcTd);
            }
        }

        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
        bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
        if (bdcSpxx != null)
            insertVoList.add(bdcSpxx);
        //zdd 不动产单元
        BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
        if (bdcdy != null) {
            bdcxm.setBdcdyid(bdcdy.getBdcdyid());
            insertVoList.add(bdcdy);
        }
        List<BdcQlr>  ybdcQlrList = getYbdcQlrList(project);
        List<BdcQlr> ybdcYwrList = getYbdcYwrList(project);
        //zdd 人方信息
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcQlr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcQlr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcQlr, tempBdcQlrList, project.getProid());
                insertVoList.add(bdcQlr);
            }
        }
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            for (BdcQlr bdcYwr : ybdcYwrList) {
                bdcYwr = bdcQlrService.bdcQlrTurnProjectBdcQlr(bdcYwr, tempBdcYwrList, project.getProid());
                insertVoList.add(bdcYwr);
            }
        }
        insertVoList.add(bdcxm);
        return insertVoList;
    }

}
