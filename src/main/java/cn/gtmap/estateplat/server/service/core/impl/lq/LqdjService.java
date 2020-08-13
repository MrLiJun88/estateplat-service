package cn.gtmap.estateplat.server.service.core.impl.lq;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.core.ProjectCustomService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/4/19
 * @description 林权登记服务
 */
@Service
public class LqdjService implements ProjectCustomService {
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private DjSjMapper djSjMapper;
    @Autowired
    BdcCfService bdcCfService;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private GdFwService gdFwService;

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String PARAMETER_FJ_YZX = "\n预转现";

    /**
     * @param xmxx 信息信息
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 初始化项目，根据不动产单元号或者证书（明）信息初始化BDC_XM,收件信息C_SJXX，收件材料C_SJCl，审批信息C_SPXX，
     * 不动产权利信息，不动产单元BDC_BDCDY、不动产登记簿BDC_、权利人信息
     */
    @Override
    public void initializeProject(Xmxx xmxx) {

    }

    /**
     * @param wiid
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 更新项目权属状态，包括本次项目权利状态、上一手权属状态（包括过渡数据权属状态）
     */
    @Override
    public void updateProjectQszt(String wiid) throws Exception {

    }

    /**
     * @param wiid 工作流项目ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/28
     * @description 撤回项目的权属状态，上一手权属状态（包括过渡数据权属状态）,用于项目从登簿环节撤回、退回或删除
     */
    @Override
    public void revertProjectQszt(String wiid) {

    }

    @Override
    public void deleteProjectQlxx(String wiid) {

    }

    /**
     * @param bdcXm 不动产登记项目
     * @description 修改过渡数据的权属状态
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     */
    protected void changeYgQszt(BdcXm bdcXm) {
        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
            if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                List<BdcYg> list = bdcYgService.getBdcYgList(bdcBdcdy.getBdcdyh(), Constants.QLLX_QSZT_XS.toString());
                if (CollectionUtils.isNotEmpty(list)) {
                    for (BdcYg bdcYg : list) {
                        String ygdjProid = bdcYg.getProid();
                        if (StringUtils.equals(ygdjProid, bdcXm.getProid())) {
                            continue;
                        }

                        List<BdcQlr> ygdjqlrList = bdcQlrService.queryBdcQlrByProid(ygdjProid);
                        List<HashMap<String, String>> ygdjQlrs = new ArrayList<HashMap<String, String>>();
                        if (CollectionUtils.isNotEmpty(ygdjqlrList)) {
                            for (BdcQlr ygdjqlr : ygdjqlrList) {
                                HashMap<String, String> qlrmap = new HashMap<String, String>();
                                qlrmap.put("qlr", ygdjqlr.getQlrmc());
                                qlrmap.put("zjh", ygdjqlr.getQlrzjh());
                                ygdjQlrs.add(qlrmap);
                            }
                        }
                        List<BdcQlr> qlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                        if (CollectionUtils.isNotEmpty(qlrList)) {
                            for (BdcQlr bdcQlr : qlrList) {
                                HashMap<String, String> qlrmap = new HashMap<String, String>();
                                qlrmap.put("qlr", bdcQlr.getQlrmc());
                                qlrmap.put("zjh", bdcQlr.getQlrzjh());
                                if (ygdjQlrs.contains(qlrmap)) {
                                    //zdd 预告登记权利人信息与当前权利人信息一致   注销原预告登记
                                    bdcYg.setQszt(Constants.QLLX_QSZT_HR);
                                    String fj = bdcYg.getFj();
                                    if (StringUtils.isNotBlank(fj)) {
                                        //此方法如果多权利人附记预转现可能会多次赋值
                                        if (!(StringUtils.indexOf(fj, "预转现") > -1)) {
                                            if (bdcYg.getDjsj() != null)
                                                bdcYg.setFj(fj + "\n" + CalendarUtil.formateToStrChinaYMDDate(bdcYg.getDjsj()) + PARAMETER_FJ_YZX);
                                            else
                                                bdcYg.setFj(fj + PARAMETER_FJ_YZX);
                                        }
                                    } else {
                                        if (bdcYg.getDjsj() != null)
                                            bdcYg.setFj(CalendarUtil.formateToStrChinaYMDDate(bdcYg.getDjsj()) + PARAMETER_FJ_YZX);
                                        else
                                            bdcYg.setFj(PARAMETER_FJ_YZX);
                                    }
                                    entityMapper.updateByPrimaryKeySelective(bdcYg);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param bdcXmRel
     * @param qszt
     * @description 修改过渡数据的权属状态
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     */
    protected void changeGdsjQszt(BdcXmRel bdcXmRel, Integer qszt) {
        if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && !bdcXmRel.getYdjxmly().equals("1")
                &&StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
            gdXmService.updateGdQszt(bdcXmRel.getYqlid(), qszt);
        }
    }


    /**
     * @param wiid 工作流项目ID
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 还原原权利类型状态
     */
    @Override
    public void revertYqlxx(String wiid) {

    }

    /**
     * @param wiid 工作流项目ID
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/7/28
     * @description 生成证书信息，包括附记、权利其他状况等
     */
    @Override
    public void generateProjectZs(String wiid) {

    }

    /**
     * @return
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @version 1.0, 2016/8/10
     * @description 项目业务编号
     */
    @Override
    public String getProjectCode() {
        return null;
    }

    /**
     * @param qlid 权利ID
     * @param qszt 权属状态
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 更新过度数据权属状态
     */
    protected void updateGdQszt(String qlid, Integer qszt) {
        if (StringUtils.isNotBlank(qlid) && qszt != null) {

            //林权
            //待补

            //抵押
            Example dyExample = new Example(GdDy.class);
            dyExample.createCriteria().andEqualTo("dyid", qlid);
            List<GdDy> gdDyList = entityMapper.selectByExample(dyExample);
            if (CollectionUtils.isNotEmpty(gdDyList)) {
                for (GdDy gdDy : gdDyList) {
                    gdDy.setIsjy(qszt);
                    entityMapper.saveOrUpdate(gdDy, gdDy.getDyid());
                }
            }
            //查封
            Example cfExample = new Example(GdCf.class);
            cfExample.createCriteria().andEqualTo("cfid", qlid);
            List<GdCf> gdCfList = entityMapper.selectByExample(cfExample);
            if (CollectionUtils.isNotEmpty(gdCfList)) {
                for (GdCf gdCf : gdCfList) {
                    gdCf.setIsjf(qszt);
                    entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                }
            }
            //预告
            Example ygExample = new Example(GdYg.class);
            ygExample.createCriteria().andEqualTo("ygid", qlid);
            List<GdYg> gdYgList = entityMapper.selectByExample(ygExample);
            if (CollectionUtils.isNotEmpty(gdYgList)) {
                for (GdYg gdYg : gdYgList) {
                    gdYg.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdYg, gdYg.getYgid());
                }
            }
            //异议
            Example yyExample = new Example(GdYy.class);
            yyExample.createCriteria().andEqualTo("yyid", qlid);
            List<GdYy> gdYyList = entityMapper.selectByExample(yyExample);
            if (CollectionUtils.isNotEmpty(gdYyList)) {
                for (GdYy gdYy : gdYyList) {
                    gdYy.setIszx(qszt);
                    entityMapper.saveOrUpdate(gdYy, gdYy.getYyid());
                    gdFwService.changeGdqlztByQlid(gdYy.getYyid(),qszt.toString());
                }
            }
        }
    }

    /**
     * @param bdcXmRel 不动产项目关系
     * @param qszt     权属状态
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 还原上一手权利信息（包括上一手过渡数据），包括权利状态等，主要用于删除项目，撤销或退回项目
     */
    protected void revertGdsjQszt(BdcXmRel bdcXmRel, Integer qszt) {
        if (StringUtils.isNotBlank(bdcXmRel.getYproid()) && !bdcXmRel.getYdjxmly().equals("1")
                &&StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
            updateGdQszt(bdcXmRel.getYqlid(), qszt);
        }
    }


    /**
     * @param list 项目list
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 保存更新初始化项目，包括BDC_XM,收件信息BDC_SJXX，收件材料BDC_SJCl，审批信息BDC_SPXX，
     * 不动产权利信息，不动产单元BDC_BDCDY、不动产登记簿BDC_BDCDJB、权利人信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateProjectDate(List<InsertVo> list) {
        for (int i = 0; i < list.size(); i++) {
            InsertVo vo = list.get(i);
            if (vo != null) {
                Method method1 = AnnotationsUtils.getAnnotationsName(vo);
                String id = null;
                try {
                    if (method1.invoke(vo) != null)
                        id = method1.invoke(vo).toString();
                } catch (IllegalAccessException e) {
                    logger.error("LqdjService.saveOrUpdateProjectDate",e);
                } catch (InvocationTargetException e) {
                    logger.error("LqdjService.saveOrUpdateProjectDate",e);
                }
                if (StringUtils.isNotBlank(id)) {
                    entityMapper.saveOrUpdate(vo, id);
                }
            }
        }
    }
    /**
     * @param project 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始化不动产登记簿
     */
    protected BdcBdcdjb initBdcdjb(Project project) {
        BdcBdcdjb bdcdjb = null;
        // 如果宗地宗海号为空  则不生成
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            // 将项目信息读取到不动产登记薄
            bdcdjb = bdcDjbService.getBdcdjbFromProject(project, bdcdjb);
            // 将宗地信息读取到不动产登记薄
            bdcdjb = bdcDjbService.getBdcdjbFromNydZdxx(getDjsjNydDcbByBdcdyh(project.getBdcdyh()), bdcdjb);
            // 将林权信息读取到不动产登记薄
            bdcdjb = bdcDjbService.getBdcdjbFromLqxx(getDjsjLqxx(project.getDjId()), bdcdjb);
        }
        return bdcdjb;
    }

    /**
     * @param djsjNydDcbList 地籍农用地调查表
     * @param project        项目信息
     * @param qllx           权利类型
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始化不动产土地属性表
     */
    protected BdcTd intBdcTd(List<DjsjNydDcb> djsjNydDcbList, Project project, String qllx) {
        BdcTd bdcTd = new BdcTd();
        if (StringUtils.isBlank(bdcTd.getTdid()))
            bdcTd.setTdid(UUIDGenerator.generate18());
        if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
            DjsjNydDcb djsjQszdDcb = djsjNydDcbList.get(0);
            if(djsjQszdDcb!=null) {
                if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                    bdcTd.setZdmj(djsjQszdDcb.getFzmj());
                }else {
                    bdcTd.setZdmj(djsjQszdDcb.getScmj());
                }
                bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
                bdcTd.setYt(djsjQszdDcb.getTdyt());
                bdcTd.setQllx(djsjQszdDcb.getQsxz());
                bdcTd.setZl(djsjQszdDcb.getTdzl());
            }
            bdcTd.setZdzhh(project.getZdzhh());
            //地籍没有单位  默认就是亩
            if (StringUtils.isNotBlank(qllx)) {
                HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(qllx);
                if (dwMap != null && StringUtils.isNotBlank(dwMap.get(qllx)))
                    bdcTd.setMjdw(dwMap.get(qllx));
                else
                    bdcTd.setMjdw(Constants.DW_M);
            } else
                bdcTd.setMjdw(Constants.DW_M);
        }
        return bdcTd;
    }


    /**
     * @param project 项目信息
     * @param bdcSpxx 不动产审批表信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始化审批信息
     */
    protected BdcSpxx initBdcSpxx(Project project, BdcSpxx bdcSpxx) {
        //如果不动产单元号为空  则不生成
        if (project!=null&&StringUtils.isNotBlank(project.getProid())) {
            if (bdcSpxx == null)
                bdcSpxx = new BdcSpxx();
            if (StringUtils.isBlank(bdcSpxx.getSpxxid()))
                bdcSpxx.setSpxxid(UUIDGenerator.generate18());
            //读取project信息到审批信息
            bdcSpxx = bdcSpxxService.getBdcSpxxFromProject(project, bdcSpxx);
            //读取林权信息到审批信息
            if(StringUtils.isNotBlank(project.getDjId()))
            bdcSpxx = bdcSpxxService.getBdcSpxxFromLq(getDjsjLqxx(project.getDjId()), bdcSpxx);
            //读取过渡信息到审批信息
            if (StringUtils.isNotBlank(project.getGdproid()) && StringUtils.isNotBlank(project.getYqlid()))
                bdcSpxx = bdcSpxxService.getBdcSpxxFromGdxm(project.getGdproid(), project.getYqlid(), project.getXmly(), bdcSpxx);
            //读取原项目信息到审批信息
            if (StringUtils.isNotBlank(project.getYxmid()))
                bdcSpxx = bdcSpxxService.getBdcSpxxFromYProject(project, bdcSpxx);
            //若审批信息中有mc,将mc转为dm
            bdcSpxx = bdcZdGlService.changeToDm(bdcSpxx);
        }
        return bdcSpxx;
    }

    /**
     * @param project 项目信息
     * @param bdcdjb  不动产登记簿信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 初始不动产单元
     */
    protected BdcBdcdy initBdcdy(Project project, BdcBdcdjb bdcdjb) {
        BdcBdcdy bdcdy = null;
        //zdd 如果不动产单元号为空  则不生成
        if (project!=null&&StringUtils.isNotBlank(project.getBdcdyh())) {
            bdcdy = new BdcBdcdy();
            if (bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid()))
                bdcdy.setDjbid(bdcdjb.getDjbid());
            //读取project信息到不动产单元
            bdcdy = bdcdyService.getBdcdyFromProject(project, bdcdy);
            //读取原项目信息到不动产单元
            if (StringUtils.isNotBlank(project.getYxmid())) {
                bdcdy = bdcdyService.getBdcdyFromYProject(project, bdcdy);
            }
            BdcBdcdy tempBdcdy = bdcdyService.queryBdcdyByBdcdyh(bdcdy.getBdcdyh());
            if (tempBdcdy != null) {
                bdcdy.setBdcdyid(tempBdcdy.getBdcdyid());
            } else {
                //当没有该不动产单元号的单元信息时 如果project中的bdcdyid不为空 赋值
                if (StringUtils.isNotBlank(project.getBdcdyid())) {
                    bdcdy.setBdcdyid(project.getBdcdyid());
                } else {
                    bdcdy.setBdcdyid(UUIDGenerator.generate18());
                }
            }
        }
        return bdcdy;
    }

    /**
     * @param project  项目信息
     * @param djsjLqxx 地籍数据林权信息
     * @param qlrlx    权利人类型
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 根据地籍数据初始化权利人
     */
    protected List<BdcQlr> initBdcQlrFromDjsj(Project project, DjsjLqxx djsjLqxx, String qlrlx) {
        List<BdcQlr> bdcQlrList = null;
        if (djsjLqxx != null) {
            //读取林权信息到权利人信息
            bdcQlrList = initBdcQlrFromLq(project, djsjLqxx, qlrlx);
        }
        return bdcQlrList;
    }

    /**
     * @param project  项目信息
     * @param djsjLqxx 地籍数据林权信息
     * @param qlrlx    权利人类型
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 读取林权信息到权利人信息
     */
    private List<BdcQlr> initBdcQlrFromLq(Project project, DjsjLqxx djsjLqxx, String qlrlx) {
        List<BdcQlr> bdcQlrList;
        BdcQlr bdcQlr = new BdcQlr();
        //读取project信息到权利人信息
        bdcQlr = bdcQlrService.getBdcQlrFromProject(project, bdcQlr);
        //读取林权信息到权利人信息
        bdcQlrList = bdcQlrService.getBdcQlrFromLq(djsjLqxx, bdcQlr);
        bdcQlr.setQlrlx(qlrlx);
        if (StringUtils.isNotBlank(bdcQlr.getQlrmc()))
            bdcQlrList.add(bdcQlr);
        return bdcQlrList;
    }

    /**
     * @param project 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 获取原不动产权利人信息
     */
    protected List<BdcQlr> getYbdcQlrList(Project project) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(project.getYxmid()))
            bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getYxmid());
        //如果地籍有权利人信息  则以地籍为准
        if (CollectionUtils.isEmpty(bdcQlrList)&&StringUtils.isNotBlank(project.getYqlid()) && !project.getXmly().equals("1")) {
            //当项目来源不为不动产登记项目时   读取过渡数据权利人
            List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid(project.getYqlid(), Constants.QLRLX_QLR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        }
        //將不动产权利人里从过渡数据读取过来的身份证件种类数据转化为字典数据
        bdcQlrList = bdcQlrService.changeGdqlrYsjToZdsj(bdcQlrList);
        return bdcQlrList;
    }

    /**
     * @param project 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 获取原不动产义务人信息
     */
    protected List<BdcQlr> getYbdcYwrList(Project project) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(project.getYxmid()))
            bdcQlrList = bdcQlrService.queryBdcYwrByProid(project.getYxmid());
        //如果地籍有权利人信息  则以地籍为准
        if (CollectionUtils.isEmpty(bdcQlrList)
                &&StringUtils.isNotBlank(project.getYqlid()) && !project.getXmly().equals("1")) {
            //当项目来源不为不动产登记项目时   读取过渡数据权利人
            List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid(project.getYqlid(), Constants.QLRLX_YWR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        }
        //將不动产权利人里从过渡数据读取过来的身份证件种类数据转化为字典数据
        bdcQlrList = bdcQlrService.changeGdqlrYsjToZdsj(bdcQlrList);
        return bdcQlrList;
    }

    /**
     * @param project 项目信息
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @description 通过project获取过度的权利人和义务人，过程匹配权利人和义务人不变
     */
    public List<BdcQlr> keepQlrByGcPp(Project project) {
        List<BdcQlr> ybdcQlrList = null;
        if (StringUtils.equals(project.getMsg(), "gl")) {
            //这个是为了关联项目的时候填了权利人，则不要取权利人，若没有,则取过渡的权利人
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isEmpty(bdcQlrList))
                //获取原不动产权利人信息
                ybdcQlrList = getYbdcQlrList(project);
        } else {
            ybdcQlrList = getYbdcQlrList(project);
        }
        List<BdcQlr> ybdcYwrList = getYbdcYwrList(project);
        List<BdcQlr> list = new ArrayList<BdcQlr>();
        //权利人变权利人
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            //获取项目的权利人
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcYwr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                //将原项目的权利人转为权利人,同时根据当前项目是否有相同的权利人来确定qlrid 值
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        //义务人变义务人
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            //获取项目的义务人
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            for (BdcQlr bdcQlr : ybdcYwrList) {
                //将原项目的权利人转为义务人
                bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        return list;
    }

    protected final DjsjLqxx getDjsjLqxx(final String djId){
        return bdcDjsjService.getDjsjLqxx(djId);
    }

    protected final List<DjsjNydDcb> getDjsjNydDcbByBdcdyh(final String bdcdyh){
        if (StringUtils.isNotBlank(bdcdyh) && bdcdyh.length() > 19) {
            String djh = StringUtils.substring(bdcdyh, 0, 19);
            //通过地籍号读取农用地信息
            return djSjMapper.getDjsjNydDcbByDjh(djh);
        }
        return null;
    }
}
