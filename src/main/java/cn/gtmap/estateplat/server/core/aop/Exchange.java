package cn.gtmap.estateplat.server.core.aop;


import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.exchange.national.*;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcZdGlService;
import cn.gtmap.estateplat.server.core.service.GdQlrService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.HttpClientUtil;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.service.exchange.share.RealEstateShareService;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.config.AppConfig;
import com.gtis.util.ThreadPool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0, 2016/9/13
 * @author<a href="mailto:jhj@gtmap.cn>jhj</a>
 * @discription
 */

public class Exchange implements Serializable {
    @Autowired
    private RealEstateShareService realEstateShareService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcZdGlService bdcZdGlService;

    protected final Logger logger = LoggerFactory.getLogger(Exchange.class);

    /**
     * @author
     * @description 创建项目时即推送数据
     */
    public void insertYwxxToShare(final JoinPoint jp){
        ThreadPool.execute(new Runnable() {
            public void run() {
                BdcXm bdcXm = (BdcXm)jp.getArgs()[0];
                if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())&&!CommonUtil.indexOfStrs(Constants.SQLX_CFDJ_DM,bdcXm.getSqlx())){
                    realEstateShareService.shareRunningProject2Db(bdcXm.getProid());
                }
            }
        });
    }


    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @description 将登簿的项目的基本信息、权利信息、不动产单元信息推送到共享库
     */
    public void insertDbYwxxToShare(final JoinPoint jp){
        ThreadPool.execute(new Runnable() {
            public void run() {
                BdcXm bdcXm = (BdcXm)jp.getArgs()[0];
                if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getWiid())){
                    realEstateShareService.shareRegisteredProject2DbByWiid(bdcXm.getWiid());
                }
            }
        });
    }

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @description 办结时完善所有表信息
     */
    public void insertAllYwxxToShare(final JoinPoint jp){
        ThreadPool.execute(new Runnable() {
            public void run() {
                if(jp.getArgs()[1] instanceof BdcXm){
                    BdcXm bdcXm = (BdcXm)jp.getArgs()[1];
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())){
                        realEstateShareService.share2Database(bdcXm.getProid());
                    }
                }else if(jp.getArgs()[1] instanceof ArrayList){
                    List<BdcXm> bdcXmList = (List<BdcXm>) jp.getArgs()[1];
                    if(CollectionUtils.isNotEmpty(bdcXmList)){
                        for (BdcXm bdcXm:bdcXmList){
                            if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())){
                                realEstateShareService.share2Database(bdcXm.getProid());
                                break;
                            }
                        }
                    }
                }

            }
        });
    }

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @description 项目删除时，更新业务信息表中的xmzt为4即用户撤回
     */
    public void updateYwxxToShare(final JoinPoint jp){
        ThreadPool.execute(new Runnable() {
            public void run() {
                String status = "4";
                if(jp.getArgs()[1] instanceof BdcXm){
                    BdcXm bdcXm = (BdcXm)jp.getArgs()[1];
                    /**
                     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                     * @description 删除工作流事件已经遍历处理
                     */
                    if(bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())){
                        realEstateShareService.shareProjectStatus2Db(bdcXm.getProid(),status);
                    }
                } else if(jp.getArgs()[1] instanceof ArrayList){
                    List<BdcXm> bdcXmList = (List<BdcXm>) jp.getArgs()[1];
                    if(CollectionUtils.isNotEmpty(bdcXmList)){
                        for(BdcXm bdcXm:bdcXmList) {
                            realEstateShareService.shareProjectStatus2Db(bdcXm.getProid(),status);
                        }
                    }
                }
            }
        });
    }

    /**
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @param
     * @rerutn
     * @description 一键抵押注销时，向业务信息表、房屋户表、抵押权表、注销表中插入信息（一证多房，循环插入，业务信息表中一条，其余多条）
     *              统一：注销不往权利表插数据，解封往查封表插数据
     */
    public void insertGdFwZx(JoinPoint jp){
        Date date = CalendarUtil.getCurDate();
        DataModel dataModel = new DataModel();
        String qlid = jp.getArgs()[0].toString();
        GdDy gddy = entityMapper.selectByPrimaryKey(GdDy.class, qlid);
        GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class,qlid);
        GdYg gdYg = entityMapper.selectByPrimaryKey(GdYg.class,qlid);
        DjfDjYwxx djfDjYwxx = new DjfDjYwxx();
        List<KttFwH> kttFwHList = new ArrayList<KttFwH>();
        List<QlfQlDyaq> qlfQlDyaqList = new ArrayList<QlfQlDyaq>();
        List<QlfQlCfdj> qlCfdjList = new ArrayList<QlfQlCfdj>();
        List<QlfQlYgdj> qlfQlYgdjList = new ArrayList<QlfQlYgdj>();
        List<QlfQlZxdj> qlfQlZxdjList = new ArrayList<QlfQlZxdj>();
        if(gddy != null){
            String sqlxdm = Constants.SQLX_DYZX;
            StringBuilder qlr = new StringBuilder();
            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(qlid,"qlr");
            if(CollectionUtils.isNotEmpty(gdQlrList)){
                for(GdQlr gdQlr : gdQlrList){
                    if(StringUtils.isBlank(qlr)) {
                        qlr.append(gdQlr.getQlr());
                    }else {
                        qlr.append(",").append(gdQlr.getQlr());
                    }
                }
            }

            Example example = new Example(GdBdcQlRel.class);
            example.createCriteria().andEqualTo("qlid",qlid);
            List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(example);
            GdFw gdFw = null;
            String fwid = "";

            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                djfDjYwxx.setYwh(qlid);
                djfDjYwxx.setYywh(gddy.getProid());
                djfDjYwxx.setKssj(date);
                djfDjYwxx.setJssj(date);
                djfDjYwxx.setXzqdm(PlatformUtil.getCurrentUserDwdm());
                djfDjYwxx.setDjlx(Constants.DJLX_DYDJ_DM);
                djfDjYwxx.setQllx(Constants.QLLX_DYAQ);
                djfDjYwxx.setSqlx(sqlxdm);
                djfDjYwxx.setYbdcqzh(gddy.getDydjzmh());
                djfDjYwxx.setXmzt("1");
                djfDjYwxx.setUpdatetime(date);
                djfDjYwxx.setCreatetime(date);

                KttFwH kttFwH;
                QlfQlZxdj qlfQlZxdj;
                for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList){
                    kttFwH = new KttFwH();
                    qlfQlZxdj = new QlfQlZxdj();
                    fwid = gdBdcQlRel.getBdcid();
                    gdFw = entityMapper.selectByPrimaryKey(GdFw.class,fwid);
                    if(gdFw != null){
                        kttFwH.setBdcdyh(fwid);
                        kttFwH.setFwbm(gdFw.getFwid());
                        kttFwH.setZl(gdFw.getFwzl());
                        kttFwH.setCreatetime(date);
                        kttFwH.setUpdatetime(date);
                        kttFwHList.add(kttFwH);
                    }

                    qlfQlZxdj.setBdcdyh(fwid);
                    qlfQlZxdj.setBdcqzh(gddy.getDydjzmh());
                    qlfQlZxdj.setYwh(qlid);
                    qlfQlZxdj.setZxywh(gddy.getProid());
                    qlfQlZxdj.setZxsj(date);
                    qlfQlZxdj.setQxdm(PlatformUtil.getCurrentUserDwdm());
                    qlfQlZxdj.setCreatetime(date);
                    qlfQlZxdj.setUpdatetime(date);
                    qlfQlZxdjList.add(qlfQlZxdj);
                }
                dataModel.setKttFwHList(kttFwHList);
                dataModel.setQlfQlDyaqList(qlfQlDyaqList);
                dataModel.setQlfQlZxdjList(qlfQlZxdjList);
            }
        }else if(gdCf!=null){
            String sqlxdm = Constants.SQLX_JF;
            StringBuilder qlr = new StringBuilder();
            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(qlid,"qlr");
            if(CollectionUtils.isNotEmpty(gdQlrList)){
                for(GdQlr gdQlr : gdQlrList){
                    if(StringUtils.isBlank(qlr)) {
                        qlr.append(gdQlr.getQlr());
                    }else {
                        qlr.append(",").append(gdQlr.getQlr());
                    }
                }
            }

            Example example = new Example(GdBdcQlRel.class);
            example.createCriteria().andEqualTo("qlid",qlid);
            List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(example);
            GdFw gdFw = null;
            String fwid = "";

            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                djfDjYwxx.setYwh(qlid);
                djfDjYwxx.setYywh(gdCf.getProid());
                djfDjYwxx.setKssj(date);
                djfDjYwxx.setJssj(date);
                djfDjYwxx.setXzqdm(PlatformUtil.getCurrentUserDwdm());
                djfDjYwxx.setDjlx(Constants.DJLX_CFDJ_DM);
                djfDjYwxx.setQllx(Constants.QLLX_CFDJ);
                djfDjYwxx.setSqlx(sqlxdm);
                djfDjYwxx.setYbdcqzh(gdCf.getCfwh());
                djfDjYwxx.setXmzt("1");
                djfDjYwxx.setCreatetime(date);
                djfDjYwxx.setUpdatetime(date);

                for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList){
                    KttFwH kttFwH = new KttFwH();
                    QlfQlCfdj qlfQlCfdj = new QlfQlCfdj();
                    fwid = gdBdcQlRel.getBdcid();
                    gdFw = entityMapper.selectByPrimaryKey(GdFw.class,fwid);
                    if(gdFw != null){
                        kttFwH.setBdcdyh(fwid);
                        kttFwH.setFwbm(gdFw.getFwid());
                        kttFwH.setZl(gdFw.getFwzl());
                        kttFwH.setCreatetime(date);
                        kttFwH.setUpdatetime(date);
                        kttFwHList.add(kttFwH);
                    }
                    qlfQlCfdj.setBdcdyh(fwid);
                    qlfQlCfdj.setYwh(qlid);
                    qlfQlCfdj.setCfjg(gdCf.getCfjg());
                    qlfQlCfdj.setCfqssj(gdCf.getCfksrq());
                    qlfQlCfdj.setCfjssj(gdCf.getCfjsrq());
                    if(StringUtils.isNotBlank(gdCf.getCflx())){
                        String dm = bdcZdGlService.getCflxDmByMc(gdCf.getCflx());
                        if(StringUtils.isNotBlank(dm))
                            qlfQlCfdj.setCflx(dm);
                        else
                            qlfQlCfdj.setCflx("1");
                    }
                    qlfQlCfdj.setCfwh(gdCf.getCfwh());
                    qlfQlCfdj.setCfwj(gdCf.getCfwj());
                    qlfQlCfdj.setJfdbr(PlatformUtil.getCurrentUserLoginName());
                    qlfQlCfdj.setJfdjsj(date);
                    qlfQlCfdj.setJfywh(qlid);
                    qlfQlCfdj.setCreatetime(date);
                    qlfQlCfdj.setUpdatetime(date);
                    qlfQlCfdj.setQszt("2");
                    qlCfdjList.add(qlfQlCfdj);

                }
                dataModel.setKttFwHList(kttFwHList);
                dataModel.setQlfQlCfdjList(qlCfdjList);
                dataModel.setQlfQlZxdjList(qlfQlZxdjList);
            }
        }else if(gdYg!=null){
            String sqlxdm = Constants.SQLX_YG_ZX;
            StringBuilder qlr = new StringBuilder();
            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrs(qlid,"qlr");
            if(CollectionUtils.isNotEmpty(gdQlrList)){
                for(GdQlr gdQlr : gdQlrList){
                    if(StringUtils.isBlank(qlr)) {
                        qlr.append(gdQlr.getQlr());
                    }else {
                        qlr.append(",").append(gdQlr.getQlr());
                    }
                }
            }

            Example example = new Example(GdBdcQlRel.class);
            example.createCriteria().andEqualTo("qlid",qlid);
            List<GdBdcQlRel> gdBdcQlRelList = entityMapper.selectByExample(example);
            GdFw gdFw = null;
            String fwid = "";

            if(CollectionUtils.isNotEmpty(gdBdcQlRelList)){
                djfDjYwxx.setYwh(qlid);
                djfDjYwxx.setYywh(gdYg.getProid());
                djfDjYwxx.setKssj(date);
                djfDjYwxx.setJssj(date);
                djfDjYwxx.setXzqdm(PlatformUtil.getCurrentUserDwdm());
                djfDjYwxx.setDjlx(Constants.DJLX_YGDJ_DM);
                djfDjYwxx.setQllx(Constants.QLLX_YGDJ);
                djfDjYwxx.setSqlx(sqlxdm);
                djfDjYwxx.setYbdcqzh(gdYg.getYgdjzmh());
                djfDjYwxx.setXmzt("1");
                djfDjYwxx.setCreatetime(date);
                djfDjYwxx.setUpdatetime(date);

                for(GdBdcQlRel gdBdcQlRel : gdBdcQlRelList){
                    KttFwH kttFwH = new KttFwH();
                    QlfQlZxdj qlfQlZxdj = new QlfQlZxdj();
                    fwid = gdBdcQlRel.getBdcid();
                    gdFw = entityMapper.selectByPrimaryKey(GdFw.class,fwid);
                    if(gdFw != null){
                        kttFwH.setBdcdyh(fwid);
                        kttFwH.setFwbm(gdFw.getFwid());
                        kttFwH.setZl(gdFw.getFwzl());
                        kttFwH.setCreatetime(date);
                        kttFwH.setUpdatetime(date);
                        kttFwHList.add(kttFwH);
                    }

                    qlfQlZxdj.setBdcdyh(fwid);
                    qlfQlZxdj.setBdcqzh(gdYg.getYgdjzmh());
                    qlfQlZxdj.setYwh(qlid);
                    qlfQlZxdj.setZxywh(gdYg.getProid());
                    qlfQlZxdj.setZxsj(date);
                    qlfQlZxdj.setQxdm(PlatformUtil.getCurrentUserDwdm());
                    qlfQlZxdj.setCreatetime(date);
                    qlfQlZxdj.setUpdatetime(date);
                    qlfQlZxdjList.add(qlfQlZxdj);
                }
                dataModel.setKttFwHList(kttFwHList);
                dataModel.setQlfQlYgdjList(qlfQlYgdjList);
                dataModel.setQlfQlZxdjList(qlfQlZxdjList);
            }
        }

        if(dataModel != null){
            realEstateShareService.shareNationalDataModel2Db(dataModel,djfDjYwxx,"123");
        }
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 登簿上报
     */
    public void registerReport(final JoinPoint jp) {
        ThreadPool.execute(new Runnable() {
            public void run() {
                String proid = (String) jp.getArgs()[0];
                String userid = (String) jp.getArgs()[2];
                String exchangeUrl = AppConfig.getProperty("exchange.url");
                if ((StringUtils.isNotBlank(exchangeUrl)) && (StringUtils.isNotBlank(proid)) && (StringUtils.isNotBlank(userid))) {
                    String url = exchangeUrl + "/nationalAccess/autoAccess?proid=" + proid + "&userid" + userid;
                    try {
                        HttpClientUtil.doGet(url, null);
                    } catch (Exception e) {
                        logger.error("Exchange.registerReport",e);
                    }
                }
            }
        });
    }
}
