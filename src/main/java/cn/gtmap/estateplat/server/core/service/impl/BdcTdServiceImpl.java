package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.mapper.BdcTdMapper;
import cn.gtmap.estateplat.server.core.mapper.DjsjFwMapper;
import cn.gtmap.estateplat.server.core.service.*;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ReadXmlProps;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author zzhw
 * @version V1.0, 15-3-18
 */
@Repository
public class BdcTdServiceImpl implements BdcTdService {

    @Autowired
    private BdcTdMapper bdcTdMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private QllxService qllxService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private BdcJsydzjdsyqService bdcJsydzjdsyqService;
    @Autowired
    private BdcQlrService bdcQlrService;
    @Autowired
    private BdcDyaqService bdcDyaqService;
    @Autowired
    private DjsjFwMapper djsjFwMapper;
    @Autowired
    private BdcDjsjService bdcDjsjService;
    @Autowired
    private BdcFdcqService bdcFdcqService;
    @Autowired
    private BdcFdcqDzService bdcFdcqDzService;
    @Autowired
    private BdcSpxxService bdcSpxxService;
    @Autowired
    private DjsjFwService djsjFwService;

    @Override
    @Transactional(readOnly = true)
    public BdcTd selectBdcTd(final String zdzhh) {
        return bdcTdMapper.selectBdcTd(zdzhh);
    }

    @Override
    public BdcTd getBdcTdFromZdxx(final DjsjZdxx djsjZdxx, final Project project, BdcTd bdcTd) {
        if (bdcTd == null) {
            bdcTd = new BdcTd();
        }
        if (StringUtils.isBlank(bdcTd.getTdid())) {
            bdcTd.setTdid(UUIDGenerator.generate18());
        }
        if (djsjZdxx == null) {
            return bdcTd;
        }
        bdcTd.setZdzhh(project.getZdzhh());
        bdcTd.setQlxz(djsjZdxx.getSyqlx());
        if (djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() != 0) {
            bdcTd.setZdmj(djsjZdxx.getFzmj());
        } else {
            bdcTd.setZdmj(djsjZdxx.getScmj());
        }
        bdcTd.setYt(djsjZdxx.getTdyt());
        bdcTd.setQlsdfs(djsjZdxx.getQlsdfs());
        bdcTd.setQllx(djsjZdxx.getQsxz());
        bdcTd.setRjl(djsjZdxx.getJzrjl());
        bdcTd.setJzmd(djsjZdxx.getJzmd());
        bdcTd.setJzxg(djsjZdxx.getJzxg());
        bdcTd.setJg(djsjZdxx.getQdjg());
        bdcTd.setDj(djsjZdxx.getTdjb());
        //zdd 地籍没有单位  默认就是平方米
        bdcTd.setMjdw(Constants.DW_PFM);
        return bdcTd;
    }

    @Override
    public BdcTd getBdcTdFromQsZdxx(final List<DjsjQszdDcb> djsjQszdDcbList, final Project project, BdcTd bdcTd, final String qllx) {
        if (bdcTd == null) {
            bdcTd = new BdcTd();
        }
        if (StringUtils.isBlank(bdcTd.getTdid())) {
            bdcTd.setTdid(UUIDGenerator.generate18());
        }
        if (CollectionUtils.isEmpty(djsjQszdDcbList)) {
            return bdcTd;
        }
        DjsjQszdDcb djsjQszdDcb = djsjQszdDcbList.get(0);
        if (djsjQszdDcb != null) {
            bdcTd.setZdzhh(project.getZdzhh());
            if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                bdcTd.setZdmj(djsjQszdDcb.getFzmj());
            } else {
                bdcTd.setZdmj(djsjQszdDcb.getScmj());
            }
            bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
            bdcTd.setYt(djsjQszdDcb.getTdyt());
            bdcTd.setQllx(djsjQszdDcb.getQsxz());
            bdcTd.setZl(djsjQszdDcb.getTdzl());
        }
        //zdd 地籍没有单位  默认就是平方米
        if (StringUtils.isNotBlank(qllx)) {
            HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(qllx);
            if (dwMap != null && StringUtils.isNotBlank(dwMap.get(qllx))) {
                bdcTd.setMjdw(dwMap.get(qllx));
            } else {
                bdcTd.setMjdw(Constants.DW_PFM);
            }
        } else {
            bdcTd.setMjdw(Constants.DW_PFM);
        }
        return bdcTd;
    }

    @Override
    @Transactional
    public void changeGySjydZt(BdcXm bdcXm,List<String> djhList) {
        if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CSDJ_DM)) {
            List<BdcFdcq> bdcFdcqList=bdcFdcqService.getBdcFdcqByProid(bdcXm.getProid());
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                BdcFdcq bdcFdcq=bdcFdcqList.get(0);
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null) {
                    String djh = StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19);
                    if (!djhList.contains(djh)) {
                        List<BdcJsydzjdsyq> bdcJsydzjdsyqList = bdcJsydzjdsyqService.getJsyByDjh(djh);
                        if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                            BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqList.get(0);
                            if (StringUtils.isNotBlank(bdcJsydzjdsyq.getQlid())) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                                bdcJsydzjdsyq.setQszt(Constants.QLLX_QSZT_HR);
                                if (bdcFdcq.getDjsj() != null) {
                                    if (StringUtils.isNotBlank(bdcJsydzjdsyq.getFj())) {
                                        bdcJsydzjdsyq.setFj(bdcJsydzjdsyq.getFj() + sdf.format(bdcFdcq.getDjsj()) + "办理房屋首次登记");
                                    } else {
                                        bdcJsydzjdsyq.setFj(sdf.format(bdcFdcq.getDjsj()) + "办理房屋首次登记");
                                    }
                                } else {
                                    if (StringUtils.isNotBlank(bdcJsydzjdsyq.getFj())) {
                                        bdcJsydzjdsyq.setFj(bdcJsydzjdsyq.getFj() + sdf.format(new Date()) + "办理房屋首次登记");
                                    } else {
                                        bdcJsydzjdsyq.setFj(sdf.format(new Date()) + "办理房屋首次登记");
                                    }
                                }
                                entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
                            }
                        }
                        djhList.add(djh);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void backGySjydZt(BdcXm bdcXm) {
        if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CSDJ_DM)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            if (qllxVo instanceof BdcFdcq) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null && StringUtils.isNotBlank(bdcBdcdy.getBdcdyh()) && bdcBdcdy.getBdcdyh().length() > 19) {
                    List<BdcJsydzjdsyq> bdcJsydzjdsyqList = bdcJsydzjdsyqService.getJsyByDjh(StringUtils.substring(bdcBdcdy.getBdcdyh(), 0, 19));
                    if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                        BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqList.get(0);
                        if (StringUtils.isNotBlank(bdcJsydzjdsyq.getQlid())) {
                            bdcJsydzjdsyq.setQszt(Constants.QLLX_QSZT_XS);
                            entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void changeBackQlFj(BdcXm bdcXm) {
        if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_CSDJ_DM)) {
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            if (qllxVo instanceof BdcFdcq) {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(bdcXm.getBdcdyid());
                if (bdcBdcdy != null) {
                    List<BdcJsydzjdsyq> bdcJsydzjdsyqList = bdcJsydzjdsyqService.getJsyByDjh(StringUtils.substring(bdcBdcdy.getBdcdyid(), 0, 19));
                    if (CollectionUtils.isNotEmpty(bdcJsydzjdsyqList)) {
                        BdcJsydzjdsyq bdcJsydzjdsyq = bdcJsydzjdsyqList.get(0);
                        if (StringUtils.isNotBlank(bdcJsydzjdsyq.getQlid())) {
                            bdcJsydzjdsyq.setQszt(Constants.QLLX_QSZT_XS);
                            bdcJsydzjdsyq.setFj("");
                            entityMapper.saveOrUpdate(bdcJsydzjdsyq, bdcJsydzjdsyq.getQlid());
                        }
                    }
                }
            }
        } else if (StringUtils.equals(bdcXm.getDjlx(), Constants.DJLX_DYDJ_DM)) {
            HashMap<String, String> map = new HashMap<String, String>();
            QllxVo qllxVo = qllxService.makeSureQllx(bdcXm);
            if (qllxVo instanceof BdcDyaq) {
                map.put("proid", bdcXm.getProid());
                List<BdcDyaq> dyaqList = bdcDyaqService.queryBdcDyaq(map);
                if (CollectionUtils.isNotEmpty(dyaqList)) {
                    BdcDyaq bdcDyaq = dyaqList.get(0);
                    entityMapper.saveOrUpdate(bdcDyaq, bdcDyaq.getQlid());
                }
            }
        }
    }

    @Override
    public BdcTd getBdcTdFromDjxx(final DjsjZdxx djsjZdxx, final List<DjsjQszdDcb> djsjQszdDcbList, final List<DjsjNydDcb> djsjNydDcbList, final Project project, String qllx) {
        BdcTd bdcTd = new BdcTd();
        if (StringUtils.isBlank(bdcTd.getTdid())) {
            bdcTd.setTdid(UUIDGenerator.generate18());
        }
        if (djsjZdxx == null && (djsjQszdDcbList == null || CollectionUtils.isEmpty(djsjQszdDcbList)) && (djsjNydDcbList == null || CollectionUtils.isEmpty(djsjNydDcbList))) {
            return bdcTd;
        }
        if (djsjZdxx != null) {
            bdcTd.setZdzhh(project.getZdzhh());
            bdcTd.setQlxz(djsjZdxx.getSyqlx());
            if (djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() != 0) {
                bdcTd.setZdmj(djsjZdxx.getFzmj());
            } else {
                bdcTd.setZdmj(djsjZdxx.getScmj());
            }
            bdcTd.setQlsdfs(djsjZdxx.getQlsdfs());
            bdcTd.setYt(djsjZdxx.getTdyt());
            bdcTd.setQllx(djsjZdxx.getQsxz());
            bdcTd.setRjl(djsjZdxx.getJzrjl());
            bdcTd.setJzmd(djsjZdxx.getJzmd());
            bdcTd.setJzxg(djsjZdxx.getJzxg());
            bdcTd.setJg(djsjZdxx.getQdjg());
            bdcTd.setDj(djsjZdxx.getTdjb());
            //zdd 地籍没有单位  默认就是平方米
            bdcTd.setMjdw(Constants.DW_PFM);
        } else if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
            DjsjQszdDcb djsjQszdDcb = null;
            djsjQszdDcb = djsjQszdDcbList.get(0);
            bdcTd.setZdzhh(project.getZdzhh());
            if (djsjQszdDcb != null) {
                if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                    bdcTd.setZdmj(djsjQszdDcb.getFzmj());
                } else {
                    bdcTd.setZdmj(djsjQszdDcb.getScmj());
                }
                bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
                bdcTd.setYt(djsjQszdDcb.getTdyt());
                bdcTd.setQllx(djsjQszdDcb.getQsxz());
                bdcTd.setZl(djsjQszdDcb.getTdzl());
            }
            //zdd 地籍没有单位  默认就是平方米
            if (StringUtils.isNotBlank(qllx)) {
                HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(qllx);
                if (dwMap != null && StringUtils.isNotBlank(dwMap.get(qllx))) {
                    bdcTd.setMjdw(dwMap.get(qllx));
                } else {
                    bdcTd.setMjdw(Constants.DW_PFM);
                }
            } else {
                bdcTd.setMjdw(Constants.DW_PFM);
            }

        } else if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
            DjsjNydDcb djsjQszdDcb = null;
            djsjQszdDcb = djsjNydDcbList.get(0);
            bdcTd.setZdzhh(project.getZdzhh());
            if (djsjQszdDcb != null) {
                if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                    bdcTd.setZdmj(djsjQszdDcb.getFzmj());
                } else {
                    bdcTd.setZdmj(djsjQszdDcb.getScmj());
                }
                bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
                bdcTd.setYt(djsjQszdDcb.getTdyt());
                bdcTd.setQllx(djsjQszdDcb.getQsxz());
                bdcTd.setZl(djsjQszdDcb.getTdzl());
            }
            //zdd 地籍没有单位  默认就是平方米
            if (StringUtils.isNotBlank(qllx)) {
                HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(qllx);
                if (dwMap != null && StringUtils.isNotBlank(dwMap.get(qllx))) {
                    bdcTd.setMjdw(dwMap.get(qllx));
                } else {
                    bdcTd.setMjdw(Constants.DW_PFM);
                }
            } else {
                bdcTd.setMjdw(Constants.DW_PFM);
            }
        }
        return bdcTd;
    }

    @Override
    public BdcTd getBdcTdFromDjsjZdxx(DjsjZdxx djsjZdxx, ProjectPar projectPar) {
        if (djsjZdxx == null || projectPar == null) {
            return null;
        }
        BdcTd bdcTd = new BdcTd();
        bdcTd.setTdid(UUIDGenerator.generate18());
        bdcTd.setZdzhh(projectPar.getDjh());
        bdcTd.setQlxz(djsjZdxx.getSyqlx());
        if (djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() != 0) {
            bdcTd.setZdmj(djsjZdxx.getFzmj());
        } else {
            bdcTd.setZdmj(djsjZdxx.getScmj());
        }
        bdcTd.setQlsdfs(djsjZdxx.getQlsdfs());
        bdcTd.setYt(djsjZdxx.getTdyt());
        bdcTd.setQllx(djsjZdxx.getQsxz());
        bdcTd.setRjl(djsjZdxx.getJzrjl());
        bdcTd.setJzmd(djsjZdxx.getJzmd());
        bdcTd.setJzxg(djsjZdxx.getJzxg());
        bdcTd.setJg(djsjZdxx.getQdjg());
        bdcTd.setDj(djsjZdxx.getTdjb());
        //zdd 地籍没有单位  默认就是平方米
        bdcTd.setMjdw(Constants.DW_PFM);
        return bdcTd;
    }

    @Override
    public BdcTd getBdcTdFromQszdDcb(List<DjsjQszdDcb> djsjQszdDcbList, ProjectPar projectPar) {
        if (CollectionUtils.isEmpty(djsjQszdDcbList) || projectPar == null) {
            return null;
        }
        BdcTd bdcTd = new BdcTd();
        bdcTd.setTdid(UUIDGenerator.generate18());
        bdcTd.setZdzhh(projectPar.getDjh());
        DjsjQszdDcb djsjQszdDcb = djsjQszdDcbList.get(0);
        if (djsjQszdDcb != null) {
            if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                bdcTd.setZdmj(djsjQszdDcb.getFzmj());
            } else {
                bdcTd.setZdmj(djsjQszdDcb.getScmj());
            }
            bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
            bdcTd.setYt(djsjQszdDcb.getTdyt());
            bdcTd.setQllx(djsjQszdDcb.getQsxz());
            bdcTd.setZl(djsjQszdDcb.getTdzl());
        }
        //zdd 地籍没有单位  默认就是平方米
        if (StringUtils.isNotBlank(projectPar.getQllx())) {
            HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(projectPar.getQllx());
            if (dwMap != null && StringUtils.isNotBlank(dwMap.get(projectPar.getQllx()))) {
                bdcTd.setMjdw(dwMap.get(projectPar.getQllx()));
            } else {
                bdcTd.setMjdw(Constants.DW_PFM);
            }
        } else {
            bdcTd.setMjdw(Constants.DW_PFM);
        }
        return bdcTd;
    }

    @Override
    public BdcTd getBdcTdFromNydDcb(List<DjsjNydDcb> djsjNydDcbList, ProjectPar projectPar) {
        if (CollectionUtils.isEmpty(djsjNydDcbList) || projectPar == null) {
            return null;
        }
        BdcTd bdcTd = new BdcTd();
        bdcTd.setTdid(UUIDGenerator.generate18());
        bdcTd.setZdzhh(projectPar.getDjh());
        DjsjNydDcb djsjQszdDcb = null;
        djsjQszdDcb = djsjNydDcbList.get(0);
        if (djsjQszdDcb != null) {
            if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                bdcTd.setZdmj(djsjQszdDcb.getFzmj());
            } else {
                bdcTd.setZdmj(djsjQszdDcb.getScmj());
            }
            bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
            bdcTd.setYt(djsjQszdDcb.getTdyt());
            bdcTd.setQllx(djsjQszdDcb.getQsxz());
            bdcTd.setZl(djsjQszdDcb.getTdzl());
        }
        //zdd 地籍没有单位  默认就是平方米
        if (StringUtils.isNotBlank(projectPar.getQllx())) {
            HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(projectPar.getQllx());
            if (dwMap != null && StringUtils.isNotBlank(dwMap.get(projectPar.getQllx()))) {
                bdcTd.setMjdw(dwMap.get(projectPar.getQllx()));
            } else {
                bdcTd.setMjdw(Constants.DW_PFM);
            }
        } else {
            bdcTd.setMjdw(Constants.DW_PFM);
        }
        return bdcTd;
    }

    private BdcTd getBdcTdFromDjxx(final DjsjZdxx djsjZdxx, final List<DjsjQszdDcb> djsjQszdDcbList, final List<DjsjNydDcb> djsjNydDcbList, final String zdzhh, String qllx) {
        BdcTd bdcTd = new BdcTd();
        if (StringUtils.isBlank(bdcTd.getTdid())) {
            bdcTd.setTdid(UUIDGenerator.generate18());
        }
        if (djsjZdxx == null && (djsjQszdDcbList == null || CollectionUtils.isEmpty(djsjQszdDcbList)) && (djsjNydDcbList == null || CollectionUtils.isEmpty(djsjNydDcbList))) {
            return bdcTd;
        }
        if (djsjZdxx != null) {
            bdcTd.setZdzhh(zdzhh);
            bdcTd.setQlxz(djsjZdxx.getSyqlx());
            if (djsjZdxx.getFzmj() != null && djsjZdxx.getFzmj() != 0) {
                bdcTd.setZdmj(djsjZdxx.getFzmj());
            } else {
                bdcTd.setZdmj(djsjZdxx.getScmj());
            }
            bdcTd.setQlsdfs(djsjZdxx.getQlsdfs());
            bdcTd.setYt(djsjZdxx.getTdyt());
            bdcTd.setQllx(djsjZdxx.getQsxz());
            bdcTd.setRjl(djsjZdxx.getJzrjl());
            bdcTd.setJzmd(djsjZdxx.getJzmd());
            bdcTd.setJzxg(djsjZdxx.getJzxg());
            bdcTd.setJg(djsjZdxx.getQdjg());
            bdcTd.setDj(djsjZdxx.getTdjb());
            //zdd 地籍没有单位  默认就是平方米
            bdcTd.setMjdw(Constants.DW_PFM);
        } else if (CollectionUtils.isNotEmpty(djsjQszdDcbList)) {
            DjsjQszdDcb djsjQszdDcb = null;
            djsjQszdDcb = djsjQszdDcbList.get(0);
            bdcTd.setZdzhh(zdzhh);
            if (djsjQszdDcb != null) {
                if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                    bdcTd.setZdmj(djsjQszdDcb.getFzmj());
                } else {
                    bdcTd.setZdmj(djsjQszdDcb.getScmj());
                }
                bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
                bdcTd.setYt(djsjQszdDcb.getTdyt());
                bdcTd.setQllx(djsjQszdDcb.getQsxz());
                bdcTd.setZl(djsjQszdDcb.getTdzl());
            }
            //zdd 地籍没有单位  默认就是平方米
            if (StringUtils.isNotBlank(qllx)) {
                HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(qllx);
                if (dwMap != null && StringUtils.isNotBlank(dwMap.get(qllx))) {
                    bdcTd.setMjdw(dwMap.get(qllx));
                } else {
                    bdcTd.setMjdw(Constants.DW_PFM);
                }
            } else {
                bdcTd.setMjdw(Constants.DW_PFM);
            }

        } else if (CollectionUtils.isNotEmpty(djsjNydDcbList)) {
            DjsjNydDcb djsjQszdDcb = null;
            djsjQszdDcb = djsjNydDcbList.get(0);
            bdcTd.setZdzhh(zdzhh);
            if (djsjQszdDcb != null) {
                if (djsjQszdDcb.getFzmj() != null && djsjQszdDcb.getFzmj() != 0) {
                    bdcTd.setZdmj(djsjQszdDcb.getFzmj());
                } else {
                    bdcTd.setZdmj(djsjQszdDcb.getScmj());
                }
                bdcTd.setQlsdfs(djsjQszdDcb.getQlsdfs());
                bdcTd.setYt(djsjQszdDcb.getTdyt());
                bdcTd.setQllx(djsjQszdDcb.getQsxz());
                bdcTd.setZl(djsjQszdDcb.getTdzl());
            }
            //zdd 地籍没有单位  默认就是平方米
            if (StringUtils.isNotBlank(qllx)) {
                HashMap<String, String> dwMap = ReadXmlProps.getDwByQllx(qllx);
                if (dwMap != null && StringUtils.isNotBlank(dwMap.get(qllx))) {
                    bdcTd.setMjdw(dwMap.get(qllx));
                } else {
                    bdcTd.setMjdw(Constants.DW_PFM);
                }
            } else {
                bdcTd.setMjdw(Constants.DW_PFM);
            }
        }
        return bdcTd;
    }

    @Override
    public void delBdcTdByZdzhh(final String zdzhh) {
        if (StringUtils.isNotBlank(zdzhh)) {
            Example example = new Example(BdcTd.class);
            example.createCriteria().andEqualTo("zdzhh", zdzhh);
            entityMapper.deleteByExample(example);
        }
    }

    @Override
    public String getZdLb(String proid) {
        String zdTzm = StringUtils.EMPTY;
        String zdLb = StringUtils.EMPTY;
        //通过proid获取不动产单元号
        String bdcdyh = bdcdyService.getBdcdyhByProid(proid);
        if (StringUtils.isNotBlank(bdcdyh)) {
            //截取不动产单元号的12-13位为宗地特征码
            zdTzm = bdcdyh.substring(12, 14);
            //截取不动产单元号的0-18位为宗地宗海号
        }
        //获取BDC_XT_ZDLB_CONFIG表中配置的宗地类别判定方式
        HashMap<String, String> resultMap = bdcTdMapper.getFsAndDefaultZdLbByZdTzm(zdTzm);
        if (resultMap == null) {
            resultMap = new HashMap<String, String>();
        }
        String fs = StringUtils.EMPTY;
        String defaultZdLb = StringUtils.EMPTY;
        if (!resultMap.isEmpty()) {
            //FS 宗地判定方式
            if (StringUtils.isNotBlank(resultMap.get("FS"))) {
                fs = resultMap.get("FS");
            }
            //ZDLB 宗地类别
            if (StringUtils.isNotBlank(resultMap.get("ZDLB"))) {
                defaultZdLb = resultMap.get("ZDLB");
            }
        } else {
            resultMap = bdcTdMapper.getFsAndDefaultZdLbByZdTzm(Constants.ZDTZM_DEFAULT);
            if (resultMap != null) {
                defaultZdLb = resultMap.get("ZDLB");
                if (StringUtils.isNotBlank(resultMap.get("FS"))) {
                    fs = resultMap.get("FS");
                }
            }
        }
        if (StringUtils.isBlank(defaultZdLb)) {
            defaultZdLb = Constants.ZDLB_DYZ;
        }
        if (StringUtils.isNotBlank(fs)) {
            //不动产单元方式,根据宗地上不动产单元数量判定是独有宗还是共有宗
            if (StringUtils.equals(StringUtils.upperCase(fs), Constants.ZDLB_PDFS_BDCDY)) {
                int bdcdyCount = bdcDjsjService.queryBdcdyCountByDjh(StringUtils.substring(bdcdyh, 0, 19));
                if (bdcdyCount == 1 || bdcdyCount == 0) {
                    zdLb = Constants.ZDLB_DYZ;
                } else {
                    zdLb = Constants.ZDLB_GYZ;
                }
                //权利人方式,根据proid获取权利人数量,通过权利人数量判定是独有宗还是共有宗
            } else if (StringUtils.equals(StringUtils.upperCase(fs), Constants.ZDLB_PDFS_QLR)) {
                List<BdcQlr> bdcQlrList = bdcQlrService.queryBdcQlrByProid(proid);
                if (CollectionUtils.isNotEmpty(bdcQlrList)) {
                    if (bdcQlrList.size() == 1) {
                        zdLb = Constants.ZDLB_DYZ;
                    } else {
                        zdLb = Constants.ZDLB_GYZ;
                    }
                }
            } else if (StringUtils.equals(StringUtils.upperCase(fs), "BDCDYFWLX")) {
                List<String> bdcfwlxList = djsjFwMapper.getBdcfwlxByBdcdyh(bdcdyh);
                if (CollectionUtils.isNotEmpty(bdcfwlxList)) {
                    if (StringUtils.equals(bdcfwlxList.get(0), "4")) {
                        zdLb = Constants.ZDLB_GYZ;
                    } else {
                        zdLb = Constants.ZDLB_DYZ;
                    }
                }
            }
        } else {
            zdLb = defaultZdLb;
        }
        return zdLb;
    }

    @Override
    public String changeMjByZdLb(final String zdLb, final String mj, final String bdclx) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(zdLb)) {
            if (StringUtils.equals(Constants.ZDLB_DYZ, zdLb)) {
                for (String string : mj.split("/")) {
                    if (string.indexOf("共有宗地") > -1) {
                        string = StringUtils.replace(string, "共有宗地", "宗地");
                    }
                    if (!(StringUtils.equals(bdclx, "FW")) || StringUtils.indexOf(string, "房屋") > -1) {
                        stringBuilder.append(string);
                        stringBuilder.append("/");
                    }
                }
            } else if (StringUtils.equals(Constants.ZDLB_GYZ, zdLb)) {
                for (String string : mj.split("/")) {
                    if (string.indexOf("宗地") > -1 && string.indexOf("共有宗地") < 0) {
                        string = StringUtils.replace(string, "宗地", "共有宗地");
                    }
                    //jyl南通需求
                    if (string.indexOf("建筑面积") > -1) {
                        string = StringUtils.replace(string, "建筑面积", "面积");
                    }
                    if (!(StringUtils.equals(bdclx, "FW")) || StringUtils.indexOf(string, "房屋") > -1) {
                        stringBuilder.append(string);
                        stringBuilder.append("/");
                    }
                }
            }
        }
        if (stringBuilder != null && stringBuilder.toString().length() > 0) {
            return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        } else {
            return "";
        }
    }

    @Override
    public String changeMjByNt(String mj, String bdclx, String proid) {
        if (StringUtils.equals(bdclx, Constants.BDCLX_TDFW)) {
            List<BdcFdcq> bdcFdcqList = bdcFdcqService.getBdcFdcqByProid(proid);
            BdcFdcqDz bdcFdcqDz = bdcFdcqDzService.getBdcFdcqDz(proid);
            BdcSpxx bdcSpxx = bdcSpxxService.queryBdcSpxxByProid(proid);
            String noMjByGhyt = AppConfig.getProperty("noMjByGhyt");
            if (CollectionUtils.isNotEmpty(bdcFdcqList)) {
                BdcFdcq bdcFdcq = bdcFdcqList.get(0);
                String[] noMjByGhytArr = null;
                if (StringUtils.isNoneBlank(noMjByGhyt)) {
                    noMjByGhytArr = noMjByGhyt.split(",");
                }
                if (noMjByGhytArr != null && CommonUtil.indexOfStrs(noMjByGhytArr, bdcSpxx.getYt())) {
                    //车库为空
                    mj = "";
                } else {
                    if (bdcFdcq.getDytdmj() == null || bdcFdcq.getDytdmj() == 0) {
                        StringBuilder stringBuilderTs = new StringBuilder();
                        for (String string : mj.split("/")) {
                            if (string.indexOf("宗地") > -1) {
                                string = "宗地面积-㎡";
                            }
                            if (!(StringUtils.equals(bdclx, "FW")) || StringUtils.indexOf(string, "房屋") > -1) {
                                stringBuilderTs.append(string);
                                stringBuilderTs.append("/");
                            }
                        }
                        mj = stringBuilderTs.toString().substring(0, stringBuilderTs.length() - 1);
                        /**
                         * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                         * @description 宗地宗海面积为0时，拼接 宗地面积-㎡/
                         */
                        if (mj.indexOf("宗地") == -1 && bdcSpxx != null && (bdcSpxx.getZdzhmj() == null || bdcSpxx.getZdzhmj() == 0)) {
                            mj = "宗地面积-㎡/" + mj;
                        }
                    }
                }
            } else if (bdcFdcqDz != null && (bdcFdcqDz.getDytdmj() == null || bdcFdcqDz.getDytdmj() == 0)) {
                StringBuilder stringBuilderTs = new StringBuilder();
                for (String string : mj.split("/")) {
                    if (string.indexOf("宗地") > -1) {
                        string = "宗地面积-㎡";
                    }
                    if (!(StringUtils.equals(bdclx, "FW")) || StringUtils.indexOf(string, "房屋") > -1) {
                        stringBuilderTs.append(string);
                        stringBuilderTs.append("/");
                    }
                }
                mj = stringBuilderTs.toString().substring(0, stringBuilderTs.length() - 1);
                /**
                 * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
                 * @description 宗地宗海面积为0时，拼接 宗地面积-㎡/
                 */
                if (mj.indexOf("宗地") == -1 && bdcSpxx != null && (bdcSpxx.getZdzhmj() == null || bdcSpxx.getZdzhmj() == 0)) {
                    mj = "宗地面积-㎡/" + mj;
                }
            }
        }
        return mj;
    }

    /**
     * @param
     * @return
     * @author wangtao
     * @description
     */
    @Override
    public List<Map> getZdty() {
        return bdcTdMapper.getZdyt();
    }


    public List<String> getZdzhhList(Xmxx xmxx) {
        Project project = null;
        List<String> zdzhhList = new ArrayList<String>();
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
            if (StringUtils.isNotBlank(project.getDcbIndex())) {
                String zdzhh = getZdzhhByDcbIndex(project.getDcbIndex());
                if (StringUtils.isNotBlank(zdzhh)) {
                    zdzhhList.add(zdzhh);
                }
            }
            if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
                for (String dcbIndex : project.getDcbIndexs()) {
                    String zdzhh = getZdzhhByDcbIndex(dcbIndex);
                    if (StringUtils.isNotBlank(zdzhh) && !zdzhhList.contains(zdzhh)) {
                        zdzhhList.add(zdzhh);
                    }
                }
            }
            if (StringUtils.isNotBlank(project.getBdcdyh()) && StringUtils.isNotBlank(project.getDjId())
                    && project.getBdcdyh().length() > 19) {
                String zdzhh = project.getBdcdyh().substring(0, 19);
                if (!zdzhhList.contains(zdzhh)) {
                    zdzhhList.add(zdzhh);
                }
            }
            if (CollectionUtils.isNotEmpty(project.getBdcdyhs()) && CollectionUtils.isNotEmpty(project.getDjIds())
                    && project.getBdcdyhs().size() == project.getDjIds().size()) {
                List<String> bdcdyhs = project.getBdcdyhs();
                if (StringUtils.isNotBlank(bdcdyhs.get(0)) && bdcdyhs.get(0).contains("\\$")) {
                    String[] bdcdyhArray = bdcdyhs.get(0).split("\\$");
                    bdcdyhs = Arrays.asList(bdcdyhArray);
                }
                for (String bdcdyh : bdcdyhs) {
                    if (bdcdyh.length() > 19) {
                        String zdzhh = bdcdyh.substring(0, 19);
                        if (!zdzhhList.contains(zdzhh)) {
                            zdzhhList.add(zdzhh);
                        }
                    }
                }
            }
        }
        return zdzhhList;
    }

    private String getZdzhhByDcbIndex(String dcbIndex) {
        HashMap ljzMap = new HashMap();
        ljzMap.put("fw_dcb_index", dcbIndex);
        List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(ljzMap);
        DjsjFwLjz djsjFwLjz = CollectionUtils.isEmpty(djsjFwLjzList) ? null : djsjFwLjzList.get(0);
        if (null != djsjFwLjz && StringUtils.isNotBlank(djsjFwLjz.getLszd())) {
            return djsjFwLjz.getLszd();
        }
        return null;
    }

    @Override
    public void initBdcTdAhead(final Xmxx xmxx) {
//        Project project = null;
//
//        List<BdcTd> bdcTdList = new ArrayList<BdcTd>();
//        if (xmxx instanceof Project) {
//            project = (Project) xmxx;
//        }
//        if (null != project) {
//            List<String> djsjIds = project.getDjIds();
//            List<String> bdcdyhIds = project.getBdcdyhs();
//            // 根据project中的值进行区分，混选则都有值
//            // 户室生成bdcTd逻辑
//            if (StringUtils.isNotBlank(project.getDcbIndex())) {
//                getBdcTdListByDcbIndex(project.getDcbIndex(), bdcTdList, project);
//            }
//            if (CollectionUtils.isNotEmpty(project.getDcbIndexs())) {
//                for (String dcbIndex : project.getDcbIndexs()) {
//                    getBdcTdListByDcbIndex(dcbIndex, bdcTdList, project);
//                }
//            }
//            // 独幢生成bdcTd逻辑
//            if (CollectionUtils.isNotEmpty(djsjIds) && CollectionUtils.isNotEmpty(bdcdyhIds)) {
//                if (bdcdyhIds.size() == 1 && StringUtils.indexOf(bdcdyhIds.get(0), "\\$") >= 0) {
//                    String[] bdcdyhIdArray = StringUtils.split(bdcdyhIds.get(0), "\\$");
//                    bdcdyhIds = Arrays.asList(bdcdyhIdArray);
//                }
//                for (String bdcdyh : bdcdyhIds) {
//                    if (StringUtils.isNotBlank(bdcdyh) && StringUtils.length(bdcdyh) > 19) {
//                        getBdcTdListByDjh(StringUtils.substring(bdcdyh, 0, 19), bdcTdList, project);
//                    }
//                }
//            }
//            saveBdcTdList(bdcTdList);
//        }
    }


    @Override
    public void initBdcTdAhead(final Xmxx xmxx, final List<String> zdzhhList) {
        Project project = null;
        List<BdcTd> bdcTdList = new ArrayList<BdcTd>();
        if (xmxx instanceof Project) {
            project = (Project) xmxx;
        }
        if (null != project && CollectionUtils.isNotEmpty(zdzhhList)) {
            for (String zdzhh : zdzhhList) {
                getBdcTdListByDjh(zdzhh, bdcTdList, zdzhh);
            }
            saveBdcTdList(bdcTdList);
        }
    }

    @Override
    public void saveBdcTdList(List<BdcTd> bdcTdList) {
        // 根据宗地宗海号去重
        if (CollectionUtils.isNotEmpty(bdcTdList)) {
            List<String> zdzhhList = new ArrayList<String>();
            List<BdcTd> targetSaveList = new ArrayList<BdcTd>();
            for (BdcTd bdcTdTemp : bdcTdList) {
                // 防止主键为空
                if (StringUtils.isBlank(bdcTdTemp.getTdid())) {
                    bdcTdTemp.setTdid(UUIDGenerator.generate18());
                }
                if (StringUtils.isNotBlank(bdcTdTemp.getZdzhh()) && !zdzhhList.contains(bdcTdTemp.getZdzhh())) {
                    zdzhhList.add(bdcTdTemp.getZdzhh());
                    targetSaveList.add(bdcTdTemp);
                }
            }
            entityMapper.batchSaveSelective(targetSaveList);
        }
    }

    /**
     * @param bdcTdList
     * @param dcbIndex
     * @param project
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据逻辑幢获取宗地宗海号（地籍号）获取bdctd
     */
    private void getBdcTdListByDcbIndex(String dcbIndex, List<BdcTd> bdcTdList, Project project) {
//        HashMap ljzMap = new HashMap();
//        ljzMap.put("fw_dcb_index", dcbIndex);
//        List<DjsjFwLjz> djsjFwLjzList = djsjFwService.getDjsjFwLjz(ljzMap);
//        DjsjFwLjz djsjFwLjz = CollectionUtils.isEmpty(djsjFwLjzList) ? null : djsjFwLjzList.get(0);
//        if (null != djsjFwLjz && StringUtils.isNotBlank(djsjFwLjz.getLszd())) {
//            getBdcTdListByDjh(djsjFwLjz.getLszd(), bdcTdList, project);
//        }
    }

    /**
     * @param djh
     * @param bdcTdList
     * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
     * @description 根据地籍号zd获取bdctd所需要的数据
     */
    private void getBdcTdListByDjh(String djh, List<BdcTd> bdcTdList, String zdzhh) {
        if (StringUtils.isNotBlank(djh)) {
            BdcTd bdcTd = selectBdcTd(djh);
            if (null == bdcTd) {
                List<DjsjZdxx> djsjZdxxList = bdcDjsjService.getDjsjZdxxForDjh(djh);
                if (CollectionUtils.isNotEmpty(djsjZdxxList)) {
                    DjsjZdxx djsjZdxx = djsjZdxxList.get(0);
                    if (djsjZdxx != null) {
                        bdcTd = getBdcTdFromDjxx(djsjZdxx, null, null, zdzhh, null);
                    }
                }
                if (null != bdcTd) {
                    bdcTdList.add(bdcTd);
                }
            }
        }
    }

}
