package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.service.BdczsBhService;
import cn.gtmap.estateplat.server.service.ProjectService;
import cn.gtmap.estateplat.server.service.TurnProjectService;
import cn.gtmap.estateplat.server.service.core.ProjectLifeManageService;
import cn.gtmap.estateplat.server.thread.BdcPreviewInfoForCreateBdczqhThread;
import cn.gtmap.estateplat.server.thread.BdcThreadEngine;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.WorkFlowXml;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.service.WorkFlowCoreService;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.WorkFlowException;
import com.gtis.plat.wf.WorkFlowInfo;
import com.gtis.plat.wf.model.ActivityModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.*;

/**
 * .
 * <p/>
 * 默认流程转发实现类  在不清楚业务逻辑时  请不要修改默认方法
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-18
 */

public class TurnProjectDefaultServiceImpl implements TurnProjectService {
    private static final Log log = LogFactory.getLog(TurnProjectDefaultServiceImpl.class);
    @Autowired
    public BdcXtConfigService bdcXtConfigService;
    @Autowired
    public BdcZsQlrRelService bdcZsQlrRelService;
    @Autowired
    public BdcXmZsRelService bdcXmZsRelService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private WorkFlowCoreService workFlowCoreService;
    @Autowired
    private SysWorkFlowDefineService workFlowDefineService;
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdczsBhService bdczsBhService;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcZjjzwxxService bdcZjjzwxxService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectLifeManageService projectLifeManageService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcThreadEngine bdcThreadEngine;

    public String userid = "";

    @Override
    public QllxVo saveQllxVo(BdcXm bdcXm) {
        QllxVo qllxVo = null;
        if (bdcXm != null) {
            qllxVo = qllxService.makeSureQllx(bdcXm);

            /* wcz 2016-04-24 获取原权利信息 */
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            BdcXmRel bdcXmRel = null;
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                bdcXmRel = bdcXmRelList.get(0);
            }

            //过度不走下面这块
            if (StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC) &&
                    (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFMMZYDJ_DM)
                            || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_FWDY_DM)
                            || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFSCKFSZC_DM)
                            || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM))
                    && bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                BdcXm ybdcxm = bdcXmService.getBdcXmByProid(bdcXmRel.getYproid());
                if (ybdcxm != null) {
                    QllxVo yqllxVo = qllxService.queryQllxVo(ybdcxm);

                    if ((yqllxVo instanceof BdcYg) && (qllxVo instanceof BdcFdcq)) {
                        List<BdcFdcq> bdcFdcqList = qllxService.getFdcqByBdcdyId(bdcXm.getBdcdyid());
                        BdcYg yg = (BdcYg) yqllxVo;
                        BdcFdcq fdcq = (BdcFdcq) qllxVo;
                        if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                            fdcq = bdcFdcqList.get(0);
                            fdcq.setQlid(UUIDGenerator.generate18());
                            fdcq.setProid(bdcXm.getProid());
                        } else {
                            fdcq.setQlid(UUIDGenerator.generate18());
                            fdcq.setProid(bdcXm.getProid());
                            fdcq.setYwh(bdcXm.getBh());
                            fdcq.setDbr(null);
                            fdcq.setDjsj(null);
                            fdcq.setQllx(bdcXm.getQllx());
                            fdcq.setBdcdyid(bdcXm.getBdcdyid());
                            fdcq.setSzc(yg.getSzc());
                            fdcq.setSzmyc(yg.getSzmyc());
                            fdcq.setZcs(yg.getZcs());
                            fdcq.setSzmyc(yg.getSzmyc());
                            fdcq.setFj(yg.getFj());
                            fdcq.setFwxz(yg.getFwxz());
                            fdcq.setGhyt(yg.getGhyt());
                            fdcq.setJzmj(yg.getJzmj());
                            fdcq.setScmj(yg.getJzmj());
                            fdcq.setGyqk(yg.getGyqk());
                            fdcq.setJyjg(yg.getJyje());
                            fdcq.setMjdw(yg.getMjdw());
                        }
                        qllxVo = fdcq;
                    }

                    if ((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFSCKFSZC_DM)) && (qllxVo instanceof BdcFdcq)) {
                        BdcFdcq fdcq = (BdcFdcq) yqllxVo;
                        fdcq.setQlid(UUIDGenerator.generate18());
                        fdcq.setQszt(Constants.QLLX_QSZT_LS);
                        fdcq.setFzlx(Constants.FZLX_FZS);
                        qllxVo = fdcq;
                    } else if ((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFXZBG_DM)) && (qllxVo instanceof BdcFdcq)) {
                        BdcFdcq fdcq = (BdcFdcq) yqllxVo;
                        fdcq.setQlid(UUIDGenerator.generate18());
                        fdcq.setQszt(Constants.QLLX_QSZT_LS);
                        fdcq.setFzlx(Constants.FZLX_FZM);
                        qllxVo = fdcq;
                    }
                    if ((yqllxVo instanceof BdcYg) && (qllxVo instanceof BdcDyaq)) {
                        BdcYg yg = (BdcYg) yqllxVo;
                        BdcDyaq dyaq = (BdcDyaq) qllxVo;
                        dyaq.setQlid(UUIDGenerator.generate18());
                        dyaq.setProid(bdcXm.getProid());
                        dyaq.setYwh(bdcXm.getBh());
                        dyaq.setDbr(null);
                        dyaq.setDjsj(null);
                        dyaq.setQllx(bdcXm.getQllx());
                        dyaq.setBdcdyid(bdcXm.getBdcdyid());
                        dyaq.setFj(yg.getFj());
                        dyaq.setGyqk(yg.getGyqk());
                        qllxVo = dyaq;
                    }
                }
            }
            /* wcz 2016-04-24 获取原权利信息 */

            String tipOfFsssInFj = null;
            QllxVo qllxVoTemp = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
            qllxVo = qllxService.getQllxVoFromBdcXm(bdcXm, bdcXmRel, qllxVo);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(bdcXm.getProid());
            if (bdcBdcdy != null) {
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("bdcdyh", bdcBdcdy.getBdcdyh());
                param.put("isfsss", "1");
                List<Map> djsjFwHsMap = djsjFwService.getDjsjFwHsByMap(param);
                if (CollectionUtils.isNotEmpty(djsjFwHsMap)) {
                    tipOfFsssInFj = AppConfig.getProperty("tipOfFsssInFj");
                }
            }

            if (qllxVo != null) {

                if (qllxVoTemp == null) {
                    qllxVo = qllxService.initQllxVoFromOntBdcXm(qllxVo, bdcXm.getProid());
                    //对于换证流程，要把原业务中的附记内容带入到新建业务的附记中
                    if (StringUtils.isNotBlank(bdcXm.getSqlx()) && bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYqlid()) && StringUtils.isNotBlank(bdcXmRel.getProid()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_HZ_DM)) {
                        String yqlid = bdcXmRel.getYqlid();
                        String yproid = bdcXmRel.getYproid();
                        List<GdTdsyq> gdTdsyqList = gdFwService.queryTdsyqByQlid(yqlid);
                        List<GdFwsyq> gdFwsyqList = gdFwService.getGdFwsyqListByGdproid(yproid, 0);
                        if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                            GdFwsyq gdFwsyq = gdFwsyqList.get(0);
                            qllxVo.setFj(gdFwsyq.getFj());
                        } else if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                            GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                            qllxVo.setFj(gdTdsyq.getFj());
                        }
                    }
                    if (StringUtils.isNotBlank(tipOfFsssInFj) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM)) {
                        qllxVo.setFj(StringUtils.isNotBlank(qllxVo.getFj()) ? qllxVo.getFj() + "\\n" + tipOfFsssInFj : tipOfFsssInFj);
                    }
                    if ((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM)) && (qllxVo instanceof BdcFdcq)) {
                        BdcFdcq fdcq = (BdcFdcq) qllxVo;
                        fdcq.setFzlx("2");
                        qllxVo = fdcq;
                    }
                    if (qllxVo instanceof BdcDyaq && StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
                        Boolean isExistDyaq = bdcDyaqService.validateDyaq(bdcXm.getBdcdyid());
                        if (!isExistDyaq) {
                            BdcDyaq dyaq = (BdcDyaq) qllxVo;
                            dyaq.setDysw(1);
                            qllxVo = dyaq;
                        }
                    }
                    entityMapper.insertSelective(qllxVo);
                } else {
                    qllxService.delQllxByproid(qllxVoTemp, qllxVoTemp.getProid());
                    qllxVo = qllxService.initQllxVoFromOntBdcXm(qllxVo, bdcXm.getProid());
                    if (StringUtils.isNotBlank(tipOfFsssInFj) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SPFGYSCDJ_DM)) {
                        qllxVo.setFj(StringUtils.isNotBlank(qllxVo.getFj()) ? qllxVo.getFj() + "\\n" + tipOfFsssInFj : tipOfFsssInFj);
                    }
                    entityMapper.insertSelective(qllxVo);
                }
                //以房屋为主的在建工程抵押 厂房的不动产信息插入在建建筑物信息表中 从已生成的spxx中取 对宗地的抵押进行注销
                if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_ZJJZWDY_FW_DM)) {
                    String bdcdyfwlx = "";
                    String zdDjh = "";
                    String zdBdcdyh = "";
                    String zdBdcdyid = "";
                    if (StringUtils.isNotBlank(bdcXm.getProid())) {
                        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                        if (bdcSpxx != null) {
                            bdcdyfwlx = bdcXmService.getBdcdyfwlxByYcbdcdyh(bdcSpxx.getBdcdyh());
                        }
                        if (bdcSpxx != null && StringUtils.isNotBlank(bdcdyfwlx) && StringUtils.equals(bdcdyfwlx, Constants.DJSJ_FWDZ_DM)) {
                            bdcZjjzwxxService.createZjjzwxx(bdcSpxx.getProid(), "1", bdcSpxx.getBdcdyh());
                            zdDjh = StringUtils.substring(bdcSpxx.getBdcdyh(), 0, 19);
                            zdBdcdyh = zdDjh + "W00000000";
                            zdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(zdBdcdyh);
                            HashMap map = new HashMap();
                            map.put("bdcdyid", zdBdcdyid);
                            List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                                BdcDyaq bdcDyaq = bdcDyaqList.get(0);
                                bdcDyaq.setQszt(Constants.QLLX_QSZT_HR);
                                entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                            }
                        }
                    }
                }
                if (StringUtils.equals(AppConfig.getProperty("dwdm"), Constants.DWDM_SZ) && CommonUtil.indexOfStrs(Constants.SQLX_ZXCFDJ_DM, bdcXm.getSqlx())
                        && qllxVo instanceof BdcCf) {
                    BdcCf bdcCf = (BdcCf) qllxVo;
                    bdcCf.setJfjg(bdcCf.getCfjg());
                    bdcCf.setJfywh(bdcXm.getBh());
                    entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                    qllxVo = bdcCf;
                }
            }
        }
        return qllxVo;
    }

    @Override
    public void saveYQllxVo(final BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            List<BdcXm> ybdcXmList = bdcXmService.getYbdcXmListByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(ybdcXmList)) {
                for (BdcXm ybdcXm : ybdcXmList) {
                    if (ybdcXm != null && StringUtils.isNotBlank(ybdcXm.getProid())) {
                        QllxVo qllxVo = null;
                        qllxVo = qllxService.makeSureQllx(ybdcXm);
                        qllxVo = qllxService.queryQllxVo(qllxVo, ybdcXm.getProid());
                        if (qllxVo != null) {
                            if (qllxVo instanceof BdcCf) {
                                BdcCf bdcCf = (BdcCf) qllxVo;
                                bdcCf.setJfywh(bdcXm.getBh());
                                bdcCf.setJfsj(new Date());
                                entityMapper.updateByPrimaryKeySelective(bdcCf);
                            } else if (qllxVo instanceof BdcDyaq) {
                                BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                                bdcDyaq.setZxdyywh(bdcXm.getBh());
                                bdcDyaq.setZxdyyy(bdcXm.getDjyy());
                                entityMapper.updateByPrimaryKeySelective(bdcDyaq);

                            } else if (qllxVo instanceof BdcYy) {
                                BdcYy bdcYy = (BdcYy) qllxVo;
                                bdcYy.setZxyyh(bdcXm.getBh());
                                bdcYy.setZxyyyy(bdcXm.getDjyy());
                                entityMapper.updateByPrimaryKeySelective(bdcYy);
                            }
                        }
                    }
                }
            }

            //过度走解封
            if ((StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_FWSP)
                    || StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_TDSP))
                    && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_JF)) {
                QllxVo qllxVo = null;
                qllxVo = qllxService.makeSureQllx(bdcXm);
                qllxVo = qllxService.queryQllxVo(qllxVo, bdcXm.getProid());
                if (qllxVo instanceof BdcCf) {
                    BdcCf bdcCf = (BdcCf) qllxVo;
                    bdcCf.setJfywh(bdcXm.getBh());
                    bdcCf.setJfsj(new Date());
                    entityMapper.updateByPrimaryKeySelective(bdcCf);
                }
            }
        }
    }

    @Override
    public List<InsertVo> createQllxVo(final BdcXm bdcXm) {
        List<InsertVo> qllsList = new ArrayList<InsertVo>();
        QllxVo qllxVo;
        if (bdcXm != null) {
            qllxVo = qllxService.makeSureQllx(bdcXm);
            QllxVo qllxVo1 = qllxService.getQllxVoFromBdcXm(bdcXm);
            if (qllxVo1 != null) {
                qllxService.delQllxByproid(qllxVo, bdcXm.getProid());


                if (qllxVo1 instanceof BdcFdcqDz) {
                    //  先选择房屋独幢数据，再选择多幢不插入权利登记信息bug
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(qllxVo1.getProid());
                    if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        bdcFdcqService.delBdcFdcqByProid(qllxVo1.getProid());
                    }

                    BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo1;
                    List<BdcFwfzxx> bdcFwfzxxList = bdcFdcqDz.getFwfzxxList();
                    if (CollectionUtils.isNotEmpty(bdcFwfzxxList)) {
                        qllsList.addAll(bdcFwfzxxList);
                    }
                    //zdd 防止插入报错
                    bdcFdcqDz.setFwfzxxList(null);
                    qllsList.add(bdcFdcqDz);
                } else if (qllxVo1 instanceof BdcFdcq) {
                    //            先选择房屋多幢数据，再选择多独幢不插入权利登记信息bug
                    BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(qllxVo1.getProid());
                    if (bdcFdcqDz != null) {
                        bdcFdcqDzService.delBdcFdcqDzByProid(qllxVo1.getProid());
                    }

                    qllsList.add(qllxVo1);
                } else {
                    qllsList.add(qllxVo1);
                }
            }
        }
        return qllsList;
    }

    @Override
    public void saveOrUpdateInsertVo(final List<InsertVo> dataList) {
        //存储分组后的List数据
        List<List<InsertVo>> newList = new ArrayList<List<InsertVo>>();
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        for (int i = 0; i < dataList.size(); i++) {
            InsertVo vo = dataList.get(i);
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                log.error("TurnProjectDefaultServiceImpl.saveOrUpdateInsertVo", e);
            }
            if (StringUtils.isNotBlank(id) && entityMapper.selectByPrimaryKey(vo.getClass(), id) != null) {
                entityMapper.updateByPrimaryKeySelective(vo);
                continue;
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


    @Override
    public List<BdcZs> saveBdcZs(BdcXm bdcXm, String previewZs) {
        List<BdcZs> list = new ArrayList<BdcZs>();
        List<BdcXm> bdcXmList;
        Boolean boolPreviewZs = false;
        if (StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs, "true")) {
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    List<BdcZs> bdcZsList;
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXmTemp.getProid());
                    //共有方式按份共有中存在共同共有
                    if (bdcQlrService.isAfgyContainGtgy(bdcQlrList)) {
                        bdcZsList = createBdcZs(bdcXmTemp, previewZs);
                        if (CollectionUtils.isNotEmpty(bdcZsList))
                            list.addAll(bdcZsList);
                    } else {
                        bdcZsList = bdcZsService.creatBdcqz(bdcXmTemp, bdcQlrList, this.userid, boolPreviewZs);
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            list.addAll(bdcZsList);
                        }

                        //zdd 生成权利人证书关系表
                        bdcZsQlrRelService.creatBdcZsQlrRel(bdcXmTemp, bdcZsList, bdcQlrList);
                        //zdd 生成项目证书关系表
                        bdcXmZsRelService.creatBdcXmZsRel(bdcZsList, bdcXmTemp.getProid());
                    }

                }
            }
        }
        return list;
    }

    @Override
    public List<BdcZs> saveBdcZs(BdcXm bdcXm) {
        return null;
    }


    public List<BdcZs> createBdcZs(BdcXm bdcXm, String previewZs) {
        Boolean boolPreviewZs = false;
        if (StringUtils.isNotBlank(previewZs) && StringUtils.equals(previewZs, "true")) {
            boolPreviewZs = Boolean.parseBoolean(previewZs);
        }
        List<BdcZs> bdcZsList = new ArrayList<BdcZs>();
        HashMap czrMap = new HashMap();
        czrMap.put("proid", bdcXm.getProid());
        czrMap.put("sfczr", Constants.CZR);
        czrMap.put("qlrlx", Constants.QLRLX_QLR);
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        List<BdcQlr> czrList = bdcQlrService.queryBdcQlrList(czrMap);
        if (CollectionUtils.isNotEmpty(czrList)) {
            //jyl 每个持证人都要生成一本证
            for (BdcQlr bdcQlr : czrList) {
                //jyl 为多抵一预留
                List<BdcXm> bdcXmList = new ArrayList<BdcXm>();
                //jyl 列出该权利人下的其余共有人，非持证人肯定是权利人，为了生成后续的BdcZsQlrRel
                List<BdcQlr> gyrList = null;
                if (StringUtils.isNotBlank(bdcQlr.getQygyr())) {
                    HashMap gyrMap = new HashMap();
                    gyrMap.put("proid", bdcQlr.getProid());
                    gyrMap.put("qlrlx", Constants.QLRLX_QLR);
                    gyrMap.put("qygyr", bdcQlr.getQygyr());
                    gyrMap.put("sfczr", Constants.SFCZR_NO);
                    gyrList = bdcQlrService.queryBdcQlrList(gyrMap);
                }
                BdcXm bdcXmTemp = bdcXmService.getBdcXmByProid(bdcQlr.getProid());
                bdcXmList.add(bdcXmTemp);
                BdcZs bdcZs = bdcZsService.creatBdcqzCrossSharingMode(bdcXmTemp, bdcQlr, this.userid, boolPreviewZs);
                List<BdcZs> bdcZsListTemp = new ArrayList<BdcZs>();
                if (bdcZs != null) {
                    bdcZsList.add(bdcZs);
                    bdcZsListTemp.add(bdcZs);

                    //生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRelArbitrary(bdcZs, bdcQlr, gyrList);
                    //生成项目证书关系表
                    bdcXmZsRelService.creatBdcXmZsRelArbitrary(bdcZs.getZsid(), bdcXmList);
                    //多个权利人，只有一个持证人的情况,生成权利人证书关系表
                    bdcZsQlrRelService.creatBdcZsQlrRelForOtherQlrExceptCzr(bdcZs, czrList, bdcQlrList, bdcXm);
                }
            }
        }
        return bdcZsList;
    }

    @Override
    public Project turnWfActivity(Project project) {
        if (project != null && StringUtils.isNotBlank(project.getTaskid()) && StringUtils.isNotBlank(project.getUserId()) && StringUtils.isNotBlank(project.getWorkFlowDefId())) {
            try {
                WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(project.getUserId(), project.getTaskid());
                // 处理活动
                if (info != null) {
                    List<ActivityModel> lstActivityModel = info.getTransInfo().getTranActivitys();
                    PfWorkFlowDefineVo pfWorkFlowDefineVo = workFlowDefineService.getWorkFlowDefine(project.getWorkFlowDefId());
                    // /如果splittype为xor
                    String xml = workFlowDefineService.getWorkFlowDefineXml(pfWorkFlowDefineVo);
                    WorkFlowXml workXml = new WorkFlowXml(xml);
                    workXml.setModifyDate(pfWorkFlowDefineVo.getModifyDate());
                    ActivityModel activityModel = workXml.getActivity(info.getSourceActivity().getActivityDefinitionId());
                    if (StringUtils.isBlank(activityModel.getSplitType()) || activityModel.getSplitType().equalsIgnoreCase("XOR")) {
                        int i = 0;
                        for (Iterator<ActivityModel> it = lstActivityModel.iterator(); it.hasNext(); ) {
                            if (i > 0) {
                                it.remove();
                            }
                            it.next();
                            i++;
                        }
                    }

                    //开始转发
                    workFlowCoreService.postWorkFlow(project.getUserId(), project.getTaskid(), info);
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(project.getProid());
                    if (pfWorkFlowInstanceVo != null) {
                        List<PfTaskVo> taskVoList = sysTaskService.getTaskListByInstance(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                        if (CollectionUtils.isNotEmpty(taskVoList)) {
                            boolean hasTaskAuthority = false;
                            for (PfTaskVo pfTaskVo : taskVoList) {
                                if (StringUtils.equals(project.getUserId(), pfTaskVo.getUserVo().getUserId())) {
                                    project.setTaskid(pfTaskVo.getTaskId());
                                    hasTaskAuthority = true;
                                    break;
                                }
                            }
                            if (!hasTaskAuthority)
                                project.setTaskid(null);
                        }
                    }

                }
            } catch (WorkFlowException wfex) {
                log.debug(wfex.getMessage());
                project.setMsg(wfex.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                project.setMsg(e.getMessage());
            }
        }
        return project;
    }

    @Override
    public void saveBdcqzh(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcZs> bdcZsList = bdcZsService.getPlZsByWiid(wiid);
            if (CollectionUtils.isNotEmpty(bdcZsList)) {
                for (BdcZs bdcZs : bdcZsList) {
                    synchronized (this) {
                        String proid = bdcXmZsRelService.getProidByZsid(bdcZs.getZsid());
                        if (StringUtils.isNotBlank(proid) && StringUtils.isBlank(bdcZs.getBdcqzh())) {
                            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
                            if (bdcXm != null) {
                                bdcZs = bdczsBhService.creatBdcqzBh(bdcXm, bdcZs, 0, null);
                            }
                            entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                        }
                    }

                }
            }
        }
    }

    @Override
    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public List<BdcZs> saveBdcZsArbitrary(final BdcXm bdcXm, String bdcqzFlag, String bdcqzmFlag) {
        return null;
    }

    @Override
    public void completeZsInfo(BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiidOrdeBy(bdcXm);
            // 变更和转移合并登记、转移和其他转移合并登记需进行排序
            String sqlxdm = "";
            if (StringUtils.isNotBlank(bdcXm.getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
            // 变更和转移合并登记、转移和其他转移合并登记需进行排序,优先生成主项目证号,需单独处理
            if (StringUtils.equals(sqlxdm, Constants.SQLX_BGZY_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_ZYQTZY_DM)) {
                bgZyAndZyQtzyCompleteZsInfo(bdcXm);
            } else {
                // 除变更和转移合并登记、转移和其他转移合并登记外其他流程走下面
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    List<BdcZs> bdcZsList = null;
                    List<BdcZs> dyaqBdcZsList = new ArrayList<BdcZs>();
                    List<BdcXm> dyaqXmList = new ArrayList<BdcXm>();
                    List<BdcZs> cqBdcZsList = new ArrayList<BdcZs>();
                    List<BdcXm> cqXmList = new ArrayList<BdcXm>();
                    Map zstypeMap = new HashMap();
                    if (bdcXmList.size() > 10) {
                        List<BdcPreviewInfoForCreateBdczqhThread> bdcPreviewInfoForCreateBdczqhThreadList = new ArrayList<BdcPreviewInfoForCreateBdczqhThread>();
                        for (BdcXm xm : bdcXmList) {
                            BdcPreviewInfoForCreateBdczqhThread bdcPreviewInfoForCreateBdczqhThread = new BdcPreviewInfoForCreateBdczqhThread(xm, bdcZsService);
                            bdcPreviewInfoForCreateBdczqhThreadList.add(bdcPreviewInfoForCreateBdczqhThread);
                        }

                        bdcThreadEngine.excuteThread(bdcPreviewInfoForCreateBdczqhThreadList);
                        for (BdcPreviewInfoForCreateBdczqhThread bdcPreviewInfoForCreateBdczqhThread : bdcPreviewInfoForCreateBdczqhThreadList) {
                            Map result = bdcPreviewInfoForCreateBdczqhThread.getResult();
                            if (MapUtils.isNotEmpty(result)) {
                                zstypeMap.putAll(result);
                            }
                        }
                    }
                    for (int i = 0; i < bdcXmList.size(); i++) {
                        BdcXm bdcXmTemp = bdcXmList.get(i);
                        bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmTemp.getProid());
                        //jiangganzhi 考虑预告合并登记
                        if (StringUtils.equals(bdcXmTemp.getQllx(), Constants.QLLX_YGDJ) && StringUtils.isNotBlank(bdcXmTemp.getProid())) {
                            BdcYg bdcYg = bdcYgService.getBdcYgByProid(bdcXmTemp.getProid());
                            if (bdcYg != null && StringUtils.isNotBlank(bdcYg.getYgdjzl()) && CollectionUtils.isNotEmpty(bdcZsList)) {
                                if (StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPF)) {
                                    cqXmList.add(bdcXmTemp);
                                    cqBdcZsList.add(bdcZsList.get(0));
                                } else if (StringUtils.equals(bdcYg.getYgdjzl(), Constants.YGDJZL_YGSPFDY)) {
                                    dyaqXmList.add(bdcXmTemp);
                                    dyaqBdcZsList.add(bdcZsList.get(0));
                                }
                            }
                        } else {
                            if (StringUtils.equals(bdcXmTemp.getQllx(), Constants.QLLX_DYAQ)) {
                                dyaqXmList.add(bdcXmTemp);
                            } else {
                                cqXmList.add(bdcXmTemp);
                            }
                        }
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                if (zstypeMap.containsKey(bdcXm.getProid()) && StringUtils.isNotBlank((CharSequence) zstypeMap.get(bdcXm.getProid()))) {
                                    bdcZsService.completeZsInfo(bdcXmTemp, bdcZs, this.userid, String.valueOf(zstypeMap.get(bdcXm.getProid())));
                                } else {
                                    bdcZsService.completeZsInfo(bdcXmTemp, bdcZs, this.userid, null);
                                }
                                if (StringUtils.isNotBlank(bdcZs.getZstype()) && StringUtils.equals(bdcZs.getZstype(), Constants.BDCQZM_BH_FONT)) {
                                    dyaqBdcZsList.add(bdcZs);
                                } else {
                                    cqBdcZsList.add(bdcZs);
                                }
                            }
                        }
                    }
                    bdcZsService.dealHbZsInfo(dyaqBdcZsList, cqBdcZsList, dyaqXmList, cqXmList);
                }
            }
        }
    }

    @Override
    public void creatBdcZsInfo(String proid, String userid) {
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(userid)) {
            //zdd  生成证书的逻辑根据业务来确定是生成一本还是多本   此处不在直接循环生成证书
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            if (bdcXm != null) {
                if (StringUtils.isNotBlank(bdcXm.getBdclx()) && (Constants.BDCLX_TDSL.equals(bdcXm.getBdclx()) || Constants.BDCLX_TDQT.equals(bdcXm.getBdclx())) && StringUtils.isNotBlank(bdcXm.getWiid())) {
                    projectLifeManageService.generateProjectZs(bdcXm.getWiid(), null);
                } else {
                    TurnProjectService turnProjectDefaultService = projectService.getTurnProjectService(bdcXm);
                    turnProjectDefaultService.setUserid(userid);
                    if (StringUtils.equals(bdcXm.getSftqsczs(), "1")) {
                        projectService.dbBdcXm(turnProjectDefaultService, bdcXm);
                    } else {
                        turnProjectDefaultService.saveBdcZs(bdcXm, null);
                    }
                }
            }
        }
    }

    private void bgZyAndZyQtzyCompleteZsInfo(BdcXm bdcXm) {
        Example example = new Example(BdcXm.class);
        example.createCriteria().andEqualTo("wiid", bdcXm.getWiid());
        example.setOrderByClause("cjsj");
        List<BdcXm> bdcXmList = entityMapper.selectByExample(BdcXm.class, example);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            StringBuilder ybdcqzh = new StringBuilder();
            for (int i = 0; i < bdcXmList.size(); i++) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmList.get(i).getProid());
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    for (BdcZs bdcZs : bdcZsList) {
                        bdcZsService.completeZsInfo(bdcXmList.get(i), bdcZs, this.userid, null);
                        // 变更新生成的证号为转移的原不动产权证号
                        if (i == 0) {
                            if (StringUtils.isNotBlank(ybdcqzh)) {
                                ybdcqzh.append(",").append(bdcZs.getBdcqzh());
                            } else {
                                ybdcqzh.append(bdcZs.getBdcqzh());
                            }
                        }
                    }
                }
                if (i == 1 && StringUtils.isNotBlank(ybdcqzh.toString())) {
                    // 修改项目表原不动产权证号
                    bdcXmList.get(i).setYbdcqzh(ybdcqzh.toString());
                    entityMapper.saveOrUpdate(bdcXmList.get(i), bdcXmList.get(i).getProid());
                    // 修改权利qlqtzk
                    List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXmList.get(i).getProid());
                    String qlqtzk = "";
                    if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                        BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                        qlqtzk = bdcZsService.getQlqtzkByReplaceBdcqzh(bdcFdcq.getQlqtzk(), ybdcqzh.toString());
                        if (StringUtils.isNotBlank(qlqtzk)) {
                            bdcFdcq.setQlqtzk(qlqtzk);
                            bdcFdcqService.saveBdcFdcq(bdcFdcq);
                        }
                    } else {
                        // 多幢
                        BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXmList.get(i).getProid());
                        if (bdcFdcqDz != null) {
                            qlqtzk = bdcZsService.getQlqtzkByReplaceBdcqzh(bdcFdcqDz.getQlqtzk(), ybdcqzh.toString());
                            if (StringUtils.isNotBlank(qlqtzk)) {
                                bdcFdcqDz.setQlqtzk(qlqtzk);
                                bdcFdcqDzService.saveBdcFdcqDz(bdcFdcqDz);
                            }
                        }
                    }
                    // 修改证书qlqtzk
                    List<BdcZs> bdcZsListTemp = bdcZsService.queryBdcZsByProid(bdcXmList.get(i).getProid());
                    if (CollectionUtils.isNotEmpty(bdcZsListTemp) && StringUtils.isNotBlank(qlqtzk)) {
                        for (BdcZs bdcZsTemp : bdcZsListTemp) {
                            bdcZsTemp.setQlqtzk(qlqtzk);
                            entityMapper.saveOrUpdate(bdcZsTemp, bdcZsTemp.getZsid());
                        }
                    }
                }
            }
        }
    }
}
