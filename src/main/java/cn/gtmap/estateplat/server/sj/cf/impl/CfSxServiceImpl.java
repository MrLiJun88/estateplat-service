package cn.gtmap.estateplat.server.sj.cf.impl;

import cn.gtmap.estateplat.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.estateplat.model.server.core.*;
import cn.gtmap.estateplat.server.core.service.BdcCfService;
import cn.gtmap.estateplat.server.core.service.BdcXmService;
import cn.gtmap.estateplat.server.core.service.BdcdyService;
import cn.gtmap.estateplat.server.core.service.SyncRyzdService;
import cn.gtmap.estateplat.server.service.CreatProjectService;
import cn.gtmap.estateplat.server.sj.cf.CfSxService;
import cn.gtmap.estateplat.server.sj.model.ProjectPar;
import cn.gtmap.estateplat.server.utils.Constants;
import cn.gtmap.estateplat.server.utils.ParamsConstants;
import cn.gtmap.estateplat.server.utils.PlatformUtil;
import cn.gtmap.estateplat.utils.CalendarUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2020-05-06
 * @description 查封失效服务
 */
@Service
public class CfSxServiceImpl implements CfSxService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private BdcCfService bdcCfService;
    @Autowired
    private BdcXmService bdcXmService;
    @Autowired
    private BdcdyService bdcdyService;
    @Autowired
    private SyncRyzdService syncRyzdService;
    @Autowired
    @Resource(name = "creatProjectDefaultService")
    private CreatProjectService creatProjectService;

    /**
     * @param proid
     * @param sxyy
     * @param ip
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 查封失效
     */
    @Override
    public Map cfsx(String proid, String sxyy, String ip) {
        Map map = Maps.newHashMap();
        BdcCf bdcCf = bdcCfService.selectCfByProid(proid);
        if (bdcCf != null && !(bdcCf.getQszt() == 2)) {
            List<InsertVo> insertVoList = Lists.newArrayList();
            String userid = PlatformUtil.getCurrentUserId();
            BdcXm yBdcXm = bdcXmService.getBdcXmByProid(proid);
            BdcXm bdcXm = initSxCfXm(userid, yBdcXm);
            BdcXmRel bdcXmRel = initSxCfXmRel(proid, bdcXm);
            bdcCf = initSxCf(bdcCf, userid, sxyy, bdcXm);
            insertVoList.add(bdcXm);
            insertVoList.add(bdcCf);
            insertVoList.add(bdcXmRel);
            creatProjectService.insertProjectData(insertVoList);
            saveLog(bdcCf, userid, ip);
            if (bdcXm != null) {
                syncRyzdService.synchronizationField(bdcXm.getWiid());
            }
            if (yBdcXm != null) {
                syncRyzdService.synchronizationField(yBdcXm.getWiid());
            }
            map.put(ParamsConstants.MSG_LOWERCASE, ParamsConstants.SUCCESS_LOWERCASE);
        } else {
            map.put(ParamsConstants.MSG_LOWERCASE, "未找到该查封");
        }
        return map;
    }

    private BdcXm initSxCfXm(String userid, BdcXm yBdcXm) {
        BdcXm bdcXm = intiXmFromYxm(userid, yBdcXm);
        if (bdcXm != null) {
            if (StringUtils.isBlank(bdcXm.getBh())) {
                bdcXm.setBh(bdcXmService.creatXmbh(bdcXm));
            }
        }
        return bdcXm;
    }

    private BdcXmRel initSxCfXmRel(String proid, BdcXm bdcXm) {
        BdcXmRel bdcXmRel = new BdcXmRel();
        bdcXmRel.setRelid(UUIDGenerator.generate18());
        if (bdcXm != null) {
            bdcXmRel.setProid(bdcXm.getProid());
        }
        bdcXmRel.setYproid(proid);
        return bdcXmRel;
    }

    private BdcXm intiXmFromYxm(String userid, BdcXm yBdcXm) {
        ProjectPar projectPar = new ProjectPar();
        projectPar.setProid(UUIDGenerator.generate18());
        if (yBdcXm != null) {
            projectPar.setGdslbh(yBdcXm.getBh());
            projectPar.setQllx(yBdcXm.getQllx());
            projectPar.setYxmid(yBdcXm.getProid());
            projectPar.setBdcdyid(yBdcXm.getBdcdyid());
            if (StringUtils.isNotBlank(yBdcXm.getBdcdyh())) {
                projectPar.setBdcdyh(yBdcXm.getBdcdyh());
            } else {
                BdcBdcdy bdcBdcdy = bdcdyService.queryBdcdyById(yBdcXm.getBdcdyid());
                if (bdcBdcdy != null) {
                    projectPar.setBdcdyh(bdcBdcdy.getBdcdyh());
                }
            }
        }
        projectPar.setUserId(userid);
        BdcXm bdcXm = bdcXmService.initBdcXmFromProjectPar(projectPar);
        bdcXm.setXmly(Constants.XMLY_BDC);
        bdcXm.setDjlx(Constants.DJLX_CFDJ_DM);
        bdcXm.setSqlx(Constants.SQLX_JF);
        bdcXm.setXmzt(Constants.XMZT_BJ);
        bdcXm.setWiid(projectPar.getProid());
        return bdcXm;
    }

    private BdcCf initSxCf(BdcCf bdcCf, String userid, String sxyy, BdcXm bdcXm) {
        if (bdcCf != null) {
            if (StringUtils.isBlank(userid)) {
                userid = PlatformUtil.getCurrentUserId();
            }
            StringBuilder fj = new StringBuilder();
            if (StringUtils.isNotBlank(bdcCf.getFj())) {
                fj.append(bdcCf.getFj());
            }
            fj.append(CalendarUtil.formatDateToString(new Date())).append(sxyy);
            bdcCf.setFj(fj.toString());
            bdcCf.setQszt(Constants.QLLX_QSZT_HR);
            bdcCf.setIssx(Constants.SXZT_ISSX);
            bdcCf.setJfyy(sxyy);
            if (bdcXm != null && StringUtils.isNotBlank(bdcXm.getBh())) {
                bdcCf.setJfywh(bdcXm.getBh());
            }
            bdcCf.setJfdbr(PlatformUtil.getCurrentUserName(userid));
        }
        return bdcCf;
    }

    /**
     * @param bdcCf
     * @param userid
     * @param ip
     * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
     * @description 保存日志
     */
    @Override
    public void saveLog(BdcCf bdcCf, String userid, String ip) {
        if (bdcCf != null && StringUtils.isNotBlank(bdcCf.getProid())) {
            BdcXtLog bdcXtLog = new BdcXtLog();
            bdcXtLog.setLogid(UUIDGenerator.generate18());
            bdcXtLog.setController("查询调用登记接口，进行解封");
            if (StringUtils.isNotBlank(userid)) {
                bdcXtLog.setUserid(userid);
                bdcXtLog.setUsername(PlatformUtil.getCurrentUserName(userid));
            }
            bdcXtLog.setIp(ip);
            bdcXtLog.setCzrq(new Date());
            bdcXtLog.setWiid(bdcCf.getProid());
            bdcXtLog.setParmjson(JSON.toJSONString(bdcCf));
            entityMapper.saveOrUpdate(bdcXtLog, bdcXtLog.getLogid());
        }
    }
}
