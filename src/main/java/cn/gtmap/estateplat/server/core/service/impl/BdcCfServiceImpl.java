package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.log.AuditLog;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcCfMapper;
import cn.gtmap.estateplat.server.core.mapper.GdCfMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import cn.gtmap.estateplat.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.web.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查封解封
 *
 * @author <a href="mailto:zhaodongdong@gtmap.cn">zdd</a>
 * @version V1.0, 15-5-12
 */
@Service
public class BdcCfServiceImpl implements BdcCfService {
    private static final Log log = LogFactory.getLog(BdcCfServiceImpl.class);
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 轮候查封转查封的附记追加内容
     */
    private static final String LHCF_CF_FJ_ZJNR = "轮候查封转查封";
    /**
     * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
     * @description 预查封转查封的附记追加内容
     */
    private static final String YCF_CF_FJ_ZJNR = "预查封转查封";

    private static final String XF_CF_FJ_ZJNR = "续封转查封";

    private static final String PARAMETER_CFYSX = "\n该查封已失效";

    private static final String PARAMETER_CFSX_SDSX = "手动失效";

    @Autowired
    BdcCfMapper bdcCfMapper;
    @Autowired
    BdcdyService bdcdyService;
    @Autowired
    EntityMapper entityMapper;
    @Autowired
    BdcXmService bdcXmService;
    @Autowired
    BdcQlrService bdcQlrService;
    @Autowired
    BdcXmRelService bdcXmRelService;
    @Autowired
    QllxService qllxService;
    @Autowired
    BdcSpxxService bdcSpxxService;
    @Autowired
    GdFwService gdFwService;
    @Autowired
    GdTdService gdTdService;
    @Autowired
    GdCfService gdCfService;
    @Autowired
    BdcGdDyhRelService bdcGdDyhRelService;
    @Autowired
    GdBdcQlRelService gdBdcQlRelService;
    @Autowired
    GdXmService gdXmService;
    @Autowired
    GdQlrService gdQlrService;
    @Autowired
    BdcZdGlService bdcZdGlService;
    @Autowired
    BdcSlbhCustomServiceContext bdcSlbhCustomServiceContext;
    @Autowired
    GdCfMapper gdCfMapper;
    @Autowired
    BdcFwFsssService bdcFwFsssService;
    @Autowired
    BdcZsCdService bdcZsCdService;

    @Override
    @Transactional
    public void updateBdcCfForJfxx(final BdcCf bdcCf) {
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid()))
            bdcCfMapper.updateBdcCfForJfxx(bdcCf);
    }


    @Override
    public BdcCf selectCfByProid(final String proid) {
        if (StringUtils.isNotBlank(proid)) {
            return bdcCfMapper.selectCfByProid(proid);
        }
        return null;
    }


    @Override
    public List<BdcCf> queryCfByBdcdyid(final String bdcdyid) {
        return bdcCfMapper.getCfByBdcdyid(bdcdyid);
    }

    @Override
    public void dealNotInheritFsssForCf(String proid, String yproid) {
        if (StringUtils.isNotBlank(proid) && StringUtils.isNotBlank(yproid)) {
            BdcXm bdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
            BdcBdcdy bdcBdcdy = bdcdyService.queryBdcBdcdyByProid(yproid);
            HashMap hashMap = new HashMap();
            if (bdcXm != null && ybdcXm != null && bdcBdcdy != null) {
                hashMap.put("proid", ybdcXm.getProid());
                hashMap.put("zfbdcdyh", bdcBdcdy.getBdcdyh());
                double fsssZmj = 0.00;
                List<BdcFwfsss> bdcFwfsssList = bdcFwFsssService.getBdcFwfsssList(hashMap);
                if (CollectionUtils.isNotEmpty(bdcFwfsssList)) {
                    for (BdcFwfsss bdcFwfsss : bdcFwfsssList) {
                        if (bdcFwfsss.getJzmj() != null && StringUtils.isNotBlank(bdcFwfsss.getProid())) {
                            fsssZmj += bdcFwfsss.getJzmj();
                        }
                    }
                }
            }
        }
    }


    @Transactional
    public void ycfChangeCf(BdcXm xbdcXm, BdcCf ybdcCf) {
        if (xbdcXm != null && StringUtils.isNotBlank(xbdcXm.getProid())) {
            //获得原审批信息
//            BdcSpxx ybdcSpxx = null;

//            if (ybdcCf != null && StringUtils.isNotBlank(ybdcCf.getProid()))
//                ybdcSpxx = bdcSpxxService.queryBdcSpxxByProid(ybdcCf.getProid());
            //获取权利人
//            HashMap<String, String> hashMap = new HashMap<String, String>();
//            hashMap.put("proid", xbdcXm.getProid());
//            List<BdcQlr> ybdcQlrList = bdcQlrService.queryBdcQlrList(hashMap);

            //预查封转查封
            if (ybdcCf != null && StringUtils.isNotBlank(ybdcCf.getQlid())) {
//                ybdcCf.setQszt(Constants.QLLX_QSZT_HR);
                StringBuilder fj = new StringBuilder();
                if (StringUtils.isNotBlank(ybdcCf.getFj())) {
                    fj.append(ybdcCf.getFj());
                }
                fj.append(CalendarUtil.formatDateToString(new Date())).append(YCF_CF_FJ_ZJNR);
                ybdcCf.setCflx(Constants.CFLX_ZD_CF);
                ybdcCf.setFj(fj.toString());
                entityMapper.saveOrUpdate(ybdcCf, ybdcCf.getQlid());
            }
            //创建项目关系表,防止下面的对象赋值冲突掉proid
//            BdcXmRel bdcXmRel = new BdcXmRel();
//            bdcXmRel.setYproid(xbdcXm.getProid());

            //创建项目
//            BdcXm bdcXm = xbdcXm;
//            bdcXm.setProid(UUIDGenerator.generate18());
//            bdcXm.setWiid(bdcXm.getProid());
//            bdcXm.setCjsj(new Date());
//            bdcXm.setSqlx(Constants.SQLX_CF);
//            bdcXm.setQllx(Constants.QLLX_CFDJ);
//            bdcXm.setDjlx(Constants.DJLX_CFDJ_DM);
//            bdcXm.setXmzt(Constants.XMZT_BJ);
//            bdcXm.setBh(bdcSlbhCustomServiceContext.getSlbhService().generateBdcXmSlbh(bdcXm));
//            entityMapper.saveOrUpdate(bdcXm, bdcXm.getProid());

            //创建项目关联表
//            bdcXmRel.setRelid(UUIDGenerator.generate18());
//            bdcXmRel.setProid(bdcXm.getProid());
//            entityMapper.saveOrUpdate(bdcXmRel, bdcXmRel.getRelid());

            //创建审批信息
//            if (ybdcSpxx != null) {
//                BdcSpxx bdcSpxx = new BdcSpxx();
//                BeanUtils.copyProperties(bdcSpxx, ybdcSpxx);
//                bdcSpxx.setProid(xbdcXm.getProid());
//                bdcSpxx.setSpxxid(UUIDGenerator.generate());
//                entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
//            }

            //创建权利人
//            List<BdcQlr> bdcQlrList = null;
//            bdcQlrList = ybdcQlrList;
//            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
//                for (BdcQlr bdcQlr : bdcQlrList) {
//                    bdcQlr.setQlrid(UUIDGenerator.generate18());
//                    bdcQlr.setProid(bdcXm.getProid());
//                    entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
//                }
//            }

            //插入查封数据
//            if (ybdcCf != null) {
//                BdcCf bdcCf = ybdcCf;
//                bdcCf.setFj(fj);
//                bdcCf.setQlid(UUIDGenerator.generate18());
//                bdcCf.setProid(bdcXm.getProid());
//                bdcCf.setDjsj(new Date());
//
//                bdcCf.setCflx(Constants.CFLX_ZD_CF);
//                bdcCf.setQszt(Constants.QLLX_QSZT_XS);
//                entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
//            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BdcCf> queryYcfByBdcdyh(final String bdcdyh) {
        return bdcCfMapper.queryYcfByBdcdyh(bdcdyh);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getBdcCflxMc() {
        return bdcCfMapper.getBdcCflxMc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map> queryBdcCfByPage(final Map map) {
        return bdcCfMapper.queryBdcCfByPage(map);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map> queryBdcGdCfByPage(final Map map) {
        return bdcCfMapper.queryBdcGdCf(map);
    }


    public String createNewJfywh(String proid, String userid) {
        String jfywh = null;
        if (StringUtils.isNotBlank(proid)) {
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(proid);
            if (ybdcXm != null) {
                //生成新的项目
                BdcXm newXm = ybdcXm;
                newXm.setProid(UUIDGenerator.generate());
                newXm.setWiid(UUIDGenerator.generate());
                newXm.setCjsj(new Date());
                newXm.setDwdm(PlatformUtil.getCurrentUserDwdmByUserid(userid));
                jfywh = bdcSlbhCustomServiceContext.getSlbhService().getBdcXmSlbh(newXm);
            }
        }
        return jfywh;
    }


    @Override
    public boolean turnXfToCf(final BdcCf bdcCf, final String bdcdyid) {
        boolean result = false;
        String xfTurnToCfEnable = StringUtils.deleteWhitespace(AppConfig.getProperty("xfTurnToCf.enable"));
        if (StringUtils.equals(xfTurnToCfEnable, ParamsConstants.TRUE_LOWERCASE)) {
            List<GdCf> gdCfList = null;
            List<GdCf> gdCfTempList = null;
            List<BdcCf> bdcCfListTemp = null;
            List<BdcCf> bdcXfList = null;
            if (StringUtils.isNotBlank(bdcdyid)) {
                /**
                 * 不动产库 现势的查封记录
                 */
                bdcCfListTemp = getBdcCfList(bdcdyid, Constants.CFLX_ZD_CF, Constants.QLLX_QSZT_XS);
                /**
                 * 不动产库 现势的续封
                 */
                bdcXfList = getBdcCfList(bdcdyid, Constants.CFLX_XF, Constants.QLLX_QSZT_XS);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                Map<String, List<GdCf>> gdCfListMap = getGdCfList(bdcBdcdy);
                if (gdCfListMap != null) {
                    gdCfList = gdCfListMap.get("gdCfList");
                    gdCfTempList = gdCfListMap.get("gdCfTempList");
                }
                //zdd 只有当系统中不存在查封记录时才会 将续封的记录更新为查封
                if (CollectionUtils.isEmpty(bdcCfListTemp) && CollectionUtils.isEmpty(gdCfTempList)) {
                    if (CollectionUtils.isNotEmpty(gdCfList)) {
                        GdCf gdCfTemp = gdCfList.get(0);
                        cancelGdXf(gdCfTemp);
                        createNewGdCfInfoByXf(gdCfTemp);
                        result = true;
                    } else if (CollectionUtils.isNotEmpty(bdcXfList)) {
                        BdcCf bdcCfTemp = bdcXfList.get(0);
                        cancelBdcXf(bdcCfTemp);
                        createNewBdcCfInfoByXf(bdcCfTemp, bdcCf);
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    public List<BdcCf> getBdcCfList(String bdcdyid, String cflx, Integer qszt) {
        List<BdcCf> bdcCfListTemp = null;
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            map.put("cflx", cflx);
            map.put("qszt", qszt);
            bdcCfListTemp = andEqualQueryCf(map, null);
        }
        return bdcCfListTemp;
    }

    public Map<String, List<GdCf>> getGdCfList(BdcBdcdy bdcBdcdy) {
        List<GdCf> gdCfList = null;
        List<GdCf> gdCfTempList = null;
        Map<String, List<GdCf>> gdCfListMap = null;
        List<GdBdcQlRel> gdBdcQlRelList = getGdBdcQlRelList(bdcBdcdy);
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
            gdCfList = new ArrayList<GdCf>();
            gdCfTempList = new ArrayList<GdCf>();
            gdCfListMap = new HashMap<String, List<GdCf>>();
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                if (gdBdcQlRel != null && StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                    GdCf gdCf = gdFwService.getGdCfByCfid(gdBdcQlRel.getQlid(), 0);
                    /**
                     * 过渡库中的字典项存值都是汉字
                     */
                    if (gdCf != null && StringUtils.isNotBlank(gdCf.getCflx()) && StringUtils.equals(bdcZdGlService.getCflxDmByMc(gdCf.getCflx()), Constants.CFLX_ZD_CF)) {
                        gdCfTempList.add(gdCf);
                    }
                    if (gdCf != null && StringUtils.isNotBlank(gdCf.getCflx()) && StringUtils.equals(bdcZdGlService.getCflxDmByMc(gdCf.getCflx()), Constants.CFLX_XF)) {
                        gdCfList.add(gdCf);
                    }
                }
            }
            gdCfListMap.put("gdCfTempList", gdCfTempList);
            gdCfListMap.put("gdCfList", gdCfList);
        }
        return gdCfListMap;
    }

    public List<GdBdcQlRel> getGdBdcQlRelList(BdcBdcdy bdcBdcdy) {
        List<GdBdcQlRel> gdBdcQlRelList = null;
        if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
            gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
            List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(bdcBdcdy.getBdcdyh());
            if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                    if (bdcGdDyhRel != null && StringUtils.isNotBlank(bdcGdDyhRel.getGdid())) {
                        List<GdBdcQlRel> gdBdcQlRelTempList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcGdDyhRel.getGdid());
                        if (CollectionUtils.isNotEmpty(gdBdcQlRelTempList))
                            gdBdcQlRelList.addAll(gdBdcQlRelTempList);
                    }
                }
            }
        }
        return gdBdcQlRelList;
    }

    public void cancelBdcXf(BdcCf bdcCfTemp) {
        bdcCfTemp.setQszt(Constants.QLLX_QSZT_HR);
        entityMapper.saveOrUpdate(bdcCfTemp, bdcCfTemp.getQlid());
    }

    public void cancelGdXf(GdCf gdCfTemp) {
        gdCfTemp.setIsjf(1);
        entityMapper.saveOrUpdate(gdCfTemp, gdCfTemp.getCfid());
        gdFwService.changeGdqlztByQlid(gdCfTemp.getCfid(), Constants.QLLX_QSZT_XS.toString());
    }

    public void createNewGdCfInfoByXf(GdCf gdCfTemp) {
        GdXm ygdxm = gdXmService.getGdXm(gdCfTemp.getProid());
        String proid = UUIDGenerator.generate18();
        String ywh = UUIDGenerator.generate18();
        if (ygdxm != null) {
            GdXm gdXm = ygdxm;
            gdXm.setProid(proid);
            gdXm.setSlbh(ywh);
            gdXm.setCsrq(new Date());
            entityMapper.saveOrUpdate(gdXm, gdXm.getProid());
            List<GdQlr> gdQlrList = gdQlrService.queryGdQlrListByProid(gdCfTemp.getProid(), Constants.QLRLX_QLR);
            if (CollectionUtils.isNotEmpty(gdQlrList)) {
                for (GdQlr gdQlr : gdQlrList) {
                    gdQlr.setProid(proid);
                    gdQlr.setQlrid(UUIDGenerator.generate18());
                    entityMapper.saveOrUpdate(gdQlr, gdQlr.getQlrid());
                }
            }
        }
        /**
         * @description 将续封转为查封，生成新的查封权利
         */
        GdCf gdcf = gdCfTemp;
        Date cfksqx = gdCfTemp.getCfksrq();
        Date cfjsqx = gdCfTemp.getCfjsrq();
        if (cfksqx != null && cfjsqx != null) {
            Long days = CommonUtil.getDaySub(cfksqx, cfjsqx);
            int daysI = days.intValue() - 1;
            gdcf.setCfksrq(new Date());
            String jsqx = CalendarUtil.addDay(CalendarUtil.formatDateTime(gdcf.getCfksrq()), daysI);
            gdcf.setCfjsrq(CalendarUtil.formatDate(jsqx));
        }
        gdcf.setProid(proid);
        gdcf.setCfid(UUIDGenerator.generate18());
        gdcf.setCflx(Constants.CFLX_GD_CF);
        gdcf.setIsjf(0);
        gdcf.setCfsx(null);
        gdcf.setFj(CalendarUtil.getCurChinaYMDStrDate() + XF_CF_FJ_ZJNR);
        entityMapper.insertSelective(gdcf);
    }

    public void createNewBdcCfInfoByXf(BdcCf bdcCfTemp, BdcCf oldBdcCf) {
        //生成查封数据
        BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcCfTemp.getProid());
        String proid = UUIDGenerator.generate();
        String ywh = "";
        if (ybdcXm != null) {
            //生成新的项目
            BdcXm newXm = ybdcXm;
            newXm.setProid(proid);
            newXm.setWiid(proid);
            newXm.setCjsj(new Date());
            ywh = bdcSlbhCustomServiceContext.getSlbhService().generateBdcXmSlbh(newXm);
            newXm.setBh(ywh);
            newXm.setDjzx("1202");
            newXm.setYbh(ybdcXm.getBh());
            entityMapper.saveOrUpdate(newXm, newXm.getProid());
            //生成新的审批信息
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcCfTemp.getProid());
            if (bdcSpxx != null) {
                BdcSpxx newBdcSpxx = bdcSpxx;
                newBdcSpxx.setSpxxid(UUIDGenerator.generate18());
                newBdcSpxx.setProid(proid);
                entityMapper.saveOrUpdate(newBdcSpxx, newBdcSpxx.getSpxxid());
            }
            //生成新的权利人
            List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcCfTemp.getProid());
            if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                for (int j = 0; j < bdcQlrList.size(); j++) {
                    BdcQlr bdcQlr = bdcQlrList.get(j);
                    bdcQlr.setProid(proid);
                    bdcQlr.setQlrid(UUIDGenerator.generate18());
                    entityMapper.insertSelective(bdcQlr);
                }
            }
        }

        Date cfksqx = bdcCfTemp.getCfksqx();
        Date cfjsqx = bdcCfTemp.getCfjsqx();
        BdcCf newCf = bdcCfTemp;

        if (cfksqx != null && cfjsqx != null) {
            Long days = CommonUtil.getDaySub(cfksqx, cfjsqx);
            int daysI = days.intValue() - 1;
            if (oldBdcCf != null && oldBdcCf.getJfdjsj() != null) {
                String jsqx = CalendarUtil.addDay(CalendarUtil.formatDateTime(oldBdcCf.getJfdjsj()), daysI);
                newCf.setCfjsqx(CalendarUtil.formatDate(jsqx));
            } else {
                Date jsqx = CommonUtil.plusDay(new Date(), daysI);
                newCf.setCfjsqx(jsqx);
            }
        }
        newCf.setYwh(ywh);
        newCf.setProid(proid);
        newCf.setQlid(UUIDGenerator.generate());
        if (oldBdcCf != null) {
            newCf.setCfksqx(oldBdcCf.getJfdjsj());
            newCf.setDbr(oldBdcCf.getJfdbr());
        }
        newCf.setCflx(Constants.CFLX_ZD_CF);
        newCf.setQszt(Constants.QLLX_QSZT_XS);
        newCf.setLhsx(null);
        newCf.setFj(CalendarUtil.getCurChinaYMDStrDate() + XF_CF_FJ_ZJNR);
        entityMapper.insertSelective(newCf);
    }

    @Override
    public List<BdcCf> andEqualQueryCf(final Map map, final String orderByClause) {
        List<BdcCf> list = null;
        if (map != null) {
            Example qllxTemp = new Example(BdcCf.class);
            if (StringUtils.isNotBlank(orderByClause)) {
                qllxTemp.setOrderByClause(orderByClause);
            }
            if (map.entrySet().size() > 0) {
                Iterator iter = map.entrySet().iterator();
                Example.Criteria criteria = qllxTemp.createCriteria();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    if (val != null)
                        criteria.andEqualTo(key.toString(), val);
                }
            }
            if (CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria()) && CollectionUtils.isNotEmpty(qllxTemp.getOredCriteria().get(0).getAllCriteria()))
                list = entityMapper.selectByExample(BdcCf.class, qllxTemp);
        }
        return list;
    }

    @Override
    public List<BdcCf> getCfByBdcdyid(final String bdcdyid) {
        List<BdcCf> bdcCfList = new ArrayList<BdcCf>();
        if (StringUtils.isNotBlank(bdcdyid)) {
            HashMap map = new HashMap();
            map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
            List<BdcCf> cfList = andEqualQueryCf(map, "");
            if (CollectionUtils.isNotEmpty(cfList)) {
                for (BdcCf bdcCf : cfList) {
                    if (bdcCf.getQszt() == null || bdcCf.getQszt() == 0 || bdcCf.getQszt() == 1)
                        bdcCfList.add(bdcCf);
                }
            }
        }
        return bdcCfList;
    }

    @Override
    public List<BdcCf> getXsCfByBdcdyh(final String bdcdyh) {
        List<BdcCf> bdcCfList = null;
        if (StringUtils.isNotBlank(bdcdyh)) {
            Map hashMap = Maps.newHashMap();
            hashMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
            hashMap.put("qszt", Constants.QLLX_QSZT_XS);
            hashMap.put("cflx", Constants.CFLX_ZD_CF);
            bdcCfList = bdcCfMapper.getCfByMap(hashMap);
        }
        return bdcCfList;
    }

    @Override
    public String getCfDjzx(Project project, BdcXm yBdcXm) {
        String djzx = "";
        List<BdcCf> bdcCfList = null;
        List<GdCf> gdCfList = null;
        djzx = Constants.DJZX_CF;
        String sjppType = AppConfig.getProperty("sjpp.type");
        //解封的登记子项是解封
        if (StringUtils.equals(project.getSqlx(), Constants.SQLX_JF) || StringUtils.equals(project.getSqlx(), Constants.SQLX_PLJF) || StringUtils.equals(project.getSqlx(), Constants.SQLX_JF_NOQL) || StringUtils.equals(project.getSqlx(), Constants.SQLX_JF_NOBDCDY)) {
            djzx = Constants.DJZX_JF;
        } else {
            //查封走新建
            if (StringUtils.equals(project.getXmly(), Constants.XMLY_BDC)) {
                if (StringUtils.isNotBlank(project.getDjId())) {
                    djzx = Constants.DJZX_YCF;
                    //jiangganzhi 关联附属设施 附属设施登记子项要跟随主房登记子项
                    List<BdcXm> bdcXmList = null;
                    HashMap map = new HashMap();
                    map.put("wiid", project.getWiid());
                    bdcXmList = bdcXmService.getBdcXmList(map);
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        BdcXm bdcXm = bdcXmList.get(0);
                        if (StringUtils.isNotBlank(bdcXm.getDjzx()) && !StringUtils.equals(bdcXm.getDjzx(), Constants.DJZX_YCF)) {
                            djzx = bdcXm.getDjzx();
                        }
                    }
                } else {
                    if (yBdcXm != null) {
                        if (StringUtils.equals(yBdcXm.getQllx(), Constants.QLLX_CFDJ)) {
                            djzx = Constants.DJZX_XF;
                        } else if (StringUtils.isNotBlank(project.getBdcdyh())) {
                            bdcCfList = getXsCfByBdcdyh(project.getBdcdyh());
                            if (CollectionUtils.isNotEmpty(bdcCfList)) {
                                djzx = Constants.DJZX_LH;
                            }
                        }
                    }
                }
                //过度查封的登记子项
            } else {
                if (StringUtils.equals(sjppType, Constants.PPLX_CG)) {
                    GdFwsyq gdFwsyq = null;
                    GdTdsyq gdTdsyq = null;
                    if (StringUtils.isNotBlank(project.getYqlid())) {
                        if (StringUtils.equals(project.getBdclx(), Constants.BDCLX_TDFW)) {
                            gdFwsyq = gdFwService.getGdFwsyqByQlid(project.getYqlid());
                        } else {
                            gdTdsyq = gdTdService.getGdTdsyqByQlid(project.getYqlid());
                        }

                        //如果是选择查封权利做的流程即是续封，选择房屋所有权做的是查封
                        //lx 过渡查封做换证、遗失补证，也为查封
                        if (gdFwsyq != null || gdTdsyq != null ||
                                StringUtils.equals(project.getSqlx(), Constants.SQLX_HZ_DM) ||
                                StringUtils.equals(project.getSqlx(), Constants.SQLX_YSBZ_DM)) {
                            //获取过渡查封
                            gdCfList = gdCfService.getGdCfListByQlid(project.getYqlid(), 0);
                            GdCf gdCf = entityMapper.selectByPrimaryKey(GdCf.class, project.getYqlid());
                            if (StringUtils.isNotBlank(project.getBdcdyh())) {
                                bdcCfList = getXsCfByBdcdyh(project.getBdcdyh());
                            }
                            if (CollectionUtils.isNotEmpty(bdcCfList) || CollectionUtils.isNotEmpty(gdCfList)) {
                                djzx = Constants.DJZX_LH;
                            } else {
                                djzx = Constants.DJZX_CF;
                            }
                            //lx 根据GdCf的cflx确定djzx（与过程匹配相同），后面生成查封权利用于确定cflx
                            if (gdCf != null) {
                                if (StringUtils.equals(gdCf.getCflx(), Constants.CFLX_GD_CF)) {
                                    djzx = Constants.DJZX_CF;
                                } else if (StringUtils.equals(gdCf.getCflx(), Constants.CFLX_GD_LHCF)) {
                                    djzx = Constants.DJZX_LH;
                                } else if (StringUtils.equals(gdCf.getCflx(), Constants.CFLX_GD_XF)) {
                                    djzx = Constants.DJZX_XF;
                                } else if (StringUtils.equals(gdCf.getCflx(), Constants.CFLX_GD_YCF) || StringUtils.equals(gdCf.getCflx(), Constants.CFLX_GD_LHYCF)) {
                                    djzx = Constants.DJZX_YCF;
                                } else {
                                    djzx = Constants.DJZX_CF;
                                }
                            }
                        } else {
                            djzx = Constants.DJZX_XF;
                        }
                    }
                } else {
                    //过程登记子项的获取，以后优化
                    GdCf gdCf = gdFwService.getGdCfByCfid(project.getYqlid(), 0);
                    if (gdCf != null) {
                        if (StringUtils.isBlank(gdCf.getCflx()) || StringUtils.equals(gdCf.getCflx(), "查封")) {
                            djzx = "1202";
                        } else if (StringUtils.equals(gdCf.getCflx(), "轮候查封")) {
                            djzx = "1203";
                        } else if (StringUtils.equals(gdCf.getCflx(), "续封")) {
                            djzx = "1204";
                        } else if (StringUtils.equals(gdCf.getCflx(), "预查封") || StringUtils.equals(gdCf.getCflx(), "轮候预查封")) {
                            djzx = "1201";
                        }
                    }
                }
            }
        }
        return djzx;
    }

    @Override
    public List<BdcXm> getCfXmByBdcdyh(final String bdcdyh) {
        return StringUtils.isNotBlank(bdcdyh) ? bdcCfMapper.getCfXmByBdcdyh(bdcdyh) : null;
    }

    /**
     * @author bianwen
     * @description 查封顺序
     */
    public void updateCfsx(String cflx, String bdcdyh, String fwid, String tdid) {
        List<HashMap> cfList = new ArrayList();
        if (StringUtils.isNotBlank(cflx) && (!Constants.CFLX_XF.equals(cflx) || !Constants.CFLX_GD_XF.equals(cflx))) {

            if (StringUtils.isNotBlank(bdcdyh)) {
                HashMap paraMap = new HashMap();
                paraMap.put(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                paraMap.put("qszt", 1);
                paraMap.put("cflx", Constants.CFLX_XF);
                /**
                 * @author bianwen
                 * @description 获取不动产的查封
                 */
                List<Map> bdcCfList = bdcCfMapper.queryBdcCfByPage(paraMap);
                if (CollectionUtils.isNotEmpty(bdcCfList)) {

                    for (int i = 0; i < bdcCfList.size(); i++) {
                        HashMap<String, Date> cfMap = new HashMap<String, Date>();
                        String bdcCfid = "bdc/" + bdcCfList.get(i).get("QLID");
                        Date sdsj = (Date) bdcCfList.get(i).get("CFSJ");
                        cfMap.put(bdcCfid, sdsj);
                        cfList.add(cfMap);
                    }
                }
                /**
                 * @author bianwen
                 * @description 获取过渡查封
                 * @description 已匹配不动产单元的通过不动产单元号查询
                 * @description 若匹配了土地证，土地证的查封也要进行排序
                 */
                Example example = new Example(GdDyhRel.class);
                example.createCriteria().andEqualTo(ParamsConstants.BDCDYH_LOWERCASE, bdcdyh);
                List gdDyhRelList = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(gdDyhRelList)) {
                    HashMap<String, String> fwMap = new HashMap();
                    HashMap<String, String> tdMap = new HashMap();
                    for (int i = 0; i < gdDyhRelList.size(); i++) {
                        GdDyhRel gdDyhRel = (GdDyhRel) gdDyhRelList.get(i);
                        if (StringUtils.equals(bdcdyh.substring(19, 20), "F")) {
                            if (StringUtils.isNotBlank(gdDyhRel.getGdid()) && !fwMap.containsKey(gdDyhRel.getGdid())) {
                                fwMap.put(gdDyhRel.getGdid(), "fw");
                            }
                            if (StringUtils.isNotBlank(gdDyhRel.getTdid()) && !tdMap.containsKey(gdDyhRel.getTdid())) {
                                tdMap.put(gdDyhRel.getTdid(), "td");
                            }
                        } else if (StringUtils.equals(bdcdyh.substring(19, 20), "W") &&
                                StringUtils.isNotBlank(gdDyhRel.getGdid()) && !tdMap.containsKey(gdDyhRel.getGdid())) {
                            tdMap.put(gdDyhRel.getGdid(), "td");
                        }
                    }
                    if (MapUtils.isNotEmpty(fwMap)) {
                        Iterator i = fwMap.entrySet().iterator();
                        while (i.hasNext()) {
                            Map.Entry entry = (java.util.Map.Entry) i.next();
                            String fwidTemp = (String) entry.getKey();
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCID_LOWERCASE, fwidTemp);
                            map.put("cflx", Constants.CFLX_GD_XF);
                            List<HashMap> gdFwcfList = gdCfMapper.getGdCfListByMap(map);
                            if (CollectionUtils.isNotEmpty(gdFwcfList)) {
                                for (int j = 0; j < gdFwcfList.size(); j++) {
                                    HashMap<String, Date> cfMap = new HashMap<String, Date>();
                                    String gdCfid = "gdfw/" + gdFwcfList.get(j).get("CFID");
                                    Date sdsj = (Date) gdFwcfList.get(j).get("CFSDSJ");
                                    cfMap.put(gdCfid, sdsj);
                                    cfList.add(cfMap);
                                }
                            }
                        }
                    }
                    if (MapUtils.isNotEmpty(tdMap)) {
                        Iterator i = tdMap.entrySet().iterator();
                        while (i.hasNext()) {
                            Map.Entry entry = (java.util.Map.Entry) i.next();
                            String tdidTemp = (String) entry.getKey();
                            HashMap map = new HashMap();
                            map.put(ParamsConstants.BDCID_LOWERCASE, tdidTemp);
                            map.put("cflx", Constants.CFLX_GD_XF);
                            List<HashMap> gdTdcfList = gdCfMapper.getGdCfListByMap(map);
                            if (CollectionUtils.isNotEmpty(gdTdcfList)) {
                                for (int j = 0; j < gdTdcfList.size(); j++) {
                                    HashMap<String, Date> cfMap = new HashMap<String, Date>();
                                    String gdCfid = "gdtd/" + gdTdcfList.get(j).get("CFID");
                                    Date sdsj = (Date) gdTdcfList.get(j).get("CFSDSJ");
                                    cfMap.put(gdCfid, sdsj);
                                    cfList.add(cfMap);
                                }
                            }
                        }
                    }
                }
            }

            /**
             * @author bianwen
             * @description 获取过渡查封
             */
            if (StringUtils.isNotBlank(fwid)) {
                HashMap map = new HashMap();
                map.put(ParamsConstants.BDCID_LOWERCASE, fwid);
                map.put("cflx", Constants.CFLX_GD_XF);
                List<HashMap> gdFwcfList = gdCfMapper.getGdCfListByMap(map);
                if (CollectionUtils.isNotEmpty(gdFwcfList)) {
                    for (int j = 0; j < gdFwcfList.size(); j++) {
                        HashMap<String, Date> cfMap = new HashMap<String, Date>();
                        String gdCfid = "gdfw/" + gdFwcfList.get(j).get("CFID");
                        Date sdsj = (Date) gdFwcfList.get(j).get("CFSDSJ");
                        cfMap.put(gdCfid, sdsj);
                        cfList.add(cfMap);
                    }
                }
                Example example = new Example(GdDyhRel.class);
                example.createCriteria().andEqualTo("gdid", fwid);
                List gdDyhRelList = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(gdDyhRelList)) {

                    for (int i = 0; i < gdDyhRelList.size(); i++) {
                        GdDyhRel gdDyhRel = (GdDyhRel) gdDyhRelList.get(i);
                        String tdidTemp = gdDyhRel.getTdid();
                        HashMap map1 = new HashMap();
                        map1.put(ParamsConstants.BDCID_LOWERCASE, tdidTemp);
                        map.put("cflx", Constants.CFLX_GD_XF);
                        List<HashMap> gdTdcfList = gdCfMapper.getGdCfListByMap(map1);
                        if (CollectionUtils.isNotEmpty(gdTdcfList)) {
                            for (int j = 0; j < gdTdcfList.size(); j++) {
                                HashMap<String, Date> cfMap = new HashMap<String, Date>();
                                String gdCfid = "gdtd/" + gdTdcfList.get(j).get("CFID");
                                Date sdsj = (Date) gdTdcfList.get(j).get("CFSDSJ");
                                cfMap.put(gdCfid, sdsj);
                                cfList.add(cfMap);
                            }
                        }
                    }
                }
            }

            if (StringUtils.isNotBlank(tdid)) {
                HashMap map = new HashMap();
                map.put(ParamsConstants.BDCID_LOWERCASE, tdid);
                map.put("cflx", Constants.CFLX_GD_XF);
                List<HashMap> gdTdcfList = gdCfMapper.getGdCfListByMap(map);
                if (CollectionUtils.isNotEmpty(gdTdcfList)) {
                    for (int j = 0; j < gdTdcfList.size(); j++) {
                        HashMap<String, Date> cfMap = new HashMap<String, Date>();
                        String gdCfid = "gdtd/" + gdTdcfList.get(j).get("CFID");
                        Date sdsj = (Date) gdTdcfList.get(j).get("SDSJ");
                        cfMap.put(gdCfid, sdsj);
                        cfList.add(cfMap);
                    }
                }
            }

            Collections.sort(cfList, new Comparator<HashMap>() {
                public int compare(HashMap map1, HashMap map2) {
                    Date kssj1 = null;
                    Date kssj2 = null;
                    Iterator i = map1.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (java.util.Map.Entry) i.next();
                        kssj1 = (Date) entry.getValue();
                    }
                    Iterator j = map2.entrySet().iterator();
                    while (j.hasNext()) {
                        Map.Entry entry = (java.util.Map.Entry) j.next();
                        kssj2 = (Date) entry.getValue();
                    }
                    if (kssj1 != null && kssj2 != null) {
                        if (CalendarUtil.beforeDate(kssj1, kssj2)) {
                            return -1;
                        } else if (kssj1.after(kssj2)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                    return 0;
                }
            });
        }

        if (CollectionUtils.isNotEmpty(cfList)) {
            for (int j = 0; j < cfList.size(); j++) {
                String cfid = null;
                HashMap cfMapTemp = cfList.get(j);
                Iterator i = cfMapTemp.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry entry = (java.util.Map.Entry) i.next();
                    cfid = (String) entry.getKey();
                }
                if (StringUtils.isNotBlank(cfid) && (cfid.indexOf("gdfw") > -1 || cfid.indexOf("gdtd") > -1)) {
                    GdCf gdCfTemp = gdFwService.getGdCfByCfid(cfid.substring(cfid.indexOf("/") + 1), 0);
                    gdCfTemp.setCfsx(j + 1);

                    entityMapper.saveOrUpdate(gdCfTemp, gdCfTemp.getCfid());

                } else if (StringUtils.isNotBlank(cfid) && cfid.indexOf("bdc") > -1) {
                    Example example = new Example(BdcCf.class);
                    example.createCriteria().andEqualTo("qlid", cfid.substring(cfid.indexOf("/") + 1));
                    List cfListTemp = entityMapper.selectByExample(example);
                    if (CollectionUtils.isNotEmpty(cfListTemp)) {

                        BdcCf bdcCfTemp = (BdcCf) cfListTemp.get(0);
                        bdcCfTemp.setCfsx(j + 1);
                        entityMapper.saveOrUpdate(bdcCfTemp, bdcCfTemp.getQlid());
                    }
                }
            }
        }
    }


    /**
     * @param bdcCf
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 保存查封信息
     */
    @Override
    public void saveBdcCf(BdcCf bdcCf) {
        entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
    }

    /**
     * @param model,qlid,bdcXm
     * @return
     * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
     * @version
     * @description 初始化不动产查封信息页面
     */
    @Override
    public Model initBdcCfForPl(Model model, String qlid, BdcXm bdcXm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder bcfQlr = new StringBuilder();
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (int j = 0; j < bdcQlrList.size(); j++) {
                if (StringUtils.isNotBlank(bdcQlrList.get(j).getQlrmc())) {
                    if (j == 0) {
                        bcfQlr.append(bdcQlrList.get(j).getQlrmc());
                    } else {
                        if (bcfQlr.indexOf(bdcQlrList.get(j).getQlrmc()) < 0) {
                            bcfQlr.append(" ").append(bdcQlrList.get(j).getQlrmc());
                        }
                    }
                }
            }
        }
        List<HashMap> cflxList = bdcZdGlService.getBdcZdCflx(new HashMap());
        BdcCf bdcCf = entityMapper.selectByPrimaryKey(BdcCf.class, qlid);
        String cfksqx = "";
        String cfjsqx = "";
        String djsj = "";
        String jfdjsj = "";
        if (bdcCf != null && bdcCf.getCfksqx() != null)
            cfksqx = sdf.format(bdcCf.getCfksqx());
        if (bdcCf != null && bdcCf.getCfjsqx() != null)
            cfjsqx = sdf.format(bdcCf.getCfjsqx());
        if (bdcCf != null && bdcCf.getDjsj() != null)
            djsj = sdf.format(bdcCf.getDjsj());
        if (bdcCf != null && bdcCf.getJfdjsj() != null)
            jfdjsj = sdf.format(bdcCf.getJfdjsj());
        model.addAttribute("bcfQlr", bcfQlr);
        model.addAttribute("cflxList", cflxList);
        model.addAttribute("cfksqx", cfksqx);
        model.addAttribute("cfjsqx", cfjsqx);
        model.addAttribute("djsj", djsj);
        model.addAttribute("jfdjsj", jfdjsj);
        model.addAttribute("bdcCf", bdcCf);
        return model;
    }


    @Override
    public void dealXfjf(BdcCf bdcCf, String proid, String username, Date date) {
        String jfywh = "";
        String jfjg = "";
        String jfwj = "";
        String jfwh = "";
        Date jfsj = new Date();
        if (bdcCf != null && StringUtils.isNotEmpty(proid)) {
            if (StringUtils.isNotEmpty(bdcCf.getJfywh())) {
                jfywh = bdcCf.getJfywh();
            }
            if (StringUtils.isNotEmpty(bdcCf.getJfjg())) {
                jfjg = bdcCf.getJfjg();
            }
            if (StringUtils.isNotEmpty(bdcCf.getJfwh())) {
                jfwh = bdcCf.getJfwh();
            }
            if (StringUtils.isNotEmpty(bdcCf.getJfwj())) {
                jfwj = bdcCf.getJfwj();
            }
            if (bdcCf.getJfsj() != null) {
                jfsj = bdcCf.getJfsj();
            }
            BdcXmRel bdcXmRel = null;
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(proid);
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                bdcXmRel = bdcXmRelList.get(0);
            }
            if (bdcXmRel != null) {
                Boolean yXmGdLy = false;
                if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                    List<GdCf> gdCfList = gdCfService.getGdCfListByQlid(bdcXmRel.getYqlid(), 0);
                    if (CollectionUtils.isNotEmpty(gdCfList)) {
                        yXmGdLy = true;
                    }
                }
                if (yXmGdLy) {
                    List<GdLsCfBh> allGdLsCfBhList = bdcXmRelService.getAllGdLsCfBhByCfid(bdcXmRel.getYqlid());
                    dealGdXfJf(allGdLsCfBhList, bdcXmRel, proid, jfywh, jfwh, jfwj, jfsj, jfjg, username, date);
                } else {
                    //先拿到所有不动产的上下手关系
                    List<BdcXmRel> allBdcXmRelList = bdcXmRelService.getAllHisXmRelByProid(proid);
                    dealBdcXfJf(allBdcXmRelList, bdcXmRel, proid, jfywh, jfwh, jfwj, jfsj, jfjg, username, date);
                }
            }
        }
    }

    @Override
    public void dealBdcXfJf(List<BdcXmRel> allBdcXmRelList, BdcXmRel bdcXmRel, String proid, String jfywh, String jfwh, String jfwj, Date jfsj, String jfjg, String username, Date date) {
        if (CollectionUtils.isNotEmpty(allBdcXmRelList)) {
            Boolean haveCf = false;
            String yCfid = null;
            for (BdcXmRel bdcXmRelTemp : allBdcXmRelList) {
                String proidTemp = null;
                if (StringUtils.isNotBlank(bdcXmRelTemp.getProid())) {
                    proidTemp = bdcXmRelTemp.getProid();
                }
                BdcCf bdcCfTemp = selectCfByProid(proidTemp);
                if (bdcCfTemp != null && StringUtils.isNotBlank(bdcCfTemp.getCflx()) && StringUtils.equals(bdcCfTemp.getCflx(), Constants.CFLX_XF)) {
                    bdcCfTemp.setJfywh(jfywh);
                    bdcCfTemp.setJfwh(jfwh);
                    bdcCfTemp.setJfwj(jfwj);
                    bdcCfTemp.setJfsj(jfsj);
                    bdcCfTemp.setJfjg(jfjg);
                    bdcCfTemp.setQszt(2);
                    if (StringUtils.isNotEmpty(username)) {
                        bdcCfTemp.setJfdbr(username);
                    }
                    if (date != null) {
                        bdcCfTemp.setJfdjsj(date);
                    }
                    saveBdcCf(bdcCfTemp);
                    //生成对应bdc_xm_rel
                    BdcXmRel newBdcXmRel = new BdcXmRel();
                    newBdcXmRel.setRelid(UUIDGenerator.generate18());
                    newBdcXmRel.setProid(proid);
                    newBdcXmRel.setYqlid(bdcCfTemp.getQlid());
                    newBdcXmRel.setYproid(proidTemp);
                    newBdcXmRel.setQjid(bdcXmRel.getQjid());
                    entityMapper.saveOrUpdate(newBdcXmRel, newBdcXmRel.getRelid());
                }
                if (bdcCfTemp != null && StringUtils.isNotBlank(bdcCfTemp.getCflx()) && StringUtils.equals(bdcCfTemp.getCflx(), Constants.CFLX_ZD_CF) && bdcCfTemp.getQszt() != null && bdcCfTemp.getQszt() == 1) {
                    haveCf = true;
                    yCfid = bdcXmRelTemp.getYqlid();
                }
            }
            if (!haveCf && StringUtils.isNotBlank(yCfid)) {
                List<GdLsCfBh> allGdLsCfBhList = bdcXmRelService.getAllGdLsCfBhByCfid(yCfid);
                dealGdXfJf(allGdLsCfBhList, bdcXmRel, proid, jfywh, jfwh, jfwj, jfsj, jfjg, username, date);
            }
        }
    }

    @Override
    public void dealGdXfJf(List<GdLsCfBh> allGdLsCfBhList, BdcXmRel bdcXmRel, String proid, String jfywh, String jfwh, String jfwj, Date jfsj, String jfjg, String username, Date date) {
        if (CollectionUtils.isNotEmpty(allGdLsCfBhList)) {
            for (GdLsCfBh gdLsCfBh : allGdLsCfBhList) {
                List<GdCf> gdCfList = gdCfService.getGdCfListByQlid(gdLsCfBh.getCfid(), 0);
                if (CollectionUtils.isNotEmpty(gdCfList)) {
                    for (GdCf gdCf : gdCfList) {
                        if (StringUtils.isNotBlank(gdCf.getCflx()) && StringUtils.equals(gdCf.getCflx(), Constants.CFLX_GD_XF)) {
                            gdCf.setJfywh(jfywh);
                            gdCf.setJfwh(jfwh);
                            gdCf.setJfwj(jfwj);
                            gdCf.setJfrq(jfsj);
                            gdCf.setJfjg(jfjg);
                            gdCf.setIsjf(1);
                            if (StringUtils.isNotEmpty(username)) {
                                gdCf.setJfdbr(username);
                            }
                            if (date != null) {
                                gdCf.setJfdbsj(date);
                            }
                            entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                        }
                    }
                }
            }
        }
    }


    @Transactional
    @Override
    public void turnLhcfToCf(final BdcCf bdcCf, final String bdcdyid, final BdcXm bdcXm) {
        String lhcfTurnToCfEnable = StringUtils.deleteWhitespace(AppConfig.getProperty("lhcfTurnToCf.enable"));
        if (StringUtils.equals(lhcfTurnToCfEnable, ParamsConstants.TRUE_LOWERCASE)) {
            try {
                if (StringUtils.isNotBlank(bdcdyid)) {
                    /**
                     * @author bianwen
                     * @description 不动产库 现势的查封记录
                     */
                    HashMap map = new HashMap();
                    map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                    map.put("cflx", Constants.CFLX_ZD_CF);
                    map.put("qszt", Constants.QLLX_QSZT_XS);
                    List<BdcCf> bdcCfListTemp = andEqualQueryCf(map, null);

                    /**
                     * @author bianwen
                     * @description 不动产库 现势的轮候查封
                     */
                    map = new HashMap();
                    map.put(ParamsConstants.BDCDYID_LOWERCASE, bdcdyid);
                    map.put("cflx", Constants.CFLX_LHCF);
                    map.put("qszt", Constants.QLLX_QSZT_XS);
                    List<BdcCf> bdcCfList = andEqualQueryCf(map, null);

                    /**
                     * @author jiangganzhi
                     * @description 过渡库 现势的查封和轮候查封记录
                     */
                    List<GdCf> gdCfList = new ArrayList<GdCf>();
                    List<GdCf> gdCfTempList = new ArrayList<GdCf>();
                    List<GdBdcQlRel> gdBdcQlRelList = new ArrayList<GdBdcQlRel>();
                    BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcdyid);
                    if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh())) {
                        List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByDyh(bdcBdcdy.getBdcdyh());
                        if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                            for (BdcGdDyhRel bdcGdDyhRel : bdcGdDyhRelList) {
                                if (bdcGdDyhRel != null && StringUtils.isNotBlank(bdcGdDyhRel.getGdid())) {
                                    List<GdBdcQlRel> gdBdcQlRelTempList = gdBdcQlRelService.queryGdBdcQlListByBdcid(bdcGdDyhRel.getGdid());
                                    if (CollectionUtils.isNotEmpty(gdBdcQlRelTempList))
                                        gdBdcQlRelList.addAll(gdBdcQlRelTempList);
                                }
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(gdBdcQlRelList)) {
                        for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList) {
                            if (gdBdcQlRel != null && StringUtils.isNotBlank(gdBdcQlRel.getQlid())) {
                                GdCf gdCf = gdFwService.getGdCfByCfid(gdBdcQlRel.getQlid(), 0);
                                /**
                                 * @author bianwen
                                 * @description 过渡库中的字典项存值都是汉字
                                 */
                                if (gdCf != null && StringUtils.isNotBlank(gdCf.getCflx()) && StringUtils.equals(bdcZdGlService.getCflxDmByMc(gdCf.getCflx()), Constants.CFLX_ZD_CF))
                                    gdCfTempList.add(gdCf);

                                if (gdCf != null && StringUtils.isNotBlank(gdCf.getCflx()) && StringUtils.equals(bdcZdGlService.getCflxDmByMc(gdCf.getCflx()), Constants.CFLX_LHCF))
                                    gdCfList.add(gdCf);
                            }
                        }
                    }
                    Boolean needIgnore = false;
                    if (CollectionUtils.isNotEmpty(bdcCfListTemp)) {
                        BdcCf checkBdcCf = bdcCfListTemp.get(0);
                        if (checkBdcCf != null && StringUtils.isNotBlank(checkBdcCf.getProid()) && bdcCf != null
                                && StringUtils.isNotBlank(bdcCf.getProid()) && StringUtils.equals(checkBdcCf.getProid(), bdcCf.getProid())) {
                            needIgnore = true;
                        }
                    }
                    //zdd 只有当系统中不存在查封记录时才会 将轮候查封的记录更新为查封
                    if ((CollectionUtils.isEmpty(bdcCfListTemp) && CollectionUtils.isEmpty(gdCfTempList)) || needIgnore) {
                        turnLhcfToCfByCfList(bdcCfList, gdCfList, bdcCf, bdcXm);
                    }
                }
            } catch (Exception e) {
                log.error("turnLhcfToCf");
            }
        }
    }

    @Override
    public void turnLhcfToCfByCfList(List<BdcCf> bdcCfList, List<GdCf> gdCfList, BdcCf bdcCf, BdcXm bdcXm) {
        /**
         * @author bianwen
         * @description 获取不动产和过渡所有的轮候查封一起进行查封开始时间的排序
         */
        if (CollectionUtils.isNotEmpty(gdCfList) || CollectionUtils.isNotEmpty(bdcCfList)) {
            List<HashMap> cfList = new ArrayList<HashMap>();
            if (CollectionUtils.isNotEmpty(gdCfList)) {

                for (int i = 0; i < gdCfList.size(); i++) {
                    HashMap<String, Date> lhcfMap = new HashMap<String, Date>();
                    String gdCfid = "gd/" + gdCfList.get(i).getCfid();
                    Date cfkssj = gdCfList.get(i).getCfksrq();
                    lhcfMap.put(gdCfid, cfkssj);
                    cfList.add(lhcfMap);
                }
            }
            if (CollectionUtils.isNotEmpty(bdcCfList)) {
                for (int j = 0; j < bdcCfList.size(); j++) {
                    HashMap<String, Date> lhcfMap = new HashMap<String, Date>();
                    String bdcCfid = "bdc/" + bdcCfList.get(j).getQlid();
                    Date cfkssj = bdcCfList.get(j).getCfksqx();
                    lhcfMap.put(bdcCfid, cfkssj);
                    cfList.add(lhcfMap);
                }
            }

            Collections.sort(cfList, new Comparator<HashMap>() {
                public int compare(HashMap mapOne, HashMap mapTwo) {
                    Date kssjOne = null;
                    Date kssjTwo = null;
                    Iterator i = mapOne.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry entry = (java.util.Map.Entry) i.next();
                        kssjOne = (Date) entry.getValue();
                    }
                    Iterator j = mapTwo.entrySet().iterator();
                    while (j.hasNext()) {
                        Map.Entry entry = (java.util.Map.Entry) j.next();
                        kssjTwo = (Date) entry.getValue();
                    }
                    if (kssjOne != null && kssjTwo != null) {
                        if (CalendarUtil.beforeDate(kssjOne, kssjTwo)) {
                            return -1;
                        } else if (kssjOne.after(kssjTwo)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                    return 0;
                }
            });

            String cfid = null;
            HashMap cfMapTemp = cfList.get(0);
            Iterator i = cfMapTemp.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (java.util.Map.Entry) i.next();
                cfid = (String) entry.getKey();
            }
            if (StringUtils.isNotBlank(cfid) && cfid.indexOf("gd") > -1) {
                turnLhcfToCfByGdCfid(cfid);
            } else if (StringUtils.isNotBlank(cfid) && cfid.indexOf("bdc") > -1) {
                turnLhcfToCfByBdcCfid(cfid, bdcXm, bdcCf);
            }
        }
    }

    @Override
    public void turnLhcfToCfByGdCfid(String cfid) {
        GdCf gdCfTemp = gdFwService.getGdCfByCfid(cfid.substring(cfid.indexOf("/") + 1), 0);
        gdCfTemp.setIsjf(1);
        entityMapper.saveOrUpdate(gdCfTemp, gdCfTemp.getCfid());
        gdFwService.changeGdqlztByQlid(gdCfTemp.getCfid(), Constants.QLLX_QSZT_XS.toString());
        GdXm ygdxm = gdXmService.getGdXm(gdCfTemp.getProid());
        String proid = UUIDGenerator.generate18();
        String ywh = UUIDGenerator.generate18();
        if (ygdxm != null) {
            GdXm gdXm = ygdxm;
            gdXm.setProid(proid);
            gdXm.setSlbh(ywh);
            gdXm.setCsrq(new Date());
            entityMapper.saveOrUpdate(gdXm, gdXm.getProid());
        }

        /**
         * @author bianwen
         * @description 将轮候转为查封，生成新的查封权利
         */
        GdCf gdcf = null;
        try {
            gdcf = (GdCf) BeanUtils.cloneBean(gdCfTemp);
        } catch (Exception e) {
            log.error("BdcCfServiceImpl.turnLhcfToCfByGdCfid", e);
        }
        String cfidStr = UUIDGenerator.generate18();

        if (gdcf != null) {
            gdcf.setProid(proid);
            gdcf.setCfid(cfidStr);
            gdcf.setCflx(Constants.CFLX_GD_CF);
            gdcf.setIsjf(0);
            gdcf.setCfsx(null);
            gdcf.setFj(CalendarUtil.getCurChinaYMDStrDate() + LHCF_CF_FJ_ZJNR);
            entityMapper.insertSelective(gdcf);
        }


        List<GdQlr> gdQlrList = gdQlrService.queryGdQlrListByProid(gdCfTemp.getProid(), Constants.QLRLX_QLR);
        if (CollectionUtils.isNotEmpty(gdQlrList)) {
            for (GdQlr gdQlr : gdQlrList) {
                gdQlr.setProid(proid);
                gdQlr.setQlid(cfidStr);
                gdQlr.setQlrid(UUIDGenerator.generate18());
                entityMapper.saveOrUpdate(gdQlr, gdQlr.getQlrid());
            }
        }
        /**
         * @author bianwen
         * @description 插入gd_fw_ql或者gd_td_ql表
         */
        GdFwQl gdFwQl = entityMapper.selectByPrimaryKey(GdFwQl.class, gdCfTemp.getCfid());
        if (gdFwQl != null) {
            gdFwQl.setQlid(cfidStr);
            gdFwQl.setProid(proid);
            gdFwQl.setZslx(Constants.GDQL_CFWH_ZSLX);
            gdFwQl.setIszx("0");
            entityMapper.saveOrUpdate(gdFwQl, gdFwQl.getQlid());
        }
        GdTdQl gdTdQl = entityMapper.selectByPrimaryKey(GdTdQl.class, gdCfTemp.getCfid());
        if (gdTdQl != null) {
            gdTdQl.setQlid(cfidStr);
            gdTdQl.setProid(proid);
            gdTdQl.setZslx(Constants.GDQL_CFWH_ZSLX);
            gdTdQl.setIszx("0");
            entityMapper.saveOrUpdate(gdTdQl, gdTdQl.getQlid());
        }

        List<GdBdcQlRel> gdBdcQlRelList1 = gdBdcQlRelService.queryGdBdcQlListByQlid(gdCfTemp.getCfid());
        if (CollectionUtils.isNotEmpty(gdBdcQlRelList1)) {
            for (GdBdcQlRel gdBdcQlRel : gdBdcQlRelList1) {
                gdBdcQlRel.setQlid(cfidStr);
                gdBdcQlRel.setRelid(UUIDGenerator.generate18());
                entityMapper.saveOrUpdate(gdBdcQlRel, gdBdcQlRel.getRelid());
            }
        }

        List<GdQlDyhRel> gdQlDyhRelList = bdcGdDyhRelService.getGdQlDyhRel("", gdCfTemp.getCfid(), "");
        if (CollectionUtils.isNotEmpty(gdQlDyhRelList)) {
            for (GdQlDyhRel gdQlDyhRel : gdQlDyhRelList) {
                GdQlDyhRel newGdQlDyhRel = new GdQlDyhRel();
                newGdQlDyhRel.setRelid(UUIDGenerator.generate18());
                newGdQlDyhRel.setQlid(cfidStr);
                newGdQlDyhRel.setBdcdyh(gdQlDyhRel.getBdcdyh());
                newGdQlDyhRel.setBdclx(gdQlDyhRel.getBdclx());
                newGdQlDyhRel.setDjid(gdQlDyhRel.getDjid());
                newGdQlDyhRel.setTdqlid(gdQlDyhRel.getTdqlid());
                entityMapper.saveOrUpdate(newGdQlDyhRel, newGdQlDyhRel.getRelid());
            }
        }
    }

    @Override
    public void turnLhcfToCfByBdcCfid(String cfid, BdcXm bdcXm, BdcCf bdcCf) {
        Example example = new Example(BdcCf.class);
        example.createCriteria().andEqualTo("qlid", cfid.substring(cfid.indexOf("/") + 1));
        List cfListTemp = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(cfListTemp)) {
            BdcCf bdcCfTemp = (BdcCf) cfListTemp.get(0);
            bdcCfTemp.setQszt(Constants.QLLX_QSZT_HR);
            entityMapper.saveOrUpdate(bdcCfTemp, bdcCfTemp.getQlid());
            //生成查封数据
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(bdcCfTemp.getProid());
            String proid = UUIDGenerator.generate();
            //生成轮候查封的解封项目
            createLhcfJfXm(ybdcXm, bdcXm, bdcCfTemp, proid);
            //生成查封新的项目
            turnLhcfToBdcCf(ybdcXm, bdcXm, bdcCfTemp, proid);
        }
    }

    @Override
    public void createLhcfJfXm(BdcXm ybdcXm, BdcXm bdcXm, BdcCf bdcLhCf, String proid) {
        if (ybdcXm != null) {
            //生成新解封项目 使推送中间库时改变中间库轮候查封数据状态
            String jfProid = UUIDGenerator.generate();
            BdcXm jfXm = bdcXm;
            jfXm.setProid(jfProid);
            String jfYwh = bdcXmService.creatXmbh(bdcXm);
            if (StringUtils.isNotBlank(jfYwh)) {
                jfXm.setBh(jfYwh);
                jfXm.setYbh(ybdcXm.getBh());
                jfXm.setCjsj(new Date());
                jfXm.setBjsj(new Date());
                jfXm.setWiid(bdcXm.getWiid());
                entityMapper.saveOrUpdate(jfXm, jfXm.getProid());

                //生成新的权利人
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcLhCf.getProid());
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    for (int j = 0; j < bdcQlrList.size(); j++) {
                        BdcQlr bdcQlr = bdcQlrList.get(j);
                        bdcQlr.setProid(jfProid);
                        bdcQlr.setQlrid(UUIDGenerator.generate18());
                        entityMapper.insertSelective(bdcQlr);
                    }
                }

                //生成新的审批信息
                BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcLhCf.getProid());
                if (bdcSpxx != null) {
                    BdcSpxx newBdcSpxx = bdcSpxx;
                    newBdcSpxx.setSpxxid(UUIDGenerator.generate18());
                    newBdcSpxx.setProid(jfProid);
                    entityMapper.saveOrUpdate(newBdcSpxx, newBdcSpxx.getSpxxid());
                }

                BdcXmRel jfBdcXmRel = new BdcXmRel();
                jfBdcXmRel.setRelid(UUIDGenerator.generate());
                jfBdcXmRel.setProid(jfProid);
                jfBdcXmRel.setYproid(bdcLhCf.getProid());
                jfBdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                List<BdcXmRel> lhCfBdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcLhCf.getProid());
                if (CollectionUtils.isNotEmpty(lhCfBdcXmRelList)) {
                    BdcXmRel lhCfBdcXmRel = lhCfBdcXmRelList.get(0);
                    jfBdcXmRel.setQjid(lhCfBdcXmRel.getQjid());
                }
                entityMapper.saveOrUpdate(jfBdcXmRel, jfBdcXmRel.getRelid());

                //创建轮候查封解封项目的同时将轮候查封的jfywh赋值
                bdcLhCf.setJfywh(jfXm.getBh());
                entityMapper.saveOrUpdate(bdcLhCf, bdcLhCf.getQlid());
            }

        }
    }

    @Override
    public void turnLhcfToBdcCf(BdcXm ybdcXm, BdcXm bdcXm, BdcCf bdcLhCf, String proid) {
        BdcXm newXm = ybdcXm;
        newXm.setProid(proid);
        newXm.setWiid(bdcXm.getWiid());
        newXm.setCjsj(new Date());
        String ywh = bdcSlbhCustomServiceContext.getSlbhService().generateBdcXmSlbh(newXm);
        newXm.setBh(ywh);
        newXm.setDjzx("1202");
        newXm.setYbh(ybdcXm.getBh());
        entityMapper.saveOrUpdate(newXm, newXm.getProid());
        //生成新的审批信息
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcLhCf.getProid());
        if (bdcSpxx != null) {
            BdcSpxx newBdcSpxx = bdcSpxx;
            newBdcSpxx.setSpxxid(UUIDGenerator.generate18());
            newBdcSpxx.setProid(proid);
            entityMapper.saveOrUpdate(newBdcSpxx, newBdcSpxx.getSpxxid());
        }
        //生成新的权利人
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(bdcLhCf.getProid());
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            for (int j = 0; j < bdcQlrList.size(); j++) {
                BdcQlr bdcQlr = bdcQlrList.get(j);
                bdcQlr.setProid(proid);
                bdcQlr.setQlrid(UUIDGenerator.generate18());
                entityMapper.insertSelective(bdcQlr);
            }
        }

        BdcCf newCf = new BdcCf();
        try {
            newCf = (BdcCf) BeanUtils.cloneBean(bdcLhCf);
        } catch (Exception e) {
            log.error("BdcCfServiceImpl.turnLhcfToBdcCf", e);
        }
        newCf.setYwh(ywh);
        newCf.setProid(proid);
        newCf.setQlid(UUIDGenerator.generate());
        //轮候查封转查封读轮候查封登簿人
        newCf.setDbr(bdcLhCf.getDbr());
        newCf.setCflx(Constants.CFLX_ZD_CF);
        newCf.setQszt(Constants.QLLX_QSZT_XS);
        newCf.setLhsx(null);
        //原轮候查封附记保留
        if (StringUtils.isNotBlank(bdcLhCf.getFj())) {
            newCf.setFj(bdcLhCf.getFj() + CalendarUtil.getCurChinaYMDStrDate() + LHCF_CF_FJ_ZJNR);
        } else {
            newCf.setFj(CalendarUtil.getCurChinaYMDStrDate() + LHCF_CF_FJ_ZJNR);
        }
        newCf.setJfywh(null);
        entityMapper.insertSelective(newCf);

        BdcXmRel lhtoCfBdcXmRel = new BdcXmRel();
        lhtoCfBdcXmRel.setRelid(UUIDGenerator.generate());
        lhtoCfBdcXmRel.setProid(newCf.getProid());
        lhtoCfBdcXmRel.setYproid(bdcLhCf.getProid());
        lhtoCfBdcXmRel.setYdjxmly(Constants.XMLY_BDC);
        // 添加轮候查封转查封，新生成的查封添加与产权的项目关系
        BdcXmRel newCfCqBdcXmRel = new BdcXmRel();
        newCfCqBdcXmRel.setRelid(UUIDGenerator.generate());
        newCfCqBdcXmRel.setProid(newCf.getProid());
        newCfCqBdcXmRel.setYdjxmly(Constants.XMLY_BDC);
        List<BdcXmRel> lhCfBdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcLhCf.getProid());
        if (CollectionUtils.isNotEmpty(lhCfBdcXmRelList)) {
            BdcXmRel lhCfBdcXmRel = lhCfBdcXmRelList.get(0);
            lhtoCfBdcXmRel.setQjid(lhCfBdcXmRel.getQjid());
            newCfCqBdcXmRel.setYproid(lhCfBdcXmRel.getYproid());
            newCfCqBdcXmRel.setQjid(lhCfBdcXmRel.getQjid());
        }
        entityMapper.saveOrUpdate(newCfCqBdcXmRel, newCfCqBdcXmRel.getRelid());
        entityMapper.saveOrUpdate(lhtoCfBdcXmRel, lhtoCfBdcXmRel.getRelid());
    }

    @Override
    public void updateAdjudicationState(BdcXm bdcXm, List<BdcXmRel> bdcXmRelList) {
        // 司法裁定办结后需要将查封全注销
        if (null != bdcXm) {
            List<BdcCf> bdcCfLst = getCfByBdcdyid(bdcXm.getBdcdyid());
            String userName = null != SessionUtil.getCurrentUser() ? SessionUtil.getCurrentUser().getUsername() : "";
            QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
            Date jfdjsj = null;
            if (qllxVo != null && qllxVo.getDjsj() != null) {
                //依生效法律文书申请转移登记、依法转移和抵押登记，核定登簿先进行解封登簿，再进行转移登簿，解封的登簿时间应该小于转移的登簿时间。
                jfdjsj = DateUtils.addSeconds(qllxVo.getDjsj(), -1);
            }
            if (jfdjsj == null) {
                jfdjsj = new Date(System.currentTimeMillis());
            }
            if (CollectionUtils.isNotEmpty(bdcCfLst)) {
                for (BdcCf bdcCf : bdcCfLst) {
                    //排除当前项目的查封
                    if (!judgeBdcCfForCurrentProject(bdcXm, bdcCf)) {
                        //将所有查封致为失效状态
                        bdcCf.setQszt(Constants.QLLX_QSZT_HR);
                        bdcCf.setIssx(Constants.SXZT_ISSX);
                        if (StringUtils.isNotBlank(bdcCf.getIscd()) && StringUtils.equals(bdcCf.getIscd(), Constants.ISCD_POSITIVE)) {
                            bdcCf.setIscd(Constants.ISCD_NEGETIVE);
                        }
                        bdcCf.setJfywh(bdcXm.getBh());
                        bdcCf.setJfdjsj(jfdjsj);
                        bdcCf.setJfdbr(userName);
                        String fj = bdcCf.getFj();
                        fj = fj + "\n" + "由于办理司法裁定转移后失效";
                        bdcCf.setFj(fj);
                        entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                        // 生成项目关系便于共享推送状态，插入前检查，避免插重
                        if (!bdcXmRelService.existBdcXmRelByProidAndYproid(bdcXm.getProid(), bdcCf.getProid())) {
                            BdcXmRel bdcXmRel = new BdcXmRel();
                            bdcXmRel.setRelid(UUIDGenerator.generate18());
                            bdcXmRel.setYdjxmly(Constants.XMLY_BDC);
                            bdcXmRel.setYqlid(bdcCf.getQlid());
                            bdcXmRel.setYproid(bdcCf.getProid());
                            bdcXmRel.setProid(bdcXm.getProid());
                            bdcXmRelService.saveBdcXmRel(bdcXmRel);
                        }
                    }
                }
            }

            // 依法转移上一手项目如果是过渡房产证 则yqlid为该房产使用权的qlid
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.isNotBlank(bdcXmRel.getYqlid())) {
                        List<GdCf> gdCfList = gdCfService.getGdCfListByQlid(bdcXmRel.getYqlid(), 0);
                        if (CollectionUtils.isNotEmpty(gdCfList)) {
                            for (GdCf gdCf : gdCfList) {
                                //将所有过渡查封失效
                                gdCf.setIsjf(1);
                                gdCf.setIssx(Constants.SXZT_ISSX);
                                if (StringUtils.isNotBlank(gdCf.getIscd()) && StringUtils.equals(gdCf.getIscd(), Constants.ISCD_POSITIVE)) {
                                    gdCf.setIscd(Constants.ISCD_NEGETIVE);
                                }
                                gdCf.setJfywh(bdcXm.getBh());
                                gdCf.setJfrq(jfdjsj);
                                gdCf.setJfdbr(userName);
                                String fj = gdCf.getFj();
                                fj = fj + "\n" + "由于办理司法裁定转移后失效";
                                gdCf.setFj(fj);
                                entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
                                gdFwService.changeGdqlztByQlid(gdCf.getCfid(), Constants.QLLX_QSZT_XS.toString());

                                // 生成项目关系便于共享推送状态，插入前检查，避免插重
                                if (!bdcXmRelService.existBdcXmRelByProidAndYproid(bdcXm.getProid(), gdCf.getProid())) {
                                    BdcXmRel bdcXmRelTemp = new BdcXmRel();
                                    bdcXmRelTemp.setRelid(UUIDGenerator.generate18());
                                    bdcXmRelTemp.setYdjxmly(Constants.XMLY_FWSP);
                                    bdcXmRelTemp.setYqlid(gdCf.getCfid());
                                    bdcXmRelTemp.setProid(bdcXm.getProid());
                                    bdcXmRelTemp.setYproid(gdCf.getProid());
                                    bdcXmRelService.saveBdcXmRel(bdcXmRelTemp);
                                }
                            }
                        }
                    }
                }
            }
            bdcZsCdService.cancelBdcZscdByBdcXm(bdcXm);
        }
    }

    @Override
    public void jfBdcCfByXf(BdcCf bdcCf) {
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getCflx()) && StringUtils.equals(bdcCf.getCflx(), Constants.CFLX_XF)) {
            BdcCf bdcXf = bdcCfMapper.selectCfByProid(bdcCf.getProid());
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcCf.getProid());
            if (CollectionUtils.isNotEmpty(bdcXmRelList) && bdcXf != null) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.equals(bdcXmRel.getYdjxmly(), Constants.XMLY_BDC)) {
                        BdcCf bdcCfTemp = bdcCfMapper.selectCfByProid(bdcXmRel.getYproid());
                        if (bdcCfTemp != null && bdcCfTemp.getQszt() == Constants.QLLX_QSZT_XS && StringUtils.equals(bdcCfTemp.getCflx(), Constants.CFLX_ZD_CF)) {
                            bdcCfTemp.setJfywh(bdcXf.getJfywh());
                            bdcCfTemp.setJfwh(bdcXf.getJfwh());
                            bdcCfTemp.setJfwj(bdcXf.getJfwj());
                            bdcCfTemp.setJfsj(bdcXf.getJfsj());
                            bdcCfTemp.setJfjg(bdcXf.getJfjg());
                            bdcCfTemp.setQszt(Constants.QLLX_QSZT_HR);
                            bdcCfTemp.setJfdbr(bdcXf.getJfdbr());
                            if (bdcXf.getJfdjsj() != null) {
                                bdcCfTemp.setJfdjsj(bdcXf.getJfdjsj());
                            }
                            saveBdcCf(bdcCfTemp);
                        }
                    } else {
                        List<GdCf> gdCfTempList = gdCfService.getGdCfListByQlid(bdcXmRel.getYqlid(), 0);
                        if (CollectionUtils.isNotEmpty(gdCfTempList)) {
                            GdCf gdCfTemp = gdCfTempList.get(0);
                            if (gdCfTemp != null && StringUtils.equals(gdCfTemp.getCflx(), Constants.CFLX_GD_XF)) {
                                gdCfTemp.setJfywh(bdcXf.getJfywh());
                                gdCfTemp.setJfwh(bdcXf.getJfwh());
                                gdCfTemp.setJfwj(bdcXf.getJfwj());
                                gdCfTemp.setJfrq(bdcXf.getJfsj());
                                gdCfTemp.setJfjg(bdcXf.getJfjg());
                                gdCfTemp.setIsjf(1);
                                gdCfTemp.setJfdbr(bdcXf.getJfdbr());
                                if (bdcXf.getJfdjsj() != null) {
                                    gdCfTemp.setJfdbsj(bdcXf.getJfdjsj());
                                }
                                entityMapper.saveOrUpdate(gdCfTemp, gdCfTemp.getCfid());
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public List<BdcCf> getBdcCfByCfwhAndBdcdyh(Map map) {
        return bdcCfMapper.getBdcCfByCfwhAndBdcdyh(map);
    }

    @Override
    @AuditLog(name = "查封失效", description = "BdcCf")
    public void bdcCfsx(BdcCf bdcCf) {
        String jfywh = createNewJfywh(bdcCf.getProid(), null);
        bdcCf.setIssx("1");
        bdcCf.setQszt(Constants.QLLX_QSZT_HR);
        bdcCf.setJfywh(jfywh);
        bdcCf.setJfdjsj(new Date());
        bdcCf.setJfsj(new Date());
        bdcCf.setQljssj(new Date());
        bdcCf.setJfyy(PARAMETER_CFSX_SDSX);
        bdcCf.setJfdbr(SessionUtil.getCurrentUser().getUsername());
        String fj = StringUtils.isNotBlank(bdcCf.getFj()) ? bdcCf.getFj() + PARAMETER_CFYSX : PARAMETER_CFYSX;
        bdcCf.setFj(fj);
        entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
        String bdcdyid = bdcCf.getBdcdyid();
        if (StringUtils.isNotBlank(bdcdyid)) {
            boolean cfHasXf = turnXfToCf(bdcCf, bdcdyid);
            if (!cfHasXf) {
                //轮候查封转查封
                turnLhcfToCf(bdcCf, bdcdyid, null);
            }
        }
    }

    @Override
    @AuditLog(name = "查封失效", description = "GdCf")
    public void gdCfsx(GdCf gdCf) {
        if (gdCf != null) {
            gdCf.setIssx("1");
            gdCf.setIsjf(1);
            gdCf.setJfr(SessionUtil.getCurrentUser().getUsername());
            gdCf.setJfrq(new Date());
            gdCf.setJfyy(PARAMETER_CFSX_SDSX);
            String fj = StringUtils.isNotBlank(gdCf.getFj()) ? gdCf.getFj() + PARAMETER_CFYSX : PARAMETER_CFYSX;
            gdCf.setFj(fj);
            entityMapper.saveOrUpdate(gdCf, gdCf.getCfid());
            gdFwService.changeGdqlztByQlid(gdCf.getCfid(), Constants.QLLX_QSZT_XS.toString());
            //轮候转查封
            checkGdCfHasLhcf(gdCf.getCfid());
        }

    }

    @Override
    public String getCfDjzxByProjectPar(ProjectPar projectPar, BdcXm ybdcXm) {
        String djzx = "";
        List<BdcCf> bdcCfList = null;
        List<GdCf> gdCfList = null;
        djzx = Constants.DJZX_CF;
        String sjppType = AppConfig.getProperty("sjpp.type");
        //解封的登记子项是解封
        if (StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_JF) || StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_PLJF)
                || StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_JF_NOQL) || StringUtils.equals(projectPar.getSqlx(), Constants.SQLX_JF_NOBDCDY)) {
            djzx = Constants.DJZX_JF;
        } else {
            //查封走新建
            if (StringUtils.equals(projectPar.getXmly(), Constants.XMLY_BDC)) {
                if (StringUtils.isNotBlank(projectPar.getQjid())) {
                    djzx = Constants.DJZX_YCF;
                    //jiangganzhi 关联附属设施 附属设施登记子项要跟随主房登记子项
                    List<BdcXm> bdcXmList = null;
                    HashMap map = new HashMap();
                    map.put("wiid", projectPar.getWiid());
                    bdcXmList = bdcXmService.getBdcXmList(map);
                    if (CollectionUtils.isNotEmpty(bdcXmList)) {
                        BdcXm bdcXm = bdcXmList.get(0);
                        if (StringUtils.isNotBlank(bdcXm.getDjzx()) && !StringUtils.equals(bdcXm.getDjzx(), Constants.DJZX_YCF)) {
                            djzx = bdcXm.getDjzx();
                        }
                    }
                    // 轮候预查封
                    if (StringUtils.isNotBlank(projectPar.getBdcdyid())) {
                        bdcCfList = getBdcCfList(projectPar.getBdcdyid(), Constants.CFLX_ZD_YCF, Constants.QLLX_QSZT_XS);
                    }
                    if (CollectionUtils.isNotEmpty(bdcCfList)) {
                        djzx = Constants.DJZX_LHYCF;
                    }
                } else {
                    if (ybdcXm != null) {
                        if (StringUtils.equals(ybdcXm.getQllx(), Constants.QLLX_CFDJ)) {
                            djzx = Constants.DJZX_XF;
                        } else if (StringUtils.isNotBlank(projectPar.getBdcdyh())) {
                            bdcCfList = getXsCfByBdcdyh(projectPar.getBdcdyh());
                            if (CollectionUtils.isNotEmpty(bdcCfList)) {
                                djzx = Constants.DJZX_LH;
                            }
                        }
                    }
                }
                //过度查封的登记子项
            }
        }
        return djzx;
    }

    @Override
    public boolean checkIsXf(BdcXm bdcXm) {
        if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getProid())) {
            BdcCf bdcCf = selectCfByProid(bdcXm.getProid());
            if (bdcCf != null && StringUtils.equals(bdcCf.getCflx(), Constants.CFLX_XF)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map changeLhcfToCf(String proid, String userid) {
        Map map = Maps.newHashMap();
        if (StringUtils.isNotBlank(proid)) {
            BdcCf bdcCf = selectCfByProid(proid);
            if (bdcCf != null && StringUtils.equals(bdcCf.getCflx(), Constants.CFLX_LHCF)) {
                StringBuilder fj = new StringBuilder();
                if (StringUtils.isNotBlank(bdcCf.getFj())) {
                    fj.append(bdcCf.getFj());
                }
                fj.append(CalendarUtil.formatDateToString(new Date())).append(LHCF_CF_FJ_ZJNR);
                bdcCf.setCflx(Constants.CFLX_ZD_CF);
                bdcCf.setFj(fj.toString());
                bdcCf.setCfksqx(DateUtils.now());
                bdcCf.setCfjsqx(CalendarUtil.addYears(bdcCf.getCfksqx(), 3));
                entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
                //日志记录
                BdcXtLog bdcXtLog = new BdcXtLog();
                bdcXtLog.setLogid(UUIDGenerator.generate18());
                bdcXtLog.setController("查询调用登记接口，进行轮候查封转查封操作");
                if (StringUtils.isNotBlank(userid)) {
                    bdcXtLog.setUserid(userid);
                    bdcXtLog.setUsername(PlatformUtil.getCurrentUserName(userid));
                }
                bdcXtLog.setCzrq(new Date());
                bdcXtLog.setWiid(proid);
                bdcXtLog.setParmjson(JSON.toJSONString(bdcCf));
                entityMapper.saveOrUpdate(bdcXtLog, bdcXtLog.getLogid());
                map.put(ParamsConstants.MSG_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
            } else {
                map.put(ParamsConstants.MSG_LOWERCASE, "未找到该轮候查封");
            }
        } else {
            map.put(ParamsConstants.MSG_LOWERCASE, "proid为空");
        }
        return map;
    }

    @Override
    public Map changeCfxx(String ip, BdcCf bdcCf) {
        Map map = Maps.newHashMap();
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getQlid())) {
            String userid = PlatformUtil.getCurrentUserId();
            StringBuilder fj = new StringBuilder();
            if (StringUtils.isNotBlank(bdcCf.getFj())) {
                fj.append(bdcCf.getFj());
            }
            fj.append(CalendarUtil.formatDateToString(new Date())).append("查询台账修改查封信息");
            bdcCf.setFj(fj.toString());
            entityMapper.saveOrUpdate(bdcCf, bdcCf.getQlid());
            if (bdcCf.getCfksqx() == null || bdcCf.getCfjsqx() == null) {
                bdcCfMapper.updatCfsjForNull(bdcCf);
            }
            //日志记录
            BdcXtLog bdcXtLog = new BdcXtLog();
            bdcXtLog.setLogid(UUIDGenerator.generate18());
            bdcXtLog.setController("查询调用登记接口,修改查封信息");
            if (StringUtils.isNotBlank(userid)) {
                bdcXtLog.setUserid(userid);
                bdcXtLog.setUsername(PlatformUtil.getCurrentUserName(userid));
            }
            bdcXtLog.setIp(ip);
            bdcXtLog.setCzrq(new Date());
            bdcXtLog.setWiid(bdcCf.getProid());
            bdcXtLog.setParmjson(JSON.toJSONString(bdcCf));
            entityMapper.saveOrUpdate(bdcXtLog, bdcXtLog.getLogid());
            map.put(ParamsConstants.MSG_LOWERCASE, "修改成功");
        } else {
            map.put(ParamsConstants.MSG_LOWERCASE, "不动产查封为空");
        }
        return map;
    }

    /**
     * 过度查封轮候查封转查封
     *
     * @param cfid
     */
    public void checkGdCfHasLhcf(String cfid) {
        List<GdBdcQlRel> gdBdcQlRelTempList = gdBdcQlRelService.queryGdBdcQlListByQlid(cfid);
        if (CollectionUtils.isNotEmpty(gdBdcQlRelTempList)) {
            GdBdcQlRel gdBdcQlRel = gdBdcQlRelTempList.get(0);
            if (gdBdcQlRel != null) {
                List<BdcGdDyhRel> bdcGdDyhRelList = bdcGdDyhRelService.getGdDyhRelByGdId(gdBdcQlRel.getBdcid());
                if (CollectionUtils.isNotEmpty(bdcGdDyhRelList)) {
                    BdcGdDyhRel bdcGddyhRel = bdcGdDyhRelList.get(0);
                    if (bdcGddyhRel != null) {
                        String bdcdyid = bdcdyService.getBdcdyidByBdcdyh(bdcGddyhRel.getBdcdyh());
                        boolean cfHasXf = turnXfToCf(null, bdcdyid);
                        if (!cfHasXf) {
                            //轮候查封转查封
                            turnLhcfToCf(null, bdcdyid, null);
                        }
                    }
                }
            }
        }
    }


    /**
     * @param bdcXm
     * @param bdcCf
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 判断查封是否是针对当前产权项目
     */
    private Boolean judgeBdcCfForCurrentProject(BdcXm bdcXm, BdcCf bdcCf) {
        if (bdcXm != null && bdcCf != null) {
            List<BdcXmRel> bdcXmRelList = bdcXmRelService.getHisBdcXmRelByProid(bdcCf.getProid());
            if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                for (BdcXmRel bdcXmRel : bdcXmRelList) {
                    if (StringUtils.equals(bdcXmRel.getProid(), bdcXm.getProid())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
