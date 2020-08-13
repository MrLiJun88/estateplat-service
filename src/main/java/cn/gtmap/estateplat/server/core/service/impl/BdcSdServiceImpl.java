package cn.gtmap.estateplat.server.core.service.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.core.support.mybatis.mapper.Example;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcSdService;
import cn.gtmap.estateplat.server.core.service.BdcZsService;
import cn.gtmap.estateplat.server.core.service.GdFwService;
import cn.gtmap.estateplat.server.core.service.GdTdService;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xinghuajian@gtmap.cn">xinghuajian</a>
 * @version 1.0, 2018-08-07
 * @description
 */
@Service
public class BdcSdServiceImpl implements BdcSdService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcZsService bdcZsService;
    @Autowired
    private GdFwService gdFwService;
    @Autowired
    private GdTdService gdTdService;

    private static final String PARAMS_PROID = "proid";
    private static final String PARAMS_SDLCGZLID = "sdlcgzlid";
    private static final String PARAMS_JSLCGZLID = "jslcgzlid";

    @Override
    public void handleSdxxThroughWorkflow(Project project, String sqlxdm) {
        if (null != project) {
            String czrName = "";
            if (StringUtils.isNotBlank(project.getUserId())) {
                PfUserVo pfUserVo = PlatformUtil.getCurrentUserInfo();
                if (null != pfUserVo) {
                    czrName = StringUtils.isNotBlank(pfUserVo.getUserName()) ? pfUserVo.getUserName() : "";
                }
            }
            // 根据不同业务数据处理不同来源的锁定信息
            if (StringUtils.isNotBlank(project.getYxmid())) {
                handleBdcSdxx(project, czrName, sqlxdm);
            } else if (StringUtils.isNotBlank(project.getGdproid())) {
                handleGdSdxx(project, czrName, sqlxdm);
            }
        }
    }

    @Override
    public List<BdcBdcZsSd> queryBdcZsSdByMap(Map<String, Object> queryMap) {
        String proid = "";
        String cqzh = "";
        String xzzt = "";
        String sdlcgzlid = "";
        String jslcgzlid = "";
        List<BdcBdcZsSd> bdcBdcZsSdList = null;
        if (null != queryMap) {
            proid = CommonUtil.formatEmptyValue(queryMap.get(PARAMS_PROID));
            cqzh = CommonUtil.formatEmptyValue(queryMap.get("cqzh"));
            xzzt = CommonUtil.formatEmptyValue(queryMap.get("xzzt"));
            sdlcgzlid = CommonUtil.formatEmptyValue(queryMap.get(PARAMS_SDLCGZLID));
            jslcgzlid = CommonUtil.formatEmptyValue(queryMap.get(PARAMS_JSLCGZLID));
        }
        Example example = new Example(BdcBdcZsSd.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(proid)) {
            criteria.andEqualTo(PARAMS_PROID, proid);
        }
        if (StringUtils.isNotBlank(cqzh)) {
            criteria.andEqualTo("cqzh", cqzh);
        }
        if (StringUtils.isNotBlank(xzzt)) {
            criteria.andEqualTo("xzzt", xzzt);
        }
        if (StringUtils.isNotBlank(sdlcgzlid)) {
            criteria.andEqualTo(PARAMS_SDLCGZLID, sdlcgzlid);
        }
        if (StringUtils.isNotBlank(jslcgzlid)) {
            criteria.andEqualTo(PARAMS_JSLCGZLID, jslcgzlid);
        }
        bdcBdcZsSdList = entityMapper.selectByExample(example);
        return bdcBdcZsSdList;
    }

    @Override
    public List<GdBdcSd> queryGdBdcSdByMap(Map<String, Object> queryMap) {
        String gdProid = "";
        String wiid = "";
        String jslcgzlid = "";
        List<GdBdcSd> gdBdcSdList = null;
        Example example = new Example(GdBdcSd.class);
        Example.Criteria criteria = example.createCriteria();
        if (null != queryMap) {
            gdProid = CommonUtil.formatEmptyValue(queryMap.get(PARAMS_PROID));
            wiid = CommonUtil.formatEmptyValue(queryMap.get("wiid"));
            jslcgzlid = CommonUtil.formatEmptyValue(queryMap.get(PARAMS_JSLCGZLID));
            String xzzt = CommonUtil.formatEmptyValue(queryMap.get("xzzt"));
            if (StringUtils.isNotBlank(xzzt)) {
                criteria.andEqualTo("xzzt", xzzt);
            }

        }
        if (StringUtils.isNotBlank(gdProid)) {
            criteria.andEqualTo(PARAMS_PROID, gdProid);
        }
        if (StringUtils.isNotBlank(wiid)) {
            criteria.andEqualTo("wiid", wiid);
        }
        if (StringUtils.isNotBlank(jslcgzlid)) {
            criteria.andEqualTo(PARAMS_JSLCGZLID, jslcgzlid);
        }
        gdBdcSdList = entityMapper.selectByExample(example);
        return gdBdcSdList;
    }

    @Override
    public void changeSdZt(BdcXm bdcXm) {
        if (null != bdcXm && StringUtils.isNotBlank(bdcXm.getWiid()) && StringUtils.isNotBlank(bdcXm.getSqlx())) {
            HashMap<String, Object> queryMap = Maps.newHashMap();
            if (StringUtils.equals(Constants.SQLX_BDCDYDJ_DM, bdcXm.getSqlx())) {
                queryMap.put(PARAMS_SDLCGZLID, bdcXm.getWiid());
            } else {
                queryMap.put(PARAMS_JSLCGZLID, bdcXm.getWiid());
            }
            List<BdcBdcZsSd> bdcBdcZsSdList = queryBdcZsSdByMap(queryMap);
            // 锁定时间在改变锁定状态时赋值
            if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                for (BdcBdcZsSd bdcBdcZsSd : bdcBdcZsSdList) {
                    if (bdcBdcZsSd != null) {
                        if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(Constants.SQLX_BDCDYDJ_DM, bdcXm.getSqlx())) {
                            bdcBdcZsSd.setXzzt(Constants.ZS_SDZT_YSD);
                            bdcBdcZsSd.setSdsj(new Date());
                        } else {
                            bdcBdcZsSd.setJssj(new Date());
                            bdcBdcZsSd.setXzzt(Constants.ZS_SDZT_WSD);
                        }
                        entityMapper.saveOrUpdate(bdcBdcZsSd, bdcBdcZsSd.getSdid());
                    }
                }
            } else {
                queryMap.clear();
                if (StringUtils.equals(Constants.SQLX_BDCDYDJ_DM, bdcXm.getSqlx())) {
                    queryMap.put("wiid", bdcXm.getWiid());
                } else {
                    queryMap.put(PARAMS_JSLCGZLID, bdcXm.getWiid());
                }
                List<GdBdcSd> gdBdcSdList = queryGdBdcSdByMap(queryMap);
                if (CollectionUtils.isNotEmpty(gdBdcSdList)) {
                    for (GdBdcSd gdBdcSd : gdBdcSdList) {
                        if (gdBdcSd != null) {
                            if (StringUtils.isNotBlank(bdcXm.getSqlx()) && StringUtils.equals(Constants.SQLX_BDCDYDJ_DM, bdcXm.getSqlx())) {
                                gdBdcSd.setSdsj(new Date());
                                gdBdcSd.setXzzt(Constants.ZS_SDZT_YSD);
                            } else {
                                gdBdcSd.setJssj(new Date());
                                gdBdcSd.setXzzt(Constants.ZS_SDZT_WSD);
                            }
                            entityMapper.saveOrUpdate(gdBdcSd, gdBdcSd.getSdid());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handleBdcSdxx(Project project, String czrName, String sqlxdm) {
        BdcBdcZsSd bdcBdcZsSd = new BdcBdcZsSd();
        if (StringUtils.isNotBlank(sqlxdm)) {
            if (StringUtils.equals(Constants.SQLX_BDCDYJD_DM, sqlxdm)) {
                HashMap<String, Object> queryMap = Maps.newHashMap();
                queryMap.put(PARAMS_PROID, project.getYxmid());
                queryMap.put("xzzt", Constants.XZZT_SD);
                if (StringUtils.isNotBlank(project.getYbdcqzh())) {
                    queryMap.put("cqzh", project.getYbdcqzh());
                }
                List<BdcBdcZsSd> bdcBdcZsSdList = queryBdcZsSdByMap(queryMap);
                if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                    bdcBdcZsSd = bdcBdcZsSdList.get(0);
                    bdcBdcZsSd.setJsr(czrName);
                    bdcBdcZsSd.setJslcgzlid(project.getWiid());
                    entityMapper.saveOrUpdate(bdcBdcZsSd, bdcBdcZsSd.getSdid());
                }
            } else {
                bdcBdcZsSd.setSdid(UUIDGenerator.generate18());
                bdcBdcZsSd.setBdclx(project.getBdclx());
                bdcBdcZsSd.setProid(project.getYxmid());
                BdcZs bdcZs = new BdcZs();
                if (StringUtils.isNotBlank(project.getYbdcqzh())) {
                    bdcZs = bdcZsService.queryBdcZsByBdcqzh(project.getYbdcqzh());
                } else {
                    List<BdcZs> bdcZslist = bdcZsService.queryBdcZsByProid(project.getYxmid());
                    if (CollectionUtils.isNotEmpty(bdcZslist)) {
                        bdcZs = bdcZslist.get(0);
                    }
                }
                if (bdcZs != null && StringUtils.isNotBlank(bdcZs.getZsid())) {
                    bdcBdcZsSd.setZsid(bdcZs.getZsid());
                    bdcBdcZsSd.setCqzh(bdcZs.getBdcqzh());
                }
                bdcBdcZsSd.setSdr(czrName);
                bdcBdcZsSd.setXzzt(Constants.ZS_SDZT_WSD);
                bdcBdcZsSd.setSdlcgzlid(project.getWiid());
                entityMapper.saveOrUpdate(bdcBdcZsSd, bdcBdcZsSd.getSdid());
            }

        }

    }

    @Override
    public void handleGdSdxx(Project project, String czrName, String sqlxdm) {
        GdBdcSd gdbdcSd = null;
        if (StringUtils.isNotBlank(sqlxdm)) {
            if (StringUtils.equals(Constants.SQLX_BDCDYJD_DM, sqlxdm)) {
                HashMap<String, Object> queryMap = Maps.newHashMap();
                queryMap.put(PARAMS_PROID, project.getGdproid());
                queryMap.put("xzzt", Constants.XZZT_SD);
                List<GdBdcSd> gdBdcSdList = queryGdBdcSdByMap(queryMap);
                if (CollectionUtils.isNotEmpty(gdBdcSdList)) {
                    gdbdcSd = gdBdcSdList.get(0);
                    gdbdcSd.setJslcgzlid(project.getWiid());
                    gdbdcSd.setJsr(czrName);
                    entityMapper.saveOrUpdate(gdbdcSd, gdbdcSd.getSdid());
                }
            } else {
                gdbdcSd = new GdBdcSd();
                gdbdcSd.setSdid(UUIDGenerator.generate18());
                gdbdcSd.setBdclx(project.getBdclx());
                gdbdcSd.setProid(project.getGdproid());
                gdbdcSd.setQlid(project.getYqlid());
                GdFwsyq gdFwsyq = gdFwService.getGdFwsyqByQlid(project.getYqlid());
                if (gdFwsyq != null && StringUtils.isNotBlank(gdFwsyq.getFczh())) {
                    gdbdcSd.setCqzh(gdFwsyq.getFczh());
                } else {
                    GdTdsyq gdTdsyq = gdTdService.getGdTdsyqByQlid(project.getYqlid());
                    if (gdTdsyq != null && StringUtils.isNotBlank(gdTdsyq.getTdzh())) {
                        gdbdcSd.setCqzh(gdTdsyq.getTdzh());
                    }
                }
                gdbdcSd.setSdr(czrName);
                gdbdcSd.setXzzt(Constants.ZS_SDZT_WSD);
                gdbdcSd.setWiid(project.getWiid());
                entityMapper.saveOrUpdate(gdbdcSd, gdbdcSd.getSdid());

            }

        }
    }

    @Override
    public void deleteSdxx(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            Example bdcExample = new Example(BdcBdcZsSd.class);
            bdcExample.createCriteria().andEqualTo(PARAMS_SDLCGZLID, wiid);
            entityMapper.deleteByExample(bdcExample);

            Example gdExample = new Example(GdBdcSd.class);
            gdExample.createCriteria().andEqualTo("wiid", wiid);
            entityMapper.deleteByExample(gdExample);
        }
    }

    @Override
    public void backSdStatus(String wiid) {
        if (StringUtils.isNotBlank(wiid)) {
            HashMap<String, Object> queryMap = Maps.newHashMap();
            queryMap.put(PARAMS_JSLCGZLID, wiid);
            List<BdcBdcZsSd> bdcBdcZsSdList = queryBdcZsSdByMap(queryMap);
            List<GdBdcSd> gdBdcSdList = queryGdBdcSdByMap(queryMap);
            if (CollectionUtils.isNotEmpty(bdcBdcZsSdList)) {
                for (BdcBdcZsSd bdcBdcZsSd : bdcBdcZsSdList) {
                    if (bdcBdcZsSd != null) {
                        bdcBdcZsSd.setJssj(null);
                        bdcBdcZsSd.setXzzt(Constants.ZS_SDZT_YSD);
                        bdcBdcZsSd.setJsr("");
                        bdcBdcZsSd.setJsyy("");
                        bdcBdcZsSd.setJslcgzlid("");
                    }
                }
                entityMapper.batchSaveSelective(bdcBdcZsSdList);
            }
            if (CollectionUtils.isNotEmpty(gdBdcSdList)) {
                for (GdBdcSd gdBdcSd : gdBdcSdList) {
                    if (gdBdcSd != null) {
                        gdBdcSd.setJssj(null);
                        gdBdcSd.setJsr("");
                        gdBdcSd.setJsyy("");
                        gdBdcSd.setXzzt(Constants.ZS_SDZT_YSD);
                        gdBdcSd.setJslcgzlid("");
                    }
                }
                entityMapper.batchSaveSelective(gdBdcSdList);
            }
        }
    }

}
