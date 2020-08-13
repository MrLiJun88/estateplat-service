package cn.gtmap.estateplat.server.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.DjSjMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.model.InitVoFromParm;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.AnnotationsUtils;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.service.WorkFlowCoreService;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.WorkFlowInfo;
import com.gtis.web.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * .
 * <p/>
 * 默认创建项目方法
 * **********************在不清楚业务逻辑时  请不要修改默认方法************************
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-3-16
 */

public class CreatProjectDefaultServiceImpl implements CreatProjectService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private WorkFlowCoreService workFlowCoreService;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Resource(name = "fileCenterNodeServiceImpl")
    private NodeService fileCenterNodeServiceImpl;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private BdcdjbService bdcDjbService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private DjSjMapper djSjMapper;
    @Autowired
    private DjsjFwService djsjFwService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcTdService bdcTdService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private BdcSjdService bdcSjdService;
    @Resource(name = "creatProjectCfdjServiceImpl")
    CreatProjectCfdjServiceImpl creatProjectCfdjService;
    @Resource(name = "creatProjectDyBgdjServiceImpl")
    CreatProjectDyBgdjServiceImpl creatProjectDyBgdjService;
    @Resource(name = "creatProjectYgBgdjServiceImpl")
    CreatProjectYgBgdjServiceImpl creatProjectYgBgdjService;
    @Autowired
    BdcFdcqDzService bdcFdcqDzService;

    protected final Logger logger = LoggerFactory.getLogger(CreatProjectDefaultServiceImpl.class);

    /**
     * zdd 对于那些需要读取原项目信息的流程
     *
     * @param bdcXm
     * @param ybdcxm
     * @return
     */
    public BdcXm readYbdcxm(BdcXm bdcXm, BdcXm ybdcxm) {
        if (ybdcxm != null) {
            if (StringUtils.isBlank(bdcXm.getQllx()) && StringUtils.isNotBlank(ybdcxm.getQllx())) {
                bdcXm.setQllx(ybdcxm.getQllx());
            }
            if (StringUtils.isBlank(bdcXm.getDjsy()) && StringUtils.isNotBlank(ybdcxm.getDjsy())) {
                bdcXm.setDjsy(ybdcxm.getDjsy());
            }
            if (StringUtils.isBlank(bdcXm.getBdcdyid()) && StringUtils.isNotBlank(ybdcxm.getBdcdyid())) {
                bdcXm.setBdcdyid(ybdcxm.getBdcdyid());
            }
        }
        return bdcXm;

    }

    /**
     * zdd 根据原项目ID或者房产证ID 获取原权利人信息
     * 如果有原项目信息  则不再读取房产证权利人
     *
     * @param project
     * @return
     */
    protected List<BdcQlr> getYbdcQlrList(Project project) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(project.getYxmid())) {
            bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getYxmid());
            bdcQlrList = handleQlrForZjgcdyzxdy(project, bdcQlrList);
        }
        //zdd 如果地籍有权利人信息  则以地籍为准
        if (CollectionUtils.isEmpty(bdcQlrList) && StringUtils.isNotBlank(project.getYqlid()) && !project.getXmly().equals("1")) {
            //zdd 当项目来源不为不动产登记项目时   读取过渡数据权利人
            List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid(project.getYqlid(), Constants.QLRLX_QLR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        }
        bdcQlrList = bdcQlrService.changeGdqlrYsjToZdsj(bdcQlrList);
        return bdcQlrList;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 处理在建工程抵押转现抵押权利人继承逻辑，本手业务是首次，上手是在建抵押，权利人继承义务人信息
     */
    private List<BdcQlr> handleQlrForZjgcdyzxdy(Project project, List<BdcQlr> bdcQlrList) {
        if (StringUtils.equals(Constants.SQLX_SPFGYSCDJ_DM, project.getSqlx()) || StringUtils.equals(project.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) {
            List<BdcQlr> yBdcQlrList = new ArrayList<BdcQlr>();
            QllxVo yqllxVo = qllxService.getQllxVoByProid(project.getYxmid());
            if (yqllxVo instanceof BdcDyaq) {
                yBdcQlrList = bdcQlrService.queryBdcYwrByProid(project.getYxmid());
            }
            if (CollectionUtils.isNotEmpty(yBdcQlrList)) {
                bdcQlrList = new ArrayList<BdcQlr>();
                for (BdcQlr bdcQlr : yBdcQlrList) {
                    bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                    bdcQlrList.add(bdcQlr);
                }
            }
        }
        return bdcQlrList;
    }


    /**
     * zdd 根据原项目ID或者房产证ID 获取原义务人信息
     * 如果有原项目信息  则不再读取房产证义务人
     *
     * @param project
     * @return
     */
    protected List<BdcQlr> getYbdcYwrList(Project project) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(project.getYxmid())) {
            bdcQlrList = bdcQlrService.queryBdcYwrByProid(project.getYxmid());
        }
        //zdd 如果地籍有权利人信息  则以地籍为准
        if (CollectionUtils.isEmpty(bdcQlrList) && StringUtils.isNotBlank(project.getYqlid()) && !project.getXmly().equals("1")) {
            //zdd 当项目来源不为不动产登记项目时   读取过渡数据权利人
            List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid(project.getYqlid(), Constants.QLRLX_YWR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        }
        bdcQlrList = bdcQlrService.changeGdqlrYsjToZdsj(bdcQlrList);
        return bdcQlrList;
    }

    /**
     * ZX 根据原项目ID或者房产证ID 获取原借款人信息
     * 如果有原项目信息  则不再读取房产证义务人
     *
     * @param project
     * @return
     */
    protected List<BdcQlr> getYbdcJkrList(Project project) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(project.getYxmid())) {
            bdcQlrList = bdcQlrService.queryBdcJkrByProid(project.getYxmid());
        }
        //zdd 如果地籍有权利人信息  则以地籍为准
        if (CollectionUtils.isEmpty(bdcQlrList) && StringUtils.isNotBlank(project.getYqlid()) && !project.getXmly().equals("1")) {
            //zdd 当项目来源不为不动产登记项目时   读取过渡数据权利人
            List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid(project.getYqlid(), Constants.QLRLX_JKR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        }
        bdcQlrList = bdcQlrService.changeGdqlrYsjToZdsj(bdcQlrList);
        return bdcQlrList;
    }

    @Override
    public InitVoFromParm getDjxx(final Xmxx xmxx) {
        DjsjFwxx djsjFwxx = null;
        DjsjLqxx djsjLqxx = null;
        DjsjZdxx djsjZdxx = null;
        DjsjZhxx djsjZhxx = null;
        DjsjQszdDcb djsjQszdDcb = null;
        DjsjCbzdDcb cbzdDcb = null;
        DjsjNydDcb djsjNydDcb = null;
        List<DjsjZdxx> djsjZdxxList = null;
        List<DjsjZhxx> djsjZhxxList = null;
        List<DjsjFwxx> djsjFwQlrList = null;

        List<DjsjQszdDcb> djsjQszdDcbList = null;
        List<DjsjNydDcb> djsjNydDcbList = null;
        InitVoFromParm initVoFromParm = new InitVoFromParm();
        if (xmxx instanceof Project) {
            Project project = (Project) xmxx;
            if (StringUtils.isBlank(project.getDjId()) && StringUtils.isNotBlank(project.getBdcdyh())) {
                project.setDjId(bdcDjsjService.getDjidByBdcdyh(project.getBdcdyh(), project.getBdclx()));
            }
            if (StringUtils.isNotBlank(project.getDjId()) && Constants.BDCLX_TDFW.equals(project.getBdclx())) {
                djsjFwxx = djsjFwService.getDjsjFwxx(project.getDjId());
                if (djsjFwxx != null) {
                    if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && StringUtils.length(djsjFwxx.getBdcdyh()) > 19) {
                        project.setZdzhh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
                    }
                    if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                        djsjFwQlrList = bdcDjsjService.getDjsjFwQlr(djsjFwxx.getId());
                    }
                }
                /**
                 * zzhw
                 * 房屋信息中需要查询土地信息继承过来（tdzl、fzmj）
                 */
                if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && StringUtils.length(djsjFwxx.getBdcdyh()) > 19) {
                    djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
                }
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    djsjZdxx = djsjZdxxList.get(0);
                }
            }

            if (StringUtils.isNotBlank(project.getDjId()) && Constants.BDCLX_TDSL.equals(project.getBdclx())) {
                djsjLqxx = bdcDjsjService.getDjsjLqxx(project.getDjId());
            }
            if (Constants.BDCLX_HY.equals(project.getBdclx())) {
                if (StringUtils.isNotBlank(project.getBdcdyh()) && StringUtils.length(project.getBdcdyh()) > 19) {
                    djsjZhxxList = bdcDjsjService.getDjsjZhxxForDjh(StringUtils.substring(project.getBdcdyh(), 0, 19));
                }
                if (CollectionUtils.isNotEmpty(djsjZhxxList)) {
                    djsjZhxx = djsjZhxxList.get(0);
                }
                if (djsjZhxx == null && StringUtils.isNotBlank(project.getDjId())) {
                    djsjZhxx = bdcDjsjService.getDjsjZhxx(project.getDjId());
                }
            }

            if (StringUtils.isNotBlank(project.getDjId()) && (Constants.BDCLX_TD.equals(project.getBdclx()) || Constants.BDCLX_TDQT.equals(project.getBdclx()))) {
                cbzdDcb = djSjMapper.getDjsjCbzdDcbByDjid(project.getDjId());
                if (cbzdDcb != null && StringUtils.isNotBlank(cbzdDcb.getDjh())) {
                    djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(cbzdDcb.getDjh());
                }
                if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
                    djsjNydDcb = djsjNydDcbList.get(0);
                }
            }

            if (StringUtils.isNotBlank(project.getDjId()) && StringUtils.isNotBlank(project.getBdclx()) && project.getBdclx().indexOf(Constants.BDCLX_TD) > -1) {
                djsjQszdDcbList = bdcDjsjService.getDjsjQszdDcb(project.getDjId());
                if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
                    djsjQszdDcb = djsjQszdDcbList.get(0);
                }
                djsjZdxxList = bdcDjsjService.getDjsjZdxx(project.getDjId());
                //zwq 取农用地调查表信息
                if (CollectionUtils.isEmpty(djsjZdxxList)) {
                    djsjZdxxList = bdcDjsjService.getDjsjNydxx(project.getDjId());
                    if (project.getBdclx().equals(Constants.BDCLX_LQ) && (CollectionUtils.isEmpty(djsjZdxxList)) && StringUtils.isNotBlank(project.getBdcdyh()) && project.getBdcdyh().length() > 19) {
                        String djh = StringUtils.substring(project.getBdcdyh(), 0, 19);
                        djsjZdxxList = bdcDjsjService.getDjsjNydxxByDjh(djh);
                    }
                }

                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    project.setZdzhh(djsjZdxxList.get(0).getDjh());
                }
            }
            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                djsjZdxx = djsjZdxxList.get(0);
            }
            initVoFromParm.setProject(project);
        }
        initVoFromParm.setDjsjFwxx(djsjFwxx);
        initVoFromParm.setDjsjLqxx(djsjLqxx);
        initVoFromParm.setDjsjZdxx(djsjZdxx);
        initVoFromParm.setDjsjZhxx(djsjZhxx);
        initVoFromParm.setDjsjQszdDcb(djsjQszdDcb);
        initVoFromParm.setCbzdDcb(cbzdDcb);
        initVoFromParm.setDjsjNydDcb(djsjNydDcb);
        initVoFromParm.setDjsjFwQlrList(djsjFwQlrList);
        initVoFromParm.setDjsjQszdDcbList(djsjQszdDcbList);
        initVoFromParm.setDjsjNydDcbList(djsjNydDcbList);
        initVoFromParm.setDjsjZdxxList(djsjZdxxList);
        initVoFromParm.setDjsjZhxxList(djsjZhxxList);
        return initVoFromParm;
    }

    /**
     * @param xmxx
     * @author bianwen
     * @rerutn
     * @description fwxx取预测户室的数据
     */
    protected InitVoFromParm getDjxxByDjsjFwychs(Xmxx xmxx) {
        DjsjFwxx djsjFwxx = null;
        DjsjLqxx djsjLqxx = null;
        DjsjZdxx djsjZdxx = null;
        DjsjZhxx djsjZhxx = null;
        DjsjQszdDcb djsjQszdDcb = null;
        DjsjCbzdDcb cbzdDcb = null;
        DjsjNydDcb djsjNydDcb = null;
        List<DjsjZdxx> djsjZdxxList = null;
        List<DjsjZhxx> djsjZhxxList = null;

        List<DjsjFwxx> djsjFwQlrList = null;

        List<DjsjQszdDcb> djsjQszdDcbList = null;
        List<DjsjNydDcb> djsjNydDcbList = null;
        InitVoFromParm initVoFromParm = new InitVoFromParm();

        if (xmxx instanceof Project) {
            Project project = (Project) xmxx;
            if (StringUtils.isNotBlank(project.getDjId()) && Constants.BDCLX_TDFW.equals(project.getBdclx())) {
                djsjFwxx = djsjFwService.getDjsjFwxxByFwychs(project.getDjId());
                if (djsjFwxx != null) {
                    if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && StringUtils.length(djsjFwxx.getBdcdyh()) > 19) {
                        project.setZdzhh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
                    }
                    if (StringUtils.isNotBlank(djsjFwxx.getBdcdyh())) {
                        djsjFwQlrList = bdcDjsjService.getDjsjFwQlr(djsjFwxx.getId());
                    }
                }
                /**
                 * zzhw
                 * 房屋信息中需要查询土地信息继承过来（tdzl、fzmj）
                 */
                if (djsjFwxx != null && StringUtils.isNotBlank(djsjFwxx.getBdcdyh()) && StringUtils.length(djsjFwxx.getBdcdyh()) > 19) {
                    djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(StringUtils.substring(djsjFwxx.getBdcdyh(), 0, 19));
                }
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    djsjZdxx = djsjZdxxList.get(0);
                }
            }

            if (StringUtils.isNotBlank(project.getDjId()) && Constants.BDCLX_TDSL.equals(project.getBdclx())) {
                djsjLqxx = bdcDjsjService.getDjsjLqxx(project.getDjId());
            }
            if (Constants.BDCLX_HY.equals(project.getBdclx())) {
                if (StringUtils.isNotBlank(project.getBdcdyh()) && StringUtils.length(project.getBdcdyh()) > 19)
                    djsjZhxxList = bdcDjsjService.getDjsjZhxxForDjh(StringUtils.substring(project.getBdcdyh(), 0, 19));
                if (CollectionUtils.isNotEmpty(djsjZhxxList)) {
                    djsjZhxx = djsjZhxxList.get(0);
                }
                if (djsjZhxx == null && StringUtils.isNotBlank(project.getDjId())) {
                    djsjZhxx = bdcDjsjService.getDjsjZhxx(project.getDjId());
                }
            }

            if (StringUtils.isNotBlank(project.getDjId()) && Constants.BDCLX_TD.equals(project.getBdclx())) {
                cbzdDcb = djSjMapper.getDjsjCbzdDcbByDjid(project.getDjId());
                if (cbzdDcb != null && StringUtils.isNotBlank(cbzdDcb.getDjh())) {
                    djsjNydDcbList = djSjMapper.getDjsjNydDcbByDjh(cbzdDcb.getDjh());
                }
            }

            if (StringUtils.isNotBlank(project.getDjId()) && StringUtils.equals(project.getBdclx(), Constants.BDCLX_TD)) {
                djsjQszdDcbList = bdcDjsjService.getDjsjQszdDcb(project.getDjId());
                if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
                    djsjQszdDcb = djsjQszdDcbList.get(0);
                }
                djsjZdxxList = bdcDjsjService.getDjsjZdxx(project.getDjId());
                //zwq 取农用地调查表信息
                if (CollectionUtils.isEmpty(djsjZdxxList)) {
                    djsjZdxxList = bdcDjsjService.getDjsjNydxx(project.getDjId());
                    if (project.getBdclx().equals(Constants.BDCLX_LQ) && (CollectionUtils.isEmpty(djsjZdxxList)) && StringUtils.isNotBlank(project.getBdcdyh()) && project.getBdcdyh().length() > 19) {
                        String djh = StringUtils.substring(project.getBdcdyh(), 0, 19);
                        djsjZdxxList = bdcDjsjService.getDjsjNydxxByDjh(djh);
                    }
                }

                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    project.setZdzhh(djsjZdxxList.get(0).getDjh());
                }
            }
            if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                djsjZdxx = djsjZdxxList.get(0);
            }
            initVoFromParm.setProject(project);
        }
        initVoFromParm.setDjsjFwxx(djsjFwxx);
        initVoFromParm.setDjsjLqxx(djsjLqxx);
        initVoFromParm.setDjsjZdxx(djsjZdxx);
        initVoFromParm.setDjsjZhxx(djsjZhxx);
        initVoFromParm.setDjsjQszdDcb(djsjQszdDcb);
        initVoFromParm.setCbzdDcb(cbzdDcb);
        initVoFromParm.setDjsjNydDcb(djsjNydDcb);
        initVoFromParm.setDjsjFwQlrList(djsjFwQlrList);
        initVoFromParm.setDjsjQszdDcbList(djsjQszdDcbList);
        initVoFromParm.setDjsjNydDcbList(djsjNydDcbList);
        initVoFromParm.setDjsjZdxxList(djsjZdxxList);
        initVoFromParm.setDjsjZhxxList(djsjZhxxList);
        return initVoFromParm;
    }

    protected BdcBdcdjb initBdcdjb(final InitVoFromParm initVoFromParm) {

        BdcBdcdjb bdcdjb = null;
        //zdd 如果宗地宗海号为空  则不生成
        if (initVoFromParm != null && initVoFromParm.getProject() != null && StringUtils.isNotBlank(initVoFromParm.getProject().getZdzhh())) {
            bdcdjb = bdcDjbService.getBdcdjbFromProject(initVoFromParm.getProject(), bdcdjb);
            bdcdjb = bdcDjbService.getBdcdjbFromQsdcb(initVoFromParm.getDjsjQszdDcb(), bdcdjb);
            bdcdjb = bdcDjbService.getBdcdjbFromNydZdxx(initVoFromParm.getDjsjNydDcbList(), bdcdjb);
            bdcdjb = bdcDjbService.getBdcdjbFromZdxx(initVoFromParm.getDjsjZdxx(), bdcdjb);
            bdcdjb = bdcDjbService.getBdcdjbFromFwxx(initVoFromParm.getDjsjFwxx(), bdcdjb);
            bdcdjb = bdcDjbService.getBdcdjbFromLqxx(initVoFromParm.getDjsjLqxx(), bdcdjb);
            bdcdjb = bdcDjbService.getBdcdjbFromZhxx(initVoFromParm.getDjsjZhxx(), bdcdjb);
        }
        return bdcdjb;
    }

    /**
     * zdd 初始化审批信息
     *
     * @return
     */
    protected BdcSpxx initBdcSpxx(InitVoFromParm initVoFromParm, BdcSpxx bdcSpxx) {
        //zdd 如果不动产单元号为空  则不生成
        if (initVoFromParm.getProject() != null && StringUtils.isNotBlank(initVoFromParm.getProject().getProid())) {
            if (bdcSpxx == null) {
                bdcSpxx = new BdcSpxx();
            }
            if (StringUtils.isBlank(bdcSpxx.getSpxxid())) {
                bdcSpxx.setSpxxid(UUIDGenerator.generate18());
            }
            bdcSpxx = bdcSpxxService.getBdcSpxxFromProject(initVoFromParm.getProject(), bdcSpxx);
            bdcSpxx = bdcSpxxService.getBdcSpxxFromZd(initVoFromParm.getDjsjZdxx(), bdcSpxx);
            bdcSpxx = bdcSpxxService.getBdcSpxxFromFw(initVoFromParm.getDjsjFwxx(), bdcSpxx, initVoFromParm.getProject());
            bdcSpxx = bdcSpxxService.getBdcSpxxFromLq(initVoFromParm.getDjsjLqxx(), bdcSpxx);
            bdcSpxx = bdcSpxxService.getBdcSpxxFromZh(initVoFromParm.getDjsjZhxx(), bdcSpxx);
            String djlxdm = "";
            if (StringUtils.isNotBlank(initVoFromParm.getProject().getDjlx())) {
                djlxdm = initVoFromParm.getProject().getDjlx();
            }
            if (StringUtils.isBlank(djlxdm) && StringUtils.isNotBlank(initVoFromParm.getProject().getSqlx())) {
                djlxdm = bdcZdGlService.getDjlxDmBySqlxdm(initVoFromParm.getProject().getSqlx());
            }


            if (CollectionUtils.isNotEmpty(initVoFromParm.getDjsjNydDcbList())) {
                bdcSpxx = bdcSpxxService.getBdcSpxxFromTdcb(initVoFromParm.getDjsjNydDcbList().get(0), bdcSpxx);
            }
            if (CollectionUtils.isNotEmpty(initVoFromParm.getDjsjQszdDcbList())) {
                bdcSpxx = bdcSpxxService.getBdcSpxxFromTdSyq(initVoFromParm.getDjsjQszdDcbList().get(0), bdcSpxx);
            }
            if (StringUtils.isNotBlank(initVoFromParm.getProject().getGdproid())
                    && StringUtils.isNotBlank(initVoFromParm.getProject().getYqlid())
                    && !CommonUtil.indexOfStrs(Constants.SQLX_FGZY_DM, initVoFromParm.getProject().getSqlx())
                    && !(StringUtils.isNotBlank(djlxdm) && StringUtils.equals(djlxdm, Constants.DJLX_CSDJ_DM))) {
                bdcSpxx = bdcSpxxService.getBdcSpxxFromGdxm(initVoFromParm.getProject().getGdproid(), initVoFromParm.getProject().getYqlid(), initVoFromParm.getProject().getXmly(), bdcSpxx);
            }
            if (StringUtils.isNotBlank(initVoFromParm.getProject().getYxmid())
                    && !(StringUtils.isNotBlank(djlxdm) && StringUtils.equals(djlxdm, Constants.DJLX_CSDJ_DM) &&
                    !StringUtils.equals(initVoFromParm.getProject().getSqlx(), Constants.SQLX_SPFSCKFSZC_DM))) {
                bdcSpxx = bdcSpxxService.getBdcSpxxFromYProject(initVoFromParm.getProject(), bdcSpxx);
            }
            if (StringUtils.equals(initVoFromParm.getProject().getSqlx(), Constants.SQLX_SPFMMZYDJ_DM)) {
                bdcSpxx = bdcSpxxService.getBdcSpxxFromYg(initVoFromParm.getProject().getBdcdyh(), bdcSpxx);
            }
            //zwq 若审批信息中有mc,将mc转为dm
            bdcSpxx = bdcZdGlService.changeToDm(bdcSpxx);


            //yanzhenkun 针对项目内多幢的情况，处理审批信息bdcspxx里面的mj信息
            if (initVoFromParm.getDjsjFwxx() != null &&
                    StringUtils.isNotBlank(initVoFromParm.getDjsjFwxx().getBdcdyfwlx()) && StringUtils.equals(initVoFromParm.getDjsjFwxx().getBdcdyfwlx(), "1")) {
                String xmly = "";
                if (StringUtils.isNotBlank(initVoFromParm.getProject().getXmly())) {
                    xmly = initVoFromParm.getProject().getXmly();
                }
                String bdcdyid = "";
                if (StringUtils.isNotBlank(initVoFromParm.getProject().getBdcdyid())) {
                    bdcdyid = initVoFromParm.getProject().getBdcdyid();
                } else if (StringUtils.isNotBlank(initVoFromParm.getProject().getBdcdyh())) {
                    bdcdyid = bdcdyService.getBdcdyidByBdcdyh(initVoFromParm.getProject().getBdcdyh());
                }
                String bdcdyh = "";
                if (StringUtils.isNotBlank(initVoFromParm.getProject().getBdcdyh())) {
                    bdcdyh = initVoFromParm.getProject().getBdcdyh();
                }
                String djlx = "";
                if (StringUtils.isNotBlank(initVoFromParm.getProject().getDjlx())) {
                    djlx = initVoFromParm.getProject().getDjlx();
                }
                Double fwzmj = bdcFdcqDzService.getBdcFdcqDzFwzmj(xmly, bdcdyid, bdcdyh, djlx);
                if (fwzmj > 0) {
                    bdcSpxx.setMj(fwzmj);
                    bdcSpxx.setScmj(fwzmj);
                }
            }
        }

        return bdcSpxx;
    }


    protected BdcBdcdy initBdcdy(final InitVoFromParm initVoFromParm, final BdcBdcdjb bdcdjb) {
        BdcBdcdy bdcdy = null;
        //zdd 如果不动产单元号为空  则不生成
        Project project = initVoFromParm.getProject();
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            bdcdy = new BdcBdcdy();
            if (bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid())) {
                bdcdy.setDjbid(bdcdjb.getDjbid());
            }
            bdcdy = bdcdyService.getBdcdyFromProject(project, bdcdy);
            bdcdy = bdcdyService.getBdcdyFromFw(initVoFromParm.getDjsjFwxx(), bdcdy);

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

    //初始化过渡多个不动产单元登记簿和不动产单元
    protected List<InsertVo> initGdMulBdcdyAndBdcdjb(List<InsertVo> list, Project project) {
        return list;
    }

    //初始化过渡土地多个不动产单元登记簿和不动产单元
    protected List<InsertVo> initGdTdMulBdcdyAndBdcdjb(List<InsertVo> list, Project project) {
        return list;
    }

    //初始化项目多个不动产单元登记簿和不动产单元
    protected List<InsertVo> initMulBdcdyAndBdcdjb(List<InsertVo> list, final InitVoFromParm initVoFromParm) {
        Project project = initVoFromParm.getProject();
        //zdd 防止一个项目选择不同宗地宗海号 造成的垃圾数据
        if (project != null && project.getBdcdyhs() != null && project.getDjIds() != null && CollectionUtils.isNotEmpty(project.getBdcdyhs()) && project.getBdcdyhs().size() == project.getDjIds().size()) {
            HashMap djbMap = new HashMap();
            HashMap bdcTdMap = new HashMap();
            int i = 0;
            for (String bdcdyh : project.getBdcdyhs()) {
                project.setDjId(project.getDjIds().get(i));
                getDjxx((Xmxx) project);

                if (StringUtils.isNotBlank(bdcdyh) && StringUtils.length(bdcdyh) > 19) {
                    project.setBdcdyh(bdcdyh);
                    BdcBdcdjb bdcdjb = null;
                    String djh = StringUtils.substring(bdcdyh, 0, 19);
                    if (StringUtils.isNotBlank(djh)) {
                        List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(djh);
                        if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                            if (djbMap.get(djh) != null) {
                                bdcdjb = (BdcBdcdjb) djbMap.get(djh);
                            } else {
                                bdcdjb = initBdcdjb(initVoFromParm);
                                djbMap.put(djh, bdcdjb);
                                list.add(bdcdjb);
                            }

                        } else {
                            bdcdjb = bdcdjbTemps.get(0);
                        }
                        // zzhw 在选取不动产单元的时候顺便继承土地信息到bdc_td表中
                        BdcTd bdcTd = bdcTdService.selectBdcTd(djh);
                        if (bdcTd == null) {
                            if (bdcTdMap.get(djh) != null) {
                                bdcTd = (BdcTd) bdcTdMap.get(djh);
                            } else {
                                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), project, null);
                                bdcTdMap.put(djh, bdcTd);
                                list.add(bdcTd);
                            }
                        }
                    }
                    BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(project.getProid());
                    bdcSpxx = initBdcSpxx(initVoFromParm, bdcSpxx);
                    if (bdcSpxx != null) {
                        list.add(bdcSpxx);
                    }
                    //   不动产单元
                    BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
                    if (bdcdy != null) {
                        list.add(bdcdy);
                    }

                }
                i++;
            }
        }

        return list;
    }

    /**
     * zdd 根据地籍数据初始化权利人
     *
     * @param project
     * @param djsjFwQlrList
     * @param djsjLqxx
     * @param djsjZdxxList
     * @param qlrlx
     * @return
     */
    public List<BdcQlr> initBdcQlrFromDjsj(final Project project, final List<DjsjFwxx> djsjFwQlrList, final DjsjLqxx djsjLqxx, final List<DjsjZdxx> djsjZdxxList, final List<DjsjQszdDcb> djsjQszdDcbList, final DjsjCbzdDcb cbzdDcb, final String qlrlx) {
        List<BdcQlr> bdcQlrList = null;
        if (CollectionUtils.isNotEmpty(djsjFwQlrList)) {
            List<BdcQlr> bdcQlrListFromFw = initBdcQlrFromFw(project, djsjFwQlrList, qlrlx);
            if (CollectionUtils.isNotEmpty(bdcQlrListFromFw)) {
                bdcQlrList = bdcQlrListFromFw;
            }
        } else if (djsjLqxx != null) {
            List<BdcQlr> bdcQlrListFromLq = initBdcQlrFromLq(project, djsjLqxx, qlrlx);
            if (CollectionUtils.isNotEmpty(bdcQlrListFromLq)) {
                bdcQlrList = bdcQlrListFromLq;
            }
        } else if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            List<BdcQlr> bdcQlrListFromZd = initBdcQlrFromZd(project, djsjZdxxList, qlrlx);
            if (CollectionUtils.isNotEmpty(bdcQlrListFromZd)) {
                bdcQlrList = bdcQlrListFromZd;
            }
        } else if (cbzdDcb != null) {
            bdcQlrList = initBdcQlrFromCb(project, cbzdDcb);
        } else if (djsjQszdDcbList != null) {
            bdcQlrList = initBdcQlrFromQszd(project, djsjQszdDcbList);
        }
        return bdcQlrList;
    }

    /**
     * zdd 根据 djsjFwQlrList 初始化权利人
     *
     * @param project
     * @param djsjFwQlrList
     * @param qlrlx         Constants.QLRLX
     * @return
     */
    protected List<BdcQlr> initBdcQlrFromFw(Project project, List<DjsjFwxx> djsjFwQlrList, String qlrlx) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (CollectionUtils.isNotEmpty(djsjFwQlrList)) {
            for (DjsjFwxx fwxx : djsjFwQlrList) {
                BdcQlr bdcQlr = new BdcQlr();
                bdcQlr = bdcQlrService.getBdcQlrFromProject(project, bdcQlr);
                bdcQlr = bdcQlrService.getBdcQlrFromFw(fwxx, bdcQlr);
                bdcQlr.setQlrlx(qlrlx);
                if (StringUtils.isNotBlank(bdcQlr.getQlrmc()))
                    bdcQlrList.add(bdcQlr);
            }
        }

        return bdcQlrList;
    }

    /**
     * zdd 根据 djsjZdxxList 初始化权利人
     *
     * @param project
     * @param djsjZdxxList
     * @param qlrlx
     * @return
     */
    protected List<BdcQlr> initBdcQlrFromZd(Project project, List<DjsjZdxx> djsjZdxxList, String qlrlx) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
            for (DjsjZdxx zdxx : djsjZdxxList) {
                BdcQlr bdcQlr = new BdcQlr();
                bdcQlr = bdcQlrService.getBdcQlrFromProject(project, bdcQlr);
                bdcQlr = bdcQlrService.getBdcQlrFromZd(zdxx, bdcQlr);
                bdcQlr.setQlrlx(qlrlx);
                if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                    bdcQlrList.add(bdcQlr);
                }
            }
        }
        return bdcQlrList;
    }

    protected List<BdcQlr> initBdcQlrFromQszd(Project project, List<DjsjQszdDcb> djsjQszdDcbList) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        BdcQlr bdcQlr = new BdcQlr();
        if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
            DjsjQszdDcb djsjQszdDcb = djsjQszdDcbList.get(0);
            bdcQlr = bdcQlrService.getBdcQlrFromProject(project, bdcQlr);
            bdcQlrList = bdcQlrService.getBdcQlrFromQszd(djsjQszdDcb, bdcQlr);
        }
        return bdcQlrList;
    }


    protected List<BdcQlr> initBdcQlrFromCb(Project project, DjsjCbzdDcb cbzdDcb) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        BdcQlr bdcQlr = new BdcQlr();
        if (cbzdDcb != null) {
            bdcQlr = bdcQlrService.getBdcQlrFromProject(project, bdcQlr);
            bdcQlrList = bdcQlrService.getBdcQlrFromCb(cbzdDcb, bdcQlr);
        }
        return bdcQlrList;
    }

    /**
     * zdd 根据 djsjLqxx 初始化权利人
     *
     * @param project
     * @param djsjLqxx
     * @param qlrlx
     * @return
     */
    protected List<BdcQlr> initBdcQlrFromLq(Project project, DjsjLqxx djsjLqxx, String qlrlx) {
        List<BdcQlr> bdcQlrList;
        BdcQlr bdcQlr = new BdcQlr();
        bdcQlr = bdcQlrService.getBdcQlrFromProject(project, bdcQlr);
        bdcQlrList = bdcQlrService.getBdcQlrFromLq(djsjLqxx, bdcQlr);
        bdcQlr.setQlrlx(qlrlx);
        if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
            bdcQlrList.add(bdcQlr);
        }
        return bdcQlrList;
    }


    @Override
    public Project creatWorkFlow(final Xmxx xmxx) {
        Project project = null;
        String proId = "";
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }
        //jyl 一个流程实例只要生成一次
        if (project != null && StringUtils.isNotBlank(project.getWiid())) {
            proId = PlatformUtil.getProIDFromPlatform(project.getWiid());
        }
        if (project != null && StringUtils.isBlank(proId)) {
            proId = project.getProid();
        }
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proId);
        if (project != null && StringUtils.isNotBlank(project.getWorkFlowDefId()) && null == pfWorkFlowInstanceVo) {
            PfWorkFlowDefineVo wfDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(project.getWorkFlowDefId());
            PfWorkFlowInstanceVo wfVO = new PfWorkFlowInstanceVo();
            Date createTime = new Date();
            wfVO.setTimeLimit(wfDefineVo.getTimeLimit());
            wfVO.setCreateTime(createTime);
            if (StringUtils.isBlank(project.getProid())) {
                project.setProid(UUIDGenerator.generate18());
            }
            wfVO.setWorkflowIntanceId(UUIDGenerator.generate18());
            wfVO.setWorkflowDefinitionId(project.getWorkFlowDefId());
            wfVO.setProId(project.getProid());


            project.setWiid(wfVO.getWorkflowIntanceId());
            if (StringUtils.isBlank(project.getBh())) {
                project.setBh(bdcXmService.creatXmbh(project));
            }
            wfVO.setWorkflowIntanceName(project.getBh());
            String remark = CommonUtil.formatEmptyValue(project.getBh()) + Constants.SPLIT_STR + CommonUtil.formatEmptyValue(project.getXmmc()) + Constants.SPLIT_STR + CommonUtil.formatEmptyValue(project.getBdcdyh()) + Constants.SPLIT_STR + CommonUtil.formatEmptyValue(project.getGdproid()) + Constants.SPLIT_STR;
            wfVO.setRemark(remark);

            if (StringUtils.isBlank(project.getUserId())) {
                project.setUserId(SessionUtil.getCurrentUserId());
            }
            project.setDwdm(PlatformUtil.getCurrentUserDwdmByUserid(project.getUserId()));

            wfVO.setCreateUser(project.getUserId());
            try {
                SysWorkFlowInstanceService sysWorkFlowInstanceService = PlatformUtil.getSysWorkFlowInstanceService();
                PfWorkFlowInstanceVo wfInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(project.getProid());
                if (wfInstanceVo == null) {
                    WorkFlowInfo workFlowInfo = workFlowCoreService.createWorkFlowInstance(wfVO, project.getUserId());
                    if (workFlowInfo != null && CollectionUtils.isNotEmpty(workFlowInfo.getTargetTasks())) {
                        project.setTaskid(workFlowInfo.getTargetTasks().get(0).getTaskId());
                    }
                }
            } catch (Exception e) {
                logger.error("CreatProjectDefaultServiceImpl.creatWorkFlow", e);
            }
        }
        if (project != null && null != pfWorkFlowInstanceVo) {
            project.setWiid(pfWorkFlowInstanceVo.getWorkflowIntanceId());
        }
        return project;
    }

    @Override
    public Integer creatProjectNode(final String proid) {
        Space rootSpace = fileCenterNodeServiceImpl.getWorkSpace("WORK_FLOW_STUFF",
                true);
        com.gtis.fileCenter.model.Node sournode = fileCenterNodeServiceImpl.getNode(
                rootSpace.getId(), proid, true);

        return sournode.getId();
    }

    @Override
    public List<InsertVo> initVoFromOldData(final Xmxx xmxx) {
        return null;
    }


    //    sc 多个不动产单元流程，暂时先考虑抵押
    @Override
    public List<InsertVo> initVoFromOldDataMul(final Xmxx xmxx) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertProjectData(final List<InsertVo> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            saveOrUpdateProjectData(list);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateProjectData(final List<InsertVo> list) {
        //存储分组后的List数据
        List<List<InsertVo>> newList = new ArrayList<List<InsertVo>>();
        //存储实体类名为主键的map
        HashMap<String, List<InsertVo>> map = new HashMap<String, List<InsertVo>>();
        List<InsertVo> voList;
        List<String> zdzhhList = new ArrayList<String>();
        List<String> bdcdyhList = new ArrayList<String>();
        List<String> bdcTdList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            InsertVo vo = list.get(i);
            if (vo == null)
                continue;
            Method method1 = AnnotationsUtils.getAnnotationsName(vo);
            String id = null;
            try {
                if (method1.invoke(vo) != null) {
                    id = method1.invoke(vo).toString();
                }
            } catch (Exception e) {
                logger.error("CreatProjectDefaultServiceImpl.saveOrUpdateProjectData", e);
            }
            if (StringUtils.isNotBlank(id) && entityMapper.selectByPrimaryKey(vo.getClass(), id) != null) {
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
    public synchronized void updateWorkFlow(final Xmxx xmxx) {
        BdcXm bdcxm = null;
        List<BdcXm> bdcXmList = null;
        HashMap map = new HashMap();
        StringBuilder remark = new StringBuilder();
        if (xmxx instanceof BdcXm)
            bdcxm = (BdcXm) xmxx;
        String sqlxDm = "";
        if (bdcxm != null && StringUtils.isNotBlank(bdcxm.getProid())) {
            sqlxDm = bdcxm.getSqlx();
            //zwq  一个bdcdy对应一个bdcxm
            if (StringUtils.isNotBlank(bdcxm.getWiid())) {
                //jyl  过滤不动产附属设施的不动产项目。
                map.put("wiid", bdcxm.getWiid());
                bdcXmList = bdcXmService.getBdcXmList(map);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXm1 : bdcXmList) {
                        if (StringUtils.isNotBlank(bdcXm1.getSqlx()) && !StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_DY_GDDY) && !StringUtils.equals(bdcXm1.getSqlx(), Constants.SQLX_CF_GDCF)) {
                            sqlxDm = bdcXm1.getSqlx();
                            break;
                        }
                    }
                }
            }
            Example qlrQuery = new Example(BdcQlr.class);

            List proidList = new ArrayList();
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    if (StringUtils.equals(bdcxm.getSqlx(), bdcXm.getSqlx())) {
                        proidList.add(bdcXm.getProid());
                    }
                }
            }

            /**
             * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
             * @description 配置查封权利人是否为执行人 若是，则查封在代办任务中取义务人显示；否则，取权利人
             */
            String cfqlr = AppConfig.getProperty("cfdj.qlr");
            if (StringUtils.isNotBlank(cfqlr) && StringUtils.equals(cfqlr, Constants.CFQLR_COURT)) {
                if (CommonUtil.indexOfStrs(Constants.CF_SQLX, sqlxDm)) {
                    qlrQuery.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid()).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_YWR);
                } else {
                    qlrQuery.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid()).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
                }
            } else {
                if (CollectionUtils.isNotEmpty(proidList)) {
                    qlrQuery.createCriteria().andIn(ParamsConstants.PROID_LOWERCASE, proidList).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
                } else {
                    qlrQuery.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid()).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
                }
            }

            qlrQuery.setOrderByClause("sxh");
            List<BdcQlr> qlrList = entityMapper.selectByExample(BdcQlr.class, qlrQuery);
            Example ywrQuery = new Example(BdcQlr.class);
            if (CollectionUtils.isNotEmpty(proidList)) {
                ywrQuery.createCriteria().andIn(ParamsConstants.PROID_LOWERCASE, proidList).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_YWR);
            } else {
                ywrQuery.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid()).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_YWR);
            }
            ywrQuery.setOrderByClause("sxh");
            List<BdcQlr> ywrList = entityMapper.selectByExample(BdcQlr.class, ywrQuery);

            Example spxxQuery = new Example(BdcSpxx.class);
            if (CollectionUtils.isNotEmpty(proidList)) {
                spxxQuery.createCriteria().andIn(ParamsConstants.PROID_LOWERCASE, proidList);
            } else {
                spxxQuery.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcxm.getProid());
            }
            List<BdcSpxx> bdcSpxxList = entityMapper.selectByExample(BdcSpxx.class, spxxQuery);

            List<String> bdcdyidList = null;
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                bdcdyidList = new ArrayList<String>();
                for (BdcXm bdcXm : bdcXmList) {
                    if (StringUtils.isNotBlank(bdcXm.getBdcdyid()) && !bdcdyidList.contains(bdcXm.getBdcdyid()))
                        bdcdyidList.add(bdcXm.getBdcdyid());
                }
            }
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcxm.getWiid());
            if (pfWorkFlowInstanceVo != null) {
                StringBuilder qlr = new StringBuilder();
                StringBuilder ywr = new StringBuilder();
                if (qlrList != null) {
                    for (BdcQlr bdcQlr : qlrList) {
                        if (StringUtils.isNotBlank(bdcQlr.getQlrmc()) && !StringUtils.contains(qlr, bdcQlr.getQlrmc()))
                            qlr.append(bdcQlr.getQlrmc()).append(",");
                    }
                }
                if (ywrList != null) {
                    for (BdcQlr bdcQlr : ywrList) {
                        if (StringUtils.isNotBlank(bdcQlr.getQlrmc()) && !StringUtils.contains(ywr, bdcQlr.getQlrmc()))
                            ywr.append(bdcQlr.getQlrmc()).append(",");
                    }
                }
                if (StringUtils.isNotBlank(qlr))
                    qlr = new StringBuilder(StringUtils.substring(qlr.toString(), 0, qlr.lastIndexOf(",")));
                if (StringUtils.isNotBlank(ywr))
                    ywr = new StringBuilder(StringUtils.substring(ywr.toString(), 0, ywr.lastIndexOf(",")));
                if (StringUtils.isNotBlank(qlr))
                    pfWorkFlowInstanceVo.setWorkflowIntanceName(qlr.toString());
                if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
                    /**
                     * @author <a href="mailto:zhanglili@gtmap.cn">zhanglili</a>
                     * @description 修改批量查封待办任务显示权利人，显示不动产单元号等问题
                     */
                    StringBuilder bdcdyh = new StringBuilder();
                    StringBuilder zl = new StringBuilder();
                    for (BdcSpxx bdcSpxx : bdcSpxxList) {
                        if (StringUtils.isNotBlank(bdcSpxx.getBdcdyh())) {
                            bdcdyh.append(bdcSpxx.getBdcdyh()).append(",");
                        }
                        if (StringUtils.isNotBlank(bdcSpxx.getZl())) {
                            zl.append(bdcSpxx.getZl()).append(",");
                        }
                    }
                    if (StringUtils.isNotBlank(bdcdyh)) {
                        bdcdyh = new StringBuilder(StringUtils.substring(bdcdyh.toString(), 0, bdcdyh.lastIndexOf(",")));
                    }
                    if (StringUtils.isNotBlank(zl)) {
                        zl = new StringBuilder(StringUtils.substring(zl.toString(), 0, zl.lastIndexOf(",")));
                    }
                    String bhTemp = CommonUtil.formatEmptyValue(bdcxm.getBh());
                    StringBuilder qlrTemp = new StringBuilder(CommonUtil.formatEmptyValue(qlr));
                    StringBuilder ywrTemp = new StringBuilder(CommonUtil.formatEmptyValue(ywr));
                    StringBuilder bdcdyhTemp = new StringBuilder(CommonUtil.formatEmptyValue(bdcdyh));
                    StringBuilder zlTemp = new StringBuilder(CommonUtil.formatEmptyValue(zl));
                    StringBuilder ybhTemp = new StringBuilder(CommonUtil.formatEmptyValue(bdcxm.getYbh()));
                    if ((CollectionUtils.isNotEmpty(bdcdyidList) && bdcdyidList.size() > 1)) {
                        /**
                         * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
                         * @description 房屋分割登记remark字段显示全部权利人名称
                         */
                        if (CommonUtil.indexOfStrs(Constants.SQLX_FWFG_DJ, bdcxm.getSqlx())) {
                            qlrTemp = new StringBuilder("");
                            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                                List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
                                for (BdcXm xm : bdcXmList) {
                                    Example query = new Example(BdcQlr.class);
                                    query.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, xm.getProid()).andEqualTo(ParamsConstants.QLRLX_LOWERCASE, Constants.QLRLX_QLR);
                                    query.setOrderByClause("sxh");
                                    List<BdcQlr> list = entityMapper.selectByExample(BdcQlr.class, query);
                                    if (CollectionUtils.isNotEmpty(list)) {
                                        bdcQlrList.addAll(list);
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                                    for (int i = 0; i < bdcQlrList.size(); i++) {
                                        BdcQlr bdcQlr = bdcQlrList.get(i);
                                        if (i == bdcQlrList.size() - 1) {
                                            qlrTemp.append(bdcQlr.getQlrmc());
                                        } else {
                                            qlrTemp.append(bdcQlr.getQlrmc()).append(",");
                                        }
                                    }
                                }
                            }
                        } else {
                            if (StringUtils.isNotBlank(qlrTemp) && !StringUtils.equals(sqlxDm, Constants.SQLX_ZJJZWDY_FW_DM) && StringUtils.indexOf(qlrTemp, ",") > -1) {
                                qlrTemp = new StringBuilder(StringUtils.substring(qlrTemp.toString(), 0, qlrTemp.indexOf(",")));
                                //商品房及业主共有部分首次登记,权利人不应该加等字
                                /**
                                 * @author bianwen
                                 * @description 在建工程以房屋为主权利人不加等
                                 */
                                if (StringUtils.isNotBlank(sqlxDm) && !StringUtils.equals(sqlxDm, Constants.SQLX_SPFGYSCDJ_DM)
                                        && !CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, sqlxDm)) {
                                    qlrTemp.append("等");
                                }
                            }
                        }

                        if (StringUtils.isNotBlank(ywrTemp) && StringUtils.indexOf(ywrTemp, ",") > -1) {
                            ywrTemp = new StringBuilder(StringUtils.substring(ywrTemp.toString(), 0, ywrTemp.indexOf(",")));
                            /**
                             * @author bianwen
                             * @description 在建工程以房屋为主义务人不加等
                             */
                            if (StringUtils.isNotBlank(sqlxDm) && !CommonUtil.indexOfStrs(Constants.SQLX_ZJJZW_FW_DM, sqlxDm))
                                ywrTemp.append("等");
                        }
                        if (StringUtils.isNotBlank(bdcdyhTemp)) {
                            if (StringUtils.indexOf(bdcdyhTemp, ",") > -1) {
                                bdcdyhTemp = new StringBuilder(StringUtils.substring(bdcdyhTemp.toString(), 0, bdcdyhTemp.indexOf(",")));
                                bdcdyhTemp.append("等");
                            } else {
                                if (StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM) || CommonUtil.indexOfStrs(Constants.SQLX_FWFG_DJ, bdcxm.getSqlx())) {
                                    bdcdyhTemp.append("等");
                                }
                            }
                        }

                        if (StringUtils.isNotBlank(zlTemp)) {
                            if (StringUtils.indexOf(zlTemp, ",") > -1) {
                                zlTemp = new StringBuilder(StringUtils.substring(zlTemp.toString(), 0, zlTemp.indexOf(",")));
                                zlTemp.append("等");
                            } else {
                                /**
                                 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
                                 * @description 商品房及业主共有部分的首次登记的remark字段的坐落显示逻辑幢的坐落
                                 */
                                if (StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_PLFZ_DM) || StringUtils.equals(bdcxm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) {
                                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcxm.getProid());
                                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                                        String qjid = "";
                                        for (BdcXmRel bdcXmRel : bdcXmRelList) {
                                            if (StringUtils.isNotBlank(bdcXmRel.getQjid())) {
                                                qjid = bdcXmRel.getQjid();
                                                break;
                                            }
                                        }
                                        if (StringUtils.isNotBlank(qjid)) {
                                            HashMap hsMap = new HashMap();
                                            hsMap.put("fw_hs_index", qjid);
                                            List<DjsjFwHs> djsjFwHsList = djsjFwService.getDjsjFwHs(hsMap);
                                            if (CollectionUtils.isNotEmpty(djsjFwHsList)) {
                                                DjsjFwHs djsjFwHs = djsjFwHsList.get(0);
                                                if (StringUtils.isNotBlank(djsjFwHs.getFwDcbIndex())) {
                                                    HashMap ljzMap = new HashMap();
                                                    ljzMap.put("fw_dcb_index", djsjFwHs.getFwDcbIndex());
                                                    List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(ljzMap);
                                                    if (CollectionUtils.isNotEmpty(djsjFwLjzList)) {
                                                        DjsjFwLjz djsjFwLjz = djsjFwLjzList.get(0);
                                                        if (StringUtils.isNotBlank(djsjFwLjz.getZldz())) {
                                                            zlTemp = new StringBuilder(djsjFwLjz.getZldz());
                                                        }
                                                    }
                                                }


                                            }
                                        }

                                    }

                                } else if (CommonUtil.indexOfStrs(Constants.SQLX_FWFG_DJ, bdcxm.getSqlx())) {
                                    zlTemp.append("等");
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(ybhTemp) && StringUtils.indexOf(ybhTemp, ",") > -1) {
                            ybhTemp.append("等");
                        }
                        //合并流程中首页待办任务中显示的权利人和义务人应该包含两个子流程的权利人和义务人
                    } else if (CollectionUtils.isNotEmpty(bdcdyidList) && bdcdyidList.size() == 1) {
                        String sqlxdm = "";
                        //获取平台的申请类型代码,主要为了合并
                        if (StringUtils.isNotBlank(bdcxm.getWiid())) {
                            PfWorkFlowInstanceVo pfWorkFlowInstance = sysWorkFlowInstanceService.getWorkflowInstance(bdcxm.getWiid());
                            if (pfWorkFlowInstance != null && StringUtils.isNotBlank(pfWorkFlowInstance.getWorkflowDefinitionId())) {
                                sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstance.getWorkflowDefinitionId());
                            }
                        }
                        if (StringUtils.isNotBlank(sqlxdm) && CommonUtil.indexOfStrs(Constants.SQLX_hblc_zlc, sqlxdm)) {
                            List<BdcXm> xmList = bdcXmService.getBdcXmListByWiid(bdcxm.getWiid());
                            if (CollectionUtils.isNotEmpty(xmList)) {
                                qlrTemp = new StringBuilder();
                                ywrTemp = new StringBuilder();
                                for (int j = 0; j < xmList.size(); j++) {
                                    BdcXm xm = xmList.get(j);
                                    HashMap<String, String> map1 = new HashMap<String, String>();
                                    map1.put(ParamsConstants.PROID_LOWERCASE, xm.getProid());
                                    List<BdcQlr> qlrs = bdcQlrService.queryBdcQlrList(map1);
                                    if (!StringUtils.equals(xm.getQllx(), Constants.QLLX_DYAQ)) {
                                        for (BdcQlr qlr1 : qlrs) {
                                            if (Constants.QLRLX_QLR.equals(qlr1.getQlrlx()) && StringUtils.isNotBlank(qlr1.getQlrmc()) && !qlrTemp.toString().contains(qlr1.getQlrmc())) {
                                                qlrTemp.insert(0, qlr1.getQlrmc() + ",");
                                            } else if (Constants.QLRLX_YWR.equals(qlr1.getQlrlx()) && StringUtils.isNotBlank(qlr1.getQlrmc()) && !ywrTemp.toString().contains(qlr1.getQlrmc())) {
                                                ywrTemp.insert(0, qlr1.getQlrmc() + ",");
                                            }
                                        }
                                    } else {
                                        for (BdcQlr qlr1 : qlrs) {
                                            if (Constants.QLRLX_QLR.equals(qlr1.getQlrlx()) && StringUtils.isNotBlank(qlr1.getQlrmc()) && !qlrTemp.toString().contains(qlr1.getQlrmc())) {
                                                qlrTemp.append(qlr1.getQlrmc() + ",");
                                            } else if (Constants.QLRLX_YWR.equals(qlr1.getQlrlx()) && StringUtils.isNotBlank(qlr1.getQlrmc()) && !ywrTemp.toString().contains(qlr1.getQlrmc())) {
                                                ywrTemp.append(qlr1.getQlrmc() + ",");
                                            }
                                        }
                                    }

                                }
                                qlrTemp = new StringBuilder(StringUtils.substring(qlrTemp.toString(), 0, qlrTemp.lastIndexOf(",")));
                                ywrTemp = new StringBuilder(StringUtils.substring(ywrTemp.toString(), 0, ywrTemp.lastIndexOf(",")));
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(bdcdyhTemp) && StringUtils.indexOf(bdcdyhTemp, ",") > -1) {
                        bdcdyhTemp = new StringBuilder(StringUtils.substring(bdcdyhTemp.toString(), 0, bdcdyhTemp.indexOf(",")));
                    }
                    if (StringUtils.isNotBlank(zlTemp) && StringUtils.indexOf(zlTemp, ",") > -1) {
                        zlTemp = new StringBuilder(StringUtils.substring(zlTemp.toString(), 0, zlTemp.indexOf(",")));
                    }
                    remark.append(bhTemp).append(Constants.SPLIT_STR).append(qlrTemp).append(Constants.SPLIT_STR).append(bdcdyhTemp)
                            .append(Constants.SPLIT_STR).append(zlTemp).append(Constants.SPLIT_STR).append(ybhTemp).append(Constants.SPLIT_STR)
                            .append(ywrTemp);
                    pfWorkFlowInstanceVo.setWorkflowIntanceName(qlrTemp.toString());
                } else {
                    remark.append(CommonUtil.formatEmptyValue(bdcxm.getBh())).append(Constants.SPLIT_STR).append(CommonUtil.formatEmptyValue(qlr)).append(Constants.SPLIT_STR)
                            .append("").append(Constants.SPLIT_STR).append(CommonUtil.formatEmptyValue(bdcxm.getZl())).append(Constants.SPLIT_STR).append(CommonUtil.formatEmptyValue(bdcxm.getYbh()))
                            .append(Constants.SPLIT_STR).append(CommonUtil.formatEmptyValue(ywr));
                    pfWorkFlowInstanceVo.setWorkflowIntanceName(qlr.toString());
                }


                if (StringUtils.isNotBlank(remark)) {
                    pfWorkFlowInstanceVo.setRemark(remark.toString());
                }
                sysWorkFlowInstanceService.updateWorkFlowInstanceRemark(pfWorkFlowInstanceVo);
                sysWorkFlowInstanceService.updateWorkFlowIntanceName(pfWorkFlowInstanceVo);
            }


        }
    }

    /**
     * zx 根据过渡期原项目ID或者房产证ID 如果是房产证上的义务人获取原土地证的权利人
     * 如果有原项目信息  则不再读取房产证义务人
     *
     * @param project
     * @return
     */
    public List<BdcQlr> getGdYbdcYwrList(final Project project) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (StringUtils.isNotBlank(project.getYxmid())) {
            bdcQlrList = bdcQlrService.queryBdcYwrByProid(project.getYxmid());
        }
        //zdd 如果地籍有权利人信息  则以地籍为准
        if (CollectionUtils.isEmpty(bdcQlrList) && StringUtils.isNotBlank(project.getYqlid()) && !project.getXmly().equals("1")) {
            //zdd 当项目来源不为不动产登记项目时   读取过渡数据权利人
            List<GdQlr> gdQlrs = gdXmService.getGdqlrByQlid(project.getYqlid(), Constants.QLRLX_YWR);
            bdcQlrList = gdQlrService.readGdQlrs(gdQlrs);
        }
        return bdcQlrList;
    }

    protected BdcBdcdy initBdcdyMul(InitVoFromParm initVoFromParm, BdcBdcdjb bdcdjb) {
        BdcBdcdy bdcdy = null;
        Project project = initVoFromParm.getProject();
        //zdd 如果不动产单元号为空  则不生成
        if (project != null && StringUtils.isNotBlank(project.getBdcdyh())) {
            bdcdy = new BdcBdcdy();
            if (bdcdjb != null && StringUtils.isNotBlank(bdcdjb.getDjbid()))
                bdcdy.setDjbid(bdcdjb.getDjbid());
            bdcdy = bdcdyService.getBdcdyFromProject(project, bdcdy);
            bdcdy = bdcdyService.getBdcdyFromFw(initVoFromParm.getDjsjFwxx(), bdcdy);
            if (StringUtils.isNotBlank(project.getYxmid())) {
                bdcdy = bdcdyService.getBdcdyFromYProject(project, bdcdy);
            }
            Example example = new Example(BdcBdcdy.class);
            example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, project.getProid()).andEqualTo("bdcdyh", project.getBdcdyh());
            entityMapper.deleteByExample(BdcBdcdy.class, example);
            bdcdy.setBdcdyid(UUIDGenerator.generate());
        }

        return bdcdy;
    }

    /**
     * zhx 根据 djsjZhxxList 初始化权利人
     *
     * @param project
     * @param djsjZhxxList
     * @param qlrlx
     * @return
     */
    protected List<BdcQlr> initBdcQlrFromZh(final Project project, final List<DjsjZhxx> djsjZhxxList, String qlrlx) {
        List<BdcQlr> bdcQlrList = new ArrayList<BdcQlr>();
        if (CollectionUtils.isNotEmpty(djsjZhxxList)) {
            for (DjsjZhxx zhxx : djsjZhxxList) {
                BdcQlr bdcQlr = new BdcQlr();
                bdcQlr = bdcQlrService.getBdcQlrFromProject(project, bdcQlr);
                bdcQlr = bdcQlrService.getBdcQlrFromZh(zhxx, bdcQlr);
                bdcQlr.setQlrlx(qlrlx);
                if (StringUtils.isNotBlank(bdcQlr.getQlrmc()))
                    bdcQlrList.add(bdcQlr);
            }
        }
        return bdcQlrList;
    }

    /**
     * @author:<a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @data:2016/3/16
     * @param: project
     * @return: List<BdcQlr>
     * @description: 通过project获取过度的权利人和义务人，过程匹配权利人和义务人不变
     */
    protected List<BdcQlr> keepQlrByGcPp(Project project) {
        List<BdcQlr> ybdcQlrList = null;
        if (StringUtils.equals(project.getMsg(), "gl")) {
            //zwq 这个是为了关联项目的时候填了权利人，则不要取权利人，若没有,则取过渡的权利人
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isEmpty(bdcQlrList)) {
                ybdcQlrList = getYbdcQlrList(project);
            }
        } else {
            ybdcQlrList = getYbdcQlrList(project);
        }
        List<BdcQlr> ybdcYwrList = getYbdcYwrList(project);
        List<BdcQlr> ybdcJkrList = getYbdcJkrList(project);
        List<BdcQlr> list = new ArrayList<BdcQlr>();

        //权利人变权利人
        if (CollectionUtils.isNotEmpty(ybdcQlrList)) {
            List<BdcQlr> tempBdcQlrList = bdcQlrService.queryBdcQlrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcQlrList)) {
                for (BdcQlr bdcYwr : tempBdcQlrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_QLR);
                }
            }
            for (BdcQlr bdcQlr : ybdcQlrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectQlr(bdcQlr, tempBdcQlrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        //义务人变义务人
        if (CollectionUtils.isNotEmpty(ybdcYwrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_YWR);
                }
            }
            for (BdcQlr bdcQlr : ybdcYwrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectYwr(bdcQlr, tempBdcYwrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        //义务人变义务人
        if (CollectionUtils.isNotEmpty(ybdcJkrList)) {
            List<BdcQlr> tempBdcYwrList = bdcQlrService.queryBdcQlrYwrByProid(project.getProid());
            if (CollectionUtils.isNotEmpty(tempBdcYwrList)) {
                for (BdcQlr bdcYwr : tempBdcYwrList) {
                    bdcQlrService.delBdcQlrByQlrid(bdcYwr.getQlrid(), Constants.QLRLX_JKR);
                }
            }
            for (BdcQlr bdcQlr : ybdcJkrList) {
                bdcQlr = bdcQlrService.qlrTurnProjectJkr(bdcQlr, tempBdcYwrList, project.getProid());
                list.add(bdcQlr);
            }
        }
        return list;
    }


    @Override
    public void glBdcdyh(final Project project) {
        if (StringUtils.isNotBlank(project.getBdcdyh()) && StringUtils.isNotBlank(project.getProid())) {
            String bdcdyid = "";
            List<InsertVo> list = new ArrayList<InsertVo>();
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 初始化不动产多元号
             */
            String zl = "";
            HashMap map = initBdcdy(list, project);
            if (map.get(ParamsConstants.BDCDYID_LOWERCASE) != null) {
                bdcdyid = map.get(ParamsConstants.BDCDYID_LOWERCASE).toString();
            }
            if (map.get("list") != null) {
                list = (List<InsertVo>) map.get("list");
            }
            if (map.get("zl") != null) {
                zl = map.get("zl").toString();
            }
            /**
             * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
             * @description 初始化其他需要修改的表
             */
            initQt(list, project.getProid(), bdcdyid);
            updateQt(list, project.getProid(), zl);
            saveOrUpdateProjectData(list);
        }
    }


    /**
     * @param list,project
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 初始化不动产多元号
     */
    private HashMap initBdcdy(List<InsertVo> list, Project project) {
        HashMap map = new HashMap();
        BdcXm bdcxm = bdcXmService.getBdcXmByProid(project.getProid());
        BdcBdcdjb bdcdjb = null;
        String zl = "";
        InitVoFromParm initVoFromParm = getDjxx(project);
        if (CollectionUtils.isNotEmpty(initVoFromParm.getDjsjZdxxList())) {
            DjsjZdxx djsjZdxx = initVoFromParm.getDjsjZdxxList().get(0);
            if (StringUtils.isNotBlank(djsjZdxx.getTdzl())) {
                zl = djsjZdxx.getTdzl();
            }
        }
        if (StringUtils.isNotBlank(project.getZdzhh())) {
            List<BdcBdcdjb> bdcdjbTemps = bdcDjbService.selectBdcdjb(project.getZdzhh());
            if (CollectionUtils.isEmpty(bdcdjbTemps)) {
                bdcdjb = initBdcdjb(initVoFromParm);
                list.add(bdcdjb);
            } else {
                bdcdjb = bdcdjbTemps.get(0);
            }
            BdcTd bdcTd = bdcTdService.selectBdcTd(project.getZdzhh());
            if (bdcTd == null) {
                bdcTd = bdcTdService.getBdcTdFromDjxx(initVoFromParm.getDjsjZdxx(), initVoFromParm.getDjsjQszdDcbList(), initVoFromParm.getDjsjNydDcbList(), project, bdcxm.getQllx());
                list.add(bdcTd);
            }
            BdcBdcdy bdcdy = initBdcdy(initVoFromParm, bdcdjb);
            list.add(bdcdy);
            if (bdcdy != null) {
                map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdy.getBdcdyid());
            }
            if (CollectionUtils.isNotEmpty(list)) {
                map.put("list", list);
            }
            if (StringUtils.isNotBlank(zl)) {
                map.put("zl", zl);
            }
        }
        return map;
    }

    private List<InsertVo> updateQt(List<InsertVo> list, final String proid, final String zl) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (InsertVo insertVo : list) {
                if (insertVo instanceof BdcXm) {
                    ((BdcXm) insertVo).setZl(zl);
                }
                if (insertVo instanceof BdcSpxx) {
                    ((BdcSpxx) insertVo).setZl(zl);
                }
            }
        }
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            List<BdcXm> bdcXmList = bdcXmService.getBdcXmListByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm1 : bdcXmList) {
                    bdcXm1.setZl(zl);
                    entityMapper.saveOrUpdate(bdcXm1, bdcXm1.getProid());
                }
            }
            List<BdcSpxx> bdcSpxxList = bdcSpxxService.getBdcSpxxByWiid(bdcXm.getWiid());
            if (CollectionUtils.isNotEmpty(bdcSpxxList)) {
                for (BdcSpxx bdcSpxx : bdcSpxxList) {
                    bdcSpxx.setZl(zl);
                    entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
                }
            }
        }
        return list;
    }

    /**
     * @param list,project,bdcdyid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @rerutn
     * @description 初始化其他需要修改的表
     */
    private List<InsertVo> initQt(List<InsertVo> list, final String proid, final String bdcdyid) {
        BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
        if (bdcXm != null) {
            bdcXm.setBdcdyid(bdcdyid);
            list.add(bdcXm);
        }
        QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
        HashMap map = new HashMap();
        map.put(ParamsConstants.PROID_LOWERCASE, proid);
        List<QllxVo> qllxVos = qllxService.andEqualQueryQllx(qllxVo, map);
        if (CollectionUtils.isNotEmpty(qllxVos)) {
            qllxVo = qllxVos.get(0);
            qllxVo.setBdcdyid(bdcdyid);
            list.add(qllxVo);
        }
        return list;
    }

    /**
     * @param bdcXm 不动产项目
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @rerutn
     * @description 复制不动产项目，生成新的项目
     */
    @Override
    public List<InsertVo> copyBdcxxListFromBdcxm(BdcXm bdcXm) {
        List<InsertVo> bdcxxList = new ArrayList<InsertVo>();
        String proid = UUIDGenerator.generate();
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {

            List<BdcSjxx> bdcSjxxList = bdcSjdService.queryBdcSjdByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcSjxxList)) {
                for (BdcSjxx bdcSjxx : bdcSjxxList) {
                    bdcSjxx.setProid(proid);
                    bdcSjxx.setSjxxid(UUIDGenerator.generate());
                    bdcxxList.add(bdcSjxx);
                }
            }

            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
            if (bdcSpxx != null) {
                bdcSpxx.setProid(proid);
                bdcSpxx.setSpxxid(UUIDGenerator.generate());
                bdcxxList.add(bdcSpxx);
            }
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    bdcQlr.setProid(proid);
                    bdcQlr.setQlrid(UUIDGenerator.generate());
                    bdcxxList.add(bdcQlr);
                }
            }
            QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
            if (qllxVo != null) {
                qllxVo.setProid(proid);
                qllxVo.setQlid(UUIDGenerator.generate());
                bdcxxList.add(qllxVo);
            }

            List<BdcXmRel> xmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(xmRelList)) {
                for (BdcXmRel xmrel : xmRelList) {
                    xmrel.setProid(proid);
                    xmrel.setRelid(UUIDGenerator.generate());
                    bdcxxList.add(xmrel);
                }
            }

            bdcXm.setProid(proid);
            bdcxxList.add(bdcXm);
        }
        return bdcxxList;
    }


    /**
     * @description 从过渡库继承预告信息
     */
    private List<InsertVo> initYgFromGd(Project project, List<GdYg> gdYgList) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (CollectionUtils.isNotEmpty(gdYgList)) {
            for (int i = 0; i < gdYgList.size(); i++) {
                Project ygProject = new Project();
                try {
                    BeanUtils.copyProperties(ygProject, project);
                    ygProject.setYqlid(gdYgList.get(i).getYgid());
                    ygProject.setProid(UUIDGenerator.generate18());
                    ygProject.setGdproid(gdYgList.get(i).getProid());

                    ygProject.setQllx(Constants.QLLX_YGDJ);

                    //带入预告抵押djsy应该是抵押权
                    if (StringUtils.isNotBlank(gdYgList.get(i).getYgdjzl()) &&
                            (StringUtils.equals(gdYgList.get(i).getYgdjzl(), "3") || StringUtils.equals(gdYgList.get(i).getYgdjzl(), "4"))) {
                        ygProject.setDjsy(Constants.DJSY_DYAQ);
                    } else {
                        ygProject.setDjsy(Constants.DJSY_QT);
                    }

                    ygProject.setDjlx(Constants.DJLX_YGDJ_DM);

                    List<InsertVo> yglist = creatProjectYgBgdjService.initVoFromOldData(ygProject);
                    for (InsertVo vo : yglist) {
                        if (vo instanceof BdcXm) {
                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                            if (bdcSjxx != null) {
                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                insertVoList.add(bdcSjxx);
                            }
                            ((BdcXm) vo).setSqlx(Constants.SQLX_YG_GFYG);
                        }
                        insertVoList.add(vo);
                    }
                } catch (Exception e) {
                    logger.error("CreatProjectDefaultServiceImpl.initYgFromGd", e);
                }
            }
        }
        return insertVoList;
    }

    /**
     * @description 从过渡库继承抵押信息
     */
    private List<InsertVo> initDyFromGd(Project project, List<GdDy> gdDyList) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (CollectionUtils.isNotEmpty(gdDyList)) {
            for (int i = 0; i < gdDyList.size(); i++) {
                Project dyProject = new Project();
                try {
                    BeanUtils.copyProperties(dyProject, project);
                    dyProject.setYqlid(gdDyList.get(i).getDyid());
                    dyProject.setProid(UUIDGenerator.generate18());
                    dyProject.setGdproid(gdDyList.get(i).getProid());

                    dyProject.setQllx(Constants.QLLX_DYAQ);
                    dyProject.setDjsy(Constants.DJSY_DYAQ);
                    dyProject.setDjlx(Constants.DJLX_BGDJ_DM);
                    List<InsertVo> dylist = creatProjectDyBgdjService.initVoFromOldData(dyProject);

                    for (InsertVo vo : dylist) {
                        if (vo instanceof BdcXm) {
                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                            if (bdcSjxx != null) {
                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                insertVoList.add(bdcSjxx);
                            }
                            ((BdcXm) vo).setSqlx(Constants.SQLX_DY_GDDY);
                        }
                        insertVoList.add(vo);
                    }
                } catch (Exception e) {
                    logger.error("CreatProjectDefaultServiceImpl.initDyFromGd", e);
                }
            }
        }
        return insertVoList;
    }

    /**
     * @description 从过渡库初始化查封
     */
    private List<InsertVo> initCfFromGd(Project project, List<GdCf> gdCfList) {
        List<InsertVo> insertVoList = new ArrayList<InsertVo>();
        if (CollectionUtils.isNotEmpty(gdCfList)) {
            for (int i = 0; i < gdCfList.size(); i++) {
                Project cfProject = new Project();
                try {
                    BeanUtils.copyProperties(cfProject, project);
                    cfProject.setYqlid(gdCfList.get(i).getCfid());
                    cfProject.setProid(UUIDGenerator.generate18());
                    cfProject.setGdproid(gdCfList.get(i).getProid());
                    //修改权利类型
                    cfProject.setQllx(Constants.QLLX_CFDJ);
                    cfProject.setDjlx(Constants.DJLX_CFDJ_DM);
                    List<InsertVo> cflist = creatProjectCfdjService.initVoFromOldData(cfProject);

                    for (InsertVo vo : cflist) {
                        if (vo instanceof BdcXm) {
                            BdcSjxx bdcSjxx = bdcSjdService.createSjxxByBdcxmByProid((BdcXm) vo);
                            if (bdcSjxx != null) {
                                bdcSjxx.setSjxxid(UUIDGenerator.generate());
                                insertVoList.add(bdcSjxx);
                            }
                            ((BdcXm) vo).setSqlx(Constants.SQLX_CF_GDCF);
                        }
                        insertVoList.add(vo);
                    }

                } catch (Exception e) {
                    logger.error("CreatProjectDefaultServiceImpl.initCfFromGd", e);
                }
            }
        }
        return insertVoList;
    }

    @Override
    public List<InsertVo> copyBdcxxListFromBdcxm(BdcXm bdcXm, String yproid) {
        List<InsertVo> bdcxxList = new ArrayList<InsertVo>();
        if (StringUtils.isNotBlank(yproid) && bdcXm != null && StringUtils.isNoneBlank(bdcXm.getProid())) {
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
            //删除原spxx
            BdcSpxx ybdcSpxx = bdcSpxxService.queryBdcSpxxByProid(yproid);
            if (ybdcSpxx != null) {
                entityMapper.deleteByPrimaryKey(BdcSpxx.class, ybdcSpxx.getSpxxid());
            }
            if (bdcSpxx != null) {
                bdcSpxx.setProid(yproid);
                bdcSpxx.setSpxxid(UUIDGenerator.generate());
                bdcxxList.add(bdcSpxx);
            }
            //删除原dyaq
            Example ybdcDyaqExample = new Example(BdcDyaq.class);
            ybdcDyaqExample.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, yproid);
            List<BdcDyaq> ybdcDyaqList = entityMapper.selectByExample(BdcDyaq.class, ybdcDyaqExample);
            if (CollectionUtils.isNotEmpty(ybdcDyaqList) && StringUtils.isNotBlank(yproid)) {
                Example example = new Example(BdcBdcdy.class);
                example.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, yproid);
                entityMapper.deleteByExample(BdcDyaq.class, example);
            }
            Example bdcDyaqExample = new Example(BdcDyaq.class);
            bdcDyaqExample.createCriteria().andEqualTo(ParamsConstants.PROID_LOWERCASE, bdcXm.getProid());
            List<BdcDyaq> bdcDyaqList = entityMapper.selectByExample(BdcDyaq.class, bdcDyaqExample);
            if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                for (BdcDyaq bdcDyaq : bdcDyaqList) {
                    bdcDyaq.setProid(yproid);
                    bdcDyaq.setQlid(UUIDGenerator.generate());
                    entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                }
            }
        }
        return bdcxxList;
    }

}
