package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.model.server.print.DataToPrintXml;
import cn.gtmap.estateplat.model.server.print.XmlData;
import cn.gtmap.estateplat.server.core.mapper.BdcQlrMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcXmMapper;
import cn.gtmap.estateplat.server.core.mapper.BdcZsMapper;
import cn.gtmap.estateplat.server.core.model.MulDataToPrintXml;
import cn.gtmap.estateplat.server.core.model.OntBdcXm;
import cn.gtmap.estateplat.server.core.model.PageXml;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.server.utils.PublicUtil;
import cn.gtmap.estateplat.service.server.BdcXmxxService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysCalendarService;
import com.gtis.plat.service.SysSignService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfSignVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.model.ActivityModel;
import com.gtis.web.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 不动产项目
 * Created by lst on 2015/3/17.
 */
@Repository
public class BdcXmServiceImpl implements BdcXmService, BdcXmxxService {

    @Autowired
    BdcXmMapper bdcXmMapper;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    BdcCheckCancelService bdcCheckCancelService;
    @Autowired
    DjsjFwService djsjFwService;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZdGlService bdcZdGlService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private BdcTzmDjsjRelService bdcTzmDjsjRelService;
    @Autowired
    private BdcSqlxDjsyRelService bdcSqlxDjsyRelService;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdXmService gdXmService;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private SysCalendarService sysCalendarService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    BdcSlbhCustomServiceContext bdcSlbhCustomServiceContext;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcYgService bdcYgService;
    @Autowired
    private SysSignService sysSignService;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    private GdDyService gdDyService;
    @Autowired
    private DwxxService dwxxService;
    @Autowired
    private BdcZsMapper bdcZsMapper;
    @Autowired
    private BdcQlrMapper bdcQlrMapper;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private GdCfService gdCfService;
    @Autowired
    private BdcGdDyhRelService bdcGdDyhRelService;

    private static final String UPPERCASE_PROID = "PROID";
    private static final String LOWERCASE_PROID = "proid";
    private static final String WIID = "wiid";
    private static final String SQLXDM = "SQLXDM";
    private static final String DJLXDM = "DJLXDM";
    private static final String QLLX = "QLLX";
    private static final String DYDJLX = "DYDJLX";
    private static final String DJSYDM = "DJSYDM";
    private static final String QLLXDM = "QLLXDM";
    private static final String UPPERCASE_BDCDYH = "BDCDYH";
    private static final String LOWERCASE_BDCDYH = "bdcdyh";
    private static final String PRINTTYPE = "printType";
    private static final String TDSYKSQX = "TDSYKSQX";
    private static final String TDSYJSQX = "TDSYJSQX";
    private static final String TDORTDFWMJ = "tdOrTdfwMj";
    private static final String TDORTDFWYT = "tdOrTdfwYt";
    private static final String TDORTDFWQLXZ = "tdOrTdfwQlxz";
    private static final String UPPERCASE_TDORTDFWMJ = "TDORTDFWMJ";
    private static final String UPPERCASE_TDORTDFWYT = "TDORTDFWYT";
    private static final String UPPERCASE_TDORTDFWQLXZ = "TDORTDFWQLXZ";
    private static final String ZWLXKSQX = "ZWLXKSQX";
    private static final String ZWLXJSQX = "ZWLXJSQX";
    private static final String ZJGCDYFW = "ZJGCDYFW";
    private static final String BDBZZQSE = "BDBZZQSE";
    private static final String DBFW = "DBFW";
    private static final String ZDZHQLXZ = "ZDZHQLXZ";
    private static final String FWDYMJ = "FWDYMJ";
    private static final String TDDYMJ = "TDDYMJ";
    private static final String TDDYJG = "TDDYJG";
    private static final String FWDYJG = "FWDYJG";
    private static final String QLQTZK = "QLQTZK";
    private static final String FJ = "FJ";
    private static final String QLR = "QLR";
    private static final String QLRSFZJZL = "QLRSFZJZL";
    private static final String QLRZJH = "QLRZJH";
    private static final String QLRQLBL = "QLRQLBL";
    private static final String GYQK = "GYQK";
    private static final String QLRLXDH = "QLRLXDH";
    private static final String QLRDLR = "QLRDLR";
    private static final String QLRDLRZJZL = "QLRDLRZJZL";
    private static final String QLRDLRZJH = "QLRDLRZJH";
    private static final String QLRDLRDH = "QLRDLRDH";
    private static final String YWR = "YWR";
    private static final String YWRSFZJZL = "YWRSFZJZL";
    private static final String YWRZJH = "YWRZJH";
    private static final String YWRQLBL = "YWRQLBL";
    private static final String YWRLXDH = "YWRLXDH";
    private static final String YWRDLR = "YWRDLR";
    private static final String YWRDLRZJZL = "YWRDLRZJZL";
    private static final String YWRDLRZJH = "YWRDLRZJH";
    private static final String YWRDLRDH = "YWRDLRDH";
    private static final String BDCLX = "BDCLX";
    private static final String SQZSBS = "SQZSBS";
    private static final String STRING = "String";
    private static final String SQFBCZ = "SQFBCZ";
    private static final String YBDCQZH = "YBDCQZH";

    private static final String CSSIGNDATE = "CSSIGNDATE";
    private static final String FSSIGNDATE = "FSSIGNDATE";
    private static final String HDSIGNDATE = "HDSIGNDATE";
    private static final String CSOPINION = "CSOPINION";
    private static final String FSOPINION = "FSOPINION";
    private static final String HDOPINION = "HDOPINION";
    private static final FastDateFormat SLBH_DATE_FORMAT = FastDateFormat.getInstance("yyyy年MM月dd日");

    @Override
    public synchronized String creatXmbh(BdcXm bdcXm) {
        String xmbh = "";
        xmbh = bdcSlbhCustomServiceContext.getSlbhService().generateBdcXmSlbh(bdcXm);
        return xmbh;
    }

    @Override
    public BdcXm getBdcXmFromProject(Project project) {
        BdcXm bdcXm = new BdcXm();
        if (project != null) {
            if (StringUtils.isNotBlank(project.getBh())) {
                bdcXm.setBh(project.getBh());
            }
            if (StringUtils.isNotBlank(project.getGdslbh())) {
                bdcXm.setYbh(project.getGdslbh());
            }
            if (project.getBjsj() != null) {
                bdcXm.setBjsj(project.getBjsj());
            }
            if (StringUtils.isNotBlank(project.getBz())) {
                bdcXm.setBz(project.getBz());
            }
            if (StringUtils.isNotBlank(project.getCjr())) {
                bdcXm.setCjr(project.getCjr());
            }
            if (StringUtils.isNotBlank(project.getDjlx())) {
                bdcXm.setDjlx(project.getDjlx());
            }
            if (project.getCjsj() != null) {
                bdcXm.setCjsj(project.getCjsj());
            }
            if (StringUtils.isNotBlank(project.getDjyy())) {
                bdcXm.setDjyy(project.getDjyy());
            }
            if (StringUtils.isNotBlank(project.getDwdm())) {
                bdcXm.setDwdm(project.getDwdm());
            }
            if (StringUtils.isNotBlank(project.getLsh())) {
                bdcXm.setLsh(project.getLsh());
            }
            if (StringUtils.isNotBlank(project.getNf())) {
                bdcXm.setNf(project.getNf());
            }
            if (StringUtils.isNotBlank(project.getProid())) {
                bdcXm.setProid(project.getProid());
            }
            if (StringUtils.isNotBlank(project.getDjyy())) {
                bdcXm.setDjyy(project.getDjyy());
            }
            if (StringUtils.isNotBlank(project.getSqfbcz())) {
                bdcXm.setSqfbcz(project.getSqfbcz());
            }
            if (StringUtils.isNotBlank(project.getSqzsbs())) {
                bdcXm.setSqzsbs(project.getSqzsbs());
            }

            if (StringUtils.isNotBlank(project.getSqrsm())) {
                bdcXm.setSqrsm(project.getSqrsm());
            }
            if (StringUtils.isNotBlank(project.getXmly())) {
                bdcXm.setXmly(project.getXmly());
            }
            if (StringUtils.isNotBlank(project.getXmmc())) {
                bdcXm.setXmmc(project.getXmmc());
            }
            if (StringUtils.isNotBlank(project.getXmzt())) {
                bdcXm.setXmzt(project.getXmzt());
            }
            if (StringUtils.isNotBlank(project.getQllx())) {
                String qllx = project.getQllx();
                //如果在集合中的sqlx的qllx读取权籍的QSXZ并通过对照表
                if (StringUtils.isNotBlank(project.getSqlx()) && CommonUtil.indexOfStrs(Constants.SQLX_QLLX_QJ, project.getSqlx())) {
                    String qsxz = bdcDjsjService.getDjsjQsxz(project.getBdcdyh());
                    if (StringUtils.isNotBlank(qsxz)) {
                        qllx = qsxz;
                    } else {
                        bdcXm.setQllx(project.getQllx());
                    }
                }
                bdcXm.setQllx(qllx);
            }

            if (StringUtils.isNotBlank(project.getBdcdyid())) {
                bdcXm.setBdcdyid(project.getBdcdyid());
            }
            if (StringUtils.isNotBlank(project.getSqlx())) {
                bdcXm.setSqlx(project.getSqlx());
            }
            if (StringUtils.isNotBlank(project.getDjsy())) {
                bdcXm.setDjsy(project.getDjsy());
            }

            if (StringUtils.isNotBlank(project.getWiid())) {
                bdcXm.setWiid(project.getWiid());
            }
            if (project.getCjsj() != null) {
                bdcXm.setCjsj(project.getCjsj());
            } else {
                bdcXm.setCjsj(new Date());
            }
            bdcXm.setBdclx(project.getBdclx());
            if (StringUtils.isNotBlank(project.getXmly())) {
                bdcXm.setXmly(project.getXmly());
            }
            if (StringUtils.isNotBlank(project.getYbdcqzh())) {
                bdcXm.setYbdcqzh(project.getYbdcqzh());
            }
            if (StringUtils.isNotBlank(project.getYbh())) {
                bdcXm.setYbh(project.getYbh());
            }
            if (StringUtils.isNotBlank(project.getBlyzh())) {
                bdcXm.setBlyzh(project.getBlyzh());
            }
            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                bdcXm.setDwmc(dwxxService.getDwmcByDwdm(project.getBdcdyh().substring(0, 9)));
            }
        }
        return bdcXm;
    }

    @Override
    public Project getProjectFromBdcXm(BdcXm bdcXm, Project project) {
        if (project == null)
            project = new Project();
        if (bdcXm != null) {
            if (StringUtils.isNotBlank(bdcXm.getBdclx()))
                project.setBdclx(bdcXm.getBdclx());
            if (StringUtils.isNotBlank(bdcXm.getBh()))
                project.setBh(bdcXm.getBh());
            if (bdcXm.getBjsj() != null)
                project.setBjsj(bdcXm.getBjsj());
            if (StringUtils.isNotBlank(bdcXm.getBz()))
                project.setBz(bdcXm.getBz());
            if (StringUtils.isNotBlank(bdcXm.getCjr()))
                project.setCjr(bdcXm.getCjr());
            if (StringUtils.isNotBlank(bdcXm.getDjlx()))
                project.setDjlx(bdcXm.getDjlx());
            if (bdcXm.getCjsj() != null)
                project.setCjsj(bdcXm.getCjsj());
            if (StringUtils.isNotBlank(bdcXm.getDjyy()))
                project.setDjyy(bdcXm.getDjyy());
            if (StringUtils.isNotBlank(bdcXm.getDwdm()))
                project.setDwdm(bdcXm.getDwdm());
            if (StringUtils.isNotBlank(bdcXm.getWiid()))
                project.setWiid(bdcXm.getWiid());
            if (StringUtils.isNotBlank(bdcXm.getLsh()))
                project.setLsh(bdcXm.getLsh());
            if (StringUtils.isNotBlank(bdcXm.getNf()))
                project.setNf(bdcXm.getNf());
            if (StringUtils.isNotBlank(bdcXm.getProid()))
                project.setProid(bdcXm.getProid());
            if (StringUtils.isNotBlank(bdcXm.getDjyy()))
                project.setDjyy(bdcXm.getDjyy());
            if (StringUtils.isNotBlank(bdcXm.getSqfbcz()))
                project.setSqfbcz(bdcXm.getSqfbcz());
            if (StringUtils.isNotBlank(bdcXm.getSqzsbs()))
                project.setSqzsbs(bdcXm.getSqzsbs());

            if (StringUtils.isNotBlank(bdcXm.getSqrsm()))
                project.setSqrsm(bdcXm.getSqrsm());
            if (StringUtils.isNotBlank(bdcXm.getXmly()))
                project.setXmly(bdcXm.getXmly());
            if (StringUtils.isNotBlank(bdcXm.getXmmc()))
                project.setXmmc(bdcXm.getXmmc());
            if (StringUtils.isNotBlank(bdcXm.getXmzt()))
                project.setXmzt(bdcXm.getXmzt());
            if (StringUtils.isNotBlank(bdcXm.getQllx()))
                project.setQllx(bdcXm.getQllx());
            if (StringUtils.isNotBlank(bdcXm.getBdcdyid()))
                project.setBdcdyid(bdcXm.getBdcdyid());
            if (StringUtils.isNotBlank(bdcXm.getSqlx()))
                project.setSqlx(bdcXm.getSqlx());
            if (StringUtils.isNotBlank(bdcXm.getDjsy()))
                project.setDjsy(bdcXm.getDjsy());
            if (bdcXm.getCjsj() != null) {
                project.setCjsj(bdcXm.getCjsj());
            } else {
                project.setCjsj(new Date());
            }
            if (StringUtils.isNotBlank(bdcXm.getXmly()))
                project.setXmly(bdcXm.getXmly());

            if (StringUtils.isNotBlank(bdcXm.getYbh()))
                project.setYbh(bdcXm.getYbh());
            if (StringUtils.isNotBlank(bdcXm.getBlyzh()))
                project.setBlyzh(bdcXm.getBlyzh());

            /**
             * @author bianwen
             * @description 查封强制验证要判断登记子项
             */
            if (StringUtils.isNotBlank(bdcXm.getDjzx()))
                project.setDjzx(bdcXm.getDjzx());

            if (StringUtils.isNotBlank(bdcXm.getYcslywh())) {
                project.setYcslywh(bdcXm.getYcslywh());
            }

        }
        return project;
    }

    @Override
    public BdcXm getBdcXmByProid(String proid) {
        BdcXm bdcXm = null;
        if (StringUtils.isNotBlank(proid)) {
            bdcXm = bdcXmMapper.getBdcXmByProid(proid);
        }
        return bdcXm;
    }

    @Override
    public void delBdcXmByProid(String proid) {
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo(LOWERCASE_PROID, proid);
            entityMapper.deleteByExample(BdcXm.class, example);
        }
    }

    @Override
    public void batchUpdateBdcXmLzrq(List<BdcXm> bdcXmList, Date date) {
        if (CollectionUtils.isNotEmpty(bdcXmList) && date != null) {
            for (BdcXm bdcXm : bdcXmList) {
                if (StringUtils.isNotBlank(bdcXm.getProid())) {
                    bdcXm.setLzrq(date);
                    saveBdcXm(bdcXm);
                }
            }
        }
    }

    @Override
    public BdcXm endBdcXm(BdcXm bdcXm) {
        if (bdcXm != null) {
            bdcXm.setBjsj(DateUtils.now());
            bdcXm.setXmzt("1");
            entityMapper.updateByPrimaryKeySelective(bdcXm);
        }
        return bdcXm;
    }

    @Override
    public BdcXm newBdcxm(final Project project) {
        BdcXm bdcXm = new BdcXm();
        BdcXm yBdcXm = null;
        if (project != null) {
            if (StringUtils.isBlank(project.getUserId())) {
                project.setUserId(SessionUtil.getCurrentUserId());
            }
            if (StringUtils.isNotBlank(project.getUserId())) {
                project.setDwdm(PlatformUtil.getCurrentUserDwdmByUserid(project.getUserId()));
                project.setCjr(PlatformUtil.getCurrentUserName(project.getUserId()));
            } else {
                project.setDwdm(SessionUtil.getCurrentUser().getRegionCode());
                project.setCjr(SessionUtil.getCurrentUser().getUsername());
            }
            bdcXm = getBdcXmFromProject(project);
            bdcXm.setXmzt("0");
            if (StringUtils.isBlank(bdcXm.getBh()))
                bdcXm.setBh(creatXmbh(bdcXm));

            if (StringUtils.isBlank(bdcXm.getProid()))
                bdcXm.setProid(UUIDGenerator.generate18());
            //获取原项目相关信息
            if (yBdcXm == null && StringUtils.isNotBlank(project.getYxmid())) {
                yBdcXm = getBdcXmByProid(project.getYxmid());
            }
            getYxmInfo(bdcXm, yBdcXm, project);

            //处理bdcXm的djsy
            dealBdcXmDjsy(project, bdcXm);

            //zhouwanqing 查封这块可能有匹配创建的情况，所以单独放出来
            if (StringUtils.equals(project.getDjlx(), Constants.DJLX_CFDJ_DM) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD) || !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_SFCD_PL)) {
                String djzx = bdcCfService.getCfDjzx(project, yBdcXm);
                bdcXm.setDjzx(djzx);
            }
            bdcXm = getDydjlx(bdcXm);

            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 多本房产证匹配同一个不动产单元，完善房产证号
             */
            if (project != null && StringUtils.isNotBlank(project.getXmly()) && StringUtils.equals(project.getXmly(), Constants.XMLY_FWSP)) {
                addFfczh(project);
            }
            //完善处理bdcXm的ybdcqzmh
            String ybdcqzmh = completeBdcXmYbdcqzmh(project, bdcXm);
            bdcXm.setYbdcqzmh(ybdcqzmh);

            //完善处理bdcXm的ybdcqzh
            String ybdcqzh = completeBdcXmYbdcqzh(project, bdcXm);
            bdcXm.setYbdcqzh(ybdcqzh);

            bdcXm = initBdcXmFromOntBdcXm(bdcXm, project.getProid());
            //创建bdc_xm时将Yfczh赋值,作为证书权利其他状态中的原不动产权证号
            //加上qllx的条件是因为合并流程中涉及到抵押变更的也需要改变yfczh
            String yfczh = getBdcXmYfczh(project.getYxmid(), bdcXm);
            if (StringUtils.isNotBlank(yfczh)) {
                bdcXm.setYfczh(yfczh);
            }
        }
        bdcXm.setCjsj(new Date());
        Date lzrq = getLzrqByWiid(project.getWiid(), bdcXm);
        if (lzrq != null) {
            bdcXm.setLzrq(lzrq);
        }
        return bdcXm;
    }

    @Override
    public BdcXm getYxmInfo(BdcXm bdcXm, BdcXm yBdcXm, Project project) {
        if (StringUtils.isNotBlank(project.getYxmid())) {
            if (yBdcXm != null) {
                if (StringUtils.equals(project.getDjlx(), Constants.DJLX_CFDJ_DM) || StringUtils.equals(project.getDjlx(), Constants.DJLX_YGDJ_DM) || StringUtils.equals(project.getDjlx(), Constants.DJLX_YYDJ_DM) || StringUtils.equals(project.getDjlx(), Constants.DJLX_DYDJ_DM)) {
                    if (StringUtils.isNotBlank(yBdcXm.getQllx()) && StringUtils.isBlank(bdcXm.getDjsy())) {
                        bdcXm.setDjsy(yBdcXm.getDjsy());
                    }
                    //获取原项目的登记子项
                    if (StringUtils.equals(project.getDjlx(), Constants.DJLX_YYDJ_DM)) {
                        bdcXm.setDjzx("");
                    } else {
                        if (StringUtils.isNotBlank(yBdcXm.getDjzx()) && StringUtils.isBlank(bdcXm.getDjzx()) && (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZYDJ, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, bdcXm.getSqlx()))) {
                            bdcXm.setDjzx(yBdcXm.getDjzx());
                        }
                    }
                }
                if (StringUtils.equals(project.getDjlx(), Constants.DJLX_GZDJ_DM) && StringUtils.isNotBlank(yBdcXm.getQllx())) {
                    bdcXm.setDjsy(yBdcXm.getDjsy());
                }
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GYJSYDHB_BGDJ)) {
                    StringBuilder ybh = new StringBuilder();
                    if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                        for (BdcXmRel bdcXmRel : project.getBdcXmRelList()) {
                            BdcXm yXm = getBdcXmByProid(bdcXmRel.getYproid());
                            if (yXm != null) {
                                if (StringUtils.isBlank(ybh)) {
                                    ybh.append(yXm.getBh());
                                } else {
                                    ybh.append("," + yXm.getBh());
                                }
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(ybh)) {
                        bdcXm.setYbh(String.valueOf(ybh));
                    }
                } else {
                    if (StringUtils.isNotBlank(yBdcXm.getBh())) {
                        bdcXm.setYbh(yBdcXm.getBh());
                    }
                }
                if (StringUtils.isBlank(project.getQllx()) && StringUtils.isBlank(bdcXm.getQllx())) {
                    bdcXm.setQllx(yBdcXm.getQllx());
                }
                if (StringUtils.isBlank(bdcXm.getDjsy())) {
                    String djsy = bdcXmMapper.getBdcQllxDjsyRel(bdcXm.getQllx());
                    if (StringUtils.isNotBlank(djsy)) {
                        bdcXm.setDjsy(djsy);
                    }
                }
                if (StringUtils.isNotBlank(yBdcXm.getBdclx()) && StringUtils.isBlank(bdcXm.getBdclx())) {
                    bdcXm.setBdclx(yBdcXm.getBdclx());
                }
            }
        } else if (StringUtils.isNotBlank(project.getGdproid()) && CommonUtil.indexOfStrs(Constants.DJLX_DYAQ_ZXDJ_NEEDDJZX_SQLXDM, bdcXm.getSqlx()) && StringUtils.isNotBlank(project.getYqlid())) {
            //选择房产证、土地证创建项目读取gd_dy中的抵押方式作为登记子项
            String djzxDm = getDjzxFromGddy(project.getYqlid());
            if (StringUtils.isNotBlank(djzxDm)) {
                bdcXm.setDjzx(djzxDm);
            }
        }
        return yBdcXm;
    }

    @Override
    public String getDjzxFromGddy(String yqlid) {
        String djzxDm = null;
        GdDy gdDy = gdFwService.getGdDyByDyid(yqlid, null);
        if (gdDy != null && StringUtils.isNotBlank(gdDy.getDyfs())) {
            HashMap dyfsMap = new HashMap();
            dyfsMap.put("dm", gdDy.getDyfs());
            List<HashMap> dyfsHashMapList = bdcZdGlService.getDyfs(dyfsMap);
            if (CollectionUtils.isNotEmpty(dyfsHashMapList)) {
                HashMap dyfsHashMap = dyfsHashMapList.get(0);
                String djzxMc = CommonUtil.formatEmptyValue(dyfsHashMap.get("MC"));
                if (StringUtils.isNotBlank(djzxMc)) {
                    HashMap djzxMap = new HashMap();
                    djzxMap.put("mc", djzxMc);
                    List<HashMap> djzxHashMapList = bdcZdGlService.getDjzx(djzxMap);
                    if (CollectionUtils.isNotEmpty(djzxHashMapList)) {
                        HashMap djzxHashMap = djzxHashMapList.get(0);
                        djzxDm = CommonUtil.formatEmptyValue(djzxHashMap.get("DM"));
                    }
                }
            }
        }
        return djzxDm;
    }

    @Override
    public Date getLzrqByWiid(String wiid, BdcXm bdcXm) {
        Date lzrq = null;
        if (StringUtils.isNotBlank(wiid)) {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
            //领证日期
            if (pfWorkFlowInstanceVo != null) {
                String dwdm = AppConfig.getProperty("dwdm");
                if (StringUtils.equals(dwdm, "320500")) {
                    int overDate = 0;
                    overDate = overDate + Integer.parseInt(pfWorkFlowInstanceVo.getTimeLimit());
                    Date dates = sysCalendarService.getOverTime(bdcXm.getCjsj(), overDate + "");
                    List<Date> list = sysCalendarService.getHolidayList(bdcXm.getCjsj(), dates);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(bdcXm.getCjsj());
                    if (list != null) {
                        calendar.add(calendar.DATE, list.size() + overDate);
                    }
                    lzrq = calendar.getTime();
                } else {
                    List<ActivityModel> activityModelList = PlatformUtil.getAllActivity(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                    //jyl 总时限减发证节点时限，自动生成领证日期。
                    String timeLimit = pfWorkFlowInstanceVo.getTimeLimit();
                    if (CollectionUtils.isNotEmpty(activityModelList)) {
                        int overDate = 0;
                        for (ActivityModel activityModel : activityModelList) {
                            //发证作为终止节点，所以可以这样写，否则不能
                            if (StringUtils.isNotBlank(timeLimit) && StringUtils.equals(activityModel.getActivityDefineName(), Constants.WORKFLOW_FZ)) {
                                overDate = Integer.parseInt(timeLimit) - Integer.parseInt(activityModel.getLimit());
                            }
                        }
                        if (overDate != 0) {
                            overDate = overDate + 1;
                        }
                        lzrq = sysCalendarService.getOverTime(new Date(), overDate + "");
                    }
                }
            }
        }
        return lzrq;
    }

    @Override
    public String getBdcXmYfczh(String yxmid, BdcXm bdcXm) {
        String yfczh = null;
        if (StringUtils.isNotBlank(bdcXm.getSqlx()) &&
                (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ) || StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYQ))
                && CommonUtil.indexOfStrs(Constants.SQLX_YFCZH, bdcXm.getSqlx()) && StringUtils.isNotBlank(yxmid)) {
            //抵押证明的原证号是yfczh
            BdcXm bdcXm1 = getBdcXmByProid(yxmid);
            if (bdcXm1 != null) {
                if (StringUtils.isNotBlank(bdcXm1.getYfczh())) {
                    yfczh = bdcXm1.getYfczh();
                } else if (StringUtils.isNotBlank(bdcXm1.getYbdcqzh())) {
                    yfczh = bdcXm1.getYbdcqzh();
                }
            }
        }
        return yfczh;
    }

    @Override
    public String completeBdcXmYbdcqzh(Project project, BdcXm bdcXm) {
        String ybdcqzh = bdcXm.getYbdcqzh();
        //jiangganzhi 商品房转移抵押合并登记 存量房转移抵押合并登记，换证和抵押登记 换证（补发）和抵押变更登记 抵押项目在缮证前权利其他状况不显示原不动产权证号
        String wfDfid = PlatformUtil.getWfDfidByWiid(project.getWiid());
        String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(wfDfid);
        if ((StringUtils.equals(sqlxdm, Constants.SQLX_CLF) || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD)
                || StringUtils.equals(sqlxdm, Constants.SQLX_HZDY_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_YFZYDYDJ_DM)
                || StringUtils.equals(sqlxdm, Constants.SQLX_DYHZ_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_ZY_DYBG)
                || StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) && StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)
                ) {
            ybdcqzh = "";
        } else {
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 土地分割合并变更登记，根据项目来源查出来的原不动产证号为空，添加处理保留分割合并前老证作为项目的原不动产权证
             */
            if (StringUtils.isNotBlank(project.getSqlx()) && (StringUtils.equals(project.getSqlx(), Constants.SQLX_TDFGHBBG_DM) || StringUtils.equals(project.getSqlx(), Constants.SQLX_TDFGHBZY_DM))) {
                String yBdccqzhTmp = getYbdcqzh(project, bdcXm);
                if (StringUtils.isNotBlank(yBdccqzhTmp)) {
                    ybdcqzh = yBdccqzhTmp;
                }
            } else {
                ybdcqzh = getYbdcqzh(project, bdcXm);
            }
        }
        return ybdcqzh;
    }

    @Override
    public void dealBdcXmDjsy(Project project, BdcXm bdcXm) {
        //zhouwanqing若未配置则根据不动产单元号去搜索
        if (StringUtils.isBlank(project.getDjsy())) {
            if ((StringUtils.isNotBlank(bdcXm.getXmly()) && StringUtils.equals(bdcXm.getXmly(), "2") || StringUtils.equals(bdcXm.getXmly(), "3")
                    || (StringUtils.equals(project.getDjlx(), Constants.DJLX_CFDJ_DM) || StringUtils.equals(project.getDjlx(), Constants.DJLX_YGDJ_DM)
                    || StringUtils.equals(project.getDjlx(), Constants.DJLX_YYDJ_DM)))) {
                getDjsyByBdcdyh(bdcXm, project.getBdcdyh());
            } else {
                //针对土地分割合并换证登记获取登记事由
                if (StringUtils.isNotBlank(project.getSqlx()) && !StringUtils.equals(project.getSqlx(), Constants.SQLX_CLF)) {
                    String currentXmWorkFlowName = bdcZdGlService.getSqlxMcByDm(bdcXm.getSqlx());
                    if (StringUtils.isNotBlank(currentXmWorkFlowName)) {
                        String djsy = "";
                        List<Map> mapList = getAllLxByWfName(currentXmWorkFlowName);
                        if (CollectionUtils.isNotEmpty(mapList)) {
                            Map map = mapList.get(0);
                            if (map.get(SQLXDM) != null) {
                                djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(bdcXm.getSqlx());
                            }
                            if (StringUtils.isNotBlank(djsy)) {
                                bdcXm.setDjsy(djsy);
                            } else {
                                if (map.get(DJSYDM) != null)
                                    bdcXm.setDjsy(map.get(DJSYDM).toString());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> getXsBdcCqProidByBdcdyh(String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcXmMapper.getXsBdcCqProidByBdcdyh(bdcdyh) : null;
    }

    @Override
    public List<String> getXsGdCqProidByBdcdyh(String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcXmMapper.getXsGdCqProidByBdcdyh(bdcdyh) : null;
    }

    public void addFfczh(Project project) {
        if (project != null && StringUtils.isNotBlank(project.getXmly()) && StringUtils.equals(project.getXmly(), Constants.XMLY_FWSP) && StringUtils.isNotBlank(project.getYqlid())) {
            String yqlids = project.getYqlid();
            String[] yqlidArr = yqlids.split(",");
            List<String> yqlidList = new ArrayList<String>();
            CollectionUtils.addAll(yqlidList, yqlidArr);
            if (CollectionUtils.isNotEmpty(yqlidList)) {
                for (String yqlid : yqlidList) {
                    GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(yqlid);
                    if (gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getFczh()) && StringUtils.isNotBlank(project.getYbdcqzh()) && !project.getYbdcqzh().contains(gdFwsyq.getFczh())) {
                        project.setYbdcqzh(project.getYbdcqzh() + "、" + gdFwsyq.getFczh());
                    }
                }
            }
        }
    }

    @Override
    public List<Map> getBdcZsByPage(final Map map) {
        return bdcXmMapper.getBdcZsByPage(map);
    }

    @Override
    public BdcXm creatBdcXm(Project project, BdcXm ybdcXm, String userName) {
        BdcXm bdcxm = new BdcXm();
        bdcxm.setProid(project.getProid());
        bdcxm.setWiid(project.getWiid());
        bdcxm.setDjlx(project.getDjlx());
        bdcxm.setQllx(project.getQllx());
        bdcxm.setSqlx(project.getSqlx());
        bdcxm.setDjsy(project.getDjsy());
        if (StringUtils.isNotBlank(project.getBdclx())) {
            bdcxm.setBdclx(project.getBdclx());
        }
        if (StringUtils.isNotBlank(ybdcXm.getBh())) {
            bdcxm.setBh(ybdcXm.getBh());
        }
        if (StringUtils.isNotBlank(ybdcXm.getBdclx())) {
            bdcxm.setBdclx(ybdcXm.getBdclx());
        }
        if (StringUtils.isNotBlank(userName)) {
            bdcxm.setCjr(userName);
        }
        bdcxm.setCjsj(new Date());
        bdcxm = getDydjlx(bdcxm);
        entityMapper.saveOrUpdate(bdcxm, bdcxm.getProid());
        return bdcxm;
    }


    @Override
    public BdcXm creatBdcXm(final String proid, final String userName, final String wdid, final String wiid) {
        BdcXm bdcxm = new BdcXm();
        bdcxm.setProid(proid);
        String workFlowName = "";
        if (StringUtils.isNotBlank(wdid))
            workFlowName = PlatformUtil.getWdNameByWdid(wdid);
        else
            workFlowName = PlatformUtil.getWorkFlowNameByProid(proid);

        //优先使用wdid进行查询
        if (StringUtils.isNotBlank(wdid)) {
            List<Map> mapList = getAllLxByWdid(wdid);
            if (CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                String djsy = "";
                if (map.get(QLLXDM) != null)
                    bdcxm.setQllx(map.get(QLLXDM).toString());
                if (map.get(SQLXDM) != null) {
                    bdcxm.setSqlx(map.get(SQLXDM).toString());
                    djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(bdcxm.getSqlx());
                }
                if (map.get(DJLXDM) != null)
                    bdcxm.setDjlx(map.get(DJLXDM).toString());
                if (StringUtils.isNotBlank(djsy)) {
                    bdcxm.setDjsy(djsy);
                } else {
                    if (map.get(DJSYDM) != null)
                        bdcxm.setDjsy(map.get(DJSYDM).toString());
                }
            } else {
                //zdd 无法根据申请类型对应权利类型的情况
                List<Map> sqlxList = bdcZdGlService.getZdSqlxList();
                if (CollectionUtils.isNotEmpty(sqlxList)) {
                    String sqlxdm = "";
                    for (Map sqlxmap : sqlxList) {
                        if (sqlxmap.containsKey("MC") && sqlxmap.get("MC") != null && sqlxmap.get("MC").toString().equals(workFlowName)) {
                            if (sqlxmap.containsKey("DM") && sqlxmap.get("DM") != null)
                                sqlxdm = sqlxmap.get("DM").toString();
                            break;
                        }
                    }
                    String djlxdm = "";
                    List<Map> djlxSqlxRelList = bdcZdGlService.getDjlxSqlxRel();
                    if (CollectionUtils.isNotEmpty(djlxSqlxRelList)) {
                        for (Map djlxSqlxMap : djlxSqlxRelList) {
                            if (djlxSqlxMap.containsKey(SQLXDM) && djlxSqlxMap.get(SQLXDM) != null && djlxSqlxMap.get(SQLXDM).equals(sqlxdm)) {
                                if (djlxSqlxMap.containsKey(DJLXDM) && djlxSqlxMap.get(DJLXDM) != null)
                                    djlxdm = djlxSqlxMap.get(DJLXDM).toString();
                                break;
                            }
                        }
                    }
                    bdcxm.setSqlx(sqlxdm);
                    bdcxm.setDjlx(djlxdm);

                }
            }
            if (StringUtils.isNotBlank(userName))
                bdcxm.setCjr(userName);
            bdcxm.setCjsj(new Date());
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
            if (StringUtils.isBlank(bdcxm.getBh()) && pfWorkFlowInstanceVo != null) {
                bdcxm.setBh(pfWorkFlowInstanceVo.getWorkflowIntanceName());
                bdcxm.setLzrq(CalendarUtil.addDays(pfWorkFlowInstanceVo.getTimeLimit()));
                bdcxm.setDwdm(pfWorkFlowInstanceVo.getRegionCode());
            }
            //领证日期
            if (pfWorkFlowInstanceVo != null) {
                String dwdm = AppConfig.getProperty("dwdm");
                if (StringUtils.equals(dwdm, "320500")) {
                    int overDate = 0;
                    overDate = overDate + Integer.parseInt(pfWorkFlowInstanceVo.getTimeLimit());
                    Date dates = sysCalendarService.getOverTime(bdcxm.getCjsj(), overDate + "");
                    List<Date> list = sysCalendarService.getHolidayList(bdcxm.getCjsj(), dates);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(bdcxm.getCjsj());
                    if (list != null)
                        calendar.add(calendar.DATE, list.size() + overDate);
                    Date lzrq = calendar.getTime();
                    if (lzrq != null) {
                        bdcxm.setLzrq(lzrq);
                    }
                } else {
                    List<ActivityModel> activityModelList = PlatformUtil.getAllActivity(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                    //jyl 总时限减发证节点时限，自动生成领证日期。
                    String timeLimit = pfWorkFlowInstanceVo.getTimeLimit();
                    if (CollectionUtils.isNotEmpty(activityModelList)) {
                        int overDate = 0;
                        for (ActivityModel activityModel : activityModelList) {
                            //发证作为终止节点，所以可以这样写，否则不能
                            if (StringUtils.isNotBlank(timeLimit) && StringUtils.equals(activityModel.getActivityDefineName(), Constants.WORKFLOW_FZ)) {
                                overDate = Integer.parseInt(timeLimit) - Integer.parseInt(activityModel.getLimit());
                            }
                        }
                        if (overDate != 0) {
                            overDate = overDate + 1;
                        }

                        Date lzrq = sysCalendarService.getOverTime(new Date(), overDate + "");
                        if (lzrq != null)
                            bdcxm.setLzrq(lzrq);
                    }
                }
            }
        } else if (StringUtils.isNotBlank(workFlowName)) {
            List<Map> mapList = getAllLxByWfName(workFlowName);
            if (CollectionUtils.isNotEmpty(mapList)) {
                Map map = mapList.get(0);
                String djsy = "";
                if (map.get(QLLXDM) != null)
                    bdcxm.setQllx(map.get(QLLXDM).toString());
                if (map.get(SQLXDM) != null) {
                    bdcxm.setSqlx(map.get(SQLXDM).toString());
                    djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(bdcxm.getSqlx());
                }
                if (map.get(DJLXDM) != null)
                    bdcxm.setDjlx(map.get(DJLXDM).toString());
                if (StringUtils.isNotBlank(djsy)) {
                    bdcxm.setDjsy(djsy);
                } else {
                    if (map.get(DJSYDM) != null)
                        bdcxm.setDjsy(map.get(DJSYDM).toString());
                }
            } else {
                //zdd 无法根据申请类型对应权利类型的情况
                List<Map> sqlxList = bdcZdGlService.getZdSqlxList();
                if (CollectionUtils.isNotEmpty(sqlxList)) {
                    String sqlxdm = "";
                    for (Map sqlxmap : sqlxList) {
                        if (sqlxmap.containsKey("MC") && sqlxmap.get("MC") != null && sqlxmap.get("MC").toString().equals(workFlowName)) {
                            if (sqlxmap.containsKey("DM") && sqlxmap.get("DM") != null)
                                sqlxdm = sqlxmap.get("DM").toString();
                            break;
                        }
                    }
                    String djlxdm = "";
                    List<Map> djlxSqlxRelList = bdcZdGlService.getDjlxSqlxRel();
                    if (CollectionUtils.isNotEmpty(djlxSqlxRelList)) {
                        for (Map djlxSqlxMap : djlxSqlxRelList) {
                            if (djlxSqlxMap.containsKey(SQLXDM) && djlxSqlxMap.get(SQLXDM) != null && djlxSqlxMap.get(SQLXDM).equals(sqlxdm)) {
                                if (djlxSqlxMap.containsKey(DJLXDM) && djlxSqlxMap.get(DJLXDM) != null)
                                    djlxdm = djlxSqlxMap.get(DJLXDM).toString();
                                break;
                            }
                        }
                    }
                    bdcxm.setSqlx(sqlxdm);
                    bdcxm.setDjlx(djlxdm);

                }
            }
            if (StringUtils.isNotBlank(userName))
                bdcxm.setCjr(userName);
            bdcxm.setCjsj(new Date());
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstanceByProId(proid);
            if (StringUtils.isBlank(bdcxm.getBh()) && pfWorkFlowInstanceVo != null) {
                bdcxm.setBh(pfWorkFlowInstanceVo.getWorkflowIntanceName());
                bdcxm.setLzrq(CalendarUtil.addDays(pfWorkFlowInstanceVo.getTimeLimit()));
                bdcxm.setDwdm(pfWorkFlowInstanceVo.getRegionCode());
            }
            //领证日期
            if (pfWorkFlowInstanceVo != null) {
                List<ActivityModel> activityModelList = PlatformUtil.getAllActivity(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                //jyl 总时限减发证节点时限，自动生成领证日期。
                String timeLimit = pfWorkFlowInstanceVo.getTimeLimit();
                if (CollectionUtils.isNotEmpty(activityModelList)) {
                    int overDate = 0;
                    for (ActivityModel activityModel : activityModelList) {
                        //发证作为终止节点，所以可以这样写，否则不能
                        if (StringUtils.isNotBlank(timeLimit) && StringUtils.equals(activityModel.getActivityDefineName(), Constants.WORKFLOW_FZ)) {
                            overDate = Integer.parseInt(timeLimit) - Integer.parseInt(activityModel.getLimit());
                        }
                    }
                    if (overDate != 0) {
                        overDate = overDate + 1;
                    }
                    Date lzrq = sysCalendarService.getOverTime(new Date(), overDate + "");
                    if (lzrq != null)
                        bdcxm.setLzrq(lzrq);
                }
            }
        }
        bdcxm = getDydjlx(bdcxm);
        if (StringUtils.isNotBlank(wiid)) {
            bdcxm.setWiid(wiid);
        }
        entityMapper.saveOrUpdate(bdcxm, bdcxm.getProid());
        return bdcxm;
    }

    @Override
    public String getXmLx(final Map map) {
        return bdcXmMapper.getXmLx(map);
    }

    @Override
    public List<Map> getAllLxByWfName(final String workFlowName) {
        return bdcXmMapper.getAllLxByWfName(workFlowName);
    }

    @Override
    public List<Map> getAllLxByWdid(String wdid) {
        return bdcXmMapper.getAllLxByWdid(wdid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcXm> andEqualQueryBdcXm(HashMap<String, String> map) {
        List<BdcXm> list = null;
        Example bdcxm = new Example(BdcXm.class);
        if (map != null && CollectionUtils.isNotEmpty(map.entrySet())) {
            Iterator iter = map.entrySet().iterator();
            Example.Criteria criteria = bdcxm.createCriteria();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val != null) {
                    if (StringUtils.equals(key.toString(), "order"))
                        bdcxm.setOrderByClause(val.toString());
                    else
                        criteria.andEqualTo(key.toString(), val);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(bdcxm.getOredCriteria()) && CollectionUtils.isNotEmpty(bdcxm.getOredCriteria().get(0).getAllCriteria()))
            list = entityMapper.selectByExample(BdcXm.class, bdcxm);

        return list;
    }

    @Override
    public String getBdcQllxDjsyRel(String qllxdm) {
        return bdcXmMapper.getBdcQllxDjsyRel(qllxdm);
    }

    @Override
    public BdcXm getDjsyByBdcdyh(BdcXm bdcxm, String bdcdyh) {
        String djsj = bdcTzmDjsjRelService.getDjsjByBdcdyh(bdcdyh);
        if (StringUtils.isNotBlank(djsj))
            bdcxm.setDjsy(djsj);
        return bdcxm;
    }

    @Override
    public List<BdcXm> getYbdcXmListByProid(String proid) {
        List<BdcXm> bdcXmList = null;

        if (StringUtils.isNotBlank(proid)) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                bdcXmList = new ArrayList<BdcXm>();
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (bdcXmRel != null && StringUtils.isNotBlank(bdcXmRel.getYproid())) {
                        BdcXm ybdcXm = getBdcXmByProid(bdcXmRel.getYproid());
                        if (ybdcXm != null) {
                            bdcXmList.add(ybdcXm);
                        }
                    }
                }
            }
        }

        return bdcXmList;
    }

    @Override
    public void saveDjsy(final String djsy, final String proid) {
        String newDjsy = "";
        String[] newdjsy = djsy.split("/");
        String djsydmOne = bdcXmMapper.getDjsyDmByMc(newdjsy[0]);
        if (newdjsy.length > 1) {
            String djsydmTwo = bdcXmMapper.getDjsyDmByMc(newdjsy[1]);
            int[] djsyInt = {Integer.parseInt(djsydmOne), Integer.parseInt(djsydmTwo)};
            Arrays.sort(djsyInt);
            newDjsy = djsyInt[0] + "/" + djsyInt[1];
        } else {
            newDjsy = djsydmOne;
        }
        BdcXm bdcXm = getBdcXmByProid(proid);
        bdcXm.setDjsy(newDjsy);
        entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
    }

    public BdcXm getDydjlx(BdcXm bdcxm) {
        if (StringUtils.isNotBlank(bdcxm.getSqlx())) {
            String dydjlx = "";
            if (CommonUtil.indexOfStrs(Constants.SQLX_DY_SCDJ, bdcxm.getSqlx())) {
                dydjlx = Constants.DJLX_CSDJ_DM;
            } else if (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZYDJ, bdcxm.getSqlx())) {
                dydjlx = Constants.DJLX_ZYDJ_DM;
            } else if (CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, bdcxm.getSqlx())) {
                dydjlx = Constants.DJLX_BGDJ_DM;
            } else if (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZXDJ, bdcxm.getSqlx())) {
                dydjlx = Constants.DJLX_ZXDJ_DM;
            }
            bdcxm.setDydjlx(dydjlx);
        }
        return bdcxm;
    }

    @Override
    public List<BdcXm> getBdcXmList(Map map) {
        return bdcXmMapper.getBdcXmList(map);
    }

    @Override
    public List<BdcXm> getBdcXmListForXmRel(Map map) {
        return bdcXmMapper.getBdcXmListForXmRel(map);
    }

    @Override
    public List<BdcXm> getBdcXmByBdcdyAndSqlx(Map map) {
        return bdcXmMapper.getBdcXmByBdcdyAndSqlx(map);
    }

    @Override
    public void changeXmzt(String proid, String xmzt) {
        BdcXm bdcXm = getBdcXmByProid(proid);
        if (bdcXm != null) {
            bdcXm.setXmzt(xmzt);
            entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
        }
    }

    @Transactional
    @Override
    public void batchChangeXmzt(List<BdcXm> bdcXmList, String xmzt) {
        if (CollectionUtils.isNotEmpty(bdcXmList) && StringUtils.isNotBlank(xmzt)) {
            HashMap map = Maps.newHashMap();
            map.put("xmzt", xmzt);
//            map.put("bjsj",DateUtils.now());
            map.put("bdcXmList", bdcXmList);
            bdcXmMapper.batchChangeXmzt(map);
        }
    }

    @Override
    public List<BdcXm> getBdcXmBySlbh(String slbh) {
        List<BdcXm> bdcXmList = new ArrayList<BdcXm>();
        if (StringUtils.isNotBlank(slbh)) {
            bdcXmList = bdcXmMapper.getBdcXmBySlbh(slbh);
        }
        return bdcXmList;
    }

    @Override
    public String getPlatfromSqlxByBh(String bh) {
        String plafromSqlx = "";
        if (StringUtils.isNotBlank(bh)) {
            List<BdcXm> bdcXmList = bdcXmMapper.getBdcXmListByBh(bh);
            if (CollectionUtils.isNotEmpty(bdcXmList) && bdcXmList.get(0) != null && StringUtils.isNotBlank(bdcXmList.get(0).getWiid())) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXmList.get(0).getWiid());
                if (pfWorkFlowInstanceVo != null && StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                    plafromSqlx = bdcZdGlService.getBdcSqlxdmByWdid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                }
            }
        }
        return plafromSqlx;
    }

    @Override
    public String getProidByGdproid(String gdproid) {
        List<String> proidList = bdcXmMapper.getProidByGdproid(gdproid);
        if (CollectionUtils.isNotEmpty(proidList))
            return proidList.get(0);
        else
            return null;
    }

    @Override
    public List<BdcXm> queryBdcxmByBdcdyh(String bdcdyh) {
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            bdcXmList = bdcXmMapper.queryBdcXmByBdcdyh(bdcdyh);
        }

        return bdcXmList;
    }

    @Override
    public String getProidsByProid(String proid) {
        StringBuilder proids = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                HashMap map = new HashMap();
                map.put("wiid", bdcXm.getWiid());
                List<BdcXm> bdcXmList = andEqualQueryBdcXm(map);
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXmTemp : bdcXmList) {
                        if (StringUtils.isNotBlank(proids)) {
                            proids.append(Constants.SPLIT_STR).append(bdcXmTemp.getProid());
                        } else {
                            proids.append(bdcXmTemp.getProid());
                        }
                    }
                }
            }

        }
        return proids.toString();
    }

    @Override
    public List<String> getProidListByWiid(String wiids) {
        List<String> proidList = new ArrayList<String>();
        if (StringUtils.isNotBlank(wiids)) {
            String[] wiidArr = wiids.split(",");
            if (wiidArr != null) {
                List<String> wiidList = Arrays.asList(wiidArr);
                if (CollectionUtils.isNotEmpty(wiidList)) {
                    HashMap<String, Object> map = new HashMap();
                    map.put("list", wiidList);
                    proidList = bdcXmMapper.getProidListByWiidList(map);
                }
            }
        }
        return proidList;
    }

    @Override
    public String getProidsByQllxAndWiid(String wiid, String qllx) {
        StringBuilder proids = new StringBuilder();
        if (StringUtils.isNotBlank(wiid)) {
            HashMap map = new HashMap();
            map.put("wiid", wiid);
            if (StringUtils.isNotBlank(qllx)) {
                map.put("qllx", qllx);
            }
            List<BdcXm> bdcXmList = andEqualQueryBdcXm(map);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXmTemp : bdcXmList) {
                    if (StringUtils.isNotBlank(proids)) {
                        proids.append(Constants.SPLIT_STR).append(bdcXmTemp.getProid());
                    } else {
                        proids.append(bdcXmTemp.getProid());
                    }
                }
            }
        }
        return proids.toString();
    }

    @Override
    public String getProidsByProidAndBdcdyid(final String proid, final String bdcdyid) {
        StringBuilder proids = new StringBuilder();
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                HashMap map = new HashMap();
                map.put("wiid", bdcXm.getWiid());
                List<BdcXm> bdcXmList = null;
                if (StringUtils.isNotBlank(bdcXm.getWiid()) && StringUtils.isNotBlank(bdcdyid)) {
                    Example example = new Example(BdcXm.class);
                    example.createCriteria().andEqualTo("wiid", bdcXm.getWiid()).andNotEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                    bdcXmList = entityMapper.selectByExample(example);
                }
                if (CollectionUtils.isNotEmpty(bdcXmList)) {
                    for (BdcXm bdcXmTemp : bdcXmList) {
                        if (StringUtils.isNotBlank(proids)) {
                            proids.append(Constants.SPLIT_STR).append(bdcXmTemp.getProid());
                        } else {
                            proids.append(bdcXmTemp.getProid());
                        }
                    }
                }
            }

        }
        return proids.toString();
    }

    public String findTdzh(String bdcqzhs, String bdcdyh) {
        String ybdcqzh = "";
        HashSet<String> fczhSet = new HashSet<String>();
        HashSet<String> tdzhSet = new HashSet<String>();
        if (StringUtils.isNotBlank(bdcqzhs)) {
            //zhouwanqing 用顿号分隔与房产的逗号进行区分
            String[] bdcqzh = bdcqzhs.split("、");
            for (int i = 0; i != bdcqzh.length; i++) {
                if (StringUtils.isNotBlank(bdcqzh[i])) {
                    fczhSet.add(bdcqzh[i]);
                    HashMap hashMap = new HashMap();
                    hashMap.put("fczh", bdcqzh[i]);
                    hashMap.put(LOWERCASE_BDCDYH, bdcdyh);
                    hashMap.put("fwsyq", "true");
                    List<GdTdsyq> gdTdsyqList = gdTdService.getTdsyqByFczh(hashMap);
                    // 可能出现一本房产证匹配多本土地证的情况，需要全面添加
                    if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                        for (GdTdsyq gdTdsyq : gdTdsyqList) {
                            if (gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getTdzh())) {
                                tdzhSet.add(gdTdsyq.getTdzh());
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(fczhSet)) {
                ybdcqzh = "房产证号:" + PublicUtil.join(",", new ArrayList<String>(fczhSet));
            }
            if (StringUtils.isNotBlank(ybdcqzh)) {
                ybdcqzh = ybdcqzh + ";";
            }
            if (CollectionUtils.isNotEmpty(tdzhSet)) {
                ybdcqzh = ybdcqzh + "土地证号:" + PublicUtil.join(",", new ArrayList<String>(tdzhSet));
            }
            if (StringUtils.isNotBlank(ybdcqzh) && StringUtils.endsWith(ybdcqzh, ";"))
                ybdcqzh = StringUtils.substring(ybdcqzh, 0, ybdcqzh.length() - 1);
        }
        return ybdcqzh;
    }

    @Override
    public void getYBdcXmDjzx(String proid, BdcXm bdcXm) {
        BdcXm ybdcXm = bdcXmMapper.getBdcXmByProid(proid);
        if (ybdcXm != null) {
            bdcXm.setDjzx(ybdcXm.getDjzx());
        }
    }

    @Override
    public List<BdcXm> getBdcXmListByWiid(final String wiid) {
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(wiid)) {
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo("wiid", wiid);
            bdcXmList = entityMapper.selectByExample(example);
        }
        return bdcXmList;
    }


    @Override
    public List<BdcXm> getBdcXmListByWiidOrdeBy(BdcXm bdcXm) {
        List<BdcXm> bdcXmList = null;
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
            Map map = Maps.newHashMap();
            BdcYg bdcYg = bdcYgService.getBdcYgByProid(bdcXm.getProid());
            if (bdcYg != null) {
                map.put("yg", "yg");
            }
            map.put("wiid", bdcXm.getWiid());
            bdcXmList = bdcXmMapper.getBdcXmListByWiidOrdeBy(map);
        }
        return bdcXmList;
    }

    @Override
    public List<BdcXm> getBdcXmListByWiidAndBdcdyid(final String wiid, final String bdcdyid) {
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(wiid) && StringUtils.isNotBlank(bdcdyid)) {
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo(WIID, wiid).andEqualTo(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            bdcXmList = entityMapper.selectByExample(example);
        }
        return bdcXmList;
    }

    @Override
    public List<BdcXm> getBdcXmListByProid(final String proid) {
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(proid)) {
            Example example = new Example(BdcXm.class);
            example.createCriteria().andEqualTo(LOWERCASE_PROID, proid);
            bdcXmList = entityMapper.selectByExample(example);
        }
        return bdcXmList;
    }

    @Override
    public BdcXm getBdcXmByYqlid(final String yqlid) {
        return bdcXmMapper.queryBdcXmByYqlid(yqlid);
    }


    /**
     * @param bdcXm,proid
     * @return BdcXm
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 根据OntBdcXm初始化不动产项目
     */
    public BdcXm initBdcXmFromOntBdcXm(BdcXm bdcXm, String proid) {
        if (RequestContextHolder.getRequestAttributes() != null && ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest() != null
                && ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession() != null) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            List<OntBdcXm> ontBdcXmList = (List<OntBdcXm>) session.getAttribute("ontBdcXm_" + proid);
            String lclx = null;
            if (session.getAttribute("lclx_" + proid) != null) {
                lclx = session.getAttribute("lclx_" + proid).toString();
            }
            if (ontBdcXmList != null && !ontBdcXmList.isEmpty()) {
                OntBdcXm ontBdcXm = ontBdcXmList.get(0);
                bdcXm.setBh(ontBdcXm.getSjh());
                //初始化是否分别持证(预告,抵押组合登记中抵押不继承分别持证)
                if ((StringUtils.isBlank(lclx) || StringUtils.equals(lclx, "YG")) && StringUtils.isNotBlank(ontBdcXm.getSffbcz())) {
                    bdcXm.setSqfbcz(ontBdcXm.getSffbcz());
                }
                if (StringUtils.isNotBlank(ontBdcXm.getYbdcqzh())) {
                    bdcXm.setYbdcqzh(ontBdcXm.getYbdcqzh());
                }
            }
        }
        return bdcXm;
    }

    @Override
    public void saveBdcXm(BdcXm bdcXm) {
        entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
    }

    @Override
    public List<BdcXm> getBdcXmBySqlxAndWiid(Map map) {
        return bdcXmMapper.getBdcXmBySqlxAndWiid(map);
    }

    /**
     * @param proid
     * @return
     * @author <a href="mailto:wangming@gtmap.cn">wangming</a>
     * @description 根据proid 获得同流程下抵押权没有现世的所有项目proid
     */
    @Override
    public List<String> getXsDyxmProidsByproid(String proid) {
        return bdcXmMapper.getXsDyxmProidsByproid(proid);
    }

    @Override
    public BdcXm getYzjgcXm(String proid) {
        return bdcXmMapper.getYzjgcXm(proid);
    }

    /**
     * @param
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @rerutn
     * @description 根据zsid取不动产项目
     */
    @Override
    public List<BdcXm> getBdcXmListByZsid(final String zsid) {
        return bdcXmMapper.getBdcXmListByZsid(zsid);
    }


    @Override
    public List<String> queryCdzyProidByBdcdyh(String bdcdyh) {
        String sqlx = Constants.SQLX_ZY_SFCD;
        HashMap map = new HashMap();
        map.put(LOWERCASE_BDCDYH, bdcdyh);
        map.put("sqlx", sqlx);
        return bdcXmMapper.queryCdzyProidByBdcdyh(map);
    }

    @Override
    public List<String> queryCdBdcXmByBdcdyh(String bdcdyh) {
        String sqlx = Constants.SQLX_SFCD;
        HashMap map = new HashMap();
        map.put(LOWERCASE_BDCDYH, bdcdyh);
        map.put("sqlx", sqlx);
        return bdcXmMapper.queryCdBdcXmByBdcdyh(map);
    }

    @Override
    public String getBdcdyfwlxByYcbdcdyh(final String bdcdyh) {
        String bdcdyfwlx = "";
        if (StringUtils.isNotBlank(bdcdyh)) {
            bdcdyfwlx = bdcXmMapper.getBdcdyfwlxByYcbdcdyh(bdcdyh);
        }
        return bdcdyfwlx;
    }

    @Override
    public List<BdcXm> getBdcXmByQlr(Map map) {
        return bdcXmMapper.getBdcXmByQlr(map);
    }

    @Override
    public List<BdcXm> getBdcXmListOrderByBdcdyh(Map map) {
        return bdcXmMapper.getBdcXmListOrderByBdcdyh(map);
    }

    @Override
    public List<BdcXm> getBdcXmByBdcqzh(String bdcqzh) {
        List<BdcXm> bdcXmList = null;
        if (StringUtils.isNotBlank(bdcqzh)) {
            HashMap hashMap = new HashMap();
            hashMap.put("bdcqzh", bdcqzh);
            bdcXmList = bdcXmMapper.getBdcXmListByBdcZs(hashMap);
        }
        return bdcXmList;
    }

    @Transactional
    @Override
    public void batchDelBdcXm(List<BdcXm> bdcXmList) {
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            bdcXmMapper.batchDelBdcXm(bdcXmList);
        }
    }

    @Override
    public List<BdcBdcZsSd> getBdcSd(String colName, String colValue, int sdzt) {
        Example example = new Example(BdcBdcZsSd.class);
        example.createCriteria().andEqualTo(colName, colValue).andEqualTo("xzzt", sdzt);
        return entityMapper.selectByExample(BdcBdcZsSd.class, example);
    }

    public String getYbdcqzh(Project project, BdcXm bdcXm) {
        String ybdcqzh = "";
        if (StringUtils.equals(project.getSqlx(), Constants.SQLX_ZJJZWDY_FW_DM) || StringUtils.equals(project.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM) || StringUtils.equals(project.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
            //房屋在建建筑物登记，特殊流程，独立出来
            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                //选择厂房时 获取宗地的抵押证号
                String bdcdyfwlx = getBdcdyfwlxByYcbdcdyh(project.getBdcdyh());
                String zdDjh = StringUtils.substring(project.getBdcdyh(), 0, 19);
                String zdBdcdyh = zdDjh + "W00000000";
                String zdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(zdBdcdyh);
                List<BdcZs> bdcZsList = null;
                if (StringUtils.isNotBlank(bdcdyfwlx) && StringUtils.equals(bdcdyfwlx, Constants.DJSJ_FWDZ_DM)) {
                    if (StringUtils.isNotBlank(zdBdcdyid)) {
                        HashMap map = new HashMap();
                        map.put(ParamsConstants.BDCDYID_LOWERCASE, zdBdcdyid);
                        List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                        if (CollectionUtils.isNotEmpty(bdcDyaqList) && StringUtils.isNotBlank(bdcDyaqList.get(0).getProid())) {
                            bdcZsList = bdcZsService.queryBdcZsByProid(bdcDyaqList.get(0).getProid());
                        }
                        //宗地不存在抵押时，获取原宗地不动产单元号
                        if (CollectionUtils.isEmpty(bdcZsList)) {
                            List<BdcJsydzjdsyq> bdcJsydzjdsyqList = bdcJsydzjdsyqService.getBdcJsydzjdsyqList(map);
                            if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                                for (BdcJsydzjdsyq bdcJsydzjdsyq : bdcJsydzjdsyqList) {
                                    if (StringUtils.isNotEmpty(bdcJsydzjdsyq.getProid()) && (bdcJsydzjdsyq.getQszt() == 1)) {
                                        bdcZsList = bdcZsService.queryBdcZsByProid(bdcJsydzjdsyq.getProid());
                                    }
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            ybdcqzh = bdcZsList.get(0).getBdcqzh();
                        }
                    } else {
                        ybdcqzh = gdTdService.getTdzhByBdcdyh(zdBdcdyh);
                    }
                } else {
                    ybdcqzh = bdcZsService.getYtdzhByZdbdcdyh(zdBdcdyh);
                    if (StringUtils.isBlank(ybdcqzh)) {
                        ybdcqzh = bdcZsService.getYbdcqzhByProid(zdBdcdyh);
                    }
                }
            }
        } else if (StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
            //新建
            if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                //批量流程
                for (BdcXmRel bdcXmRel : project.getBdcXmRelList()) {
                    String bdcqzh = getXjBdcqzh(bdcXmRel.getYproid(), bdcXm);
                    if (StringUtils.isBlank(ybdcqzh)) {
                        ybdcqzh = bdcqzh;
                    } else {
                        ybdcqzh += "," + bdcqzh;
                    }
                }
            } else if (StringUtils.isNotBlank(project.getYxmid())) {
                //获取上一手项目的ybdcqzh，未得到则通过yxmid再找下
                ybdcqzh = getXjBdcqzh(project.getYxmid(), bdcXm);
            }
        } else if (StringUtils.equals(project.getXmly(), Constants.XMLY_FWSP)) {
            //房屋审批
            if (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZXDJ, project.getSqlx()) || CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, project.getSqlx())) {
                ybdcqzh = project.getYbdcqzh();
            } else {
                if (StringUtils.isNotBlank(project.getYbdcqzh())) {
                    // 遗失补证等流程的ybdcqzh应该取他项证对应的房产证
                    if (StringUtils.isNotBlank(project.getQllx()) && StringUtils.equals(Constants.QLLX_DYAQ, project.getQllx()) && StringUtils.isNotBlank(project.getSqlx()) && !StringUtils.equals(Constants.SQLX_FWDY_DM, project.getSqlx()) && !StringUtils.equals(Constants.SQLX_FWDY_XS_DM, project.getSqlx())) {
                        project.setYbdcqzh(getYcqzhByDyzmh(project.getYbdcqzh()));
                    }
                    ybdcqzh = findTdzh(project.getYbdcqzh(), project.getBdcdyh());
                } else if (StringUtils.isNotBlank(project.getBdcdyh()) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YG_ZX)) {
                    //预告抵押注销原不动产权证应该通过gdproid查找预抵证明
                    ybdcqzh = gdXmService.getGdYzhByBdcdyh(project);
                } else if (StringUtils.isNotBlank(project.getGdproid())) {
                    ybdcqzh = gdXmService.getYzhByGdproid(project);
                }
            }
        } else if (StringUtils.equals(project.getXmly(), Constants.XMLY_TDSP)) {
            //土地审批
            if (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZXDJ, project.getSqlx()) || CommonUtil.indexOfStrs(Constants.DYFS_ZXDJ_NOBDC, project.getSqlx())) {
                ybdcqzh = project.getYbdcqzh();
            } else {
                if (StringUtils.isNotBlank(project.getYbdcqzh())) {
                    if (StringUtils.isNotBlank(project.getQllx()) && StringUtils.equals(Constants.QLLX_DYAQ, project.getQllx()) && StringUtils.isNotBlank(project.getSqlx()) && !StringUtils.equals(Constants.SQLX_TDDY_DM, project.getSqlx())) {
                        project.setYbdcqzh(getYcqzhByDyzmh(project.getYbdcqzh()));
                    }
                    ybdcqzh = project.getYbdcqzh();
                } else if (StringUtils.isNotBlank(project.getGdproid())) {
                    ybdcqzh = gdXmService.getYzhByGdproid(project);
                } else if (StringUtils.isNotBlank(project.getBdcdyh())) {
                    ybdcqzh = gdTdService.getTdzhByBdcdyh(project.getBdcdyh());
                }
            }
        }
        //zdd 数据库查询出来是用"/"隔开的   文档要求用"，"隔开
        if (StringUtils.isNotBlank(ybdcqzh) && ybdcqzh.contains("/")) {
            ybdcqzh = ybdcqzh.replaceAll("/", ",");
        }
        return ybdcqzh;
    }

    public String getXjBdcqzh(String yproid, BdcXm bdcXm) {
        String ybdcqzh = "";
        if (bdcXm != null) {
            String qllx = bdcXm.getQllx();
            //qijiadong 更正登记抵押权的ybdcqzh取上一手项目的ybdcqzh
            if (CommonUtil.indexOfStrs(Constants.YXMBDQZH_SQLX, bdcXm.getSqlx()) || StringUtils.equals(Constants.DJZX_XF, bdcXm.getDjzx()) || (StringUtils.equals(Constants.QLLX_DYAQ, qllx) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GZ_DM))) {
                ybdcqzh = getXmYbdcqzh(yproid);
            } else {
                ybdcqzh = bdcZsService.getCombineBdcqzhByProid(yproid);
            }
        }
        return ybdcqzh;
    }

    public String getYbdcqzhByBdcdyid(String bdcdyid) {
        String ybdcqzh = "";
        if (StringUtils.isNotBlank(bdcdyid)) {
            List<String> proidList = getXsCqProid(bdcdyid);
            if (CollectionUtils.isNotEmpty(proidList)) {
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(proidList.get(0));
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    ybdcqzh = bdcZsList.get(0).getBdcqzh();
                }
            }
        }

        return ybdcqzh;
    }

    public String getXmYbdcqzh(String yproid) {
        String ybdcqzh = "";
        if (StringUtils.isNotBlank(yproid)) {
            BdcXm yXm = getBdcXmByProid(yproid);
            if (yXm != null)
                ybdcqzh = yXm.getYbdcqzh();
        }
        return ybdcqzh;
    }

    public void getYbdcqzhByYMethod(Project project, BdcXm bdcXm) {
        String ybdcqzh = "";
        if (StringUtils.isNotBlank(project.getYbdcqzh())) {
            if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                ybdcqzh = findTdzh(project.getYbdcqzh(), project.getBdcdyh());
            } else {
                ybdcqzh = project.getYbdcqzh();
            }
            //分别持证，选择其中一个，要把另一个也带入
            if (StringUtils.isNotBlank(project.getYxmid())) {
                List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(project.getYxmid());
                if (bdcZsList != null && bdcZsList.size() > 1) {
                    for (BdcZs bdcZs : bdcZsList) {
                        if (StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh = bdcZs.getBdcqzh();
                        } else if (StringUtils.isNotBlank(ybdcqzh) && (StringUtils.indexOf(ybdcqzh, bdcZs.getBdcqzh()) == -1)) {
                            ybdcqzh = ybdcqzh + "," + bdcZs.getBdcqzh();
                        }
                    }
                }
            }
        } else if (StringUtils.isNotBlank(project.getYxmid()) && (StringUtils.isNotBlank(bdcXm.getXmly()) && StringUtils.equals(project.getXmly(), "1"))) {
            if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GYJSYDHB_BGDJ)) {
                if (CollectionUtils.isNotEmpty(project.getBdcXmRelList())) {
                    for (BdcXmRel bdcXmRel : project.getBdcXmRelList()) {
                        List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmRel.getYproid());
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                if (StringUtils.isBlank(ybdcqzh)) {
                                    ybdcqzh = bdcZs.getBdcqzh();
                                } else {
                                    ybdcqzh = ybdcqzh + "," + bdcZs.getBdcqzh();
                                }
                            }
                        }
                    }
                }

            } else {
                List<BdcZs> bdcZsList = bdcZsService.getPlZsByProid(project.getYxmid());
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    for (BdcZs bdcZs : bdcZsList) {
                        if (StringUtils.isBlank(ybdcqzh)) {
                            ybdcqzh = bdcZs.getBdcqzh();
                        } else {
                            ybdcqzh = ybdcqzh + "," + bdcZs.getBdcqzh();
                        }
                    }
                }
                /**
                 *  xuchao 如果是批量解封流程
                 */
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLJF)
                        && StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_BDC)
                        && StringUtils.isNotBlank(project.getYxmid())) {
                    BdcXm yXm = getBdcXmByProid(project.getYxmid());
                    if (yXm != null && StringUtils.isNotBlank(yXm.getYbdcqzh())) {
                        bdcXm.setYbdcqzh(yXm.getYbdcqzh());
                    }
                }

                /**
                 *  jiangganzhi 如果是续封登记，注销查封登记
                 */
                if ((StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_CF) && StringUtils.isNotBlank(bdcXm.getDjzx()) && StringUtils.equals(bdcXm.getDjzx(), Constants.DJZX_XF)) || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_JF)) {
                    BdcXm yCfXm = getBdcXmByProid(project.getYxmid());
                    if (yCfXm != null && StringUtils.isNotBlank(yCfXm.getYbdcqzh()))
                        bdcXm.setYbdcqzh(yCfXm.getYbdcqzh());
                }
            }

        } else if (StringUtils.isNotBlank(project.getGdproid())) {

            ybdcqzh = gdXmService.getYzhByGdproid(project);
        } else if (StringUtils.isNotBlank(project.getBdcdyh()) && StringUtils.isNotBlank(project.getSqlx()) && StringUtils.equals(project.getSqlx(), Constants.SQLX_ZJJZWDY_FW_DM)) {
            //选择厂房时 获取宗地的抵押证号
            String bdcdyfwlx = getBdcdyfwlxByYcbdcdyh(project.getBdcdyh());
            String zdDjh = StringUtils.substring(project.getBdcdyh(), 0, 19);
            String zdBdcdyh = zdDjh + "W00000000";
            String zdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(zdBdcdyh);
            List<BdcZs> bdcZsList = null;
            if (StringUtils.isNotBlank(bdcdyfwlx) && StringUtils.equals(bdcdyfwlx, Constants.DJSJ_FWDZ_DM)) {
                if (StringUtils.isNotBlank(zdBdcdyid)) {
                    HashMap map = new HashMap();
                    map.put(ParamsConstants.BDCDYID_LOWERCASE, zdBdcdyid);
                    List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                    if (CollectionUtils.isNotEmpty(bdcDyaqList)) {
                        String dyaqProid = bdcDyaqList.get(0).getProid();
                        if (StringUtils.isNotBlank(dyaqProid)) {
                            bdcZsList = bdcZsService.queryBdcZsByProid(dyaqProid);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        bdcXm.setYbdcqzh(bdcZsList.get(0).getBdcqzh());
                    }
                } else {
                    String tdzh = gdTdService.getTdzhByBdcdyh(zdBdcdyh);
                    if (StringUtils.isNotBlank(tdzh))
                        bdcXm.setYbdcqzh(tdzh);
                }
            } else {
                String zjgcZdqzh = "";
                zjgcZdqzh = bdcZsService.getYtdzhByZdbdcdyh(zdBdcdyh);
                if (StringUtils.isBlank(zjgcZdqzh)) {
                    zjgcZdqzh = bdcZsService.getYbdcqzhByProid(zdBdcdyh);

                }
                if (StringUtils.isNotBlank(zjgcZdqzh)) {
                    bdcXm.setYbdcqzh(zjgcZdqzh);
                }
            }
        }
        //zdd 数据库查询出来是用"/"隔开的   文档要求用"，"隔开
        if (StringUtils.isNotBlank(ybdcqzh)) {
            if (ybdcqzh.contains("/")) ybdcqzh = ybdcqzh.replaceAll("/", ",");
            bdcXm.setYbdcqzh(ybdcqzh);
        }
    }

    @Override
    public List<HashMap> getPrintxxList(HashMap map) {
        List<HashMap> bdcPrintxxListTmp = new ArrayList<HashMap>();
        if (map != null && map.get(PRINTTYPE) != null && StringUtils.isNotBlank(map.get(PRINTTYPE).toString())) {
            if (StringUtils.equals("sqs", map.get(PRINTTYPE).toString())) {
                bdcPrintxxListTmp = getSqsxxList(map);
            } else if (StringUtils.equals("spb", map.get(PRINTTYPE).toString())) {
                bdcPrintxxListTmp = getSpbxxList(map);
            } else if (StringUtils.equals("fzjl", map.get(PRINTTYPE).toString())) {
                bdcPrintxxListTmp = getFzjlxxList(map);
            }
        }
        return bdcPrintxxListTmp;
    }

    @Override
    public List<HashMap> getSqsxxList(HashMap map) {
        List<HashMap> bdcSqsxxListTmp = bdcXmMapper.getSqsxxList(map);
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        List<HashMap> bdczddjlxList = bdcZdGlService.getBdcZdDjlx(new HashMap());
        List<BdcZdFwyt> fwytList = bdcZdGlService.getBdcZdFwyt();
        List<HashMap> bdcZdzhytList = bdcZdGlService.getZdzhytZdb(new HashMap());
        List<HashMap> bdcZdzhQlxzList = bdcZdGlService.getQlxzZdb(new HashMap());
        if (CollectionUtils.isNotEmpty(bdcSqsxxListTmp)) {
            for (HashMap sqsXx : bdcSqsxxListTmp) {
                String tdsyksqx = "";
                String tdsyjsqx = "";
                String djsy = "";
                String djlx = "";
                String fwyt = "";
                String zdzhqlxz = "";
                if (sqsXx.get(UPPERCASE_PROID) != null && StringUtils.isNotEmpty(sqsXx.get(UPPERCASE_PROID).toString())) {
                    //添加权利人信息
                    sqsXx = addQlrxxForPrint(sqsXx, sqsXx.get(UPPERCASE_PROID).toString(), zjlxList);
                    //添加义务人信息
                    sqsXx = addYwrxxForPrint(sqsXx, sqsXx.get(UPPERCASE_PROID).toString(), zjlxList);
                    //添加抵押信息
                    sqsXx = addDyaxxForPrint(sqsXx, sqsXx.get(UPPERCASE_PROID).toString());
                    //添加土地使用期限
                    Map<String, Object> tdsyqxMap = bdcFdcqService.getTdsyqx(sqsXx.get(UPPERCASE_PROID).toString());
                    if (tdsyqxMap != null) {
                        if (tdsyqxMap.get("SYKSQX") != null) {
                            tdsyksqx = tdsyqxMap.get("SYKSQX").toString();
                        }
                        if (tdsyqxMap.get("SYJSQX") != null) {
                            tdsyjsqx = tdsyqxMap.get("SYJSQX").toString();
                        }
                    }
                    sqsXx.put(TDSYKSQX, tdsyksqx);
                    sqsXx.put(TDSYJSQX, tdsyjsqx);
                    //处理登记事由
                    djsy = bdcXmMapper.getDjsyMcByProid(sqsXx.get(UPPERCASE_PROID).toString());
                    if (StringUtils.isNotEmpty(djsy)) {
                        sqsXx.put("DJSY", djsy);
                    } else {
                        sqsXx.put("DJSY", "");
                    }
                    //处理登记类型
                    if (sqsXx.get("DJLX") != null && StringUtils.isNotEmpty(sqsXx.get("DJLX").toString())) {
                        djlx = bdcZdGlService.getDjlxMcByDm(sqsXx.get("DJLX").toString(), bdczddjlxList);
                    }
                    if (StringUtils.isNotEmpty(djlx)) {
                        sqsXx.put(DYDJLX, djlx);
                    } else {
                        sqsXx.put(DYDJLX, "");
                    }
                    //处理房屋用途
                    if (sqsXx.get("FWYT") != null && StringUtils.isNotEmpty(sqsXx.get("FWYT").toString())) {
                        fwyt = bdcZdGlService.getFwytMcByDm(sqsXx.get("FWYT").toString(), fwytList);
                        if (StringUtils.isNotEmpty(fwyt)) {
                            sqsXx.put("FWYT", fwyt);
                        } else {
                            sqsXx.put("FWYT", "");
                        }
                    }
                    //处理土地用途
                    if (sqsXx.get("TDYT") != null && StringUtils.isNotEmpty(sqsXx.get("TDYT").toString())) {
                        String zdzhyt = bdcZdGlService.getZdzhytMcByDm(sqsXx.get("TDYT").toString(), bdcZdzhytList);
                        if (StringUtils.isNotEmpty(zdzhyt)) {
                            sqsXx.put("TDYT", zdzhyt);
                        } else {
                            sqsXx.put("TDYT", "");
                        }
                    }
                    //处理宗地宗海权利性质
                    if (sqsXx.get(ZDZHQLXZ) != null && StringUtils.isNotEmpty(sqsXx.get(ZDZHQLXZ).toString())) {
                        zdzhqlxz = bdcZdGlService.getZdzhqlxzMcByDm(sqsXx.get(ZDZHQLXZ).toString(), bdcZdzhQlxzList);
                        if (StringUtils.isNotEmpty(zdzhqlxz)) {
                            sqsXx.put(ZDZHQLXZ, zdzhqlxz);
                        } else {
                            sqsXx.put(ZDZHQLXZ, "");
                        }
                    }
                    //处理不动产是否共有
                    List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(sqsXx.get(UPPERCASE_PROID).toString());
                    if (CollectionUtils.isNotEmpty(bdcQlrList) && bdcQlrList.size() > 1) {
                        sqsXx.put("SFGY", "是");
                    } else {
                        sqsXx.put("SFGY", "否");
                    }
                }
            }
        }
        return bdcSqsxxListTmp;
    }

    public HashMap addDyaxxForPrint(HashMap printXx, String proid) {
        if (StringUtils.isNotBlank(proid)) {
            String bdbzzqse = "";
            String zwlxksqx = "";
            String zwlxjsqx = "";
            String dbfw = "";
            String fwdymj = "";
            String tddymj = "";
            String zjgcdyfw = "";
            String fwdyjg = "";
            String tddyjg = "";
            String zjg = "";
            Map dyaqXx = bdcDyaqService.getDyaqxxForPrint(proid);
            if (dyaqXx != null) {
                if (dyaqXx != null && dyaqXx.get(BDBZZQSE) != null && StringUtils.isNotBlank(dyaqXx.get(BDBZZQSE).toString())) {
                    bdbzzqse = dyaqXx.get(BDBZZQSE).toString();
                }
                if (dyaqXx != null && dyaqXx.get(ZWLXKSQX) != null && StringUtils.isNotBlank(dyaqXx.get(ZWLXKSQX).toString())) {
                    zwlxksqx = dyaqXx.get(ZWLXKSQX).toString();
                }
                if (dyaqXx != null && dyaqXx.get(ZWLXJSQX) != null && StringUtils.isNotBlank(dyaqXx.get(ZWLXJSQX).toString())) {
                    zwlxjsqx = dyaqXx.get(ZWLXJSQX).toString();
                }
                if (dyaqXx != null && dyaqXx.get(ZJGCDYFW) != null && StringUtils.isNotBlank(dyaqXx.get(ZJGCDYFW).toString())) {
                    zjgcdyfw = dyaqXx.get(ZJGCDYFW).toString();
                }
                if (dyaqXx != null && dyaqXx.get(DBFW) != null && StringUtils.isNotBlank(dyaqXx.get(DBFW).toString())) {
                    dbfw = dyaqXx.get(DBFW).toString();
                }
                if (dyaqXx != null && dyaqXx.get(FWDYMJ) != null && StringUtils.isNotBlank(dyaqXx.get(FWDYMJ).toString())) {
                    fwdymj = dyaqXx.get(FWDYMJ).toString();
                }
                if (dyaqXx != null && dyaqXx.get(TDDYMJ) != null && StringUtils.isNotBlank(dyaqXx.get(TDDYMJ).toString())) {
                    tddymj = dyaqXx.get(TDDYMJ).toString();
                }
                if (dyaqXx != null && dyaqXx.get(TDDYJG) != null && StringUtils.isNotBlank(dyaqXx.get(TDDYJG).toString())) {
                    tddyjg = dyaqXx.get(TDDYJG).toString();
                }
                if (dyaqXx != null && dyaqXx.get(FWDYJG) != null && StringUtils.isNotBlank(dyaqXx.get(FWDYJG).toString())) {
                    fwdyjg = dyaqXx.get(FWDYJG).toString();
                }

                if (StringUtils.isNotBlank(fwdyjg) || StringUtils.isNotBlank(tddyjg)) {
                    if (StringUtils.isBlank(fwdyjg)) {
                        zjg = tddyjg;
                    }
                    if (StringUtils.isNotBlank(fwdyjg) && StringUtils.isBlank(tddyjg)) {
                        zjg = fwdyjg;
                    }
                    if (StringUtils.isNotBlank(fwdyjg) && StringUtils.isNotBlank(tddyjg)) {
                        zjg = String.valueOf(Double.parseDouble(fwdyjg) + Double.parseDouble(tddyjg));
                    }
                }
            }
            printXx.put(BDBZZQSE, bdbzzqse);
            printXx.put(ZWLXKSQX, zwlxksqx);
            printXx.put(ZWLXJSQX, zwlxjsqx);
            printXx.put(DBFW, dbfw);
            printXx.put(ZJGCDYFW, zjgcdyfw);
            printXx.put(FWDYMJ, fwdymj);
            printXx.put(TDDYMJ, tddymj);
            printXx.put(TDDYJG, tddyjg);
            printXx.put(FWDYJG, fwdyjg);
            printXx.put("ZJG", zjg);
        }
        return printXx;
    }

    public HashMap addDjsjForPrint(HashMap printXx, String proid) {
        if (StringUtils.isNotBlank(proid)) {
            String djsj = "";
            djsj = bdcXmMapper.getDjsjByProid(proid);
            printXx.put("DJSJ", djsj);
        }
        return printXx;
    }


    public HashMap addQlqtzkAndFjForPrint(HashMap printXx, String proid) {
        String qlqtzk = "";
        String fj = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = getBdcXmByProid(proid);
            if (bdcXm != null) {
                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                if (qllxVo != null) {
                    if (StringUtils.isNotEmpty(qllxVo.getQlqtzk())) {
                        qlqtzk = qllxVo.getQlqtzk();
                    }
                    fj = StringUtils.defaultString(qllxVo.getFj(), "");
                }
            }
        }
        printXx.put(QLQTZK, qlqtzk);
        printXx.put(FJ, fj);
        return printXx;
    }

    public HashMap addQlrxxForPrint(HashMap printXx, String proid, List<BdcZdZjlx> zjlxList) {
        if (StringUtils.isNotBlank(proid)) {
            StringBuilder qlr = new StringBuilder();
            StringBuilder qlrsfzjzl = new StringBuilder();
            StringBuilder qlrzjh = new StringBuilder();
            StringBuilder qlrqlbl = new StringBuilder();
            StringBuilder gyqk = new StringBuilder();
            StringBuilder qlrlxdh = new StringBuilder();
            StringBuilder qlrdlr = new StringBuilder();
            StringBuilder qlrdlrzjzl = new StringBuilder();
            StringBuilder qlrdlrzjh = new StringBuilder();
            StringBuilder qlrdlrdh = new StringBuilder();
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    if (StringUtils.isNotBlank(bdcQlr.getQlrmc())) {
                        if (StringUtils.isBlank(qlr)) {
                            qlr.append(bdcQlr.getQlrmc());
                        } else {
                            qlr.append(" " + bdcQlr.getQlrmc());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrsfzjzl())) {
                        String zjlxmc = bdcZdGlService.getZjlxMcByDm(bdcQlr.getQlrsfzjzl(), zjlxList);
                        if (StringUtils.isNotEmpty(zjlxmc)) {
                            if (StringUtils.isBlank(qlrsfzjzl)) {
                                qlrsfzjzl.append(zjlxmc);
                            } else {
                                qlrsfzjzl.append(" " + zjlxmc);
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrzjh())) {
                        if (StringUtils.isBlank(qlrzjh)) {
                            qlrzjh.append(bdcQlr.getQlrzjh());
                        } else {
                            qlrzjh.append(" " + bdcQlr.getQlrzjh());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlbl())) {
                        if (StringUtils.isBlank(qlrqlbl)) {
                            qlrqlbl.append(bdcQlr.getQlbl());
                        } else {
                            qlrqlbl.append(" " + bdcQlr.getQlbl());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getGyqk())) {
                        if (StringUtils.isBlank(gyqk)) {
                            gyqk.append(bdcQlr.getGyqk());
                        } else {
                            gyqk.append(" " + bdcQlr.getGyqk());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrlxdh())) {
                        if (StringUtils.isBlank(qlrlxdh)) {
                            qlrlxdh.append(bdcQlr.getQlrlxdh());
                        } else {
                            qlrlxdh.append(" " + bdcQlr.getQlrlxdh());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrdlr())) {
                        if (StringUtils.isBlank(qlrdlr)) {
                            qlrdlr.append(bdcQlr.getQlrdlr());
                        } else {
                            qlrdlr.append(" " + bdcQlr.getQlrdlr());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrdlrzjzl())) {
                        String zjlxmc = bdcZdGlService.getZjlxMcByDm(bdcQlr.getQlrdlrzjzl(), zjlxList);
                        if (StringUtils.isNotEmpty(zjlxmc)) {
                            if (StringUtils.isBlank(qlrdlrzjzl)) {
                                qlrdlrzjzl.append(zjlxmc);
                            } else {
                                qlrdlrzjzl.append(" " + zjlxmc);
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrdlrzjh())) {
                        if (StringUtils.isBlank(qlrdlrzjh)) {
                            qlrdlrzjh.append(bdcQlr.getQlrdlrzjh());
                        } else {
                            qlrdlrzjh.append(" " + bdcQlr.getQlrdlrzjh());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcQlr.getQlrdlrdh())) {
                        if (StringUtils.isBlank(qlrdlrzjh)) {
                            qlrdlrdh.append(bdcQlr.getQlrdlrdh());
                        } else {
                            qlrdlrdh.append(" " + bdcQlr.getQlrdlrdh());
                        }
                    }
                }
            }
            printXx.put("QLR", qlr);
            printXx.put(QLRSFZJZL, qlrsfzjzl);
            printXx.put(QLRZJH, qlrzjh);
            printXx.put(QLRQLBL, qlrqlbl);
            printXx.put(GYQK, gyqk);
            printXx.put(QLRLXDH, qlrlxdh);
            printXx.put(QLRDLR, qlrdlr);
            printXx.put(QLRDLRZJZL, qlrdlrzjzl);
            printXx.put(QLRDLRZJH, qlrdlrzjh);
            printXx.put(QLRDLRDH, qlrdlrdh);
        }
        return printXx;
    }

    public HashMap addYwrxxForPrint(HashMap printXx, String proid, List<BdcZdZjlx> zjlxList) {
        if (StringUtils.isNotBlank(proid)) {
            StringBuilder ywr = new StringBuilder();
            StringBuilder ywrsfzjzl = new StringBuilder();
            StringBuilder ywrzjh = new StringBuilder();
            StringBuilder ywrqlbl = new StringBuilder();
            StringBuilder ywrlxdh = new StringBuilder();
            StringBuilder ywrdlr = new StringBuilder();
            StringBuilder ywrdlrzjzl = new StringBuilder();
            StringBuilder ywrdlrzjh = new StringBuilder();
            StringBuilder ywrdlrdh = new StringBuilder();
            List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                for (BdcQlr bdcYwr : bdcYwrList) {
                    if (StringUtils.isNotBlank(bdcYwr.getQlrmc())) {
                        if (StringUtils.isBlank(ywr)) {
                            ywr.append(bdcYwr.getQlrmc());
                        } else {
                            ywr.append(" " + bdcYwr.getQlrmc());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlrsfzjzl())) {
                        String zjlxmc = bdcZdGlService.getZjlxMcByDm(bdcYwr.getQlrsfzjzl(), zjlxList);
                        if (StringUtils.isNotEmpty(zjlxmc)) {
                            if (StringUtils.isBlank(ywrsfzjzl)) {
                                ywrsfzjzl.append(zjlxmc);
                            } else {
                                ywr.append(" " + zjlxmc);
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlrzjh())) {
                        if (StringUtils.isBlank(ywrzjh)) {
                            ywrzjh.append(bdcYwr.getQlrzjh());
                        } else {
                            ywrzjh.append(" " + bdcYwr.getQlrzjh());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlbl())) {
                        if (StringUtils.isBlank(ywrqlbl)) {
                            ywrqlbl.append(bdcYwr.getQlbl());
                        } else {
                            ywrqlbl.append(" " + bdcYwr.getQlbl());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlrlxdh())) {
                        if (StringUtils.isBlank(ywrlxdh)) {
                            ywrlxdh.append(bdcYwr.getQlrlxdh());
                        } else {
                            ywrlxdh.append(" " + bdcYwr.getQlrlxdh());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlrdlr())) {
                        if (StringUtils.isBlank(ywrdlr)) {
                            ywrdlr.append(bdcYwr.getQlrdlr());
                        } else {
                            ywrdlr.append(" " + bdcYwr.getQlrdlr());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlrdlrzjzl())) {
                        String zjlxmc = bdcZdGlService.getZjlxMcByDm(bdcYwr.getQlrdlrzjzl(), zjlxList);
                        if (StringUtils.isNotEmpty(zjlxmc)) {
                            if (StringUtils.isBlank(ywrdlrzjzl)) {
                                ywrdlrzjzl.append(zjlxmc);
                            } else {
                                ywrdlrzjzl.append(" " + zjlxmc);
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlrdlrzjh())) {
                        if (StringUtils.isBlank(ywrdlrzjzl)) {
                            ywrdlrzjzl.append(bdcYwr.getQlrdlrzjh());
                        } else {
                            ywrdlrzjzl.append(" " + bdcYwr.getQlrdlrzjh());
                        }
                    }
                    if (StringUtils.isNotBlank(bdcYwr.getQlrdlrdh())) {
                        if (StringUtils.isBlank(ywrdlrdh)) {
                            ywrdlrdh.append(bdcYwr.getQlrdlrdh());
                        } else {
                            ywrdlrdh.append(" " + bdcYwr.getQlrdlrdh());
                        }
                    }
                }
            }
            printXx.put(YWR, ywr);
            printXx.put(YWRSFZJZL, ywrsfzjzl);
            printXx.put(YWRZJH, ywrzjh);
            printXx.put(YWRQLBL, ywrqlbl);
            printXx.put(YWRLXDH, ywrlxdh);
            printXx.put(YWRDLR, ywrdlr);
            printXx.put(YWRDLRZJZL, ywrdlrzjzl);
            printXx.put(YWRDLRZJH, ywrdlrzjh);
            printXx.put(YWRDLRDH, ywrdlrdh);
        }
        return printXx;
    }

    @Override
    public DataToPrintXml getAllPrintXxXml(final String proid, final String printType) {
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(printType)) {
            if (StringUtils.equals(printType, "sqs")) {
                dataToPrintXml = getSqsXxXml(proid);
            } else if (StringUtils.equals(printType, "spb")) {
                dataToPrintXml = getSpbXxXml(proid);
            } else if (StringUtils.equals(printType, "fzjl")) {
                dataToPrintXml = getFzjlXxXml(proid);
            }
        }
        return dataToPrintXml;
    }

    @Override
    public DataToPrintXml getSqsXxXml(String proid) {
        List<HashMap> hashMapList;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;
        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        //在数据为空的时候赋空防止有控件默认数据出现
        String[] fpname = {"TXM", "BH", "DJSY", DYDJLX, QLR, QLRSFZJZL, QLRZJH, QLRLXDH, QLRDLR, QLRDLRZJZL, QLRDLRZJH, QLRDLRDH,
                YWR, YWRSFZJZL, YWRZJH, YWRLXDH, YWRDLR, YWRDLRZJZL, YWRDLRZJH, YWRDLRDH, "ZL", UPPERCASE_BDCDYH, YBDCQZH, "FWYT", "FWMJ", "TDYT",
                "TDMJ", TDSYKSQX, TDSYJSQX, ZDZHQLXZ, "SFGY", GYQK, SQZSBS, BDBZZQSE, DBFW, ZJGCDYFW, ZWLXKSQX, ZWLXJSQX, FWDYMJ, TDDYMJ};
        HashMap map = new HashMap();
        map.put(LOWERCASE_PROID, proid);
        hashMapList = getSqsxxList(map);
        //组织数据
        if (CollectionUtils.isNotEmpty(hashMapList)) {
            for (HashMap hashMap : hashMapList) {
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                    dataSourceDataList.add(dataSourceData);
                }
            }
        } else {
            for (int i = 0; i != fpname.length; i++) {
                dataSourceDataList.add(zzData(fpname[i], STRING, ""));
            }
        }
        dataToPrintXml.setData(dataSourceDataList);
        return dataToPrintXml;
    }

    @Override
    public DataToPrintXml getSpbXxXml(String proid) {
        List<HashMap> hashMapList;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;
        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        //在数据为空的时候赋空防止有控件默认数据出现
        String[] fpname = {"TXM", "BH", "DJSY", DYDJLX, SQFBCZ, QLR, QLRSFZJZL, QLRZJH, QLRQLBL,
                YWR, YWRSFZJZL, YWRZJH, YWRQLBL, "ZL", UPPERCASE_BDCDYH, BDCLX, UPPERCASE_TDORTDFWMJ, UPPERCASE_TDORTDFWYT, UPPERCASE_TDORTDFWQLXZ, YBDCQZH,
                GYQK, SQZSBS, "CSR", CSSIGNDATE, CSOPINION, "FSR", FSSIGNDATE, FSOPINION, "HDR", HDSIGNDATE, HDOPINION,
                BDBZZQSE, DBFW, ZJGCDYFW, ZWLXKSQX, ZWLXJSQX, TDDYJG, FWDYJG, "ZJG", QLQTZK, FJ};
        HashMap map = new HashMap();
        map.put(LOWERCASE_PROID, proid);
        hashMapList = getSpbxxList(map);
        //组织数据
        if (CollectionUtils.isNotEmpty(hashMapList)) {
            for (HashMap hashMap : hashMapList) {
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                    dataSourceDataList.add(dataSourceData);
                }
            }
        } else {
            for (int i = 0; i != fpname.length; i++) {
                dataSourceDataList.add(zzData(fpname[i], STRING, ""));
            }
        }
        dataToPrintXml.setData(dataSourceDataList);
        return dataToPrintXml;
    }

    @Override
    public DataToPrintXml getFzjlXxXml(String proid) {
        List<HashMap> hashMapList;
        DataToPrintXml dataToPrintXml = new DataToPrintXml();
        XmlData dataSourceData = null;
        List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
        //在数据为空的时候赋空防止有控件默认数据出现
        String[] fpname = {"BDCQZH", "QLR", "YWR", "GYQK", "ZL", UPPERCASE_BDCDYH, "QLLX", "QLXZ", "YT", "MJ",
                "SYQX", "BH", "ZSBH", QLQTZK, FJ, "LZRQ", BDCLX, "DJSJ"};
        HashMap map = new HashMap();
        map.put(LOWERCASE_PROID, proid);
        hashMapList = getFzjlxxList(map);
        //组织数据
        if (CollectionUtils.isNotEmpty(hashMapList)) {
            for (HashMap hashMap : hashMapList) {
                Iterator iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                    dataSourceDataList.add(dataSourceData);
                }
            }
        } else {
            for (int i = 0; i != fpname.length; i++) {
                dataSourceDataList.add(zzData(fpname[i], STRING, ""));
            }
        }
        dataToPrintXml.setData(dataSourceDataList);
        return dataToPrintXml;
    }

    @Override
    public MulDataToPrintXml getAllSqsxxPrintXml(Map<String, String> proidMap) {
        List<HashMap> hashMapList;
        MulDataToPrintXml muldataToPrintXml = new MulDataToPrintXml();
        XmlData dataSourceData = null;
        List<PageXml> pageXmlList = new LinkedList<PageXml>();
        for (Map.Entry proidEntry : proidMap.entrySet()) {
            String proid = String.valueOf(proidEntry.getValue());
            PageXml pageXml = new PageXml();
            List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
            //在数据为空的时候赋空防止有控件默认数据出现
            String[] fpname = {"TXM", "BH", "DJSY", DYDJLX, QLR, QLRSFZJZL, QLRZJH, QLRLXDH, QLRDLR, QLRDLRZJZL, QLRDLRZJH, QLRDLRDH,
                    YWR, YWRSFZJZL, YWRZJH, YWRLXDH, YWRDLR, YWRDLRZJZL, YWRDLRZJH, YWRDLRDH, "ZL", UPPERCASE_BDCDYH, YBDCQZH, "FWYT", "FWMJ", "TDYT",
                    "TDMJ", TDSYKSQX, TDSYJSQX, ZDZHQLXZ, "SFGY", GYQK, SQZSBS, BDBZZQSE, DBFW, ZJGCDYFW, ZWLXKSQX, ZWLXJSQX, FWDYMJ, TDDYMJ};
            HashMap map = new HashMap();
            map.put(LOWERCASE_PROID, proid);
            hashMapList = getSqsxxList(map);
            //组织数据
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                        dataSourceDataList.add(dataSourceData);
                    }
                }
            } else {
                for (int i = 0; i != fpname.length; i++) {
                    dataSourceDataList.add(zzData(fpname[i], STRING, ""));
                }
            }
            pageXml.setData(dataSourceDataList);
            pageXmlList.add(pageXml);
        }
        muldataToPrintXml.setPage(pageXmlList);
        return muldataToPrintXml;
    }

    @Override
    public List<HashMap> getSpbxxList(HashMap map) {
        List<HashMap> bdcSpbxxListTmp = bdcXmMapper.getSpbxxList(map);
        List<BdcZdZjlx> zjlxList = bdcZdGlService.getBdcZdZjlx();
        List<HashMap> bdczddjlxList = bdcZdGlService.getBdcZdDjlx(new HashMap());
        List<Map> bdclxList = bdcZdGlService.getZdBdclx();
        if (CollectionUtils.isNotEmpty(bdcSpbxxListTmp)) {
            //通过主proid获取签名信息
            String pro_id = null;
            PfSignVo csrSign = new PfSignVo();
            PfSignVo fsrSign = new PfSignVo();
            PfSignVo hdrSign = new PfSignVo();
            HashMap spbxxHashMap = bdcSpbxxListTmp.get(0);
            if (spbxxHashMap != null && spbxxHashMap.get(UPPERCASE_PROID) != null && StringUtils.isNotEmpty(spbxxHashMap.get(UPPERCASE_PROID).toString())) {
                BdcXm bdcXm = getBdcXmByProid(spbxxHashMap.get(UPPERCASE_PROID).toString());
                if (bdcXm != null && StringUtils.isNotEmpty(bdcXm.getWiid())) {
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(bdcXm.getWiid());
                    if (pfWorkFlowInstanceVo != null) {
                        pro_id = pfWorkFlowInstanceVo.getProId();
                    }
                }
            }
            if (StringUtils.isNotEmpty(pro_id)) {
                List<PfSignVo> csrSignList = sysSignService.getSignList("csr", pro_id);
                if (CollectionUtils.isNotEmpty(csrSignList)) {
                    csrSign = csrSignList.get(0);
                }
                List<PfSignVo> fsrSignList = sysSignService.getSignList("fsr", pro_id);
                if (CollectionUtils.isNotEmpty(fsrSignList)) {
                    fsrSign = fsrSignList.get(0);
                }
                List<PfSignVo> hdrSignList = sysSignService.getSignList("hdr", pro_id);
                if (CollectionUtils.isNotEmpty(hdrSignList)) {
                    hdrSign = hdrSignList.get(0);
                }
            }
            for (HashMap spbXx : bdcSpbxxListTmp) {
                String djsy = "";
                String djlx = "";
                String bdclx = "";
                String csr = "";
                String fsr = "";
                String hdr = "";
                String cssigndate = "";
                String fssigndate = "";
                String hdsigndate = "";
                String csopinion = "";
                String fsopinion = "";
                String hdopinion = "";
                if (spbXx.get(UPPERCASE_PROID) != null && StringUtils.isNotEmpty(spbXx.get(UPPERCASE_PROID).toString())) {
                    //处理登记事由
                    djsy = bdcXmMapper.getDjsyMcByProid(spbXx.get(UPPERCASE_PROID).toString());
                    if (StringUtils.isNotEmpty(djsy)) {
                        spbXx.put("DJSY", djsy);
                    } else {
                        spbXx.put("DJSY", "");
                    }
                    //处理登记类型
                    if (spbXx.get("DJLX") != null && StringUtils.isNotEmpty(spbXx.get("DJLX").toString())) {
                        djlx = bdcZdGlService.getDjlxMcByDm(spbXx.get("DJLX").toString(), bdczddjlxList);
                    }
                    if (StringUtils.isNotEmpty(djlx)) {
                        spbXx.put(DYDJLX, djlx);
                    } else {
                        spbXx.put(DYDJLX, "");
                    }
                    //处理共有方式
                    if (spbXx.get("GYQK") == null || (spbXx.get("GYQK") != null && StringUtils.isBlank(spbXx.get("GYQK").toString()))) {
                        spbXx.put("GYQK", "单独所有");
                    }
                    //添加权利人信息
                    spbXx = addQlrxxForPrint(spbXx, spbXx.get(UPPERCASE_PROID).toString(), zjlxList);
                    //添加义务人信息
                    spbXx = addYwrxxForPrint(spbXx, spbXx.get(UPPERCASE_PROID).toString(), zjlxList);
                    //添加抵押权信息
                    spbXx = addDyaxxForPrint(spbXx, spbXx.get(UPPERCASE_PROID).toString());
                    //获取拼接的房屋土地信息
                    HashMap<String, String> tdfwCXxHashmap = bdcFdcqService.getTdAndFwSjxx(spbXx.get(UPPERCASE_PROID).toString());
                    //处理土地房屋面积
                    if (tdfwCXxHashmap != null && tdfwCXxHashmap.get(TDORTDFWMJ) != null && StringUtils.isNotEmpty(tdfwCXxHashmap.get(TDORTDFWMJ))) {
                        spbXx.put(UPPERCASE_TDORTDFWMJ, tdfwCXxHashmap.get(TDORTDFWMJ));
                    } else {
                        spbXx.put(UPPERCASE_TDORTDFWMJ, "");
                    }
                    //处理土地房屋用途
                    if (tdfwCXxHashmap != null && tdfwCXxHashmap.get(TDORTDFWYT) != null && StringUtils.isNotEmpty(tdfwCXxHashmap.get(TDORTDFWYT))) {
                        spbXx.put(UPPERCASE_TDORTDFWYT, tdfwCXxHashmap.get(TDORTDFWYT));
                    } else {
                        spbXx.put(UPPERCASE_TDORTDFWYT, "");
                    }
                    //处理土地房屋权利性质
                    if (tdfwCXxHashmap != null && tdfwCXxHashmap.get(TDORTDFWQLXZ) != null && StringUtils.isNotEmpty(tdfwCXxHashmap.get(TDORTDFWQLXZ))) {
                        spbXx.put(UPPERCASE_TDORTDFWQLXZ, tdfwCXxHashmap.get(TDORTDFWQLXZ));
                    } else {
                        spbXx.put(UPPERCASE_TDORTDFWQLXZ, "");
                    }
                    //处理不动产类型
                    if (spbXx.get(BDCLX) != null && StringUtils.isNotEmpty(spbXx.get(BDCLX).toString())) {
                        bdclx = bdcZdGlService.getBdclxMcByDm(spbXx.get(BDCLX).toString(), bdclxList);
                        if (StringUtils.isNotEmpty(bdclx)) {
                            spbXx.put(BDCLX, bdclx);
                        }
                    }
                    //处理权利其他状况和附记
                    spbXx = addQlqtzkAndFjForPrint(spbXx, spbXx.get(UPPERCASE_PROID).toString());
                    //处理初审签名信息
                    if (StringUtils.isNotEmpty(csrSign.getSignName())) {
                        csr = csrSign.getSignName();
                    }
                    if (StringUtils.isNotEmpty(csrSign.getSignOpinion())) {
                        csopinion = csrSign.getSignOpinion();
                    }
                    if (csrSign.getSignDate() != null) {
                        cssigndate = CalendarUtil.sdf_China.format(csrSign.getSignDate());
                    }
                    //处理复审签名信息
                    if (StringUtils.isNotEmpty(fsrSign.getSignName())) {
                        fsr = fsrSign.getSignName();
                    }
                    if (StringUtils.isNotEmpty(fsrSign.getSignOpinion())) {
                        fsopinion = fsrSign.getSignOpinion();
                    }
                    if (fsrSign.getSignDate() != null) {
                        fssigndate = CalendarUtil.sdf_China.format(fsrSign.getSignDate());
                    }
                    //处理核定签名信息
                    if (StringUtils.isNotEmpty(hdrSign.getSignName())) {
                        hdr = hdrSign.getSignName();
                    }
                    if (StringUtils.isNotEmpty(hdrSign.getSignOpinion())) {
                        hdopinion = hdrSign.getSignOpinion();
                    }
                    if (hdrSign.getSignDate() != null) {
                        hdsigndate = CalendarUtil.sdf_China.format(hdrSign.getSignDate());
                    }
                    spbXx.put("CSR", csr);
                    spbXx.put(CSOPINION, csopinion);
                    spbXx.put(CSSIGNDATE, cssigndate);
                    spbXx.put("FSR", fsr);
                    spbXx.put(FSOPINION, fsopinion);
                    spbXx.put(FSSIGNDATE, fssigndate);
                    spbXx.put("HDR", hdr);
                    spbXx.put(HDOPINION, hdopinion);
                    spbXx.put(HDSIGNDATE, hdsigndate);
                }
            }
        }
        return bdcSpbxxListTmp;
    }

    public List<HashMap> getFzjlxxList(HashMap map) {
        List<HashMap> bdcFzjlxxListTmp = bdcXmMapper.getFzjlZsxxList(map);
        if (CollectionUtils.isNotEmpty(bdcFzjlxxListTmp)) {
            for (HashMap fzjlXx : bdcFzjlxxListTmp) {
                String gyqk = "";
                String qllx = "";
                if (fzjlXx.get(UPPERCASE_PROID) != null && StringUtils.isNotEmpty(fzjlXx.get(UPPERCASE_PROID).toString())) {
                    HashMap queryMap = new HashMap();
                    queryMap.put("proid", fzjlXx.get(UPPERCASE_PROID).toString());
                    HashMap bdcFzjlBdcqzxx = bdcZsMapper.getFzjlBdcqzxxList(queryMap);
                    HashMap bdcFzjlQlrxx = bdcQlrMapper.getFzjlQlrxxList(queryMap);
                    if (bdcFzjlBdcqzxx != null) {
                        fzjlXx.putAll(bdcFzjlBdcqzxx);
                    }
                    if (bdcFzjlQlrxx != null) {
                        fzjlXx.putAll(bdcFzjlQlrxx);
                    }
                    //处理共有方式
                    if (fzjlXx.get("GYQK") != null && StringUtils.isNotEmpty(fzjlXx.get("GYQK").toString())) {
                        gyqk = bdcZdGlService.getGyfsMcByDm(fzjlXx.get("GYQK").toString());
                        if (StringUtils.isNotEmpty(gyqk)) {
                            fzjlXx.put("GYQK", gyqk);
                        } else {
                            fzjlXx.put("GYQK", "");
                        }
                    }
                    if (fzjlXx.get("GYQK") == null || (fzjlXx.get("GYQK") != null && StringUtils.isBlank(fzjlXx.get("GYQK").toString()))) {
                        fzjlXx.put("GYQK", "单独所有");
                    }
                    //处理权利类型
                    if (fzjlXx.get("QLLX") != null && StringUtils.isNotEmpty(fzjlXx.get("QLLX").toString())) {
                        qllx = bdcZdGlService.getQllxMcByDm(fzjlXx.get("QLLX").toString());
                        if (StringUtils.isNotEmpty(qllx)) {
                            fzjlXx.put("QLLX", qllx);
                        } else {
                            fzjlXx.put("QLLX", "");
                        }
                    }
                    //处理登记时间
                    addDjsjForPrint(fzjlXx, fzjlXx.get(UPPERCASE_PROID).toString());
                }
            }
        }
        return bdcFzjlxxListTmp;
    }

    @Override
    public MulDataToPrintXml getAllSpbxxPrintXml(Map<String, String> proidMap) {
        List<HashMap> hashMapList;
        MulDataToPrintXml muldataToPrintXml = new MulDataToPrintXml();
        XmlData dataSourceData = null;
        List<PageXml> pageXmlList = new LinkedList<PageXml>();
        for (Map.Entry proidEntry : proidMap.entrySet()) {
            String proid = String.valueOf(proidEntry.getValue());
            PageXml pageXml = new PageXml();
            List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
            //在数据为空的时候赋空防止有控件默认数据出现
            String[] fpname = {"TXM", "BH", "DJSY", DYDJLX, SQFBCZ, QLR, QLRSFZJZL, QLRZJH, QLRQLBL,
                    YWR, YWRSFZJZL, YWRZJH, YWRQLBL, "ZL", UPPERCASE_BDCDYH, BDCLX, UPPERCASE_TDORTDFWMJ, UPPERCASE_TDORTDFWYT, UPPERCASE_TDORTDFWQLXZ, YBDCQZH,
                    GYQK, SQZSBS, "CSR", CSSIGNDATE, CSOPINION, "FSR", FSSIGNDATE, FSOPINION, "HDR", HDSIGNDATE, HDOPINION,
                    BDBZZQSE, DBFW, ZJGCDYFW, ZWLXKSQX, ZWLXJSQX, TDDYJG, FWDYJG, "ZJG", "BZ"};
            HashMap map = new HashMap();
            map.put(LOWERCASE_PROID, proid);
            hashMapList = getSpbxxList(map);
            //组织数据
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                        dataSourceDataList.add(dataSourceData);
                    }
                }
            } else {
                for (int i = 0; i != fpname.length; i++) {
                    dataSourceDataList.add(zzData(fpname[i], STRING, ""));
                }
            }
            pageXml.setData(dataSourceDataList);
            pageXmlList.add(pageXml);
        }
        muldataToPrintXml.setPage(pageXmlList);
        return muldataToPrintXml;
    }

    @Override
    public MulDataToPrintXml getAllFzjlxxPrintXml(Map<String, String> proidMap) {
        List<HashMap> hashMapList;
        MulDataToPrintXml muldataToPrintXml = new MulDataToPrintXml();
        XmlData dataSourceData = null;
        List<PageXml> pageXmlList = new LinkedList<PageXml>();
        for (Map.Entry proidEntry : proidMap.entrySet()) {
            String proid = String.valueOf(proidEntry.getValue());
            PageXml pageXml = new PageXml();
            List<XmlData> dataSourceDataList = new ArrayList<XmlData>();
            //在数据为空的时候赋空防止有控件默认数据出现
            String[] fpname = {"BDCQZH", QLR, GYQK, "ZL", UPPERCASE_BDCDYH, QLLX, "QLXZ", "YT", "MJ",
                    "SYQX", "BH", "ZSBH", QLQTZK, FJ, "LZRQ", BDCLX, "DJSJ"};
            HashMap map = new HashMap();
            map.put(LOWERCASE_PROID, proid);
            hashMapList = getFzjlxxList(map);
            //组织数据
            if (CollectionUtils.isNotEmpty(hashMapList)) {
                for (HashMap hashMap : hashMapList) {
                    Iterator iterator = hashMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        dataSourceData = zzData(CommonUtil.formatEmptyValue(entry.getKey()), STRING, CommonUtil.formatEmptyValue(entry.getValue()));
                        dataSourceDataList.add(dataSourceData);
                    }
                }
            } else {
                for (int i = 0; i != fpname.length; i++) {
                    dataSourceDataList.add(zzData(fpname[i], STRING, ""));
                }
            }
            pageXml.setData(dataSourceDataList);
            pageXmlList.add(pageXml);
        }
        muldataToPrintXml.setPage(pageXmlList);
        return muldataToPrintXml;
    }

    //创建data类并塞入数据
    public XmlData zzData(String name, String type, String value) {
        XmlData xmlData = new XmlData();
        xmlData.setName(name);
        xmlData.setType(type);
        xmlData.setValue(value);
        return xmlData;
    }

    @Override
    public List<String> getXsQlProid(HashMap map) {
        return bdcXmMapper.getXsQlProid(map);
    }

    @Override
    public String completeXmbh(String proid) {
        String msg = "fail";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = getBdcXmByProid(proid);
            if (bdcXm != null) {
                bdcXm.setBh(creatXmbh(bdcXm));
                entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());
                if (StringUtils.isNotBlank(bdcXm.getBh())) {
                    msg = "success";
                }
            }
        }
        return msg;
    }

    @Override
    public List<String> getXsCqProid(String bdcdyid) {
        HashMap map = new HashMap();
        map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
        return bdcXmMapper.getXsCqProid(map);
    }

    /**
     * @param dydjzmh
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 遗失补证等流程选择他项证返回的原不动产证书应是房产证号而不是他项证明号
     */
    private String getYcqzhByDyzmh(String dydjzmh) {
        String ycqzh = "";
        GdDy gdDy = gdDyService.getGdDyByDydjzmh(dydjzmh);
        if (null != gdDy && StringUtils.isNotBlank(gdDy.getDyid())) {
            List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdDy.getDyid());
            if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                GdBdcQlRel gdBdcQlRel = gdBdcQlRelList.get(0);
                if (null != gdBdcQlRel && StringUtils.isNotBlank(gdBdcQlRel.getBdcid())) {
                    List<GdBdcQlRel> gdBdcQlRels = gdBdcQlRelService.queryGdBdcQlListByBdcid(gdBdcQlRel.getBdcid());
                    if (CollectionUtils.isNotEmpty(gdBdcQlRels)) {
                        for (GdBdcQlRel gdBdcQlRelTemp : gdBdcQlRels) {
                            if (null != gdBdcQlRelTemp) {
                                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRelTemp.getQlid());
                                if (null != gdFwsyq && StringUtils.isNotBlank(gdFwsyq.getFczh()) && null != gdFwsyq.getIszx() && Constants.GDQL_ISZX_WZX.equals(gdFwsyq.getIszx())) {
                                    ycqzh = gdFwsyq.getFczh();
                                    break;
                                }
                                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRelTemp.getQlid());
                                if (null != gdTdsyq && StringUtils.isNotBlank(gdTdsyq.getTdzh()) && null != gdTdsyq.getIszx() && Constants.GDQL_ISZX_WZX.equals(gdTdsyq.getIszx())) {
                                    ycqzh = gdTdsyq.getTdzh();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return ycqzh;
    }

    @Override
    public Boolean isHb(String proid) {
        Boolean isHb = false;
        int dyaqNumber = 0;
        int otherNumber = 0;
        String wiid = "";
        BdcXm bdcXm = getBdcXmByProid(proid);
        if (bdcXm != null) {
            wiid = bdcXm.getWiid();
        }
        if (StringUtils.isNotBlank(wiid)) {
            List<BdcXm> bdcXmList = getBdcXmListByWiid(wiid);
            String wfDfid = PlatformUtil.getWfDfidByWiid(wiid);
            String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(wfDfid);
            String wfDjlx = bdcZdGlService.getDjlxBySqlx(sqlxdm);
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm isHbXm : bdcXmList) {
                    if (dyaqNumber > 0 && otherNumber > 0) {
                        isHb = true;
                        break;
                    }
                    String qllx = isHbXm.getQllx();
                    if (StringUtils.equals(qllx, Constants.QLLX_DYAQ)) {
                        dyaqNumber += 1;
                    } else if (!StringUtils.equals(isHbXm.getQllx(), Constants.QLLX_YGDJ)) {
                        otherNumber += 1;
                    }
                    //预告合并登记单独处理
                    if (StringUtils.equals(wfDjlx, Constants.DJLX_HBDJ_DM) && StringUtils.equals(isHbXm.getQllx(), Constants.QLLX_YGDJ)) {
                        isHb = true;
                        break;
                    }

                }
                if (dyaqNumber > 0 && otherNumber > 0) {
                    isHb = true;
                }
            }
            if (StringUtils.isNotBlank(sqlxdm) && StringUtils.equals(sqlxdm, "111")) {
                isHb = false;
            }
            if (StringUtils.isNotBlank(sqlxdm) && (StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBHZ_DM) || StringUtils.equals(sqlxdm, "9960402") || StringUtils.equals(sqlxdm, "9960401") || StringUtils.equals(sqlxdm, Constants.SQLX_TDFGHBZY_DM) || StringUtils.equals(sqlxdm, "9960301"))) {
                isHb = true;
            }
        }
        return isHb;
    }

    @Override
    public void inheritXzzrnxInfo(BdcXm bdcXm, Boolean sz) {
        if (bdcXm != null) {
            //判断是否属于需要继承限制转让年限信息的流程
            String xzzrSqlxs = AppConfig.getProperty("xzzrnx.inheritFj.sqlxdm");
            if (StringUtils.isNotBlank(xzzrSqlxs) && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.indexOf(xzzrSqlxs, bdcXm.getSqlx()) > -1) {
                Date xzzrdbsj = null;
                Integer xzzrnx = null;
                BdcFdcq bdcFdcq = null;
                BdcFdcqDz bdcFdcqDz = null;
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    bdcFdcq = bdcFdcqList.get(0);
                    if (bdcFdcq.getXzzrnx() != null && bdcFdcq.getXzzrnx() > 0) {
                        xzzrnx = bdcFdcq.getXzzrnx();
                        xzzrdbsj = bdcFdcq.getXzzrdbsj();
                    }
                } else {
                    bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXm.getProid());
                    if (bdcFdcqDz != null && bdcFdcqDz.getXzzrnx() != null && bdcFdcqDz.getXzzrnx() > 0) {
                        xzzrnx = bdcFdcqDz.getXzzrnx();
                        xzzrdbsj = bdcFdcqDz.getXzzrdbsj();
                    }
                }
                //xzzrnx是否为空决定是否需要继承
                if (xzzrnx != null && xzzrnx > 0) {
                    String xzzrnxFjmb = AppConfig.getProperty("xzzrnx.fj.mb");
                    if (StringUtils.isBlank(xzzrnxFjmb)) {
                        xzzrnxFjmb = ParamsConstants.XZZRNX_FJ_MB;
                    }
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                    if (sz) {
                        if (bdcFdcq != null) {
                            if (xzzrdbsj == null) {
                                if (bdcFdcq.getDjsj() != null) {
                                    bdcFdcq.setXzzrdbsj(bdcFdcq.getDjsj());
                                } else {
                                    bdcFdcq.setXzzrdbsj(CalendarUtil.getCurHMSDate());
                                }
                                xzzrdbsj = bdcFdcq.getXzzrdbsj();
                            }
                            if (StringUtils.isNotBlank(bdcFdcq.getFj())) {
                                bdcFdcq.setFj(StringUtils.replace(bdcFdcq.getFj(), ParamsConstants.XZZR_DBSJ_FJ_PARAM, DateUtils.formatTime(xzzrdbsj, SLBH_DATE_FORMAT)));
                            }
                            entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                        } else if (bdcFdcqDz != null) {
                            if (xzzrdbsj == null) {
                                if (bdcFdcqDz.getDjsj() != null) {
                                    bdcFdcqDz.setXzzrdbsj(bdcFdcqDz.getDjsj());
                                } else {
                                    bdcFdcqDz.setXzzrdbsj(CalendarUtil.getCurHMSDate());
                                }
                                xzzrdbsj = bdcFdcqDz.getXzzrdbsj();
                            }
                            if (StringUtils.isNotBlank(bdcFdcqDz.getFj()) && xzzrdbsj != null) {
                                bdcFdcqDz.setFj(StringUtils.replace(bdcFdcqDz.getFj(), ParamsConstants.XZZR_DBSJ_FJ_PARAM, DateUtils.formatTime(xzzrdbsj, SLBH_DATE_FORMAT)));
                            }
                            entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
                        }
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                if (xzzrdbsj != null) {
                                    if (StringUtils.isNotBlank(bdcZs.getFj())) {
                                        bdcZs.setFj(bdcZs.getFj().replace(ParamsConstants.XZZR_DBSJ_FJ_PARAM, DateUtils.formatTime(xzzrdbsj, SLBH_DATE_FORMAT)));
                                    }
                                    entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                                }
                            }
                        }
                    } else {
                        String fjContent = xzzrnxFjmb.replace(ParamsConstants.XZZR_NX_FJ_PARAM, xzzrnx.toString());
                        if (xzzrdbsj != null) {
                            fjContent = fjContent.replace(ParamsConstants.XZZR_DBSJ_FJ_PARAM, DateUtils.formatTime(xzzrdbsj, SLBH_DATE_FORMAT));
                        }
                        if (bdcFdcq != null && StringUtils.isNotBlank(fjContent)) {
                            if (StringUtils.isNotBlank(bdcFdcq.getFj())) {
                                bdcFdcq.setFj(bdcFdcq.getFj() + "\n" + fjContent);
                            } else {
                                bdcFdcq.setFj(fjContent);
                            }
                            entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                        } else if (bdcFdcqDz != null && StringUtils.isNotBlank(fjContent)) {
                            if (StringUtils.isNotBlank(bdcFdcqDz.getFj())) {
                                bdcFdcqDz.setFj(bdcFdcqDz.getFj() + "\n" + fjContent);
                            } else {
                                bdcFdcqDz.setFj(fjContent);
                            }
                            entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
                        }
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            for (BdcZs bdcZs : bdcZsList) {
                                if (StringUtils.isNotBlank(bdcZs.getFj())) {
                                    bdcZs.setFj(bdcZs.getFj() + "\n" + fjContent);
                                } else {
                                    bdcZs.setFj(fjContent);
                                }
                                entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void delXzzrnxInfoFj(BdcXm bdcXm) {
        if (bdcXm != null) {
            String qlFj = "";
            BdcFdcq bdcFdcq = null;
            BdcFdcqDz bdcFdcqDz = null;
            String xzzrSqlxs = AppConfig.getProperty("xzzrnx.inheritFj.sqlxdm");
            if (StringUtils.isNotBlank(xzzrSqlxs) && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.indexOf(xzzrSqlxs, bdcXm.getSqlx()) > -1) {
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    bdcFdcq = bdcFdcqList.get(0);
                    qlFj = bdcFdcq.getFj();
                } else {
                    bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXm.getProid());
                    if (bdcFdcqDz != null && bdcFdcqDz.getXzzrnx() != null && bdcFdcqDz.getXzzrnx() > 0) {
                        qlFj = bdcFdcqDz.getFj();
                    }
                }
            }
            if (StringUtils.isNotBlank(qlFj)) {
                StringBuilder resultFj = new StringBuilder();
                if (StringUtils.indexOf(qlFj, ParamsConstants.XZZR_NX_FJ_FKZR) > -1) {
                    String[] qlFjs = qlFj.split("\\n");
                    List<String> qlFjList = Arrays.asList(qlFjs);
                    for (String fj : qlFjList) {
                        if (StringUtils.indexOf(fj, ParamsConstants.XZZR_NX_FJ_FKZR) == -1) {
                            resultFj.append(fj);
                        }
                    }
                    if (bdcFdcq != null) {
                        bdcFdcq.setFj(resultFj.toString());
                        entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
                    } else {
                        if (bdcFdcqDz != null) {
                            bdcFdcqDz.setFj(resultFj.toString());
                            entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
                        }
                    }
                }
            }
        }

    }

    @Override
    public void inheritXzzrnxInfoForNullPreviewZs(BdcXm bdcXm) {
        if (bdcXm != null) {
            String xzzrSqlxs = AppConfig.getProperty("xzzrnx.inheritFj.sqlxdm");
            if (StringUtils.isNotBlank(xzzrSqlxs) && StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.indexOf(xzzrSqlxs, bdcXm.getSqlx()) > -1) {
                Date xzzrdbsj = null;
                BdcFdcq bdcFdcq = null;
                BdcFdcqDz bdcFdcqDz = null;
                Integer xzzrnx = null;
                List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                    bdcFdcq = bdcFdcqList.get(0);
                } else {
                    bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(bdcXm.getProid());
                }
                if (bdcFdcq != null) {
                    xzzrdbsj = bdcFdcq.getXzzrdbsj();
                    xzzrnx = bdcFdcq.getXzzrnx();
                } else {
                    if (bdcFdcqDz != null) {
                        xzzrdbsj = bdcFdcqDz.getXzzrdbsj();
                        xzzrnx = bdcFdcqDz.getXzzrnx();
                    }
                }
                if (xzzrnx != null && xzzrnx > 0) {
                    String xzzrnxFjmb = AppConfig.getProperty("xzzrnx.fj.mb");
                    if (StringUtils.isBlank(xzzrnxFjmb)) {
                        xzzrnxFjmb = ParamsConstants.XZZRNX_FJ_MB;
                    }
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcZsList)) {
                        String fjContent = xzzrnxFjmb.replace(ParamsConstants.XZZR_NX_FJ_PARAM, xzzrnx.toString());
                        if (xzzrdbsj == null) {
                            xzzrdbsj = CalendarUtil.getCurHMSDate();
                        }
                        fjContent = fjContent.replace(ParamsConstants.XZZR_DBSJ_FJ_PARAM, DateUtils.formatTime(xzzrdbsj, SLBH_DATE_FORMAT));
                        for (BdcZs bdcZs : bdcZsList) {
                            if (StringUtils.isNotBlank(bdcZs.getFj())) {
                                bdcZs.setFj(bdcZs.getFj() + "\n" + fjContent);
                            } else {
                                bdcZs.setFj(fjContent);
                            }
                            entityMapper.saveOrUpdate(bdcZs, bdcZs.getZsid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void zsAopByBdcXm(BdcXm bdcXm) {
        System.out.println("bdcXmService.zsAopByBdcXm");
    }

    @Override
    public Map<String, String> zscdGetXsCqzhAndProidByBdcXm(BdcXm bdcXm) {
        String cqzh = null;
        String proid = null;
        Map<String, String> map = new HashMap<String, String>();
        BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
        //通过bdcxmrel找到对应现势产权
        List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
        if (StringUtils.isNotBlank(bdcXm.getBdcdyid())) {
            //匹配数据找现势产权
            List<String> xsCqProidList = getXsCqProid(bdcXm.getBdcdyid());
            if (CollectionUtils.isNotEmpty(xsCqProidList)) {
                String xsCqProid = xsCqProidList.get(0);
                List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(xsCqProid);
                if (CollectionUtils.isNotEmpty(bdcZsList)) {
                    BdcZs bdcZs = bdcZsList.get(0);
                    cqzh = bdcZs.getBdcqzh();
                    proid = xsCqProid;
                }
            } else {
                //产权不在不动产中 则去过渡中找
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                    List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel(bdcBdcdy.getBdcdyh(), null, null);
                    if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
                        for (GdQlDyhRel gdQlDyhRel : gdQlDyhRelList) {
                            GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdQlDyhRel.getQlid());
                            if (gdFwsyq != null && gdFwsyq.getIszx() == Constants.GDQL_ISZX_WZX) {
                                cqzh = gdFwsyq.getFczh();
                                proid = gdFwsyq.getProid();
                                break;
                            }
                            GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(gdQlDyhRel.getQlid());
                            if (gdTdsyq != null && gdTdsyq.getIszx() == Constants.GDQL_ISZX_WZX) {
                                cqzh = gdTdsyq.getTdzh();
                                proid = gdTdsyq.getProid();
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            //未匹配数据找现势产权
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                GdCf gdCf = gdCfService.getGdCfByCfid(bdcXmRelList.get(0).getYqlid());
                if (gdCf != null) {
                    List<GdBdcQlRel> gdBdcQlRelList = gdBdcQlRelService.queryGdBdcQlListByQlid(gdCf.getCfid());
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        List<GdBdcQlRel> gdBdcQlRelListTemp = gdBdcQlRelService.queryGdBdcQlListByBdcid(gdBdcQlRelList.get(0).getBdcid());
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelListTemp)) {
                            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(gdBdcQlRel.getQlid());
                                if (gdFwsyq != null && gdFwsyq.getIszx() == Constants.GDQL_ISZX_WZX) {
                                    cqzh = gdFwsyq.getFczh();
                                    proid = gdFwsyq.getProid();
                                    break;
                                }
                                GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(gdBdcQlRel.getQlid());
                                if (gdTdsyq != null && gdTdsyq.getIszx() == Constants.GDQL_ISZX_WZX) {
                                    cqzh = gdTdsyq.getTdzh();
                                    proid = gdTdsyq.getProid();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(cqzh) && StringUtils.isNotBlank(proid)) {
            map.put(ParamsConstants.PROID_LOWERCASE, proid);
            map.put(ParamsConstants.CQZH_LOWERCASE, cqzh);
        }
        return map;
    }

    @Override
    public BdcXm initBdcXmFromProjectPar(ProjectPar projectPar) {
        BdcXm bdcXm = new BdcXm();
        if (projectPar != null) {
            if (StringUtils.isBlank(projectPar.getUserId())) {
                projectPar.setUserId(SessionUtil.getCurrentUserId());
            }
            if (StringUtils.isNotBlank(projectPar.getUserId())) {
                projectPar.setDwdm(PlatformUtil.getCurrentUserDwdmByUserid(projectPar.getUserId()));
                projectPar.setCjr(PlatformUtil.getCurrentUserName(projectPar.getUserId()));
            } else {
                projectPar.setDwdm(SessionUtil.getCurrentUser().getRegionCode());
                projectPar.setCjr(SessionUtil.getCurrentUser().getUsername());
            }
            if (StringUtils.isNotBlank(projectPar.getBh())) {
                bdcXm.setBh(projectPar.getBh());
            }
            if (StringUtils.isNotBlank(projectPar.getGdslbh())) {
                bdcXm.setYbh(projectPar.getGdslbh());
            }
            if (StringUtils.isNotBlank(projectPar.getCjr())) {
                bdcXm.setCjr(projectPar.getCjr());
            }
            if (projectPar.getCjsj() != null) {
                bdcXm.setCjsj(projectPar.getCjsj());
            }
            if (StringUtils.isNotBlank(projectPar.getDwdm())) {
                bdcXm.setDwdm(projectPar.getDwdm());
            }
            if (StringUtils.isNotBlank(projectPar.getProid())) {
                bdcXm.setProid(projectPar.getProid());
            }
            if (StringUtils.isNotBlank(projectPar.getXmly())) {
                bdcXm.setXmly(projectPar.getXmly());
            }
            if (StringUtils.isNotBlank(projectPar.getQllx())) {
                String qllx = projectPar.getQllx();
                //如果在集合中的sqlx的qllx读取权籍的QSXZ并通过对照表
                if (StringUtils.isNotBlank(projectPar.getSqlx()) && CommonUtil.indexOfStrs(Constants.SQLX_QLLX_QJ, projectPar.getSqlx())) {
                    String qsxz = bdcDjsjService.getDjsjQsxz(projectPar.getBdcdyh());
                    if (StringUtils.isNotBlank(qsxz)) {
                        qllx = qsxz;
                    } else {
                        bdcXm.setQllx(projectPar.getQllx());
                    }
                }
                bdcXm.setQllx(qllx);
            }
            if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                bdcXm.setBdcdyid(projectPar.getBdcdyid());
            }
            if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
                bdcXm.setBdcdyh(projectPar.getBdcdyh());
            }
            if (projectPar.getBdcBdcdy() != null && StringUtils.isNotBlank(projectPar.getBdcBdcdy().getFwbm())) {
                bdcXm.setBdcdybh(projectPar.getBdcBdcdy().getFwbm());
            }
            if (StringUtils.isNotBlank(projectPar.getSqlx())) {
                bdcXm.setSqlx(projectPar.getSqlx());
            }
            if (StringUtils.isNotBlank(projectPar.getDjlx())) {
                bdcXm.setDjlx(projectPar.getDjlx());
            }
            if (StringUtils.isNotBlank(projectPar.getWiid())) {
                bdcXm.setWiid(projectPar.getWiid());
            }
            if (StringUtils.isNotBlank(projectPar.getDjsy())) {
                bdcXm.setDjsy(projectPar.getDjsy());
            }
            if (StringUtils.isNotBlank(projectPar.getYbdcqzh())) {
                bdcXm.setYbdcqzh(projectPar.getYbdcqzh());
            }
            if (projectPar.getCjsj() != null) {
                bdcXm.setCjsj(projectPar.getCjsj());
            } else {
                bdcXm.setCjsj(new Date());
            }
            if (StringUtils.isNotBlank(projectPar.getBdclx())) {
                bdcXm.setBdclx(projectPar.getBdclx());
            }
            if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
                bdcXm.setDwmc(dwxxService.getDwmcByDwdm(projectPar.getBdcdyh().substring(0, 9)));
            }
            bdcXm.setXmzt("0");
        }
        return bdcXm;
    }

    @Override
    public void initBdcXmFromYbdcXm(BdcXm bdcXm, BdcXm yBdcXm, ProjectPar projectPar) {
        if (StringUtils.isNotBlank(projectPar.getYxmid())) {
            if (yBdcXm != null) {
                if (StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_CFDJ_DM) || StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_YGDJ_DM)
                        || StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_YYDJ_DM) || StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_DYDJ_DM)) {
                    if (StringUtils.isNotBlank(yBdcXm.getQllx()) && StringUtils.isBlank(bdcXm.getDjsy())) {
                        bdcXm.setDjsy(yBdcXm.getDjsy());
                    }
                    //获取原项目的登记子项
                    if (StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_YYDJ_DM)) {
                        bdcXm.setDjzx("");
                    } else {
                        if (StringUtils.isNotBlank(yBdcXm.getDjzx()) && StringUtils.isBlank(bdcXm.getDjzx()) && (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZYDJ, bdcXm.getSqlx()) || CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, bdcXm.getSqlx()))) {
                            bdcXm.setDjzx(yBdcXm.getDjzx());
                        }
                    }
                }
                if (StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_GZDJ_DM) && StringUtils.isNotBlank(yBdcXm.getQllx())) {
                    bdcXm.setDjsy(yBdcXm.getDjsy());
                }
                if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GYJSYDHB_BGDJ)) {
                    StringBuilder ybh = new StringBuilder();
                    if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                        for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                            BdcXm yXm = getBdcXmByProid(bdcXmRel.getYproid());
                            if (yXm != null) {
                                if (StringUtils.isBlank(ybh)) {
                                    ybh.append(yXm.getBh());
                                } else {
                                    ybh.append("," + yXm.getBh());
                                }
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(ybh)) {
                        bdcXm.setYbh(String.valueOf(ybh));
                    }
                } else {
                    if (StringUtils.isNotBlank(yBdcXm.getBh())) {
                        bdcXm.setYbh(yBdcXm.getBh());
                    }
                }
                if (StringUtils.isBlank(projectPar.getQllx()) && StringUtils.isBlank(bdcXm.getQllx())) {
                    bdcXm.setQllx(yBdcXm.getQllx());
                }
                if (StringUtils.isBlank(bdcXm.getDjsy())) {
                    String djsy = bdcXmMapper.getBdcQllxDjsyRel(bdcXm.getQllx());
                    if (StringUtils.isNotBlank(djsy)) {
                        bdcXm.setDjsy(djsy);
                    }
                }
                if (StringUtils.isNotBlank(yBdcXm.getBdclx()) && StringUtils.isBlank(bdcXm.getBdclx())) {
                    bdcXm.setBdclx(yBdcXm.getBdclx());
                }
                if (!StringUtils.equals(yBdcXm.getQllx(), bdcXm.getQllx())) {
                    bdcXm.setSfyzdf(yBdcXm.getSfyzdf());
                }
            }
        }
    }

    @Override
    public void dealBdcXmDjsyByProjectPar(ProjectPar projectPar, BdcXm bdcXm) {
        if (StringUtils.isBlank(projectPar.getDjsy())) {
            if ((StringUtils.isNotBlank(bdcXm.getXmly()) && StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_TDSP) || StringUtils.equals(bdcXm.getXmly(), Constants.XMLY_FWSP)
                    || (StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_CFDJ_DM) || StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_YGDJ_DM)
                    || StringUtils.equals(projectPar.getDjlx(), Constants.DJLX_YYDJ_DM)))) {
                getDjsyByBdcdyh(bdcXm, projectPar.getBdcdyh());
            } else {
                //针对土地分割合并换证登记获取登记事由
                if (StringUtils.isNotBlank(projectPar.getSqlx()) && !StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_CLF)) {
                    String currentXmWorkFlowName = bdcZdGlService.getSqlxMcByDm(bdcXm.getSqlx());
                    if (StringUtils.isNotBlank(currentXmWorkFlowName)) {
                        String djsy = "";
                        List<Map> mapList = getAllLxByWfName(currentXmWorkFlowName);
                        if (CollectionUtils.isNotEmpty(mapList)) {
                            Map map = mapList.get(0);
                            if (map.get(SQLXDM) != null) {
                                djsy = bdcSqlxDjsyRelService.getDjsyBySqlx(bdcXm.getSqlx());
                            }
                            if (StringUtils.isNotBlank(djsy)) {
                                bdcXm.setDjsy(djsy);
                            } else {
                                if (map.get(DJSYDM) != null)
                                    bdcXm.setDjsy(map.get(DJSYDM).toString());
                            }
                        }
                    }
                }
            }
        }
    }

    public String completeBdcXmYbdcqzmh(ProjectPar projectPar, BdcXm bdcXm) {
        String ybdcqzmh = "";
        //qijiadong 抵押转移、变更、注销登记ybdcqzmh获取上一手
        if (bdcXm != null && (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZYDJ, bdcXm.getSqlx())
                || CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, bdcXm.getSqlx())
                || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM))
                || CommonUtil.indexOfStrs(Constants.SQLX_DY_ZXDJ, bdcXm.getSqlx())) {
            ybdcqzmh = getYbdcqzmh(projectPar.getXmly(), projectPar.getBdcXmRelList());
        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GZ_DM)) {
            if (StringUtils.equals(projectPar.getXmly(), Constants.XMLY_BDC)) {
                BdcXm lastBdcXm = getBdcXmByProid(projectPar.getYxmid());
                if (lastBdcXm != null && StringUtils.isNotBlank(lastBdcXm.getQllx()) && StringUtils.equals(lastBdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                    ybdcqzmh = getYbdcqzmh(projectPar.getXmly(), projectPar.getBdcXmRelList());
                }
            }
        }
        return ybdcqzmh;
    }

    @Override
    public String completeBdcXmYbdcqzh(ProjectPar projectPar, BdcXm bdcXm) {
        String ybdcqzh = bdcXm.getYbdcqzh();
        //jiangganzhi 商品房转移抵押合并登记 存量房转移抵押合并登记，换证和抵押登记 换证（补发）和抵押变更登记 抵押项目在缮证前权利其他状况不显示原不动产权证号
        String wfDfid = PlatformUtil.getWfDfidByWiid(projectPar.getWiid());
        String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(wfDfid);
        if ((StringUtils.equals(sqlxdm, Constants.SQLX_CLF) || StringUtils.equals(sqlxdm, Constants.SQLX_CLF_ZDYD)
                || StringUtils.equals(sqlxdm, Constants.SQLX_HZDY_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_YFZYDYDJ_DM)
                || StringUtils.equals(sqlxdm, Constants.SQLX_DYHZ_DM) || StringUtils.equals(sqlxdm, Constants.SQLX_ZY_DYBG)
                || StringUtils.equals(sqlxdm, Constants.SQLX_DYBG_DM)) && StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_DYAQ)
                ) {
            ybdcqzh = "";
        } else {
            /**
             *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
             *@description 土地分割合并变更登记，根据项目来源查出来的原不动产证号为空，添加处理保留分割合并前老证作为项目的原不动产权证
             */
            if (StringUtils.isNotBlank(projectPar.getSqlx()) && (StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_TDFGHBBG_DM) || StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_TDFGHBZY_DM))) {
                String yBdccqzhTmp = getYbdcqzh(projectPar, bdcXm);
                if (StringUtils.isNotBlank(yBdccqzhTmp)) {
                    ybdcqzh = yBdccqzhTmp;
                }
            } else {
                ybdcqzh = getYbdcqzh(projectPar, bdcXm);
            }
        }
        return ybdcqzh;
    }

    private String completeBdcXmYbdcqzmh(Project project, BdcXm bdcXm) {
        String ybdcqzmh = "";
        //qijiadong 抵押转移登记ybdcqzmh获取上一手
        if (bdcXm != null && (CommonUtil.indexOfStrs(Constants.SQLX_DY_ZYDJ, bdcXm.getSqlx())
                || CommonUtil.indexOfStrs(Constants.SQLX_DY_BGDJ, bdcXm.getSqlx())
                || StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_YSBZ_ZM_DM))) {
            ybdcqzmh = getYbdcqzmh(project.getXmly(), project.getBdcXmRelList());
        } else if (StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GZ_DM)) {
            if (StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
                BdcXm lastBdcXm = getBdcXmByProid(project.getYxmid());
                if (lastBdcXm != null && StringUtils.isNotBlank(lastBdcXm.getQllx()) && StringUtils.equals(lastBdcXm.getQllx(), Constants.QLLX_DYAQ)) {
                    ybdcqzmh = getYbdcqzmh(project.getXmly(), project.getBdcXmRelList());
                }
            } else if (StringUtils.equals(project.getXmly(), Constants.XMLY_FWSP) || StringUtils.equals(project.getXmly(), Constants.XMLY_TDSP)) {
                GdDy gdDy = gdDyService.getGdDyByDyDyid(project.getYqlid());
                if (gdDy != null) {
                    ybdcqzmh = getYbdcqzmh(project.getXmly(), project.getBdcXmRelList());
                }
            }
        }
        return ybdcqzmh;
    }

    private String getYbdcqzmh(String xmly, List<BdcXmRel> bdcXmRelList) {
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
            if (StringUtils.equals(xmly, Constants.XMLY_BDC)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    List<BdcZs> bdcZsList = bdcZsService.queryBdcZsByProid(bdcXmRel.getYproid());
                    for (BdcZs bdcZs : bdcZsList) {
                        if (StringUtils.isNotBlank(stringBuilder)) {
                            stringBuilder.append(",").append(bdcZs.getBdcqzh());
                        } else {
                            stringBuilder.append(bdcZs.getBdcqzh());
                        }
                    }
                }
            } else if (StringUtils.equals(xmly, Constants.XMLY_FWSP) || StringUtils.equals(xmly, Constants.XMLY_TDSP)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    GdDy gdDy = gdDyService.getGdDyByDyDyid(bdcXmRel.getYqlid());
                    if (gdDy != null && StringUtils.isNotBlank(gdDy.getDydjzmh())) {
                        if (StringUtils.isNotBlank(stringBuilder)) {
                            stringBuilder.append(",").append(gdDy.getDydjzmh());
                        } else {
                            stringBuilder.append(gdDy.getDydjzmh());
                        }
                    }
                }
            }

        }
        return stringBuilder.toString();
    }

    private String getYbdcqzh(ProjectPar projectPar, BdcXm bdcXm) {
        String ybdcqzh = "";
        if (StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_ZJJZWDY_FW_DM) || StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_ZJJZW_ZY_FW_DM) || StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_ZJJZW_BG_FW_DM)) {
            //房屋在建建筑物登记，特殊流程，独立出来
            if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
                //选择厂房时 获取宗地的抵押证号
                String bdcdyfwlx = getBdcdyfwlxByYcbdcdyh(projectPar.getBdcdyh());
                String zdDjh = StringUtils.substring(projectPar.getBdcdyh(), 0, 19);
                String zdBdcdyh = zdDjh + "W00000000";
                String zdBdcdyid = bdcdyService.getBdcdyidByBdcdyh(zdBdcdyh);
                List<BdcZs> bdcZsList = null;
                if (StringUtils.isNotBlank(bdcdyfwlx) && StringUtils.equals(bdcdyfwlx, Constants.DJSJ_FWDZ_DM)) {
                    if (StringUtils.isNotBlank(zdBdcdyid)) {
                        HashMap map = new HashMap();
                        map.put(ParamsConstants.BDCDYID_LOWERCASE, zdBdcdyid);
                        List<BdcDyaq> bdcDyaqList = bdcDyaqService.queryBdcDyaq(map);
                        if (CollectionUtils.isNotEmpty(bdcDyaqList) && StringUtils.isNotBlank(bdcDyaqList.get(0).getProid())) {
                            bdcZsList = bdcZsService.queryBdcZsByProid(bdcDyaqList.get(0).getProid());
                        }
                        //宗地不存在抵押时，获取原宗地不动产单元号
                        if (CollectionUtils.isEmpty(bdcZsList)) {
                            List<BdcJsydzjdsyq> bdcJsydzjdsyqList = bdcJsydzjdsyqService.getBdcJsydzjdsyqList(map);
                            if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                                for (BdcJsydzjdsyq bdcJsydzjdsyq : bdcJsydzjdsyqList) {
                                    if (StringUtils.isNotEmpty(bdcJsydzjdsyq.getProid()) && (bdcJsydzjdsyq.getQszt() == 1)) {
                                        bdcZsList = bdcZsService.queryBdcZsByProid(bdcJsydzjdsyq.getProid());
                                    }
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(bdcZsList)) {
                            ybdcqzh = bdcZsList.get(0).getBdcqzh();
                        }
                    } else {
                        ybdcqzh = gdTdService.getTdzhByBdcdyh(zdBdcdyh);
                    }
                } else {
                    ybdcqzh = bdcZsService.getYtdzhByZdbdcdyh(zdBdcdyh);
                    if (StringUtils.isBlank(ybdcqzh)) {
                        ybdcqzh = bdcZsService.getYbdcqzhByProid(zdBdcdyh);
                    }
                }
            }
        } else {
            //新建
            if (CollectionUtils.isNotEmpty(projectPar.getBdcXmRelList())) {
                //批量流程
                for (BdcXmRel bdcXmRel : projectPar.getBdcXmRelList()) {
                    String bdcqzh = getXjBdcqzh(bdcXmRel.getYproid(), bdcXm);
                    if (StringUtils.isBlank(ybdcqzh)) {
                        ybdcqzh = bdcqzh;
                    } else if (!StringUtils.equals(bdcqzh, ybdcqzh)) {
                        ybdcqzh += "," + bdcqzh;
                    }
                }
            } else if (StringUtils.isNotBlank(projectPar.getYxmid())) {
                //获取上一手项目的ybdcqzh，未得到则通过yxmid再找下
                ybdcqzh = getXjBdcqzh(projectPar.getYxmid(), bdcXm);
            }
        }
        //zdd 数据库查询出来是用"/"隔开的   文档要求用"，"隔开
        if (StringUtils.isNotBlank(ybdcqzh) && ybdcqzh.contains("/")) {
            ybdcqzh = ybdcqzh.replaceAll("/", ",");
        }
        return ybdcqzh;
    }

    /**
     * @param bdcqzh 不动产权证号
     * @param qlr    权利人名称
     * @return proid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产权证号、权利人名称获取proid
     */
    @Override
    public List<String> getProid(String bdcqzh, String qlr) {
        List<String> proidList = Lists.newArrayList();
        if (StringUtils.isNotBlank(bdcqzh)) {
            List<String> proidZsList = bdcXmMapper.getProidByBdcqzh(bdcqzh);
            if (CollectionUtils.isNotEmpty(proidZsList)) {
                proidList.addAll(proidZsList);
            }
        }
        if (StringUtils.isNotBlank(qlr)) {
            Example example = new Example(BdcQlr.class);
            example.createCriteria().andLike("qlrmc", qlr).andEqualTo("qlrlx", Constants.QLRLX_QLR);
            List<BdcQlr> bdcQlrList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (BdcQlr bdcQlr : bdcQlrList) {
                    if (StringUtils.isNotBlank(bdcQlr.getProid())) {
                        proidList.add(bdcQlr.getProid());
                    }
                }
            }
        }
        return proidList;
    }

    /**
     * @param proid 项目ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 改变是否收档
     */
    @Override
    public String changeSfsdByProid(String proid) {
        String msg = "";
        if (StringUtils.isNotBlank(proid)) {
            BdcXm bdcXm = getBdcXmByProid(proid);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())) {
                List<BdcXm> bdcXmList = getBdcXmListByWiid(bdcXm.getWiid());
                for (BdcXm bdcXmTemp : bdcXmList) {
                    if (StringUtils.equals(bdcXmTemp.getSfsd(), "1")) {
                        bdcXmTemp.setSfsd("0");
                    } else {
                        bdcXmTemp.setSfsd("1");
                    }
                    saveBdcXm(bdcXmTemp);
                }
                msg = "success";
            } else if (bdcXm != null) {
                if (StringUtils.equals(bdcXm.getSfsd(), "1")) {
                    bdcXm.setSfsd("0");
                } else {
                    bdcXm.setSfsd("1");
                }
                saveBdcXm(bdcXm);
                msg = "success";
            } else {
                msg = "未找到项目!";
            }
        }
        return msg;
    }

    /**
     * @param wiid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目信息
     */
    @Override
    public List<BdcXm> getYzdfBdcXm(String wiid) {
        List<BdcXm> bdcXmList = Lists.newArrayList();
        if (StringUtils.isNotBlank(wiid)) {
            bdcXmList = bdcXmMapper.getYzdfBdcXmXx(wiid);
        }
        if (CollectionUtils.isEmpty(bdcXmList)) {
            bdcXmList = Collections.EMPTY_LIST;
        }
        return bdcXmList;
    }

    /**
     * @param proidList
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目
     */
    @Override
    public List<BdcXm> getYzdfBdcXm(List<String> proidList) {
        List<BdcXm> bdcXmList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(proidList)) {
            Map map = Maps.newHashMap();
            map.put("proidList", proidList);
            bdcXmList = bdcXmMapper.getYzdfBdcXm(map);
        }
        if (CollectionUtils.isEmpty(bdcXmList)) {
            bdcXmList = Collections.EMPTY_LIST;
        }
        return bdcXmList;
    }

    /**
     * @param proidList
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 获取一证多房项目
     */
    @Override
    public List<String> getYzdfProids(List<String> proidList) {
        List<String> yzdfProidList = Lists.newArrayList();
        List<BdcXm> bdcXmList = getYzdfBdcXm(proidList);
        if (CollectionUtils.isNotEmpty(bdcXmList)) {
            for (BdcXm bdcXm : bdcXmList) {
                if (!proidList.contains(bdcXm.getProid())) {
                    yzdfProidList.add(bdcXm.getProid());
                }
            }
        }
        return yzdfProidList;
    }

    /**
     * @param bdcdyid
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元ID获取项目ID
     */
    @Override
    public String getProidByBdcdyid(String bdcdyid) {
        String proid = "";
        if (StringUtils.isNotBlank(bdcdyid)) {
            List<String> proidList = bdcXmMapper.getProidByBdcdyid(bdcdyid);
            if (CollectionUtils.isNotEmpty(proidList)) {
                proid = proidList.get(0);
            }
        }
        return proid;
    }

    @Override
    public List<String> getXsCqProidBybdcdyid(String bdcdyid) {
        if (StringUtils.isNotBlank(bdcdyid)) {
            return bdcXmMapper.getXsCqProidBybdcdyid(bdcdyid);
        } else {
            return null;
        }
    }

    @Override
    public String getSqlxMcByWiid(String wiid) {
        String sqlxmc = "";
        if (StringUtils.isNotBlank(wiid)) {
            String wfDfid = PlatformUtil.getWfDfidByWiid(wiid);
            if (StringUtils.isNotBlank(wfDfid)) {
                String sqlxdm = bdcZdGlService.getBdcSqlxdmByWdid(wfDfid);
                if (StringUtils.isNotBlank(sqlxdm)) {
                    sqlxmc = bdcZdGlService.getSqlxMcByDm(sqlxdm);
                }
            }
        }
        return sqlxmc;
    }

    @Override
    public String getLclx(String wiid) {
        String lclx = "";
        if (StringUtils.isNotBlank(wiid)) {
            List<String> sqlxList = bdcXmMapper.getSqlx(wiid);
            if (CollectionUtils.isNotEmpty(sqlxList)) {
                if (sqlxList.size() > 1) {
                    lclx = Constants.LCLX_ZH;
                } else {
                    Integer count = bdcXmMapper.getXmCount(wiid);
                    if (count == 1) {
                        lclx = Constants.LCLX_DY;
                    } else if (count > 1) {
                        lclx = Constants.LCLX_PL;
                    }
                }
            }
        }
        return lclx;
    }

    /**
     * @param bdcdyh 不动产单元号
     * @return 现势不动产信息
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 根据不动产单元号获取现势不动产信息
     */
    @Override
    public List<Map> getXsxxByBdcdyh(String bdcdyh) {
        List<Map> mapList = Lists.newArrayList();
        if (StringUtils.isNotBlank(bdcdyh)) {
            mapList = bdcXmMapper.getXsxxByBdcdyh(bdcdyh);
        }
        if (CollectionUtils.isEmpty(mapList)) {
            mapList = Collections.EMPTY_LIST;
        }
        return mapList;
    }

    @Override
    public String getSmztByWiid(String wiid) {
        String smzt = "0";
        if (StringUtils.isNotBlank(wiid)) {
            smzt = bdcXmMapper.getSmztByWiid(wiid);
        }
        return smzt;
    }
}