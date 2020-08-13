package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.ex.AppException;
import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.DelProjectService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import com.gtis.common.util.UUIDGenerator;
import oracle.jdbc.driver.Const;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, ${date}
 * @description 不动产登记服务
 */
public class CreateComplexScYtdDyServiceImpl extends CreatProjectDefaultServiceImpl {
    @Autowired
    ProjectService projectService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcDyaqService bdcDyaqService;
    @Resource(name = "creatProjectScdjService")
    CreatProjectScdjServiceImpl creatProjectScdjService;
    @Resource(name = "creatProjectDydjServiceImpl")
    CreatProjectDydjServiceImpl creatProjectDydjService;
    @Resource(name = "delProjectDydjServiceImpl")
    DelProjectService delProjectDydjServiceImpl;

    private static final String PARAMETER_BDCDYAQ = "bdcDyaq";


    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        List<InsertVo> insertVoList = new LinkedList<InsertVo>();
        Project project;
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        } else {
            throw new AppException(4005);
        }
        String workflowProid = getWorkflowProid(project);
        creatProjectNode(workflowProid);
        //根据工作流wiid，查找是否存在记录，若存在则删除对应记录
        //jyl 暂时注释
        Map<String, Object> dyMap = getDy(project);
        String bdcdyfwlx = bdcDjsjService.getBdcdyfwlxByBdcdyh(project.getBdcdyh());
        if (!StringUtils.equalsIgnoreCase(bdcdyfwlx, Constants.DJSJ_FWDZ_DM) || (null == dyMap.get("gdDy") && null == dyMap.get(PARAMETER_BDCDYAQ))) {
            if (StringUtils.isNotBlank(project.getBdcdyh()) && project.getBdcdyh().length() >= 19) {
                //qjd 查找不动产土地证号赋给原产权证号，完善bdcxmrel
                String bdcdyh = StringUtils.left(project.getBdcdyh(), 19) + "W00000000";
                String bdcqzh = bdcZsService.getYtdbdcqzhByZdbdcdyh(bdcdyh);
                if (StringUtils.isNotBlank(bdcqzh)) {
                    project.setYbdcqzh(bdcqzh);
                    String yproid = bdcZsService.getProidByBdczqh(bdcqzh);
                    if (StringUtils.isNotBlank(yproid)) {
                        Map queryMap = new HashMap(4);
                        queryMap.put("proid",yproid);
                        BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqService.getBdcJsydzjdsyq(queryMap);
                        project.setYxmid(yproid);
                        if(null != bdcJsydzjdsyq) {
                            project.setYqlid(bdcJsydzjdsyq.getQlid());
                        }
                    }
                } else {
                    //qjd 查找过渡土地证号赋给原产权证号，完善bdcxmrel
                    List<GdTdsyq> gdTdsyqList = getGdTdsyq(project);
                    StringBuilder ybdcqzhBuilder = new StringBuilder();
                    StringBuilder yproidBuilder = new StringBuilder();
                    StringBuilder yqlidBuilder = new StringBuilder();
                    if(CollectionUtils.isNotEmpty(gdTdsyqList)) {
                        for (GdTdsyq gdTdsyqTemp :gdTdsyqList) {
                            if (gdTdsyqTemp != null && StringUtils.isNotBlank(gdTdsyqTemp.getTdzh())) {
                                if(StringUtils.isBlank(ybdcqzhBuilder)){
                                    ybdcqzhBuilder.append(gdTdsyqTemp.getTdzh());
                                }else{
                                    ybdcqzhBuilder.append(",").append(gdTdsyqTemp.getTdzh());
                                }
                                if(StringUtils.isBlank(yproidBuilder)){
                                    yproidBuilder.append(gdTdsyqTemp.getProid());
                                }else{
                                    yproidBuilder.append(",").append(gdTdsyqTemp.getProid());
                                }
                                if(StringUtils.isBlank(yqlidBuilder)){
                                    yqlidBuilder.append(gdTdsyqTemp.getQlid());
                                }else{
                                    yqlidBuilder.append(",").append(gdTdsyqTemp.getQlid());
                                }
                            }
                        }
                    }
                    project.setYbdcqzh(ybdcqzhBuilder.toString());
                    project.setGdproid(yproidBuilder.toString());
                    project.setYqlid(yqlidBuilder.toString());
                    project.setXmly(Constants.XMLY_TDSP);
                }
            }
            List<InsertVo> list = creatProjectScdjService.initVoFromOldData(project);
            if (StringUtils.isNotBlank(project.getYbdcqzh())) {
                ((BdcXm) list.get(0)).setYbdcqzh(project.getYbdcqzh());
            }
            return list;
        }
        //创建登记
        createScDj(project, insertVoList);
        //创建抵押流程
        createDydj(project, insertVoList, dyMap);
        if (CollectionUtils.isNotEmpty(insertVoList)) {
            ((BdcXm) insertVoList.get(1)).setYbdcqzh(project.getYbdcqzh());
        }
        return insertVoList;
    }


    /**
     * @param project
     */
    public Map<String, Object> getDy(Project project) {
        Map<String, Object> map = new HashMap<String, Object>();
        String bdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
        BdcDyaq bdcDyaq = getBdcDyaq(bdcdyh);
        GdDy gdDy = getGdDy(bdcdyh);
        map.put(PARAMETER_BDCDYAQ, bdcDyaq);
        map.put("gdDy", gdDy);
        return map;
    }

    public void ceateQlrOfDy(String yqlid, String proid, List<InsertVo> list) {
        List<BdcQlr> bdcQlrList = null;
        for (InsertVo vo : list) {
            if (vo instanceof BdcXmRel && ((BdcXmRel) vo).getYproid() != null) {
                bdcQlrList = bdcQlrService.queryBdcQlrByProid(((BdcXmRel) vo).getYproid());
            }
        }
        if (CollectionUtils.isEmpty(bdcQlrList) && StringUtils.isNotBlank(yqlid)) {
            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(yqlid, Constants.QLRLX_QLR);
            if (CollectionUtils.isNotEmpty(gdQlrList)) {
                bdcQlrList = gdQlrService.readGdQlrs(gdQlrList);
            }
        }
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlr.setQlrid(UUIDGenerator.generate18());
                bdcQlr.setProid(proid);
            }
            list.addAll(bdcQlrList);
        }
    }

    /**
     * @param bdcdyh
     * @return
     */
    public BdcDyaq getBdcDyaq(String bdcdyh) {
        List<BdcDyaq> bdcDyaqList = null;
        BdcDyaq bdcDyaq = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            HashMap<String, Object> querymap = new HashMap<String, Object>();
            querymap.put("bdcDyh", bdcdyh);
            querymap.put("qszt", "1");
            bdcDyaqList = bdcDyaqService.queryBdcDyaq(querymap);
        }
        if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
            bdcDyaq = bdcDyaqList.get(0);
        }
        return bdcDyaq;
    }

    /**
     * @param bdcdyh
     * @return
     */
    public GdDy getGdDy( String bdcdyh) {
        GdDy gdDy = null;
        GdDy returnValue = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            List<String> qlids = gdTdService.getGdTdQlidByDjh(bdcdyh.substring(0, 19));
            if (CollectionUtils.isNotEmpty(qlids)) {
                for (String qlid : qlids) {
                    returnValue = gdTdService.getGddyqByQlid(qlid,Constants.GDQL_ISZX_WZX);
                    if (returnValue != null && returnValue.getIsjy() != null) {
                        gdDy = returnValue;
                        break;
                    }
                }
            }
        }
        return gdDy;
    }

    /**
     * @param project
     * @return
     */
    public QllxVo getQllxVo(Project project) {
        List<QllxVo> bdcJsydzjdsyqList = null;
        QllxVo qllxVo = null;
        String zdbdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
        if (StringUtils.isNotBlank(zdbdcdyh)) {
            bdcJsydzjdsyqList = qllxService.getQllxByBdcdyh(new BdcJsydzjdsyq(), zdbdcdyh);
        }
        if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
            qllxVo = bdcJsydzjdsyqList.get(0);
        }
        return qllxVo;
    }

    /**
     * 获取土地证
     *
     * @param project
     * @return
     */
    public List<GdTdsyq> getGdTdsyq(Project project) {
        List<GdTdsyq> gdTdsyqList = null;
        String zdbdcdyh = bdcdyService.getZdBdcdyh(project.getBdcdyh());
        if (StringUtils.isNotBlank(zdbdcdyh)) {
            String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(zdbdcdyh);
            if (StringUtils.isNotBlank(bdcdyid)) {
                gdTdsyqList = gdXmService.getGdTdsyqListByBdcdyid(bdcdyid);
            }
            if (CollectionUtils.isEmpty(gdTdsyqList) && zdbdcdyh.length() >= 19) {
                List<String> qlids = gdTdService.getGdTdQlidByDjh(zdbdcdyh.substring(0, 19));
                if (CollectionUtils.isNotEmpty(qlids)) {
                    GdTdsyq returnValue = null;
                    gdTdsyqList = new ArrayList<GdTdsyq>();
                    for (String qlid : qlids) {
                        returnValue = gdTdService.getGdTdsyqByQlid(qlid);
                        if (returnValue != null && returnValue.getIszx() != 1) {
                            gdTdsyqList.add(returnValue);
                        }
                    }
                }
            }
        }
        return gdTdsyqList;
    }

    /**
     * @param project
     * @return
     */
    public String getWorkflowProid(Project project) {
        String workflowProid = null;
        if (StringUtils.isNotBlank(project.getWorkflowProid())) {
            workflowProid = project.getWorkflowProid();
        } else if (StringUtils.isNotBlank(project.getProid())) {
            workflowProid = project.getProid();
        }
        return workflowProid;
    }

    /**
     * @param project
     * @param djlx
     * @param sqlx
     * @param qllx
     * @param djbid
     * @return
     */
    public Project setProject(Project project, String djlx,
                              String sqlx, String qllx, String djbid, String djsy, String proid) {
        if (StringUtils.isNotBlank(djbid)) {
            project.setDjbid(djbid);
        }
        if (StringUtils.isNotBlank(sqlx)) {
            project.setSqlx(sqlx);
        }
        if (StringUtils.isNotBlank(qllx)) {
            project.setQllx(qllx);
        }
        if (StringUtils.isNotBlank(djlx)) {
            project.setDjlx(djlx);
        }
        if (StringUtils.isNotBlank(djsy)) {
            project.setDjsy(djsy);
        }
        if (StringUtils.isNotBlank(proid)) {
            project.setProid(proid);
        }
        return project;
    }

    /**
     * @param bdcXmRel
     * @param qllxVo
     * @param gdTdsyq
     * @param gdDy
     * @return
     */
    public BdcXmRel setYqlInfo(BdcXmRel bdcXmRel, QllxVo qllxVo, GdTdsyq gdTdsyq, GdDy gdDy) {
        if (null != gdTdsyq) {
            bdcXmRel.setYqlid(gdTdsyq.getQlid());
            bdcXmRel.setYproid(gdTdsyq.getProid());
        }
        if (null != gdDy) {
            bdcXmRel.setYproid(gdDy.getProid());
            bdcXmRel.setYqlid(gdDy.getDyid());
        }
        if (null != qllxVo) {
            bdcXmRel.setYproid(qllxVo.getProid());
            bdcXmRel.setYqlid(qllxVo.getQlid());
        }
        if (null != gdTdsyq || null != gdDy) {
            bdcXmRel.setYdjxmly(Constants.XMLY_TDSP);
        }
        return bdcXmRel;
    }

    /**
     * @param gdTdsyq
     * @param qllxVo
     * @return
     */
    public String getYbdcqzh(GdTdsyq gdTdsyq, QllxVo qllxVo, List<InsertVo> list) {
        String ybdcqzh = null;
        if (null != gdTdsyq && StringUtils.isNotBlank(gdTdsyq.getTdzh())) {
            ybdcqzh = gdTdsyq.getTdzh();
        } else if (null != qllxVo) {
            List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(qllxVo.getProid());
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                ybdcqzh = bdcZsList.get(0).getBdcqzh();
            }
        }
        if (!StringUtils.isNotBlank(ybdcqzh))
            for (InsertVo vo : list) {
                if (vo instanceof BdcXmRel && ((BdcXmRel) vo).getYproid() != null) {
                    BdcXm bdcXm = bdcXmService.getBdcXmByProid(((BdcXmRel) vo).getYproid());
                    if (null != bdcXm) {
                        ybdcqzh = bdcXm.getYbdcqzh();
                    }
                }
            }

        return ybdcqzh;
    }

    /**
     * 创建首次登记
     *
     * @param project
     * @param insertVoList
     * @return
     */
    public List<InsertVo> createScDj(Project project, List<InsertVo> insertVoList) {
        List<GdTdsyq> gdTdsyq = getGdTdsyq(project);
        QllxVo qllxVo = getQllxVo(project);
        if (StringUtils.isNotBlank(project.getBdclx())) {
            project = setProject(project, Constants.DJLX_CSDJ_DM, Constants.SQLX_GYJSYD_GZW_DM, Constants.QLLX_GYTD_FWSUQ, project.getBdcdyh().substring(0, 19), null, null);
            List<InsertVo> list = creatProjectScdjService.initVoFromOldData(project);
            for (InsertVo vo : list) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;
                    // 创建收件信息
                    BdcSjxx bdcSjxx = createBdcSjxx(bdcXm, project);
                    if (bdcSjxx != null) {
                        insertVoList.add(bdcSjxx);
                    }
                    for (GdTdsyq gdTdsyqTemp :gdTdsyq) {
                        StringBuilder ybdcqzh = new StringBuilder();
                        if (StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh.append(getYbdcqzh(gdTdsyqTemp, qllxVo, list));
                        }else if (StringUtils.isNotBlank(ybdcqzh)) {
                            ybdcqzh.append(",").append(getYbdcqzh(gdTdsyqTemp, qllxVo, list));
                        }
                        project.setYbdcqzh(ybdcqzh.toString());
                    }
                }
                if (vo instanceof BdcXmRel) {
                    for (GdTdsyq gdTdsyqTemp :gdTdsyq) {
                        setYqlInfo((BdcXmRel) vo, qllxVo, gdTdsyqTemp, null);
                    }
                }
                if (vo instanceof BdcBdcdy) {
                    project.setBdcdyid(((BdcBdcdy) vo).getBdcdyid());
                }
                insertVoList.add(vo);
            }

        }
        return insertVoList;
    }

    /**
     * 创建抵押流程
     *
     * @param project
     * @param insertVoList
     * @return
     */
    public List<InsertVo> createDydj(Project project, List<InsertVo> insertVoList, Map<String, Object> dyMap) {
        GdDy gdDy = (GdDy) dyMap.get("gdDy");
        BdcDyaq bdcDyaq = (BdcDyaq) dyMap.get(PARAMETER_BDCDYAQ);
        List<GdTdsyq> gdTdsyq = getGdTdsyq(project);
        QllxVo qllxVo = getQllxVo(project);
        //创建抵押流程
        if (StringUtils.isNotBlank(project.getBdclx())&&StringUtils.isNotBlank(project.getProid())) {
            project = setProject(project, Constants.DJLX_DYDJ_DM, Constants.SQLX_FWDYBG_DM, Constants.QLLX_DYAQ, null, Constants.DJSY_DYAQ, UUIDGenerator.generate18());
            List<InsertVo> list1 = creatProjectDydjService.initVoFromOldData(project);
            for (InsertVo vo : list1) {
                if (vo instanceof BdcXm) {
                    BdcXm bdcXm = (BdcXm) vo;
                    // 创建收件信息
                    BdcSjxx bdcSjxx = createBdcSjxx(bdcXm, project);
                    if (bdcSjxx != null) {
                        insertVoList.add(bdcSjxx);
                    }
                    // 产生新证前抵押流程中原不动产权证号置空
                    bdcXm.setYbdcqzh(null);
                }
                if (vo instanceof BdcXmRel) {
                    setYqlInfo((BdcXmRel) vo, bdcDyaq, null, gdDy);
                    for (GdTdsyq gdTdsyqTemp :gdTdsyq) {
                        StringBuilder ybdcqzh = new StringBuilder();
                        if (StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh.append(getYbdcqzh(gdTdsyqTemp, qllxVo, list1));
                        }else if (StringUtils.isNotBlank(ybdcqzh)) {
                            ybdcqzh.append(",").append(getYbdcqzh(gdTdsyqTemp, qllxVo, list1));
                        }
                        project.setYbdcqzh(ybdcqzh.toString());
                    }
                }
                insertVoList.add(vo);
            }
            String qlid = null != gdDy ? gdDy.getDyid() : null;
            ceateQlrOfDy(qlid, project.getProid(), insertVoList);
        }
        return insertVoList;
    }

    /**
     * 根据工作流wiid，查找是否存在记录，若存在则删除对应记录
     *
     * @param project
     */
    public void deleteInfo(Project project) {
        if (StringUtils.isNotBlank(project.getWiid())) {
            //删除收件信息记录
            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByWiid(project.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                bdcSjdService.delBdcSjxxByWiid(project.getWiid());
            }
        }
    }

    /**
     * 创建bdc_sjxx
     *
     * @param bdcXm
     * @param project
     * @return
     */
    public BdcSjxx createBdcSjxx(BdcXm bdcXm, Project project) {
        BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid(bdcXm);
        if (bdcSjxx != null) {
            bdcSjxx.setSjxxid(UUIDGenerator.generate());
            bdcSjxx.setProid(project.getProid());
            bdcSjxx.setWiid(project.getWiid());
        }
        return bdcSjxx;
    }


    @Override
    @Transactional
    public void saveOrUpdateProjectData(final List<InsertVo> list) {
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        Set<String> zdzhhList = new HashSet<String>();
        Set<String> bdcdyhList = new HashSet<String>();
        Set<String> bdcTdList = new HashSet<String>();
        for (int i = 0; i < list.size(); i++) {
            InsertVo vo = list.get(i);
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                logger.error("CreateComplexScYtdDyServiceImpl.saveOrUpdateProjectData",e);
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
                voList = new LinkedList<InsertVo>();
                voList.add(vo);
                map.put(vo.getClass().getSimpleName(), voList);
            } else {
                voList = map.get(vo.getClass().getSimpleName());
                voList.add(vo);
            }
        }
        if (null != map && map.size() > 0) {
            List<InsertVo> finalList;
            for (Map.Entry entry : map.entrySet()) {
                finalList = (List<InsertVo>) entry.getValue();
                if (CollectionUtils.isNotEmpty(finalList)) {
                    entityMapper.insertBatchSelective(finalList);
                }
            }
        }
    }
}
