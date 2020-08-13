package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.utils.CalendarUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijian@gtmap.cn">lijian</a>
 * @version 1.0, 2016/7/27
 */
@Service
public class GlZsServiceImpl implements GlZsService {
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcXmRelService bdcXmRelService;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private GdQlrService gdQlrService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcZsService bdcZsService;


    @Override
    public void saveTdxxToBdcFdcq(List<GdTdsyq> gdTdsyqList, List<GdTd> gdTdList, BdcXm bdcXm) {
        //继承证书信息，后续可补充
        QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
        //jiangganzhi 苏州新建商品房流程 不需要对产权中土地相关信息赋值
        if (qllxVo instanceof BdcFdcq && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_PLFZ_DM) && !StringUtils.equals(bdcXm.getSqlx(), Constants.SQLX_GJPTSCDJ_DM)) {
            BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
            if (CollectionUtils.isNotEmpty(gdTdsyqList)) {
                GdTdsyq gdTdsyq = gdTdsyqList.get(0);
                bdcFdcq.setDytdmj(gdTdsyq.getDymj());
                bdcFdcq.setFttdmj(gdTdsyq.getFtmj());
            }
            if (CollectionUtils.isNotEmpty(gdTdList)) {
                GdTd gdTd = gdTdList.get(0);
                bdcFdcq.setTdsyksqx(CalendarUtil.formatDate(gdTd.getQsrq()));
                bdcFdcq.setTdsyjsqx(CalendarUtil.formatDate(gdTd.getZzrq()));
            }
            entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
        }
    }

    @Override
    public void saveDyxxToBdcDyaq(List<GdDy> gdDyList, List<GdTd> gdTdList, BdcXm bdcXm) {
        //继承证书信息，后续可补充
        QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
        if (qllxVo instanceof BdcDyaq) {
            BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
            if (CollectionUtils.isNotEmpty(gdDyList)) {
                GdDy gdDy = gdDyList.get(0);
                bdcDyaq.setZwlxksqx(gdDy.getDyksrq());
                bdcDyaq.setZwlxjsqx(gdDy.getDyjsrq());
                bdcDyaq.setBdbzzqse(gdDy.getBdbzzqse());
            }
            entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
        }
    }

    @Override
    public void saveZsQlr(BdcXm bdcXm, String xmly, String yproid, String yqlid) {
        //读取原证的权利人
        if (StringUtils.equals(xmly, Constants.XMLY_BDC)) {
            String sqlx = bdcXm.getSqlx();
            List<BdcQlr> oldBdcQlrLst = bdcQlrService.queryBdcQlrByProid(yproid);
            if (StringUtils.isNotBlank(sqlx) && (StringUtils.equals(sqlx, Constants.SQLX_FWFGHBBG_DM) || StringUtils.equals(sqlx, Constants.SQLX_TDFGHBBG_DM))) {
                List<BdcQlr> newBdcQlrLst = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                List<BdcQlr> newBdcYwrLst = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(oldBdcQlrLst)) {
                    for (BdcQlr oldBdcQlr : oldBdcQlrLst) {
                        if (CollectionUtils.isNotEmpty(newBdcQlrLst)) {
                            boolean isSaveQlr = true;
                            for (BdcQlr newQlr : newBdcQlrLst) {
                                //通过权利人名称比对
                                if (StringUtils.equals(newQlr.getQlrmc(), oldBdcQlr.getQlrmc())) {
                                    isSaveQlr = false;
                                    break;
                                }
                            }
                            if (isSaveQlr) {
                                oldBdcQlr.setQlrid(UUIDGenerator.generate());
                                oldBdcQlr.setProid(bdcXm.getProid());
                                entityMapper.saveOrUpdate(oldBdcQlr, oldBdcQlr.getQlrid());
                            }
                        } else {
                            oldBdcQlr.setQlrid(UUIDGenerator.generate());
                            oldBdcQlr.setProid(bdcXm.getProid());
                            entityMapper.saveOrUpdate(oldBdcQlr, oldBdcQlr.getQlrid());
                        }
                        /**
                         *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                         *@description 12782 不动产证做分割（合并）变更登记时当前项目的义务人信息也读取上一手的权利人信息
                         */
                        if (CollectionUtils.isNotEmpty(newBdcYwrLst)) {
                            boolean isSaveQlr = true;
                            for (BdcQlr newQlr : newBdcYwrLst) {
                                //通过权利人名称比对
                                if (StringUtils.equals(newQlr.getQlrmc(), oldBdcQlr.getQlrmc())) {
                                    isSaveQlr = false;
                                    break;
                                }
                            }
                            if (isSaveQlr) {
                                oldBdcQlr.setQlrid(UUIDGenerator.generate());
                                oldBdcQlr.setProid(bdcXm.getProid());
                                oldBdcQlr.setQlrlx(Constants.QLRLX_YWR);
                                entityMapper.saveOrUpdate(oldBdcQlr, oldBdcQlr.getQlrid());
                            }
                        } else {
                            oldBdcQlr.setQlrid(UUIDGenerator.generate());
                            oldBdcQlr.setProid(bdcXm.getProid());
                            oldBdcQlr.setQlrlx(Constants.QLRLX_YWR);
                            entityMapper.saveOrUpdate(oldBdcQlr, oldBdcQlr.getQlrid());
                        }
                    }
                }
            } else if ((StringUtils.isNotBlank(sqlx) && (StringUtils.equals(sqlx, Constants.SQLX_FWFGHBZY_DM) || StringUtils.equals(sqlx, Constants.SQLX_TDFGHBZY_DM)))) {
                List<BdcQlr> newBdcQlrLst = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(oldBdcQlrLst)) {
                    for (BdcQlr oldBdcQlr : oldBdcQlrLst) {
                        if (CollectionUtils.isNotEmpty(newBdcQlrLst)) {
                            boolean isSaveQlr = true;
                            for (BdcQlr newQlr : newBdcQlrLst) {
                                //通过权利人名称比对
                                if (StringUtils.equals(newQlr.getQlrmc(), oldBdcQlr.getQlrmc())) {
                                    isSaveQlr = false;
                                    break;
                                }
                            }
                            if (isSaveQlr) {
                                oldBdcQlr.setQlrid(UUIDGenerator.generate());
                                oldBdcQlr.setProid(bdcXm.getProid());
                                oldBdcQlr.setQlrlx(Constants.QLRLX_YWR);
                                entityMapper.saveOrUpdate(oldBdcQlr, oldBdcQlr.getQlrid());
                            }
                        } else {
                            oldBdcQlr.setQlrid(UUIDGenerator.generate());
                            oldBdcQlr.setProid(bdcXm.getProid());
                            oldBdcQlr.setQlrlx(Constants.QLRLX_YWR);
                            entityMapper.saveOrUpdate(oldBdcQlr, oldBdcQlr.getQlrid());
                        }
                    }
                }
            }
        } else {
            String sqlx = bdcXm.getSqlx();
            List<GdQlr> oldGdQlrLst = gdQlrService.queryGdQlrListByProid(yproid, Constants.QLRLX_QLR);
            //通过proid查不到时通过QLID查找
            if (CollectionUtils.isEmpty(oldGdQlrLst)) {
                oldGdQlrLst = gdQlrService.queryGdQlrs(yqlid, Constants.QLRLX_QLR);
            }

            if (StringUtils.isNotBlank(sqlx) && (StringUtils.equals(sqlx, Constants.SQLX_FWFGHBBG_DM) || StringUtils.equals(sqlx, Constants.SQLX_TDFGHBBG_DM))) {
                List<BdcQlr> newBdcQlrLst = bdcQlrService.queryBdcQlrByProid(bdcXm.getProid());
                List<BdcQlr> newBdcYwrLst = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(oldGdQlrLst)) {
                    for (GdQlr oldGdQlr : oldGdQlrLst) {
                        if (CollectionUtils.isNotEmpty(newBdcQlrLst)) {
                            boolean isSaveQlr = true;
                            for (BdcQlr newQlr : newBdcQlrLst) {
                                //通过权利人名称比对
                                if (StringUtils.equals(newQlr.getQlrmc(), oldGdQlr.getQlr())) {
                                    isSaveQlr = false;
                                    break;
                                }
                            }
                            if (isSaveQlr) {
                                BdcQlr bdcQlr = new BdcQlr();
                                bdcQlr.setQlrid(UUIDGenerator.generate());
                                bdcQlr.setProid(bdcXm.getProid());
                                bdcQlr.setQlrmc(oldGdQlr.getQlr());
                                bdcQlr.setQlrzjh(oldGdQlr.getQlrzjh());
                                bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                                bdcQlr.setQlrsfzjzl(oldGdQlr.getQlrsfzjzl());
                                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                            }
                        } else {
                            BdcQlr bdcQlr = new BdcQlr();
                            bdcQlr.setQlrid(UUIDGenerator.generate());
                            bdcQlr.setProid(bdcXm.getProid());
                            bdcQlr.setQlrmc(oldGdQlr.getQlr());
                            bdcQlr.setQlrzjh(oldGdQlr.getQlrzjh());
                            bdcQlr.setQlrlx(Constants.QLRLX_QLR);
                            if (!NumberUtils.isNumber(oldGdQlr.getQlrsfzjzl()) && StringUtils.isNotBlank(oldGdQlr.getQlrsfzjzl())) {
                                Example example = new Example(BdcZdZjlx.class);
                                example.createCriteria().andEqualTo("mc", oldGdQlr.getQlrsfzjzl());
                                List<BdcZdZjlx> list = entityMapper.selectByExample(BdcZdZjlx.class, example);
                                if (list != null) {
                                    bdcQlr.setQlrsfzjzl(list.get(0).getDm());
                                }
                            } else {
                                bdcQlr.setQlrsfzjzl(oldGdQlr.getQlrsfzjzl());
                            }
                            entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                        }
                        /**
                         *@author <a href="mailto:yanzhenkun@gtmap.cn">yanzhenkun</a>
                         *@description 12782 不动产证做分割（合并）变更登记时当前项目的义务人信息也读取上一手的权利人信息
                         */
                        if (CollectionUtils.isNotEmpty(newBdcYwrLst)) {
                            boolean isSaveQlr = true;
                            for (BdcQlr newQlr : newBdcYwrLst) {
                                //通过权利人名称比对
                                if (StringUtils.equals(newQlr.getQlrmc(), oldGdQlr.getQlr())) {
                                    isSaveQlr = false;
                                    break;
                                }
                            }
                            if (isSaveQlr) {
                                BdcQlr bdcQlr = new BdcQlr();
                                bdcQlr.setQlrid(UUIDGenerator.generate());
                                bdcQlr.setProid(bdcXm.getProid());
                                bdcQlr.setQlrmc(oldGdQlr.getQlr());
                                bdcQlr.setQlrzjh(oldGdQlr.getQlrzjh());
                                bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                                bdcQlr.setQlrsfzjzl(oldGdQlr.getQlrsfzjzl());
                                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                            }
                        } else {
                            BdcQlr bdcQlr = new BdcQlr();
                            bdcQlr.setQlrid(UUIDGenerator.generate());
                            bdcQlr.setProid(bdcXm.getProid());
                            bdcQlr.setQlrmc(oldGdQlr.getQlr());
                            bdcQlr.setQlrzjh(oldGdQlr.getQlrzjh());
                            bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                            bdcQlr.setQlrsfzjzl(oldGdQlr.getQlrsfzjzl());
                            entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                        }
                    }
                }
            } else if ((StringUtils.isNotBlank(sqlx) && (StringUtils.equals(sqlx, Constants.SQLX_FWFGHBZY_DM) || StringUtils.equals(sqlx, Constants.SQLX_TDFGHBZY_DM)))) {
                List<BdcQlr> newBdcQlrLst = bdcQlrService.queryBdcYwrByProid(bdcXm.getProid());
                if (CollectionUtils.isNotEmpty(oldGdQlrLst)) {
                    for (GdQlr oldGdQlr : oldGdQlrLst) {
                        if (CollectionUtils.isNotEmpty(newBdcQlrLst)) {
                            boolean isSaveQlr = true;
                            for (BdcQlr newQlr : newBdcQlrLst) {
                                //通过权利人名称比对
                                if (StringUtils.equals(newQlr.getQlrmc(), oldGdQlr.getQlr())) {
                                    isSaveQlr = false;
                                    break;
                                }
                            }
                            if (isSaveQlr) {
                                BdcQlr bdcQlr = new BdcQlr();
                                bdcQlr.setQlrid(UUIDGenerator.generate());
                                bdcQlr.setProid(bdcXm.getProid());
                                bdcQlr.setQlrmc(oldGdQlr.getQlr());
                                bdcQlr.setQlrzjh(oldGdQlr.getQlrzjh());
                                bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                                bdcQlr.setQlrsfzjzl(oldGdQlr.getQlrsfzjzl());
                                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                            }
                        } else {
                            BdcQlr bdcQlr = new BdcQlr();
                            bdcQlr.setQlrid(UUIDGenerator.generate());
                            bdcQlr.setProid(bdcXm.getProid());
                            bdcQlr.setQlrmc(oldGdQlr.getQlr());
                            bdcQlr.setQlrzjh(oldGdQlr.getQlrzjh());
                            bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                            bdcQlr.setQlrsfzjzl(oldGdQlr.getQlrsfzjzl());
                            entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void saveFwxxToBdcFdcq(List<GdFwsyq> gdFwsyqList, List<GdFw> gdFwList, BdcXm bdcXm) {
        //继承证书信息，后续可补充
        QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
        if (qllxVo instanceof BdcFdcq) {
            BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
            if (CollectionUtils.isNotEmpty(gdFwsyqList)) {
                GdFwsyq gdFwsyq = gdFwsyqList.get(0);
                bdcFdcq.setFttdmj(gdFwsyq.getFttdmj());
                bdcFdcq.setDytdmj(gdFwsyq.getDytdmj());
            }
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                GdFw gdFw = gdFwList.get(0);
                bdcFdcq.setJzmj(gdFw.getJzmj());
                bdcFdcq.setScmj(gdFw.getScmj());
            }
            entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
        }
    }

    @Override
    public void addFsssxx2BdcFdcq(List<GdFwsyq> gdFwsyqList, List<GdFw> gdFwList, BdcXm bdcXm) {
        //继承证书信息，后续可补充
        QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
        if (qllxVo instanceof BdcFdcq) {
            BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
            if (CollectionUtils.isNotEmpty(gdFwList)) {
                GdFw gdFw = gdFwList.get(0);
                double jzmj = CommonUtil.formatObjectToDouble(bdcFdcq.getJzmj()) + CommonUtil.formatObjectToDouble(gdFw.getJzmj());
                bdcFdcq.setJzmj(CommonUtil.formatTwoNumber(jzmj));
                double scmj = CommonUtil.formatObjectToDouble(bdcFdcq.getScmj()) + CommonUtil.formatObjectToDouble(gdFw.getScmj());
                bdcFdcq.setScmj(CommonUtil.formatTwoNumber(scmj));
                double tnjzmj = CommonUtil.formatObjectToDouble(bdcFdcq.getTnjzmj()) + CommonUtil.formatObjectToDouble(gdFw.getTnjzmj());
                bdcFdcq.setTnjzmj(tnjzmj);
                double ftjzmj = CommonUtil.formatObjectToDouble(bdcFdcq.getFtjzmj()) + CommonUtil.formatObjectToDouble(gdFw.getFtjzmj());
                bdcFdcq.setFtjzmj(ftjzmj);
            }
            entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
        }
    }

    @Override
    public void saveTdqlxx(BdcXm bdcXm, String yproid, String yqlid) {
        BdcSpxx ybdcSpxx = bdcSpxxService.queryBdcSpxxByProid(yproid);
        BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
        if (bdcSpxx != null && ybdcSpxx != null) {
            if (StringUtils.isBlank(bdcSpxx.getZdzhyt()) && StringUtils.isNotBlank(ybdcSpxx.getZdzhyt())) {
                bdcSpxx.setZdzhyt(ybdcSpxx.getZdzhyt());
            }
            if (StringUtils.isBlank(bdcSpxx.getZdzhqlxz()) && StringUtils.isNotBlank(ybdcSpxx.getZdzhqlxz())) {
                bdcSpxx.setZdzhqlxz(ybdcSpxx.getZdzhqlxz());
            }
            if (bdcSpxx.getZdzhmj() != null && ybdcSpxx.getZdzhmj() != null) {
                bdcSpxx.setZdzhmj(ybdcSpxx.getZdzhmj());
            }
            entityMapper.saveOrUpdate(bdcSpxx, bdcSpxx.getSpxxid());
        }

        QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
        if (qllxVo != null) {
            BdcJsydzjdsyq bdcJsydzjdsyq = new BdcJsydzjdsyq();
            BdcXm ybdcXm = bdcXmService.getBdcXmByProid(yproid);
            QllxVo yqllxVo = qllxService.queryQllxVo(ybdcXm);
            if (yqllxVo instanceof BdcJsydzjdsyq) {
                bdcJsydzjdsyq = (BdcJsydzjdsyq) yqllxVo;
            }

            if (qllxVo instanceof BdcFdcq) {
                BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                if (bdcFdcq.getTdsyksqx() == null && bdcJsydzjdsyq.getSyksqx() != null) {
                    bdcFdcq.setTdsyksqx(bdcJsydzjdsyq.getSyksqx());
                }
                if (bdcFdcq.getTdsyjsqx() == null && bdcJsydzjdsyq.getSyjsqx() != null) {
                    bdcFdcq.setTdsyjsqx(bdcJsydzjdsyq.getSyjsqx());
                }
                if (bdcFdcq.getDytdmj() == null && bdcJsydzjdsyq.getDytdmj() != null) {
                    bdcFdcq.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                }
                if (bdcFdcq.getFttdmj() == null && bdcJsydzjdsyq.getFttdmj() != null) {
                    bdcFdcq.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                }
                entityMapper.saveOrUpdate(bdcFdcq, bdcFdcq.getQlid());
            } else if (qllxVo instanceof BdcFdcqDz) {
                BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                if (bdcFdcqDz.getTdsyksqx() == null && bdcJsydzjdsyq.getSyksqx() != null) {
                    bdcFdcqDz.setTdsyksqx(bdcJsydzjdsyq.getSyksqx());
                }
                if (bdcFdcqDz.getTdsyjsqx() == null && bdcJsydzjdsyq.getSyjsqx() != null) {
                    bdcFdcqDz.setTdsyjsqx(bdcJsydzjdsyq.getSyjsqx());
                }
                if (bdcFdcqDz.getDytdmj() == null && bdcJsydzjdsyq.getDytdmj() != null) {
                    bdcFdcqDz.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                }
                if (bdcFdcqDz.getFttdmj() == null && bdcJsydzjdsyq.getFttdmj() != null) {
                    bdcFdcqDz.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                }
                entityMapper.saveOrUpdate(bdcFdcqDz, bdcFdcqDz.getQlid());
            }
        }
    }

    /**
     * @param qlids 权利ID
     * @param wiid
     * @param proid
     * @return wiid 工作流ID
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 关联土地证
     */
    @Override
    public void gltdz(String qlids, String wiid, String proid) {
        String[] qlidStr = qlids.split(",");
        if ((StringUtils.isNotBlank(wiid) || StringUtils.isNotBlank(proid)) && qlidStr != null && qlidStr.length > 0) {
            Map<String, BdcJsydzjdsyq> bdcJsydzjdsyqMap = Maps.newHashMap();
            for (String qlid : qlidStr) {
                BdcJsydzjdsyq bdcJsydzjdsyq = entityMapper.selectByPrimaryKey(BdcJsydzjdsyq.class, qlid);
                if (bdcJsydzjdsyq != null) {
                    bdcJsydzjdsyqMap.put(bdcJsydzjdsyq.getProid(), bdcJsydzjdsyq);
                }
            }
            List<BdcXm> bdcXmList = new ArrayList<>();
            if (StringUtils.isNotBlank(proid)) {
                BdcXm bdcXmByProid = bdcXmService.getBdcXmByProid(proid);
                bdcXmList.add(bdcXmByProid);
            } else {
                bdcXmList = bdcXmService.getBdcXmListByWiid(wiid);
            }
            if (CollectionUtils.isNotEmpty(bdcXmList)) {
                for (BdcXm bdcXm : bdcXmList) {
                    boolean sfgl = true;
                    List<BdcXmRel> bdcXmRelList = bdcXmRelService.queryBdcXmRelByProid(bdcXm.getProid());
                    if (CollectionUtils.isNotEmpty(bdcXmRelList)) {
                        for (BdcXmRel bdcXmRel : bdcXmRelList) {
                            if (bdcJsydzjdsyqMap.containsKey(bdcXmRel.getYproid())) {
                                bdcJsydzjdsyqMap.remove(bdcXmRel.getYproid());
                                sfgl = false;
                                break;
                            }
                        }
                    }
                    if (sfgl && MapUtils.isNotEmpty(bdcJsydzjdsyqMap)) {
                        for (String key : bdcJsydzjdsyqMap.keySet()) {
                            BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqMap.get(key);
                            if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_GYTD_FWSUQ)) {
                                String tdzh = bdcZsService.getTdzhByQlid(bdcJsydzjdsyq.getQlid());
                                if (StringUtils.isNotBlank(tdzh)) {
                                    if (StringUtils.isBlank(bdcXm.getYbdcqzh())) {
                                        bdcXm.setYbdcqzh(tdzh);
                                    } else {
                                        if (!(StringUtils.indexOf(bdcXm.getYbdcqzh(), tdzh) > -1)) {
                                            bdcXm.setYbdcqzh(bdcXm.getYbdcqzh() + "," + tdzh);
                                        }
                                    }
                                    bdcXmService.saveBdcXm(bdcXm);
                                }
                                QllxVo qllxVo = qllxService.queryQllxVo(bdcXm);
                                if (qllxVo != null && qllxVo instanceof BdcFdcq) {
                                    BdcFdcq bdcFdcq = (BdcFdcq) qllxVo;
                                    gltdzInitXmRel(bdcXm.getProid(), bdcJsydzjdsyq.getProid());
                                    if ((bdcFdcq.getFttdmj() == null || bdcFdcq.getFttdmj() == 0) && (bdcJsydzjdsyq.getFttdmj() != null && bdcJsydzjdsyq.getFttdmj() > 0)) {
                                        bdcFdcq.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                                    }
                                    if ((bdcFdcq.getDytdmj() == null || bdcFdcq.getDytdmj() == 0) && (bdcJsydzjdsyq.getDytdmj() != null && bdcJsydzjdsyq.getDytdmj() > 0)) {
                                        bdcFdcq.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                                    }
                                    if (bdcFdcq.getTdsyksqx() == null && bdcJsydzjdsyq.getSyksqx() != null) {
                                        bdcFdcq.setTdsyksqx(bdcJsydzjdsyq.getSyksqx());
                                    }
                                    if (bdcFdcq.getTdsyjsqx() == null && bdcJsydzjdsyq.getSyjsqx() != null) {
                                        bdcFdcq.setTdsyjsqx(bdcJsydzjdsyq.getSyjsqx());
                                    }
                                    qllxService.saveQllxVo(bdcFdcq);
                                }
                                if (qllxVo != null && qllxVo instanceof BdcFdcqDz) {
                                    BdcFdcqDz bdcFdcqDz = (BdcFdcqDz) qllxVo;
                                    gltdzInitXmRel(bdcXm.getProid(), bdcJsydzjdsyq.getProid());
                                    if ((bdcFdcqDz.getFttdmj() == null || bdcFdcqDz.getFttdmj() == 0) && (bdcJsydzjdsyq.getFttdmj() != null && bdcJsydzjdsyq.getFttdmj() > 0)) {
                                        bdcFdcqDz.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                                    }
                                    if ((bdcFdcqDz.getDytdmj() == null || bdcFdcqDz.getDytdmj() == 0) && (bdcJsydzjdsyq.getDytdmj() != null && bdcJsydzjdsyq.getDytdmj() > 0)) {
                                        bdcFdcqDz.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                                    }
                                    if (bdcFdcqDz.getTdsyksqx() == null && bdcJsydzjdsyq.getSyksqx() != null) {
                                        bdcFdcqDz.setTdsyksqx(bdcJsydzjdsyq.getSyksqx());
                                    }
                                    if (bdcFdcqDz.getTdsyjsqx() == null && bdcJsydzjdsyq.getSyjsqx() != null) {
                                        bdcFdcqDz.setTdsyjsqx(bdcJsydzjdsyq.getSyjsqx());
                                    }
                                    qllxService.saveQllxVo(bdcFdcqDz);
                                }
                                if (qllxVo != null && qllxVo instanceof BdcDyaq) {
                                    BdcDyaq bdcDyaq = (BdcDyaq) qllxVo;
                                    gltdzInitXmRel(bdcXm.getProid(), bdcJsydzjdsyq.getProid());
                                    if ((bdcDyaq.getFttdmj() == null || bdcDyaq.getFttdmj() == 0) && (bdcJsydzjdsyq.getFttdmj() != null && bdcJsydzjdsyq.getFttdmj() > 0)) {
                                        bdcDyaq.setFttdmj(bdcJsydzjdsyq.getFttdmj());
                                    }
                                    if ((bdcDyaq.getDytdmj() == null || bdcDyaq.getDytdmj() == 0) && (bdcJsydzjdsyq.getDytdmj() != null && bdcJsydzjdsyq.getDytdmj() > 0)) {
                                        bdcDyaq.setDytdmj(bdcJsydzjdsyq.getDytdmj());
                                    }
                                    qllxService.saveQllxVo(bdcDyaq);
                                }
                            } else if (StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_GYTD_JSYDSYQ) || StringUtils.equals(bdcXm.getQllx(), Constants.QLLX_JTTD_JSYDSYQ)) {
                                String tdzh = bdcZsService.getTdzhByQlid(bdcJsydzjdsyq.getQlid());
                                if (StringUtils.isNotBlank(tdzh)) {
                                    if (StringUtils.isBlank(bdcXm.getYbdcqzh())) {
                                        bdcXm.setYbdcqzh(tdzh);
                                    } else {
                                        if (!(StringUtils.indexOf(bdcXm.getYbdcqzh(), tdzh) > -1)) {
                                            bdcXm.setYbdcqzh(bdcXm.getYbdcqzh() + "," + tdzh);
                                        }
                                    }
                                    bdcXmService.saveBdcXm(bdcXm);
                                }
                                saveYqlrToYwr(bdcJsydzjdsyq.getProid(), bdcXm.getProid());
                                gltdzInitXmRel(bdcXm.getProid(), bdcJsydzjdsyq.getProid());
                            }
                            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcXm.getProid());
                            BdcSpxx yBdcSpxx = bdcSpxxService.queryBdcSpxxByProid(bdcJsydzjdsyq.getProid());
                            if (bdcSpxx != null && yBdcSpxx != null) {
                                if ((bdcSpxx.getZdzhmj() == null || bdcSpxx.getZdzhmj() == 0) && (yBdcSpxx.getZdzhmj() != null && yBdcSpxx.getZdzhmj() > 0)) {
                                    bdcSpxx.setZdzhmj(yBdcSpxx.getZdzhmj());
                                }
                                if (StringUtils.isBlank(bdcSpxx.getZdzhyt()) && StringUtils.isNotBlank(yBdcSpxx.getZdzhyt())) {
                                    bdcSpxx.setZdzhyt(yBdcSpxx.getZdzhyt());
                                }
                                if (StringUtils.isBlank(bdcSpxx.getZdzhqlxz()) && StringUtils.isNotBlank(yBdcSpxx.getZdzhqlxz())) {
                                    bdcSpxx.setZdzhqlxz(yBdcSpxx.getZdzhqlxz());
                                }
                                bdcSpxxService.saveBdcSpxx(bdcSpxx);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
     * @Time 2020/5/21 11:38
     * @description 将原权利人保存到义务人
     */
    private void saveYqlrToYwr(String yproid, String proid) {
        List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(yproid);
        List<BdcQlr> bdcYwrList = bdcQlrService.queryBdcYwrByProid(proid);
        if (CollectionUtils.isNotEmpty(bdcQlrList)) {
            if (CollectionUtils.isNotEmpty(bdcYwrList)) {
                Iterator<BdcQlr> iterator = bdcQlrList.iterator();
                while (iterator.hasNext()) {
                    BdcQlr nextQlr = iterator.next();
                    for (BdcQlr bdcYwr : bdcYwrList) {
                        if (StringUtils.equals(nextQlr.getQlrmc(), bdcYwr.getQlrmc())) {
                            iterator.remove();
                        }
                    }
                }
            }

            for (BdcQlr bdcQlr : bdcQlrList) {
                bdcQlr.setQlrid(UUIDGenerator.generate18());
                bdcQlr.setQlrlx(Constants.QLRLX_YWR);
                bdcQlr.setProid(proid);
                entityMapper.saveOrUpdate(bdcQlr, bdcQlr.getQlrid());
            }
        }
    }

    private void gltdzInitXmRel(String proid, String yproid) {
        BdcXmRel bdcXmRel = new BdcXmRel();
        bdcXmRel.setRelid(UUIDGenerator.generate18());
        bdcXmRel.setProid(proid);
        bdcXmRel.setYproid(yproid);
        bdcXmRelService.saveBdcXmRel(bdcXmRel);
    }
}
